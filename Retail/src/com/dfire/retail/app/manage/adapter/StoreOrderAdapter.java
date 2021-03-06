package com.dfire.retail.app.manage.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.OrderGoodsVo;

@SuppressLint({ "ResourceAsColor", "SimpleDateFormat", "InflateParams" })
public class StoreOrderAdapter extends BaseAdapter {
	
	private List<OrderGoodsVo> orderGoods;
	private LayoutInflater layoutInflater;
	
	public StoreOrderAdapter(Context mContext,List<OrderGoodsVo> orderGoods) {
		this.orderGoods = orderGoods;
		this.layoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return orderGoods.size();
	}

	@Override
	public Object getItem(int position) {
		return orderGoods.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.activity_goods_order_adapter_item, null);
		}
		OrderGoodsVo goodsVo = orderGoods.get(position);
		if (goodsVo!=null) {
			((TextView)convertView.findViewById(R.id.orderno)).setText(goodsVo.getOrderGoodsNo());
			if (goodsVo.getSendEndTime()!=null) {
				Date date = new Date(Long.parseLong(goodsVo.getSendEndTime()));
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				((TextView)convertView.findViewById(R.id.order_time)).setText("要求到货日："+format.format(date));
			}
			((TextView)convertView.findViewById(R.id.states)).setText(goodsVo.getBillStatusName());
			if (goodsVo.getBillStatus()==1) {
				((TextView)convertView.findViewById(R.id.states)).setTextColor(Color.parseColor("#00CC00"));
			}else if(goodsVo.getBillStatus()==2) {
				((TextView)convertView.findViewById(R.id.states)).setTextColor(Color.parseColor("#FF0033"));
			}
		}
		return convertView;
	}

}
