package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

public class PurchaseSaveReturnBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2205931807947443330L;
	/**
	 * 进货id
	 */
	private String stockInId;
	/**
	 * 消息ID
	 */
	private String messageId;
	/**
	 * 进货单编号
	 */
	private String stockInNo;
	
	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}
	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	/**
	 * @return the stockInNo
	 */
	public String getStockInNo() {
		return stockInNo;
	}
	/**
	 * @param stockInNo the stockInNo to set
	 */
	public void setStockInNo(String stockInNo) {
		this.stockInNo = stockInNo;
	}
	/**
	 * @return the stockInId
	 */
	public String getStockInId() {
		return stockInId;
	}
	/**
	 * @param stockInId the stockInId to set
	 */
	public void setStockInId(String stockInId) {
		this.stockInId = stockInId;
	}
}
