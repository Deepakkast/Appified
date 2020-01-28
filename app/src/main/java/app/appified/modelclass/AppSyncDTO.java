package app.appified.modelclass;

import android.graphics.drawable.Drawable;

public class AppSyncDTO {

   public String apps_name;
    public  String package_name;
    public String install_date;
    public  String last_updated_date;
    public boolean is_hide;
    public Drawable app_icon;

    public AppSyncDTO(String apps_name, String package_name, String install_date, String last_updated_date, boolean is_hide, Drawable app_icon) {
        this.apps_name = apps_name;
        this.package_name = package_name;
        this.install_date = install_date;
        this.last_updated_date = last_updated_date;
        this.is_hide = is_hide;
        this.app_icon = app_icon;
    }

    public String getApps_name() {
        return apps_name;
    }

    public void setApps_name(String apps_name) {
        this.apps_name = apps_name;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getInstall_date() {
        return install_date;
    }

    public void setInstall_date(String install_date) {
        this.install_date = install_date;
    }

    public String getLast_updated_date() {
        return last_updated_date;
    }

    public void setLast_updated_date(String last_updated_date) {
        this.last_updated_date = last_updated_date;
    }

    public boolean getIs_hide() {
        return is_hide;
    }

    public void setIs_hide(boolean is_hide) {
        this.is_hide = is_hide;
    }

    public Drawable getApp_icon() {
        return app_icon;
    }

    public void setApp_icon(Drawable app_icon) {
        this.app_icon = app_icon;
    }

    @Override
    public String toString() {
        return "AppSyncDTO{" +
                "apps_name='" + apps_name + '\'' +
                ", package_name='" + package_name + '\'' +
                ", install_date='" + install_date + '\'' +
                ", last_updated_date='" + last_updated_date + '\'' +
                ", is_hide='" + is_hide + '\'' +
                ", app_icon='" + app_icon + '\'' +
                '}';
    }
}

