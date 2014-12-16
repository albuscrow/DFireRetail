package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 员工初始化返回结果
 * @author Administrator
 *
 */
public class UserInitBo extends BaseRemoteBo{
	
	List<RoleVo>	 roleList;	//员工角色					
	List<AllShopVo>	 shopList;	//所属门店					
	List<DicVo> sexList	;		//性别列表				
	List<DicVo>	 identityTypeList;//证件类型
	
	public List<RoleVo> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<RoleVo> roleList) {
		this.roleList = roleList;
	}
	public List<AllShopVo> getShopList() {
		return shopList;
	}
	public void setShopList(List<AllShopVo> shopList) {
		this.shopList = shopList;
	}
	public List<DicVo> getSexList() {
		return sexList;
	}
	public void setSexList(List<DicVo> sexList) {
		this.sexList = sexList;
	}
	public List<DicVo> getIdentityTypeList() {
		return identityTypeList;
	}
	public void setIdentityTypeList(List<DicVo> identityTypeList) {
		this.identityTypeList = identityTypeList;
	}	
	
	


}
