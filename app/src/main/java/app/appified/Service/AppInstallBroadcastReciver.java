package app.appified.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import app.appified.Config.App;
import app.appified.R;

public class AppInstallBroadcastReciver extends BroadcastReceiver {
    public static final String HIDE_UNHIDE = "hide_unhide";
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("appified receiver", "in on recive of install");

        if (App.getPreferences().getUserAppStatusFlag()==1) {
            final String packageNames = intent.getData().getEncodedSchemeSpecificPart();
            //PackageManager packageManager = context.getPackageManager();
            if (packageNames != null) {
                Log.d("appified receiver", "installed app is : " + packageNames);
                try {
                    int notification_id = 123;
                    Intent hide = new Intent(context, ButtonReciver.class);
                    hide.setAction("HIDE");
                    hide.putExtra("packagename", packageNames);
                    hide.putExtra("notification_id", notification_id);
                    PendingIntent pendinghide = PendingIntent.getBroadcast(context, 0, hide, PendingIntent.FLAG_UPDATE_CURRENT);
                    //for unhide
                    Intent unhide = new Intent(context, ButtonReciver.class);
                    unhide.setAction("UNHIDE");
                    unhide.putExtra("notification_id", notification_id);
                    unhide.putExtra("packagename", packageNames);
                    PendingIntent unhidepending = PendingIntent.getBroadcast(context, 0, unhide, PendingIntent.FLAG_UPDATE_CURRENT);
                    notificationManagerCompat = NotificationManagerCompat.from(context);
                    Notification notification = new NotificationCompat.Builder(context, HIDE_UNHIDE)
                            .setSmallIcon(R.drawable.logo_big)
                            .setContentTitle("Do you want to hide from your Appified friends")
                            .setAutoCancel(true)
                            .setCategory("hide")
                            .addAction(R.drawable.ic_hide_iamge, "Share", pendinghide)
                            .addAction(R.drawable.ic_hide_iamge, "Don't Share", unhidepending)
                            .setContentText(" Appified Application ")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setCategory(NotificationCompat.CATEGORY_MESSAGE).build();
                    notificationManagerCompat.notify(notification_id, notification);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel(HIDE_UNHIDE, "Hide_Unhide", NotificationManager.IMPORTANCE_HIGH);
                        notificationChannel.setDescription("this is new notification");
                        NotificationManager manager = context.getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(notificationChannel);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

