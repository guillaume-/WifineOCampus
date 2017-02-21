package com.neocampus.wifishared.listeners;

/**
 * OnFragmentConfigListener permet de communiquer avec un fragment de configuration
 *
 * @author Hirochi ?
 * @version 1.0.0
 */
public interface OnFragmentConfigListener {

    /**
     * Renvoi la valeur configur� du seuil de consommation de donn�es
     * @return seuil de consommation de donn�es
     */
    float   getLimiteDataTraffic();

    /**
     * Renvoi la valeur configur� du seuil de batterie
     * @return seuil de batterie
     */
    int     getLimiteBatterie();

    /**
     * Renvoi la valeur configur� du seuil de temps d'activation d'une session de partage.
     * @return seuil de temps d'activation
     */
    long    getLimiteTemps();

    /**
     * Renvoi le nouveau code de notification
     * @return code de notification
     */
    int getNotificationCode();
}
