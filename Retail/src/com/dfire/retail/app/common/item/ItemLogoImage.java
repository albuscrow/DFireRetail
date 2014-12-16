package com.dfire.retail.app.common.item;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.util.StringUtils;

public class ItemLogoImage extends ItemBase {

	public ItemLogoImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.globe_item_image_logo, this);
		initMainView();
		saveTag.setVisibility(View.GONE);
	}

	private Bitmap oldBitmap;
	private Bitmap newBitmap;

	private TextView lblName;
	private ImageView img;
	private ImageView portrait;
	private LinearLayout mAddLogo;

	/**
	 * @param label
	 *            标签名称.
	 * @param hit
	 *            提示信息.
	 */
	public void initLabel(String label, String hit) {
		lblName.setText(StringUtils.isEmpty(label) ? "" : label);
	}

	public void initData(Bitmap val) {
		setOldImage(val);
		portrait.setVisibility(View.VISIBLE);
		portrait.setImageBitmap(val);
//		isChange();
	}
	
	public void changeData(Bitmap val) {
		portrait.setImageBitmap(val);
		newBitmap = val;
		isChange();
		if (val != null) {
			img.setVisibility(View.VISIBLE);
//			mAddLogo.setVisibility(View.GONE);
		} else {
			img.setVisibility(View.INVISIBLE);
			mAddLogo.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 返回图片信息的二进制
	 * 
	 * @return
	 */
	public byte[] getPortrait() {
		if (newBitmap == null)
			return null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		newBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
		byte[] b = stream.toByteArray();
		return b;
	}

	public Bitmap getBitmapPortrait() {
		return newBitmap;
	}

	public void clearChange() {
		setOldImage(getBitmapPortrait());
		isChange();
	}

	public void setOldImage(Bitmap bitmap) {
		oldBitmap = bitmap;
	}

	/**
	 * 初始化.
	 */
	private void initMainView() {
		lblName = (TextView) this.findViewById(R.id.lblName);
		saveTag = (TextView) this.findViewById(R.id.saveTag);
		img = (ImageView) this.findViewById(R.id.img);
		portrait = (ImageView) this.findViewById(R.id.lblVal);
		lblName.setFocusable(false);
		mAddLogo = (LinearLayout) findViewById(R.id.add_shop_logo_line);
		portrait.setFocusable(true);
		this.setFocusableInTouchMode(true);
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeData(null);
			}
		});
	}

	@Override
	public Boolean isChange() {
		if (newBitmap == null && oldBitmap != null) {
			changeStatus = true;
		} else if (newBitmap == null && oldBitmap == null) {
			changeStatus = false;
		} else {
			changeStatus = !newBitmap.equals(oldBitmap);
		}
		if (newBitmap != null) {
//			mAddLogo.setVisibility(View.GONE);
			portrait.setVisibility(View.VISIBLE);
		} else {
			mAddLogo.setVisibility(View.VISIBLE);
//			portrait.setVisibility(View.GONE);
		}
		saveTag.setVisibility(changeStatus ? View.VISIBLE : View.GONE);
		// 如果有修改，把是否修改的结果信息反馈给UI界面
		if (itemIsChangeListener != null)
			itemIsChangeListener.onItemIsChangeListener(ItemLogoImage.this);

		return changeStatus;
	}

	/**
	 * 得到名称标签.
	 * 
	 * @return 名称标签.
	 */
	public TextView getLblName() {
		return lblName;
	}

	/**
	 * 得到图标.
	 * @return 图标.
	 */
	public ImageView getImg() {
		return img;
	}
	/**
	 * 得到图片
	 */
	public LinearLayout getAddLogo(){
		return mAddLogo;
	}
	public byte[] getBytes() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		newBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 60;
		while (baos.toByteArray().length / 1024 > 1024) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			newBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		// ByteArrayInputStream isBm = new
		// ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		// Bitmap bitmap = BitmapFactory.decodeStream(isBm, null,
		// null);//把ByteArrayInputStream数据生成图片
		return baos.toByteArray();
	}
}
