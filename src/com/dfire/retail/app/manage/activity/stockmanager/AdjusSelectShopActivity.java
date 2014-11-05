package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.stockmanager.StockAdjustmentActivity;
import com.dfire.retail.app.manage.adapter.SelectShopAdapter;
import com.dfire.retail.app.manage.data.AllShopVo;
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
 * 选择类别第一类  全体门店（type  =  2）
 * @author ys
 *
 */
public class AdjusSelectShopActivity extends TitleActivity implements OnItemClickListener{

	private ListView selectshoplist;
	
	private int currentPage = 1;
	
	private  ArrayList<AllShopVo> allShops;
	
	private ProgressDialog progressDialog;
	
	private String selectShopId;
	
	private String activity;
	
	private boolean nodata;
	
	private boolean is_divPage;// 是否进行分页操作
	
	private Integer pageSize = 0;
	
	private SelectShopAdapter selectShopAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_shop);
		setTitleText("选择门店");
		showBackbtn();
		this.init();
		
	}
	private void init(){
		activity = getIntent().getExtras().getString("activity");
		selectShopId = getIntent().getExtras().getString("selectShopId");
		allShops = new ArrayList<AllShopVo>();//店铺
		
		selectshoplist = (ListView) findViewById(R.id.selectshoplist);
		selectShopAdapter = new SelectShopAdapter(AdjusSelectShopActivity.this, allShops,selectShopId);
		selectshoplist.setAdapter(selectShopAdapter);
		selectshoplist.setOnItemClickListener(AdjusSelectShopActivity.this);
		
		selectshoplist.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				 if (is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (nodata) {
						currentPage++;
						getShop();
					}
				 }
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});
		
		progressDialog = new ProgressDialog(AdjusSelectShopActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		
		this.getShop();
	}
	/**
	 * 获取店铺列表
	 * @param getShop
	 */
	private void getShop() {
		nodata = false;
		progressDialog.show();
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SHOPALLSHOPLIST);
		params.setParam(Constants.SHOP_ID, RetailApplication.getShopVo().getShopId());
		params.setParam(Constants.PAGE, currentPage ++);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null ||!returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(AdjusSelectShopActivity.this, "获取失败");
					return;
				}
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(AdjusSelectShopActivity.this)) {
					return;
				}
				pageSize = (Integer)ju.get(Constants.PAGE_SIZE,Integer.class);
				List<AllShopVo> allShopVos = new ArrayList<AllShopVo>();
				allShopVos = (List<AllShopVo>) ju.get(Constants.ALL_SHOP_LIST,new TypeToken<List<AllShopVo>>(){}.getType());
				if (allShopVos != null && allShopVos.size() > 0) {
					if (currentPage<=pageSize){
						nodata = true;
					}
					allShops.addAll(allShopVos);
				}
				selectShopAdapter.notifyDataSetChanged();
			}
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(AdjusSelectShopActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		Intent select = null;
		if (activity.equals("stockAdjustmentActivity")) {
			select = new Intent(AdjusSelectShopActivity.this,StockAdjustmentActivity.class);
		} 
		Bundle bundle=new Bundle();
		bundle.putSerializable("shopVo", allShops.get(arg2));
		select.putExtras(bundle);
		setResult(100, select);
		finish();
	
	}
}
