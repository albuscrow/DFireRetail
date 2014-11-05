package com.dfire.retail.app.manage.activity.shopmanager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.login.LoginActivity;
import com.dfire.retail.app.manage.activity.retailmanager.RetailBGdetailActivity;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.common.ErrorMsg;
import com.dfire.retail.app.manage.data.AddShopVo;
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
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ShopInfoBaseActivity extends TitleActivity{
	
	static final String TAG="ShopInfoBaseActivity";
	private ShopVo mShopVo;
	private List<DicVo> mShopTypeList;
	private List<ProvinceVo> mAdressList;
	private List<AllShopVo> mShopList;
	
	public ImageButton change2saveFinishMode() {
		if (mBack == null) {
			mBack = (ImageButton) findViewById(R.id.title_back);
		}
		mBack.setVisibility(View.VISIBLE);
		mLeft = (ImageButton) findViewById(R.id.title_left);
		mLeft.setImageResource(R.drawable.cancel);
		mLeft.setVisibility(View.GONE);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShopInfoBaseActivity.this.finish();
			}
		});

		mRight = (ImageButton) findViewById(R.id.title_right);
		mRight.setVisibility(View.GONE);
		mRight.setImageResource(R.drawable.save);
		return mRight;
	}
	
	
	public ImageButton change2saveMode() {
		if (mBack == null) {
			mBack = (ImageButton) findViewById(R.id.title_back);
		}
		mBack.setVisibility(View.GONE);
		mLeft = (ImageButton) findViewById(R.id.title_left);
		mLeft.setImageResource(R.drawable.cancel);
		mLeft.setVisibility(View.VISIBLE);
		mLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mBack.setVisibility(View.VISIBLE);
				mLeft.setVisibility(View.GONE);
				mRight.setVisibility(View.GONE);
				mBack.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShopInfoBaseActivity.this.finish();
					}
				});
				
			}
		});

		mRight = (ImageButton) findViewById(R.id.title_right);
		mRight.setVisibility(View.GONE);
		mRight.setImageResource(R.drawable.save);
		setRightBtn(R.drawable.save);
		return mRight;
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
	public String getProvName(Integer provId,List<ProvinceVo> mAdressList){
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
	public String getCityName(Integer provId,Integer cityId,List<ProvinceVo> mAdressList){
		
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


	/**
	 * 获取城市名称
	 * @param provId 省份ID
	 * @param cityId 城市ID
	 * @return 如果省份和城市ID都正确，返回城市名称,否则返回""
	 */
	public String getShopTypeName(List<DicVo> shopTypeList,String shopType){
		
		String ret ="";
		 for(int i=0;i < shopTypeList.size();i++){
			 //如果找到对应的省份ID，终止循环
			 if(shopTypeList.get(i).getVal().equals(Integer.valueOf(shopType))){
				 //开始查找城市信息
				 ret = shopTypeList.get(i).getName();
				 break;
				 }
				 
			
		 }
		return ret;
	}

	public String getDistrictName(Integer provId,Integer cityId,Integer countyId,List<ProvinceVo> mAdressList){
		
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
							 Log.i("districtVo","districtVo = "+ districtVo.getDistrictName());
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
	  
	/**
	 * 搜索数据
	 */
	protected void searchShopBycode(String shopCode){
		
		//显示进度条对话框
		getProgressDialog().setCancelable(false);
		getProgressDialog()
				.setMessage("搜索店家信息");
		getProgressDialog().show();
		
		RequestParameter params = new RequestParameter(true);
		
		params.setUrl(Constants.SHOPDETAILBYCODE);
		params.setParam(Constants.SEARCHSHOPCODE,shopCode);	
	   	
		AsyncHttpPost httpsearch = new AsyncHttpPost(params,
	    new RequestResultCallback() {
	        @Override
	        public void onSuccess(String str) {
	        	
	          	getProgressDialog().dismiss();
	          	
	        	JsonUtil ju = new JsonUtil(str);
	        	mShopVo = (ShopVo) ju.get(Constants.SHOP, ShopVo.class);  
	        	
	        	Log.i(TAG,"shop getAddress = "+mShopVo.getAddress());
	        	
				 mShopTypeList =  (List<DicVo>) ju.get(Constants.SHOPTYPE_LIST, 
						new TypeToken<List<DicVo>>(){}.getType());				 				 				 
				 mAdressList =  (List<ProvinceVo>) ju.get(Constants.ADDRESS_LIST, 
							new TypeToken<List<ProvinceVo>>(){}.getType());				 				 
				 mShopList =  (List<AllShopVo>) ju.get(Constants.SHOP_LIST, 
							new TypeToken<List<AllShopVo>>(){}.getType());				
				 
	            Message msg = new Message();
	            if(ju.getString(Constants.RETURN_CODE).equals(Constants.SUCCESS)){
	            		updateView();
	            }else{
	            	msg.what = Constants.HANDLER_FAIL	            			;
	            	msg.obj = ju.getReturnCode();
	            }

	           mShopHandler.sendMessage(msg);
	        }
	        @Override
	        public void onFail(Exception e) {
	        	
	        	getProgressDialog().dismiss();
	            e.printStackTrace();
	            Message msg = new Message();
	            msg.what = Constants.HANDLER_ERROR;
	            msg.obj = "该店家信息不存在";
	            mShopHandler.sendMessage(msg);
	        }
	    });
		httpsearch.execute();
	}

	public void updateView(){
		
	}
	
	/**
	 * 处理返回的结果，如果成功解析网络返回的json数据
	 */
	public Handler mShopHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			getProgressDialog().dismiss();
			
			switch (msg.what) {
				case Constants.HANDLER_SUCESS:
					   if (msg.obj != null) {
							ToastUtil.showShortToast(getApplicationContext(),
							        msg.obj.toString());
					    } 
				
				break;
				
				case Constants.HANDLER_FAIL:
				    if (msg.obj != null) {
						ToastUtil.showShortToast(getApplicationContext(),
								   ErrorMsg.getErrorMsg(msg.obj.toString()));
				    } 
				 
					break;
				case Constants.HANDLER_ERROR:
				    if (msg.obj != null) {
						ToastUtil.showShortToast(getApplicationContext(),
					        msg.obj.toString());
				    } 
					break;
			}
		}
	};
	
	/**
	 * 保存修改数据
	 */
	public void saveShopInfo(String operate,ShopVo shopVo){
		
		//显示进度条对话框
		getProgressDialog().setCancelable(false);
		getProgressDialog()
				.setMessage("保存店家信息");
		getProgressDialog().show();
		
		RequestParameter params = new RequestParameter(true);
		
		params.setUrl(Constants.SHOPSAVE);
//		mShopVo.setFile(mContent);
//		params.setParam(Constants.SHOP,mShopVo.toString());
		params.setParam(Constants.OPT_TYPE,operate);	
	   	
		try {
			params.setParam(Constants.SHOP, new JSONObject(new GsonBuilder().serializeNulls().create().toJson(shopVo)));
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
	            if(ju.getString(Constants.RETURN_CODE).equals(Constants.SUCCESS)){
	            	msg.what = Constants.HANDLER_SUCESS;
	                msg.obj = "保存成功！";
	                change2saveFinishMode();
	            }else{
	            	msg.what = Constants.HANDLER_FAIL;
	            	msg.obj = ju.getExceptionCode();
	            }

	            mShopHandler.sendMessage(msg);
	        }
	        @Override
	        public void onFail(Exception e) {
	        	
	        	getProgressDialog().dismiss();
	            e.printStackTrace();
	            Log.e("results", "Login FaiL");
	            Message msg = new Message();
	            msg.what = Constants.HANDLER_ERROR;
	            msg.obj = e.getMessage();
	            mShopHandler.sendMessage(msg);
	        }
	    });
		httpsave.execute();
	}
	
	
	
	
}