package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;
import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;

/**
 * Created by Asus on 10/01/2017.
 */
@Table(TableName = "TableConsommation", Order = 1)
public class TableConsommation extends SqlDataSchema {
    public static final String _NAME = "TableConsommation" ;

    @Column(Primary = true, Auto = true, Nullable = false, Type = SqlType.INTEGER)
    public static final String _ID =  "Column_ID";

    @Column(Type = SqlType.TEXT, Nullable = false)
    public static final String _Date = "Column_Date";

    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String NbreUser = "NombreUser";

    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String _Periode = "Periode";

    @Column(Type = SqlType.REAL, Nullable = false)
    public static final String _Consommation = "Consommation";


    public TableConsommation(ContentValues values) {
        super(values);
    }

    public int getID(){
        return this.values.getAsInteger(_ID);
    }

    public String getDATE(){
        return  this.values.getAsString(_Date);
    }

    public void setDate(String dte){
        this.values.put(_Date,dte);
    }

    public int getNBREUSER(){
        return this.values.getAsInteger(NbreUser);
    }

    public void setNbreUser(int nbre_user){
        this.values.put(NbreUser,nbre_user);
    }

    public int getPERIODE(){
        return this.values.getAsInteger(_Periode);
    }

    public void setPeriode(int period){
        this.values.put(_Periode,period);
    }

    public String getCONSOMMATION(){
        return this.values.getAsString(_Consommation);
    }

    public void setConsommation(String conso){
        this.values.put(_Consommation,conso);
    }
}
