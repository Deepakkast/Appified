 package app.appified.Adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import app.appified.Utils.CommonUtils;
import app.appified.Utils.LogUtil;
import app.appified.modelclass.InstallDetailDto;
import app.appified.R;

public class AppDetailAdapter extends RecyclerView.Adapter<AppDetailAdapter.ViewHolder> {
    Context context1;
    List<InstallDetailDto>installDetailDto;


    public AppDetailAdapter(Context context,List<InstallDetailDto>list)

    {
        this.context1=context;
        this.installDetailDto=list;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public ImageView imageView;
        public TextView username;
        public TextView installdate;
        public CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.cardViewDetail);
            imageView = (ImageView) view.findViewById(R.id.imageview);
            username = (TextView) view.findViewById(R.id.tvusername);
            installdate = (TextView) view.findViewById(R.id.tvinstalldate );
          }
    }

        @Override
        public AppDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppDetailAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.appdetail_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppDetailAdapter.ViewHolder holder, int position) {




         InstallDetailDto userdetails=installDetailDto.get(position);
         if (userdetails!=null) {
             LogUtil.debug("date is : " +userdetails.installdate);
             holder.username.setText(userdetails.username);

             holder.installdate.setText(CommonUtils.epochToDate(userdetails.installdate));
             LogUtil.debug("date is : "  + CommonUtils.epochToDate(userdetails.installdate));
             Picasso.get().load(userdetails.profile).into(holder.imageView);
         }
     }

    @Override
    public int getItemCount() {
        return installDetailDto.size();
    }
}
