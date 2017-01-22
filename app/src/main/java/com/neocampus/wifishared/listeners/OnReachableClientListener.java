package com.neocampus.wifishared.listeners;

import com.neocampus.wifishared.utils.WifiApControl;

import java.util.List;

/**
 * Created by Hirochi ☠ on 12/01/17.
 */

public interface OnReachableClientListener {
//    void onReachableClient(WifiApControl.Client client);
    void onReachableClients(List<WifiApControl.Client> clients);

}
