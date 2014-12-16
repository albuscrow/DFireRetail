/**
 * 
 */
package com.dfire.retail.app.manage.common;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;

/**
 * @author qiuch
 * 
 */
public class CommonUtils {
	/**
	 * 判断文字列为空
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isEmpty(String value) {
		if (value == null || value.length() == 0) {
			return true;
		}
		return false;
	}
	
	//获取错误消息
	public static String getUMFailMsg(Context context,String result) {	
		
		String strErrorMsg = "";
		String strRet= "";
		int errCode = 0;
		JSONObject jobj;		
		try {
			jobj = new JSONObject(result);
			strErrorMsg = jobj.getString("exceptionCode");

		}catch (JSONException e) {
			e.printStackTrace();
			strErrorMsg ="";
		}
		errCode = ErrorMsg.getEorMsgId(strErrorMsg);
		
		switch (errCode) {
		
			case 1:
				strRet = context.getResources().getString(R.string.um_msg_000001);				
				break;
			case 2:
				strRet = context.getResources().getString(R.string.um_msg_000002);				
				break;
			case 3:
				strRet = context.getResources().getString(R.string.um_msg_000003);				
				break;
			case 4:
				strRet = context.getResources().getString(R.string.um_msg_000004);				
				break;
			case 5:
				strRet = context.getResources().getString(R.string.um_msg_000005);				
				break;	
			case 6:
				strRet = context.getResources().getString(R.string.um_msg_000006);				
				break;
			case 7:
				strRet = context.getResources().getString(R.string.um_msg_000007);				
				break;
			case 8:
				strRet = context.getResources().getString(R.string.um_msg_000008);				
				break;
			case 9:
				strRet = context.getResources().getString(R.string.um_msg_000009);				
				break;
			case 10:
				strRet = context.getResources().getString(R.string.um_msg_0000010);				
				break;	
			case 11:
				strRet = context.getResources().getString(R.string.um_msg_0000011);				
				break;
			case 12:
				strRet = context.getResources().getString(R.string.um_msg_0000012);				
				break;
			case 13:
				strRet = context.getResources().getString(R.string.um_msg_0000013);				
				break;
			case 14:
				strRet =context.getResources().getString(R.string.um_msg_0000014);				
				break;		
	
			default:
				strRet = Constants.getErrorInf(strErrorMsg,"位置错误");
			break;
		}
		
		return strRet;
	}
	
	
	//判断登陆是否成功
	public static boolean isResuestSucess(String result) {	
		
		boolean ret = false;		
		JSONObject jobj;		
		try {
			jobj = new JSONObject(result);
			if(!jobj.getString("returnCode").equalsIgnoreCase(Constants.REPONSE_FAIL)){
				ret = true;	
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return ret;
	}	

	

 
 
 

	  /**
     * 角色一览，获取角色名字长度
     */
    
	
    public static String getRoleListMax(List<RoleVo> _roleList) {
        String longest = Constants.EMPTY_STRING;
        if (_roleList != null) {
            for (RoleVo item : _roleList) {
                if (longest.length() < item.getRoleName().length()) {
                    longest = item.getRoleName();
                }
            }
        }
        return longest;
    }
    
    /**
     * 商户一览，获取商户名字长度
     */
 
    public static String getShopListMax(List<AllShopVo> mShopList) {
        String longest = Constants.EMPTY_STRING;
        if (mShopList != null) {
            for (AllShopVo item : mShopList) {
                if (longest.length() < item.getShopName().length()) {
                    longest = item.getShopName();
                }
            }
        }
        return longest;
    }
    
    /**
     * 性别一览，获取性别预览长度
     */
  

    public static String getSexListMax(List<DicVo> _sexList) {
        String longest = Constants.EMPTY_STRING;
        if (_sexList != null) {
            for (DicVo item : _sexList) {
                if (longest.length() < item.getName().length()) {
                    longest = item.getName();
                }
            }
        }
        return longest;
    }
    
    /**
     * 证件类型一览,获取spiner长度
     */
    
    public static String getIdentityListMax(List<DicVo> _identityTypeList) {
        String longest = Constants.EMPTY_STRING;
        if (_identityTypeList != null) {
            for (DicVo item : _identityTypeList) {
                if (longest.length() < item.getName().length()) {
                    longest = item.getName();
                }
            }
        }
        return longest;
    }
    
    /**
     * 证件类型一览,获取spiner长度
     */
    
    public static String geStringListMax(ArrayList<String> strList) {
        String longest = Constants.EMPTY_STRING;
        if (strList  !=  null) {
            for (String item : strList) {
                if (longest.length() < item.length()) {
                    longest = item;
                }
            }
        }
        return longest;
    }
    // 权限
    public static boolean getPermission(String name) {
        if (RetailApplication.mUserActionMap != null && RetailApplication.mUserActionMap.get(name) != null) {
            return true;
        }
        return false;
    }
}
