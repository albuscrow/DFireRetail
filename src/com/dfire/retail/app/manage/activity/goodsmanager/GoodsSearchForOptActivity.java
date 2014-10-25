package com.dfire.retail.app.manage.activity.goodsmanager;
import java.util.List;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.CaptureActivity;
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
public class GoodsSearchForOptActivity extends GoodsManagerBaseActivity implements OnClickListener, OnItemClickListener, OnRefreshListener, OnLoadListener {
	
	/** The mode. */
	int optMode;
	
	/** The title. */
	private TextView title;
	
	private GoodsListWithImageForPrecessAdapter adapter;
	private List<GoodsVo> goodsList;
	private ListView rules;

	private SwipeRefreshLayout swipeRefreshLayout;

	private int currentPage = Constants.PAGE_SIZE_OFFSET;

	private int totalPage;

	private String code;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_goods_search_for_process);
		hideRight();
		optMode = getIntent().getIntExtra(Constants.MODE, Constants.INVALID_INT);
		
		findViewById(R.id.title_left).setOnClickListener(this);
		((TextView)findViewById(R.id.title_text)).setText(Constants.GOODS_TITLE);
		
		findViewById(R.id.minus).setOnClickListener(this);
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
		
		title = (TextView)findViewById(R.id.title);
		switch (optMode) {
		case GoodsManagerMainMenuActivity.CHAIFEN:
			title.setText(Constants.TITLE_SPLIT_RULE);
			setTitleText(Constants.TITLE_OPT_SEARCH_SPLIT);
			break;
		case GoodsManagerMainMenuActivity.ZUZHUANG:
			title.setText(Constants.TITLE_ASSEMBLE_RULE);
			setTitleText(Constants.TITLE_OPT_SEARCH_ASSEMBLE);
			break;
		case GoodsManagerMainMenuActivity.JIAGONG:
			title.setText(Constants.TITLE_PROCESS_RULE);
			setTitleText(Constants.TITLE_OPT_SEARCH_PROCESS);
			break;

		default:
			break;
		}
		findViewById(R.id.scan).setOnClickListener(this);
		findViewById(R.id.scanButton).setOnClickListener(this);
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
			
		case R.id.minus:
			startActivity(new Intent(GoodsSearchForOptActivity.this, GoodsOptDetailActivity.class).putExtra(Constants.MODE, optMode));
			break;
			
		case R.id.search:
			search();
			break;
			
		case R.id.scan:
		case R.id.scanButton:
			startActivityForResult(new Intent(this, CaptureActivity.class), Constants.REQUEST_CODE_FOR_SCAN);
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
		if (code == null) {
			code = "";
//			ToastUtil.showShortToast(this, Constants.INPUT_SEARCH_CODE);
//			return; TODO
		}
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SEARCH_CODE, code);
		currentPage = Constants.PAGE_SIZE_OFFSET;
		params.setParam(Constants.PAGE, currentPage++);
		switch (optMode) {
		case GoodsManagerMainMenuActivity.CHAIFEN:
			params.setUrl(Constants.GOODS_SPLIT_URL);
			break;
		case GoodsManagerMainMenuActivity.ZUZHUANG:
			params.setUrl(Constants.GOODS_ASSEMBLE_URL);
			break;
		case GoodsManagerMainMenuActivity.JIAGONG:
			params.setUrl(Constants.GOODS_PROCESSING_URL);
			break;

		default:
			break;
		}
		final ProgressDialog pd = ProgressDialog.show(GoodsSearchForOptActivity.this, null, Constants.WAIT_SEARCH_RULE, true, false);
		new AsyncHttpPost(params, new RequestResultCallback() {

			@Override
			public void onSuccess(String str) {
				pd.dismiss();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsSearchForOptActivity.this)) {
					return;
				}
				goodsList = (List<GoodsVo>) ju.get(Constants.GOODS_HANDLERS_LIST, new TypeToken<List<GoodsVo>>(){}.getType());
				adapter = new GoodsListWithImageForPrecessAdapter(GoodsSearchForOptActivity.this, goodsList);
				rules.setAdapter(adapter);
				totalPage = ju.getInt(Constants.PAGE_SIZE);
			}
			
			@Override
			public void onFail(Exception e) {
				pd.dismiss();
				ToastUtil.showUnknowError(GoodsSearchForOptActivity.this);
				e.printStackTrace();
			}
		}).execute();
	}
	
	@Override
	protected void onResume() {
		getMoreData(true);
		super.onResume();
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
		startActivity(new Intent(this, GoodsOptDetailActivity.class)
		.putExtra(Constants.MODE, optMode)
		.putExtra(Constants.GOODS, goodsList.get(position)));
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
			params.setUrl(Constants.GOODS_SPLIT_URL);
			break;
		case GoodsManagerMainMenuActivity.ZUZHUANG:
			params.setUrl(Constants.GOODS_ASSEMBLE_URL);
			break;
		case GoodsManagerMainMenuActivity.JIAGONG:
			params.setUrl(Constants.GOODS_PROCESSING_URL);
			break;

		default:
			break;
		}
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsSearchForOptActivity.this)) {
					return;
				}
				goodsList = (List<GoodsVo>) ju.get(Constants.GOODS_HANDLERS_LIST, new TypeToken<List<GoodsVo>>(){}.getType());
				if (isRefresh) {
					adapter.refreshData(goodsList);
					swipeRefreshLayout.setRefreshing(false);
				}else{
					adapter.addData(goodsList);
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
				ToastUtil.showUnknowError(GoodsSearchForOptActivity.this);
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
