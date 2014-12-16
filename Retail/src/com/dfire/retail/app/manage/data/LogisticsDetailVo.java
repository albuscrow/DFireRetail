package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物流Vo
 * @author 李锦运
 *
 */
public class LogisticsDetailVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String goodsId;//				string				32			商品id			
	private String goodsName;//			string						    商品名称		
	private BigDecimal goodsPrice;//				string							价格		
	private Integer goodsSum;//				bigdecimal							合计数量			
	private BigDecimal goodsTotalPrice;//				integer							合计价格		
	private String goodsBarcode;//				String						条码		
	
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
	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Integer getGoodsSum() {
		return goodsSum;
	}
	public void setGoodsSum(Integer goodsSum) {
		this.goodsSum = goodsSum;
	}
	public BigDecimal getGoodsTotalPrice() {
		return goodsTotalPrice;
	}
	public void setGoodsTotalPrice(BigDecimal goodsTotalPrice) {
		this.goodsTotalPrice = goodsTotalPrice;
	}

	public String getGoodsBarcode() {
		return goodsBarcode;
	}

	public void setGoodsBarcode(String goodsBarcode) {
		this.goodsBarcode = goodsBarcode;
	}

	@Override
	public String toString() {
		return "LogisticsDetailVo [goodsId=" + goodsId + ", goodsName="
				+ goodsName + ", goodsPrice=" + goodsPrice
				+ ", goodsSum=" + goodsSum + ", goodsTotalPrice="
				+ goodsTotalPrice + ", goodsBarcode=" + goodsBarcode+"]";
	}
}
