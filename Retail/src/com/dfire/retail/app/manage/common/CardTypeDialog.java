/**
 * 
 */
package com.dfire.retail.app.manage.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.wheel.widget.WheelView;
import com.dfire.retail.app.manage.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * @author qiuch
 * 
 */
public class CardTypeDialog extends Dialog {

	private Context mContext;
	private CardTypeAdapter mAdapter;
	private WheelView mWheelView;
	private Button mConfirmButton, mCancelButton;

	public CardTypeDialog(Context context) {
		super(context, R.style.dialog);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_type_dialog);

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
		mAdapter = new CardTypeAdapter(mContext);
		mWheelView.setViewAdapter(mAdapter);
		mWheelView.setCurrentItem(2);
	}

	public Button getConfirmButton() {
		return mConfirmButton;

	}

	public Button getCancelButton() {
		return mCancelButton;

	}

	public String getCurrentData() {
		return mAdapter.getItemText(mWheelView.getCurrentItem()).toString();

	}

	private class CardTypeAdapter extends AbstractWheelTextAdapter {
		// 卡类型
		final String cities[] = new String[] { "金卡", "银卡", "贵宾卡", "普通会员卡" };

		/**
		 * Constructor
		 */
		protected CardTypeAdapter(Context context) {
			super(context, R.layout.card_type_wheel, NO_RESOURCE);
			setItemTextResource(R.id.card_type_text);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return cities.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return cities[index];
		}
	}

}
