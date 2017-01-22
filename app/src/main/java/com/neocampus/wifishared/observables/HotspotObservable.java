package com.neocampus.wifishared.observables;

import java.util.Observable;

/**
 * Created by Hirochi ☠ on 22/01/17.
 */

public class HotspotObservable extends Observable {
    private boolean running;

    public HotspotObservable()
    {
        running = false;
    }

    /**
     *@return the value
     */
    public boolean isRunning() {
        return running;
    }

    /**
     *@param value
     * the value to set
     */
    public void setRunning(boolean value) {
        if(this.running != value) {
            this.running = value;
            setChanged();
            notifyObservers(value);
        }
    }

}
