    package app.appified.Fregment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;
import app.appified.Adapter.FriendListAdapter;
import app.appified.Config.ApolloClientService;
import app.appified.Config.App;
import app.appified.FriendList;
import app.appified.R;
import app.appified.Utils.LogUtil;
import app.appified.modelclass.FriendDetailDto;
public class FriendAppFragment extends Fragment {

    RecyclerView recyclerViewApps;
    FriendListAdapter adapter;
    List<FriendDetailDto>detailDtos=new ArrayList<>();
    private static final String TAG = "MyActivity";
    ImageView logout;
    Toolbar toolbar;
    TextView shareMessage,emptyShareMessage;
    ImageButton share,share_empty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_app, container, false);
        logout = view.findViewById(R.id.userprofile);
        recyclerViewApps = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewApps.setLayoutManager(layoutManager);
        adapter = new FriendListAdapter(requireContext(), detailDtos);
        recyclerViewApps.setAdapter(adapter);
        share=view.findViewById(R.id.share);
        shareMessage=view.findViewById(R.id.share_message);
        emptyShareMessage=view.findViewById(R.id.share_message_center);
        share_empty=view.findViewById(R.id.share_center);
        adapter.notifyDataSetChanged();
        friendList(App.getPreferences().getUserId());
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                share.putExtra(Intent.EXTRA_TEXT, "http://www.Appified.com");

                startActivity(Intent.createChooser(share, "Share link!"));
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
       /* final AppScreenActivity activity = (AppScreenActivity) getActivity();
        activity.setActionBarTitle("Friend list");
*/
       }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
         friendList(App.getPreferences().getUserId());
    }

    public void friendList(String user_id)
         {
             ApolloClientService.friendList(user_id, new ApolloClientService.OnRequestComplete() {
                 @Override
                 public void onSuccess(Object object) {
                     LogUtil.debug("friendlist showing :");
                     final FriendList.Data data = (FriendList.Data) object;
                     getActivity().runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             if (data.friendList() != null ) {
                                 detailDtos.clear();
                                 for (int i = 0; i < data.friendList().size(); i++) {
                                     FriendDetailDto friendDetailDto = new FriendDetailDto();
                                     friendDetailDto.setProfile(data.friendList().get(i).profile());
                                     friendDetailDto.setUserName(data.friendList().get(i).userName());
                                     friendDetailDto.setId(data.friendList().get(i)._id());
                                     Log.d(TAG, String.valueOf(data.friendList().get(i)));
                                     detailDtos.add(friendDetailDto);
                                     adapter.notifyDataSetChanged();
                                     share.setVisibility(View.VISIBLE);
                                     share_empty.setVisibility(View.GONE);
                                     shareMessage.setVisibility(View.VISIBLE);
                                     emptyShareMessage.setVisibility(View.GONE);

                                 }
                             }
                              else {
                                  if (data==null)
                                     share.setVisibility(View.GONE);
                                     share_empty.setVisibility(View.VISIBLE);
                                     shareMessage.setVisibility(View.GONE);
                                     emptyShareMessage.setVisibility(View.VISIBLE);
                                    }
                         }
                     });
                 }
                     @Override
                     public void onFailure(String errorMessage, int errorCode) {
                            LogUtil.debug("you are in failure");
                 }
             });

             }

}
