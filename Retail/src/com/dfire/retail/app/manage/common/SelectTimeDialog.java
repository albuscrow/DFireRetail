package com.dfire.retail.app.manage.common;

import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
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

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class SelectTimeDialog extends Dialog{

	private Context mContext;
	private WheelView  mHour, mMinute;
	
	private HourArrayAdapter mHourAdapter;
	private MinuteArrayAdapter mMinuteAdapter;
	private Button mConfirmButton, mCancelButton;
	private TextView mTitle;
	private LinearLayout mClearDate;
	private boolean isClear;
	public SelectTimeDialog(Context context) {
		super(context);
		mContext = context;
	}
	public SelectTimeDialog(Context context, String type) {
		super(context, R.style.dialog);
		mContext = context;
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
		mClearDate = (LinearLayout) findViewById(R.id.clear_date);
		if (isClear) {
			mClearDate.setVisibility(View.VISIBLE);
		}
		this.setCanceledOnTouchOutside(true);
		Window window = this.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		Point p = new Point();
		window.getWindowManager().getDefaultDisplay().getSize(p);
		params.width = p.x;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);
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
		// 显示默认时间
		Calendar calendar = Calendar.getInstance();
		int curHour = calendar.get(Calendar.HOUR_OF_DAY);
		int curMinute = calendar.get(Calendar.MINUTE);
		String hour[] = new String[24];
		for (int i = 0; i < 24; i++) {
			 hour[i]=i+"时";
		}
		mHourAdapter = new HourArrayAdapter(mContext, hour, curHour);
		mHour.setViewAdapter(mHourAdapter);
		mHour.setCurrentItem(curHour);
		String minute[] = new String[60];
		for (int i = 0; i < 60; i++) {
			minute[i]=i+"";
		}
		mMinuteAdapter = new MinuteArrayAdapter(mContext, minute, curMinute);
		mMinute.setViewAdapter(mMinuteAdapter);
		mMinute.setCurrentItem(curMinute);
		// day
		updateDays(mHour, mMinute);
		mMinute.addChangingListener(listener);
		mHour.addChangingListener(listener);
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
	void updateDays(WheelView hour, WheelView minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, hour.getCurrentItem());
		calendar.set(Calendar.MINUTE, minute.getCurrentItem());
	}

	private class MinuteArrayAdapter extends ArrayWheelAdapter<String> {
		public MinuteArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			setTextSize(20);
		}
		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
		}
		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			return super.getItem(index, cachedView, parent);
		}
	}
	private class HourArrayAdapter extends ArrayWheelAdapter<String> {
		public HourArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			setTextSize(20);
		}
		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
		}
		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			return super.getItem(index, cachedView, parent);
		}
	}
}
