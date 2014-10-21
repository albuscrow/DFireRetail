package com.dfire.retail.app.manage.activity.retailmanager;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.CommonItem;
import com.dfire.retail.app.manage.adapter.LogBookDetailAdapter;
import com.dfire.retail.app.manage.util.Utility;

public class LogBookDetailActivity extends TitleActivity{
	
	ArrayList<CommonItem> mList;
	ArrayList<CommonItem> mPayDetailList;
	private ListView mListView,mPayDetailListView;
	private LogBookDetailAdapter adapter,adapter2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.logbook_detail);
		
		setTitle("交接班详情");
		showBackbtn();
		
		mListView = (ListView)findViewById(R.id.logbookdetail_list);
		mPayDetailListView = (ListView)findViewById(R.id.paydetail_list);
		mList = new ArrayList<CommonItem>();
		mPayDetailList = new ArrayList<CommonItem>();
		initDetailList();
		
		adapter = new LogBookDetailAdapter(LogBookDetailActivity.this, mList);
		mListView.setAdapter(adapter);
		
		adapter2 = new LogBookDetailAdapter(LogBookDetailActivity.this, mPayDetailList);
		mPayDetailListView.setAdapter(adapter2);
		
		Utility.setListViewHeightBasedOnChildren(mListView);
		Utility.setListViewHeightBasedOnChildren(mPayDetailListView);
	}
	
	/**
	 * 添加详细信息记录
	 */
	private void initDetailList(){
		mList.add(new CommonItem("员工姓名","", "李晓明"));
		mList.add(new CommonItem("员工工号","", "002"));
		mList.add(new CommonItem("员工角色","", "收银员"));
		mList.add(new CommonItem("所属门店","", "杭州西湖区体育场路店"));
		mList.add(new CommonItem("登陆时间","2014-08-21", "08:00"));
		mList.add(new CommonItem("结束时间","2014-08-21", "16:00"));
		mList.add(new CommonItem("收银单数","", "152"));
		mList.add(new CommonItem("商品数量","", "1008"));
		mList.add(new CommonItem("销售总额(元)","", "1600.00"));
		mList.add(new CommonItem("充值金额(元)","", "500.00"));
		mList.add(new CommonItem("赠送金额(元)","", "500.00"));
		mList.add(new CommonItem("退货单数","", "2"));
		mList.add(new CommonItem("退货金额(元)","", "102.00"));	
		
		mPayDetailList.add(new CommonItem("现金(元)","", "102.00"));	
		mPayDetailList.add(new CommonItem("银行卡(元)","", "102.00"));	
		mPayDetailList.add(new CommonItem("储值卡(元)","", "102.00"));	
		mPayDetailList.add(new CommonItem("支付宝(元)","", "102.00"));	
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		View listItem = listAdapter.getView(0, null, listView);
		listItem.measure(0, 0);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
}
