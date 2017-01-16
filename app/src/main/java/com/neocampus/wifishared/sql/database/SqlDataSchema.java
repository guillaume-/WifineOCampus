package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;


/**
 * Created by Hirochi on 02/10/2016.
 */


public abstract class SqlDataSchema {

    protected ContentValues values;

    public SqlDataSchema(ContentValues values) {
        this.values = values;
    }

    public <T> T get(Class<T> valueClass, String key) {
        if (!values.containsKey(key))
            return null;
        if (valueClass == Byte.class) {
            return (T) this.values.getAsByte(key);
        } else if (valueClass == Short.class) {
            return (T) this.values.getAsShort(key);
        } else if (valueClass == Integer.class) {
            return (T) this.values.getAsInteger(key);
        } else if (valueClass == Long.class) {
            return (T) this.values.getAsLong(key);
        } else if (valueClass == Float.class) {
            return (T) this.values.getAsFloat(key);
        } else if (valueClass == Double.class) {
            return (T) this.values.getAsDouble(key);
        } else if (valueClass == Boolean.class) {
            return (T) this.values.getAsBoolean(key);
        } else if (valueClass == byte[].class) {
            return (T) this.values.getAsByteArray(key);
        } else if (valueClass == String.class) {
            return (T) this.values.getAsString(key);
        }
        return (T) this.values.get(key);
    }

    public <T> void set(String key, T value) {
        if (value instanceof Byte) {
            this.values.put(key, (Byte) value);
        } else if (value instanceof Short) {
            this.values.put(key, (Short) value);
        } else if (value instanceof Integer) {
            this.values.put(key, (Integer) value);
        } else if (value instanceof Long) {
            this.values.put(key, (Long) value);
        } else if (value instanceof Float) {
            this.values.put(key, (Float) value);
        } else if (value instanceof Double) {
            this.values.put(key, (Double) value);
        } else if (value instanceof Boolean) {
            this.values.put(key, (Boolean) value);
        } else if (value instanceof byte[]) {
            this.values.put(key, (byte[]) value);
        } else if (value instanceof String) {
            this.values.put(key, (String) value);
        }

    }

}
