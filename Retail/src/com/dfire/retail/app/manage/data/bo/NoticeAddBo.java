package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

/**
 * 添加公告返回
 * @author ys
 *
 */
public class NoticeAddBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5584626552881657287L;
	/**
	 * 消息id
	 */
	private String noticeId;
	/**
	 * @return the noticeId
	 */
	public String getNoticeId() {
		return noticeId;
	}
	/**
	 * @param noticeId the noticeId to set
	 */
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
}
