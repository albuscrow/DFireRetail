package com.dfire.retail.app.manage.network;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.dfire.retail.app.manage.RetailApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 封装用于网络请求的网络地址和网络地址, 并按照格式输出 注意自己保证数据的完整性
 * 
 * @author 刘思海
 */
public class RequestParameter {

    /** 保存网络请求的api */
    private String mUrl = null;
    /** 保存网络请求的参数及值 */
    private JSONObject mParams = null;

    /**
     * 网络请求的参数中默认自带某些接口参数
     */
    public RequestParameter() {
        mParams = new JSONObject();
    }

    /**
     * 网络请求的参数中根据要求决定是否带某些默认参数。 已知的登录界面是不需要带默认参数的。
     * 
     * @param hasDefaultParams
     *            标示是否有默认的网络请求参数
     */
    public RequestParameter(boolean hasDefaultParams) {
        mParams = new JSONObject();
        if (hasDefaultParams) {
            setDefaultParams();
        }
    }

    /** 设置默认的参数 */
    private void setDefaultParams() {
        if (mParams == null)
            mParams = new JSONObject();
        try {
            mParams.put("sessionId", RetailApplication.getmSessionId());
            mParams.put("version", "RMB_1.0");
            mParams.put("appKey", "54b1304dd75c4d89a97a4241563ab4bc");
            String timeStamp = String.valueOf(System.currentTimeMillis());
			mParams.put("timestamp", timeStamp);
				mParams.put("sign", getSign("" + timeStamp, RetailApplication.getmSessionId()));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}

    }
    
    
    //共通参数：version:RMB_1.0 appKey:54b1304dd75c4d89a97a4241563ab4bc secret：0447f776c68a4f4e99282787422a828d
    static public final String  secret="0447f776c68a4f4e99282787422a828d";
    static public final String CHARSET_UTF8 = "utf8";
    //Sign生成： 
    
    private static String getSign(String timestamp, String sessionId) throws IOException {
    	// 把所有参数值串在一起
    	StringBuilder query = new StringBuilder();

    	query.append(timestamp).append(sessionId);
    	// HMAC加密
    	byte[] bytes;
    	bytes = encryptHMAC(query.toString(), secret);

    	// 把二进制转化为大写的十六进制
    	return byte2hex(bytes);
    }
    
    private static String byte2hex(byte[] array)  {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString().toUpperCase(Locale.CHINA);
	}

    private static byte[] encryptHMAC(String data, String secret) throws IOException {
    	byte[] bytes = null;
    	try{
    		SecretKey secretKey = new SecretKeySpec(secret.getBytes(CHARSET_UTF8), "HmacMD5");
    		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
    		mac.init(secretKey);
    		bytes = mac.doFinal(data.getBytes(CHARSET_UTF8));
    	} catch (GeneralSecurityException gse){
    		String msg = gse.getMessage();
    		throw new IOException(msg);
    	}
    	return bytes;
    }
    

    /**
     * 设置网络请求的具体api地址
     * 
     * @param urlApi
     *            网络功能api
     */
    public void setUrl(String urlApi) {
        mUrl = urlApi;
    }

    public String getUrl() {
        return mUrl;
    }

    /**
     * 设置用于网络请求的参数及参数值
     * 
     * @param name
     *            参数名称
     * @param value
     *            参数值
     */
    public void setParam(String name, Object value) {
        try {
            mParams.put(name, value);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // /**
    // * 设置用于网络请求的参数及参数值
    // * @param name 参数名称
    // * @param value 参数值
    // */
    // public void setParamObject(String name, String value) {
    // try {
    // mParams.put(name, new jsono);
    // } catch (JSONException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    public JSONObject getParams() {
        return mParams;
    }

    @Override
    public String toString() {
        return new StringBuilder(mUrl).append("\n").append(mParams.toString())
                .toString();
    }

    /** 清空数据 */
    public void clear() {
        mUrl = null;
    }
}
