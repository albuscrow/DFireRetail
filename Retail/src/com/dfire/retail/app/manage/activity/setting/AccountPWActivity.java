package com.dfire.retail.app.manage.activity.setting;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.global.ShopInfo;
import com.dfire.retail.app.manage.global.UserInfo;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.MD5;

public class AccountPWActivity extends TitleActivity implements OnClickListener {
	private final static String TAG="AccountPWActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_account_password);
        
        ShopInfo shopInfo=RetailApplication.getmShopInfo();
        UserInfo userInfo=RetailApplication.getmUserInfo();

        View view = findViewById(R.id.cancel_title);
        view.findViewById(R.id.title_left_btn).setOnClickListener(this);
        view.findViewById(R.id.title_right_btn).setOnClickListener(this);
        TextView text = (TextView) view.findViewById(R.id.titleText);
        text.setText(R.string.password_change);
        
        //原信息从login中拿去
        view = findViewById(R.id.user_name);
        text = (TextView) view.findViewById(R.id.main);
        text.setText(R.string.account_name);
        text = (TextView) view.findViewById(R.id.second);
        text.setText(userInfo.getUserName());
        
        view = findViewById(R.id.shop_number);
        text = (TextView) view.findViewById(R.id.main);
        text.setText(R.string.account_name_number);
        text = (TextView) view.findViewById(R.id.second);
        text.setText(shopInfo.getShopId());
        
        view = findViewById(R.id.current_password);
        text = (TextView) view.findViewById(R.id.main);
        text.setText(R.string.old_password);
        final EditText currentPasswordEdit = (EditText) view.findViewById(R.id.edit);
        currentPasswordEdit.setText("当前密码");
        currentPasswordEdit.setOnFocusChangeListener(new OnFocusChangeListener() {  
        	@Override  
            public void onFocusChange(View v, boolean hasFocus){
        		if(currentPasswordEdit.getText().toString().trim().equals("当前密码")){
        			currentPasswordEdit.setText("");
        		}
        		currentPasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        		if(!hasFocus){
        			if(currentPasswordEdit.getText().toString().trim().equals("当前密码") ||CommonUtils.isEmpty(currentPasswordEdit.getText().toString().trim())){
        				currentPasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        				currentPasswordEdit.setText("当前密码");
        			}
        		}
        	}
        });
        
        view = findViewById(R.id.new_password);
        text = (TextView) view.findViewById(R.id.main);
        text.setText(R.string.new_password);
        final EditText newPasswordEdit = (EditText)view.findViewById(R.id.edit);
        newPasswordEdit.setText("新密码");
        newPasswordEdit.setOnFocusChangeListener(new OnFocusChangeListener() {  
        	@Override  
            public void onFocusChange(View v, boolean hasFocus){
        		if(newPasswordEdit.getText().toString().trim().equals("新密码")){
        			newPasswordEdit.setText("");
        		}
        		newPasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        		if(!hasFocus){
        			if(newPasswordEdit.getText().toString().trim().equals("新密码") || CommonUtils.isEmpty(newPasswordEdit.getText().toString().trim())){
        				newPasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        				newPasswordEdit.setText("新密码");
        			}
        		}
        	}
        });
        
        view = findViewById(R.id.again_password);
        text = (TextView) view.findViewById(R.id.main);
        text.setText(R.string.new_password_again);
        final EditText againPasswordEdit = (EditText) view.findViewById(R.id.edit);
        againPasswordEdit.setText("新密码");
        againPasswordEdit.setOnFocusChangeListener(new OnFocusChangeListener() {  
        	@Override  
            public void onFocusChange(View v, boolean hasFocus){
        		if(againPasswordEdit.getText().toString().trim().equals("新密码")){
        			againPasswordEdit.setText("");
        		}
        		againPasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        		if(!hasFocus){
        			if(againPasswordEdit.getText().toString().trim().equals("新密码") ||CommonUtils.isEmpty(againPasswordEdit.getText().toString().trim())){
        				againPasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        				againPasswordEdit.setText("新密码");
        			}
        		}
        	}
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        	case R.id.title_left_btn:
        		finish();
        		break;

        	case R.id.title_right_btn:{
        		View view = findViewById(R.id.current_password);
        	    EditText currentPasswordEdit = (EditText) view.findViewById(R.id.edit); 
        	    String currentPasswordStr=currentPasswordEdit.getText().toString().trim();
        	    if(currentPasswordStr.equals("当前密码") ||CommonUtils.isEmpty(currentPasswordStr)){
        	    	Toast.makeText(AccountPWActivity.this, "请输当前密码",
							Toast.LENGTH_SHORT).show();
					return;
        	    }
        	    
        	    view = findViewById(R.id.new_password);
                EditText newPasswordEdit = (EditText)view.findViewById(R.id.edit);
                String newPasswordStr=newPasswordEdit.getText().toString().trim();
                if(newPasswordStr.equals("新密码") || CommonUtils.isEmpty(newPasswordStr)){
                	Toast.makeText(AccountPWActivity.this, "请输新密码",
							Toast.LENGTH_SHORT).show();
					return;
                }
        		
                view = findViewById(R.id.again_password);
                EditText againPasswordEdit = (EditText) view.findViewById(R.id.edit);
                String againPasswordStr=againPasswordEdit.getText().toString().trim();
                if(againPasswordStr.equals("新密码") ||CommonUtils.isEmpty(againPasswordStr)){
                	Toast.makeText(AccountPWActivity.this, "请再次输入新密码",
							Toast.LENGTH_SHORT).show();
					return;
                }
                
                if(!againPasswordStr.equals(newPasswordStr)){
                	Toast.makeText(AccountPWActivity.this, "两次输入的新密码不一致",
							Toast.LENGTH_SHORT).show();
					return;
                }
                
                /*显示进度条*/
                getProgressDialog().setCancelable(false);
				getProgressDialog()
						.setMessage(getResources().getText(R.string.in_change_password));
				getProgressDialog().show();
				
				RequestParameter params = new RequestParameter(true);
				params.setUrl(Constants.CHANGE_PASSWORD);
				params.setParam("oldPwd", MD5.GetMD5Code(currentPasswordStr));
				params.setParam("newPwd", MD5.GetMD5Code(newPasswordStr));
				
				
				AsyncHttpPost httppost = new AsyncHttpPost(params,
						new RequestResultCallback() {
					@Override
					public void onSuccess(String str) {
						Log.d(TAG,str);
						Message msg = new Message();
						msg.what = Constants.HANDLER_SUCESS;
						msg.obj = str;
						mChangePwdHandler.sendMessage(msg);
					}
					
					@Override
					public void onFail(Exception e) {
						e.printStackTrace();
		                Message msg = new Message();
						msg.what = Constants.HANDLER_FAIL;
						msg.obj = e.getMessage();
						mChangePwdHandler.sendMessage(msg);
					}
				});
				httppost.execute();
        		break;
        	}
        }
    }
    
    /**
	 * 处理返回的结果，如果成功解析网络返回的json数据
	 */
	Handler mChangePwdHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			getProgressDialog().dismiss();
			
			switch (msg.what) {
				case Constants.HANDLER_SUCESS:
				try {
					if(parserJson(msg.obj.toString())!=0){
						Toast.makeText(AccountPWActivity.this, "密码修改不成功，请使用原密码！",
								Toast.LENGTH_SHORT).show();
						return;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(AccountPWActivity.this, "密码修改成功，请使用新密码！",
						Toast.LENGTH_SHORT).show();
				finish();
				break;
				
				case Constants.HANDLER_FAIL:
					Toast.makeText(AccountPWActivity.this, "密码修改不成功，请使用原密码！",
							Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};
	
	//解析json数据
	protected int parserJson(String result) throws JSONException {
		JSONObject jobj = new JSONObject(result);
		String returnCode = jobj.getString("returnCode");
		if(returnCode.equals(Constants.REPONSE_FAIL))
			return -1;
		return 0;
	}
    
}
