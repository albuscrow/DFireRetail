package com.dfire.retail.app.manage.data;

import java.math.BigDecimal;

import android.R.integer;

/**
 * 支付方式统计信息					
 * @author kyolee
 *
 */
public class StatsPaymentTypeVo {

	private integer paymentTypeId;//支付方式ID
	private String paymentTypeName;//支付方式名称
	private integer amount;//销售金额
	
	
	public integer getPaymentTypeId() {
		return paymentTypeId;
	}
	public void setPaymentTypeId(integer paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}
	public String getPaymentTypeName() {
		return paymentTypeName;
	}
	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}
	public integer getAmount() {
		return amount;
	}
	public void setAmount(integer amount) {
		this.amount = amount;
	}



}
