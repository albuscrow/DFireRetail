package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 商户信息
 * @author Administrator
 *
 */
public class ShopDetailBo extends BaseRemoteBo{
	
	private ShopVo shop;

	//获取商户信息
	public ShopVo getShop() {
		return shop;
	}
	//设置商户信息
	public void setShop(ShopVo shop) {
		this.shop = shop;
	}
	
	

}
