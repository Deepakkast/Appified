package app.appified.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import app.appified.R;

public class MpagerAdapter extends PagerAdapter {
    private int [] layouts;
    private LayoutInflater layoutInflater;
    private Context context;


    public MpagerAdapter(int[] layouts, Context context)
    {
        this.layouts=layouts;
        this.context=context;
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=layoutInflater.inflate(layouts[position],container,false);
        container.addView(view);
        if (position==0){
            ImageView slide1=view.findViewById(R.id.slide1);
            Picasso.get()
                    .load(R.drawable.screen1)
                    .fit()
                        .into(slide1);
        }else if (position==1){
            ImageView slide2=view.findViewById(R.id.slide2);
            Picasso.get()
                    .load(R.drawable.screen2)
                    .fit()
                    .into(slide2);

        }else if (position==2){
            ImageView slide3=view.findViewById(R.id.slide3);
            Picasso.get()
                    .load(R.drawable.screen3)
                    .fit()
                    .into(slide3);

        }
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view=(View)object;
        container.removeView(view);

    }
}
