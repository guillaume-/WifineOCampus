package com.neocampus.wifishared.observables;

import java.util.Observable;

/**
 * TimeObservable permet d'observer le déclanchement d'une alarme et de notifier
 * celui-ci
 */
public class TimeObservable extends Observable {

    /**
     * Date du déclanchement de l'alarme
     */
    private long date;

    /**
     * Constructeur de l'observateur
     */
    public TimeObservable() {
        this.date = 0;
    }

    /**
     * Renvoi la date du déclanchement de l'alarme
     *@return date du déclanchement de l'alarme
     */
    public long getDate() {
        return date;
    }


    /**
     * Tente de modifier la date actuelle, notifie le changement en cas de succès
     *@param value nouvelle date de déclanchement
     */
    public void setDate(long value) {
        if(this.date != value) {
            this.date = value;
            setChanged();
            notifyObservers(value);
        }
    }
}
