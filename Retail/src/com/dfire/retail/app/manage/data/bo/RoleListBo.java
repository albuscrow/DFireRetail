package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 项目名称：Retail  
 * 类名称：RoleListBo  
 * 类描述：   角色一览
 * 创建时间：2014年11月22日 下午2:39:47  
 * @author chengzi  
 * @version 1.0
 */
public class RoleListBo extends BaseRemoteBo {

    private static final long serialVersionUID = -3718549975448905563L;

    private List<RoleVo> roleList;

    public List<RoleVo> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleVo> roleList) {
        this.roleList = roleList;
    }
    
    
}
