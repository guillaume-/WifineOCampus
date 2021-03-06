package com.neocampus.wifishared.listeners;

import com.neocampus.wifishared.sql.database.TableConsommation;
import com.neocampus.wifishared.sql.database.TableUtilisateur;

import java.util.List;

/**
 * OnActivitySetListener permet de communiquer avec l'activit� principale {@link com.neocampus.wifishared.activity.MainActivity}
 *
 * @author Hirochi ?
 * @version 1.0.0
 */
public interface OnActivitySetListener {

    /**
     * Renvoi le seuil de consommation de donn�es
     * @return seuil de consommation de donn�es
     */
    float       getLimiteDataTrafic();

    /**
     * Renvoi le seuil de batterie
     * @return seuil de batterie
     */
    int         getLimiteBatterie();

    /**
     * Renvoi le seuil de temps d'activation d'une session de partage
     * @return seuil de temps d'activation
     */
    long        getLimiteTemps();

    /**
     * Renvoi le code de notification
     * @return code de notification
     */
    int         getNotificationCode();

    /**
     * Poste une demande de mise � jour du temps d'activation d'une session de partage
     */
    void        postRequestTimeValue();

    /**
     * Poste une demande de mise � jour des clients
     */
    void        postRequestListClients();

    /**
     * Poste une demande de mise � jour du total de consommation de donn�es
     */
    void        postRequestDataTraffic();

    /**
     * Rend invisible le button de rafra�chissement de l'affichage
     */
    void        hideAppBarRefresh();

    /**
     * Rend visible le button de rafra�chissement de l'affichage
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

    /**
     * Renvoi la liste de tous les sessions de partage
     * @return liste des sessions de partage
     */
    List<TableConsommation>   getAllConsommations();

    /**
     * Renvoi la liste de tous les clients d'une session de partage
     * @param iDConso identifiant de la session de partage
     * @return liste des clients qui se sont conn�ct�s
     */
    List<TableUtilisateur> getUtilisateurs(int iDConso);
}
