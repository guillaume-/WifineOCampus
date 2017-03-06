package com.neocampus.wifishared.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.neocampus.wifishared.R;

/**
 * CirclePageIndicator permet d'affiche le cercle pour changer de page sur une vue multi-page
 * @author Hirochi ?
 */
public class CirclePageIndicator extends LinearLayout {

    private ViewPager viewPager;

    public CirclePageIndicator(Context context) {
        super(context);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        int count = this.viewPager.getAdapter().getCount();
        for (int position = 0; position < count; position++) {
            createCircleIndicator(position);
        }
        this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int newPosition = 0, oldPosition = 0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                oldPosition = newPosition;
                getChildAt(oldPosition).setActivated(false);
                getChildAt(position).setActivated(true);
                newPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        this.viewPager.setCurrentItem(0);
        getChildAt(0).setActivated(true);
    }

    private void createCircleIndicator(final int position) {
        View view = LayoutInflater.from(getContext()).
                inflate(R.layout.circle_indicator, null, false);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(position);
                view.setActivated(true);
            }
        });
        addView(view, params);
    }
}
