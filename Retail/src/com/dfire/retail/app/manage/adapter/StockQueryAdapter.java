package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.StockInfoVo;

/**
 * 库存管理-库存查询适配器
 * @author ys
 */
@SuppressLint("InflateParams")
public class StockQueryAdapter extends BaseAdapter{

	private List<StockInfoVo> list=new ArrayList<StockInfoVo>();
	private LayoutInflater mLayoutInflater;
	private boolean isSingle;
	//单店和连锁店显示结果不同
	public StockQueryAdapter(Context context, List<StockInfoVo> list,boolean isSingleShop) {
		super();
		this.list = list;
		this.isSingle = isSingleShop;
		mLayoutInflater =  LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		StockInfoVo stockInfoVo = list.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.stock_query_item, null);
            viewHolder = new ViewHolder();
            viewHolder.stock_good_code = (TextView) convertView.findViewById(R.id.stock_good_code);
            viewHolder.stock_title = (TextView) convertView.findViewById(R.id.stock_title);
            viewHolder.tock_number = (TextView) convertView.findViewById(R.id.stock_number);
            viewHolder.tock_goods_number = (TextView) convertView.findViewById(R.id.stock_goods_number);
            viewHolder.tock_purchase_price = (TextView) convertView.findViewById(R.id.tock_purchase_price);
            viewHolder.tock_total = (TextView) convertView.findViewById(R.id.tock_total);
            viewHolder.tock_purchase_price_text = (TextView) convertView.findViewById(R.id.tock_purchase_price_text);
            viewHolder.tock_total_layoutd = (LinearLayout) convertView.findViewById(R.id.tock_total_layoutd);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (stockInfoVo!=null) {
	        viewHolder.stock_title.setText(stockInfoVo.getGoodsName()+"");
	        viewHolder.tock_number.setText(stockInfoVo.getNowStore()+"");
	        viewHolder.tock_goods_number.setText(stockInfoVo.getFallDueNum()+"");
	        viewHolder.stock_good_code.setText(stockInfoVo.getBarCode());
	    
	        if(!isSingle){
	        	viewHolder.tock_total_layoutd.setVisibility(View.GONE);
	        	viewHolder.tock_purchase_price_text.setText("零售价:");
	        	if (stockInfoVo.getPowerPrice()!=null) {
	        		viewHolder.tock_purchase_price.setText(stockInfoVo.getPowerPrice()+"");
				}
	        }else{
	        	if (stockInfoVo.getPowerPrice()!=null) {
	        		viewHolder.tock_purchase_price.setText(stockInfoVo.getPowerPrice()+"");
	        	}
	        	if (stockInfoVo.getSumMoney()!=null) {
	        		viewHolder.tock_total.setText(stockInfoVo.getSumMoney()+"");
	        	}
	        }
        }	
        return convertView;
	}
	
	private class ViewHolder {
        TextView stock_title;
        TextView stock_good_code;
        TextView tock_number;
        TextView tock_goods_number;
        TextView tock_purchase_price;
        TextView tock_total;
        TextView tock_purchase_price_text;
        LinearLayout tock_total_layoutd;
	}
}
