/**
 * 
 */
package com.dfire.retail.app.manage.vo;

import java.io.Serializable;

/**
 * @author 李锦运 2014-10-22 ReturnGoodsVo（退货单信息）
 */
public class ReturnGoodsVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String returnGoodsId;
	
	private String returnGoodsNo;
	
	private long sendEndTime;
	
	private int billStatus;
	
	private int goodsTotalSum;
	
	private double goodsTotalPrice;
	
	private String supplyId;
	
	private String supplyName;
	
	private String billStatusName;

	/**
	 * @return the returnGoodsId
	 */
	public String getReturnGoodsId() {
		return returnGoodsId;
	}

	/**
	 * @param returnGoodsId the returnGoodsId to set
	 */
	public void setReturnGoodsId(String returnGoodsId) {
		this.returnGoodsId = returnGoodsId;
	}

	/**
	 * @return the returnGoodsNo
	 */
	public String getReturnGoodsNo() {
		return returnGoodsNo;
	}

	/**
	 * @param returnGoodsNo the returnGoodsNo to set
	 */
	public void setReturnGoodsNo(String returnGoodsNo) {
		this.returnGoodsNo = returnGoodsNo;
	}

	/**
	 * @return the sendEndTime
	 */
	public long getSendEndTime() {
		return sendEndTime;
	}

	/**
	 * @param sendEndTime the sendEndTime to set
	 */
	public void setSendEndTime(long sendEndTime) {
		this.sendEndTime = sendEndTime;
	}

	/**
	 * @return the billStatus
	 */
	public int getBillStatus() {
		return billStatus;
	}

	/**
	 * @param billStatus the billStatus to set
	 */
	public void setBillStatus(int billStatus) {
		this.billStatus = billStatus;
	}

	/**
	 * @return the goodsTotalSum
	 */
	public int getGoodsTotalSum() {
		return goodsTotalSum;
	}

	/**
	 * @param goodsTotalSum the goodsTotalSum to set
	 */
	public void setGoodsTotalSum(int goodsTotalSum) {
		this.goodsTotalSum = goodsTotalSum;
	}

	/**
	 * @return the goodsTotalPrice
	 */
	public double getGoodsTotalPrice() {
		return goodsTotalPrice;
	}

	/**
	 * @param goodsTotalPrice the goodsTotalPrice to set
	 */
	public void setGoodsTotalPrice(double goodsTotalPrice) {
		this.goodsTotalPrice = goodsTotalPrice;
	}

	/**
	 * @return the supplyId
	 */
	public String getSupplyId() {
		return supplyId;
	}

	/**
	 * @param supplyId the supplyId to set
	 */
	public void setSupplyId(String supplyId) {
		this.supplyId = supplyId;
	}

	/**
	 * @return the supplyName
	 */
	public String getSupplyName() {
		return supplyName;
	}

	/**
	 * @param supplyName the supplyName to set
	 */
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}

	/**
	 * @return the billStatusName
	 */
	public String getBillStatusName() {
		return billStatusName;
	}

	/**
	 * @param billStatusName the billStatusName to set
	 */
	public void setBillStatusName(String billStatusName) {
		this.billStatusName = billStatusName;
	}

}
