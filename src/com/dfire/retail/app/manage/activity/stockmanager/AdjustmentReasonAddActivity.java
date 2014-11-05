package com.dfire.retail.app.manage.activity.stockmanager;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AdjustmentReasonAddActivity extends Activity implements OnClickListener{

	private ItemEditText typeName;
	
	private ImageView save,cancel;
	
	private String adjustReason;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adjustment_reason_add);
		this.findView();
	}
	
	private void findView(){
		this.typeName = (ItemEditText)findViewById(R.id.addType);
		this.save = (ImageView)findViewById(R.id.save);
		this.cancel = (ImageView)findViewById(R.id.cancel);
		
		this.typeName.initLabel("调整原因名称", null, Boolean.TRUE,InputType.TYPE_CLASS_TEXT);
		
		this.save.setOnClickListener(this);
		this.cancel.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v==cancel) {
			finish();
		}else if(v==save){
			if (!typeName.getStrVal().equals("")) {
				adjustReason = typeName.getStrVal();
				addAdjustReason();
			}else {
				ToastUtil.showShortToast(AdjustmentReasonAddActivity.this, "请输入要添加的调整原因！");
			}
		}
	}
	
	/**
	 * 添加调整原因
	 */
	private void addAdjustReason(){
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockAdjust/addAdjustReason");
		params.setParam("adjustReason", adjustReason);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get("returnCode");
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				JsonUtil ju = new JsonUtil(str);
				if (returnCode == null ||!returnCode.equals("success")) {
					if (ju.isError(AdjustmentReasonAddActivity.this)) {
						return;
					}
				}else {
					AdjustmentReasonAddActivity.this.finish();
				}
			}
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(AdjustmentReasonAddActivity.this, "获取失败2");
				e.printStackTrace();
			}
		}).execute();
	}

}
