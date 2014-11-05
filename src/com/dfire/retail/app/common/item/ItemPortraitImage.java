package com.dfire.retail.app.common.item;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.util.StringUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemPortraitImage extends ItemBase{

	public ItemPortraitImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.globe_item_image_portrait, this);
		initMainView();
		saveTag.setVisibility(View.GONE);
	}

	private String label;
	private String hit;
	private Bitmap mBitmap;

	private TextView lblName;
	private ImageView img;
	private ImageView portrait;
	private TextView lblHit;

	/**
	 * <code>注册事件处理</code>.
	 */

	/**
	 * @param label
	 *            标签名称.
	 * @param hit
	 *            提示信息.
	 */
	public void initLabel(String label, String hit) {
		lblName.setText(StringUtils.isEmpty(label) ? "" : label);
		//lblHit.setHint(StringUtils.isEmpty(hit) ? "" : hit);
		//lblHit.setVisibility(StringUtils.isEmpty(hit) ? View.GONE
		//		: View.VISIBLE);
	}


	public void initData(Bitmap val) {
		//setOldVal(StringUtils.isEmpty(val) ? "" : val);
		//changeData(val);
	}

	public void changeData(String val) {
	
	}

	public String getStrVal() {
		return getCurrVal();
	}

	public Boolean getVal() {
		return StringUtils.isEquals("1", getCurrVal());
	}

	public void clearChange() {
		setOldVal(getCurrVal());
		isChange();
	}

	/**
	 * 初始化.
	 */
	private void initMainView() {
		lblName = (TextView) this.findViewById(R.id.lblName);
		//lblHit = (TextView) this.findViewById(R.id.lblHit);
		saveTag = (TextView) this.findViewById(R.id.saveTag);
		img = (ImageView) this.findViewById(R.id.img);
		portrait = (ImageView) this.findViewById(R.id.lblVal);

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
	 * 
	 * @return 图标.
	 */
	public ImageView getImg() {
		return img;
	}
}
