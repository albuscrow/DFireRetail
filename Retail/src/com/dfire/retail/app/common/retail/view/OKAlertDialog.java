package com.dfire.retail.app.common.retail.view;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;

public class OKAlertDialog {
	Context context;
	android.app.AlertDialog ad;
	private TextView messageView;
	private Button yes_onclik; 
	public OKAlertDialog(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		ad=new android.app.AlertDialog.Builder(context).create();
		ad.show();
		//关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = ad.getWindow();
		window.setContentView(R.layout.ok_alertdialog);
		messageView=(TextView)window.findViewById(R.id.message);
		yes_onclik=(Button)window.findViewById(R.id.yes_onclik);
	}
	public void setMessage(int resId) {
		messageView.setText(resId);
	}
 
	public void setMessage(String message)
	{
		messageView.setText(message);
	}
	/**
	 * 设置按钮
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text,final View.OnClickListener listener)
	{
		yes_onclik.setText(text);
		yes_onclik.setOnClickListener(listener);

	}
 
//	/**
//	 * 设置按钮
//	 * @param text
//	 * @param listener
//	 */
//	public void setNegativeButton(String text,final View.OnClickListener listener)
//	{
//		no_onclik.setText(text);
//		no_onclik.setOnClickListener(listener);
// 
//	}
	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		ad.dismiss();
	}
}
