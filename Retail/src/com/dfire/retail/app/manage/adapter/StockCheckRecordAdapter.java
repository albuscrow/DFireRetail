package com.dfire.retail.app.manage.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.StockCheckRecordVo;

/**
 * 库存管理-盘点记录查询适配器
 * @author ys
 *
 */
@SuppressLint({ "SimpleDateFormat", "InflateParams" })
public class StockCheckRecordAdapter extends BaseAdapter{

	private List<StockCheckRecordVo> list=new ArrayList<StockCheckRecordVo>();
	private LayoutInflater mLayoutInflater;
	
	public StockCheckRecordAdapter(Context context, List<StockCheckRecordVo> list) {
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
		StockCheckRecordVo stockCheckRecordVo = list.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.stock_check_record_item, null);
            viewHolder = new ViewHolder();
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.money = (TextView) convertView.findViewById(R.id.money);
            viewHolder.total = (TextView) convertView.findViewById(R.id.total);
            viewHolder.sum = (TextView) convertView.findViewById(R.id.sum);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (stockCheckRecordVo!=null) {
        	if (stockCheckRecordVo.getStockCheckRecordTime()!=null) {
	        	Date date = new Date(Long.parseLong(stockCheckRecordVo.getStockCheckRecordTime()));
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				viewHolder.time.setText(format.format(date));
        	}
        	viewHolder.total.setText("库存总数:"+stockCheckRecordVo.getStockTotalCount());		        
        	viewHolder.sum.setText("库存金额(元):"+stockCheckRecordVo.getStockMoney());
        	viewHolder.name.setText("操作员:"+stockCheckRecordVo.getName());
        	viewHolder.money.setText(""+stockCheckRecordVo.getExhibit());
		}	
        return convertView;
	}
	
	 private class ViewHolder {
	        TextView time;
	        TextView money;
	        TextView total;
	        TextView sum;
	        TextView name;
	        
	    }

}
