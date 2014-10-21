package com.dfire.retail.app.manage.activity.goodsmanager;


import java.util.List;


import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.CaptureActivity;
import com.dfire.retail.app.manage.adapter.ChooseGoodsAdapter;
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
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Class GoodsSearchForProcessActivity.
 * 
 * @author albuscrow
 */
public class ChooseGoodsActivity extends GoodsManagerBaseActivity implements OnClickListener, OnItemClickListener, OnRefreshListener, OnLoadListener {
	
	private ChooseGoodsAdapter adapter;
	private List<GoodsVo> goodsList;
	private ListView rules;

	private SwipeRefreshLayout swipeRefreshLayout;

	private int currentPage = Constants.PAGE_SIZE_OFFSET;

	private int totalPage;

	private String code;
	private int optMode;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_choose_goods);
		hideRight();
		
		findViewById(R.id.title_left).setOnClickListener(this);
		((TextView)findViewById(R.id.title_text)).setText(Constants.CHOOSE_GOODS);
		
		findViewById(R.id.search).setOnClickListener(this);
		
		rules = (ListView)findViewById(R.id.list);
		rules.setOnItemClickListener(this);
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.goodsListLayout);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setOnLoadListener(this);
//		swipeRefreshLayout.setColor(android.R.color.holo_blue_bright,
//	                            android.R.color.holo_green_light,
//	                            android.R.color.holo_orange_light,
//	                            android.R.color.holo_red_light);
		swipeRefreshLayout.setLoadNoFull(true);
		optMode = getIntent().getIntExtra(Constants.MODE, Constants.INVALID_INT);
		findViewById(R.id.scan).setOnClickListener(this);
		findViewById(R.id.scanButton).setOnClickListener(this);
		
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
			search();
			break;
			
		case R.id.scan:
		case R.id.scanButton:
			startActivityForResult(new Intent(this, CaptureActivity.class), 10086);
			break;
			
		default:
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			search(data.getStringExtra(Constants.DEVICE_CODE));
		}
	}
	

	private void search(String code) {
		this.code = code;
		currentPage = Constants.PAGE_SIZE_OFFSET;
		if (code == null || code.length() == 0) {
			ToastUtil.showShortToast(this, Constants.INPUT_SEARCH_CODE);
//			return;
		}
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SEARCH_CODE, code);
		params.setParam(Constants.PAGE, currentPage++);
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
		final ProgressDialog pd = ProgressDialog.show(ChooseGoodsActivity.this, null, Constants.WAIT_SEARCH_RULE, true, false);
		new AsyncHttpPost(params, new RequestResultCallback() {

			@Override
			public void onSuccess(String str) {
				pd.dismiss();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(ChooseGoodsActivity.this)) {
					return;
				}
				goodsList = (List<GoodsVo>) ju.get(Constants.GOODS_LIST, new TypeToken<List<GoodsVo>>(){}.getType());
				adapter = new ChooseGoodsAdapter(ChooseGoodsActivity.this, goodsList);
				rules.setAdapter(adapter);
				totalPage = ju.getInt(Constants.PAGE_SIZE);
			}
			
			@Override
			public void onFail(Exception e) {
				pd.dismiss();
				ToastUtil.showUnknowError(ChooseGoodsActivity.this);
				e.printStackTrace();
			}
		}).execute();
	}

	/**
	 * Search.
	 */
	private void search() {
		String code = ((TextView)findViewById(R.id.code)).getText().toString();
		search(code);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		setResult(Activity.RESULT_OK, new Intent().putExtra(Constants.GOODS, goodsList.get(position)));
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
			if (isRefresh) {
				swipeRefreshLayout.setRefreshing(false);
			}else{
				swipeRefreshLayout.setLoading(false);
			}
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
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(ChooseGoodsActivity.this)) {
					return;
				}
				List<GoodsVo> goods = (List<GoodsVo>) ju.get(Constants.GOODS_LIST, new TypeToken<List<GoodsVo>>(){}.getType());
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
				ToastUtil.showUnknowError(ChooseGoodsActivity.this);
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