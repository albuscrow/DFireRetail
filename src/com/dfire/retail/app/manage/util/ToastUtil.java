package com.dfire.retail.app.manage.util;

import com.dfire.retail.app.manage.activity.setting.ParamSettingActivity;
import com.dfire.retail.app.manage.global.Constants;

import android.content.Context;
import android.widget.Toast;

/**
 *
 * @author jixing
 * @create 2011-5-13
 */
public class ToastUtil
{
    public static void showLongToast(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
    
    public static void showShortToast(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

	public static void showUnknowError(Context context) {
		showShortToast(context, Constants.UNKNOW_ERROR);
	}
}
