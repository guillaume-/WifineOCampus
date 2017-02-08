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
 * Created by NALINGA on 23/07/2015.
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


    public int newConsommation(long date, long dataT0, boolean isUPS)
    {
        ContentValues value = new ContentValues();
        value.put(TableConsommation._Date_Start, date);
        value.put(TableConsommation._Consommation_T0, dataT0);
        value.put(TableConsommation._UPS_Location, isUPS);
        return (int) database.insert(TableConsommation._NAME, null, value);
    }

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

    public void removeConsommationByID(int iD) {

        String delete = String.format(Locale.FRANCE,
                "%s = %d", TableConsommation._ID, iD);
        database.delete(TableConsommation._NAME, delete, null);
    }

    public void removeAllConsommations() {
        database.delete(TableConsommation._NAME, null, null);
    }


    public void updateNbreUser(int id, int newCount){
        database.execSQL(String.format(Locale.FRANCE,
                "UPDATE %s SET %s=%s + %d WHERE %s = ?",
                TableConsommation._NAME, TableConsommation._NbreUser,
                TableConsommation._NbreUser, newCount, TableConsommation._ID),
                new String[]{String.valueOf(id)});
    }

    public void updatePeriode(int id, int periode ) {
        database.execSQL(String.format(Locale.FRANCE,
                "UPDATE %s SET %s=%s + %d WHERE %s = ?",
                TableConsommation._NAME, TableConsommation._Periode,
                TableConsommation._Periode, periode, TableConsommation._ID),
                new String[]{String.valueOf(id)});
    }

    public int updateConsommationDateEnd(int id, long date ) {

        String selection = TableConsommation._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableConsommation._Date_End, date);

        return database.update(TableConsommation._NAME, values, selection, null);
    }

    public int updateConsommationDataT0(int id, long newConsommation){
        String selection = TableConsommation._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableConsommation._Consommation_T0, newConsommation);

        return database.update(TableConsommation._NAME, values, selection, null);
    }
    public int updateConsommationDataTx(int id, long newConsommation){
        String selection = TableConsommation._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableConsommation._Consommation_Tx, newConsommation);

        return database.update(TableConsommation._NAME, values, selection, null);
    }

    public int updateLocalisation(int id, boolean isUPSLocation){
        String selection = TableConsommation._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableConsommation._UPS_Location, isUPSLocation);

        return database.update(TableConsommation._NAME, values, selection, null);
    }

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


    public int addUtilisateur(int idconso, String adresse_mac, String adresse_ip, long date_debut_cnx, long date_fin_cnx) {

        ContentValues value = new ContentValues();

        value.put(TableUtilisateur._ID_CONSO, idconso);
        value.put(TableUtilisateur._ADRESSE_MAC, adresse_mac);
        value.put(TableUtilisateur._ADRESSE_IP, adresse_ip);
        value.put(TableUtilisateur._DATE_DEBUT_CNX, date_debut_cnx);
        value.put(TableUtilisateur._DATE_FIN_CNX, date_fin_cnx);

        return (int) database.insert(TableUtilisateur._NAME, null, value);
    }

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

    public int updateConnectedTime(int id, long time){
        String selection = TableUtilisateur._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableUtilisateur._DATE_DEBUT_CNX, time);

        return database.update(TableUtilisateur._NAME, values, selection, null);
    }

    public int updateDisconnectedTime(int id, long time){
        String selection = TableUtilisateur._ID + " = " + id;

        ContentValues values = new ContentValues();
        values.put(TableUtilisateur._DATE_FIN_CNX, time);

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


//
//    public void insertMultiple(Object[] objects) {
//        TimeLog.begin();
//        final int part = 100;
//        int quotient = objects.length / part;
//        int reste = objects.length % part;
//        if (quotient > 0) {
//            //insertArticleMultiplePart(TABLE_NAME, objects, 0, quotient * part, part);
//        }
//        TimeLog.mark("Quotient");
//        if (reste > 0) {
//            //insertArticleMultiplePart(TABLE_NAME, objects, quotient * part, quotient * part + reste, reste);
//        }
//        TimeLog.mark("Reste");
//        TimeLog.end();
//    }
//
//    private void insertMultiplePart(String tableName, Object[] objects,
//                                    int indexFrom, int indexTo, int stepInsertion) {
//        /*
//        String[] columns = TableName.getColumns();
//
//        String column = TextUtils.join(",", columns);
//
//        String[] values = new String[columns.length];
//        Arrays.fill(values, "?");
//
//        String replace = ",(" + TextUtils.join(",", values) + ")";
//        String value = new String(new char[stepInsertion]).replace("\0", replace).substring(1);
//
//        String sql = String.format("INSERT INTO %DexUtils (%DexUtils) VALUES %DexUtils", tableName, column, value);
//        SQLiteStatement statement = database.compileStatement(sql);
//
//        TableName table;
//        int index = 1, step = 1;
//        for (int i = indexFrom; i < indexTo; i++, step++) {
//            table = objects[i];
//            for (int j = 1; j <= columns.length; j++, index++) {
//                Object object = table.get(Object.class, columns[j - 1]);
//                if (object == null) {
//                    statement.bindNull(index);
//                } else if (object instanceof String) {
//                    statement.bindString(index, object.toString());
//                } else if (object instanceof byte[]) {
//                    statement.bindBlob(index, (byte[]) object);
//                } else if (object instanceof Integer) {
//                    statement.bindLong(index, (Integer) object);
//                } else if (object instanceof Long) {
//                    statement.bindLong(index, (Long) object);
//                } else if (object instanceof Boolean) {
//                    statement.bindLong(index, ((Boolean) object) ? 1 : 0);
//                }
//            }
//            if (step == stepInsertion) {
//                index = 1;
//                step = 0;
//                statement.executeInsert();
//                statement.clearBindings();
//            }
//        }
//        statement.close();
//        */
//    }



/*
    public Object get() {
        Cursor c = null;
        Object object = null;
        try {
            String DexUtils = String.format(Locale.FRANCE,
                    "SELECT * FROM %DexUtils", _NAME);
            c = database.rawQuery(DexUtils, null);
            if (c.moveToFirst()) {
                object =
                        new TableName(cursorToContentValues(c));
            }
            return object;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    */

    /*
    public ArrayList<Object> getAll() {
        Cursor c = null;
        ArrayList<Object> objects = new ArrayList<>();
        try {
            String DexUtils = String.format(Locale.FRANCE,
                    "SELECT * FROM %DexUtils ORDER BY %DexUtils desc", _NAME, _COLUMN);
            c = database.rawQuery(DexUtils, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Object object =
                        new TableName(cursorToContentValues(c));
                objects.add(object);
                c.moveToNext();
            }
            return objects;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    */

    /*
    public int update(Object object) {
        String selection = _ID + " = " + Object.getId();

        ContentValues values = new ContentValues();
        return database.update(_NAME, values, selection, null);
    }
    */

    /*
    public void removeByID(int iD) {
        String selection = String.format(Locale.FRANCE, "%DexUtils = %d",
                _ID, iD);
        database.delete(_NAME, selection, null);
    }
    */

    /*
    public void removeMultiple(List<Object> objects) {
        List<Integer> integers = new ArrayList<>();
        for (Object o : objects) {
            integers.add(o.getId());
        }
        String ids = TextUtils.join(",", integers);
        String selection = String.format(Locale.FRANCE, "%DexUtils in (%DexUtils)",
                _ID, ids);
        database.delete(_NAME, selection, null);
    }
    */
}
