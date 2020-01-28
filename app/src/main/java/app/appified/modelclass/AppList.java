package app.appified.modelclass;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class AppList {
    private String appName;
    private Drawable icon;
    private boolean isHiden;
    private long date;              //for tab third fragment page used
    private String demoText;
    public List<AppList> lists = new ArrayList<>();
    public AppList(String appName, Drawable icon, boolean isHiden) {
        this.appName = appName;
        this.icon = icon;
        this.isHiden = isHiden;
    }

    public String getDemoText() {
        return demoText;
    }

    public void setDemoText(String demoText) {
        this.demoText = demoText;
    }

    //          for using in third tab fragment page
    public AppList(String appName, Drawable icon, long date) {
        this.appName = appName;
        this.icon = icon;
        this.date = date;
        //this.demoText = demoText;
    }
    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public boolean isHiden() {
        return isHiden;
    }
    public void setHiden(boolean isHiden) {
        this.isHiden = isHiden;
    }
    public List<AppList> getLists() {
        return lists;
    }
    public void setLists(List<AppList> lists) {
        this.lists = lists;
    }
}