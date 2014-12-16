/**
 * 
 */
package com.dfire.retail.app.manage.vo;

import java.math.BigDecimal;

/**
 * @author 李锦运 2014-10-27
 */
public class allocateDetailVo {

	private String allocateDetailId;

	private String goodsId;

	private String goodsName;

	private String goodsBarcode;

	private BigDecimal goodsPrice;

	private int goodsSum;

	private BigDecimal goodsTotalPrice;

	private long productionDate;

	private String operateType;

	/**
	 * @return the allocateDetailId
	 */
	public String getAllocateDetailId() {
		return allocateDetailId;
	}

	/**
	 * @param allocateDetailId
	 *            the allocateDetailId to set
	 */
	public void setAllocateDetailId(String allocateDetailId) {
		this.allocateDetailId = allocateDetailId;
	}

	/**
	 * @return the goodsId
	 */
	public String getGoodsId() {
		return goodsId;
	}

	/**
	 * @param goodsId
	 *            the goodsId to set
	 */
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	/**
	 * @return the goodsName
	 */
	public String getGoodsName() {
		return goodsName;
	}

	/**
	 * @param goodsName
	 *            the goodsName to set
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/**
	 * @return the goodsBarcode
	 */
	public String getGoodsBarcode() {
		return goodsBarcode;
	}

	/**
	 * @param goodsBarcode
	 *            the goodsBarcode to set
	 */
	public void setGoodsBarcode(String goodsBarcode) {
		this.goodsBarcode = goodsBarcode;
	}

	/**
	 * @return the goodsPrice
	 */
	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	/**
	 * @param goodsPrice the goodsPrice to set
	 */
	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	/**
	 * @return the goodsSum
	 */
	public int getGoodsSum() {
		return goodsSum;
	}

	/**
	 * @param goodsSum
	 *            the goodsSum to set
	 */
	public void setGoodsSum(int goodsSum) {
		this.goodsSum = goodsSum;
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
	 * @return the productionDate
	 */
	public long getProductionDate() {
		return productionDate;
	}

	/**
	 * @param productionDate
	 *            the productionDate to set
	 */
	public void setProductionDate(long productionDate) {
		this.productionDate = productionDate;
	}

	/**
	 * @return the operateType
	 */
	public String getOperateType() {
		return operateType;
	}

	/**
	 * @param operateType
	 *            the operateType to set
	 */
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

}