package com.dfire.retail.app.manage.activity.stockmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.StockAdjustmentAdapter;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.StockAdjustVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

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
	
	private ListView store_collect_lv;
	
	private TextView store_collect_time;
	
	private ProgressDialog progressDialog;
	
	public int currentPage = 1;
	
	public List<StockAdjustVo> stockAdjustVos;
	
	private TextView shopTextView;
	
	private ShopVo currentShop;
	
	private boolean nodata;
	
	private boolean is_divPage;// 是否进行分页操作
	
	private Integer pageSize = 0;
	
	private StockAdjustmentAdapter adjustmentAdapter;
	
	private SelectDateDialog mDateDialog;
	
	private String selectDate = null;
	
	private Long sendEndTime;
	
	private String shopId;//店铺id
	
	private AllShopVo allShopVo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_adjustment);
		setTitleText("库存调整");
		instance = this;
		showBackbtn();
		findView();
	}
	
	public void findView(){
		this.shopId = RetailApplication.getShopVo().getShopId();
		new ArrayList<DicVo>();
		this.stockAdjustVos=new ArrayList<StockAdjustVo>();
		this.store_collect_lv=(ListView)findViewById(R.id.store_collect_lv);
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
			this.add.setVisibility(View.VISIBLE);
			isVisPrice = true;
		}else {
			//连锁
			if (currentShop.getType() == ShopVo.DANDIAN || currentShop.getType() == ShopVo.MENDIAN) {
				this.shopTextView.setCompoundDrawables(null, null, null, null);
				this.add.setVisibility(View.VISIBLE);
				this.shopTextView.setText(currentShop.getShopName());
				isVisPrice = false;
			}else{
				if (currentShop.getParentId()!=null) {
					isVisPrice = false;
				}else {
					isVisPrice = true;
				}
				this.shopTextView.setText("请选择");
				this.shopTextView.setOnClickListener(this);
				this.add.setVisibility(View.GONE);
				
			}
		}
		this.adjustmentAdapter = new StockAdjustmentAdapter(StockAdjustmentActivity.this, stockAdjustVos);
		this.store_collect_lv.setAdapter(adjustmentAdapter);
		this.store_collect_lv.setOnItemClickListener(StockAdjustmentActivity.this);
		//滚动 加载更多
		this.store_collect_lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {//下拉分页
				 if (is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (nodata) {
						currentPage++;
						getStockAdjustmentList();
					}
				 }
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});
		
		this.progressDialog = new ProgressDialog(StockAdjustmentActivity.this);
		this.progressDialog.setCancelable(false);
		this.progressDialog.setCanceledOnTouchOutside(false);
		this.progressDialog.setMessage("加载中，请稍后。。。");
		this.mDateDialog = new SelectDateDialog(StockAdjustmentActivity.this,true);//时间
		
		getStockAdjustmentList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.store_collect_time:
			this.pushDate();
			break;
		case R.id.minus:
			Intent adjustmentIntent=new Intent(StockAdjustmentActivity.this,StockAdjustmentAddActivity.class);
			adjustmentIntent.putExtra("adjustmentStatue", "add");
			adjustmentIntent.putExtra("shopId", shopId);
			startActivity(adjustmentIntent);
			break;
		case R.id.shopName:
			Intent selectIntent =new Intent(StockAdjustmentActivity.this,AdjusSelectShopActivity.class);
			selectIntent.putExtra("selectShopId", shopId);
			selectIntent.putExtra("activity", "stockAdjustmentActivity");
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
				this.currentPage = 1;//初始化页数
				this.stockAdjustVos.clear();
				this.getStockAdjustmentList();//根据门店获取订货单
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
				currentPage = 1;//选择以后初始化页数
				stockAdjustVos.clear();
				getStockAdjustmentList();
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
				currentPage = 1;//选择以后初始化页数
				stockAdjustVos.clear();
				getStockAdjustmentList();
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
	 * 获取库存调整列表
	 */
	public void getStockAdjustmentList() {
		nodata = false;
		progressDialog.show();
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockAdjust/getStockAdjustList");
		params.setParam("shopId", shopId);
		params.setParam("currentPage", currentPage);
		params.setParam("endtime", sendEndTime);
		params.setParam("starttime", sendEndTime);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get("returnCode");
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null ||!returnCode.equals("success")) {
					ToastUtil.showShortToast(StockAdjustmentActivity.this, "获取失败");
					return;
				}
				
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(StockAdjustmentActivity.this)) {
					return;
				}
				pageSize = (Integer)ju.get(Constants.PAGE_SIZE,Integer.class);
				List<StockAdjustVo> adjustVos = new ArrayList<StockAdjustVo>();
				adjustVos = (List<StockAdjustVo>) ju.get(Constants.STOCK_ADJUST_LIST, new TypeToken<List<StockAdjustVo>>(){}.getType());
				
				if (adjustVos != null && adjustVos.size() > 0) {
					if (currentPage<=pageSize){
						nodata = true;
					}
					stockAdjustVos.addAll(adjustVos);
				}
				adjustmentAdapter.notifyDataSetChanged();
			}
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StockAdjustmentActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		StockInVo stockInVo = stockInList.get(position);
//		Integer billStatus = stockInVo.getBillStatus();
//		
//		Intent collectIntent =new Intent(StockAdjustmentActivity.this,StoreCollectAddActivity.class);
//		Bundle bundle=new Bundle();
//		bundle.putSerializable("stockInVo", stockInVo);
//		collectIntent.putExtras(bundle);
//		
//		if (RetailApplication.getEntityModel()==1) {//单店
//			if (billStatus==1) {
//				collectIntent.putExtra("collectState", Constants.STORE_COLLECT_DISTRIBUTION);//配送中  可以看到进货价  也可以修改 添加 确定 拒绝 
//			}else{
//				collectIntent.putExtra("collectState", Constants.STORE_COLLECT_RECEIVING );//单店 查看 可以看到进货价
//			}
//		}else {//连锁
//			if (currentShop.getType() == ShopVo.DANDIAN || currentShop.getType() == ShopVo.MENDIAN) {
//				if (billStatus==1) {
//					collectIntent.putExtra("collectState", Constants.COLLECT_DISTRIBUTION);//连锁门店  配送中 不能看到进货价 可以 修改 添加 确定 拒绝
//				}else{
//					collectIntent.putExtra("collectState", Constants.COLLECT_RECEIVING);//连锁门店  不能看到进货价  只能查看 确定 和拒绝的 
//				}
//			}else{
//				if (currentShop.getParentId()!=null) {
//					collectIntent.putExtra("collectState", Constants.COLLECT_RECEIVING);//分公司  不能看到进货价  只能查看
//				}else {
//					collectIntent.putExtra("collectState", Constants.STORE_COLLECT_RECEIVING);// 总部 可以看到进货价 但是不能操作进货单 只能查看
//				}
//			}
//		}
//		startActivity(collectIntent);
	}
}