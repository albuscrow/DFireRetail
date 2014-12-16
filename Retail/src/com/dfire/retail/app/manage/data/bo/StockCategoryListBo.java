package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.CategoryVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

/**
 * 分类一览
 * @author ys
 *
 */
public class StockCategoryListBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1225283122345786122L;

	/**
	 * 分类列表
	 */
	private List<CategoryVo> categoryList;

	public List<CategoryVo> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryVo> categoryList) {
		this.categoryList = categoryList;
	}
	
}
