package com.neocampus.wifishared.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.neocampus.wifishared.utils.WifiApControl.EXTRA_WIFI_AP_STATE;
import static com.neocampus.wifishared.utils.WifiApControl.WIFI_AP_STATE_DISABLED;

/**
 * Created by Hirochi ☠ on 10/01/17.
 */

public class OnHotspotReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(EXTRA_WIFI_AP_STATE)) {
            int wifi_state = intent.
                    getIntExtra(EXTRA_WIFI_AP_STATE, -1);
            switch (wifi_state) {
                case WIFI_AP_STATE_DISABLED:
                    replaceUserWifiConfiguration(context);
                    break;
            }
            System.out.println();
        }
    }

    private boolean replaceUserWifiConfiguration(Context context) {
        /*
        if (!checkPermission(context))
            return false;

        WifiApControl apControl = WifiApControl.getInstance(context);
        WifiConfiguration
                configuration = apControl.getWifiApConfiguration();
        WifiConfiguration upsConfig = WifiApControl.getUPSWifiConfiguration();
        if(!WifiApControl.equals(configuration, upsConfig))
            return false;

        TableConfiguration tableConfiguration;
        SQLManager sqlManager = new SQLManager(context);
        sqlManager.open();

        try {

            tableConfiguration = sqlManager.getConfiguration();
            byte[] bytes = tableConfiguration.getUserConfiguration();
            WifiConfiguration userConfiguration
                    = ParcelableUtil.unmarshall(bytes, WifiConfiguration.class);
            apControl.setWifiApConfiguration(userConfiguration);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        finally {
            sqlManager.close();
        }
        */
        return true;
    }
}
