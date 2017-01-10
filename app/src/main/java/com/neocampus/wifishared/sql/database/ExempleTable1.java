package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;

import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;

/**
 * Created by Hirochi ☠ on 09/01/17.
 */
@Table(TableName = "exemple1_table_name", Order = 1)
public class ExempleTable1 extends SqlDataSchema {

    public static final String _NAME = "exemple1_table_name";

    /**
     *
     */
    @Column(Primary = true, Auto = true,
            Nullable = false, Type = SqlType.INTEGER)
    public static final String _ID =  "exemple_1_id";


    /**
     *
     */
    @Column(Type = SqlType.TEXT, Nullable = false)
    public static final String _URL = "exemple_1_url";


    public ExempleTable1(ContentValues values) {
        super(values);
    }

    public int getID()
    {
        return this.values.getAsInteger(_ID);
    }

}
