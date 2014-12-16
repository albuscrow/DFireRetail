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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.StockAddGoodsAdapter;
import com.dfire.retail.app.manage.data.StockInfoVo;
import com.dfire.retail.app.manage.data.bo.StockInfoGoodsListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.zxing.MipcaActivityCapture;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/**
 * 库存管理-库存调整-选择商品
 * 
 * @author ys
 * 
 */
@SuppressLint("UseValueOf")
public class StockAddGoodsActivity extends TitleActivity implements OnClickListener {
	
	public static StockAddGoodsActivity instance = null;  
	
	private TextView search;
	
	private EditText input;
	
	private ImageView scan;
	
	private PullToRefreshListView store_add_goods_lv;
	
	private List<StockInfoVo> searchGoodsVoList;
	
	private int currentPage = 1;
	
	private String flag;
	
	private Integer pageSize = 0;
	
	private StockAddGoodsAdapter stockAddGoodsAdapter;
	
	private String shopId;
	
	private ImageView ico_scan;
	
	private String findParameter;
	
	private ImageView clear_input;
	
	private int mode;// 判断异步执行完是否禁用加载更多
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_order_add_goods);
		setTitleText("选择商品");
		instance = this;
		showBackbtn();
		findView();
		store_add_goods_lv.setMode(Mode.PULL_FROM_START);
		getResult();
		store_add_goods_lv.setRefreshing();
	}

	public void findView() {
		searchGoodsVoList = new ArrayList<StockInfoVo>();
		flag = getIntent().getStringExtra("flag");
		shopId = getIntent().getStringExtra("shopId");
		search = (TextView) findViewById(R.id.search);
		search.setOnClickListener(this);
		input = (EditText) findViewById(R.id.input);
		scan = (ImageView) findViewById(R.id.scan);
		ico_scan = (ImageView) findViewById(R.id.ico_scan);
		clear_input = (ImageView) findViewById(R.id.clear_input);
		clear_input.setOnClickListener(this);
		ico_scan.setOnClickListener(this);
		scan.setOnClickListener(this);
		store_add_goods_lv = (PullToRefreshListView) findViewById(R.id.store_add_goods_lv);
		stockAddGoodsAdapter = new StockAddGoodsAdapter(StockAddGoodsActivity.this, searchGoodsVoList);
		store_add_goods_lv.setAdapter(stockAddGoodsAdapter);
		store_add_goods_lv.setMode(Mode.BOTH);
		new ListAddFooterItem(this, store_add_goods_lv.getRefreshableView());
		// 列表刷新和加载更多操作
		store_add_goods_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StockAddGoodsActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = 1;//选择以后初始化页数
				getResult();
			}
			/**
			 * 上拉加载更多
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StockAddGoodsActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				getResult();
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
		store_add_goods_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent add;
				if (("returnAdjustmentAdd").equals(flag)) { 
					add = new Intent(StockAddGoodsActivity.this, StockGoodInfoActivity.class);
					add.putExtra("activity", "stockGoodInfoActivity");
					add.putExtra("goodId", searchGoodsVoList.get(arg2-1).getGoodsId());
					add.putExtra("shopId", shopId);
					add.putExtra("barCode", searchGoodsVoList.get(arg2-1).getBarCode());
					add.putExtra("goodName", searchGoodsVoList.get(arg2-1).getGoodsName());
					startActivity(add);
				}
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK) {
			findParameter = data.getStringExtra(Constants.DEVICE_CODE);
			input.setText(findParameter);
			currentPage = 1;
			store_add_goods_lv.setMode(Mode.PULL_FROM_START);
			getResult();
			store_add_goods_lv.setRefreshing();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			currentPage = 1;
			findParameter = input.getText().toString();
			store_add_goods_lv.setMode(Mode.PULL_FROM_START);
			getResult();
			store_add_goods_lv.setRefreshing();
			break;
		case R.id.scan:
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.FOR_GET);
//			startActivityForResult(new Intent(this, CaptureActivity.class),10086);
			break;
		case R.id.ico_scan:
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.FOR_GET);
//			startActivityForResult(new Intent(this, CaptureActivity.class),10086);
			break;
		case R.id.clear_input:
			input.setText("");
			clear_input.setVisibility(View.GONE);
		break;
		default:
			break;
		}
	}
	/**
	 * 库存调整列表
	 */
	private void getResult() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockAdjust/list");
		params.setParam("shopId", shopId);
		params.setParam("findParameter", findParameter);
		params.setParam("currentPage", currentPage);
		new AsyncHttpPost(this,params,StockInfoGoodsListBo.class,false, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				StockInfoGoodsListBo bo = (StockInfoGoodsListBo)oj;
				if (bo!=null) {
					pageSize = bo.getPageCount();
					List<StockInfoVo> infoVos = new ArrayList<StockInfoVo>();
					infoVos = bo.getStockInfoList();
					
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							searchGoodsVoList.clear();
						}
						if (infoVos != null && infoVos.size() > 0) {
							store_add_goods_lv.setMode(Mode.BOTH);
							searchGoodsVoList.addAll(infoVos);
						}else {
							mode = 1;
						}
						stockAddGoodsAdapter.notifyDataSetChanged();
					}else {
						searchGoodsVoList.clear();
						stockAddGoodsAdapter.notifyDataSetChanged();
						mode = 1;
					}
					store_add_goods_lv.onRefreshComplete();
					if (mode == 1) {
						store_add_goods_lv.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
				}
			}
			@Override
			public void onFail(Exception e) {
				store_add_goods_lv.onRefreshComplete();
			}
		}).execute();
	}
}