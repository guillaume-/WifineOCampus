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
    List<WifiApControl.Client> historiqueClients;

    public ClientObservable() {
        clients = new ArrayList<>();
        historiqueClients = new ArrayList<>();
    }

    /**
     *@return the value
     */
    public List<WifiApControl.Client> getClients() {
        return clients;
    }

    public List<WifiApControl.Client> getHistoriqueClients() {
        return historiqueClients;
    }

    public void addClient(WifiApControl.Client client) {
        clients.add(client);
        logClients(client);
        setChanged();
        notifyObservers(client);
    }

    public void removeClient(WifiApControl.Client client) {
        clients.remove(client);
        logClients(client);
        setChanged();
        notifyObservers(client);
    }

    private void logClients(WifiApControl.Client client){
        if(client.connected)
            historiqueClients.add(client);
        else {
            historiqueClients.lastIndexOf(client);
        }
    }

    public void clear() {
        clients.clear();
        historiqueClients.clear();
    }

    public int getCount() {
        return clients.size();
    }


}
