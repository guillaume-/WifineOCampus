package com.neocampus.wifishared.listeners;

import com.neocampus.wifishared.utils.WifiApControl;

import java.util.List;

/**
 * Created by Hirochi ☠ on 10/01/17.
 */

public interface OnActivitySetListener {
    List<WifiApControl.Client> getReachableClients(OnReachableClientListener listener);
}
