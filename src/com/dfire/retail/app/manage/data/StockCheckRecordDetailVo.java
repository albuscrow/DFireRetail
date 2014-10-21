package com.dfire.retail.app.manage.data;


/**
 * 盘点记录详情查询
 * 
 */
public class StockCheckRecordDetailVo {

	private String goodsBarCode	;//			String				32			商品条码			
	private int stockTotalCount;//				Integer							库存总数			
	private int stockRealCount	;//			Integer							库存实际存储			
	private int exhibitCount;// 	"				Integer							亏盈数			
	private double sell;//				BigDecimal							销售价			
	private double stockMoney;//				BigDecimal							库存金额			
	private double exhibit;//"				BigDecimal							亏盈金额			
	private String goodsName	;//			String				50			商品名字			
	private String stockCheckManName;//				String				50			操作员姓名			
	private String stockCheckTime;//				Date							盘点时间			
	private String stockCheckManStaffId;//				String				50			操作员工号		
	public String getGoodsBarCode() {
		return goodsBarCode;
	}
	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}
	public int getStockTotalCount() {
		return stockTotalCount;
	}
	public void setStockTotalCount(int stockTotalCount) {
		this.stockTotalCount = stockTotalCount;
	}
	public int getStockRealCount() {
		return stockRealCount;
	}
	public void setStockRealCount(int stockRealCount) {
		this.stockRealCount = stockRealCount;
	}
	public int getExhibitCount() {
		return exhibitCount;
	}
	public void setExhibitCount(int exhibitCount) {
		this.exhibitCount = exhibitCount;
	}
	public double getSell() {
		return sell;
	}
	public void setSell(double sell) {
		this.sell = sell;
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
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getStockCheckManName() {
		return stockCheckManName;
	}
	public void setStockCheckManName(String stockCheckManName) {
		this.stockCheckManName = stockCheckManName;
	}
	public String getStockCheckTime() {
		return stockCheckTime;
	}
	public void setStockCheckTime(String stockCheckTime) {
		this.stockCheckTime = stockCheckTime;
	}
	public String getStockCheckManStaffId() {
		return stockCheckManStaffId;
	}
	public void setStockCheckManStaffId(String stockCheckManStaffId) {
		this.stockCheckManStaffId = stockCheckManStaffId;
	}
	@Override
	public String toString() {
		return "StockCheckRecordDetailVo [goodsBarCode=" + goodsBarCode
				+ ", stockTotalCount=" + stockTotalCount + ", stockRealCount="
				+ stockRealCount + ", exhibitCount=" + exhibitCount + ", sell="
				+ sell + ", stockMoney=" + stockMoney + ", exhibit=" + exhibit
				+ ", goodsName=" + goodsName + ", stockCheckManName="
				+ stockCheckManName + ", stockCheckTime=" + stockCheckTime
				+ ", stockCheckManStaffId=" + stockCheckManStaffId + "]";
	}
	 
	

}
