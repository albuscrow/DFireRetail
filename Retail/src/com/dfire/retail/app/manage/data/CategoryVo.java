package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

public class CategoryVo implements Serializable{
	
	String id;
	String entityId;
	String code;
	String sortCode;
	String name;
	String spell;
	String parentId;
	String memo;
	String lastVer;
	Integer goodsSum;
	/**
	 * @return the goodsSum
	 */
	public Integer getGoodsSum() {
		return goodsSum;
	}
	/**
	 * @param goodsSum the goodsSum to set
	 */
	public void setGoodsSum(Integer goodsSum) {
		this.goodsSum = goodsSum;
	}

	ArrayList<CategoryVo> categoryVoList;
	/**
	 * @return the categoryId
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param categoryId the categoryId to set
	 */
	public void setId(String categoryId) {
		this.id = categoryId;
	}
	/**
	 * @return the entityId
	 */
	public String getEntityId() {
		return entityId;
	}
	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the sortCode
	 */
	public String getSortCode() {
		return sortCode;
	}
	/**
	 * @param sortCode the sortCode to set
	 */
	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
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
	public String getLastVer() {
		return lastVer;
	}
	/**
	 * @param lastVer the lastVer to set
	 */
	public void setLastVer(String lastVer) {
		this.lastVer = lastVer;
	}
	/**
	 * @return the categoryList
	 */
	public ArrayList<CategoryVo> getCategoryList() {
		return categoryVoList;
	}
	/**
	 * @param categoryList the categoryList to set
	 */
	public void setCategoryList(ArrayList<CategoryVo> categoryList) {
		this.categoryVoList = categoryList;
	}
	public boolean hasParent() {
		if (parentId == null || parentId.length() == 0) {
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public String toString() {
		return new GsonBuilder().serializeNulls().create().toJson(this);
	}
}
