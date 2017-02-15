package com.neocampus.wifishared.sql.manage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.neocampus.wifishared.sql.database.TableConfiguration;
import com.neocampus.wifishared.sql.database.TableConsommation;
import com.neocampus.wifishared.sql.database.TableUtilisateur;

import java.util.ArrayList;
import java.util.Locale;

/**
 * <b>Classe qui rassemble les fonctions de manipulation de la base de données.</b>
 * <p>
 *     Cette classe contient des fonctions : insertion, mise à jour et suppression.
 * </p>
 *
 * @author NALINGA
 */
public class SQLManager {

    private SQLiteDatabase database;

    /**
     * Méthode qui initialise une instance de communication
     * @param context
     */
    public SQLManager(Context context) {
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLDatabaseManager.initializeInstance(dbHelper);
    }

    /**
     * Méthode qui ouvre une instance de communication
     * @throws SQLException
     */
    public synchronized void open() throws SQLException {
        database = SQLDatabaseManager.getInstance().openDatabase();
    }

    public synchronized void close() {
        SQLDatabaseManager.getInstance().closeDatabase();
    }

//    CREATE  TRIGGER trigger_name [BEFORE|AFTER] UPDATE OF column_name
//    ON table_name
//    BEGIN
//    -- Trigger logic goes here....
//    END;

//    CREATE TRIGGER update_customer_address UPDATE OF address ON customers
//    BEGIN
//    UPDATE orders SET address = new.address WHERE customer_name = old.TableName;
//    END;

//    IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE Name = 'Trigger' AND Type = 'TR')

    /**
     * Méthode qui récupère l'ensemble des données contenues dans le curseur.
     * @param c
     * @return une liste des données contenues dans le curseur
     */
    private static ContentValues cursorToContentValues(Cursor c) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < c.getColumnCount(); i++) {
            switch (c.getType(i)) {
                case Cursor.FIELD_TYPE_INTEGER:
                    int intVal = c.getInt(i);
                    long longVal = c.getLong(i);
                    values.put(c.getColumnName(i), (intVal == longVal) ? intVal : longVal);
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    values.put(c.getColumnName(i), +c.getFloat(i));
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    values.put(c.getColumnName(i), c.getString(i));
                    break;
                case Cursor.FIELD_TYPE_BLOB:
                    values.put(c.getColumnName(i), c.getBlob(i));
                    break;
                case Cursor.FIELD_TYPE_NULL:
                    values.putNull(c.getColumnName(i));
                    break;
            }
        }
        return values;
    }

    /*==================== Débuts des fonctions de manipulations de TableConfiguration ====================*/

    /**
     * Méthode de mise à jour de colonne configuration de TableConfiguration
     * @param config
     * @return ID de l'insertion
     */
    public int setConfiguration(byte[] config) {
        int result;
        String selection = TableConfiguration._ID + " = 1";

        ContentValues value = new ContentValues();

        value.put(TableConfiguration._Wifi_Configuration, config);

        if ((result = database.update(TableConfiguration._NAME, value, selection, null)) == 0) {
            return (int) database.insert(TableConfiguration._NAME, null, value);
        }
        return result;
    }

    /**
     * Méthode de mise à jour de colonne limite de batterie de TableConfiguration
     * @param limite_batterie
     * @return ID de l'insertion
     */
    public int setConfigurationB(int limite_batterie) {
        int result;
        String selection = TableConfiguration._ID + " = 1";

        ContentValues value = new ContentValues();

        value.put(TableConfiguration._LimiteBatterie, limite_batterie);

        if ((result = database.update(TableConfiguration._NAME, value, selection, null)) == 0) {
            return (int) database.insert(TableConfiguration._NAME, null, value);
        }
        return result;
    }

    /**
     * Méthode de mise à jour de colonne limite de consommation de TableConfiguration
     * @param limite_conso
     * @return ID de l'insertion
     */
    public int setConfigurationC(long limite_conso) {
        int result;
        String selection = TableConfiguration._ID + " = 1";

        ContentValues value = new ContentValues();

        value.put(TableConfiguration._LimiteConsommation, limite_conso);

        if ((result = database.update(TableConfiguration._NAME, value, selection, null)) == 0) {
            return (int) database.insert(TableConfiguration._NAME, null, value);
        }
        return result;
    }

    /**
     * Méthode de mise à jour de colonne limite de temps de TableConfiguration
     * @param limite_temps
     * @return ID de l'insertion
     */
    public int setConfigurationT(long limite_temps) {
        int result;
        String selection = TableConfiguration._ID + " = 1";

        ContentValues value = new ContentValues();

        value.put(TableConfiguration._LimiteTemps, limite_temps);

        if ((result = database.update(TableConfiguration._NAME, value, selection, null)) == 0) {
            return (int) database.insert(TableConfiguration._NAME, null, value);
        }
        return result;
    }

    /**
     * Méthode de mise à jour de colonne date de début de partage de TableConfiguration
     * @param dataT0
     * @return ID de l'insertion
     */
    public int setConfigurationD(long dataT0) {
        int result;
        String selection = TableConfiguration._ID + " = 1";

        ContentValues value = new ContentValues();

        value.put(TableConfiguration._DataT0, dataT0);

        if ((result = database.update(TableConfiguration._NAME, value, selection, null)) == 0) {
            return (int) database.insert(TableConfiguration._NAME, null, value);
        }
        return result;
    }

    /**
     * Méthode de mise à jour de colonne date de fin de partage de TableConfiguration
     * @param date_alarm
     * @return ID de l'insertion
     */
    public int setConfigurationA(long date_alarm) {
        int result;
        String selection = TableConfiguration._ID + " = 1";

        ContentValues value = new ContentValues();

        value.put(TableConfiguration._DateAlarm, date_alarm);

        if ((result = database.update(TableConfiguration._NAME, value, selection, null)) == 0) {
            return (int) database.insert(TableConfiguration._NAME, null, value);
        }
        return result;
    }

    /**
     * Méthode de mise à jour de colonne qui indique un sauvegarde forcé de TableConfiguration
     * @param stored
     * @return ID de l'insertion
     */
    public int setConfigurationS(boolean stored) {
        int result;
        String selection = TableConfiguration._ID + " = 1";

        ContentValues value = new ContentValues();

        value.put(TableConfiguration._Stored, stored);

        if ((result = database.update(TableConfiguration._NAME, value, selection, null)) == 0) {
            return (int) database.insert(TableConfiguration._NAME, null, value);
        }
        return result;
    }

    /**
     * Méthode de mise à jour de colonne qui indique le code de notification
     * @param notificationCode
     * @return ID de l'insertion
     */
    public int setConfigurationN(int notificationCode) {
        int result;
        String selection = TableConfiguration._ID + " = 1";

        ContentValues value = new ContentValues();

        value.put(TableConfiguration._Notification, notificationCode);

        if ((result = database.update(TableConfiguration._NAME, value, selection, null)) == 0) {
            return (int) database.insert(TableConfiguration._NAME, null, value);
        }
        return result;
    }

    /**
     * Méthode qui retourne les informations de configuration de TableConfiguration
     * @return une liste contenant les données de TableConfiguration
     */
    public TableConfiguration getConfiguration() {
        Cursor c = null;
        TableConfiguration tableConfiguration = null;
        try {
            String DexUtils = String.format(Locale.FRANCE,
                    "SELECT * FROM %s WHERE %S = 1",
                    TableConfiguration._NAME, TableConfiguration._ID);
            c = database.rawQuery(DexUtils, null);
            c.moveToFirst();
            if (!c.isAfterLast()) {
                tableConfiguration =
                        new TableConfiguration(cursorToContentValues(c));
            }
            else {
                setConfiguration(null);
                return getConfiguration();
            }
            return tableConfiguration;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /*==================== Fin des fonctions de manipulations de TableConfiguration ====================*/


    /*==================== Debut de fonctions de manipulation de TableConsommation ====================*/


    /**
     * Méthode pour une insertion de date et data definie dans la TableConsommation
     * @param date
     * @param dataT0
     * @return ID de l'insertion
     */
    public int newConsommation(long date, long dataT0, boolean isUPS)
    {
        ContentValues value = new ContentValues();
        value.put(TableConsommation._Date_Start, date);
        value.put(TableConsommation._Consommation_T0, dataT0);
        value.put(TableConsommation._UPS_Location, isUPS);
        return (int) database.insert(TableConsommation._NAME, null, value);
    }

    /**
     * Méthode pour une nouvelle insertion dans la TableConsommation
     * @param date
     * @param nbre_user
     * @param periode
     * @param dataT0
     * @param dataTx
     * @return ID de l'insertion
     */
    public int addConsommation(long date,
                               int nbre_user, int periode, long dataT0, long dataTx) {

        ContentValues value = new ContentValues();

        value.put(TableConsommation._Date_Start, date);
        value.put(TableConsommation._NbreUser, nbre_user);
        value.put(TableConsommation._Periode, periode);
        value.put(TableConsommation._Consommation_T0, dataT0);
        value.put(TableConsommation._Consommation_Tx, dataTx);

        return (int) database.insert(TableConsommation._NAME, null, value);
    }

    /**
     * Méthode pour l'extration de données de TableConsommation
     * @return une liste contenant les données de TableConsommation
     */
    public ArrayList<TableConsommation> getAllConsommations() {
        Cursor c = null;
        ArrayList<TableConsommation> consommations = new ArrayList<>();
        try {
            String s = String.format(Locale.FRANCE,
                    "SELECT * FROM %s order by %s desc", TableConsommation._NAME, TableConsommation._Date_Start);
            c = database.rawQuery(s, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                TableConsommation consommation =
                        new TableConsommation(cursorToContentValues(c));
                consommations.add(consommation);
                c.moveToNext();
            }
            return consommations;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Méthode de suppression par ID dans TableConsommation
     * @param iD
     */
    public void removeConsommationByID(int iD) {

        String delete = String.format(Locale.FRANCE,
                "%s = %d", TableConsommation._ID, iD);
        database.delete(TableConsommation._NAME, delete, null);
    }

    /**
     * Méthode de suppression de tous les données de TableConsommation
     */
    public void removeAllConsommations() {
        database.delete(TableConsommation._NAME, null, null);
    }


    /**
     * Méthode pour mettre à jour le nombre des users sur la borne
     * @param id
     * @param newCount
     */
    public void updateNbreUser(int id, int newCount){
        database.execSQL(String.format(Locale.FRANCE,
                "UPDATE %s SET %s=%s + %d WHERE %s = ?",
                TableConsommation._NAME, TableConsommation._NbreUser,
                TableConsommation._NbreUser, newCount, TableConsommation._ID),
                new String[]{String.valueOf(id)});
    }

    /**
     * Méthode pour mettre à jour la période de partage de connexion
     * @param id
     * @param periode
     */
    public void updatePeriode(int id, int periode ) {
        database.execSQL(String.format(Locale.FRANCE,
                "UPDATE %s SET %s=%s + %d WHERE %s = ?",
                TableConsommation._NAME, TableConsommation._Periode,
                TableConsommation._Periode, periode, TableConsommation._ID),
                new String[]{String.valueOf(id)});
    }

    /**
     * Méthode pour mettre à jour la date de fin de partage de connexion
     * @param id
     * @param date
     * @return le nombre de lignes qui vient d'être mis à jour
     */
    public int updateConsommationDateEnd(int id, long date ) {

        String selection = TableConsommation._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableConsommation._Date_End, date);

        return database.update(TableConsommation._NAME, values, selection, null);
    }

    /**
     * Méthode pour mettre à jour data initial de partage
     * @param id
     * @param newConsommation
     * @return le nombre de lignes qui vient d'être mis à jour
     */
    public int updateConsommationDataT0(int id, long newConsommation){
        String selection = TableConsommation._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableConsommation._Consommation_T0, newConsommation);

        return database.update(TableConsommation._NAME, values, selection, null);
    }

    /**
     * Méthode pour mettre à jour la consommation de data sur la borne
     * @param id
     * @param newConsommation
     * @return le nombre de lignes qui vient d'être mis à jour
     */
    public int updateConsommationDataTx(int id, long newConsommation){
        String selection = TableConsommation._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableConsommation._Consommation_Tx, newConsommation);

        return database.update(TableConsommation._NAME, values, selection, null);
    }

    /**
     * Méthode pour mettre à jour la localisation
     * @param id
     * @param newLocation
     * @return le nombre de lignes qui vient d'être mis à jour
     */
    public int updateLocalisation(int id, boolean isUPSLocation){
        String selection = TableConsommation._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableConsommation._UPS_Location, isUPSLocation);

        return database.update(TableConsommation._NAME, values, selection, null);
    }

    /**
     * Méthode pour recuperer la dernière insertion dans TableConsommation
     * @return la dernière insertion
     */
    public TableConsommation getLastConsommation() {
        Cursor c = null;
        TableConsommation tableConsommation = null;
        try {
            String s = String.format(Locale.FRANCE,
                    "SELECT * FROM %s WHERE %s = (SELECT MAX(%s) AS maxDate FROM %s)",
                    TableConsommation._NAME, TableConsommation._ID,
                    TableConsommation._ID, TableConsommation._NAME );
            c = database.rawQuery(s, null);
            c.moveToFirst();
            if (!c.isAfterLast()) {
                tableConsommation = new TableConsommation(cursorToContentValues(c));
            }
            return tableConsommation;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /*==================== Fin des fonctions de manipulation de TableConsommation ====================*/


    /*==================== Débuts des fonctions de manipulations de TableUtilisateur ====================*/


    /**
     * Méthode d'insertion dans la TableUtilisateur
     * @param idconso
     * @param adresse_mac
     * @param adresse_ip
     * @param date_debut_cnx
     * @param date_fin_cnx
     * @return ID de l'insertion
     */
    public int addUtilisateur(int idconso, String adresse_mac, String adresse_ip, long date_debut_cnx, long date_fin_cnx) {

        ContentValues value = new ContentValues();

        value.put(TableUtilisateur._ID_CONSO, idconso);
        value.put(TableUtilisateur._ADRESSE_MAC, adresse_mac);
        value.put(TableUtilisateur._ADRESSE_IP, adresse_ip);
        value.put(TableUtilisateur._DATE_DEBUT_CNX, date_debut_cnx);
        value.put(TableUtilisateur._DATE_FIN_CNX, date_fin_cnx);

        return (int) database.insert(TableUtilisateur._NAME, null, value);
    }

    /**
     * Méthode pour extraire les données de TableUtilisateur à partir de ID_CONSO
     * @param idConso
     * @return une liste des users à partir de ID_CONSO
     */
    public ArrayList<TableUtilisateur> getUtilisateurs(int idConso) {
        Cursor c = null;
        ArrayList<TableUtilisateur> utilisateurs = new ArrayList<>();
        try {
            String s = String.format(Locale.FRANCE,
                    "SELECT * FROM %s where %s = %d",
                    TableUtilisateur._NAME, TableUtilisateur._ID_CONSO, idConso);
            c = database.rawQuery(s, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                ContentValues values = cursorToContentValues(c);
                TableUtilisateur utilisateur= new TableUtilisateur(values);
                utilisateurs.add(utilisateur);
                c.moveToNext();
            }
            return utilisateurs;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Méthode pour extraire les données de TableUtilisateur
     * @return la liste de tous les users
     */
    public ArrayList<TableUtilisateur> getAllUtilisateurs() {
        Cursor c = null;
        ArrayList<TableUtilisateur> utilisateurs = new ArrayList<>();
        try {
            String s = String.format(Locale.FRANCE,
                    "SELECT * FROM %s", TableUtilisateur._NAME);
            c = database.rawQuery(s, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                ContentValues values = cursorToContentValues(c);
                TableUtilisateur utilisateur= new TableUtilisateur(values);
                utilisateurs.add(utilisateur);
                c.moveToNext();
            }
            return utilisateurs;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Méthode pour mettre à jour date de début de partage de connexion
     * @param id
     * @param time
     * @return le nombre de ligne qui vient d'être mis à jour
     */
    public int updateConnectedTime(int id, long time){
        String selection = TableUtilisateur._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableUtilisateur._DATE_DEBUT_CNX, time);

        return database.update(TableUtilisateur._NAME, values, selection, null);
    }

    /**
     * Méthode pour mettre à jour date de fin de partage de connexion
     * @param id
     * @param time
     * @return le nombre de ligne qui vient d'être mis à jour
     */
    public int updateDisconnectedTime(int id, long time){
        String selection = TableUtilisateur._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableUtilisateur._DATE_FIN_CNX, time);

        return database.update(TableUtilisateur._NAME, values, selection, null);
    }

    /**
     * Méthode de suppression des users à partir de ID
     * @param iduser
     */
    public void removeUtilisateurByID(int iduser) {
        String delete = String.format(Locale.FRANCE,  "%s = %d", TableUtilisateur._ID, iduser);
        database.delete(TableUtilisateur._NAME, delete, null);
    }

    /**
     * Méthode pour supprimer tous les users
     */
    public void removeAllUtilisateur() {
        database.delete(TableUtilisateur._NAME, null, null);
    }

    /*==================== Fin des fonctions de manipulations de TableUtilisateur ====================*/

}
