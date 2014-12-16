package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 门店调拨保存
 * @author Administrator
 *
 */
public class RllocateSaveBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5895466515469870003L;
	/**
	 * 调拨ID
	 */
	private String allocateId;
	/**
	 * 消息id
	 */
	private String messageId;
	/**
	 * 调拨单号
	 */
	private String allocateNo;
	
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
	 * @return the allocateNo
	 */
	public String getAllocateNo() {
		return allocateNo;
	}
	/**
	 * @param allocateNo the allocateNo to set
	 */
	public void setAllocateNo(String allocateNo) {
		this.allocateNo = allocateNo;
	}
	/**
	 * @return the allocateId
	 */
	public String getAllocateId() {
		return allocateId;
	}
	/**
	 * @param allocateId the allocateId to set
	 */
	public void setAllocateId(String allocateId) {
		this.allocateId = allocateId;
	}
}
