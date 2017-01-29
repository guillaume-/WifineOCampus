package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;
import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;

/**
 * Created by NALINGA on 10/01/2017.
 */
@Table(TableName = "TableConsommation", Order = 1)
public class TableConsommation extends SqlDataSchema {
    public static final String _NAME = "TableConsommation" ;

    @Column(Primary = true, Auto = true, Nullable = false, Type = SqlType.INTEGER)
    public static final String _ID =  "Column_ID";

    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String _Date_Start = "Column_Date";

    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _Date_End = "Column_Date_End";

    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _NbreUser = "Column_NombreUser";

    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _Periode = "Column_Periode";

    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _Consommation_T0 = "Column_Consommation_T0";

    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _Consommation_Tx = "Column_Consommation_Tx";

    @Column(Type = SqlType.TEXT, Nullable = true)
    public static final String _Localisation = "Column_Localisation";


    public TableConsommation(ContentValues values) {
        super(values);
    }

    public int getID(){
        return this.values.getAsInteger(_ID);
    }

    public long getDate(){
        return  this.values.getAsLong(_Date_Start);
    }

    public void setDate(long date){
        this.values.put(_Date_Start, date);
    }

    public int getNbreUser(){
        return this.values.getAsInteger(_NbreUser);
    }

    public void setNbreUser(int nbre_user){
        this.values.put(_NbreUser,nbre_user);
    }

    public int getPeriode(){
        return this.values.getAsInteger(_Periode);
    }

    public void setPeriode(int period){
        this.values.put(_Periode,period);
    }

    public int getDateEnd(){
        return this.values.getAsInteger(_Date_End);
    }

    public void setRunningPeriode(int period){
        this.values.put(_Date_End, period);
    }

    public long getConsommationT0(){
        return this.values.getAsLong(_Consommation_T0);
    }

    public void setConsommationT0(long consommation){
        this.values.put(_Consommation_T0, consommation);
    }

    public long getConsommationTx(){
        return this.values.getAsLong(_Consommation_Tx);
    }

    public void setConsommationTx(long consommation){
        this.values.put(_Consommation_Tx, consommation);
    }

    public String getPosition(){ return  this.values.getAsString(_Localisation); }

    public void setPosition(String pos){ this.values.put(_Localisation, pos); }
}
