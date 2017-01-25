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
import com.neocampus.wifishared.listeners.OnFragmentSetListener;
import com.neocampus.wifishared.views.BatterieSurfaceView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentSetListener} interface
 * to handle interaction events.
 * Use the {@link FragmentBatterie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBatterie extends Fragment implements OnFragmentConfigListener {

    private static final String ARG_PARAM1 = "param1";
    private BatterieSurfaceView surfaceView;
    private int mBatterieLimit;

    private OnActivitySetListener mListener;

    public FragmentBatterie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param batterie Parameter 1.
     * @return A new instance of fragment FragmentBatterie.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBatterie newInstance(int batterie) {
        FragmentBatterie fragment = new FragmentBatterie();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, batterie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBatterieLimit = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mListener.hideAppBarRefresh();
        this.mListener.showAppBarSaveConfig();

        View view = inflater.inflate(R.layout.fragment_batterie, container, false);
        surfaceView = (BatterieSurfaceView) view.findViewById(R.id.batterie_configuration);
        surfaceView.setLimiteBatterie(mBatterieLimit);
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
        return this.surfaceView.getLimiteBatterie();
    }

    @Override
    public long getLimiteTemps() {
        return 0;
    }
}
