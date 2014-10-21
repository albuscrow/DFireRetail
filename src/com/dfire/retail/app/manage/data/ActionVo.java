package com.dfire.retail.app.manage.data;

import java.math.BigDecimal;

import android.R.integer;

/**
 * 用户权限
 * @author kyolee
 *
 */
public class ActionVo {

	private String actionId;//权限ID
	private String actionName;//权限名
	private integer actionType;//1：功能权限  2：数据权限 3：增值服务权限"		
	private String code;//代码
	private integer isMenu;//是否是菜单
	private String urlPath;//入口路径
	
	
	
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public integer getActionType() {
		return actionType;
	}
	public void setActionType(integer actionType) {
		this.actionType = actionType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public integer getIsMenu() {
		return isMenu;
	}
	public void setIsMenu(integer isMenu) {
		this.isMenu = isMenu;
	}
	public String getUrlPath() {
		return urlPath;
	}
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
	
	

	
}
