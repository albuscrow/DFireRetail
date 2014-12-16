package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dfire.retail.app.manage.global.Constants;

/**
 * 库存信息
 * @author ys
 *
 */
public class StockInfoVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6834547101869109822L;
	private Integer nowStore;				//Integer							库存数			
	private BigDecimal powerPrice;			//	Bigdecimal							平均进货单价			
	private Integer fallDueNum;				//Integer							临期库存数			
	private String shopId;			//	string				32			商户ID			
	private String goodsName;//				string				32			商品名字			
	private String goodsId;//				string				33			商品ID			
	private BigDecimal sumMoney;//				BigDecimal							进货总价			
	private String barCode;//				string				1~10			简码			
	private String fileName;//  String                        商品图片
	public Integer getNowStore() {
		return nowStore;
	}
	public void setNowStore(Integer nowStore) {
		this.nowStore = nowStore;
	}
	public BigDecimal getPowerPrice() {
		return powerPrice;
	}
	public void setPowerPrice(BigDecimal powerPrice) {
		this.powerPrice = powerPrice;
	}
	public Integer getFallDueNum() {
		return fallDueNum;
	}
	public void setFallDueNum(Integer fallDueNum) {
		this.fallDueNum = fallDueNum;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public BigDecimal getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(BigDecimal sumMoney) {
		this.sumMoney = sumMoney;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
		return "StockInfoVo [nowStore=" + nowStore + ", powerPrice="
				+ powerPrice + ", fallDueNum=" + fallDueNum + ", shopId="
				+ shopId + ", goodsName=" + goodsName + ", goodsId=" + goodsId
				+ ", sumMoney=" + sumMoney + ", barCode=" + barCode
				+ ", fileName=" + fileName + "]";
	}
	 
}
