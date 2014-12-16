/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;

import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.bo.ReasonAddBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;

/**
 * @author ys
 */
public class StoreReturnGoodsReasonAddActivity extends TitleActivity{

	private ItemEditText lblName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_return_reason_add);
		setTitleText("退货原因");
		showBackbtn();
		initView();
	}

	private void initView() {
		this.lblName = (ItemEditText) this.findViewById(R.id.lblName1);
		this.lblName.initLabel("退货原因名称", null, Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		this.lblName.setMaxLength(50);
		this.lblName.setIsChangeListener(this.getItemChangeListener());
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
		if (lblName.getStrVal()!=null&&!lblName.getStrVal().equals("")) {
			RequestParameter parameters = new RequestParameter(true);
			parameters.setUrl(Constants.RETURNGOODS_REASON_ADD);
			parameters.setParam("resonName", lblName.getStrVal());
			new AsyncHttpPost(this, parameters, ReasonAddBo.class, new RequestCallback() {
				
				@Override
				public void onSuccess(Object oj) {
					ReasonAddBo bo = (ReasonAddBo)oj;
					if (bo!=null) {
						StoreReturnGoodsReasonAddActivity.this.finish();				
					}
				}
				@Override
				public void onFail(Exception e) {
				}
			}).execute();
		}else {
			new ErrDialog(this, getResources().getString(R.string.please_print_return_goods_reason)).show();
		}
	}
}
