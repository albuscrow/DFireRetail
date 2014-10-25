package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsListWithImageActivity;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsListWithImageAdapter extends BaseAdapter {
	GoodsListWithImageActivity activity;
	private LayoutInflater layoutInflater;
	private List<GoodsVo> data;

	public GoodsListWithImageAdapter(
			GoodsListWithImageActivity goodsListWithImageActivity, List<GoodsVo> goods) {
		this.activity = goodsListWithImageActivity;
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
			return Long.parseLong(data.get(position).getGoodsId());
		} catch (Exception e) {
			return -1l;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_good_with_image, null);
		}
		GoodsVo goods = data.get(position);
		String imageUrl = goods.getFileNameSmall();
		if (imageUrl != null) {
			ImageLoader.getInstance().displayImage(imageUrl, (ImageView) convertView.findViewById(R.id.image));
		}
		((TextView)convertView.findViewById(R.id.name)).setText(goods.getGoodsName());
		((TextView)convertView.findViewById(R.id.price)).setText("￥"+goods.getPetailPrice());
		
		return convertView;
	}

	public void refreshData(ArrayList<GoodsVo> goods) {
		this.data = goods;
		notifyDataSetChanged();
	}
	
	public void addData(List<GoodsVo> datas){
		this.data.addAll(datas);
		notifyDataSetChanged();
	}

	public List<GoodsVo> getData() {
		return data;
	}

}
