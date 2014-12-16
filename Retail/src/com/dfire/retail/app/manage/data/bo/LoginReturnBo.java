package com.dfire.retail.app.manage.data.bo;

import java.util.Map;

import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
import com.dfire.retail.app.manage.global.UserInfo;
/**
 * 登陆返回
 * @author ys
 *
 */
public class LoginReturnBo extends BaseRemoteBo{

	private static final long serialVersionUID = -3647274399516197171L;
	private UserInfo user;
	private ShopVo shop;
	private String jsessionId;
	private Map<String,Integer> userActionMap;
	private Integer entityModel;
	/**
	 * @return the user
	 */
	public UserInfo getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(UserInfo user) {
		this.user = user;
	}
	/**
	 * @return the shop
	 */
	public ShopVo getShop() {
		return shop;
	}
	/**
	 * @param shop the shop to set
	 */
	public void setShop(ShopVo shop) {
		this.shop = shop;
	}
	/**
	 * @return the jsessionId
	 */
	public String getJsessionId() {
		return jsessionId;
	}
	/**
	 * @param jsessionId the jsessionId to set
	 */
	public void setJsessionId(String jsessionId) {
		this.jsessionId = jsessionId;
	}
	/**
	 * @return the userActionMap
	 */
	public Map<String, Integer> getUserActionMap() {
		return userActionMap;
	}
	/**
	 * @param userActionMap the userActionMap to set
	 */
	public void setUserActionMap(Map<String, Integer> userActionMap) {
		this.userActionMap = userActionMap;
	}
	/**
	 * @return the entityModel
	 */
	public Integer getEntityModel() {
		return entityModel;
	}
	/**
	 * @param entityModel the entityModel to set
	 */
	public void setEntityModel(Integer entityModel) {
		this.entityModel = entityModel;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LoginReturnBo [user=" + user + ", shop=" + shop
				+ ", jsessionId=" + jsessionId + ", userActionMap="
				+ userActionMap + ", entityModel=" + entityModel + "]";
	}
}
