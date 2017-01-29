package com.neocampus.wifishared.listeners;

import com.neocampus.wifishared.observables.HotspotObservable;
import com.neocampus.wifishared.utils.WifiApControl;

/**
 * Created by Hirochi ☠ on 11/01/17.
 */

public interface OnFragmentSetListener {

    void onRefreshAll();
    void onRefreshClientCount(int newCount);
    void onRefreshTimeValue(long newDateValue);
    void onRefreshBatterieLevel(int newLevel);
    void onRefreshHotpostState(HotspotObservable observable);
    void onRefreshDataTraffic(long dataTrafficOctet);
    void onRefreshClient(WifiApControl.Client client);




    void onRefreshAllConfig();
    void onRefreshTimeConfig(long newTimeLimit);
    void onRefreshDataConfig(float newDataLimite);
    void onRefreshBatterieConfig(int newBatterieLimit);
}
