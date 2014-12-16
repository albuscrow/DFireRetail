package com.dfire.retail.app.manage.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 项目名称：retail-cashdesk  
 * 类名称：TextLimitUtil  
 * 类描述：   文本框输入字数限制类
 * 创建时间：2014年9月27日 下午5:47:05  
 * @author chengzi  
 * @version 1.0
 */
public class TextLimitUtil implements TextWatcher {

	private CharSequence temp;
    private int selectionStart ;
    private int selectionEnd ;
    private EditText editText;
    private TextView saveTag;
    private Context context;
    private int num;
    private ImageButton clear;
    TextLimitUtil(Context context,EditText editText,TextView saveTag,ImageButton clear,int num) {
    	this.editText = editText;
    	this.context = context;
    	this.num = num;
    	this.saveTag=saveTag;
    	this.clear=clear;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int arg1, int arg2,int arg3) {
    	temp = s;
    }

    @Override
    public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        selectionStart = editText.getSelectionStart();
        selectionEnd = editText.getSelectionEnd();
        if(temp.length()>0){
        	saveTag.setVisibility(View.VISIBLE);
        }else{
        	saveTag.setVisibility(View.GONE);
        }
        if(temp.length()>0){
        	clear.setVisibility(View.VISIBLE);
        }else{
        	clear.setVisibility(View.GONE);
        }
        if (temp.length() > num) {
            Toast.makeText(context, "最多只能输入" + num + "个字符", Toast.LENGTH_SHORT).show();
            s.delete(selectionStart-1, selectionEnd);
            int tempSelection = selectionStart;
            editText.setText(s);
            editText.setSelection(tempSelection);
        }
    }


}
