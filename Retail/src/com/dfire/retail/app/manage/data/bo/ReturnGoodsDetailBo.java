package com.dfire.retail.app.manage.data.bo;

import java.math.BigDecimal;
import java.util.List;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
import com.dfire.retail.app.manage.vo.ReturnGoodsDetailVo;

/**
 * 退货详情
 * @author ys
 *
 */
public class ReturnGoodsDetailBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8602449431370584110L;
	/**
	 * 商品一栏信息
	 */
	private List<ReturnGoodsDetailVo> returnGoodsDetailList;
	/**
	 * 退货单号
	 */
	private String returnGoodsNo;
	/**
	 * 供应商名称
	 */
	private String supplyName;		
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
	 * 供应商id
	 */
	private String supplyId;
	
	/**
	 * @return the supplyId
	 */
	public String getSupplyId() {
		return supplyId;
	}
	/**
	 * @param supplyId the supplyId to set
	 */
	public void setSupplyId(String supplyId) {
		this.supplyId = supplyId;
	}
	/**
	 * @return the returnGoodsDetailList
	 */
	public List<ReturnGoodsDetailVo> getReturnGoodsDetailList() {
		return returnGoodsDetailList;
	}
	/**
	 * @param returnGoodsDetailList the returnGoodsDetailList to set
	 */
	public void setReturnGoodsDetailList(
			List<ReturnGoodsDetailVo> returnGoodsDetailList) {
		this.returnGoodsDetailList = returnGoodsDetailList;
	}
	/**
	 * @return the returnGoodsNo
	 */
	public String getReturnGoodsNo() {
		return returnGoodsNo;
	}
	/**
	 * @param returnGoodsNo the returnGoodsNo to set
	 */
	public void setReturnGoodsNo(String returnGoodsNo) {
		this.returnGoodsNo = returnGoodsNo;
	}
	/**
	 * @return the supplyName
	 */
	public String getSupplyName() {
		return supplyName;
	}
	/**
	 * @param supplyName the supplyName to set
	 */
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
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
		return "ReturnGoodsDetailBo [returnGoodsDetailList="
				+ returnGoodsDetailList + ", returnGoodsNo=" + returnGoodsNo
				+ ", supplyName=" + supplyName + ", goodsTotalSum="
				+ goodsTotalSum + ", goodsTotalPrice=" + goodsTotalPrice
				+ ", lastVer=" + lastVer + "]";
	}		
}
