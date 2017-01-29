package com.neocampus.wifishared.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.listeners.OnFragmentSetListener;
import com.neocampus.wifishared.listeners.OnReachableClientListener;
import com.neocampus.wifishared.observables.HotspotObservable;
import com.neocampus.wifishared.utils.BatterieUtils;
import com.neocampus.wifishared.utils.WifiApControl;
import com.neocampus.wifishared.views.CirclePageIndicator;
import com.neocampus.wifishared.views.CirclePagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class FragmentHome extends Fragment implements OnFragmentSetListener,
        OnReachableClientListener, Chronometer.OnChronometerTickListener {

    private View view;
    private TextView clientCount;
    private TextView batterieLevel;
    private TextView batterieLimite;
    private TextView dataLevel;
    private TextView dataLimite;
    private Chronometer chronometer;
    private TextView timeLimite;
    private Button hotSpotButton;

    private OnActivitySetListener mListener;

    public FragmentHome() {
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
        this.mListener.showAppBarRefresh();

        this.view = inflater.inflate(R.layout.fragment_home, container, false);

        this.chronometer = (Chronometer) view.findViewById(R.id.time_level);
        this.timeLimite = (TextView) view.findViewById(R.id.time_limit);
        this.dataLevel = (TextView) view.findViewById(R.id.data_traffic);
        this.dataLimite = (TextView) view.findViewById(R.id.data_limit);
        this.batterieLevel = (TextView) view.findViewById(R.id.batterie_level_result);
        this.batterieLimite = (TextView) view.findViewById(R.id.batterie_level_limit);
        this.clientCount = (TextView) view.findViewById(R.id.reachable_client_count);
        this.hotSpotButton = (Button) view.findViewById(R.id.button);

        this.chronometer.setOnChronometerTickListener(this);

        ViewPager viewPager = (ViewPager) this.view.findViewById(R.id.id_view_pager);
        CirclePageIndicator indicator = (CirclePageIndicator) this.view.findViewById(R.id.id_circle_indicator);
        viewPager.setAdapter(new CirclePagerAdapter(viewPager));
        indicator.setViewPager(viewPager);

        onRefreshAll();
        onRefreshAllConfig();

        if (WifiApControl.checkPermission(getContext())) {
            WifiApControl apControl = WifiApControl.getInstance(getContext());
            if (apControl.isEnabled()) {
                if (apControl.isUPSWifiConfiguration()) {
                    hotSpotButton.setText(getString(R.string.desactiver_le_partage));
                } else {
                    hotSpotButton.setText(getString(R.string.desactiver_native_partage));
                }
            }

        } else {
            hotSpotButton.setText(getString(R.string.no_permission));
        }
        return view;
    }

    @Override
    public void onReachableClients(List<WifiApControl.Client> clients) {
        onRefreshClientCount(clients.size());
    }


    @Override
    public void onRefreshAll() {
        onRefreshClientCount(0);
        this.mListener.postRequestTimeValue();
        this.mListener.postRequestListClients();
        this.mListener.postRequestDataTraffic();
        onRefreshBatterieLevel((int) BatterieUtils.getBatteryLevel(getContext()));
    }

    @Override
    public void onRefreshClient(WifiApControl.Client client) {
    }

    @Override
    public void onRefreshClientCount(final int newCount) {
        if (clientCount != null) {
            clientCount.post(new Runnable() {
                @Override
                public void run() {
                    clientCount.setText("(" + newCount + ")");
                }
            });
        }
    }

    @Override
    public void onRefreshTimeValue(long newDateValue) {
        if (chronometer != null) {
            if (newDateValue == 0) {
                chronometer.setBase(0L);
                chronometer.setText("00 sec");
                chronometer.stop();
            } else {
                chronometer.start();
                chronometer.setBase(newDateValue);
                chronometer.setText("00 sec");
            }
        }
    }

    @Override
    public void onRefreshHotpostState(HotspotObservable observable) {
        if (hotSpotButton != null) {
            switch (observable.getState()) {
                case WifiApControl.STATE_ENABLING:
                    hotSpotButton.setText(observable.isUPS() ?
                            getString(R.string.activation_en_cours) :
                            getString(R.string.activation_native_en_cours));
                    break;
                case WifiApControl.STATE_ENABLED:
                    hotSpotButton.setText(observable.isUPS() ?
                            getString(R.string.desactiver_le_partage) :
                            getString(R.string.desactiver_native_partage));
                    break;
                case WifiApControl.STATE_DISABLING:
                    hotSpotButton.setText(observable.isUPS() ?
                            getString(R.string.desactivation_en_cours) :
                            getString(R.string.desactivation_native_en_cours));
                    break;
                case WifiApControl.STATE_DISABLED:
                    hotSpotButton.setText(getString(R.string.activer_le_partage));
                    onRefreshClientCount(0);
                    break;
                case WifiApControl.STATE_FAILED:
                    hotSpotButton.setText(getString(R.string.activation_echec));
                    onRefreshClientCount(0);
                    break;

            }
        }
        onRefreshAll();
    }

    @Override
    public void onRefreshDataTraffic(long dataTrafficOctet) {
        if (dataLevel != null) {
            final String strDataTraffic;
            float dataTraffic = dataTrafficOctet / (1000.0f * 1000.0f * 1000.0f);
            if (dataTraffic >= 1.0f) {
                strDataTraffic = String.format(Locale.FRANCE, "%.3f Go", dataTraffic);
            } else {
                strDataTraffic = String.format(Locale.FRANCE, "%.2f Mo", (dataTraffic * 1000.f));
            }
            dataLevel.post(new Runnable() {
                @Override
                public void run() {
                    dataLevel.setText(strDataTraffic);
                }
            });
        }
    }

    @Override
    public void onRefreshBatterieLevel(int newBatterieLevel) {
        if (batterieLevel != null) {
            int batterieLimit = this.mListener.getLimiteBatterie();
            batterieLevel.setText(String.format(Locale.FRANCE, "%d %% ", newBatterieLevel - batterieLimit));
        }
    }

    @Override
    public void onRefreshAllConfig() {
        onRefreshTimeConfig(this.mListener.getLimiteTemps());
        onRefreshDataConfig(this.mListener.getLimiteDataTrafic());
        onRefreshBatterieConfig(this.mListener.getLimiteBatterie());
    }

    @Override
    public void onRefreshBatterieConfig(int newBatterieLimit) {
        if (batterieLimite != null) {
            batterieLimite.setText(String.format(Locale.FRANCE, "%d %% ", newBatterieLimit));
        }
    }

    @Override
    public void onRefreshTimeConfig(long newTimeLimit) {
        if (timeLimite != null) {
            SimpleDateFormat format;
            String s = 60 * 60 * 1000 <= newTimeLimit ?
                    "HH'h'mm" : "mm 'min'";
            format = new SimpleDateFormat(s, Locale.FRANCE);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            timeLimite.setText(format.format(newTimeLimit));
        }
    }

    @Override
    public void onRefreshDataConfig(float newDataLimit) {
        if (dataLimite != null) {
            String limiteData;
            if (newDataLimit >= 1.0f) {
                limiteData = String.format(Locale.FRANCE, "%.3f Go", newDataLimit);
            } else {
                limiteData = String.format(Locale.FRANCE, "%.2f Mo", newDataLimit * 1000.f);
            }
            dataLimite.setText(limiteData);
        }
    }


    @Override
    public void onChronometerTick(Chronometer chronometer) {
        long time = chronometer.getBase() - new Date().getTime();
        String timeText = "00 sec";
        if (time > 0) {
            int minute = (int) ((time / 60000L) % 60L);
            int heure = (int) (time / (60000L * 60L));
            if (heure > 1) {
                timeText = String.format(Locale.FRANCE, "%02dh%02d", heure, minute);
            } else if (minute > 1) {
                timeText = String.format(Locale.FRANCE, "%02d min", minute);
            } else {
                int seconde = (int) ((time / 1000L) % 60L);
                timeText = String.format(Locale.FRANCE, "%02d sec", seconde);
            }
        }
        chronometer.setText(timeText);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivitySetListener) {
            mListener = (OnActivitySetListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnActivitySetListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
