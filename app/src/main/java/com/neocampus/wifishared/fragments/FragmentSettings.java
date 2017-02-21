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
import com.neocampus.wifishared.observables.HotspotObservable;
import com.neocampus.wifishared.utils.WifiApControl;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * FragmentSettings est un fragment qui affiche une vue contenant l'ensemble des paramétrages de l'application
 *
 * @see Fragment
 */
public class FragmentSettings extends Fragment implements OnFragmentSetListener {

    /**
     * Vue affiché par le fragment
     */
    private View view;

    /**
     * Objet graphique affichant le seuil de la batterie
     */
    private TextView settingBatterie;

    /**
     * Objet graphique affichant le seuil du total de consommation de données
     */
    private TextView settingData;

    /**
     * Objet graphique affichant le seuil du temps d'activation d'une session de partage
     */
    private TextView settingTime;

    /**
     * Interface de communication avec l'activité principale {@link com.neocampus.wifishared.activity.MainActivity}
     * #see {@link OnActivitySetListener}
     */
    private OnActivitySetListener mListener;

    /**
     * Constructeur du fragment
     */
    public FragmentSettings() {
        // Required empty public constructor
    }

    /**
     * Crée la vue affiché par le fragment
     *
     * @see Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
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

    /**
     * @param context Context de l'application
     *
     * @see Fragment#onAttach(Context)
     */
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

    /**
     * Do nothing
     */
    @Override
    public void onRefreshAll(){
    }

    /**
     * Do nothing
     */
    @Override
    public void onRefreshBatterieLevel(int newLevel) {
    }

    /**
     * Do nothing
     */
    @Override
    public void onRefreshDataTraffic(long dataTrafficOctet) {
    }

    /**
     * Do nothing
     */
    @Override
    public void onRefreshClient(WifiApControl.Client client) {
    }

    /**
     * Do nothing
     */
    @Override
    public void onRefreshClientCount(int newCOunt) {
    }

    /**
     * Do nothing
     */
    @Override
    public void onRefreshTimeValue(long newDateValue) {
    }

    /**
     * Do nothing
     */
    @Override
    public void onRefreshHotpostState(HotspotObservable observable) {
    }

    /**
     * @see OnFragmentSetListener#onRefreshAllConfig()
     */
    @Override
    public void onRefreshAllConfig() {
        onRefreshBatterieConfig(this.mListener.getLimiteBatterie());
        onRefreshDataConfig(this.mListener.getLimiteDataTrafic());
        onRefreshTimeConfig(this.mListener.getLimiteTemps());
    }

    /**
     * @see OnFragmentSetListener#onRefreshBatterieConfig(int)
     */
    @Override
    public void onRefreshBatterieConfig(int newBatterieLimit) {
        if (settingBatterie != null) {
            settingBatterie.setText(String.format(Locale.FRANCE, "%d %% ", newBatterieLimit));
        }
    }

    /**
     * @see OnFragmentSetListener#onRefreshDataConfig(float)
     */
    @Override
    public void onRefreshDataConfig(float newDataLimit) {
        if (settingData != null) {
            String limiteData;
            if (newDataLimit >= 1.0f) {
                limiteData = String.format(Locale.FRANCE, "%.3f Go", newDataLimit);
            } else {
                limiteData = String.format(Locale.FRANCE, "%.3f Mo", newDataLimit * 1000.f);
            }
            settingData.setText(limiteData);
        }
    }

    /**
     * @see OnFragmentSetListener#onRefreshTimeConfig(long)
     */
    @Override
    public void onRefreshTimeConfig(long newTimeLimit) {
        if (settingTime != null) {
            SimpleDateFormat format;
            String s = 60*60*1000 <= newTimeLimit ?
                    "HH'h'mm": "mm 'min'";
            format = new SimpleDateFormat(s, Locale.FRANCE);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            settingTime.setText(format.format(newTimeLimit));
        }
    }


}
