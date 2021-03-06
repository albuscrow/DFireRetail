package com.dfire.retail.app.manage.activity.retailmanager;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.EmployeeInfoDetailAdapter;
import com.dfire.retail.app.manage.adapter.EmployeeInfoDetailItem;
import com.dfire.retail.app.manage.util.Utility;
/**
 * 店家信息
 * @author Administrator
 *
 */
public class RetailInfoDetailActivity extends  TitleActivity{
	
	ArrayList<EmployeeInfoDetailItem> mList;
	private ListView mListView;
	private EmployeeInfoDetailAdapter adapter;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.store_info_detail);
		
		setTitle("店家信息");
		change2saveMode();
		setRightBtn(R.drawable.save);
		
		mListView = (ListView)findViewById(R.id.employee_info_detail_list);	
		mList = new ArrayList<EmployeeInfoDetailItem>();
		initEmployeeInfo();
		
		adapter = new EmployeeInfoDetailAdapter(RetailInfoDetailActivity.this, mList);
				
		mListView.setAdapter(adapter);
		Utility.setListViewHeightBasedOnChildren(mListView);
		
	}
	
  private void initEmployeeInfo(){
	  
	  mList.add(new EmployeeInfoDetailItem("员工姓名", "王晓红", false));
	  mList.add(new EmployeeInfoDetailItem("员工工号", "0001", false));
	  mList.add(new EmployeeInfoDetailItem("登陆密码", "1234", false));
	  mList.add(new EmployeeInfoDetailItem("员工角色", "收银员", true));
	  mList.add(new EmployeeInfoDetailItem("所属门店", "二维火文一路店", true));
	  mList.add(new EmployeeInfoDetailItem("入职时间", "2014-04-15", true));
	  mList.add(new EmployeeInfoDetailItem("联系电话", "12454645641", false));
	  mList.add(new EmployeeInfoDetailItem("性别", "女", true));
	  mList.add(new EmployeeInfoDetailItem("生日", "1990-04-15", true));
	  mList.add(new EmployeeInfoDetailItem("证件类型", "身份证", true));
	  mList.add(new EmployeeInfoDetailItem("证件号码", "教工路552号408室", false));	  
  }
}

