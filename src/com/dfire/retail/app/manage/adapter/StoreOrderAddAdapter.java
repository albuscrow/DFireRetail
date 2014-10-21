package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.StockInDetailVo;

/**
 * 物流管理-添加门店订货
 * @author wangpeng
 *
 */
public class StoreOrderAddAdapter extends BaseAdapter{

	private Context context;
	private List<StockInDetailVo> list=new ArrayList<StockInDetailVo>();
	private LayoutInflater mLayoutInflater;
	private ViewHolder viewHolder = null;
	public StoreOrderAddAdapter(Context context, List<StockInDetailVo> list) {
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
	
	public List<StockInDetailVo> getList() {
		return list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.stock_order_add_item, null);
            viewHolder = new ViewHolder();
            viewHolder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
            viewHolder.goods_price = (TextView) convertView.findViewById(R.id.goods_price);
            viewHolder.reduce_img = (ImageView) convertView.findViewById(R.id.reduce_img);
            viewHolder.plus_img = (ImageView) convertView.findViewById(R.id.plus_img);
            viewHolder.editText1 = (EditText) convertView.findViewById(R.id.editText1);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        	
	        viewHolder.goods_name.setText(""+list.get(position).getGoodsName()+"");
	        viewHolder.goods_price.setText("￥:"+list.get(position).getGoodsPrice()+"");
	        viewHolder.reduce_img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	        viewHolder.plus_img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	                
        return convertView;
	}
	
	 private class ViewHolder {
	        TextView goods_name;
	        TextView goods_price;
	        ImageView reduce_img;
	        ImageView plus_img;
	        EditText editText1;
	    }

}
