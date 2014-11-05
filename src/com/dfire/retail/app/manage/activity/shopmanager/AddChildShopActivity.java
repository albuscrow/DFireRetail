package com.dfire.retail.app.manage.activity.shopmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.TimePicker;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditRadio;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.ItemTextView;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.retailmanager.UserInfoInit;
import com.dfire.retail.app.manage.adapter.EmployeeInfoDetailItem;
import com.dfire.retail.app.manage.adapter.ShopInfoAdapter;
import com.dfire.retail.app.manage.adapter.ShopInfoItem;
import com.dfire.retail.app.manage.common.SelectEreaDialog;
import com.dfire.retail.app.manage.data.AddShopVo;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ProvinceVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;

/**
 * 新增门店信息
 * @author Administrator
 *
 */
public class AddChildShopActivity extends ShopInfoBaseActivity implements
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
	public ArrayList<String> shopTypeNameList = new ArrayList<String>();
	public List<ProvinceVo> mAdressList;
	public List<AllShopVo> mShopList;
    private SelectEreaDialog mSelectErea;
    
    private Integer tmpSelectProvId,tmpSelectCityId,tmpSelectDistrictId;
	String shopStr;
	
	private SpinerPopWindow shopTypePopupWindow;
	private Integer shopTypeVal = 1;
	
	//复制的门店ID
	
	private String tmpDataFromId;
	private static final int SELECTSHOPRECODE =2;
	
	
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
	
     public final static int ADD_PHOTO = 1;
     public final static int FROM_CAPTURE = 2;
     public final static int FROM_ABLUM = 3;
     public final static int ADD_CANCEL = 4;
	//add the new window
	WindowManager mWindowManager;
	Window mWindow ;
	public Button mAddChildShop;
	public PopupWindow mPw;
	
	public String parentName;
	public String shopType;
	public String dataFromShopId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		parentName = intent.getStringExtra(Constants.SHOPARENTNAME);
		shopType = intent.getStringExtra(Constants.SHOPTYPE);
		
		dataFromShopId = intent.getStringExtra(Constants.DATAFROMSHOPID);
		
		mAdressList = RetailApplication.getProvinceVo();
		mShopTypeList = RetailApplication.getShopTypeList();
		
		for(int i =0;i < mShopTypeList.size();i++){
			shopTypeNameList.add(mShopTypeList.get(i).getName());
		}
				
		setContentView(R.layout.activity_child_shop_info);		
		setTitleRes(R.string.shop_info);
		change2AddSaveMode();
		
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
	
    private void initRolePopupWidnow(final TextView v) {
        if (shopTypePopupWindow == null) {
        	shopTypePopupWindow = new SpinerPopWindow(this);
        	shopTypePopupWindow.setItemListener(new IOnItemSelectListener() {

                @Override
                public void onItemClick(int position) {
                   // _roleChoicePosition = position;
                    mET_ChildShopType.initData(mShopTypeList.get(position).getName(), mShopTypeList.get(position).getName()); //(mRoleList.get(position).getRoleName());
                    shopTypeVal = mShopTypeList.get(position).getVal();
                    //startSerachData();
                }
            });
        	shopTypePopupWindow.refreshData(shopTypeNameList, 0);
        	shopTypePopupWindow.setWidth((int)(v.getPaint().measureText(UserInfoInit.getInstance().getRoleListMax()) * 1.4f));
        }
    }
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//httppost
		//显示进度条对话框
		
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
		mEL_ChildSelectShop.setOnClickListener(this);
		mChildShopRadio=(ItemEditRadio)findViewById(R.id.childShopFlag);
		
		mAddChildShop = (Button)findViewById(R.id.bt_child_add_shop);
		mAddChildShop.setOnClickListener(this);
		

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
				if(mShopVo == null){
				     mShopVo = new ShopVo();
				}
				
				Log.i(TAG,"save shop info");
				mShopVo.setCode(mED_ChildShopCode.getCurrVal());
				mShopVo.setShopId("");
				if(StringUtils.isEquals("1", mChildShopRadio.getCurrVal())){
					mShopVo.setCopyFlag("1");
					mShopVo.setDataFromShopId(tmpDataFromId);
				}else{
					mShopVo.setCopyFlag("0");
					mShopVo.setDataFromShopId("");
				}
				mShopVo.setShopType(shopTypeVal.toString());
				mShopVo.setParentId(dataFromShopId);
				mShopVo.setSpell("");
				mShopVo.setPhone1(mED_ChildShopPhone.getCurrVal());
				mShopVo.setAddress(mED_ChildShopAdress.getCurrVal());
				mShopVo.setShopName(mED_ChildShopName.getCurrVal());
				mShopVo.setWeixin(mED_ChildShopWeixin.getCurrVal());
//				mShopVo.setStartTime("00:00");
//				mShopVo.setEndTime("24:00");
				mShopVo.setStartTime(mEL_ChildShopStartTime.getCurrVal());
				mShopVo.setEndTime(mEL_ChildShopEndTime.getCurrVal());
				mShopVo.setBusinessTime(null);
				mShopVo.setProvinceId(tmpSelectProvId);
				mShopVo.setCityId(tmpSelectCityId);
				mShopVo.setCountyId(tmpSelectDistrictId);
				mShopVo.setShopList(null);
				mShopVo.setLastVer("1");
				mShopVo.setShopName(mED_ChildShopName.getCurrVal());			 
				saveShopInfo(Constants.ADD,mShopVo);
				//change2saveFinishMode();
			}
		});
		
		mED_ChildShopName.initLabel("店家名称", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);		
		mED_ChildShopAdress.initLabel("店家地址", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		mED_ChildShopCode.initLabel("店家编码", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		mED_ChildShopPhone.initLabel("联系电话", "", Boolean.FALSE, InputType.TYPE_CLASS_NUMBER);
		mED_ChildShopWeixin.initLabel("微信", "", Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		
		mEL_ChildSelectShop.initLabel("选择门店","",Boolean.TRUE,this);		
		mET_ChildParentName.initLabel("店家编号", "");;
		mET_ChildParentName.initData(parentName, parentName);
		
		mEL_ChildShopArea.initLabel("所在区域","",Boolean.TRUE,this);
		mEL_ChildShopStartTime.initLabel("营业开始时间","",Boolean.FALSE,this);
		mEL_ChildShopStartTime.initData("00:00", "00:00");
		mEL_ChildShopEndTime.initLabel("营业结束时间","",Boolean.FALSE,this);
		mEL_ChildShopEndTime.initData("12:00", "12:00");
		mET_ChildShopType.initLabel("店家类型","",Boolean.TRUE,this);
		mET_ChildShopType.initData(shopType, shopType);
		
		mChildShopRadio.setVisibility(View.GONE);
		
		mChildShopRadio.initLabel("复制其他门店商品数据", "");
		mChildShopRadio.initData("1");
		
			
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
	if(v.getId() == R.id.childShopEndTime){
		Time time = new Time();     
	    time.setToNow();             
	    new TimePickerDialog(AddChildShopActivity.this,  new TimePickerDialog.OnTimeSetListener() {			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				String hour="";
				String min ="";
				if(hourOfDay <10){
					hour = "0"+hourOfDay;
				}
				else{
					hour = hourOfDay+"";
				}
				if(minute <10){
					min = "0"+minute;
				}
				else{
					min = minute+"";
				}
				mEL_ChildShopEndTime.initData(hour+":"+min, hour+":"+min);
			}
		}, time.hour, time.minute, true).show();
	}
	//开始营业时间
	else if(v.getId() == R.id.childShopStartTime){
		Time time = new Time();     
	    time.setToNow();             
	    new TimePickerDialog(AddChildShopActivity.this,  new TimePickerDialog.OnTimeSetListener() {			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				String hour="";
				String min ="";
				if(hourOfDay <10){
					hour = "0"+hourOfDay;
				}
				else
				{
					hour = hourOfDay+"";
				}
				if(minute <10){
					min = "0"+minute;
				}
				else{
					min = minute+"";
				}
				mEL_ChildShopStartTime.initData(hour+":"+min, hour+":"+min);
			}
		}, time.hour, time.minute, true).show();
	}
	//如果是选择区域，显示选择区域对话框
	else if(v.getId() == R.id.childShopArea){
		selectErea();
	}else if(v.getId() == R.id.childSelectShop){
        Intent intent = new Intent(AddChildShopActivity.this,SelecCopyShopActivity.class);
        intent.putExtra(Constants.SHOP_ID,dataFromShopId);

        startActivityForResult(intent, SELECTSHOPRECODE);
		}
}

//选择门店类型
public void ClickListener(View v) {
    //int position = Integer.parseInt(String.valueOf(v.getTag()));
    ItemEditList tmp = (ItemEditList)v;
    Log.i("kyolee","ClickListener = =====");
    switch (v.getId()) {
    case R.id.role_choice:        	
        initRolePopupWidnow(tmp.getLblVal());
        shopTypePopupWindow.showAsDropDown(tmp.getLblVal());
        break;
    }
}


/*
 * 通过网络请求，获取门店信息
 */
/*private void getRetialInfo(){
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
        	Log.i(TAG,"shopTypeList = "+ju.getJsonObject().get("shopTypeList").toString());
        	Log.i(TAG,"addressList = "+ju.getJsonObject().get("addressList").toString());
        	Log.i(TAG,"shopList = "+ju.getJsonObject().get("shopList").toString());
        	Log.i(TAG,"SHOP = "+ju.getJsonObject().get("shop").toString());

        	mShopVo = (ShopVo) ju.get(Constants.SHOP, ShopVo.class);  
        	
        	Log.i(TAG,"shop getAddress = "+mShopVo.getAddress());
        	
			 mShopTypeList =  (List<DicVo>) ju.get(Constants.SHOPTYPE_LIST, 
					new TypeToken<List<DicVo>>(){}.getType());
			 for(int i=0;i< mShopTypeList.size();i++)
				 
			 Log.i(TAG,"SHOP TYPE = "+mShopTypeList.get(i).getName() +" val = "+ mShopTypeList.get(i).getVal().toString());
			 
			 
			 
			 mAdressList =  (List<ProvinceVo>) ju.get(Constants.ADDRESS_LIST, 
						new TypeToken<List<ProvinceVo>>(){}.getType());
			 
			 for(int i=0;i < mAdressList.size();i++)
				 Log.i(TAG," mAdressList "+ i +" prvic = "+mAdressList.get(i).getProvinceName());
			 
			 mShopList =  (List<AllShopVo>) ju.get(Constants.SHOP_LIST, 
						new TypeToken<List<AllShopVo>>(){}.getType());
			
			 for(int i=0;i < mShopList.size();i++)
				 Log.i(TAG,"shop list = "+i+ "name = "+mShopList.get(i).getShopName());
			 
			 //更新UI显示信息
			 updateView();
			 getProgressDialog().dismiss();
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
            e.printStackTrace();
            getProgressDialog().dismiss();
            Log.e("results", "Login FaiL");
            Message msg = new Message();
            msg.what = Constants.HANDLER_FAIL;
            msg.obj = e.getMessage();
            //mLoginHandler.sendMessage(msg);
        }
    });

}*/


@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	if( data != null){
		 tmpDataFromId =  data.getStringExtra(Constants.SHOP_ID);
		String tmpShopName = data.getStringExtra(Constants.SHOPCOPNAME);
		mEL_ChildSelectShop.initData(tmpShopName, tmpShopName);
	}
	
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
		area += getCityName(mShopVo.getProvinceId(), mShopVo.getCityId(),mAdressList);
	}
	if(mShopVo.getCountyId() != null){
		area += getDistrictName(mShopVo.getProvinceId(),mShopVo.getCityId(),mShopVo.getCountyId(),mAdressList);
	}
	
	mEL_ChildShopArea.initData(area, area);		
	mED_ChildShopAdress.initData(mShopVo.getAddress());	
	mED_ChildShopPhone.initData(mShopVo.getPhone1());	
	mED_ChildShopWeixin.initData(mShopVo.getWeixin());	
	mEL_ChildShopStartTime.initData(mShopVo.getStartTime(), mShopVo.getStartTime());	
	mEL_ChildShopEndTime.initData(mShopVo.getEndTime(), mShopVo.getEndTime());
	

		
	}
	

public void selectErea(){
	mSelectErea = new SelectEreaDialog(AddChildShopActivity.this, 
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
