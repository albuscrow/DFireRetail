package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.SearchGoodsVo;

/**
 * 物流 查询商品  商品适配器
 * @author wangpeng
 *
 */
public class StockCheckGoodsListAdapter extends BaseAdapter{

	private List<SearchGoodsVo> list;
	private LayoutInflater mLayoutInflater;
	
	public StockCheckGoodsListAdapter(Context context, List<SearchGoodsVo> list) {
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
	    SearchGoodsVo goods = (SearchGoodsVo)this.getItem(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.stock_check_goods_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
            viewHolder.goods_price = (TextView) convertView.findViewById(R.id.goods_price);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        	
	    viewHolder.goods_name.setText(goods.getGoodsName());
	    viewHolder.goods_price.setText("￥:"+goods.getPurchasePrice());
	                
        return convertView;
	}
	
	 private class ViewHolder {
	        TextView goods_name;
	        TextView goods_price;
	        
	    }

}
