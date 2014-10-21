package com.dfire.retail.app.manage.data;

import java.sql.Date;

import android.R.integer;

public class CardVo {
	private String cardId;//会员卡ID
	private String kindCardId;//会员卡类型ID
	private String kindCardName;//会员卡类型名称
	private double balance;//余额
	private double consumeAmount;//累计消费
	private integer consumeCount;//消费次数
	private integer point;//积分
	private Date activeDate;//办卡日期
	private String status;//卡状态

	public String getCardId() {
		return cardId;

	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getKindCardId() {
		return kindCardId;

	}

	public void setKindCardId(String kindCardId) {
		this.kindCardId = kindCardId;
	}

	public String getKindCardName() {
		return kindCardName;

	}

	public void setKindCardName(String kindCardName) {
		this.kindCardName = kindCardName;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(double consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	public integer getConsumeCount() {
		return consumeCount;
	}

	public void setConsumeCount(integer consumeCount) {
		this.consumeCount = consumeCount;
	}

	public integer getPoint() {
		return point;
	}

	public void setPoint(integer point) {
		this.point = point;
	}

	public Date getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
