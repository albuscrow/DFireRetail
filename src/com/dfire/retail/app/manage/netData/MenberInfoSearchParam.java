package com.dfire.retail.app.manage.netData;

public class MenberInfoSearchParam extends BaseRequestData {
	private String keywords;//会员卡/姓名/手机号
	private String dateFrom;//商户ID
	private Integer currentPage;//当前页

	public String getKeywords() {
		return keywords;

	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDateFrom() {
		return dateFrom;

	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Integer getCurrentPage() {
		return currentPage;

	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
}
