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
import com.dfire.retail.app.manage.data.StockInVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 物流管理-门店进货
 * @author wangpeng
 *
 */
public class StoreCollectActivity extends TitleActivity implements OnClickListener{
	ImageButton add;
	private ListView store_collect_lv;
	private TextView store_collect_time;
	ProgressDialog progressDialog;
	private int currentPage = 1;
	StockInVo stockInVo;
	List<StockInVo> stockInList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_collect);
		setTitleText("门店进货");
		showBackbtn();
		findView();
	}
	
	public void findView(){
		store_collect_lv=(ListView)findViewById(R.id.store_collect_lv);
		store_collect_time=(TextView)findViewById(R.id.store_collect_time);
		store_collect_time.setOnClickListener(this);
		add=(ImageButton)findViewById(R.id.add);
		add.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.store_collect_time:
			getStoreCollect();
			
			break;

		case R.id.add:
			Intent orderAdd=new Intent(StoreCollectActivity.this,StoreCollectAddActivity.class);
			startActivity(orderAdd);
			break;
		}
	}
	
	private void getStoreCollect() {
		progressDialog = new ProgressDialog(StoreCollectActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		progressDialog.show();
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "purchase/list");
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
					ToastUtil.showShortToast(StoreCollectActivity.this, "获取失败");
					return;
				}
				stockInList=new ArrayList<StockInVo>();
//				shops.add(currentShop);
//				shops.addAll((Collection<? extends ShopVo>) new Gson().fromJson(jo.get("shopList"), new TypeToken<List<ShopVo>>(){}.getType()));

			}
			
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StoreCollectActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}
	

}