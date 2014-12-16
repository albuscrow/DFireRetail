package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author kyolee
 *
 */
public class ReceiptVo implements Serializable {
	private ReceiptStyleVo receiptStyle;
	private ArrayList<ReceiptTemplateVo> receiptTemplateList;
	private ArrayList<ConfigItemOptionVo> receiptWidthList;
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
		this.receiptTemplateList = (ArrayList<ReceiptTemplateVo>) receiptTemplete;
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
		this.receiptWidthList = (ArrayList<ConfigItemOptionVo>) receiptWidthList;
	}
	
	
}
