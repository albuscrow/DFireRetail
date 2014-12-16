package com.dfire.retail.app.manage.activity.logisticmanager;

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
import com.dfire.retail.app.manage.adapter.StoreAllocationAdapter;
import com.dfire.retail.app.manage.common.BillStatusDialog;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.AllocateListBo;
import com.dfire.retail.app.manage.data.bo.PurchaseStatusBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.vo.AllocateVo;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/** 
 * 物流管理-门店调拨
 * @author ys 2014-11-15
 */
@SuppressLint("SimpleDateFormat")
public class StoreAllocationActivity extends TitleActivity implements OnClickListener,OnItemClickListener {
	
	public static StoreAllocationActivity instance = null;
	
	private ImageButton add;

	private TextView date;

	private List<AllocateVo> allocateVoList;

	private List<DicVo> dicVos = new ArrayList<DicVo>();

	private List<String> statusNameList = new ArrayList<String>();

	private SelectDateDialog mDateDialog;

	private Long sendEndTimeDate;

	private BillStatusDialog billStatusDialog;

	private Integer val;

	private String selectDate = null;
	
	private PullToRefreshListView store_allocation_lv;
	
	private StoreAllocationAdapter storeAllocationAdapter;
	
	private int currentPage = 1;//初始化页数
	
	private Integer pageSize = 0;
	
	private TextView shopName,lsStatus;
	
	private ShopVo currentShop;
	
	private String shopId;
	
	private AllShopVo allShopVo;
	
	private int mode;// 判断异步执行完是否禁用加载更多
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_allocation);
		setTitleText("门店调拨");
		instance = this;
		showBackbtn();
		findShopView();
		store_allocation_lv.setMode(Mode.PULL_FROM_START);
		initData();
		store_allocation_lv.setRefreshing();
	}

	private void findShopView() {
		allocateVoList = new ArrayList<AllocateVo>();
		this.shopId =  RetailApplication.getShopVo().getShopId();
		this.currentShop = RetailApplication.getShopVo();
		this.date = (TextView) findViewById(R.id.date);
		this.date.setOnClickListener(this);
		this.shopName = (TextView) findViewById(R.id.shopName);
		this.add = (ImageButton) findViewById(R.id.add);
		this.add.setOnClickListener(this);
		this.lsStatus = (TextView) findViewById(R.id.lsStatus);
		this.lsStatus.setOnClickListener(this);
		this.store_allocation_lv=(PullToRefreshListView)findViewById(R.id.store_allocation_lv);
		this.storeAllocationAdapter = new StoreAllocationAdapter(StoreAllocationActivity.this, allocateVoList);
		
		this.store_allocation_lv.setAdapter(storeAllocationAdapter);
		this.store_allocation_lv.setOnItemClickListener(this);
		this.store_allocation_lv.setMode(Mode.BOTH);
		new ListAddFooterItem(this, store_allocation_lv.getRefreshableView());
		//连锁
		if (currentShop.getType() == ShopVo.MENDIAN) {
			this.shopName.setCompoundDrawables(null, null, null, null);
			this.add.setVisibility(View.VISIBLE);
			this.shopName.setTextColor(Color.parseColor("#666666"));
			this.shopName.setText(currentShop.getShopName());
		}else{
			this.shopName.setText("所有下属门店");
			this.shopName.setOnClickListener(this);
			this.add.setVisibility(View.GONE);
			
		}
		searchStatusList();
		mDateDialog = new SelectDateDialog(StoreAllocationActivity.this, true);// 时间
		// 列表刷新和加载更多操作
		this.store_allocation_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StoreAllocationActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = 1;//选择以后初始化页数
				initData();
			}
			/**
			 * 上拉加载更多
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StoreAllocationActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				initData();
			}
		});
	}
	
	/**
	 * 加条件以后刷新数据
	 */
	public void reFreshing(){
		currentPage = 1;//选择以后初始化页数
		store_allocation_lv.setMode(Mode.PULL_FROM_START);
		initData();
		store_allocation_lv.setRefreshing();
	}
	/**获取调拨列表*/
	private void initData() {
		// 传递请求参数
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.ALLOCATE_LIST);
		parameters.setParam("shopId", shopId);
		parameters.setParam("billStatus", val);
		parameters.setParam("currentPage", currentPage);
		if (sendEndTimeDate != null) {
			parameters.setParam("sendEndTime", sendEndTimeDate);
		}
		new AsyncHttpPost(this, parameters, AllocateListBo.class,false, new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				AllocateListBo bo = (AllocateListBo)oj;
				if (bo!=null) {
					List<AllocateVo> allocateVoList1 = new ArrayList<AllocateVo>();
					pageSize = bo.getPageSize();
					allocateVoList1 = bo.getAllocateList();
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							allocateVoList.clear();
						}
						if (allocateVoList1 != null && allocateVoList1.size() > 0) {
							store_allocation_lv.setMode(Mode.BOTH);
							allocateVoList.addAll(allocateVoList1);
						}else {
							mode = 1;
						}
						storeAllocationAdapter.notifyDataSetChanged();
					}else {
						allocateVoList.clear();
						storeAllocationAdapter.notifyDataSetChanged();
						mode = 1;
					}
					store_allocation_lv.onRefreshComplete();
					if (mode == 1) {
						store_allocation_lv.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
				}
			}
			
			@Override
			public void onFail(Exception e) {
				store_allocation_lv.onRefreshComplete();
			}
		}).execute();
	}
	/**获取调拨状态*/
	private void searchStatusList() {

		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.ALLOCATE_STATUSLIST);
		new AsyncHttpPost(this, parameters, PurchaseStatusBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				PurchaseStatusBo bo = (PurchaseStatusBo) oj;
				if (bo!=null) {
					List<DicVo> vos = new ArrayList<DicVo>();
					vos = bo.getStatusList();
					dicVos.clear();
					statusNameList.clear();
					for (DicVo dicVo : vos) {
						statusNameList.add(dicVo.getName());
						dicVos.add(dicVo);
					}
					billStatusDialog = new BillStatusDialog(StoreAllocationActivity.this, dicVos);// 状态
				}
			}
			@Override
			public void onFail(Exception e) {
			}
		}).execute();
	}

	/**弹出状态*/
	private void pushStatus() {
		billStatusDialog.show();
		billStatusDialog.getmTitle().setText("调拨状态");
		billStatusDialog.updateType(val);
		billStatusDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				billStatusDialog.dismiss();
				Integer index = billStatusDialog.getCurrentData();
				DicVo dicVo = dicVos.get(index);
				lsStatus.setText(dicVo.getName());
				val = dicVo.getVal();
				reFreshing();
			}
		});
		billStatusDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				billStatusDialog.dismiss();
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){
			Bundle b=data.getExtras();
			allShopVo=(AllShopVo)b.getSerializable("shopVo");
			if (allShopVo!=null) {
				this.shopName.setText(allShopVo.getShopName());
				this.shopId = allShopVo.getShopId();
				reFreshing();
			}
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.add:
				Intent returnGoods = new Intent(StoreAllocationActivity.this, StoreAllocationAddActivity.class);
				returnGoods.putExtra("shopName", shopName.getText().toString()+"");
				returnGoods.putExtra("allocation", "add");
				returnGoods.putExtra("shopId", shopId);
				startActivity(returnGoods);
				break;
			case R.id.shopName:
				Intent selectIntent =new Intent(StoreAllocationActivity.this,SelectShopActivity.class);
				selectIntent.putExtra("selectShopId", shopId);
				selectIntent.putExtra("activity", "storeAllocationActivity");
				startActivityForResult(selectIntent, 100);
				break;
			case R.id.lsStatus:
				pushStatus();
				break;
			case R.id.date:
				pushDate();
				break;		
		}
	}

	private void pushDate() {
		mDateDialog.show();
		mDateDialog.getTitle().setText("要求调拨日期");
		mDateDialog.updateDays(selectDate);
		mDateDialog.getmClearDate().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				mDateDialog.dismiss();
				date.setText("请选择");
				sendEndTimeDate = null;
				reFreshing();
			}

		});

		mDateDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDateDialog.dismiss();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");
				selectDate = mDateDialog.getCurrentData();
				date.setText(selectDate);
				if (selectDate!=null) {
					try {
						sendEndTimeDate = (sdf.parse((selectDate+" 00:00:00"))).getTime();
					} catch (ParseException e) {
						sendEndTimeDate = null;
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
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AllocateVo allocateVo = allocateVoList.get(position-1);
		Intent listDetail = new Intent(StoreAllocationActivity.this, StoreAllocationAddActivity.class);
		listDetail.putExtra("shopId", shopId);
		if (allocateVo!=null) {
			if (allocateVo.getBillStatus()==1) {//调拨中
				if (RetailApplication.getShopVo().getType()==ShopVo.FENGBU||RetailApplication.getShopVo().getType()==ShopVo.ZHONGBU) {
					listDetail.putExtra("allocation", "see");
				}else {
					listDetail.putExtra("allocation", "noadd");
				}
			}else{
				listDetail.putExtra("allocation", "see");
			}
		}
		listDetail.putExtra("allocateVo", allocateVo);
		startActivity(listDetail);
	}
	/**
	 *	调拨单创建以后弹出提示信息
	 */
	public void pullDig(String id){
		new ErrDialog(this, getResources().getString(R.string.LM_MSG_000029)+id).show();
	}
}
