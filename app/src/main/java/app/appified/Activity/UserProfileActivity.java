package app.appified.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import app.appified.Adapter.ProfileAdapter;
import app.appified.Config.ApolloClientService;
import app.appified.FriendApps;
import app.appified.R;
import app.appified.Utils.LogUtil;
import app.appified.modelclass.FriendDetailDto;
import app.appified.modelclass.FriendsDto;
import app.appified.modelclass.UserApp;
public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    RecyclerView diffrent;
    RecyclerView common;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutManager1;
    ProfileAdapter unCommonAdapter;
    ProfileAdapter commonAdapter;
    ImageView profilePic;
    TextView username;
    List<UserApp> uncommonList = new ArrayList<>();
    List<UserApp> commonList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        diffrent = (RecyclerView) findViewById(R.id.diffrentrecyclerview);
        common=findViewById(R.id.commonrecyclerview);
        Toolbar toolbar=findViewById(R.id.my_toolbar);
        layoutManager = new LinearLayoutManager(UserProfileActivity.this,LinearLayoutManager.VERTICAL,false);
        layoutManager1=new LinearLayoutManager(UserProfileActivity.this,LinearLayoutManager.VERTICAL,false);
        diffrent.setLayoutManager(layoutManager);
        common.setLayoutManager(layoutManager1);
        profilePic = findViewById(R.id.userprofile);
        username = findViewById(R.id.Username);
        diffrent.setNestedScrollingEnabled(false);
        common.setNestedScrollingEnabled(false);

        //get userid from friendlist fregment

        Intent intent = getIntent();
        FriendDetailDto user_id= (FriendDetailDto) intent.getSerializableExtra("userid");

        Log.d(TAG, "user id is  ;" + user_id.id);
        friendAppDetails(user_id.id);

        unCommonAdapter = new ProfileAdapter(this, uncommonList);
        diffrent.setAdapter(unCommonAdapter);
        unCommonAdapter.notifyDataSetChanged();
        commonAdapter=new ProfileAdapter(this,commonList);
        common.setAdapter(commonAdapter);
        commonAdapter.notifyDataSetChanged();
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
    public void friendAppDetails(String user_id) {
           LogUtil.debug("user id is :" + user_id);
           if (user_id != null) {
               ApolloClientService.friendApps(user_id, new ApolloClientService.OnRequestComplete() {
                   @Override
                   public void onSuccess(final Object object) {

                       final FriendApps.Data data = (FriendApps.Data) object;
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {


                               if (data.friendApps() != null) {
                                   username.setText(data.friendApps().userName());
                                   if (data.friendApps() != null) {
                                       Picasso.get().load(data.friendApps().profile()).into(profilePic);
                                   }
                               }
                               if (data.friendApps()!=null) {
                                   if (data.friendApps().unCommonApp() != null) {
                                       for (FriendApps.UnCommonApp application : data.friendApps().unCommonApp()) {
                                           UserApp app = new UserApp(application.appName(), application.packageName(), application.icon());
                                           uncommonList.add(app);
                                           //uncommonList.clear();

                                           Collections.sort(uncommonList, new Comparator<UserApp>() {
                                               @Override
                                               public int compare(UserApp o1, UserApp o2) {
                                                   String name = o2.getAppName();
                                                   return o1.getAppName().compareToIgnoreCase(name);
                                               }
                                           });

                                       }
                                   }
                                   if (data.friendApps().commonApp() != null) {
                                       for (FriendApps.CommonApp application : data.friendApps().commonApp()) {
                                           UserApp app = new UserApp(application.appName(), application.packageName(), application.icon());
                                           commonList.add(app);
                                           //commonList.clear();
                                           Collections.sort(commonList, new Comparator<UserApp>() {
                                               @Override
                                               public int compare(UserApp o1, UserApp o2) {
                                                   String name = o2.getAppName();
                                                   return o1.getAppName().compareToIgnoreCase(name);
                                               }
                                           });

                                       }
                                   }
                               }
                               unCommonAdapter.notifyDataSetChanged();
                               commonAdapter.notifyDataSetChanged();

                           }
                       });


                   }

                   @Override
                   public void onFailure(String errorMessage, int errorCode) {

                   }

               });
           }

       }
 }
