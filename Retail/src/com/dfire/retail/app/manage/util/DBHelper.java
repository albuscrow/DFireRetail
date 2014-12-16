package com.dfire.retail.app.manage.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    //数据库版本  
    private static final int VERSION = 1; 
    public DBHelper(Context context) {
        super(context, "2dfire_retail.db", null, VERSION);  
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table stockcheck("
                + "goodsid varchar(32),"
                + "stockcheckid varchar(32),"
                + "goodsname varchar(32),"
                + "count int,"
                + "checkcount int,"
                + "region text,"
                + "purchaseprice numeric(18,2),"
                + "retailprice numeric(18,2),"
                + "lossnumber int,"
                + "resultprice numeric(18,2),"
                + "checkcountprice numeric(18,2),"
                + "barcode text);");
        db.execSQL("create table stockcheckarea("
                + "stockcheckid varchar(32),"
                + "shopid varchar(32),"
                + "region text);");
        db.execSQL("create table stockchecksearchgoods("
                + "goodsid varchar(32),"
                + "shopid varchar(32),"
                + "goodsname text,"
                + "nowstore int,"
                + "purchaseprice numeric(18,2),"
                + "retailprice numeric(18,2),"
                + "filename text,"
                + "spell text,"
                + "shortcode text,"
                + "barcode text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        // TODO Auto-generated method stub  

    }

}
