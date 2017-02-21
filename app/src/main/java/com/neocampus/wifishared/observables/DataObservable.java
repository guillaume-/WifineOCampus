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
     * Total de la consommation de donn�es actuelle
     */
    private long lastValue = 0;

    /**
     * Renvoi la total consommation de donn�es actuelle
     *@return total consommation de donn�es
     */
    public long getValue() {
        return lastValue;
    }

    /**
     * Tente de modifier la total de consommmation de donn�es, et notifie
     * le changement en cas de succ�s
     *@param dataTx total de consommation de donn�es observ�
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
