/**
 * 
 */
package com.dfire.retail.app.manage.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import com.dfire.retail.app.manage.R;

/**
 * @author ys
 * 
 */
public class LoadingDialog extends Dialog {
	private Context mContext;
	private boolean mCancelable;

	public LoadingDialog(Context context) {
		super(context, R.style.dialog);
		mContext = context;
	}

	public LoadingDialog(Context context, boolean cancelable) {
		super(context, R.style.dialog);
		mContext = context;
		mCancelable = cancelable;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_dialog_layout);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(mCancelable);
		findViewById(R.id.loading_image).startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.progress_anim));
	}

}
