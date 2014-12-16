package com.dfire.retail.app.manage.data.bo;

import java.math.BigDecimal;
import java.util.List;

import com.dfire.retail.app.manage.data.LogisticsDetailVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 物流详情
 * @author ys
 *
 */
public class LogisticsDetailBo extends BaseRemoteBo{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1274893622568537833L;
	/**
	 * 物流一览明细信息	
	 */
	private List<LogisticsDetailVo> logisticsDetailList;	
	/**
	 * 物流编号	
	 */
	private String logisticsNo;
	/**
	 * 供应商
	 */
	private String supplyName;				
	/**
	 * 门店名称
	 */
	private String shopName;
	/**
	 * 合计数量
	 */
	private Integer goodsTotalSum;		
	/**
	 * 合计价格
	 */
	private BigDecimal goodsTotalPrice;	
	/**
	 * 类型名称
	 */
	private String typeName;		
	/**
	 * "1:供应商为总部0：供应商不为总部"	
	 */
	private Integer supplyIsHeadShop;
	/**
	 * @return the logisticsDetailList
	 */
	public List<LogisticsDetailVo> getLogisticsDetailList() {
		return logisticsDetailList;
	}
	/**
	 * @param logisticsDetailList the logisticsDetailList to set
	 */
	public void setLogisticsDetailList(List<LogisticsDetailVo> logisticsDetailList) {
		this.logisticsDetailList = logisticsDetailList;
	}
	/**
	 * @return the logisticsNo
	 */
	public String getLogisticsNo() {
		return logisticsNo;
	}
	/**
	 * @param logisticsNo the logisticsNo to set
	 */
	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
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
	 * @return the shopName
	 */
	public String getShopName() {
		return shopName;
	}
	/**
	 * @param shopName the shopName to set
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
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
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	/**
	 * @return the supplyIsHeadShop
	 */
	public Integer getSupplyIsHeadShop() {
		return supplyIsHeadShop;
	}
	/**
	 * @param supplyIsHeadShop the supplyIsHeadShop to set
	 */
	public void setSupplyIsHeadShop(Integer supplyIsHeadShop) {
		this.supplyIsHeadShop = supplyIsHeadShop;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LogisticsDetailBo [logisticsNo=" + logisticsNo
				+ ", supplyName=" + supplyName + ", shopName=" + shopName
				+ ", goodsTotalSum=" + goodsTotalSum + ", goodsTotalPrice="
				+ goodsTotalPrice + ", typeName=" + typeName
				+ ", supplyIsHeadShop=" + supplyIsHeadShop + "]";
	}					
}
