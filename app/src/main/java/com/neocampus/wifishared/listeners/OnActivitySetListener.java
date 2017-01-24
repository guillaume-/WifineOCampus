package com.neocampus.wifishared.listeners;

/**
 * Created by Hirochi ☠ on 10/01/17.
 */

public interface OnActivitySetListener {

    float       getLimiteDataTrafic();
    int         getLimiteBatterie();
    long        getLimiteTemps();

    /**/
    void        postRequestListClients();
    void        postRequestDataTraffic();

    /**/
    void        hideAppBarRefresh();
    void        showAppBarRefresh();
    void        hideAppBarSaveConfig();
    void        showAppBarSaveConfig();
}
