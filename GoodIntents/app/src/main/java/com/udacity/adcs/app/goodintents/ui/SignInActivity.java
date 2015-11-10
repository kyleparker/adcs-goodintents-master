package com.udacity.adcs.app.goodintents.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.ui.base.BaseActivity;
import com.udacity.adcs.app.goodintents.utils.Constants;
import com.udacity.adcs.app.goodintents.utils.DialogUtils;
import com.udacity.adcs.app.goodintents.utils.IntentUtils;
import com.udacity.adcs.app.goodintents.utils.LogUtils;
import com.udacity.adcs.app.goodintents.utils.PreferencesUtils;
import com.udacity.adcs.app.goodintents.utils.SystemUtils;

/**
 *
 * Created by kyleparker on 11/9/2015.
 */
public class SignInActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = LogUtils.makeLogTag(SignInActivity.class);

    private NetworkReceiver mNetworkReceiver = null;

    private Intent mFinishIntent;
    private GoogleSignInAccount mAccount;
    private GoogleSignInOptions mSignInOptions;
    private MaterialDialog mProgressDialog = null;
    private Constants.Action mAction;
    private boolean mLoginError = false;

    // Client used to interact with Google APIs
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        getExtras();

        // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile
        // are included in DEFAULT_SIGN_IN.
        mSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mSignInOptions)
                .build();

        if (mAction.equals(Constants.Action.LOGIN)) {
            // TODO: If the user has already logged in and just updating the app, don't require the user to select the account again
            // NOTE: This should be handled by the AccountUtils.isAuthenticated method
            handleLogin(savedInstanceState);
        } else {
            findViewById(R.id.sign_in_container).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RequestCode.HANDLE_GMS || requestCode == Constants.RequestCode.RECOVER_FROM_AUTH_ERROR
                || requestCode == Constants.RequestCode.PLAY_SERVICES_ERROR_DIALOG) {
            if (resultCode == RESULT_OK) {
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
            }
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        } else if (requestCode == Constants.RequestCode.SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void onStart() {
        super.onStart();

        if (mAction.equals(Constants.Action.LOGIN)) {
            checkCachedCredentials();
        }
    }

    @Override
    protected void onDestroy() {
        if (mNetworkReceiver != null) {
            mActivity.getApplicationContext().unregisterReceiver(mNetworkReceiver);
            mNetworkReceiver = null;
        }

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        super.onDestroy();
    }

    private void checkCachedCredentials() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired, this asynchronous branch
            // will attempt to sign in the user silently. Cross-device single sign-on will occur in this branch.
            showProgressDialog(R.string.dialog_login_message);
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Get extras from the intent bundle
     */
    private void getExtras() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            mAction = (Constants.Action) bundle.getSerializable(Constants.Extra.ACTION);
        }
    }

    private void handleLogin(Bundle savedInstanceState) {
        final Intent intent = getIntent();
        if (intent.hasExtra(Constants.Extra.FINISH_INTENT)) {
            mFinishIntent = intent.getParcelableExtra(Constants.Extra.FINISH_INTENT);
        }

        if (savedInstanceState == null) {
            setupView();
        } else {
//            mPerson = savedInstanceState.getParcelable(Person.class.getName());
//            mLogin = savedInstanceState.getParcelable(Login.class.getName());
        }

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(mSignInOptions.getScopeArray());
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, Constants.RequestCode.SIGN_IN);
            }
        });
    }

    /**
     * Handle the logout operation
     * <p/>
     * Delete the login credentials from the local SQLite database and call the handler to delete from the backend SQL Server
     * Also reset any preferences/IDs that should not carry over to a new user
     * <p/>
     * Redirect to the {@link MainActivity} upon completion
     */
    private void handleLogout() {
        showProgressDialog(R.string.dialog_logout_message);
//        final Login login = mProvider.getLogin();

//        if (login != null) {
            Runnable load = new Runnable() {
                public void run() {
                    try {
//                        // Sign out of GCM message router
//                        GCMUtils.onSignOut(mActivity, login);
//                        LoginHandler.postLogout(login.getUserGuid(), login.getLoginGuid());
//
//                        mProvider.deleteLogin(login);
//                        mProvider.deleteAllPlaces();
//                        mProvider.deleteAllSearch();
//                        // Clear out the purchase history to ensure multiple users on a device don't hack to get the cloud sync
//                        mProvider.deletePersonToPurchase();
//
//                        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(mActivity,
//                                SearchRecentProvider.AUTHORITY, SearchRecentProvider.MODE);
//                        suggestions.clearHistory();
//
//                        UIUtils.updateAllWidgets(mActivity);
//                        AccountUtils.resetPreferences(mActivity);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        mActivity.runOnUiThread(handleLogoutRunnable);
                    }
                }
            };

            Thread thread = new Thread(null, load, "handleLogout");
            thread.start();
//        }
    }

    /**
     * Once the logout process has completed, display the login screen
     */
    private final Runnable handleLogoutRunnable = new Runnable() {
        public void run() {
            dismissProgressDialog();
            Intent intent = IntentUtils.newIntent(mActivity, SignInActivity.class);
            intent.putExtra(Constants.Extra.ACTION, Constants.Action.LOGIN);
            mActivity.startActivity(intent);
        }
    };

    /**
     * Handle the result of the sign in attempt
     *
     * @param result
     */
    private void handleSignInResult(GoogleSignInResult result) {
        LogUtils.LOGE(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            mAccount = result.getSignInAccount();
            finishSetup();
        } else {
            setupView();
        }
    }

    /**
     * Once the user has been authenticated, complete the login process
     */
    private void finishSetup() {
        showProgressDialog(R.string.dialog_login_message);
        PreferencesUtils.setString(mActivity, R.string.google_account_key, mAccount.getEmail());
        PreferencesUtils.setString(mActivity, R.string.google_account_id_key, mAccount.getId());

        Runnable load = new Runnable() {
            public void run() {
                try {
                } catch (Exception ex) {
                    mLoginError = false;
                    ex.printStackTrace();
                } finally {
                    mActivity.runOnUiThread(finishSetupRunnable);
                }
            }
        };

        Thread thread = new Thread(null, load, "finishSetup");
        thread.start();
    }

    /**
     * On the UI thread, set the final account information and start the main activity
     */
    private final Runnable finishSetupRunnable = new Runnable() {
        public void run() {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            try {
                if (!mLoginError) {
                    if (mFinishIntent != null) {
                        PreferencesUtils.getString(mActivity, R.string.google_account_key, mAccount.getEmail());

                        // Ensure the finish intent is unique within the task. Otherwise, if the task was started with this
                        // intent, and it finishes like it should, then startActivity on the intent again won't work.
                        mFinishIntent.addCategory(Constants.Post.AUTH_CATEGORY);
                        startActivity(mFinishIntent);
                    }

                    finish();
                } else {
                    handleLogout();
                    Toast.makeText(mActivity, R.string.toast_error_login, Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                handleLogout();
                Toast.makeText(mActivity, R.string.toast_error_login, Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    };

    /**
     * Start the login process
     * <p/>
     * A network connection is required in order to authenticate the user.
     */
    private void setupView() {
        dismissProgressDialog();

//        mLogin = mProvider.getLogin();
//        mPerson = mLogin != null ? mProvider.getPerson(mLogin.getUserGuid(), false) : new Person();

        PreferencesUtils.setString(mActivity, R.string.google_account_key, PreferencesUtils.GOOGLE_ACCOUNT_DEFAULT);

        // TODO: Determine online versus data connection available?
        if (SystemUtils.isOnline(mActivity)) {
            findViewById(R.id.login_container).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_container).setVisibility(View.VISIBLE);
            findViewById(R.id.open_settings_container).setVisibility(View.GONE);
        } else {
            // Registers BroadcastReceiver to track network connection changes.
            IntentFilter networkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            mNetworkReceiver = new NetworkReceiver();
            mActivity.getApplicationContext().registerReceiver(mNetworkReceiver, networkFilter);

            findViewById(R.id.login_container).setVisibility(View.GONE);
            findViewById(R.id.open_settings_container).setVisibility(View.VISIBLE);

            FloatingActionButton fabSettings = (FloatingActionButton) findViewById(R.id.fab_open_settings);
            fabSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                }
            });
        }
    }

    private void showProgressDialog(int message) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogUtils.createSpinnerProgressDialog(mActivity, DialogUtils.DEFAULT_TITLE_ID, message, false);
        }
        mProgressDialog.show();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, Constants.RequestCode.RECOVER_FROM_AUTH_ERROR);
            } catch (IntentSender.SendIntentException e) {
                LogUtils.LOGE(TAG, "Internal error encountered: " + e.getMessage());
            }
            return;
        }

        final int errorCode = connectionResult.getErrorCode();
        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, Constants.RequestCode.PLAY_SERVICES_ERROR_DIALOG).show();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (mAction.equals(Constants.Action.LOGOUT)) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            handleLogout();
                        }
                    });
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SystemUtils.isOnline(mActivity)) {
                checkCachedCredentials();
                setupView();
            }
        }
    }
}
