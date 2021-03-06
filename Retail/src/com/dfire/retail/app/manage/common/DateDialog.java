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
import android.widget.TextView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.wheel.widget.OnWheelChangedListener;
import com.dfire.retail.app.manage.wheel.widget.WheelView;
import com.dfire.retail.app.manage.wheel.widget.adapters.ArrayWheelAdapter;
import com.dfire.retail.app.manage.wheel.widget.adapters.NumericWheelAdapter;

/**
 * @author qiuch
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class DateDialog extends Dialog {

	private Context mContext;
	private WheelView mYear, mMounth, mDay;
	private YearNumericAdapter mYearAdapter;
	private DateArrayAdapter mMonthAdapter;
	private DateNumericAdapter mDayAdapter;
	private Button mConfirmButton, mCancelButton;
	private TextView mTitle;

	public DateDialog(Context context) {
		super(context, R.style.dialog);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_dialog);

		mConfirmButton = (Button) findViewById(R.id.card_type_confirm);
		mCancelButton = (Button) findViewById(R.id.card_type_cancel);
		mTitle = (TextView) findViewById(R.id.date_dialog_title);
		this.setCanceledOnTouchOutside(true);
		Window window = this.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		Point p = new Point();
		window.getWindowManager().getDefaultDisplay().getSize(p);
		params.width = p.x;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);

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

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(mYear, mMounth, mDay);
			}
		};

		Calendar calendar = Calendar.getInstance();
		// month
		int curMonth = calendar.get(Calendar.MONTH);
		String months[] = new String[] { "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" };
		mMonthAdapter = new DateArrayAdapter(mContext, months, curMonth);
		mMounth.setViewAdapter(mMonthAdapter);
		mMounth.setCurrentItem(curMonth);
		mMounth.addChangingListener(listener);

		// year
		int curYear = calendar.get(Calendar.YEAR);
		mYearAdapter = new YearNumericAdapter(mContext, 1940, 2020, 1990);
		mYear.setViewAdapter(mYearAdapter);
		mYear.setCurrentItem(curYear);
		mYear.addChangingListener(listener);

		// day
		updateDays(mYear, mMounth, mDay);
		mYear.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
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

	public String getCurrentData() {
		return mYearAdapter.getItemText(mYear.getCurrentItem()) + "-" + mMonthAdapter.getItemText(mMounth.getCurrentItem()) + "-" + mDayAdapter.getItemText(mDay.getCurrentItem());
	}
	
	public String getPushDate(){
		String year = mYearAdapter.getItemText(mYear.getCurrentItem()).toString();
		String mounth = (mMonthAdapter.getItemText(mMounth.getCurrentItem())).toString().replaceAll("月","");
		String day = mDayAdapter.getItemText(mDay.getCurrentItem()).toString();
		if (Integer.parseInt(mounth)<10) {
			mounth = "0"+mounth;
		}
		if (Integer.parseInt(day)<10) {
			day = "0"+day;
		}
		return year+"-"+mounth+"-"+day;
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
