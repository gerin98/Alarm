package com.example.gerin.alarm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

        int resId = 0;
        //View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        //additional layouts must be (RelativeLayout)
        switch (position){
            case 0:
                resId = R.layout.slide_layout;
                //view = layoutInflater.inflate(R.layout.slide_layout, container, false);
                break;
            case 1:
                resId = R.layout.clock_layout;
                //view = layoutInflater.inflate(R.layout.clock_layout, container, false);
                break;
            case 2:
                resId = R.layout.alarm_layout;
                //view = layoutInflater.inflate(R.layout.alarm_layout, container, false);
                break;
//            case 3:
//                resId = R.layout.slide_layout;
//                //view = layoutInflater.inflate(R.layout.slide_layout, container, false);
//                break;
            default:
                resId = R.layout.slide_layout;
                break;


        }
        View view = (View) layoutInflater.inflate(resId, null);
        //View view = layoutInflater.inflate(resId, container, false);
        //container.addView(view);
        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        ((ViewPager)container).removeView((RelativeLayout) object);
    }
}
