package com.udacity.adcs.app.goodintents.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.udacity.adcs.app.goodintents.ParseDeepLinkActivity;
import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.objects.Event;
import com.udacity.adcs.app.goodintents.objects.Person;
import com.udacity.adcs.app.goodintents.objects.Search;
import com.udacity.adcs.app.goodintents.ui.base.BaseActivity;
import com.udacity.adcs.app.goodintents.utils.AccountUtils;
import com.udacity.adcs.app.goodintents.utils.Constants;
import com.udacity.adcs.app.goodintents.utils.IntentUtils;
import com.udacity.adcs.app.goodintents.utils.LogUtils;
import com.udacity.adcs.app.goodintents.utils.PreferencesUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 *
 * Created by kyleparker on 11/9/2015.
 */
public class MainActivity extends BaseActivity {
    private BroadcastReceiver mRegistrationReceiver = null;
    private BroadcastReceiver mDeepLinkReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (AccountUtils.isAuthenticated(this)) {
            if (savedInstanceState == null) {
                // No savedInstanceState, so it is the first launch of this activity
                Intent intent = getIntent();
                if (AppInviteReferral.hasReferral(intent)) {
                    // In this case the referral data is in the intent launching the MainActivity,
                    // which means this user already had the app installed. We do not have to
                    // register the Broadcast Receiver to listen for Play Store Install information
                    launchDeepLinkActivity(intent);
                    return;
                }
            }

            // The person has logged in, so continue on to the next screen
            registerReceiver();
            checkAccount();
        } else {
            AccountUtils.startAuthenticationFlow(this, getIntent());
            mActivity.finish();
        }
    }

    /**
     * Launch DeepLinkActivity with an intent containing App Invite information
     */
    private void launchDeepLinkActivity(Intent intent) {
        LogUtils.LOGE("***> Here", "launchDeepLinkActivity:" + intent);
        Intent newIntent = new Intent(intent).setClass(this, ParseDeepLinkActivity.class);
        startActivity(newIntent);
    }

    /**
     * Register receivers for the activity. The broadcast receiver for the GCM registration may not be needed - while it is possible
     * to return an error, need to review how to initiate another intent service to retrieve the token.
     */
    private void registerReceiver() {
        // TODO: Is this needed?
        mRegistrationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean sentToken = PreferencesUtils.getBoolean(mActivity, R.string.gcm_sent_to_server_key, false);

                if (sentToken) {
                    LogUtils.LOGE("***>GCM", "sent");
                } else {
                    LogUtils.LOGE("***>GCM", "error");
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.RequestCode.HANDLE_GMS:
                startActivity();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRegistrationReceiver != null) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationReceiver,
                    new IntentFilter(Constants.ACTION_GCM_REGISTRATION_COMPLETE));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerDeepLinkReceiver();
    }

    @Override
    protected void onPause() {
        if (mRegistrationReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationReceiver);
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterDeepLinkReceiver();
    }

    /**
     * Step 1 of the profile sync process
     *
     * Retrieve the person object from the local database based on the user guid to determine if the Google Account ID is present
     */
    private void checkAccount() {
        Runnable load = new Runnable() {
            public void run() {
                try {
//                    mPerson = mProvider.getPerson(mLogin.getUserGuid(), false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    mActivity.runOnUiThread(checkAccountRunnable);
                }
            }
        };

        Thread thread = new Thread(null, load, "checkAccount");
        thread.start();
    }

    /**
     * Verify the person object exists and determine if the Google Account ID has been sycned to the profile
     *
     * If not, continue to step 2 of the process, otherwise start the next activity to display the app
     */
    private final Runnable checkAccountRunnable = new Runnable() {
        public void run() {
            try {
//                if (mPerson != null) {
//                    // Check to see if the profile has a Google account ID AND that the user is online
//                    // If the user is not currently connected to the network, they will not be able to retrieve the profile data
//                    // and therefore won't be able to use the app. The next time the app is opened, it will check again.
//                    if (TextUtils.isEmpty(mPerson.getGoogleAccountId()) && SystemUtils.isOnline(mActivity)) {
//                        // The user has not sync a Google account to the profile, show the account chooser
//                        Intent intent = IntentUtils.newIntent(mActivity, SignInActivity.class);
//                        intent.putExtra(Constants.Extra.ACTION, Constants.Action.LOGOUT);
//                        mActivity.startActivity(intent);
//                        mActivity.finish();
//                    } else {
//                        if (TextUtils.isEmpty(mLogin.getGCMRegistrationId())) {
//                            // Start IntentService to register this application with GCM.
//                            Intent intent = new Intent(mActivity.getApplicationContext(), RegistrationIntentService.class);
//                            startService(intent);
//                        }

                if (!PreferencesUtils.getBoolean(mActivity, R.string.initial_search_load_key, false)) {
                    LoadSearch task = new LoadSearch();
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                if (!PreferencesUtils.getBoolean(mActivity, R.string.initial_db_load_key, false)) {
                    LoadDatabase task = new LoadDatabase();
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                startActivity();
//                    }
//                } else {
//                    // Clean-up partial login and allow the user to try again
//                    PreferencesUtils.setString(mActivity, R.string.google_account_key, PreferencesUtils.GOOGLE_ACCOUNT_DEFAULT);
//                    SharedPreferencesHelper.setAppData(mActivity, Constants.Pref.LOGIN_DONE, false);
//
////                    Toast.makeText(mActivity, R.string.toast_error_login, Toast.LENGTH_LONG).show();
//                    Intent intent = IntentUtils.newIntent(mActivity, SignInActivity.class);
//                    intent.putExtra(Constants.Extra.ACTION, Constants.Action.LOGIN);
//                    mActivity.startActivity(intent);
//                    mActivity.finish();
//                }
            } catch (Exception ex) {
                // Clean-up partial login and allow the user to try again
                PreferencesUtils.setString(mActivity, R.string.google_account_key, PreferencesUtils.GOOGLE_ACCOUNT_DEFAULT);
//                SharedPreferencesHelper.setAppData(mActivity, Constants.Pref.LOGIN_DONE, false);

                Intent intent = IntentUtils.newIntent(mActivity, SignInActivity.class);
                intent.putExtra(Constants.Extra.ACTION, Constants.Action.LOGIN);
                mActivity.startActivity(intent);
                mActivity.finish();

                ex.printStackTrace();
            }
        }
    };

    /**
     * Check to see whether the Google Play Services are installed on the device - required to use Maps API v2
     */
    private boolean checkGooglePlayServices() {
        int code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (code != ConnectionResult.SUCCESS) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(code, mActivity, Constants.RequestCode.HANDLE_GMS,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            mActivity.finish();
                        }
                    });
            if (dialog != null) {
                dialog.show();
            }

            return false;
        }

        return true;
    }

    private void registerDeepLinkReceiver() {
        // Create local Broadcast receiver that starts DeepLinkActivity when a deep link
        // is found
        mDeepLinkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (AppInviteReferral.hasReferral(intent)) {
                    launchDeepLinkActivity(intent);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_DEEP_LINK_JOIN_ACTIVITY);
        LocalBroadcastManager.getInstance(mActivity.getApplicationContext()).registerReceiver(mDeepLinkReceiver, intentFilter);
    }

    /**
     * Start the next activity
     */
    private void startActivity() {
        if (checkGooglePlayServices()) {
            mActivity.startActivity(IntentUtils.newIntent(mActivity, FeedActivity.class));
            mActivity.finish();
        }
    }

    private void unregisterDeepLinkReceiver() {
        if (mDeepLinkReceiver != null) {
            LocalBroadcastManager.getInstance(mActivity.getApplicationContext()).unregisterReceiver(mDeepLinkReceiver);
        }
    }

    /**
     * Async task to load the initial collection of data for the search
     */
    private class LoadSearch extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... args) {
            try {
                // Retrieve all events
                List<Event> eventList = mProvider.getEventList();

                for (Event event : eventList) {
                    Search search = mProvider.getSearch(event.getId(), Constants.Type.EVENT);

                    if (search == null) {
                        search = new Search();
                        search.setId(event.getId());
                        search.setName(event.getName());
                        search.setDesc(event.getDescription());
                        search.setOrderBy(1);
                        search.setTypeId(Constants.Type.EVENT);

                        mProvider.insertSearch(search);
                    } else {
                        search.setId(event.getId());
                        search.setName(event.getName());
                        search.setDesc(event.getDescription());
                        search.setOrderBy(2);
                        search.setTypeId(Constants.Type.EVENT);

                        mProvider.updateSearch(search);
                    }
                }

                // Retrieve all people
                List<Person> personList = mProvider.getPersonList();

                for (Person person : personList) {
                    Search search = mProvider.getSearch(person.getId(), Constants.Type.PERSON);

                    if (search == null) {
                        search = new Search();
                        search.setId(person.getId());
                        search.setName(person.getDisplayName());
                        search.setDesc(person.getEmailAddress());
                        search.setOrderBy(2);
                        search.setTypeId(Constants.Type.PERSON);

                        mProvider.insertSearch(search);
                    } else {
                        search.setId(person.getId());
                        search.setName(person.getDisplayName());
                        search.setDesc(person.getEmailAddress());
                        search.setOrderBy(2);
                        search.setTypeId(Constants.Type.PERSON);

                        mProvider.updateSearch(search);
                    }
                }

                PreferencesUtils.setBoolean(mActivity, R.string.initial_search_load_key, true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Initial load for the database
     */
    private class LoadDatabase extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... args) {
            try {
                boolean isSuccess = false;
                File data = Environment.getDataDirectory();

                String currentDbPath = "//data//com.udacity.adcs.app.goodintents//databases//goodintents.db";

                File currentDb = new File(data, currentDbPath);
                File restoreDb = getFileFromAsset(mActivity, mActivity.getExternalFilesDir(null) + "/", "goodintents.db");

                if (restoreDb != null ? restoreDb.exists() : false) {
                    FileInputStream is = new FileInputStream(restoreDb);
                    FileOutputStream os = new FileOutputStream(currentDb);

                    FileChannel src = is.getChannel();
                    FileChannel dst = os.getChannel();
                    dst.transferFrom(src, 0, src.size());

                    is.close();
                    os.close();
                    src.close();
                    dst.close();
                    isSuccess = true;
                }

                if (isSuccess) {
                    PreferencesUtils.setBoolean(mActivity, R.string.initial_db_load_key, true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        private File getFileFromAsset(Context context, String path, String filename) {
            AssetManager assetManager = context.getResources().getAssets();
            File destinationFile = new File(path, new File(filename).getName());

            if (destinationFile.exists()) {
                destinationFile.delete();
            }

            try {
                // The local file does not exist, so move the download into the proper folder
                InputStream in = assetManager.open(filename);
                OutputStream out = new FileOutputStream(destinationFile);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }

                in.close();
                out.flush();
                out.close();

                return destinationFile;
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
