package com.dfire.retail.app.manage.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.stockmanager.StockRemindSettingGoodsListActivity;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.util.ImageUtil;
import com.dfire.retail.app.manage.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

@SuppressLint("InflateParams")
public class StockRemindSettingGoodsAdapter extends BaseAdapter {
	StockRemindSettingGoodsListActivity activity;
	private LayoutInflater layoutInflater;
	private List<GoodsVo> data;

	public StockRemindSettingGoodsAdapter(
			StockRemindSettingGoodsListActivity stockRemindSettingGoodsListActivity, List<GoodsVo> goods) {
		this.activity = stockRemindSettingGoodsListActivity;
		this.layoutInflater = LayoutInflater.from(activity);
		this.data = goods;
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		GoodsVo goods = data.get(position);
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_good_with_image, null);
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView)convertView.findViewById(R.id.image);
			viewHolder.name = (TextView)convertView.findViewById(R.id.name);
			viewHolder.price = (TextView)convertView.findViewById(R.id.price);
			convertView.setTag(viewHolder);
		}else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
		final ImageView imageView = viewHolder.image;
		if (goods!=null) {
			String imageUrl = goods.getFileNameSmall();
        	if (!StringUtils.isEmpty(imageUrl)&&!StringUtils.isEquals(imageUrl, "")) {
				ImageLoader.getInstance().loadImage(imageUrl, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						imageView.setImageResource(R.drawable.pic_none);
					}
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						if (loadedImage == null) {
							imageView.setImageResource(R.drawable.pic_none);
						}else{
							imageView.setImageBitmap(ImageUtil.getRoundedCornerBitmap(loadedImage, 5));
						}
					}
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
					}
				});
        	}else {
				viewHolder.image.setImageResource(R.drawable.pic_none);
			}
			
			
			if (imageUrl != null) {
				ImageLoader.getInstance().displayImage(imageUrl,viewHolder.image);
			}
			viewHolder.name.setText(goods.getGoodsName());
			viewHolder.price.setText("￥"+goods.getPetailPrice());
		}
		return convertView;
	}
	private class ViewHolder {
       TextView name;
       TextView price;
       ImageView image;
	}
}
