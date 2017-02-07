package com.neocampus.wifishared.observables;

import java.util.Observable;

/**
 * DataObservable detects dataTraffic changes at a period defined in services/ServiceDataTraffic
 * and notify the Observer, which can stop the sharing
 *
 * @author Guillaume RIPOLL
 */
public class DataObservable extends Observable {

    /**
     * Total de la consommation de données actuelle
     */
    private long lastValue = 0;

    /**
     * Renvoi la total consommation de données actuelle
     *@return total consommation de données
     */
    public long getValue() {
        return lastValue;
    }

    /**
     * Tente de modifier la total de consommmation de données, et notifie
     * le changement en cas de succès
     *@param dataTx total de consommation de données observé
     *
     */
    public void setValue(long dataTx) {
        if(dataTx != lastValue) {
            lastValue = dataTx;
            setChanged();
            notifyObservers(dataTx);
        }
    }
}
