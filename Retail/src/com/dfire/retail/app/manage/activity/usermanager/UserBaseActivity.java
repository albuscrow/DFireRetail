package com.dfire.retail.app.manage.activity.usermanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import com.dfire.retail.app.manage.activity.TitleActivity;

public class UserBaseActivity extends TitleActivity {
	private final static String TAG = "UserBaseActivity";

	  /**
	   * 网络上获取图片资源
	   * @param path
	   * @return
	   * @throws IOException
	   */
	  public static byte[] getImage(String path) throws IOException { 
		  	
		  	Log.i(TAG,"path = "+path);
		  	
	        URL url = new URL(path);  
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	        conn.setRequestMethod("GET");   //设置请求方法为GET  
	        conn.setReadTimeout(5*1000);    //设置请求过时时间为5秒  
	        InputStream inputStream = conn.getInputStream();   //通过输入流获得图片数据  
	        byte[] data = readInputStream(inputStream);     //获得图片的二进制数据  
	        return data;  
	          
	    }
	    
	    /* 
	     * 从数据流中获得数据 
	     */  
	    public static  byte[] readInputStream(InputStream inputStream) throws IOException {  
	        byte[] buffer = new byte[1024];  
	        int len = 0;  
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	        while((len = inputStream.read(buffer)) != -1) {  
	            bos.write(buffer, 0, len);  
	        }  
	        bos.close();  
	        return bos.toByteArray();  
	          
	    } 
	   
}
