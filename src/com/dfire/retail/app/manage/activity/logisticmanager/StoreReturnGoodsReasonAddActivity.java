/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;

import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;

/**
 * @author 李锦运 2014-10-23
 */
public class StoreReturnGoodsReasonAddActivity extends TitleActivity implements OnClickListener {

	private ItemEditText lblName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_return_reason_add);
		setTitleText("退货原因");

		change2saveMode();
		initView();
	}

	private void initView() {
		this.lblName = (ItemEditText) this.findViewById(R.id.lblName1);
		this.lblName.initLabel("退货原因名称", null, Boolean.TRUE, InputType.TYPE_CLASS_TEXT);

		mRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveReason();
			}
		});
		
		mLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void saveReason() {

		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.RETURNGOODS_REASON_ADD);
		parameters.setParam("resonName", lblName.getStrVal());

		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				Intent returnReasonView = new Intent(StoreReturnGoodsReasonAddActivity.this, StoreReturnGoodsReasonActivity.class);
				startActivity(returnReasonView);
				finish();
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.add) {

		}
	}

}