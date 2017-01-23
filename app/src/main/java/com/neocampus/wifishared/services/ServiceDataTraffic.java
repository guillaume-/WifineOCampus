package com.neocampus.wifishared.services;

import android.content.Context;
import android.net.TrafficStats;
import com.neocampus.wifishared.observables.DataObservable;
import java.nio.channels.AlreadyConnectedException;

/*
 * Created by Guillaume RIPOLL on 23/01/17.
 */
public class ServiceDataTraffic implements Runnable{
    private DataObservable observable;
    private volatile boolean running = false;
    private int lookUpPeriode = 1000;
    private long dataT0;
    private boolean isUsable;

    public ServiceDataTraffic(Context context, DataObservable observable) {
        this.observable = observable;
        this.dataT0 = TrafficStats.getMobileRxBytes();
        this.isUsable = TrafficStats.getMobileRxBytes() != TrafficStats.UNSUPPORTED;
        this.dataT0 += TrafficStats.getMobileTxBytes();
    }

    public void refreshFromDataBase(long newT0){
        dataT0 = newT0;
    }

    private Void doInBackground() {
        long dataTx;
        if (isUsable) try {
            while (running) {
                dataTx = TrafficStats.getMobileRxBytes()+TrafficStats.getMobileTxBytes()-dataT0;
                observable.setValue(dataTx);
                Thread.sleep(lookUpPeriode);
            }
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
