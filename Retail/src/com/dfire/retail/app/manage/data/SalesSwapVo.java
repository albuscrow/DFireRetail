package com.dfire.retail.app.manage.data;

import java.math.BigDecimal;
import java.util.List;

public class SalesSwapVo {
	private String salesSwapId;
	private String salesSwapName;
	private String startTime;
	private String endTime;
	private int isValid;
	private List<GoodsVo> goodsList;
	private BigDecimal priceSpread;
	private int isMember;
	public String getSalesSwapId() {
		return salesSwapId;
	}
	public void setSalesSwapId(String salesSwapId) {
		this.salesSwapId = salesSwapId;
	}
	public String getSalesSwapName() {
		return salesSwapName;
	}
	public void setSalesSwapName(String salesSwapName) {
		this.salesSwapName = salesSwapName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getIsValid() {
		return isValid;
	}
	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}
	public List<GoodsVo> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<GoodsVo> goodsList) {
		this.goodsList = goodsList;
	}
	public BigDecimal getPriceSpread() {
		return priceSpread;
	}
	public void setPriceSpread(BigDecimal priceSpread) {
		this.priceSpread = priceSpread;
	}
	public int getIsMember() {
		return isMember;
	}
	public void setIsMember(int isMember) {
		this.isMember = isMember;
	}
}
