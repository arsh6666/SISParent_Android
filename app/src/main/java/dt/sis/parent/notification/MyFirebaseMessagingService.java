package dt.sis.parent.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import dt.sis.parent.R;
import dt.sis.parent.activity.SplashActivity;
import dt.sis.parent.helper.SessionManager;
import retrofit2.Call;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    SessionManager myUtility;
    String title="";
    String body="";
    String mini_icon="";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.e(TAG, "data: " + remoteMessage.getData().toString());

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Message data payload: " + remoteMessage.getData());
                mini_icon = remoteMessage.getData().get("icon");
                body = remoteMessage.getData().get("body");
                title = remoteMessage.getData().get("title");

            }
            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                body = remoteMessage.getNotification().getBody();
                title = remoteMessage.getNotification().getTitle();
                mini_icon= remoteMessage.getNotification().getIcon();

            }
            handleNotification();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onNewToken(@NonNull String token) {
        myUtility = new SessionManager(getApplicationContext());
        myUtility.setFirebaseToken(token);

    }

    private void handleNotification() {
        int requestId = new Random().nextInt(9999-1000)+1000;

        Bitmap iconBitMap = null;
        if (mini_icon != null) {
            iconBitMap =    getBitmapFromURL(mini_icon);
        }
        Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = null;

        final int icon = R.drawable.logo ; // image should be in drawable folder
        if (iconBitMap == null) {
//            When Inbox Style is applied, user can expand the notification
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle(title);
            bigTextStyle.setSummaryText(body);
            notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setStyle(bigTextStyle)
                    .setSmallIcon(icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), icon))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent) ;

        }else{
            //If Bitmap is created from URL, show big icon
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.setBigContentTitle(title);
            bigPictureStyle.setSummaryText(Html.fromHtml(body).toString());
            bigPictureStyle.bigPicture(iconBitMap);
            notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setStyle(bigPictureStyle)
                    .setSmallIcon(icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), icon))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    getString(R.string.default_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            channel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            channel.setLightColor(Color.BLUE);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        final Notification notification = notificationBuilder.build();

        if (notificationManager != null) {

            notificationManager.notify(requestId, notification);

        }

    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
