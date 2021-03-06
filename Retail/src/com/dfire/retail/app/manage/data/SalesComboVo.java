package com.dfire.retail.app.manage.data;

import java.math.BigDecimal;
import java.util.List;

public class SalesComboVo {
	private String salesComboId;
	private String salesComboName;
	private String startTime;
	private String endTime;
	private int isValid;
	private List<GoodsVo> goodsList;
	private BigDecimal comboPrice;
	private int isMember;
	public String getSalesComboId() {
		return salesComboId;
	}
	public void setSalesComboId(String salesComboId) {
		this.salesComboId = salesComboId;
	}
	public String getSalesComboName() {
		return salesComboName;
	}
	public void setSalesComboName(String salesComboName) {
		this.salesComboName = salesComboName;
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
	public BigDecimal getComboPrice() {
		return comboPrice;
	}
	public void setComboPrice(BigDecimal comboPrice) {
		this.comboPrice = comboPrice;
	}
	public int getIsMember() {
		return isMember;
	}
	public void setIsMember(int isMember) {
		this.isMember = isMember;
	}
}
