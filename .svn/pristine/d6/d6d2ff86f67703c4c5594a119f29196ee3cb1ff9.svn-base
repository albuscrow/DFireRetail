package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.goodsmanager.Category;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsManagerBaseActivity;

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
	
	static final int[] DOTS_IDS = new int[]{R.drawable.category_dot, R.drawable.category_dot1, R.drawable.category_dot2};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_goods_sort, null);
		}
		TextView categoryName = (TextView)convertView.findViewById(R.id.name);
		Category category = data.get(position);
		if (category.depth != 0) {
			Drawable bitmap = activity.getResources().getDrawable(DOTS_IDS[category.depth - 1]);
			bitmap.setBounds(0, 0, bitmap.getIntrinsicWidth(), bitmap.getIntrinsicHeight());
			categoryName.setCompoundDrawables(bitmap, null, null, null);
		}else{
			categoryName.setCompoundDrawables(null, null, null, null);
		}
		categoryName.setText(category.name);
		
		return convertView;
	}

}