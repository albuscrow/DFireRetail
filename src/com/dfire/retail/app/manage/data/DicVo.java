package com.dfire.retail.app.manage.data;

import com.dfire.retail.app.manage.global.Constants;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 字典信息, 有三种方式完成一个信息的初始化
 * 1.相关字典信息，进行json解析并初始化.//对于需要序列化的类，要特别注意，可能有问题，未验证。
 * 2.直接的进行设置初始化
 * 3.逐步初始化.
 * 
 * @author 刘思海
 *
 */
public class DicVo {
    
    private static final String TAG = "DicVo";
    
    /**
     * 字典ID
     */
    private Integer _strId; 
    /**
     * 字典名
     */
    private String _strName;
    /**
     * 字典值
     */
    private String _strValue;
    
    public DicVo() {}
    
    /**
     * 使用时请保证数据的正确性
     * @param jobj
     * @throws JSONException
     */
    public DicVo(JSONObject jobj) throws JSONException, NumberFormatException {
        if(Constants.DEBUG)
            Log.i(TAG,"init res string:" + jobj.toString());
        _strId = Integer.valueOf(jobj.getString("dicId"));
        _strName = jobj.getString("dicName");
        _strValue = jobj.getString("dicValue");
    }
    
    public DicVo(Integer id, String name, String value) {
        _strId = id;
        _strName = name;
        _strValue = value;
    }
    
    public Integer getDicId() {
        return _strId;
    }
    
    public void setDicId(Integer id) {
        _strId = id;
    }
    
    public String getDicName() {
        return _strName;
    }
    
    public void setDicName(String name) {
        _strName = name;
    }
    
    public String getDicValue() {
        return _strValue;
    }
    
    public void setDicValue(String value) {
        _strValue = value;
    }
}