package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;

import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;

/**
 * Created by NALINGA on 10/01/2017.
 */
@Table(TableName = "TableConfiguration", Order = 1)
public class TableConfiguration extends SqlDataSchema {

    public static final String _NAME = "TableConfiguration" ;

    @Column(Primary = true, Auto = true, Nullable = false, Type = SqlType.INTEGER)
    public static final String _ID =  "Column_ID";

    @Column(Type = SqlType.BLOB, Nullable = true, value = "null")
    public static final String _Wifi_Configuration = "Column_Wifi_Configuration";


    @Column(Type = SqlType.INTEGER, Nullable = true, value = "30")
    public static final String _LimiteBatterie = "Column_Batterie";


    @Column(Type = SqlType.INTEGER, Nullable = true, value = "1500000000")
    public static final String _LimiteConsommation = "Column_Consommation";


    @Column(Type = SqlType.INTEGER, Nullable = true, value = "14400000")
    public static final String _LimiteTemps = "Column_Temps";


    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _DateAlarm = "Column_Date_Alarm";


    public TableConfiguration(ContentValues values) {
        super(values);
    }

    public int getID(){
        return this.values.getAsInteger(_ID);
    }

    public void setID(String iduser){
        this.values.put(_ID, iduser);}

    public byte[] getWifiConfiguration(){
        return  this.values.getAsByteArray(_Wifi_Configuration); }

    public void setWifiConfiguration(byte[] configuration){
        this.values.put(_Wifi_Configuration, configuration);}

    public int getLimiteBatterie() {
        return this.values.getAsInteger(_LimiteBatterie);
    }

    public void setLimiteBatterie(int value) {
        this.values.put(_LimiteBatterie, value);
    }

    public long getLimiteConsommation() {
        return this.values.getAsLong(_LimiteConsommation);
    }

    public void setLimiteConsommation(long value) {
        this.values.put(_LimiteConsommation, value);
    }

    public long getLimiteTemps() {
        return this.values.getAsLong(_LimiteTemps);
    }

    public void setLimiteTemps(long value) {
        this.values.put(_LimiteTemps, value);
    }

    public long getDateAlarm() {
        return this.values.getAsLong(_DateAlarm);
    }

    public void setDateAlarm(long value) {
        this.values.put(_DateAlarm, value);
    }
}
