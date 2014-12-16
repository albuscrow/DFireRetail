package com.dfire.retail.app.manage.data;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;

public class StockCheckArea implements Serializable,Comparable<StockCheckArea> {

    private static final long serialVersionUID = 3814434607297244289L;

    private String stockCheckId;
    private String shopId;
    private String region;
    
    private int checkedCount;
   
    public String getStockCheckId() {
        return stockCheckId;
    }
    public void setStockCheckId(String stockCheckId) {
        this.stockCheckId = stockCheckId;
    }
    public String getShopId() {
        return shopId;
    }
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public int getCheckedCount() {
        return checkedCount;
    }
    public void setCheckedCount(int checkedCount) {
        this.checkedCount = checkedCount;
    }
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();  
        contentValues.put("stockcheckid", this.stockCheckId);
        contentValues.put("shopid", this.shopId);
        contentValues.put("region", this.region);
        return contentValues;
    }
    
    public void doInit(Cursor cursor){
        this.stockCheckId = cursor.getString(cursor.getColumnIndex("stockcheckid"));
        this.region = cursor.getString(cursor.getColumnIndex("region"));
    }
    @Override
    public int compareTo(StockCheckArea another) {
        return this.getRegion().compareTo(another.getRegion());
    }
}
