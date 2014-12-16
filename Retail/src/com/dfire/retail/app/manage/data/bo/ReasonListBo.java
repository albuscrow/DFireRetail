package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

public class ReasonListBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4760922594220223862L;
	/**
	 * 退货原因一栏
	 */
	private List<DicVo> returnResonList;
	/**
	 * @return the returnResonList
	 */
	public List<DicVo> getReturnResonList() {
		return returnResonList;
	}
	/**
	 * @param returnResonList the returnResonList to set
	 */
	public void setReturnResonList(List<DicVo> returnResonList) {
		this.returnResonList = returnResonList;
	}
	
}
