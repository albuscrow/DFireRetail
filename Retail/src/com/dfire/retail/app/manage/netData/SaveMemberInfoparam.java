package com.dfire.retail.app.manage.netData;


public class SaveMemberInfoparam extends BaseRequestData {
	private String customer;//会员信息
	private String operateType;//操作类别：add：添加edit：编辑

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getOperateType() {
		return operateType;

	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
}
