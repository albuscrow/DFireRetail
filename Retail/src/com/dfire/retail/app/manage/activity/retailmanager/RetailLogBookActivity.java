package com.dfire.retail.app.manage.activity.retailmanager;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.AttendItemAdapter;
import com.dfire.retail.app.manage.adapter.AttendanceItem;
import com.dfire.retail.app.manage.util.Utility;

/**
 * 交接班记录
 * @author Administrator
 *
 */
public class RetailLogBookActivity extends TitleActivity{
	
	ArrayList<AttendanceItem> mList;
	private ListView mListView;
	private AttendItemAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.logbook_main);
		setTitle("交接班记录");
		showBackbtn();		
		mListView = (ListView)findViewById(R.id.attendance_list);
		
		mList = new ArrayList<AttendanceItem>();
		mList.add(new AttendanceItem(getResources().getDrawable(R.drawable.man), 
				"店长", "王宇", "工号：003", "2014-08-29", "2014-08-29","08:00", "18:00"
				,"开始时间","结束时间"));
		mList.add(new AttendanceItem(getResources().getDrawable(R.drawable.woman), 
				"收银员", "王宇", "工号：003", "2014-08-29", "2014-08-29","08:00", "18:00","开始时间","结束时间"));
		mList.add(new AttendanceItem(getResources().getDrawable(R.drawable.man), 
				"收银员", "王宇", "工号：003", "2014-08-29", "2014-08-29","08:00", "18:00","开始时间","结束时间"));
		mList.add(new AttendanceItem(getResources().getDrawable(R.drawable.man), 
				"收银员", "王宇", "工号：003", "2014-08-29", "2014-08-29","08:00", "18:00","开始时间","结束时间"));
		
		adapter = new AttendItemAdapter(RetailLogBookActivity.this, mList);
		mListView.setAdapter(adapter);
		
		Utility.setListViewHeightBasedOnChildren(mListView);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RetailLogBookActivity.this,LogBookDetailActivity.class);
				startActivity(intent);
				
			}
		});
		
	}
	
	public void ClickListener(View v) {
		switch (Integer.parseInt(String.valueOf(v.getTag()))) {
			case 1:
				startActivity(new Intent(RetailLogBookActivity.this, SelectDateActivity.class));
				break;	
		}
	}
}
