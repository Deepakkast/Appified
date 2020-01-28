package app.appified.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import app.appified.Activity.UserProfileActivity;
import app.appified.R;
import app.appified.Utils.LogUtil;
import app.appified.modelclass.FriendDetailDto;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder>{
    Context context1;
    List<FriendDetailDto> stringList;


    public FriendListAdapter(Context context, List<FriendDetailDto> list)
    {
        this.context1=context;
        this.stringList=list;

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public ImageView imageView;
        public TextView username;


        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            imageView = (ImageView) view.findViewById(R.id.imageview);
            username=view.findViewById(R.id.apk_name);
              }


    }


    @NonNull
    @Override
    public FriendListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewfrnddetail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListAdapter.ViewHolder holder, int position) {
        final FriendDetailDto  detailDto = stringList.get(position);

       holder.username.setText(detailDto.userName);
       Picasso.get().load(detailDto.profile).fit().into(holder.imageView);
       LogUtil.debug("your user id is : " + detailDto.id);
       if (detailDto.id!=null)
       {
           LogUtil.debug("your user id is : " + detailDto.id);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(v.getContext(),UserProfileActivity.class);
               intent.putExtra("userid", (Serializable) detailDto);
               context1.startActivity(intent);
           }
       });
    }
    else {
           Toast.makeText(context1,"user id is null: ",Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }
}
