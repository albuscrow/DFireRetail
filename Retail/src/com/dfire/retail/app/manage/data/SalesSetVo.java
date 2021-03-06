package com.dfire.retail.app.manage.data;

import java.util.List;

/**
 * The Class SalesSetVo.
 * 
 * @author albuscrow
 */
public class SalesSetVo {
	
	/** The percent age. 提成*/
	Float percentAge;
	
	/** The has degree. jifen*/
	Short hasDegree;
	
	/** The Is sales. youhui */
	Short IsSales;
	
	/** The special flag. tijia */
	String specialFlag;
	
	/** The discount rate. zhekou*/
	Float discountRate;
	
	/** The start time. kaishi*/
	Long startTime;
	
	/** The end time. jieshu*/
	Long endTime;
	
	/** The is member. huiyuan*/
	Short isMember;
	
	/** The sales set goods list. */
	List<SalesSetGoodsVo> salesSetGoodsList;
	
	/**
	 * Gets the percent age.
	 * 
	 * @return the percentAge
	 */
	public Float getPercentAge() {
		return percentAge;
	}
	
	/**
	 * Sets the percent age.
	 * 
	 * @param percentAge
	 *            the percentAge to set
	 */
	public void setPercentAge(Float percentAge) {
		this.percentAge = percentAge;
	}
	
	/**
	 * Gets the checks for degree.
	 * 
	 * @return the hasDegree
	 */
	public Short getHasDegree() {
		return hasDegree;
	}
	
	/**
	 * Sets the checks for degree.
	 * 
	 * @param hasDegree
	 *            the hasDegree to set
	 */
	public void setHasDegree(Short hasDegree) {
		this.hasDegree = hasDegree;
	}
	
	/**
	 * Gets the checks if is sales.
	 * 
	 * @return the isSales
	 */
	public Short getIsSales() {
		return IsSales;
	}
	
	/**
	 * Sets the checks if is sales.
	 * 
	 * @param isSales
	 *            the isSales to set
	 */
	public void setIsSales(Short isSales) {
		IsSales = isSales;
	}
	
	/**
	 * Gets the special flag.
	 * 
	 * @return the specialFlag
	 */
	public String getSpecialFlag() {
		return specialFlag;
	}
	
	/**
	 * Sets the special flag.
	 * 
	 * @param specialFlag
	 *            the specialFlag to set
	 */
	public void setSpecialFlag(String specialFlag) {
		this.specialFlag = specialFlag;
	}
	
	/**
	 * Gets the discount rate.
	 * 
	 * @return the discountRate
	 */
	public Float getDiscountRate() {
		return discountRate;
	}
	
	/**
	 * Sets the discount rate.
	 * 
	 * @param discountRate
	 *            the discountRate to set
	 */
	public void setDiscountRate(Float discountRate) {
		this.discountRate = discountRate;
	}
	
	/**
	 * @return the startTime
	 */
	public Long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Long getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the checks if is member.
	 * 
	 * @return the isMember
	 */
	public Short getIsMember() {
		return isMember;
	}
	
	/**
	 * Sets the checks if is member.
	 * 
	 * @param isMember
	 *            the isMember to set
	 */
	public void setIsMember(Short isMember) {
		this.isMember = isMember;
	}
	
	/**
	 * Gets the sales set goods list.
	 * 
	 * @return the salesSetGoodsList
	 */
	public List<SalesSetGoodsVo> getSalesSetGoodsList() {
		return salesSetGoodsList;
	}
	
	/**
	 * Sets the sales set goods list.
	 * 
	 * @param salesSetGoodsList
	 *            the salesSetGoodsList to set
	 */
	public void setSalesSetGoodsList(List<SalesSetGoodsVo> salesSetGoodsList) {
		this.salesSetGoodsList = salesSetGoodsList;
	}
}
