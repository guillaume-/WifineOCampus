package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;

import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.MetaData;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;

/**
 * <b>TableUtilisateur est la table qui staocke les informations des utilisatuers connect�s � une borne.</b>
 * <p>
 * Les informations stock�es par cette table sont : adresse mac, adresse ip,
 * dur�e de connexion sur une borne.
 *
 * </p>
 *
 * @author NALINGA
 */
@Table(TableName = "TableUtilisateur", Order = 3)
public class TableUtilisateur extends SqlDataSchema {

    /**
     * Nom de la table
     */
    public static final String _NAME = "TableUtilisateur" ;

    /**
     * ID de TableUtilisateur
     */
    @Column(Primary = true, Auto = true, Nullable = false, Type = SqlType.INTEGER)
    public static final String _ID =  "Column_ID";

    /**
     * ID de TableConsommation
     */
    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String _ID_CONSO =  "Column_ID_CONSO";

    /**
     * Colonne des adresses MAC
     */
    @Column(Type = SqlType.TEXT, Nullable = false)
    public static final String _ADRESSE_MAC =  "Column_ADRESSE_MAC";

    /**
     * Colonne des adresses IP
     */
    @Column(Type = SqlType.TEXT, Nullable = false)
    public static final String _ADRESSE_IP =  "Column_ADRESSE_IP";

    /**
     * Colonne des dates de d�but d'une connexion
     */
    @Column(Type = SqlType.INTEGER, Nullable = true)
    public static final String _DATE_DEBUT_CNX =  "Column_DATE_DEBUT_CNX";

    /**
     * Colonne des dates de fin d'une connexion
     */
    @Column(Type = SqlType.INTEGER, Nullable = true)
    public static final String _DATE_FIN_CNX=  "Column_DATE_FIN_CNX";

    /**
     * Constructeur par defaut de la classe
     * @param values
     */
    public TableUtilisateur(ContentValues values) {
        super(values);
    }

    /**
     * Cette m�thode retourne identifiant de la TableUtilisateur
     * @return ID d'un utilisateur
     */
    public int getID(){
        return this.values.getAsInteger(_ID);
    }

    /**
     * Cette m�thode retourne identifiant de TableConsommation
     * @return ID_CONSO de TableConsommation
     */
    public int getIdConso(){ return this.values.getAsInteger(_ID_CONSO); }

    /**
     * Cette m�thode met � jour ID de TableConsommation
     * @param id_conso
     */
    public void setIdConso(int id_conso){ this.values.put(_ID_CONSO, id_conso);}

    /**
     * Cette m�thode retourne adresse MAC d'un user de la borne
     * @return une adresse MAC connect�e sur la borne
     */
    public String getAdressMac(){ return this.values.getAsString(_ADRESSE_MAC); }

    /**
     * Cette m�thode met � jour adresse MAC
     * @param adresse_mac
     */
    public void setAdressMac(String adresse_mac){ this.values.put(_ADRESSE_MAC, adresse_mac); }

    /**
     * Cette m�thode retourne adresse IP d'un user de la borne
     * @return une adresse IP connect�e sur la borne
     */
    @MetaData(name = "host")
    public String getAdressIP(){ return this.values.getAsString(_ADRESSE_IP); }

    /**
     * Cette m�thode met � jour adresse IP d'un user de la borne
     * @param adresse_ip
     */
    public void setAdressIP(String adresse_ip){ this.values.put(_ADRESSE_MAC, adresse_ip); }

    /**
     * Cette m�thode retourne date de d�but de connexion
     * @return la date de d�but de connexion
     */
    @MetaData(name = "date_connexion")
    public long getDateDebutCnx(){ return this.values.getAsLong(_DATE_DEBUT_CNX); }

    /**
     * Cette m�thode met � jour date de d�but de connexion
     * @param date_debut_cnx
     */
    public void setDateDebutCnx(long date_debut_cnx){ this.values.put(_DATE_DEBUT_CNX, date_debut_cnx); }

    /**
     * Cette m�thode retourne la date de fin de connexion
     * @return la date de fin de connexion
     */
    @MetaData(name = "date_deconnexion")
    public long getDateFinCnx(){ return this.values.getAsLong(_DATE_FIN_CNX); }

    /**
     * Cette m�thode met � jour la date de fin de connexion
     * @param date_fin_cnx
     */
    public void setDateFinCnx(long date_fin_cnx){ this.values.put(_DATE_FIN_CNX, date_fin_cnx); }


}
