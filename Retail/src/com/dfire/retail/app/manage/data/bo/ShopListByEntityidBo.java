package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

/**
 * 门店调拨选择门店  类型2 不含分公司
 * @author ys
 *
 */
public class ShopListByEntityidBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4265600409803687469L;

	/**
	 * 
	 */
	private List<AllShopVo> shopList;
	
	/**
	 * 总页数
	 */
	private Integer pageSize;

	/**
	 * @return the shopList
	 */
	public List<AllShopVo> getShopList() {
		return shopList;
	}

	/**
	 * @param shopList the shopList to set
	 */
	public void setShopList(List<AllShopVo> shopList) {
		this.shopList = shopList;
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
