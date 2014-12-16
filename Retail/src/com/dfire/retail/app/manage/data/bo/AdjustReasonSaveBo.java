package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 添加调整原因
 * @author ys
 *
 */
public class AdjustReasonSaveBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8112838453408300887L;
	/**
	 * 调整类型号
	 */
	private String adjustReasonId;
	public String getAdjustReasonId() {
		return adjustReasonId;
	}
	public void setAdjustReasonId(String adjustReasonId) {
		this.adjustReasonId = adjustReasonId;
	}
}
