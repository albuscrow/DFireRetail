package com.dfire.retail.app.manage.activity.shopmanager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TimePicker;

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
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.CityVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.DistrictVo;
import com.dfire.retail.app.manage.data.ProvinceVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.Utility;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * 店家信息
 * @author Administrator
 *
 */
public class ShopInfoActivity extends  TitleActivity implements
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
	private List<DicVo> mShopTypeList;
	private List<ProvinceVo> mAdressList;
	private List<AllShopVo> mShopList;
	String shopStr;

	private ImageView mShopLogo; 
     Bitmap myBitmap; 
     private byte[] mContent; 
	
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
		
		setTitleRes(R.string.shop_info);
		change2saveMode();
		setRightBtn(R.drawable.save);
		
		//分店信息
		findView();
				
		//初始化门店信息
		initView();
		getRetialInfo();
		//httppost
		//显示进度条对话框
		getProgressDialog().setCancelable(false);
		getProgressDialog()
				.setMessage("获取店家信息");
		getProgressDialog().show();
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
		mShopLogo = (ImageView)findViewById(R.id.shop_logo);
		mRight = (ImageButton) findViewById(R.id.title_right);

	}
	
	private void initView() {
		mRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG,"save shop info");
				saveShopInfo();
			}
		});
		
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
		mSubAdapter = new EmployeePFMAdapter(ShopInfoActivity.this, mSubStoreList);		
		mSubStoreListView.setAdapter(mSubAdapter);		
		Utility.setListViewHeightBasedOnChildren(mSubStoreListView);
		
			
		}
	
 /* private void initRetailInfo(){
	    	  
	  mSubStoreList.add(new EmployeePFMItem(null, null, "二维或杭州公司", "店家代码：0100"));
	  mSubStoreList.add(new EmployeePFMItem(null, null, "二维或杭州公司", "店家代码：0101"));
  }*/
  
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	  ContentResolver resolver = getContentResolver(); 
      /** 
       * 因为两种方式都用到了startActivityForResult方法，这个方法执行完后都会执行onActivityResult方法， 
       * 所以为了区别到底选择了那个方式获取图片要进行判断，这里的requestCode跟startActivityForResult里面第二个参数对应 
       */ 
      if (requestCode == 0 && data != null) { 
          try { 
              //获得图片的uri 
              Uri originalUri = data.getData(); 
              //将图片内容解析成字节数组 
              mContent=readStream(resolver.openInputStream(Uri.parse(originalUri.toString()))); 
              //将字节数组转换为ImageView可调用的Bitmap对象 
              myBitmap = getPicFromBytes(mContent, null); 
              ////把得到的图片绑定在控件上显示 
              mShopLogo.setImageBitmap(myBitmap); 
          } catch (Exception e) { 
              e.printStackTrace();
          } 
      //将图片解析成byte[]类型    
      }else if(requestCode ==1 && data != null){ 
              try { 
                      super.onActivityResult(requestCode, resultCode, data); 
                          Bundle extras = data.getExtras(); 
                          myBitmap = (Bitmap) extras.get("data"); 
                          ByteArrayOutputStream baos = new ByteArrayOutputStream();     
                          myBitmap.compress(Bitmap.CompressFormat.JPEG , 100, baos);     
                           mContent=baos.toByteArray(); 
                      } catch (Exception e) { 
                              // TODO Auto-generated catch block 
                              e.printStackTrace(); 
                      } 
                      //把得到的图片绑定在控件上显示 

              mShopLogo.setImageBitmap(myBitmap); 
              
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
			Intent getImageByCamera= new Intent("android.media.action.IMAGE_CAPTURE");   
	        startActivityForResult(getImageByCamera, 1);
	    	if(mPw!=null && mPw.isShowing())
			{
				mPw.dismiss();
			}
			break;
		case FROM_ABLUM:
			Intent getImage = new Intent(Intent.ACTION_GET_CONTENT); 
	        getImage.addCategory(Intent.CATEGORY_OPENABLE); 
	        getImage.setType("image/jpeg"); 
	        startActivityForResult(getImage, 0);
	    	if(mPw!=null && mPw.isShowing())
			{
				mPw.dismiss();
			}
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
	      LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  	  mView = inflater.inflate(R.layout.store_info_add_menu,null, false);	
	      mPw =new PopupWindow(mView,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,true);
//	      getWindow().setAttributes(lp);
	      mPw.setBackgroundDrawable(new BitmapDrawable());// 响应返回键必须的语句。
	      mPw.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 10, 10);
	    
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
	//营业结束时间
	Log.i("ShopInfo","view id = "+v.getId());
	if(v.getId() == R.id.retailEndTime){
	    Time time = new Time();     
	    time.setToNow();             
	    new TimePickerDialog(ShopInfoActivity.this,  new TimePickerDialog.OnTimeSetListener() {			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
			}
		}, time.hour, time.minute, true).show();
	}
	//开始营业时间
	else if(v.getId() == R.id.retailStartTime){
		 Time time = new Time();     
		    time.setToNow();             
		    new TimePickerDialog(ShopInfoActivity.this,  new TimePickerDialog.OnTimeSetListener() {			
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
				}
			}, time.hour, time.minute, true).show();
	}
	//如果是选择区域，显示选择区域对话框
	else if(v.getId() == R.id.retailArea){
		
	}
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

}

private void saveShopInfo(){
	
	//显示进度条对话框
	getProgressDialog().setCancelable(false);
	getProgressDialog()
			.setMessage("保存店家信息");
	getProgressDialog().show();
	
	RequestParameter params = new RequestParameter(true);
	
	params.setUrl(Constants.SHOPSAVE);
//	mShopVo.setFile(mContent);
//	params.setParam(Constants.SHOP,mShopVo.toString());
	params.setParam("systemtype","2");
	params.setParam(Constants.OPT_TYPE,Constants.EDIT);	
	mShopVo.setProvinceId(mAdressList.get(0).getProvinceId());
	mShopVo.setCityId(mAdressList.get(0).getCityVoList().get(0).getCityId());
	mShopVo.setCountyId(mAdressList.get(0).getCityVoList().get(0).getDistrictVoList().get(0).getDistrictId());
	mShopVo.setStreetId(mAdressList.get(0).getCityVoList().get(0).getDistrictVoList().get(0).getDistrictId());
	mShopVo.setLogoId("4353543");
	mShopVo.setMemo("图片测试");
	mShopVo.setPhone1("4382472748234");
	mShopVo.setShopList(null);
	
	try {
		params.setParam(Constants.SHOP, new JSONObject(new GsonBuilder().serializeNulls().create().toJson(mShopVo)));
	} catch (JSONException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	
	AsyncHttpPost httpsave = new AsyncHttpPost(params,
    new RequestResultCallback() {
        @Override
        public void onSuccess(String str) {
        	
          	Log.i(TAG,"saveShopInfo str = "+str);
          	getProgressDialog().dismiss();
          	
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
        	
        	getProgressDialog().dismiss();
            e.printStackTrace();
            Log.e("results", "Login FaiL");
            Message msg = new Message();
            msg.what = Constants.HANDLER_FAIL;
            msg.obj = e.getMessage();
            //mLoginHandler.sendMessage(msg);
        }
    });
	httpsave.execute();
}

/**
 * 网络请求成功，更新UI信息
 */
private void updateView(){
	//mED_RetailName.initLabel("店家名称", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
	mED_RetailName.initData(mShopVo.getShopName());
	
	//mTX_RetailNO.initLabel("店家编号", "");
	mTX_RetailNO.initData(mShopVo.getCode(), "");
	
	//mEL_RetailArea.initLabel("所在区域","",Boolean.TRUE,this);
	
	//mEL_RetailArea.initData(RetailApplication.getShopVo().g, "1");
	String area = "";
	if(mShopVo.getProvinceId() != null){
		area += getProvName(mShopVo.getProvinceId());
	}
	if(mShopVo.getCityId() != null){
		area += getCityName(mShopVo.getProvinceId(), mShopVo.getCityId());
	}
	if(mShopVo.getCountyId() != null){
		area += getDistrictName(mShopVo.getProvinceId(),mShopVo.getCityId(),mShopVo.getCountyId());
	}
	
	mEL_RetailArea.initData(area, "1");		

	mED_RetailAdress.initData(mShopVo.getAddress());
	
	mED_RetailPhone.initData(mShopVo.getPhone1());
	
	mED_RetailWeixin.initData(mShopVo.getWeixin());
	
	mEL_RetailStartTime.initData(mShopVo.getStartTime(), "1");
	
	mEL_RetailEndTime.initData(mShopVo.getEndTime(), "1");
	
	if(mShopVo.getShopList().size() > 0){
		mSubStoreList.clear();
		for(int i= 0 ;i < mShopVo.getShopList().size();i++){
			mSubStoreList.add(new EmployeePFMItem(null, null, mShopVo.getShopList().get(i).getShopName(), 
					getResources().getString(R.string.shop_code)+mShopVo.getShopList().get(i).getCode()));
		}
	}
	
	mSubAdapter = new EmployeePFMAdapter(ShopInfoActivity.this, mSubStoreList);		
	mSubStoreListView.setAdapter(mSubAdapter);		
	Utility.setListViewHeightBasedOnChildren(mSubStoreListView);
	
		
	}
	

/**
 * 将byte类型的图片数据、转换成bitmap类型
 * @param bytes 图片数据信息
 * @param opts 转换选项
 * @return
 */
public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) { 
    if (bytes != null) 
        if (opts != null) 
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts); 
        else 
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length); 
    return null; 
} 

/**
 * 从输入流中读取数据、并转换成byte[]类型
 * @param inStream 相册图片的输入流
 * @return 返回byte[]类型的数据流
 * @throws Exception
 */
public static byte[] readStream(InputStream inStream) throws Exception { 
    byte[] buffer = new byte[1024]; 
    int len = -1; 
    ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
    while ((len = inStream.read(buffer)) != -1) { 
             outStream.write(buffer, 0, len); 
    } 
    byte[] data = outStream.toByteArray(); 
    outStream.close(); 
    inStream.close(); 
    return data; 

} 

/**
 * 通过省份ID，获取省份名字
 * @param provId 省份ID
 * @return 返回省份名称
 */
public String getProvName(Integer provId){
	String ret ="";
	 for(int i=0;i < mAdressList.size();i++){
		 if(mAdressList.get(i).getProvinceId().equals(provId)){
			 ret = mAdressList.get(i).getProvinceName();
			 break;
		 }
		 Log.i(TAG," mAdressList "+ i +" prvic = "+mAdressList.get(i).getProvinceName());
	 }
	return ret;
}

/**
 * 获取城市名称
 * @param provId 省份ID
 * @param cityId 城市ID
 * @return 如果省份和城市ID都正确，返回城市名称,否则返回""
 */
public String getCityName(Integer provId,Integer cityId){
	
	String ret ="";
	CityVo tmpCityVo;
	 for(int i=0;i < mAdressList.size();i++){
		 //如果找到对应的省份ID，终止循环
		 if(mAdressList.get(i).getProvinceId().equals(provId)){
			 //开始查找城市信息
			 for(int j=0;j < mAdressList.get(i).getCityVoList().size();j++){
				 tmpCityVo = mAdressList.get(i).getCityVoList().get(j);
				//如果找到对应的城市ID，终止循环
				 if(tmpCityVo.getCityId().equals(cityId)){
					 ret = tmpCityVo.getCityName();
					 break;
				 }
			 }
			 break;
		 }
		 Log.i(TAG," mAdressList "+ i +" prvic = "+mAdressList.get(i).getProvinceName());
	 }
	return ret;
}



public String getDistrictName(Integer provId,Integer cityId,Integer countyId){
	
	String ret ="";
	CityVo tmpCityVo;
	DistrictVo districtVo;
	
	 for(int i= 0 ;i < mAdressList.size();i++){
		 //如果找到对应的省份ID，终止循环
		 if(mAdressList.get(i).getProvinceId().equals(provId)){
			 //开始查找城市信息
			 for(int j = 0;j < mAdressList.get(i).getCityVoList().size();j++){
				 tmpCityVo = mAdressList.get(i).getCityVoList().get(j);
				//如果找到对应的城市ID，终止循环
				 if(tmpCityVo.getCityId().equals(cityId)){
					
					 //查询对应的区域信息
					 for(int m = 0;m < tmpCityVo.getDistrictVoList().size();m++){
						 
						 districtVo = tmpCityVo.getDistrictVoList().get(m);
						 
						 if(districtVo.getDistrictId().equals(countyId)){
							 ret = districtVo.getDistrictName();							 
							 break;
						 }
					 }
					 
					 
					 break;
				 }
			 }
			 break;
		 }
		 Log.i(TAG," mAdressList "+ i +" prvic = "+mAdressList.get(i).getProvinceName());
	 }
	return ret;
}

public String getStreetName(Integer provId,Integer cityId,Integer countyId,Integer streetId){
	
	return "";
}
  
}


