package com.dfire.retail.app.manage.activity.retailmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.EmployeeInfoDetailAdapter;
import com.dfire.retail.app.manage.adapter.EmployeeInfoDetailItem;
import com.dfire.retail.app.manage.adapter.EmployeePFMAdapter;
import com.dfire.retail.app.manage.adapter.EmployeePFMItem;
import com.dfire.retail.app.manage.util.Utility;

/**
 * 店家信息
 * @author Administrator
 *
 */
public class RetailInfoActivity extends  TitleActivity{
	
	ArrayList<EmployeeInfoDetailItem> mList;
	
	ArrayList<EmployeePFMItem> mSubStoreList;
	private ListView mListView;
	private EmployeeInfoDetailAdapter adapter;
	private EmployeePFMAdapter mSubAdapter;
	private ListView mSubStoreListView;
	private View mView = null;
	ListView mMenuList = null;
	private LayoutInflater mLayoutInflater;
	
	private final static int ADD_PHOTO = 1;
	private final static int FROM_CAPTURE = 2;
	private final static int FROM_ABLUM = 3;
	private final static int ADD_CANCEL = 4;
	
	//add the new window
	WindowManager mWindowManager;
	Window mWindow ;
    private PopupWindow mPw;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.store_info_detail);
		
		setTitle("店家信息");
		change2saveMode();
		setRightBtn(R.drawable.save);
		
		mListView = (ListView)findViewById(R.id.store_info_detail_list);	
		mList = new ArrayList<EmployeeInfoDetailItem>();
		mSubStoreList = new ArrayList<EmployeePFMItem>();
		
		//初始化店家信息
		initEmployeeInfo();
		
		adapter = new EmployeeInfoDetailAdapter(RetailInfoActivity.this, mList);				
		mListView.setAdapter(adapter);
		
		//分店信息
		
		mSubStoreListView = (ListView)findViewById(R.id.sub_store_info_detail_list);	
		mSubAdapter = new EmployeePFMAdapter(RetailInfoActivity.this, mSubStoreList);
		mSubStoreListView.setAdapter(mSubAdapter);
		
		
		Utility.setListViewHeightBasedOnChildren(mListView);
		Utility.setListViewHeightBasedOnChildren(mSubStoreListView);
	}
	
  private void initEmployeeInfo(){
	  
	  mList.add(new EmployeeInfoDetailItem("店家名称", "二维火浙江公司", false));
	  mList.add(new EmployeeInfoDetailItem("店家编码", "0001", false));
	  mList.add(new EmployeeInfoDetailItem("所在地区", "浙江省杭州市拱墅区/县 ", true));
	  mList.add(new EmployeeInfoDetailItem("详细地址", "教工路552号", false));
	  mList.add(new EmployeeInfoDetailItem("所属门店", "二维火文一路店", true));
	  mList.add(new EmployeeInfoDetailItem("联系电话", "12454645641", false));
	  mList.add(new EmployeeInfoDetailItem("微信", "12454645641", false));
	  mList.add(new EmployeeInfoDetailItem("营业开始时间", "08:30", true));
	  mList.add(new EmployeeInfoDetailItem("营业结束时间", "12:00", true));	  
	  
	  mSubStoreList.add(new EmployeePFMItem(null, null, "二维或杭州公司", "店家代码：0100"));
	  mSubStoreList.add(new EmployeePFMItem(null, null, "二维或杭州公司", "店家代码：0101"));
  }
  
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
  	if (1 == requestCode) {
  		// 系统相机返回处理         
  		if (resultCode == Activity.RESULT_OK) {
  			Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");  
  			// 处理图像 	
  			//wallpaper.setImageBitmap(cameraBitmap);
  			}         
  		}
  	super.onActivityResult(requestCode, resultCode, data);
  		
  } 
  
  public void addClick(View v){
		Log.i("kyolee","tag = "+Integer.parseInt(String.valueOf(v.getTag())));
		switch (Integer.parseInt(String.valueOf(v.getTag()))) {	
		
		case ADD_PHOTO:
			showAddMenu();
			break;
		case FROM_CAPTURE: 
	        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    startActivityForResult(intent, 1);
			break;
		case FROM_ABLUM:
			break;
		case ADD_CANCEL:
			if(mPw!=null && mPw.isShowing())
			{
				mPw.dismiss();
			}
			break;
			
		}
			
  }
  
  private void showAddMenu(){
//	  mWindowManager =  getWindowManager();	
//	  if(mMenuList == null)
//		  mMenuList = new ListView(StoreInfoActivity.this);	  
//		mWindow = getWindow();		
//		WindowManager.LayoutParams lp = mWindow.getAttributes(); 	
//		//mWindow.setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
//		
//		Display display = mWindowManager.getDefaultDisplay();		 
//	    lp.width = (int) (display.getWidth()-20);
//	    lp.height = LayoutParams.WRAP_CONTENT;
	  		
	      LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  	  mView = inflater.inflate(R.layout.store_info_add_menu,null, false);	
	      mPw =new PopupWindow(mView,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,true);
//	      getWindow().setAttributes(lp);
	      mPw.setBackgroundDrawable(new BitmapDrawable());// 响应返回键必须的语句。
	      mPw.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 10, 10);
	    
//	    mWindow.setAttributes(lp);
	    	    
//	    lp.gravity=Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
	    
	    //mMenuList.setBackgroundColor(getResources().getColor(R.color.white));
	    
//	    mView = View.inflate(StoreInfoActivity.this,R.layout.store_info_add_menu,null);
//	    mWindow.setBackgroundDrawableResource(getResources().getColor(R.color.transparent));
//	    mWindow.addContentView(mView, lp);
//	    String Routes[]={"第1条","第2条","第3条","第4条","第5条"};
//
//        ArrayList<String> data = new ArrayList<String>();
//                for (int i = 0; i < 5; i++) {
//                        data.add(Routes[i]);
//                }
//	    mMenuList.setAdapter(new ArrayAdapter<String>(StoreInfoActivity.this,
//	    		android.R.layout.simple_list_item_1,data));
//	    mWindowManager.addView(mView, lp);
	    
	    //mView.setFocusable(true);
	    //mView.setFocusableInTouchMode(true);
	    mView.requestFocus();
  }


  
}


