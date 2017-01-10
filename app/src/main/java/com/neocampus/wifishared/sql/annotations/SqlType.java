package com.neocampus.wifishared.sql.annotations;

/**
 * Created by Hirochi ☠ on 09/01/17.
 */

public enum SqlType {
    TEXT ("TEXT"),
    BLOB ("BLOB"),
    REAL ("REAL"),
    NUMERIC ("NUMERIC"),
    INTEGER ("INTEGER");

    private final String name;
    private SqlType(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
