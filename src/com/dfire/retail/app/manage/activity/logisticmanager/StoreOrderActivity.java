package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.OrderGoodsVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 物流管理-门店订货
 * @author wangpeng
 *
 */
public class StoreOrderActivity extends TitleActivity implements OnClickListener{
	ImageButton add;
	private ListView store_order_lv;
	private TextView store_order_time;
	private int currentPage = 1;
	
	List<OrderGoodsVo> orderGoods;
	OrderGoodsVo orderGoodsVo;
	
	ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_order);
		setTitleText("门店订货");
		showBackbtn();
		findView();
	}
	
	public void findView(){
		store_order_lv=(ListView)findViewById(R.id.store_order_lv);
		store_order_time=(TextView)findViewById(R.id.store_order_time);
		store_order_time.setOnClickListener(this);
		add=(ImageButton)findViewById(R.id.add);
		add.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.store_order_time:
			getStoreOrderGoods();
			break;

		case R.id.add:
			Intent orderAdd=new Intent(StoreOrderActivity.this,StoreOrderAddActivity.class);
			startActivity(orderAdd);
			break;
		}
	}
	
	private void getStoreOrderGoods() {
		progressDialog = new ProgressDialog(StoreOrderActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		progressDialog.show();
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "orderGoods/list");
		params.setParam("shopId", RetailApplication.getmShopInfo().getShopId());
		params.setParam("currentPage", currentPage);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				System.out.println(">>>>>>>"+str);
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get("returnCode");
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null ||!returnCode.equals("success")) {
					ToastUtil.showShortToast(StoreOrderActivity.this, "获取失败");
					return;
				}
				orderGoods=new ArrayList<OrderGoodsVo>();
//				shops.add(currentShop);
//				shops.addAll((Collection<? extends ShopVo>) new Gson().fromJson(jo.get("shopList"), new TypeToken<List<ShopVo>>(){}.getType()));
			}
			
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StoreOrderActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}
	
	private void getOrderGoods() {
		progressDialog = new ProgressDialog(StoreOrderActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		progressDialog.show();
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "orderGoods/list");
		params.setParam("shopId", RetailApplication.getmShopInfo().getShopId());
		params.setParam("currentPage", currentPage);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				System.out.println(">>>>>>>"+str);
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get("returnCode");
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null ||!returnCode.equals("success")) {
					ToastUtil.showShortToast(StoreOrderActivity.this, "获取失败");
					return;
				}
				orderGoods=new ArrayList<OrderGoodsVo>();
//				shops.add(currentShop);
//				shops.addAll((Collection<? extends ShopVo>) new Gson().fromJson(jo.get("shopList"), new TypeToken<List<ShopVo>>(){}.getType()));
			}
			
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StoreOrderActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}

}