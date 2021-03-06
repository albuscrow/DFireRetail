package com.dfire.retail.app.manage.activity.retailmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.ItemTextView;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.EmployeeInfoDetailItem;
import com.dfire.retail.app.manage.adapter.EmployeePFMAdapter;
import com.dfire.retail.app.manage.adapter.EmployeePFMItem;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.Utility;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;

/**
 * 店家信息
 * @author Administrator
 *
 */
public class RetailInfoActivity extends  TitleActivity implements
OnClickListener, IItemListListener, IOnItemSelectListener {
	
	static final String TAG="RetailInfoActivity";
	
	ArrayList<EmployeeInfoDetailItem> mList;
	ArrayList<String> mAreaList = new ArrayList<String>();		
	AsyncHttpPost httppost;
	ArrayList<EmployeePFMItem> mSubStoreList;
	private EmployeePFMAdapter mSubAdapter;
	private ListView mSubStoreListView;
	private View mView = null;
	ListView mMenuList = null;
	private LayoutInflater mLayoutInflater;
	
	private ShopVo mShopVo;
	
	//店家区域列表
	private ItemEditList mEL_RetailArea;
	
	//门店开始营业时间
	private ItemEditList mEL_RetailStartTime;
	
	//门店结束营业时间
	private ItemEditList mEL_RetailEndTime;
	
	//门店名称
	private ItemEditText mED_RetailName;
	
	//门店联系电话
	private ItemEditText mED_RetailPhone;
	
	//门店微信
	private ItemEditText mED_RetailWeixin;
	
	//门店详细地址
	private ItemEditText mED_RetailAdress;
	
	//店家编号
	private ItemTextView mTX_RetailNO;

	
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
		
		setContentView(R.layout.activity_retail_info);
		
		setTitle("店家信息");
		change2saveMode();
		setRightBtn(R.drawable.save);
		
		//分店信息
		findView();
				
		//初始化门店信息
		initView();
		getRetialInfo();
		httppost.execute();
		
	}
	
	/**
	 * 查找控件ID信息
	 * 
	 */
	private void findView(){
		mSubStoreListView = (ListView)findViewById(R.id.sub_store_info_detail_list);
		mEL_RetailArea = (ItemEditList)findViewById(R.id.retailArea);
		mEL_RetailStartTime = (ItemEditList)findViewById(R.id.retailStartTime);
		mEL_RetailEndTime = (ItemEditList)findViewById(R.id.retailEndTime);		
		mED_RetailName = (ItemEditText)findViewById(R.id.retailName);
		mED_RetailAdress = (ItemEditText)findViewById(R.id.retailDetailAdress);
		mED_RetailPhone = (ItemEditText)findViewById(R.id.retailPhone);
		mED_RetailWeixin = (ItemEditText)findViewById(R.id.retailWeixin);
		mTX_RetailNO = (ItemTextView)findViewById(R.id.retailNo);

	}
	
	private void initView() {
		
		mED_RetailName.initLabel("店家名称", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		mED_RetailName.initData("二维火浙江公司");
		
		mTX_RetailNO.initLabel("店家编号", "");
		mTX_RetailNO.initData("5164", "");
		
		mEL_RetailArea.initLabel("所在区域","",Boolean.TRUE,this);
		
		//mEL_RetailArea.initData(RetailApplication.getShopVo().g, "1");
		
		mAreaList.add("浙江省杭州市拱墅区/县");
		mAreaList.add("浙江省杭州市西湖区/县");
		mAreaList.add("浙江省杭州市下沙区/县");
		mAreaList.add("浙江省杭州市上城区/县");
		mAreaList.add("浙江省杭州市下城区/县");
		mEL_RetailArea.initData("浙江省杭州市拱墅区/县", "1");
		mEL_RetailArea.initData("浙江省杭州市西湖区/县", "2");
		mEL_RetailArea.initData("浙江省杭州市下沙区/县", "3");
		mEL_RetailArea.initData("浙江省杭州市上城区/县", "4");
		mEL_RetailArea.initData("浙江省杭州市下城区/县", "5");
		
		mED_RetailAdress.initLabel("详细地址", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		mED_RetailAdress.initData(RetailApplication.getShopVo().getAddress());
		
		mED_RetailPhone.initLabel("联系电话", "", Boolean.TRUE, InputType.TYPE_CLASS_NUMBER);
		mED_RetailPhone.initData("15068735196");
		
		mED_RetailWeixin.initLabel("微信", "", Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		mED_RetailWeixin.initData("haohaokill");
		
		mEL_RetailStartTime.initLabel("开始时间","",Boolean.TRUE,this);
		mEL_RetailStartTime.initData("08:30", "1");
		
		mEL_RetailEndTime.initLabel("结束时间","",Boolean.TRUE,this);
		mEL_RetailEndTime.initData("17:30", "1");
		
		mSubStoreList = new ArrayList<EmployeePFMItem>();
		mSubAdapter = new EmployeePFMAdapter(RetailInfoActivity.this, mSubStoreList);		
		mSubStoreListView.setAdapter(mSubAdapter);		
		Utility.setListViewHeightBasedOnChildren(mSubStoreListView);
		
		
		
//		   lsShop.initLabel("店家","asdfasdfasdf",Boolean.FALSE,this);
//		   lsShop.initData("男", "1");
//		   
//		   lblShop.initLabel("店家名称", "我测试数据是否为空!");
//		   lblShop.initData("红烧肉", "123445667");
		
//		   txtName.initLabel("名称", "vvvvv", Boolean.TRUE, InputType.TYPE_NUMBER_FLAG_SIGNED);
//		   rdoOn.initLabel("开关", "开关箱测试啊。。。。。。");
//		   rdoOn.initData("1");
			
		}
	
  private void initRetailInfo(){
	    	  
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

@Override
public void onItemClick(int pos) {
	// TODO Auto-generated method stub
	mEL_RetailArea.changeData(mAreaList.get(pos), Integer.toString(pos));
}

@Override
public void onItemListClick(ItemEditList obj) {
	// TODO Auto-generated method stub

	
}

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
}

/*
 * 通过网络请求，获取门店信息
 */
private void getRetialInfo(){
	//传递请求参数
	RequestParameter param = new RequestParameter(true);
	param.setUrl(Constants.SHOPINIT);
	String shopId = RetailApplication.getShopVo().getShopId();
	param.setParam(Constants.SHOP_ID,shopId);
	Log.i(TAG,"SHOPID = "+shopId);
	//param.setParam(Constants.SHOP_ID，shopId);
//	RetailApplication.getShopVo().getId();
//	String strPass="";
//	strPass = MD5.GetMD5Code(mPassword.getText().toString());
//	parameters.setParam("entityCode", mShop.getText().toString());
//	parameters.setParam("username", mUserName.getText().toString());
//	parameters.setParam("password", strPass);
	
	httppost = new AsyncHttpPost(param,
    new RequestResultCallback() {
        @Override
        public void onSuccess(String str) {
        	
        	Log.i(TAG,"STR = "+str.length());
        	JsonUtil ju = new JsonUtil(str);
//        	RetailApplication.setShopVo((ShopVo) ju.get("shop", ShopVo.class));
//        	RetailApplication.setEntityModel((Integer) ju.get("entityModel", Integer.class));
            Message msg = new Message();
            if(CommonUtils.isResuestSucess(str)){
            	msg.what = Constants.HANDLER_SUCESS;
                msg.obj = str;
            }else{
            	msg.what = Constants.HANDLER_ERROR;
            	msg.obj = CommonUtils.getUMFailMsg(getBaseContext(),str);
            }

           // mLoginHandler.sendMessage(msg);
        }
        @Override
        public void onFail(Exception e) {
//        	getProgressDialog().dismiss();
            e.printStackTrace();
            Log.e("results", "Login FaiL");
            Message msg = new Message();
            msg.what = Constants.HANDLER_FAIL;
            msg.obj = e.getMessage();
            //mLoginHandler.sendMessage(msg);
        }
    });

}

private void saveShopInfo(){
	RequestParameter param = new RequestParameter(true);
	param.setUrl(Constants.SHOPINIT);
	//String shopId = RetailApplication.getShopVo().getId();
	//param.setParam(Constants.SHOP_ID,shopId);
	//param.setParam(Constants.SHOP_ID，shopId);
	//RetailApplication.getShopVo().getId();
	
	AsyncHttpPost httpsave = new AsyncHttpPost(param,
    new RequestResultCallback() {
        @Override
        public void onSuccess(String str) {
        	
          	Log.i(TAG,"str = "+str);
        	
        	JsonUtil ju = new JsonUtil(str);
        	
            Message msg = new Message();
            if(CommonUtils.isResuestSucess(str)){
            	msg.what = Constants.HANDLER_SUCESS;
                msg.obj = str;
            }else{
            	msg.what = Constants.HANDLER_ERROR;
            	msg.obj = CommonUtils.getUMFailMsg(getBaseContext(),str);
            }

           // mLoginHandler.sendMessage(msg);
        }
        @Override
        public void onFail(Exception e) {
//        getProgressDialog().dismiss();
            e.printStackTrace();
            Log.e("results", "Login FaiL");
            Message msg = new Message();
            msg.what = Constants.HANDLER_FAIL;
            msg.obj = e.getMessage();
            //mLoginHandler.sendMessage(msg);
        }
    });
}
  
}


