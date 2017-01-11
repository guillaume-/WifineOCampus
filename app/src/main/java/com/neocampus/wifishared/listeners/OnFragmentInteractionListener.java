package com.neocampus.wifishared.listeners;

import android.net.Uri;

import com.neocampus.wifishared.utils.WifiApControl;

import java.util.List;

/**
 * Created by Hirochi ☠ on 10/01/17.
 */

public interface OnFragmentInteractionListener {
    // TODO: Update argument Type and TableName
    void onFragmentInteraction(Uri uri);
    int getClientCount();
    List<WifiApControl.Client> getClients();
}
