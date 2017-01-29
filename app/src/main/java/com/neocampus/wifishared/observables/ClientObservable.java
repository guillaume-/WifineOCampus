package com.neocampus.wifishared.observables;

import com.neocampus.wifishared.sql.database.TableUtilisateur;
import com.neocampus.wifishared.utils.WifiApControl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
        client.date = new WifiApControl.DataSync();
        clients.add(client);
        logClients(client);
        setChanged();
        notifyObservers(client);
    }

    public void removeClient(WifiApControl.Client client) {
        clients.remove(client);
        client.connected = false;
        if(client.date != null) {
            client.date.disconnected = new Date().getTime();
        }
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
        for (Iterator<WifiApControl.Client>
             iterator = clients.iterator(); iterator.hasNext(); ) {
            removeClient(iterator.next());
        }
        historiqueClients.clear();
    }

    public int getCount() {
        return clients.size();
    }


    public void restoreClient(TableUtilisateur utilisateur) {
        WifiApControl.Client client = new WifiApControl.Client(
                utilisateur.getAdressIP() , utilisateur.getAdressMac());
        client.date = new WifiApControl.DataSync();
        client.date.id = utilisateur.getID();
        client.date.connected = utilisateur.getDateDebutCnx();
        client.date.disconnected = utilisateur.getDateFinCnx();
        client.connected = client.date.disconnected == 0;
        if(client.connected) clients.add(client);
        historiqueClients.add(client);
    }

}
