package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.StockAdjustVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 库存调整一览
 * @author ys
 *
 */
public class StockAdjustListBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 501285852497681872L;
	
	/**
	 * 库存调整列表
	 */
	private List<StockAdjustVo> stockAdjustList;
	/**
	 * 总页数
	 */
	private Integer pageCount;
 
	/**
	 * @return the stockAdjustList
	 */
	public List<StockAdjustVo> getStockInfoList() {
		return stockAdjustList;
	}
	/**
	 * @param stockAdjustList the stockAdjustList to set
	 */
	public void setStockInfoList(List<StockAdjustVo> stockAdjustList) {
		this.stockAdjustList = stockAdjustList;
	}
	/**
	 * @return the pageCount
	 */
	public Integer getPageCount() {
		return pageCount;
	}
	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
}
