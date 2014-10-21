package com.dfire.retail.app.manage.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.dfire.retail.app.manage.global.Constants;

/**
 * 
 * 异步HttpPost请求
 * 异步HttpPost请求, 线程的终止工作交给线程池，当activity停止的时候，设置回调函数为false ，就不会执行回调方法
 */
public class AsyncHttpPost {
    private static final String TAG = "AsyncHttpPost";

	private static DefaultHttpClient httpClient;
    
    private RequestParameter mUrlParams;
    private String mUrl; //网络请求地址
    private RequestResultCallback mRequestCallback; //请求回调
    private HttpClient mHttpClient;
    private HttpPostTask mAsyncTask = null;
    
    
    private static void initHttpClient(HttpClient httpClient, int timeout) {
    	HttpParams httpParams = httpClient.getParams();
    	HttpConnectionParams.setConnectionTimeout(httpParams, 2000);
    	HttpConnectionParams.setSoTimeout(httpParams, timeout);
//    	httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
//    			HttpVersion.HTTP_1_1);
	}
    
    public static String postParm(String url,String json){  
    	String res = "";
//    	StrictMode.setThreadPolicy(ThreadUtils.SHARE_THREAD_POLICY);
    	if(httpClient == null) {
    		httpClient = new DefaultHttpClient();
    		initHttpClient(httpClient, 20000);
    		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
    	}
    	try {
    		HttpPost post = new HttpPost(url);
    		StringEntity entity = new StringEntity(json,HTTP.UTF_8);  
    		entity.setContentType("application/json");  
    		post.setEntity(entity); 
    		HttpResponse response = httpClient.execute(post);
    		Log.i(TAG,"response "+response.getStatusLine().getStatusCode());
    		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
    			HttpEntity ent = response.getEntity();  
    			res = EntityUtils.toString(ent);
    		}  
    	} catch (Exception e) {  
    		throw new RuntimeException(e);  
    	}  
    	Log.i(TAG,"result"+res);
    	return res; 
    }
    
    public AsyncHttpPost(RequestParameter requestParams, RequestResultCallback requestCallback) {
        mUrlParams = requestParams;
        mUrl = requestParams.getUrl();
        mRequestCallback = requestCallback;
        if (mHttpClient == null){
            mHttpClient = new DefaultHttpClient();
        }
    }
    
    /** 
     * 开始异步Task的执行
     * 这里不同于Android AsyncTask, 每次执行时都要重新new 一次。只需创建一次，然后在调用的地方执行。
     */
    public void execute() {
        cancel();
        mAsyncTask = new HttpPostTask();
        mAsyncTask.execute();
    }
    
    /** 
     * 取消异步Task的执行
     * 特别注意在Activity destory时要cancel正在在后台执行的Task. 不然会导致内存泄露或crash.
     */
    public void cancel() {
       if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
            mAsyncTask = null;
        }
    }
    
    private class HttpPostTask extends AsyncTask<Void, Void, Object> {
        
        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof RequestException) {
                mRequestCallback.onFail((RequestException)result);
            } else {
                mRequestCallback.onSuccess((String)result);
            }
        }
    
        @Override
        protected Object doInBackground(Void... params) {
            Object result = null;
            ByteArrayOutputStream content = null;
            try {
            	showDebugInfo();
            	return postParm(mUrl, mUrlParams.getParams().toString());
//                HttpPost request = new HttpPost(mUrl);
//                formatRequestParams(request);
//                mHttpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
//                HttpResponse response = mHttpClient.execute(request);
//                int statusCode = response.getStatusLine().getStatusCode();
//                if (statusCode == HttpStatus.SC_OK) {
//                    content = new ByteArrayOutputStream();
//                    response.getEntity().writeTo(content);
//                    result = new String(content.toByteArray(), "UTF-8").trim();
//                } else {
//                    result = new RequestException(RequestException.IO_EXCEPTION, "网络请求异常");
//                    Log.e(TAG, "request to url :" + mUrl);
//                }
     
            } catch (java.lang.IllegalArgumentException e) {
                result = new RequestException(RequestException.IO_EXCEPTION, "连接错误");
                printErr(e);
            } 
//            catch (SocketTimeoutException e) {
//                result = new RequestException(
//                        RequestException.SOCKET_TIMEOUT_EXCEPTION, "读取数据超时");
//                printErr(e);
//            } catch (UnsupportedEncodingException e) {
//                result = new RequestException(
//                        RequestException.UNSUPPORTED_ENCODEING_EXCEPTION, "编码错误");
//                printErr(e);
//            } catch (org.apache.http.conn.HttpHostConnectException e) {
//                result = new RequestException(
//                        RequestException.CONNECT_EXCEPTION, "连接错误");
//                printErr(e);
//            } catch (ClientProtocolException e) {
//                result = new RequestException(
//                        RequestException.CLIENT_PROTOL_EXCEPTION, "客户端协议异常");
//                printErr(e);
//            } catch (IOException e) {
//                result = new RequestException(
//                        RequestException.IO_EXCEPTION, "数据读取异常");
//                printErr(e);
//            }
            catch (Exception e) {
                result = new RequestException(RequestException.EXCEPTION, "异常");
                printErr(e);
            } finally {
                if (content != null) {
                    try {
                        content.close();
                        content = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    
            return result;
        }
        
        private void showDebugInfo() {
        	Log.i(TAG,mUrl);
        	Log.i(TAG,mUrlParams.getParams().toString());
		}

		private void printErr(Exception e) {
            Log.e(TAG, "request to url :" + mUrl);
            e.printStackTrace();
        }
        
        /**
         * 格式网络请求数据，返回用于网络请求的url
         * 注意相关数据的完整性. 数据为null时有可能引起异常。
         */
        private void formatRequestParams(HttpPost request) throws Exception {
            request.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT,Constants.CONNECT_TIMEOUT);
            request.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
            		Constants.READ_TIMEOUT);
 
			StringEntity entity = new StringEntity(mUrlParams.getParams().toString());  
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");			
			request.setEntity(entity); 
        }
    }	
}