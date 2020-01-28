package app.appified.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import app.appified.AddUserAppsInfo;
import app.appified.Config.App;
import app.appified.Database.AppModel;
import app.appified.R;
import app.appified.Service.AppInstallBroadcastReciver;
import app.appified.Utils.CommonUtils;
import app.appified.Utils.LogUtil;
import app.appified.modelclass.AppSyncDTO;
import app.appified.type.AppData;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private static long TIMEOUT_SECONDS = 2;
    private static long READ_TIMEOUT_SECONDS = 2;
    private final int SPLASH_DISPLAY_LENGTH = 500;
    public ApolloClient apolloClient;
    List<PackageInfo> packageInfos;
    ArrayList<AppSyncDTO> syncDTOMutableList = new ArrayList<>();
    private Context context;
    private static final String BASE_URL = "https://appified.appspot.com/graphql";
    private static final String Local_UR="http://192.168.100.19:4000/graphql";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LogUtil.debug("enter in on create method : ");
        context = this;


        //checking is user present or not

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtil.debug("enter in run : ");
                if (App.getPreferences().getUserToken() != null) {
                    Intent intent = new Intent(SplashActivity.this, AppScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                  getAppInfo();
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }



    //getting all application information

    private void getAppInfo() {
        LogUtil.debug("enter in getinfo : ");
        PackageManager packageManager = getApplication().getPackageManager();
        packageInfos = packageManager.getInstalledPackages(packageManager.GET_META_DATA);
        for (PackageInfo packageInfo : packageInfos) {
            //skip own app
            if (getPackageName().equals(packageInfo.packageName)) {
                continue;
            }
            try {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    LogUtil.debug("enter in get data of application : ");
                    String app_name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                    String package_name = packageInfo.packageName;
                    String install_date = String.valueOf(packageInfo.firstInstallTime);
                    String last_updated = CommonUtils.getOnlyDateToString(packageInfo.lastUpdateTime);
                    String version = packageInfo.versionName;
                    Drawable app_icon = packageInfo.applicationInfo.loadIcon(packageManager);
                    syncDTOMutableList.add(new AppSyncDTO(app_name, package_name, install_date, last_updated, false, app_icon));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        storeInDb();
    }

    private void storeInDb() {
        for (AppSyncDTO dto : syncDTOMutableList) {
            LogUtil.debug("enter in store db : ");
            AppModel app_model = new AppModel();
            String pack = dto.package_name;
            AppModel packagenameindata = app_model.findDataBypackage(pack);
            if (packagenameindata == null) {
                app_model.setApp_name(dto.apps_name);
                app_model.setPackage_name(dto.package_name);
                app_model.setInsatll_date(dto.install_date);
                app_model.setUpdate_date(dto.last_updated_date);

                //server sync 0 means that particular app decision is pending and it goes on let me select
                //server sync 1 means user take decision on that particular app
                if (App.getPreferences().getUserAppStatusFlag() == 1) {
                    app_model.setServer_sync(App.getPreferences().getUserAppStatusFlag());
                    app_model.setIs_hide(true);
                    app_model.setServer_sync(0);
                } else if (App.getPreferences().getUserAppStatusFlag() == 2) {
                    app_model.setServer_sync(1);
                    app_model.setIs_hide(false);
                } else if (App.getPreferences().getUserAppStatusFlag() == 3) {
                    app_model.setServer_sync(1);
                    app_model.setIs_hide(true);
                }
                app_model.save();
                AppData single_app = AppData.builder().appName(app_model.app_name).packageName(app_model.package_name).isHide(app_model.is_hide).installedAt(app_model.insatll_date).lastUpdatedAt(app_model.update_date).isInstallation(true).build();

                newInstallApp(app_model.is_hide, single_app);
            }
        }
    }



    //if any new app found then send to server

    public void newInstallApp(boolean visiblity, AppData single_app) {
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
                .serverUrl(BASE_URL)
                .okHttpClient(httpClient)
                .build();
        Log.d(TAG, "in apollo client url");
        apolloClient.mutate(AddUserAppsInfo.builder().user_app(single_app).app_trigger(visiblity).build()).enqueue(new ApolloCall.Callback<AddUserAppsInfo.Data>() {
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


    @Override
    protected void onStart() {
        LogUtil.debug("enter in on start method : ");
        super.onStart();


        IntentFilter installIntent = new IntentFilter();
        installIntent.addAction(Intent.ACTION_PACKAGE_ADDED);
        installIntent.addDataScheme("package");
        registerReceiver(new AppInstallBroadcastReciver(), installIntent);

    }
}
