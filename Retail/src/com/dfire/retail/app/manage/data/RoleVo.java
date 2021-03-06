package com.dfire.retail.app.manage.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.dfire.retail.app.manage.global.Constants;

/**
 * 角色信息
 * 
 * @author kyolee
 * 
 */
public class RoleVo implements Serializable{

    private static final long serialVersionUID = 1268731686534140709L;

    private static final String TAG = "RoleVo";
    
    private String roleId;// 角色ID
    private String roleName;// 角色名
    private String shopId;// 商户ID
    private String lastVer;//未知属性，JSON字段
    
    /** 间接赋值 */
    public RoleVo () {}
    
    /** 直接赋值 */
    public RoleVo (String id, String name, String shopId) {
        roleId = id;
        roleName = name;
        this.shopId = shopId;
    }
    
    /** JSON数据解析赋值 */
    public RoleVo(JSONObject jobj) throws JSONException {
        if (jobj != null) {
            if (Constants.DEBUG) {
                Log.i(TAG, jobj.toString());
            }
            
            roleId = jobj.getString("roleId");
            roleName = jobj.getString("roleName");
            if (!jobj.isNull("shopId")) {
                shopId = jobj.getString("shopId");
            }
            if (!jobj.isNull("lastVer")) {
                lastVer = jobj.getString("lastVer");
            }
        }
    }
    
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    public void setLastVer(String lastVer) {
        this.lastVer = lastVer;
    }
    
    public String getLastVer() {
        return lastVer;
    }
    
    @Override
    public String toString() {
        return roleName;
    }
}
