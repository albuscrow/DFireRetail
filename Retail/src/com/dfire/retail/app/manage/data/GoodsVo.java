package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dfire.retail.app.manage.global.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GoodsVo implements Serializable {


	String  barCode		  ;
	String  baseId		  ;
	private Bitmap bp;
	String  brandId		  ;
	String  brandName	  ;
	String  categoryId	  ;
	String  categoryName  ;
	byte[]  file		  ;
	boolean fileDeleteFlg;
	String  fileName	  ;
	String  goodsId		  ;
	String  goodsName	  ;
	Integer hasDegree	  ;
	String  innerCode	  ;
	Integer isSales		  ;
	Long    lastVer		  ;
	String  memo		  ;
	Integer number		  ;
	String  origin		  ;
	String  percentage	  ;
	Integer period		  ;
	String  petailPrice	  ;
	String  purchasePrice ;
	String  shortCode	  ;
	String  specification ;
	String  spell		  ;
	String  synShopId	  ;
	Integer  type		  ;
	String  unitId		  ;
	public void addLastVer() {
		if (lastVer == null) {
			this.lastVer = 1l;
		}else{
			++ this.lastVer;
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		GoodsVo result = new GoodsVo();
		
		result.barCode = this.barCode;
		result.baseId = this.baseId;
		result.brandId = this.brandId;
		result.brandName = this.brandName;
		result.categoryId = this.categoryId;
		result.categoryName = this.categoryName;
		result.file = this.file;
		result.fileName = this.fileName;
		result.goodsId = this.goodsId;
		result.goodsName = this.goodsName;
		result.hasDegree = this.hasDegree;
		result.innerCode = this.innerCode;
		result.isSales = this.isSales;
		result.lastVer = this.lastVer;
		result.memo = this.memo;
		result.number = this.number;
		result.origin = this.origin;
		result.percentage = this.percentage;
		result.period = this.period;
		result.petailPrice = this.petailPrice;
		result.purchasePrice = this.purchasePrice;
		result.shortCode = this.shortCode;
		result.specification = this.specification;
		result.spell = this.spell;
		result.synShopId = this.synShopId;
		result.type = this.type;
		result.unitId = this.unitId;
		
		return result;
	}
	public GoodsVo cloneForAdd() {
		GoodsVo goods = new GoodsVo();
		goods.setBarCode(this.barCode);
		goods.setGoodsName(this.goodsName);
		goods.setInnerCode(this.innerCode);
		return goods;
	}
	/**
	 * @return the barCode
	 */
	public String getBarCode() {
		return barCode;
	}
	/**
	 * @return the baseId
	 */
	public String getBaseId() {
		return baseId;
	}
	
	/**
	 * @return the bp
	 */
	public Bitmap getBp() {
		return bp;
	}
	/**
	 * @return the brandId
	 */
	public String getBrandId() {
		return brandId;
	}
	/**
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}
	/**
	 * @return the categoryId
	 */
	public String getCategoryId() {
		return categoryId;
	}
	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * @return the file
	 */
	public byte[] getFile() {
		return file;
	}
	public String getFileName() {
		return fileName;
	}
	/**
	 * @return the fileName
	 */
	public String getFileNameBig() {
		if (fileName == null || fileName.length() == 0) {
			return "";
		}else{
			return fileName+Constants.FILENAME_SUFFIX_BIG;
		}	}
	/**
	 * @return the fileName
	 */
	public String getFileNameSmall() {
		if (fileName == null || fileName.length() == 0) {
			return "";
		}else{
			return fileName+Constants.FILENAME_SUFFIX_SMALL;
		}
	}
	/**
	 * @return the goodsId
	 */
	public String getGoodsId() {
		return goodsId;
	}
	/**
	 * @return the goodsName
	 */
	public String getGoodsName() {
		
		return goodsName;
	}
	/**
	 * @return the hasDegree
	 */
	public Integer getHasDegree() {
		return hasDegree;
	}
	public Bitmap getImage() {
		if (file == null) {
			return null;
		}
		return BitmapFactory.decodeByteArray(file, 0, file.length);
	}
	/**
	 * @return the innerCode
	 */
	public String getInnerCode() {
		return innerCode;
	}
	/**
	 * @return the isSales
	 */
	public Integer getIsSales() {
		return isSales;
	}
	/**
	 * @return the lastVer
	 */
	public Long getLastVer() {
		return lastVer;
	}
	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @return the number
	 */
	public Integer getNumber() {
		if (number == null) {
			number = 0;
		}
		return number;
	}
	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}
	/**
	 * @return the percentage
	 */
	public String getPercentage() {
		return percentage;
	}
	/**
	 * @return the period
	 */
	public Integer getPeriod() {
		return period;
	}
	/**
	 * @return the petailPrice
	 */
	public String getPetailPrice() {
		return petailPrice;
	}
	/**
	 * @return the purchasePrice
	 */
	public String getPurchasePrice() {
		return purchasePrice;
	}
	/**
	 * @return the shortCode
	 */
	public String getShortCode() {
		return shortCode;
	}
	/**
	 * @return the specification
	 */
	public String getSpecification() {
		return specification;
	}
	/**
	 * @return the spell
	 */
	public String getSpell() {
		return spell;
	}
	/**
	 * @return the synShopId
	 */
	public String getSynShopId() {
		return synShopId;
	}
	/**
	 * @return the type
	 */
	public Integer getType() {
		if (type == null) {
			return 1;
		}
		return type;
	}
	/**
	 * @return the unitId
	 */
	public String getUnitId() {
		return unitId;
	}
	
	public Boolean hasDegree() {
		if (hasDegree != null && hasDegree == 1) {
			return true;
		}else{
			return false;
		}
	}
	/**
	 * @return the fileDeleteFlg
	 */
	public boolean isFileDeleteFlg() {
		return fileDeleteFlg;
	}
	public Boolean isSales() {
		if (isSales != null && isSales == 1) {
			return true;
		}else{
			return false;
		}
	}
	/**
	 * @param barCode the barCode to set
	 */
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	/**
	 * @param baseId the baseId to set
	 */
	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	/**
	 * @param bp the bp to set
	 */
	public void setBp(Bitmap bp) {
		this.bp = bp;
	}
	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(byte[] file) {
		this.file = file;
	}
	/**
	 * @param fileDeleteFlg the fileDeleteFlg to set
	 */
	public void setFileDeleteFlg(boolean fileDeleteFlg) {
		this.fileDeleteFlg = fileDeleteFlg;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @param goodsId the goodsId to set
	 */
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	/**
	 * @param goodsName the goodsName to set
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public void setHasDegree(boolean checked) {
		if (checked) {
			setHasDegree(1);
		}else{
			setHasDegree(0);
		}
		
	}
	/**
	 * @param hasDegree the hasDegree to set
	 */
	public void setHasDegree(Integer hasDegree) {
		this.hasDegree = hasDegree;
	}
	/**
	 * @param innerCode the innerCode to set
	 */
	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}
	public void setIsSales(boolean checked) {
		if (checked) {
			setIsSales(1);
		}else{
			setIsSales(0);
		}
		
	}
	/**
	 * @param isSales the isSales to set
	 */
	public void setIsSales(Integer isSales) {
		this.isSales = isSales;
	}
	/**
	 * @param lastVer the lastVer to set
	 */
	public void setLastVer(Long lastVer) {
		this.lastVer = lastVer;
	}
	
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}
	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(String percentage) {
		percentage = fixFloat(percentage);
		this.percentage = percentage;
	}
	
	/**
	 * @param period the period to set
	 */
	public void setPeriod(Integer period) {
		this.period = period;
	}
	
	/**
	 * @param petailPrice the petailPrice to set
	 */
	public void setPetailPrice(String petailPrice){
		petailPrice = fixFloat(petailPrice);
		this.petailPrice = petailPrice;
	}

	private String fixFloat(String petailPrice) {
		int position = petailPrice.indexOf(".");
		if (position == -1) {
			petailPrice = petailPrice + ".00";
		}else{
			int i = petailPrice.length() - position;
			if (i == 1) {
				petailPrice = petailPrice + "00";
			}else if (i == 2) {
				petailPrice = petailPrice + "0";
			}
		}
		return petailPrice;
	}

	/**
	 *
	 * @param purchasePrice the purchasePrice to set
	 */
	public void setPurchasePrice(String purchasePrice) {
		if (purchasePrice != null) {
			purchasePrice = fixFloat(purchasePrice);
		}
		this.purchasePrice = purchasePrice;
	}
	
	/**
	 * @param shortCode the shortCode to set
	 */
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	/**
	 * @param specification the specification to set
	 */
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	/**
	 * @param spell the spell to set
	 */
	public void setSpell(String spell) {
		this.spell = spell;
	}
	/**
	 * @param synShopId the synShopId to set
	 */
	public void setSynShopId(String synShopId) {
		this.synShopId = synShopId;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	@Override
	public String toString() {
		return new GsonBuilder().serializeNulls().create().toJson(this);
	} 

}
