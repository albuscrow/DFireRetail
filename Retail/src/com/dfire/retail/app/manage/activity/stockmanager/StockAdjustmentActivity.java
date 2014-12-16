package com.dfire.retail.app.manage.activity.stockmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.StockAdjustmentAdapter;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.StockAdjustVo;
import com.dfire.retail.app.manage.data.bo.StockAdjustListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/**
 * 库存管理-库存调整
 * @author ys
 *
 */
@SuppressLint("SimpleDateFormat")
public class StockAdjustmentActivity extends TitleActivity implements OnClickListener,OnItemClickListener{
	
	public static StockAdjustmentActivity instance = null;
	
	public static Boolean isVisPrice = false;
	
	private ImageButton add;
	
	private PullToRefreshListView stock_adjust_lv;
	
	private TextView store_collect_time;
	
	private int currentPage = 1;
	
	private List<StockAdjustVo> stockAdjustVos;
	
	private TextView shopTextView;
	
	private ShopVo currentShop;
	
	private Integer pageSize = 0;
	
	private StockAdjustmentAdapter adjustmentAdapter;
	
	private SelectDateDialog mDateDialog;
	
	private String selectDate = null;
	
	private Long sendEndTime;
	
	private String shopId;//店铺id
	
	private AllShopVo allShopVo;
	
	private int mode;// 判断异步执行完是否禁用加载更多
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_adjustment);
		setTitleText("库存调整");
		instance = this;
		showBackbtn();
		findView();
		stock_adjust_lv.setMode(Mode.PULL_FROM_START);
		getStockAdjustmentList();
		stock_adjust_lv.setRefreshing();
	}
	
	public void findView(){
		this.shopId = RetailApplication.getShopVo().getShopId();
		new ArrayList<DicVo>();
		this.stockAdjustVos=new ArrayList<StockAdjustVo>();
		this.stock_adjust_lv=(PullToRefreshListView)findViewById(R.id.stock_adjust_lv);
		this.store_collect_time=(TextView)findViewById(R.id.store_collect_time);
		this.shopTextView = (TextView) findViewById(R.id.shopName);
		this.currentShop = RetailApplication.getShopVo();
		this.store_collect_time.setOnClickListener(this);
		this.add=(ImageButton)findViewById(R.id.minus);
		
		this.add.setOnClickListener(this);
		if (RetailApplication.getEntityModel()==1) {
			//单店
			this.shopTextView.setCompoundDrawables(null, null, null, null);
			this.shopTextView.setText(currentShop.getShopName());
			this.shopTextView.setTextColor(Color.parseColor("#666666"));
			isVisPrice = true;
		}else {
			//连锁
			if (currentShop.getType() == ShopVo.MENDIAN) {
				this.shopTextView.setCompoundDrawables(null, null, null, null);
				this.shopTextView.setText(currentShop.getShopName());
				this.shopTextView.setTextColor(Color.parseColor("#666666"));
				isVisPrice = false;
			}else{
				if (currentShop.getParentId()!=null) {
					isVisPrice = false;
				}else {
					isVisPrice = true;
				}
				this.shopTextView.setText("请选择");
				this.shopTextView.setOnClickListener(this);
				
			}
		}
		this.adjustmentAdapter = new StockAdjustmentAdapter(StockAdjustmentActivity.this, stockAdjustVos);
		this.stock_adjust_lv.setAdapter(adjustmentAdapter);
		this.stock_adjust_lv.setOnItemClickListener(this);
		this.stock_adjust_lv.setMode(Mode.BOTH);
		new ListAddFooterItem(this, stock_adjust_lv.getRefreshableView());
		// 列表刷新和加载更多操作
		this.stock_adjust_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StockAdjustmentActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = 1;//选择以后初始化页数
				getStockAdjustmentList();
			}
			/**
			 * 上拉加载更多
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StockAdjustmentActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				getStockAdjustmentList();
			}
		});
		
		this.mDateDialog = new SelectDateDialog(StockAdjustmentActivity.this,true);//时间
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.store_collect_time:
			this.pushDate();
			break;
		case R.id.minus:
			String shopName = shopTextView.getText().toString();
			if (!StringUtils.isEquals(shopName,"请选择")) {
				Intent adjustmentIntent=new Intent(StockAdjustmentActivity.this,StockAdjustmentAddActivity.class);
				adjustmentIntent.putExtra("adjustmentStatue", "add");
				adjustmentIntent.putExtra("shopName", shopName);
				adjustmentIntent.putExtra("shopId", shopId);
				startActivity(adjustmentIntent);
			}else {
				new ErrDialog(this, getResources().getString(R.string.please_select_shop)).show();
			}
			break;
		case R.id.shopName:
			Intent selectIntent =new Intent(StockAdjustmentActivity.this,AdjusSelectShopActivity.class);
			selectIntent.putExtra("selectShopId", shopId);
			startActivityForResult(selectIntent, 100);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){
		    allShopVo = (AllShopVo)data.getSerializableExtra("shopVo");
			if (allShopVo!=null) {
				this.shopTextView.setText(allShopVo.getShopName());
				this.shopId = allShopVo.getShopId();
				reFreshing();
			}
		}
	}
	 
	/**
	 * 弹出日期
	 */
	private void pushDate(){
		this.mDateDialog.show();
		this.mDateDialog.getTitle().setText("调整日期");
		this.mDateDialog.updateDays(selectDate);
		this.mDateDialog.getmClearDate().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDateDialog.dismiss();
				store_collect_time.setText("请选择");
				sendEndTime = null;
				reFreshing();
			}
			
		});
		
		this.mDateDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDateDialog.dismiss();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");
				selectDate = mDateDialog.getCurrentData();
				store_collect_time.setText(selectDate);
				if (selectDate!=null) {
					try {
						sendEndTime = (sdf.parse((selectDate+" 00:00:00"))).getTime();
					} catch (ParseException e) {
						sendEndTime = null;
					}
				}
				reFreshing();
			}
		});
		this.mDateDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDateDialog.dismiss();
			}
		});
	}
	/**
	 * 加条件以后刷新数据
	 */
	public void reFreshing(){
		currentPage = 1;//选择以后初始化页数
		stock_adjust_lv.setMode(Mode.PULL_FROM_START);
		getStockAdjustmentList();
		stock_adjust_lv.setRefreshing();
	}
	/**
	 * 获取库存调整列表
	 */
	private void getStockAdjustmentList() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockAdjust/getStockAdjustList");
		params.setParam("shopId", shopId);
		params.setParam("currentPage", currentPage);
		params.setParam("endtime", sendEndTime);
		params.setParam("starttime", sendEndTime);
		new AsyncHttpPost(this, params, StockAdjustListBo.class,false, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				StockAdjustListBo bo = (StockAdjustListBo)oj;
				if (bo!=null) {
					pageSize = bo.getPageCount();
					List<StockAdjustVo> adjustVos = new ArrayList<StockAdjustVo>();
					adjustVos = bo.getStockInfoList();
					
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							stockAdjustVos.clear();
						}
						if (adjustVos != null && adjustVos.size() > 0) {
							stock_adjust_lv.setMode(Mode.BOTH);
							stockAdjustVos.addAll(adjustVos);
						}else {
							mode = 1;
						}
						adjustmentAdapter.notifyDataSetChanged();
					}else {
						stockAdjustVos.clear();
						adjustmentAdapter.notifyDataSetChanged();
						mode = 1;
					}
					stock_adjust_lv.onRefreshComplete();
					if (mode == 1) {
						stock_adjust_lv.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
				}
			}
			@Override
			public void onFail(Exception e) {
				stock_adjust_lv.onRefreshComplete();
			}
		}).execute();
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		StockAdjustVo stockAdjustVo = stockAdjustVos.get(position-1);
		Intent adjustmentIntent =new Intent(StockAdjustmentActivity.this,StockAdjustmentAddActivity.class);
		adjustmentIntent.putExtra("adjustmentStatue", "noAdd");
		adjustmentIntent.putExtra("shopId", shopId);
		adjustmentIntent.putExtra("shopName", shopTextView.getText().toString());
		Bundle bundle=new Bundle();
		bundle.putSerializable("stockAdjustVo", stockAdjustVo);
		adjustmentIntent.putExtras(bundle);
		startActivity(adjustmentIntent);
	}
}