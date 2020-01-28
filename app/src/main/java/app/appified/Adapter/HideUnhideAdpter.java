package app.appified.Adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.appified.Config.ApolloClientService;
import app.appified.Database.AppModel;
import app.appified.R;
import app.appified.Utils.LogUtil;
import app.appified.modelclass.AppList;
import de.hdodenhof.circleimageview.CircleImageView;

public class HideUnhideAdpter extends RecyclerView.Adapter<HideUnhideAdpter.ViewHolder> {

    private List<AppModel> appLists;
    private Context context;
    private Callback callback;
    public interface Callback{
        public void onClick(int index,boolean isChecked);
    }

    public HideUnhideAdpter(List<AppModel> itemLists, Context context, Callback callback) {
        this.appLists = itemLists;
        this.context = context;
        this.callback = callback;
    }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_app_ishide_row,viewGroup,false);
            return  new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
            final AppModel appList = appLists.get(position);
            viewHolder.appName.setText(appList.getApp_name());
            Drawable app_icon = null;
            try {
                app_icon = context.getPackageManager().getApplicationIcon(appList.package_name);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            viewHolder.logo.setImageDrawable(app_icon);
            viewHolder.toggleBtn.setChecked(!appList.isIs_hide());
            viewHolder.toggleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                viewHolder.toggleBtn.isChecked();
                    if (callback!=null)
                        callback.onClick(position,viewHolder.toggleBtn.isChecked());
                        appList.findPackageNameAndHideAndShow(appList.package_name,appList.is_hide);
                        LogUtil.debug("hide or not in database" + appList.is_hide);
                        apphide(appList.package_name,appList.is_hide);
                        notifyDataSetChanged();

                }
            });
        /*if (isChecked){
            isChecked = true;
        }*/
        }
        @Override
        public int getItemCount() {
            return appLists.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder{
    public TextView appName;
    public ImageView logo;
    public SwitchCompat toggleBtn;
    public boolean isHiden = false;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        appName = itemView.findViewById(R.id.app_name);
        logo = itemView.findViewById(R.id.iv_logo);
        toggleBtn = itemView.findViewById(R.id.switch_btn);
//            toggleBtn.setOnCheckedChangeListener(this);

    }
}


 public  void apphide(String packagename,boolean ishide)
 {
     ApolloClientService.isHide(packagename,ishide, new ApolloClientService.OnRequestComplete() {
         @Override
         public void onSuccess(Object object) {

         }

         @Override
         public void onFailure(String errorMessage, int errorCode) {

         }
     });
 }


    }
