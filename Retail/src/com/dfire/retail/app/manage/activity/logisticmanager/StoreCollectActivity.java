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
import com.dfire.retail.app.manage.adapter.StoreCollectAdapter;
import com.dfire.retail.app.manage.common.BillStatusDialog;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.StockInVo;
import com.dfire.retail.app.manage.data.bo.PurchaseListvBo;
import com.dfire.retail.app.manage.data.bo.PurchaseStatusBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/**
 * 物流管理-门店进货
 * @author ys
 *
 */
@SuppressLint("SimpleDateFormat")
public class StoreCollectActivity extends TitleActivity implements OnClickListener,OnItemClickListener{
	
	public static StoreCollectActivity instance = null;
	
	private ImageButton add;
	
	private PullToRefreshListView store_collect_lv;
	
	private TextView store_collect_time;
	
	private int currentPage = 1;
	
	private List<StockInVo> stockInList;
	
	private TextView shopTextView,status;
	
	private ShopVo currentShop;
	
	private Integer pageSize = 0;
	
	private StoreCollectAdapter storeCollectAdapter;
	
	private SelectDateDialog mDateDialog;
	
	private BillStatusDialog billStatusDialog;
	
	private List<DicVo> dicVos = null;
	
	private Integer val;
	
	private String selectDate = null;
	
	private Long sendEndTime;
	
	private String shopId;//店铺id
	
	private AllShopVo allShopVo;
	
	private int mode;// 判断异步执行完是否禁用加载更多
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_collect);
		setTitleText("门店进货");
		instance = this;
		showBackbtn();
		findView();
		// 设置列表的刷新模式为下拉刷新，同时进行刷新操作
		store_collect_lv.setMode(Mode.PULL_FROM_START);
		getStoreCollect();
		store_collect_lv.setRefreshing();
	}
	
	public void findView(){
		this.shopId = RetailApplication.getShopVo().getShopId();
		this.dicVos = new ArrayList<DicVo>();//状态
		this.stockInList=new ArrayList<StockInVo>();
		this.store_collect_lv=(PullToRefreshListView)findViewById(R.id.store_collect_lv);
		this.store_collect_time=(TextView)findViewById(R.id.store_collect_time);
		this.shopTextView = (TextView) findViewById(R.id.shopName);
		this.status=(TextView)findViewById(R.id.status);
		this.currentShop = RetailApplication.getShopVo();
		this.store_collect_time.setOnClickListener(this);
		this.add=(ImageButton)findViewById(R.id.minus);
		
		this.status.setOnClickListener(this);
		this.add.setOnClickListener(this);
		if (RetailApplication.getEntityModel()==1) {
			//单店
			this.shopTextView.setCompoundDrawables(null, null, null, null);
			this.shopTextView.setText(currentShop.getShopName());
			this.shopTextView.setTextColor(Color.parseColor("#666666"));
			this.add.setVisibility(View.VISIBLE);
		}else {
			//连锁
			if (currentShop.getType() == ShopVo.MENDIAN) {
				this.shopTextView.setCompoundDrawables(null, null, null, null);
				this.shopTextView.setTextColor(Color.parseColor("#666666"));
				this.add.setVisibility(View.VISIBLE);
				this.shopTextView.setText(currentShop.getShopName());
			}else{
				setTitleText("进货单查询");
				this.shopTextView.setText("所有下属门店");
				this.shopTextView.setOnClickListener(this);
				this.add.setVisibility(View.GONE);
				
			}
		}
		this.storeCollectAdapter = new StoreCollectAdapter(StoreCollectActivity.this, stockInList);
		this.store_collect_lv.setAdapter(storeCollectAdapter);
		this.store_collect_lv.setOnItemClickListener(this);
		this.store_collect_lv.setMode(Mode.BOTH);
		new ListAddFooterItem(this,store_collect_lv.getRefreshableView());
		// 列表刷新和加载更多操作
		this.store_collect_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StoreCollectActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = 1;//选择以后初始化页数
				getStoreCollect();
			}
			/**
			 * 上拉加载更多
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StoreCollectActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				getStoreCollect();
			}
		});
		this.getStatusList();//获取状态
		this.billStatusDialog = new BillStatusDialog(StoreCollectActivity.this,dicVos);//状态
		this.mDateDialog = new SelectDateDialog(StoreCollectActivity.this,true);//时间
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.store_collect_time:
			this.pushDate();
			break;
		case R.id.minus:
			Intent collectIntent=new Intent(StoreCollectActivity.this,StoreCollectAddActivity.class);
			collectIntent.putExtra("shopId", shopId);
			collectIntent.putExtra("collectState", Constants.COLLECT_ADD);//配送中
			startActivity(collectIntent);
			break;
		case R.id.status:
			this.pushStatus();
			break;
		case R.id.shopName:
			Intent selectIntent =new Intent(StoreCollectActivity.this,SelectShopActivity.class);
			selectIntent.putExtra("selectShopId", shopId);
			selectIntent.putExtra("activity", "storeCollectActivity");
			startActivityForResult(selectIntent, 100);
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
				this.shopTextView.setText(allShopVo.getShopName());
				this.shopId = allShopVo.getShopId();
				reFreshing();
			}
		}
	}
	/**
	 * 弹出状态
	 */
	private void pushStatus(){
		this.billStatusDialog.show();
		this.billStatusDialog.getmTitle().setText("进货状态");
		this.billStatusDialog.updateType(val);
		this.billStatusDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				billStatusDialog.dismiss();
				Integer index = billStatusDialog.getCurrentData();
				DicVo dicVo = dicVos.get(index);
				status.setText(dicVo.getName());
				val = dicVo.getVal();
				reFreshing();
			}
		});
		this.billStatusDialog.getCancelButton().setOnClickListener(new OnClickListener() {
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
		this.mDateDialog.show();
		this.mDateDialog.getTitle().setText("要求到货日期");
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
					reFreshing();
				}
				
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
		store_collect_lv.setMode(Mode.PULL_FROM_START);
		getStoreCollect();
		store_collect_lv.setRefreshing();
	}
	/**
	 * 查询状态 
	 */
	private void getStatusList(){
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "purchase/statusList");
		new AsyncHttpPost(this, params, PurchaseStatusBo.class,false, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				PurchaseStatusBo bo = (PurchaseStatusBo) oj;
				if (bo!=null) {
					List<DicVo> vos = bo.getStatusList();
					if (vos != null && vos.size()>0) {
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
	 * 获取进货列表
	 */
	private void getStoreCollect() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "purchase/list");
		params.setParam("shopId", shopId);
		params.setParam("currentPage", currentPage);
		params.setParam("billStatus", val);
		params.setParam("sendEndTime", sendEndTime);
		new AsyncHttpPost(this, params, PurchaseListvBo.class,false,new RequestCallback(){
			@Override
			public void onSuccess(Object object) {
				PurchaseListvBo bo = (PurchaseListvBo)object;
				if (bo!=null) {
					List<StockInVo> list = bo.getStockInList();
					pageSize = bo.getPageSize();
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							stockInList.clear();
						}
						if (list != null && list.size() > 0) {
							store_collect_lv.setMode(Mode.BOTH);
							stockInList.addAll(list);
						}else {
							mode = 1;
						}
						storeCollectAdapter.notifyDataSetChanged();
					}else {
						stockInList.clear();
						storeCollectAdapter.notifyDataSetChanged();
						mode = 1;
					}
					store_collect_lv.onRefreshComplete();
					if (mode == 1) {
						store_collect_lv.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
				}
			}
			@Override
			public void onFail(Exception e) {
				store_collect_lv.onRefreshComplete();
			}}).execute();;
		}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		StockInVo stockInVo = stockInList.get(position-1);
		Integer billStatus = stockInVo.getBillStatus();
		
		Intent collectIntent =new Intent(StoreCollectActivity.this,StoreCollectAddActivity.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable("stockInVo", stockInVo);
		collectIntent.putExtras(bundle);
		
		if (RetailApplication.getEntityModel()==1) {//单店
			if (billStatus==1) {
				collectIntent.putExtra("collectState", Constants.STORE_COLLECT_DISTRIBUTION);//配送中  可以看到进货价  也可以修改 添加 确定 拒绝 
			}else{
				collectIntent.putExtra("collectState", Constants.STORE_COLLECT_RECEIVING );//单店 查看 可以看到进货价
			}
		}else {//连锁
			if (currentShop.getType() == ShopVo.DANDIAN || currentShop.getType() == ShopVo.MENDIAN) {
				if (billStatus==1) {
					collectIntent.putExtra("collectState", Constants.COLLECT_DISTRIBUTION);//连锁门店  配送中 不能看到进货价 可以 修改 添加 确定 拒绝
				}else{
					collectIntent.putExtra("collectState", Constants.COLLECT_RECEIVING);//连锁门店  不能看到进货价  只能查看 确定 和拒绝的 
				}
			}else{
				if (currentShop.getParentId()!=null) {
					collectIntent.putExtra("collectState", Constants.COLLECT_RECEIVING);//分公司  不能看到进货价  只能查看
				}else {
					collectIntent.putExtra("collectState", Constants.STORE_COLLECT_RECEIVING);// 总部 可以看到进货价 但是不能操作进货单 只能查看
				}
			}
		}
		collectIntent.putExtra("shopId", shopId);
		startActivity(collectIntent);
	}
	/**
	 *	进货单创建以后弹出提示信息
	 */
	public void pullDig(String id){
		new ErrDialog(this, getResources().getString(R.string.LM_MSG_000028)+id).show();
	}
}