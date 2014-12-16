package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 订单 修改添加删除  返回
 * @author Administrator
 *
 */
public class OrderGoodsSaveBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8318970638536701477L;

	/**
	 * 订货id
	 */
	private String orderGoodsId;

	/**
	 * 消息ID
	 */
	private String messageId;
	/**
	 * 订单编号
	 */
	private String orderGoodsNo;
	/**
	 * 配送单编号
	 */
	private String distributionNo;
	
	/**
	 * @return the orderGoodsId
	 */
	public String getOrderGoodsId() {
		return orderGoodsId;
	}

	/**
	 * @param orderGoodsId the orderGoodsId to set
	 */
	public void setOrderGoodsId(String orderGoodsId) {
		this.orderGoodsId = orderGoodsId;
	}

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
	 * @return the orderGoodsNo
	 */
	public String getOrderGoodsNo() {
		return orderGoodsNo;
	}

	/**
	 * @param orderGoodsNo the orderGoodsNo to set
	 */
	public void setOrderGoodsNo(String orderGoodsNo) {
		this.orderGoodsNo = orderGoodsNo;
	}

	/**
	 * @return the distributionNo
	 */
	public String getDistributionNo() {
		return distributionNo;
	}

	/**
	 * @param distributionNo the distributionNo to set
	 */
	public void setDistributionNo(String distributionNo) {
		this.distributionNo = distributionNo;
	}
}
