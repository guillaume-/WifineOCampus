package com.neocampus.wifishared.observables;

import java.util.Observable;

import static com.neocampus.wifishared.utils.WifiApControl.STATE_ENABLED;

/**
 * HotspotObservable permet d'observer l'�tat du WIFi-AP et
 * de notifier les changement d'�tat.
 */
public class HotspotObservable extends Observable {

    /**
     * Indique si la configuration actuelle est celle de l'UPS
     */
    private boolean UPS;

    /**
     * Indique l'�tat du WIFI-AP
     */
    private int state;

    /**
     * Indique la session de partage actuelle
     */
    private int sessionId = -1;

    /**
     * Constructeur de l'observateur
     */
    public HotspotObservable()
    {
        state = -1;
    }

    /**
     * Renvoi si la session de partage est active
     *@return vrai si en cours et faux sinon
     */
    public boolean isRunning() {
        return state == STATE_ENABLED;
    }

    /**
     * Tente de modifier l'�tat du WIFI-AP, notifie le changement en cas de r�ussite
     *@param state nouvelle �tat observ�
     */
    public void setState(int state) {
        if(this.state != state) {
            this.state = state;
            setChanged();
            notifyObservers(state);
        }
    }

    /**
     * Renvoi l'�tat du WIFI-Ap
     * @return �tat du WIFI-Ap
     */
    public int getState() {
        return state;
    }

    /**
     * Modifie le type de configuration observ�
     * @param UPS vrai si configuration UPS, faux sinon
     */
    public void setUPS(boolean UPS) {
        this.UPS = UPS;
    }

    /**
     * Renseigne si la configuration actuelle est celle de l'UPS ou non
     * @return vrai si configuration UPS, faux sinon
     */
    public boolean isUPS() {
        return UPS;
    }


    /**
     * D�finie l'identifiant de la session actuelle en cas d'activation par l'utilisateur
     * @param sessionId
     */
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Renvoi l'identifiant de la session actuelle
     * @return l'identifiant de la session actuelle
     */
    public int getSessionId() {
        return sessionId;
    }
}
