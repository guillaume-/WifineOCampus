package com.neocampus.wifishared.views;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * CirclePagerAdapter permet de gérer une vue multi-page
 * @author Hirochi ? 
 */
public class CirclePagerAdapter extends PagerAdapter {

    private List<View> views = new ArrayList<>();

    public CirclePagerAdapter(ViewGroup viewGroup) {
        while (viewGroup.getChildCount() > 0) {
            views.add(viewGroup.getChildAt(0));
            viewGroup.removeViewAt(0);
        }
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        View view = views.get(position);
        ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
        lp.width = ViewPager.LayoutParams.FILL_PARENT;
        lp.height = ViewPager.LayoutParams.FILL_PARENT;
        view.setLayoutParams(lp);
        collection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = views.get(position);
        container.removeView(view);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }
}