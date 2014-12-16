package com.dfire.retail.app.common.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;

public class ListAddFooterItem{
	private Context mContext;
	
	public ListAddFooterItem(Context mContext,ListView listView) {
		this.mContext = mContext;
		addFooter(listView, false);
	}
	public void addFooter(ListView listView, boolean showDivider){
		if (listView == null) {
			return;
		}
		View inflate = LayoutInflater.from(mContext).inflate(R.layout.empty, listView, false);
		listView.addFooterView(inflate, null, false);
	}
}
