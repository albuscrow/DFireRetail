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
import com.dfire.retail.app.manage.data.StockAdjustVo;

/**
 * 库存管理-库存调整-库存调整信息适配器
 * @author ys
 */
@SuppressLint({ "SimpleDateFormat", "InflateParams" })
public class StockAdjustmentAdapter extends BaseAdapter{

	private List<StockAdjustVo> adjustVos =new ArrayList<StockAdjustVo>();
	private LayoutInflater mLayoutInflater;
	
	public StockAdjustmentAdapter(Context context, List<StockAdjustVo> adjustVos) {
		super();
		this.adjustVos = adjustVos;
		mLayoutInflater =  LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return adjustVos.size();
	}

	@Override
	public Object getItem(int position) {
		return adjustVos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		StockAdjustVo stockInVo = adjustVos.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.stock_adjustment_item, null);
            viewHolder = new ViewHolder();
            viewHolder.adjustCode = (TextView) convertView.findViewById(R.id.adjustCode);
            viewHolder.resultPrice = (TextView) convertView.findViewById(R.id.resultPrice);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.opTime = (TextView) convertView.findViewById(R.id.opTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (stockInVo!=null) {
        	viewHolder.adjustCode.setText(stockInVo.getAdjustCode());
        	viewHolder.resultPrice.setText(stockInVo.getResultPrice()+"");
        	viewHolder.name.setText("操作人："+stockInVo.getName());
			if (stockInVo.getOpTime()!=null) {
				Date date = new Date(Long.parseLong(stockInVo.getOpTime()));
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				viewHolder.opTime.setText("调整日期："+format.format(date));
			}
		}
        return convertView;
	}
	
	 private class ViewHolder {
	    private TextView adjustCode;
	    private TextView resultPrice;
	    private TextView name;
	    private TextView opTime;
	}
}
