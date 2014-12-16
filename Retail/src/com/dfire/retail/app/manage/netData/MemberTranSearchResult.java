package com.dfire.retail.app.manage.netData;

import java.util.List;

import com.dfire.retail.app.manage.data.CustomerVo;

public class MemberTranSearchResult extends BaseResult {
	private List<CustomerVo> customerList;
	private Integer pageSize;

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
