package app.appified.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import app.appified.Adapter.AppDetailAdapter;
import app.appified.AppDetail;
import app.appified.Config.ApolloClientService;
import app.appified.Database.AppModel;
import app.appified.modelclass.InstallDetailDto;
import app.appified.R;
import app.appified.modelclass.UserApp;
import de.hdodenhof.circleimageview.CircleImageView;
public class AppDetailActivity extends AppCompatActivity {
    CircleImageView icon;
    RatingBar version;
    RecyclerView recyclerView;
    AppDetailAdapter appDetailAdapter;
    TextView appName, devloper, descrption, size, installCount;
    List<InstallDetailDto> installdetaillist = new ArrayList<>();
    private static final String TAG = "MyActivity";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        context = this;
        recyclerView = findViewById(R.id.recylerview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AppDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        appDetailAdapter = new AppDetailAdapter(context, installdetaillist);
        recyclerView.setAdapter(appDetailAdapter);
//        sortByDate();
        appDetailAdapter.notifyDataSetChanged();
        icon = findViewById(R.id.ivAppIcon);
        appName = findViewById(R.id.tvAppName);
        devloper = findViewById(R.id.tvCompanyName);
        size = findViewById(R.id.tvSize);
        installCount = findViewById(R.id.tvInstallCount);
        AppModel packagenames = (AppModel) getIntent().getSerializableExtra("packagenames");
         UserApp packagename = (UserApp) getIntent().getSerializableExtra("packagename");

        version = findViewById(R.id.tvVersion);
        if (packagenames != null) appDetail(packagenames.package_name);
        if (packagename != null) appDetail(packagename.packageName);
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

    public void appDetail(String packagename) {
        ApolloClientService.appDetail(packagename, new ApolloClientService.OnRequestComplete() {
            @Override
            public void onSuccess(final Object object) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        AppDetail.Data data = (AppDetail.Data) object;
                        Log.d(TAG, "details name : " + data.appDetail());
                        if (data.appDetail() != null) {
                            Picasso.get().load(data.appDetail().icon()).into(icon);
                            appName.setText(data.appDetail().title());
                            devloper.setText(data.appDetail().developer());
                            size.setText(data.appDetail().size());
                            installCount.setText(data.appDetail().installs());
                            version.setRating(Float.parseFloat(data.appDetail().scoreText()));
                            installdetaillist.clear();
                            for (AppDetail.User userdetail : data.appDetail().users()) {
                                InstallDetailDto detailDto = new InstallDetailDto(userdetail.profile(), userdetail.userName(), userdetail.installedAt());
                                installdetaillist.add(detailDto);
                            }
                            sortByDate();
                            appDetailAdapter.notifyDataSetChanged();
                        }
                        else
                        {

                        }


                    }
                });


            }

            @Override
            public void onFailure(String errorMessage, int errorCode) {


            }
        });
    }


    public void sortByDate() {
            Collections.sort(installdetaillist, new Comparator<InstallDetailDto>() {
                @Override
                public int compare(InstallDetailDto o1, InstallDetailDto o2) {
                    return o1.getInstalldate().compareToIgnoreCase(o2.getInstalldate());
                }
            });

    }

}