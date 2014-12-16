package com.dfire.retail.app.manage.data;

import java.io.Serializable;

/**
 * 盘点记录查询
 * @author ys
 */
public class StockCheckRecordVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String stockCheckRecordTime;//				Date							盘点时间			
	private int stockTotalCount;	//			Integer							库存总数			
	private double stockMoney;//				BigDecimal							库存金额			
	private double exhibit;//          BigDecimal							亏盈金额			
	private String stockCheckId;//				String				32			盘点ID	
	private String name; //                     String        操作员的名字  
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStockCheckRecordTime() {
		return stockCheckRecordTime;
	}
	public void setStockCheckRecordTime(String stockCheckRecordTime) {
		this.stockCheckRecordTime = stockCheckRecordTime;
	}
	public int getStockTotalCount() {
		return stockTotalCount;
	}
	public void setStockTotalCount(int stockTotalCount) {
		this.stockTotalCount = stockTotalCount;
	}
	public double getStockMoney() {
		return stockMoney;
	}
	public void setStockMoney(double stockMoney) {
		this.stockMoney = stockMoney;
	}
	public double getExhibit() {
		return exhibit;
	}
	public void setExhibit(double exhibit) {
		this.exhibit = exhibit;
	}
	public String getStockCheckId() {
		return stockCheckId;
	}
	public void setStockCheckId(String stockCheckId) {
		this.stockCheckId = stockCheckId;
	}
	@Override
	public String toString() {
		return "StockCheckRecordVo [stockCheckRecordTime="
				+ stockCheckRecordTime + ", stockTotalCount=" + stockTotalCount
				+ ", stockMoney=" + stockMoney + ", exhibit=" + exhibit
				+ ", stockCheckId=" + stockCheckId + "]";
	}

}
