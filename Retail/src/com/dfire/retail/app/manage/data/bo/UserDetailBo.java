package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.UserVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

/*
 * 用户详细信息
 */
public class UserDetailBo extends BaseRemoteBo{
	UserVo	 user;

	public UserVo getUser() {
		return user;
	}

	public void setUser(UserVo user) {
		this.user = user;
	}					


}
