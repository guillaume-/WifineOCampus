package com.neocampus.wifishared.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.listeners.OnFragmentConfigListener;
import com.neocampus.wifishared.views.ChronoTimeView;

import java.util.Date;


public class FragmentTime extends Fragment implements OnFragmentConfigListener{
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private long mParam1;
    private ChronoTimeView timeView;

    private OnActivitySetListener mListener;

    public FragmentTime() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param time Parameter 1.
     * @return A new instance of fragment FragmentTime.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTime newInstance(long time) {
        FragmentTime fragment = new FragmentTime();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getLong(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mListener.hideAppBarRefresh();
        this.mListener.showAppBarSaveConfig();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time, container, false);

        this.timeView = (ChronoTimeView) view.findViewById(R.id.timeView);

        this.timeView.setHours((int) (mParam1 / (3600*1000)));
        this.timeView.setMinute(new Date(mParam1).getMinutes());
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
        return 0;
    }

    @Override
    public int getLimiteBatterie() {
        return 0;
    }

    @Override
    public long getLimiteTemps() {
        return ((timeView.getHours() * 3600)
                + (timeView.getMinute() * 60))*1000;
    }
}
