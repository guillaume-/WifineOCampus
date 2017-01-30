package com.neocampus.wifishared.observables;

import java.util.Observable;

/*
 * Created by Guillaume RIPOLL on 23/01/17.
 * DataObservable :
 * Detects dataTraffic changes at a period defined in services/ServiceDataTraffic
 * and notify the Observer, which can stop the sharing
 */
public class DataObservable extends Observable {
    private long lastValue = 0;

    /**
     *@return the value
     */
    public long getValue() {
        return lastValue;
    }

    /**
     *@param dataTx
     * the value to set
     */
    public void setValue(long dataTx) {
        if(dataTx != lastValue) {
            lastValue = dataTx;
            setChanged();
            notifyObservers(dataTx);
        }
    }
}