package com.dfire.retail.app.manage.util;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;

import com.dfire.retail.app.manage.data.LogisticsVo;
import com.dfire.retail.app.manage.data.ReceiptVo;
import com.dfire.retail.app.manage.global.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 *
 * @author jixing
 * @create 2011-5-13
 */
public class JsonUtil {
	
	private JsonObject jo;
	
	public JsonUtil(String input) {
		jo = new JsonParser().parse(input).getAsJsonObject();
	}
	
	public JsonObject getJsonObject(){
		return jo;
	}


	public String getString(String name){
		JsonElement jsonElement = jo.get(name);
		if (jsonElement == null || jsonElement.isJsonNull()) {
			return null;
		}
		return jsonElement.getAsString();
	}
	
	public int getInt(String name){
		JsonElement jsonElement = jo.get(name);
		if (jsonElement == null || jsonElement.isJsonNull()) {
			return 0;
		}
		return jsonElement.getAsInt();
	}
	
	public String getReturnCode(){
		return getString(Constants.RETURN_CODE);
	}
	
	public String getExceptionCode(){
		return getString(Constants.EXCEPTAION_CODE);
	}

	public boolean isError() {
		String returnCode = getReturnCode();
		return returnCode == null || !returnCode.equals(Constants.LSUCCESS);
	}
	
	public boolean isErrorSpecial() {
		String returnCode = getReturnCode();
		return returnCode == null|| !returnCode.equals(Constants.LSUCCESS);
	}
	
	public boolean isError(Context context){
		if (isError()) {
			ToastUtil.showShortToast(context, Constants.getErrorInf(getExceptionCode(), Constants.NO_NET));
			return true;
		}else{
			return false;
		}
	}

	public Object get(String name, Type type) {
		return new Gson().fromJson(jo.get(name), type);
	}
	public ReceiptVo get(Class<ReceiptVo> class1) {
		return new Gson().fromJson(jo, class1);
	}
}
