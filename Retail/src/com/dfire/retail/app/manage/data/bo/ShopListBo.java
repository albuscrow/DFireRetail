package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

public class ShopListBo extends BaseRemoteBo{

	private List<AllShopVo> shopList;
	/**
	 * 总页数
	 */
	private Integer pageSize;
	
	public List<AllShopVo> getShopList() {
		return shopList;
	}
	
	public void setShopList(List<AllShopVo> shopList) {
		this.shopList = shopList;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	
	
}
