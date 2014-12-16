/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.dfire.retail.app.manage.adapter.StoreReturnGoodsAdapter;
import com.dfire.retail.app.manage.common.BillStatusDialog;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.PurchaseStatusBo;
import com.dfire.retail.app.manage.data.bo.ReturnGoodsListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.vo.ReturnGoodsVo;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/**
 * 物流管理-门店退货
 * 
 * @author ys 2014-11-12
 */
@SuppressLint("SimpleDateFormat")
public class StoreReturnGoodsActivity extends TitleActivity implements OnClickListener,OnItemClickListener{
	
	public static StoreReturnGoodsActivity instance = null;
	
	public static Boolean isVisPrice = false;
	
	private ImageButton add;
	
	private TextView lsStatus,shop_name;

	private RetailApplication application;

	private TextView date;

	private SelectDateDialog mDateDialog;
	
	private String selectDate = null;
	
	private int currentPage = 1;//初始化页数
	
	private Long sendEndTimeDate;
	
	private BillStatusDialog billStatusDialog;
	
	private List<DicVo> dicVos = new ArrayList<DicVo>();
	
	private List<ReturnGoodsVo> returnGoodsVoList=new ArrayList<ReturnGoodsVo>();

	private List<String> statusNameList = new ArrayList<String>();
	
	private Integer val;
	
	private PullToRefreshListView store_return_goods_lv;
	
	private Integer pageSize = 0;
	
	private StoreReturnGoodsAdapter storeReturnGoodsAdapter;
	
	private ShopVo currentShop;
	
	private String shopId;//店铺id
	
	private AllShopVo allShopVo;
	
	private int mode;// 判断异步执行完是否禁用加载更
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_return_goods);
		application = (RetailApplication) getApplication();
		setTitleText("门店退货");
		instance = this;
		showBackbtn();
		findShopView();
		initData();
		store_return_goods_lv.setMode(Mode.PULL_FROM_START);
		searchStatusList();
		store_return_goods_lv.setRefreshing();
		this.mDateDialog = new SelectDateDialog(StoreReturnGoodsActivity.this, true);// 时间
	}

	private void findShopView() {
		shopId = RetailApplication.getShopVo().getShopId();
		currentShop = RetailApplication.getShopVo();
		date = (TextView) findViewById(R.id.date);
		date.setOnClickListener(this);
		shop_name = (TextView) findViewById(R.id.shop_name);
		add = (ImageButton) findViewById(R.id.add);
		add.setOnClickListener(this);
		lsStatus = (TextView) findViewById(R.id.lsStatus);
		lsStatus.setOnClickListener(this);
		store_return_goods_lv=(PullToRefreshListView)findViewById(R.id.store_return_goods_lv);
		storeReturnGoodsAdapter = new StoreReturnGoodsAdapter(StoreReturnGoodsActivity.this, returnGoodsVoList);
		
		store_return_goods_lv.setAdapter(storeReturnGoodsAdapter);
		store_return_goods_lv.setOnItemClickListener(this);
		store_return_goods_lv.setMode(Mode.BOTH);
		new ListAddFooterItem(this, store_return_goods_lv.getRefreshableView());
		// 列表刷新和加载更多操作
		store_return_goods_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StoreReturnGoodsActivity.this, System.currentTimeMillis(), 
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
				String label = DateUtils.formatDateTime(StoreReturnGoodsActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				initData();
			}
		});
		initView();
	}
	private void initView() {
		
		if (RetailApplication.getEntityModel()==1) {
			//单店
			shop_name.setCompoundDrawables(null, null, null, null);
			shop_name.setTextColor(Color.parseColor("#666666"));
			shop_name.setText(currentShop.getShopName());
			add.setVisibility(View.VISIBLE);
			isVisPrice = true;
		}else {
			if (currentShop.getType() == ShopVo.MENDIAN) {
				shop_name.setCompoundDrawables(null, null, null, null);
				shop_name.setTextColor(Color.parseColor("#666666"));
				add.setVisibility(View.VISIBLE);
				shop_name.setText(currentShop.getShopName());
				isVisPrice = false;
			}else{
				if (currentShop.getParentId()!=null) {
					isVisPrice = false;
				}else {
					isVisPrice = true;
				}
				setTitleText("退货单查询"); 
				shop_name.setOnClickListener(this);
				add.setVisibility(View.GONE);
				shop_name.setText("所有下属门店");
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){
			Bundle b=data.getExtras();
			allShopVo=(AllShopVo)b.getSerializable("shopVo");
			if (allShopVo!=null) {
				shop_name.setText(allShopVo.getShopName());
				shopId = allShopVo.getShopId();
				reFreshing();
			}
		}
	}
	//退货状态查询
	private void searchStatusList() {
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.RETURNGOODS_STATUSLIST);
		new AsyncHttpPost(this, parameters, PurchaseStatusBo.class,false,new RequestCallback() {
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
					billStatusDialog = new BillStatusDialog(StoreReturnGoodsActivity.this, dicVos);// 状态
					vos = (List<DicVo>) bo.getStatusList();
					if (vos.size()>0) {
						dicVos.clear();
						dicVos.addAll(vos);
					}
				}
			}
			@Override
			public void onFail(Exception e) {
			}
		}).execute();
	}
	/**
	 * 加条件以后刷新数据
	 */
	public void reFreshing(){
		currentPage = 1;//选择以后初始化页数
		store_return_goods_lv.setMode(Mode.PULL_FROM_START);
		initData();
		store_return_goods_lv.setRefreshing();
	}
	/** 
	 * 加载退货单号列表
	 */
	private void initData() {
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.RETURN_GOODS_LIST);
		parameters.setParam("shopId", shopId);
		parameters.setParam("currentPage", currentPage);
		parameters.setParam("billStatus", val);
		if (sendEndTimeDate != null) {
			parameters.setParam("sendEndTime", sendEndTimeDate);
		}
		new AsyncHttpPost(this, parameters, ReturnGoodsListBo.class,false, new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				ReturnGoodsListBo bo = (ReturnGoodsListBo)oj;
				if (bo!=null) {
					List<ReturnGoodsVo>	returnGoodsVoList1 = new ArrayList<ReturnGoodsVo>();
					pageSize = bo.getPageSize();
					returnGoodsVoList1 = bo.getReturnGoodsList();
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							returnGoodsVoList.clear();
						}
						if (returnGoodsVoList1 != null && returnGoodsVoList1.size() > 0) {
							store_return_goods_lv.setMode(Mode.BOTH);
							returnGoodsVoList.addAll(returnGoodsVoList1);
						}else {
							mode = 1;
						}
						storeReturnGoodsAdapter.notifyDataSetChanged();
					}else {
						returnGoodsVoList.clear();
						storeReturnGoodsAdapter.notifyDataSetChanged();
						mode = 1;
					}
					store_return_goods_lv.onRefreshComplete();
					if (mode == 1) {
						store_return_goods_lv.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
				}
			}
			
			@Override
			public void onFail(Exception e) {
				store_return_goods_lv.onRefreshComplete();
			}
		}).execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			HashMap<String, List<Object>> map = application.getObjectMap();
			if (map.get("returnGoods") != null) {
				map.get("returnGoods").clear();
				application.setObjectMap(map);
			}
			Intent returnGoods = new Intent(StoreReturnGoodsActivity.this, StoreReturnGoodsAddActivity.class);
			returnGoods.putExtra("shopId", shopId);
			returnGoods.putExtra("operation", "add");
			startActivity(returnGoods);
			break;
		case R.id.date:
			pushDate();
			break;
		case R.id.shop_name:
			Intent selectIntent =new Intent(StoreReturnGoodsActivity.this,SelectShopActivity.class);
			selectIntent.putExtra("selectShopId", shopId);
			selectIntent.putExtra("activity", "storeReturnGoodsActivity");
			startActivityForResult(selectIntent, 100);
			break;
		case R.id.lsStatus:
			pushStatus();
			break;
		}
	}

	private void pushDate(){
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
				if (selectDate != null) {
					try {
						sendEndTimeDate = (sdf.parse((selectDate + " 00:00:00"))).getTime();
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
	private void pushStatus() {
		billStatusDialog.show();
		billStatusDialog.getmTitle().setText("订单状态");
		billStatusDialog.updateType(val);
		billStatusDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Integer index = billStatusDialog.getCurrentData();
				DicVo dicVo = dicVos.get(index);
				lsStatus.setText(dicVo.getName());
				val = dicVo.getVal();
				reFreshing();
				billStatusDialog.dismiss();
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ReturnGoodsVo returnGoodsVo = returnGoodsVoList.get(position-1);
		Integer billStatus = returnGoodsVo.getBillStatus();
		
		Intent listDetail = new Intent(StoreReturnGoodsActivity.this, StoreReturnGoodsAddActivity.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable("returnGoodsVo", returnGoodsVo);
		listDetail.putExtras(bundle);
		listDetail.putExtra("shopId", shopId);
		if (RetailApplication.getEntityModel()==1) {//单店
			if (billStatus==1) {//退货中
				listDetail.putExtra("operation", "edit");
			}else {//已退货 已拒绝
				listDetail.putExtra("operation", "see");					
			}
		}else {//连锁
			if (currentShop.getType() == ShopVo.DANDIAN || currentShop.getType() == ShopVo.MENDIAN) {//连锁门店
				if (billStatus==1) {//退货中
					listDetail.putExtra("operation", "edit");
				}else {//已退货 已拒绝
					listDetail.putExtra("operation", "see");					
				}
			}else{
				if (currentShop.getParentId()!=null) {//连锁分公司
					listDetail.putExtra("operation", "see");
				}else {//连锁总公司
					if (billStatus==1) {//退货中  可以确认退货 
						listDetail.putExtra("operation", "receiptOrrefuse");
					}else {//已退货 已拒绝
						listDetail.putExtra("operation", "see");					
					}
				}
			}
		}
		startActivity(listDetail);
	}
	/**
	 *	退货单创建以后弹出提示信息
	 */
	public void pullDig(String id){
		new ErrDialog(this, getResources().getString(R.string.LM_MSG_000030)+id).show();
	}
}
