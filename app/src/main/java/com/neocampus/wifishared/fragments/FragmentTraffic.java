package com.neocampus.wifishared.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.listeners.OnFragmentConfigListener;
import com.neocampus.wifishared.views.DataSurfaceView;


public class FragmentTraffic extends Fragment implements OnFragmentConfigListener {
    private static final String ARG_PARAM1 = "param1";

    private float mLimiteData;

    private ScrollView scrollView;
    private DataSurfaceView gigaSurfaceView;
    private DataSurfaceView megaSurfaceView;

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
        this.gigaSurfaceView = (DataSurfaceView) view.findViewById(R.id.echelle_surface);
        this.megaSurfaceView = (DataSurfaceView) view.findViewById(R.id.data_configuration);
        this.gigaSurfaceView.setDateType(DataSurfaceView.DATA_TYPE.DATA_GIGA);
        this.gigaSurfaceView.setDataValue(1.0f * (float) Math.floor(mLimiteData) );

        this.megaSurfaceView.setDateType(DataSurfaceView.DATA_TYPE.DATA_MEGA);
        this.megaSurfaceView.setDataValue((float) (mLimiteData - Math.floor(mLimiteData))* 1000.0f);

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
    public float getLimiteDataTraffic() {
        float giga = (float) Math.floor(this.gigaSurfaceView.getDataValue());
        float mega = this.megaSurfaceView.getDataValue();
        return giga + (mega / 1000.f);
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
