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
import static com.neocampus.wifishared.utils.WifiApControl.WIFI_AP_STATE_ENABLED;

/**
 * Created by Hirochi ☠ on 10/01/17.
 */

public class OnHotspotReceiver extends BroadcastReceiver {

    private HotspotObservable observable;

    public OnHotspotReceiver() {
        this.observable = null;
    }

    public OnHotspotReceiver(HotspotObservable observable) {
        this.observable = observable;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(EXTRA_WIFI_AP_STATE)) {
            int wifi_state = intent.
                    getIntExtra(EXTRA_WIFI_AP_STATE, -1);

            switch (wifi_state) {
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
            System.out.println();
        }
    }

    private boolean replaceUserWifiConfiguration(Context context) {

        WifiApControl apControl = WifiApControl.getInstance(context);
        if(!apControl.isUPSWifiConfiguration())
            return false;

        TableConfiguration tableConfiguration;
        SQLManager sqlManager = new SQLManager(context);
        sqlManager.open();

        try {
            tableConfiguration = sqlManager.getConfiguration();
            byte[] bytes = tableConfiguration.getWifiConfiguration();
            if(bytes != null) {
                WifiConfiguration userConfiguration
                        = ParcelableUtils.unmarshall(bytes, WifiConfiguration.class);
                apControl.setWifiApConfiguration(userConfiguration);
                sqlManager.setConfiguration(null);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        finally {
            sqlManager.close();
        }

        return true;
    }

    public void updateHotspotState(Context context)
    {
        if (observable != null
                && WifiApControl.checkPermission(context)) {
            WifiApControl apControl = WifiApControl.getInstance(context);
            if(apControl.isUPSWifiConfiguration()) {
                observable.setRunning(apControl.isEnabled());
            }
        }
    }
}
