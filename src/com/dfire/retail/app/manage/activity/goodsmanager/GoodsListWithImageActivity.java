package com.dfire.retail.app.manage.activity.goodsmanager;

import java.util.ArrayList;
import java.util.List;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.CaptureActivity;
import com.dfire.retail.app.manage.adapter.GoodsListWithImageAdapter;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.swipelayout.SwipeRefreshLayout;
import com.dfire.retail.app.manage.widget.swipelayout.SwipeRefreshLayout.OnLoadListener;
import com.dfire.retail.app.manage.widget.swipelayout.SwipeRefreshLayout.OnRefreshListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * The Class GoodsListWithImageActivity.
 * 
 * @author albuscrow
 */
public class GoodsListWithImageActivity extends GoodsManagerBaseActivity implements OnItemClickListener, OnClickListener, OnRefreshListener, OnLoadListener{
	
	/** The goods. */
	private ArrayList<GoodsVo> goods;
	private ShopVo currentShop;
	private GoodsListWithImageAdapter adapter;
	private int currentPage = Constants.PAGE_SIZE_OFFSET;
	private String categoryId;
	private String searchCode;
	private ListView goodsListView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private int totalPage;
	private int status;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_manager_list_with_image);
		setTitleText(Constants.GOODS_TITLE);
		hideRight();
		goods = (ArrayList<GoodsVo>) getIntent().getSerializableExtra(Constants.GOODS);
		currentShop = (ShopVo) getIntent().getSerializableExtra(Constants.SHOP);
		categoryId = getIntent().getStringExtra(Constants.CATEGORY_ID);
		searchCode = getIntent().getStringExtra(Constants.SEARCH_CODE);
		totalPage = getIntent().getIntExtra(Constants.PAGE_SIZE, Constants.INVALID_INT);
		status = getIntent().getIntExtra(Constants.SEARCH_STATUS, Constants.INVALID_INT);
		
		goodsListView = (ListView) findViewById(R.id.goodsList);
		
		adapter = new GoodsListWithImageAdapter(this, goods);
		goodsListView.setAdapter(adapter);
		goodsListView.setOnItemClickListener(this);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.goodsListLayout);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setOnLoadListener(this);
//		swipeRefreshLayout.setColor(android.R.color.holo_blue_bright,
//	                            android.R.color.holo_green_light,
//	                            android.R.color.holo_orange_light,
//	                            android.R.color.holo_red_light);
		swipeRefreshLayout.setLoadNoFull(true);
		
		findViewById(R.id.more).setOnClickListener(this);
		findViewById(R.id.add).setOnClickListener(this);
		findViewById(R.id.scanButton).setOnClickListener(this);
		setBack();
	}
	
	@Override
	protected void onResume() {
		getMoreData(true);
		super.onResume();
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more:
			startActivity(new Intent(this, GoodsListActivity.class)
							.putExtra(Constants.GOODS, goods));
			break;
			
		case R.id.add:
			startActivity(new Intent(GoodsListWithImageActivity.this, GoodsDetailActivity.class).putExtra(Constants.MODE, Constants.ADD));
			break;
			
		case R.id.scanButton:
			startActivityForResult(new Intent(this, CaptureActivity.class), 10086);
			break;

		default:
			break;
		}
		
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		startActivity(new Intent(this, GoodsDetailActivity.class)
		.putExtra(Constants.GOODS, goods.get(position))
		.putExtra(Constants.SHOP, currentShop)
		.putExtra(Constants.MODE, Constants.EDIT));
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			search(data.getStringExtra(Constants.DEVICE_CODE));
		}
	}
	
	
	
	private void search(final String code) {
		final ProgressDialog pd = ProgressDialog.show(this, null, Constants.WAIT_SEARCH_GOODS, true, false);
		if (code == null || code.length() == 0) {
			return;
		}
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SHOP_ID, currentShop.getShopId());
		params.setParam(Constants.SEARCH_CODE, code);
		params.setParam(Constants.PAGE, Constants.PAGE_SIZE_OFFSET);
		params.setUrl(Constants.GOODS_LIST_URL);
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				pd.dismiss();
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null || !returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(GoodsListWithImageActivity.this, Constants.getErrorInf(null, null));
					return;
				}
				ArrayList<GoodsVo> goods = new Gson().fromJson(jo.get(Constants.GOODS_LIST), new TypeToken<List<GoodsVo>>(){}.getType());
				adapter.refreshData(goods);
			}
			
			@Override
			public void onFail(Exception e) {
				pd.dismiss();
				ToastUtil.showShortToast(GoodsListWithImageActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}
	
	/**
	 * Search.
	 */
	private void getMoreData(final boolean isRefresh) {
		if (isRefresh) {
			currentPage = Constants.PAGE_SIZE_OFFSET;
		}
		if (totalPage < currentPage) {
			if (isRefresh) {
				swipeRefreshLayout.setRefreshing(false);
			}else{
				swipeRefreshLayout.setLoading(false);
			}
			return;
		}
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SHOP_ID, currentShop.getShopId());
		if (searchCode != null) {
			params.setParam(Constants.SEARCH_CODE, searchCode);
		}
		if (categoryId != null) {
			params.setParam(Constants.CATEGORY_ID, categoryId);
		}
		params.setParam(Constants.PAGE, currentPage ++);
		params.setUrl(Constants.GOODS_LIST_URL);
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null || !returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(GoodsListWithImageActivity.this, Constants.getErrorInf(null, null));
					return;
				}
				ArrayList<GoodsVo> goods = new Gson().fromJson(jo.get(Constants.GOODS_LIST), new TypeToken<List<GoodsVo>>(){}.getType());
				if (isRefresh) {
					adapter.refreshData(goods);
					swipeRefreshLayout.setRefreshing(false);
				}else{
					adapter.addData(goods);
					swipeRefreshLayout.setLoading(false);
				}
			}
			
			@Override
			public void onFail(Exception e) {
				if (isRefresh) {
					swipeRefreshLayout.setRefreshing(false);
				}else{
					swipeRefreshLayout.setLoading(false);
				}
				ToastUtil.showShortToast(GoodsListWithImageActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}

	@Override
	public void onLoad() {
		getMoreData(false);
	}

	@Override
	public void onRefresh() {
		getMoreData(true);
	}
}
