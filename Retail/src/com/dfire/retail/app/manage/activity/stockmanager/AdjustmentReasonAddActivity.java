package com.dfire.retail.app.manage.activity.stockmanager;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;

import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.bo.AdjustReasonSaveBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
/**
 * 添加库存调整原因
 * @author ys
 *
 */
public class AdjustmentReasonAddActivity extends TitleActivity implements OnClickListener{

	private ItemEditText typeName;
	private String adjustReason;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adjustment_reason_add);
		showBackbtn();
		setTitleText("调整原因");
		this.findView();
	}
	private void findView(){
		this.typeName = (ItemEditText)findViewById(R.id.addType);
		
		this.typeName.initLabel("调整原因名称", null, Boolean.TRUE,InputType.TYPE_CLASS_TEXT);
		this.typeName.setIsChangeListener(this.getItemChangeListener());
		this.typeName.setMaxLength(50);
		this.mRight.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right:
			if (typeName.getStrVal()!=null) {
				adjustReason = typeName.getStrVal();
				addAdjustReason();
			}else {
				new ErrDialog(this, getResources().getString(R.string.please_print_reason)).show();
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 添加调整原因
	 */
	private void addAdjustReason(){
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockAdjust/addAdjustReason");
		params.setParam("adjustReason", adjustReason);
		new AsyncHttpPost(this, params, AdjustReasonSaveBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				AdjustReasonSaveBo bo = (AdjustReasonSaveBo)oj;
				if (bo!=null) {
					AdjustmentReasonAddActivity.this.finish();
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}
}
