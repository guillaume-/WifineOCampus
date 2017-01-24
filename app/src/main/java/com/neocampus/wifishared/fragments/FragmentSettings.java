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
import com.neocampus.wifishared.utils.WifiApControl;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class FragmentSettings extends Fragment implements OnFragmentSetListener {

    private View view;
    private TextView settingBatterie;
    private TextView settingData;
    private TextView settingTime;
    private SimpleDateFormat format;
    private OnActivitySetListener mListener;

    public FragmentSettings() {
        // Required empty public constructor
        format = new SimpleDateFormat("HH 'h' mm 'min'", Locale.FRANCE);
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
        this.settingTime = (TextView) view.findViewById(R.id.setting_time);

        onRefreshAllConfig();

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
    public void onRefreshAll(){
    }

    @Override
    public void onRefreshBatterieLevel(int newLevel) {
    }

    @Override
    public void onRefreshDataTraffic(long dataTrafficOctet) {
    }

    @Override
    public void onRefreshClient(WifiApControl.Client client) {
    }

    @Override
    public void onRefreshClientCount(int newCOunt) {
    }

    @Override
    public void onRefreshHotpostState(boolean activate) {
    }

    @Override
    public void onRefreshAllConfig() {
        onRefreshBatterieConfig(this.mListener.getLimiteBatterie());
        onRefreshDataConfig(this.mListener.getLimiteDataTrafic());
        onRefreshTimeConfig(this.mListener.getLimiteTemps());
    }

    @Override
    public void onRefreshBatterieConfig(int newBatterieLimit) {
        if (settingBatterie != null) {
            settingBatterie.setText(String.format(Locale.FRANCE, "%d %% ", newBatterieLimit));
        }
    }

    @Override
    public void onRefreshDataConfig(float newDataLimit) {
        if (settingData != null) {
            String limiteData;
            if (newDataLimit >= 1.0f) {
                limiteData = String.format(Locale.FRANCE, "%.3f Go", newDataLimit);
            } else {
                limiteData = String.format(Locale.FRANCE, "%d Mo", (int) (newDataLimit * 1000.f));
            }
            settingData.setText(limiteData);
        }
    }

    @Override
    public void onRefreshTimeConfig(long newTimeLimit) {
        if (settingTime != null) {

            settingTime.setText(format.format(newTimeLimit));
        }
    }


}
