package app.appified.modelclass;

import java.io.Serializable;

public class UserApp implements Serializable {

    public String appName;
    public  String packageName;
    public String icon;


    public UserApp(String appName, String packageName,String icon) {
        this.appName = appName;
        this.packageName = packageName;
        this.icon=icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
