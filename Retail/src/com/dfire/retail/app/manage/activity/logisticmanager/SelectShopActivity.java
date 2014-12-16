package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
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
import com.dfire.retail.app.manage.activity.messagemanage.MessageManageraddActivity;
import com.dfire.retail.app.manage.activity.messagemanage.MessageManangeActivity;
import com.dfire.retail.app.manage.activity.stockmanager.StockCheckRecordActivity;
import com.dfire.retail.app.manage.adapter.SelectShopAdapter;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.bo.AllShopListIncludeCompanyBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;
/**
 * 全部门店 包括分公司
 * @author ys
 * 类别二
 */
public class SelectShopActivity extends TitleActivity implements OnItemClickListener,OnClickListener{

	private PullToRefreshListView selectshoplist;
	
	private int currentPage = 1;
	
	private  ArrayList<AllShopVo> allShops;
	
	private String selectShopId;
	
	private String activity;
	
	private Integer pageSize = 0;
	
	private EditText input;
	
	private TextView search;
	
	private SelectShopAdapter selectShopAdapter;
	
	private String keyWord;
	
	private AllShopVo allShopVo;
	
	private ImageView clear_input;
	
	private int mode;// 判断异步执行完是否禁用加载更多
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_shop);
		setTitleText("选择门店");
		showBackbtn();
		this.init();
		selectshoplist.setMode(Mode.PULL_FROM_START);
		getShop();
		selectshoplist.setRefreshing();
	}
	private void init(){
		activity = getIntent().getExtras().getString("activity");
		selectShopId = getIntent().getExtras().getString("selectShopId");
		allShops = new ArrayList<AllShopVo>();//店铺
		allShopVo = new AllShopVo();
		allShopVo.setShopId(RetailApplication.getShopVo().getShopId());
		allShopVo.setShopName("所有下属门店");
		allShopVo.setParentId(RetailApplication.getShopVo().getParentId());
		allShopVo.setCode("");
		allShops.add(allShopVo);
		
		input = (EditText) findViewById(R.id.input);
		search = (TextView) findViewById(R.id.search);
		clear_input = (ImageView) findViewById(R.id.clear_input);
		clear_input.setOnClickListener(this);
		search.setOnClickListener(this);
		selectshoplist = (PullToRefreshListView) findViewById(R.id.selectshoplist);
		selectShopAdapter = new SelectShopAdapter(SelectShopActivity.this, allShops,selectShopId);
		selectshoplist.getRefreshableView().setAdapter(selectShopAdapter);
		selectshoplist.setOnItemClickListener(this);
		selectshoplist.setMode(Mode.BOTH);
		new ListAddFooterItem(this, selectshoplist.getRefreshableView());
		// 列表刷新和加载更多操作
		selectshoplist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(SelectShopActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				
				currentPage = 1;//选择以后初始化页数
				keyWord = input.getText().toString();
				getShop();
			}
			/**
			 * 上拉加载更多
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(SelectShopActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				getShop();
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
		if (v==search) {
			currentPage = 1;
			keyWord = input.getText().toString();
			selectshoplist.setMode(Mode.PULL_FROM_START);
			getShop();
			selectshoplist.setRefreshing();
		}else if (v==clear_input) {
			input.setText("");
			clear_input.setVisibility(View.GONE);
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
				AllShopListIncludeCompanyBo  bo = (AllShopListIncludeCompanyBo)oj;
				if (bo!=null) {
					pageSize = bo.getPageSize();
					List<AllShopVo> allShopVos = new ArrayList<AllShopVo>();
					allShopVos = bo.getAllShopList();
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							allShops.clear();
							if (StringUtils.isEmpty(keyWord)||StringUtils.isEquals(keyWord, "")) {
								allShops.add(allShopVo);
							}
						}
						if (allShopVos != null && allShopVos.size() > 0) {
							selectshoplist.setMode(Mode.BOTH);
							allShops.addAll(allShopVos);
						}else {
							mode = 1;
						}
						selectShopAdapter.notifyDataSetChanged();
					}else {
						allShops.clear();
						selectShopAdapter.notifyDataSetChanged();
						mode = 1;
					}
					selectshoplist.onRefreshComplete();
					if (mode == 1) {
						selectshoplist.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
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
		Intent select = null;
		if (activity == null) {
			select = new Intent();
			select.putExtra(Constants.SHOP, allShops.get(arg2-1));
			setResult(RESULT_OK, select);
			finish();
			return;
		}else if (activity.equals("storeOrderActivity")) {
			select = new Intent(SelectShopActivity.this,StoreOrderActivity.class);
		}else if (activity.equals("storeCollectActivity")) {
			select = new Intent(SelectShopActivity.this,StoreCollectActivity.class);
		}else if (activity.equals("messageManageraddActivity")) {
			select = new Intent(SelectShopActivity.this,MessageManageraddActivity.class);
		}else if (activity.equals("messageManangeActivity")) {
			select = new Intent(SelectShopActivity.this,MessageManangeActivity.class);
		}else if (activity.equals("stockCheckRecordActivity")) {
			select = new Intent(SelectShopActivity.this,StockCheckRecordActivity.class);
		}else if (activity.equals("storeReturnGoodsActivity")) {
			select = new Intent(SelectShopActivity.this,StoreReturnGoodsActivity.class);
		}else if (activity.endsWith("logisticsRecordsCheckActivity")) {
			select = new Intent(SelectShopActivity.this,LogisticsRecordsCheckActivity.class);
		}else if (activity.equals("storeAllocationActivity")) {
			select = new Intent(SelectShopActivity.this,StoreAllocationActivity.class);
		}else{
			select = new Intent();
		}
		Bundle bundle=new Bundle();
		bundle.putSerializable("shopVo", allShops.get(arg2-1));
		select.putExtras(bundle);
		setResult(100, select);
		finish();
	
	}
}
