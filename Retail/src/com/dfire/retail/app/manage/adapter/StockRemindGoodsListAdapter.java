package com.dfire.retail.app.manage.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.stockmanager.StockRemindGoodsListActivity;
import com.dfire.retail.app.manage.data.GoodsVo;

/**
 * StockRemindGoodsListActivity 适配
 * @author ys
 *
 */
@SuppressLint("InflateParams")
public class StockRemindGoodsListAdapter extends BaseAdapter {
	StockRemindGoodsListActivity activity;
	private LayoutInflater layoutInflater;
	private List<GoodsVo> data;
	private boolean[] selectStatus;

	public StockRemindGoodsListAdapter(
			StockRemindGoodsListActivity stockRemindGoodsListActivity, List<GoodsVo> goods, boolean[] goodsSelectStatus) {
		this.activity = stockRemindGoodsListActivity;
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

}
