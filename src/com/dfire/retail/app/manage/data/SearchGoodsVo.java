package com.dfire.retail.app.manage.data;

import java.io.Serializable;

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
	private double purchasePrice	;//			Bigdecimal							商品进价			
	private int period;//				integer							商品保质期天数			
	private int nowstore;//				integer							前库存数			 
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
	public double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(double purchasePrice) {
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
	@Override
	public String toString() {
		return "SearchGoodsVo [goodsId=" + goodsId + ", goodsName=" + goodsName
				+ ", purchasePrice=" + purchasePrice + ", period=" + period
				+ ", nowstore=" + nowstore + "]";
	}


}