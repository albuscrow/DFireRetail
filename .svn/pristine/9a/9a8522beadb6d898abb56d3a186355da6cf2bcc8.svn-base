package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 盘点记录导出
 * @author ys
 *
 */
public class StockExportActivity extends TitleActivity implements OnClickListener {
	
	private EditText email;
	
	private Button sendEmail;
	
	private String shopId;
	
	private Long sendEndTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_export);
		setTitleText("盘点记录导出");
		showBackbtn();
		this.findView();
	}
	
	private void findView(){
		shopId = getIntent().getExtras().getString("shopId");
		if (getIntent().getExtras().getLong("sendTime")==0) {
			sendEndTime = null;
		}else {
			sendEndTime = getIntent().getExtras().getLong("sendTime");
		}
		this.email = (EditText) findViewById(R.id.email);
		this.sendEmail = (Button) findViewById(R.id.send_email);
		this.sendEmail.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.send_email:
			if (checkEmail()) {
				sendEmail();
			}
			break;
		}
	}
	/**
	 * check email
	 */
	private boolean checkEmail(){
		/**判断邮箱是否格式正确*/
		if (!email.getText().toString().equals("")&&email.getText().toString()!=null) {
			Pattern pattern = Pattern.compile("\\w{1,}[@][\\w\\-]{1,}([.]([\\w\\-]{1,})){1,3}$");
			Matcher matcher = pattern.matcher(email.getText().toString());
			if (matcher.matches()) {
				return true;
			}else {
				ToastUtil.showShortToast(StockExportActivity.this, "邮箱格式不正确！");
				return false;
			}
		}else {
			ToastUtil.showShortToast(StockExportActivity.this, "请输入你要发送的邮箱地址！");
			return false;
		}
	}

	/**
	 * 发送报表到邮箱
	 */
	private void sendEmail(){
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "checkStockRecord/exportExcel");
		params.setParam("shopId", shopId);
		params.setParam("starttime", sendEndTime);
		params.setParam("email", email.getText().toString());
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get("returnCode");
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}

				if (returnCode == null || !returnCode.equals("success")) {
					ToastUtil.showShortToast(StockExportActivity.this, "导出失败");
					return;
				}else {
					JsonUtil ju = new JsonUtil(str);
					if (ju.isError(StockExportActivity.this)) {
						return;
					}
					ToastUtil.showShortToast(StockExportActivity.this, "导出成功，请查看邮箱！");
				}
				
			}
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(StockExportActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}
}
