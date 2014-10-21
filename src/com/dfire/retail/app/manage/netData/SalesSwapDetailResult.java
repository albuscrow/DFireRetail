package com.dfire.retail.app.manage.netData;

import com.dfire.retail.app.manage.data.SalesSwapVo;

public class SalesSwapDetailResult extends BaseResult {
	private SalesSwapVo salesSwap;

	public SalesSwapVo getSalesSwap() {
		return salesSwap;
	}

	public void setSalesSwap(SalesSwapVo salesSwap) {
		this.salesSwap = salesSwap;
	}
}
