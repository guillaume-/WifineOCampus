package com.neocampus.wifishared.observables;

import com.neocampus.wifishared.sql.database.TableUtilisateur;
import com.neocampus.wifishared.utils.WifiApControl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * ClientObservable permet d'observer l'accès à une session de partage par des clients,
 */
public class ClientObservable extends Observable {

    /**
     * Liste des clients connectés
     */
    private List<WifiApControl.Client> clients;

    /**
     * Historique des clients connectés depuis le lancement
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
     * Renvoi la liste des clients connectés
     *@return liste des clients connectés
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
     * Ajoute à la liste des connectés et dans l'historique, un client s'étant connecté,
     * et notifie le changement d'état
     *
     * @param client Client s'étant connecté à la session de partage
     */
    public void addClient(WifiApControl.Client client) {
        client.date = new WifiApControl.DataSync();
        clients.add(client);
        logClients(client);
        setChanged();
        notifyObservers(client);
    }

    /**
     * Supprime de la liste des connectés, un client s'étant déconnecté de la session de partage,
     * met à jour l'historique et notifie le changement d'état
     * @param client Client s'étant deconnecté à la session de partage
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
     * Ajoute ou met à jour l'historique des connexions
     * @param client client s'étant connecté ou déconnecté
     */
    private void logClients(WifiApControl.Client client){
        if(client.connected)
            historiqueClients.add(client);
        else {
            historiqueClients.lastIndexOf(client);
        }
    }

    /**
     * Réinitialise la liste en cas d'arrêt de la session de partage
     */
    public void clear() {
        for (Iterator<WifiApControl.Client>
             iterator = clients.iterator(); iterator.hasNext(); ) {
            removeClient(iterator.next());
        }
        historiqueClients.clear();
    }

    /**
     * Renvoi le nombre de client connecté
     * @return
     */
    public int getCount() {
        return clients.size();
    }


    /**
     * Restaure les données en cas d'arrêt intempestive
     * @param utilisateur dernier état sauvegardé d'un client avant l'arrêt
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
