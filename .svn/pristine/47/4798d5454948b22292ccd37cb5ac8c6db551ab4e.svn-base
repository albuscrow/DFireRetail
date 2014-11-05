package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.google.gson.JsonObject;

public class AddShopVo  implements Serializable{
	JsonObject mAddShopVo;
	private String shopId;// 商户ID
	private String entityId;
	private String code;//商户编号
	private String shopName;
	private String spell;
	private String shopType;
	private String  dataFromShopId;
	private Integer provinceId;// 省份ID
	private Integer cityId;// 城市ID
	private Integer countyId;// 区县ID
	private Integer streetId;// 街道ID
	private String address;// 住址
	private String phone1;// 联系电话一
    private String phone2;// 联系电话二
    private String weixin;//微信
    private String startTime;
    private String endTime;
    private String logoId;//
    private String memo;
    private String parentId;
    private String lastVer;
    private String copyFlag;
    private String fileName;
    private byte[] file;
    
    public AddShopVo(String shopId,String entityId,String code,String shopName,
	String spell,String shopType,String  dataFromShopId,Integer provinceId,Integer cityId,Integer countyId,// 区县ID
	Integer streetId,String address,String phone1,String phone2,
    String weixin,String startTime,String endTime,String logoId,String memo,
    String lastVer, String parentId,String fileName,byte[] file){
    	this.shopId = shopId;
    	this.shopName = shopName;
    	this.code = code;
    	this.spell = spell;
    	this.shopType = shopType;
    	this.dataFromShopId = dataFromShopId;
    	this.parentId = parentId;
    	this.provinceId = provinceId;
    	this.cityId = cityId;
    	this.countyId = countyId;
    	this.streetId = streetId;
    	this.address = address;
    	this.phone1 = phone1;
    	this.phone2 = phone2;
    	this.weixin = weixin;
    	this.startTime = startTime;
    	this.endTime = endTime; 
    	this.logoId = logoId;
    	this.memo = memo;
    	this.lastVer = lastVer;
    	this.copyFlag = copyFlag; 
    	this.fileName = fileName;
    	this.file = file;
    	
    	
    	
    }
    
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getSpell() {
		return spell;
	}
	public void setSpell(String spell) {
		this.spell = spell;
	}
	public String getShopType() {
		return shopType;
	}
	public void setShopType(String shopType) {
		this.shopType = shopType;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getLogoId() {
		return logoId;
	}
	public void setLogoId(String logoId) {
		this.logoId = logoId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getLastVer() {
		return lastVer;
	}
	public void setLastVer(String lastVer) {
		this.lastVer = lastVer;
	}
	public String getCopyFlag() {
		return copyFlag;
	}
	public void setCopyFlag(String copyFlag) {
		this.copyFlag = copyFlag;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
    
    public String getAddShop(){
    	HashMap map = new HashMap(); 
    	String string = "{";  
         map.put( "shopId",shopId  );  
         map.put( "entityId", entityId );  
         map.put( "code", code);  
         map.put( "shopName", shopName );           
         map.put( "spell",spell  );  
         map.put( "shopType", shopType );  
         map.put( "dataFromShopId", dataFromShopId);        
         map.put( "provinceId", provinceId );  
         map.put( "cityId",cityId  );  
         map.put( "countyId", countyId );  
         map.put( "streetId", streetId);  
         map.put( "address", address );  
         map.put( "phone1",phone1  );  
         map.put( "phone2", phone2 );  
         map.put( "weixin", weixin);          
         map.put( "startTime", startTime );  
         map.put( "endTime",endTime  );  
         map.put( "logoId", logoId );  
         map.put( "memo", memo);  
         map.put( "parentId", parentId );  
         map.put( "lastVer",lastVer  );  
         map.put( "copyFlag", copyFlag );  
         map.put( "fileName", fileName);  
         map.put( "file", file );  
   
    	  
    	
    	return hashMapToJson(map);   
    }


 
        /**把数据源HashMap转换成json 
         * @param map  
         */  
        public String hashMapToJson(HashMap map) {  
            String string = "{";  
            for (Iterator it = map.entrySet().iterator(); it.hasNext();) {  
                Entry e = (Entry) it.next();  
               string += "\"";
               string += e.getKey().toString();
               string += "\"";
               string += ":";
               string += "\"";
               string += e.getValue().toString();
               string += "\",";
//                string += 
            }  
            string = string.substring(0, string.lastIndexOf(","));  
            string += "}";  
            return string;  
        }  
}
