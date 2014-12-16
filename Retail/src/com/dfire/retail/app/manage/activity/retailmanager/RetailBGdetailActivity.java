package com.dfire.retail.app.manage.activity.retailmanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.BaseActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsManagerMainMenuActivity;
import com.dfire.retail.app.manage.activity.login.ChangeBackgroundActivity;
import com.dfire.retail.app.manage.activity.logisticmanager.LogisticsManagerActivity;
import com.dfire.retail.app.manage.activity.messagemanage.MessageManangeActivity;
import com.dfire.retail.app.manage.activity.setting.SettingActivity;
import com.dfire.retail.app.manage.activity.stockmanager.StockManagerActivity;
import com.dfire.retail.app.manage.activity.usermanager.EmployeeActivity;
import com.dfire.retail.app.manage.adapter.MoreInfoAdapter;
import com.dfire.retail.app.manage.adapter.MoreInfoItem;
import com.dfire.retail.app.manage.common.ErrorMsg;
import com.dfire.retail.app.manage.data.IncomeVo;
import com.dfire.retail.app.manage.data.bo.IncomeReturnBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;

/**
 * 后台主页面
 * @author kyolee
 * @date 2014-9-10
 */

public class RetailBGdetailActivity extends BaseActivity{
	
	private String TAG = "RetailBackGround";
	private ViewPager viewPager = null;
	private List<View> pageList = null;
	MyPagerAdapter mMyPagerAdapter = null;
	private TextView mRetailNameTV = null;
	private TextView mUserNameTV = null;
	private TextView mDateTimeTV =null;
	
	IncomeVo todayIncome,yesterdayIncome,monthIncome;
	List<IncomeVo> profit = new ArrayList<IncomeVo>();
	
	//今日收入记录
	private TextView mNowdayTotalIncomeTV = null;
	private TextView mNowdayTotalProfitTV = null;
	private TextView mNowdayTotalOrdernumTV =null;
	private TextView mNowdayPerConsumeTV =null;
	
	//昨日收入记录
	private TextView mYesterdayTotalIncomeTV = null;
	private TextView mYesterdayTotalProfitTV = null;
	private TextView mYesterdayTotalOrdernumTV =null;
	private TextView mYesterdayPerConsumeTV =null;
	
	//月收入记录
	private TextView mNowmonthTotalIncomeTV = null;
	private TextView mNowmonthTotalProfitTV = null;
	private TextView mNowmonthTotalOrdernumTV =null;
	private TextView mNowmonthPerConsumeTV =null;
	
//	AsyncHttpPost httppost2 ;
//	
//	AsyncHttpPost httppost3 ;
	
	private MenuDrawer mMenu;
	private ListView mListView;
	private ArrayList<MoreInfoItem> mList;
	private MoreInfoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//求下次注释别人的代码写一下原因
		mMenu = MenuDrawer.attach(this,Position.RIGHT);
		mMenu.setContentView(R.layout.retail_main);
		mMenu.setMenuView(R.layout.user_info);
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.retail_main);
//		initIntroductionPage();
		initMenu();
		
		mRetailNameTV = (TextView)findViewById(R.id.retail_name);
		mUserNameTV = (TextView)findViewById(R.id.account_user_name);
		mDateTimeTV = (TextView)findViewById(R.id.today_time);
		
		//当前页面，不需要获取用户初始化信息
		//UserInfoInit.getInstance().startGetUserInfo();//初始化用户数据
		initIntroductionPage();
		//updateView();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private void initMenu() {
		mListView = (ListView)mMenu.findViewById(R.id.more_info_list);
		
		mList = new ArrayList<MoreInfoItem>();
		mList.add(new MoreInfoItem(R.drawable.ico_more_contact, "帮助中心"));
		mList.add(new MoreInfoItem (R.drawable.ico_more_pw,"修改密码"));
		mList.add(new MoreInfoItem (R.drawable.ico_more_bg,"更换图片"));
		mList.add(new MoreInfoItem (R.drawable.ico_more_help,"帮助中心"));
		mList.add(new MoreInfoItem (R.drawable.ico_more_about,"关于"));		
		mList.add(new MoreInfoItem (R.drawable.ico_more_quit,"退出"));		
	
		adapter = new MoreInfoAdapter(this, mList);
		
		mListView.setAdapter(adapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				MoreInfoItem infoItem = mList.get(position);
				if (infoItem!=null) {
					Intent intent = new Intent();
					if (StringUtils.isEquals(infoItem.getText(), "帮助中心")) {
						intent.setClass(RetailBGdetailActivity.this,ChangeBackgroundActivity.class);
					}else if (StringUtils.isEquals(infoItem.getText(), "更换图片")) {
						intent.setClass(RetailBGdetailActivity.this,ChangeBackgroundActivity.class);
					}else {
						intent.setClass(RetailBGdetailActivity.this,ChangeBackgroundActivity.class);
					}
					startActivity(intent);
				}
			}
		});
		
		TextView userInfoTextView = (TextView)findViewById(R.id.user_info_name);
		
		String name = RetailApplication.getmUserInfo().getName();
		userInfoTextView.setText(name);
		((TextView)findViewById(R.id.user_title)).setText(name);
		((TextView)findViewById(R.id.user_store)).setText(RetailApplication.getShopVo().getShopName());
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	
	
	
	/**
	 * 初始化收入页面信息
	 */
	private void initIntroductionPage() {

		//初始化触摸探测
		LayoutInflater inflater = LayoutInflater.from(this);
		pageList = new LinkedList<View>();
		View v1 = inflater.inflate(R.layout.profit_1, null);
		View v2 = inflater.inflate(R.layout.profit_2, null);
		View v3 = inflater.inflate(R.layout.profit_3, null);
		pageList.add(v1);
		pageList.add(v2);
		pageList.add(v3);
		
		findPageTextView(v1,v2,v3);
		
		viewPager = (ViewPager)findViewById(R.id.profit_viewflipper);		
		mMyPagerAdapter = new MyPagerAdapter(RetailBGdetailActivity.this,profit);
		
		if(mMyPagerAdapter!=null)
		{
			viewPager.setAdapter(mMyPagerAdapter);		
		}
	}
	
	/**
	 * 点击后台主页面信息，跳转到对应的模块
	 * @param v
	 */
	public void ImgClickListener(View v) {
		switch (Integer.parseInt(String.valueOf(v.getTag()))) {
		case 1:// 营销设置
			startActivity(new Intent(RetailBGdetailActivity.this, SettingActivity.class));
			break;
		case 2:// 商品管理
			startActivity(new Intent(RetailBGdetailActivity.this, GoodsManagerMainMenuActivity.class));
			break;
		case 3:// 员工
			startActivity(new Intent(RetailBGdetailActivity.this, EmployeeActivity.class));
			break;
		case 4:// 物流管理
			startActivity(new Intent(RetailBGdetailActivity.this, LogisticsManagerActivity.class));
			
			break;
		case 5:// 库存管理
			startActivity(new Intent(RetailBGdetailActivity.this, StockManagerActivity.class));
			break;
		case 6:// 消息中心
			startActivity(new Intent(RetailBGdetailActivity.this, MessageManangeActivity.class));
		case 7:// 会员管理
			
			break;
		case 8:// 营销管理
			//startActivity(new Intent(StoreDetail.this, MemberInfoActivity.class));
			break;
		case 9:// 报表中心
			startActivity(new Intent(RetailBGdetailActivity.this, ReportCenterActivity.class));
			break;	
		case 10:// 更多用户信息
//			startActivity(new Intent(RetailBGdetailActivity.this, UserInfoActivity.class));
			mMenu.toggleMenu();
			break;		
		default :
			break;
		}
	}

	/**
	 *收益信息适配器
	 * @author 
	 *
	 */
	class MyPagerAdapter extends PagerAdapter {

		private Context ctx;
		
		public MyPagerAdapter(Context ctx,List<IncomeVo> income) {
			this.ctx = ctx;
	
		}
		
		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			return pageList == null ? 0 : pageList.size();
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
		 */
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
		 */
		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.View, int, java.lang.Object)
		 */
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(pageList.get(position));
		}
		
		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.View, int)
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			
			
			((ViewPager)container).addView(pageList.get(position));
			
			return pageList.get(position);
		}
		
	
	}
	/**
	 * 获取收入页面textview信息
	 */
	private void findPageTextView(View today,View yesterday,View month){
		
		mNowdayTotalIncomeTV = (TextView) today.findViewById(R.id.nowday_total_income);
		mNowdayTotalOrdernumTV = (TextView) today.findViewById(R.id.nowday_total_odernum);
		mNowdayTotalProfitTV = (TextView) today.findViewById(R.id.nowday_total_profit);
		mNowdayPerConsumeTV = (TextView) today.findViewById(R.id.nowday_per_consume);
		
		mYesterdayTotalIncomeTV = (TextView) yesterday.findViewById(R.id.yesterday_total_income);
		mYesterdayTotalOrdernumTV = (TextView) yesterday.findViewById(R.id.yesterday_total_odernum);
		mYesterdayTotalProfitTV = (TextView) yesterday.findViewById(R.id.yesterday_total_profit);
		mYesterdayPerConsumeTV = (TextView) yesterday.findViewById(R.id.yesterday_per_consume);
		
		mNowmonthTotalIncomeTV = (TextView) month.findViewById(R.id.nowmonth_total_income);
		mNowmonthTotalOrdernumTV = (TextView)month.findViewById(R.id.nowmonth_total_odernum);
		mNowmonthTotalProfitTV = (TextView)month.findViewById(R.id.nowmonth_total_profit);
		mNowmonthPerConsumeTV = (TextView) month.findViewById(R.id.nowmonth_per_consume);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Date now = new Date(); 
		//可以方便地修改日期格式 
		SimpleDateFormat dateFormat = new SimpleDateFormat(RetailBGdetailActivity.this.getResources().getString(R.string.date_format));
		String strTime = dateFormat.format( now ); 
		
		mRetailNameTV.setText(getMyApp().getShopVo().getShopName());
		mUserNameTV.setText(getMyApp().getmUserInfo().getName()); 
		mDateTimeTV.setText(strTime);
		
		
		
		//传递请求参数
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.MAINPAGE);
	//	getMyApp();
		//显示今天的数据信息
		parameters.setParam(Constants.SHOP_ID, RetailApplication.getShopVo().getShopId());
		parameters.setParam(Constants.TIMERANGE, "nowDay");		
		AsyncHttpPost httppost = new AsyncHttpPost(RetailBGdetailActivity.this,parameters, IncomeReturnBo.class,true,
        new RequestCallback() {
	        @Override
	        public void onSuccess(Object str) {
       
	        	IncomeReturnBo bo = (IncomeReturnBo)str;
		        	todayIncome = (IncomeVo) bo.getInComeMsgDay();
		        	if(todayIncome == null ){
		        		 Log.i("results", "kyolee today INCOME IS NULL");
		        	}
		        	yesterdayIncome = (IncomeVo) bo.getInComeMsgYDay();
		        	monthIncome = (IncomeVo) bo.getInComeMsgMonth();
		        	// 初始化页面信息
		        	
		        	updateView();
		            
		      
	        }
	        @Override
	        public void onFail(Exception e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = Constants.HANDLER_ERROR;
                msg.obj = e.getMessage();
                mIncomeHandler.sendMessage(msg);
	        }
        });
		httppost.execute();
		
							
	}
	
	/**
	 * 更新后台主页面信息
	 */
	private void updateView(){

		Log.i("KYOLEE","mNowdayTotalIncomeTV = "+mNowdayTotalIncomeTV + " todayIncome = ");
		
		if(todayIncome != null){
			if(todayIncome.getTotalIncome() !=null)
				mNowdayTotalIncomeTV.setText(todayIncome.getTotalProfit().toString());
			if(todayIncome.getTotalOrderNum() !=null)
				mNowdayTotalOrdernumTV.setText(todayIncome.getTotalOrderNum().toString());
			if(todayIncome.getTotalProfit() !=null)
				mNowdayTotalProfitTV.setText(todayIncome.getTotalIncome().toString());
			if(todayIncome.getPerConsume() !=null)
				mNowdayPerConsumeTV.setText(todayIncome.getPerConsume().toString());
		}
		if(yesterdayIncome != null){
			if(yesterdayIncome.getTotalIncome() !=null)
				mYesterdayTotalIncomeTV.setText(yesterdayIncome.getTotalProfit().toString());
			if(yesterdayIncome.getTotalOrderNum() !=null)
			    mYesterdayTotalOrdernumTV.setText(yesterdayIncome.getTotalOrderNum().toString());
			if(yesterdayIncome.getTotalProfit() !=null)
			    mYesterdayTotalProfitTV.setText(yesterdayIncome.getTotalIncome().toString());
			if(yesterdayIncome.getPerConsume() !=null)
			    mYesterdayPerConsumeTV.setText(yesterdayIncome.getPerConsume().toString());
		}
		
		if(monthIncome  != null){
			if(monthIncome.getTotalIncome() !=null)
			    mNowmonthTotalIncomeTV.setText(monthIncome.getTotalProfit().toString());
			if(monthIncome.getTotalOrderNum() !=null)
			    mNowmonthTotalOrdernumTV.setText(monthIncome.getTotalOrderNum().toString());
			if(monthIncome.getTotalProfit() !=null)
			    mNowmonthTotalProfitTV.setText(monthIncome.getTotalIncome().toString());
			if(monthIncome.getPerConsume() !=null)
			    mNowmonthPerConsumeTV.setText(monthIncome.getPerConsume().toString());
		}
						
		
	}
	
	/**
	 * 处理返回的结果，如果成功解析网络返回的json数据
	 */
	Handler mIncomeHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
//			getProgressDialog().dismiss();
			
			switch(msg.what){
			case Constants.HANDLER_SUCESS:								
					
					
				break;
				
            case Constants.HANDLER_FAIL:
                if (msg.obj != null) {
                    ToastUtil.showShortToast(getApplicationContext(),
                            ErrorMsg.getErrorMsg(msg.obj.toString()));
                } else {
                    ToastUtil.showShortToast(getApplicationContext(), "网络请求失败");
                }
                break;
            case Constants.HANDLER_ERROR:
                if (msg.obj != null) {
                    ToastUtil.showShortToast(getApplicationContext(),
                            msg.obj.toString());
                } else {
                    ToastUtil.showShortToast(getApplicationContext(), "网络请求错误");
                }
                break;

			}
		}
	};
	
	/**
	 * 键盘返回事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			RetailApplication.setmSessionId("");
			this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}	
	
}
