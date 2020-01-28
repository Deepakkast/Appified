package app.appified.Database;

import android.graphics.drawable.Drawable;
import android.util.Log;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import java.io.Serializable;
import java.util.List;
import app.appified.Utils.LogUtil;

@Table(name = "APPS_TABLE")
public class AppModel extends Model implements Serializable {


    private static final String TAG = "MyActivity";

    private static final String package_names = " packagename LIKE ?";

    public AppModel() {
        super();
    }

    @Column(name = "appname")
    public String app_name;

    @Column(name = "packagename", unique = true
            , onUniqueConflict = Column.ConflictAction.REPLACE, index = true)
    public String package_name;

    @Column(name = "installdate")
    public String insatll_date;

    @Column(name = "updatedate")
    public String update_date;

    @Column(name = "isinstall")
    public boolean is_install;

    @Column(name = "isserversync")
    public int server_sync;

    @Column(name = "ishide")
    public boolean is_hide;

    AppModel appModel;


    public AppModel(String app_name, String package_name, String insatll_date, String update_date, boolean is_install, int server_sync, boolean is_hide, Drawable icon) {

        //this.id=id;
        this.app_name = app_name;
        this.package_name = package_name;
        this.insatll_date = insatll_date;
        this.update_date = update_date;
        this.is_install = is_install;
        this.server_sync = server_sync;
        this.is_hide = is_hide;
        // this.is_found=is_found;
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

    public String getInsatll_date() {
        return insatll_date;
    }

    public void setInsatll_date(String insatll_date) {
        this.insatll_date = insatll_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public boolean isIs_install() {
        return is_install;
    }

    public void setIs_install(boolean is_install) {
        this.is_install = is_install;
    }

    public int isServer_sync() {
        return server_sync;
    }

    public void setServer_sync(int server_sync) {
        this.server_sync = server_sync;
    }

    public boolean isIs_hide() {
        return is_hide;
    }

    public void setIs_hide(boolean is_hide) {
        this.is_hide = is_hide;
    }

    public AppModel findDataBypackage(String package_name) {
        appModel = new Select().from(AppModel.class).where(package_names, package_name).executeSingle();
        return appModel;

    }

    public void findPackageNameAndHideAndShow(String package_name, boolean is_hide) {
         Log.d("imageview","hhide in method databse: ");
         AppModel appModel1 = new Select().from(AppModel.class).where(package_names, package_name).executeSingle();
         appModel1.is_hide = is_hide;
         appModel1.save();
         Log.d("image view tag: "," updated value is  :  "+ appModel1.is_hide+  appModel1.package_name);
         Log.d("image view tag: "," updated value is  :  "+ is_hide  +   package_name);
    }

    public void findServerSyncStatus(String package_name, int is_server_sync) {
        AppModel appModel1 = new Select().from(AppModel.class).where(package_names, package_name).executeSingle();
        appModel1.server_sync = is_server_sync;
        appModel1.save();
        Log.d("image view tag: "," updated value is  :  "+ appModel1.server_sync+  appModel1.package_name);
        Log.d("image view tag: "," updated value is  :  "+ server_sync  +   package_name);

    }
    private List<AppModel> getData() {
        List<AppModel> getappModel = new Select().from(AppModel.class).execute();

        return getappModel;


    }

    private void getDatas() {
        List<AppModel> getappModel = new Select().from(AppModel.class).execute();
        Log.d(TAG, "for loop");
        if (getappModel.size() > 0) {
            for (int i = 0; getappModel.size() > i; i++) {
                Log.d(TAG, "application name : " + getappModel.get(i).app_name);
                Log.d(TAG, "Package name : " + getappModel.get(i).package_name);

            }
        } else {
            Log.d(TAG, "empty database");
        }
        //getData();
    }

    public void deleteAll() {
        Log.d(TAG, "in delete all method");
        new Delete().from(AppModel.class).execute();

    }



    public void deleteAppByPackage(String package_name) {

         new Delete().from(AppModel.class).where(package_names, package_name).execute();
         LogUtil.debug("your deleted app is in method :  " + package_name);

    }
}
