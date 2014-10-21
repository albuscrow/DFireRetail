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
import com.dfire.retail.app.manage.data.StockCheckRecordVo;

/**
 * 库存管理-盘点记录查询适配器
 * @author wangpeng
 *
 */
public class StockCheckRecordAdapter extends BaseAdapter{

	private Context context;
	private List<StockCheckRecordVo> list=new ArrayList<StockCheckRecordVo>();
	private LayoutInflater mLayoutInflater;
	
	public StockCheckRecordAdapter(Context context, List<StockCheckRecordVo> list) {
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
            convertView = mLayoutInflater.inflate(R.layout.stock_check_record_item, null);
            viewHolder = new ViewHolder();
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.money = (TextView) convertView.findViewById(R.id.money);
            viewHolder.total = (TextView) convertView.findViewById(R.id.total);
            viewHolder.sum = (TextView) convertView.findViewById(R.id.sum);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        	
	        viewHolder.time.setText(list.get(position).getStockCheckRecordTime());
	        viewHolder.money.setText(list.get(position).getStockMoney()+"");
	        viewHolder.total.setText(list.get(position).getStockTotalCount());		        
	        viewHolder.sum.setText(list.get(position).getExhibit()+"");
	                
        return convertView;
	}
	
	 private class ViewHolder {
	        TextView time;
	        TextView money;
	        TextView total;
	        TextView sum;
	        
	    }

}
