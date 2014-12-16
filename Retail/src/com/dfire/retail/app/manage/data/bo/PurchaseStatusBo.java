package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

/**
 * 订货  进货状态一栏
 * @author ys
 *
 */
public class PurchaseStatusBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 787055156461412344L;

	/**
	 * statusList
	 */
	private List<DicVo> statusList;

	/**
	 * @return the statusList
	 */
	public List<DicVo> getStatusList() {
		return statusList;
	}

	/**
	 * @param statusList the statusList to set
	 */
	public void setStatusList(List<DicVo> statusList) {
		this.statusList = statusList;
	}
	
	
}
