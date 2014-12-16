package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.NoticeVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 消息一览
 * @author ys
 *
 */
public class NoticeListBo extends BaseRemoteBo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3695741922186312953L;
	/**
	 * 消息列表信息
	 */
	private List<NoticeVo> noticeList;
	/**
	 * 总页数
	 */
	private Integer pageSize;
	/**
	 * @return the noticeList
	 */
	public List<NoticeVo> getNoticeList() {
		return noticeList;
	}
	/**
	 * @param noticeList the noticeList to set
	 */
	public void setNoticeList(List<NoticeVo> noticeList) {
		this.noticeList = noticeList;
	}
	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
