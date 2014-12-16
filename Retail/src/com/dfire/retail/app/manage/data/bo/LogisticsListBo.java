package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.LogisticsVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 物流列表
 * @author ys
 *
 */
public class LogisticsListBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5200676968807896687L;
	/**
	 * 物流记录列表
	 */
	private List<LogisticsVo> logisticsList;
	/**
	 * 总页数
	 */
	private Integer pageSize;
	/**
	 * @return the logisticsList
	 */
	public List<LogisticsVo> getLogisticsList() {
		return logisticsList;
	}
	/**
	 * @param logisticsList the logisticsList to set
	 */
	public void setLogisticsList(List<LogisticsVo> logisticsList) {
		this.logisticsList = logisticsList;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LogisticsListBo [logisticsList=" + logisticsList
				+ ", pageSize=" + pageSize + "]";
	}
}
