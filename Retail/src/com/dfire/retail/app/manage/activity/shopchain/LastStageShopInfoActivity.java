package com.dfire.retail.app.manage.activity.shopchain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditRadio;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.listener.IItemIsChangeListener;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.usermanager.AlertDialogCommon;
import com.dfire.retail.app.manage.adapter.EmployeeInfoDetailItem;
import com.dfire.retail.app.manage.adapter.ShopInfoAdapter;
import com.dfire.retail.app.manage.adapter.ShopInfoItem;
import com.dfire.retail.app.manage.common.CommonSelectTypeDialog;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.common.SelectEreaDialog;
import com.dfire.retail.app.manage.common.SelectTimeDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ProvinceVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.ShopDetailBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.util.Utility;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;

public class LastStageShopInfoActivity extends ShopInfoBaseActivity implements
OnClickListener, IItemListListener, IOnItemSelectListener ,IItemIsChangeListener{
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
	
	private SpinerPopWindow shopTypePopupWindow;
	private Integer shopTypeVal;
	private static final int SELECTPARENTID =1;
	public EditText mEditCode;
	public Button mSearch;
	public ImageView mShopLogo; 
     Bitmap myBitmap; 
     public byte[] mContent; 
	
	//店家区域列表
     public ItemEditList mEL_ChildShopArea;
	//门店开始营业时间
     public ItemEditList mEL_ChildShopStartTime;	
	//上级门店名称
     public ItemEditList mEL_ChildParentName;	
	//门店结束营业时间
     public ItemEditList mEL_ChildShopEndTime;	
     public ItemEditList mEL_ChildShopType;
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
	public String parentId;
	public String shopCode;
    private String tmpSelectProvName,tmpSelectCityName,tmpSelectDistictName;
    private SelectTimeDialog mStartTime;
    private SelectTimeDialog mEndTime;
    private String selectTime= null;
    private LinearLayout mAddChildShopLine; 
	public Button  childShopDelete;
	
	 //增加监听数据修改的监听器，切换到save模式
	 
	 private boolean[] isChange;
	 private boolean isSaveMode = false;
	 private final static int EDITCOUNDT = 10;
	 private CommonSelectTypeDialog mSelectShopType;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//初始化是否改变呢信息
		isChange = new boolean[EDITCOUNDT];		
		for(int i=0 ;i < EDITCOUNDT;i++){
			isChange[i] = false;
		}
		Intent intent=getIntent();
		parentName=intent.getStringExtra(Constants.SHOPARENTNAME);
		shopCode=intent.getStringExtra(Constants.SEARCHSHOPCODE);
		
		Log.i(TAG,"shopCode = "+shopCode);
		mAdressList = RetailApplication.getProvinceVo();
		
		mShopTypeList = RetailApplication.getShopTypeList();		
		for(int i =0;i < mShopTypeList.size();i++){
			shopTypeNameList.add(mShopTypeList.get(i).getName());
		}
		setContentView(R.layout.activity_child_shop_info);		
		setTitleRes(R.string.shop_info);
		showBackbtn();
		findView();				
		initView();
	    mChildShopRadio.setVisibility(View.GONE);
	    mAddChildShop.setVisibility(View.GONE);
	    mSearchLine = (LinearLayout)findViewById(R.id.childShopSearcheLine);
	    mChildCountLine = (LinearLayout)findViewById(R.id.line_child_shop_count);
	    mSearchLine.setVisibility(View.GONE);
	    mChildCountLine.setVisibility(View.GONE);
	    mSubStoreListView.setVisibility(View.GONE);
		getRetialInfo();
	}
	
    private void initShopTypePopupWidnow(final TextView v) {
       /* if (shopTypePopupWindow == null) {
        	shopTypePopupWindow = new SpinerPopWindow(this);
        	shopTypePopupWindow.setItemListener(new IOnItemSelectListener() {

                @Override
                public void onItemClick(int position) {
                   // _roleChoicePosition = position;
                	mEL_ChildShopType.initData(mShopTypeList.get(position).getName(), mShopTypeList.get(position).getName()); //(mRoleList.get(position).getRoleName());
                    shopTypeVal = mShopTypeList.get(position).getVal();
                    //startSerachData();
                }
            });
        	shopTypePopupWindow.refreshData(shopTypeNameList, 0);
        	shopTypePopupWindow.setWidth((int)(v.getPaint().measureText(CommonUtils.geStringListMax(shopTypeNameList)) *1.4f));
        }*/
    	 mSelectShopType = new CommonSelectTypeDialog(LastStageShopInfoActivity.this,shopTypeNameList);
 	    mSelectShopType.show();
 	    
 	    mSelectShopType.getTitle().setText(this.getResources().getString(R.string.shop_type));;	        
 	    mSelectShopType.getConfirmButton().setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				mEL_ChildShopType.changeData(mSelectShopType.getCurrentData(),mSelectShopType.getCurrentData());
 				
 				 shopTypeVal = mShopTypeList.get(mSelectShopType.getCurrentPosition()).getVal();
 				
 				 mSelectShopType.dismiss();
 			}
 		});
 	    
 	    mSelectShopType.getCancelButton().setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				mSelectShopType.dismiss();
 			}
 		});	
    	
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		getProgressDialog().setCancelable(false);
//		getProgressDialog()
//				.setMessage("获取店家信息");
//		getProgressDialog().show();
		
	}


	/**
	 * 查找控件ID信息
	 * 
	 */
	protected void findView(){
		mSubStoreListView = (ListView)findViewById(R.id.child_info_detail_list);		
		mAddChildShopLine = (LinearLayout)findViewById(R.id.addChildShopLine);
		mEL_ChildParentName= (ItemEditList)findViewById(R.id.childShopParent);
		mEL_ChildShopArea = (ItemEditList)findViewById(R.id.childShopArea);
		mEL_ChildShopStartTime = (ItemEditList)findViewById(R.id.childShopStartTime);		
		mEL_ChildShopEndTime = (ItemEditList)findViewById(R.id.childShopEndTime);
		mEL_ChildShopType = (ItemEditList)findViewById(R.id.childShopType);
		mEL_ChildSelectShop = (ItemEditList)findViewById(R.id.childSelectShop);
		
		mED_ChildShopAdress = (ItemEditText)findViewById(R.id.childShoDetailAdress);
		mED_ChildShopName = (ItemEditText)findViewById(R.id.childShopName);
		mED_ChildShopPhone= (ItemEditText)findViewById(R.id.childShopPhone);
		mED_ChildShopWeixin = (ItemEditText)findViewById(R.id.childShopWeixin);
		
		mED_ChildShopCode = (ItemEditText)findViewById(R.id.childShopCode);

		mEditCode = (EditText)findViewById(R.id.edit_childshop_code);
		mSearch = (Button)findViewById(R.id.childshop_search);
		
		mED_ChildShopAdress.setMaxLength(100);
		mED_ChildShopName.setMaxLength(50);
		mED_ChildShopPhone.setMaxLength(13);
		mED_ChildShopWeixin.setMaxLength(50);
		mED_ChildShopCode.setMaxLength(50);
		
		mEL_ChildParentName.setOnClickListener(this);
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
		mAddChildShopLine.setVisibility(View.GONE);
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
		tmpSelectCityId = getCityID(tmpSelectProvId, mSelectErea.getCurCityName(), mAdressList);
		mShopVo.setCityId(tmpSelectCityId);
		tmpSelectDistrictId= getDistrictID(tmpSelectProvId, tmpSelectCityId, mSelectErea.getCurDistrictName(), mAdressList);
		mShopVo.setCountyId(tmpSelectDistrictId);

	}
	/**
	 * 初始化页面显示信息
	 */
	public void initView() {
		
		mED_ChildShopName.initLabel("店家名称", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);		
		mED_ChildShopAdress.initLabel("店家地址", "", Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		mED_ChildShopCode.initLabel("店家代码", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		mED_ChildShopPhone.initLabel("联系电话", "", Boolean.FALSE, InputType.TYPE_CLASS_NUMBER);
		mED_ChildShopWeixin.initLabel("微信", "", Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		
		mEL_ChildSelectShop.initLabel("选择门店","",Boolean.TRUE,this);		
		mEL_ChildParentName.initLabel("上级","",Boolean.TRUE,this);
		mEL_ChildParentName.initData(parentName, parentName);
		
		mEL_ChildShopArea.initLabel("所在区域","",Boolean.TRUE,this);
		mEL_ChildShopStartTime.initLabel("营业开始时间","",Boolean.FALSE,this);
		mEL_ChildShopEndTime.initLabel("营业结束时间","",Boolean.FALSE,this);
		mEL_ChildShopType.initLabel("店家类型","",Boolean.FALSE,this);

		
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
  	if(SELECTPARENTID ==requestCode &&  data != null){
		 parentId =  data.getStringExtra(Constants.SHOP_ID);
		 parentName = data.getStringExtra(Constants.SHOPCOPNAME);
		mEL_ChildParentName.changeData(parentName, parentName);
	}
  		
  } 
  
  public void addClick(View v){
		Log.i("kyolee","tag = "+Integer.parseInt(String.valueOf(v.getTag())));
		switch (Integer.parseInt(String.valueOf(v.getTag()))) {	

		case 5:
			String str = "";
			//searchShopBycode(str);
			break;
			
		}
			
  }
  

@Override
public void onItemClick(int pos) {
	// TODO Auto-generated method stub
	//mEL_RetailArea.changeData(mAreaList.get(pos), Integer.toString(pos));
}

@Override
public void onItemListClick(ItemEditList v) {
	// TODO Auto-generated method stub
	   if(R.id.childShopType == v.getId()) { 
	    	if(CommonUtils.isEmpty(RetailApplication.getShopVo().getParentId())){
		    	initShopTypePopupWidnow(v.getLblVal());
		        //shopTypePopupWindow.showAsDropDown(v.getLblVal());
	    	}
	    }
		else if(v.getId() == R.id.childShopEndTime){
		
			pushEndTime();
		}
		//开始营业时间
		else if(v.getId() == R.id.childShopStartTime){
		
			pushStartTime();
		}
		//如果是选择区域，显示选择区域对话框
		else if(v.getId() == R.id.childShopArea){
			selectErea();
			
		}else if(v.getId() == R.id.childShopParent){
		         Intent intent = new Intent(LastStageShopInfoActivity.this,ParentShopSelectActivity.class);
		         intent.putExtra(Constants.SHOP_ID,mShopVo.getShopId());
				 intent.putExtra(Constants.PARENTID,mShopVo.getParentId());
				 intent.putExtra(Constants.SHOPNAME,mShopVo.getShopName());
				 intent.putExtra(Constants.SHOPCODE,mShopVo.getCode());

			startActivityForResult(intent, SELECTPARENTID);
			}
	
}

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	//营业结束时间
	Log.i("ShopInfo","view id = "+v.getId());
	if(v.getId() == R.id.childShopEndTime){
	    /*Time time = new Time();     
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
				
				mEL_ChildShopEndTime.changeData(hour+":"+min, hour+":"+min);
			}
		}, time.hour, time.minute, true).show();*/
		pushStartTime();
	}
	//开始营业时间
	else if(v.getId() == R.id.childShopStartTime){
		 /*Time time = new Time();     
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
					
					mEL_ChildShopStartTime.changeData(hour+":"+min, hour+":"+min);
				}
			}, time.hour, time.minute, true).show();*/
		pushStartTime();
	}
	//如果是选择区域，显示选择区域对话框
	else if(v.getId() == R.id.childShopArea){
		selectErea();
	}else if(v.getId() == R.id.childShopParent){
        Intent intent = new Intent(LastStageShopInfoActivity.this,ParentShopSelectActivity.class);
        intent.putExtra(Constants.SHOP_ID,mShopVo.getParentId());
        startActivityForResult(intent, SELECTPARENTID);
		}
	else if(v.getId() == R.id.childShopDelete){
		dialog();
		
		//finish();
	}
}

protected void dialog() {
	
	final AlertDialogCommon alertDialog = new AlertDialogCommon(LastStageShopInfoActivity.this) ;

	alertDialog.setMessage(getResources().getString(R.string.removeShop));
	alertDialog.setPositiveButton(Constants.CONFIRM, new OnClickListener() {
		@Override
		public void onClick(View v) {
			alertDialog.dismiss();
			deleteShopInfo(mShopVo.getShopId());
		}
	});
	
	alertDialog.setNegativeButton(Constants.CANCEL, new OnClickListener() {
		@Override
		public void onClick(View v) {
			alertDialog.dismiss();
		}
	});
}

/*
 * 通过网络请求，获取门店信息
 */
private void getRetialInfo(){
	//传递请求参数

	RequestParameter param = new RequestParameter(true);
	param.setUrl(Constants.SHOPDETAILBYCODE);
	param.setParam(Constants.SEARCHSHOPCODE,shopCode);

	
	httppost = new AsyncHttpPost(LastStageShopInfoActivity.this,param,ShopDetailBo.class,true,
    new RequestCallback() {
        @Override
        public void onSuccess(Object str) {
        	
        //	Log.i(TAG,"STR = "+str.length());
        	
//        	JsonUtil ju = new JsonUtil(str.toString());
//        
//			 getProgressDialog().dismiss();
//            Message msg = new Message();
//            if(ju.getReturnCode().equals(Constants.SUCCESS)){
//            	msg.what = Constants.HANDLER_SUCESS;
        	ShopDetailBo bo = (ShopDetailBo)str;
            	mShopVo = (ShopVo) bo.getShop();  
            	
            	if(mShopVo !=null){
       		   //初始化选择区域信息
    	       	    tmpSelectCityId = mShopVo.getCityId();
    				 tmpSelectDistrictId = mShopVo.getCountyId();
    				 tmpSelectProvId = mShopVo.getProvinceId();
    				 parentId = mShopVo.getParentId();
    	        	mAdressList = RetailApplication.getProvinceVo();
    	        	Log.i(TAG,"shopStr = "+shopStr);
    	        	mShopTypeList = RetailApplication.getShopTypeList();
    	        												 
    				 //更新UI显示信息
    				 updateView();
            	}
//            }else{
//            	msg.what = Constants.HANDLER_FAIL;
//            	msg.obj = ju.getExceptionCode();
//            	msg.arg1 =1;
//            	mShopHandler.sendMessage(msg);
//        		LastStageShopInfoActivity.this.finish();
//            }

           // mLoginHandler.sendMessage(msg);
        }
        @Override
        public void onFail(Exception e) {
            e.printStackTrace();
//            getProgressDialog().dismiss();
//            Log.e("results", "Login FaiL");
//            Message msg = new Message();
//            msg.what = Constants.HANDLER_ERROR;
//            msg.obj = e.getMessage(); 
//            msg.arg1 =1;
//            mShopHandler.sendMessage(msg);
            LastStageShopInfoActivity.this.finish();
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
	parentId = mShopVo.getParentId();
	String area = "";
	//tmpSelectProvName,tmpSelectCityName,tmpSelectDistictName;
	
	if(mShopVo.getProvinceId() != null){
		area += getProvName(mShopVo.getProvinceId(),mAdressList);
		tmpSelectProvName= area;
	}
	if(mShopVo.getCityId() != null){
		String tmpCity = "";
		tmpCity = getCityName(mShopVo.getProvinceId(), mShopVo.getCityId(),mAdressList);
		tmpSelectCityName = tmpCity;
		if(!area.equals(tmpCity)){
			area += tmpCity;
		}
		
	}
	if(mShopVo.getCountyId() != null){
		tmpSelectDistictName = getDistrictName(mShopVo.getProvinceId(),mShopVo.getCityId(),mShopVo.getCountyId(),mAdressList);
		area += tmpSelectDistictName;
	}
	shopTypeVal = Integer.valueOf(mShopVo.getShopType());
	String shopTypeName = getShopTypeName(mShopTypeList,mShopVo.getShopType());
	mEL_ChildShopType.initData(shopTypeName, shopTypeName);
	mEL_ChildShopArea.initData(area, area);		
	mED_ChildShopAdress.initData(mShopVo.getAddress());	
	mED_ChildShopPhone.initData(mShopVo.getPhone1());	
	mED_ChildShopWeixin.initData(mShopVo.getWeixin());	
	mEL_ChildShopStartTime.initData(mShopVo.getStartTime(), mShopVo.getEndTime());	
	mEL_ChildShopEndTime.initData(mShopVo.getEndTime(), mShopVo.getEndTime());
		//绑定监听item数据是否变化
	mED_ChildShopAdress.setIsChangeListener(this);
	mED_ChildShopCode.setIsChangeListener(this);
	mED_ChildShopName.setIsChangeListener(this);
	mED_ChildShopPhone.setIsChangeListener(this);
	mED_ChildShopWeixin.setIsChangeListener(this);
	
	mEL_ChildParentName.setIsChangeListener(this);
	mEL_ChildShopArea.setIsChangeListener(this);
	mEL_ChildShopEndTime.setIsChangeListener(this);
	mEL_ChildShopStartTime.setIsChangeListener(this);
	mEL_ChildShopType.setIsChangeListener(this);
	
	
	}

public void selectErea(){
	mSelectErea = new SelectEreaDialog(LastStageShopInfoActivity.this, 
			mAdressList, 0, 0, 0);
	mSelectErea.show();
	mSelectErea.updateType(tmpSelectProvId, tmpSelectCityId, tmpSelectDistrictId);
	mSelectErea.getConfirmButton().setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			//tmpSelectProvName,tmpSelectCityName,tmpSelectDistictName;
			tmpSelectProvName =mSelectErea.getCurProvName();
			tmpSelectCityName = mSelectErea.getCurCityName();
			tmpSelectDistictName = mSelectErea.getCurDistrictName();
			tmpSelectProvId = mSelectErea.getCurProvinceId();
			tmpSelectCityId = getCityID(tmpSelectProvId, tmpSelectCityName, mAdressList);
			tmpSelectDistrictId= getDistrictID(tmpSelectProvId, tmpSelectCityId, tmpSelectDistictName, mAdressList);
			mEL_ChildShopArea.changeData(mSelectErea.getSelectErea(), mSelectErea.getSelectErea());
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
//选择门店类型
public void ClickListener(View v) {
  //int position = Integer.parseInt(String.valueOf(v.getTag()));
  ItemEditList tmp = (ItemEditList)v;
  Log.i("kyolee","ClickListener = =====");
  switch (v.getId()) {
  case R.id.childShopType:        	
      initRolePopupWidnow(tmp.getLblVal());
      shopTypePopupWindow.showAsDropDown(tmp.getLblVal());
      break;
  }
}

private void initRolePopupWidnow(final TextView v) {
    if (shopTypePopupWindow == null) {
    	shopTypePopupWindow = new SpinerPopWindow(this);
    	shopTypePopupWindow.setItemListener(new IOnItemSelectListener() {

            @Override
            public void onItemClick(int position) {
               // _roleChoicePosition = position;
            	mEL_ChildShopType.changeData(mShopTypeList.get(position).getName(), mShopTypeList.get(position).getName()); //(mRoleList.get(position).getRoleName());
                shopTypeVal = mShopTypeList.get(position).getVal();
                //startSerachData();
            }
        });
    	shopTypePopupWindow.refreshData(shopTypeNameList, 0);
    	shopTypePopupWindow.setWidth((int)(v.getPaint().measureText(CommonUtils.geStringListMax(shopTypeNameList)) *1.4f));
    }
}
@Override
public void onItemIsChangeListener(View v) {
	// TODO Auto-generated method stub
	Log.i(TAG,"onItemIsChangeListener test");
	switch (v.getId()) {
	//店家名称修改
	case R.id.childShopName:	
		isChange[0] = mED_ChildShopName.getChangeStatus();
		setTitleText(mED_ChildShopName.getCurrVal());
		break;
		//店家地址信息修改
		case R.id.childShoDetailAdress:	
			isChange[1] = mED_ChildShopAdress.getChangeStatus();

			break;
			//店家联系方式
		case R.id.childShopPhone:	
			isChange[2] = mED_ChildShopPhone.getChangeStatus();
			break;	
		//店家微信	
		case R.id.childShopWeixin:	
			isChange[3] = mED_ChildShopWeixin.getChangeStatus();					
			break;
			//店家编号
			case R.id.childShopCode:	
				isChange[4] = mED_ChildShopCode.getChangeStatus();
				break;
			//员工工号
			case R.id.childShopType:	
				isChange[5] = mEL_ChildShopType.getChangeStatus();						
				break;
				
			case R.id.childShopArea:	
				isChange[6] = mEL_ChildShopArea.getChangeStatus();
				
				break;
				//员工生日修改
				case R.id.childShopStartTime:	
					isChange[7] = mEL_ChildShopStartTime.getChangeStatus();
					//统计当前数据时候有变化							
	
					break;
					//证件类型修改
				case R.id.childShopEndTime:	
					isChange[8] = mEL_ChildShopEndTime.getChangeStatus();
					
					break;
					
				case R.id.childShopParent:	
					isChange[9] = mEL_ChildParentName.getChangeStatus();
					
					break;
												
	}
	
	isSaveMode = isHaveChange(isChange);
	if(isSaveMode){
		change2saveMode();
	}else{
		change2saveFinishMode();
	}
	
}

public ImageButton change2saveFinishMode() {
	// TODO Auto-generated method stub
	childShopDelete.setVisibility(View.VISIBLE);
	return super.change2saveFinishMode();
}


@Override
public ImageButton change2saveMode() {
	// TODO Auto-generated method stub
	super.change2saveMode();
	childShopDelete.setVisibility(View.GONE);
	mRight.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			if(checkShopVo() != null){
				ToastUtil.showLongToast(LastStageShopInfoActivity.this, checkShopVo());
				return;
			}
			
			mShopVo.setCode(mED_ChildShopCode.getCurrVal());
//			mShopVo.setEntityId("");
//			mShopVo.setShopId("");
			if(shopTypeVal != null)
				mShopVo.setShopType(shopTypeVal.toString());
			mShopVo.setSpell("");
			if(CommonUtils.isEmpty(mED_ChildShopPhone.getCurrVal()))
					mShopVo.setPhone1(mED_ChildShopPhone.getCurrVal());
			if(CommonUtils.isEmpty(mED_ChildShopAdress.getCurrVal()))
					mShopVo.setAddress(mED_ChildShopAdress.getCurrVal());		
			if(CommonUtils.isEmpty(mED_ChildShopName.getCurrVal()))
					mShopVo.setShopName(mED_ChildShopName.getCurrVal());
			if(CommonUtils.isEmpty(mED_ChildShopWeixin.getCurrVal()))
					mShopVo.setWeixin(mED_ChildShopWeixin.getCurrVal());
//			mShopVo.setStartTime("00:00");
//			mShopVo.setEndTime("24:00");
			mShopVo.setStartTime(mEL_ChildShopStartTime.getCurrVal());
			mShopVo.setEndTime(mEL_ChildShopEndTime.getCurrVal());
			mShopVo.setBusinessTime(null);
			mShopVo.setProvinceId(tmpSelectProvId);
			tmpSelectCityId = getCityID(tmpSelectProvId, tmpSelectCityName, mAdressList);
			mShopVo.setCityId(tmpSelectCityId);
			tmpSelectDistrictId= getDistrictID(tmpSelectProvId, tmpSelectCityId, tmpSelectDistictName, mAdressList);
			mShopVo.setCountyId(tmpSelectDistrictId);
			mShopVo.setParentId(parentId);
			//mShopVo.setShopList(null);
			//mShopVo.setLastVer("");
			if(CommonUtils.isEmpty(mShopVo.getCopyFlag())){
				mShopVo.setCopyFlag("0");
			}
			mShopVo.setShopName(mED_ChildShopName.getCurrVal());			 
			saveShopInfo(Constants.EDIT,mShopVo);
		}
	});
	mLeft.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
//			updateView();
//			change2saveFinishMode();
			LastStageShopInfoActivity.this.finish();
		}
	});
	return null;
}

private void pushStartTime(){
	
	if(mStartTime == null){
		mStartTime = new SelectTimeDialog(LastStageShopInfoActivity.this,true);
	}
	mStartTime.show();
	mStartTime.getTitle().setText(LastStageShopInfoActivity.this.getResources().getString(R.string.select_time));
	mStartTime.updateDays(selectTime);
	mStartTime.getConfirmButton().setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			mStartTime.dismiss();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");
			selectTime = mStartTime.getCurrentTime();
			mEL_ChildShopStartTime.changeData(selectTime,selectTime);
			
			
		}
	});
	mStartTime.getCancelButton().setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mStartTime.dismiss();
			mStartTime.closeOptionsMenu();
			
		}
	});
}


private void pushEndTime(){
	if(mEndTime == null){
		mEndTime = new SelectTimeDialog(LastStageShopInfoActivity.this,true);
	}
	mEndTime.show();
	mEndTime.getTitle().setText(LastStageShopInfoActivity.this.getResources().getString(R.string.select_time));
	mEndTime.updateDays(selectTime);
	mEndTime.getConfirmButton().setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			mEndTime.dismiss();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");
			selectTime = mEndTime.getCurrentTime();
			mEL_ChildShopEndTime.changeData(selectTime,selectTime);
			
			
		}
	});
	mEndTime.getCancelButton().setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mEndTime.dismiss();
			mEndTime.closeOptionsMenu();
			
		}
	});
}

/**
 * 检测当前的数据是否符合要求
 * @return
 */
private String checkShopVo(){
	
	String ret =null;
	if(CommonUtils.isEmpty(mED_ChildShopName.getCurrVal())){
		ret = "商户名称不能为空，请输入!";
	}else if(CommonUtils.isEmpty(mED_ChildShopCode.getCurrVal())){
		ret = "店家代码不能为空，请输入！";
	}else if(tmpSelectCityId == null || tmpSelectProvId == null ){
		ret = "所在地区不能为空，请输入!";
	}else if(!CommonUtils.isEmpty(mED_ChildShopPhone.getCurrVal())){
		
		if(mED_ChildShopPhone.getCurrVal().length()!=0 &&
				mED_ChildShopPhone.getCurrVal().length()!=11 &&mED_ChildShopPhone.getCurrVal().length()!=13){
			ret = "电话号码是11或13位！";
		}
	}
		
	return ret;
}
}
