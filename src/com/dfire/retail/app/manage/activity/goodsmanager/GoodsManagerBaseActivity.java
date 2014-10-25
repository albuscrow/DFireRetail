package com.dfire.retail.app.manage.activity.goodsmanager;


import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.BaseActivity;
import com.dfire.retail.app.manage.global.Constants;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
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
	 * Switch to edit mode.
	 */
	protected void switchToEditMode() {
		setRightBtn(R.drawable.yes, Constants.SAVE_TEXT);
		setLeftBtn(R.drawable.no, Constants.CANCEL_TEXT);
	}


}
