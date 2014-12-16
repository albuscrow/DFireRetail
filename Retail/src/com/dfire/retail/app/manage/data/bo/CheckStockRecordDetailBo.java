package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.StockCheckRecordDetailVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 盘点记录查询详细说明
 * @author ys
 *
 */
public class CheckStockRecordDetailBo extends BaseRemoteBo{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8886120131775497937L;
	/**
	 * 库存盘点列表
	 */
	private List<StockCheckRecordDetailVo> stockCheckRecordList;
	/**
	 * 总页数
	 */
	private Integer pageCount;
	
 
	public List<StockCheckRecordDetailVo> getStockCheckRecordList() {
		return stockCheckRecordList;
	}
	public void setStockCheckRecordList(
			List<StockCheckRecordDetailVo> stockCheckRecordList) {
		this.stockCheckRecordList = stockCheckRecordList;
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
}
