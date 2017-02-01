package com.neocampus.wifishared.listeners;

import com.neocampus.wifishared.sql.database.TableConsommation;
import com.neocampus.wifishared.sql.database.TableUtilisateur;

import java.util.List;

/**
 * Created by Hirochi ☠ on 10/01/17.
 */

public interface OnActivitySetListener {

    float       getLimiteDataTrafic();
    int         getLimiteBatterie();
    long        getLimiteTemps();

    /**/
    void        postRequestTimeValue();
    void        postRequestListClients();
    void        postRequestDataTraffic();

    /**/
    void        hideAppBarRefresh();
    void        showAppBarRefresh();
    void        hideAppBarSaveConfig();
    void        showAppBarSaveConfig();

    /**/
    List<TableConsommation>   getAllConsommations();
    List<TableUtilisateur> getUtilisateurs(int iDConso);
}
