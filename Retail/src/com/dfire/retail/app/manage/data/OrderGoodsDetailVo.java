package com.dfire.retail.app.manage.data;

import java.math.BigDecimal;

/**
 * 订货单详细信息
 * @author wangpeng
 *
 */
public class OrderGoodsDetailVo {
	private String orderGoodsDetailId; //   string              32         订货单ID
	private String goodsId;//				string				32			商品ID			
	private String goodsName	;//			string				50			商品名称			
	private String goodsBarcode;//				string							商品条码			
	private BigDecimal goodsPrice;//				bigdecimal							商品价格			
	private int goodsSum;//				integer							商品数量			
	private BigDecimal goodsTotalPrice;//				bigdecimal							商品合计价格			
	private String productionDate;//				Date							生产日期			
	private int nowStore;//				integer							库存数			
	private String operateType;//				string				"操作类型 add：添加 edit：修改 del：删除"		
	
	public String getOrderGoodsDetailId() {
		return orderGoodsDetailId;
	}
	public void setOrderGoodsDetailId(String orderGoodsDetailId) {
		this.orderGoodsDetailId = orderGoodsDetailId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsBarcode() {
		return goodsBarcode;
	}
	public void setGoodsBarcode(String goodsBarcode) {
		this.goodsBarcode = goodsBarcode;
	}
	public int getGoodsSum() {
		return goodsSum;
	}
	public void setGoodsSum(int goodsSum) {
		this.goodsSum = goodsSum;
	}
	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public BigDecimal getGoodsTotalPrice() {
		return goodsTotalPrice;
	}
	public void setGoodsTotalPrice(BigDecimal goodsTotalPrice) {
		this.goodsTotalPrice = goodsTotalPrice;
	}
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public int getNowStore() {
		return nowStore;
	}
	public void setNowStore(int nowStore) {
		this.nowStore = nowStore;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	@Override
	public String toString() {
		return "OrderGoodsDetailVo [goodsId=" + goodsId + ", goodsName="
				+ goodsName + ", goodsBarcode=" + goodsBarcode
				+ ", goodsPrice=" + goodsPrice + ", goodsSum=" + goodsSum
				+ ", goodsTotalPrice=" + goodsTotalPrice + ", productionDate="
				+ productionDate + ", nowStore=" + nowStore + ", operateType="
				+ operateType + "]";
	}


}
