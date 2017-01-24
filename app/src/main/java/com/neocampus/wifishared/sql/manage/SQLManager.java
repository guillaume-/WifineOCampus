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
 * Created by NALINGA on 23/12/2016.
 */
public class SQLManager {

    private SQLiteDatabase database;

    public SQLManager(Context context) {
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLDatabaseManager.initializeInstance(dbHelper);
    }

    public synchronized void open() throws SQLException {
        database = SQLDatabaseManager.getInstance().openDatabase();
    }

    public synchronized void close() {
        SQLDatabaseManager.getInstance().closeDatabase();
    }

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

    public int setConfiguration(byte[] config, int limite_batterie,
                                long limite_conso, long limite_temps) {
        int result;
        String selection = TableConfiguration._ID + " = 1";

        ContentValues value = new ContentValues();

        value.put(TableConfiguration._Wifi_Configuration, config);
        value.put(TableConfiguration._LimiteBatterie, limite_batterie);
        value.put(TableConfiguration._LimiteConsommation, limite_conso);
        value.put(TableConfiguration._LimiteTemps, limite_temps);

        if ((result = database.update(TableConfiguration._NAME, value, selection, null)) == 0) {
            return (int) database.insert(TableConfiguration._NAME, null, value);
        }
        return result;
    }

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
            return tableConfiguration;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    /*==================== Fin des fonctions de manipulations de TableConfiguration ====================*/


    /*==================== Debut de fonctions de manipulation de TableConsommation ====================*/

    public int addConsommation(long date,
                               int nbre_user, int periode, double consommation, String localisation) {

        ContentValues value = new ContentValues();

        value.put(TableConsommation._Date, date);
        value.put(TableConsommation._NbreUser, nbre_user);
        value.put(TableConsommation._Periode, periode);
        value.put(TableConsommation._Consommation, consommation);
        value.put(TableConsommation._Localisation, localisation);

        return (int) database.insert(TableConsommation._NAME, null, value);
    }

    public ArrayList<TableConsommation> getAllConsommations() {
        Cursor c = null;
        ArrayList<TableConsommation> consommations = new ArrayList<>();
        try {
            String s = String.format(Locale.FRANCE,
                    "SELECT * FROM %s", TableConsommation._NAME);
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

    public int updateDATE(int id, TableConsommation conso ) {
        String selection = TableConsommation._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableConsommation._Date, conso.getDATE());

        return database.update(TableConsommation._NAME, values, selection, null);
    }

    public int updateNBREUSER(int id, TableConsommation conso){
        String selection = TableConsommation._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableConsommation._Date, conso.getNBREUSER());

        return database.update(TableConsommation._NAME, values, selection, null);
    }

    public int updatePeriode(int id, TableConsommation conso ) {
        String selection = TableConsommation._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableConsommation._Date, conso.getPERIODE());

        return database.update(TableConsommation._NAME, values, selection, null);
    }

    public int updateConsommation(int id, TableConsommation conso){
        String selection = TableConsommation._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableConsommation._Date, conso.getCONSOMMATION());

        return database.update(TableConsommation._NAME, values, selection, null);
    }

    public int updateLocalisation(int id, TableConsommation conso){
        String selection = TableConsommation._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableConsommation._Date, conso.getPosition());

        return database.update(TableConsommation._NAME, values, selection, null);
    }

    public TableConsommation getLsatConsommation() {
        Cursor c = null;
        TableConsommation tableConsommation = null;
        try {
            String DexUtils = String.format(Locale.FRANCE,
                    "SELECT * FROM %s WHERE %S = MAX("+ TableConsommation._ID + ")",
                    TableConsommation._NAME, TableConsommation._ID);
            c = database.rawQuery(DexUtils, null);
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

    public void removeConsommationByID(int iD) {

        String delete = String.format(Locale.FRANCE,
                "%s = %d", TableConsommation._ID, iD);
        database.delete(TableConsommation._NAME, delete, null);
    }

    public void removeAllConsommations() {
        database.delete(TableConsommation._NAME, null, null);
    }

    /*==================== Fin des fonctions de manipulation de TableConsommation ====================*/

    /*==================== Débuts des fonctions de manipulations de TableUtilisateur ====================*/

    public int addUtilisateur(int idconso, long adresse_mac, long adresse_ip, long date_debut_cnx, long date_fin_cnx) {

        ContentValues value = new ContentValues();

        value.put(TableUtilisateur._ID_CONSO, idconso);
        value.put(TableUtilisateur._ADRESSE_MAC, adresse_mac);
        value.put(TableUtilisateur._ADRESSE_IP, adresse_ip);
        value.put(TableUtilisateur._DATE_DEBUT_CNX, date_debut_cnx);
        value.put(TableUtilisateur._DATE_FIN_CNX, date_fin_cnx);

        return (int) database.insert(TableUtilisateur._NAME, null, value);
    }

    public ArrayList<TableUtilisateur> getAllUtilisateur() {
        Cursor c = null;
        ArrayList<TableUtilisateur> utilisateurs = new ArrayList<>();
        try {
            String s = String.format(Locale.FRANCE, "SELECT * FROM %s", TableUtilisateur._NAME);
            c = database.rawQuery(s, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                TableUtilisateur utilisateur  = new TableUtilisateur(cursorToContentValues(c));
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

    public int updateDEBUTCNX(int id, TableUtilisateur user){
        String selection = TableUtilisateur._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableUtilisateur._DATE_DEBUT_CNX, user.getDATE_DEBUT_CNX());

        return database.update(TableUtilisateur._NAME, values, selection, null);
    }

    public int updateFINCNX(int id, TableUtilisateur user){
        String selection = TableUtilisateur._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableUtilisateur._DATE_FIN_CNX, user.getDATE_FIN_CNX());

        return database.update(TableUtilisateur._NAME, values, selection, null);
    }

    public void removeUtilisateurByID(int iduser) {
        String delete = String.format(Locale.FRANCE,  "%s = %d", TableUtilisateur._ID, iduser);
        database.delete(TableUtilisateur._NAME, delete, null);
    }

    public void removeAllUtilisateur() {
        database.delete(TableUtilisateur._NAME, null, null);
    }

    /*==================== Fin des fonctions de manipulations de TableUtilisateur ====================*/

}
