/**
 * 
 */
package com.dfire.retail.app.manage.netData;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dfire.retail.app.manage.global.ActionInfo;
import com.dfire.retail.app.manage.global.ShopInfo;
import com.dfire.retail.app.manage.global.UserInfo;

/**
 * 登录返回
 * 
 * @author qiuch
 * 
 */
public class LoginResult {
	private static final String TAG="LoginResult";
	private UserInfo user;
	private ShopInfo shop;
	private String jsessionId;
	private String postAttachmentUrl;
	private List<ActionInfo> userActions;
	
	public LoginResult(String res){
		
		userActions = new ArrayList<ActionInfo>();
		JSONObject jobj;
		try {
			jobj = new JSONObject(res);
			this.jsessionId = jobj.getString("jsessionId");
			//this.user = new UserInfo(jobj.getString("user"));
			this.shop = new ShopInfo(jobj.getString("shop"));
			
			JSONArray actionInfoJson=jobj.getJSONArray("userActionList");
			for(int i=0;i<actionInfoJson.length();i++){
				userActions.add(new ActionInfo(jobj.getString("userActionList")));
			}
			this.postAttachmentUrl = jobj.getString("postAttachmentUrl");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public ShopInfo getShop() {
		return shop;
	}

	public void setShop(ShopInfo shop) {
		this.shop = shop;
	}

	public String getJsessionId() {
		return jsessionId;
	}

	public void setJsessionId(String jsessionId) {
		this.jsessionId = jsessionId;
	}

	public String getPostAttachmentUrl() {
		return postAttachmentUrl;
	}

	public void setPostAttachmentUrl(String postAttachmentUrl) {
		this.postAttachmentUrl = postAttachmentUrl;
	}

	public List<ActionInfo> getUserActions() {
		return userActions;
	}

	public void setUserActions(List<ActionInfo> userActions) {
		this.userActions = userActions;
	}

}
