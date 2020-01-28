package app.appified.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import app.appified.Activity.AppDetailActivity;
import app.appified.Database.AppModel;
import app.appified.R;
import app.appified.Utils.CommonUtils;
import app.appified.Utils.LogUtil;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    Context context1;
    List<AppModel> stringList;
    Callback callback;

    public AppsAdapter(Context context, List<AppModel> list, Callback callback) {
        this.context1 = context;
        this.stringList = list;
        this.callback = callback;
    }

    @Override
    public AppsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //stringList=new ArrayList<>();

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_app_rowlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final AppModel appModel = stringList.get(position);
        viewHolder.textView_App_Name.setText(appModel.app_name);
        //LogUtil.debug("date format is : " + appModel.insatll_date);

   viewHolder.installedDate.setText(CommonUtils.epochToDate(appModel.insatll_date));
        Drawable app_icon = null;
        try {
            app_icon = context1.getPackageManager().getApplicationIcon(appModel.package_name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        viewHolder.imageView.setImageDrawable(app_icon);
        //LogUtil.debug("Server id:" + appModel.server_sync + "Package name" + appModel.package_name);
        if (appModel.server_sync == 0) {
            viewHolder.togle.setVisibility(View.VISIBLE);
            viewHolder.linearLayoutSetting.setVisibility(View.GONE);
        }
            viewHolder.togle.setChecked(appModel.is_hide);
            viewHolder.togle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null)
                        callback.onClick(position, viewHolder.togle.isChecked());
                    appModel.findPackageNameAndHideAndShow(appModel.package_name, appModel.is_hide);
                    notifyDataSetChanged();

                }
            });

            viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.debug("Clicked on icon");
                    showDialog(context1,viewHolder.settingsBtn, appModel);
                }
            });
            viewHolder.playStoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.debug("Clicked on icon");
                    showDialog(context1,viewHolder.settingsBtn, appModel);
                }
            });
            viewHolder.settingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.debug("Clicked on icon");
                    showDialog(context1,viewHolder.settingsBtn, appModel);
                }
            });
            viewHolder.linearLayoutSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    LogUtil.debug("Clicked on Liner layout");
                }
            });


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1=new Intent(v.getContext(),AppDetailActivity.class);
                    intent1.putExtra("packagenames",(Serializable) appModel);
                    context1.startActivity(intent1);
                }
            });
        }

    private void showDialog(final Context context,ImageView imageView, final AppModel appModel) {
        LogUtil.debug("Clicked method call");
        final PopupMenu popupMenu = new PopupMenu(context, imageView);
        popupMenu.getMenuInflater().inflate(R.menu.optionmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.playstore) {
                    final String appPackageName = appModel.package_name; // getPackageName() from Context or Activity object
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }

                if (item.getItemId() == R.id.setting) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", appModel.package_name, null);
                    intent.setData(uri);
                    context.startActivity(intent);
                }

                if (item.getItemId() == R.id.uninstall) {
                    if (callback != null)
                        callback.onClickUninstall(appModel);
                        Log.d("tag ", "Adapter callback method");
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        //return stringList.size();
        return stringList.size();
    }
    public void filterList(ArrayList<AppModel> filteredList){
        stringList = filteredList;
        notifyDataSetChanged();
    }

    public interface Callback {
        void onClickUninstall(AppModel object);

        void onClick(int index, boolean isChecked);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public ImageView imageView;
        public TextView textView_App_Name, installedDate;
        public CheckBox checkBox;
        public ImageButton optionmenu;
        public ImageView playStoreBtn, settingsBtn, deleteBtn;
        public SwitchCompat togle;
        public LinearLayout linearLayoutSetting;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            imageView = (ImageView) view.findViewById(R.id.iv_logo_userapp);
            textView_App_Name = view.findViewById(R.id.app_name_userapp);
            linearLayoutSetting = view.findViewById(R.id.layout_menu_setting);
            togle = view.findViewById(R.id.switch_btn_pending_apps);
            playStoreBtn = view.findViewById(R.id.iv_playstore);
            settingsBtn = view.findViewById(R.id.iv_setting);
            deleteBtn = view.findViewById(R.id.iv_delete);
            installedDate = view.findViewById(R.id.installed_date);
        }


    }

}



