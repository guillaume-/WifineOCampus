package com.neocampus.wifishared.sql.manage;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;
import com.neocampus.wifishared.sql.annotations.Trigger;
import com.neocampus.wifishared.utils.AnnotationUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 7;
    private Context context;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            Set<Class<?>> classes = AnnotationUtils.
                    getAnnotationClasses(context, Table.class);
            List<Class<?>> listClasses = new ArrayList<>();
            for (Class<?> aClass : classes) {
                Table table = aClass.getAnnotation(Table.class);
                if (table.enabled()) {
                    listClasses.add(aClass);
                }
            }
            Collections.sort(listClasses, new Table.IComparator());

            createAllTables(database, listClasses);
            createAllTriggers(database, listClasses);

        } catch (ClassNotFoundException | PackageManager.NameNotFoundException | IOException |
                NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        HashMap<String, Cursor> hashMap = new HashMap<>();
        try {
            Set<Class<?>> classes = AnnotationUtils.
                    getAnnotationClasses(context, Table.class);
            List<Class<?>> listClasses = new ArrayList<>();
            for (Class<?> aClass : classes) {
                Table table = aClass.getAnnotation(Table.class);
                if (table.enabled()) {
                    listClasses.add(aClass);
                }
            }
            Collections.sort(listClasses, new Table.IComparator());

            /*Store all tables to duplicate*/
            Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {
                    String tableName = c.getString(0);
                    if(!"android_metadata".equals(tableName)
                            && !"sqlite_sequence".equals(tableName)) {
                        Cursor cursor = store( database, tableName);
                        try {
                            hashMap.put(tableName, cursor);
                        } catch (Exception e) {
                        }
                    }
                    c.moveToNext();
                }
            }

            /*Drop all tables without duplicate*/
            for (Class<?> aClass : listClasses) {
                Table table = aClass.getAnnotation(Table.class);
                database.execSQL("DROP TABLE IF EXISTS " + table.TableName());
            }

            createAllTables(database, listClasses);

            /*Restore all tables*/
            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {
                    String tableName = c.getString(0);
                    if(!"android_metadata".equals(tableName)
                            && !"sqlite_sequence".equals(tableName)) {
                        restore(database, tableName, hashMap.get(tableName));
                    }
                    c.moveToNext();
                }
            }

            createAllTriggers(database, listClasses);

            /*Drop all duplicate tables*/
            for (Class<?> aClass : listClasses) {
                Table table = aClass.getAnnotation(Table.class);
                database.execSQL("DROP TABLE IF EXISTS _" + table.TableName()+"_");
            }

        } catch (ClassNotFoundException | PackageManager.NameNotFoundException | IOException |
                NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        try {
            Set<Class<?>> classes = AnnotationUtils.
                    getAnnotationClasses(context, Table.class);
            List<Class<?>> listClasses = new ArrayList<>();
            for (Class<?> aClass : classes) {
                Table table = aClass.getAnnotation(Table.class);
                if (table.enabled()) {
                    listClasses.add(aClass);
                }
            }

            Collections.sort(listClasses, new Table.IComparator());

            dropAllTriggers(database);
            dropAllTables(database);

            createAllTables(database, listClasses);
            createAllTriggers(database, listClasses);

        } catch (ClassNotFoundException | PackageManager.NameNotFoundException |
                IOException | NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static String buildSQL(Class<?> aClass)
            throws NoSuchFieldException, IllegalAccessException {

        StringBuilder s = new StringBuilder("");
        Table table = aClass.getAnnotation(Table.class);
        s.append(String.format("create table %s ( ", table.TableName()));
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())
                    && field.isAnnotationPresent(Column.class)) {

                Column column = field.getAnnotation(Column.class);
                s.append(String.format("%s %s", field.get(null), column.Type()));
                if (column.Primary()) {
                    s.append(String.format(" primary key %s", column.Auto() ? "autoincrement" : ""));
                } else {
                    s.append(!column.Nullable() ? " not null " : "");
                    if (!"".equals(column.value()) && column.Type() != SqlType.BLOB) {
                        String value = column.value();
                        if((column.Type() == SqlType.TEXT))
                            value = String.format("'%s'", value);
                        s.append(" default ").append(value);
                    }
                }
                s.append(",");
            }
        }
        s.append(");");
        int index = s.lastIndexOf(",");
        s.replace(index, index + 1, "");
        return s.toString();
    }

    private static Cursor store(SQLiteDatabase database, String tableName) {
        database.execSQL(String.format("ALTER TABLE %s RENAME TO %s", tableName, "_"+tableName+"_"));
        Cursor c;
        try {
            String req = String.format("SELECT * FROM %s", "_"+tableName+"_");
            c = database.rawQuery(req, null);
            return c;
        } finally {
        }
    }

    private static void restore(SQLiteDatabase database,String tableName, Cursor c) {
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

    private static void createAllTables(SQLiteDatabase database, List<Class<?>> listClasses)
            throws NoSuchFieldException, IllegalAccessException {
        /*Create all tables*/
        for (Class<?> aClass : listClasses) {
            String createTable = buildSQL(aClass);
            database.execSQL(createTable);
        }
    }

    private static void createAllTriggers(SQLiteDatabase database, List<Class<?>> listClasses)
            throws InvocationTargetException, IllegalAccessException {
        for (Class<?> aClass : listClasses) {
            Set<Method> methods = AnnotationUtils
                    .getAnnotationsMethods(aClass, Trigger.class);
            for(Method method : methods) {
                Trigger trigger = method.getAnnotation(Trigger.class);
                method.invoke(null, database, trigger.name());
            }
        }
    }

    private static void dropAllTriggers(SQLiteDatabase database)
    {
        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='trigger'", null);
        if (cursor.moveToFirst()) {
            while ( !cursor.isAfterLast() ) {
                String triggerName = cursor.getString(0);
                database.execSQL("DROP TRIGGER IF EXISTS " + triggerName);
                System.out.println("DROP TRIGGER IF EXISTS " + triggerName);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    private static void dropAllTables(SQLiteDatabase database)
    {
        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (cursor.moveToFirst()) {
            while ( !cursor.isAfterLast() ) {
                String tableName = cursor.getString(0);
                if(!"android_metadata".equals(tableName) && !"sqlite_sequence".equals(tableName)) {
                    database.execSQL("DROP TABLE IF EXISTS " + tableName);
                    System.out.println("DROP TABLE IF EXISTS " + tableName);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

}