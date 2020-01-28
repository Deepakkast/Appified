package app.appified.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import app.appified.AddUserAppsInfo;
import app.appified.Config.App;
import app.appified.Database.AppModel;
import app.appified.Utils.CommonUtils;
import app.appified.Utils.LogUtil;
import app.appified.type.AppData;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public  class ButtonReciver extends BroadcastReceiver {
    private static long TIMEOUT_SECONDS = 2;
    private static long READ_TIMEOUT_SECONDS = 2;
    private static final String TAG = "MyActivity";
    public ApolloClient apolloClient;


    @Override
    public void onReceive(Context context, Intent intent) {

        AppModel appModel = new AppModel();
        LogUtil.debug("you are in button reciver");
        Intent intent1 = new Intent();
        int notification_id = intent1.getIntExtra("notification_id", 123);
        String packageName = intent.getStringExtra("packagename");
        LogUtil.debug("package empty :" + packageName);
        NotificationManagerCompat.from(context).cancel(notification_id);
        String action = intent.getAction();
        LogUtil.debug("packagename is : " + packageName);
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = App.getAppContext().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            String package_name = packageInfo.packageName;
            String install_date = CommonUtils.getOnlyDateToString(packageInfo.firstInstallTime);
            String last_updated = CommonUtils.getOnlyDateToString(packageInfo.lastUpdateTime);
            String version = packageInfo.versionName;




            if (action != null) {
                if (action.equals("HIDE")) {
                    appModel.setApp_name(appName);
                    appModel.setPackage_name(package_name);
                    appModel.setInsatll_date(install_date);
                    appModel.setUpdate_date(last_updated);
                    appModel.save();
                    appModel.setIs_hide(true);
                    appModel.save();
                    LogUtil.debug("you pressed hide thank you");
                    AppData single_app = AppData.builder().appName(appName).packageName(package_name).isHide(appModel.is_hide).installedAt(install_date).lastUpdatedAt(last_updated).isInstallation(true).version(version).build();
                    onHideUnideClick(true, single_app);
                } else {
                    appModel.setApp_name(appName);
                    appModel.setPackage_name(package_name);
                    appModel.setInsatll_date(install_date);
                    appModel.setUpdate_date(last_updated);
                    appModel.save();
                    appModel.setIs_hide(false);
                    appModel.save();
                    AppData single_app = AppData.builder().appName(appName).packageName(package_name).isHide(appModel.is_hide).installedAt(install_date).lastUpdatedAt(last_updated).isInstallation(true).version(version).build();
                    onHideUnideClick(false, single_app);
                    LogUtil.debug("you pressed unhide");
                }
            }
        }

    }
        public void onHideUnideClick(boolean hideUnhide,AppData single_app) {
        final OkHttpClient httpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.MINUTES)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.MINUTES)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                        Log.d(TAG, "in okhttp");
                        return chain
                                .proceed(chain.request().newBuilder().addHeader("Authorization", App.getPreferences().getUserToken()).build());
                    }
                }).build();

        Log.d(TAG, "in apollo client");
        apolloClient = ApolloClient.builder()
                .serverUrl("http://192.168.100.19:4000/graphql")
                .okHttpClient(httpClient)
                .build();
        Log.d(TAG, "in apollo client url");
        apolloClient.mutate(AddUserAppsInfo.builder().user_app(single_app).app_trigger(hideUnhide).build()).enqueue(new ApolloCall.Callback<AddUserAppsInfo.Data>() {
            @Override
            public void onResponse(@Nonnull Response<AddUserAppsInfo.Data> response) {

                LogUtil.debug("your app is installed");

            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            LogUtil.debug("you are in failure");

            }

            });

    }

}
