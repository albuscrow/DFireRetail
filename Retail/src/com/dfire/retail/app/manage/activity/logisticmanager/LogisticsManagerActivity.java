package com.dfire.retail.app.manage.activity.logisticmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.global.ConfigConstants;

/**
 * 物流管理
 * 
 * 
 */
public class LogisticsManagerActivity extends TitleActivity implements OnClickListener {

	private LinearLayout store_order_layout;
	private LinearLayout store_collect_layout;
	private LinearLayout store_return_goods_layout;
	private LinearLayout store_allocation;
	private LinearLayout logistics_records_check_layout;
	private LinearLayout supplier_information_management_layout;
	private View view1,view2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logistics_manager);
		setTitleText("物流管理");
		showBackbtn();
		findView();
		initPermissions();
	}

	public void findView() {
		store_order_layout = (LinearLayout) findViewById(R.id.store_order_layout);
		store_order_layout.setOnClickListener(this);
		store_collect_layout = (LinearLayout) findViewById(R.id.store_collect_layout);
		store_collect_layout.setOnClickListener(this);

		store_return_goods_layout = (LinearLayout) findViewById(R.id.store_return_goods_layout);
		store_return_goods_layout.setOnClickListener(this);
		store_allocation = (LinearLayout) findViewById(R.id.store_allocation);
		store_allocation.setOnClickListener(this);
		logistics_records_check_layout = (LinearLayout) findViewById(R.id.logistics_records_check_layout);
		logistics_records_check_layout.setOnClickListener(this);
		supplier_information_management_layout = (LinearLayout) findViewById(R.id.supplier_information_management_layout);
		supplier_information_management_layout.setOnClickListener(this);
		view1 = (View) findViewById(R.id.view1);
		view2 = (View) findViewById(R.id.view2);
		
		if (RetailApplication.getEntityModel()==1) {
			store_order_layout.setVisibility(View.GONE);
			store_allocation.setVisibility(View.GONE);
			view1.setVisibility(View.GONE);
			view2.setVisibility(View.GONE);
		}
	}
	/**
     * 加载是否有权限。 根据此方法显示又锁无锁标志  下面onclick 方法会做提示并且没有点击效果
     * ConfigConstants 里面定义的有每个模块的常量名
     */
    private void initPermissions() {
        if(!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_ORDER)) {
        	store_order_layout.findViewById(R.id.store_order_lock).setVisibility(View.VISIBLE);
        }
        if (!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_IN)) {
        	store_collect_layout.findViewById(R.id.store_collect_lock).setVisibility(View.VISIBLE);
		}
        if (!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_RETURN)) {
        	store_return_goods_layout.findViewById(R.id.store_return_goods_lock).setVisibility(View.VISIBLE);
		}
        if (!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_ALLOCATE)){
        	store_allocation.findViewById(R.id.store_allocation_lock).setVisibility(View.VISIBLE);
		}
        if (!CommonUtils.getPermission(ConfigConstants.ACTION_MATERIAL_FLOW)) {
        	logistics_records_check_layout.findViewById(R.id.logistics_records_check_lock).setVisibility(View.VISIBLE);
		}
        if (!CommonUtils.getPermission(ConfigConstants.ACTION_SUPPLIER_MANAGE)) {
        	supplier_information_management_layout.findViewById(R.id.supplier_information_management_lock).setVisibility(View.VISIBLE);
		}
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 门店订货
		case R.id.store_order_layout:
			if(!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_ORDER)) {
		        new ErrDialog(this, this.getString(R.string.MC_MSG_000005)).show();
	            return;
	        } 
			Intent order = new Intent(LogisticsManagerActivity.this, StoreOrderActivity.class);
			startActivity(order);
			break;
		//门店进货
		case R.id.store_collect_layout:
			if (!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_IN)) {
				new ErrDialog(this, this.getString(R.string.MC_MSG_000005)).show();
				return;
			}
			Intent collect = new Intent(LogisticsManagerActivity.this, StoreCollectActivity.class);
			startActivity(collect);
			break;
		// 门店退货
		case R.id.store_return_goods_layout:
			if (!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_RETURN)) {
				new ErrDialog(this, this.getString(R.string.MC_MSG_000005)).show();
				return;
			}
			Intent returnGoods = new Intent(LogisticsManagerActivity.this, StoreReturnGoodsActivity.class);
			startActivity(returnGoods);
			break;
		// 门店调拨
		case R.id.store_allocation:
			if (!CommonUtils.getPermission(ConfigConstants.ACTION_STOCK_ALLOCATE)){
				new ErrDialog(this, this.getString(R.string.MC_MSG_000005)).show();
				return;
			}
			Intent allocation = new Intent(LogisticsManagerActivity.this, StoreAllocationActivity.class);
			startActivity(allocation);
			break;
		// 物流记录查询
		case R.id.logistics_records_check_layout:
			if (!CommonUtils.getPermission(ConfigConstants.ACTION_MATERIAL_FLOW)) {
				new ErrDialog(this, this.getString(R.string.MC_MSG_000005)).show();
				return;
			}
			Intent record = new Intent(LogisticsManagerActivity.this, LogisticsRecordsCheckActivity.class);
			startActivity(record);
			break;
		// 供应商信息管理
		case R.id.supplier_information_management_layout:
			if (!CommonUtils.getPermission(ConfigConstants.ACTION_SUPPLIER_MANAGE)) {
				new ErrDialog(this, this.getString(R.string.MC_MSG_000005)).show();
				return;
			}
			Intent supplier = new Intent(LogisticsManagerActivity.this, SupplierManagementActivity.class);
			startActivity(supplier);
			break;
		}

	}

}
