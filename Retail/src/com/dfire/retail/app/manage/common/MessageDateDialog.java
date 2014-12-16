package com.dfire.retail.app.manage.common;

import java.util.Calendar;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.wheel.widget.OnWheelChangedListener;
import com.dfire.retail.app.manage.wheel.widget.WheelView;
import com.dfire.retail.app.manage.wheel.widget.adapters.ArrayWheelAdapter;
import com.dfire.retail.app.manage.wheel.widget.adapters.NumericWheelAdapter;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class MessageDateDialog extends Dialog{
	private Context mContext;
	private WheelView mYear, mMounth, mDay;
	private YearNumericAdapter mYearAdapter;
	private DateArrayAdapter mMonthAdapter;
	private DateNumericAdapter mDayAdapter;
	private Button mConfirmButton, mCancelButton;
	private TextView mTitle;
/*	private LinearLayout mClearDate;*/
	private String mType = "";// 日期选择框类型，生日日期选择，默认为1985年1月1日;一般日期选择，默认为系统日期
	private boolean isClear;
	public MessageDateDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public MessageDateDialog(Context context, String type) {
		super(context, R.style.dialog);
		mContext = context;
		mType = type;
	}
	
	public MessageDateDialog(Context context,boolean isClear){
		super(context,R.style.dialog);
		mContext = context;
		this.isClear = isClear;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_date_dialog);

		mConfirmButton = (Button) findViewById(R.id.card_type_confirm);
		mCancelButton = (Button) findViewById(R.id.card_type_cancel);
		mTitle = (TextView) findViewById(R.id.date_dialog_title);
		
		this.setCanceledOnTouchOutside(true);

		mYear = (WheelView) findViewById(R.id.year_wheel);
		mMounth = (WheelView) findViewById(R.id.month_wheel);
		mDay = (WheelView) findViewById(R.id.day_wheel);

		mYear.setVisibleItems(7); // Number of items
		mYear.setWheelBackground(android.R.color.transparent);
		mYear.setWheelForeground(android.R.color.transparent);
		mYear.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		mYear.setCyclic(true);

		mMounth.setVisibleItems(7); // Number of items
		mMounth.setWheelBackground(android.R.color.transparent);
		mMounth.setWheelForeground(android.R.color.transparent);
		mMounth.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		mMounth.setCyclic(true);

		mDay.setVisibleItems(7); // Number of items
		mDay.setWheelBackground(android.R.color.transparent);
		mDay.setWheelForeground(android.R.color.transparent);
		mDay.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		mDay.setCyclic(true);
		Window window = this.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		Point p = new Point();
		window.getWindowManager().getDefaultDisplay().getSize(p);
		params.width = p.x;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(mYear, mMounth, mDay);
			}
		};
		// 显示默认日期

		Calendar calendar = Calendar.getInstance();
		int curDay = calendar.get(Calendar.DAY_OF_MONTH);
		int curMonth = calendar.get(Calendar.MONTH);
		int curYear = calendar.get(Calendar.YEAR);

		if (mType.equals("birth")) {
			curYear = 1985;
			curMonth = 0;
			curDay = 1;
		}
		// month
		String months[] = new String[] { "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" };
		mMonthAdapter = new DateArrayAdapter(mContext, months, curMonth);
		mMounth.setViewAdapter(mMonthAdapter);
		mMounth.setCurrentItem(curMonth);

		// year
		mYearAdapter = new YearNumericAdapter(mContext, 1960, 2020, 1990);
		mYear.setViewAdapter(mYearAdapter);
		mYear.setCurrentItem(curYear - 1960);

		// day
		updateDays(mYear, mMounth, mDay);
		mDay.setCurrentItem(curDay - 1);
		mMounth.addChangingListener(listener);
		mYear.addChangingListener(listener);
	}

	public void updateDays(String time) {
		if (time!=null&&!time.equals("请选择")) { 
			int year = Integer.parseInt(time.split("-")[0]);
			int month = Integer.parseInt(time.split("-")[1]);
			int day = Integer.parseInt(time.split("-")[2]);
			if (year >= 1960) {
				Log.i("year", year + "");
				mYear.setCurrentItem(year - 1960);
			} else {
				mYear.setCurrentItem(0);
			}
			mMounth.setCurrentItem(month - 1);
			mDay.setCurrentItem(day - 1);
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

	/*public LinearLayout getmClearDate() {
		return mClearDate;
	}*/

	public String getCurrentData() {
		String month = "00" + mMonthAdapter.getItemText(mMounth.getCurrentItem());
		month = month.replace("月", "");
		month = month.substring(month.length() - 2);
		String day = "00" + mDayAdapter.getItemText(mDay.getCurrentItem());
		day = day.substring(day.length() - 2);
		return mYearAdapter.getItemText(mYear.getCurrentItem()) + "-" + month + "-" + day;
	}

	void updateDays(WheelView year, WheelView month, WheelView day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day.setViewAdapter(null);
		mDayAdapter = null;
		mDayAdapter = new DateNumericAdapter(mContext, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1);
		day.setViewAdapter(mDayAdapter);
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
	}

	private class DateNumericAdapter extends NumericWheelAdapter {

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
			super(context, minValue, maxValue);
			// this.currentValue = current;
			setTextSize(20);
			setUnit("日");
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

	private class YearNumericAdapter extends NumericWheelAdapter {
		// int currentItem;
		// int currentValue;

		/**
		 * Constructor
		 */
		public YearNumericAdapter(Context context, int minValue, int maxValue, int current) {
			super(context, minValue, maxValue);
			// this.currentValue = current;
			setTextSize(20);
			setUnit("年");
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

	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		// int currentItem;
		// Index of item to be highlighted
		// int currentValue;

		/**
		 * Constructor
		 */
		public DateArrayAdapter(Context context, String[] items, int current) {
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
