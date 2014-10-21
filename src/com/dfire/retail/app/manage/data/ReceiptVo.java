package com.dfire.retail.app.manage.data;

import java.util.List;


/**
 * @author kyolee
 *
 */
public class ReceiptVo {
	private ReceiptStyleVo receiptStyle;
	private List<ReceiptTemplateVo> receiptTemplateList;
	private List<ConfigItemOptionVo> receiptWidthList;
	/**
	 * @return the receiptStyle
	 */
	public ReceiptStyleVo getReceiptStyle() {
		return receiptStyle;
	}
	/**
	 * @param receiptStyle the receiptStyle to set
	 */
	public void setReceiptStyle(ReceiptStyleVo receiptStyle) {
		this.receiptStyle = receiptStyle;
	}
	/**
	 * @return the receiptTemplete
	 */
	public List<ReceiptTemplateVo> getReceiptTemplete() {
		return receiptTemplateList;
	}
	/**
	 * @param receiptTemplete the receiptTemplete to set
	 */
	public void setReceiptTemplete(List<ReceiptTemplateVo> receiptTemplete) {
		this.receiptTemplateList = receiptTemplete;
	}
	/**
	 * @return the receiptWidthList
	 */
	public List<ConfigItemOptionVo> getReceiptWidthList() {
		return receiptWidthList;
	}
	/**
	 * @param receiptWidthList the receiptWidthList to set
	 */
	public void setReceiptWidthList(List<ConfigItemOptionVo> receiptWidthList) {
		this.receiptWidthList = receiptWidthList;
	}
	
	
}