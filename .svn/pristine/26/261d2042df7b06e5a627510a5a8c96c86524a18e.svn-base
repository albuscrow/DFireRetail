package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.util.List;

import com.google.gson.GsonBuilder;

//省份信息
public class ProvinceVo implements Serializable {
	
	private static final String TAG = "ProvinceVo";
	
	//基本数据类型
	Integer provinceId;
	String provinceName;
	List<CityVo> cityVoList;
	
	
	
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public List<CityVo> getCityVoList() {
		return cityVoList;
	}
	public void setCityVoList(List<CityVo> cityVoList) {
		this.cityVoList = cityVoList;
	}
	

	
	@Override
	public String toString() {
		return new GsonBuilder().serializeNulls().create().toJson(this);
	}
	
}
