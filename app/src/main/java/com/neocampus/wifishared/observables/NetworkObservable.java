package com.neocampus.wifishared.observables;

import java.util.Observable;

/**
 * NetworkObservable permet d'observer l'état de la connexion internet mobile de l'utilisateur
 *
 */
public class NetworkObservable extends Observable {

    /**
     * Indique si internet est accessible
     */
    private boolean enabled;

    /**
     * Constructeur de l'observateur
     */
    public NetworkObservable() {
        this.enabled = true;
    }

    /**
     * Renseigne si la connexion internet mobile est accessible
     *@return vrai si accessible, faux sinon
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Tente de modifier l'état de l'accès internet mobile,
     * notifie le changement en cas de succès
     *@param enabled vrai si accessible, faux sinon
     */
    public void setEnabled(boolean enabled) {
        if(this.enabled != enabled) {
            this.enabled = enabled;
            setChanged();
            notifyObservers(enabled);
        }
    }

}
