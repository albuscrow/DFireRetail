package com.dfire.retail.app.manage.netData;

import com.dfire.retail.app.manage.data.SalesMatchRuleVo;

public class SalesMatchDetailResult extends BaseResult {
	private SalesMatchRuleVo salesMatchRule;

	public SalesMatchRuleVo getSalesMatchRule() {
		return salesMatchRule;
	}

	public void setSalesMatchRule(SalesMatchRuleVo salesMatchRule) {
		this.salesMatchRule = salesMatchRule;
	}
}
