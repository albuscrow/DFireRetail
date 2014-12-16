package com.dfire.retail.app.manage.activity.logisticmanager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.item.StoreOrderGoodsItem;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.OrderGoodsDetailVo;
import com.dfire.retail.app.manage.data.OrderGoodsVo;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.OrderGoodsDetailBo;
import com.dfire.retail.app.manage.data.bo.OrderGoodsSaveBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.Gson;
/**
 * 物流管理-添加门店订货
 * @author ys
 *
 */
@SuppressLint("SimpleDateFormat")
public class StoreOrderAddActivity extends Activity implements OnClickListener{
	
	private ImageButton title_left,title_return;
	
	private ImageButton title_right;
	
	private RelativeLayout add_layout;
	
	private LinearLayout store_order_add_lv;
	
	private SearchGoodsVo searchGoodsVo;
	
	private OrderGoodsDetailVo orderGoodsDetailVo;
	
	private List<OrderGoodsDetailVo> stockInDetailVoList;
	
	private LayoutInflater inflater;
	
	private OrderGoodsVo orderGoodsVo;
	
	private Date sendEndTime;//要求到货时间
	
	private Long endTime;//选择时间
	
	private String orderGoodsNo;//订货单号
	
	private LinearLayout ordergoodsview;
	
	private TextView ordergoodsid,store_order_time,order_add_title;
	
	private Button delete,confirm;
	
	private String lastVer = "";
	
	private List<OrderGoodsDetailVo> detailVos = null;
	
	private String getOrderGoodsId;
	
	private String orderState;//操作状态
	
	private SelectDateDialog mDateDialog;
	
	private String selectDate = null;
	
	private HashMap<String, StoreOrderGoodsItem> storeOrderHashMap = new HashMap<String, StoreOrderGoodsItem>();
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");
	
	private String shopId;
	
	private ImageView order_add_iv;
	
	private String isPrice = "true";//是否显示价格
	
	private View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_order_add);
		inflater = LayoutInflater.from(this);
		findView();
	}
	
	public void findView(){
		this.stockInDetailVoList=new ArrayList<OrderGoodsDetailVo>();//详情列表
		this.title_left=(ImageButton)findViewById(R.id.title_left);
		this.title_right=(ImageButton)findViewById(R.id.title_right);
		this.title_return=(ImageButton)findViewById(R.id.title_return);
		this.add_layout=(RelativeLayout)findViewById(R.id.add_layout);
		this.store_order_add_lv=(LinearLayout)findViewById(R.id.store_order_add_lv);
		this.ordergoodsview=(LinearLayout)findViewById(R.id.ordergoodsview);
		this.ordergoodsid=(TextView)findViewById(R.id.ordergoodsid);
		this.store_order_time=(TextView)findViewById(R.id.store_order_time);
		this.order_add_iv=(ImageView)findViewById(R.id.order_add_iv);
		this.order_add_title=(TextView)findViewById(R.id.order_add_title);
		this.view = (View)findViewById(R.id.view);
		TextPaint tp = order_add_title.getPaint(); 
		tp.setFakeBoldText(true);
		this.selectDate = format.format(new Date());
		try {
			endTime = (sdf.parse((selectDate+" 00:00:00"))).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.delete=(Button)findViewById(R.id.delete);
		this.confirm=(Button)findViewById(R.id.confirm);

		this.title_left.setOnClickListener(this);
		this.title_right.setOnClickListener(this);
		this.title_return.setOnClickListener(this);
		this.add_layout.setOnClickListener(this);
		this.delete.setOnClickListener(this);
		this.confirm.setOnClickListener(this);
		this.order_add_iv.setOnClickListener(this);
		
		this.mDateDialog = new SelectDateDialog(StoreOrderAddActivity.this);//时间
		this.shopId = getIntent().getStringExtra("shopId");
		if (RetailApplication.getShopVo().getType() == ShopVo.MENDIAN) {
			isPrice = "false"; 
		}else{
			isPrice = "true";
		}
		
		this.orderState = getIntent().getStringExtra("orderState");
		if (!orderState.equals(Constants.ORDER_ADD)) {
			this.orderGoodsVo = (OrderGoodsVo) getIntent().getSerializableExtra("orderGoods");
			this.ordergoodsview.setVisibility(View.VISIBLE);
			this.detailVos = new ArrayList<OrderGoodsDetailVo>();
			this.getOrderGoodsId = orderGoodsVo.getOrderGoodsId();
			if (orderState.equals(Constants.UNRECOGNIZED)) {
				this.store_order_time.setText(selectDate);
				this.delete.setVisibility(View.VISIBLE);
				this.store_order_time.setOnClickListener(this);
			}else if(orderState.equals(Constants.CONFIRMATION)){
				this.store_order_time.setCompoundDrawables(null, null, null, null);
				this.store_order_time.setTextColor(Color.parseColor("#666666"));
				this.title_return.setVisibility(View.VISIBLE);
				this.confirm.setVisibility(View.VISIBLE);
				this.title_right.setVisibility(View.GONE);
				this.title_left.setVisibility(View.GONE);
			}else if(orderState.equals(Constants.CONFIRMATION_AFTER)){
				this.store_order_time.setCompoundDrawables(null, null, null, null);
				this.store_order_time.setTextColor(Color.parseColor("#666666"));
				this.delete.setVisibility(View.GONE);
				this.confirm.setVisibility(View.GONE);
				this.add_layout.setVisibility(View.GONE);
				this.title_return.setVisibility(View.VISIBLE);
				this.title_right.setVisibility(View.GONE);
				this.title_left.setVisibility(View.GONE);
				this.order_add_iv.setVisibility(View.GONE);
				this.view.setVisibility(View.GONE);
			}
			this.findOrderInfoById(getOrderGoodsId);
		}else {
			this.store_order_time.setText(selectDate);
			this.store_order_time.setOnClickListener(this);
		}
	}
	/**
	 * 回调返回 数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){
			Bundle b = data.getExtras();
			searchGoodsVo=(SearchGoodsVo)b.getSerializable("orderAdd");
			//判断 map 里面是否存有 该key 对应的value
			if (storeOrderHashMap.containsKey(searchGoodsVo.getGoodsId())) {
				StoreOrderGoodsItem goodsItem = storeOrderHashMap.get(searchGoodsVo.getGoodsId());
				for (int i = 0; i < stockInDetailVoList.size(); i++) {
					if (stockInDetailVoList.get(i).getGoodsId().equals(goodsItem.getOrderGoodsDetailVo().getGoodsId())) {
						if (stockInDetailVoList.get(i).getOperateType().equals("del")) {
							stockInDetailVoList.get(i).setOperateType("edit");
							store_order_add_lv.addView(goodsItem.getItemView());
						}
						Integer sum = stockInDetailVoList.get(i).getGoodsSum();//之前的数量
						goodsItem.getGoodNum().setText(String.valueOf(sum+1));//设置textView
						BigDecimal price = stockInDetailVoList.get(i).getGoodsPrice();
						stockInDetailVoList.get(i).setGoodsSum(sum+1);//设置list里面的数量
						stockInDetailVoList.get(i).setGoodsTotalPrice(price.multiply(new BigDecimal(sum+1)));//设置list里面的盈亏数
					}
				}
			}else {
				//添加新的view 并且加入list里面
				orderGoodsDetailVo = new OrderGoodsDetailVo();
				orderGoodsDetailVo.setGoodsId(searchGoodsVo.getGoodsId());//id
				orderGoodsDetailVo.setGoodsName(searchGoodsVo.getGoodsName());//名称
				orderGoodsDetailVo.setGoodsBarcode(searchGoodsVo.getBarcode());//条码
				orderGoodsDetailVo.setGoodsPrice(searchGoodsVo.getPurchasePrice());//价格
				orderGoodsDetailVo.setGoodsSum(1);//默认 选择1件
				orderGoodsDetailVo.setNowStore(searchGoodsVo.getNowstore());//库存
				orderGoodsDetailVo.setGoodsTotalPrice(searchGoodsVo.getPurchasePrice());//默认合计 。 会乘以数量
				orderGoodsDetailVo.setOperateType("add"); //操作类型
				stockInDetailVoList.add(orderGoodsDetailVo);
				
				StoreOrderGoodsItem goodsItem = new StoreOrderGoodsItem(this,inflater,false);
				goodsItem.initWithData(orderGoodsDetailVo);
				store_order_add_lv.addView(goodsItem.getItemView());
				storeOrderHashMap.put(orderGoodsDetailVo.getGoodsId(), goodsItem);//goodsItem 存入map(新view)
			}
		}
	}
 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_left:
				finish();
				break;
			case R.id.title_right:
				if (initData()) {
					if (getOrderGoodsId==null||getOrderGoodsId.equals("")) {
						if (stockInDetailVoList.size()<=0) {
							ToastUtil.showShortToast(this, "请选择要添加的商品!");
						}else {
							this.getResult("add",null);//添加订单
						}
					}else {
						this.getResult("edit",getOrderGoodsId);//修改订单
					}	
				}
				break;
			case R.id.add_layout:
				if (initData()) {
					Intent addGoods=new Intent(StoreOrderAddActivity.this,StoreOrderAddGoodsActivity.class);
					addGoods.putExtra("shopId", shopId);
					addGoods.putExtra("flag", "returnOrderAdd");
					addGoods.putExtra("isPrice",isPrice);
					startActivityForResult(addGoods, 100);
				}
				break;
			case R.id.delete:
				final AlertDialog alertDialog = new AlertDialog(this);
				alertDialog.setMessage(getResources().getString(R.string.isdeleteorder));
				alertDialog.setCanceledOnTouchOutside(false);
				alertDialog.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (getOrderGoodsId!=null&&!getOrderGoodsId.equals("")) {
							StoreOrderAddActivity.this.getResult("del",getOrderGoodsId);//删除订单
						} 
						alertDialog.dismiss();
					}
				});
				alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
				break;
			case R.id.confirm:
				if (getOrderGoodsId!=null&&!getOrderGoodsId.equals("")) {
					if (initData()) {
						this.getResult("config",getOrderGoodsId);//确认订单  （总部）
					}
				}
				break;
			case R.id.title_return:
				finish();
				break;
			case R.id.store_order_time:
				this.pushDate();
				break;
			case R.id.order_add_iv:
				if (initData()) {
					Intent addGoodsiv=new Intent(StoreOrderAddActivity.this,StoreOrderAddGoodsActivity.class);
					addGoodsiv.putExtra("shopId", shopId);
					addGoodsiv.putExtra("isPrice",isPrice);
					addGoodsiv.putExtra("flag", "returnOrderAdd");
					startActivityForResult(addGoodsiv, 100);
				}
				break;
		}
	}
	/**
	 * 弹出日期
	 */
	private void pushDate(){
		mDateDialog.show();
		mDateDialog.getTitle().setText("要求到货日期");
		mDateDialog.updateDays(selectDate);
		mDateDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDateDialog.dismiss();
				selectDate = mDateDialog.getCurrentData();
				store_order_time.setText(selectDate);
				try {
					endTime = (sdf.parse((selectDate+" 00:00:00"))).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
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
	 * list 设置  页面输入的值
	 */
	private boolean initData(){
		for (int i = 0; i < stockInDetailVoList.size(); i++) {
			StoreOrderGoodsItem goodsItem = storeOrderHashMap.get(stockInDetailVoList.get(i).getGoodsId());
			Integer num = 0;
			try {
				num =Integer.parseInt(goodsItem.getGoodNum().getText().toString());
			} catch (NumberFormatException e) {
				num = 0;
			}  
			stockInDetailVoList.get(i).setGoodsTotalPrice(stockInDetailVoList.get(i).getGoodsPrice().multiply(new BigDecimal(num)));//设置总价
			stockInDetailVoList.get(i).setGoodsSum(num);//给list 里面的每项设置数量
		}
		return true;
	}

	/**
	 * 添加、修改、删除 订单
	 */
	private void getResult(String type,final String getOrderId) {
		if (stockInDetailVoList!=null&&stockInDetailVoList.size()>0) {
			RequestParameter params = new RequestParameter(true);
			params.setUrl(Constants.BASE_URL + "orderGoods/save");
			params.setParam("shopId", shopId);
			params.setParam("orderGoodsId", getOrderId);
			params.setParam("sendEndTime", endTime);
			params.setParam("operateType", type);
			params.setParam("lastVer", lastVer);
			try {
				params.setParam("orderGoodsList",  new JSONArray(new Gson().toJson(stockInDetailVoList)));
			} catch (JSONException e1) {
				params.setParam("orderGoodsList",  null);
			}
			new AsyncHttpPost(this, params, OrderGoodsSaveBo.class, new RequestCallback() {
				
				@Override
				public void onSuccess(Object oj) {
					OrderGoodsSaveBo bo = (OrderGoodsSaveBo)oj;
					if (bo!=null) {
						if(orderState.equals(Constants.UNRECOGNIZED)){//未确认订单
							StoreOrderActivity.instance.changeDate(String.valueOf(endTime));
							StoreOrderAddActivity.this.finish();
						}else {
							if(orderState.equals(Constants.CONFIRMATION)){
								StoreOrderActivity.instance.pullDigCenter(bo.getDistributionNo());
							}else{
								if (StringUtils.isEmpty(getOrderId)&&StringUtils.isEquals(getOrderId, "")) {
									StoreOrderActivity.instance.pullDig(bo.getOrderGoodsNo());
								}	
							}
							StoreOrderAddActivity.this.finish();
							StoreOrderActivity.instance.reFreshing();
						}
					}
				}
				@Override
				public void onFail(Exception e) {
				}
			}).execute();
		}else {
			new ErrDialog(StoreOrderAddActivity.this, getResources().getString(R.string.please_select_order_goods)).show();
		}
	}
	/**
	 * 更改每个list里面每项的type
	 */
	public void changeListOperType(StoreOrderGoodsItem goodsItem){
		OrderGoodsDetailVo detailVo = goodsItem.getOrderGoodsDetailVo();
		if (detailVo.getOrderGoodsDetailId()!=null&&!detailVo.getOrderGoodsDetailId().equals("")) {
			for (int i = 0; i < stockInDetailVoList.size(); i++) {
				if (stockInDetailVoList.get(i).getGoodsId().equals(goodsItem.getOrderGoodsDetailVo().getGoodsId())) {
					stockInDetailVoList.get(i).setOperateType("edit");
				}
			}
		}
	}
	/**
	 * 移除item
	 */
	public void removeView(StoreOrderGoodsItem goodsItem){
		store_order_add_lv.removeView(goodsItem.getItemView());
		String goodsId = goodsItem.getOrderGoodsDetailVo().getGoodsId();
		if (stockInDetailVoList!=null&&stockInDetailVoList.size()>0) {
			for (int i = 0; i < stockInDetailVoList.size(); i++) {
				if (stockInDetailVoList.get(i).getGoodsId().equals(goodsId)) {
					if (stockInDetailVoList.get(i).getOrderGoodsDetailId()!=null&&!stockInDetailVoList.get(i).getOrderGoodsDetailId().equals("")) {
						stockInDetailVoList.get(i).setOperateType("del");
						stockInDetailVoList.get(i).setGoodsSum(0);
					}else {
						storeOrderHashMap.remove(stockInDetailVoList.get(i).getGoodsId());
						stockInDetailVoList.remove(i);
					}
				}
			}
		}
	}
	/**
	 * 根据id 查询订单下面的商品信息
	 */
	private void findOrderInfoById(String orderGoodsId){
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "orderGoods/detail");
		params.setParam("orderGoodsId", orderGoodsId);
		new AsyncHttpPost(this, params, OrderGoodsDetailBo.class, new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				OrderGoodsDetailBo bo = (OrderGoodsDetailBo)oj;
				if (bo!=null) {
					detailVos = bo.getOrderGoodsDetailList();
					endTime = bo.getSendEndTime();
					if (bo.getSendEndTime()!=null&&!bo.getSendEndTime().equals("")) {
						sendEndTime = new Date(Long.valueOf(bo.getSendEndTime()));//要求到货时间
					}
					orderGoodsNo = bo.getOrderGoodsNo();//订单号
					lastVer = String.valueOf(bo.getLastVer());//得到当前版本号
					
					ordergoodsid.setText(orderGoodsNo);
					store_order_time.setText(format.format(sendEndTime));
					if (detailVos!=null&& detailVos.size() > 0) {
						stockInDetailVoList.addAll(detailVos);
					}
					/**创建单项*/
					if (stockInDetailVoList!=null) {
						for (int i = 0; i < stockInDetailVoList.size(); i++) {
							StoreOrderGoodsItem goodsItem = null;
							if(orderState.equals(Constants.CONFIRMATION_AFTER)){
								goodsItem = new StoreOrderGoodsItem(StoreOrderAddActivity.this,inflater,true);
							}else {
								goodsItem = new StoreOrderGoodsItem(StoreOrderAddActivity.this,inflater,false);
							}
							goodsItem.initWithData(stockInDetailVoList.get(i));
							store_order_add_lv.addView(goodsItem.getItemView());
							storeOrderHashMap.put(stockInDetailVoList.get(i).getGoodsId(), goodsItem);//goodsItem 存入map(原有view)
						}
					}
				}
			}
			
			@Override
			public void onFail(Exception e) {
				
			}
		}).execute();
		
	}
}
