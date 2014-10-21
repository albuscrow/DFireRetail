package com.dfire.retail.app.manage.adapter;

import java.util.List;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.goodsmanager.Category;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsListWithImageActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsSearchActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsSortListActivity;
import com.dfire.retail.app.manage.data.GoodsVo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsSortListForMenuAdapter extends BaseAdapter {
	GoodsSearchActivity activity;
	private LayoutInflater layoutInflater;
	private List<Category> data;

	public GoodsSortListForMenuAdapter(
			GoodsSearchActivity goodsSearchActivity, List<Category> goods) {
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