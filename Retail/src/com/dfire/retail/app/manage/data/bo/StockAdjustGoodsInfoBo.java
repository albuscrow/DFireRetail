package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.StockAdjustVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

/**
 * 得到调整商品信息
 * @author ys
 *
 */
public class StockAdjustGoodsInfoBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6549435730299401813L;

	/**
	 * 商品信息
	 */
	private StockAdjustVo stockAdjustVo;
	/**
	 * 是否是单店：1是0否
	 */
	private String isSingle;
	public StockAdjustVo getStockAdjustVo() {
		return stockAdjustVo;
	}
	public void setStockAdjustVo(StockAdjustVo stockAdjustVo) {
		this.stockAdjustVo = stockAdjustVo;
	}
	public String getIsSingle() {
		return isSingle;
	}
	public void setIsSingle(String isSingle) {
		this.isSingle = isSingle;
	}
}
