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
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.logisticmanager.SelectShopActivity;
import com.dfire.retail.app.manage.adapter.StockCheckRecordAdapter;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.StockCheckRecordVo;
import com.dfire.retail.app.manage.data.bo.CheckStockRecordListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/**
 * 库存管理-盘点记录
 * @author ys
 *
 */
@SuppressLint("SimpleDateFormat")
public class StockCheckRecordActivity extends TitleActivity implements OnClickListener,OnItemClickListener{
	
	private List<StockCheckRecordVo> stockCheckRecordList;
	
	private PullToRefreshListView inventory_records_lv;
	
	private int currentPage=1;
	
	private TextView shopName,select_time;
	
	private ShopVo currentShop;
	
	private String shopId;
	
	private SelectDateDialog mDateDialog;
	
	private String selectDate = null;
	
	private Long sendEndTime;
	
	private Integer pageSize = 0;
	
	private AllShopVo allShopVo;
	
	private StockCheckRecordAdapter stockCheckRecordAdapter;
	
	private int mode;// 判断异步执行完是否禁用加载更多
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_check_records);
		showBackbtn();
		setTitleText("盘点记录");
		mRight.setImageResource(R.drawable.export);
		mRight.setVisibility(View.VISIBLE);
		mRight.setOnClickListener(this);
		this.findView();
		inventory_records_lv.setMode(Mode.PULL_FROM_START);
		getStockQuery();
		inventory_records_lv.setRefreshing();
	}
	public void findView(){
		this.shopId = RetailApplication.getShopVo().getShopId();
		this.currentShop = RetailApplication.getShopVo();
		this.stockCheckRecordList = new ArrayList<StockCheckRecordVo>();
		this.select_time = (TextView)findViewById(R.id.select_time);
		this.shopName = (TextView)findViewById(R.id.shopName);
		this.select_time.setOnClickListener(this);
		this.inventory_records_lv =( PullToRefreshListView)findViewById(R.id.inventory_records_lv);
		this.stockCheckRecordAdapter = new StockCheckRecordAdapter(StockCheckRecordActivity.this, stockCheckRecordList);
		
		this.inventory_records_lv.setAdapter(stockCheckRecordAdapter);
		this.inventory_records_lv.setOnItemClickListener(this);
		this.inventory_records_lv.setMode(Mode.BOTH);
		new ListAddFooterItem(this, inventory_records_lv.getRefreshableView());
		// 列表刷新和加载更多操作
		this.inventory_records_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StockCheckRecordActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = 1;//选择以后初始化页数
				getStockQuery();
			}
			/**
			 * 上拉加载更多
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StockCheckRecordActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				getStockQuery();
			}
		});
		//判断是否是总部 登陆的
		if (currentShop.getType() == ShopVo.DANDIAN || currentShop.getType() == ShopVo.MENDIAN) {
			this.shopName.setCompoundDrawables(null, null, null, null);
			this.shopName.setText(currentShop.getShopName());
			this.shopName.setTextColor(Color.parseColor("#666666"));
		}else{
			this.shopName.setOnClickListener(this);
			this.shopName.setText("所有门店");
		}
		this.mDateDialog = new SelectDateDialog(StockCheckRecordActivity.this,true);//时间
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_time:
			pushDate();
			break;
		case R.id.shopName:
			Intent selectIntent =new Intent(StockCheckRecordActivity.this,SelectShopActivity.class);
			selectIntent.putExtra("selectShopId", shopId);
			selectIntent.putExtra("activity", "stockCheckRecordActivity");
			startActivityForResult(selectIntent, 100);
			break;
		case R.id.title_right:/**导出*/
			Intent exportIntent =new Intent(StockCheckRecordActivity.this,StockExportActivity.class);
			exportIntent.putExtra("shopId", shopId);
			exportIntent.putExtra("sendTime", sendEndTime);
			startActivity(exportIntent);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){
			Bundle b=data.getExtras();
			allShopVo=(AllShopVo)b.getSerializable("shopVo");
			if (allShopVo!=null) {
				shopName.setText(allShopVo.getShopName());
				shopId = allShopVo.getShopId();
				reFreshing();
			}
		}
	}
	/**
	 * 弹出日期
	 */
	private void pushDate(){
		mDateDialog.show();
		mDateDialog.getTitle().setText("要求到货日期");
		mDateDialog.updateDays(selectDate);
		mDateDialog.getmClearDate().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDateDialog.dismiss();
				select_time.setText("请选择");
				sendEndTime = null;
				reFreshing();
			}
			
		});
		
		mDateDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDateDialog.dismiss();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");
				selectDate = mDateDialog.getCurrentData();
				select_time.setText(selectDate);
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
		mDateDialog.getCancelButton().setOnClickListener(new OnClickListener() {
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
		inventory_records_lv.setMode(Mode.PULL_FROM_START);
		getStockQuery();
		inventory_records_lv.setRefreshing();
	}
	
	/**
	 * 盘点记录列表
	 */
	private void getStockQuery() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "checkStockRecord/list");
		params.setParam("shopId", shopId);
		params.setParam("currentPage", currentPage);
		params.setParam("starttime", sendEndTime);
		params.setParam("endtime", sendEndTime);
		new AsyncHttpPost(this, params, CheckStockRecordListBo.class,false, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				CheckStockRecordListBo bo = (CheckStockRecordListBo)oj;
				if (bo!=null) {
					List<StockCheckRecordVo> checkRecordVos = new ArrayList<StockCheckRecordVo>();
					checkRecordVos = bo.getStockCheckRecordList();
					pageSize = bo.getPageCount();
					
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							stockCheckRecordList.clear();
						}
						if (checkRecordVos != null && checkRecordVos.size() > 0) {
							inventory_records_lv.setMode(Mode.BOTH);
							stockCheckRecordList.addAll(checkRecordVos);
						}else {
							mode = 1;
						}
						stockCheckRecordAdapter.notifyDataSetChanged();
					}else {
						stockCheckRecordList.clear();
						stockCheckRecordAdapter.notifyDataSetChanged();
						mode = 1;
					}
					inventory_records_lv.onRefreshComplete();
					if (mode == 1) {
						inventory_records_lv.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
				}
			}
			@Override
			public void onFail(Exception e) {
				inventory_records_lv.onRefreshComplete();
			}
		}).execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		StockCheckRecordVo checkRecordVo = stockCheckRecordList.get(position-1);
		Intent intent = new Intent(StockCheckRecordActivity.this,StockCheckRecordResultActivity.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable("checkRecordVo", checkRecordVo);
		intent.putExtras(bundle);
		intent.putExtra("shopId", shopId);
		startActivity(intent);
	}
}
