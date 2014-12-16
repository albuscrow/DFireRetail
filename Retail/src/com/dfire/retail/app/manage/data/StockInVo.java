package com.dfire.retail.app.manage.data;

import java.io.Serializable;

/**
 * 进货单信息
 * @author wangpeng
 *
 */
public class StockInVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String stockInId	;//			string				32			订货单ID			
	private String stockInNo;//				string				50			进货单号			
	private String sendEndTime;//				date							要求到货日			
	private int billStatus;//				integer							货单状态			
	private int goodsTotalSum;//				integer							合计产品数量			
	private double goodsTotalPrice;//				bigdecimal							合计产品价格			
	private String supplyId;//				string				32			供应商ID			
	private String supplyName;//				string				50			供应商名称			
	private String recordType;//				string				"数据来源 p：进货数据 o：订货数据"		
	private String billStatusName;//           string   订单状态名称
	
	public String getBillStatusName() {
		return billStatusName;
	}
	public void setBillStatusName(String billStatusName) {
		this.billStatusName = billStatusName;
	}
	public String getStockInId() {
		return stockInId;
	}
	public void setStockInId(String stockInId) {
		this.stockInId = stockInId;
	}
	public String getStockInNo() {
		return stockInNo;
	}
	public void setStockInNo(String stockInNo) {
		this.stockInNo = stockInNo;
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
	public String getSupplyId() {
		return supplyId;
	}
	public void setSupplyId(String supplyId) {
		this.supplyId = supplyId;
	}
	public String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	@Override
	public String toString() {
		return "StockInVo [stockInId=" + stockInId + ", stockInNo=" + stockInNo
				+ ", sendEndTime=" + sendEndTime + ", billStatus=" + billStatus
				+ ", goodsTotalSum=" + goodsTotalSum + ", goodsTotalPrice="
				+ goodsTotalPrice + ", supplyId=" + supplyId + ", supplyName="
				+ supplyName + ", recordType=" + recordType + "]";
	}


}
