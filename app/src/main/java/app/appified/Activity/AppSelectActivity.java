package app.appified.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.ArrayList;
import java.util.List;

import app.appified.Adapter.AppSelectAdapter;
import app.appified.Config.App;
import app.appified.Database.AppModel;
import app.appified.Fregment.UserAppFragment;
import app.appified.R;
import app.appified.Service.AppSyncService;
import app.appified.Utils.CommonUtils;
import app.appified.Utils.LogUtil;
import app.appified.modelclass.AppSyncDTO;

public class AppSelectActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AppSelectAdapter adapter;
    Context context;
    static List<AppModel> appModelList = new ArrayList<>();
    Button sync;
    RadioGroup group;
    private static final String TAG = "MyActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_select);
        context = this;
        recyclerView = findViewById(R.id.recylerview);
        group = findViewById(R.id.radiogroup);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AppSelectActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        appModelList.clear();
        appModelList = new Select().from(AppModel.class).execute();
        for (int i = 0; i < appModelList.size(); i++) {
            appModelList.get(i).setIs_hide(true);

        }
        for (int i = 0; i < appModelList.size(); i++) {
            LogUtil.debug(appModelList.get(i).isIs_hide());
            LogUtil.debug(appModelList.get(i).getApp_name());
        }
        sync = findViewById(R.id.syncbtn);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.select_all) {
                    adapter.setCanCheck(false);

                    for (int i = 0; i < appModelList.size(); i++) {
                        LogUtil.debug("you are in select all");
                        appModelList.get(i).setIs_hide(true);
                        if (appModelList.get(i).is_hide==false)
                        {

                        }

                    }

                    adapter.notifyDataSetChanged();
                } else {
                    adapter.setCanCheck(true);
                    for (int i = 0; i < appModelList.size(); i++) {
                        appModelList.get(i).setIs_hide(false);
                        }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        adapter = new AppSelectAdapter(context, appModelList, false,      new AppSelectAdapter.Callback() {
            @Override
            public void onChecked(int index, final boolean isChecked) {
                appModelList.get(index).setIs_hide(isChecked);
                LogUtil.debug("you clicked on" + appModelList.get(index));

//                adapter.notifyItemChanged(index);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getPreferences().setSYNCFLAG(true);
                if (isAllSelected(group.getCheckedRadioButtonId())) {
                    updateAppsAndSendToServer();
                } else {
                    int selectedAppCount = 0;
                    for (int i = 0; i < appModelList.size(); i++) {
                        if (appModelList.get(i).is_hide)
                            selectedAppCount++;
                    }
                    if (selectedAppCount < 10) {
                        Toast.makeText(context, "Please select at least 10 apps", Toast.LENGTH_SHORT).show();
                    } else {
                        updateAppsAndSendToServer();
                    }
                }
            }
        });




    }




    private boolean isAllSelected(int checkedRadioButtonId) {
        return checkedRadioButtonId == R.id.select_all;

    }

    private void updateAppsAndSendToServer() {
        /*
         * todo Update database
         * */
        AppModel app_model = new AppModel();
        for (int i = 0; i < appModelList.size(); i++) {
            app_model.findPackageNameAndHideAndShow(appModelList.get(i).package_name, !appModelList.get(i).is_hide);
     }
            Intent intent = new Intent(AppSelectActivity.this, AppScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Intent service = new Intent(AppSelectActivity.this, AppSyncService.class);
            startService(service);

    }





    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }



}
