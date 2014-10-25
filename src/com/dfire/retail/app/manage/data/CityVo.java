package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.util.List;

import com.google.gson.GsonBuilder;

//城市信息
public class CityVo implements Serializable{
	//基本数据类型
	Integer cityId;
	String cityName;
	List<DistrictVo> districtVoList;
	
	
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public List<DistrictVo> getDistrictVoList() {
		return districtVoList;
	}
	public void setDistrictVoList(List<DistrictVo> districtVoList) {
		this.districtVoList = districtVoList;
	}
	
	@Override
	public String toString() {
		return new GsonBuilder().serializeNulls().create().toJson(this);
	}
}
