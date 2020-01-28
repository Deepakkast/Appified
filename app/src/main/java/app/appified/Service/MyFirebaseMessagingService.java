package app.appified.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import app.appified.Activity.SplashActivity;
import app.appified.Config.ApolloClientService;
import app.appified.R;
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Context context;

    @Override
    public void onNewToken(String s) {
        Log.d("On token refresh","refresh token : " +s);
        super.onNewToken(s);
        sendRegistrationToServer(s);
        storeRegIdInPref(s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("On message received","message recived");
        context=this;
        String title = remoteMessage.getData().get("Name");
        String message = remoteMessage.getData().get("Icon");
        sendNotification(title,message);
        if (title != null || message!=null) {
            sendNotification(title,message);
        }
        super.onMessageReceived(remoteMessage);
    }




    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.d("Tag","send to server:  " + token);


        ApolloClientService.firebaseToken(token, null, new ApolloClientService.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {


                Log.d("TAG","token send sucess in service ");
            }

            @Override
            public void onFailure(String errorMessage, int errorCode) {
                Log.d("TAG","token send failure in service ");

            }
        });


    }
    private void storeRegIdInPref(String token) {

    }



    private void sendNotification(String title,String description) {
        Intent intent = new Intent(this,SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.app_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_search_black_24dp)
                        .setLargeIcon(getBitmapFromURL(description))
                        .setContentTitle("Your Friend" + " " + title + " " + " has installed new app" )
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



    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
