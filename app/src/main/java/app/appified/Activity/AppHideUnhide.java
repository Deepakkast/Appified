package app.appified.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.activeandroid.query.Select;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import app.appified.Adapter.HideUnhideAdpter;
import app.appified.Config.App;
import app.appified.Database.AppModel;
import app.appified.R;
import app.appified.Utils.LogUtil;

public class AppHideUnhide extends AppCompatActivity {
    List<AppModel> appModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HideUnhideAdpter mAdapter;
    Context context;
    TextView settingStatus,defaultSettingText,changeSetting;
    ConstraintLayout layoutDefaultSettingText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_hide_unhide);
        context=this;
        recyclerView = findViewById(R.id.my_recycler_view);
        Toolbar toolbar=findViewById(R.id.my_toolbar);
        changeSetting = findViewById(R.id.text_setting_change);
        settingStatus = findViewById(R.id.tv_default_setting_status);
        defaultSettingText = findViewById(R.id.tv_default_setting);
        layoutDefaultSettingText = findViewById(R.id.layout_default_setting);
         appStatusTextview();


        appModelList = new Select().from(AppModel.class).execute(); //getting the list of installed apps

        //sorting the list alphabtically
        Collections.sort(appModelList, new Comparator<AppModel>() {
                @Override
                public int compare(AppModel o1, AppModel o2) {
                    String name=o2.getApp_name();
                    return o1.getApp_name().compareToIgnoreCase(name);
                    }
            });

        layoutDefaultSettingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSettingAlertbox();
            }
        });

        //setting app list to adapter

        mAdapter = new HideUnhideAdpter(appModelList, this, new HideUnhideAdpter.Callback() {
            @Override
            public void onClick(int index, boolean isChecked) {
                appModelList.get(index).setIs_hide(!isChecked);
                mAdapter.notifyItemChanged(index);
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AppHideUnhide.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    //set preferences manully
    private int getSelectedItemPosition(int checkedRadioButtonId) {
        if (checkedRadioButtonId == R.id.let_me_choose_radio_button){
            App.getPreferences().setUserAppStatusFlag(1);
            return 1;
        }

        else if (checkedRadioButtonId == R.id.show_radio_button){
            App.getPreferences().setUserAppStatusFlag(2);
            return 2;
        }

        else if (checkedRadioButtonId == R.id.hide_radio_button){
            App.getPreferences().setUserAppStatusFlag(3);
            return 3;
        }

        return 0;
    }
    //set text in changeSetting for first time
    public void appStatusTextview(){
        final int radioBtnStatus1 = App.getPreferences().getUserAppStatusFlag();
        if (radioBtnStatus1 == 1){
            settingStatus.setText("Let me choose");
        } else if (radioBtnStatus1 == 3){
            settingStatus.setText("Only me");
        }
        else{
            settingStatus.setText("Share with Friends");
        }
    }
    //for change default setting manully
    public void changeSettingAlertbox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AppHideUnhide.this);
        builder.setTitle("Customize your Setting Here");
        View view1 = View.inflate(AppHideUnhide.this, R.layout.dialoge_permission, null);
        builder.setView(view1);
        final RadioGroup radioGroup = view1.findViewById(R.id.permission_radio);
        final int radioBtnStatus = App.getPreferences().getUserAppStatusFlag();
        // Log.d("TAG","radiobtn status :" +radioBtnStatus);
        if (radioBtnStatus == 1){
            radioGroup.check(R.id.let_me_choose_radio_button);
            settingStatus.setText("Let me choose");
        } else if (radioBtnStatus == 3){
            radioGroup.check(R.id.hide_radio_button);
            settingStatus.setText("Only me");
        }else{
            radioGroup.check(R.id.show_radio_button);
            settingStatus.setText("Share with Friends");
        }
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                App.getPreferences().setIsShowDialog(true);
                App.getPreferences().setUserAppStatusFlag(getSelectedItemPosition(radioGroup.getCheckedRadioButtonId()));
                LogUtil.debug(App.getPreferences().getUserAppStatusFlag() + " is :");
                appStatusTextview();
                dialog.cancel();
            }
        });
        builder.show();
    }
}
