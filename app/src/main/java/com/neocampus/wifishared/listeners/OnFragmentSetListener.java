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



    /**
     * Rafraichie tous les seuils
     */
    void onRefreshAllConfig();

    /**
     * Rafraichie le seuil du temps d'activation
     * @param newTimeLimit nouveau seuil de temps
     */
    void onRefreshTimeConfig(long newTimeLimit);

    /**
     * Rafraichie le seuil total de consommation de données
     * @param newDataLimit nouveau seuil total de consommation de données
     */
    void onRefreshDataConfig(float newDataLimit);

    /**
     * Rafraichie le seuil de la batterie
     * @param newBatterieLimit nouveau seuil de la batterie
     */
    void onRefreshBatterieConfig(int newBatterieLimit);
}
