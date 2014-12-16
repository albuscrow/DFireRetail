package com.dfire.retail.app.manage.data;

import java.math.BigDecimal;

/**
 * 进货单详细信息
 * @author wangpeng
 *
 */
public class StockInDetailVo {
	
	private String stockInDetailId; // String          32      订货单ID
	private String goodsId;//				string				32			商品id			
	private String goodsName;//				string				50			商品名称			
	private String goodsBarcode;//				string							商品条码			
	private BigDecimal goodsPrice;//				bigdecimal							商品价格			
	private int goodsSum;//				integer							商品数量			
	private BigDecimal goodsTotalPrice;//				bigdecimal							商品合计价格			
	private Long productionDate;//				Long							生产日期		
	private String operateType;//				string							"操作类型 add：添加 edit：修改 del：删除"		
	
	public String getStockInDetailId() {
		return stockInDetailId;
	}
	public void setStockInDetailId(String stockInDetailId) {
		this.stockInDetailId = stockInDetailId;
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
	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public int getGoodsSum() {
		return goodsSum;
	}
	public void setGoodsSum(int goodsSum) {
		this.goodsSum = goodsSum;
	}
	public BigDecimal getGoodsTotalPrice() {
		return goodsTotalPrice;
	}
	public void setGoodsTotalPrice(BigDecimal goodsTotalPrice) {
		this.goodsTotalPrice = goodsTotalPrice;
	}
	public Long getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(Long productionDate) {
		this.productionDate = productionDate;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	@Override
	public String toString() {
		return "StockInDetailVo [stockInDetailId=" + stockInDetailId
				+ ", goodsId=" + goodsId + ", goodsName=" + goodsName
				+ ", goodsBarcode=" + goodsBarcode + ", goodsPrice="
				+ goodsPrice + ", goodsSum=" + goodsSum + ", goodsTotalPrice="
				+ goodsTotalPrice + ", productionDate=" + productionDate
				+ ", operateType=" + operateType + "]";
	}
}
