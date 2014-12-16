package com.dfire.retail.app.manage.activity.retailmanager;

import android.os.Bundle;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;

public class MessageContactActivity extends TitleActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("店家信息");
		showBackbtn();
		setContentView(R.layout.activity_store_collect);
	}
	
}
