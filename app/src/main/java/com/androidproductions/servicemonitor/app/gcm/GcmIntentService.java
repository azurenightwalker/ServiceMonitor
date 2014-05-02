package com.androidproductions.servicemonitor.app.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.androidproductions.servicemonitor.app.DetailsActivity;
import com.androidproductions.servicemonitor.app.R;
import com.androidproductions.servicemonitor.app.data.services.ServiceState;
import com.androidproductions.servicemonitor.app.data.services.ServiceStateHelper;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    NotificationManager mNotificationManager;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                GCMMessage message = new GCMMessage(extras, getApplicationContext());
                ServiceStateHelper.saveStatusFromMessage(getApplicationContext(), message);
                sendNotification(message);
                Log.i("ServiceMonitor", "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(GCMMessage msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra(DetailsActivity.ServiceKey,msg.getServiceId());
        detailsIntent.setFlags(detailsIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                detailsIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(String.format(getString(R.string.notificationHeader),
                                msg.getServiceId(), ServiceState.parse(msg.getStatus())))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg.toString()))
                        .setContentText(msg.toString());

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
