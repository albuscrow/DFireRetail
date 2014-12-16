package com.dfire.retail.app.manage.netData;

import java.util.List;

import com.dfire.retail.app.manage.data.SalesComboVo;

public class SalesComboListResult extends BaseResult{
	private List<SalesComboVo> salesComboList;
	private int pageSize;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<SalesComboVo> getSalesComboList() {
		return salesComboList;
	}

	public void setSalesComboList(List<SalesComboVo> salesComboList) {
		this.salesComboList = salesComboList;
	}
}
