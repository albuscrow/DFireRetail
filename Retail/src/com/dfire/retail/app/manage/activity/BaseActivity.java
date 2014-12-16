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
	
	public static final int REQUEST_SCAN = 99;
	public static final int RESULT_DELETE = 97;
	public static final int RESULT_EDIT = 98;
	public static final int RESULT_SAVE = 99;
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
	
		return RetailApplication.getmSessionId();//getJsessionId();
	}
	
	public ProgressDialog getProgressDialog() {
		if (mProgressDialog == null)
			mProgressDialog = new ProgressDialog(this);
		return mProgressDialog;
	}


}
