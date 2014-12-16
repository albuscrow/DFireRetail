package com.dfire.retail.app.manage.netData;

import java.util.List;

import com.dfire.retail.app.manage.data.CustomerVo;

public class MemberInfoSearchResult extends BaseResult {
	private List<CustomerVo> customerList;//会员列表信息
	private Integer pageSize;//总页数

	public List<CustomerVo> getCustomerList() {
		return customerList;

	}

	public void setCustomerList(List<CustomerVo> customerList) {
		this.customerList = customerList;
	}

	public Integer getPageSize() {
		return pageSize;

	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
