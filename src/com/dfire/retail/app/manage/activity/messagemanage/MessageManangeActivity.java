package com.dfire.retail.app.manage.activity.messagemanage;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;

public class MessageManangeActivity extends TitleActivity implements OnItemClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_manager);
		setTitleText("公告通知");
		showBackbtn();
	}
	
	
	
	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
		
    }
	

}
