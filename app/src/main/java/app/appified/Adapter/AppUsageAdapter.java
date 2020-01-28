package app.appified.Adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.appified.R;
import app.appified.modelclass.AppList;
import app.appified.modelclass.AppUsageAppList;

public class AppUsageAdapter extends RecyclerView.Adapter<AppUsageAdapter.ViewHolder> {

private List<AppUsageAppList> appLists;
private Context context;
private String packageName;
private Long appUsageTime;





public AppUsageAdapter(List<AppUsageAppList> itemLists, Context context) {
        this.appLists = itemLists;
        this.context = context;

        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.app_usage_rowlist,viewGroup,false);
        return  new ViewHolder(view);
        }
@Override
public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
final AppUsageAppList appList = appLists.get(position);
        PackageManager packageManager = context.getPackageManager();
//        packageName = appList.getPackageName();
        String appUsage1="-";
        try {
        appUsageTime = appList.getUsageStats().getTotalTimeInForeground();
        long second = (appUsageTime/1000)%60;
        long minute = (appUsageTime/(1000*60))%60;
        long hour = (appUsageTime/(1000*60*60))%24;
        appUsage1 = hour + " h " + minute + " m ";
        }catch (NullPointerException e){
        e.printStackTrace();
        }

        // usageTime.setText(appUsage1);
        //Log.d("Tag", "app usage time :" +appUsageTime);
       /* Log.d(AppAdapter.class.getSimpleName(),"package name"+packageName);
        String nameAppUsage = null;
        Drawable appUsageIcon = null;
        try {
            nameAppUsage = (String) packageManager.getApplicationLabel(packageManager.
                    getApplicationInfo(packageName, PackageManager.GET_META_DATA));
            appUsageIcon = packageManager.getApplicationIcon(packageManager.getApplicationInfo(packageName,PackageManager.CERT_INPUT_RAW_X509));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/
        viewHolder.appName.setText(appList.getAppName());
        viewHolder.logo.setImageDrawable(appList.getIcon());
        viewHolder.text1.setText(appUsage1);

        }
@Override
public int getItemCount() {
        return appLists.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder{
    public TextView appName;
    public ImageView logo;
    public TextView text1;



    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        appName = itemView.findViewById(R.id.app_name);
        logo = itemView.findViewById(R.id.iv_logo);
        text1 = itemView.findViewById(R.id.text1);


    }
}
}


