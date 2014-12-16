package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.StockInfoVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 库存调整商品
 * @author ys
 *
 */
public class StockGoodsListBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5495461249717287299L;
	/**
	 * 商品列表
	 */
	private List<StockInfoVo> stockInfoList;
	/**
	 * 总页数
	 */
	private Integer pageCount;
	/**
	 * @return the stockInfoList
	 */
	public List<StockInfoVo> getStockInfoList() {
		return stockInfoList;
	}
	/**
	 * @param stockInfoList the stockInfoList to set
	 */
	public void setStockInfoList(List<StockInfoVo> stockInfoList) {
		this.stockInfoList = stockInfoList;
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
