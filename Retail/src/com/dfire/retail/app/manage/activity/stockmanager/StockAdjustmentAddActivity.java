package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.item.StockAdjustmentItem;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.StockAdjustVo;
import com.dfire.retail.app.manage.data.bo.ReturnNotMsgBo;
import com.dfire.retail.app.manage.data.bo.StockAdjustDetailListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
import com.google.gson.Gson;

/**
 * 库存管理  添加
 * @author ys
 *
 */
public class StockAdjustmentAddActivity extends Activity implements OnClickListener{

	public static StockAdjustmentAddActivity instance = null; 
	
	private TextView shopname_tx,name_tx;
	
	private ImageButton save,cancel,back;
	
	private RelativeLayout add_layout;
	
	private String shopId,shopName;
	
	private StockAdjustVo adjustVo;
	
	private LinearLayout stock_adjustment_lv;
	
	private LayoutInflater inflater;
	
	private List<StockAdjustVo> mStockAdjustVos;
	
	public HashMap<String, StockAdjustmentItem> collectHashMap = new HashMap<String, StockAdjustmentItem>();

	private String adjustmentStatue;
	
	private StockAdjustVo stockAdjustVo;
	
	private TextView adjustment_add_title;
	
	private ImageView adjustment_add_iv;
	
	private View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_anjustment_add);
		inflater = LayoutInflater.from(this);
		instance = this;
		findView();
		initData();
	}
	/**
	 * 初始化控件
	 */
	public void findView(){
		this.mStockAdjustVos = new ArrayList<StockAdjustVo>();
		adjustmentStatue = getIntent().getStringExtra("adjustmentStatue");
		shopId = getIntent().getStringExtra("shopId");
		shopName = getIntent().getStringExtra("shopName");
		add_layout=(RelativeLayout)findViewById(R.id.add_layout);
		shopname_tx=(TextView)findViewById(R.id.shopname_tx);
		name_tx=(TextView)findViewById(R.id.name_tx);
		save=(ImageButton)findViewById(R.id.save);
		cancel=(ImageButton)findViewById(R.id.cancel);
		back=(ImageButton)findViewById(R.id.back);
		stock_adjustment_lv=(LinearLayout)findViewById(R.id.stock_adjustment_lv);
		adjustment_add_iv=(ImageView)findViewById(R.id.adjustment_add_iv);
		view = (View)findViewById(R.id.view);
		adjustment_add_title=(TextView)findViewById(R.id.adjustment_add_title);
		TextPaint tp = adjustment_add_title.getPaint(); 
		tp.setFakeBoldText(true);
		
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
		add_layout.setOnClickListener(this);
		back.setOnClickListener(this);
		adjustment_add_iv.setOnClickListener(this);
		
		if (StringUtils.isEquals(adjustmentStatue, "noAdd")) {
			stockAdjustVo = (StockAdjustVo) getIntent().getSerializableExtra("stockAdjustVo");
			if (stockAdjustVo!=null) {
				add_layout.setVisibility(View.GONE);
				save.setVisibility(View.GONE);
				cancel.setVisibility(View.GONE);
				back.setVisibility(View.VISIBLE);
				adjustment_add_iv.setVisibility(View.GONE);
				view.setVisibility(View.GONE);
				findAdjustmentById();
			}
		}
	}
	
	/**
	 * 初始化数据
	 */
	public void initData(){
		shopname_tx.setText(shopName);
		if (RetailApplication.getmUserInfo().getName()!=null) {
			name_tx.setText(RetailApplication.getmUserInfo().getName());
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
		adjustVo = (StockAdjustVo)map.get("returnAdjustmentAdd");
		if (adjustVo!=null) {
			if(collectHashMap.containsKey(adjustVo.getGoodsId())) {//map里面存在
				StockAdjustmentItem adjustmentItem = collectHashMap.get(adjustVo.getGoodsId());
				adjustmentItem.setAdjustStore("变动数:"+String.valueOf(adjustVo.getAdjustStore()));
				adjustmentItem.setResultPrice("盈亏金额(元):"+String.valueOf(adjustVo.getResultPrice()));
				
				for (int i = 0; i < mStockAdjustVos.size(); i++) {
					if (mStockAdjustVos.get(i).getGoodsId().equals(adjustVo.getGoodsId())) {
						mStockAdjustVos.get(i).setAdjustStore(adjustVo.getAdjustStore());//设置list里面的调整金额
						mStockAdjustVos.get(i).setResultPrice(adjustVo.getResultPrice());//设置list里面的盈亏数
					}
				}			
			}else {
				mStockAdjustVos.add(adjustVo);
				StockAdjustmentItem adjustmentItem = new StockAdjustmentItem(this,inflater);
				adjustmentItem.initWithData(adjustVo,true);
				stock_adjustment_lv.addView(adjustmentItem.getItemView());
				collectHashMap.put(adjustVo.getGoodsId(), adjustmentItem);
			}
		}
		map.put("returnAdjustmentAdd", null);
	}
 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_layout:
			Intent addAdjustment = new Intent(StockAdjustmentAddActivity.this,StockAddGoodsActivity.class);
			addAdjustment.putExtra("flag", "returnAdjustmentAdd");
			addAdjustment.putExtra("shopId", shopId);
			startActivityForResult(addAdjustment, 100);
			break;
		case R.id.save:
			getSaveAdjust();
			break;
		case R.id.cancel:
			finish();  
			break;
		case R.id.back:
			finish();
			break;
		case R.id.adjustment_add_iv:
			Intent addAdjustmentiv = new Intent(StockAdjustmentAddActivity.this,StockAddGoodsActivity.class);
			addAdjustmentiv.putExtra("flag", "returnAdjustmentAdd");
			addAdjustmentiv.putExtra("shopId", shopId);
			startActivityForResult(addAdjustmentiv, 100);
			break;
		}
	}
	/**
	 * 移除 item 删除掉list里面的对象
	 */
	public void removeItem(String goodId){
		if(collectHashMap.containsKey(goodId)) {//map里面存在
			collectHashMap.size();
			StockAdjustmentItem adjustmentItem = collectHashMap.get(goodId);
			stock_adjustment_lv.removeView(adjustmentItem.getItemView());//移除控件
			for (int i = 0; i < mStockAdjustVos.size(); i++) {
				if (mStockAdjustVos.get(i).getGoodsId().equals(goodId)) {
					collectHashMap.remove(mStockAdjustVos.get(i).getGoodsId());
					mStockAdjustVos.remove(i);
				}
			}
		}
	}
	/**
	 * 根据id 查询库存调整单详情
	 */
	private void findAdjustmentById(){
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockAdjust/getStockAdjustDetailList");
		params.setParam("shopId", shopId);
		params.setParam("adjustcode", stockAdjustVo.getAdjustCode());
		new AsyncHttpPost(this, params, StockAdjustDetailListBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				StockAdjustDetailListBo bo = (StockAdjustDetailListBo)oj;
				if (bo!=null) {
					List<StockAdjustVo> stockList = new ArrayList<StockAdjustVo>();
					stockList = bo.getStockAdjustList();
					if (stockList!=null&& stockList.size() > 0) {
						mStockAdjustVos.addAll(stockList);
					}
					/**创建单项*/
					if (mStockAdjustVos!=null) {
						for (int i = 0; i < mStockAdjustVos.size(); i++) { 
							StockAdjustmentItem adjustmentItem = new StockAdjustmentItem(StockAdjustmentAddActivity.this,inflater);
							adjustmentItem.initWithData(mStockAdjustVos.get(i),false);
							stock_adjustment_lv.addView(adjustmentItem.getItemView());
						}
					}
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}
	/**
	 * 保存
	 */
	private void getSaveAdjust() {
		if (mStockAdjustVos!=null&&mStockAdjustVos.size()>0) {
			RequestParameter params = new RequestParameter(true);
			params.setUrl(Constants.BASE_URL + "stockAdjust/saveAdjust");
			try {
				params.setParam("stockAdjustVo",new JSONArray(new Gson().toJson(mStockAdjustVos)));
			} catch (JSONException e1) {
				params.setParam("stockAdjustVo",null);
			}
			new AsyncHttpPost(this, params, ReturnNotMsgBo.class, new RequestCallback() {
				@Override
				public void onSuccess(Object oj) {
					ReturnNotMsgBo bo = (ReturnNotMsgBo)oj;
					if (bo!=null) {
						StockAdjustmentAddActivity.this.finish();
						StockAdjustmentActivity.instance.reFreshing();
					}
				}
				@Override
				public void onFail(Exception e) {
					// TODO Auto-generated method stub
				}
			}).execute();
		}else {
			new ErrDialog(this, getResources().getString(R.string.please_select_adjust_goods)).show();
		}
	}
}
