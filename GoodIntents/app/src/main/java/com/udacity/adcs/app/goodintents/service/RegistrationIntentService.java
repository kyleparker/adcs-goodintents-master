package com.udacity.adcs.app.goodintents.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.utils.Constants;
import com.udacity.adcs.app.goodintents.utils.GCMUtils;
import com.udacity.adcs.app.goodintents.utils.LogUtils;
import com.udacity.adcs.app.goodintents.utils.PreferencesUtils;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {
    private static final String TAG = LogUtils.makeLogTag(RegistrationIntentService.class);

    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // Initially this call goes out to the network to retrieve the token, subsequent calls are local.
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(Constants.GCM_SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                LogUtils.LOGE(TAG, "GCM Registration Token: " + token);

                // Register the user's device token on the server
                boolean registered = GCMUtils.sendRegistrationToServer(this, token);

                // Subscribe to topic channels
                subscribeTopics(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
                PreferencesUtils.setBoolean(this, R.string.gcm_sent_to_server_key, registered);
            }
        } catch (Exception e) {
            LogUtils.LOGE(TAG, "Failed to complete token refresh " + e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            PreferencesUtils.setBoolean(this, R.string.gcm_sent_to_server_key, false);
        }

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Constants.ACTION_GCM_REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
