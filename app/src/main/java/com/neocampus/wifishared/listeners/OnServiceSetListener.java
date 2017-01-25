package com.neocampus.wifishared.listeners;

import java.util.Observer;

/**
 * Created by Hirochi ☠ on 22/01/17.
 */

public interface OnServiceSetListener {

    void    forceSave();
    void    addObserver(Observer observer);
    void    removeObserver(Observer observer);

    void    peekTimeValue(OnFragmentSetListener listener);
    void    peekDataTraffic(OnFragmentSetListener listener);
    void    peekAllClients(OnReachableClientListener listener);
    void    peekReachableClients(OnReachableClientListener listener);
}
