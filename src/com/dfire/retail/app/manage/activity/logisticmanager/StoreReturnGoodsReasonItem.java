/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.DicVo;

/**
 * @author 李锦运 2014-10-23
 */
public class StoreReturnGoodsReasonItem implements OnClickListener {

	private LayoutInflater inflater;

	private StoreReturnGoodsReasonActivity context;

	private View itemView;

	private TextView txt_reason;

	private RelativeLayout layout;

	private String reasontxt;
	
	private View ico_view;

	private String txt;
	
	private int reasonVal;
	public StoreReturnGoodsReasonItem(StoreReturnGoodsReasonActivity context, LayoutInflater inflater,String txt) {
		this.context = context;
		this.inflater = inflater;
		this.txt=txt;
		this.init();
	}

	private void init() {

		this.itemView = inflater.inflate(R.layout.store_return_reason_item, null);
		this.txt_reason = (TextView) itemView.findViewById(R.id.txt_reason);
		this.ico_view=(View) itemView.findViewById(R.id.ico_view);
		
		layout = (RelativeLayout) itemView.findViewById(R.id.layout);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent returngoods = new Intent();
				returngoods.setClass(context, StoreReturnGoodsDetailItemActivity.class);

				Bundle bundle = new Bundle();
				bundle.putString("reason", reasontxt);
				bundle.putInt("reasonVal", reasonVal);
				
				returngoods.putExtras(bundle);

				context.startActivity(returngoods);

			}
		});

	}

	public void initWithAppInfo(DicVo dicVo) {

		txt_reason.setText(dicVo.getName() == null ? null : dicVo.getName());
		reasontxt = dicVo.getName() == null ? null : dicVo.getName();
		reasonVal = dicVo.getVal() == 0 ? null : dicVo.getVal();
		if(dicVo.getName().equals(txt)){
			ico_view.setVisibility(View.VISIBLE);
		}
	}

	public View getItemView() {
		return itemView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
