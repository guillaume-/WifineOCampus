package com.neocampus.wifishared.observables;

import java.util.Observable;

/**
 * BatterieObservable permet d'observer le niveau de la batterie et de notifier les changements
 */
public class BatterieObservable extends Observable {

    /**
     * Niveau de la batterie
     */
    private int level;

    /**
     * Constructeur de l'observable
     */
    public  BatterieObservable() {
        this.level = -1;
    }

    /**
     * Renvoi le niveau de la batterie
     *@return niveau de la batterie
     */
    public int getValue() {
        return level;
    }

    /**
     * Tente de modifier le niveau observé de la batterie et notifie le changement de valeur,
     * si de réussite
     *@param value niveau de la batterie
     */
    public void setValue(int value) {
        if(this.level != value) {
            this.level = value;
            setChanged();
            notifyObservers(value);
        }
    }
}
