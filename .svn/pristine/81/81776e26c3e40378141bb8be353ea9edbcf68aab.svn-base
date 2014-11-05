package com.dfire.retail.app.manage.adapter;

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
import com.dfire.retail.app.manage.data.LogisticsVo;

@SuppressLint({ "ResourceAsColor", "SimpleDateFormat", "InflateParams" })
public class LogisticsRecordAdapter extends BaseAdapter {
	
	private List<LogisticsVo> logisticsRecords;
	private LayoutInflater layoutInflater;
	
	public LogisticsRecordAdapter(Context mContext,List<LogisticsVo> logisticsRecords) {
		this.logisticsRecords = logisticsRecords;
		this.layoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return logisticsRecords.size();
	}

	@Override
	public Object getItem(int position) {
		return logisticsRecords.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.activity_logistics_record_adapter_item, null);
		}
		LogisticsVo logisticsVo = logisticsRecords.get(position);
		if (logisticsVo!=null) {
			((TextView)convertView.findViewById(R.id.supply_name)).setText(logisticsVo.getSupplyName()+"");
			((TextView)convertView.findViewById(R.id.logistics_no)).setText(logisticsVo.getLogisticsNo());
			((TextView)convertView.findViewById(R.id.type_name)).setText(logisticsVo.getTypeName()+"");
			((TextView)convertView.findViewById(R.id.bill_status)).setText(logisticsVo.getBillStatusName());
			if (logisticsVo.getBillStatus()==1) {
				((TextView)convertView.findViewById(R.id.bill_status)).setTextColor(Color.parseColor("#00CC00"));
			}else if(logisticsVo.getBillStatus()==2) {
				((TextView)convertView.findViewById(R.id.bill_status)).setTextColor(Color.parseColor("#FF0033"));
			}
		}
		return convertView;
	}

}
