package com.neocampus.wifishared.observables;

import java.util.Observable;

/**
 * Created by Hirochi ☠ on 22/01/17.
 */

public class BatterieObservable extends Observable {
    private int level;

    public  BatterieObservable() {
        this.level = -1;
    }

    /**
     *@return the value
     */
    public int getValue() {
        return level;
    }

    /**
     *@param value
     * the value to set
     */
    public void setValue(int value) {
        if(this.level != value) {
            this.level = value;
            setChanged();
            notifyObservers(value);
        }
    }
}
