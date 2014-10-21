/**
 * 
 */
package com.dfire.retail.app.manage.activity.login;


import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.retailmanager.RetailBGdetailActivity;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.common.DateDialog;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.netData.LoginResult;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.MD5;
import com.dfire.retail.app.manage.util.ToastUtil;

/**
 * 登录
 * 
 * @author qiuch
 * 
 */
public class LoginActivity extends TitleActivity {
	
	private static final String TAG="LoginActivity";


	private EditText mShop, mUserName, mPassword;
	private ImageView mShopClear, mUserNameClear, mPasswordClaear;
	private Button mLogin;
	LoginResult mResultData;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		findviews();
	}

	private void findviews() {
		setTitleRes(R.string.login_title);
		setRightBtn(R.drawable.help).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DateDialog(LoginActivity.this).show();
				// new CardTypeDialog(LoginActivity.this).show();

			}
		});

		mShop = (EditText) findViewById(R.id.edit_shop_num);
		mUserName = (EditText) findViewById(R.id.edit_user_name);
		mPassword = (EditText) findViewById(R.id.edit_pass);

		mShopClear = (ImageView) findViewById(R.id.edit_shop_num_clear);
		mUserNameClear = (ImageView) findViewById(R.id.edit_user_name_clear);
		mPasswordClaear = (ImageView) findViewById(R.id.edit_pass_clear);
		mLogin = (Button) findViewById(R.id.loigin_button);
		
		mShopClear.setVisibility(View.VISIBLE);
		mShop.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if (hasFocus) {
					mShopClear.setVisibility(View.VISIBLE);
				} else {
					mShopClear.setVisibility(View.INVISIBLE);
					mLogin.setBackgroundResource(R.drawable.login_btn_press);
					mLogin.setClickable(true);
				}
			}
		});

		mUserName.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mUserNameClear.setVisibility(View.VISIBLE);
				} else {
					mUserNameClear.setVisibility(View.INVISIBLE);
				}
			}
		});

		mPassword.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mPasswordClaear.setVisibility(View.VISIBLE);
				} else {
					mPasswordClaear.setVisibility(View.INVISIBLE);
				}
			}
		});

		mLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//Intent intent = new Intent(LoginActivity.this, RetailBGdetailActivity.class);
				//startActivity(intent);
				
				if (CommonUtils.isEmpty(mShop.getText().toString())) {
					Toast.makeText(LoginActivity.this, "请输入店家编号",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (CommonUtils.isEmpty(mUserName.getText().toString())) {
					Toast.makeText(LoginActivity.this, "请输入员工",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (CommonUtils.isEmpty(mPassword.getText().toString())) {
					Toast.makeText(LoginActivity.this, "请输入密码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				//显示进度条对话框

				getProgressDialog().setCancelable(false);
				// progressDialog.setCanceledOnTouchOutside(false);
				getProgressDialog()
						.setMessage(getResources().getText(R.string.in_login));
				getProgressDialog().show();
				
				//传递请求参数
				RequestParameter parameters = new RequestParameter(true);
				parameters.setUrl(Constants.LOGIN);
				String strPass="";
				strPass = MD5.GetMD5Code(mPassword.getText().toString());
				parameters.setParam("entityCode", mShop.getText().toString());
				parameters.setParam("username", mUserName.getText().toString());
				parameters.setParam("password", strPass);
				
				AsyncHttpPost httppost = new AsyncHttpPost(parameters,
		        new RequestResultCallback() {
			        @Override
			        public void onSuccess(String str) {
			        	JsonUtil ju = new JsonUtil(str);
			        	RetailApplication.setShopVo((ShopVo) ju.get("shop", ShopVo.class));
			        	RetailApplication.setEntityModel((Integer) ju.get("entityModel", Integer.class));
                        Message msg = new Message();
                        if(CommonUtils.isResuestSucess(str)){
                        	msg.what = Constants.HANDLER_SUCESS;
                            msg.obj = str;
                        }else{
                        	msg.what = Constants.HANDLER_ERROR;
                        	msg.obj = CommonUtils.getUMFailMsg(getBaseContext(),str);
                        }

                        mLoginHandler.sendMessage(msg);
			        }
			        @Override
			        public void onFail(Exception e) {
                        e.printStackTrace();
                        Log.e("results", "Login FaiL");
                        Message msg = new Message();
                        msg.what = Constants.HANDLER_FAIL;
                        msg.obj = e.getMessage();
                        mLoginHandler.sendMessage(msg);
			        }
		        });
				httppost.execute();
		
		
			}
		});

		//被陆哲琪注释，以方便调试
//		mLogin.setClickable(false);

	}

	/**
	 * 处理返回的结果，如果成功解析网络返回的json数据
	 */
	Handler mLoginHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			getProgressDialog().dismiss();
			
			switch (msg.what) {
				case Constants.HANDLER_SUCESS:
					
					try {
						parserJson(msg.obj.toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startActivity(new Intent(LoginActivity.this, RetailBGdetailActivity.class));
				break;
				
				case Constants.HANDLER_FAIL:
				    if (msg.obj != null) {
    					ToastUtil.showShortToast(getApplicationContext(),
    					        msg.obj.toString());
				    } else {
                        ToastUtil.showShortToast(getApplicationContext(),
                                "登录失败");
				    }
				    //测试时不管成功或失败
				    if (Constants.TEST){
				    	startActivity(new Intent(LoginActivity.this, RetailBGdetailActivity.class));
				    }
					break;
				case Constants.HANDLER_ERROR:
				    if (msg.obj != null) {
    					ToastUtil.showShortToast(getApplicationContext(),
    				        msg.obj.toString());
				    } else {
                        ToastUtil.showShortToast(getApplicationContext(),
                                "登录出错");
				    }
                    //测试时不管成功或失败
				    if (Constants.TEST){
				        startActivity(new Intent(LoginActivity.this, RetailBGdetailActivity.class));
				    }
					break;
			}
		}
	};
	
	//解析json数据
	protected void parserJson(String result) throws JSONException {
		mResultData=new LoginResult(result);	
		//save the login info in application
		RetailApplication.setLoginResult(mResultData);
		Log.i(TAG," jsessionId =  "+mResultData.getJsessionId());
	}
	
	
}