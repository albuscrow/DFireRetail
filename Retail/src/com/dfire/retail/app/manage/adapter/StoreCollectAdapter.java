package com.dfire.retail.app.manage.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.StockInVo;

/**
 * 物流管理-门店进货-门店进货信息适配器
 * @author ys
 *
 */
@SuppressLint({ "SimpleDateFormat", "InflateParams" })
public class StoreCollectAdapter extends BaseAdapter{

	private List<StockInVo> stockInVos =new ArrayList<StockInVo>();
	private LayoutInflater mLayoutInflater;
	
	public StoreCollectAdapter(Context context, List<StockInVo> stockInVos) {
		super();
		this.stockInVos = stockInVos;
		mLayoutInflater =  LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return stockInVos.size();
	}

	@Override
	public Object getItem(int position) {
		return stockInVos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		StockInVo stockInVo = stockInVos.get(position);
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
        if (stockInVo!=null) {
        	viewHolder.orderGoodsNo.setText(stockInVo.getStockInNo());
			if (stockInVo.getSendEndTime()!=null) {
				Date date = new Date(Long.parseLong(stockInVo.getSendEndTime()));
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				viewHolder.sendEndTime.setText("要求到货日："+format.format(date));
			}
			viewHolder.billStatus.setText(stockInVo.getBillStatusName());
			if (stockInVo.getBillStatus()==1) {
				viewHolder.billStatus.setTextColor(Color.parseColor("#00CC00"));
			}else{
				viewHolder.billStatus.setTextColor(Color.parseColor("#FF0033"));
			}
		}
        return convertView;
	}
	
	 private class ViewHolder {
	        TextView orderGoodsNo;
	        TextView sendEndTime;
	        TextView billStatus;
	    }

}
