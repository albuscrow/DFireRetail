/**
 * 
 */
package com.dfire.retail.app.manage.activity;

import com.dfire.retail.app.manage.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 头部栏
 * 
 * @author qiuch
 * 
 */
public class TitleActivity extends BaseActivity {
	protected ImageButton mBack, mLeft, mRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(R.layout.title_layout);
		FrameLayout body = (FrameLayout) findViewById(R.id.body);
		body.requestFocus();
		getLayoutInflater().inflate(layoutResID, body, true);
	}

	/**
	 * 设置头部标题内容
	 * 
	 * @param 标题字符串
	 */
	public void setTitleText(String title) {
		((TextView) findViewById(R.id.title_text)).setText(title);
	}

	/**
	 * 设置头部标题内容
	 * 
	 * @param 标题资源ID
	 */
	public void setTitleRes(int resId) {
		((TextView) findViewById(R.id.title_text)).setText(resId);
	}

	/**
	 * 显示返回按钮
	 */
	public void showBackbtn() {
		mBack = (ImageButton) findViewById(R.id.title_back);
		mBack.setVisibility(View.VISIBLE);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TitleActivity.this.finish();
			}
		});
	}

	public ImageButton setRightBtn(int ResId) {
		mRight = (ImageButton) findViewById(R.id.title_right);
		mRight.setVisibility(View.VISIBLE);
		mRight.setImageResource(ResId);
		return mRight;
	}

	public ImageButton change2saveMode() {
		if (mBack == null) {
			mBack = (ImageButton) findViewById(R.id.title_back);
		}
		mBack.setVisibility(View.GONE);
		mLeft = (ImageButton) findViewById(R.id.title_left);
		mLeft.setImageResource(R.drawable.cancel);
		mLeft.setVisibility(View.VISIBLE);
		mLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mBack.setVisibility(View.VISIBLE);
				mLeft.setVisibility(View.GONE);
				mRight.setVisibility(View.GONE);
			}
		});

		mRight = (ImageButton) findViewById(R.id.title_right);
		mRight.setVisibility(View.VISIBLE);
		mRight.setImageResource(R.drawable.save);
		return mRight;
	}

	public ImageButton change2saveFinishMode() {
		if (mBack == null) {
			mBack = (ImageButton) findViewById(R.id.title_back);
		}
		mBack.setVisibility(View.GONE);
		mLeft = (ImageButton) findViewById(R.id.title_left);
		mLeft.setImageResource(R.drawable.cancel);
		mLeft.setVisibility(View.VISIBLE);
		mLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TitleActivity.this.finish();
			}
		});

		mRight = (ImageButton) findViewById(R.id.title_right);
		mRight.setVisibility(View.VISIBLE);
		mRight.setImageResource(R.drawable.save);
		return mRight;
	}

}
