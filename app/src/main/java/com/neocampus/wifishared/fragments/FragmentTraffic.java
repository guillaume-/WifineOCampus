package com.neocampus.wifishared.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.listeners.OnFragmentConfigListener;
import com.neocampus.wifishared.views.DataSurfaceView;
import com.neocampus.wifishared.views.EchelleSurfaceView;


public class FragmentTraffic extends Fragment implements View.OnTouchListener,
        ViewTreeObserver.OnScrollChangedListener, OnFragmentConfigListener {
    private static final String ARG_PARAM1 = "param1";

    private float mLimiteData;

    private ScrollView scrollView;
    private EchelleSurfaceView surfaceView;
    private DataSurfaceView dataSurfaceView;
    private ViewTreeObserver observer;
    private Rect scrollBounds = new Rect();

    private OnActivitySetListener mListener;

    public FragmentTraffic() {
        // Required empty public constructor
    }


    public static FragmentTraffic newInstance(float limite_data) {
        FragmentTraffic fragment = new FragmentTraffic();
        Bundle args = new Bundle();
        args.putFloat(ARG_PARAM1, limite_data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mLimiteData = getArguments().getFloat(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mListener.hideAppBarRefresh();
        this.mListener.showAppBarSaveConfig();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_config, container, false);
        this.surfaceView = (EchelleSurfaceView) view.findViewById(R.id.echelle_surface);
        this.dataSurfaceView = (DataSurfaceView) view.findViewById(R.id.data_configuration);
        this.scrollView = (ScrollView) view.findViewById(R.id.data_scroll);
        this.scrollView.post(new Runnable() {
            @Override
            public void run() {
                surfaceView.setScrollHeight(scrollView.getHeight());
                float tauxLevel = 1.0f - (mLimiteData / surfaceView.getMaxGigaoctets());
                int positionMax = surfaceView.getSurfaceMaxHeigth();
                int positionMin = surfaceView.getSurfaceMinHeigth();
                int positionValue = (int) (tauxLevel * (positionMax- positionMin));
                scrollView.setScrollY(positionValue);
                dataSurfaceView.setLimiteData(mLimiteData);
            }
        });
        this.scrollView.setOnTouchListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivitySetListener) {
            mListener = (OnActivitySetListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (observer == null) {
            observer = scrollView.getViewTreeObserver();
            observer.addOnScrollChangedListener(this);
        }
        else if (!observer.isAlive()) {
            observer.removeOnScrollChangedListener(this);
            observer = scrollView.getViewTreeObserver();
            observer.addOnScrollChangedListener(this);
        }
        return false;
    }

    @Override
    public void onScrollChanged() {
        scrollView.getDrawingRect(scrollBounds);
        int scale = surfaceView.getSurfaceScale();
        int positionValue = scrollBounds.bottom - scale;
        int positionMax = surfaceView.getSurfaceMaxHeigth();
        int positionMin = surfaceView.getSurfaceMinHeigth();

        float scaleValue = (positionValue - positionMin);
        float scalePositionMax = (positionMax - positionMin);
        float tauxLevel = 1.0f - (scaleValue / scalePositionMax);
        float level = tauxLevel * surfaceView.getMaxGigaoctets();

        this.dataSurfaceView.setLimiteData(level);
    }

    @Override
    public float getLimiteDataTraffic() {
        return this.dataSurfaceView.getLimiteData();
    }

    @Override
    public int getLimiteBatterie() {
        return 0;
    }

    @Override
    public long getLimiteTemps() {
        return 0;
    }


}
