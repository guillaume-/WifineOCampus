package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;
import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;

/**
 * Created by NALINGA on 10/01/2017.
 */
@Table(TableName = "TableConsommation", Order = 2)
public class TableConsommation extends SqlDataSchema {
    public static final String _NAME = "TableConsommation" ;

    @Column(Primary = true, Auto = true, Nullable = false, Type = SqlType.INTEGER)
    public static final String _ID =  "Column_ID";

    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String _Date = "Column_Date";

    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _NbreUser = "Column_NombreUser";

    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String _Periode = "Column_Periode";

    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _Consommation = "Column_Consommation";

    @Column(Type = SqlType.TEXT, Nullable = false)
    public static final String _Localisation = "Column_Localisation";


    public TableConsommation(ContentValues values) {
        super(values);
    }

    public int getID(){
        return this.values.getAsInteger(_ID);
    }

    public long getDATE(){
        return  this.values.getAsLong(_Date);
    }

    public void setDate(long date){
        this.values.put(_Date, date);
    }

    public int getNBREUSER(){
        return this.values.getAsInteger(_NbreUser);
    }

    public void setNbreUser(int nbre_user){
        this.values.put(_NbreUser,nbre_user);
    }

    public int getPERIODE(){
        return this.values.getAsInteger(_Periode);
    }

    public void setPeriode(int period){
        this.values.put(_Periode,period);
    }

    public long getCONSOMMATION(){
        return this.values.getAsLong(_Consommation);
    }

    public void setConsommation(long consommation){
        this.values.put(_Consommation, consommation);
    }

    public String getPosition(){ return  this.values.getAsString(_Localisation); }

    public void setPosition(String pos){ this.values.put(_Localisation, pos); }

}
