package com.neocampus.wifishared.observables;

import java.util.Observable;

/**
 * Created by Hirochi ☠ on 06/02/17.
 */

public class NetworkObservable extends Observable {
    private boolean enabled;

    public NetworkObservable() {
        this.enabled = true;
    }

    /**
     *@return the value
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     *@param enabled
     * the value to set
     */
    public void setEnabled(boolean enabled) {
        if(this.enabled != enabled) {
            this.enabled = enabled;
            setChanged();
            notifyObservers(enabled);
        }
    }

}
