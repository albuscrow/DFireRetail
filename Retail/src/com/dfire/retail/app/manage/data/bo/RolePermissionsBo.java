package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.SystemInfoVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 项目名称：Retail  
 * 类名称：RolePermissionsBo  
 * 类描述：   角色权限-角色对应的权限
 * 创建时间：2014年11月24日 上午10:03:01  
 * @author chengzi  
 * @version 1.0
 */
public class RolePermissionsBo extends BaseRemoteBo {

    private static final long serialVersionUID = -7600409059760000119L;

    private RoleVo role;
    /**根据角色查权限*/
    private List<SystemInfoVo> actionList;
    /**初始化*/
    private List<SystemInfoVo> systemInfoList;
   
    public RoleVo getRole() {
        return role;
    }
    public void setRole(RoleVo role) {
        this.role = role;
    }
    public List<SystemInfoVo> getActionList() {
        return actionList;
    }
    public void setActionList(List<SystemInfoVo> actionList) {
        this.actionList = actionList;
    }
    public List<SystemInfoVo> getSystemInfoList() {
        return systemInfoList;
    }
    public void setSystemInfoList(List<SystemInfoVo> systemInfoList) {
        this.systemInfoList = systemInfoList;
    }
    
    
}
