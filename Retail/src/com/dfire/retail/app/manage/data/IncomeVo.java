package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class IncomeVo  implements Serializable{
	private BigDecimal totalIncome;//				bigdecimal	
	private BigDecimal totalOrderNum;//				bigdecimal	
	private BigDecimal totalProfit;//				bigdecimal	
	private BigDecimal perConsume;//				bigdecimal	
	
	public BigDecimal getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}
	public BigDecimal getTotalOrderNum() {
		return totalOrderNum;
	}
	
	
	public void setTotalOrderNum(BigDecimal totalOrderNum) {
		this.totalOrderNum = totalOrderNum;
	}
	
	
	public BigDecimal getTotalProfit() {
		return totalProfit;
	}
	
	
	public void setTotalProfit(BigDecimal totalProfit) {
		this.totalProfit = totalProfit;
	}
	
	
	public BigDecimal getPerConsume() {
		return perConsume;
	}
	
	public void setPerConsume(BigDecimal perConsume) {
		this.perConsume = perConsume;
	}
	
	
}
