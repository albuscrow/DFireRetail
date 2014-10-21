package com.dfire.retail.app.manage.netData;

import java.util.List;

import com.dfire.retail.app.manage.data.CustomerVo;

public class ListResult extends BaseResult{
	private List<CustomerVo> customerList;
	private int pageSize;
	public List<CustomerVo> getCustomerList() {
		return customerList;
	}
	public void setCustomerList(List<CustomerVo> customerList) {
		this.customerList = customerList;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
