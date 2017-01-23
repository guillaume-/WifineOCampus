package com.neocampus.wifishared.listeners;

import com.neocampus.wifishared.utils.WifiApControl;

/**
 * Created by Hirochi ☠ on 11/01/17.
 */

public interface OnFragmentSetListener {

    void onRefreshAll();
    void onRefreshBatterieLevel(int newLevel);
    void onRefreshDataTraffic(long dataTrafficOctet);
    void onRefreshClient(WifiApControl.Client client);
    void onRefreshClientCount(int newCount);
    void onRefreshHotpostState(boolean activate);


    void onRefreshAllConfig();
    void onRefreshDataConfig(float newDataLimite);
    void onRefreshBatterieConfig(int newBatterieLimit);
}
