package com.dfire.retail.app.manage.activity.shopchain;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.ShopInfoAdapter;
import com.dfire.retail.app.manage.adapter.ShopInfoItem;
import com.dfire.retail.app.manage.common.ErrorMsg;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.ShopDetailBo;
import com.dfire.retail.app.manage.data.bo.ShopListBo;
import com.dfire.retail.app.manage.data.bo.ShopListReturnBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

public class SelecCopyShopActivity extends TitleActivity{
	
	static final String TAG="ShopInfo";
	private List<AllShopVo> mShopList ;
	ArrayList<ShopInfoItem> shopInfoList = new ArrayList<ShopInfoItem>();
	private ShopInfoAdapter mshopAdapter;
	private PullToRefreshListView mListView;
    private TextView	searchButton;
    private EditText editKeyWord;
    
    private int _currentRequestPage = 1;
    private  int pageSize;
    private String shopUrl ="";
    private String searchShopId = "";
    private String shopName;
    private String shopCode;
    private String parentId;
    
    private ShopVo mShopVo;
    private boolean isStartSeach = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_select_shop_ext);
		
		setTitleRes(R.string.selectShop);
		showBackbtn();
		
		shopUrl = Constants.SHOPALLSHOPLIST;
		searchShopId = getIntent().getStringExtra(Constants.SHOP_ID);
		shopCode = getIntent().getStringExtra(Constants.SHOPCODE);
		shopName= getIntent().getStringExtra(Constants.SHOPNAME);
		//parentId = getIntent().getStringExtra(Constants.PARENTID);
		
		mListView = (PullToRefreshListView) findViewById(R.id.selectshoplist);
		editKeyWord = (EditText) findViewById(R.id.input);
		searchButton = (TextView) findViewById(R.id.search);
		
		startSerachData(_currentRequestPage+"",shopUrl,searchShopId);
		mshopAdapter = new ShopInfoAdapter(SelecCopyShopActivity.this, shopInfoList);		
		mListView.setAdapter(mshopAdapter);		
		//Utility.setListViewHeightBasedOnChildren(mListView);
		new ListAddFooterItem(this, mListView.getRefreshableView());
	    mListView.setMode(Mode.BOTH);
	    mListView.setRefreshing(false);
	   // mListView.setVisibility(View.VISIBLE);
	    mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

				@Override
				public void onPullDownToRefresh(
						PullToRefreshBase<ListView> refreshView) {
					// TODO Auto-generated method stub
					Log.i(TAG,"kyolee onPullDownToRefresh = "+_currentRequestPage);
					String label = DateUtils.formatDateTime(SelecCopyShopActivity.this, System.currentTimeMillis(), 
							DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
					//_currentRequestPage = 1;//
					startSerachData(_currentRequestPage+"",shopUrl,searchShopId);
				}

				@Override
				public void onPullUpToRefresh(
						PullToRefreshBase<ListView> refreshView) {
					// TODO Auto-generated method stub
					Log.i(TAG,"kyolee onPullUpToRefresh = "+_currentRequestPage);
					
					String label = DateUtils.formatDateTime(SelecCopyShopActivity.this, System.currentTimeMillis(), 
							DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
					//_currentRequestPage = 1;//
					startSerachData(_currentRequestPage+"",shopUrl,searchShopId);
				}
	        	
			});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(Constants.SHOP_ID, shopInfoList.get(arg2-1).getShopId());
				intent.putExtra(Constants.SHOPCOPNAME,shopInfoList.get(arg2-1).getShopName());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//if(!isStartSeach){
				//	isStartSeach = true;
					_currentRequestPage =1;
					startSerachData(_currentRequestPage+"",shopUrl,searchShopId);
			//	}
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
//		param.setParam("keyWord","");
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
//	
//				// updateView();
//				 getProgressDialog().dismiss();
//
//	            Message msg = new Message();
//	            if(ju.getString(Constants.RETURN_CODE).equals(Constants.SUCCESS)){
//	            	mShopVo = (ShopVo) ju.get(Constants.SHOP, ShopVo.class); 
//	            	//如果查询成功，显示当前的
//	            	if(mShopVo  == null ){
//	            		shopInfoList.clear();
//	            		shopInfoList.add(new ShopInfoItem(mShopVo.getShopId(),
//	            				mShopVo.getShopName(), mShopVo.getParentId(), mShopVo.getCode()));
//	            		ad
//	            		
//	            	}else{
//	            		msg.what = Constants.HANDLER_FAIL;
//	 	                msg.obj = "没有找到相符合的门店信息！";
//	            	}
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
	
/**
 * 查询商店信息	
 * @param pagesize
 */
private void startSerachData(String pagesize,String url ,String shopId) {
       
          
        RequestParameter parameters = new RequestParameter(true);
        parameters.setUrl(url);
        parameters.setParam(Constants.SHOP_ID, shopId);       
        parameters.setParam(Constants.PAGE,Integer.valueOf(pagesize) );
        parameters.setParam(Constants.SHOPKEYWORD, editKeyWord.getText().toString());
        
        new AsyncHttpPost(SelecCopyShopActivity.this,parameters,ShopListReturnBo.class,true,
        	    new RequestCallback() {
			
            @Override
            public void onFail(Exception e) {
//                getProgressDialog().dismiss();
 //               ToastUtil.showShortToast(getApplicationContext(), e.toString());
                //testListData();
            }
            
            @Override
            public void onSuccess(Object str) {
                //mListView.onRefreshComplete();       
            	isStartSeach = false;
            	ShopListReturnBo bo = (ShopListReturnBo)str;
//                JsonUtil ju = new JsonUtil(str.toString());
                List<AllShopVo> tmpList ;	
                
                
               // if(ju.getString(Constants.RETURN_CODE).equals(Constants.SUCCESS)){ 
                	if( _currentRequestPage ==1){
                		//申请第一页数据时，清空list
                		shopInfoList.clear();
                		//if(CommonUtils.isEmpty(editKeyWord.getText().toString())){
                		//shopInfoList.add(new ShopInfoItem(searchShopId, shopName, parentId, SelecCopyShopActivity.this.getResources().getString(R.string.shop_code)+shopCode));
                		//}
                		tmpList =  bo.getAllShopList();
		 
		                //如果门店数量大于0，显示信息
		                if(tmpList.size() > 0){
		                	mListView.setVisibility(View.VISIBLE);
		                }
		                if(tmpList != null){
			        		for(int i =0 ; i < tmpList.size();i++){
	
			        			shopInfoList.add(new ShopInfoItem(tmpList.get(i).getShopId(),tmpList.get(i).getShopName(),
			        					tmpList.get(i).getParentId(),
			        					SelecCopyShopActivity.this.getResources().getString(R.string.shop_code)+tmpList.get(i).getCode()));
			        			mshopAdapter.notifyDataSetChanged();
			        		}
		                }
		                //请求下一页数据
                	}else{
                		tmpList = bo.getAllShopList();
                		for(int i =0 ; i < tmpList.size();i++){
                			shopInfoList.add(new ShopInfoItem(tmpList.get(i).getShopId(),tmpList.get(i).getShopName(),
		        					tmpList.get(i).getParentId(),
		        					SelecCopyShopActivity.this.getResources().getString(R.string.shop_code)+tmpList.get(i).getCode()));
                			mshopAdapter.notifyDataSetChanged();
                		}
                		 
                		
                	}
                	//
                	//Utility.setListViewHeightBasedOnChildren(mListView);
                	
	                pageSize = bo.getPageSize().intValue();
	                
	                if(_currentRequestPage <= pageSize){
	                	_currentRequestPage++;

	                }else{
	                	mListView.setMode(Mode.DISABLED);	                	
	                }
	                if(bo.getPageSize().intValue() ==0){
	                	 shopInfoList.clear();
	             		 mShopList.clear();
	                }	
	                mListView.onRefreshComplete();       
	            	isStartSeach = false; 	

//                }else{
//
//                	 msg.arg1 = Constants.HANDLER_FAIL;
//	        		 msg.obj = ju.getExceptionCode();
//	        		
//                }
//               //处理返回结果 
//                mHandler.sendMessage(msg);
//                
            }
		}).execute();
    }
	
	/**
	 * 处理返回的结果
	 */
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
//			getProgressDialog().dismiss();
			
			switch (msg.what) {
				case Constants.HANDLER_SUCESS:
					
					ToastUtil.showLongToast(SelecCopyShopActivity.this,msg.obj.toString());	
					//AddUserInfoActivity.this.finish();
					
				break;
				
				case Constants.HANDLER_FAIL:	
                        ToastUtil.showLongToast(SelecCopyShopActivity.this,ErrorMsg.getErrorMsg(msg.obj.toString()));
				 
					break;
				case Constants.HANDLER_ERROR:		
                        ToastUtil.showLongToast(SelecCopyShopActivity.this,
                                msg.obj.toString());
                 break;
				case  Constants.HANDLER_SEARCH:
					
//					startSerachData(msg.obj.toString(),shopUrl,searchShopId);
					
					break;	
					
			}
		}
	};

	/**
	 * 搜索数据
	 */
	protected void searchShopBycode(String shopCode){
		
		//显示进度条对话框
	
		RequestParameter params = new RequestParameter(true);
		
		params.setUrl(Constants.SHOPDETAILBYCODE);
		params.setParam(Constants.SEARCHSHOPCODE,shopCode);	
	   	
		AsyncHttpPost httpsearch = new AsyncHttpPost(SelecCopyShopActivity.this,params,ShopDetailBo.class,true,
	    new RequestCallback() {
	        @Override
	        public void onSuccess(Object str) {
	        	
	        	ShopDetailBo bo = (ShopDetailBo) str;
//	          	Message msg = new Message();
//	        	JsonUtil ju = new JsonUtil(str.toString());
//	        	if(ju.getString(Constants.RETURN_CODE).equals(Constants.SUCCESS)){
	        	mShopVo = bo.getShop();  	        		        				

	            	//如果查询成功，显示当前的
	            	if(mShopVo  == null ){
	            		shopInfoList.clear();
	            		shopInfoList.add(new ShopInfoItem(mShopVo.getShopId(),
	            				mShopVo.getShopName(), mShopVo.getParentId(), mShopVo.getCode()));
	            		mshopAdapter.notifyDataSetChanged();
	            	}

	        }
	        @Override
	        public void onFail(Exception e) {
	        	
//	        	getProgressDialog().dismiss();
	        }
	    });
		httpsearch.execute();
	}

	
	

}
