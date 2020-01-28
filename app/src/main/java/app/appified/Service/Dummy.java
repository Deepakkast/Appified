/*
package app.appified.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import app.appified.Activity.SplashActivity;
import app.appified.Config.ApolloClientService;
import app.appified.R;
import app.appified.Utils.LogUtil;

public class Dummy {


    package app.appified.Service;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import app.appified.Activity.SplashActivity;
import app.appified.Config.ApolloClientService;
import app.appified.R;
import app.appified.Utils.LogUtil;

    public class MyFirebaseMessagingService extends FirebaseMessagingService {
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
            String username = remoteMessage.getData().get("Name");
            LogUtil.debug("username");
            String icon=remoteMessage.getData().get("icon");
            String notification=remoteMessage.getData().get("installApp");
            LogUtil.debug("user install is : " +username + icon + notification );
            sendNotification(username,icon,notification);
            if (username != null || icon!=null || notification!=null) {
                sendNotification(username,icon,notification);
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



        private void sendNotification(String username,String icon,String notification) {
            Intent intent = new Intent(this,SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 */
/* Request code *//*
, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String channelId = getString(R.string.app_name);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(Integer.parseInt(icon))
                            .setContentTitle(username)
                            .setContentText(notification)
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

            NotificationManagerCompat.from(this).notify(0 */
/* ID of notification *//*
, notificationBuilder.build());
        }
    }

}
*/
