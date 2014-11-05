package com.dfire.retail.app.manage.activity.employeemanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.retailmanager.UserInfoInit;
import com.dfire.retail.app.manage.adapter.EmployeeInfoAdapter;
import com.dfire.retail.app.manage.common.ErrorMsg;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.UserVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.util.Utility;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.google.gson.reflect.TypeToken;

public class EmployeeInfoActivity extends TitleActivity implements
        RequestResultCallback, OnClickListener, IItemListListener, IOnItemSelectListener {

    private static final String TAG = "EmployeeInfoActivity";
    
    private ItemEditList _shopChoiceButton;
    private ItemEditList  _roleChoiceButton;
    
    private Button	searchButton;
    private EditText editKeyWord;
    
    private Button      mBTAddUserInfo;
    
    private List<RoleVo> mRoleList;
    private List<AllShopVo> mShopList;
    private List<DicVo> mSexList;
    private List<DicVo> mIdentityTypeList;
    
	private String tmpDataFromId;
	private static final int SELECTSHOPRECODE =2;
	public String dataFromShopId;
	private String roleId;

    List<UserVo> mList = new ArrayList<UserVo>();
    private ListView mListView;
    private EmployeeInfoAdapter mAdapter;

    private SpinerPopWindow _shopPopupWindow;
    private SpinerPopWindow _rolePopupWindow;

    private int _shopChoicePosition = 0;
    private int _roleChoicePosition = 0;
    private ArrayList<String> roleNameList = new ArrayList<String>();
  
    
    /**
     * 标记List当前显示的页面，当向下滑动时，请求下一页.
     */
    private int _currentRequestPage = 1;
    private  int pageSize;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_employee_info);
        
        mListView = (ListView) findViewById(R.id.attendance_list);
        mListView.setVisibility(View.GONE);
        initList();
        //
        editKeyWord = (EditText) findViewById(R.id.edit_user_name);
        
        searchButton = (Button) findViewById(R.id.bt_search_userinfo);
        
        searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startSerachData(String.valueOf(_currentRequestPage));
			}
		});
        
        //理论上可以直接获取到员工所在的店铺和角色，然后直接设置
        _shopChoiceButton = (ItemEditList) findViewById(R.id.shop_choice);
        _shopChoiceButton.getImg().setImageResource(R.drawable.arrow_right);;
        _shopChoiceButton.initLabel("选择门店","",Boolean.TRUE,this);
        _shopChoiceButton.initData(RetailApplication.getShopVo().getShopName(), 
        RetailApplication.getShopVo().getShopName());
        _shopChoiceButton.setOnClickListener(this);
   
                    
        _roleChoiceButton = (ItemEditList) findViewById(R.id.role_choice);
        _roleChoiceButton.initLabel("角色选择","",Boolean.TRUE,this);
        _roleChoiceButton.initData(RetailApplication.getmUserInfo().getRoleName(), 
        RetailApplication.getmUserInfo().getRoleName());
        
       // List<RoleVo> roleList = RetailApplication.getRoleList();
       // _roleChoiceButton.setOnClickListener(this);
        
        mBTAddUserInfo = (Button)findViewById(R.id.member_info_add);
        mBTAddUserInfo.setOnClickListener(this);
        
        setTitleRes(R.string.employee_info);
        showBackbtn();
        getUserInfoInit();
        //默认店名Id
        tmpDataFromId = RetailApplication.getShopVo().getShopId();
        roleId = RetailApplication.getmUserInfo().getRoleId();
        //startRequestListData();
    }
    
    
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mListView.isShown()){
			mList.clear();
			Log.i(TAG,"onResume data info kyolee");
			_currentRequestPage = 1;
			startSerachData(_currentRequestPage+"");
		}
	}

	/**
	 * 初始化员工角色控件，绑定spinner
	 * @param v
	 */
    private void initRolePopupWidnow(final TextView v) {
        if (_rolePopupWindow == null) {
            _rolePopupWindow = new SpinerPopWindow(this);
            _rolePopupWindow.setItemListener(new IOnItemSelectListener() {

                @Override
                public void onItemClick(int position) {
                   // _roleChoicePosition = position;
                    _shopChoiceButton.initData(mRoleList.get(position).getRoleName(), mRoleList.get(position).getRoleName()); //(mRoleList.get(position).getRoleName());
                    roleId = mRoleList.get(position).getRoleId();
                    //startSerachData();
                }
            });
            _rolePopupWindow.refreshData(roleNameList, _roleChoicePosition);
            _rolePopupWindow.setWidth((int)(v.getPaint().measureText(UserInfoInit.getInstance().getRoleListMax()) * 1.4f));
        }
    }
    
 
    public void ClickListener(View v) {
        //int position = Integer.parseInt(String.valueOf(v.getTag()));
        ItemEditList tmp = (ItemEditList)v;
        Log.i("kyolee","ClickListener = =====");
        switch (v.getId()) {
        case R.id.role_choice:        	
            initRolePopupWidnow(tmp.getLblVal());
            _rolePopupWindow.showAsDropDown(tmp.getLblVal());
            break;
        }
    }
    
    
	/**
	 * 获取初始化员工信息
	 */
	public void getUserInfoInit(){
		
		//显示进度条对话框
		getProgressDialog().setCancelable(false);
		getProgressDialog()
				.setMessage("保存用户信息");
		getProgressDialog().show();		
		RequestParameter params = new RequestParameter(true);		
		params.setUrl(Constants.EMPLOYEE_INFO_INIT);
				   	
		
		AsyncHttpPost httpinit = new AsyncHttpPost(params,new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				// TODO Auto-generated method stub
				JsonUtil ju = new JsonUtil(str);
				getProgressDialog().dismiss();
		        Message msg = new Message();
		        if(ju.getString(Constants.RETURN_CODE).equals(Constants.SUCCESS)){
		        	//msg.what = Constants.HANDLER_SUCESS;
		            
		        	// 初始化角色信息
		        	mRoleList = (List<RoleVo>) ju.get(Constants.ROLE_LIST, 
							new TypeToken<List<RoleVo>>(){}.getType());		        	
		        	mShopList = (List<AllShopVo>) ju.get(Constants.SHOP_LIST, 
							new TypeToken<List<AllShopVo>>(){}.getType());
		        	mSexList = (List<DicVo>) ju.get(Constants.SEX_LIST, 
							new TypeToken<List<DicVo>>(){}.getType());
		        	mIdentityTypeList = (List<DicVo>) ju.get(Constants.IDENTITYTYPE_LIST, 
							new TypeToken<List<DicVo>>(){}.getType());
		        	roleNameList.clear();
		        	for(int i = 0;i < mRoleList.size();i++){
		    			roleNameList.add(mRoleList.get(i).getRoleName());
		    		}
		        	
		        	RetailApplication.setRoleList(mRoleList);
		        	RetailApplication.setShopList(mShopList);
		        	RetailApplication.setSexList(mSexList);
		        	RetailApplication.setIdentityTypeList(mIdentityTypeList);
		            
		        }else{
                	 msg.arg1 = Constants.HANDLER_FAIL;
	        		 msg.obj = ju.getExceptionCode();
	        		 mEmployeeHandler.sendMessage(msg);
		        }

			}
			
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
		      	getProgressDialog().dismiss();
		        e.printStackTrace();
		        Log.e("results", "Login FaiL");
		        Message msg = new Message();
		        msg.what = Constants.HANDLER_FAIL;
		        msg.obj = e.getMessage();
			}
		});
		httpinit.execute();
	}    
    
    
    /**
     * 网络请求，显示员工列表.
     * 根据操作(viewTag) 和 选中位置(position)决定
     * 
     */
    private void startSerachData(String pagesize ) {
//        List<ShopVo> shopList = UserInfoInit.getInstance().getShopList();
//        List<RoleVo> roleList = UserInfoInit.getInstance().getRoleList();
//        if (shopList == null || shopList.size() <= _shopChoicePosition
//                || roleList == null || roleList.size() <= _roleChoicePosition) {
//            return;//无数据，直接结束
//        }
        
        getProgressDialog().setCancelable(false);
        getProgressDialog().setMessage("请求员工信息");
        getProgressDialog().show();
        
        RequestParameter parameters = new RequestParameter(true);
        parameters.setUrl(Constants.EMPLOYEE_INFO_LIST);
        parameters.setParam(Constants.SHOP_ID, tmpDataFromId);
        parameters.setParam(Constants.ROLEID, roleId);
        parameters.setParam(Constants.PAGE,pagesize );
        parameters.setParam(Constants.SHOPKEYWORD, editKeyWord.getText());
        
        new AsyncHttpPost(parameters, new RequestResultCallback() {
			
            @Override
            public void onFail(Exception e) {
                getProgressDialog().dismiss();
                ToastUtil.showShortToast(getApplicationContext(), e.toString());
                //testListData();
            }
            
            @Override
            public void onSuccess(String str) {
            	//if(_currentRequestPage == pageSize){
            	Message msg = new Message();
            		getProgressDialog().dismiss();
            	//}
                JsonUtil ju = new JsonUtil(str);
                List<UserVo> tmpList ;
                if(ju.getString(Constants.RETURN_CODE).equals(Constants.SUCCESS)){ 
                	if( _currentRequestPage ==1){
                		//申请第一页数据时，清空list
                		mList.clear();
              		
                		tmpList =  (List<UserVo>) ju.get(Constants.USER_LIST, 
		        		new TypeToken<List<UserVo>>(){}.getType());
		                initListShopAndRoleData();
		                //如果员工数量大于0，显示信息
		                if(tmpList.size() > 0){
		                	mListView.setVisibility(View.VISIBLE);
		                	
		                //如果员工人数为0	
		                }else{
		                	mListView.setVisibility(View.GONE);
		                	msg.arg1 = Constants.HANDLER_SUCESS;
		                	msg.obj = "没有符合要求的记录";
		                }
		        		for(int i =0 ; i < tmpList.size();i++){
                			mList.add(tmpList.get(i));
                		}
		                //请求下一页数据
                	}else{
                		 tmpList = (List<UserVo>) ju.get(Constants.USER_LIST, 
        		        		new TypeToken<List<UserVo>>(){}.getType());
                		for(int i =0 ; i < tmpList.size();i++){
                			mList.add(tmpList.get(i));
                		}
                		 
                		
                	}
                	//刷星list的长度
                	Utility.setListViewHeightBasedOnChildren(mListView);
                	
	                pageSize = ju.getInt(Constants.PAGE_SIZE);
	                if(_currentRequestPage < pageSize){
	                	_currentRequestPage++;

	                	msg.arg1 = Constants.HANDLER_SEARCH;
	                	msg.obj = _currentRequestPage;
	                }	                	

                }else{
                	 msg.arg1 = Constants.HANDLER_FAIL;
	        		 msg.obj = ju.getExceptionCode();
	        		
                }
                mEmployeeHandler.sendMessage(msg); 
            }
		}).execute();
    }

    @Override
    public void onFail(Exception e) {
        getProgressDialog().dismiss();
        ToastUtil.showShortToast(getApplicationContext(), e.toString());
        //testListData();
    }
    
    @Override
    public void onSuccess(String str) {
        getProgressDialog().dismiss();
        JsonUtil ju = new JsonUtil(str);
            	
    	
        mList =  (List<UserVo>) ju.get(Constants.USER_LIST, 
				new TypeToken<List<UserVo>>(){}.getType());
        pageSize = ju.getInt(Constants.PAGE_SIZE);
       
        
        initListShopAndRoleData();
        //如果员工数量大于0，显示信息
        if(mList.size() > 0){
        	mListView.setVisibility(View.VISIBLE);
        	initList();
        //如果员工人数为0	
        }else{
        	mListView.setVisibility(View.GONE);
        	
        }
        
    }

    /**
     * 初始化员工管理查询列表的角色数据及店家数据
     */
    private void initListShopAndRoleData() {
    	
    }
    
    private void initList() {
        mAdapter = new EmployeeInfoAdapter(EmployeeInfoActivity.this, mList);
        mListView.setAdapter(mAdapter);
        Utility.setListViewHeightBasedOnChildren(mListView);
        
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                    long arg3) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("data", mList.get(position));
                Intent intent = new Intent(EmployeeInfoActivity.this,
                        ShowUserDetailInfoActivity.class);
                intent.putExtra(Constants.USERID,mList.get(position).getUserId());
                startActivityForResult(intent, position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        if (_shopPopupWindow != null) {
            _shopPopupWindow.dismiss();
            _shopPopupWindow = null;
        }

        if (_rolePopupWindow != null) {
            _rolePopupWindow.dismiss();
            _rolePopupWindow = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
		
    	if(data !=null){
			 tmpDataFromId =  data.getStringExtra(Constants.SHOP_ID);
			String tmpShopName = data.getStringExtra(Constants.SHOPCOPNAME);
			_shopChoiceButton.initData(tmpShopName, tmpShopName);
    	}
    }
    
    /**
     * 更新服务端用户数据
     */
    private void updateUserVo() {
        
    }
    


	@Override
	public void onItemClick(int pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemListClick(ItemEditList obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		if(v.getId() == R.id.member_info_add) {			
//			startActivity(new Intent(EmployeeInfoActivity.this,AddUserInfoActivity.class));		
//		}
		 switch (v.getId()) {
	        case R.id.shop_choice:
	        	
	        	 Intent intent = new Intent(EmployeeInfoActivity.this,UserShopSelectActivity.class);
	           
	             startActivityForResult(intent, SELECTSHOPRECODE);
	        	
	            //initShopPopupWindow((TextView)v);
	            //_shopPopupWindow.showAsDropDown(v);
	            break;

	        //case R.id.role_choice:
	        //    initRolePopupWidnow((TextView)((ItemEditList)v).getLblVal());
	        //    _rolePopupWindow.showAsDropDown(v);
	        //    break;
	            
	        case R.id.member_info_add://member_info_add
	        	startActivity(new Intent(EmployeeInfoActivity.this,AddUserInfoActivity.class));
	            break;
	        }
		
	}
	
	
	
	/**
	 * 处理返回的结果，如果成功解析网络返回的json数据
	 */
	Handler mEmployeeHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			getProgressDialog().dismiss();
			
			switch (msg.what) {
				case Constants.HANDLER_SUCESS:
					
				
					//startActivity(new Intent(LoginActivity.this, RetailBGdetailActivity.class));
				break;
				
				case Constants.HANDLER_FAIL:	
                        ToastUtil.showLongToast(getApplicationContext(),
                                ErrorMsg.getErrorMsg(msg.obj.toString()));
			
					break;
				case Constants.HANDLER_ERROR:		
                        ToastUtil.showLongToast(getApplicationContext(),
                                "网络异常");
                    //测试时不管成功或失败

					break;
					
				case  Constants.HANDLER_SEARCH:
					
					startSerachData(msg.obj.toString());
					
					break;
			}
		}
	};
	


	
}
