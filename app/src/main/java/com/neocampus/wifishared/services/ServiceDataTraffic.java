package com.neocampus.wifishared.services;

import android.content.Context;
import com.neocampus.wifishared.observables.DataObservable;

import java.nio.channels.AlreadyConnectedException;

/*
 * Created by Guillaume RIPOLL on 23/01/17.
 */
public class ServiceDataTraffic implements Runnable{
    private DataObservable observable;
    private volatile boolean running = false;
    private int lookUpPeriode = 10000;

    public ServiceDataTraffic(Context context, DataObservable observable) {
        this.observable = observable;
    }

    private Void doInBackground() {
        try {
            while (running) {
                observable.getValue();
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
        if (running) {
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
