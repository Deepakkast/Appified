package app.appified.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import app.appified.R;
import app.appified.Utils.LogUtil;
import app.appified.modelclass.PagfListModel;

public class PagfAdapter extends RecyclerView.Adapter<PagfAdapter.ViewHolder> {
    TextView timer;
    int seconds, minutes;
    private List<PagfListModel> pagfItemLists;
    private Context context;

    public PagfAdapter(List<PagfListModel> pagfItemLists, Context context) {
        this.pagfItemLists = pagfItemLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pgf_tab_rowlist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        //super.onBindViewHolder(holder, position,);
        final PagfListModel itemList = pagfItemLists.get(position);
        final String appPackageName = itemList.getPackageName();
        viewHolder.appName.setText(itemList.getAppName());
        Picasso.get().load(itemList.icon).fit().into(viewHolder.appIcon);
        String priceDiscount = "₹ " + itemList.getOriginalPrice();
        viewHolder.appOriginalPrice.setText(priceDiscount);
        viewHolder.appOriginalPrice.setPaintFlags(viewHolder.appOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Log.d(PagfAdapter.class.getSimpleName(), "onBindViewHolder: " + String.valueOf(itemList.isFree));
      //checking item is free or on discount
        if (itemList.getIsFree()) {
            viewHolder.onOfferPrice.setText("Free");
        } else {
                viewHolder.onOfferPrice.setText(String.valueOf("₹ " + itemList.getOnOfferPrice()));

        }


        //          for countdown timer

        long currentTime = new Date().getTime();
        long futureTime = ((Long.valueOf(itemList.getTimer())) / 1000) - 19800; //   1554898080 - 19800;
        futureTime = futureTime * 1000;
        long differ = (futureTime - currentTime);
        if (futureTime > currentTime) {
            new CountDownTimer(differ, 1000) {

                public void onTick(long millisUntilFinished) {
                    if (TimeUnit.MILLISECONDS.toHours(millisUntilFinished)>=4){
                        viewHolder.countDown.setTextColor(Color.parseColor("#149108"));
                    }else {
                        viewHolder.countDown.setTextColor(Color.parseColor("#ff0000"));
                    }
                    viewHolder.countDown.setText(String.format(Locale.US, "%02d h %02d m",
                            (TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished))));
                }

                public void onFinish() {
                    viewHolder.countDown.setVisibility(View.GONE);
                }
            }.start();

        } else {
            viewHolder.countDown.setVisibility(View.GONE);
        }
            // for cliking on app item going to playstore
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LogUtil.debug(itemList.packageName);
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + itemList.packageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + itemList.packageName)));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return pagfItemLists.size();
    }

    //for searchview

    public void filterList(ArrayList<PagfListModel> filteredList) {
        pagfItemLists = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView appName, appOriginalPrice,timer, onOfferPrice, countDown;
        public ImageView appIcon;
        ImageView playstoreIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.app_name_pgf_tab);
            appIcon = itemView.findViewById(R.id.iv_logo_pgf_tab1);
            appOriginalPrice = itemView.findViewById(R.id.tv_apps_original_price);
            onOfferPrice = itemView.findViewById(R.id.apps_on_offer_price);
            playstoreIcon = itemView.findViewById(R.id.iv_playstore_icon);
            countDown = itemView.findViewById(R.id.count_down_timer);


        }
    }

}
