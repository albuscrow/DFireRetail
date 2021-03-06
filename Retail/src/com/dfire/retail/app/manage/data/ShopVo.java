package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.global.Constants;

/**
 * 店铺信息
 * 
 * @author kyolee
 * 
 */
public class ShopVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String TAG = "ShopVo";

    // 要求的基本数据
    private String shopId;// 商户ID
    private String shopName;// 商户名称
    private String shortName;// 商户简称
    private String address;// 住址
    private Integer provinceId;// 省份ID
    private String entityId;
    private String spell;
	private Integer cityId;// 城市ID
    private Integer countyId;// 区县ID
    private Integer streetId;// 街道ID
    private String phone1;// 联系电话一
    private String phone2;// 联系电话二
    private String weixin;//微信
	private String parentId;// 上级商户ID
    private String code;//商户编号
	// 最原始code, 未知是否有用数据
    private String businessTime;
    private String lastVer;
    private String copyFlag;
    private String startTime;
    private String endTime;
    private String shopType;
    private List<AllShopVo> shopList;
    private String dataFromShopId;
    private String fileName;
    private byte[] file;
    private Short fileOperate;
    
    public String getWeixin() {
		return weixin;
	}


	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

    
    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


    public ShopVo() {
    }

    public ShopVo(String id, String name, String shortName, String address,
    		Integer provinceId, Integer cityId, Integer countyId, Integer streetId,
            String phone1, String phone2, String parentId) {
        shopId = id;
        shopName = name;
        this.shortName = shortName;
        this.address = address;
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.countyId = countyId;
        this.streetId = streetId;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.parentId = parentId;
    }
    public String getEntityId() {
		return entityId;
	}


	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
    public ShopVo(JSONObject jobj) throws JSONException {
        if (jobj != null) {
            if (Constants.DEBUG) {
                Log.i(TAG, jobj.toString());
            }
            
            shopId = jobj.getString("shopId");
            shopName = jobj.getString("shopName");
            shortName = jobj.getString("shortName");
            address = jobj.getString("address");
            provinceId = Integer.valueOf(jobj.getString("provinceId"));
            cityId = Integer.valueOf(jobj.getString("cityId"));
            countyId = Integer.valueOf(jobj.getString("countyId"));
            streetId = Integer.valueOf(jobj.getString("streetId"));
            phone1 = jobj.getString("phone1");
            phone2 = jobj.getString("phone2");
            parentId = jobj.getString("parentId");
            
            initData(jobj);
        }
    }

    /**
     * 未知是否有用的标志, 在定义的数据结构中未定义，但是在JSON数据中存在
     * @param jobj
     * @throws JSONException
     */
    public void initData(JSONObject jobj) throws JSONException {
        if (!jobj.isNull("lastVer")) {
            lastVer = jobj.getString("lastVer");
        }
        if (!jobj.isNull("copyFlag")) {
            copyFlag = jobj.getString("copyFlag");
        }
        if (!jobj.isNull("startTime")) {
            startTime = jobj.getString("startTime");
        }
        if (!jobj.isNull("endTime")) {
            endTime = jobj.getString("endTime");
        }
        if (!jobj.isNull("shopType")) {
            shopType = jobj.getString("shopType");
        }
        if (!jobj.isNull("shopList")) {
            if (shopList == null) {
                shopList = new ArrayList<AllShopVo>();
            } else {
                shopList.clear();
            }
            JSONArray obj = jobj.getJSONArray("shopList");
            for (int i = 0; i < obj.length(); i++) {
                shopList.add(new AllShopVo(obj.getJSONObject(i)));
            }
        }
        if (!jobj.isNull("dataFromShopId")) {
            dataFromShopId = jobj.getString("dataFromShopId");
        }
        if (!jobj.isNull("file")) {
            file = jobj.getString("file").getBytes();
        }
        if (!jobj.isNull("fileName")) {
            fileName = jobj.getString("fileName");
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

    /**
     * @return the provinceId
     */
    public Integer getProvinceId() {
        return provinceId;
    }

    public String getSpell() {
		return spell;
	}


	public void setSpell(String spell) {
		this.spell = spell;
	}


	/**
     * @param provinceId
     *            the provinceId to set
     */
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    /**
     * @return the cityId
     */
    public Integer getCityId() {
        return cityId;
    }

    /**
     * @param cityId
     *            the cityId to set
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * @return the countyId
     */
    public Integer getCountyId() {
        return countyId;
    }

    /**
     * @param countyId
     *            the countyId to set
     */
    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    /**
     * @return the streetId
     */
    public Integer getStreetId() {
        return streetId;
    }

    /**
     * @param streetId
     *            the streetId to set
     */
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
   
    /**
     * @return the businessTime
     */
    public String getBusinessTime() {
        return businessTime;
    }

    /**
     * @param businessTime
     *            the businessTime to set
     */
    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }
    
    /**
     * @return the lastVer
     */
    public String getLastVer() {
        return lastVer;
    }

    /**
     * @param lastVer
     *            the lastVer to set
     */
    public void setLastVer(String lastVer) {
        this.lastVer = lastVer;
    }

    /**
     * @return the copyFlag
     */
    public String getCopyFlag() {
        return copyFlag;
    }

    /**
     * @param copyFlag
     *            the copyFlag to set
     */
    public void setCopyFlag(String copyFlag) {
        this.copyFlag = copyFlag;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the shopType
     */
    public String getShopType() {
        return shopType;
    }

    /**
     * @param shopType
     *            the shopType to set
     */
    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    /**
     * @return the shopList
     */
    public List<AllShopVo> getShopList() {
        return shopList;
    }

    /**
     * @param shopList
     *            the shopList to set
     */
    public void setShopList(List<AllShopVo> shopList) {
        this.shopList = shopList;
    }

    /**
     * @return the dataFromShopId
     */
    public String getDataFromShopId() {
        return dataFromShopId;
    }

    /**
     * @param dataFromShopId
     *            the dataFromShopId to set
     */
    public void setDataFromShopId(String dataFromShopId) {
        this.dataFromShopId = dataFromShopId;
    }

    /**
     * @return the file
     */
    public byte[] getFile() {
        return file;
    }

    /**
     * @param file
     *            the file to set
     */
    public void setFile(byte[] file) {
        this.file = file;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public static final int ZHONGBU = 0;
    public static final int FENGBU = 1;
    public static final int MENDIAN = 2;
    
    public static final int DANDIAN = 3;
    public Integer getType(){
    	if (RetailApplication.getEntityModel() == RetailApplication.DANDIAN) {
			return DANDIAN;
		}else{
			if (parentId == null || parentId.length() == 0) {
				return ZHONGBU;
			}else if (shopType != null && shopType.equals("2")) {
				return MENDIAN;
			}else{
				return FENGBU;
			}
		}
    }
    
    public boolean isLeaf(){
    	return getType() == ShopVo.DANDIAN || getType() == ShopVo.MENDIAN;
    }
    
    public boolean isZhongdian(){
    	return getType() == ZHONGBU;
    }

	public boolean canEdit() {
		return getType() == DANDIAN || getType() == ZHONGBU;
	}
	
//	public boolean canEdit() {
//		return getType() == ZHONGBU;
//	}
	//图片操作
	public Short getFileOperate() {
		return fileOperate;
	}


	public void setFileOperate(Short fileOperate) {
		this.fileOperate = fileOperate;
	}


	public AllShopVo toAllShopVo() {
		AllShopVo allShopVo = new AllShopVo();
		allShopVo.setCode(code);
		allShopVo.setParentId(parentId);
		allShopVo.setShopId(shopId);
		allShopVo.setShopName(shopName);
		return allShopVo;
	}
	
}
