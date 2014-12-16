package com.dfire.retail.app.manage.activity.goodsmanager;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.adapter.ChooseGoodsAdapter;
import com.dfire.retail.app.manage.adapter.GoodsListWithImageForPrecessAdapter;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.swipelayout.SwipeRefreshLayout;
import com.dfire.retail.app.manage.widget.swipelayout.SwipeRefreshLayout.OnLoadListener;
import com.dfire.retail.app.manage.widget.swipelayout.SwipeRefreshLayout.OnRefreshListener;
import com.dfire.retail.app.zxing.MipcaActivityCapture;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Class GoodsSearchForProcessActivity.
 * 
 * @author albuscrow
 */
public class ChooseGoodsActivity extends GoodsManagerBaseActivity implements OnClickListener, OnItemClickListener{
	
	private ChooseGoodsAdapter adapter;
	private List<GoodsVo> goodsList;
	private PullToRefreshListView goodsListView;

//	private SwipeRefreshLayout swipeRefreshLayout;

	private int currentPage = Constants.PAGE_SIZE_OFFSET;

	private int totalPage = Integer.MAX_VALUE;

	private String code;
	private int optMode;
	private List<String> ids;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		optMode = getIntent().getIntExtra(Constants.MODE, Constants.INVALID_INT);
		ids = (List<String>) getIntent().getSerializableExtra(Constants.IDS);
		
		setContentView(R.layout.activity_choose_goods);
		hideRight();
		
		findViewById(R.id.title_left).setOnClickListener(this);
		((TextView)findViewById(R.id.title_text)).setText(Constants.CHOOSE_GOODS);
		
		findViewById(R.id.search).setOnClickListener(this);
		
		goodsListView = (PullToRefreshListView)findViewById(R.id.list);
		addFooter(goodsListView.getRefreshableView());
		goodsListView.setOnItemClickListener(this);
		
		goodsListView.setMode(Mode.BOTH);
		
		initPullToRefreshText(goodsListView);

		// 列表刷新和加载更多操作
		goodsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(ChooseGoodsActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// 启动异步
				getMoreData(true);
				// 不执行异步，数据写死测试
				// initDatasTest();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(ChooseGoodsActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				getMoreData(false);
			}
		});
		adapter = new ChooseGoodsAdapter(this, null);
		goodsListView.setAdapter(adapter);
		goodsListView.setRefreshing(false);
		
//		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.goodsListLayout);
//		swipeRefreshLayout.setOnRefreshListener(this);
//		swipeRefreshLayout.setOnLoadListener(this);
//		swipeRefreshLayout.setColor(android.R.color.holo_blue_bright,
//	                            android.R.color.holo_green_light,
//	                            android.R.color.holo_orange_light,
//	                            android.R.color.holo_red_light);
//		swipeRefreshLayout.setLoadNoFull(true);
		findViewById(R.id.scan).setOnClickListener(this);
		findViewById(R.id.scanButton).setOnClickListener(this);
		setSearchClear((EditText) findViewById(R.id.code));
		setBack();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
			
		case R.id.search:
			code = ((TextView)findViewById(R.id.code)).getText().toString();
			if (code == null) {
				code = Constants.EMPTY_STRING;
			}
			currentPage = Constants.PAGE_SIZE_OFFSET;
			totalPage = Integer.MAX_VALUE;
			goodsListView.setRefreshing();
			break;
			
		case R.id.scan:
		case R.id.scanButton:
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.FOR_SEARCH);
			break;
			
		default:
			break;
		}
		
	}
	
	private boolean isScan;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			code = data.getStringExtra(Constants.DEVICE_CODE);
			((EditText)findViewById(R.id.code)).setText(code);
			if (code == null) {
				code = Constants.EMPTY_STRING;
			}
			isScan = true;
			goodsListView.setRefreshing();
		}
	}
	

//	private void search(final String code) {
//		this.code = code;
//		currentPage = Constants.PAGE_SIZE_OFFSET;
//		RequestParameter params = new RequestParameter(true);
//		params.setParam(Constants.SEARCH_CODE, code);
//		params.setParam(Constants.PAGE, currentPage++);
//		switch (optMode) {
//		case GoodsManagerMainMenuActivity.CHAIFEN:
//			params.setUrl(Constants.GOODS_SPLIT_CHOICE_URL);
//			break;
//			
//		case GoodsManagerMainMenuActivity.ZUZHUANG:
//			params.setUrl(Constants.GOODS_ASSEMBLE_CHOICE_URL);
//			break;
//			
//		case GoodsManagerMainMenuActivity.JIAGONG:
//			params.setUrl(Constants.GOODS_PROCESSING_CHOICE_URL);
//			break;
//
//		default:
//			break;
//		}
//		new AsyncHttpPost(this, params, new RequestResultCallback() {
//
//			@Override
//			public void onSuccess(String str) {
//				JsonUtil ju = new JsonUtil(str);
//				if (ju.isError(ChooseGoodsActivity.this)) {
//					return;
//				}
//				goodsList = (List<GoodsVo>) ju.get(Constants.GOODS_LIST, new TypeToken<List<GoodsVo>>(){}.getType());
//				if (goodsList == null) {
//					goodsList = new ArrayList<GoodsVo>();
//				}
//				
//				totalPage = ju.getInt(Constants.PAGE_SIZE);
//				filter(goodsList);
//				
//				adapter = new ChooseGoodsAdapter(ChooseGoodsActivity.this, goodsList);
//				goodsListView.setAdapter(adapter);
//				if (currentPage > totalPage) {
//					goodsListView.setMode(Mode.PULL_FROM_START);
//				}else{
//					goodsListView.setMode(Mode.BOTH);
//				}
//			}
//			
//
//			@Override
//			public void onFail(Exception e) {
//				ToastUtil.showShortToast(ChooseGoodsActivity.this, Constants.NO_NET);
//				e.printStackTrace();
//			}
//		}).execute();
//	}
//	
	
	private void filter(List<GoodsVo> goodsList) {
		boolean isRemove = false;
		boolean isBig = getIntent().getBooleanExtra(Constants.IS_BIG, false);
		for (Iterator iterator = goodsList.iterator(); iterator.hasNext();) {
			GoodsVo goodsVo = (GoodsVo) iterator.next();
			if (aleardyExist(goodsVo) || (isBig && goodsVo.getType() != 1)) {
				iterator.remove();
				isRemove = true;
			}
		}
		if (isRemove) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					getMoreData(false);
				}
			});
		}
	}

	private boolean aleardyExist(GoodsVo goodsVo) {
		for (String id : ids) {
			if (id.equals(goodsVo.getGoodsId())) {
				return true;
			}
		}
		return false;
	}
	
	
//	@Override
//	protected void onResume() {
//		search();
//		String code = ((TextView)findViewById(R.id.code)).getText().toString();
//		if (code == null) {
//			code = Constants.EMPTY_STRING;
//		}
//	
//		super.onResume();
//	}

//	/**
//	 * Search.
//	 */
//	private void search() {
//		search(code);
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		setResult(Activity.RESULT_OK, new Intent().putExtra(Constants.GOODS, (Serializable)adapter.getItem(position - 1)));
		finish();
	}

	/**
	 * Search.
	 */
	private void getMoreData(final boolean isRefresh) {
		if (isRefresh) {
			currentPage = Constants.PAGE_SIZE_OFFSET;
		}
		if (totalPage < currentPage) {
			goodsListView.onRefreshComplete();
//			if (isRefresh) {
//				swipeRefreshLayout.setRefreshing(false);
//			}else{
//				swipeRefreshLayout.setLoading(false);
//			}
			return;
		}

		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SEARCH_CODE, code);
		params.setParam(Constants.PAGE, currentPage ++);
		switch (optMode) {
		case GoodsManagerMainMenuActivity.CHAIFEN:
			params.setUrl(Constants.GOODS_SPLIT_CHOICE_URL);
			break;
			
		case GoodsManagerMainMenuActivity.ZUZHUANG:
			params.setUrl(Constants.GOODS_ASSEMBLE_CHOICE_URL);
			break;
			
		case GoodsManagerMainMenuActivity.JIAGONG:
			params.setUrl(Constants.GOODS_PROCESSING_CHOICE_URL);
			break;

		default:
			break;
		}
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			

			@Override
			public void onSuccess(String str) {
				goodsListView.onRefreshComplete();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(ChooseGoodsActivity.this)) {
					return;
				}
				
				List<GoodsVo> goods = (List<GoodsVo>) ju.get(Constants.GOODS_LIST, new TypeToken<List<GoodsVo>>(){}.getType());
				if (goods == null) {
					goods = new ArrayList<GoodsVo>();
				}
				if (goods.size() == 0 && isScan && getIntent().getBooleanExtra(Constants.NEED_ASK_TO_ADD, false)) {
					isScan = false;

					final AlertDialog alertDialog = new AlertDialog(ChooseGoodsActivity.this);
					alertDialog.setMessage(Constants.INF_NO_GOODS);
					alertDialog.setPositiveButton(Constants.CONFIRM, new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();

							GoodsVo goods = new GoodsVo();
							goods.setBarCode(code);
							startActivity(new Intent(ChooseGoodsActivity.this, GoodsDetailActivity.class)
							.putExtra(Constants.GOODS, goods)
							.putExtra(Constants.SHOP, RetailApplication.getShopVo().toAllShopVo())
							.putExtra(Constants.SEARCH_STATUS, Constants.ZHONGDIANYOU)
							.putExtra(Constants.MODE, Constants.ADD));
						}
					});
					alertDialog.setNegativeButton(Constants.CANCEL, new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
						}
					});
				}
				
				filter(goods);
				
				if (goods.size() == 1 && code != null && code.length() != 0) {
					setResult(Activity.RESULT_OK, new Intent().putExtra(Constants.GOODS, goods.get(0)));
					finish();
					return;
				}
				
				if (isRefresh) {
					goodsList = goods;
					adapter.refreshData(goods);
				}else{
					adapter.addData(goods);
				}
				totalPage = ju.getInt(Constants.PAGE_SIZE);
				if (totalPage < currentPage) {
					goodsListView.setMode(Mode.PULL_FROM_START);
				}else{
					goodsListView.setMode(Mode.BOTH);
				}
			}
			
			@Override
			public void onFail(Exception e) {
				goodsListView.onRefreshComplete();
//				if (isRefresh) {
//					swipeRefreshLayout.setRefreshing(false);
//				}else{
//					swipeRefreshLayout.setLoading(false);
//				}
				ToastUtil.showShortToast(ChooseGoodsActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}, false).execute();
	}

}
