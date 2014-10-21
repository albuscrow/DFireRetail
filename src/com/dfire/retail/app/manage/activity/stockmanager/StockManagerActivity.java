package com.dfire.retail.app.manage.activity.stockmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;


/**
 * 库存管理
 * @author wangpeng
 *
 */
public class StockManagerActivity extends TitleActivity implements OnClickListener{
	
	private LinearLayout stock_query_layout;
	private LinearLayout stock_check_layout;
	private LinearLayout stock_check_records_layout;
	private LinearLayout stock_remind_setting_layout;
	private LinearLayout stock_regulation_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_manager);
		setTitleText("库存管理");
		showBackbtn();
		findView();
	}
	public void findView(){
		stock_query_layout=(LinearLayout)findViewById(R.id.stock_query_layout);
		stock_query_layout.setOnClickListener(this);
		stock_check_layout=(LinearLayout)findViewById(R.id.stock_check_layout);
		stock_check_layout.setOnClickListener(this);
		stock_check_records_layout=(LinearLayout)findViewById(R.id.stock_check_records_layout);
		stock_check_records_layout.setOnClickListener(this);
		stock_remind_setting_layout=(LinearLayout)findViewById(R.id.stock_remind_setting_layout);
		stock_remind_setting_layout.setOnClickListener(this);
		stock_regulation_layout=(LinearLayout)findViewById(R.id.stock_regulation_layout);
		stock_regulation_layout.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stock_query_layout:
			Intent query=new Intent(StockManagerActivity.this,StockQueryActivity.class);
			startActivity(query);
			break;
		case R.id.stock_check_layout:
			
			break;
		case R.id.stock_check_records_layout:
			Intent check_records=new Intent(StockManagerActivity.this,StockCheckRecordActivity.class);
			startActivity(check_records);
			break;
		case R.id.stock_remind_setting_layout:
			Intent remind_setting=new Intent(StockManagerActivity.this,StockRemindSettingActivity.class);
			startActivity(remind_setting);
			break;
		case R.id.stock_regulation_layout:
			
			break;
		}
		
	}

}
