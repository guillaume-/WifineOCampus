package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;
import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;

/**
 * Created by Asus on 10/01/2017.
 */
@Table(TableName = "TableConsommation", Order = 2)
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

    @Column(Type = SqlType.INTEGER, Nullable = false, value = "15")
    public static final String LimiteBatterie = "Limite_Batterie";

    @Column(Type = SqlType.DOUBLE, Nullable = false, value = "1")
    public static final String LimiteConsommation = "Limite_Consommation";

    @Column(Type = SqlType.DOUBLE, Nullable = false, value = "300")
    public static final String LimiteTemps = "Limite_Temps";


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

    public int getLimiteBatterie(){ return this.values.getAsInteger(LimiteBatterie);}

    public void setLimiteBatterie(String lim_bat){ this.values.put(LimiteBatterie, lim_bat);}

    public Double getLimiteConsommation(){ return this.values.getAsDouble(LimiteConsommation);}

    public void setLimiteConsommation(String lim_conso){ this.values.put(LimiteConsommation, lim_conso);}

    public Double getLimiteTemps(){ return this.values.getAsDouble(LimiteTemps);}

    public void setLimiteTemps(String lim_tmp){ this.values.put(LimiteTemps, lim_tmp);}
}
