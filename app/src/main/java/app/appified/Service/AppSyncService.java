package app.appified.Service;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
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
import app.appified.Utils.LogUtil;
import app.appified.modelclass.AppSyncDTO;
import app.appified.type.AppData;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
public class AppSyncService extends Service {

    private static final String BASE_URL = "https://appified.appspot.com/graphql";
    private static final String Local_UR="http://192.168.100.19:4000/graphql";

    private static final String TAG = "MyActivity";
    List<PackageInfo> packageInfos;
    public ApolloClient apolloClient;
    private static long TIMEOUT_SECONDS = 2;
    private static long READ_TIMEOUT_SECONDS = 2;
    List<AppModel> appModelList = new ArrayList<>();
    ArrayList<AppSyncDTO> syncDTOMutableList = new ArrayList<>();
    public  String device_id;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Inside Service");
          syncWithServer();
     return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void syncWithServer() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient httpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.MINUTES)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.MINUTES)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                        return chain
                                .proceed(chain.request().newBuilder().addHeader("Authorization", App.getPreferences().getUserToken()).build());
                    }
                }).build();
        LogUtil.debug(App.getPreferences().getUserToken());
        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(httpClient)
                .build();

        appModelList = new Select().from(AppModel.class).execute();
        final List<AppData> abc = new ArrayList<AppData>();

        try {

            List<AppModel> getappModel = new Select().from(AppModel.class).execute();

            for (int i = 0; getappModel.size() > i; i++) {
                abc.add(AppData.builder()
                        .appName(getappModel.get(i).getApp_name())
                        .packageName(getappModel.get(i).getPackage_name())
                        .installedAt(getappModel.get(i).getInsatll_date())
                        .lastUpdatedAt(getappModel.get(i).getUpdate_date())
                        .isHide(getappModel.get(i).isIs_hide())
                        .build());
                Log.d(TAG, String.valueOf(abc.get(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            }
        for (int j = 0; j < abc.size(); j++) {
            LogUtil.debug("sync app is : " + abc.get(j).appName());
            final int finalJ = j;
            apolloClient.mutate(AddUserAppsInfo.builder().user_app(abc.get(j)).build())
                    .enqueue(new ApolloCall.Callback<AddUserAppsInfo.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<AddUserAppsInfo.Data> response) {
                            if (response.data()!=null) {
//                                Log.d(TAG, "respoce is : " + response.data().appSync().message());
                                 LogUtil.debug("sync app name is : " + abc.get(finalJ).appName());
                                 LogUtil.debug("out of for loop");
                                 /*for (int i = 0; i < appModelList.size(); i++) {
                                     LogUtil.debug("in for loop");
                                    // appModelList.get(i).findServerSyncStatus(abc.get(finalJ).packageName(), true);
                                     LogUtil.debug("server sync is : " +appModelList.get(i).isServer_sync());
                                 }*/
                                 //LogUtil.debug("sync app is pack name : : " +response.data().appSync().packageName());
                                 }
                        }
                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            Log.d(TAG, "in failure");
                        }
                    });
        }
    }
    public void deleteAll() {
        Log.d(TAG, "in delete all method");
        new Delete().from(AppModel.class).execute();
        }




}
