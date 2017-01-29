package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;

import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;

/**
 * Created by NALINGA on 23/01/2017.
 */

@Table(TableName = "TableUtilisateur", Order = 3)
public class TableUtilisateur extends SqlDataSchema {
    public static final String _NAME = "TableUtilisateur" ;

    @Column(Primary = true, Auto = true, Nullable = false, Type = SqlType.INTEGER)
    public static final String _ID =  "Column_ID";

    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String _ID_CONSO =  "Column_ID_CONSO";

    @Column(Type = SqlType.TEXT, Nullable = false)
    public static final String _ADRESSE_MAC =  "Column_ADRESSE_MAC";

    @Column(Type = SqlType.TEXT, Nullable = false)
    public static final String _ADRESSE_IP =  "Column_ADRESSE_IP";

    @Column(Type = SqlType.INTEGER, Nullable = true)
    public static final String _DATE_DEBUT_CNX =  "Column_DATE_DEBUT_CNX";

    @Column(Type = SqlType.INTEGER, Nullable = true)
    public static final String _DATE_FIN_CNX=  "Column_DATE_FIN_CNX";


    public TableUtilisateur(ContentValues values) {
        super(values);
    }

    public int getID(){
        return this.values.getAsInteger(_ID);
    }

    public int getIdConso(){ return this.values.getAsInteger(_ID_CONSO); }

    public void setIdConso(int id_conso){ this.values.put(_ID_CONSO, id_conso);}

    public String getAdressMac(){ return this.values.getAsString(_ADRESSE_MAC); }

    public void setAdressMac(String adresse_mac){ this.values.put(_ADRESSE_MAC, adresse_mac); }

    public String getAdressIP(){ return this.values.getAsString(_ADRESSE_IP); }

    public void setAdressIP(String adresse_ip){ this.values.put(_ADRESSE_MAC, adresse_ip); }

    public long getDateDebutCnx(){ return this.values.getAsLong(_DATE_DEBUT_CNX); }

    public void setDateDebutCnx(long date_debut_cnx){ this.values.put(_DATE_DEBUT_CNX, date_debut_cnx); }

    public long getDateFinCnx(){ return this.values.getAsLong(_DATE_FIN_CNX); }

    public void setDateFinCnx(long date_fin_cnx){ this.values.put(_DATE_FIN_CNX, date_fin_cnx); }


}
