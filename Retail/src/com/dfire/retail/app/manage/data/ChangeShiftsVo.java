package com.dfire.retail.app.manage.data;

import java.math.BigDecimal;
import java.util.List;

import android.R.integer;

/**
 * 员工交接班信息
 * @author kyolee
 *
 */
public class ChangeShiftsVo {

	private integer cashierOrderNum;//收银单数
	private integer goodsNum;//商品数量
	private integer sellAmount;//销售总额
	private List<StatsPaymentTypeVo> statsPaymentTypeList;//支付方式统计信息
	
	
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
	public List<StatsPaymentTypeVo> getStatsPaymentTypeList() {
		return statsPaymentTypeList;
	}
	public void setStatsPaymentTypeList(
			List<StatsPaymentTypeVo> statsPaymentTypeList) {
		this.statsPaymentTypeList = statsPaymentTypeList;
	}
	

	
}
