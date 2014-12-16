package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.StockCheckRecordVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 盘点记录查询,查询列表
 * @author ys
 *
 */
public class CheckStockRecordListBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1363554512245793766L;

	/**
	 * 库存盘点列表
	 */
	private List<StockCheckRecordVo> stockCheckRecordList;
	/**
	 * 总页数
	 */
	private Integer pageCount;
	public List<StockCheckRecordVo> getStockCheckRecordList() {
		return stockCheckRecordList;
	}
	public void setStockCheckRecordList(
			List<StockCheckRecordVo> stockCheckRecordList) {
		this.stockCheckRecordList = stockCheckRecordList;
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
}
