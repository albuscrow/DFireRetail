package com.dfire.retail.app.manage.activity.logisticmanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.item.StoreOrderGoodsItem;
import com.dfire.retail.app.manage.data.OrderGoodsDetailVo;
import com.dfire.retail.app.manage.data.OrderGoodsVo;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * 物流管理-添加门店订货
 * @author ys
 *
 */
@SuppressLint("SimpleDateFormat")
public class StoreOrderAddActivity extends Activity implements OnClickListener{

	private ProgressDialog progressDialog;
	
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
	
	private String orderGoodsNo;//订货单号
	
	private LinearLayout ordergoodsview;
	
	private TextView ordergoodsid,store_order_time;
	
	private Button delete,confirm;
	
	private String lastVer = "";
	
	private List<OrderGoodsDetailVo> detailVos = null;
	
	private String getOrderGoodsId;
	
	private String orderState;//操作状态
	
	private HashMap<String, StoreOrderGoodsItem> storeOrderHashMap = new HashMap<String, StoreOrderGoodsItem>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_order_add);
		inflater = LayoutInflater.from(this);
		findView();
	}
	
	public void findView(){
		stockInDetailVoList=new ArrayList<OrderGoodsDetailVo>();
		title_left=(ImageButton)findViewById(R.id.title_left);
		title_right=(ImageButton)findViewById(R.id.title_right);
		title_return=(ImageButton)findViewById(R.id.title_return);
		add_layout=(RelativeLayout)findViewById(R.id.add_layout);
		store_order_add_lv=(LinearLayout)findViewById(R.id.store_order_add_lv);
		ordergoodsview=(LinearLayout)findViewById(R.id.ordergoodsview);
		ordergoodsid=(TextView)findViewById(R.id.ordergoodsid);
		store_order_time=(TextView)findViewById(R.id.store_order_time);
		delete=(Button)findViewById(R.id.delete);
		confirm=(Button)findViewById(R.id.confirm);

		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);
		title_return.setOnClickListener(this);
		add_layout.setOnClickListener(this);
		delete.setOnClickListener(this);
		confirm.setOnClickListener(this);
		
		orderState = getIntent().getStringExtra("orderState").toString();
		if (!orderState.equals(Constants.ORDER_ADD)) {
			orderGoodsVo = (OrderGoodsVo) getIntent().getSerializableExtra("orderGoods");
			ordergoodsview.setVisibility(View.VISIBLE);
			detailVos = new ArrayList<OrderGoodsDetailVo>();
			getOrderGoodsId = orderGoodsVo.getOrderGoodsId();
			if (orderState.equals(Constants.UNRECOGNIZED)) {
				delete.setVisibility(View.VISIBLE);
			}else if(orderState.equals(Constants.CONFIRMATION)){
				title_return.setVisibility(View.VISIBLE);
				confirm.setVisibility(View.VISIBLE);
				title_right.setVisibility(View.GONE);
				title_left.setVisibility(View.GONE);
			}else if(orderState.equals(Constants.CONFIRMATION_AFTER)){
				store_order_time.setCompoundDrawables(null, null, null, null);
				delete.setVisibility(View.GONE);
				confirm.setVisibility(View.GONE);
				add_layout.setVisibility(View.GONE);
				store_order_time.setTextColor(Color.parseColor("#666666"));
				title_return.setVisibility(View.VISIBLE);
				title_right.setVisibility(View.GONE);
				title_left.setVisibility(View.GONE);
			}
			this.findOrderInfoById(getOrderGoodsId);
		}
	}
	/**
	 * 回调返回 数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){
			Bundle b=data.getExtras();
			searchGoodsVo=(SearchGoodsVo)b.getSerializable("orderAdd");
			//判断 map 里面是否存有 该key 对应的value
			if (storeOrderHashMap.containsKey(searchGoodsVo.getGoodsId())) {
				StoreOrderGoodsItem goodsItem = storeOrderHashMap.get(searchGoodsVo.getGoodsId());
				//判断该item 里面对应的对象的goodsSum 属性是否为0  如果是0 把移除的view 在加到LinearLayout里面去 如果不为空 给对应的数量加1
				if (goodsItem.getOrderGoodsDetailVo().getGoodsSum()==0){
					//判断是operateType 是否是 del如果是修改为edit
					if (goodsItem.getOrderGoodsDetailVo().getOperateType().equals("del")) {
						stockInDetailVoList.get(goodsItem.getListIndex()).setOperateType("edit");
					}
					store_order_add_lv.addView(goodsItem.getItemView());
					storeOrderHashMap.get(searchGoodsVo.getGoodsId()).getGoodNum().setText(String.valueOf(1));
					goodsItem.getOrderGoodsDetailVo().setGoodsSum(1);
				}else {
					Integer sum = Integer.parseInt(storeOrderHashMap.get(searchGoodsVo.getGoodsId()).getGoodNum().getText().toString());
					storeOrderHashMap.get(searchGoodsVo.getGoodsId()).getGoodNum().setText(String.valueOf(sum+1));
				}
				
			}else {
				//添加新的view 并且加入list里面
				orderGoodsDetailVo = new OrderGoodsDetailVo();
				orderGoodsDetailVo.setGoodsId(searchGoodsVo.getGoodsId());//id
				orderGoodsDetailVo.setGoodsName(searchGoodsVo.getGoodsName());//名称
				orderGoodsDetailVo.setGoodsBarcode(searchGoodsVo.getGoodsBarCode());//条码
				orderGoodsDetailVo.setGoodsPrice(searchGoodsVo.getPurchasePrice());//价格
				orderGoodsDetailVo.setGoodsSum(1);//默认 选择1件
				orderGoodsDetailVo.setNowStore(searchGoodsVo.getNowstore());//库存
				orderGoodsDetailVo.setGoodsTotalPrice(searchGoodsVo.getPurchasePrice());//默认合计 。 会乘以数量
				orderGoodsDetailVo.setOperateType("add"); //操作类型
				stockInDetailVoList.add(orderGoodsDetailVo);
				
				StoreOrderGoodsItem goodsItem = new StoreOrderGoodsItem(this,inflater,false);
				goodsItem.initWithData(orderGoodsDetailVo,stockInDetailVoList.size()-1);
				store_order_add_lv.addView(goodsItem.getItemView());
				storeOrderHashMap.put(orderGoodsDetailVo.getGoodsId(), goodsItem);//goodsItem 存入map(新view)
			}
		}
	}
 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_left:
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
				Intent addGoods=new Intent(StoreOrderAddActivity.this,StoreOrderAddGoodsActivity.class);
				addGoods.putExtra("add", 1);
				startActivityForResult(addGoods, 100);
				break;
			case R.id.delete:
				if (getOrderGoodsId!=null&&!getOrderGoodsId.equals("")) {
					this.getResult("del",getOrderGoodsId);//删除订单
				}
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
			
		}
	}
	/**
	 * 对应设值
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
			stockInDetailVoList.get(i).setGoodsSum(num);//给list 里面的每项设置数量
		}
		for (int i = 0; i < stockInDetailVoList.size(); i++) {
			OrderGoodsDetailVo orderGoodsDetailVo = stockInDetailVoList.get(i);
			if (orderGoodsDetailVo.getOperateType().equals("add")&&orderGoodsDetailVo.getGoodsSum()==0) {
				stockInDetailVoList.remove(i);
			}
		}
		return true;
	}
	/**
	 * 添加、修改、删除 订单
	 */
	private void getResult(String type,String getOrderId) {
		progressDialog = new ProgressDialog(StoreOrderAddActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		progressDialog.show();
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "orderGoods/save");
		params.setParam("shopId", RetailApplication.getmShopInfo().getShopId());
		params.setParam("orderGoodsId", getOrderId);
		params.setParam("sendEndTime", (new Date()).getTime());
		params.setParam("operateType", type);
		try {
			params.setParam("orderGoodsList",  new JSONArray(new Gson().toJson(stockInDetailVoList)));
		} catch (JSONException e1) {
			params.setParam("orderGoodsList",  null);
		}
		if (!lastVer.equals("")) {
			params.setParam("lastVer", lastVer);
		}else {
			params.setParam("lastVer", null);
		}
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
			}
			
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StoreOrderAddActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}
	/**
	 * 更改每个list里面每项的type
	 */
	public void changeListOperType(StoreOrderGoodsItem goodsItem){
		OrderGoodsDetailVo detailVo = goodsItem.getOrderGoodsDetailVo();
		if (detailVo.getOrderGoodsDetailId()!=null&&!detailVo.getOrderGoodsDetailId().equals("")) {
			stockInDetailVoList.get(goodsItem.getListIndex()).setOperateType("edit");
		}
	}
	/**
	 * 移除item
	 */
	public void removeView(StoreOrderGoodsItem goodsItem){
		store_order_add_lv.removeView(goodsItem.getItemView());
		OrderGoodsDetailVo detailVo = goodsItem.getOrderGoodsDetailVo();
		if (detailVo.getOrderGoodsDetailId()!=null&&!detailVo.getOrderGoodsDetailId().equals("")) {
			stockInDetailVoList.get(goodsItem.getListIndex()).setOperateType("del");
		}
		stockInDetailVoList.get(goodsItem.getListIndex()).setGoodsSum(0);
	}
	/**
	 * 根据id 查询订单下面的商品信息
	 */
	private void findOrderInfoById(String orderGoodsId){
		progressDialog = new ProgressDialog(StoreOrderAddActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		progressDialog.show();
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "orderGoods/detail");
		params.setParam("orderGoodsId", orderGoodsId);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(StoreOrderAddActivity.this)) {
					return;
				}
				detailVos = (List<OrderGoodsDetailVo>) ju.get(Constants.ORDER_GOODS_DETAIL_LIST, new TypeToken<List<OrderGoodsDetailVo>>(){}.getType());
				String date = ju.get("sendEndTime",String.class).toString();
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				if (date!=null&&!date.equals("")) {
					sendEndTime = new Date(Long.valueOf(date));//要求到货时间
				}
				orderGoodsNo = ju.get("orderGoodsNo",String.class).toString();//订单号
				lastVer = String.valueOf(ju.get("lastVer",Integer.class));//得到当前版本号
				
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
						goodsItem.initWithData(stockInDetailVoList.get(i),i);
						store_order_add_lv.addView(goodsItem.getItemView());
						
						storeOrderHashMap.put(stockInDetailVoList.get(i).getGoodsId(), goodsItem);//goodsItem 存入map(原有view)
						
					}
				}
			}
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StoreOrderAddActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}
}
