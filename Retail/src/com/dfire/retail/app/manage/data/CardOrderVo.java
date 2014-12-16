package com.dfire.retail.app.manage.data;

import java.math.BigDecimal;

public class CardOrderVo {
	private String cardId;//会员卡ID
	private String orderId;//订单ID
	private String code;//单号/流水号
	private long consumeFee;//消费额度
	private String payType;//付款方式
	private Integer point;//获取积分
	private String orderTime;//交易日期

	public String getCardId() {
		return cardId;

	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getOrderId() {
		return orderId;

	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCode() {
		return code;

	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getConsumeFee() {
		return consumeFee;
	}

	public void setConsumeFee(long consumeFee) {
		this.consumeFee = consumeFee;
	}

	public String getPayType() {
		return payType;

	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Integer getPoint() {
		return point;

	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public String getOrderTime() {
		return orderTime;

	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
}
