package com.dfire.retail.app.manage.netData;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * function 收入信息统计
 * @author kyolee
 *
 */
public class IncomeResult {

	private String strTotalIncome = "";
	private String strTotalProfit = "";
	private String strTotalOrderNum = "";
	private String strPerConsume = "";
	
	
	public IncomeResult(String res){
	JSONObject jobj;
	try {
		//获取inComeMsg
		jobj = new JSONObject(res);
		JSONObject incomeMsg = jobj.getJSONObject("inComeMsg");
		
		if(incomeMsg.getString("totalIncome")!=null)
			this.strTotalIncome = incomeMsg.getString("totalIncome");
		if(incomeMsg.getString("totalProfit")!=null)
			this.strTotalProfit = incomeMsg.getString("totalProfit");
		if(incomeMsg.getString("totalOrderNum")!=null)
			this.strTotalOrderNum = incomeMsg.getString("totalOrderNum");
		if(incomeMsg.getString("perConsume")!=null)
			this.strPerConsume = incomeMsg.getString("perConsume");
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}


	public String getStrTotalIncome() {
		return strTotalIncome;
	}


	public void setStrTotalIncome(String strTotalIncome) {
		this.strTotalIncome = strTotalIncome;
	}


	public String getStrTotalProfit() {
		return strTotalProfit;
	}


	public void setStrTotalProfit(String strTotalProfit) {
		this.strTotalProfit = strTotalProfit;
	}


	public String getStrTotalOrderNum() {
		return strTotalOrderNum;
	}


	public void setStrTotalOrderNum(String strTotalOrderNum) {
		this.strTotalOrderNum = strTotalOrderNum;
	}


	public String getStrPerConsume() {
		return strPerConsume;
	}


	public void setStrPerConsume(String strPerConsume) {
		this.strPerConsume = strPerConsume;
	}
	
	
	
}
