package com.dfire.retail.app.manage.data;
/**
 * 库存提醒设置
 * @author ys
 *
 */
public class StockInfoAlertVo {
	private String goodsId;//				String							商品Id			
	private Short isAlertNum;//				Short							是否打开数量预警（0 关闭）（1 打开）			
	private Integer alertNum;//				Integer							最低预警数			
	private Short isAlertDay;//				Short							是否打开时间预警（0 关闭）（1 打开）			
	private Integer alertDay;//				Integer							最低提醒日期天数			
	private Long lastVer;//				Long							版本号			
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
	 * @return the isAlertNum
	 */
	public Short getIsAlertNum() {
		return isAlertNum;
	}
	/**
	 * @param isAlertNum the isAlertNum to set
	 */
	public void setIsAlertNum(Short isAlertNum) {
		this.isAlertNum = isAlertNum;
	}
	/**
	 * @return the alertNum
	 */
	public Integer getAlertNum() {
		return alertNum;
	}
	/**
	 * @param alertNum the alertNum to set
	 */
	public void setAlertNum(Integer alertNum) {
		this.alertNum = alertNum;
	}
	/**
	 * @return the isAlertDay
	 */
	public Short getIsAlertDay() {
		return isAlertDay;
	}
	/**
	 * @param isAlertDay the isAlertDay to set
	 */
	public void setIsAlertDay(Short isAlertDay) {
		this.isAlertDay = isAlertDay;
	}
	/**
	 * @return the alertDay
	 */
	public Integer getAlertDay() {
		return alertDay;
	}
	/**
	 * @param alertDay the alertDay to set
	 */
	public void setAlertDay(Integer alertDay) {
		this.alertDay = alertDay;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StockInfoAlertVo [goodsId=" + goodsId + ", isAlertNum="
				+ isAlertNum + ", alertNum=" + alertNum + ", isAlertDay="
				+ isAlertDay + ", alertDay=" + alertDay + ", lastVer="
				+ lastVer + "]";
	}
}
