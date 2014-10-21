package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.StockInfoVo;

/**
 * 库存管理-库存查询适配器
 * @author wangpeng
 *
 */
public class StockQueryAdapter extends BaseAdapter{

	private Context context;
	private List<StockInfoVo> list=new ArrayList<StockInfoVo>();
	private LayoutInflater mLayoutInflater;
	
	public StockQueryAdapter(Context context, List<StockInfoVo> list) {
		super();
		this.context = context;
		this.list = list;
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
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.stock_query_item, null);
            viewHolder = new ViewHolder();
            viewHolder.stock_title = (TextView) convertView.findViewById(R.id.stock_title);
            viewHolder.tock_number = (TextView) convertView.findViewById(R.id.tock_number);
            viewHolder.tock_goods_number = (TextView) convertView.findViewById(R.id.tock_goods_number);
            viewHolder.tock_purchase_price = (TextView) convertView.findViewById(R.id.tock_purchase_price);
            viewHolder.tock_total = (TextView) convertView.findViewById(R.id.tock_total);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        	
	        viewHolder.stock_title.setText(list.get(position).getGoodsName()+"");
	        viewHolder.tock_number.setText("库存数:"+list.get(position).getFallDueNum()+"");
	        viewHolder.tock_goods_number.setText("临期商品数:"+list.get(position).getFallDueNum()+"");		        
	        viewHolder.tock_purchase_price.setText("进货价:"+list.get(position).getPowerPrice()+"");
	        viewHolder.tock_total.setText("总金额:"+list.get(position).getSumMoney()+"");
	                
        return convertView;
	}
	
	 private class ViewHolder {
	        TextView stock_title;
	        TextView tock_number;
	        TextView tock_goods_number;
	        TextView tock_purchase_price;
	        TextView tock_total;
	        ImageView imageView;
	        
	    }

}