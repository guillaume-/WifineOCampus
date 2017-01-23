package com.neocampus.wifishared.services;

import android.content.Context;

import com.neocampus.wifishared.listeners.OnReachableClientListener;
import com.neocampus.wifishared.observables.ClientObservable;
import com.neocampus.wifishared.utils.WifiApControl;

import java.nio.channels.AlreadyConnectedException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Hirochi ☠ on 22/01/17.
 */

public class ServiceTaskClients implements Runnable , OnReachableClientListener{
    private ClientObservable observable;
    private WifiApControl apControl;
    private volatile boolean running = false;
    private int lookUpPeriode = 20000;

    public ServiceTaskClients(Context context, ClientObservable observable) {
        this.observable = observable;
        this.apControl = WifiApControl.getInstance(context);
    }

    protected Void doInBackground() {
        try {
            while (running) {
                apControl.getReachableClients(lookUpPeriode / 2, this);
                Thread.sleep(lookUpPeriode);
            }
        } catch (InterruptedException e) {
        }
        return null;
    }

    @Override
    public synchronized void onReachableClients(List<WifiApControl.Client> pclients) {
        List<WifiApControl.Client> clients = observable.getClients();
        Iterator<WifiApControl.Client> iterator = clients.iterator();

        while (iterator.hasNext())
        {
            WifiApControl.Client client = iterator.next();
            if(!pclients.contains(client)) {
                client.connected = false;
                client.date_disconnected = new Date().getTime();
                observable.removeClient(client);
            }
        }

        for (WifiApControl.Client client: pclients) {
            if(!clients.contains(client)){
                client.connected = true;
                client.date_connected = new Date().getTime();
                observable.addClient(client);
            }
        }
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
