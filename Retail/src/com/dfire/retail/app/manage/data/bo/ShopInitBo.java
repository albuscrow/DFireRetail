package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ProvinceVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
//商户初始化返回结果信息
public class ShopInitBo extends BaseRemoteBo{
	
	ShopVo shop;	//商户信息					
	List<DicVo>	shopTypeList;//商户类型列表					
	List<ProvinceVo> addressList;//地址信息列表						
	List<AllShopVo>	 shopList;//子店信息列表
	public ShopVo getShop() {
		return shop;
	}
	public void setShop(ShopVo shop) {
		this.shop = shop;
	}
	public List<DicVo> getShopTypeList() {
		return shopTypeList;
	}
	public void setShopTypeList(List<DicVo> shopTypeList) {
		this.shopTypeList = shopTypeList;
	}
	public List<ProvinceVo> getAddressList() {
		return addressList;
	}
	public void setAddressList(List<ProvinceVo> addressList) {
		this.addressList = addressList;
	}
	public List<AllShopVo> getShopList() {
		return shopList;
	}
	public void setShopList(List<AllShopVo> shopList) {
		this.shopList = shopList;
	}
	
	


}
