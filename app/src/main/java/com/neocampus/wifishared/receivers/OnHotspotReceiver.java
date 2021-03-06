package com.neocampus.wifishared.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;

import com.neocampus.wifishared.observables.HotspotObservable;
import com.neocampus.wifishared.sql.database.TableConfiguration;
import com.neocampus.wifishared.sql.manage.SQLManager;
import com.neocampus.wifishared.utils.ParcelableUtils;
import com.neocampus.wifishared.utils.WifiApControl;

import static com.neocampus.wifishared.utils.WifiApControl.EXTRA_WIFI_AP_STATE;
import static com.neocampus.wifishared.utils.WifiApControl.WIFI_AP_STATE_DISABLED;
import static com.neocampus.wifishared.utils.WifiApControl.WIFI_AP_STATE_DISABLING;
import static com.neocampus.wifishared.utils.WifiApControl.WIFI_AP_STATE_ENABLED;
import static com.neocampus.wifishared.utils.WifiApControl.WIFI_AP_STATE_ENABLING;
import static com.neocampus.wifishared.utils.WifiApControl.WIFI_AP_STATE_FAILED;

/**
 * OnHotspotReceiver permet d'�tre inform� par le syst�me android lors du changement de l'�tat du hotspot wifi,
 * et notifie le nouvel �tat au {@link java.util.Observer}
 */
public class OnHotspotReceiver extends BroadcastReceiver {

    /**
     * Observable qui d�tecte et notifie aux {@link java.util.Observer} le nouvel �tat
     */
    private HotspotObservable observable;

    /**
     * Constructeur par d�faut
     */
    public OnHotspotReceiver() {
        this.observable = null;
    }

    /**
     * Constructeur de la classe, initialise l'{@link java.util.Observable}
     * @param observable {@link HotspotObservable} par d�faut
     */
    public OnHotspotReceiver(HotspotObservable observable) {
        this.observable = observable;
    }

    /**
     * Cette m�thode est appel� lorsque le syst�me android notifie le changement de l'�tat du hotspot wifi
     * @param context contexte de l'application
     * @param intent contient les informations d'identification de l'�v�nement
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(EXTRA_WIFI_AP_STATE)) {
            int wifi_state = intent.
                    getIntExtra(EXTRA_WIFI_AP_STATE, -1);

            switch (wifi_state) {
                case WIFI_AP_STATE_DISABLING:
                case WIFI_AP_STATE_ENABLING:
                case WIFI_AP_STATE_FAILED:
                case WIFI_AP_STATE_ENABLED:
                    updateHotspotState(context);
                    break;
                case WIFI_AP_STATE_DISABLED:
                    if (WifiApControl.checkPermission(context)) {
                        if (observable == null) {
                            replaceUserWifiConfiguration(context);
                        } else {
                            updateHotspotState(context);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * restaure la configuration par d�faut de l'utilisateur
     * @param context contexte de l'application
     * @return vrai si la restauration s'est produite faux sinon
     *
     * @see TableConfiguration#getWifiConfiguration()
     * @see ParcelableUtils#unmarshall(byte[], Class)
     * @see WifiApControl#setWifiApConfiguration(WifiConfiguration)
     */
    private boolean replaceUserWifiConfiguration(Context context) {

        WifiApControl apControl = WifiApControl.getInstance(context);
        if (!apControl.isUPSWifiConfiguration())
            return false;

        TableConfiguration tableConfiguration;
        SQLManager sqlManager = new SQLManager(context);
        sqlManager.open();

        try {
            tableConfiguration = sqlManager.getConfiguration();
            byte[] bytes = tableConfiguration.getWifiConfiguration();
            if (bytes != null) {
                WifiConfiguration userConfiguration
                        = ParcelableUtils.unmarshall(bytes, WifiConfiguration.class);
                apControl.setWifiApConfiguration(userConfiguration);
                sqlManager.setConfiguration(null);
                sqlManager.setConfigurationA(0);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            sqlManager.close();
        }

        return true;
    }

    /**
     * Met � jour l'�tat du hotspot, lorsque le syst�me indique un nouvel �tat,
     * et notifie ce nouvel �tat au {@link java.util.Observer}
     * @param context
     */
    public void updateHotspotState(Context context) {
        if (observable != null
                && WifiApControl.checkPermission(context)) {
            WifiApControl apControl = WifiApControl.getInstance(context);
            observable.setUPS(apControl.isUPSWifiConfiguration());
            observable.setState(apControl.getState());
        }
    }
}
