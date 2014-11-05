package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.StockInfoVo;

/**
 * 库存管理-查询商品 适配器
 * @author ys
 *
 */
@SuppressLint("InflateParams")
public class StockAddGoodsAdapter extends BaseAdapter{

	private List<StockInfoVo> list=new ArrayList<StockInfoVo>();
	private LayoutInflater mLayoutInflater;
	
	public StockAddGoodsAdapter(Context context, List<StockInfoVo> list) {
		super();
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
            convertView = mLayoutInflater.inflate(R.layout.stock_add_goods_item, null);
            viewHolder = new ViewHolder();
            viewHolder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
            viewHolder.goods_price = (TextView) convertView.findViewById(R.id.goods_price);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        	
	        viewHolder.goods_name.setText(""+list.get(position).getGoodsName()+"");
	        viewHolder.goods_price.setText("￥:"+list.get(position).getPowerPrice()+"");
	                
        return convertView;
	}
	
	 private class ViewHolder {
	        TextView goods_name;
	        TextView goods_price;
	        
	    }

}
