package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.OrderGoodsVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 门店订货一栏
 * @author ys
 *
 */
public class OrderGoodsListBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2865140982475666674L;
	/**
	 * 订货一栏
	 */
	private List<OrderGoodsVo> orderGoodsList;
	/**
	 * 总页数
	 */
	private Integer pageSize;
	/**
	 * @return the orderGoodsList
	 */
	public List<OrderGoodsVo> getOrderGoodsList() {
		return orderGoodsList;
	}
	/**
	 * @param orderGoodsList the orderGoodsList to set
	 */
	public void setOrderGoodsList(List<OrderGoodsVo> orderGoodsList) {
		this.orderGoodsList = orderGoodsList;
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
