package com.dfire.retail.app.manage.data.bo;

import java.math.BigDecimal;
import java.util.List;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
import com.dfire.retail.app.manage.vo.allocateDetailVo;
/**
 * 门店调拨详情
 * @author ys
 *
 */
public class AllocateDetailBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7836178894030722340L;
	/**
	 * 商品一栏信息
	 */
	private List<allocateDetailVo> allocateDetailList;
	/**
	 * 调拨单号
	 */
	private String allocateNo;
	/**
	 * 调出店ID
	 */
	private String outShopId;
	/**
	 * 调出店名称
	 */
	private String outShopName;
	/**
	 * 调入店ID
	 */
	private String inShopId;	
	/**
	 * 调入店名称
	 */
	private String inShopName;		
	/**
	 * 合计产品数量
	 */
	private Integer goodsTotalSum;		
	/**
	 * 合计产品价格
	 */
	private BigDecimal goodsTotalPrice;		
	/**
	 * 当前版本
	 */
	private String lastVer;
	/**
	 * @return the allocateDetailList
	 */
	public List<allocateDetailVo> getAllocateDetailList() {
		return allocateDetailList;
	}
	/**
	 * @param allocateDetailList the allocateDetailList to set
	 */
	public void setAllocateDetailList(List<allocateDetailVo> allocateDetailList) {
		this.allocateDetailList = allocateDetailList;
	}
	/**
	 * @return the allocateNo
	 */
	public String getAllocateNo() {
		return allocateNo;
	}
	/**
	 * @param allocateNo the allocateNo to set
	 */
	public void setAllocateNo(String allocateNo) {
		this.allocateNo = allocateNo;
	}
	/**
	 * @return the outShopId
	 */
	public String getOutShopId() {
		return outShopId;
	}
	/**
	 * @param outShopId the outShopId to set
	 */
	public void setOutShopId(String outShopId) {
		this.outShopId = outShopId;
	}
	/**
	 * @return the outShopName
	 */
	public String getOutShopName() {
		return outShopName;
	}
	/**
	 * @param outShopName the outShopName to set
	 */
	public void setOutShopName(String outShopName) {
		this.outShopName = outShopName;
	}
	/**
	 * @return the inShopId
	 */
	public String getInShopId() {
		return inShopId;
	}
	/**
	 * @param inShopId the inShopId to set
	 */
	public void setInShopId(String inShopId) {
		this.inShopId = inShopId;
	}
	/**
	 * @return the inShopName
	 */
	public String getInShopName() {
		return inShopName;
	}
	/**
	 * @param inShopName the inShopName to set
	 */
	public void setInShopName(String inShopName) {
		this.inShopName = inShopName;
	}
	/**
	 * @return the goodsTotalSum
	 */
	public Integer getGoodsTotalSum() {
		return goodsTotalSum;
	}
	/**
	 * @param goodsTotalSum the goodsTotalSum to set
	 */
	public void setGoodsTotalSum(Integer goodsTotalSum) {
		this.goodsTotalSum = goodsTotalSum;
	}
	/**
	 * @return the goodsTotalPrice
	 */
	public BigDecimal getGoodsTotalPrice() {
		return goodsTotalPrice;
	}
	/**
	 * @param goodsTotalPrice the goodsTotalPrice to set
	 */
	public void setGoodsTotalPrice(BigDecimal goodsTotalPrice) {
		this.goodsTotalPrice = goodsTotalPrice;
	}
	/**
	 * @return the lastVer
	 */
	public String getLastVer() {
		return lastVer;
	}
	/**
	 * @param lastVer the lastVer to set
	 */
	public void setLastVer(String lastVer) {
		this.lastVer = lastVer;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AllocateDetailBo [allocateDetailList=" + allocateDetailList
				+ ", allocateNo=" + allocateNo + ", outShopId=" + outShopId
				+ ", outShopName=" + outShopName + ", inShopId=" + inShopId
				+ ", inShopName=" + inShopName + ", goodsTotalSum="
				+ goodsTotalSum + ", goodsTotalPrice=" + goodsTotalPrice
				+ ", lastVer=" + lastVer + "]";
	}																			

}
