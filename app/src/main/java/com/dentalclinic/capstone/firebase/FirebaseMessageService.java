package com.dentalclinic.capstone.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.activities.FeedbackActivity;
import com.dentalclinic.capstone.activities.MainActivity;
import com.dentalclinic.capstone.api.responseobject.FeedbackResponse;
import com.dentalclinic.capstone.models.Setting;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.SettingManager;
import com.dentalclinic.capstone.utils.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.Map;

public class FirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage message) {
        String responseType = message.getData().get("type");
        Log.d(AppConst.DEBUG_TAG, "FIREBASE message received");
        if (responseType != null && responseType.equals(AppConst.RESPONSE_FEEDBACK)) {
            try {
                Log.d(AppConst.DEBUG_TAG, message.getData().get("body"));
                FeedbackResponse response =
                        Utils.parseJson(message.getData().get("body"), FeedbackResponse.class);
                Map<String, String> map = message.getData();
                String title = map.get("title");
                String msg = map.get("message");
                showNotifications(title, msg, response);
            } catch (JsonSyntaxException ex) {
                ex.printStackTrace();
                showToast(ex.getMessage());
            }
        } else if (responseType != null && responseType.equals(AppConst.RESPONSE_REMINDER)) {
            Map<String, String> map = message.getData();
            String title = map.get("title");
            String msg = map.get("message");
            String body = map.get("body");

            Log.d(AppConst.DEBUG_TAG, message.getData().get("body"));
            showNotifications(title, msg, body);
        }else if (responseType != null && responseType.equals(AppConst.RESPONSE_REMINDER)) {
            Map<String, String> map = message.getData();
            String title = map.get("title");
            String msg = map.get("message");
            String body = map.get("body");

            Log.d(AppConst.DEBUG_TAG, message.getData().get("body"));
            showNotifications(title, msg, body);
        }
    }///End oncreated

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotificationFinal(String messageBody) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "XXX";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void showToast(String msg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        });
    }

    private void showNotifications(String title, String msg, FeedbackResponse response) {
        String channelId = AppConst.CHANNEL_FEEDBACK;
        Intent i = new Intent(this, FeedbackActivity.class);
        i.putExtra(AppConst.TREATMENT_DETAIL_BUNDLE, response);
        Context c = getApplicationContext();
        c.startActivity(i);
//        Log.d(AppConst.DEBUG_TAG, "FirebaseMessageService:RUN");
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                i, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationManager manager =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, channelId)
//                .setContentText(msg)
//                .setContentTitle(title)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setSmallIcon(R.mipmap.ic_launcher_round);
//        Setting setting = SettingManager.getSettingPref(this);
//        if (setting != null && setting.isVibrate()) {
//            notiBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                    .setSound(defaultSoundUri);
//        }
//        Notification notification = notiBuilder.build();
//// Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            manager.createNotificationChannel(channel);
//        }
//        manager.notify(0, notification);
    }

    private void showNotifications(String title, String msg, String body) {
        String channelId = AppConst.CHANNEL_FEEDBACK;
        Log.d(AppConst.DEBUG_TAG, "FirebaseMessageService:RUN");
        Intent intent = new Intent(this, MainActivity.class);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, AppConst.REQUEST_CODE_REMINDER,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(msg)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body));
        Setting setting = SettingManager.getSettingPref(this);
        if (setting != null && setting.isVibrate()) {
            notiBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(defaultSoundUri);
        }

        Notification notification = notiBuilder.build();
// Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, notification);
    }


}
