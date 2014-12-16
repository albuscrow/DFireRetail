package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.StockInVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 进货列表
 * @author ys
 *
 */
public class PurchaseListvBo extends BaseRemoteBo{

	private static final long serialVersionUID = -3727011012412986634L;

	/**
	 * 进货一栏
	 */
	private List<StockInVo> stockInList;
	/**
	 * 总页数
	 */
	private Integer pageSize;
	/**
	 * @return the stockInList
	 */
	public List<StockInVo> getStockInList() {
		return stockInList;
	}
	/**
	 * @param stockInList the stockInList to set
	 */
	public void setStockInList(List<StockInVo> stockInList) {
		this.stockInList = stockInList;
	}
	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
