package com.dfire.retail.app.manage.data;

import java.io.Serializable;

public class ReceiptStyleVo  implements Serializable{
	private String receiptStyleId	;
	private Integer hasLogo			;
	private String receiptTitle		;
	private String bottomContent	;
	private Integer receiptWidthId	;
	private int lastVer;
	/**
	 * @return the lastVer
	 */
	public int getLastVer() {
		return lastVer;
	}
	/**
	 * @param lastVer the lastVer to set
	 */
	public void setLastVer(int lastVer) {
		this.lastVer = lastVer;
	}
	/**
	 * @return the receiptTemplateCode
	 */
	public String getReceiptTemplateCode() {
		return receiptTemplateCode;
	}
	/**
	 * @param receiptTemplateCode the receiptTemplateCode to set
	 */
	public void setReceiptTemplateCode(String receiptTemplateCode) {
		this.receiptTemplateCode = receiptTemplateCode;
	}
	private String receiptTemplateCode;
	/**
	 * @return the receiptStyleId
	 */
	public String getReceiptStyleId() {
		return receiptStyleId;
	}
	/**
	 * @param receiptStyleId the receiptStyleId to set
	 */
	public void setReceiptStyleId(String receiptStyleId) {
		this.receiptStyleId = receiptStyleId;
	}
	/**
	 * @return the hasLogo
	 */
	public Integer getHasLogo() {
		return hasLogo;
	}
	/**
	 * @param hasLogo the hasLogo to set
	 */
	public void setHasLogo(Integer hasLogo) {
		this.hasLogo = hasLogo;
	}
	/**
	 * @return the receiptTitle
	 */
	public String getReceiptTitle() {
		return receiptTitle;
	}
	/**
	 * @param receiptTitle the receiptTitle to set
	 */
	public void setReceiptTitle(String receiptTitle) {
		this.receiptTitle = receiptTitle;
	}
	/**
	 * @return the bottomContent
	 */
	public String getBottomContent() {
		return bottomContent;
	}
	/**
	 * @param bottomContent the bottomContent to set
	 */
	public void setBottomContent(String bottomContent) {
		this.bottomContent = bottomContent;
	}
	/**
	 * @return the receiptWidthId
	 */
	public Integer getReceiptWidthId() {
		return receiptWidthId;
	}
	/**
	 * @param receiptWidthId the receiptWidthId to set
	 */
	public void setReceiptWidthId(Integer receiptWidthId) {
		this.receiptWidthId = receiptWidthId;
	}
	public boolean getHasLogoBoolean() {
		return hasLogo == 1;
	}
	public void setShowLogo(boolean checked) {
		if (checked) {
			hasLogo = 1;
		}else{
			hasLogo = 0;
		}
	}
	
}
