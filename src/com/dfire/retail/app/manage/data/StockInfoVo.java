package com.dfire.retail.app.manage.data;

/**
 * 库存信息
 * @author wangpeng
 *
 */
public class StockInfoVo {

	private int nowStore;				//Integer							库存数			
	private double powerPrice;			//	Bigdecimal							平均进货单价			
	private int fallDueNum;				//Integer							临期库存数			
	private String shopId;			//	string				32			商户ID			
	private String goodsName;//				string				32			商品名字			
	private String goodsId;//				string				33			商品ID			
	private double sumMoney;//				BigDecimal							进货总价			
	private String shortCode;//				string				1~10			简码			
	public int getNowStore() {
		return nowStore;
	}
	public void setNowStore(int nowStore) {
		this.nowStore = nowStore;
	}
	public double getPowerPrice() {
		return powerPrice;
	}
	public void setPowerPrice(double powerPrice) {
		this.powerPrice = powerPrice;
	}
	public int getFallDueNum() {
		return fallDueNum;
	}
	public void setFallDueNum(int fallDueNum) {
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
	public double getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(double sumMoney) {
		this.sumMoney = sumMoney;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	@Override
	public String toString() {
		return "StockInfoVoActivity [nowStore=" + nowStore + ", powerPrice="
				+ powerPrice + ", fallDueNum=" + fallDueNum + ", shopId="
				+ shopId + ", goodsName=" + goodsName + ", goodsId=" + goodsId
				+ ", sumMoney=" + sumMoney + ", shortCode=" + shortCode + "]";
	}
	
	

}
