package com.udacity.adcs.app.goodintents;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.udacity.adcs.app.goodintents.ui.base.BaseActivity;
import com.udacity.adcs.app.goodintents.utils.LogUtils;

public class ParseDeepLinkActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
	
	private Activity mActivity;
    private GoogleApiClient mGoogleApiClient;

    private Intent mCachedInvitationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

        // Note: for simplicity, this sample uses the 'enableAutoManage' feature of
        // GoogleApiClient.  This sample does not handle all possible error cases that
        // can arise when using enableAutoManage, so consult the documentation before
        // using enableAutoManage in a more complicated application
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .addApi(AppInvite.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If app is already installed app and launched with deep link that matches
        // DeepLinkActivity filter, then the referral info will be in the intent
        Intent intent = getIntent();
        processReferralIntent(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {
        LogUtils.LOGE("***> deep link", "googleApiClient:onConnected");

        // We got a referral invitation ID before the GoogleApiClient was connected,
        // so send it now
        if (mCachedInvitationIntent != null) {
            updateInvitationStatus(mCachedInvitationIntent);
            mCachedInvitationIntent = null;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    /**
     * Get the intent for an activity corresponding to the deep link ID.
     *
     * @param intent
     *
     * @return The intent corresponding to the deep link ID.
     */
    private void processReferralIntent(Intent intent) {
        if (!AppInviteReferral.hasReferral(intent)) {
            LogUtils.LOGE("***> deep link", "Error: DeepLinkActivity Intent does not contain App Invite");
            return;
        }

        // Extract referral information from the intent
        // TODO: Consider saving the AppInvite ids for notification/reporting purposes
        String invitationId = AppInviteReferral.getInvitationId(intent);
        String deepLink = AppInviteReferral.getDeepLink(intent);

        if (mGoogleApiClient.isConnected()) {
            // Notify the API of the install success and invitation conversion
            updateInvitationStatus(intent);
        } else {
            // Cache the invitation ID so that we can call the AppInvite API after the GoogleAPIClient connects
            LogUtils.LOGE("***> deep link", "Warning: GoogleAPIClient not connected, can't update invitation.");
            mCachedInvitationIntent = intent;
        }

        LogUtils.LOGE("***> deep link", "Found Referral: " + invitationId + ":" + deepLink);

        Uri uri = Uri.parse(deepLink);
//        String tripGuid = uri.getQueryParameter(TripToPersonColumns.TRIP_GUID);
//        String tripToPersonGuid = uri.getQueryParameter(TripToPersonColumns.TRIP_TO_PERSON_GUID);

//        if (!TextUtils.isEmpty(tripGuid) && !TextUtils.isEmpty(tripToPersonGuid)) {
//            Intent joinIntent = IntentUtils.newIntent(mActivity, TripJoinActivity.class);
//            joinIntent.putExtra(Constants.Extra.TRIP_GUID, tripGuid);
//            joinIntent.putExtra(Constants.Extra.TRIP_TO_PERSON_GUID, tripToPersonGuid);
//
//            mActivity.startActivity(joinIntent);
//        } else {
//            mActivity.startActivity(IntentUtils.newIntent(getApplicationContext(), TripListActivity.class));
//        }

        mActivity.finish();
    }

    private void updateInvitationStatus(Intent intent) {
        LogUtils.LOGE("***> deep link", "updateInvitationStatus");
        String invitationId = AppInviteReferral.getInvitationId(intent);

        // Note: these  calls return PendingResult(s), so one could also wait to see
        // if this succeeds instead of using fire-and-forget, as is shown here
        if (AppInviteReferral.isOpenedFromPlayStore(intent)) {
            AppInvite.AppInviteApi.updateInvitationOnInstall(mGoogleApiClient, invitationId);
        }

        // If your invitation contains deep link information such as a coupon code, you may
        // want to wait to call `convertInvitation` until the time when the user actually
        // uses the deep link data, rather than immediately upon receipt
        AppInvite.AppInviteApi.convertInvitation(mGoogleApiClient, invitationId);
    }
}
