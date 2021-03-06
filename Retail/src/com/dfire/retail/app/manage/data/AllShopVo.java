package com.dfire.retail.app.manage.data;

import java.io.Serializable;

import org.json.JSONObject;

import com.google.gson.GsonBuilder;

/**
 * AllShopVo（全体商户信息）
 * @author ys
 *
 */
public class AllShopVo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String shopId;//商户ID
	
	private String shopName;//商户名
	
	private String parentId;//上级商户ID
	
	private String code;//	商户代码
	
	public AllShopVo() {
		// TODO Auto-generated constructor stub
	}
	public AllShopVo(JSONObject jsonObject) {
		// TODO Auto-generated constructor stub
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return new GsonBuilder().serializeNulls().create().toJson(this);
	}
}
