package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
import com.dfire.retail.app.manage.vo.AllocateVo;
/**
 * 调拨一栏
 * @author ys
 *
 */
public class AllocateListBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8792360174878086364L;
	/**
	 * 调拨一栏
	 */
	private List<AllocateVo> allocateList;
	/**
	 * 总页数
	 */
	private Integer pageSize;
	/**
	 * @return the allocateList
	 */
	public List<AllocateVo> getAllocateList() {
		return allocateList;
	}
	/**
	 * @param allocateList the allocateList to set
	 */
	public void setAllocateList(List<AllocateVo> allocateList) {
		this.allocateList = allocateList;
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
