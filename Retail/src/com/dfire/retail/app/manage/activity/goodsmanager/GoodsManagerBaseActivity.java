package com.dfire.retail.app.manage.activity.goodsmanager;


import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.BaseActivity;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.listview.PullToRefreshStickyListView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Class GoodsManagerBaseActivity.
 * 
 * @author albuscrow
 */
public class GoodsManagerBaseActivity extends BaseActivity {
	
	/** The m right. */
	protected TextView mLeft, mRight;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void setWatcher(CountWatcher countWatcher){
		MyEditTextLayout.setWatcher(countWatcher);
		MySpinnerLayout.setWatcher(countWatcher);
		MyCheckBoxLayout.setWatcher(countWatcher);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#setContentView(int)
	 */
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(R.layout.goods_manager_title_layout);
		FrameLayout body = (FrameLayout) findViewById(R.id.body);
		body.requestFocus();
		getLayoutInflater().inflate(layoutResID, body, true);
		mLeft = (TextView) findViewById(R.id.title_left);
		mRight = (TextView) findViewById(R.id.title_right);
	}

	/**
	 * 设置头部标题内容.
	 * 
	 * @param title
	 *            the new title text
	 */
	public void setTitleText(String title) {
		((TextView) findViewById(R.id.title_text)).setText(title);
	}

	/**
	 * 设置头部标题内容.
	 * 
	 * @param resId
	 *            the new title res
	 */
	public void setTitleRes(int resId) {
		((TextView) findViewById(R.id.title_text)).setText(resId);
	}

	/**
	 * Sets the right btn.
	 * 
	 * @param resId
	 *            the res id
	 * @param text
	 *            the text
	 * @return the image button
	 */
	public TextView setRightBtn(int resId, String text) {
		if (mRight == null) {
			mRight = (TextView) findViewById(R.id.title_right);
		}
		mRight.setVisibility(View.VISIBLE);
		mRight.setText(text);
		Drawable drawable = getResources().getDrawable(resId);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		mRight.setCompoundDrawables(drawable, null, null, null);
		
		return mRight;
	}
	
	/**
	 * Hide right.
	 */
	public void hideRight(){
		if (mRight == null) {
			mRight = (TextView) findViewById(R.id.title_right);
		}
		mRight.setVisibility(View.GONE);
	}
	
	/**
	 * Sets the left btn.
	 * 
	 * @param resId
	 *            the res id
	 * @param text
	 *            the text
	 * @return the image button
	 */
	public TextView setLeftBtn(int resId, String text){
		if (mLeft == null) {
			mLeft = (TextView) findViewById(R.id.title_left);
		}
		mLeft.setVisibility(View.VISIBLE);
		mLeft.setText(text);
		Drawable drawable = getResources().getDrawable(resId);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		mLeft.setCompoundDrawables(drawable, null, null, null);
		return mLeft;
	}
	
	/**
	 * Sets the back.
	 */
	public void setBack(){
		setLeftBtn(R.drawable.ico_back, Constants.BACK_TEXT);
		mLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	/**
	 * set the cancel
	 */
	public void setCancel(){
		setLeftBtn(R.drawable.ico_cancel, Constants.CANCEL_TEXT);
		mLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	
	/**
	 * Switch to edit mode.
	 */
	protected void switchToEditMode() {
		setRightBtn(R.drawable.yes,Constants.SAVE_TEXT);
		setLeftBtn(R.drawable.no, Constants.CANCEL_TEXT);
	}
	
	
	protected void addFooter(ListView listView, boolean showDivider){
		if (listView == null) {
			return;
		}
		View inflate = getLayoutInflater().inflate(R.layout.empty, listView, false);
		if (showDivider) {
			inflate.findViewById(R.id.divider).setVisibility(View.VISIBLE);
		}
		listView.addFooterView(inflate, new Object(), false);
	}
	
	
	protected void initPullToRefreshText(PullToRefreshStickyListView prl){
		com.handmark.pulltorefresh.listview.ILoadingLayout loadingLayoutProxy = prl.getLoadingLayoutProxy(true, false);
		loadingLayoutProxy.setPullLabel(Constants.PULL);
		loadingLayoutProxy.setReleaseLabel(Constants.RELEASE);
		loadingLayoutProxy.setRefreshingLabel(Constants.REFRESHING);
		String label = DateUtils.formatDateTime(this, System.currentTimeMillis(), 
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		loadingLayoutProxy.setLastUpdatedLabel(label);
		
		loadingLayoutProxy = prl.getLoadingLayoutProxy(false, true);
		loadingLayoutProxy.setPullLabel(Constants.DPULL);
		loadingLayoutProxy.setReleaseLabel(Constants.DRELEASE);
		loadingLayoutProxy.setRefreshingLabel(Constants.DREFRESHING);
		loadingLayoutProxy.setLastUpdatedLabel(label);
	}
	
	protected void initPullToRefreshText(PullToRefreshListView prl){
		ILoadingLayout loadingLayoutProxy = prl.getLoadingLayoutProxy(true, true);
		loadingLayoutProxy.setPullLabel(Constants.PULL);
//		loadingLayoutProxy.setReleaseLabel(Constants.RELEASE);
//		loadingLayoutProxy.setRefreshingLabel(Constants.REFRESHING);
		String label = DateUtils.formatDateTime(this, System.currentTimeMillis(), 
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		loadingLayoutProxy.setLastUpdatedLabel(label);
//		
//		loadingLayoutProxy = prl.getLoadingLayoutProxy(false, true);
//		loadingLayoutProxy.setPullLabel(Constants.DPULL);
//		loadingLayoutProxy.setReleaseLabel(Constants.DRELEASE);
//		loadingLayoutProxy.setRefreshingLabel(Constants.DREFRESHING);
//		loadingLayoutProxy.setLastUpdatedLabel(label);
	}
	
//	protected void addFooter(PullToRefreshListView listView, boolean showDivider){
//		if (listView == null) {
//			return;
//		}
//		View inflate = getLayoutInflater().inflate(R.layout.empty, listView, false);
//		if (showDivider) {
//			inflate.findViewById(R.id.divider).setVisibility(View.VISIBLE);
//		}
//		listView.getRefreshableView().addFooterView(inflate, null, false);
//	}

	protected void addFooter(ListView listView){
		addFooter(listView, false);
	}

	public void switchToBackMode() {
		setBack();
		hideRight();
	}
	
	public void setSearchClear(final EditText searchCode){
		final View clear = findViewById(R.id.clear);
		clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchCode.setText(Constants.EMPTY_STRING);
			}
		});
		
		searchCode.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && searchCode.getText().length() != 0) {
					clear.setVisibility(View.VISIBLE);
				}else{
					clear.setVisibility(View.GONE);
				}
				
			}
		});
		searchCode.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() != 0 && searchCode.isFocused()) {
					clear.setVisibility(View.VISIBLE);
				}else{
					clear.setVisibility(View.GONE);
				}
			}
		});
	}
	
	static final int noError = 0;
	static final int tooMany = 1;
	static final int tooLess = 2;
	static final int tooBig = 3;

	
	protected int checkFloat(String input){
		int position = input.indexOf('.');
		if (position == -1) {
			if (input.length() > 6) {
				return tooBig;
			}else{
				return noError;
			}
		}else if(position > 6){
			return tooBig;
		}else if(input.length() - position == 1){
			return tooLess;
		}else if(input.length() - position > 3){
			return tooMany;
		}else{
			return noError;
		}
	}
	
	protected boolean checkFloat(String tichengStr, float parseFloat, String label, float f, View view) {
		int result;
		if (parseFloat < 0 ){
			ToastUtil.showShortToast(this,label + Constants.XIAOSHU_NEGTIVE, view);
			return false;
		}else if(parseFloat > f ){
			if (f == 100f) {
				ToastUtil.showShortToast(this,label + Constants.ZHENGSHU_ERROR_TOO_LONG_3, view);
			}else{
				ToastUtil.showShortToast(this,label + Constants.ZHENGSHU_ERROR_TOO_LONG_6, view);
			}
			return false;
		}else if(0 !=(result = checkFloat(tichengStr))) {
			if (result == tooLess) {
				ToastUtil.showShortToast(this,label + Constants.XIAOSHU_ERROR_TOO_SHORT, view);
			}else if(result == tooBig){
				ToastUtil.showShortToast(this,label + Constants.ZHENGSHU_ERROR_TOO_LONG_6, view);
			}else{
				ToastUtil.showShortToast(this,label + Constants.XIAOSHU_ERROR_TOO_LONG, view);
			}
			return false;
		}
		return true;
	}

}
