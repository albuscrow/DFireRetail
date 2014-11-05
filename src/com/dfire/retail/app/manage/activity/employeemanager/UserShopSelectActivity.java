package com.dfire.retail.app.manage.activity.employeemanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.shopmanager.SelecCopyShopActivity;
import com.dfire.retail.app.manage.adapter.ShopInfoAdapter;
import com.dfire.retail.app.manage.adapter.ShopInfoItem;
import com.dfire.retail.app.manage.common.ErrorMsg;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.util.Utility;
import com.google.gson.reflect.TypeToken;

public class UserShopSelectActivity extends TitleActivity{
	
	static final String TAG="ShopInfo";
	private List<AllShopVo> mShopList ;
	ArrayList<ShopInfoItem> shopInfoList = new ArrayList<ShopInfoItem>();
	private ShopInfoAdapter mshopAdapter;
	private ListView mListView;
    private TextView	searchButton;
    private EditText editKeyWord;
    private RelativeLayout relateSearch;
    

    private String shopUrl ="";
    private String shopId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_select_shop);
		
		setTitleRes(R.string.shop_info);
		showBackbtn();
			
		mShopList = RetailApplication.getShopList();
		mListView = (ListView) findViewById(R.id.selectshoplist);
		editKeyWord = (EditText) findViewById(R.id.input);
		searchButton = (TextView) findViewById(R.id.search);
		relateSearch = (RelativeLayout)findViewById(R.id.search_layout);
		
		shopInfoList.clear();
		
		for(int i=0; i < mShopList.size();i++){
			shopInfoList.add(new ShopInfoItem(mShopList.get(i).getShopId(),mShopList.get(i).getShopName(),
					mShopList.get(i).getParentId(),
		        					getResources().getString(R.string.shop_code)+mShopList.get(i).getCode()));
		}
	
		mshopAdapter = new ShopInfoAdapter(UserShopSelectActivity.this, shopInfoList);		
		mListView.setAdapter(mshopAdapter);		
		Utility.setListViewHeightBasedOnChildren(mListView);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(Constants.SHOP_ID, shopInfoList.get(arg2).getShopId());
				intent.putExtra(Constants.SHOPCOPNAME,shopInfoList.get(arg2).getShopName());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
	
	

	}
	


