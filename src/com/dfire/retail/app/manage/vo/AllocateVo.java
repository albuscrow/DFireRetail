/**
 * 
 */
package com.dfire.retail.app.manage.vo;

import java.util.Date;

/**
 * @author 李锦运 2014-10-27
 */
public class AllocateVo {

	private String allocateId;

	private String entityId;

	private String shopID;

	private String batchNo;

	private double realTotalPrice;

	private int realSum;

	private Date sendStartTIme;

	private int billStatus;

	private String billStatusName;
	
	
	private long sendEndTime;
	
	/**
	 * @return the allocateId
	 */
	public String getAllocateId() {
		return allocateId;
	}

	/**
	 * @param allocateId
	 *            the allocateId to set
	 */
	public void setAllocateId(String allocateId) {
		this.allocateId = allocateId;
	}

	/**
	 * @return the entityId
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the shopID
	 */
	public String getShopID() {
		return shopID;
	}

	/**
	 * @param shopID
	 *            the shopID to set
	 */
	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

	/**
	 * @return the batchNo
	 */
	public String getBatchNo() {
		return batchNo;
	}

	/**
	 * @param batchNo
	 *            the batchNo to set
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * @return the realTotalPrice
	 */
	public double getRealTotalPrice() {
		return realTotalPrice;
	}

	/**
	 * @param realTotalPrice
	 *            the realTotalPrice to set
	 */
	public void setRealTotalPrice(double realTotalPrice) {
		this.realTotalPrice = realTotalPrice;
	}

	/**
	 * @return the realSum
	 */
	public int getRealSum() {
		return realSum;
	}

	/**
	 * @param realSum
	 *            the realSum to set
	 */
	public void setRealSum(int realSum) {
		this.realSum = realSum;
	}

	/**
	 * @return the sendStartTIme
	 */
	public Date getSendStartTIme() {
		return sendStartTIme;
	}

	/**
	 * @param sendStartTIme
	 *            the sendStartTIme to set
	 */
	public void setSendStartTIme(Date sendStartTIme) {
		this.sendStartTIme = sendStartTIme;
	}

	/**
	 * @return the billStatus
	 */
	public int getBillStatus() {
		return billStatus;
	}

	/**
	 * @param billStatus
	 *            the billStatus to set
	 */
	public void setBillStatus(int billStatus) {
		this.billStatus = billStatus;
	}

	/**
	 * @return the billStatusName
	 */
	public String getBillStatusName() {
		return billStatusName;
	}

	/**
	 * @param billStatusName
	 *            the billStatusName to set
	 */
	public void setBillStatusName(String billStatusName) {
		this.billStatusName = billStatusName;
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




}
