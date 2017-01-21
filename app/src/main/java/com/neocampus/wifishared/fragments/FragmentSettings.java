package com.neocampus.wifishared.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.listeners.OnFragmentSetListener;

import java.util.Locale;


public class FragmentSettings extends Fragment implements OnFragmentSetListener {

    private View view;
    private TextView settingBatterie;
    private TextView settingData;

    private OnActivitySetListener mListener;

    public FragmentSettings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.mListener.hideAppBarSaveConfig();
        this.mListener.hideAppBarRefresh();

        this.view = inflater.inflate(R.layout.fragment_settings, container, false);

        this.settingBatterie = (TextView) view.findViewById(R.id.setting_batterie);
        this.settingData = (TextView) view.findViewById(R.id.setting_data);

        onRefreshNotify();
        onRefreshConfigNotify();

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
    public void onRefreshNotify() {
        if(view != null) {
            int batterie_limite_level = this.mListener.getLimiteBatterieLevel();
            settingBatterie.setText(String.format(Locale.FRANCE, "%d %% ", batterie_limite_level));
        }
    }

    @Override
    public void onRefreshConfigNotify() {
        String limiteData;
        float data_limite_trafic = this.mListener.getLimiteDataTrafic();
        if(data_limite_trafic >= 1.0f) {
            limiteData = String.format(Locale.FRANCE, "%.3f Go", data_limite_trafic);
        }
        else {
            limiteData = String.format(Locale.FRANCE, "%d Mo", (int)(data_limite_trafic * 1000.f));
        }
        settingData.setText(limiteData);
    }
}
