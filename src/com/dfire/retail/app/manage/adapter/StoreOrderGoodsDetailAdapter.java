package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.OrderGoodsDetailVo;

/**
 * 物流管理-门店订货-订货单详细信息适配器
 * @author wangpeng
 *
 */
public class StoreOrderGoodsDetailAdapter extends BaseAdapter{

	private Context context;
	private List<OrderGoodsDetailVo> list=new ArrayList<OrderGoodsDetailVo>();
	private LayoutInflater mLayoutInflater;
	
	public StoreOrderGoodsDetailAdapter(Context context, List<OrderGoodsDetailVo> list) {
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
            convertView = mLayoutInflater.inflate(R.layout.store_order_goods_detail_item, null);
            viewHolder = new ViewHolder();
            viewHolder.goodsName = (TextView) convertView.findViewById(R.id.goodsName);
            viewHolder.nowStore = (TextView) convertView.findViewById(R.id.nowStore);
            viewHolder.order_goods_detail_1=(ImageView)convertView.findViewById(R.id.order_goods_detail_1);
            viewHolder.order_goods_detail_2=(ImageView)convertView.findViewById(R.id.order_goods_detail_2);
            viewHolder.order_goods_detail_edit=(EditText)convertView.findViewById(R.id.order_goods_detail_edit);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        	
	        viewHolder.goodsName.setText(list.get(position).getGoodsName());
	        viewHolder.nowStore.setText(list.get(position).getNowStore());
	                
        return convertView;
	}
	
	 private class ViewHolder {
	        TextView goodsName;
	        TextView nowStore;
	        ImageView order_goods_detail_1;
	        ImageView order_goods_detail_2;
	        EditText order_goods_detail_edit;
	    }

}
