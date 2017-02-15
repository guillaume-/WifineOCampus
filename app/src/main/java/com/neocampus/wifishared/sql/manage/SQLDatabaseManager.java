package com.neocampus.wifishared.sql.manage;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>Cette classe permet de fournir une instance unique d'un objet permettant de communiquer</b>
 *
 * @author NALINGA
 */
public class SQLDatabaseManager {

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static SQLDatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    /**
     * Méthode de création et d'initialisation d'une instance de communication
     * @param helper
     */
    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new SQLDatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    /**
     * Méthode permettant la création de plusieurs instances de communication
     * @return une instance de communication
     */
    public static synchronized SQLDatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(SQLDatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    /**
     * Méthode pour ouverture de base de données
     * @return une instance de communication avec la base de données
     */
    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    /**
     * Méthode de fermeture de base de données
     */
    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            mDatabase.close();
        }
    }

}
