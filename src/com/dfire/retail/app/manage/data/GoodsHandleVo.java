package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import android.R.integer;
import android.graphics.Bitmap;

/**
 * 用户权限
 * @author kyolee
 *
 */
public class GoodsHandleVo implements Serializable{


	String						              goodsHandId; //	商品操作ID				
	String						              newGoodsId; //	新商品ID				
	Integer						             newGoodsNum; //	新商品数量				
	String					              newGoodsBarCode; //	新商品条码				
	String					              newGoodsName	; //新商品名称				
	Float					           retailPrice		; //新商品销售价				
	Short						               handleType; //	操作区分				1：组装2：拆分3：加工
	String						              memo		; //备注				
	Long						                lastVer	; //	版本号				
	List<GoodsHandleByGoodsVo>         oldGoodsList		; //			老商品list				
	/**
	 * @return the goodsHandId
	 */
	public String getGoodsHandId() {
		return goodsHandId;
	}
	/**
	 * @param goodsHandId the goodsHandId to set
	 */
	public void setGoodsHandId(String goodsHandId) {
		this.goodsHandId = goodsHandId;
	}

	/**
	 * @return the newGoodsId
	 */
	public String getNewGoodsId() {
		return newGoodsId;
	}
	/**
	 * @param newGoodsId the newGoodsId to set
	 */
	public void setNewGoodsId(String newGoodsId) {
		this.newGoodsId = newGoodsId;
	}
	/**
	 * @return the newGoodsNum
	 */
	public Integer getNewGoodsNum() {
		return newGoodsNum;
	}
	/**
	 * @param newGoodsNum the newGoodsNum to set
	 */
	public void setNewGoodsNum(Integer newGoodsNum) {
		this.newGoodsNum = newGoodsNum;
	}
	/**
	 * @return the newGoodsBarCode
	 */
	public String getNewGoodsBarCode() {
		return newGoodsBarCode;
	}
	/**
	 * @param newGoodsBarCode the newGoodsBarCode to set
	 */
	public void setNewGoodsBarCode(String newGoodsBarCode) {
		this.newGoodsBarCode = newGoodsBarCode;
	}
	/**
	 * @return the newGoodsName
	 */
	public String getNewGoodsName() {
		return newGoodsName;
	}
	/**
	 * @param newGoodsName the newGoodsName to set
	 */
	public void setNewGoodsName(String newGoodsName) {
		this.newGoodsName = newGoodsName;
	}
	/**
	 * @return the retailPrice
	 */
	public Float getRetailPrice() {
		return retailPrice;
	}
	/**
	 * @param retailPrice the retailPrice to set
	 */
	public void setRetailPrice(Float retailPrice) {
		this.retailPrice = retailPrice;
	}

	/**
	 * @return the handleType
	 */
	public Short getHandleType() {
		return handleType;
	}
	/**
	 * @param handleType the handleType to set
	 */
	public void setHandleType(Short handleType) {
		this.handleType = handleType;
	}
	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * @return the lastVer
	 */
	public Long getLastVer() {
		return lastVer;
	}
	/**
	 * @param lastVer the lastVer to set
	 */
	public void setLastVer(Long lastVer) {
		this.lastVer = lastVer;
	}
	/**
	 * @return the oldGoodsList
	 */
	public List<GoodsHandleByGoodsVo> getOldGoodsList() {
		return oldGoodsList;
	}
	/**
	 * @param oldGoodsList the oldGoodsList to set
	 */
	public void setOldGoodsList(List<GoodsHandleByGoodsVo> oldGoodsList) {
		this.oldGoodsList = oldGoodsList;
	}
	
	public String getImageUrl() {
		return "";
	}
	
	
}
