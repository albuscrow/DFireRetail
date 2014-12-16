package com.dfire.retail.app.manage.activity.logisticmanager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.item.StoreCollectGoodsItem;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.common.SupplyDialog;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.StockInDetailVo;
import com.dfire.retail.app.manage.data.StockInVo;
import com.dfire.retail.app.manage.data.bo.PurchaseDetailBo;
import com.dfire.retail.app.manage.data.bo.PurchaseSaveReturnBo;
import com.dfire.retail.app.manage.data.bo.SupplyInfoListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.vo.supplyManageVo;
import com.google.gson.Gson;

/**
 * 物流管理-门店进货
 * @author ys
 *
 */
public class StoreCollectAddActivity extends TitleActivity implements OnClickListener{

	public static StoreCollectAddActivity instance = null; 
	
	private RelativeLayout add_layout,collect_layout;
	
	private LinearLayout store_collect_add_lv,goodsTotalPrice_view;
	
	private StockInDetailVo stockInDetailVo;
	
	private List<StockInDetailVo> stockInDetailVos;
	
	private LayoutInflater inflater;
	
	private String collectState;//操作状态
	
	private StockInVo stockInVo;

	private List<StockInDetailVo> stockList;//查询的list
	
	private String lastVer = "";//版本号
	
	private String stockInNo = "";
	
	private TextView supplyName_tx,stockInNo_tx,mGoodsTotalSum,mGoodsTotalPrice,collect_add_title;
	
	private ImageView collect_add_iv;
	
	private String stockInId,recordType = "p";//进货单id  数据来源(添加默认p)
	
	private SupplyDialog supplyDialog;
	
	private List<supplyManageVo> supplyManageVos;
	
	private String supplyId;//供应商id  
	
	private Button confirm,refuse;
	
	private View view;
	
	private String shopId;
	
	public HashMap<String, StoreCollectGoodsItem> collectHashMap = new HashMap<String, StoreCollectGoodsItem>();
	
	private String isPrice = "false";//是否显示进货价
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_collect_add);
		inflater = LayoutInflater.from(this);
		instance = this;
		setTitleText("门店进货");
		showBackbtn();
		findView();
	}
	/**
	 * 初始化控件
	 */
	public void findView(){
		stockList = new ArrayList<StockInDetailVo>();
		stockInDetailVos=new ArrayList<StockInDetailVo>();
		supplyManageVos=new ArrayList<supplyManageVo>();
		add_layout=(RelativeLayout)findViewById(R.id.add_layout);
		store_collect_add_lv=(LinearLayout)findViewById(R.id.store_collect_add_lv);
		collect_layout=(RelativeLayout)findViewById(R.id.collect_layout);
		supplyName_tx=(TextView)findViewById(R.id.supplyName_tx);
		stockInNo_tx=(TextView)findViewById(R.id.stockInNo_tx);
		mGoodsTotalSum=(TextView)findViewById(R.id.goodsTotalSum);
		mGoodsTotalPrice=(TextView)findViewById(R.id.goodsTotalPrice);
		confirm=(Button)findViewById(R.id.confirm);
		refuse=(Button)findViewById(R.id.refuse);
		view=(View)findViewById(R.id.view);
		goodsTotalPrice_view=(LinearLayout)findViewById(R.id.goodsTotalPrice_view);
		collect_add_iv=(ImageView)findViewById(R.id.collect_add_iv);
		collect_add_title=(TextView)findViewById(R.id.collect_add_title);
		TextPaint tp = collect_add_title.getPaint(); //加粗
		tp.setFakeBoldText(true); 

		collect_add_iv.setOnClickListener(this);
		confirm.setOnClickListener(this);
		refuse.setOnClickListener(this);
		add_layout.setOnClickListener(this);
		
		supplyList();
		supplyDialog = new SupplyDialog(StoreCollectAddActivity.this,supplyManageVos);//状态
		if (RetailApplication.getShopVo().getType() == ShopVo.MENDIAN||RetailApplication.getShopVo().getType() == ShopVo.FENGBU) {
			isPrice = "false";
			goodsTotalPrice_view.setVisibility(View.INVISIBLE);
		}else if (RetailApplication.getShopVo().getType() == ShopVo.DANDIAN) {
			isPrice = "true";
			goodsTotalPrice_view.setVisibility(View.VISIBLE);
		}else if (RetailApplication.getShopVo().getType() == ShopVo.ZHONGBU){
			setTitleText("进货单查询");
			isPrice = "true";
			goodsTotalPrice_view.setVisibility(View.VISIBLE);
		}
		//获取传来的值
		collectState = getIntent().getStringExtra("collectState");
		shopId = getIntent().getStringExtra("shopId");
		if (!collectState.equals(Constants.COLLECT_ADD)) {
			stockInVo = (StockInVo) getIntent().getSerializableExtra("stockInVo");
			supplyName_tx.setCompoundDrawables(null, null, null, null);
			supplyName_tx.setTextColor(Color.parseColor("#666666"));
			collect_layout.setVisibility(View.VISIBLE);
			if (collectState.equals(Constants.COLLECT_RECEIVING)||collectState.equals(Constants.STORE_COLLECT_RECEIVING)) {
				view.setVisibility(View.GONE);
				add_layout.setVisibility(View.GONE);
				refuse.setVisibility(View.GONE);
				confirm.setVisibility(View.GONE);
				collect_add_iv.setVisibility(View.GONE);
			}
			stockInId = stockInVo.getStockInId();
			recordType = stockInVo.getRecordType();
			this.findCollectInfoById();
		}else {
			supplyName_tx.setOnClickListener(this);
			refuse.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	/**
	 * 回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		RetailApplication application = (RetailApplication) getApplication();
		HashMap<String, Object> map = application.getObjMap();
		stockInDetailVo = (StockInDetailVo)map.get("returnCollectAdd");
		if (stockInDetailVo!=null) {
			//判断 map 里面是否存有 该key 对应的value
			if (collectHashMap.containsKey(stockInDetailVo.getGoodsId())) {
				StoreCollectGoodsItem goodsItem = collectHashMap.get(stockInDetailVo.getGoodsId());
				for (int i = 0; i < stockInDetailVos.size(); i++) {
					if (stockInDetailVos.get(i).getGoodsId().equals(goodsItem.getStockInDetailVo().getGoodsId())) {
						if (stockInDetailVos.get(i).getOperateType().equals("del")) {
							stockInDetailVos.get(i).setOperateType("edit");
							store_collect_add_lv.addView(goodsItem.getItemView());
						}
						Integer sum = stockInDetailVos.get(i).getGoodsSum();//之前的数量
						Integer newSum = stockInDetailVo.getGoodsSum();
						goodsItem.getGoodNum().setText(String.valueOf(sum+newSum));//设置数量
						goodsItem.getGoods_price().setText("进货价："+String.valueOf(stockInDetailVo.getGoodsPrice()));//设置进货价
						stockInDetailVos.get(i).setGoodsSum(sum+newSum);//设置list里面的数量
						stockInDetailVos.get(i).setGoodsPrice(stockInDetailVo.getGoodsPrice());//设置list里面的价格
						stockInDetailVos.get(i).setGoodsTotalPrice(stockInDetailVos.get(i).getGoodsPrice().multiply(new BigDecimal(sum+newSum)));//合计
					}
				}
			}else {
				stockInDetailVo.setOperateType("add");
				stockInDetailVos.add(stockInDetailVo);
				StoreCollectGoodsItem collectGoodsItem = new StoreCollectGoodsItem(this,inflater,false,isPrice);
				if (isPrice.equals("false")) {
					collectGoodsItem.getGoods_price().setVisibility(View.INVISIBLE);//隐藏进货价
				}
				collectGoodsItem.initWithData(stockInDetailVo);
				store_collect_add_lv.addView(collectGoodsItem.getItemView());
				collectHashMap.put(stockInDetailVo.getGoodsId(), collectGoodsItem);
			}
			changePriceNumber(null);
		}
		map.put("returnCollectAdd", null);
	}
 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_layout:
			addGoods();
			break;
		case R.id.supplyName_tx:
			pushStatus();
			break;
		case R.id.confirm:
			if (initData()) {
				isReceiptCollect("receipt");
			}
			break;
		case R.id.refuse:
			isReceiptCollect("refuse");
			break;
		case R.id.collect_add_iv:
			addGoods();
			break;
		}
	}
	/**
	 * 添加
	 */
	private void addGoods(){
		if (initData()) {
			if (supplyManageVos!=null&&supplyManageVos.size()>0) {
				if (!supplyName_tx.getText().toString().equals("请选择")) {
					Intent addGoodsiv=new Intent(StoreCollectAddActivity.this,StoreOrderAddGoodsActivity.class);
					addGoodsiv.putExtra("flag", "returnCollectAdd");
					addGoodsiv.putExtra("isPrice",isPrice);
					addGoodsiv.putExtra("shopId", shopId);
					startActivityForResult(addGoodsiv, 100);
				}else {
					new ErrDialog(StoreCollectAddActivity.this,getResources().getString(R.string.LM_MSG_000006)).show();
				}
			}else {
				new ErrDialog(StoreCollectAddActivity.this, getResources().getString(R.string.LM_MSG_000006)).show();
			}
		}
	}
	/**
	 * 提交 对应设值
	 */
	private boolean initData(){
		if (stockInDetailVos!=null&&collectHashMap!=null&&collectHashMap.size()>0) {
			for (int i = 0; i < stockInDetailVos.size(); i++) {
				StoreCollectGoodsItem goodsItem = collectHashMap.get(stockInDetailVos.get(i).getGoodsId());
				Integer num = 0;
				try {
					if (stockInDetailVos.get(i).getOperateType().equals("del")) {
						num = 0;
					}else {
						num = Integer.parseInt(goodsItem.getGoodNum().getText().toString());
					}
				} catch (NumberFormatException e) {
					num = 0;
				}  
				stockInDetailVos.get(i).setGoodsTotalPrice(stockInDetailVos.get(i).getGoodsPrice().multiply(new BigDecimal(num)));//设置总价
				stockInDetailVos.get(i).setGoodsSum(num);//给list 里面的每项设置数量
			}
		}
		return true;
	}
	
	/**
	 * 移除item
	 */
	public void removeView(StoreCollectGoodsItem goodsItem){
		store_collect_add_lv.removeView(goodsItem.getItemView());
		String goodsId = goodsItem.getStockInDetailVo().getGoodsId();
		if (stockInDetailVos!=null&&stockInDetailVos.size()>0) {
			for (int i = 0; i < stockInDetailVos.size(); i++) {
				if (stockInDetailVos.get(i).getGoodsId().equals(goodsId)) {
					if (stockInDetailVos.get(i).getStockInDetailId()!=null&&!stockInDetailVos.get(i).getStockInDetailId().equals("")) {
						stockInDetailVos.get(i).setOperateType("del");
					}else {
						collectHashMap.remove(stockInDetailVos.get(i).getGoodsId());
						stockInDetailVos.remove(i);
					}
				}
			}
		}
		changePriceNumber(null);
	}
	
	/**
	 * 动态监听editText 修改总价格 和数量
	 */
	public void changePriceNumber(StoreCollectGoodsItem goodsItem){
		if (goodsItem==null) {
			initData();
		}
		if (stockInDetailVos.size()>0) {
			Integer count = 0;
			BigDecimal countPrice = new BigDecimal("0");
			for (int i = 0; i < stockInDetailVos.size(); i++) {
				count = count+stockInDetailVos.get(i).getGoodsSum();
				countPrice = countPrice.add(stockInDetailVos.get(i).getGoodsPrice().multiply(new BigDecimal(stockInDetailVos.get(i).getGoodsSum())));//设置总价
			}
			mGoodsTotalSum.setText(""+count);
			mGoodsTotalPrice.setText(""+countPrice);
		}else {
			mGoodsTotalSum.setText("0");
			mGoodsTotalPrice.setText("0.00");
		}
	}
	/**
	 * 查看详情的时候 修改删除该商品
	 */
	public void changeGoodInfo(StockInDetailVo detailVo){
		if (collectHashMap.containsKey(detailVo.getGoodsId())) {
			StoreCollectGoodsItem goodsItem = collectHashMap.get(detailVo.getGoodsId());
			if (detailVo.getGoodsSum()==0) {
				removeView(goodsItem);
			}else {
				for (int i = 0; i < stockInDetailVos.size(); i++) {
					if (stockInDetailVos.get(i).getGoodsId().equals(detailVo.getGoodsId())) {
						goodsItem.getGoodNum().setText(String.valueOf(detailVo.getGoodsSum()));//设置数量
						goodsItem.getGoods_price().setText("进货价："+String.valueOf(detailVo.getGoodsPrice()));//设置进货价
						stockInDetailVos.get(i).setGoodsSum(detailVo.getGoodsSum());//设置list里面的数量
						stockInDetailVos.get(i).setGoodsPrice(detailVo.getGoodsPrice());//设置list里面的价格
						stockInDetailVos.get(i).setGoodsTotalPrice(stockInDetailVos.get(i).getGoodsPrice().multiply(new BigDecimal(detailVo.getGoodsSum())));//合计
						stockInDetailVos.get(i).setProductionDate(detailVo.getProductionDate());//设置生产日期
					}
				}
			}
			changePriceNumber(null);
		}
	}
	/**
	 * 弹出状态
	 */
	private void pushStatus(){
		this.supplyDialog.show();
		this.supplyDialog.getmTitle().setText("供应商");
		this.supplyDialog.updateType(supplyId);
		this.supplyDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				supplyDialog.dismiss();
				Integer index = supplyDialog.getCurrentData();
				if (supplyManageVos!=null&&supplyManageVos.size()>0) {
					supplyManageVo supplyManageVo = supplyManageVos.get(index);
					if (!supplyName_tx.getText().toString().equals("请选择")) {
						if (!supplyName_tx.getText().toString().equals(supplyManageVo.getName())) {
							/**更换供应商清空选择的商品*/
							stockInDetailVos.clear();
							collectHashMap.clear();
							store_collect_add_lv.removeAllViews();
							mGoodsTotalSum.setText("0");
							mGoodsTotalPrice.setText("0.00");
						}
					}
					supplyName_tx.setText(supplyManageVo.getName());
					supplyId = supplyManageVo.getId();
					if (RetailApplication.getShopVo().getType() == ShopVo.DANDIAN) {
						isPrice = "true";
						goodsTotalPrice_view.setVisibility(View.VISIBLE);
					}else {
						if (supplyName_tx.getText().toString().equals(supplyManageVos.get(0).getName())) {
							isPrice = "false";
							goodsTotalPrice_view.setVisibility(View.INVISIBLE);
						}else {
							isPrice = "true";
							goodsTotalPrice_view.setVisibility(View.VISIBLE);
						}
					}
				}
			}
		});
		this.supplyDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				supplyDialog.dismiss();
			}
		});
	}
	
	/**
	 * 供应商列表
	 */
	private void supplyList(){
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SUPPLY_INFO_MANAGE_LIST);
		if (RetailApplication.getEntityModel()==1) {
			params.setParam("showEntityFlg", "0");
		}else {
			params.setParam("showEntityFlg", "1");
		}
		params.setParam("isDividePage", "0");
		new AsyncHttpPost(this, params, SupplyInfoListBo.class,false, new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				SupplyInfoListBo bo = (SupplyInfoListBo) oj;
				if (bo!=null) {
				List<supplyManageVo> vos = bo.getSupplyManageList();
					if (vos!=null && vos.size()>0) {
						supplyManageVos.clear();
						supplyManageVos.addAll(vos);
					}
				}
			}
			@Override
			public void onFail(Exception e) {
			}
		}).execute();
	}
	/**
	 * 确定 ,拒绝
	 */
	private void isReceiptCollect(final String operateType){
		if (stockInDetailVos!=null&&stockInDetailVos.size()>0) {
			RequestParameter params = new RequestParameter(true);
			params.setUrl(Constants.BASE_URL + "purchase/save");
			params.setParam("stockInId", stockInId);
			params.setParam("recordType", recordType);
			params.setParam("operateType", operateType);
			params.setParam("lastVer", lastVer);
			if (operateType.equals("receipt")) {
				params.setParam("supplyId", supplyId);
				params.setParam("shopId", shopId);
				try {
					params.setParam("stockInDetailList",  new JSONArray(new Gson().toJson(stockInDetailVos)));
				} catch (JSONException e1) {
					params.setParam("stockInDetailList",  null);
				}
			}
			new AsyncHttpPost(this, params, PurchaseSaveReturnBo.class, new RequestCallback() {
				
				@Override
				public void onSuccess(Object oj) {
					PurchaseSaveReturnBo bo = (PurchaseSaveReturnBo)oj;
					if (bo!=null) {
						StoreCollectAddActivity.this.finish();
						if (StringUtils.isEquals(operateType, "receipt")) {
							StoreCollectActivity.instance.pullDig(bo.getStockInNo());	
						}
						StoreCollectActivity.instance.reFreshing();
					}
				}
				@Override
				public void onFail(Exception e) {
				}
			}).execute();
		}else {
			new ErrDialog(StoreCollectAddActivity.this, getResources().getString(R.string.please_select_goods)).show();
		}
	}
	/**
	 * 根据id 查询进货单详情
	 */
	private void findCollectInfoById(){
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "purchase/detail");
		params.setParam("stockInId", stockInId);
		params.setParam("recordType", recordType);
		new AsyncHttpPost(this, params, PurchaseDetailBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				PurchaseDetailBo bo = (PurchaseDetailBo) oj;
				if (bo!=null) {
					supplyName_tx.setText(bo.getSupplyName());
					stockInNo = bo.getStockInNo();//订单号
					lastVer = bo.getLastVer();//得到当前版本号
					supplyId = bo.getSupplyId();//供应商ID
					stockInNo_tx.setText(stockInNo);
					mGoodsTotalSum.setText(""+bo.getGoodsTotalSum());
					mGoodsTotalPrice.setText(""+bo.getGoodsTotalPrice());
					stockList = bo.getStockInDetailList();
					if (stockList!=null&& stockList.size() > 0) {
						stockInDetailVos.addAll(stockList);
					}
					/**总部登陆的可以查看进货价  不受供应商的限制*/
					if (RetailApplication.getShopVo().getType() == ShopVo.ZHONGBU||RetailApplication.getShopVo().getType() == ShopVo.DANDIAN) {
						isPrice = "true";
						goodsTotalPrice_view.setVisibility(View.VISIBLE);
					}else {
						if (supplyManageVos.size()>0) {
							if (bo.getSupplyName().equals(supplyManageVos.get(0).getName())) {
								isPrice = "false";
								goodsTotalPrice_view.setVisibility(View.INVISIBLE);
							}else {
								isPrice = "true";
								goodsTotalPrice_view.setVisibility(View.VISIBLE);
							}
						}
					}
					
					/**创建单项*/
					if (stockInDetailVos!=null) {
						for (int i = 0; i < stockInDetailVos.size(); i++) {
							StoreCollectGoodsItem storeCollectGoodsItem = null;
							if(collectState.equals(Constants.COLLECT_RECEIVING)||collectState.equals(Constants.STORE_COLLECT_RECEIVING)){
								storeCollectGoodsItem = new StoreCollectGoodsItem(StoreCollectAddActivity.this,inflater,true,isPrice);
							}else {
								storeCollectGoodsItem = new StoreCollectGoodsItem(StoreCollectAddActivity.this,inflater,false,isPrice);
							}
							if (isPrice.equals("false")) {
								storeCollectGoodsItem.getGoods_price().setVisibility(View.INVISIBLE);//隐藏进货价
							}
							storeCollectGoodsItem.initWithData(stockInDetailVos.get(i));
							store_collect_add_lv.addView(storeCollectGoodsItem.getItemView());
							collectHashMap.put(stockInDetailVos.get(i).getGoodsId(), storeCollectGoodsItem);//goodsItem 存入map(原有view)
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
