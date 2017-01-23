package com.neocampus.wifishared.listeners;

/**
 * Created by Hirochi ☠ on 10/01/17.
 */

public interface OnActivitySetListener {
    int getLimiteBatterieLevel();
    int getCurrentBatterieLevel();
    float getLimiteDataTrafic();
    int getLimiteBatterie();
    void peekListClients();

    /**/
    void hideAppBarRefresh();
    void showAppBarRefresh();
    void hideAppBarSaveConfig();
    void showAppBarSaveConfig();
}
