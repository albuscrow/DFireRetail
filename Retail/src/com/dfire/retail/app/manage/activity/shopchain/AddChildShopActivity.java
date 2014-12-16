package com.dfire.retail.app.manage.activity.shopchain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
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

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditRadio;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.common.item.listener.IItemRadioChangeListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.adapter.EmployeeInfoDetailItem;
import com.dfire.retail.app.manage.adapter.ShopInfoAdapter;
import com.dfire.retail.app.manage.adapter.ShopInfoItem;
import com.dfire.retail.app.manage.common.CommonSelectTypeDialog;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.common.SelectEreaDialog;
import com.dfire.retail.app.manage.common.SelectTimeDialog;
import com.dfire.retail.app.manage.data.AddShopVo;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ProvinceVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;

/**
 * 新增门店信息
 * @author Administrator
 *
 */
public class AddChildShopActivity extends ShopInfoBaseActivity implements
OnClickListener, IItemListListener, IOnItemSelectListener,IItemRadioChangeListener {
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
	private String tmpSelectProvName,tmpSelectCityName,tmpSelectDistictName;
	private String tmpDataFromId;
	private static final int SELECTPARENTID =1;
	private static final int SELECTSHOPRECODE =2;
	
    private SelectTimeDialog mStartTime;
    private SelectTimeDialog mEndTime;
    private String selectTime= null;
    private LinearLayout mAddChildShopLine; 
	public EditText mEditCode;
	public Button mSearch;
	AddShopVo myShopVo;
	public ImageView mShopLogo; 
	private LinearLayout deleteLine;
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
	public String parentId;
	public String shopType;
	public String dataFromShopId;
	private String tmpParentId;
	private String shopCode;
	private String shopName;
	private CommonSelectTypeDialog mSelectShopType;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		parentName = intent.getStringExtra(Constants.SHOPARENTNAME);
		shopType = intent.getStringExtra(Constants.SHOPTYPE);		
		dataFromShopId = intent.getStringExtra(Constants.DATAFROMSHOPID);		
		tmpParentId = intent.getStringExtra(Constants.PARENTID);
		shopCode = intent.getStringExtra(Constants.SHOPCODE);
		shopName = intent.getStringExtra(Constants.SHOPNAME);
		parentId = dataFromShopId;
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
	   mStartTime = new SelectTimeDialog(AddChildShopActivity.this,true);	
	   mEndTime = new SelectTimeDialog(AddChildShopActivity.this,true);
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
    	 mSelectShopType = new CommonSelectTypeDialog(AddChildShopActivity.this,shopTypeNameList);
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
		//显示进度条对话框
		
	}


	/**
	 * 查找控件ID信息
	 * 
	 */
	protected void findView(){
		deleteLine = (LinearLayout)findViewById(R.id.shopDeleteLine);
		deleteLine.setVisibility(View.GONE);
		mSubStoreListView = (ListView)findViewById(R.id.child_info_detail_list);
		mAddChildShopLine = (LinearLayout)findViewById(R.id.addChildShopLine);
		mAddChildShopLine.setVisibility(View.GONE);
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
		
		mED_ChildShopAdress.setMaxLength(100);
		mED_ChildShopName.setMaxLength(50);
		mED_ChildShopPhone.setMaxLength(13);
		mED_ChildShopWeixin.setMaxLength(50);
		mED_ChildShopCode.setMaxLength(50);

		mEditCode = (EditText)findViewById(R.id.edit_childshop_code);
		mSearch = (Button)findViewById(R.id.childshop_search);
		
		mEL_ChildParentName.setOnClickListener(this);
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
				if(checkShopVo() != null){
					ToastUtil.showLongToast(AddChildShopActivity.this, checkShopVo());
					return;
				}
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
				//mShopVo.setParentId(dataFromShopId);
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
				tmpSelectCityId = getCityID(tmpSelectProvId, tmpSelectCityName, mAdressList);
				mShopVo.setCityId(tmpSelectCityId);
				tmpSelectDistrictId= getDistrictID(tmpSelectProvId, tmpSelectCityId, tmpSelectDistictName, mAdressList);
				mShopVo.setCountyId(tmpSelectDistrictId);
				mShopVo.setShopList(null);
				mShopVo.setLastVer("1");
				mShopVo.setShopName(mED_ChildShopName.getCurrVal());	
				mShopVo.setParentId(parentId);
				saveShopInfo(Constants.ADD,mShopVo);
				//change2saveFinishMode();
			}
		});
		
		mED_ChildShopName.initLabel("店家名称", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);		
		mED_ChildShopAdress.initLabel("店家地址", "", Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		mED_ChildShopCode.initLabel("店家代码", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		mED_ChildShopPhone.initLabel("联系电话", "", Boolean.FALSE, InputType.TYPE_CLASS_NUMBER);
		mED_ChildShopWeixin.initLabel("微信", "", Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		
		mEL_ChildSelectShop.initLabel("选择门店","",Boolean.TRUE,this);	
		mEL_ChildSelectShop.setVisibility(View.GONE);
		
		mEL_ChildParentName.initLabel("上级","",Boolean.TRUE,this);
		mEL_ChildParentName.initData(parentName, parentName);
		
		mEL_ChildShopArea.initLabel("所在区域","",Boolean.TRUE,this);
		mEL_ChildShopStartTime.initLabel("营业开始时间","",Boolean.FALSE,this);
		mEL_ChildShopStartTime.initData("00:00", "00:00");
		mEL_ChildShopEndTime.initLabel("营业结束时间","",Boolean.FALSE,this);
		mEL_ChildShopEndTime.initData("00:00", "00:00");
		//只有总公司才有公司和门店选择，分公司只能添加门店
		mEL_ChildShopType.initLabel("店家类型","",Boolean.TRUE,this);
		if(CommonUtils.isEmpty(tmpParentId)){			
			mEL_ChildShopType.initData("", "");
		}else{
			mEL_ChildShopType.initData("门店", "门店");
			shopTypeVal = getShoyTypeVal("门店");
		}
		
		mChildShopRadio.setVisibility(View.GONE);
		
		mChildShopRadio.initLabel("复制其他门店商品数据", "",this);
		mChildShopRadio.initData("0");
		mChildShopRadio.getItemSpareLine().setVisibility(View.INVISIBLE);
					
		}
	


  


@Override
public void onItemClick(int pos) {
	// TODO Auto-generated method stub
	//mEL_RetailArea.changeData(mAreaList.get(pos), Integer.toString(pos));
	
}

@Override
public void onItemListClick(ItemEditList v) {
	// TODO Auto-generated method stub
    //int position = Integer.parseInt(String.valueOf(v.getTag()));
    Log.i("kyolee","ClickListener = =====");
 
    if(v.getId() == R.id.childShopEndTime){
		/*Time time = new Time();     
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
				mEL_ChildShopEndTime.changeData(hour+":"+min, hour+":"+min);
			}
		}, time.hour, time.minute, true).show();*/
		pushEndTime();
	}
	//开始营业时间
	else if(v.getId() == R.id.childShopStartTime){
		/*Time time = new Time();     
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
				mEL_ChildShopStartTime.changeData(hour+":"+min, hour+":"+min);
			}
		}, time.hour, time.minute, true).show();*/
		
		pushStartTime();
	}
	//如果是选择区域，显示选择区域对话框
	else if(v.getId() == R.id.childShopArea){
		selectErea();
	}else if(v.getId() == R.id.childSelectShop){
        Intent intent = new Intent(AddChildShopActivity.this,SelecCopyShopActivity.class);
        intent.putExtra(Constants.SHOP_ID,dataFromShopId);
       // intent.putExtra(Constants.PARENTID,tmpParentId);
        intent.putExtra(Constants.SHOPNAME,shopName);
        intent.putExtra(Constants.SHOPCODE,shopCode);	

        startActivityForResult(intent, SELECTSHOPRECODE);
		}
	else if(v.getId() == R.id.childShopParent){
		//如果是总店添加门店信息，才可以选择上级
			if(CommonUtils.isEmpty(tmpParentId)){
		        Intent intent = new Intent(AddChildShopActivity.this,ParentShopSelectActivity.class);
		        intent.putExtra(Constants.SHOP_ID,dataFromShopId);
		        startActivityForResult(intent, SELECTPARENTID);
			}
		}
	else if(v.getId() == R.id.childShopType){
		//如果是总店添加门店信息，才可以选择类型，分公司默认是试添加门店
		if(CommonUtils.isEmpty(tmpParentId)){
			initShopTypePopupWidnow(v.getLblVal());
		}
		//shopTypePopupWindow.showAsDropDown(v.getLblVal());
	}

}

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	//营业结束时间
//	Log.i("ShopInfo","view id = "+v.getId());
//	if(v.getId() == R.id.childShopEndTime){
//		/*Time time = new Time();     
//	    time.setToNow();             
//	    new TimePickerDialog(AddChildShopActivity.this,  new TimePickerDialog.OnTimeSetListener() {			
//			@Override
//			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//				// TODO Auto-generated method stub
//				String hour="";
//				String min ="";
//				if(hourOfDay <10){
//					hour = "0"+hourOfDay;
//				}
//				else{
//					hour = hourOfDay+"";
//				}
//				if(minute <10){
//					min = "0"+minute;
//				}
//				else{
//					min = minute+"";
//				}
//				mEL_ChildShopEndTime.changeData(hour+":"+min, hour+":"+min);
//			}
//		}, time.hour, time.minute, true).show();*/
//		pushEndTime();
//	}
//	//开始营业时间
//	else if(v.getId() == R.id.childShopStartTime){
//		/*Time time = new Time();     
//	    time.setToNow();             
//	    new TimePickerDialog(AddChildShopActivity.this,  new TimePickerDialog.OnTimeSetListener() {			
//			@Override
//			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//				// TODO Auto-generated method stub
//				String hour="";
//				String min ="";
//				if(hourOfDay <10){
//					hour = "0"+hourOfDay;
//				}
//				else
//				{
//					hour = hourOfDay+"";
//				}
//				if(minute <10){
//					min = "0"+minute;
//				}
//				else{
//					min = minute+"";
//				}
//				mEL_ChildShopStartTime.changeData(hour+":"+min, hour+":"+min);
//			}
//		}, time.hour, time.minute, true).show();*/
//		
//		pushStartTime();
//	}
//	//如果是选择区域，显示选择区域对话框
//	else if(v.getId() == R.id.childShopArea){
//		selectErea();
//	}else if(v.getId() == R.id.childSelectShop){
//        Intent intent = new Intent(AddChildShopActivity.this,SelecCopyShopActivity.class);
//        intent.putExtra(Constants.SHOP_ID,dataFromShopId);
//        //intent.putExtra(Constants.PARENTID,tmpParentId);
//        intent.putExtra(Constants.SHOPNAME,shopName);
//        intent.putExtra(Constants.SHOPCODE,shopCode);	
//
//        startActivityForResult(intent, SELECTSHOPRECODE);
//		}
//	else if(v.getId() == R.id.childShopParent){
//		
//	        Intent intent = new Intent(AddChildShopActivity.this,ParentShopSelectActivity.class);
//	        intent.putExtra(Constants.SHOP_ID,dataFromShopId);
//	        startActivityForResult(intent, SELECTPARENTID);
//		}
}

//选择门店类型
public void ClickListener(View v) {
    //int position = Integer.parseInt(String.valueOf(v.getTag()));
    ItemEditList tmp = (ItemEditList)v;
    Log.i("kyolee","ClickListener = =====");
    switch (v.getId()) {
    case R.id.childShopType:        	
    	initShopTypePopupWidnow(tmp.getLblVal());
       // shopTypePopupWindow.showAsDropDown(tmp.getLblVal());
        break;
    }
}





@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	if(SELECTSHOPRECODE ==requestCode &&  data != null){
		 tmpDataFromId =  data.getStringExtra(Constants.SHOP_ID);
		String tmpShopName = data.getStringExtra(Constants.SHOPCOPNAME);
		mEL_ChildSelectShop.changeData(tmpShopName, tmpShopName);
	}else if(SELECTPARENTID ==requestCode &&  data != null){
		 parentId =  data.getStringExtra(Constants.SHOP_ID);
		 parentName = data.getStringExtra(Constants.SHOPCOPNAME);
		mEL_ChildParentName.changeData(parentName, parentName);
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
	
	mEL_ChildShopArea.initData("请选择区域", "请选择区域");		
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
	mSelectErea.updateType(tmpSelectProvId, tmpSelectCityId, tmpSelectDistrictId);
	mSelectErea.getConfirmButton().setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
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

@Override
public void onItemRadioChange(ItemEditRadio obj) {
	// TODO Auto-generated method stub
	
	if(obj.getCurrVal().equals("0")){
		
		mEL_ChildSelectShop.setVisibility(View.GONE);
		mChildShopRadio.getItemSpareLine().setVisibility(View.INVISIBLE);
		
	}else{
		mEL_ChildSelectShop.setVisibility(View.VISIBLE);
		mChildShopRadio.getItemSpareLine().setVisibility(View.VISIBLE);
	}
	
	
}

private void pushStartTime(){
	
	if(mStartTime == null){
		mStartTime = new SelectTimeDialog(AddChildShopActivity.this,true);
	}
	mStartTime.show();
	mStartTime.getTitle().setText(AddChildShopActivity.this.getResources().getString(R.string.select_time));
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
		mEndTime = new SelectTimeDialog(AddChildShopActivity.this,true);
	}
	mEndTime.show();
	mEndTime.getTitle().setText(AddChildShopActivity.this.getResources().getString(R.string.select_time));
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

@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
}

//根据门店类型名称获取没电typeId
private Integer getShoyTypeVal(String name){
	Integer ret =0;
	
	for(int i=0;i < RetailApplication.getShopTypeList().size();i++){
		if(name.equals(RetailApplication.getShopTypeList().get(i).getName())){
			
			ret = RetailApplication.getShopTypeList().get(i).getVal();
			
			break;
		}
	}
	
	
	return ret;
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
