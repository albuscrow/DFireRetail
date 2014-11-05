package com.dfire.retail.app.common.item.listener;

import android.view.View;

/**
 * 
 * 监听Item的数据是否修改，如果有修改，未保存，更新UI界面标题界面。
 *
 */
public interface IItemIsChangeListener {
		
	void onItemIsChangeListener(View v);
}
