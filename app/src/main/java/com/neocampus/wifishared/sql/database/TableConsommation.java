package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.MetaData;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;
import com.neocampus.wifishared.sql.annotations.Trigger;


/**
 * <b>TableConsommation est la table qui staocke les informations liées à la consommation des données de la borne.</b>
 * <p>
 * Les informations stockées par cette table sont : la date de début et de fin d'une connexion, le nombre des utilisateurs,
 * la période de partage de connexion, le Mo défini pour le partage, le Mo à l'instant t, la localisation
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
     * Colonne période de partage
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _Periode = "Column_Periode";

    /**
     * Colonne de Mo défini pour le partage
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _Consommation_T0 = "Column_Consommation_T0";

    /**
     * Colonne de Mo déjà consommé
     */
    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _Consommation_Tx = "Column_Consommation_Tx";

    /**
     * Colonne de localisation
     */
    @Column(Type = SqlType.INTEGER, Nullable = true)
    public static final String _UPS_Location = "Column_UPS_Localisation";

    /**
     * Constructeur par défaut de la classe
     * @param values
     */
    public TableConsommation(ContentValues values) {
        super(values);
    }

    /**
     * Méthode qui retourne identifiant de la table
     * @return un ID
     */
    public int getID(){
        return this.values.getAsInteger(_ID);
    }

    /**
     * Méthode qui retourne la date de début de partage d'une session
     * @return date de début de partage
     */
    @MetaData(name = "date_begin")
    public long getDate(){
        return  this.values.getAsLong(_Date_Start);
    }

    /**
     * Méthode qui met à jour la date
     * @param date
     */
    public void setDate(long date){
        this.values.put(_Date_Start, date);
    }

    /**
     * Méthode qui retourne le nombre de user sur une borne
     * @return le nombre des users d'une borne
     */
    @MetaData(name = "nb_user")
    public int getNbreUser(){
        return this.values.getAsInteger(_NbreUser);
    }

    /**
     * Méthode qui met à jour le nombre de user sur une borne
     * @param nbre_user
     */
    public void setNbreUser(int nbre_user){
        this.values.put(_NbreUser,nbre_user);
    }

    /**
     * Méthode qui retourne la période de partage de connexion
     * @return la période de partage de connexion
     */
    @MetaData(name = "using_periode")
    public int getPeriode(){
        return this.values.getAsInteger(_Periode);
    }

    /**
     * Méthode qui met à jour la période de partage de connexion
     * @param period
     */
    public void setPeriode(int period){
        this.values.put(_Periode,period);
    }

    /**
     * Méthode qui retourne la date d'expiration de durée de partage de connexion
     * @return date de fin de partage de connexion
     */
    @MetaData(name = "date_end")
    public long getDateEnd(){
        return this.values.getAsLong(_Date_End);
    }

    /**
     * Méthode qui met à jour la date d'expiration de durée de partage de connexion
     * @param period
     */
    public void setRunningPeriode(int period){
        this.values.put(_Date_End, period);
    }

    /**
     * Méthode qui retourne la consommation en début de partage
     * @return la consommation en début de partage
     */
    @MetaData(name = "conso_begin")
    public long getConsommationT0(){
        return this.values.getAsLong(_Consommation_T0);
    }

    /**
     * Méthode qui met à jour le Mo de partage défini
     * @param consommation
     */
    public void setConsommationT0(long consommation){
        this.values.put(_Consommation_T0, consommation);
    }

    /**
     * Méthode qui retourne la consommation en fin de partage
     * @return la consommation en fin de partage
     */
    @MetaData(name = "conso_end")
    public long getConsommationTx(){
        return this.values.getAsLong(_Consommation_Tx);
    }

    /**
     * Méthode qui met à jour la consommation en début de partage
     * @param consommation
     */
    public void setConsommationTx(long consommation){
        this.values.put(_Consommation_Tx, consommation);
    }

    /**
     * Méthode qui retourne si la localisation est celui de l'UPS
     * @return la localisation
     */
    public boolean isUPSLocation(){ return  this.values.getAsBoolean(_UPS_Location); }

    /**
     * Méthode qui met à jour la localisation
     * @param value
     */
    public void setUPSLocation(boolean value){ this.values.put(_UPS_Location, value); }

    /**
     * Méthode qui install un trigger dans la base de données, incrémentant le nombre user à chaque ajout
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
     * Méthode qui install un trigger qui calcul la période de connexion d'un user et met à jour la periode total de TableConsommation
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
