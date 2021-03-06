package com.dfire.retail.app.manage.data;

import java.io.Serializable;

public class GoodsHandleByGoodsVo implements Serializable{
String			goodsId		;// 商品ID
	String		goodsName	;// 商品名称
String			barCode		;// 商品条形码
	Integer	oldGoodsNum		;// 老商品数量
	String type;
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the goodsId
	 */
	public String getGoodsId() {
		return goodsId;
	}
	/**
	 * @param goodsId the goodsId to set
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
	 * @param goodsName the goodsName to set
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	/**
	 * @return the barCode
	 */
	public String getBarCode() {
		return barCode;
	}
	/**
	 * @param barCode the barCode to set
	 */
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	/**
	 * @return the oldGoodsNum
	 */
	public Integer getOldGoodsNum() {
		return oldGoodsNum;
	}
	/**
	 * @param oldGoodsNum the oldGoodsNum to set
	 */
	public void setOldGoodsNum(Integer oldGoodsNum) {
		this.oldGoodsNum = oldGoodsNum;
	}

	
}
