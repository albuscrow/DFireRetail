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
 * 类名称：CodeSearchCheckGoodsVo  
 * 类描述：   库存盘点 查询商品
 * 创建时间：2014年11月18日 上午9:42:57  
 * @author chengzi  
 * @version 1.0
 */
public class CodeSearchCheckGoodsVo implements Serializable {

    private static final long serialVersionUID = -7875530838636153890L;

    private String goodsId;
    private String goodsName;
    private BigDecimal retailPrice;
    private BigDecimal purchasePrice;
    private String barcode;
    private Integer nowStore;
    private String fileName;
    private String spell;
    private String shortCode;
    
    private String shopId;
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
    public BigDecimal getRetailPrice() {
        return retailPrice;
    }
    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }
    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    public Integer getNowStore() {
        return nowStore;
    }
    public void setNowStore(Integer nowStore) {
        this.nowStore = nowStore;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getSpell() {
        return spell;
    }
    public void setSpell(String spell) {
        this.spell = spell;
    }
    public String getShortCode() {
        return shortCode;
    }
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
    public String getShopId() {
        return shopId;
    }
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();  
        contentValues.put("goodsid", this.goodsId);
        contentValues.put("goodsname", this.goodsName);
        contentValues.put("purchaseprice", this.purchasePrice == null ? null : purchasePrice.doubleValue());
        contentValues.put("retailprice", this.retailPrice == null ? null : retailPrice.doubleValue());
        contentValues.put("nowstore", this.nowStore);
        contentValues.put("barcode", this.barcode);
        contentValues.put("filename", this.fileName);
        contentValues.put("spell", this.spell);
        contentValues.put("shortcode", this.shortCode);
        contentValues.put("shopid", this.shopId);
        
        return contentValues;
    }
    
    public void doInit(Cursor cursor){
        this.goodsId = cursor.getString(cursor.getColumnIndex("goodsid"));
        this.goodsName = cursor.getString(cursor.getColumnIndex("goodsname"));
        Double purchase = cursor.getDouble(cursor.getColumnIndex("purchaseprice"));
        this.purchasePrice = purchase == null ? null : BigDecimal.valueOf(purchase);
        Double retail = cursor.getDouble(cursor.getColumnIndex("retailprice"));
        this.retailPrice = retail == null ? null : BigDecimal.valueOf(retail);
        this.nowStore = cursor.getInt(cursor.getColumnIndex("nowstore"));
        this.barcode = cursor.getString(cursor.getColumnIndex("barcode"));
        this.fileName = cursor.getString(cursor.getColumnIndex("filename"));
        this.spell = cursor.getString(cursor.getColumnIndex("spell"));
        this.shortCode = cursor.getString(cursor.getColumnIndex("shortcode"));
        this.shopId = cursor.getString(cursor.getColumnIndex("shopid"));
    }
}
