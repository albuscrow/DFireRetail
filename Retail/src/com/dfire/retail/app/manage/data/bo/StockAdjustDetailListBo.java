package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.StockAdjustVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 库存调整详情
 * @author ys
 *
 */
public class StockAdjustDetailListBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8085112998982759040L;

	private List<StockAdjustVo> stockAdjustList;

	/**
	 * @return the stockAdjustList
	 */
	public List<StockAdjustVo> getStockAdjustList() {
		return stockAdjustList;
	}

	/**
	 * @param stockAdjustList the stockAdjustList to set
	 */
	public void setStockAdjustList(List<StockAdjustVo> stockAdjustList) {
		this.stockAdjustList = stockAdjustList;
	}
}
