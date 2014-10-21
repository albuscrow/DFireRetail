package com.dfire.retail.app.manage.activity.logisticmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dfire.retail.app.manage.R;

/**
 * 物流管理-门店进货
 * @author wangpeng
 *
 */
public class StoreCollectAddActivity extends Activity implements OnClickListener{

	ImageButton title_left;
	ImageButton title_right;
	RelativeLayout add_layout;
	ListView store_collect_add_lv;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_collect_add);
		findView();
	}
	
	public void findView(){
		title_left=(ImageButton)findViewById(R.id.title_left);
		title_left.setOnClickListener(this);
		title_right=(ImageButton)findViewById(R.id.title_right);
		title_right.setOnClickListener(this);
		add_layout=(RelativeLayout)findViewById(R.id.add_layout);
		add_layout.setOnClickListener(this);
		store_collect_add_lv=(ListView)findViewById(R.id.store_collect_add_lv);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
 
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_left:
			
			break;

		case R.id.title_right:
			
			break;
			
		case R.id.add_layout:
			Intent addGoods=new Intent(StoreCollectAddActivity.this,StoreOrderAddGoodsActivity.class);
			addGoods.putExtra("add", 2);
			startActivity(addGoods);
			break;
		}
	}

}
