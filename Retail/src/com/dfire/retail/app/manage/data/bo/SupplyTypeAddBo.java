package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

/**
 * 添加供应商类型
 * @author ys
 *
 */
public class SupplyTypeAddBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2627786577583646938L;

	/**
	 * 添加类型编号
	 */
	private Integer typeVal;

	public Integer getTypeVal() {
		return typeVal;
	}

	public void setTypeVal(Integer typeVal) {
		this.typeVal = typeVal;
	}
	
}
