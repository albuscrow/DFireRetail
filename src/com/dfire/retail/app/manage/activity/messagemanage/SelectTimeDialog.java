package com.dfire.retail.app.manage.activity.messagemanage;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.wheel.widget.OnWheelChangedListener;
import com.dfire.retail.app.manage.wheel.widget.WheelView;
import com.dfire.retail.app.manage.wheel.widget.adapters.ArrayWheelAdapter;
import com.dfire.retail.app.manage.wheel.widget.adapters.NumericWheelAdapter;

public class SelectTimeDialog extends Dialog{

	private Context mContext;
	private WheelView  mHour, mMinute;
	
	private TimeArrayAdapter mHourAdapter;
	private TimeNumericAdapter mMinuteAdapter;
	private Button mConfirmButton, mCancelButton;
	private TextView mTitle;
	private LinearLayout mClearDate;
	private String mType = "";// 日期选择框类型，生日日期选择，默认为1985年1月1日;一般日期选择，默认为系统日期
	private boolean isClear;
	public SelectTimeDialog(Context context) {
		super(context);
		mContext = context;
	}
	public SelectTimeDialog(Context context, String type) {
		super(context, R.style.dialog);
		mContext = context;
		mType = type;
	}
	
	public SelectTimeDialog(Context context,boolean isClear){
		super(context,R.style.dialog);
		mContext = context;
		this.isClear = isClear;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_dialog);
		mConfirmButton = (Button) findViewById(R.id.card_type_confirm);
		mCancelButton = (Button) findViewById(R.id.card_type_cancel);
		mTitle = (TextView) findViewById(R.id.date_dialog_title);
		/*mClearDate = (LinearLayout) findViewById(R.id.clear_date);
		mClearDate.setVisibility(View.GONE);*/
		this.setCanceledOnTouchOutside(true);
		/*Window window = this.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		Point p = new Point();
		window.getWindowManager().getDefaultDisplay().getSize(p);
		params.width = p.x;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);*/

		
		mHour = (WheelView) findViewById(R.id.month_wheel);
		mMinute = (WheelView) findViewById(R.id.day_wheel);

	

		mHour.setVisibleItems(7); // Number of items
		mHour.setWheelBackground(android.R.color.transparent);
		mHour.setWheelForeground(android.R.color.transparent);
		mHour.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		mHour.setCyclic(true);

		mMinute.setVisibleItems(7); // Number of items
		mMinute.setWheelBackground(android.R.color.transparent);
		mMinute.setWheelForeground(android.R.color.transparent);
		mMinute.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		mMinute.setCyclic(true);

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(mHour, mMinute);
			}
		};
		// 显示默认日期

		Calendar calendar = Calendar.getInstance();
		int curHour = calendar.get(Calendar.HOUR_OF_DAY);
		int curMinute = calendar.get(Calendar.MINUTE);
		

		if (mType.equals("birth")) {
			curHour = 0;
			curMinute = 0;
		}
		// month
	
		String hour[] = new String[24];
		for (int i = 0; i < 24; i++) {
			 hour[i]=i+"时";
		}
		mHourAdapter = new TimeArrayAdapter(mContext, hour, curMinute);
		mHour.setViewAdapter(mHourAdapter);
		mMinute.setCurrentItem(curMinute);

	

		// day
		updateDays( mHour, mMinute);
		mHour.setCurrentItem(curMinute - 1);
		mMinute.addChangingListener(listener);
		
	}

	public void updateDays(String time) {
		if (time!=null&&!time.equals("请选择")) { 
			int hour = Integer.parseInt(time.split(":")[0]);
			int minute = Integer.parseInt(time.split(":")[1]);
			
			mHour.setCurrentItem(hour);
			mMinute.setCurrentItem(minute);
		}
	}

	public Button getConfirmButton() {
		return mConfirmButton;

	}

	public Button getCancelButton() {
		return mCancelButton;

	}

	public TextView getTitle() {
		return mTitle;

	}

	public LinearLayout getmClearDate() {
		return mClearDate;
	}
	public String getCurrentTime() {
		String hour = "00" + mHourAdapter.getItemText(mHour.getCurrentItem());
		hour = hour.replace("时", "");
		hour = hour.substring(hour.length() - 2);
		String minute = "00" + mMinuteAdapter.getItemText(mMinute.getCurrentItem());
		minute = minute.substring(minute.length() - 2);
		return hour+":"+minute;
	}

	private class mHourAdapter extends NumericWheelAdapter {
		// int currentItem;
		// int currentValue;

		/**
		 * Constructor
		 */
		public mHourAdapter(Context context, int minValue, int maxValue, int current) {
			super(context, minValue, maxValue);
			// this.currentValue = current;
			setTextSize(20);
			setUnit("时");
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

	void updateDays(WheelView hour, WheelView minute) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.HOUR, hour.getCurrentItem());

		int maxMinute = calendar.getActualMaximum(Calendar.MINUTE);
		minute.setViewAdapter(null);
		mMinuteAdapter = null;
		mMinuteAdapter = new TimeNumericAdapter(mContext, 0, maxMinute, calendar.get(Calendar.MINUTE) - 1);
		minute.setViewAdapter(mMinuteAdapter);
		int curMinute = Math.min(maxMinute, minute.getCurrentItem() + 1);
		minute.setCurrentItem(curMinute - 1, true);
	}

	private class TimeNumericAdapter extends NumericWheelAdapter {

		/**
		 * Constructor
		 */
		public TimeNumericAdapter(Context context, int minValue, int maxValue, int current) {
			super(context, minValue, maxValue);
			// this.currentValue = current;
			setTextSize(20);
			setUnit("分");
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

	

	private class TimeArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		// int currentItem;
		// Index of item to be highlighted
		// int currentValue;

		/**
		 * Constructor
		 */
		public TimeArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			// this.currentValue = current;
			setTextSize(20);
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
