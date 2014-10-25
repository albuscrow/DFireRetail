package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.StoreOrderAdapter;
import com.dfire.retail.app.manage.common.DateDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.OrderGoodsVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 物流管理-门店订货
 * @author ys
 *
 */
public class StoreOrderActivity extends TitleActivity implements OnClickListener,IOnItemSelectListener,OnItemClickListener{
	ImageButton add;
	
	private ListView store_order_lv;
	
	private TextView store_order_time;
	
	private int currentPage = 1;
	
	List<OrderGoodsVo> orderGoods;
	
	OrderGoodsVo orderGoodsVo;
	
	ProgressDialog progressDialog;
	
	private DateDialog mDateDialog;
	
	private SpinerPopWindow shopsSpinner;
	
	private TextView shopTextView,inventory_records_time;
	
	private  ArrayList<AllShopVo> allShops;
	
	private ArrayList<String> shopsStr;
	
	private ShopVo currentShop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_order);
		setTitleText("门店订货");
		showBackbtn();
		findView();
		getStoreOrderGoods(RetailApplication.getmShopInfo().getShopId());
	}
	
	public void findView(){
		allShops = new ArrayList<AllShopVo>();
		currentShop = RetailApplication.getShopVo();
		store_order_lv=(ListView)findViewById(R.id.store_order_lv);
		store_order_time=(TextView)findViewById(R.id.store_order_time);
		add=(ImageButton)findViewById(R.id.minus);
		shopTextView = (TextView) findViewById(R.id.shopName);
		inventory_records_time=(TextView)findViewById(R.id.inventory_records_time);
		orderGoods=new ArrayList<OrderGoodsVo>();
		
		if (currentShop.getType() == ShopVo.DANDIAN || currentShop.getType() == ShopVo.MENDIAN) {
			shopTextView.setCompoundDrawables(null, null, null, null);
			add.setVisibility(View.VISIBLE);
		}else{
			this.getShop();
			shopTextView.setOnClickListener(this);
			add.setVisibility(View.GONE);
		}
		shopTextView.setText(currentShop.getShopName());
		
		store_order_time.setOnClickListener(this);
		add.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.store_order_time:
			pushDate();
			break;
		case R.id.minus:
			Intent orderAdd =new Intent(StoreOrderActivity.this,StoreOrderAddActivity.class);
			orderAdd.putExtra("orderState", Constants.ORDER_ADD);// 新生订单
			startActivity(orderAdd);
			break;
		case R.id.shopName:
			shopsSpinner.setWidth(shopTextView.getWidth());
			shopsSpinner.showAsDropDown(shopTextView);
			break;
		}
		
	}
	/**
	 * 弹出日期
	 */
	private void pushDate(){
		mDateDialog = new DateDialog(StoreOrderActivity.this);
		mDateDialog.show();
		mDateDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String date = mDateDialog.getPushDate();
				store_order_time.setText(date);
//				getStoreOrderGoods();
				mDateDialog.dismiss();
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
	 * 查询订货列表
	 */
	private void getStoreOrderGoods(String shopId) {
	    
		progressDialog = new ProgressDialog(StoreOrderActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		progressDialog.show();
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "orderGoods/list");
		params.setParam("shopId", shopId);
		params.setParam("currentPage", 1);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null ||!returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(StoreOrderActivity.this, "获取失败");
					return;
				}
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(StoreOrderActivity.this)) {
					return;
				}
				List<OrderGoodsVo> orders = new ArrayList<OrderGoodsVo>();
				orders = (List<OrderGoodsVo>) ju.get(Constants.ORDER_GOODS_LIST, new TypeToken<List<OrderGoodsVo>>(){}.getType());
				if (orders.size()>0) {
					orderGoods.clear();
					orderGoods.addAll(orders);
				}
				store_order_lv.setAdapter(new StoreOrderAdapter(StoreOrderActivity.this, orderGoods));
				store_order_lv.setOnItemClickListener(StoreOrderActivity.this);
			}
			
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StoreOrderActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}
	/**
	 * 获取店铺列表
	 * @param isInit
	 */
	private void getShop() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SHOP_ALL_SHOPLIST);
		params.setParam(Constants.SHOP_ID, RetailApplication.getmShopInfo().getShopId());
		params.setParam(Constants.PAGE, currentPage ++);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null ||!returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(StoreOrderActivity.this, "获取失败");
					return;
				}
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(StoreOrderActivity.this)) {
					return;
				}
				List<AllShopVo> allShopVos = new ArrayList<AllShopVo>();
				allShopVos = (List<AllShopVo>) ju.get(Constants.ALL_SHOP_LIST,new TypeToken<List<AllShopVo>>(){}.getType());
				if (allShopVos.size()>0) {
					allShops.addAll(allShopVos);
				}
				shopsStr = new ArrayList<String>();
				int maxLength = 0;
				String Lengest = Constants.EMPTY_STRING;
				for (AllShopVo shopVo : allShops) {
					String shopName = shopVo.getShopName();
					if (shopName.length() > maxLength) {
						maxLength = shopName.length();
						Lengest = shopName;
					}
					shopsStr.add(shopName);
				}
				TextPaint paint = shopTextView.getPaint();
				shopTextView.setWidth((int) (paint.measureText(Lengest) * 1.2f));
				shopsSpinner = new SpinerPopWindow(StoreOrderActivity.this);
				shopsSpinner.refreshData(shopsStr, 0);
				shopTextView.setText(currentShop.getShopName());
				shopsSpinner.setItemListener(StoreOrderActivity.this);	

			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(StoreOrderActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}
	@Override
	public void onItemClick(int pos) {
		shopTextView.setText(allShops.get(pos).getShopName());
		String shopId = allShops.get(pos).getShopId();
		this.getStoreOrderGoods(shopId);//根据门店获取订货单
		allShops.get(pos);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		OrderGoodsVo goodsVo = orderGoods.get(position);
		Integer billStatus = goodsVo.getBillStatus();
		/**
		 * 未确认的状态下。可以修改订单数量，也可以删除 添加
		 */
		Intent orderAdd =new Intent(StoreOrderActivity.this,StoreOrderAddActivity.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable("orderGoods", goodsVo);
		orderAdd.putExtras(bundle);
		if (billStatus==1) {
			if (currentShop.getType() == ShopVo.DANDIAN || currentShop.getType() == ShopVo.MENDIAN) {//判断  是 总部 还是分店
				orderAdd.putExtra("orderState", Constants.UNRECOGNIZED);// 未确定订单
			}else{
				if (currentShop.getParentId()!=null) {
					orderAdd.putExtra("orderState", Constants.CONFIRMATION_AFTER);// 分公司 查看订单详情
				}else {
					orderAdd.putExtra("orderState", Constants.CONFIRMATION);// 总部登陆  确认订单
				}
			}
		}else if(billStatus==2){
			orderAdd.putExtra("orderState", Constants.CONFIRMATION_AFTER);// 确认后订单查看详情
		}
		startActivity(orderAdd);
	}

}