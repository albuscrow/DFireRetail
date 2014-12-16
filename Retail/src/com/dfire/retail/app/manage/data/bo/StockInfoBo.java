package com.dfire.retail.app.manage.data.bo;

import java.math.BigDecimal;
import java.util.List;

import com.dfire.retail.app.manage.data.StockInfoVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

//库存查询
public class StockInfoBo  extends BaseRemoteBo{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5169312680949572500L;
	List<StockInfoVo> stockInfoList;//库存信息						
	Integer pageCount;	//总页数						
	BigDecimal sumMoney;//商品总价格				
	Integer	 nowStore;//商品总数

	public List<StockInfoVo> getStockInfoList() {
		return stockInfoList;
	}

	public void setStockInfoList(List<StockInfoVo> stockInfoList) {
		this.stockInfoList = stockInfoList;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public BigDecimal getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(BigDecimal sumMoney) {
		this.sumMoney = sumMoney;
	}

	public Integer getNowStore() {
		return nowStore;
	}

	public void setNowStore(Integer nowStore) {
		this.nowStore = nowStore;
	}					

	

	
	


}
