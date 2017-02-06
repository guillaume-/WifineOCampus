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

    /**
     * Rend invisible le button de rafraîchissement de l'affichage
     */
    void        hideAppBarRefresh();

    /**
     * Rend visible le button de rafraîchissement de l'affichage
     */
    void        showAppBarRefresh();

    /**
     * Rend invisible le button de sauvegarde de la configuration
     */
    void        hideAppBarSaveConfig();

    /**
     * Rend visible le button de sauvegarde de la configuration
     */
    void        showAppBarSaveConfig();

    /**/
    List<TableConsommation>   getAllConsommations();
    List<TableUtilisateur> getUtilisateurs(int iDConso);
}
