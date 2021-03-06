package com.dfire.retail.app.manage.data;

import java.io.Serializable;

/**
 * 订货单信息
 * @author wangpeng
 *
 */
public class OrderGoodsVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String orderGoodsId;//				string				32			订货单ID			
	private String orderGoodsNo	;//			string				50			进货单号			
	private String sendEndTime;//				date							要求到货日			
	private int billStatus;//				integer							"货单状态 1：未确认  2：确认 "			
	private int goodsTotalSum;//				integer							合计产品数量			
	private double goodsTotalPrice;//				bigdecimal							合计产品价格			
	private String billStatusName;// String  "货单状态 1：确认 2：未确认"	
	
	public String getBillStatusName() {
		return billStatusName;
	}
	public void setBillStatusName(String billStatusName) {
		this.billStatusName = billStatusName;
	}
	public String getOrderGoodsId() {
		return orderGoodsId;
	}
	public void setOrderGoodsId(String orderGoodsId) {
		this.orderGoodsId = orderGoodsId;
	}
	public String getOrderGoodsNo() {
		return orderGoodsNo;
	}
	public void setOrderGoodsNo(String orderGoodsNo) {
		this.orderGoodsNo = orderGoodsNo;
	}
	public String getSendEndTime() {
		return sendEndTime;
	}
	public void setSendEndTime(String sendEndTime) {
		this.sendEndTime = sendEndTime;
	}
	public int getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(int billStatus) {
		this.billStatus = billStatus;
	}
	public int getGoodsTotalSum() {
		return goodsTotalSum;
	}
	public void setGoodsTotalSum(int goodsTotalSum) {
		this.goodsTotalSum = goodsTotalSum;
	}
	public double getGoodsTotalPrice() {
		return goodsTotalPrice;
	}
	public void setGoodsTotalPrice(double goodsTotalPrice) {
		this.goodsTotalPrice = goodsTotalPrice;
	}
	@Override
	public String toString() {
		return "OrderGoodsVo [orderGoodsId=" + orderGoodsId + ", orderGoodsNo="
				+ orderGoodsNo + ", sendEndTime=" + sendEndTime
				+ ", billStatus=" + billStatus + ", goodsTotalSum="
				+ goodsTotalSum + ", goodsTotalPrice=" + goodsTotalPrice + "]";
	}


}
