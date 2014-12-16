package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.UserVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
//查询门店下员工信息
public class UserQueryBo extends BaseRemoteBo{
	List<UserVo>	userList;//用户列表					
	Integer		pageSize;//页数
	
	public List<UserVo> getUserList() {
		return userList;
	}
	public void setUserList(List<UserVo> userList) {
		this.userList = userList;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	


}
