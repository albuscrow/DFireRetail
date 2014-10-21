package com.dfire.retail.app.manage.data;

public class ReceiptTemplateVo {
	private String templateCode ;
	private String templateName		;
	/**
	 * @return the receiptTemplateId
	 */
	public String getReceiptTemplateId() {
		return templateCode;
	}
	/**
	 * @param receiptTemplateId the receiptTemplateId to set
	 */
	public void setReceiptTemplateId(String receiptTemplateId) {
		this.templateCode = receiptTemplateId;
	}
	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}
	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	@Override
	public String toString() {
		if (templateName == null) {
			return "";
		}
		return templateName;
	}
	
}
