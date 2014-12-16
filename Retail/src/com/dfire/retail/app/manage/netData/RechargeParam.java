package com.dfire.retail.app.manage.netData;

import java.math.BigDecimal;

public class RechargeParam extends BaseRequestData{
	private String cardId;
	private BigDecimal amount;
	private BigDecimal presentAmount;
	private int presentPoint;
	private String rechargeRuleId;
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getPresentAmount() {
		return presentAmount;
	}
	public void setPresentAmount(BigDecimal presentAmount) {
		this.presentAmount = presentAmount;
	}
	public int getPresentPoint() {
		return presentPoint;
	}
	public void setPresentPoint(int presentPoint) {
		this.presentPoint = presentPoint;
	}
	public String getRechargeRuleId() {
		return rechargeRuleId;
	}
	public void setRechargeRuleId(String rechargeRuleId) {
		this.rechargeRuleId = rechargeRuleId;
	}
}
