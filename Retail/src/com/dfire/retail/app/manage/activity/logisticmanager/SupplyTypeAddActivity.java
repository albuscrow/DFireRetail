package com.dfire.retail.app.manage.activity.logisticmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;

import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.bo.SupplyTypeAddBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
/**
 * 物流管理-添加供应商类型
 * 
 */
@SuppressLint("SimpleDateFormat")
public class SupplyTypeAddActivity extends TitleActivity implements OnClickListener{
	
	private ItemEditText typeName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supply_type_add);
		showBackbtn();
		setTitleText("类别");
		findView();
		
	}
	
	public void findView(){
		typeName=(ItemEditText)findViewById(R.id.type_name);
		typeName.initLabel("类别名称", null, Boolean.TRUE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		typeName.setIsChangeListener(this.getItemChangeListener());
		typeName.setMaxLength(50);
		
		this.mRight.setOnClickListener(this);
	}

	/**
	 * 回调返回 数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);	
	}
 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_right:
				saveSupplyManager();
				break;
			default:
				break;
		}
	}
	
	private void saveSupplyManager() {
		if (typeName.getCurrVal()!=null&&!typeName.getCurrVal().equals("")) {
			RequestParameter parameters = new RequestParameter(true);
			parameters.setUrl(Constants.ADD_SUPPLY_TYPE);
			parameters.setParam("typeName", typeName.getCurrVal());
			parameters.setParam("opUserId", RetailApplication.getmUserInfo().getUserId());
			new AsyncHttpPost(this, parameters, SupplyTypeAddBo.class, new RequestCallback() {
				@Override
				public void onSuccess(Object oj) {
					SupplyTypeAddBo bo = (SupplyTypeAddBo)oj;
					if (bo!=null) {
						finish();
					}
				}
				@Override
				public void onFail(Exception e) {
					// TODO Auto-generated method stub
				}
			}).execute();
		}else {
			new ErrDialog(this, getResources().getString(R.string.please_print)).show();
		}
	}
}
