package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 商品列表一览
 * @author ys
 *
 */
public class LogisticsGoodsListBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5764344132174239574L;
	/**
	 * 商品列表
	 */
	private List<SearchGoodsVo> goodsList;
	/**
	 * 总页数
	 */
	private Integer pageSize;
	/**
	 * @return the goodsList
	 */
	public List<SearchGoodsVo> getGoodsList() {
		return goodsList;
	}
	/**
	 * @param goodsList the goodsList to set
	 */
	public void setGoodsList(List<SearchGoodsVo> goodsList) {
		this.goodsList = goodsList;
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
