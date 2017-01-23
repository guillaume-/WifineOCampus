package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;

import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;

/**
 * Created by NALINGA on 23/01/2017.
 */

public class TableUtilisateur extends SqlDataSchema {
    public static final String _NAME = "TableUtilisateur" ;

    @Column(Primary = true, Auto = true, Nullable = false, Type = SqlType.INTEGER)
    public static final String _ID =  "Column_ID";

    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String _ID_CONSO =  "Column_ID_CONSO";

    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String _ADRESSE_MAC =  "Column_ADRESSE_MAC";

    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String _ADRESSE_IP =  "Column_ADRESSE_IP";

    @Column(Type = SqlType.TEXT, Nullable = false)
    public static final String _LOCALISATION =  "Column_LOCALISATION";

    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String _DATE_DEBUT_CNX =  "Column_DATE_DEBUT_CNX";

    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String _DATE_FIN_CNX=  "Column_DATE_FIN_CNX";


    public TableUtilisateur(ContentValues values) {
        super(values);
    }

    public int getID(){
        return this.values.getAsInteger(_ID);
    }

    public int getID_CONSO(){ return this.values.getAsInteger(_ID_CONSO); }

    public void setID_CONSO(int id_conso){ this.values.put(_ID_CONSO, id_conso);}

    public long getADESSE_MAC(){ return this.values.getAsLong(_ADRESSE_MAC); }

    public void setADRESSE_MAC(long adresse_mac){ this.values.put(_ADRESSE_MAC, adresse_mac); }

    public long getADESSE_IP(){ return this.values.getAsLong(_ADRESSE_IP); }

    public void setADRESSE_IP(long adresse_ip){ this.values.put(_ADRESSE_MAC, adresse_ip); }

    public String getLOCALISATION(){ return this.values.getAsString(_LOCALISATION); }

    public void setLOCALISATION(String localisation){ this.values.put(_LOCALISATION, localisation); }

    public long getDATE_DEBUT_CNX(){ return this.values.getAsLong(_DATE_DEBUT_CNX); }

    public void setDATE_DEBUT_CNX(long date_debut_cnx){ this.values.put(_DATE_DEBUT_CNX, date_debut_cnx); }

    public long getDATE_FIN_CNX(){ return this.values.getAsLong(_DATE_FIN_CNX); }

    public void setDATE_FIN_CNX(long date_fin_cnx){ this.values.put(_DATE_FIN_CNX, date_fin_cnx); }


}
