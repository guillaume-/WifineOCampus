package com.neocampus.wifishared.observables;

import java.util.Observable;

/**
 * Created by Hirochi ☠ on 22/01/17.
 */

public class TimeObservable extends Observable {
    private long date;

    public TimeObservable() {
        this.date = 0;
    }

    /**
     *@return the value
     */
    public long getDate() {
        return date;
    }


    /**
     *@param value
     * the value to set
     */
    public void setDate(long value) {
        if(this.date != value) {
            this.date = value;
            setChanged();
            notifyObservers(value);
        }
    }
}
