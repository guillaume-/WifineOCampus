package com.neocampus.wifishared.listeners;

import java.util.Observer;

/**
 * Created by Hirochi ☠ on 22/01/17.
 */

public interface OnServiceSetListener {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void peekAllClients(OnReachableClientListener listener, boolean reachableOnly);
    void forceSave();
}
