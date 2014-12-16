package com.dfire.retail.app.manage.data;

import java.math.BigDecimal;

public class RechargeRuleVo {
	private String rechargeRuleId;
	private String name;
	private String startTime;
	private String endTime;
	private int isValid;
	private BigDecimal rechargeThreshoId;
	private BigDecimal money;
	private int point;
	public String getRechargeRuleId() {
		return rechargeRuleId;
	}
	public void setRechargeRuleId(String rechargeRuleId) {
		this.rechargeRuleId = rechargeRuleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public BigDecimal getRechargeThreshoId() {
		return rechargeThreshoId;
	}
	public void setRechargeThreshoId(BigDecimal rechargeThreshoId) {
		this.rechargeThreshoId = rechargeThreshoId;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
}
