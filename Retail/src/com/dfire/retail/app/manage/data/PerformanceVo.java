package com.dfire.retail.app.manage.data;

import java.math.BigDecimal;

import android.R.integer;

/**
 * PerformanceVo（员工业绩信息）		
 * @author kyolee
 *
 */
public class PerformanceVo {
	
	private integer cashierOrderNum;//收银单数
	private integer goodsNum;//商品数量
	private integer sellAmount;//销售总额
	private BigDecimal payment;//业绩提成
	
	
	public integer getCashierOrderNum() {
		return cashierOrderNum;
	}
	public void setCashierOrderNum(integer cashierOrderNum) {
		this.cashierOrderNum = cashierOrderNum;
	}
	public integer getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(integer goodsNum) {
		this.goodsNum = goodsNum;
	}
	public integer getSellAmount() {
		return sellAmount;
	}
	public void setSellAmount(integer sellAmount) {
		this.sellAmount = sellAmount;
	}
	public BigDecimal getPayment() {
		return payment;
	}
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}
	
	
	

	

}
