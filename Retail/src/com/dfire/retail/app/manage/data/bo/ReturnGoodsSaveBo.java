package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 门店退货保存
 * @author ys
 *
 */
public class ReturnGoodsSaveBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6013803688098338635L;
	/**
	 * 退货id
	 */
	private String returnGoodsId;
	/**
	 * 消息id
	 */
	private String messageId;
	/**
	 * 退货单号
	 */
	private String returnGoodsNo;
	
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
}
