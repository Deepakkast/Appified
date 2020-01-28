package app.appified.Adapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;
import app.appified.Activity.AppDetailActivity;
import app.appified.R;
import app.appified.modelclass.UserApp;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    Context context;
    List<UserApp> userApps;
    //List<Use>commonApps;

    public ProfileAdapter(Context context, List<UserApp> list)

    {
        this.context = context;
        userApps = list;
        //commonApps=list1;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public CircleImageView profile;
        public TextView username;


        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            profile = (CircleImageView) view.findViewById(R.id.imageview);
            username = (TextView) view.findViewById(R.id.apk_name);

        }


    }


    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardprofile, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {

        final UserApp friendsDto = userApps.get(position);
        holder.username.setText(friendsDto.appName);
       /* try {
            Drawable app_icon = context.getPackageManager().getApplicationIcon(friendsDto.packageName);
            holder.profile.setImageDrawable(app_icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            holder.profile.setImageDrawable(null);
        }*/


        Log.d("my activity","URL IS:"+friendsDto.icon);
        Picasso.get().load(friendsDto.icon).into(holder.profile);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),AppDetailActivity.class);
                intent.putExtra("packagename", (Serializable) friendsDto);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return userApps.size();
    }
}
