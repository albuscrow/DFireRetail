/**
 * 
 */
package com.dfire.retail.app.manage.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;

/**
 * @author ys
 * 
 */
public class ErrDialog extends Dialog {
	private String mErrmsg;
	private Button mIKnow;// “我知道了”按钮
	private int mType;
	private Context mContext;

	public ErrDialog(Context context, String errmsg) {
		super(context, R.style.dialog);
		mErrmsg = errmsg;
	}

	public ErrDialog(Context context, String errmsg, int type) {
		super(context, R.style.dialog);
		mType = type;
		mErrmsg = errmsg;
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.err_dialog_layout);
		this.setCanceledOnTouchOutside(true);
		mIKnow = (Button) findViewById(R.id.btn_iknow);
		mIKnow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mType == 1) {
					((Activity)mContext).finish();
				}
				ErrDialog.this.dismiss();
			}
		});
		TextView errText = ((TextView) findViewById(R.id.text_err_msg));
		errText.setText(mErrmsg);
	}

	public Button getIKnow() {
		return mIKnow;
	}

}
