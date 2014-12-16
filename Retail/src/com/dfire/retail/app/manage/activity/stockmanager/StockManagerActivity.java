package com.dfire.retail.app.manage.activity.stockmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.global.ConfigConstants;


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
	private LinearLayout stock_list_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_manager);
		setTitleText("库存管理");
		showBackbtn();
		findView();
		
		initPermissions();
	}
    public void findView(){
		stock_query_layout=(LinearLayout)findViewById(R.id.stock_query_layout);
		stock_query_layout.setOnClickListener(this);
		stock_check_layout=(LinearLayout)findViewById(R.id.stock_check_layout);
		stock_check_layout.setOnClickListener(this);
		stock_list_layout=(LinearLayout)findViewById(R.id.stock_list_layout);
		stock_list_layout.setOnClickListener(this);
		stock_check_records_layout=(LinearLayout)findViewById(R.id.stock_check_records_layout);
		stock_check_records_layout.setOnClickListener(this);
		stock_remind_setting_layout=(LinearLayout)findViewById(R.id.stock_remind_setting_layout);
		stock_remind_setting_layout.setOnClickListener(this);
		stock_regulation_layout=(LinearLayout)findViewById(R.id.stock_regulation_layout);
		stock_regulation_layout.setOnClickListener(this);
		
		
	}
    /**
     * 加载是否有权限。 根据此方法显示又锁无锁标志  下面onclick 方法会做提示并且没有点击效果
     * ConfigConstants 里面定义的有每个模块的常量名
     */
    private void initPermissions() {
        if(!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_CHECK)) {
            stock_list_layout.findViewById(R.id.lock).setVisibility(View.VISIBLE);
        }
        if (!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_SEARCH)) {
        	stock_query_layout.findViewById(R.id.stock_query_lock).setVisibility(View.VISIBLE);
		}
        if (!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_CHECK_SEARCH)) {
			stock_check_records_layout.findViewById(R.id.stock_check_records_lock).setVisibility(View.VISIBLE);
		}
        if (!CommonUtils.getPermission(ConfigConstants.ACTION_REMIND_SETTING)){
			stock_remind_setting_layout.findViewById(R.id.stock_remind_setting_lock).setVisibility(View.VISIBLE);
		}
        if (!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_ADJUST)) {
			stock_regulation_layout.findViewById(R.id.stock_regulation_lock).setVisibility(View.VISIBLE);
		}
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stock_query_layout:
			if(!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_SEARCH)) {
		        new ErrDialog(this, this.getString(R.string.MC_MSG_000005)).show();
	            return;
	        }
			Intent query=new Intent(StockManagerActivity.this,StockQueryActivity.class);
			startActivity(query);
			break;
		case R.id.stock_check_layout:
		    Intent stockCheck=new Intent(StockManagerActivity.this,StockCheckActivity.class);
            startActivity(stockCheck);
			break;
		case R.id.stock_list_layout:
		    if(!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_CHECK)) {
		        new ErrDialog(this, this.getString(R.string.MC_MSG_000005)).show();
	            return;
	        } 
			Intent stockList=new Intent(StockManagerActivity.this,StockCheckActivity.class);
			startActivity(stockList);
			break;
		case R.id.stock_check_records_layout:
			if(!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_CHECK_SEARCH)) {
		        new ErrDialog(this, this.getString(R.string.MC_MSG_000005)).show();
	            return;
	        }
			Intent check_records=new Intent(StockManagerActivity.this,StockCheckRecordActivity.class);
			startActivity(check_records);
			break;
		case R.id.stock_remind_setting_layout:
			if(!CommonUtils.getPermission(ConfigConstants.ACTION_REMIND_SETTING)) {
		        new ErrDialog(this, this.getString(R.string.MC_MSG_000005)).show();
	            return;
	        }
			Intent remind_setting=new Intent(StockManagerActivity.this,StockRemindSettingGoodsActivity.class);
			startActivity(remind_setting);
			break;
		case R.id.stock_regulation_layout:
			if(!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_ADJUST)) {
		        new ErrDialog(this, this.getString(R.string.MC_MSG_000005)).show();
	            return;
	        }
			Intent stock_Adjustment=new Intent(StockManagerActivity.this,StockAdjustmentActivity.class);
			startActivity(stock_Adjustment);
			break;
		}
		
	}

}
