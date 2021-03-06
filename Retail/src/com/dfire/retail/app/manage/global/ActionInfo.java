/**
 * 
 */
package com.dfire.retail.app.manage.global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 权限信息
 * 
 * @author qiuch
 * 
 */
public class ActionInfo {
	private String actionId;
	private String actionName;
	private Integer actionType;
	private String code;
	private Integer isMenu;
	private String urlPath;
	
	public ActionInfo(String res){
		JSONObject jobj;
		
		try {
			jobj = new JSONObject(res);
			this.actionId = jobj.getString("actionId");
			this.actionName = jobj.getString("actionName");
			this.actionType = Integer.valueOf(jobj.getString("actionType"));;
			
			this.code = jobj.getString("code");
			this.isMenu = Integer.valueOf(jobj.getString("isMenu"));
			this.urlPath = jobj.getString("urlPath");
					
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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

	public Integer getActionType() {
		return actionType;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getIsMenu() {
		return isMenu;
	}

	public void setIsMenu(Integer isMenu) {
		this.isMenu = isMenu;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	

}
