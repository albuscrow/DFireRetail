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
import com.dfire.retail.app.manage.common.ErrorMsg;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.LoginReturnBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.global.UserInfo;
import com.dfire.retail.app.manage.netData.LoginResult;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.MD5;
import com.dfire.retail.app.manage.util.ToastUtil;

/**
 * 登录
 * 
 * @author qiuch
 * 
 */
public class LoginActivity extends TitleActivity implements OnClickListener{
	
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
		
		mShopClear.setOnClickListener(this);
		mUserName.setOnClickListener(this);
		mPasswordClaear.setOnClickListener(this);
		
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
		mShopClear.setOnClickListener(this);
		mPasswordClaear.setOnClickListener(this);
		mUserNameClear.setOnClickListener(this);
		mLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
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
				//传递请求参数
				RequestParameter parameters = new RequestParameter(true);
				parameters.setUrl(Constants.LOGIN);
				String strPass="";
				final String userName = toUpperCase(mUserName.getText().toString());
				final String password = toUpperCase(mPassword.getText().toString());
				final String code = mShop.getText().toString();
				strPass = MD5.GetMD5Code(password);
				parameters.setParam(Constants.ENTITY_CODE,code);
				parameters.setParam(Constants.USER_NAME,userName);
				parameters.setParam(Constants.LOGIN_PASSWORD, strPass);
				
				AsyncHttpPost httppost = new AsyncHttpPost(LoginActivity.this,parameters, LoginReturnBo.class,true,
		        new RequestCallback() {
			        @Override
			        public void onSuccess(Object obj) {
			        	LoginReturnBo bo = (LoginReturnBo) obj;
			        	if (bo!=null) {
			        	    //权限
			        	    if (bo.getUserActionMap() != null) {
		                        RetailApplication.mUserActionMap = bo.getUserActionMap();
		                    }
				        	RetailApplication.setShopVo((ShopVo) bo.getShop());
				        	RetailApplication.setEntityModel((Integer) bo.getEntityModel());
				        	UserInfo userinfo = (UserInfo)bo.getUser();
				        	RetailApplication.setUserInfo(userinfo);			        					        				       
				        	RetailApplication.setmSessionId(bo.getJsessionId());
				        	RetailApplication.userName = userName;
				        	RetailApplication.password = password;
				        	RetailApplication.code = code;
			        	
				        	startActivity(new Intent(LoginActivity.this, RetailBGdetailActivity.class));
				        	finish();
			        	}
			        }
			        @Override
			        public void onFail(Exception e) {
			        }
		        });
				httppost.execute();
			}
		});
	}
	/**
	 * 大小写转换
	 */
	private String toUpperCase(String text){
		String newText = "";
		for(int i=0;i<text.length();i++){
			newText = newText+text.substring(i,i+1).toUpperCase();
		}
		return newText;
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
					//startActivity(new Intent(LoginActivity.this, RetailBGdetailActivity.class));
				break;
				
				case Constants.HANDLER_FAIL:
					Log.i("mLoginHandle",msg.obj.toString());
					if(msg.obj !=null){
                        ToastUtil.showLongToast(LoginActivity.this,
                                ErrorMsg.getErrorMsg(msg.obj.toString()));
					}
					break;
				case Constants.HANDLER_ERROR:		
                        ToastUtil.showLongToast(LoginActivity.this,
                                "网络出错");
					break;
			}
		}
	};
	

	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch (id) {
		case R.id.edit_shop_num_clear:
			mShop.setText("");
			mShopClear.setVisibility(View.GONE);
			
			break;
		case R.id.edit_user_name_clear:
			mUserName.setText("");
			mUserNameClear.setVisibility(View.GONE);
			
			break;
		case R.id.edit_pass_clear:
			mPassword.setText("");
			mPasswordClaear.setVisibility(View.GONE);
			
			break;		

		default:
			break;
		}
		
	}
	
		//解析json数据
	protected void parserJson(String result) throws JSONException {
		mResultData=new LoginResult(result);	
		//save the login info in application
		//RetailApplication.setLoginResult(mResultData);
		Log.i(TAG," jsessionId =  "+mResultData.getJsessionId());
	}
	
}
