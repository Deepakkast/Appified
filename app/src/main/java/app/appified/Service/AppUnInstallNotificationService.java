package app.appified.Service;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.Nullable;
import app.appified.Activity.AppScreenActivity;
import app.appified.R;
import app.appified.modelclass.AppUnInstallItemList;


public class AppUnInstallNotificationService extends IntentService {
    int rh;
    Drawable icon;

    private static final int flags = PackageManager.GET_META_DATA |
            PackageManager.GET_SHARED_LIBRARY_FILES |
            PackageManager.GET_UNINSTALLED_PACKAGES;

    private UsageStatsManager usageStatsManager;
    private PackageManager packageManager;
    List<AppUnInstallItemList> appLists;

    public AppUnInstallNotificationService() {
        super("AppUnInstallNotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(AppUnInstallNotificationService.class.getSimpleName(), "Inside Service");
        usageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        long beginTime = calendar.getTimeInMillis();
        packageManager = getApplicationContext().getPackageManager();
        List<String> installedApps = getInstalledAppList();
        Map<String, UsageStats> usageStats = usageStatsManager.queryAndAggregateUsageStats(beginTime, System.currentTimeMillis());
        List<UsageStats> stats = new ArrayList<>();
        if (usageStats != null){
            stats.addAll(usageStats.values());}
        List<AppUnInstallItemList> finalList = buildUsageStatsWrapper(installedApps, stats);
        Log.d("TAG", "App Lists" + finalList);
        for (int i = 0; i < finalList.size(); i++) {
            if (finalList.get(i).getUsageStats() != null) {
                Long currentTime = System.currentTimeMillis();
                Long appLastUsedTime = finalList.get(i).getUsageStats().getLastTimeUsed();
                Long difference = currentTime - appLastUsedTime;
                rh = getApplicationContext().getResources().getIdentifier("notification", "drawable", getApplicationContext().getPackageName());
                icon = finalList.get(i).getIcon();
                long duration = 2592000000L;
                if (difference > duration) {
                    sendNotification(finalList, i);
                }
            }
        }

    }
    private void sendNotification(List<AppUnInstallItemList> lists, int position){
        // Context context;
        Drawable icon = lists.get(position).getIcon() ;
        Bitmap bitmap1 = ((BitmapDrawable) icon).getBitmap();
        Intent intent = new Intent(this, AppScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        //Toast.makeText(this, "Clicked on notification", Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Poll");
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentTitle(lists.get(position).getAppName());
        builder.setLargeIcon(bitmap1);
        builder.setContentText("You not used this app more than one month");
        builder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Poll", "Poll", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager1 = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager1.createNotificationChannel(channel);

            NotificationManagerCompat.from(this).notify(new Random().nextInt(), builder.build());
        }
    }


    private List<String> getInstalledAppList(){
        List<ApplicationInfo> infos = packageManager.getInstalledApplications(flags);
        List<String> installedApps = new ArrayList<>();
        for (ApplicationInfo info : infos){
            if (!isSystemPackage(info)) installedApps.add(info.packageName);
        }
        return installedApps;
    }
    private boolean isSystemPackage(ApplicationInfo applicationInfo) {
        return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
    private List<AppUnInstallItemList> buildUsageStatsWrapper(List<String> packageNames, List<UsageStats> usageStatses) {
        List<AppUnInstallItemList> list = new ArrayList<>();
        for (String name : packageNames) {
            boolean added = false;
            for (UsageStats stat : usageStatses) {
                if (name.equals(stat.getPackageName())) {
                    added = true;
                    list.add(fromUsageStat(stat));
                }
            }
            if (!added) {
                list.add(fromUsageStat(name));
            }
        }
        Collections.sort(list);
        return list;
    }

    private AppUnInstallItemList fromUsageStat(String packageName) throws IllegalArgumentException {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 0);
            return new AppUnInstallItemList(null, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());

        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private AppUnInstallItemList fromUsageStat(UsageStats usageStats) throws IllegalArgumentException {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(usageStats.getPackageName(), 0);
            return new AppUnInstallItemList(usageStats, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());

        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
