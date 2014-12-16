package com.dfire.retail.app.manage.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.goodsmanager.Category;

public class GoodsSortListForMenuAdapter extends BaseAdapter {
	Activity activity;
	private LayoutInflater layoutInflater;
	private List<Category> data;

	public GoodsSortListForMenuAdapter(
			Activity goodsSearchActivity, List<Category> goods) {
		this.activity = goodsSearchActivity;
		this.layoutInflater = LayoutInflater.from(activity);
		this.data = goods;
	}

	@Override
	public int getCount() {
		if (data == null) {
			return 0;
		}
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		try {
			return Long.parseLong(data.get(position).id);
		} catch (Exception e) {
			return -1l;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_category, null);
		}
		Category category = data.get(position);
		((TextView)convertView.findViewById(R.id.parent)).setText(category.parents);
		((TextView)convertView.findViewById(R.id.name)).setText(category.name);
		return convertView;
	}

}
