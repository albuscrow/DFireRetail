package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.StoreCollectAdapter;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.StockInVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 物流管理-门店进货
 * @author ys
 *
 */
public class StoreCollectActivity extends TitleActivity implements OnClickListener,OnItemClickListener{
	private ImageButton add;
	private ListView store_collect_lv;
	private TextView store_collect_time;
	private ProgressDialog progressDialog;
	private int currentPage = 1;
	private StockInVo stockInVo;
	private List<StockInVo> stockInList;
	private TextView shopTextView;
	private ShopVo currentShop;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_collect);
		setTitleText("门店进货");
		showBackbtn();
		findView();
	}
	
	public void findView(){
		stockInList=new ArrayList<StockInVo>();
		store_collect_lv=(ListView)findViewById(R.id.store_collect_lv);
		store_collect_time=(TextView)findViewById(R.id.store_collect_time);
		shopTextView = (TextView) findViewById(R.id.shopName);
		currentShop = RetailApplication.getShopVo();
		store_collect_time.setOnClickListener(this);
		add=(ImageButton)findViewById(R.id.minus);
		add.setOnClickListener(this);
		if (RetailApplication.getEntityModel()==1) {
			//单店
			shopTextView.setCompoundDrawables(null, null, null, null);
			add.setVisibility(View.VISIBLE);
		}else {
			if (currentShop.getType() == ShopVo.DANDIAN || currentShop.getType() == ShopVo.MENDIAN) {
				shopTextView.setCompoundDrawables(null, null, null, null);
				add.setVisibility(View.VISIBLE);
			}else{
				shopTextView.setOnClickListener(this);
				add.setVisibility(View.GONE);
			}
		}
		shopTextView.setText(currentShop.getShortName());
		
		getStoreCollect();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.store_collect_time:
			break;

		case R.id.minus:
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
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
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
				
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(StoreCollectActivity.this)) {
					return;
				}
				List<StockInVo> stockInVos = new ArrayList<StockInVo>();
				stockInVos = (List<StockInVo>) ju.get(Constants.STOCK_IN_LIST, new TypeToken<List<StockInVo>>(){}.getType());
				if (stockInVos.size()>0) {
					stockInList.clear();
					stockInList.addAll(stockInVos);
				}
				store_collect_lv.setAdapter(new StoreCollectAdapter(StoreCollectActivity.this, stockInList));
				store_collect_lv.setOnItemClickListener(StoreCollectActivity.this);

			}
			
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StoreCollectActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}
	

}