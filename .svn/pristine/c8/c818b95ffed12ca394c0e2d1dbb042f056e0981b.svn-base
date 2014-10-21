package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.adapter.StoreOrderAddAdapter;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.data.StockInDetailVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;

/**
 * 物流管理-添加门店订货
 * @author wangpeng
 *
 */
public class StoreOrderAddActivity extends Activity implements OnClickListener{

	ProgressDialog progressDialog;
	ImageButton title_left;
	ImageButton title_right;
	RelativeLayout add_layout;
	ListView store_order_add_lv;
	SearchGoodsVo searchGoodsVo;
	StockInDetailVo stockInDetailVo;
	List<StockInDetailVo> stockInDetailVoList=new ArrayList<StockInDetailVo>();
	StoreOrderAddAdapter addGoodsAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_order_add);
		findView();
	}
	
	public void findView(){
		title_left=(ImageButton)findViewById(R.id.title_left);
		title_left.setOnClickListener(this);
		title_right=(ImageButton)findViewById(R.id.title_right);
		title_right.setOnClickListener(this);
		add_layout=(RelativeLayout)findViewById(R.id.add_layout);
		add_layout.setOnClickListener(this);
		store_order_add_lv=(ListView)findViewById(R.id.store_order_add_lv);
		addGoodsAdapter=new StoreOrderAddAdapter(StoreOrderAddActivity.this, stockInDetailVoList);
		store_order_add_lv.setAdapter(addGoodsAdapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println(">>"+requestCode+">>"+resultCode);
		if(resultCode==100){
			Bundle b=data.getExtras();
			searchGoodsVo=(SearchGoodsVo)b.getSerializable("orderAdd");
			System.out.println(">>>"+searchGoodsVo.toString()); 
			stockInDetailVo=new StockInDetailVo();
			stockInDetailVo.setGoodsId(searchGoodsVo.getGoodsId());
			stockInDetailVo.setGoodsName(searchGoodsVo.getGoodsName());
			stockInDetailVo.setGoodsPrice(searchGoodsVo.getPurchasePrice());
			stockInDetailVo.setOperateType("add");
			stockInDetailVoList.add(stockInDetailVo);
			addGoodsAdapter.notifyDataSetChanged();
		}
	}
 
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_left:
			
			break;

		case R.id.title_right:
			
			getResult();
			
			break;
			
		case R.id.add_layout:
			Intent addGoods=new Intent(StoreOrderAddActivity.this,StoreOrderAddGoodsActivity.class);
			addGoods.putExtra("add", 1);
			startActivityForResult(addGoods, 100);
			break;
		}
	}
	
	private void getResult() {
		progressDialog = new ProgressDialog(StoreOrderAddActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		progressDialog.show();
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "orderGoods/save");
		params.setParam("shopId", RetailApplication.getmShopInfo().getShopId());
//		params.setParam("orderGoodsId", "");
		Date date=new Date();
		System.out.println("date"+date.getTime());
		params.setParam("sendEndTime", "");
		params.setParam("operateType", "add");
		params.setParam("orderGoodsList", stockInDetailVoList);
		params.setParam("lastVer", "1");
//		orderGoodsId				String				否		
//		sendEndTime				Date				是		
//		shopId				String				是		
//		operateType				string				是		
//		orderGoodsList				List<StockInDetailVo>				是		
//		lastVer				String				否		

		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				System.out.println(">>>>>>>"+str);
			}
			
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StoreOrderAddActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}

}
