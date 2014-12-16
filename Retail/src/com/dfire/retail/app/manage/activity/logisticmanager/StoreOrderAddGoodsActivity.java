package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.StoreAddGoodsAdapter;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.data.bo.LogisticsGoodsListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.zxing.MipcaActivityCapture;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/**
 * 物流管理-选择商品
 * 
 * @author ys
 * 
 */
public class StoreOrderAddGoodsActivity extends TitleActivity implements OnClickListener {
	
	public static StoreOrderAddGoodsActivity instance = null;  
	
	private TextView search;
	
	private EditText input;
	
	private ImageView scan;
	
	private PullToRefreshListView store_add_goods_lv;
	
	private List<SearchGoodsVo> searchGoodsVoList;
	
	private int currentPage = 1;
	
	private String flag;
	
	private StoreAddGoodsAdapter storeAddGoodsAdapter;
	
	private Integer pageSize = 0;
	
	private String shopId;
	
	private ImageView ico_scan;
	
	private String isPrice,findParameter;
	
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
		searchGoodsVoList = new ArrayList<SearchGoodsVo>();
		flag = getIntent().getStringExtra("flag");
		isPrice = getIntent().getStringExtra("isPrice");
		shopId = getIntent().getStringExtra("shopId");
		search = (TextView) findViewById(R.id.search);
		input = (EditText) findViewById(R.id.input);
		scan = (ImageView) findViewById(R.id.scan);
		ico_scan = (ImageView) findViewById(R.id.ico_scan);
		clear_input = (ImageView) findViewById(R.id.clear_input);
		clear_input.setOnClickListener(this);
		ico_scan.setOnClickListener(this);
		search.setOnClickListener(this);
		scan.setOnClickListener(this);
		store_add_goods_lv = (PullToRefreshListView) findViewById(R.id.store_add_goods_lv);
		storeAddGoodsAdapter = new StoreAddGoodsAdapter(StoreOrderAddGoodsActivity.this, searchGoodsVoList,isPrice);
		store_add_goods_lv.setAdapter(storeAddGoodsAdapter);
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
				String label = DateUtils.formatDateTime(StoreOrderAddGoodsActivity.this, System.currentTimeMillis(), 
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
				String label = DateUtils.formatDateTime(StoreOrderAddGoodsActivity.this, System.currentTimeMillis(),
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
		/**选择商品以后*/
		store_add_goods_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent add;
				if (("returnGoodsReason").equals(flag)) {//退货返回
					add = new Intent(StoreOrderAddGoodsActivity.this, StoreReturnGoodsDetailItemActivity.class);
					add.putExtra("activity", "storeReturnGoodsAddActivity");
					add.putExtra("isPrice", isPrice);
					RetailApplication rapp = (RetailApplication) getApplication();
					HashMap<String, Object> map = rapp.getObjMap();
					map.put("returnGoodsReason", searchGoodsVoList.get(arg2-1));
					startActivity(add);
				}else if("returnOrderAdd".equals(flag)){//订货返回
					add = new Intent(StoreOrderAddGoodsActivity.this, StoreOrderAddActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("orderAdd", searchGoodsVoList.get(arg2-1));
					add.putExtras(bundle);
					setResult(100, add);
					finish();
				}else if(("allocationAdd").equals(flag)){//调拨
				    add = new Intent(StoreOrderAddGoodsActivity.this, StoreAllocationDetailItemActivity.class);
				    add.putExtra("activity", "storeAllocationAddActivity");
					RetailApplication rapp = (RetailApplication) getApplication();
					HashMap<String, Object> map = rapp.getObjMap();
					map.put("allocationAdd", searchGoodsVoList.get(arg2-1));
					startActivity(add);
				}else if ("returnCollectAdd".equals(flag)) {//进货返回
					add = new Intent(StoreOrderAddGoodsActivity.this, StoreCollectInfoActivity.class);
					add.putExtra("activity", "storeOrderAddGoodsActivity");
					add.putExtra("isPrice", isPrice);
					RetailApplication rapp = (RetailApplication) getApplication();
					HashMap<String, Object> map = rapp.getObjMap();
					map.put("returnCollectAdd", searchGoodsVoList.get(arg2-1));
					startActivity(add);
				}
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK) {
			currentPage = 1;
			findParameter = data.getStringExtra(Constants.DEVICE_CODE);
			input.setText(findParameter);
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
			break;
		case R.id.ico_scan:
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.FOR_GET);
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
	 * 获取商品列表
	 */
	private void getResult() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "orderGoods/goodsList");
		params.setParam("shopId", shopId);
		params.setParam("searchCode", findParameter);
		params.setParam("currentPage", currentPage);
		new AsyncHttpPost(this, params, LogisticsGoodsListBo.class,false, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				LogisticsGoodsListBo bo = (LogisticsGoodsListBo)oj;
				if (bo!=null) {
					pageSize = bo.getPageSize();
					List<SearchGoodsVo> goodsVos = new ArrayList<SearchGoodsVo>();
					goodsVos = bo.getGoodsList();
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							searchGoodsVoList.clear();
						}
						if (goodsVos != null && goodsVos.size() > 0) {
							store_add_goods_lv.setMode(Mode.BOTH);
							searchGoodsVoList.addAll(goodsVos);
						}else {
							mode = 1;
						}
						storeAddGoodsAdapter.notifyDataSetChanged();
					}else {
						searchGoodsVoList.clear();
						storeAddGoodsAdapter.notifyDataSetChanged();
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