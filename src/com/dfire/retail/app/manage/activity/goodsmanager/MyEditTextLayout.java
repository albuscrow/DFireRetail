package com.dfire.retail.app.manage.activity.goodsmanager;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.global.Constants;


public class MyEditTextLayout extends RelativeLayout {
//	private String label;
//	private String hit;
//	private TextView lblName;
//	private ImageView img;
//	private Button btn;
//	private EditText lblVal;
//	private TextView lblHit;
//	
//	
//	


//	/**
//	 * <code>注册事件处理</code>.
//	 */
//	private IItemTextChangeListener itemChange;
	
	private TextView saveFlag;
	private TextView label;
	private EditText input;
	private String original;
	private ImageButton clear;
	
	private static CountWatcher watcher;

	/**
	 * @param context
	 * @param attrs
	 */
	public MyEditTextLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	public MyEditTextLayout(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
	}
	
	public void init(String labelText, String content, final String hint, int inputType){
		saveFlag = (TextView) findViewById(R.id.saveFlag);
		label = (TextView) findViewById(R.id.main);
		if (labelText != null) {
			label.setText(labelText);
		}
		input = (EditText) findViewById(R.id.secend);
		input.setHint(hint);
		if (content != null) {
			input.setText(content);
		}
		if (hint.equals(Constants.NECESSARY)) {
			input.setHintTextColor(getResources().getColor(R.color.necessary));
		}else{
			input.setHintTextColor(getResources().getColor(R.color.not_necessary));
		}
		
		input.addTextChangedListener(new TextWatcher() {
			
			private String before;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				this.before = s.toString();
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (!input.getText().toString().equals(original)) {
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
				if (s.length() == 0) {
					if (hint.equals(Constants.NECESSARY)) {
						input.setHintTextColor(getResources().getColor(R.color.necessary));
					}else{
						input.setHintTextColor(getResources().getColor(R.color.not_necessary));
					}
				}
				if (listener != null) {
					listener.afterTextChange(s.toString(), before);
				}
				
			}
		});
		input.setInputType(inputType);
		clear = (ImageButton) findViewById(R.id.clear);
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				input.setText(Constants.EMPTY_STRING);
			}
		});
		input.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					clear.setVisibility(View.VISIBLE);
				}else{
					clear.setVisibility(View.GONE);
				}
			}
		});
	}
	
	public String getValue(){
		return input.getText().toString();
	}
	

	public void clearSaveFlag(){
		original = input.getText().toString();
		if (saveFlag.getVisibility() == View.VISIBLE) {
			saveFlag.setVisibility(View.GONE);
			watcher.minus();
		}
	}
	
	public void setTextChangeListener(TextChangeListener listener) {
		this.listener = listener;
	}
	
	private TextChangeListener listener;
	public interface TextChangeListener{
		void afterTextChange(String after, String before);
	}
	
	public EditText getInputText() {
		return input;
	}
	
	public void setEditable(boolean canEdit) {
		input.setEnabled(canEdit);
	}
	
	static public void setWatcher(CountWatcher watcher){
		MyEditTextLayout.watcher = watcher;
	}
	public void setValue(String before) {
		input.setText(before);
	}

}