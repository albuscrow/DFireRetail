package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dfire.retail.app.manage.global.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GoodsVo implements Serializable {


	String  goodsId		  ;
	String  goodsName	  ;
	String  baseId		  ;
	String  barCode		  ;
	String  innerCode	  ;
	String  shortCode	  ;
	String  type		  ;
	String  spell		  ;
	String  brandId		  ;
	String  brandName	  ;
	/**
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}
	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	String  specification ;
	String  categoryId	  ;
	String  categoryName  ;
	String  origin		  ;
	Integer period		  ;
	String  percentage	  ;
	Integer hasDegree	  ;
	Integer isSales		  ;
	String  purchasePrice ;
	String  petailPrice	  ;
	Integer number		  ;
	String  synShopId	  ;
	String  unitId		  ;
	String  memo		  ;
	Long    lastVer		  ;
	byte[]  file		  ;
	String  fileName	  ;
	
	/**
	 * @return the innerCode
	 */
	public String getInnerCode() {
		return innerCode;
	}
	/**
	 * @param innerCode the innerCode to set
	 */
	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
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
	 * @return the baseId
	 */
	public String getBaseId() {
		return baseId;
	}
	/**
	 * @param baseId the baseId to set
	 */
	public void setBaseId(String baseId) {
		this.baseId = baseId;
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
	 * @return the shortCode
	 */
	public String getShortCode() {
		return shortCode;
	}
	/**
	 * @param shortCode the shortCode to set
	 */
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
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
	 * @return the spell
	 */
	public String getSpell() {
		return spell;
	}
	/**
	 * @param spell the spell to set
	 */
	public void setSpell(String spell) {
		this.spell = spell;
	}
	/**
	 * @return the brandId
	 */
	public String getBrandId() {
		return brandId;
	}
	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	/**
	 * @return the specification
	 */
	public String getSpecification() {
		return specification;
	}
	/**
	 * @param specification the specification to set
	 */
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	/**
	 * @return the categoryId
	 */
	public String getCategoryId() {
		return categoryId;
	}
	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}
	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	/**
	 * @return the period
	 */
	public Integer getPeriod() {
		return period;
	}
	/**
	 * @param period the period to set
	 */
	public void setPeriod(Integer period) {
		this.period = period;
	}
	/**
	 * @return the percentage
	 */
	public String getPercentage() {
		return percentage;
	}
	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	/**
	 * @return the hasDegree
	 */
	public Integer getHasDegree() {
		return hasDegree;
	}
	
	public Boolean hasDegree() {
		if (hasDegree != null && hasDegree == 1) {
			return true;
		}else{
			return false;
		}
	}
	/**
	 * @param hasDegree the hasDegree to set
	 */
	public void setHasDegree(Integer hasDegree) {
		this.hasDegree = hasDegree;
	}
	/**
	 * @return the isSales
	 */
	public Integer getIsSales() {
		return isSales;
	}
	public Boolean isSales() {
		if (isSales != null && isSales == 1) {
			return true;
		}else{
			return false;
		}
	}
	/**
	 * @param isSales the isSales to set
	 */
	public void setIsSales(Integer isSales) {
		this.isSales = isSales;
	}
	/**
	 * @return the purchasePrice
	 */
	public String getPurchasePrice() {
		return purchasePrice;
	}
	/**
	 * @param purchasePrice the purchasePrice to set
	 */
	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	/**
	 * @return the petailPrice
	 */
	public String getPetailPrice() {
		return petailPrice;
	}
	/**
	 * @param petailPrice the petailPrice to set
	 */
	public void setPetailPrice(String petailPrice) {
		this.petailPrice = petailPrice;
	}
	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}
	/**
	 * @return the synShopId
	 */
	public String getSynShopId() {
		return synShopId;
	}
	/**
	 * @param synShopId the synShopId to set
	 */
	public void setSynShopId(String synShopId) {
		this.synShopId = synShopId;
	}
	/**
	 * @return the unitId
	 */
	public String getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(String unitId) {
		this.unitId = unitId;
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
	 * @return the file
	 */
	public byte[] getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(byte[] file) {
		this.file = file;
	}
	
	/**
	 * @return the fileName
	 */
	public String getFileNameSmall() {
		if (fileName == null || fileName.length() == 0) {
			return "";
		}else{
			return fileName+Constants.FILENAME_SUFFIX_SMALL;
		}
	}	/**
	 * @return the fileName
	 */
	public String getFileNameBig() {
		if (fileName == null || fileName.length() == 0) {
			return "";
		}else{
			return fileName+Constants.FILENAME_SUFFIX_BIG;
		}	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Bitmap getImage() {
		if (file == null) {
			return null;
		}
		return BitmapFactory.decodeByteArray(file, 0, file.length);
	}
	
	@Override
	public String toString() {
		return new GsonBuilder().serializeNulls().create().toJson(this);
	}
	public void setHasDegree(boolean checked) {
		if (checked) {
			setHasDegree(1);
		}else{
			setHasDegree(0);
		}
		
	}
	public void setIsSales(boolean checked) {
		if (checked) {
			setIsSales(1);
		}else{
			setIsSales(0);
		}
		
	}

}
