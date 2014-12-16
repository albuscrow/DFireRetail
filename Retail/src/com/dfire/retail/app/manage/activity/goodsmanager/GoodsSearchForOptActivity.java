package com.dfire.retail.app.manage.activity.goodsmanager;
import java.util.ArrayList;
import java.util.List;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.adapter.GoodsListWithImageForPrecessAdapter;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.zxing.MipcaActivityCapture;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
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
public class GoodsSearchForOptActivity extends GoodsManagerBaseActivity implements OnClickListener, OnItemClickListener{
	
	/** The mode. */
	int optMode;
	
	/** The title. */
	private TextView title;
	
	private GoodsListWithImageForPrecessAdapter adapter;
	private List<GoodsVo> goodsList;
	private PullToRefreshListView rules;

//	private SwipeRefreshLayout swipeRefreshLayout;
	private boolean isScan = false;

	private int currentPage = Constants.PAGE_SIZE_OFFSET;

	private int totalPage;

	private String code;

	private EditText codeText;

	private int index;

	private int top;

	private boolean quiteByHome = false;

	private int handleRules = 1;

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
		adapter = new GoodsListWithImageForPrecessAdapter(this, null);
		rules = (PullToRefreshListView)findViewById(R.id.list);
		rules.setAdapter(adapter);
		addFooter(rules.getRefreshableView());
		rules.setOnItemClickListener(this);
		
		
		rules.setMode(Mode.BOTH);
		
		initPullToRefreshText(rules);
		
		

		// 列表刷新和加载更多操作
		rules.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(GoodsSearchForOptActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// 启动异步
				getMoreData(true);
				// 不执行异步，数据写死测试
				// initDatasTest();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(GoodsSearchForOptActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				getMoreData(false);
			}
		});
		
		
//		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.goodsListLayout);
//		swipeRefreshLayout.setOnRefreshListener(this);
//		swipeRefreshLayout.setOnLoadListener(this);
//		swipeRefreshLayout.setColor(android.R.color.holo_blue_bright,
//	                            android.R.color.holo_green_light,
//	                            android.R.color.holo_orange_light,
//	                            android.R.color.holo_red_light);
//		swipeRefreshLayout.setLoadNoFull(true);
		
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
		codeText = (EditText)findViewById(R.id.code);
		setSearchClear(codeText);
		setBack();
	
		//get init data;
		quiteByHome = false;
		currentPage = Constants.PAGE_SIZE_OFFSET;
		code = codeText.getText().toString();
		rules.setRefreshing(false);
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
			startActivityForResult(new Intent(GoodsSearchForOptActivity.this, GoodsOptDetailActivity.class).putExtra(Constants.MODE, optMode), handleRules);
			break;
			
		case R.id.search:
//			search(true);
			currentPage = Constants.PAGE_SIZE_OFFSET;
			code = codeText.getText().toString();
			needDialogForNoData = true;
			rules.setRefreshing(false);
			break;
			
		case R.id.scan:
		case R.id.scanButton:
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.REQUEST_CODE_FOR_SCAN);
			break;
			
		default:
			break;
		}
		
	}
	public static final int DELETE = 0;
	public static final int MODIFY = 2;
	public static final int ADD = 1;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Constants.REQUEST_CODE_FOR_SCAN) {
				codeText.setText(data.getStringExtra(Constants.DEVICE_CODE));
				code = data.getStringExtra(Constants.DEVICE_CODE);
				currentPage = Constants.PAGE_SIZE_OFFSET;
				isScan = true;
				needDialogForNoData = true;
				rules.setRefreshing();
			}else{
				int opt = data.getIntExtra(Constants.OPT_TYPE, Constants.INVALID_INT);
				switch (opt) {
				case DELETE:
					String id = data.getStringExtra(Constants.GOODS_ID);
					adapter.delete(id);
					break;
					
				case MODIFY:
					GoodsVo goodsAfterModify = (GoodsVo) data.getSerializableExtra(Constants.GOODS);
					adapter.update(goodsAfterModify);
					break;

				case ADD:
					GoodsVo newGoods = (GoodsVo) data.getSerializableExtra(Constants.GOODS);
					adapter.addNewGoods(newGoods);
					break;

				default:
					break;
				}
				goodsList = adapter.getData();
			}
//			search(data.getStringExtra(Constants.DEVICE_CODE), true);
		}
	}
	

//	private void search(String code, final boolean searchByUser) {
//		rules.setRefreshing(false);
//		this.code = code;
//		if (code == null) {
//			code = "";
////			ToastUtil.showShortToast(this, Constants.INPUT_SEARCH_CODE);
////			return; TODO
//		}
//		RequestParameter params = new RequestParameter(true);
//		params.setParam(Constants.SEARCH_CODE, code);
//		currentPage = Constants.PAGE_SIZE_OFFSET;
//		params.setParam(Constants.PAGE, currentPage++);
//		switch (optMode) {
//		case GoodsManagerMainMenuActivity.CHAIFEN:
//			params.setUrl(Constants.GOODS_SPLIT_URL);
//			break;
//		case GoodsManagerMainMenuActivity.ZUZHUANG:
//			params.setUrl(Constants.GOODS_ASSEMBLE_URL);
//			break;
//		case GoodsManagerMainMenuActivity.JIAGONG:
//			params.setUrl(Constants.GOODS_PROCESSING_URL);
//			break;
//
//		default:
//			break;
//		}
//		new AsyncHttpPost(this, params, new RequestResultCallback() {
//
//			@Override
//			public void onSuccess(String str) {
//				rules.onRefreshComplete();
//				JsonUtil ju = new JsonUtil(str);
//				if (ju.isError(GoodsSearchForOptActivity.this)) {
//					return;
//				}
//				goodsList = (List<GoodsVo>) ju.get(Constants.GOODS_HANDLERS_LIST, new TypeToken<List<GoodsVo>>(){}.getType());
//				if ((goodsList == null || goodsList.size() == 0) && searchByUser) {
//					ToastUtil.showShortToast(GoodsSearchForOptActivity.this, Constants.NO_RULES);
//				}
//				if (goodsList == null) {
//					goodsList = new ArrayList<GoodsVo>();
//				}
//				adapter = new GoodsListWithImageForPrecessAdapter(GoodsSearchForOptActivity.this, goodsList);
//				rules.setAdapter(adapter);
//				rules.getRefreshableView().setSelectionFromTop(index, top);
//				totalPage = ju.getInt(Constants.PAGE_SIZE);
//				
//				if (currentPage > totalPage) {
//					rules.setMode(Mode.PULL_FROM_START);
//				}else{
//					rules.setMode(Mode.BOTH);
//				}
//			}
//			
//			@Override
//			public void onFail(Exception e) {
//				rules.onRefreshComplete();
//				ToastUtil.showUnknowError(GoodsSearchForOptActivity.this);
//				e.printStackTrace();
//			}
//		}, false).execute();
//	}
	
//	@Override
//	protected void onResume() {
//		getMoreData(true);
//		if (!quiteByHome) {
//			currentPage = Constants.PAGE_SIZE_OFFSET;
//			code = codeText.getText().toString();
//			rules.setRefreshing(false);
//		}else{
//			quiteByHome = false;
//		}
//		super.onResume();
//	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_HOME) {
//			quiteByHome = true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	/**
	 * Search.
	 * @param b 
	 */
//	private void search(boolean b) {
//		code = codeText.getText().toString();
////		search(code, b);
//		rules.setRefreshing();
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// save index and top position  
		index = parent.getFirstVisiblePosition();  
		View v = parent.getChildAt(0);  
		top = (v == null) ? 0 : v.getTop();
		
		startActivityForResult(new Intent(this, GoodsOptDetailActivity.class)
		.putExtra(Constants.MODE, optMode)
		.putExtra(Constants.GOODS, goodsList.get(position - 1)), handleRules);
	}
	
	private boolean needDialogForNoData = false;
	/**
	 * Search.
	 */
	private void getMoreData(final boolean isRefresh) {
		if (isRefresh) {
			currentPage = Constants.PAGE_SIZE_OFFSET;
			totalPage = Integer.MAX_VALUE;
		}
		if (totalPage < currentPage) {
			rules.onRefreshComplete();
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
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			


			@Override
			public void onSuccess(String str) {
				rules.onRefreshComplete();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsSearchForOptActivity.this)) {
					return;
				}
				
				List<GoodsVo> goodsList = (List<GoodsVo>) ju.get(Constants.GOODS_HANDLERS_LIST, new TypeToken<List<GoodsVo>>(){}.getType());
				
				if (goodsList == null) {
					goodsList = new ArrayList<GoodsVo>();
				}
				
//				if ((goodsList == null || goodsList.size() == 0) && needDialogForNoData) {
//					needDialogForNoData = false;
//					ToastUtil.showShortToast(GoodsSearchForOptActivity.this, Constants.NO_RULES);
//				}
				
				if (isRefresh) {
					GoodsSearchForOptActivity.this.goodsList = goodsList;
					if (adapter == null) {
						adapter = new GoodsListWithImageForPrecessAdapter(GoodsSearchForOptActivity.this, null);
						rules.setAdapter(adapter);
					}
					adapter.refreshData(goodsList);
				}else{
					adapter.addData(goodsList);
				}
				totalPage = ju.getInt(Constants.PAGE_SIZE);
				if (currentPage > totalPage) {
					rules.setMode(Mode.PULL_FROM_START);
				}else{
					rules.setMode(Mode.BOTH);
				}
			}
			
			@Override
			public void onFail(Exception e) {
				rules.onRefreshComplete();
//				if (isRefresh) {
//					swipeRefreshLayout.setRefreshing(false);
//				}else{
//					swipeRefreshLayout.setLoading(false);
//				}
				ToastUtil.showShortToast(GoodsSearchForOptActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}, false).execute();
	}


}
