package com.dfire.retail.app.manage.netData;

public class SalesMatchDetailRequestData extends BaseRequestData {
	private String salesMatchRuleId;
	private String operateType;
	public String getSalesMatchRuleId() {
		return salesMatchRuleId;
	}
	public void setSalesMatchRuleId(String salesMatchRuleId) {
		this.salesMatchRuleId = salesMatchRuleId;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
}
