package com.neocampus.wifishared.listeners;

import java.util.Observer;

/**
 * OnServiceSetListener permet de communiquer avec le service prinpale {@link com.neocampus.wifishared.services.ServiceNeOCampus}
 *
 * @author Abdellah
 */
public interface OnServiceSetListener {

    /**
     * Force la sauvegarde des données dans la base de données
     */
    void    storeInDataBase();

    /**
     * Permet de s'inscrire dans la liste d'un observateur afin d'obtenir une notification en cas de changement d'état
     * @param observer objet recevant la notification
     */
    void    addObserver(Observer observer);

    /**
     * Permet de se désinscrire de la liste d'un observateur
     * @param observer objet recevant la notification
     */
    void    removeObserver(Observer observer);

    /**
     * Transmet le temps d'activation restant à un fragment via l'interface de communication
     * @param listener interface de communication avec fragment
     */
    void    peekTimeValue(OnFragmentSetListener listener);

    /**
     * Transmet le temps de total de consommation de données à un fragment via l'interface de communication
     * @param listener interface de communication avec fragment
     */
    void    peekDataTraffic(OnFragmentSetListener listener);

    /**
     * Transmet l'historique des connexions pour la session de partage actuelle à un fragment,
     * en utilisant son interface de communication
     * @param listener interface de communication avec fragment
     */
    void    peekAllClients(OnReachableClientListener listener);

    /**
     * Transmet la liste de clients connectés pour la session de partage actuelle à un fragment,
     * en utilisant son interface de communication
     * @param listener interface de communication avec fragment
     */
    void    peekReachableClients(OnReachableClientListener listener);

    /**
     * Réinitialise le total de données
     */
    void    resetBaseT0();
}
