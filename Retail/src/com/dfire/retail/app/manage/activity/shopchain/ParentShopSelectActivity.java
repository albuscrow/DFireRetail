package com.dfire.retail.app.manage.activity.shopchain;

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
import com.dfire.retail.app.manage.adapter.ShopInfoAdapter;
import com.dfire.retail.app.manage.adapter.ShopInfoItem;
import com.dfire.retail.app.manage.common.ErrorMsg;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.util.Utility;
import com.google.gson.reflect.TypeToken;

public class ParentShopSelectActivity extends ShopInfoBaseActivity{
	
	static final String TAG="ShopInfo";
	private List<AllShopVo> mShopList ;
	ArrayList<ShopInfoItem> shopInfoList = new ArrayList<ShopInfoItem>();
	private ShopInfoAdapter mshopAdapter;
	private ListView mListView;
    private TextView	searchButton;
    private EditText editKeyWord;
    
    private int _currentRequestPage = 1;
    private  int pageSize;
    private String shopUrl ="";
    private String shopId = "";
    
    private RelativeLayout search_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		List<AllShopVo> tmpList ;
		
		setContentView(R.layout.activity_select_shop);
		
		setTitleRes(R.string.selectShop);
		showBackbtn();
		
		shopUrl = Constants.COMPANY_ALL_SHOP_LIST_URL;
		shopId = getIntent().getStringExtra(Constants.SHOP_ID);
		search_layout = (RelativeLayout)findViewById(R.id.search_layout);		
		mListView = (ListView) findViewById(R.id.selectshoplist);
		editKeyWord = (EditText) findViewById(R.id.input);
		searchButton = (TextView) findViewById(R.id.search);
		search_layout.setVisibility(View.GONE);
		//startSerachData(_currentRequestPage+"",shopUrl,shopId);
		shopInfoList.clear();		
		tmpList = RetailApplication.getCompanyShopList();		
		if(tmpList!=null && tmpList.size() > 0){
			for(int i=0;i <tmpList.size();i++){
			shopInfoList.add(new ShopInfoItem(tmpList.get(i).getShopId(),tmpList.get(i).getShopName(),
					tmpList.get(i).getParentId(),
					getResources().getString(R.string.shop_code)+tmpList.get(i).getCode()));
			}
		}
		mshopAdapter = new ShopInfoAdapter(ParentShopSelectActivity.this, shopInfoList);		
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
	
//	/*
//	 * 通过网络请求，获取门店信息
//	 */
//	private void getRetialInfo(){
//		//传递请求参数
//		RequestParameter param = new RequestParameter(true);
//		param.setUrl(Constants.SHOPALLSHOPLIST);
//		//param.setUrl(Constants.SHOPDETAILBYCODE);
//		param.setParam(Constants.SHOP_ID,"");
//		param.setParam("keyWord","公司");
//		param.setParam(Constants.PAGE,"1");
//		
//		AsyncHttpPost httppost = new AsyncHttpPost(param,
//	    new RequestResultCallback() {
//	        @Override
//	        public void onSuccess(String str) {
//	        	
//	        //	Log.i(TAG,"STR = "+str.length());
//	        	
//	        	JsonUtil ju = new JsonUtil(str);
//	        	
//	       	 mShopList =  (List<AllShopVo>) ju.get(Constants.SHOP_LIST, 
//						new TypeToken<List<AllShopVo>>(){}.getType());
//				// updateView();
//				 getProgressDialog().dismiss();
//
//	            Message msg = new Message();
//	            if(ju.getString(Constants.RETURN_CODE).equals(Constants.SUCCESS)){
//	            	msg.what = Constants.HANDLER_SUCESS;
//	                msg.obj = "查询成功！";
//	            }else{
//	            	msg.what = Constants.HANDLER_FAIL;
//	            	msg.obj = ju.getExceptionCode();
//	            }
//	            mHandler.sendMessage(msg);
//	        
//
//	           // mLoginHandler.sendMessage(msg);
//	        }
//	        @Override
//	        public void onFail(Exception e) {
//	            e.printStackTrace();
//	            getProgressDialog().dismiss();
//	            Log.e("results", "Login FaiL");
//	            Message msg = new Message();
//	            msg.what = Constants.HANDLER_FAIL;
//	            msg.obj = e.getMessage();
//	            
//	            mHandler.sendMessage(msg);
//	        }
//	    });
//		
//
//	}
//	
/**
 * 查询商店信息	
 * @param pagesize
 */
//private void startSerachData(String pagesize,String url ,String shopId) {
//       
//        getProgressDialog().setCancelable(false);
//        getProgressDialog().setMessage("查询店家信息");
//        getProgressDialog().show();
//        
//        RequestParameter parameters = new RequestParameter(true);
//        parameters.setUrl(url);
//        parameters.setParam(Constants.SHOP_ID, shopId);       
//        parameters.setParam("currentPage",pagesize );
//        parameters.setParam("keyword", editKeyWord.getText()+"公司");
//        
//        new AsyncHttpPost(parameters, new RequestResultCallback() {
//			
//            @Override
//            public void onFail(Exception e) {
//                getProgressDialog().dismiss();
//                ToastUtil.showShortToast(getApplicationContext(), e.toString());
//                //testListData();
//            }
//            
//            @Override
//            public void onSuccess(String str) {
//            	//if(_currentRequestPage == pageSize){
//            		getProgressDialog().dismiss();
//            		Message msg = new Message();
//                JsonUtil ju = new JsonUtil(str);
//                List<AllShopVo> tmpList ;
//                if(ju.getString(Constants.RETURN_CODE).equals(Constants.SUCCESS)){ 
//                	if( _currentRequestPage ==1){
//                		//申请第一页数据时，清空list
//                		shopInfoList.clear();
//              		
//                		tmpList =  (List<AllShopVo>) ju.get(Constants.ALL_SHOP_LIST ,
//		        		new TypeToken<List<AllShopVo>>(){}.getType());
//		                //如果员工数量大于0，显示信息
//		                if(tmpList.size() > 0){
//		                	mListView.setVisibility(View.VISIBLE);
//		                	
//		                //如果员工人数为0	
//		                }else{
//		                	//mListView.setVisibility(View.GONE);
//		                	//msg.arg1 = Constants.HANDLER_SUCESS;
//		                	//msg.obj = "没有符合要求的记录";
//		                	tmpList = RetailApplication.getShopList();
//		                }
//		                if(tmpList != null){
//			        		for(int i =0 ; i < tmpList.size();i++){
//			        			
//			        			shopInfoList.add(new ShopInfoItem(tmpList.get(i).getShopId(),tmpList.get(i).getShopName(),
//			        					tmpList.get(i).getParentId(),
//			        					getResources().getString(R.string.shop_code)+tmpList.get(i).getCode()));
//	                		}
//		                }
//		                //请求下一页数据
//                	}else{
//                		tmpList =  (List<AllShopVo>) ju.get(Constants.ALL_SHOP_LIST ,
//        		        		new TypeToken<List<AllShopVo>>(){}.getType());
//                		for(int i =0 ; i < tmpList.size();i++){
//                			shopInfoList.add(new ShopInfoItem(tmpList.get(i).getShopId(),tmpList.get(i).getShopName(),
//		        					tmpList.get(i).getParentId(),
//		        					getResources().getString(R.string.shop_code)+tmpList.get(i).getCode()));
//                		}
//                		 
//                		
//                	}
//                	//刷星list的长度
//                	Utility.setListViewHeightBasedOnChildren(mListView);
//                	
//	                pageSize = ju.getInt(Constants.PAGE_SIZE);
//	                
//	                if(_currentRequestPage < pageSize){
//	                	_currentRequestPage++;
//
//	                	msg.arg1 = Constants.HANDLER_SEARCH;
//	                	msg.obj = _currentRequestPage;
//	                }
//	                	
//
//                }else{
//
//                	 msg.arg1 = Constants.HANDLER_FAIL;
//	        		 msg.obj = ju.getExceptionCode();
//	        		
//                }
//               //处理返回结果 
//                mHandler.sendMessage(msg);
//                
//            }
//		}).execute();
//    }
	
	/**
	 * 处理返回的结果
	 */
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
//			getProgressDialog().dismiss();
			
			switch (msg.what) {
				case Constants.HANDLER_SUCESS:
					
					ToastUtil.showLongToast(ParentShopSelectActivity.this,msg.obj.toString());	
					//AddUserInfoActivity.this.finish();
					
				break;
				
				case Constants.HANDLER_FAIL:	
                        ToastUtil.showLongToast(ParentShopSelectActivity.this,ErrorMsg.getErrorMsg(msg.obj.toString()));
				 
					break;
				case Constants.HANDLER_ERROR:		
                        ToastUtil.showLongToast(ParentShopSelectActivity.this,
                                msg.obj.toString());
                 break;
//				case  Constants.HANDLER_SEARCH:
//					
//					startSerachData(msg.obj.toString(),shopUrl,shopId);
//					
//					break;	
					
			}
		}
	};


}

