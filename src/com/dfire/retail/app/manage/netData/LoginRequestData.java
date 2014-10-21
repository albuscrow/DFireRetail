/**
 * 
 */
package com.dfire.retail.app.manage.netData;

/**
 * 登录提交参数
 * @author qiuch
 * 
 */
public class LoginRequestData {

	private String shopCode;
	private String username;
	private String password;

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
