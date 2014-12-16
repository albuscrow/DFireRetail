package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.goodsmanager.ChooseGoodsActivity;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseGoodsAdapter extends BaseAdapter {
	Activity activity;
	private LayoutInflater layoutInflater;
	private List<GoodsVo> data;

	public ChooseGoodsAdapter(
			ChooseGoodsActivity chooseGoodsActivity, List<GoodsVo> goodsHandleList) {
		this.activity = chooseGoodsActivity;
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
		if (data == null) {
			return null;
		}
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
			convertView = layoutInflater.inflate(R.layout.item_good_with_image_w, null);
		}
		GoodsVo goods = data.get(position);
		final ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
//		ImageLoader.getInstance().displayImage(goods.getFileNameSmall(), imageView);
		ImageLoader.getInstance().loadImage(goods.getFileNameSmall(), new ImageLoadingListener() {

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
					imageView.setImageBitmap(getRoundedCornerBitmap(loadedImage, 5));
				}
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {

			}

		});
		((TextView)convertView.findViewById(R.id.name)).setText(goods.getGoodsName());
		((TextView)convertView.findViewById(R.id.price)).setText("￥"+goods.getPetailPrice());
		convertView.findViewById(R.id.arrow).setVisibility(View.GONE);
		return convertView;
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
				.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public void refreshData(List<GoodsVo> goods) {
		if (goods == null) {
			return;
		}
		this.data = goods;
		notifyDataSetChanged();
	}
	
	public void addData(List<GoodsVo> datas){
		if (datas == null) {
			return;
		}
		if (this.data == null) {
			this.data = new ArrayList<GoodsVo>();
		}
		this.data.addAll(datas);
		notifyDataSetChanged();
	}

	public List<GoodsVo> getData() {
		return data;
	}

}
