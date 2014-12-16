package com.dfire.retail.app.manage.activity.usermanager;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;

public class AlertDialogCommon {
	Context context;
	android.app.AlertDialog ad;
	private TextView messageView;
	private Button no_onclik,yes_onclik; 
	
	public AlertDialogCommon(Context context) {
	// TODO Auto-generated constructor stub
	this.context=context;
	ad=new android.app.AlertDialog.Builder(context).create();
	ad.show();
	//
	Window window = ad.getWindow();
	window.setContentView(R.layout.alertdialog);
	messageView=(TextView)window.findViewById(R.id.message);
	yes_onclik=(Button)window.findViewById(R.id.yes_onclik);
	no_onclik=(Button)window.findViewById(R.id.no_onclik);
}
public void setMessage(int resId) {
	messageView.setText(resId);
}

public void setMessage(String message)
{
	messageView.setText(message);
}
/**
 * 璁剧疆鎸夐挳
 * @param text
 * @param listener
 */
public void setPositiveButton(String text,final View.OnClickListener listener)
{
	yes_onclik.setText(text);
	yes_onclik.setOnClickListener(listener);

}

/**
 * 璁剧疆鎸夐挳
 * @param text
 * @param listener
 */
public void setNegativeButton(String text,final View.OnClickListener listener)
{
	no_onclik.setText(text);
	no_onclik.setOnClickListener(listener);

}
/**
 * 鍏抽棴瀵硅瘽妗?
 */
public void dismiss() {
	ad.dismiss();
}
/**
 * 杈规澶栫偣鍑讳笉娑堝け
 */
public void setCanceledOnTouchOutside(boolean isFalse){
	ad.setCanceledOnTouchOutside(isFalse);
}
}
