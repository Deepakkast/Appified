package app.appified.Config;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import app.appified.AddUserAppsInfo;
import app.appified.AppDetail;
import app.appified.AppHide;
import app.appified.AppUninstall;
import app.appified.CreateNewUser;
import app.appified.FcmToken;
import app.appified.FriendApps;
import app.appified.FriendList;
import app.appified.GetPagfData;
import app.appified.Utils.LogUtil;
import app.appified.modelclass.DeviceidDto;
import app.appified.modelclass.FbFriendDTO;
import app.appified.modelclass.FriendDetailDto;
import app.appified.modelclass.UserApp;
import app.appified.type.AppData;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class ApolloClientService {
    private static long TIMEOUT_SECONDS = 1;
    private static long READ_TIMEOUT_SECONDS =1;
    private static final String TAG = "MyActivity";
    private static final String BASE_URL = "https://appified.appspot.com/graphql";
    private static final String Local_UR="http://192.168.100.19:4000/graphql";



    public static void createUser(String fb_id, String name, String email, String profile_url, String mobile,
                                  ArrayList<FbFriendDTO> friendlist, String device_id, boolean isloginManual, final OnRequestComplete requestComplete) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        ApolloClient apolloClient;
        final OkHttpClient httpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.MINUTES)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.MINUTES)
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
        final List<String> abc = new ArrayList<String>();
        if (friendlist != null) {
            for (int i = 0; friendlist.size() > i; i++) {
                abc.add(friendlist.get(i).getId());
                Log.d(TAG, "frends id is :" + abc);
            }
        }
        apolloClient.mutate(CreateNewUser.builder().user_name(name).user_email(email).profile_pic(email).fb_id(fb_id).friend_List(abc).profile_pic(profile_url).user_mobile(mobile).deviceId(device_id).isLoginManual(isloginManual).build()).enqueue(new ApolloCall.Callback<CreateNewUser.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<CreateNewUser.Data> response) {

                if (!response.hasErrors()) {
                    try {
                        Log.d(TAG, "after send data");
                        Log.d(TAG, response.data().createUser().userName());
                        Log.d(TAG, response.data().createUser().token());
                        App.getPreferences().setUserToken(response.data().createUser().token());
                        App.getPreferences().setUserId(response.data().createUser()._id());
                        App.getPreferences().setIsPresent(response.data().createUser().isPresent());

                        Log.d(TAG, "login responce" + response.data().createUser());
                        LogUtil.debug("user token is : " + App.getPreferences().getUserToken());
                        requestComplete.onSuccess(response.data());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        requestComplete.onFailure("Error ",700);
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                e.printStackTrace();
                requestComplete.onFailure("Exception", 700);
            }
        });
        }

    public static void  isHide(String packageName,boolean ishide,final OnRequestComplete onRequestComplete)
    {
        ApolloClient apolloClient = App.getAppContext().getApolloClient();
        apolloClient.mutate(AppHide.builder().packageName(packageName).isHide(ishide).build()).enqueue(new ApolloCall.Callback<AppHide.Data>() {
            @Override
            public void onResponse(@Nonnull Response<AppHide.Data> response) {
                LogUtil.debug("MSG:" + response.toString());
                try {
                    if (response.data().hide_Unhide().message()!=null)
                    {
                        Log.d("TAG","Sucessfully hide : " +response.data().hide_Unhide().message() );
                    }
                    onRequestComplete.onSuccess(response.data());

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                }

            @Override
            public void onFailure(@Nonnull ApolloException e) {


                Log.d("TAG","App hide is in failure  :"   );
            }
        });
    }


    public static void isuninstall(String packagename,final OnRequestComplete onRequestComplete)
    {
        ApolloClient apolloClient = App.getAppContext().getApolloClient();
        apolloClient.mutate(AppUninstall.builder().packageName(packagename).build()).enqueue(new ApolloCall.Callback<AppUninstall.Data>() {
            @Override
            public void onResponse(@Nonnull Response<AppUninstall.Data> response) {
                if (response.data()!=null)
                {
                    if (response.data().unInstall()!=null)
                      if (response.data().unInstall().message()!=null)
                     {
                      Log.d(TAG,"your appUnistall is in sucess : "+response.data().unInstall().message());
                }}onRequestComplete.onSuccess(response.data());


            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.d(TAG,"your appUnistall is in failure : ");
            }
        });
    }



    public static void firebaseToken(String token,String deviceId,final  OnRequestComplete onRequestComplete)
    {
        try {

        String device_id;
        device_id = Settings.Secure.getString(App.getAppContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
            ApolloClient apolloClient = App.getAppContext().getApolloClient();
            App.getPreferences().setDeviceId(device_id);
            apolloClient.mutate(FcmToken.builder().deviceId(App.getPreferences().getDeviceId()).fcmToken(token).build()).enqueue(new ApolloCall.Callback<FcmToken.Data>() {
            @Override
            public void onResponse(@Nonnull Response<FcmToken.Data> response) {
                if (response.data().fcm() != null) {
                    Log.d(TAG, "token send sucess : " + response.data().fcm().message());
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.d(TAG, "fcm in failure");

            }

        });



     }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void appDetail(String packagename,final OnRequestComplete onRequestComplete)
    {

        ApolloClient apolloClient = App.getAppContext().getApolloClient();
        apolloClient.query(AppDetail.builder().packageName(packagename).build()).enqueue(new ApolloCall.Callback<AppDetail.Data>() {
            @Override
            public void onResponse(@Nonnull Response<AppDetail.Data> response) {
                onRequestComplete.onSuccess(response.data());

                onRequestComplete.onFailure("error ",700);
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

                onRequestComplete.onFailure("error ",700);
            }
        });

    }

    public static void friendApps(String user_id, final OnRequestComplete onRequestComplete)
    {

        ApolloClient apolloClient = App.getAppContext().getApolloClient();
        apolloClient.query(FriendApps.builder().userId(user_id).build()).enqueue(new ApolloCall.Callback<FriendApps.Data>() {
            @Override
            public void onResponse(@Nonnull Response<FriendApps.Data> response) {


                if (response.data()!=null) {
                    onRequestComplete.onSuccess(response.data());
                }onRequestComplete.onFailure("error",500);

                }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                onRequestComplete.onFailure("error msg",401);

            }
        });

    }


    public static void friendList(String user_id,final OnRequestComplete onRequestComplete)
    {
//        final List<FriendDetailDto>detailDtos=new ArrayList<>();
        ApolloClient apolloClient = App.getAppContext().getApolloClient();
        if(user_id!=null) {
            apolloClient.query(FriendList.builder().userId(user_id).build()).enqueue(new ApolloCall.Callback<FriendList.Data>() {
                @Override
                public void onResponse(@Nonnull Response<FriendList.Data> response) {
                    if (response.data() != null) {
                        onRequestComplete.onSuccess(response.data());
                    } else onRequestComplete.onFailure("Error in response", 500);


                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    onRequestComplete.onFailure("Error msg", 401);
                }
            });

        }
        else {
            LogUtil.debug("your user id may be null");
        }

    }


    public static void syncServer(AppData userapp,final OnRequestComplete onRequestComplete)

    {
        ApolloClient apolloClient=App.getAppContext().getApolloClient();
        apolloClient.mutate(AddUserAppsInfo.builder().user_app(userapp).build()).enqueue(new ApolloCall.Callback<AddUserAppsInfo.Data>() {
            @Override
            public void onResponse(@Nonnull Response<AddUserAppsInfo.Data> response) {
                if (response.data()!=null)
                {
                    onRequestComplete.onSuccess(response.data());
                }onRequestComplete.onFailure("error msg",500);
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
               onRequestComplete.onFailure("error msg",401);
            }
        });
    }


    public static void getPagfData(String user_id,final OnRequestComplete onRequestComplete)
    {
        ApolloClient apolloClient=App.getAppContext().getApolloClient();
        apolloClient.query(GetPagfData.builder().userId(user_id).build()).enqueue(new ApolloCall.Callback<GetPagfData.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GetPagfData.Data> response) {
                LogUtil.debug("on responce ");
                if(response.data()!=null)
                {
                    onRequestComplete.onSuccess(response.data());
                }onRequestComplete.onFailure("error message",401);
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                LogUtil.debug("onfailure");
                onRequestComplete.onFailure("error msg",401);
            }
        });
    }

    public interface OnRequestComplete<T> {
        void onSuccess(T object);
        void onFailure(String errorMessage, int errorCode);
    }

}

