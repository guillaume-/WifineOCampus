package com.neocampus.wifishared.observables;

import com.neocampus.wifishared.utils.WifiApControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Hirochi ☠ on 22/01/17.
 */

public class ClientObservable extends Observable {
    List<WifiApControl.Client> clients;

    public ClientObservable() {
        clients = new ArrayList<>();
    }

    /**
     *@return the value
     */
    public List<WifiApControl.Client> getClients() {
        return clients;
    }

    public void addClient(WifiApControl.Client client) {
        clients.add(client);
        setChanged();
        notifyObservers(client);
    }

    public void removeClient(WifiApControl.Client client) {
        clients.remove(client);
        setChanged();
        notifyObservers(client);
    }

    public int getCount() {
        return clients.size();
    }
}
