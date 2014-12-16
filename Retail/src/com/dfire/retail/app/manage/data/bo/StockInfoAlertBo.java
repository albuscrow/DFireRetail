package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.StockInfoAlertVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

/**
 * 得到提醒参数
 * @author ys
 *
 */
public class StockInfoAlertBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6830898218908795355L;
	
	/**
	 * 提醒参数
	 */
	private StockInfoAlertVo stockInfoAlertVo;

	public StockInfoAlertVo getStockInfoAlertVo() {
		return stockInfoAlertVo;
	}

	public void setStockInfoAlertVo(StockInfoAlertVo stockInfoAlertVo) {
		this.stockInfoAlertVo = stockInfoAlertVo;
	}
}
