package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.NoticeVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

public class NoticeDetailBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9145179419516712580L;
	/**
	 * 消息信息
	 */
	private NoticeVo notice;
	/**
	 * 是否可修改删除FLG
	 */
	private Integer editDelFlg;
	/**
	 * @return the notice
	 */
	public NoticeVo getNotice() {
		return notice;
	}
	/**
	 * @param notice the notice to set
	 */
	public void setNotice(NoticeVo notice) {
		this.notice = notice;
	}
	public Integer getEditDelFlg() {
		return editDelFlg;
	}
	public void setEditDelFlg(Integer editDelFlg) {
		this.editDelFlg = editDelFlg;
	}
}
