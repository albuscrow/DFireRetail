package com.dfire.retail.app.manage.data;

import java.io.Serializable;

public class NoticeVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2415847017954876506L;
	private String noticeId;
	private String currShopId;
	private String targetShopId;
	private String noticeTitle;
	private String noticeContent;
	private String targetShopName;
	public String getTargetShopName() {
		return targetShopName;
	}
	public void setTargetShopName(String targetShopName) {
		this.targetShopName = targetShopName;
	}
	private String publishTime;
	private Integer lastVer;
	private String status;
	
	public NoticeVo() {
		super();
	}
	/**
	 * 
	 * @param noticeId
	 * @param acceptShopId
	 * @param acceptShopName
	 * @param noticeTitle
	 * @param noticeContent
	 * @param publishTime
	 * @param lastVer
	 * @param status
	 */
	public NoticeVo(String noticeId, String targetShopId,
			String acceptShopName, String noticeTitle, String noticeContent,
			String publishTime, Integer lastVer, String status) {
		super();
		this.noticeId = noticeId;
		this.targetShopId = targetShopId;
		this.noticeTitle = noticeTitle;
		this.noticeContent = noticeContent;
		this.publishTime = publishTime;
		this.lastVer = lastVer;
		this.status = status;
	}
	public String getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	public String gettargetShopId() {
		return targetShopId;
	}
	public void settargetShopId(String targetShopId) {
		this.targetShopId = targetShopId;
	}
	public String getAcceptShopName() {
		return targetShopId;
	}
	public void setAcceptShopName(String acceptShopName) {
		this.targetShopId = acceptShopName;
	}
	public String getNoticeTitle() {
		return noticeTitle;
	}
	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	public String getNoticeContent() {
		return noticeContent;
	}
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	 
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public Integer getLastVer() {
		return lastVer;
	}
	public void setLastVer(Integer lastVer) {
		this.lastVer = lastVer;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "NoticeVo [noticeId=" + noticeId + ", currShopId=" + currShopId
				+ ", targetShopId=" + targetShopId + ", noticeTitle="
				+ noticeTitle + ", noticeContent=" + noticeContent
				+ ", publishTime=" + publishTime + ", lastVer=" + lastVer
				+ ", status=" + status + "]";
	}
	
	

}
