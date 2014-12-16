package com.dfire.retail.app.manage.activity.goodsmanager;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.wheel.widget.WheelView;
import com.dfire.retail.app.manage.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * 状态 字典表
 * @author ys
 */
@SuppressLint("NewApi")
public class OneColumnSpinner extends Dialog {

	private Context mContext;
	private CardTypeAdapter mAdapter;
	private WheelView mWheelView;
	private Button mConfirmButton, mCancelButton;
	private TextView mTitle;
	private List data;
	private String title;
	private int currentValue = 0;
	private TextView tiggerView;
	private String clearString;

	/**
	 * @return the dicVos
	 */
	public List<Object> getDicVos() {
		return data;
	}

	public void setConfirmListener(android.view.View.OnClickListener onClickListener){
		mConfirmButton.setOnClickListener(onClickListener);
	}
	
	public void setCancelListener(android.view.View.OnClickListener onClickListener){
		mCancelButton.setOnClickListener(onClickListener);
	}

	/**
	 * @param dicVos the dicVos to set
	 */
	public void setData(List dicVos) {
		this.data = dicVos;
	}
	
	
	public OneColumnSpinner(Context context) {
		this(context, null);
	}
	public OneColumnSpinner(Context context, TextView tiggerView) {
		this(context, tiggerView, null);
	}

	public OneColumnSpinner(Context context, TextView tiggerView, String clearString) {
		super(context, R.style.dialog);
		this.mContext = context;
		this.tiggerView = tiggerView;
		this.clearString = clearString;	
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_type_dialog);
		
		mTitle = (TextView) findViewById(R.id.card_type_title);
		mTitle.setText(title);
		
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
		mAdapter = new CardTypeAdapter(mContext,data);
		mWheelView.setViewAdapter(mAdapter);
		mWheelView.setCurrentItem(2);
		if (tiggerView != null) {
			View clearLayout = findViewById(R.id.clear_date);
			clearLayout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					tiggerView.setText(Constants.PLEASE_CHOOSE);
					dismiss();
				}
			});
			if (clearString != null) {
				((TextView)findViewById(R.id.clear)).setText(clearString);
			}
			clearLayout.setVisibility(View.VISIBLE);
		}else{
			LayoutParams lp = (LayoutParams) mTitle.getLayoutParams();
			lp.width = LayoutParams.MATCH_PARENT;
			mTitle.setLayoutParams(lp);
			mTitle.requestFocus();
			mTitle.setGravity(Gravity.CENTER);
		}
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
	public void setValue(String val) {
		if (val!=null&&!val.equals("")) {
			if (data.size() > 0) {
				int i;
				for (i = 0; i < data.size(); i++) {
					if (data.get(i).toString().equals(val)) {
						currentValue = i;
						if (mWheelView != null) {
							mWheelView.setCurrentItem(i);
						}
						break;
					}
				}
				if (i == data.size()) {
					mWheelView.setCurrentItem(0);
				}
			}
		}else{
			if (mWheelView != null) {
				mWheelView.setCurrentItem(0);
			}
		}
	}
	public int getCurrentData() {
		return mWheelView.getCurrentItem();
//		return mAdapter.getItemText(mWheelView.getCurrentItem()).toString();

	}
	public TextView getmTitle() {
		return mTitle;
	}

	public void setmTitle(TextView mTitle) {
		this.mTitle = mTitle;
	}

	private class CardTypeAdapter extends AbstractWheelTextAdapter {
		private List<Object> dicVos;
		protected CardTypeAdapter(Context context,List<Object> dicVos) {
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
			return dicVos.get(index).toString();
		}
	}
	
	public void setTitleText(String title) {
		if (mTitle != null) {
			mTitle.setText(title);
		}
		this.title = title;
	}

}
