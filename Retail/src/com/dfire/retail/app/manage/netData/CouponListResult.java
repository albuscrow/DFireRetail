package com.dfire.retail.app.manage.netData;

import java.util.List;

import com.dfire.retail.app.manage.data.SalesCouponVo;

public class CouponListResult extends BaseResult{
	private List<SalesCouponVo> salesCouponList;
	private int pageSize;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<SalesCouponVo> getSalesCouponList() {
		return salesCouponList;
	}

	public void setSalesCouponList(List<SalesCouponVo> salesCouponList) {
		this.salesCouponList = salesCouponList;
	}
}
