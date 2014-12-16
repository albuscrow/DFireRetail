package com.dfire.retail.app.manage.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.LogisticsDetailVo;

@SuppressLint("InflateParams")
public class LogisticsRecordDetailAdapter extends BaseAdapter{

	private List<LogisticsDetailVo> detailVos;
	
	private LayoutInflater mLayoutInflater;
	
	private boolean isPrice;
	
	public LogisticsRecordDetailAdapter(Context mContext,List<LogisticsDetailVo> detailVos,boolean isPrice) {
		super();
		this.detailVos = detailVos;
		this.isPrice = isPrice;
		mLayoutInflater =  LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		return detailVos.size();
	}

	@Override
	public Object getItem(int position) {
		return detailVos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		LogisticsDetailVo detailVo = detailVos.get(position);
		if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.activity_logistics_records_detail_item, null);
            viewHolder = new ViewHolder();
            viewHolder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
            viewHolder.goods_price = (TextView) convertView.findViewById(R.id.goods_price);
            viewHolder.goods_sum = (TextView) convertView.findViewById(R.id.goods_sum);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
		if (detailVo!=null) {
            viewHolder.goods_name.setText(detailVo.getGoodsName());
            viewHolder.goods_sum.setText(detailVo.getGoodsSum()+"");
            if (isPrice) {
            	viewHolder.goods_price.setText(String.format("%.2f", detailVo.getGoodsPrice()));
			}
		}
		return convertView;
	}
	private class ViewHolder {
	   TextView goods_name;
	   TextView goods_price;
	   TextView goods_sum;
	}
	public void setIsPrice(boolean isPrice){
		this.isPrice = isPrice;
		this.notifyDataSetChanged();
	}
}
