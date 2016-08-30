package com.software.achilles.tasked.view.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotificationService {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private static Context appContext;

    // ------------------------- Constructor -------------------------

    public NotificationService(Context appContext) {
        NotificationService.appContext = appContext;
    }

    // -------------------------- Use Cases --------------------------

    public static void showNotification(String title, String description,
                                                      Class responseActivity, int drawable){
        // Building the notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(appContext)
                        .setSmallIcon(drawable)
                        .setContentTitle(title)
                        .setContentText(description);

        // Play sound depending on the preferences
        Boolean notificationTone = PreferenceManager.getDefaultSharedPreferences(
                appContext).getBoolean("notificationTone", true);
        if(notificationTone) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(notification);
        }

        // Intent to launch at notificationOnClick
        Intent resultIntent = new Intent(appContext, responseActivity);

        // Initializing the stackBuilder
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(appContext);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(responseActivity);

        // Adds the Intent that starts the Activity on click
        stackBuilder.addNextIntent(resultIntent);

        // Setting the intent in the notification builder
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Initializing the notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Same id implies override
        int notificationId = 1;

        // Launching the Notification
        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}
