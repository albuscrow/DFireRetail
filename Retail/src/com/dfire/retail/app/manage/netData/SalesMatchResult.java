package com.dfire.retail.app.manage.netData;

import java.util.List;

import com.dfire.retail.app.manage.data.SalesMatchRuleVo;

public class SalesMatchResult extends BaseResult {
	private List<SalesMatchRuleVo> salesMatchRuleList;
	private int pageSize;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<SalesMatchRuleVo> getSalesMatchRuleList() {
		return salesMatchRuleList;
	}

	public void setSalesMatchRuleList(List<SalesMatchRuleVo> salesMatchRuleList) {
		this.salesMatchRuleList = salesMatchRuleList;
	}
}
