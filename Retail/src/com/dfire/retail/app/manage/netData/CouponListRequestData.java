package com.dfire.retail.app.manage.netData;

public class CouponListRequestData extends BaseRequestData{
	private int currentPage;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
