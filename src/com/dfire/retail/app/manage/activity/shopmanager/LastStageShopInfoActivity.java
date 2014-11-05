package com.dfire.retail.app.manage.activity.shopmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditRadio;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.ItemTextView;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.adapter.EmployeeInfoDetailItem;
import com.dfire.retail.app.manage.adapter.ShopInfoAdapter;
import com.dfire.retail.app.manage.adapter.ShopInfoItem;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.common.SelectEreaDialog;
import com.dfire.retail.app.manage.data.AddShopVo;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ProvinceVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.Utility;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;

public class LastStageShopInfoActivity extends ShopInfoBaseActivity implements
OnClickListener, IItemListListener, IOnItemSelectListener {
//	
	private LinearLayout mSearchLine = null;
	private LinearLayout mChildCountLine = null;

static final String TAG="AddChildInfoActivity";
	
	ArrayList<EmployeeInfoDetailItem> mList;
	ArrayList<String> mAreaList = new ArrayList<String>();		
	AsyncHttpPost httppost;
	ArrayList<ShopInfoItem> mSubStoreList;
	public ShopInfoAdapter mSubAdapter;
	public ListView mSubStoreListView;
	public View mView = null;
	ListView mMenuList = null;
	public LayoutInflater mLayoutInflater;
	
	public ShopVo mShopVo;
	public List<DicVo> mShopTypeList;
	public List<ProvinceVo> mAdressList;
	public List<AllShopVo> mShopList;
    private SelectEreaDialog mSelectErea;
	public ArrayList<String> shopTypeNameList = new ArrayList<String>();
    private Integer tmpSelectProvId,tmpSelectCityId,tmpSelectDistrictId;
	String shopStr;
	
	public EditText mEditCode;
	public Button mSearch;
	AddShopVo myShopVo;
	public ImageView mShopLogo; 
     Bitmap myBitmap; 
     public byte[] mContent; 
	
	//店家区域列表
     public ItemEditList mEL_ChildShopArea;
	//门店开始营业时间
     public ItemEditList mEL_ChildShopStartTime;	
	//上级门店名称
     public ItemTextView mET_ChildParentName;	
	//门店结束营业时间
     public ItemEditList mEL_ChildShopEndTime;	
     public ItemEditList mET_ChildShopType;
     public ItemEditList mEL_ChildSelectShop;
	
     public ItemEditRadio mChildShopRadio;
	//门店名称
     public ItemEditText mED_ChildShopName;	
	//门店联系电话
     public ItemEditText mED_ChildShopPhone;	
	//门店微信
     public ItemEditText mED_ChildShopWeixin;	
	//门店详细地址
     public ItemEditText mED_ChildShopAdress;	
	//店家编号
     public ItemEditText mED_ChildShopCode;
	
	//add the new window
	WindowManager mWindowManager;
	Window mWindow ;
	public Button mAddChildShop;
	public PopupWindow mPw;
	
	public String parentName;
	public String shopCode;
	
	public Button  childShopDelete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		parentName=intent.getStringExtra(Constants.SHOPARENTNAME);
		shopCode=intent.getStringExtra(Constants.SEARCHSHOPCODE);
		
		setContentView(R.layout.activity_child_shop_info);		
		setTitleRes(R.string.shop_info);
		change2saveMode();
		findView();				
		initView();
	    mChildShopRadio.setVisibility(View.VISIBLE);
	    mAddChildShop.setVisibility(View.GONE);
	    mSearchLine = (LinearLayout)findViewById(R.id.childShopSearcheLine);
	    mChildCountLine = (LinearLayout)findViewById(R.id.line_child_shop_count);
	    mSearchLine.setVisibility(View.GONE);
	    mChildCountLine.setVisibility(View.GONE);
	    mSubStoreListView.setVisibility(View.GONE);
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getRetialInfo();		
	}


	/**
	 * 查找控件ID信息
	 * 
	 */
	protected void findView(){
		mSubStoreListView = (ListView)findViewById(R.id.child_info_detail_list);		
		
		mET_ChildParentName= (ItemTextView)findViewById(R.id.childShopParent);
		mEL_ChildShopArea = (ItemEditList)findViewById(R.id.childShopArea);
		mEL_ChildShopStartTime = (ItemEditList)findViewById(R.id.childShopStartTime);		
		mEL_ChildShopEndTime = (ItemEditList)findViewById(R.id.childShopEndTime);
		mET_ChildShopType = (ItemEditList)findViewById(R.id.childShopType);
		mEL_ChildSelectShop = (ItemEditList)findViewById(R.id.childSelectShop);
		
		mED_ChildShopAdress = (ItemEditText)findViewById(R.id.childShoDetailAdress);
		mED_ChildShopName = (ItemEditText)findViewById(R.id.childShopName);
		mED_ChildShopPhone= (ItemEditText)findViewById(R.id.childShopPhone);
		mED_ChildShopWeixin = (ItemEditText)findViewById(R.id.childShopWeixin);
		
		mED_ChildShopCode = (ItemEditText)findViewById(R.id.childShopCode);

		mEditCode = (EditText)findViewById(R.id.edit_childshop_code);
		mSearch = (Button)findViewById(R.id.childshop_search);
		
		mET_ChildParentName.setOnClickListener(this);
		mEL_ChildShopArea.setOnClickListener(this);
		mEL_ChildShopStartTime.setOnClickListener(this);
		mEL_ChildShopEndTime.setOnClickListener(this);
//		mET_ChildShopType.setOnClickListener(this);
		mEL_ChildSelectShop.setVisibility(View.GONE);
		mChildShopRadio=(ItemEditRadio)findViewById(R.id.childShopFlag);
		mChildShopRadio.setVisibility(View.GONE);
		mAddChildShop = (Button)findViewById(R.id.bt_child_add_shop);
		childShopDelete = (Button)findViewById(R.id.childShopDelete);
		childShopDelete.setVisibility(View.VISIBLE);
		childShopDelete.setOnClickListener(this);
	}
	
	/**
	 * 更新ShopVo信息
	 */
	private void upDateShopVo(){
		mShopVo.setShopName(mED_ChildShopName.getCurrVal());
		mShopVo.setCode(mED_ChildShopCode.getCurrVal());
		mShopVo.setAddress(mED_ChildShopAdress.getCurrVal());
		mShopVo.setWeixin(mED_ChildShopWeixin.getCurrVal());
		mShopVo.setPhone1(mED_ChildShopPhone.getCurrVal());
		mShopVo.setStartTime(mEL_ChildShopStartTime.getCurrVal());
		mShopVo.setEndTime(mEL_ChildShopEndTime.getCurrVal());
		if(CommonUtils.isEmpty(mShopVo.getCopyFlag())){
			mShopVo.setCopyFlag("0");
		}
		//mShopVo.setProvinceId(Integer.valueOf(11));
		//mShopVo.setCityId(Integer.valueOf(11));
		//mShopVo.setCountyId(Integer.valueOf(11));
		mShopVo.setProvinceId(tmpSelectProvId);
		mShopVo.setCityId(tmpSelectCityId);
		mShopVo.setCountyId(tmpSelectDistrictId);

	}
	/**
	 * 初始化页面显示信息
	 */
	public void initView() {
		mRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG,"save shop info");
				upDateShopVo();
				saveShopInfo(Constants.EDIT,mShopVo);
				 
				//change2saveFinishMode();
			}
		});
		
		mED_ChildShopName.initLabel("店家名称", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);		
		mED_ChildShopAdress.initLabel("店家地址", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		mED_ChildShopCode.initLabel("店家编码", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		mED_ChildShopPhone.initLabel("联系电话", "", Boolean.FALSE, InputType.TYPE_CLASS_NUMBER);
		mED_ChildShopWeixin.initLabel("微信", "", Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		
		mEL_ChildSelectShop.initLabel("选择门店","",Boolean.TRUE,this);		
		mET_ChildParentName.initLabel("上级","");
		mET_ChildParentName.initData(parentName, parentName);
		
		mEL_ChildShopArea.initLabel("所在区域","",Boolean.TRUE,this);
		mEL_ChildShopStartTime.initLabel("营业开始时间","",Boolean.FALSE,this);
		mEL_ChildShopEndTime.initLabel("营业结束时间","",Boolean.FALSE,this);
		mET_ChildShopType.initLabel("店家类型","",Boolean.FALSE,this);
		mChildShopRadio.setVisibility(View.GONE);
		mChildShopRadio.initLabel("复制其他门店商品数据", "");
		mChildShopRadio.initData("1");
		
		mSubStoreList = new ArrayList<ShopInfoItem>();
		mSubAdapter = new ShopInfoAdapter(LastStageShopInfoActivity.this, mSubStoreList);		
		mSubStoreListView.setAdapter(mSubAdapter);		
		Utility.setListViewHeightBasedOnChildren(mSubStoreListView);
		
			
		}
	

  
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	  ContentResolver resolver = getContentResolver(); 
   
  	super.onActivityResult(requestCode, resultCode, data);
  		
  } 
  
  public void addClick(View v){
		Log.i("kyolee","tag = "+Integer.parseInt(String.valueOf(v.getTag())));
		switch (Integer.parseInt(String.valueOf(v.getTag()))) {	

		case 5:
			String str = "";
			searchShopBycode(str);
			break;
			
		}
			
  }
  

@Override
public void onItemClick(int pos) {
	// TODO Auto-generated method stub
	//mEL_RetailArea.changeData(mAreaList.get(pos), Integer.toString(pos));
}

@Override
public void onItemListClick(ItemEditList obj) {
	// TODO Auto-generated method stub

	
}

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	//营业结束时间
	Log.i("ShopInfo","view id = "+v.getId());
	if(v.getId() == R.id.retailEndTime){
	    Time time = new Time();     
	    time.setToNow();             
	    new TimePickerDialog(LastStageShopInfoActivity.this,  new TimePickerDialog.OnTimeSetListener() {			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				String hour="";
				String min ="";
				if(hourOfDay <10){
					hour = "0"+hourOfDay;
				}else{
					hour = hourOfDay+"";
				}
			   if(minute <10){
					min = "0"+minute;
				}else{
					min= ""+minute;
				}
				
				mEL_ChildShopStartTime.initData(hour+":"+min, hour+":"+min);
			}
		}, time.hour, time.minute, true).show();
	}
	//开始营业时间
	else if(v.getId() == R.id.retailStartTime){
		 Time time = new Time();     
		    time.setToNow();             
		    new TimePickerDialog(LastStageShopInfoActivity.this,  new TimePickerDialog.OnTimeSetListener() {			
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
					String hour="";
					String min ="";
					if(hourOfDay <10){
						hour = "0"+hourOfDay;
					}else{
						hour = hourOfDay+"";
					}
				   if(minute <10){
						min = "0"+minute;
					}else{
						min= ""+minute;
					}
					
					mEL_ChildShopStartTime.initData(hour+":"+min, hour+":"+min);
				}
			}, time.hour, time.minute, true).show();
	}
	//如果是选择区域，显示选择区域对话框
	else if(v.getId() == R.id.childShopArea){
		selectErea();
	}
	else if(v.getId() == R.id.childShopDelete){
		deleteShopInfo();
		finish();
	}
}


/*
 * 通过网络请求，获取门店信息
 */
private void getRetialInfo(){
	//传递请求参数
	RequestParameter param = new RequestParameter(true);
	param.setUrl(Constants.SHOPDETAILBYCODE);
	param.setParam(Constants.SEARCHSHOPCODE,shopCode);

	
	httppost = new AsyncHttpPost(param,
    new RequestResultCallback() {
        @Override
        public void onSuccess(String str) {
        	
        //	Log.i(TAG,"STR = "+str.length());
        	
        	JsonUtil ju = new JsonUtil(str);
        	shopStr = ju.getJsonObject().get("shop").toString();
        	mShopVo = (ShopVo) ju.get(Constants.SHOP, ShopVo.class);  
        	
   		   //初始化选择区域信息
       	    tmpSelectCityId = mShopVo.getCityId();
			 tmpSelectDistrictId = mShopVo.getCountyId();
			 tmpSelectProvId = mShopVo.getProvinceId();
        	
        	mAdressList = RetailApplication.getProvinceVo();
        	Log.i(TAG,"shopStr = "+shopStr);
        	mShopTypeList = RetailApplication.getShopTypeList();
        												 
			 //更新UI显示信息
			 updateView();
			 getProgressDialog().dismiss();
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
            e.printStackTrace();
            getProgressDialog().dismiss();
            Log.e("results", "Login FaiL");
            Message msg = new Message();
            msg.what = Constants.HANDLER_FAIL;
            msg.obj = e.getMessage();
            //mLoginHandler.sendMessage(msg);
        }
    });
	httppost.execute();

}


/**
 * 网络请求成功，更新UI信息
 */
public void updateView(){
	
	mED_ChildShopName.initData(mShopVo.getShopName());	
	mED_ChildShopCode.initData(mShopVo.getCode());
	tmpSelectProvId = mShopVo.getProvinceId();
	tmpSelectCityId = mShopVo.getCityId();
	tmpSelectProvId = mShopVo.getProvinceId();

	String area = "";
	if(mShopVo.getProvinceId() != null){
		area += getProvName(mShopVo.getProvinceId(),mAdressList);
	}
	if(mShopVo.getCityId() != null){
		String tmpCity = "";
		tmpCity = getCityName(mShopVo.getProvinceId(), mShopVo.getCityId(),mAdressList);
		if(!area.equals(tmpCity)){
			area += tmpCity;
		}
	}
	if(mShopVo.getCountyId() != null){
		area += getDistrictName(mShopVo.getProvinceId(),mShopVo.getCityId(),mShopVo.getCountyId(),mAdressList);
	}
	String shopTypeName = getShopTypeName(mShopTypeList,mShopVo.getShopType());
	mET_ChildShopType.initData(shopTypeName, shopTypeName);
	mEL_ChildShopArea.initData(area, area);		
	mED_ChildShopAdress.initData(mShopVo.getAddress());	
	mED_ChildShopPhone.initData(mShopVo.getPhone1());	
	mED_ChildShopWeixin.initData(mShopVo.getWeixin());	
	mEL_ChildShopStartTime.initData(mShopVo.getStartTime(), mShopVo.getEndTime());	
	mEL_ChildShopEndTime.initData(mShopVo.getEndTime(), mShopVo.getEndTime());
	
	}
/*
 * 通过网络请求，删除门店信息
 */
private void deleteShopInfo(){
	//传递请求参数
	RequestParameter param = new RequestParameter(true);
	param.setUrl(Constants.SHOPDELETE);
	param.setParam(Constants.SHOP_ID,mShopVo.getShopId());
	
	httppost = new AsyncHttpPost(param,
    new RequestResultCallback() {
        @Override
        public void onSuccess(String str) {
        	
        Log.i(TAG,"STR = "+str);
        	
        	JsonUtil ju = new JsonUtil(str);  
			 getProgressDialog().dismiss();
            Message msg = new Message();
            if(CommonUtils.isResuestSucess(str)){
            	msg.what = Constants.HANDLER_SUCESS;
                msg.obj = str;
            }else{
            	msg.what = Constants.HANDLER_ERROR;
            	msg.obj = CommonUtils.getUMFailMsg(getBaseContext(),str);
            }

            //mLoginHandler.sendMessage(msg);
        }
        @Override
        public void onFail(Exception e) {
            e.printStackTrace();
            getProgressDialog().dismiss();
            Log.e("results", "Login FaiL");
            Message msg = new Message();
            msg.what = Constants.HANDLER_FAIL;
            msg.obj = e.getMessage();
            //mLoginHandler.sendMessage(msg);
        }
    });
	httppost.execute();

}

public void selectErea(){
	mSelectErea = new SelectEreaDialog(LastStageShopInfoActivity.this, 
			mAdressList, 0, 0, 0);
	mSelectErea.show();

	mSelectErea.getConfirmButton().setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			tmpSelectDistrictId = mSelectErea.getCurDistrictId();
			tmpSelectCityId = mSelectErea.getCurProvinceId();
			tmpSelectProvId = mSelectErea.getCurProvinceId();
			
			mEL_ChildShopArea.initData(mSelectErea.getSelectErea(), mSelectErea.getSelectErea());
			mSelectErea.dismiss();
		}
	});
	
	mSelectErea.getCancelButton().setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			mSelectErea.dismiss();
		}
	});
}
	
}
