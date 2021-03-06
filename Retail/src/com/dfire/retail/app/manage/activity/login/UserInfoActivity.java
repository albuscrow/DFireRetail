package com.dfire.retail.app.manage.activity.login;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.adapter.MoreInfoAdapter;
import com.dfire.retail.app.manage.adapter.MoreInfoItem;
import com.dfire.retail.app.manage.util.Utility;
import com.slidingmenu.lib.SlidingMenu;

public class UserInfoActivity extends Activity{
	
	ArrayList<MoreInfoItem> mList;
	private ListView mListView;
	private MoreInfoAdapter adapter;
	private SlidingMenu evalMenu = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_slid_main);		
		evalMenu = (SlidingMenu)findViewById(R.id.more_sliding_menu);
        evalMenu.setMode(SlidingMenu.LEFT);
        evalMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        evalMenu.setMenu(R.layout.more_sliding_menu);
        evalMenu.setContent(R.layout.user_info);
        evalMenu.setBehindWidth(200);
        
		
		mListView = (ListView)findViewById(R.id.more_info_list);
		
		mList = new ArrayList<MoreInfoItem>();
		
		mList.add(new MoreInfoItem (R.drawable.ico_more_pw,"修改密码"));
		mList.add(new MoreInfoItem (R.drawable.ico_more_bg,"更换图片"));
		mList.add(new MoreInfoItem (R.drawable.ico_more_contact,"帮助中心"));
		mList.add(new MoreInfoItem (R.drawable.ico_more_about,"关于"));		
		mList.add(new MoreInfoItem (R.drawable.ico_more_quit,"退出"));		
	
		adapter = new MoreInfoAdapter(UserInfoActivity.this, mList);
		
		mListView.setAdapter(adapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UserInfoActivity.this,ChangeBackgroundActivity.class);
				startActivity(intent);
				
			}
		});
		
		Utility.setListViewHeightBasedOnChildren(mListView);
	}

	public void slidingBtnClick(View v) {
		
	}

}
