package com.dfire.retail.app.manage.common;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.wheel.widget.WheelView;
import com.dfire.retail.app.manage.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * 状态 字典表
 * @author ys
 */
@SuppressLint("NewApi")
public class BillStatusDialog extends Dialog {

	private Context mContext;
	private CardTypeAdapter mAdapter;
	private WheelView mWheelView;
	private Button mConfirmButton, mCancelButton;
	private TextView mTitle;
	private List<DicVo> dicVos;

	public BillStatusDialog(Context context,List<DicVo> dicVos) {
		super(context, R.style.dialog);
		this.dicVos = dicVos;
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_type_dialog);
		
		mTitle = (TextView) findViewById(R.id.card_type_title);
		TextPaint tp = mTitle.getPaint(); //加粗
		tp.setFakeBoldText(true); 
		mConfirmButton = (Button) findViewById(R.id.card_type_confirm);
		mCancelButton = (Button) findViewById(R.id.card_type_cancel);
		this.setCanceledOnTouchOutside(true);
		Window window = this.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		Point p = new Point();
		window.getWindowManager().getDefaultDisplay().getSize(p);
		params.width = p.x;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);

		mWheelView = (WheelView) findViewById(R.id.card_type_wheel);
		mWheelView.setVisibleItems(4); // Number of items
		mWheelView.setWheelBackground(android.R.color.transparent);
		mWheelView.setWheelForeground(android.R.color.transparent);
		mWheelView.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		mAdapter = new CardTypeAdapter(mContext,dicVos);
		mWheelView.setViewAdapter(mAdapter);
		mWheelView.setCurrentItem(2);
	}

	public Button getConfirmButton() {
		return mConfirmButton;

	}

	public Button getCancelButton() {
		return mCancelButton;

	}
	/**
	 * 显示已选择的项目名称
	 * 
	 * @param cardTypeId
	 */
	public void updateType(Integer TypeId) {
		if (TypeId!=null&&!TypeId.equals("")) {
			if (dicVos.size() > 0) {
				for (int i = 0; i < dicVos.size(); i++) {
					if (dicVos.get(i).getVal()==TypeId) {
						mWheelView.setCurrentItem(i);
						break;
					}
				}
			}
		} else {
			mWheelView.setCurrentItem(0);
		}
	}
	public int getCurrentData() {
		return mWheelView.getCurrentItem();
	}
	public TextView getmTitle() {
		return mTitle;
	}

	public void setmTitle(TextView mTitle) {
		this.mTitle = mTitle;
	}

	private class CardTypeAdapter extends AbstractWheelTextAdapter {
		private List<DicVo> dicVos;
		protected CardTypeAdapter(Context context,List<DicVo> dicVos) {
			super(context, R.layout.card_type_wheel, NO_RESOURCE);
			setItemTextResource(R.id.card_type_text);
			this.dicVos = dicVos;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return dicVos.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return dicVos.get(index).getName();
		}
	}

}
