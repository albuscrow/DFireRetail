package com.dfire.retail.app.manage.data.bo;

import java.math.BigDecimal;
import java.util.List;

import com.dfire.retail.app.manage.data.StockInDetailVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 进货详情
 * @author ys
 *
 */
public class PurchaseDetailBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3546846761140463468L;
	/**
	 * 商品一栏信息
	 */
	private List<StockInDetailVo> stockInDetailList;
	/**
	 * 进货单号
	 */
	private String stockInNo;
	/**
	 * 数据来源
	 */
	private String recordType;
	/**
	 * 供应商Id
	 */
	private String supplyId;	
	/**
	 * 供应商名称	
	 */
	private String supplyName;		
	/**
	 * 	合计产品数量
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
	 * @return the stockInDetailList
	 */
	public List<StockInDetailVo> getStockInDetailList() {
		return stockInDetailList;
	}
	/**
	 * @param stockInDetailList the stockInDetailList to set
	 */
	public void setStockInDetailList(List<StockInDetailVo> stockInDetailList) {
		this.stockInDetailList = stockInDetailList;
	}
	/**
	 * @return the stockInNo
	 */
	public String getStockInNo() {
		return stockInNo;
	}
	/**
	 * @param stockInNo the stockInNo to set
	 */
	public void setStockInNo(String stockInNo) {
		this.stockInNo = stockInNo;
	}
	/**
	 * @return the recordType
	 */
	public String getRecordType() {
		return recordType;
	}
	/**
	 * @param recordType the recordType to set
	 */
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
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
		return "PurchaseDetailBo [stockInDetailList=" + stockInDetailList
				+ ", stockInNo=" + stockInNo + ", recordType=" + recordType
				+ ", supplyId=" + supplyId + ", supplyName=" + supplyName
				+ ", goodsTotalSum=" + goodsTotalSum + ", goodsTotalPrice="
				+ goodsTotalPrice + ", lastVer=" + lastVer + "]";
	}																				
}
