package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;

import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;

/**
 * Created by Asus on 10/01/2017.
 */
@Table(TableName = "TableConfiguration", Order = 1)
public class TableConfiguration extends SqlDataSchema {

    public static final String _NAME = "TableConfiguration" ;

    @Column(Primary = true, Auto = false, Nullable = false, Type = SqlType.TEXT)
    public static final String SSID =  "Column_SSID";

    @Column(Type = SqlType.TEXT, Nullable = false)
    public static final String Pwd = "Password";


    public TableConfiguration(ContentValues values) {
        super(values);
    }

    public String getSSID() {
        return this.values.getAsString(SSID);
    }

    public void setSSID(String ssid){
        this.values.put(SSID, ssid);
    }

    public String getPassword() {
        return this.values.getAsString(Pwd);
    }

    public void setPassword(String passd){
        this.values.put(Pwd,passd);
    }

}
