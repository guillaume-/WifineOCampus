package com.neocampus.wifishared.observables;

import android.net.TrafficStats;
import java.util.Observable;

/*
 * Created by Guillaume RIPOLL on 23/01/17.
 */
public class DataObservable extends Observable {
    private long dataT0;
    private long dataMax;
    private boolean isUsable;

    public  DataObservable(long userDataLimit) {
        this.dataT0 = TrafficStats.getMobileRxBytes();
        this.isUsable = TrafficStats.getMobileRxBytes() != TrafficStats.UNSUPPORTED;
        this.dataT0 += TrafficStats.getMobileTxBytes();
        this.dataMax = userDataLimit;
    }

    /**
     *@return the value
     */
    public long getValue() {
        if(isUsable) {
            long dataTx = TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes() - dataT0;
            if(dataTx > dataMax)
                notifyObservers();
            return dataTx;
        }
        return 0;
    }
}
