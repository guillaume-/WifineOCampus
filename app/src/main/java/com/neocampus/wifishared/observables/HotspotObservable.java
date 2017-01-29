package com.neocampus.wifishared.observables;

import java.util.Observable;

import static com.neocampus.wifishared.utils.WifiApControl.STATE_ENABLED;

/**
 * Created by Hirochi ☠ on 22/01/17.
 */

public class HotspotObservable extends Observable {
    private boolean UPS;
    private int state;
    private int sessionId = -1;

    public HotspotObservable()
    {
        state = -1;
    }

    /**
     *@return the value
     */
    public boolean isRunning() {
        return state == STATE_ENABLED;
    }

    /**
     *@param state
     * the value to set
     */
    public void setState(int state) {
        if(this.state != state) {
            this.state = state;
            setChanged();
            notifyObservers(state);
        }
    }

    public int getState() {
        return state;
    }

    public void setUPS(boolean UPS) {
        this.UPS = UPS;
    }

    public boolean isUPS() {
        return UPS;
    }


    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSessionId() {
        return sessionId;
    }
}
