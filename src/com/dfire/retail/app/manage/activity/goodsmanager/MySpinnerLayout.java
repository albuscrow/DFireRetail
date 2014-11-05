package com.dfire.retail.app.manage.activity.goodsmanager;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;


public class MySpinnerLayout extends RelativeLayout {
	private TextView saveFlag;
	private TextView label;
	private TextView input;
	private String original;
	private Listener listener;
	
	private static CountWatcher watcher;

	/**
	 * @param context
	 * @param attrs
	 */
	public MySpinnerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	public MySpinnerLayout(Context context) {
		super(context);
	}
	
	public void init(String labelText, String content, final OneColumnSpinner spinner){
		saveFlag = (TextView) findViewById(R.id.saveFlag);
		label = (TextView) findViewById(R.id.main);
		if (labelText != null) {
			label.setText(labelText);
		}
		input = (TextView) findViewById(R.id.secend);
		if (content != null) {
			input.setText(content);
		}
	
		input.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
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
				
			}
		});
		if (spinner != null) {
			input.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					spinner.show();
					spinner.setConfirmListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							spinner.dismiss();
							int pos = spinner.getCurrentData();
							if (listener != null) {
								String name = listener.confirm(pos);
								input.setText(name);
							}
						}
					});
					spinner.setCancelListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							spinner.dismiss();
						}
					});		
				}
			});
		}
	}
	

	public void clearSaveFlag(){
		original = input.getText().toString();
		if (saveFlag.getVisibility() == View.VISIBLE) {
			saveFlag.setVisibility(View.GONE);
			watcher.minus();
		}
	}
	

	
	public interface TextChangeListener{
		void afterTextChange();
	}
	
	public TextView getInputText() {
		return input;
	}
	
	public void setEditable(boolean canEdit) {
		input.setEnabled(canEdit);
	}
	
	static public void setWatcher(CountWatcher watcher){
		MySpinnerLayout.watcher = watcher;
	}
	
	public void setValue(String string, boolean init) {
		input.setText(string);
	}
	
	public void setListener(Listener listener){
		this.listener = listener;
	}
	
	public interface Listener{
		void cancel();
		String confirm(int pos);
	}

}
