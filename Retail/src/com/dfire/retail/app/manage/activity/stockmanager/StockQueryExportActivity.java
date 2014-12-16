package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.bo.ReturnNotMsgBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;

/**
 * 库存查询导出
 * @author ys
 *
 */
public class StockQueryExportActivity extends TitleActivity implements OnClickListener {
	
	private EditText email;
	
	private Button sendEmail;
	
	private String shopId;
	
	private ImageView clear_input;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_export);
		setTitleText("库存查询导出");
		showBackbtn();
		this.findView();
	}
	
	private void findView(){
		shopId = getIntent().getExtras().getString("shopId");
		this.email = (EditText) findViewById(R.id.email);
		this.sendEmail = (Button) findViewById(R.id.send_email);
		this.sendEmail.setOnClickListener(this);
		this.clear_input = (ImageView) findViewById(R.id.clear_input);
		this.clear_input.setOnClickListener(this);
		this.email.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s!=null&&!s.toString().equals("")) {
					clear_input.setVisibility(View.VISIBLE);
				}else{
					clear_input.setVisibility(View.GONE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.send_email:
			if (checkEmail()) {
				sendEmail();
			}
			break;
		case R.id.clear_input:
			this.email.setText("");
			this.clear_input.setVisibility(View.GONE);
			break;
		}
	}
	/**
	 * check email
	 */
	private boolean checkEmail(){
		/**判断邮箱是否格式正确*/
		if (email.getText().toString()!=null&&!email.getText().toString().equals("")) {
			Pattern pattern = Pattern.compile("\\w{1,}[@][\\w\\-]{1,}([.]([\\w\\-]{1,})){1,3}$");
			Matcher matcher = pattern.matcher(email.getText().toString());
			if (matcher.matches()) {
				return true;
			}else {
				new ErrDialog(this, getResources().getString(R.string.supply_email_err)).show();
				return false;
			}
		}else {
			new ErrDialog(this, getResources().getString(R.string.please_print_send_email)).show();
			return false;
		}
	}

	/**
	 * 发送报表到邮箱
	 */
	private void sendEmail(){
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockInfo/exportExcel");
		params.setParam("shopId", shopId);
		params.setParam("email", email.getText().toString());
		new AsyncHttpPost(this, params, ReturnNotMsgBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				ReturnNotMsgBo bo = (ReturnNotMsgBo)oj;
				if (bo!=null) {
					new ErrDialog(StockQueryExportActivity.this, getResources().getString(R.string.export_success),1).show();
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}
}
