package app.appified.Config;

import android.arch.persistence.room.Dao;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class AppPreferences {

    private static AppPreferences instance;
    private static String USER_TOKEN="TOKEN";
    private static String USER_ID="USER_ID";
    private static String USER_NAME="USER_NAME";
    private static String USER_PROFILE="USER_PROFILE";
    private static String USER_EMAIL="USER_EMAIL";
    private static String USER_MOBILE="USER_MOBILE";
    private static String USER_FRIENDS = "USER_FRIENDS";
    private static String USER_FLAG="USER_FLAG";
    private static String USER_FLAG_DIALOG="USER_FLAG_DIALOG";
    private static String APP_BASE_URL = "APP_BASE_URL";
    private static String DEVICE_ID="DEVICE_ID";
    private static String SYNCFLAG="SYNC";
    private static String IS_PRESENT="IS_PRESENT";
    private static String IS_USER_ACTION="IS_USER_ACTION";
    private static String SORTING_FILTER="FILTER";
    public static AppPreferences init(Context context) {
        if (instance == null) {
            instance = new AppPreferences(context);
        }
        return instance;
    }


    public Context context;
    protected SharedPreferences sharedPreferences;


    public AppPreferences(Context context) {
        super();
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }


    private void setString(String key,String value) {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    private void setInteger(String key,int value) {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    private void setFloat(String key,float value)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putFloat(key,value);
        editor.apply();
    }



    public  String getUserEmail() {
        return getString(USER_EMAIL);
    }

    public  void setUserEmail(String userEmail) {
        setString(USER_EMAIL,userEmail);
    }

    public  String getUserMobile() {
        return  getString(USER_MOBILE);
    }

    public  void setUserMobile(String userMobile) {
        setString(USER_MOBILE,userMobile);
    }

    public  String getUserFriends() {
        return getString(USER_FRIENDS);
    }

    public void setUserFriends(String userFriends) {
        setString(USER_FRIENDS,userFriends);
    }



    public void setUserAppStatusFlag(int userFlag) {
        setInteger(USER_FLAG,userFlag);
    }

    public  boolean getSYNCFLAG() {
        return getBoolean(SYNCFLAG);
    }

    public void setSYNCFLAG(boolean SYNC) {
        setBoolean(SYNCFLAG ,SYNC);
    }

    public int getUserAppStatusFlag() {
        return getInteger(USER_FLAG);
    }

    public void setIsShowDialog(boolean userFlag) {
        setBoolean(USER_FLAG_DIALOG,userFlag);
    }


    public void setIsPresent(boolean isPresent)
    {
        setBoolean(IS_PRESENT,isPresent);
    }

    public boolean getIsPresent()
    {
        return getBoolean(IS_PRESENT);
    }

    public boolean isShowDialog() {
        return getBoolean(USER_FLAG_DIALOG);
    }

    public  String getUserName() {
    return    getString(USER_NAME);
    }

    public void setUserName(String userName) {
        setString(USER_NAME,userName);
    }

    private void setLong(String key, long value)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong(key,value);
    }

    private void setBoolean(String key,boolean value)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public  int getSortingFilter() {
        return getInteger(SORTING_FILTER);
    }

    public  void setSortingFilter(int sortingFilter) {
        setInteger(SORTING_FILTER , sortingFilter);
    }


    private int getInteger(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    private String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    private boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    private Long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    private Float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }


    public  String getUserProfile() {
        return getString(USER_PROFILE) ;
    }

    public  void setUserProfile(String userProfile) {
       setString(USER_PROFILE,userProfile) ;
    }

    public  String getUserToken() {
         return getString(USER_TOKEN);


    }

    public  int getIsUserAction() {
        return getInteger(IS_USER_ACTION);
    }

    public  void setIsUserAction(int isUserAction) {
        setInteger(IS_USER_ACTION,isUserAction);
    }

    public  String getAppBaseUrl() {
        return getString(APP_BASE_URL);
    }

    public  void setAppBaseUrl(String appBaseUrl) {
       setString(APP_BASE_URL,appBaseUrl);
    }

    public void clearAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        setString(USER_TOKEN, null);
        editor.apply();
    }

    public void clearGetPresent() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        setString(IS_PRESENT, null);
        editor.apply();
    }




    public  String getDeviceId() {
      return getString(DEVICE_ID);
    }

    public  void setDeviceId(String deviceId) {
       setString(DEVICE_ID,deviceId);
    }

    public void setUserToken(String userToken) {

         setString(USER_TOKEN,userToken);


       // USER_TOKEN = userToken;
    }

    public  String getUserId() {
        return getString(USER_ID);
    }

    public  void setUserId(String userId) {
        setString(USER_ID,userId);
        Log.d("my activity",userId);
    }
}
