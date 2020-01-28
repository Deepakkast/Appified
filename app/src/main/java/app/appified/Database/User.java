
package app.appified.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.drawable.Drawable;

import java.util.Arrays;

@Entity(tableName = "Apps")
public class User {
    @PrimaryKey(autoGenerate = true)
    private Long id=null;


    @ColumnInfo(name = "app_name")
    public String app_name;

    @ColumnInfo(name = "package_name")
    public String package_name;

    @ColumnInfo(name = "last_update")
    public String last_update;

    @ColumnInfo(name = "first_install")
    public String first_install;

    @ColumnInfo(name = "server_sync")
    public boolean server_sync;


    @ColumnInfo(name = "is_install")
    public boolean is_install;

    @ColumnInfo(name = "app_icon")
    public byte[] app_icon;

    @ColumnInfo(name="is_hide")
    public boolean is_hide;


    public User(Long id, String app_name, String package_name, String last_update, String first_install, boolean server_sync, boolean is_install,boolean is_hide) {
        this.id = id;
        this.app_name = app_name;
        this.package_name = package_name;
        this.last_update = last_update;
        this.first_install = first_install;
        this.server_sync = server_sync;
        this.is_install = is_install;
        //this.app_icon=app_icon;
        this.is_hide=is_hide;
    }


    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getFirst_install() {
        return first_install;
    }

    public void setFirst_install(String first_install) {
        this.first_install = first_install;
    }

    public boolean isIs_hide() {
        return is_hide;
    }

    public void setIs_hide(boolean is_hide) {
        this.is_hide = is_hide;
    }

    public boolean isServer_sync() {
        return server_sync;
    }

    public void setServer_sync(boolean server_sync) {
        this.server_sync = server_sync;
    }

    public boolean isIs_install() {
        return is_install;
    }


    public void setIs_install(boolean is_install) {
        this.is_install = is_install;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", app_name='" + app_name + '\'' +
                ", package_name='" + package_name + '\'' +
                ", last_update='" + last_update + '\'' +
                ", first_install='" + first_install + '\'' +
                ", server_sync=" + server_sync +
                ", is_install=" + is_install +
                ", app_icon=" + Arrays.toString(app_icon) +
                ", is_hide=" + is_hide +
                '}';
    }
//  //  public byte[] getApp_icon() {
//        return app_icon;
//    }
//
//    public void setApp_icon(byte[] app_icon) {
//        this.app_icon = app_icon;
//    }
//

}
