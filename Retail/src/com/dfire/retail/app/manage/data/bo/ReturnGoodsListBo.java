package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
import com.dfire.retail.app.manage.vo.ReturnGoodsVo;

/**
 * 退货列表
 * @author ys
 *
 */
public class ReturnGoodsListBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6398439332261654815L;
	/**
	 * 退货一栏
	 */
	private List<ReturnGoodsVo> returnGoodsList;
	/**
	 * 总页数
	 */
	private Integer pageSize;
	/**
	 * @return the returnGoodsList
	 */
	public List<ReturnGoodsVo> getReturnGoodsList() {
		return returnGoodsList;
	}
	/**
	 * @param returnGoodsList the returnGoodsList to set
	 */
	public void setReturnGoodsList(List<ReturnGoodsVo> returnGoodsList) {
		this.returnGoodsList = returnGoodsList;
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
