package com.dfire.retail.app.manage.activity.retailmanager;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.DateItem;
import com.dfire.retail.app.manage.adapter.SelectDateAdapter;
import com.dfire.retail.app.manage.util.Utility;

public class SelectDateActivity extends TitleActivity{
	
	ArrayList<DateItem> mList;
	private ListView mListView;
	private SelectDateAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.select_date);
		
		setTitle("选择时间");
		showBackbtn();
		
		mListView = (ListView)findViewById(R.id.selectdate_list);
		
		mList = new ArrayList<DateItem>();
		
		mList.add(new DateItem("今天", false));
		mList.add(new DateItem("昨天", true));
		mList.add(new DateItem("最近三天", false));
		mList.add(new DateItem("本周", false));
		mList.add(new DateItem("本月", false));
		
		adapter = new SelectDateAdapter(SelectDateActivity.this, mList);
		mListView.setAdapter(adapter);
		
		Utility.setListViewHeightBasedOnChildren(mListView);
	}
}
