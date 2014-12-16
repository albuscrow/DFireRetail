package com.dfire.retail.app.manage.activity.item;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.retail.IViewItem;
import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.stockmanager.StockAdjustmentAddActivity;
import com.dfire.retail.app.manage.activity.stockmanager.StockGoodInfoActivity;
import com.dfire.retail.app.manage.data.StockAdjustVo;
/**
 * 项目名称：retail  
 * 类名称：StockAdjustmentItem  
 * @author ys  
 * @version 1.0
 */
@SuppressLint("InflateParams")
public class StockAdjustmentItem implements IViewItem, OnClickListener {

	private LayoutInflater inflater;
	
	private View itemView;
	
	private TextView goods_name,barCode,adjustStore,resultPrice;
	
	private StockAdjustVo adjustVo;
	
	public AlertDialog alertDialog;
	
	private LinearLayout gooditemview;
	
	private Context mContext;
	
	public StockAdjustmentItem(Context mContext,LayoutInflater inflater) {
		this.inflater = inflater;
		this.mContext = mContext;
		init();
	}
	@Override
	public void init() {
		initMainView();
	}

	@Override
	public void initMainView() {
		this.itemView = inflater.inflate(R.layout.stock_adjustment_add_item, null);
		this.itemView.setTag(this);
		this.goods_name=(TextView) itemView.findViewById(R.id.goods_name);
		this.barCode=(TextView) itemView.findViewById(R.id.barCode);
		this.adjustStore=(TextView) itemView.findViewById(R.id.adjustStore);
		this.resultPrice=(TextView) itemView.findViewById(R.id.resultPrice);
		this.gooditemview=(LinearLayout) itemView.findViewById(R.id.gooditemview);
	}

	public void initWithData(StockAdjustVo adjustVo,Boolean isSetOnclik){
		this.adjustVo = adjustVo;
		this.goods_name.setText(adjustVo.getGoodsName()+"");
		this.barCode.setText(adjustVo.getBarCode()+"");
		this.adjustStore.setText("变动数:"+adjustVo.getAdjustStore()+"");
		this.resultPrice.setText("盈亏金额(元):"+adjustVo.getResultPrice()+"");
		if (isSetOnclik) {
			this.gooditemview.setOnClickListener(this);
		}
	}
	
	@Override
	public void setVisibility(int visibility) {
		this.itemView.setVisibility(visibility);		
	}
	
	@Override
	public void onClick(View v) { 
		if (v==gooditemview) {
			Intent add = new Intent(mContext, StockGoodInfoActivity.class);
			add.putExtra("activity", "1");
			RetailApplication rapp = (RetailApplication)((StockAdjustmentAddActivity)mContext).getApplication();
			HashMap<String, Object> map = rapp.getObjMap();
			map.put("returnAdjustmentAdd", adjustVo);
			((StockAdjustmentAddActivity)mContext).startActivityForResult(add, 100);
		}
	}

	@Override
	public void itemSelect() {
	}
	@Override
	public View getItemView() {
		return this.itemView;
	}
	/**
	 * 获取单项信息
	 */
	public StockAdjustVo getStockAdjustVo(){
		return adjustVo;
	}
	/**
	 * 设置变动数
	 * @param adjustStore
	 */
	public void setAdjustStore(String adjustStore) {
		this.adjustStore.setText(adjustStore);
	}
	/**
	 * 设置盈亏
	 * @param resultPrice
	 */
	public void setResultPrice(String resultPrice) {
		this.resultPrice.setText(resultPrice);
	}
}