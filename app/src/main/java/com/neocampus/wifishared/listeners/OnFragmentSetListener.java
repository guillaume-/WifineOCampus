package com.neocampus.wifishared.listeners;

import com.neocampus.wifishared.observables.HotspotObservable;
import com.neocampus.wifishared.utils.WifiApControl;

/**
 * OnFragmentSetListener permet de communiquer avec un fragment
 *
 * @author Hirochi ?
 * @version 1.0.0
 */
public interface OnFragmentSetListener {

    /**
     * Permet de rafra�chir tous les donn�es affich�es
     */
    void onRefreshAll();

    /**
     * Permet de rafra�chir le nombre de clients connect�s
     * @param newCount nouveau nombre de clients
     */
    void onRefreshClientCount(int newCount);

    /**
     * Permet de rafra�chir le temps d'utilisation d'une session de partage
     * @param newDateValue nouveau temps d'utilisation
     */
    void onRefreshTimeValue(long newDateValue);

    /**
     * Permet de rafra�chir le niveau de la batterie
     * @param newLevel nouveau niveau de la batterie
     */
    void onRefreshBatterieLevel(int newLevel);

    /**
     * Permet de rafra�chir le button d'activation d'une session de partage
     * @param observable observateur de l'�tat du WIFI-AP
     */
    void onRefreshHotpostState(HotspotObservable observable);

    /**
     * Permet de rafra�chir le total de consommation de donn�es
     * @param dataTrafficOctet nouveau total de consommation
     */
    void onRefreshDataTraffic(long dataTrafficOctet);

    /**
     * Permet de rafra�chir l'�tat d'un client
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
     * Rafraichie le seuil total de consommation de donn�es
     * @param newDataLimit nouveau seuil total de consommation de donn�es
     */
    void onRefreshDataConfig(float newDataLimit);

    /**
     * Rafraichie le seuil de la batterie
     * @param newBatterieLimit nouveau seuil de la batterie
     */
    void onRefreshBatterieConfig(int newBatterieLimit);
}
