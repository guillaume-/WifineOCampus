package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;
import com.neocampus.wifishared.sql.annotations.Trigger;

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

    @Column(Type = SqlType.INTEGER, Nullable = true)
    public static final String _UPS_Location = "Column_UPS_Localisation";


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

    public long getDateEnd(){
        return this.values.getAsLong(_Date_End);
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

    public boolean isUPSLocation(){ return  this.values.getAsBoolean(_UPS_Location); }

    public void setUPSLocation(boolean value){ this.values.put(_UPS_Location, value); }

    @Trigger(name = "TRIGGER_NB_USER")
    public static void triggerNbreUser(SQLiteDatabase database, String trigger_name)
    {
        String s = String.format("CREATE TRIGGER %s BEFORE INSERT ON %s FOR EACH ROW " +
                "\nBEGIN\n" +
                "UPDATE %s SET %s = %s + 1 WHERE %s = new.%s;" +
                "\nEND;\n",
                trigger_name, TableUtilisateur._NAME, _NAME,
                _NbreUser, _NbreUser, _ID, TableUtilisateur._ID_CONSO);
        database.execSQL(s);
    }

    @Trigger(name = "TRIGGER_PERIODE_USER")
    public static void triggerPeriodeUser(SQLiteDatabase database, String trigger_name)
    {
        String s = String.format("CREATE TRIGGER %s BEFORE UPDATE OF %s ON %s " +
                "\nBEGIN\n" +
                "UPDATE %s SET %s = %s + (new.%s - old.%s) WHERE %s = old.%s;" +
                "\nEND;\n",
                trigger_name, TableUtilisateur._DATE_FIN_CNX, TableUtilisateur._NAME, _NAME,
                _Periode, _Periode, TableUtilisateur._DATE_FIN_CNX, TableUtilisateur._DATE_DEBUT_CNX,
                _ID, TableUtilisateur._ID_CONSO);

        database.execSQL(s);
    }


}
