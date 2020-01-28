package app.appified.Adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import javax.security.auth.callback.Callback;

import app.appified.Database.AppModel;
import app.appified.R;
import app.appified.Utils.LogUtil;

public class AppSelectAdapter extends RecyclerView.Adapter<AppSelectAdapter.ViewHolder> {
    Context context;
    List<AppModel> stringList;
    Callback callback;
    AppModel app_model = new AppModel();
    boolean canCheck;
    public interface Callback
    {
        void onChecked(int index ,boolean isChecked);
    }
    public AppSelectAdapter(Context context,List<AppModel>list, boolean canCheck, Callback callback)
    {
        this.context=context;
        this.stringList=list;
        this.callback=callback;
        this.canCheck = canCheck;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView imageView;
        public TextView textView_App_Name;
        public CheckBox hide;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.card_view);
            imageView = (ImageView) itemView.findViewById(R.id.imageview);
            textView_App_Name = (TextView) itemView.findViewById(R.id.apk_name);
            hide=itemView.findViewById(R.id.hide);
        }
    }


    @NonNull
    @Override
    public AppSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.appselectcardview,parent,false));

    }

    public void setCanCheck(boolean canCheck) {
        this.canCheck = canCheck;
    }

    @Override
    public void onBindViewHolder(@NonNull final AppSelectAdapter.ViewHolder holder, final int position) {

        final AppModel appModel = stringList.get(position);
        holder.textView_App_Name.setText(appModel.app_name);
        Drawable app_icon = null;
        try {
            app_icon = context.getPackageManager().getApplicationIcon(appModel.package_name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //can check for enable disable
        if (canCheck)
        {
            holder.hide.setEnabled(true);
        }

        else
        {
            holder.hide.setEnabled(false);
        }
        holder.imageView.setImageDrawable(app_icon);
        holder.hide.setChecked(appModel.isIs_hide());
        holder.hide.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {
                if (callback!=null)
                {
                   callback.onChecked(position,holder.hide.isChecked());
//                   app_model.findPackageNameAndHideAndShow(appModel.package_name, !appModel.is_hide);
//                   LogUtil.debug("update value is : " + appModel.package_name + !appModel.is_hide );

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }
}
