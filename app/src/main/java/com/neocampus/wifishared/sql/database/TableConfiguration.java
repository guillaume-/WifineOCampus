package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;

import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;

import java.sql.Blob;

/**
 * Created by Asus on 10/01/2017.
 */
@Table(TableName = "TableConfiguration", Order = 1)
public class TableConfiguration extends SqlDataSchema {

    public static final String _NAME = "TableConfiguration" ;

    @Column(Primary = true, Auto = true, Nullable = false, Type = SqlType.INTEGER)
    public static final String _ID =  "Column_ID";

    @Column(Type = SqlType.BLOB, Nullable = false)
    public static final String Configuration = "Configuration";


    public TableConfiguration(ContentValues values) {
        super(values);
    }

    public int getID(){
        return this.values.getAsInteger(_ID);
    }

    public void setID(String iduser){ this.values.put(_ID, iduser);}

    public byte[] getConfig(){ return  this.values.getAsByteArray(Configuration); }

    public void setConfig(String conf){ this.values.put(Configuration, conf);}

}
