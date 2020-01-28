package app.appified.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.facebook.login.LoginManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import app.appified.AddUserAppsInfo;
import app.appified.Config.App;
import app.appified.Database.AppDatabase;
import app.appified.Database.AppModel;
import app.appified.Database.User;
import app.appified.Utils.CommonUtils;
import app.appified.modelclass.AppSyncDTO;
import app.appified.Adapter.AppsAdapter;
import app.appified.R;
import app.appified.type.AppData;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
//import type.AppData;

public class AppSyncActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    RecyclerView recyclerViewApps;
    AppsAdapter appsAdapter;
    CheckBox checkBoxSelectAllApps;
    Context context1;
    TextView appcounts, logout;
    Button btnSync;
    AppDatabase mydb = null;
    List<PackageInfo> packageInfos;
    public ApolloClient apolloClient;
    List<User> users;
    private static long TIMEOUT_SECONDS = 2;
    private static long READ_TIMEOUT_SECONDS = 2;
    List<String> stringList;
    ArrayList<AppSyncDTO> syncDTOMutableList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_sync);
        checkBoxSelectAllApps = findViewById(R.id.cbSelectAll);
        appcounts = findViewById(R.id.tvAppCounts);
        btnSync = findViewById(R.id.syncbtn);
        logout = findViewById(R.id.tvSkip);
        context1 = this;
        getAppInfo();

        //logout.logOut();
        /*logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.appPreferences.clearAll();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(AppSyncActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
        recyclerViewApps = (RecyclerView) findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AppSyncActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewApps.setLayoutManager(layoutManager);
      /*  appsAdapter = new AppsAdapter(AppSyncActivity.this, syncDTOMutableList, new AppsAdapter.Callback() {
            @Override
            public void onClickUninstall(AppSyncDTO object) {
                Log.d(TAG, "object is" + object.toString());
                int index = syncDTOMutableList.indexOf(object);
                Log.d(TAG, "Index is" + index);
                syncDTOMutableList.get(index).setIs_hide(!object.is_hide);
                if (!object.is_hide){
                    checkBoxSelectAllApps.setChecked(false);
                }
                appcount();

                for (int i=0;i<syncDTOMutableList.size();i++)
                {
                   if (!object.is_hide)
                   {
                       checkBoxSelectAllApps.setChecked(true);
                   }
                }
            }
        });
      */
        recyclerViewApps.setAdapter(appsAdapter);
        appsAdapter.notifyDataSetChanged();

        checkBoxSelectAllApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < syncDTOMutableList.size(); i++)
                    syncDTOMutableList.get(i).setIs_hide(checkBoxSelectAllApps.isChecked());
                appsAdapter.notifyDataSetChanged();
                appcount();

            }
        });

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectone()) {
                    syncWithServer();
                    }
                    else {
                    Log.d(TAG, " please select UserApp ");
                }
            }
        });
    }

    private void getAppInfo() {
        PackageManager packageManager = getApplication().getPackageManager();
        packageInfos = packageManager.getInstalledPackages(packageManager.GET_META_DATA);
        for (PackageInfo packageInfo : packageInfos) {


            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {

                String app_name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                Log.d(TAG, "UserApp name" + app_name);
                // Log.d(TAG, app_name);
                String package_name = packageInfo.packageName;
                String install_date = CommonUtils.getOnlyDateToString(packageInfo.firstInstallTime);
                String last_updated = CommonUtils.getOnlyDateToString(packageInfo.lastUpdateTime);
                String version = packageInfo.versionName;
                Drawable app_icon = packageInfo.applicationInfo.loadIcon(packageManager);
                syncDTOMutableList.add(new AppSyncDTO(app_name, package_name, install_date, last_updated, false, app_icon));

            }
        }
        //deleteAll();
        storeInDb();
        Log.d(TAG, "before get data");
        //     getData();
    }

    private void getData() {
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

    private void appcount() {
        int count = 0;
        for (AppSyncDTO app : syncDTOMutableList) {
            if (app.is_hide) {
                count++;
            }
        }
        if (syncDTOMutableList.size() <= 0) {
            appcounts.setText("0/0");
        } else {
            Log.d(TAG, "total" + count);
            appcounts.setText(String.format("%s/%s", String.valueOf(count), String.valueOf(syncDTOMutableList.size())));
        }
        //recyclerViewApps.setAdapter();

    }


    private void syncWithServer() {
//        Log.d(TAG, App.appPreferences.getUserToken());
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient httpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.MINUTES)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.MINUTES)
                //.//addInterceptor(logging)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                        Log.d(TAG, "in okhttp");
                        return chain
                                .proceed(chain.request().newBuilder().addHeader("Authorization", App.appPreferences.getUserToken()).build());
                    }
                }).build();

        Log.d(TAG, "in apollo client");
        apolloClient = ApolloClient.builder()
                .serverUrl("http://192.168.100.19:4000/graphql")
                .okHttpClient(httpClient)
                .build();
        Log.d(TAG, "in apollo client url");

        List<AppData> abc = new ArrayList<AppData>();

        try {

            List<AppModel> getappModel = new Select().from(AppModel.class).execute();
            //new Delete().from(AppModel.class).execute();
            for (int i = 0; getappModel.size() > i; i++) {
                Log.d(TAG, "in server call" + getappModel.get(i).app_name);

                abc.add(AppData.builder()
                        .appName(getappModel.get(i).getApp_name())
                        .packageName(getappModel.get(i).getPackage_name())
                        .installedAt(getappModel.get(i).getInsatll_date())
                        .lastUpdatedAt(getappModel.get(i).getUpdate_date())
                        .build());
                Log.d(TAG, String.valueOf(abc.get(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();

        }


        for (int j = 0; j < abc.size(); j++) {

            apolloClient.mutate(AddUserAppsInfo.builder().user_app(abc.get(j)).build())
                    .enqueue(new ApolloCall.Callback<AddUserAppsInfo.Data>() {
                        @Override

                        public void onResponse(@Nonnull Response<AddUserAppsInfo.Data> response) {

                            //Log.d(TAG, "respoce is : " + response.data().appSync().message());



                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {

                            Log.d(TAG, "in failure");


                        }
                    });

        }
    }


    private void storeInDb() {
        deleteAll();
//        getData();
        for (AppSyncDTO dto : syncDTOMutableList) {
            //Log.d(TAG, dto.toString());
            AppModel app_model = new AppModel();
            String pack = dto.package_name;
            AppModel packagenameindata = app_model.findDataBypackage(pack);
            if (packagenameindata == null) {
                app_model.setApp_name(dto.apps_name);
                app_model.setPackage_name(dto.package_name);
                app_model.setInsatll_date(dto.install_date);
                app_model.setUpdate_date(dto.last_updated_date);
                app_model.setIs_hide(dto.is_hide);
                app_model.save();
                // deleteAll();
                Log.d(TAG, "user info is :" + app_model.app_name);
            } else {
                Log.d(TAG, "Apps already in db you can not insert  found in database");
            }
        }

    }


    private boolean isSelectone() {
        if (!syncDTOMutableList.isEmpty()) {

            for (AppSyncDTO app : syncDTOMutableList) {
                if (app.is_hide) {
                    return true;
                }
            }

        }
        return false;
    }

    public static void deleteAll() {
        Log.d(TAG, "in delete all method");
        new Delete().from(AppModel.class).execute();

    }


}
