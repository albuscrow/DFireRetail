package com.dfire.retail.app.manage.netData;

import com.dfire.retail.app.manage.data.SalesCouponVo;

public class CouponDetailResult extends BaseResult{
	private SalesCouponVo salesCoupon;

	public SalesCouponVo getSalesCoupon() {
		return salesCoupon;
	}

	public void setSalesCoupon(SalesCouponVo salesCoupon) {
		this.salesCoupon = salesCoupon;
	}
}
