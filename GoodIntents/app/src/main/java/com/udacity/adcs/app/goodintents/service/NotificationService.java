package com.udacity.adcs.app.goodintents.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.ui.MainActivity;

/**
 * Created by demouser on 11/10/15.
 */
public class NotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                sentNotification();
            }
        }.start();

        super.onCreate();
    }


    private void sentNotification(){
        final int WEATHER_NOTIFICATION_ID = 3004;
        String title = this.getString(R.string.app_name);
        // Define the text of the forecast.
        String contentText = "All-In to Fight Cancer Texas Hold'em Fundraiser - Volunteers Needed";
        //build your notification here.
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setColor(this.getResources().getColor(R.color.primary_material_light))
                .setSmallIcon(R.drawable.google_logo)
                .setContentTitle(title)
                .setContentText(contentText);

        //Create an explicit intent for an Activity in our app
        // Make something interesting happen when the user clicks on the notification.
        // In this case, opening the app is sufficient.
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
        mNotificationManager.notify(WEATHER_NOTIFICATION_ID, mBuilder.build());

        stopSelf();
    }
}
