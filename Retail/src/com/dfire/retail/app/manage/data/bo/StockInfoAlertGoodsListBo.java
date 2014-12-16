package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 商品查询
 * @author ys
 *
 */
public class StockInfoAlertGoodsListBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1858251876081706487L;
	/**
	 * 商品信息如果查不到任何商品则不作输出
	 */
	private List<GoodsVo> goodsList;
	/**
	 * 总页数
	 */
	private Integer pageCount;
	public List<GoodsVo> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<GoodsVo> goodsList) {
		this.goodsList = goodsList;
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
}
