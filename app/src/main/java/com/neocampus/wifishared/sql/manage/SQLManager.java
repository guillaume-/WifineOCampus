package com.neocampus.wifishared.sql.manage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.neocampus.wifishared.sql.database.ExempleTable1;
import com.neocampus.wifishared.sql.database.TableConfiguration;
import com.neocampus.wifishared.sql.database.TableConsommation;

import java.util.Locale;

/**
 * Created by Jean on 23/07/2015.
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


    public int ajouterConf(String ssid, String password){
        ContentValues value = new ContentValues();

        value.put(TableConfiguration.SSID,ssid);
        value.put(TableConfiguration.Pwd,password);
        database.insert(TableConfiguration._NAME,null,value);

        return 2;
    }

    public int ajouterConso(String date, int nbreuser, int periode, double conso){
        ContentValues value = new ContentValues();

        value.put(TableConsommation._Date, date);
        value.put(TableConsommation.NbreUser, nbreuser);
        value.put(TableConsommation._Periode, periode);
        value.put(TableConsommation._Consommation, conso);
        database.insert(TableConsommation._NAME, null, value);

        return 2;
    }

    public void supprimerConso(int iD){
        String delete = String.format(Locale.FRANCE,"%DexUtils = %d", TableConsommation._ID, iD);
        database.delete(TableConsommation._NAME, delete, null);
    }


    public int insert(String ssid, String password) {
        ContentValues values = new ContentValues();
        values.put(ExempleTable1._URL, ssid);
        database.insert(ExempleTable1._NAME, null, values);
        return -1;
    }

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
