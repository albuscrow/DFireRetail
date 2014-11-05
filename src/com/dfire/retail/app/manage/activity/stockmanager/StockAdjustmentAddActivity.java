package com.dfire.retail.app.manage.activity.stockmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;

/**
 * 库存管理  添加
 * @author ys
 *
 */
public class StockAdjustmentAddActivity extends TitleActivity implements OnClickListener{

	private TextView shopname_tx,name_tx;
	
	private RelativeLayout add_layout;
	
	private ProgressDialog progressDialog;
	
	private String shopId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_anjustment_add);
		setTitleText("库存调整");
		showBackbtn();
		findView();
	}
	/**
	 * 初始化控件
	 */
	public void findView(){
		shopId = getIntent().getStringExtra("shopId");
		add_layout=(RelativeLayout)findViewById(R.id.add_layout);
		shopname_tx=(TextView)findViewById(R.id.shopname_tx);
		name_tx=(TextView)findViewById(R.id.name_tx);
		
		add_layout.setOnClickListener(this);
		progressDialog = new ProgressDialog(StockAdjustmentAddActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		 
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	/**
	 * 回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	 System.out.println("sssssssssssssssssssssssss");
	}
 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_layout:
			Intent addAdjustment = new Intent(StockAdjustmentAddActivity.this,StockAddGoodsActivity.class);
			addAdjustment.putExtra("flag", "returnAdjustmentAdd");
			addAdjustment.putExtra("shopId", shopId);
			startActivityForResult(addAdjustment, 100);
			break;
		case R.id.supplyName_tx:
			break;
		}
	}
}
