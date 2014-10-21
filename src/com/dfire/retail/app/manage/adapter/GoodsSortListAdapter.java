package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.R.layout;
import com.dfire.retail.app.manage.activity.goodsmanager.Category;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsManagerBaseActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsSearchActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsSortListActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GoodsSortListAdapter extends BaseAdapter {
	
	GoodsManagerBaseActivity activity;
	LayoutInflater layoutInflater;
	private ArrayList<Category> data;
	
	public GoodsSortListAdapter(GoodsManagerBaseActivity activity, ArrayList<Category> categorys) {
		this.activity = activity;
		this.layoutInflater = LayoutInflater.from(activity);
		this.data = categorys;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		try {
			return Long.valueOf(data.get(position).id);
		} catch (Exception e) {
			return -1l;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_goods_sort, null);
		}
		Category category = data.get(position);
		String name = "";
		for (int i = 0; i < category.depth; ++i) {
			name += " · ";
		}
		name += category.name;
		((TextView)convertView.findViewById(R.id.name)).setText(name);
		
		return convertView;
	}

}