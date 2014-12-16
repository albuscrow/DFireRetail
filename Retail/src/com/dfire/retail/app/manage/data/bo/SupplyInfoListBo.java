package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
import com.dfire.retail.app.manage.vo.supplyManageVo;
/**
 * 供应商列表
 * @author ys
 *
 */
public class SupplyInfoListBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5034946700084942593L;
	/**
	 * 供应商列表
	 */
	private List<supplyManageVo> supplyManageList;	
	/**
	 * 总页数	
	 */
	private Integer pageCount;
	/**
	 * @return the supplyManageList
	 */
	public List<supplyManageVo> getSupplyManageList() {
		return supplyManageList;
	}
	/**
	 * @param supplyManageList the supplyManageList to set
	 */
	public void setSupplyManageList(List<supplyManageVo> supplyManageList) {
		this.supplyManageList = supplyManageList;
	}
	/**
	 * @return the pageCount
	 */
	public Integer getPageCount() {
		return pageCount;
	}
	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SupplyInfoListBo [supplyManageList=" + supplyManageList
				+ ", pageCount=" + pageCount + "]";
	}									
}
