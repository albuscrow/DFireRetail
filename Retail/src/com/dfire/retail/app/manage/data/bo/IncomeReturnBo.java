package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.IncomeVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 后台页面收入信息
 * @author Administrator
 *
 */
public class IncomeReturnBo extends BaseRemoteBo{
	
	private IncomeVo inComeMsgDay;
	private IncomeVo inComeMsgYDay;
	private IncomeVo inComeMsgMonth;
	
	//获取当天输入
	public IncomeVo getInComeMsgDay() {
		return inComeMsgDay;
	}
	public void setInComeMsgDay(IncomeVo inComeMsgDay) {
		this.inComeMsgDay = inComeMsgDay;
	}
	//获取昨天收入
	public IncomeVo getInComeMsgYDay() {
		return inComeMsgYDay;
	}
	public void setInComeMsgYDay(IncomeVo inComeMsgYDay) {
		this.inComeMsgYDay = inComeMsgYDay;
	}
	//获取月收入
	public IncomeVo getInComeMsgMonth() {
		return inComeMsgMonth;
	}
	public void setInComeMsgMonth(IncomeVo inComeMsgMonth) {
		this.inComeMsgMonth = inComeMsgMonth;
	}
		

}
