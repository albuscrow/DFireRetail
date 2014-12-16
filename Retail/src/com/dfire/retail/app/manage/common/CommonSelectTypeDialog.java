package com.dfire.retail.app.manage.common;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.wheel.widget.WheelView;
import com.dfire.retail.app.manage.wheel.widget.adapters.AbstractWheelTextAdapter;
import com.dfire.retail.app.manage.wheel.widget.adapters.ArrayWheelAdapter;

public class CommonSelectTypeDialog extends Dialog {

	private Context mContext;
	private CardTypeAdapter mAdapter;
	private WheelView mWheelView;
	private Button mConfirmButton, mCancelButton;
	private ArrayList<String> list;
	private TextView title;

	public CommonSelectTypeDialog(Context context,ArrayList<String> list) {
		super(context, R.style.dialog);
		mContext = context;
		this.list = list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_type_dialog);

		mConfirmButton = (Button) findViewById(R.id.card_type_confirm);
		mCancelButton = (Button) findViewById(R.id.card_type_cancel);
		title = (TextView) findViewById(R.id.card_type_title);
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
		if(list.size() > 4)
			mWheelView.setVisibleItems(4); // Number of items
		else{
			mWheelView.setVisibleItems(list.size()); // Number of items
		}
		mWheelView.setWheelBackground(android.R.color.transparent);
		mWheelView.setWheelForeground(android.R.color.transparent);
		mWheelView.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		mAdapter = new CardTypeAdapter(mContext,list);
		mWheelView.setViewAdapter(mAdapter);
		mWheelView.setCurrentItem(0);
	}


	
	public TextView getTitle() {
		return title;
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

	public int getCurrentPosition(){
		
		return mWheelView.getCurrentItem();
	}
	
	/**
	 * 显示已选择的项目名称
	 * 
	 * @param cardTypeId
	 */
	public void updateType(String type) {
		if (type!=null && !CommonUtils.isEmpty(type)) {
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).equals(type)) {
						mWheelView.setCurrentItem(i);
						break;
					}
				}
			}
		} else {
			mWheelView.setCurrentItem(0);
		}
	}
	
	
	private class CardTypeAdapter extends ArrayWheelAdapter<String> {
		
		private ArrayList<String> mlist;
		/**
		 * Constructor
		 */
		protected CardTypeAdapter(Context context,ArrayList<String> list) {
			super(context, list);
			this.mlist = list;
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			// if (currentItem == currentValue) {
			// view.setTextColor(0xFF0000F0);
			// }
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			// currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

}
