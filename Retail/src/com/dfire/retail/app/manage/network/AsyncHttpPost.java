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
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dfire.retail.app.common.exception.BizException;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.common.LoadingDialog;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
import com.dfire.retail.app.manage.data.bo.LoginReturnBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.util.MD5;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * 异步HttpPost请求
 * 异步HttpPost请求, 线程的终止工作交给线程池，当activity停止的时候，设置回调函数为false ，就不会执行回调方法
 */
@SuppressLint("DefaultLocale")
public class AsyncHttpPost{
	private static final String TAG = "AsyncHttpPost";
    public final static String REMOTE_MSG_PATTERN = "[A-Z]{2}_[A-Z]{3}_[0-9]{6}";
	private static DefaultHttpClient httpClient;
    
    private RequestParameter mUrlParams;
    private String mUrl; //网络请求地址
    private RequestResultCallback mRequestCallback; //请求回调
    private HttpPostTask mAsyncTask = null;
    private RequestCallback callback;

	private Class<? extends BaseRemoteBo> cls;
	private Context context;
    
	private LoadingDialog mProgressDialog;// 对话框
	
    private static void initHttpClient(HttpClient httpClient, int timeout) {
    	HttpParams httpParams = httpClient.getParams();
    	HttpConnectionParams.setConnectionTimeout(httpParams, 2000);
    	HttpConnectionParams.setSoTimeout(httpParams, timeout);
	}
    
    public static String postParm(String url,String json){  
    	
    	String res = "";
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
    		
    		synchronized (httpClient) {
    			HttpResponse response = httpClient.execute(post);
    		    if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
    		     //根据相应得到实体内容
    		    	HttpEntity ent = response.getEntity();
        			res = EntityUtils.toString(ent);
    		    }
    		}  
    	} catch (Exception e) { 
    		e.printStackTrace();
    		throw new BizException("网络连接出错，可能是未连接到网络");
    	}  
    	Log.i(TAG,"result"+res);
    	return res; 
    }
    /**
     * see AsyncHttpPost(Context context,RequestParameter requestParams,Class<? extends BaseRemoteBo> cls, RequestCallback<T> callback)
     * @param requestParams
     * @param requestCallback
     * 以前的写法
     */
//    @Deprecated
    public AsyncHttpPost(final Context context, RequestParameter requestParams, RequestResultCallback requestCallback, final boolean needShow) {
        mUrlParams = requestParams;
        mUrl = requestParams.getUrl();
        mRequestCallback = requestCallback;
        new DefaultHttpClient();
        this.context = context;
        
        if (needShow) {
        	mProgressDialog = new LoadingDialog(context, true);//请求网络 转转转
        }
    	
    }
    
    /**
     * see AsyncHttpPost(Context context,RequestParameter requestParams,Class<? extends BaseRemoteBo> cls, RequestCallback<T> callback)
     * @param requestParams
     * @param requestCallback
     * 以前的写法
     */
    @Deprecated
    public AsyncHttpPost(RequestParameter requestParams, RequestResultCallback requestCallback) {
    	
        mUrlParams = requestParams;
        mUrl = requestParams.getUrl();
        mRequestCallback = requestCallback;
        new DefaultHttpClient();
    }
    
    public AsyncHttpPost(Context context,RequestParameter requestParams, RequestResultCallback requestCallback) {
    	this(context, requestParams, requestCallback, true);
    }    /**
     * 判断是否要转转转
     * @param context
     * @param requestParams
     * @param cls
     * @param showLoading 请求获取列表的时候不需要转 ， 有下拉 刷新  上拉加载的功能 ， 所以 不显示转
     * @param callback
     */
    public AsyncHttpPost(Context context,RequestParameter requestParams,Class<? extends BaseRemoteBo> cls,boolean showLoading,RequestCallback callback) {
    	mUrlParams = requestParams;
    	mUrl = requestParams.getUrl();
    	this.callback = callback;
    	this.cls = cls;
    	this.context = context;
    	new DefaultHttpClient();
    	if (showLoading) {
    		mProgressDialog = new LoadingDialog(context, true);//请求网络 转转转
		}
    }
    public AsyncHttpPost(Context context,RequestParameter requestParams,Class<? extends BaseRemoteBo> cls, RequestCallback callback) {
    	this(context,requestParams,cls,true,callback);
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
    public void execute(RequestParameter requestParams) {
        cancel();
        this.mUrlParams = requestParams;
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
        	if (mProgressDialog != null && mProgressDialog.isShowing()) {
        		mProgressDialog.dismiss(); 
        	}
        	if (result==null) {
				return;
			}
        	/**以前的写法*/
        	if(mRequestCallback != null) {
        		if (result instanceof RequestException) {
                    mRequestCallback.onFail((RequestException)result);
                }else if (result instanceof BizException) {
                	mRequestCallback.onFail((BizException)result);
                } else {
                	Gson gson = new Gson();
                	BaseRemoteBo base = gson.fromJson((String)result, BaseRemoteBo.class);
                	if (base == null) {
						ToastUtil.showShortToast(context, Constants.NO_NET);
						return;
					}
                	if(base.getExceptionCode() != null && base.getExceptionCode().equals("CS_MSG_000019")) {
                		loginAsyncTask();
                	}else {
                		//success 
                		mRequestCallback.onSuccess((String)result);
                		//        			callback.onSuccess(oj);
                	}
                }
        	}
        	/**
        	 * 新写法
        	 */
            if(callback == null) {
            	return;
            }
            if (result instanceof RequestException) {
                callback.onFail((RequestException)result);
                return;
            }else if (result instanceof BizException) {
                //callback.onFail((BizException)result);
                new ErrDialog(context, ((BizException) result).getMessage()).show();
                return;
            }
            Gson gson = new Gson();
			Object oj = gson.fromJson((String)result, cls);
            if(oj instanceof BaseRemoteBo) {
            	BaseRemoteBo base = (BaseRemoteBo) oj;
        		if (!base.isSuccess()) {
        			//session expired
        			if(base.getExceptionCode() != null && base.getExceptionCode().equals("CS_MSG_000019")) {
        				loginAsyncTask();
        			}else {
        				String exceptionCode = base.getExceptionCode();
           			 	if(exceptionCode != null && exceptionCode.matches(REMOTE_MSG_PATTERN)) {
    				       int resource = context.getResources().getIdentifier(exceptionCode, "string", context.getPackageName());
    				       if(resource != 0) {
    				           String code = context.getResources().getString(resource);
                               new ErrDialog(context, code).show();//请求出错 提示信息
    	                    }else {
    	                       new ErrDialog(context, exceptionCode).show();//请求出错 提示信息
    	                    }
    				      
    				    }else{
    				       new ErrDialog(context, exceptionCode).show();
    				    }
        			}
        		} else {
        			//success 
        			callback.onSuccess(oj);
        		}
        	}
        }
    
        @Override
        protected Object doInBackground(Void... params) {
            Object result = null;
            ByteArrayOutputStream content = null;
            try {
            	showDebugInfo();
            	return postParm(mUrl, fix(mUrlParams.getParams().toString()));
            } catch (java.lang.IllegalArgumentException e) {
                result = new RequestException(RequestException.IO_EXCEPTION, "连接错误");
                printErr(e);
            } 
            catch (Exception e) {
            	result = new BizException("网络连接出错，可能是未连接到网络");
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
        private String fix(String string) {
        	JsonObject jo = new JsonParser().parse(string).getAsJsonObject();
        	JsonElement jsonElement = jo.get(Constants.BAR_CODE_FOR_REQUEST);
        	if (jsonElement == null) {
				return string;
			}
			String barCode = jsonElement.getAsString();
        	if (barCode.length() == 0) {
        		jo.add(Constants.BAR_CODE_FOR_REQUEST, JsonNull.INSTANCE);
			}
			String result = new GsonBuilder().serializeNulls().create().toJson(jo);
			return result;
		}
        
        /* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (mProgressDialog != null) {
	    		mProgressDialog.show();
			}
		}

		private void showDebugInfo() {
        	Log.i(TAG,mUrl);
        	Log.i(TAG,mUrlParams.getParams().toString());
		}
		private void printErr(Exception e) {
            Log.e(TAG, "request to url :" + mUrl);
            e.printStackTrace();
        }
    }	
    /**
     * 重连
     */
    private void loginAsyncTask(){

		RetailApplication.setmSessionId("");
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.LOGIN);
		String strPass="";
		String userName = RetailApplication.userName.toUpperCase();
		String password = RetailApplication.password.toUpperCase();
		String code = RetailApplication.code;
		strPass = MD5.GetMD5Code(password);
		parameters.setParam("entityCode", code);
		parameters.setParam("username",userName);
		parameters.setParam("password", strPass);
		new AsyncHttpPost(context,parameters,LoginReturnBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				LoginReturnBo bo = (LoginReturnBo)oj;
				if (bo!=null) {
					RetailApplication.setShopVo(bo.getShop());
		        	RetailApplication.setEntityModel(bo.getEntityModel());
		        	RetailApplication.setUserInfo(bo.getUser());			        					        				       
		        	RetailApplication.setmSessionId(bo.getJsessionId());
		        	
		        	mUrlParams.refreshSessionParams();
		        	new HttpPostTask().execute();
				}
			}
			@Override
			public void onFail(Exception e) {
			}
		}).execute();
    }
    
    public interface RequestCallback
    {
        /**
         * 数据请求成功
         * @param bo
         */
        public void onSuccess(Object oj);
        
    	/**
         * 数据请求失败
         * @param e
         */        
        public void onFail(Exception e);
    }
}