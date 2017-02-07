package com.neocampus.wifishared.listeners;

import com.neocampus.wifishared.observables.HotspotObservable;
import com.neocampus.wifishared.utils.WifiApControl;

/**
 * Cette interface permet de communiquer avec un fragment
 *
 * @author Hirochi ☠
 * @version 1.0.0
 */
public interface OnFragmentSetListener {

    /**
     * Permet de rafraîchir tous les données affichées
     */
    void onRefreshAll();

    /**
     * Permet de rafraîchir le nombre de clients connectés
     * @param newCount nouveau nombre de clients
     */
    void onRefreshClientCount(int newCount);

    /**
     * Permet de rafraîchir le temps d'utilisation d'une session de partage
     * @param newDateValue nouveau temps d'utilisation
     */
    void onRefreshTimeValue(long newDateValue);

    /**
     * Permet de rafraîchir le niveau de la batterie
     * @param newLevel nouveau niveau de la batterie
     */
    void onRefreshBatterieLevel(int newLevel);

    /**
     * Permet de rafraîchir le button d'activation d'une session de partage
     * @param observable observateur de l'état du WIFI-AP
     */
    void onRefreshHotpostState(HotspotObservable observable);

    /**
     * Permet de rafraîchir le total de consommation de données
     * @param dataTrafficOctet nouveau total de consommation
     */
    void onRefreshDataTraffic(long dataTrafficOctet);

    /**
     * Permet de rafraîchir l'état d'un client
     * @param client
     */
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
