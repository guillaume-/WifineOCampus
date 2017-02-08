package com.neocampus.wifishared.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
import com.neocampus.wifishared.utils.NotificationUtils;
import com.neocampus.wifishared.utils.WifiApControl;
import com.neocampus.wifishared.views.CirclePageIndicator;
import com.neocampus.wifishared.views.CirclePagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * FragmentHome est un fragment qui affiche une vue contenant les informations principales d'une session de partage en cours d'execution
 *
 * @see Fragment
 * @see OnFragmentSetListener
 */
public class FragmentHome extends Fragment implements OnFragmentSetListener,
        OnReachableClientListener, Chronometer.OnChronometerTickListener {

    /**
     * Vue affiché par le fragment
     */
    private View view;

    /**
     * Objet graphique qui affiche le nombre de client connecté
     */
    private TextView clientCount;

    /**
     * Objet graphique qui affiche le niveau restant de la batterie avant l'arrêt forcé d'une session de partage
     */
    private TextView batterieLevel;

    /**
     * Objet graphique qui affiche le seuil de la batterie
     */
    private TextView batterieLimite;

    /**
     * Objet graphique qui affiche le total de la consommation de données
     */
    private TextView dataLevel;

    /**
     * Objet graphique qui affiche le seuil de la consommation de données
     */
    private TextView dataLimite;

    /**
     * Objet graphique qui affiche le temps restant avant l'arrêt forcé d'une session de partage
     */
    private Chronometer chronometer;

    /**
     * Objet graphique qui affiche le seuil du temps d'activation d'une session de partage
     */
    private TextView timeLimite;

    /**
     * Objet graphique qui affiche le code de notification1
     */
    private TextView notification1;

    /**
     * Objet graphique qui permet d'activer ou de désactiver une session de partage
     */
    private Button hotSpotButton;

    /**
     * Interface de communication avec l'activité principale {@link com.neocampus.wifishared.activity.MainActivity}
     * #see {@link OnActivitySetListener}
     */
    private OnActivitySetListener onActivitySetListener;

    /**
     * Constructeur du fragment
     */
    public FragmentHome() {
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
        this.onActivitySetListener.hideAppBarSaveConfig();
        this.onActivitySetListener.showAppBarRefresh();

        this.view = inflater.inflate(R.layout.fragment_home, container, false);

        this.chronometer = (Chronometer) view.findViewById(R.id.time_level);
        this.timeLimite = (TextView) view.findViewById(R.id.time_limit);
        this.dataLevel = (TextView) view.findViewById(R.id.data_traffic);
        this.dataLimite = (TextView) view.findViewById(R.id.data_limit);
        this.batterieLevel = (TextView) view.findViewById(R.id.batterie_level_result);
        this.batterieLimite = (TextView) view.findViewById(R.id.batterie_level_limit);
        this.clientCount = (TextView) view.findViewById(R.id.reachable_client_count);
        this.notification1 = (TextView) view.findViewById(R.id.notification_code);
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

    /**
     * Reçoit la liste des clients connéctés
     * @param clients liste des clients connéctés
     */
    @Override
    public void onReachableClients(List<WifiApControl.Client> clients) {
        onRefreshClientCount(clients.size());
    }

    /**
     * Rafraichit les objets graphiques de la vue
     *
     * @see OnFragmentSetListener#onRefreshAll()
     */
    @Override
    public void onRefreshAll() {
        onRefreshClientCount(0);
        this.onActivitySetListener.postRequestTimeValue();
        this.onActivitySetListener.postRequestListClients();
        this.onActivitySetListener.postRequestDataTraffic();
        onRefreshBatterieLevel((int) BatterieUtils.getBatteryLevel(getContext()));
    }

    /**
     * @see OnFragmentSetListener#onRefreshClient(WifiApControl.Client)
     */
    @Override
    public void onRefreshClient(WifiApControl.Client client) {
    }

    /**
     * Rafraichit le nombre de clients connectés
     * @param newCount nouveau nombre de connectés
     *
     * @see OnFragmentSetListener#onRefreshClientCount(int)
     */
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

    /**
     * Rafraichit le temps d'activation de la session de partage en cours
     * @param newDateValue nouveau temps d'activation
     *
     * @see OnFragmentSetListener#onRefreshClientCount(int)
     */
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

    /**
     * Rafraichit l'état d'une session de partage
     * @param observable Observateur de l'état du Wi-FI AP
     *
     * @see OnFragmentSetListener#onRefreshHotpostState(HotspotObservable)
     */
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

    /**
     * Rafraichit le total de consommation de données
     * @param dataTrafficOctet nouveau total de consommation de données
     *
     * @see OnFragmentSetListener#onRefreshDataTraffic(long)
     */
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

    /**
     * Rafraichit le niveau restant de la batterie
     * @param newBatterieLevel nouveau niveau de la batterie
     *
     * @see OnFragmentSetListener#onRefreshBatterieLevel(int)
     * @see OnActivitySetListener#getLimiteBatterie()
     */
    @Override
    public void onRefreshBatterieLevel(int newBatterieLevel) {
        if (batterieLevel != null) {
            int batterieLimit = this.onActivitySetListener.getLimiteBatterie();
            batterieLevel.setText(String.format(Locale.FRANCE, "%d %% ", newBatterieLevel - batterieLimit));
        }
    }

    /**
     * @see OnFragmentSetListener#onRefreshAllConfig()
     */
    @Override
    public void onRefreshAllConfig() {
        onRefreshTimeConfig(this.onActivitySetListener.getLimiteTemps());
        onRefreshDataConfig(this.onActivitySetListener.getLimiteDataTrafic());
        onRefreshBatterieConfig(this.onActivitySetListener.getLimiteBatterie());
        onRefreshNotificationCode(this.onActivitySetListener.getNotificationCode());
    }

    /**
     * @see OnFragmentSetListener#onRefreshBatterieConfig(int)
     */
    @Override
    public void onRefreshBatterieConfig(int newBatterieLimit) {
        if (batterieLimite != null) {
            batterieLimite.setText(String.format(Locale.FRANCE, "%d %% ", newBatterieLimit));
        }
    }

    /**
     * @see OnFragmentSetListener#onRefreshTimeConfig(long)
     */
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

    /**
     * @see OnFragmentSetListener#onRefreshDataConfig(float)
     */
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

    /**
     *
     */
    public void onRefreshNotificationCode(int notificationCode) {
        if (notification1 != null) {
            String[] result = new String[]{"", "", ""};
            if(NotificationUtils.isBatterieEnabled(notificationCode)) {
                result[2] = "\uD83D\uDD0B";
            }
            if(NotificationUtils.isTimeEnabled(notificationCode)) {
                result[0] = "⏰";
            }
            if(NotificationUtils.isDataEnabled(notificationCode)) {
                result[1] = "\uD83D\uDCF6";
            }
            String notify = TextUtils.join(" ", result);
            notification1.setText(!"".equals(notify.trim()) ? notify : "\uD83D\uDD15");
        }
    }

    /**
     * Met à jour le temps restant d'activation
     * @param chronometer
     *
     * @see Chronometer.OnChronometerTickListener#onChronometerTick(Chronometer)
     */
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

    /**
     * @param context Context de l'application
     *
     * @see Fragment#onAttach(Context)
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivitySetListener) {
            onActivitySetListener = (OnActivitySetListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnActivitySetListener");
        }
    }


}
