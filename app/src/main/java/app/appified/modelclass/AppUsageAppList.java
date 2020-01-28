package app.appified.modelclass;
import android.app.usage.UsageStats;
import android.graphics.drawable.Drawable;

public class AppUsageAppList implements Comparable<AppUsageAppList> {
    private final String appName;
    private final Drawable icon;
    private final UsageStats usageStats;

    public AppUsageAppList(UsageStats usageStats, Drawable icon, String appName) {
        this.appName = appName;
        this.usageStats = usageStats;
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }


    public Drawable getIcon() {
        return icon;
    }


    public UsageStats getUsageStats() {
        return usageStats;
    }


    @Override
    public int compareTo(AppUsageAppList o) {
        if (usageStats == null && o.getUsageStats() != null) {
            return 1;
        } else if (o.getUsageStats() == null && usageStats != null) {
            return -1;
        } else if (o.getUsageStats() == null && usageStats == null) {
            return 0;
        } else {
            return Long.compare(o.getUsageStats().getTotalTimeInForeground(),
                    usageStats.getTotalTimeInForeground());
        }
    }
}
