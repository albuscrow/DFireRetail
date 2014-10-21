package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsListWithImageActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsSearchForOptActivity;
import com.dfire.retail.app.manage.data.GoodsHandleVo;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsListWithImageForPrecessAdapter extends BaseAdapter {
	Activity activity;
	private LayoutInflater layoutInflater;
	private List<GoodsVo> data;

	public GoodsListWithImageForPrecessAdapter(
			GoodsSearchForOptActivity goodsSearchForProcessActivity, List<GoodsVo> goodsHandleList) {
		this.activity = goodsSearchForProcessActivity;
		this.layoutInflater = LayoutInflater.from(activity);
		this.data = goodsHandleList;
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
		ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
		ImageLoader.getInstance().displayImage(goods.getFileName(), imageView);
		
		((TextView)convertView.findViewById(R.id.name)).setText(goods.getGoodsName());
		((TextView)convertView.findViewById(R.id.price)).setText("￥"+goods.getPetailPrice());
		return convertView;
	}

	public void refreshData(List<GoodsVo> goods) {
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
