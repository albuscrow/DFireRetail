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
import com.dfire.retail.app.manage.adapter.StoreOrderAdapter;
import com.dfire.retail.app.manage.common.BillStatusDialog;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.OrderGoodsVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.OrderGoodsListBo;
import com.dfire.retail.app.manage.data.bo.PurchaseStatusBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/**
 * 物流管理-门店订货
 * @author ys
 *IOnItemSelectListener
 */
@SuppressLint("SimpleDateFormat")
public class StoreOrderActivity extends TitleActivity implements OnClickListener,OnItemClickListener{
	
	public static StoreOrderActivity instance = null;   
	
	private ImageButton add;
	
	private PullToRefreshListView store_order_lv;
	
	private TextView store_order_time;
	
	private int currentPage = 1;
	
	private List<OrderGoodsVo> orderGoods;
	
	private SelectDateDialog mDateDialog;
	
	private BillStatusDialog billStatusDialog;
	
	private TextView shopTextView,status;
	
	private ShopVo currentShop;
	
	private List<DicVo> dicVos = null;
	
	private AllShopVo allShopVo;
	
	private String shopId;//店铺id
	
	private Integer val;
	
	private Integer pageSize = 0;
	
	private StoreOrderAdapter storeOrderAdapter;
	
	private String selectDate = null;
	
	private Long sendEndTime;
	
	private Integer index;
	
	private int mode;// 判断异步执行完是否禁用加载更多
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_order);
		showBackbtn();
		instance = this;
		this.findView();
		reFreshing();
	}
	
	public void findView(){
		this.shopId = RetailApplication.getShopVo().getShopId();
		this.dicVos = new ArrayList<DicVo>();//状态
		this.orderGoods=new ArrayList<OrderGoodsVo>();
		this.currentShop = RetailApplication.getShopVo();
		this.store_order_time=(TextView)findViewById(R.id.store_order_time);
		this.add=(ImageButton)findViewById(R.id.minus);
		this.shopTextView = (TextView) findViewById(R.id.shopName);
		this.status=(TextView)findViewById(R.id.status);
		this.store_order_lv=(PullToRefreshListView)findViewById(R.id.store_order_lv);
		this.storeOrderAdapter = new StoreOrderAdapter(StoreOrderActivity.this, orderGoods);
		store_order_lv.setAdapter(storeOrderAdapter);
		store_order_lv.setOnItemClickListener(this);
		store_order_lv.setMode(Mode.BOTH);
		new ListAddFooterItem(this, store_order_lv.getRefreshableView());
		//判断是否是总部 登陆的
		if (currentShop.getType() == ShopVo.MENDIAN) {
			setTitleText("门店订货");
			shopTextView.setCompoundDrawables(null, null, null, null);
			shopTextView.setTextColor(Color.parseColor("#666666"));
			add.setVisibility(View.VISIBLE);
			shopTextView.setText(currentShop.getShopName());
		}else{
			setTitleText("订货单查询"); 
			shopTextView.setOnClickListener(this);
			add.setVisibility(View.GONE);
			shopTextView.setText("所有下属门店");
		}
		this.store_order_time.setOnClickListener(this);
		this.add.setOnClickListener(this);
		this.status.setOnClickListener(this);
		// 列表刷新和加载更多操作
		store_order_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StoreOrderActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = 1;//选择以后初始化页数
				getStoreOrderGoods();
			}
			/**
			 * 上拉加载更多
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StoreOrderActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				getStoreOrderGoods();
			}
		});
		this.getStatusList();//获取状态
		this.billStatusDialog = new BillStatusDialog(StoreOrderActivity.this,dicVos);//状态
		this.mDateDialog = new SelectDateDialog(StoreOrderActivity.this,true);//时间
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.store_order_time:
			this.pushDate();
			break;
		case R.id.minus:
			Intent orderAdd =new Intent(StoreOrderActivity.this,StoreOrderAddActivity.class);
			orderAdd.putExtra("shopId", shopId);
			orderAdd.putExtra("orderState", Constants.ORDER_ADD);// 新生订单
			startActivity(orderAdd);
			break;
		case R.id.shopName:
			Intent selectIntent =new Intent(StoreOrderActivity.this,SelectShopActivity.class);
			selectIntent.putExtra("selectShopId", shopId);
			selectIntent.putExtra("activity", "storeOrderActivity");
			startActivityForResult(selectIntent, 100);
			break;
		case R.id.status:
			this.pushStatus();
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
				shopTextView.setText(allShopVo.getShopName());
				shopId = allShopVo.getShopId();
				reFreshing();
			}
		}
	}
	/**
	 * 弹出状态
	 */
	private void pushStatus(){
		billStatusDialog.show();
		billStatusDialog.getmTitle().setText("订单状态");
		billStatusDialog.updateType(val);
		billStatusDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Integer index = billStatusDialog.getCurrentData();
				DicVo dicVo = dicVos.get(index);
				status.setText(dicVo.getName());
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
				store_order_time.setText("请选择");
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
				store_order_time.setText(selectDate);
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
	 * 查询状态 
	 */
	private void getStatusList(){
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "orderGoods/statusList");
		new AsyncHttpPost(this, params, PurchaseStatusBo.class,false, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				PurchaseStatusBo bo = (PurchaseStatusBo) oj;
					if (bo!=null) {
					List<DicVo> vos = new ArrayList<DicVo>();
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
		store_order_lv.setMode(Mode.PULL_FROM_START);
//		getStoreOrderGoods();
		store_order_lv.setRefreshing();
	}
	/**
	 * 查询订货列表
	 */
	private void getStoreOrderGoods() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "orderGoods/list");
		params.setParam("shopId", shopId);
		params.setParam("currentPage", currentPage);
		params.setParam("billStatus", val);
		params.setParam("sendEndTime", sendEndTime);
		new AsyncHttpPost(this, params, OrderGoodsListBo.class,false,new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				OrderGoodsListBo bo = (OrderGoodsListBo)oj;
				if (bo!=null) {
					List<OrderGoodsVo> orders = bo.getOrderGoodsList();
					pageSize = bo.getPageSize();
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							orderGoods.clear();
						}
						if (orders != null && orders.size() > 0) {
							store_order_lv.setMode(Mode.BOTH);
							orderGoods.addAll(orders);
						}else {
							mode = 1;
						}
						storeOrderAdapter.notifyDataSetChanged();
					}else {
						orderGoods.clear();
						storeOrderAdapter.notifyDataSetChanged();
						mode = 1;
					}
					store_order_lv.onRefreshComplete();
					if (mode == 1) {
						store_order_lv.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
				}
			}
			@Override
			public void onFail(Exception e) {
				store_order_lv.onRefreshComplete();
			}
		}).execute();
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		OrderGoodsVo goodsVo = orderGoods.get(position-1);
		index = position-1;
		Integer billStatus = goodsVo.getBillStatus();
		/**
		 * 未确认的状态下。可以修改订单数量，也可以删除 添加
		 */
		Intent orderAIntent =new Intent(StoreOrderActivity.this,StoreOrderAddActivity.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable("orderGoods", goodsVo);
		orderAIntent.putExtras(bundle);
		if (billStatus==1) {
			if (currentShop.getType() == ShopVo.DANDIAN || currentShop.getType() == ShopVo.MENDIAN) {//判断  是 总部 还是分店
				orderAIntent.putExtra("orderState", Constants.UNRECOGNIZED);// 未确定订单
			}else{
				if (currentShop.getParentId()!=null) {
					orderAIntent.putExtra("orderState", Constants.CONFIRMATION_AFTER);// 分公司 查看订单详情
				}else {
					orderAIntent.putExtra("orderState", Constants.CONFIRMATION);// 总部登陆  确认订单
				}
			}
		}else if(billStatus==2){
			orderAIntent.putExtra("orderState", Constants.CONFIRMATION_AFTER);// 确认后订单查看详情
		}
		orderAIntent.putExtra("shopId", shopId);
		startActivity(orderAIntent);
	}
	/**
	 * 订单创建以后弹出提示信息
	 */
	public void pullDig(String id){
		new ErrDialog(this, getResources().getString(R.string.LM_MSG_000026)+id).show();
	}
	/**
	 * 总部确认订单后提示信息
	 * 总部确认以后到进货单里面去了
	 */
	public void pullDigCenter(String id){
		new ErrDialog(this, getResources().getString(R.string.LM_MSG_000027)+id).show();
	}
	/**
	 * 修改要求到货时间以后刷新当前项的时间
	 */
	public void changeDate(String date){
		orderGoods.get(index).setSendEndTime(date);
		storeOrderAdapter.notifyDataSetChanged();
	}
}