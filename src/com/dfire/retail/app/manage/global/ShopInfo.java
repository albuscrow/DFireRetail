/**
 * 
 */
package com.dfire.retail.app.manage.global;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 商店信息
 * 
 * @author qiuch
 * 
 */
public class ShopInfo {
	private String shopId;//商户ID
	private String shopName;//商户名
	private String shortName;//商户简称
	private Integer shopType;//店铺类型 1：单店模式，2：连锁模式
	private String address;//地址
	private Integer provinceId;//省份ID
	private Integer cityId;//城市ID
	private Integer countyId=1;//区县ID
	private Integer streetId;//街道ID
	private String phone1;//联系电话一
	private String phone2;//联系电话二
	private String parentId;//上级商户ID
	private static final boolean DEBUG = true;
	
	public ShopInfo(String res){
		JSONObject jobj;
		try {
			if(DEBUG)
				Log.i("ShopInfo","res = "+res);
			jobj = new JSONObject(res);			
			this.shopId = jobj.getString("shopId");
			this.shopName = jobj.getString("shopName");
			this.shortName = jobj.getString("shortName");
			this.address = jobj.getString("address");
			if(jobj.getString("provinceId")!=null && !jobj.getString("provinceId").equals("null"))
				this.provinceId = Integer.valueOf(jobj.getString("provinceId"));
			if(jobj.getString("cityId")!=null && !jobj.getString("cityId").equals("null"))
				this.cityId = Integer.valueOf(jobj.getString("cityId"));
			if(jobj.getString("countyId")!=null && !jobj.getString("countyId").equals("null"))
				this.countyId = Integer.valueOf(jobj.getString("countyId"));
			
			if(jobj.getString("streetId")!=null && !jobj.getString("streetId").equals("null"))
				this.streetId = Integer.valueOf(jobj.getString("streetId"));
			this.phone1 = jobj.getString("phone1");
			this.phone2 = jobj.getString("phone2");
			if(jobj.getString("parentId")!=null && !jobj.getString("parentId").equals("null"))
				this.parentId = jobj.getString("parentId");
			
			if(jobj.getString("shopType")!=null && !jobj.getString("shopType").equals("null")){
				this.shopType=Integer.valueOf(jobj.getString("shopType"));
			}
						
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getCountyId() {
		return countyId;
	}

	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	public Integer getStreetId() {
		return streetId;
	}

	public void setStreetId(Integer streetId) {
		this.streetId = streetId;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public Integer getShopType() {
		return shopType;
	}

	public void setShopType(Integer shopType) {
		this.shopType = shopType;
	}
	
}
