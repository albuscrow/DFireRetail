package com.dfire.retail.app.manage.activity.logisticmanager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.item.StoreOrderGoodsItem;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
/**
 * 物流管理-添加门店订货
 *
 */
@SuppressLint("SimpleDateFormat")
public class SupplyTypeAddActivity extends TitleActivity implements OnClickListener{

	private ProgressDialog progressDialog;
		
	private LayoutInflater inflater;
	
	private ItemEditText typeName;
		
	private ImageButton title_right;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supply_type_add);
		inflater = LayoutInflater.from(this);
		findView();
		change2saveMode();
	}
	
	public void findView(){
		typeName=(ItemEditText)findViewById(R.id.type_name);
		mRight = (ImageButton) findViewById(R.id.title_right);
		mRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveSupplyManager();
				
			}
		});
		typeName.initLabel("类别", null, Boolean.TRUE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		
		progressDialog = new ProgressDialog(SupplyTypeAddActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		
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
			case R.id.title_left:
				break;
//			case R.id.title_right:
//				saveSupplyManager();
//				break;
		}
	}
	
	private void saveSupplyManager() {
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.ADD_SUPPLY_TYPE);
		parameters.setParam("typeName", typeName.getCurrVal());
		parameters.setParam("opUserId", RetailApplication.getmUserInfo().getUserId());
		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				Intent types = new Intent(SupplyTypeAddActivity.this, SupplyTypeSelectActivity.class);
				types.putExtra("selectState", "SELECT");// 新生订单
				startActivity(types);
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();
	}
	
	/**
	 * 更改每个list里面每项的type
	 */
	public void changeListOperType(StoreOrderGoodsItem goodsItem){
		
	}
	/**
	 * 移除item
	 */
	public void removeView(StoreOrderGoodsItem goodsItem){
	
	}
}
