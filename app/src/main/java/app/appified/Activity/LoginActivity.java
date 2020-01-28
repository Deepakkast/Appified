package app.appified.Activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import app.appified.Adapter.MpagerAdapter;
import app.appified.Config.ApolloClientService;
import app.appified.Config.App;
import app.appified.CreateNewUser;
import app.appified.Database.AppModel;
import app.appified.Utils.CommonUtils;
import app.appified.Utils.LogUtil;
import app.appified.modelclass.AppSyncDTO;
import app.appified.modelclass.FacebookWrapper;
import app.appified.R;
import app.appified.modelclass.FbFriendDTO;

public class LoginActivity extends AppCompatActivity {
    LoginButton loginButton;
    FacebookWrapper facebookWrapper;
    CallbackManager callbackManager;
    String user_id = null;
    String user_name = null;
    String user_email=null;
    String user_mobile = null;
    String user_profile = null;
    Context context;
    ArrayList<FbFriendDTO> fbFriendList;
    private MpagerAdapter mpagerAdapter;
    List<PackageInfo> packageInfos;
    ArrayList<AppSyncDTO> syncDTOMutableList = new ArrayList<>();
    String deviceid;
    private static final String TAG = "MyActivity";
    private int[] layouts = {R.layout.first_slide, R.layout.second_slide, R.layout.third_slide};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.debug("on create of login activity");
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        context = this;
        setContentView(R.layout.activity_login);

        if (App.getPreferences().getUserToken()==null) {
            getAppInfo();
        }
        loginButton = findViewById(R.id.login_button);
        ViewPager mImageViewPager = (ViewPager) findViewById(R.id.pager);
        mpagerAdapter = new MpagerAdapter(layouts, this);
        mImageViewPager.setAdapter(mpagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mImageViewPager, true);
        callbackManager = CallbackManager.Factory.create();
        Button login = findViewById(R.id.btnLoginViaFacebook);
        deviceid = Settings.Secure.getString(App.getAppContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.performClick();
            }
        });
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {

                        Log.d(TAG, "in on success");
                        String accessToken = loginResult.getAccessToken()
                                .getToken();
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {
                                        Log.d(TAG, "in on Completed");
                                        Log.d(TAG,"Facebook Object : " + object.toString());
                                        Log.d(TAG,"Facebook response : " + response.toString());


                                        try {
                                            try {
                                                facebookWrapper = new Gson().fromJson(object.toString(), FacebookWrapper.class);
                                                Log.d(TAG, "facebbokk data : " + facebookWrapper);
                                                if (null != facebookWrapper.getId()) {
                                                    user_id = facebookWrapper.getId();
                                                }
                                                if (null != facebookWrapper.getName()) {
                                                    user_name = facebookWrapper.getName();
                                                }
                                                if (null != facebookWrapper.getEmail()) {
                                                    user_email = facebookWrapper.getEmail();
                                                    //user_email=object.getString("email");
                                                    Log.d(TAG,"user email is : "+user_email);
                                                }
                                                if (null != facebookWrapper.getPicture().getData().getUrl()) {
                                                    user_profile = facebookWrapper.getPicture().getData().getUrl();
                                                    App.getPreferences().setUserProfile(user_profile);
                                                    Log.d(TAG,"profile picture url is : "+user_profile);
                                                }
                                                if (null != facebookWrapper.getFriends().getData()) {
                                                    fbFriendList = facebookWrapper.getFriends().getData();
                                                }
                                                if (null != facebookWrapper.getMobile()) {
                                                    user_mobile = facebookWrapper.getMobile();
                                                    Log.d(TAG,"user mobile  : "+user_mobile);
                                                    }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                }


                                                //if email address not found on facebook

                                            if (user_email==null) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                                builder.setTitle("Enter email address");
                                                View view = View.inflate(LoginActivity.this, R.layout.custom_dialog_email_address, null);
                                                builder.setView(view);
                                                final EditText email_id = view.findViewById(R.id.et_email);
                                                final TextInputLayout textInputLayout = view.findViewById(R.id.til_email);
                                                builder.setPositiveButton("Submit", null);
                                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                });
                                                final AlertDialog alertDialog = builder.create();
                                                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                                    @Override
                                                    public void onShow(final DialogInterface dialogInterface) {
                                                        Button button = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                                                        button.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                if (email_id.length() <= 0) {
                                                                    textInputLayout.setError("Enter email address");
                                                                } else {
                                                                    try {
                                                                        if (CommonUtils.validateEmail(email_id.getText().toString())) {
                                                                            dialogInterface.dismiss();
                                                                            user_email = email_id.getText().toString();
                                                                            sendUSerData(user_id, user_name, user_email, user_profile, user_mobile, fbFriendList,deviceid,true);
//                                                                          dialogInterface.dismiss();
                                                                        } else {
                                                                            textInputLayout.setError("Enter valid email address");
                                                                        }
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                                alertDialog.show();
                                            }else {

                                                Log.d(TAG, "apollo obbject ");
                                                sendUSerData(user_id, user_name, user_email, user_profile, user_mobile, fbFriendList,deviceid,false);
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "email,id,name,link,friends,picture.type(large)");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }



                    @Override
                    public void onCancel() {
                        Log.d("LoginActivity", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("LoginActivity", "On error");
                        Log.d("LoginActivity", exception.toString());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }




    //send all user data to server  get from facebook

    private void sendUSerData(final String userID, final String userName, final String userEmail, final String profileUrl, String userMobile, final ArrayList<FbFriendDTO> friendsDtos,String device_id, boolean isloginmanual) {
        ApolloClientService.createUser(userID, userName, userEmail, profileUrl, userMobile, friendsDtos,device_id,isloginmanual, new ApolloClientService.OnRequestComplete() {
            @Override
            public void onSuccess(final Object object) {
                try {

                    final CreateNewUser.Data data=(CreateNewUser.Data) object;

                    if (userEmail != null) {
                        App.getPreferences().setUserEmail(userEmail);
                    }
                    if (userName != null) {
                        App.getPreferences().setUserName(userName);
                    }
                    if (profileUrl != null) {
                        App.getPreferences().setUserProfile(profileUrl);
                    }
                    if (App.getPreferences().getUserToken()!=null)
                        App.getAppContext().refreshApolloClient();

                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            final String newToken = instanceIdResult.getToken();
                            Log.e("newToken : ",newToken);


                            //send fcm token to server


                            ApolloClientService.firebaseToken(newToken, null, new ApolloClientService.OnRequestComplete() {
                                @Override
                                public void onSuccess(Object object) {
                                    Log.d(TAG,"send token success :" +newToken);
                                    }

                                @Override
                                public void onFailure(String errorMessage, int errorCode) {
                                    }
                                    });

                            //if user present or not

                             if (data.createUser().isPresent()==true) {
                                 LogUtil.debug("Token is:"+App.getPreferences().getUserToken());
                                 Intent intent = new Intent(LoginActivity.this, AppScreenActivity.class);
                                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                 startActivity(intent);
                                  }

                             else {
                                   Intent intent = new Intent(LoginActivity.this, AppSelectActivity.class);
                                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                   startActivity(intent);
                                  }

                        }
                    });




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage, int errorCode) {


                Log.d(TAG, " you are in failure");

            }
        });
    }



    private void getAppInfo() {
        PackageManager packageManager = getApplication().getPackageManager();
        packageInfos = packageManager.getInstalledPackages(packageManager.GET_META_DATA);
        for (PackageInfo packageInfo : packageInfos) {
            //skip own app
            if (getPackageName().equals(packageInfo.packageName))
            {
                continue;
            }



            try {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    String app_name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                    Log.d(TAG, "UserApp name" + app_name);
                    // Log.d(TAG, app_name);
                    String package_name = packageInfo.packageName;
                    String install_date = String.valueOf(packageInfo.firstInstallTime);
                    String last_updated = CommonUtils.getOnlyDateToString(packageInfo.lastUpdateTime);
                    String version = packageInfo.versionName;
                    Drawable app_icon = packageInfo.applicationInfo.loadIcon(packageManager);
                    syncDTOMutableList.add(new AppSyncDTO(app_name, package_name, install_date, last_updated, false, app_icon));
                }

            }catch (Exception e)
            {
                Log.d(TAG,"namenotFoundException" + packageInfo.packageName );
            }
        }
        storeInDb();
        Log.d(TAG, "before get data");
    }


    private void storeInDb() {
        for (AppSyncDTO dto : syncDTOMutableList) {
            AppModel app_model = new AppModel();
            String pack = dto.package_name;
            AppModel packagenameindata = app_model.findDataBypackage(pack);
            if (packagenameindata == null) {
                app_model.setApp_name(dto.apps_name);
                app_model.setPackage_name(dto.package_name);
                app_model.setInsatll_date(dto.install_date);
                app_model.setUpdate_date(dto.last_updated_date);
                app_model.setIs_hide(false);
                app_model.setServer_sync(1);
                app_model.save();
                Log.d(TAG, "user info is :" + app_model.app_name);
            } else {
                Log.d(TAG, "Apps already in db you can not insert  found in database");
            }
        }


    }

}
