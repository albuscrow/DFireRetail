package com.dfire.retail.app.manage.data;

import java.io.Serializable;

import com.google.gson.GsonBuilder;

//区信息
public class DistrictVo  implements Serializable{
	
	Integer	districtId;				
	String districtName;
	
	public Integer getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}				

	@Override
	public String toString() {
		return new GsonBuilder().serializeNulls().create().toJson(this);
	}
}
