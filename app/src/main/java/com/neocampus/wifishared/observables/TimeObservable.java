package com.neocampus.wifishared.observables;

import java.util.Observable;

/**
 * TimeObservable permet d'observer le d�clanchement d'une alarme et de notifier
 * celui-ci
 */
public class TimeObservable extends Observable {

    /**
     * Date du d�clanchement de l'alarme
     */
    private long date;

    /**
     * Constructeur de l'observateur
     */
    public TimeObservable() {
        this.date = 0;
    }

    /**
     * Renvoi la date du d�clanchement de l'alarme
     *@return date du d�clanchement de l'alarme
     */
    public long getDate() {
        return date;
    }


    /**
     * Tente de modifier la date actuelle, notifie le changement en cas de succ�s
     *@param value nouvelle date de d�clanchement
     */
    public void setDate(long value) {
        if(this.date != value) {
            this.date = value;
            setChanged();
            notifyObservers(value);
        }
    }
}
