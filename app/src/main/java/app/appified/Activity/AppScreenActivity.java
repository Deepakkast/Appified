package app.appified.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import app.appified.Config.App;
import app.appified.Database.AppModel;
import app.appified.Fregment.FriendAppFragment;
import app.appified.Fregment.PagfFragment;
import app.appified.Fregment.AppUsageFragment;
import app.appified.Fregment.UserAppFragment;
import app.appified.R;
import app.appified.Service.AppUnInstallBroadcastReceiver;

public class AppScreenActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    Fragment fragment;
    Context context;
    ImageView profile;
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    TextView toolbartitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        context=this;
        profile=findViewById(R.id.image_view2);


        // setting tab, tab title, tab title text color and tab name on back pressed
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof UserAppFragment){
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                    toolbartitle.setText("My Apps");
                    toolbartitle.setTextColor(Color.parseColor("#000000"));
                }else if (currentFragment instanceof FriendAppFragment){
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                    toolbartitle.setText("Friends");
                    toolbartitle.setTextColor(Color.parseColor("#000000"));
                }else if (currentFragment instanceof PagfFragment){
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    toolbartitle.setText("Paid Apps Goes Free");
                    toolbartitle.setTextColor(Color.parseColor("#ff0000"));
                }else if (currentFragment instanceof AppUsageFragment){
                    bottomNavigationView.getMenu().getItem(3).setChecked(true);
                    toolbartitle.setText("Usage");
                    toolbartitle.setTextColor(Color.parseColor("#000000"));
                }
            }
        });
        Picasso.get().load(App.getPreferences().getUserProfile()).into(profile);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }

        toolbartitle = findViewById(R.id.toolbar_title2);
        bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//          for first time loading of fragment
         toolbartitle.setText("Your App");
         fragment = new UserAppFragment();
         loadFirstFragment(fragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.userapp:
                        toolbartitle.setText("My Apps");
                        fragment = new UserAppFragment();
                        loadFirstFragment(fragment);
                        toolbartitle.setTextColor(Color.parseColor("#000000"));
                        return true;
                    case R.id.friendapp:
                        toolbartitle.setText("Friends ");
                        fragment = new FriendAppFragment();
                        loadFragment(fragment);
                        toolbartitle.setTextColor(Color.parseColor("#000000"));
                        return true;
                    case R.id.pagf:
                        toolbartitle.setText("Paid Apps Goes Free");
                        fragment = new PagfFragment();
                        loadFragment(fragment);
                        toolbartitle.setTextColor(Color.parseColor("#ff0000"));
                        return true;
                    case R.id.tab_usage:
                        toolbartitle.setText("Usage");
                        fragment = new AppUsageFragment();
                        loadFragment(fragment);
                        toolbartitle.setTextColor(Color.parseColor("#000000"));
                        return true;
                }
                return false;
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AppScreenActivity.class.getSimpleName(), "onClick: profile click");
                Context wrapper = new ContextThemeWrapper(context, R.style.PopupMenuTheme);
                PopupMenu popupMenu = new PopupMenu(wrapper, v);
                popupMenu.getMenu().add("Settings");
                popupMenu.getMenu().add("Logout");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString()) {
                            case "Logout":{

                                //on logout clear all shared preference
                                //clear all local database
                                LoginManager.getInstance().logOut();
                                Intent intent=new Intent(AppScreenActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                App.getPreferences().clearAll();
                                AppModel appModel=new AppModel();
                                appModel.deleteAll();
                                startActivity(intent);
                                break;}
                            case "Settings" :{
                                Intent intent = new Intent(AppScreenActivity.this, AppHideUnhide.class);
                                startActivity(intent);
                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        // calling method for notification of app which is not used more than a month
        AppUnInstallNotification();
    }
    public void setActionBarTitle(String title) {
        toolbartitle.setText(title);
    }

    // for loading fragments except 1st fragment
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    //for loading first fragment
    private void loadFirstFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
    // trigger notification if app not used more than a month
    public void AppUnInstallNotification(){
        Log.d("MyActivity", "Alarm On");
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,10);  //alarmTimePicker.getCurrentHour()
        calendar.set(Calendar.MINUTE, 01);
        calendar.set(Calendar.SECOND,01);
        Intent myIntent = new Intent(AppScreenActivity.this, AppUnInstallBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(AppScreenActivity.this, 0, myIntent, 0);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);// repeat alarm working here

    }
}
