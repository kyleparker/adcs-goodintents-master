package com.udacity.adcs.app.goodintents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.appinvite.AppInviteReferral;
import com.udacity.adcs.app.goodintents.utils.Constants;

/**
 * This is part of the AppInvite process - work in progress!!
 *
 * Created by kyleparker on 9/25/2015.
 */
public class ReferrerReceiver extends BroadcastReceiver {

    public ReferrerReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        // Create deep link intent with correct action and add play store referral information
        Intent deepLinkIntent = AppInviteReferral.addPlayStoreReferrerToIntent(intent,
                new Intent(Constants.ACTION_DEEP_LINK_JOIN_ACTIVITY));

        // Let any listeners know about the change
        LocalBroadcastManager.getInstance(context).sendBroadcast(deepLinkIntent);
    }
}