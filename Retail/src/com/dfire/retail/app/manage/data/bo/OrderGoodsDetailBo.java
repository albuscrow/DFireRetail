package com.dfire.retail.app.manage.data.bo;

import java.math.BigDecimal;
import java.util.List;

import com.dfire.retail.app.manage.data.OrderGoodsDetailVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 订货单详情
 * @author ys
 *
 */
public class OrderGoodsDetailBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8318970638536701477L;
	/**
	 * 商品一栏信息	
	 */
	private List<OrderGoodsDetailVo> orderGoodsDetailList;	
	/**
	 * 要求到货时间
	 */
	private Long sendEndTime;																		
	/**
	 * 进货单号	
	 */
	private String orderGoodsNo;
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
	private Integer lastVer;
	/**
	 * @return the orderGoodsDetailList
	 */
	public List<OrderGoodsDetailVo> getOrderGoodsDetailList() {
		return orderGoodsDetailList;
	}
	/**
	 * @param orderGoodsDetailList the orderGoodsDetailList to set
	 */
	public void setOrderGoodsDetailList(
			List<OrderGoodsDetailVo> orderGoodsDetailList) {
		this.orderGoodsDetailList = orderGoodsDetailList;
	}
	/**
	 * @return the sendEndTime
	 */
	public Long getSendEndTime() {
		return sendEndTime;
	}
	/**
	 * @param sendEndTime the sendEndTime to set
	 */
	public void setSendEndTime(Long sendEndTime) {
		this.sendEndTime = sendEndTime;
	}
	/**
	 * @return the orderGoodsNo
	 */
	public String getOrderGoodsNo() {
		return orderGoodsNo;
	}
	/**
	 * @param orderGoodsNo the orderGoodsNo to set
	 */
	public void setOrderGoodsNo(String orderGoodsNo) {
		this.orderGoodsNo = orderGoodsNo;
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
	public Integer getLastVer() {
		return lastVer;
	}
	/**
	 * @param lastVer the lastVer to set
	 */
	public void setLastVer(Integer lastVer) {
		this.lastVer = lastVer;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderGoodsDetailBo [orderGoodsDetailList="
				+ orderGoodsDetailList + ", sendEndTime=" + sendEndTime
				+ ", orderGoodsNo=" + orderGoodsNo + ", goodsTotalSum="
				+ goodsTotalSum + ", goodsTotalPrice=" + goodsTotalPrice
				+ ", lastVer=" + lastVer + "]";
	}

}
