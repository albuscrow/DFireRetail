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
import com.dfire.retail.app.manage.vo.supplyManageVo;

@SuppressLint({ "ResourceAsColor", "SimpleDateFormat", "InflateParams" })
public class SupplyListAdapter extends BaseAdapter {
	
	private List<supplyManageVo> supplyManageVos;
	private LayoutInflater layoutInflater;
	
	public SupplyListAdapter(Context mContext,List<supplyManageVo> supplyManageVos) {
		this.supplyManageVos = supplyManageVos;
		this.layoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return supplyManageVos.size();
	}

	@Override
	public Object getItem(int position) {
		return supplyManageVos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.activity_supply_list_adapter_item, null);
		}
		supplyManageVo supplyManageVo = supplyManageVos.get(position);
		if (supplyManageVos!=null) {
			((TextView)convertView.findViewById(R.id.supply_name)).setText(supplyManageVo.getName());
			((TextView)convertView.findViewById(R.id.supply_phone)).setText(supplyManageVo.getPhone());
			((TextView)convertView.findViewById(R.id.linkman)).setText(supplyManageVo.getRelation());
		}
		return convertView;
	}

}
