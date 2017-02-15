package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;

import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;

/**
 * <b>TableConfiguration est la table qui staocke les informations liées à la configuration de la borne.</b>
 * <p>
 * Les informations stockées par cette table sont : la limite de batterie, limite de consommation,
 * la limite de temps et la date démarrage de la borne.
 *
 * </p>
 * @author NALINGA
 */
@Table(TableName = "TableConfiguration", Order = 1)
public class TableConfiguration extends SqlDataSchema {

    /**
     * Nom de la table
     */
    public static final String _NAME = "TableConfiguration" ;

    /**
     * ID de la table
     */
    @Column(Primary = true, Auto = true, Nullable = false, Type = SqlType.INTEGER)
    public static final String _ID =  "Column_ID";

    /**
     * Configuration de point d'accès
     */
    @Column(Type = SqlType.BLOB, Nullable = true, value = "null")
    public static final String _Wifi_Configuration = "Column_Wifi_Configuration";


    /**
     * Colonne de limite de batterie
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "30")
    public static final String _LimiteBatterie = "Column_Batterie";


    /**
     * Colonne de limite de consommation
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "1500000000")
    public static final String _LimiteConsommation = "Column_Consommation";


    /**
     * Colonne de limite de temps
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "14400000")
    public static final String _LimiteTemps = "Column_Temps";

    /**
     * Colonne de date de démarrage de la borne
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _DataT0 = "Column_Data_T0";

    /**
     * Date de déclenchement de l'alarme pour une session courante
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _DateAlarm = "Column_Date_Alarm";

    /**
     * Colonne qui indique si une donnée est sauvegardée de force
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _Stored = "Column_Stored";

    /**
     * Colonne qui indique le code de notification
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "4369")
    public static final String _Notification = "Column_Notification";

    /**
     * Constructeur par défaut de la classe
     * @param values
     */
    public TableConfiguration(ContentValues values) {
        super(values);
    }

    /**
     * Méthode qui retourne identifiant d'une ligne de la table
     * @return ID d'une ligne de la table
     */
    public int getID(){
        return this.values.getAsInteger(_ID);
    }

    /**
     * Méthode qui met à jour identifiant d'une ligne de la table
     * @param iduser
     */
    public void setID(String iduser){
        this.values.put(_ID, iduser);}

    /**
     * Méthode qui retourne les informations de configuration de la borne
     * @return les informations de configuration de la borne
     */
    public byte[] getWifiConfiguration(){
        return  this.values.getAsByteArray(_Wifi_Configuration); }

    /**
     * Méthode qui met à jour les informations de configuration de la borne
     * @param configuration
     */
    public void setWifiConfiguration(byte[] configuration){
        this.values.put(_Wifi_Configuration, configuration);}

    /**
     * Méthode qui retourne la valeur limite de la batterie
     * @return la valeur limite de la batterie
     */
    public int getLimiteBatterie() {
        return this.values.getAsInteger(_LimiteBatterie);
    }

    /**
     * Méthode qui met à jour la valeur limite de la batterie
     * @param value
     */
    public void setLimiteBatterie(int value) {
        this.values.put(_LimiteBatterie, value);
    }

    /**
     * Méthode qui retourne la valeur limite de données offertes
     * @return le Mo offert pour le partage de connexion
     */
    public long getLimiteConsommation() {
        return this.values.getAsLong(_LimiteConsommation);
    }

    /**
     * Méthode qui met à jour la valeur limite de données offertes
     * @param value
     */
    public void setLimiteConsommation(long value) {
        this.values.put(_LimiteConsommation, value);
    }

    /**
     * Méthode qui retourne la valeur limite de temps de partage de connexion
     * @return la limite de temps de partage de connexion
     */
    public long getLimiteTemps() {
        return this.values.getAsLong(_LimiteTemps);
    }

    /**
     * Méthode qui met à jour la valeur limite de temps de partage de connexion
     * @param value
     */
    public void setLimiteTemps(long value) {
        this.values.put(_LimiteTemps, value);
    }

    /**
     * Méthode qui retourne la date de début de partage de connexion
     * @return la date de début de partage de connexion
     */
    public long getDataT0() {
        return this.values.getAsLong(_DataT0);
    }

    /**
     * Méthode qui met à jour la date de début de partage de connexion
     * @param value
     */
    public void setDataT0(long value) {
        this.values.put(_DataT0, value);
    }

    /**
     * Méthode qui retourne la date de déclenchement de l'alarme d'une session courante
     * @return la date de fin de partage de connexion
     */
    public long getDateAlarm() {
        return this.values.getAsLong(_DateAlarm);
    }

    /**
     * Méthode qui met à jour la date de déclenchement de l'alarme d'une session courante
     * @param value
     */
    public void setDateAlarm(long value) {
        this.values.put(_DateAlarm, value);
    }

    /**
     * Méthode qui retourne la valeur indiquant si une information est sauvegardée de force ou pas
     * @return indicateur de sauvegarde de données
     */
    public boolean isStored() {
        return this.values.getAsBoolean(_Stored);
    }

    /**
     * Méthode qui met à jour la valeur indiquant si une information est sauvegardée de force ou pas
     * @param value
     */
    public void setStored(boolean value) {
        this.values.put(_Stored, value);
    }

    /**
     * Méthode qui retourne le code de notification indiquant les notifications actives
     * @return le code de notification
     */
    public int getNotification() {
        return this.values.getAsInteger(_Notification);
    }

    /**
     * Méthode qui met à jour le code de notification
     * @param value
     */
    public void setNotification(int value) {
        this.values.put(_Notification, value);
    }
}
