package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.item.StockQueryListAddFooterItem;
import com.dfire.retail.app.manage.adapter.StockQueryAdapter;
import com.dfire.retail.app.manage.data.StockInfoVo;
import com.dfire.retail.app.manage.data.bo.StockInfoBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.zxing.MipcaActivityCapture;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/**
 * 库存管理-库存列表
 *
 */
@SuppressLint("HandlerLeak")
public class StockQueryListActivity extends TitleActivity implements OnClickListener{
	
	private TextView search,mELStockShop;
	private EditText input;
	private ImageView scan;
	private PullToRefreshListView stock_query_list;
	private RelativeLayout shoplayout;
	private List<StockInfoVo> stockInfoList;
	private int currentPage = 1;
	private String shopId,shopName,findParameter,isStore,countPrice, countNum;
	private StockQueryAdapter mStockQueryAdapter;
	private int mode;// 判断异步执行完是否禁用加载更多
	private boolean isSingleShop = false;
	private Integer pageSize;
	private StockQueryListAddFooterItem addFooterItem;
	private ImageView clear_input;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_query_list);
		setTitleText("库存查询");
		showBackbtn();
		mRight.setImageResource(R.drawable.export);
		mRight.setVisibility(View.VISIBLE);
		mRight.setOnClickListener(this);
		findView();
		// 设置列表的刷新模式为下拉刷新，同时进行刷新操作
		stock_query_list.setMode(Mode.PULL_FROM_START);
		getStockInfoList();
		stock_query_list.setRefreshing();
	}
	public void findView(){
		stockInfoList =  new ArrayList<StockInfoVo>();
		mELStockShop= (TextView) findViewById(R.id.stockShopName);
		mELStockShop.setOnClickListener(this);
		shopId = RetailApplication.getShopVo().getShopId();
		search=(TextView)findViewById(R.id.stock_search);
		search.setOnClickListener(this);
		input=(EditText)findViewById(R.id.stock_shop_input);
		scan=(ImageView)findViewById(R.id.stock_scan);
		scan.setOnClickListener(this);
		shoplayout=(RelativeLayout)findViewById(R.id.shoplayout);
		clear_input = (ImageView) findViewById(R.id.clear_input);
		clear_input.setOnClickListener(this);
		
		shopId = getIntent().getStringExtra("shopId");
		shopName = getIntent().getStringExtra("shopName");
		findParameter = getIntent().getStringExtra("searchCode");
		isStore = getIntent().getStringExtra("isStore");
		if (StringUtils.isEquals(isStore, "1")) {//单店隐藏
			shoplayout.setVisibility(View.GONE);
			isSingleShop = true;
		}
		mELStockShop.setText(shopName);
		input.setText(findParameter);
		
		stock_query_list=(PullToRefreshListView)findViewById(R.id.stock_query_list);		
		mStockQueryAdapter=new StockQueryAdapter(this, stockInfoList, isSingleShop);
		stock_query_list.getRefreshableView().setAdapter(mStockQueryAdapter);
		stock_query_list.setMode(Mode.BOTH);
		addFooterItem = new StockQueryListAddFooterItem(this, stock_query_list.getRefreshableView());
		
		stock_query_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StockQueryListActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = 1;//选择以后初始化页数
				getStockInfoList();
			}
			/**
			 * 上拉加载更多
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StockQueryListActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				getStockInfoList();
			}
		});
		
		input.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s!=null&&!s.toString().equals("")) {
					clear_input.setVisibility(View.VISIBLE);
				}else{
					clear_input.setVisibility(View.GONE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stock_search:
			findParameter = input.getText().toString();
			reFreshing();
			break;
		case R.id.stock_scan:    
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.FOR_GET);
			break;
		case R.id.clear_input:
			input.setText("");
			clear_input.setVisibility(View.GONE);
		break;
		case R.id.title_right:/**导出*/
			Intent exportIntent =new Intent(this,StockQueryExportActivity.class);
			exportIntent.putExtra("shopId", shopId);
			startActivity(exportIntent);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	 if (resultCode==RESULT_OK) {
			findParameter = data.getStringExtra(Constants.DEVICE_CODE);
			input.setText(findParameter);
			reFreshing();
		}
    }
	/**
	 * 加条件以后刷新数据
	 */
	public void reFreshing(){
		currentPage = 1;//选择以后初始化页数
		stock_query_list.setMode(Mode.PULL_FROM_START);
		getStockInfoList();
		stock_query_list.setRefreshing();
	}
	 /**
     * 查询库存列表
     */
    private void getStockInfoList(){
    	RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.STOCK_QUERY_LIST);
		params.setParam(Constants.SHOP_ID, shopId);
		params.setParam(Constants.STOCKQUERYPARAM, findParameter);
		params.setParam(Constants.PAGE, currentPage);
		new AsyncHttpPost(this, params,StockInfoBo.class,false,new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				StockInfoBo bo = (StockInfoBo)oj;
				if (bo!=null) {
					List<StockInfoVo> list = bo.getStockInfoList();
					pageSize = bo.getPageCount();
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							stockInfoList.clear();
						}
						if (list != null && list.size() > 0) {
							stock_query_list.setMode(Mode.BOTH);
							stockInfoList.addAll(list);
						}else {
							mode = 1;
						}
						mStockQueryAdapter.notifyDataSetChanged();
					}else {
						stockInfoList.clear();
						mStockQueryAdapter.notifyDataSetChanged();
						mode = 1;
					}
					stock_query_list.onRefreshComplete();
					if (mode == 1) {
						stock_query_list.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
					countPrice = bo.getSumMoney()+"";
					countNum = bo.getNowStore()+"";
					addFooterItem.initData(countPrice, countNum, isSingleShop);
				}
			}
			@Override
			public void onFail(Exception e) {
				stock_query_list.onRefreshComplete();
			}
		}).execute();		
    }
  }