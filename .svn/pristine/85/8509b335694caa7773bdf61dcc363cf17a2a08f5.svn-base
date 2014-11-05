package com.dfire.retail.app.manage.activity.retailmanager;

import android.util.Log;

import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.用于获取用户初始化信息， 包含信息有：角色一览、商户一览、性别一览、证件一览。 可用于添加员工及员工相关信息的修改。
 * 2.静态保存相关请求数据，在没有的情况下，重新进行网络请求。 
 *   网络请求过程是不带进度条提醒的，类似背后的网络请求。请特别注意。 
 * 3.其它类使用注意：尽量不要修改相关静态数据的值，特别是引用获取元素值的修改。
 * 
 * @author 刘思海
 * 
 */
public class UserInfoInit implements RequestResultCallback {

    private static final String TAG = "UserInfoInit";

    /**
     * 角色一览
     */
    private List<RoleVo> _roleList = null;
    /**
     * 商户一览
     */
    private List<ShopVo> _shopList = null;
    /**
     * 性别一览
     */
    private List<DicVo> _sexList = null;
    /**
     * 证件类型一览
     */
    private List<DicVo> _identityTypeList = null;

    private static UserInfoInit _instance = null;
    
    public static UserInfoInit getInstance() {
        if (_instance == null) {
            _instance = new UserInfoInit();
        } 
        
        return _instance;
    }
    
    private UserInfoInit() {
        
    }
    
    public void startGetUserInfo() {
        if (!checkData()) {
            clearAllData();
            startRequestData();
        }
    }

    /**
     * 简单的检查数据的完整性， 
     * 当数据不完整时，应当删除所有数据，重新网络请求。返回false. 
     * 当数据完整时，不做操作，返回true;
     * 
     * @return boolean
     */
    public boolean checkData() {
        if (_roleList == null || _roleList.size() == 0 || _shopList == null
                || _shopList.size() == 0 || _sexList == null
                || _sexList.size() == 0 || _identityTypeList == null
                || _identityTypeList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 网络请求，显示员工列表.
     */
    private void startRequestData() {
        RequestParameter parameters = new RequestParameter(true);
        parameters.setUrl(Constants.EMPLOYEE_INFO_INIT);
        new AsyncHttpPost(parameters, this).execute();
    }

    /**
     * 清空数据
     */
    public void clearAllData() {
        if (_roleList != null) {
            _roleList.clear();
            _roleList = null;
        }

        if (_shopList != null) {
            _shopList.clear();
            _shopList = null;
        }

        if (_sexList != null) {
            _sexList.clear();
            _sexList = null;
        }

        if (_identityTypeList != null) {
            _identityTypeList.clear();
            _identityTypeList = null;
        }
    }

    public List<RoleVo> getRoleList() {
        return _roleList;
    }

    public List<ShopVo> getShopList() {
        return _shopList;
    }

    public List<DicVo> getSexList() {
        return _sexList;
    }

    public List<DicVo> getIdentityTypeList() {
        return _identityTypeList;
    }

    @Override
    public void onFail(Exception e) {
        Log.i(TAG, e.toString());
        setAllTestData();
    }
    
    @Override
    public void onSuccess(String str) {
        try {
            JSONObject object = new JSONObject(str);
            if (object.isNull("returnCode") || "fail".equals(object.getString("returnCode"))) {
                if (Constants.DEBUG) {
                    Log.i(TAG, object.getString("exceptionCode"));
                }
                setAllTestData();
            } else {
                parserRoleList(object);
                parserShopList(object);
                parserSexList(object);
                parserIdentityTypeList(object);
            }
        } catch (Exception e) {
            setAllTestData();
        }
    }

    /**
     * 解析角色list
     */
    private void parserRoleList(JSONObject obj){
        try {
            initOrClearRoleList();            
            JSONArray roleList = obj.getJSONArray("roleList");
            if (roleList != null) {
                for (int i = 0; i < roleList.length(); i++) {
                    _roleList.add(new RoleVo(roleList.getJSONObject(i)));
                }
            }
        } catch (Exception e) {
            testRoleList();
        }
    }
    
    private void initOrClearRoleList() {
        if (_roleList == null) {
            _roleList = new ArrayList<RoleVo>();
        } else {
            _roleList.clear();
        }
    }
    
    /**
     * 解析商户list
     */
    private void parserShopList(JSONObject object) {
        try {
            initOrClearShopList();
            JSONArray shopList = object.getJSONArray("shopList");
            if (shopList != null) {
                for (int i = 0; i < shopList.length(); i++) {
                    _shopList.add(new ShopVo(shopList.getJSONObject(i)));
                }
            }
        } catch (Exception e) {
            testShopList();
        }
    }
    
    private void initOrClearShopList() {
        if (_shopList == null) {
            _shopList = new ArrayList<ShopVo>();
        } else {
            _shopList.clear();
        }
    }
    
    /**
     * 解析性别List
     */
    private void parserSexList(JSONObject object){
        try {
            initOrClearSexList();
            JSONArray sexList = object.getJSONArray("sexList");
            if (sexList != null) {
                for (int i = 0; i < sexList.length(); i++) {
                    _sexList.add(new DicVo(sexList.getJSONObject(i)));
                }
            }
        } catch (Exception e) {//数据解析出错时
            testSexList();
        }
    }
    
    private void initOrClearSexList() {
        if (_sexList == null) {
            _sexList = new ArrayList<DicVo>();
        } else {
            _sexList.clear();
        }
    }
    
    /**
     * 解析证件List
     */
    private void parserIdentityTypeList(JSONObject object) {
        try {
            initOrClearIdentityTypeList();
            JSONArray identityTypeList = object.getJSONArray("identityTypeList");
            if (identityTypeList != null) {
                for (int i = 0; i < identityTypeList.length(); i++) {
                    _identityTypeList.add(new DicVo(identityTypeList.getJSONObject(i)));
                }
            }
        } catch (Exception e) {
            testIdentityTypeList();
        }
    }
    
    private void initOrClearIdentityTypeList() {
        if (_identityTypeList == null) {
            _identityTypeList = new ArrayList<DicVo>();
        } else {
            _identityTypeList.clear();
        }
    }
    
    public String getSex(int id) {
        DicVo role;
        if (_sexList != null) {
            for (int i = 0; i < _sexList.size(); i++) {
                role = _sexList.get(i);
                if (role.getVal().intValue() == id) {
                    return role.getName();
                }
            }
        }
        
        return "";
    }
    
    public String getIdentityType(int id) {
        DicVo identityType;
        if (_identityTypeList != null) {
            for (int i = 0; i < _identityTypeList.size(); i++) {
                identityType = _identityTypeList.get(i);
                if (identityType.getVal().intValue() == id) {
                    return identityType.getName();
                }
            }
        }
        
        return "";
    }
    
    public String getRoleListMax() {
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
    
    public String getShopListMax() {
        String longest = Constants.EMPTY_STRING;
        if (_shopList != null) {
            for (ShopVo item : _shopList) {
                if (longest.length() < item.getShopName().length()) {
                    longest = item.getShopName();
                }
            }
        }
        return longest;
    }
    
    public String getSexListMax() {
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
    
    public String getIdentityListMax() {
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
    
    
    //==================================本地测试数据=======================================/
    private void setAllTestData() {
        if (Constants.TEST) {//测试数据
            testRoleList();
            testShopList();
            testSexList();
            testIdentityTypeList();
        }
    }
    
    private void testRoleList() {
        if (Constants.TEST) {
            initOrClearRoleList();
            _roleList.add(new RoleVo("huajianlianshuoceshiroleid000000", "bd店长", null));
            _roleList.add(new RoleVo("huajianlianshuoceshiroleid000000", "bd收银员", null));
            _roleList.add(new RoleVo("huajianlianshuoceshiroleid000000", "bd经理", null));
        }
    }
    
    private void testShopList() {
        if (Constants.TEST) {
            initOrClearShopList();
            _shopList.add(new ShopVo("fb099d45726942929c3337a6c80bcf9e", "本地测试文一路", null, null, null, null, null, null, null, null, null));
            _shopList.add(new ShopVo("fb099d45726942929c3337a6c80bcf9e", "本地测试文二路", null, null, null, null, null, null, null, null, null));
        }
    }
    
    private void testIdentityTypeList() {
        if (Constants.TEST) {
            initOrClearIdentityTypeList();
            _identityTypeList.add(new DicVo("身份证", Integer.valueOf(1)));
            _identityTypeList.add(new DicVo("学生证", Integer.valueOf(2)));
            _identityTypeList.add(new DicVo("驾驶证", Integer.valueOf(3)));
        }
    }
    
    private void testSexList() {
        if (Constants.TEST) {
            initOrClearSexList();
            _sexList.add(new DicVo("男", Integer.valueOf(1)));
            _sexList.add(new DicVo("女", Integer.valueOf(2)));
        }
    }
}
