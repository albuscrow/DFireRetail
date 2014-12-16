package com.dfire.retail.app.manage.netData;

import java.util.List;

import com.dfire.retail.app.manage.data.CustomerVo;

public class MemberInfoDetailResult extends BaseResult {
	private List<CustomerVo> customer;//会员信息

	public List<CustomerVo> getCustomer() {
		return customer;

	}

	public void setCustomer(List<CustomerVo> customer) {
		this.customer = customer;
	}
}
