/**
 * 
 */
package com.dfire.retail.app.manage.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.netData.LoginResult;

/**
 * activity基类
 * 
 * @author qiuch
 * 
 */
public class BaseActivity extends Activity {
	
	ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RetailApplication.addCurrentActivity(this);
	}

	@Override
	protected void onDestroy() {
		RetailApplication.removeActivity(this);
		super.onDestroy();
	}
	
	/**
	 * GET the Application info
	 * @return
	 */
	protected RetailApplication getMyApp() {
		RetailApplication myapp = (RetailApplication)getApplication();
		return myapp;
	}
	
	protected String getSessionId(){
		LoginResult result=getMyApp().getLoginResult();
		if(result==null){
			return null;
		}
		return result.getJsessionId();
	}
	
	public ProgressDialog getProgressDialog() {
		if (mProgressDialog == null)
			mProgressDialog = new ProgressDialog(this);
		return mProgressDialog;
	}


}
