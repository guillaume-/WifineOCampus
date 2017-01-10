package com.neocampus.wifishared.sql.manage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.HashMap;

/**
 * Created by Hirochi on 26/07/2016.
 */
public class SQLUpgradeManager {

    private SQLiteDatabase database;
    private HashMap<String, Cursor> hashMap;

    public SQLUpgradeManager(SQLiteDatabase database) {
        this.database = database;
        this.hashMap = new HashMap<>();
    }

    public void store() {
//        try {
//            hashMap.put(SQLParametreGlobaux._NAME, SQLParametreGlobaux.store(database));
//        } catch (Exception e) {
//
//        }
//        try {
//            hashMap.put(SQLActuality._NAME, SQLActuality.store(database));
//        } catch (Exception e) {
//
//        }
//        try {
//            hashMap.put(SQLParametreActuality._NAME, SQLParametreActuality.store(database));
//        } catch (Exception e) {
//
//        }
//        try {
//            hashMap.put(SQLAnnonce._NAME, SQLAnnonce.store(database));
//        } catch (Exception e) {
//
//        }
//        try {
//            hashMap.put(SQLParametreAnnonce._NAME, SQLParametreAnnonce.store(database));
//        } catch (Exception e) {
//
//        }
//        try {
//            hashMap.put(SQLArticle._NAME, SQLArticle.store(database));
//        } catch (Exception e) {
//
//        }
//
//        try {
//            hashMap.put(SQLFavorite._NAME, SQLFavorite.store(database));
//        } catch (Exception e) {
//
//        }
    }

    private Cursor storeTable(String tableName, String tableUpgrade) {
        this.database.execSQL(String.format("ALTER TABLE %DexUtils RENAME TO %DexUtils", tableName, tableUpgrade));
        Cursor c;
        try {
            String req = String.format("SELECT * FROM %DexUtils", tableUpgrade);
            c = database.rawQuery(req, null);
            return c;
        } finally {
        }
    }


    public void restore() {
        /*
        try {
            restoreTable(SQLParametreGlobaux._NAME, hashMap.get(SQLParametreGlobaux._NAME));
        } catch (Exception e) {

        }
        try {
            restoreTable(SQLActuality._NAME, hashMap.get(SQLActuality._NAME));
        } catch (Exception e) {

        }
        try {
            restoreTable(SQLParametreActuality._NAME, hashMap.get(SQLParametreActuality._NAME));
        } catch (Exception e) {

        }
        try {
            restoreTable(SQLAnnonce._NAME, hashMap.get(SQLAnnonce._NAME));
        } catch (Exception e) {

        }
        try {
            restoreTable(SQLParametreAnnonce._NAME, hashMap.get(SQLParametreAnnonce._NAME));
        } catch (Exception e) {

        }
        try {
            restoreTable(SQLArticle._NAME, hashMap.get(SQLArticle._NAME));
        } catch (Exception e) {

        }

        try {
            restoreTable(SQLFavorite._NAME, hashMap.get(SQLFavorite._NAME));
        } catch (Exception e) {

        }

        database.execSQL("DROP TABLE IF EXISTS " + SQLParametreAnnonce._UPGRADE);
        database.execSQL("DROP TABLE IF EXISTS " + SQLParametreActuality._UPGRADE);
        database.execSQL("DROP TABLE IF EXISTS " + SQLParametreGlobaux._UPGRADE);
        database.execSQL("DROP TABLE IF EXISTS " + SQLAnnonce._UPGRADE);
        database.execSQL("DROP TABLE IF EXISTS " + SQLArticle._UPGRADE);
        database.execSQL("DROP TABLE IF EXISTS " + SQLFavorite._UPGRADE);
        database.execSQL("DROP TABLE IF EXISTS " + SQLActuality._UPGRADE);
        */
    }

    private void restoreTable(String tableName, Cursor c) {
        c.moveToFirst();
        while (!c.isAfterLast()) {
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
                        break;
                }
            }
            long result = database.insert(tableName, null, values);
            if (result == -1)
                database.update(tableName, values, null, null);
            c.moveToNext();
        }
        c.close();
    }

}
