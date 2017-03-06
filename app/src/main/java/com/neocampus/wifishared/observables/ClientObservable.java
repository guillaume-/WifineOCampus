package com.neocampus.wifishared.observables;

import com.neocampus.wifishared.sql.database.TableUtilisateur;
import com.neocampus.wifishared.utils.WifiApControl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * ClientObservable permet d'observer l'acc�s � une session de partage par des clients,
 */
public class ClientObservable extends Observable {

    /**
     * Liste des clients connect�s
     */
    private List<WifiApControl.Client> clients;

    /**
     * Historique des clients connect�s depuis le lancement
     */
    private List<WifiApControl.Client> historiqueClients;

    /**
     * Constructeur de l'observateur
     */
    public ClientObservable() {
        clients = new ArrayList<>();
        historiqueClients = new ArrayList<>();
    }

    /**
     * Renvoi la liste des clients connect�s
     *@return liste des clients connect�s
     */
    public List<WifiApControl.Client> getClients() {
        return clients;
    }

    /**
     * Renvoi l'historique des connexions de la session de partage en cours
     * @return l'historique des connexions
     */
    public List<WifiApControl.Client> getHistoriqueClients() {
        return historiqueClients;
    }

    /**
     * Ajoute � la liste des connect�s et dans l'historique, un client s'�tant connect�,
     * et notifie le changement d'�tat
     *
     * @param client Client s'�tant connect� � la session de partage
     */
    public void addClient(WifiApControl.Client client) {
        client.date = new WifiApControl.DataSync();
        clients.add(client);
        logClients(client);
        setChanged();
        notifyObservers(client);
    }

    /**
     * Supprime de la liste des connect�s, un client s'�tant d�connect� de la session de partage,
     * met � jour l'historique et notifie le changement d'�tat
     * @param client Client s'�tant deconnect� � la session de partage
     */
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

    /**
     * Ajoute ou met � jour l'historique des connexions
     * @param client client s'�tant connect� ou d�connect�
     */
    private void logClients(WifiApControl.Client client){
        if(client.connected)
            historiqueClients.add(client);
        else {
            historiqueClients.lastIndexOf(client);
        }
    }

    /**
     * R�initialise la liste en cas d'arr�t de la session de partage
     */
    public void clear() {
        for (Iterator<WifiApControl.Client>
             iterator = clients.iterator(); iterator.hasNext(); ) {
            removeClient(iterator.next());
        }
        historiqueClients.clear();
    }

    /**
     * Renvoi le nombre de client connect�
     * @return le nombre de client connect�
     */
    public int getCount() {
        return clients.size();
    }


    /**
     * Restaure les donn�es en cas d'arr�t intempestive
     * @param utilisateur dernier �tat sauvegard� d'un client avant l'arr�t
     */
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
