/************************* 版权声明 **********************************
 * 版权所有：Copyright (c) 2DFIRE Co., Ltd. 2014 
 *
 * 工程名称： Retail
 * 创建者： chengzi  
 * 创建日期： 2014年11月18日
 * 创建记录： 创建类结构。
 *
 * ************************* 变更记录 ********************************
 * 修改者： 
 * 修改日期：
 * 修改记录：
 *
 **/

package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.math.BigDecimal;

import android.content.ContentValues;
import android.database.Cursor;

/**     
 * 项目名称：Retail  
 * 类名称：StockGoodsCheckVo  
 * 类描述：   盘点结果商品信息
 * 创建时间：2014年11月18日 上午10:01:14  
 * @author chengzi  
 * @version 1.0
 */
public class StockGoodsCheckVo implements Serializable {

    private static final long serialVersionUID = 7942682413883468344L;

    private String stockCheckId;
    private String goodsId;
    private String goodsName;
    private Integer count;
    private Integer checkCount;
    private String region;
    private BigDecimal purchasePrice;
    private BigDecimal retailPrice;
    private Integer getLossNumber;
    private BigDecimal resultPrice;
    private BigDecimal checkCountPrice;
    private String barCode;
    public String getStockCheckId() {
        return stockCheckId;
    }
    public void setStockCheckId(String stockCheckId) {
        this.stockCheckId = stockCheckId;
    }
    public String getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
    public String getGoodsName() {
        return goodsName;
    }
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
    public Integer getCheckCount() {
        return checkCount;
    }
    public void setCheckCount(Integer checkCount) {
        this.checkCount = checkCount;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }
    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
    public BigDecimal getRetailPrice() {
        return retailPrice;
    }
    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }
    public Integer getGetLossNumber() {
        return getLossNumber;
    }
    public void setGetLossNumber(Integer getLossNumber) {
        this.getLossNumber = getLossNumber;
    }
    public BigDecimal getResultPrice() {
        return resultPrice;
    }
    public void setResultPrice(BigDecimal resultPrice) {
        this.resultPrice = resultPrice;
    }
    public BigDecimal getCheckCountPrice() {
        return checkCountPrice;
    }
    public void setCheckCountPrice(BigDecimal checkCountPrice) {
        this.checkCountPrice = checkCountPrice;
    }
    public String getBarCode() {
        return barCode;
    }
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();  
        contentValues.put("stockcheckid", this.stockCheckId);
        contentValues.put("goodsid", this.goodsId);
        contentValues.put("goodsname", this.goodsName);
        contentValues.put("count", this.count);
        contentValues.put("checkcount", this.checkCount);
        contentValues.put("region", this.region);
        contentValues.put("purchaseprice", this.purchasePrice == null ? null : purchasePrice.doubleValue());
        contentValues.put("retailprice", this.retailPrice == null ? null : retailPrice.doubleValue());
        contentValues.put("lossnumber", this.getLossNumber);
        contentValues.put("resultprice", this.resultPrice == null ? null : resultPrice.doubleValue());
        contentValues.put("checkcountprice", this.checkCountPrice == null ? null : checkCountPrice.doubleValue());
        contentValues.put("barcode", this.barCode);
        
        return contentValues;
    }
    
    public void doInit(Cursor cursor){
        this.stockCheckId = cursor.getString(cursor.getColumnIndex("stockcheckid"));
        this.goodsId = cursor.getString(cursor.getColumnIndex("goodsid"));
        this.goodsName = cursor.getString(cursor.getColumnIndex("goodsname"));
        this.count = cursor.getInt(cursor.getColumnIndex("count"));
        this.checkCount = cursor.getInt(cursor.getColumnIndex("checkcount"));
        this.region = cursor.getString(cursor.getColumnIndex("region"));
        Double purchase = cursor.getDouble(cursor.getColumnIndex("purchaseprice"));
        this.purchasePrice = purchase == null ? null : BigDecimal.valueOf(purchase);
        Double retail = cursor.getDouble(cursor.getColumnIndex("retailprice"));
        this.retailPrice = retail == null ? null : BigDecimal.valueOf(retail);
        this.getLossNumber = cursor.getInt(cursor.getColumnIndex("lossnumber"));
        Double result = cursor.getDouble(cursor.getColumnIndex("resultprice"));
        this.resultPrice = result == null ? null : BigDecimal.valueOf(result);
        Double checkCount = cursor.getDouble(cursor.getColumnIndex("checkcountprice"));
        this.checkCountPrice = checkCount == null ? null : BigDecimal.valueOf(checkCount);
        this.barCode = cursor.getString(cursor.getColumnIndex("barcode"));
    }
    
}
