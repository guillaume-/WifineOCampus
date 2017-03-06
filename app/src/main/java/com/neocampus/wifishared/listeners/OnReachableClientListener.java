package com.neocampus.wifishared.listeners;

import com.neocampus.wifishared.utils.WifiApControl;

import java.util.List;

/**
 * OnReachableClientListener permet de transmettre une liste de clients
 *
 * @author Hirochi ?
 * @version 1.0.0
 */
public interface OnReachableClientListener {

    /**
     * Re�oit une liste de clients
     * @param clients liste de clients
     */
    void onReachableClients(List<WifiApControl.Client> clients);

}
