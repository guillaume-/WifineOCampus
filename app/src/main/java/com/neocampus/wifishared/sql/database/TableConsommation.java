package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.MetaData;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;
import com.neocampus.wifishared.sql.annotations.Trigger;


/**
 * <b>TableConsommation est la table qui staocke les informations li�es � la consommation des donn�es de la borne.</b>
 * <p>
 * Les informations stock�es par cette table sont : la date de d�but et de fin d'une connexion, le nombre des utilisateurs,
 * la p�riode de partage de connexion, le Mo d�fini pour le partage, le Mo � l'instant t, la localisation
 *
 * </p>
 * @author NALINGA
 */
@Table(TableName = "TableConsommation", Order = 1)
public class TableConsommation extends SqlDataSchema {

    /**
     * Nom de la table
     */
    public static final String _NAME = "TableConsommation" ;

    /**
     * ID de la table
     */
    @Column(Primary = true, Auto = true, Nullable = false, Type = SqlType.INTEGER)
    public static final String _ID =  "Column_ID";

    /**
     * Colonne date
     */
    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String _Date_Start = "Column_Date_Begin";

    /**
     * Colonne date expiration de partage
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _Date_End = "Column_Date_End";

    /**
     * Colonne nombre user
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _NbreUser = "Column_NombreUser";

    /**
     * Colonne p�riode de partage
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _Periode = "Column_Periode";

    /**
     * Colonne de Mo d�fini pour le partage
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _Consommation_T0 = "Column_Consommation_T0";

    /**
     * Colonne de Mo d�j� consomm�
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _Consommation_Tx = "Column_Consommation_Tx";

    /**
     * Colonne de localisation
     */
    @Column(Type = SqlType.INTEGER, Nullable = true)
    public static final String _UPS_Location = "Column_UPS_Localisation";

    /**
     * Constructeur par d�faut de la classe
     * @param values
     */
    public TableConsommation(ContentValues values) {
        super(values);
    }

    /**
     * M�thode qui retourne identifiant de la table
     * @return un ID
     */
    public int getID(){
        return this.values.getAsInteger(_ID);
    }

    /**
     * M�thode qui retourne la date de d�but de partage d'une session
     * @return date de d�but de partage
     */
    @MetaData(name = "date_begin")
    public long getDate(){
        return  this.values.getAsLong(_Date_Start);
    }

    /**
     * M�thode qui met � jour la date
     * @param date
     */
    public void setDate(long date){
        this.values.put(_Date_Start, date);
    }

    /**
     * M�thode qui retourne le nombre de user sur une borne
     * @return le nombre des users d'une borne
     */
    @MetaData(name = "nb_user")
    public int getNbreUser(){
        return this.values.getAsInteger(_NbreUser);
    }

    /**
     * M�thode qui met � jour le nombre de user sur une borne
     * @param nbre_user
     */
    public void setNbreUser(int nbre_user){
        this.values.put(_NbreUser,nbre_user);
    }

    /**
     * M�thode qui retourne la p�riode de partage de connexion
     * @return la p�riode de partage de connexion
     */
    @MetaData(name = "using_periode")
    public int getPeriode(){
        return this.values.getAsInteger(_Periode);
    }

    /**
     * M�thode qui met � jour la p�riode de partage de connexion
     * @param period
     */
    public void setPeriode(int period){
        this.values.put(_Periode,period);
    }

    /**
     * M�thode qui retourne la date d'expiration de dur�e de partage de connexion
     * @return date de fin de partage de connexion
     */
    @MetaData(name = "date_end")
    public long getDateEnd(){
        return this.values.getAsLong(_Date_End);
    }

    /**
     * M�thode qui met � jour la date d'expiration de dur�e de partage de connexion
     * @param period
     */
    public void setRunningPeriode(int period){
        this.values.put(_Date_End, period);
    }

    /**
     * M�thode qui retourne la consommation en d�but de partage
     * @return la consommation en d�but de partage
     */
    @MetaData(name = "conso_begin")
    public long getConsommationT0(){
        return this.values.getAsLong(_Consommation_T0);
    }

    /**
     * M�thode qui met � jour le Mo de partage d�fini
     * @param consommation
     */
    public void setConsommationT0(long consommation){
        this.values.put(_Consommation_T0, consommation);
    }

    /**
     * M�thode qui retourne la consommation en fin de partage
     * @return la consommation en fin de partage
     */
    @MetaData(name = "conso_end")
    public long getConsommationTx(){
        return this.values.getAsLong(_Consommation_Tx);
    }

    /**
     * M�thode qui met � jour la consommation en d�but de partage
     * @param consommation
     */
    public void setConsommationTx(long consommation){
        this.values.put(_Consommation_Tx, consommation);
    }

    /**
     * M�thode qui retourne si la localisation est celui de l'UPS
     * @return la localisation
     */
    public boolean isUPSLocation(){ return  this.values.getAsBoolean(_UPS_Location); }

    /**
     * M�thode qui met � jour la localisation
     * @param value
     */
    public void setUPSLocation(boolean value){ this.values.put(_UPS_Location, value); }

    /**
     * M�thode qui install un trigger dans la base de donn�es, incr�mentant le nombre user � chaque ajout
     * d'un utilisateur d'une borne
     * @param database
     * @param trigger_name
     */
    @Trigger(name = "TRIGGER_NB_USER")
    public static void triggerNbreUser(SQLiteDatabase database, String trigger_name)
    {
        String s = String.format("CREATE TRIGGER %s BEFORE INSERT ON %s FOR EACH ROW " +
                "\nBEGIN\n" +
                "UPDATE %s SET %s = %s + 1 WHERE %s = new.%s;" +
                "\nEND;\n",
                trigger_name, TableUtilisateur._NAME, _NAME,
                _NbreUser, _NbreUser, _ID, TableUtilisateur._ID_CONSO);
        database.execSQL(s);
    }

    /**
     * M�thode qui install un trigger qui calcul la p�riode de connexion d'un user et met � jour la periode total de TableConsommation
     */
    @Trigger(name = "TRIGGER_PERIODE_USER")
    public static void triggerPeriodeUser(SQLiteDatabase database, String trigger_name)
    {
        String s = String.format("CREATE TRIGGER %s BEFORE UPDATE OF %s ON %s " +
                "\nBEGIN\n" +
                "UPDATE %s SET %s = %s + (new.%s - old.%s) WHERE %s = old.%s;" +
                "\nEND;\n",
                trigger_name, TableUtilisateur._DATE_FIN_CNX, TableUtilisateur._NAME, _NAME,
                _Periode, _Periode, TableUtilisateur._DATE_FIN_CNX, TableUtilisateur._DATE_DEBUT_CNX,
                _ID, TableUtilisateur._ID_CONSO);

        database.execSQL(s);
    }


}
