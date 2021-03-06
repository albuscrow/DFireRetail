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
import com.dfire.retail.app.manage.data.OrderGoodsVo;

/**
 * 物流管理-门店订货-订货单信息适配器
 * @author wangpeng
 *
 */
public class StoreOrderGoodsAdapter extends BaseAdapter{

	private Context context;
	private List<OrderGoodsVo> list=new ArrayList<OrderGoodsVo>();
	private LayoutInflater mLayoutInflater;
	
	public StoreOrderGoodsAdapter(Context context, List<OrderGoodsVo> list) {
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
            convertView = mLayoutInflater.inflate(R.layout.store_order_goods_item, null);
            viewHolder = new ViewHolder();
            viewHolder.orderGoodsNo = (TextView) convertView.findViewById(R.id.orderGoodsNo);
            viewHolder.sendEndTime = (TextView) convertView.findViewById(R.id.sendEndTime);
            viewHolder.billStatus = (TextView) convertView.findViewById(R.id.billStatus);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        	
	        viewHolder.orderGoodsNo.setText(list.get(position).getOrderGoodsNo());
	        viewHolder.sendEndTime.setText(list.get(position).getSendEndTime());
	        viewHolder.billStatus.setText(list.get(position).getBillStatus());		        
	                
        return convertView;
	}
	
	 private class ViewHolder {
	        TextView orderGoodsNo;
	        TextView sendEndTime;
	        TextView billStatus;
	        
	    }

}
