package com.dfire.retail.app.manage.data;

import java.math.BigDecimal;

/**
 * 店铺业绩汇总信息
 * @author kyolee
 *
 */
public class ShopSummarizeVo {

	private String shopId;//商户ID
	private String shopName;//商户名
	private BigDecimal totalRevenue;//总收益
	private BigDecimal totalReceivable;//实收款
	private BigDecimal totalOrderNum;//交易数
	private BigDecimal averageSpend;//人均消费
	
	
	
	
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public BigDecimal getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(BigDecimal totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	public BigDecimal getTotalReceivable() {
		return totalReceivable;
	}
	public void setTotalReceivable(BigDecimal totalReceivable) {
		this.totalReceivable = totalReceivable;
	}
	public BigDecimal getTotalOrderNum() {
		return totalOrderNum;
	}
	public void setTotalOrderNum(BigDecimal totalOrderNum) {
		this.totalOrderNum = totalOrderNum;
	}
	public BigDecimal getAverageSpend() {
		return averageSpend;
	}
	public void setAverageSpend(BigDecimal averageSpend) {
		this.averageSpend = averageSpend;
	}

		

	
}
