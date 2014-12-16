package com.dfire.retail.app.manage.data;

import java.io.Serializable;

import com.dfire.retail.app.manage.global.Constants;
import com.google.gson.GsonBuilder;

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
public class DicVo  implements Serializable {
    
    private static final String TAG = "DicVo";
    
    /**
     * 字典名
     */
    private String name;
    /**
     * 字典ID
     */
    private Integer val;
    
    
    
    public DicVo() {
    }
    
    public DicVo(String name,Integer val) {
    	this.name = name;
    	this.val = val;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getVal() {
		return val;
	}

	public void setVal(Integer val) {
		this.val = val;
	}
    
	   public DicVo(JSONObject jobj) throws JSONException {
	        if (jobj != null) {
	            if (Constants.DEBUG) {
	                Log.i(TAG, jobj.toString());
	            }
	            val = Integer.valueOf(jobj.getString("val"));
	            name = jobj.getString("name");	

	        }
	    }

  
		@Override
		public String toString() {
			return new GsonBuilder().serializeNulls().create().toJson(this);
		}  
}
