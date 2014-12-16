package com.dfire.retail.app.manage.activity.goodsmanager;

import android.R.bool;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;


public class MyCheckBoxLayout extends RelativeLayout {
	private TextView saveFlag;
	private TextView label;
	private CheckBox input;
	private boolean original;
	private Listener listener;
	
	private static CountWatcher watcher;

	/**
	 * @param context
	 * @param attrs
	 */
	public MyCheckBoxLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	public MyCheckBoxLayout(Context context) {
		super(context);
	}
	
	public void init(String labelText, boolean checked){
		saveFlag = (TextView) findViewById(R.id.saveFlag);
		label = (TextView) findViewById(R.id.check_title);
		if (labelText != null) {
			label.setText(labelText);
		}
		input = (CheckBox) findViewById(R.id.checkbox);
		input.setChecked(checked);
	
		input.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (original != isChecked) {
					if (saveFlag.getVisibility() == View.GONE) {
						watcher.add();
						saveFlag.setVisibility(View.VISIBLE);
					}
				}else{
					if (saveFlag.getVisibility() == View.VISIBLE) {
						watcher.minus();
						saveFlag.setVisibility(View.GONE);
					}
				}
				if (listener != null) {
					listener.checkedChange(isChecked);
				}
			}
		});
		
	}
	

	public void clearSaveFlag(){
		original = input.isChecked();
		if (saveFlag.getVisibility() == View.VISIBLE) {
			saveFlag.setVisibility(View.GONE);
			watcher.minus();
		}
	}
	
	
	public interface TextChangeListener{
		void afterTextChange();
	}
	
	public CheckBox getCheckBox() {
		return input;
	}
	
	public void setEditable(boolean canEdit) {
		input.setEnabled(canEdit);
	}
	
	static public void setWatcher(CountWatcher watcher){
		MyCheckBoxLayout.watcher = watcher;
	}
	
//	public void setValue(String string, boolean init) {
//		input.setText(string);
//	}
//	
	public void setListener(Listener listener){
		this.listener = listener;
	}
	
	public interface Listener{
		void checkedChange(boolean isChecked);
	}

	public boolean isChecked() {
		return input.isChecked();
	}
	
	public void setChecked(boolean b){
		input.setChecked(b);
	}

}
