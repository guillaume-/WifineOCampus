package com.neocampus.wifishared.sql.database;

import android.content.ContentValues;

import com.neocampus.wifishared.sql.annotations.Column;
import com.neocampus.wifishared.sql.annotations.SqlType;
import com.neocampus.wifishared.sql.annotations.Table;

/**
 * Created by Hirochi ☠ on 22/01/17.
 */

@Table(TableName = "TableClientConnexion", Order = 3)
public class TableClientConnexion  extends SqlDataSchema  {

    public static final String _NAME = "TableClientConnexion" ;

    @Column(Primary = true, Auto = true, Nullable = false, Type = SqlType.INTEGER)
    public static final String _ID =  "Column_ID";

    @Column(Type = SqlType.INTEGER, Nullable = false)
    public static final String _ID_CONSOMMATION = "Column_ID_Consommation";

    @Column(Type = SqlType.TEXT, Nullable = false)
    public static final String _ADDRESS_MAC = "Column_Adrress_Mac";

    @Column(Type = SqlType.TEXT, Nullable = false)
    public static final String _ADDRESS_IP = "Column_Adrress_IP";

    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _DATE_CONNECTED = "Column_DATE_CONNECTED";

    @Column(Type = SqlType.INTEGER, Nullable = true, value = "0")
    public static final String _DATE_DISCONNECTED = "Column_DATE_DISCONNECTED";

    public TableClientConnexion(ContentValues values) {
        super(values);
    }
}
