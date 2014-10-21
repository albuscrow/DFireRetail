package com.dfire.retail.app.manage.activity.goodsmanager;

import org.w3c.dom.Text;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.BaseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * The Class GoodsManagerBaseActivity.
 * 
 * @author albuscrow
 */
public class GoodsManagerBaseActivity extends BaseActivity {
	
	/** The m right. */
	protected ImageButton mLeft, mRight;

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
		mLeft = (ImageButton) findViewById(R.id.title_left);
		mRight = (ImageButton) findViewById(R.id.title_right);
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
	public ImageButton setRightBtn(int resId, String text) {
		if (mRight == null) {
			mRight = (ImageButton) findViewById(R.id.title_right);
		}
		mRight.setVisibility(View.VISIBLE);
		mRight.setImageResource(resId);
		
		return mRight;
	}
	
	/**
	 * Hide right.
	 */
	public void hideRight(){
		if (mRight == null) {
			mRight = (ImageButton) findViewById(R.id.title_right);
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
	public ImageButton setLeftBtn(int resId, String text){
		if (mLeft == null) {
			mLeft = (ImageButton) findViewById(R.id.title_left);
		}
		mLeft.setVisibility(View.VISIBLE);
		mLeft.setImageResource(resId);
		return mLeft;
	}
	
	/**
	 * Sets the back.
	 */
	public void setBack(){
		if (mLeft == null) {
			mLeft = (ImageButton) findViewById(R.id.title_left);
		}	
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
		setRightBtn(R.drawable.save, "保存");
		setLeftBtn(R.drawable.cancel, "取消");
	}


}