package com.dfire.retail.app.manage.data;

import java.math.BigDecimal;

public class SalesMatchRuleVo {
	private String salesMatchRuleId;
	private String salesMarchRuleName;
	private String startTime;
	private String endTime;
	private int isValid;
	private BigDecimal matchConsume;
	private BigDecimal reduce;
	private String giftGoodsId;
	private int isMember;
	public String getSalesMatchRuleId() {
		return salesMatchRuleId;
	}
	public void setSalesMatchRuleId(String salesMatchRuleId) {
		this.salesMatchRuleId = salesMatchRuleId;
	}
	public String getSalesMarchRuleName() {
		return salesMarchRuleName;
	}
	public void setSalesMarchRuleName(String salesMarchRuleName) {
		this.salesMarchRuleName = salesMarchRuleName;
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
	public BigDecimal getMatchConsume() {
		return matchConsume;
	}
	public void setMatchConsume(BigDecimal matchConsume) {
		this.matchConsume = matchConsume;
	}
	public BigDecimal getReduce() {
		return reduce;
	}
	public void setReduce(BigDecimal reduce) {
		this.reduce = reduce;
	}
	public String getGiftGoodsId() {
		return giftGoodsId;
	}
	public void setGiftGoodsId(String giftGoodsId) {
		this.giftGoodsId = giftGoodsId;
	}
	public int getIsMember() {
		return isMember;
	}
	public void setIsMember(int isMember) {
		this.isMember = isMember;
	}
}
