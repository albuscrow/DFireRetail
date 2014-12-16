package com.dfire.retail.app.manage.util;

import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.global.Constants;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 *
 * @author jixing
 * @create 2011-5-13
 */
public class ToastUtil {
	
	
    public static void showLongToast(final Context context, final String text) {
    	showLongToast(context, text, false);
    }
	
    public static void showLongToast(final Context context, final String text, final boolean isToast) {
        
        ((Activity)context).runOnUiThread(new Runnable() {

        	@Override
        	public void run() {
        		if (isToast) {
        			Toast.makeText(context, text, Toast.LENGTH_LONG).show();
				}else{
					new ErrDialog(context, text).show();
				}
        	}
        });    }
    
    
    
    public static void showShortToast(final Context context, final String text, View view) {
    	showShortToast(context, text, false, view);
    }
    
    public static void showShortToast(final Context context, final String text) {
    	showShortToast(context, text, false, null);
    }
    
    public static void showShortToast(final Context context, final String text, final boolean isToast, final View view) {
    	((Activity)context).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (isToast) {
					Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
				}else{
					new ErrDialog(context, text).show();
				}
				if (view != null) {
					view.requestFocus();
				}
			}
		});
    }

	public static void showUnknowError(Context context) {
		showShortToast(context, Constants.UNKNOW_ERROR);
	}
}
