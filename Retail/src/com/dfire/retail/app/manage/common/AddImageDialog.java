package com.dfire.retail.app.manage.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.dfire.retail.app.manage.R;
/**
 * 上传图片对话框
 * @author Administrator
 *
 */
public class AddImageDialog extends Dialog {

	private Context mContext;
	
	private Button addFromCapture;
	private Button addFromAlbum;
	private Button cancelButton;
	//private Button deleteButton;
	
	private View captureSpareLine;
	
	public AddImageDialog(Context context) {
		super(context,R.style.dialog);
		mContext = context;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_info_add_menu);

		this.setCanceledOnTouchOutside(true);
		Window window = this.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);
		findView();
	}
	

	private void findView(){
		addFromAlbum = (Button)findViewById(R.id.addAblum);
		addFromCapture = (Button)findViewById(R.id.addCapture);
		//deleteButton = (Button)findViewById(R.id.deleteImage);
		cancelButton = (Button)findViewById(R.id.addCancel);
		captureSpareLine = (View)findViewById(R.id.captureSpareLine);
	}
	public View getCaptureSpareLine() {
		return captureSpareLine;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		dismiss();
	}

	public Button getAddFromCapture() {
		return addFromCapture;
	}

	public Button getAddFromAlbum() {
		return addFromAlbum;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

//	public Button getDeleteButton() {
//		return deleteButton;
//	}

}
