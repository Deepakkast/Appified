package app.appified.Config;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.apollographql.apollo.ApolloClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import app.appified.Database.AppModel;
import app.appified.R;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static app.appified.Utils.CommonUtils.isDebug;

public class App extends Application {
    private static App instance;
    public static AppPreferences appPreferences;
    public ApolloClient apolloClient;
    public static boolean isDebug = false;
    private static long TIMEOUT_SECONDS = 10;
    private static long READ_TIMEOUT_SECONDS = 10;
    private static final String TAG = "MyActivity";
    private static final String BASE_URL = "https://appified.appspot.com/graphql";
    private static final String Local_UR="http://192.168.100.19:4000/graphql";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appPreferences = AppPreferences.init(this);

        Configuration dbConfiguration = new Configuration.Builder(this)
                .setDatabaseName("MyDb.db").addModelClass(AppModel.class).create();
        ActiveAndroid.initialize(dbConfiguration);
    }

    public static synchronized AppPreferences getPreferences() {
        return appPreferences;
    }


    public ApolloClient getApolloClient() {
        if (apolloClient == null) {
            if (App.getPreferences().getUserToken()!=null) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                final OkHttpClient httpClient = new OkHttpClient()
                        .newBuilder()
                        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                        .addInterceptor(interceptor)
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                                Log.d(TAG, "in okhttp");
                                return chain.proceed(chain.request().newBuilder().addHeader("Authorization", App.appPreferences.getUserToken()).build());
                            }
                        }).build();

                Log.d(TAG, "in apollo client");
                apolloClient = ApolloClient.builder()
                        .serverUrl(BASE_URL)
                        .okHttpClient(httpClient)
                        .build();
            }else {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                final OkHttpClient httpClient = new OkHttpClient()
                        .newBuilder()
                        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                        .addInterceptor(interceptor)
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                                Log.d(TAG, "in okhttp");
                                return chain.proceed(chain.request().newBuilder().build());
                            }
                        }).build();

                Log.d(TAG, "in apollo client");
                apolloClient = ApolloClient.builder()
                        .serverUrl(BASE_URL)
                        .okHttpClient(httpClient)
                        .build();
            }
        }
        return apolloClient;
    }

    public static synchronized App getAppContext() {
        return instance;
    }

    public void refreshApolloClient(){
        apolloClient=null;
    }

}