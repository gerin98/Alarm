package com.example.gerin.alarm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        int resId = -1;
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        switch (position){
            case 0:
                resId = R.layout.slide_layout;
                view = layoutInflater.inflate(R.layout.slide_layout, container, false);
                break;
            case 1:
                resId = R.layout.clock_layout;
                view = layoutInflater.inflate(R.layout.clock_layout, container, false);
                break;

        }



        //View view = layoutInflater.inflate(resId, container, false);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout) object);
    }
}
