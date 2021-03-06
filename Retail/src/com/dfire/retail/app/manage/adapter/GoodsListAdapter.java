package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsListActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsListWithImageActivity;
import com.dfire.retail.app.manage.data.GoodsVo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class GoodsListAdapter extends BaseAdapter {
	GoodsListActivity activity;
	private LayoutInflater layoutInflater;
	private List<GoodsVo> data;
	private boolean[] selectStatus;

	public GoodsListAdapter(
			GoodsListActivity goodsListActivity, List<GoodsVo> goods, boolean[] goodsSelectStatus) {
		this.activity = goodsListActivity;
		this.layoutInflater = LayoutInflater.from(activity);
		this.data = goods;
		this.selectStatus = goodsSelectStatus;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.size();
	}

	@Override
	public long getItemId(int position) {
		try {
			return Long.parseLong(data.get(position).getGoodsId());
		} catch (Exception e) {
			return -1l;
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_good, null);
		}
		CheckBox checkbox = (CheckBox)convertView.findViewById(R.id.good_checkbox);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				selectStatus[position] = isChecked;
			}
		});
		checkbox.setChecked(selectStatus[position]);
		((TextView)convertView.findViewById(R.id.good_name)).setText(data.get(position).getGoodsName());
		return convertView;
	}

	public void refreshData(ArrayList<GoodsVo> goods, boolean[] status) {
		this.data = goods;
		this.selectStatus = status;
		notifyDataSetChanged();
	}

	public void reset() {
		for (int i = 0; i < selectStatus.length; ++i) {
			selectStatus[i] = false;
		}
		notifyDataSetChanged();
	}

}
