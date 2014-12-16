package com.dfire.retail.app.manage.activity.goodsmanager;


import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.item.listener.IFootItemClickListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.global.Constants;


public class MyEditTextLayout extends RelativeLayout {

	
	private int width;
	private TextView saveFlag;
	private TextView label;
	private EditText input;
	private String original;
	private ImageButton clear;
	
	private boolean isPrice;
	
	/**
	 * @return the isPrice
	 */
	public boolean isPrice() {
		return isPrice;
	}
	/**
	 * @param isPrice the isPrice to set
	 */
	public void setPrice(boolean isPrice) {
		this.isPrice = isPrice;
	}
	
	private boolean isDigitsAndNum;

	/**
	 * @return the isDigitsAndNum
	 */
	public boolean isDigitsAndNum() {
		return isDigitsAndNum;
	}
	/**
	 * @param isDigitsAndNum the isDigitsAndNum to set
	 */
	public void setDigitsAndNum(boolean isDigitsAndNum) {
		this.isDigitsAndNum = isDigitsAndNum;
		if (input == null) {
			return;
		}
		if (isDigitsAndNum) {

			input.setKeyListener(new NumberKeyListener() {

				private char[] chars;

				{
					String charsStr = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
					chars = new char[62];
					for (int i = 0; i < charsStr.length(); ++i) {
						chars[i] = charsStr.charAt(i);
					}
				}
				@Override
				public int getInputType() {
					return InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT;
				}

				@Override
				protected char[] getAcceptedChars() {
					return chars;
				}
			});
		}
		
	}

	private static CountWatcher watcher;
	private int height;
	/**
	 * @param context
	 * @param attrs
	 */
	public MyEditTextLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	public MyEditTextLayout(Context context) {
		super(context);
	}
	
	public void init(String labelText, String content, final String hint, int inputType){
		init(labelText, content, hint, inputType, Constants.INVALID_INT);
	}
	
	private boolean isPercent;
	/**
	 * @return the isPercent
	 */
	public boolean isPercent() {
		return isPercent;
	}

	/**
	 * @param isPercent the isPercent to set
	 */
	public void setPercent(boolean isPercent) {
		this.isPercent = isPercent;
	}
	
	private boolean needTowLine;

	/**
	 * @return the needTowLine
	 */
	public boolean isNeedTowLine() {
		return needTowLine;
	}

	/**
	 * @param needTowLine the needTowLine to set
	 */
	public void setNeedTowLine(boolean needTowLine) {
		this.needTowLine = needTowLine;
		input.setMaxLines(3);
		
		android.widget.RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) input.getLayoutParams();
		layoutParams.addRule(RelativeLayout.BELOW, R.id.main);
		layoutParams.addRule(RelativeLayout.RIGHT_OF, 0);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 0);
		layoutParams.width = LayoutParams.MATCH_PARENT;
		//						height = layoutParams.height;
		input.setLayoutParams(layoutParams);
		input.setPadding(0, 0, 0, input.getPaddingBottom());
		input.setGravity(Gravity.LEFT);

		DisplayMetrics dm = new DisplayMetrics();
		((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
		input.setPadding(0, (int)(10 * dm.density), 0, (int)(10 * dm.density));
		input.requestLayout();

		RelativeLayout relativeLayout = (RelativeLayout)input.getParent();
		android.widget.LinearLayout.LayoutParams plp = (android.widget.LinearLayout.LayoutParams) relativeLayout.getLayoutParams();
		height = plp.height;
		plp.height = layoutParams.WRAP_CONTENT;
		relativeLayout.setLayoutParams(plp);
		relativeLayout.requestLayout();

		android.widget.RelativeLayout.LayoutParams layoutParams2 = (android.widget.RelativeLayout.LayoutParams) label.getLayoutParams();
		layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL, 0);
		label.setLayoutParams(layoutParams2);
		label.setPadding(0, label.getPaddingTop(), 0, 0);
		label.requestLayout();
	}	
	public void init(String labelText, String content, final String hint, int inputType, int maxLength){
		clear = (ImageButton) findViewById(R.id.clear);
		saveFlag = (TextView) findViewById(R.id.saveFlag);
		label = (TextView) findViewById(R.id.main);
		if (labelText != null) {
			label.setText(labelText);
		}
		input = (EditText) findViewById(R.id.second);
		input.setHint(hint);
		if (maxLength != Constants.INVALID_INT) {
			input.setFilters(new InputFilter[] {new LengthFilter(maxLength)});
		}
		if (content != null) {
			input.setText(content);
		}
		if (hint.equals(Constants.NECESSARY)) {
			input.setHintTextColor(getResources().getColor(R.color.necessary));
		}else{
			input.setHintTextColor(getResources().getColor(R.color.not_necessary));
		}
		

		this.post(new Runnable() {
			

			@Override
			public void run() {
				DisplayMetrics dm = new DisplayMetrics();
				((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
				width = (int) (input.getWidth() - 34f / 2f * dm.density);
				height = MyEditTextLayout.this.getHeight();
			}
			
		});
		
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
				
				if ((input.getInputType() & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_NUMBER) {
					if (s.toString().equals(".")) {
						input.setText("0.");
						input.setSelection(2);
					}
				}
				
				
				if (input.isFocused() && input.getText().length() != 0) {
					android.widget.RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) input.getLayoutParams();
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
					input.setLayoutParams(layoutParams);
					input.requestLayout();
					clear.setVisibility(View.VISIBLE);
				}else{
					android.widget.RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) input.getLayoutParams();
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					input.setLayoutParams(layoutParams);
					input.requestLayout();
					clear.setVisibility(View.GONE);
				}
				
				
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
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				input.setText(Constants.EMPTY_STRING);
			}
		});
		input.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && input.getText().length() != 0) {
					android.widget.RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) input.getLayoutParams();
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
					input.setLayoutParams(layoutParams);
					input.requestLayout();
					clear.setVisibility(View.VISIBLE);
				}else{
					android.widget.RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) input.getLayoutParams();
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					input.setLayoutParams(layoutParams);
					input.requestLayout();
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
//	public void initWidth() {
//		width = input.getWidth();
//	}

}
