package com.dfire.retail.app.manage.adapter;

import com.dfire.retail.app.manage.data.AllShopVo;

public class ShopInfoItem extends AllShopVo{


	    public ShopInfoItem(String shopId, String shopName, String parentId,
	            String code) {
	        setShopId(shopId); 
	        setShopName(shopName);
	        setParentId(parentId);
	        setCode(code);
	    }

	   

}
