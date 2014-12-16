package com.dfire.retail.app.manage.activity.goodsmanager;

import java.util.ArrayList;
import java.util.List;

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

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.adapter.SelectShopAdapter;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.bo.AllShopListIncludeCompanyBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
/**
 * 全部门店 包括分公司
 * @author ys
 * 类别二
 */
public class SelectShopForGoodsManagerActivity extends GoodsManagerBaseActivity implements OnItemClickListener,OnClickListener{

	public static final int SEARCH = 1;
	public static final int ASYNC = 2;

	private PullToRefreshListView selectshoplist;
	
	private int currentPage = 1;
	
	private  ArrayList<AllShopVo> allShops;
	
	private String selectShopId;
	
	private Integer pageSize = 0;
	
	private EditText input;
	
	private TextView search;
	
	private SelectShopAdapter selectShopAdapter;
	
	private String keyWord;
	
	private AllShopVo allShopVo;
	private int from;
	private AllShopVo noShop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_shop_for_goods_manager);
		setTitleText("选择门店");
		setBack();
		hideRight();
		this.init();
	}
	
	private void init(){
		from = getIntent().getExtras().getInt(Constants.OPT_TYPE, -1);
		selectShopId = getIntent().getExtras().getString(Constants.SHOP_ID);
		allShops = new ArrayList<AllShopVo>();//店铺
		if (from == SEARCH) {
			allShopVo = new AllShopVo();
			allShopVo.setShopId(RetailApplication.getShopVo().getShopId());
			allShopVo.setShopName("所有下属门店");
			allShopVo.setParentId(RetailApplication.getShopVo().getParentId());
			allShopVo.setCode("");
			allShops.add(allShopVo);
		}else{
			
			//add all shop
			allShopVo = new AllShopVo();
			allShopVo.setShopId(RetailApplication.getShopVo().getShopId());
			allShopVo.setShopName("同步所有");
			allShopVo.setParentId(RetailApplication.getShopVo().getParentId());
			allShopVo.setCode("");
			allShops.add(allShopVo);

			//add no shop
			noShop = new AllShopVo();
			noShop.setShopId(null);
			noShop.setShopName("不同步");
			noShop.setCode(Constants.EMPTY_STRING);
			allShops.add(noShop);		}
		
		
		input = (EditText) findViewById(R.id.input);
		setSearchClear(input);
		search = (TextView) findViewById(R.id.search);
		search.setOnClickListener(this);
		selectshoplist = (PullToRefreshListView) findViewById(R.id.selectshoplist);
		selectShopAdapter = new SelectShopAdapter(SelectShopForGoodsManagerActivity.this, allShops,selectShopId);
		selectshoplist.setAdapter(selectShopAdapter);
		selectshoplist.setOnItemClickListener(this);
		selectshoplist.setMode(Mode.BOTH);
		addFooter(selectshoplist.getRefreshableView());
		initPullToRefreshText(selectshoplist);
		// 列表刷新和加载更多操作
		selectshoplist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(SelectShopForGoodsManagerActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				
				currentPage = 1;//选择以后初始化页数
				allShops.clear();
				keyWord = input.getText().toString();
				if (StringUtils.isEmpty(keyWord)||StringUtils.isEquals(keyWord, "")) {
					allShops.add(allShopVo);
					if (from == ASYNC) {
						allShops.add(noShop);
					}
				}
				getShop();
			}
			/**
			 * 上拉加载更多
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(SelectShopForGoodsManagerActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				getShop();
			}
		});
		selectshoplist.setRefreshing(false);
	}
	@Override
	public void onClick(View v) {
		if (v==search) {
			currentPage = 1;
//			allShops.clear();
//			keyWord = input.getText().toString();
//			if (StringUtils.isEmpty(keyWord)||StringUtils.isEquals(keyWord, "")) {
//				allShops.add(allShopVo);
//				if (from == ASYNC) {
//					allShops.add(noShop);
//				}
//			}
			selectshoplist.setRefreshing(false);
		}
	}
	/**
	 * 获取店铺列表
	 * @param isInit
	 */
	private void getShop() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.ALL_SHOPLIST_INCLUDECOMPANY);
		params.setParam(Constants.SHOP_ID, RetailApplication.getShopVo().getShopId());
		params.setParam("keyWord", keyWord);
		params.setParam(Constants.PAGE, currentPage);
		
		new AsyncHttpPost(this, params, AllShopListIncludeCompanyBo.class,false,new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				selectshoplist.onRefreshComplete();
				AllShopListIncludeCompanyBo  bo = (AllShopListIncludeCompanyBo)oj;
				if (bo!=null) {
					pageSize = bo.getPageSize();
					List<AllShopVo> allShopVos = new ArrayList<AllShopVo>();
					allShopVos = bo.getAllShopList();
					if (allShopVos != null && allShopVos.size() > 0) {
						if (currentPage>=pageSize){
							selectshoplist.setMode(Mode.PULL_FROM_START);
						}
						allShops.addAll(allShopVos);
						selectShopAdapter.notifyDataSetChanged();
					}
				}
			}
			@Override
			public void onFail(Exception e) {
				selectshoplist.onRefreshComplete();
			}
		}).execute();
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		Intent select = new Intent();
		select.putExtra(Constants.SHOP, allShops.get(arg2-1));
		setResult(RESULT_OK, select);
		finish();
	}
}
