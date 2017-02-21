package com.neocampus.wifishared.services;

import android.content.Context;
import android.net.TrafficStats;

import com.neocampus.wifishared.observables.DataObservable;
import com.neocampus.wifishared.utils.TrafficUtils;

import java.nio.channels.AlreadyConnectedException;

/*
 * Created by Guillaume RIPOLL on 23/01/17.
 * ServiceDataTraffic : background service
 * Call Observable every lookUpPeriode
 */
public class ServiceDataTraffic implements Runnable{
    private DataObservable observable;
    private volatile boolean running = false;
    private int lookUpPeriode = 1000;
    private long dataT0, baseT0 = 0;
    private boolean isUsable;

    public ServiceDataTraffic(Context context, DataObservable observable) {
        this.observable = observable;
//
//        this.dataT0 = TrafficStats.getMobileRxBytes();
//        this.isUsable = TrafficStats.getMobileRxBytes() != TrafficStats.UNSUPPORTED;
//        this.dataT0 += TrafficStats.getMobileTxBytes();

        this.isUsable = TrafficStats.getMobileRxBytes() != TrafficStats.UNSUPPORTED;
    }

    public void refreshFromDataBase(long newT0){
        baseT0 += newT0;
    }

    public long getBaseT0() {
        return baseT0;
    }

    private Void doInBackground() {
        long dataTx = 0;
        if (isUsable) try {
            this.dataT0 = TrafficUtils.getRxBytes()+TrafficUtils.getTxBytes();
            while (running) {
                dataTx = baseT0 + ((TrafficUtils.getRxBytes()+TrafficUtils.getTxBytes()) - dataT0);
//                dataTx = baseT0 + ((TrafficStats.getMobileRxBytes()+TrafficStats.getMobileTxBytes())-dataT0);
                observable.setValue(dataTx);
                Thread.sleep(lookUpPeriode);
            }
            baseT0 = dataTx;
        } catch (InterruptedException e) {
        }
        return null;
    }

    @Override
    public void run() {
        doInBackground();
    }

    public void startWatchDog(int lookUpPeriode) {
        if(running) {
            throw new AlreadyConnectedException();
        } else {
            running = true;
            this.lookUpPeriode = lookUpPeriode;
            new Thread(this).start();
        }
    }

    public void stopWatchDog() {
        running = false;
    }
}
