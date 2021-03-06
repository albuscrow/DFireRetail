package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dfire.retail.app.manage.global.Constants;

/**
 * 物流商品查询返回
 * 
 */
public class SearchGoodsVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String goodsId;//				string				32			商品ID			
	private String goodsName;//				string				50			商品名称			
	private String barcode;//          string                          商品条码
	private BigDecimal purchasePrice	;//			Bigdecimal							商品进价			
	private int period;//				integer							商品保质期天数			
	private int nowstore;//				integer							前库存数		
	private String fileName;//           String                         商品图片
	private String goodsBarCode;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public int getNowstore() {
		return nowstore;
	}
	public void setNowstore(int nowstore) {
		this.nowstore = nowstore;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	/**
	 * @return the fileName
	 */
	public String getFileNameSmall() {
		if (fileName == null || fileName.length() == 0) {
			return "";
		}else{
			return fileName+Constants.FILENAME_SUFFIX_SMALL;
		}
	}
	@Override
	public String toString() {
		return "SearchGoodsVo [goodsId=" + goodsId + ", goodsName=" + goodsName
				+ ", barcode=" + barcode + ", purchasePrice=" + purchasePrice
				+ ", period=" + period + ", nowstore=" + nowstore
				+ ", fileName=" + fileName + "]";
	}
	/**
	 * @return the goodsBarCode
	 */
	public String getGoodsBarCode() {
		return goodsBarCode;
	}
	/**
	 * @param goodsBarCode the goodsBarCode to set
	 */
	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}
}
