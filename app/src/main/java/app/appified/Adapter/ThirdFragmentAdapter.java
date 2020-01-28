package app.appified.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import app.appified.R;
import app.appified.modelclass.AppList;
public class ThirdFragmentAdapter extends RecyclerView.Adapter<ThirdFragmentAdapter.ViewHolder> {
    private List<AppList> itemLists;
    private Context context;
    public ThirdFragmentAdapter(List<AppList> itemLists, Context context) {
        this.itemLists = itemLists;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup,int viewType){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tab_third_itemlist, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder (@NonNull final ViewHolder viewHolder, final int position){
        final AppList itemList = itemLists.get(position);
        viewHolder.appName.setText(itemList.getAppName());
        viewHolder.logo.setImageDrawable(itemList.getIcon());
        String anythingDetail = "Anything text for demo";
        viewHolder.appDetail.setText(anythingDetail);
        //viewHolder.date.setText(itemList.getDate());
        long time = itemList.getDate();
        long minute = (time/(1000*60))%60;
        long hour = (time/(1000*60*60))%24;
        String lastUpdatedTime ="pm "+ hour +":"+ minute;
        viewHolder.date.setText(lastUpdatedTime);

       // viewHolder.date.setText(String.valueOf(itemList.getDate()));
    }
    @Override
    public int getItemCount () {
        return itemLists.size();
    }

    public void filterList(ArrayList<AppList> filteredList){
        itemLists = filteredList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView appName,appDetail;
        public ImageView logo;
        private ConstraintLayout constraintClick;
        public TextView date;
//        for custom font in textview appname
        Typeface font;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appDetail = itemView.findViewById(R.id.detail_thirdfrag);
            appName = itemView.findViewById(R.id.app_name_thirdfrag);
            logo = itemView.findViewById(R.id.iv_logo_thirdfrag);
            date = itemView.findViewById(R.id.date_third_frag);
            constraintClick = itemView.findViewById(R.id.third_tab_fragment);
            font = Typeface.createFromAsset(context.getAssets(),"fonts/brawler_regular.ttf");
            appName.setTypeface(font);
            appDetail.setTypeface(font);
        }
    }
}
