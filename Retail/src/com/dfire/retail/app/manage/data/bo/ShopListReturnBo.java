package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

/**
 * 返回店家列表信息
 * @author Administrator
 *
 */
public class ShopListReturnBo  extends BaseRemoteBo{

	List<AllShopVo> allShopList;
	Integer pageSize;
	
	//所有的子店信息
	public List<AllShopVo> getAllShopList() {
		return allShopList;
	}
	
	public void setAllShopList(List<AllShopVo> allShopList) {
		this.allShopList = allShopList;
	}
	
	//页数信息
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
