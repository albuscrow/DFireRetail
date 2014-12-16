package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 商户一览  2
 * @author Administrator
 *
 */
public class AllShopListIncludeCompanyBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6488691021784338026L;
	/**
	 * 全体商户一览
	 */
	private List<AllShopVo> allShopList;
	/**
	 * 总页数
	 */
	private Integer pageSize;
	/**
	 * @return the allShopList
	 */
	public List<AllShopVo> getAllShopList() {
		return allShopList;
	}
	/**
	 * @param allShopList the allShopList to set
	 */
	public void setAllShopList(List<AllShopVo> allShopList) {
		this.allShopList = allShopList;
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
