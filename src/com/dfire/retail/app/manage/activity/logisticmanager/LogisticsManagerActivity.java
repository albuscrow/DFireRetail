package com.dfire.retail.app.manage.activity.logisticmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;


/**
 * 物流管理
 * @author wangpeng
 *
 */
public class LogisticsManagerActivity extends TitleActivity implements OnClickListener{
	
	private LinearLayout store_order_layout;
	private LinearLayout store_collect_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logistics_manager);
		setTitleText("物流管理");
		showBackbtn();
		findView();
	}
	
	public void findView(){
		store_order_layout=(LinearLayout)findViewById(R.id.store_order_layout);
		store_order_layout.setOnClickListener(this);
		store_collect_layout=(LinearLayout)findViewById(R.id.store_collect_layout);
		store_collect_layout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.store_order_layout:
			Intent order=new Intent(LogisticsManagerActivity.this,StoreOrderActivity.class);
			startActivity(order);
			break;
		case R.id.store_collect_layout:
			Intent collect=new Intent(LogisticsManagerActivity.this,StoreCollectActivity.class);
			startActivity(collect);
			break;
		}
		
	}

}
