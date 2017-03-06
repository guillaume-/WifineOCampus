package com.neocampus.wifishared.sql.annotations;



public enum SqlType {
    TEXT ("TEXT"),
    BLOB ("BLOB"),
    REAL ("REAL"),
    NUMERIC ("NUMERIC"),
    INTEGER ("INTEGER"),
    DOUBLE ("DOUBLE");

    private final String name;
    private SqlType(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
