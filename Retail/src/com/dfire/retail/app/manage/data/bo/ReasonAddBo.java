package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 退货原因添加
 * @author ys
 *
 */
public class ReasonAddBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 889438340918968622L;
	/**
	 * id
	 */
	private String resonId;
	/**
	 * @return the resonId
	 */
	public String getResonId() {
		return resonId;
	}
	/**
	 * @param resonId the resonId to set
	 */
	public void setResonId(String resonId) {
		this.resonId = resonId;
	}
	
}
