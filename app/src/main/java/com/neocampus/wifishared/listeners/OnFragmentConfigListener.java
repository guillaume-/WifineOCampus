package com.neocampus.wifishared.listeners;

/**
 * Cette interface permet de communiquer avec un fragment de configuration
 *
 * @author Hirochi ☠
 * @version 1.0.0
 */
public interface OnFragmentConfigListener {

    /**
     * Renvoi la valeur configuré du seuil de consommation de données
     * @return seuil de consommation de données
     */
    float   getLimiteDataTraffic();

    /**
     * Renvoi la valeur configuré du seuil de batterie
     * @return seuil de batterie
     */
    int     getLimiteBatterie();

    /**
     * Renvoi la valeur configuré du seuil de temps d'activation d'une session de partage.
     * @return seuil de temps d'activation
     */
    long    getLimiteTemps();
}
