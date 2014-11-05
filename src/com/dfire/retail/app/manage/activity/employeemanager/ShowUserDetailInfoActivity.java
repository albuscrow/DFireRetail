package com.dfire.retail.app.manage.activity.employeemanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.ItemPortraitImage;
import com.dfire.retail.app.common.item.listener.IItemIsChangeListener;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.common.item.listener.IItemTextChangeListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.CommonUtils;
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
import com.dfire.retail.app.manage.util.MD5;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.google.gson.GsonBuilder;

public class ShowUserDetailInfoActivity  extends TitleActivity implements OnClickListener, IItemListListener, IOnItemSelectListener ,IItemIsChangeListener,IItemTextChangeListener{

    private static final String TAG = "ShowUserDetailInfoActivity";
    
    /**
     * 密码长度不小于6为，员工工号最大不超过6位数字
     */
    ItemEditText mEDName,mEDAccount,mEDPassword,mEDAdress,
    			mEDIdentityNo,mEDUserStaffId,mEDUserPhone;
    
    ItemEditList mELUserRole, mELUserShop,mELUserInDate,mELUserSex,mELUserBirthday,mELUserIdentityType;
    ItemPortraitImage mIMGPortrait;
    UserVo mUserVo;
    
    private String userId;
    private String tmpDataFromId;
    private static final int SELECTSHOPRECODE =2;
    private SpinerPopWindow _shopPopupWindow;
    private SpinerPopWindow _rolePopupWindow;
    private SpinerPopWindow _sexPopupWindow;
    private SpinerPopWindow _identityPopupWindow;
    private DatePickerDialog _inDateDialog;
    private DatePickerDialog _birthDayDialog;
    
    private List<RoleVo> mRoleList;
    private List<AllShopVo> mShopList;
    private List<DicVo> mSexList;
    private List<DicVo> mIdentityTypeList;
    
    private ArrayList<String> roleNameList = new ArrayList<String>();
    private ArrayList<String> shopNameList = new ArrayList<String>();
    private ArrayList<String> sexNameList = new ArrayList<String>();
    private ArrayList<String> indentityNameList = new ArrayList<String>();
    
    private String tmpRoleId;
    private Integer tmpIdentityTypeId;
    private String tmpShopId;
    private Integer tmpSexId;// 性别
    private Button userDelete;//删除按钮
    
    //标记当前员工资料是否有被修改而未被保存
    private static HashMap<String, Boolean> isChange = new HashMap<String, Boolean>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        userId=intent.getStringExtra(Constants.USERID);
		
        //初始化标记是否修改资料信息
        for(int i = 0;i < 14;i++){
        	isChange.put("1", false);
		}
        
        setContentView(R.layout.activity_user_detail_info);                                
        setTitleRes(R.string.employee_info);
        //showBackbtn();
        mRoleList = RetailApplication.getRoleList();
        mShopList = RetailApplication.getShopList();
        mSexList = RetailApplication.getSexList();
        mIdentityTypeList = RetailApplication.getIdentityTypeList();
        
        change2saveMode();
        findView();
        getUserInfoInit();
        
    }
    
    
    //查询所有控件ID
    private void findView(){
    	
    	mEDName = (ItemEditText)findViewById(R.id.userName);
    
    	
    	mEDAccount = (ItemEditText)findViewById(R.id.userAccount);
    	mEDAdress = (ItemEditText)findViewById(R.id.userAdress);
    	mEDIdentityNo = (ItemEditText)findViewById(R.id.userIdentityNo);
    	mEDPassword = (ItemEditText)findViewById(R.id.userPassword);
    	mEDPassword.setVisibility(View.GONE);
    	
    	mEDUserPhone = (ItemEditText)findViewById(R.id.userPhone);
    	mEDUserStaffId = (ItemEditText)findViewById(R.id.userStaffCode);
    	
    	//可编译的员工信息初始化
    	mEDName.initLabel("员工姓名", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
    	mEDName.setMaxLength(Constants.USERNAMEMAXLENGTH);
    	
    	mEDPassword.initLabel("登录密码", "", Boolean.TRUE, InputType.TYPE_TEXT_VARIATION_PASSWORD);
    	mEDName.setMaxLength(Constants.USERPASSWORDMAXLENGTH);
    	
    	mEDUserStaffId.initLabel("员工工号", "", Boolean.TRUE, InputType.TYPE_CLASS_NUMBER);
    	mEDUserStaffId.setMaxLength(Constants.USERSTAFFIDMAXLENGTH);
    	
      	mEDAccount.initLabel("用户名", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
      	mEDAccount.setMaxLength(Constants.USERACCOUNTMAXLENGTH);
      	
      	mEDIdentityNo.initLabel("证件号码", "", Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
      	mEDIdentityNo.setMaxLength(Constants.USERIDENTITYNOMAXLENGTH);
      	
      	mEDAdress.initLabel("住址", "", Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
      	mEDAdress.setMaxLength(Constants.USERADDRESSMAXLENGTH);
      	
      	mEDUserPhone.initLabel("联系电话", "", Boolean.FALSE, InputType.TYPE_CLASS_PHONE);
      	mEDUserPhone.setMaxLength(Constants.USERSTAFFIDMAXLENGTH);
      	
    	mELUserBirthday = (ItemEditList) findViewById(R.id.userBirthday);
    	mELUserInDate = (ItemEditList) findViewById(R.id.userInDate);
    	mELUserRole = (ItemEditList) findViewById(R.id.userRole);
    	mELUserSex = (ItemEditList) findViewById(R.id.userSex);
    	mELUserShop = (ItemEditList) findViewById(R.id.userShopName);
    	mELUserIdentityType = (ItemEditList) findViewById(R.id.userIdentityType);
    	mELUserShop.setOnClickListener(this);
    	
    	mELUserShop.initLabel("所属门店","",Boolean.TRUE,this);
    	mELUserShop.getImg().setImageResource(R.drawable.arrow_right);
    	
    	mELUserRole.initLabel("员工角色","",Boolean.TRUE,this);
    	mELUserSex.initLabel("性别","",Boolean.FALSE,this);
    	mELUserInDate.initLabel("入职时间","",Boolean.FALSE,this);
    	mELUserBirthday.initLabel("生日","",Boolean.FALSE,this);
    	mELUserIdentityType.initLabel("证件类型","",Boolean.FALSE,this);
    	
    	mIMGPortrait =  (ItemPortraitImage) findViewById(R.id.userPortrait);
    	
    	mIMGPortrait.initLabel("头像", "");
    	
    	userDelete = (Button) findViewById(R.id.userDelete);
    	userDelete.setVisibility(View.VISIBLE);
    	
    	userDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteUserInfo();
			}
		});
    	
    }
    
    /**
     * 
     */
    
    
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
		if(v.getId() == R.id.userShopName){
			 Intent intent = new Intent(ShowUserDetailInfoActivity.this,UserShopSelectActivity.class);
	           
             startActivityForResult(intent, SELECTSHOPRECODE);
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
		
    	if(data !=null){
    		tmpShopId =  data.getStringExtra(Constants.SHOP_ID);
			String tmpShopName = data.getStringExtra(Constants.SHOPCOPNAME);
			mELUserShop.initData(tmpShopName, tmpShopName);
    	}
    }	

	
	
	/**
	 * 处理返回的结果
	 */
	Handler mSaveHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			getProgressDialog().dismiss();
			
			switch (msg.what) {
				case Constants.HANDLER_SUCESS:
					
					ToastUtil.showLongToast(getApplicationContext(),msg.obj.toString());	
					//AddUserInfoActivity.this.finish();
					
				break;
				
				case Constants.HANDLER_FAIL:	
                        ToastUtil.showLongToast(getApplicationContext(),ErrorMsg.getErrorMsg(msg.obj.toString())
                                );
				 
					break;
				case Constants.HANDLER_ERROR:		
                        ToastUtil.showLongToast(getApplicationContext(),
                                msg.obj.toString());
                 
					break;
			}
		}
	};
	
	/**
	 * 添加员工信息
	 */
	public void saveUserInfo(String operate,UserVo userVo){
		
		//显示进度条对话框
		getProgressDialog().setCancelable(false);
		getProgressDialog()
				.setMessage("保存用户信息");
		getProgressDialog().show();
		
		RequestParameter params = new RequestParameter(true);
		
		params.setUrl(Constants.EMPLOYEE_INFO_SAVE);

		params.setParam(Constants.OPT_TYPE,operate);	
	   	
		try {
			params.setParam(Constants.USER, new JSONObject(new GsonBuilder().serializeNulls().create().toJson(userVo)));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		AsyncHttpPost httpsave = new AsyncHttpPost(params,new RequestResultCallback() {
			
			//网路请求成功
			@Override
			public void onSuccess(String str) {
				// TODO Auto-generated method stub
			 	Log.i(TAG,"saveUserInfo str = "+str);
		      	getProgressDialog().dismiss();
		      	
		    	JsonUtil ju = new JsonUtil(str);
		    	
		        Message msg = new Message();
		        if(ju.getString(Constants.RETURN_CODE).equals(Constants.SUCCESS)){
		        	msg.obj = "保存成功";
		        	msg.what = Constants.HANDLER_SUCESS;
		        	mSaveHandler.sendMessage(msg);
		        	
		        }else{
		        	msg.what = Constants.HANDLER_FAIL;
		        	msg.obj = ju.getExceptionCode();
		        	mSaveHandler.sendMessage(msg);
		        }

			}

			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
		       	getProgressDialog().dismiss();
		        e.printStackTrace();
		        Log.e("results", "Login FaiL");
		        Message msg = new Message();
		        
		        msg.what = Constants.HANDLER_ERROR;
		        msg.obj = e.getMessage();
		        mSaveHandler.sendMessage(msg);
			}
		});
		httpsave.execute();
	}
	
	
	



	/**
	 * 获取初始化员工信息
	 */
	public void getUserInfoInit(){
		
		//显示进度条对话框
		getProgressDialog().setCancelable(false);
		getProgressDialog()
				.setMessage("获取用户信息");
		getProgressDialog().show();		
		RequestParameter params = new RequestParameter(true);		
		params.setUrl(Constants.EMPLOYEE_INFO_DETAIL);
		params.setParam(Constants.USERID,userId);		   	
		
		AsyncHttpPost httpinit = new AsyncHttpPost(params,new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				// TODO Auto-generated method stub
				JsonUtil ju = new JsonUtil(str);
				getProgressDialog().dismiss();
		        Message msg = new Message();
		        if(ju.getString(Constants.RETURN_CODE).equals(Constants.SUCCESS)){
		        	msg.what = Constants.HANDLER_SUCESS;
		            
		        	// 初始化角色信息
//		        	mRoleList = (List<RoleVo>) ju.get(Constants.ROLE_LIST, 
//							new TypeToken<List<RoleVo>>(){}.getType());		        	
//		        	mShopList = (List<AllShopVo>) ju.get(Constants.SHOP_LIST, 
//							new TypeToken<List<AllShopVo>>(){}.getType());
//		        	mSexList = (List<DicVo>) ju.get(Constants.SEX_LIST, 
//							new TypeToken<List<DicVo>>(){}.getType());
//		        	mIdentityTypeList = (List<DicVo>) ju.get(Constants.IDENTITYTYPE_LIST, 
//							new TypeToken<List<DicVo>>(){}.getType());
		        	mUserVo = (UserVo) ju.get(Constants.USER, UserVo.class);
		        	if(!CommonUtils.isEmpty(mUserVo.getRoleId())){
		        		tmpRoleId = mUserVo.getRoleId();
		        	}
		        	if(!CommonUtils.isEmpty(mUserVo.getShopId())){
		        		tmpShopId = mUserVo.getShopId();
		        	}
		        	
		        	updateView();
		            
		        }else{
		        	//获取信息失败
		        	msg.what = Constants.HANDLER_FAIL;
		        	msg.obj = ju.getExceptionCode();
		        	mSaveHandler.sendMessage(msg);
		        }

			}
			
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
		      	getProgressDialog().dismiss();
		        e.printStackTrace();
		        Log.e("results", "Login FaiL");
		        Message msg = new Message();
		        msg.what = Constants.HANDLER_ERROR;
		        msg.obj = e.getMessage();
			}
		});
		httpinit.execute();
	}
	
	
	
	/**
	 * 删除员工信息
	 */
	public void deleteUserInfo(){
		
		//显示进度条对话框
		getProgressDialog().setCancelable(false);
		getProgressDialog()
				.setMessage("删除用户信息");
		getProgressDialog().show();		
		RequestParameter params = new RequestParameter(true);		
		params.setUrl(Constants.EMPLOYEE_INFO_DELETE);
		params.setParam(Constants.USERID,userId);		   	
		
		AsyncHttpPost httpinit = new AsyncHttpPost(params,new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				// TODO Auto-generated method stub
				JsonUtil ju = new JsonUtil(str);
				getProgressDialog().dismiss();
		        Message msg = new Message();
		        if(ju.getString(Constants.RETURN_CODE).equals(Constants.SUCCESS)){
		        	msg.what = Constants.HANDLER_SUCESS;
		        	ShowUserDetailInfoActivity.this.finish();
		            
		        }else{
		        	//返回错误信息
		        	msg.what = Constants.HANDLER_FAIL;
		        	msg.obj = ju.getExceptionCode();
		        	mSaveHandler.sendMessage(msg);
		        }

			}
			
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
		      	getProgressDialog().dismiss();
		        e.printStackTrace();
		        Log.e("results", "Login FaiL");
		        Message msg = new Message();
		        msg.what = Constants.HANDLER_ERROR;
		        msg.obj = e.getMessage();
			}
		});
		httpinit.execute();
	}
	
	/**
	 * 网络提交数据时，更新用户资料
	 * {"userId":"e55ecbcca8a24112b0bd561b706e0e43","userName":"xiangfei1QQ11",
	 * "pwd":"123456","name":"香榧","staffId":"717899",
	 *	"roleId":"170ff6426b544730aaf7512d5ed50515","shopId":"00000000000000000000000000000001",
	 *	"inDate":"2014-09-18","mobile":"12345679876",
	 *	"sex":"1","birthday":"1989-05-26","identityTypeId":"1","identityNo":"256812198905263587",
	 *	"address":"教工路","lastVer":"3","fileName":"","file":
	 */
	private void upDateUserVo(){
		//mUserVo.setUserId("");
		mUserVo.setUserName(mEDAccount.getCurrVal());
		mUserVo.setName(mEDName.getCurrVal());
		mUserVo.setStaffId(mEDUserStaffId.getCurrVal());
		mUserVo.setRoleId(tmpRoleId);
		mUserVo.setShopId(tmpShopId);
		
		//for test
		//mUserVo.setRoleId("170ff6426b544730aaf7512d5ed50515");
		//mUserVo.setShopId("00000000000000000000000000000001");
		
		mUserVo.setAddress(mEDAdress.getCurrVal());
		mUserVo.setInDate(mELUserInDate.getCurrVal());

		mUserVo.setMobile(mEDUserPhone.getCurrVal());
		if(!CommonUtils.isEmpty(mELUserSex.getCurrVal()))
			mUserVo.setSex(tmpSexId);

		mUserVo.setBirthday(mELUserBirthday.getCurrVal());
		
		if(!CommonUtils.isEmpty(mELUserIdentityType.getCurrVal())){
			mUserVo.setIdentityTypeId(tmpIdentityTypeId);
			mUserVo.setIdentityNo(mEDIdentityNo.getCurrVal());
		}

		mUserVo.setAddress(mEDAdress.getCurrVal());
		//mUserVo.setLastVer("1");
		
		
//		mUserVo.setFile(null);
//		mUserVo.setFileName("");
		
	}
	
	
	
	/**
	 * 更新ui信息
	 */
	private void updateView(){
		
		mEDAccount.initData(mUserVo.getUserName());
		mEDAdress.initData(mUserVo.getAddress());
		mEDIdentityNo.initData(mUserVo.getIdentityNo());
		mEDName.initData(mUserVo.getName());
		mRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mUserVo == null)
					mUserVo = new UserVo();			    
			    //如果信息符合符合规范，提交数据
			    if(checkUserInfo() == null){
			    	upDateUserVo();	
			    	saveUserInfo(Constants.EDIT, mUserVo);
			    //添加的数据信息不符合规范	
			    }else{
			    	ToastUtil.showLongToast(getApplicationContext(),
			    			checkUserInfo());
			    }
			}
		});
	    /*mEDName.setTextChangeListener(new IItemTextChangeListener() {
			
			@Override
			public void onItemEditTextChange(ItemEditText obj) {
				// TODO Auto-generated method stub
				//用户名修改		
				isChange.put("1", !StringUtils.isEquals(mEDName.getCurrVal(), mEDName.getOldVal()));
				if(isChange(isChange)){
					change2saveMode();
					//保存用户信息
			    	mRight.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if(mUserVo == null)
								mUserVo = new UserVo();			    
						    //如果信息符合符合规范，提交数据
						    if(checkUserInfo() == null){
						    	upDateUserVo();	
						    	saveUserInfo(Constants.EDIT, mUserVo);
						    //添加的数据信息不符合规范	
						    }else{
						    	ToastUtil.showLongToast(getApplicationContext(),
						    			checkUserInfo());
						    }
						}
					});
				}else{
					showBackbtn();
				}
			}
		});*/
    	
		mEDPassword.initData(mUserVo.getPwd());
		mEDUserPhone.initData(mUserVo.getMobile());
		mEDUserStaffId.initData(mUserVo.getStaffId());
		
		
//	    private String tmpRoleId;
//	    private Integer tmpIdentityTypeId;
//	    private String tmpShopId;
//	    private Integer tmpSexId;// 性别
		tmpRoleId = mRoleList.get(0).getRoleId();
		//tmpIdentityTypeId = mIdentityTypeList.get(0).getVal();
		tmpShopId = mShopList.get(0).getShopId();
		//tmpSexId = mSexList.get(0).getVal();
		
		mELUserBirthday.initData(mUserVo.getBirthday(), mUserVo.getBirthday());
		mELUserInDate.initData(mUserVo.getInDate(), mUserVo.getInDate());
		
		mELUserRole.initData(getRoleName(mUserVo.getRoleId()), getRoleName(mUserVo.getRoleId()));
		
		mELUserSex.initData(getSexName(mUserVo.getSex().toString()), getSexName(mUserVo.getSex().toString()));
		
		mELUserShop.initData(getShopName(mUserVo.getShopId()), getShopName(mUserVo.getShopId()));
		mELUserIdentityType.initData(getIndentityName(mUserVo.getIdentityTypeId().toString()), getIndentityName(mUserVo.getIdentityTypeId().toString()));
		

		roleNameList.clear();
		shopNameList.clear();
		sexNameList.clear();
		indentityNameList.clear();
		
		/*for(int i = 0;i < mRoleList.size();i++){
			roleNameList.add(mRoleList.get(i).getRoleName());
		}
		for(int i = 0;i < mShopList.size();i++){
			shopNameList.add(mShopList.get(i).getShopName());
		}
		for(int i = 0;i < mSexList.size();i++){
			sexNameList.add(mSexList.get(i).getName());
		}
		for(int i = 0;i < mIdentityTypeList.size();i++){
			indentityNameList.add(mIdentityTypeList.get(i).getName());
		}*/
		
	}
	
	/**
	 * 选择角色信息
	 * 
	 * @param v
	 */

	  private void initRolePopupWidnow(final ItemEditList v) {
	        if (_rolePopupWindow == null) {
	            _rolePopupWindow = new SpinerPopWindow(this);
	            _rolePopupWindow.setItemListener(new IOnItemSelectListener() {

	                @Override
	                public void onItemClick(int position) {
	                	tmpRoleId = mRoleList.get(position).getRoleId();
	                	v.initData(mRoleList.get(position).getRoleName(), mRoleList.get(position).getRoleName());
	
	                }
	            });
	            _rolePopupWindow.refreshData(roleNameList, 0);
	            //_rolePopupWindow.setWidth((int)(v.getPaint().measureText(UserInfoInit.getInstance().getRoleListMax()) * 1.4f));
	            _rolePopupWindow.setWidth((int)(v.getLblVal().getPaint().measureText( CommonUtils.geStringListMax(roleNameList)) *1.4f));
	        }
	    }
	
	  /**
	   * 选择门店
	   * @param v
	   */
	 private void initShopPopupWindow(final ItemEditList v) {
	        if (_shopPopupWindow == null) {
	            _shopPopupWindow = new SpinerPopWindow(this);
	            _shopPopupWindow.setItemListener(new IOnItemSelectListener() {

	                @Override
	                public void onItemClick(int position) {
//	                    ShopVo shop = UserInfoInit.getInstance().getShopList().get(position);
//	                    mEmployee.setShopId(shop.getShopId());
//	                    mEmployee.setShopName(shop.getShopName());
	                    tmpShopId = mShopList.get(position).getShopId();
	                    //v.setText(shop.getShopName());
	                }
	            });
	            _shopPopupWindow.refreshData(shopNameList, 0);
	            _shopPopupWindow.setWidth((int)(v.getLblVal().getPaint().measureText( CommonUtils.geStringListMax(shopNameList)) *1.4f));
	        }
	    }
	 
	    /**
	     * 选择入职时间
	     * @param v
	     */
	    private void initDateDialog(ItemEditList v) {
	        if (_inDateDialog == null) {
	            final ItemEditList textView = ((ItemEditList)v);
	            Calendar calendar = Calendar.getInstance();
	            _inDateDialog = new DatePickerDialog(this, 
	                    new OnDateSetListener() {
	                        
	                        @Override
	                        public void onDateSet(DatePicker view, int year, int monthOfYear,
	                                int dayOfMonth) {
	                            String str = new StringBuilder().append(year).append("-")
	                            .append(monthOfYear + 1).append("-").append(dayOfMonth).toString();
	                            
	                            textView.initData(str, str);
	                        }
	                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
	        }
	    }
	    
	    /**
	     * 选择性别
	     * @param v
	     */
	    private void initSexPopupWindow(final ItemEditList v) {
	        if (_sexPopupWindow == null) {
	            _sexPopupWindow = new SpinerPopWindow(this);
	            _sexPopupWindow.setItemListener(new IOnItemSelectListener() {

	                @Override
	                public void onItemClick(int position) {
	                    
	                    tmpSexId = mSexList.get(position).getVal();
	                    v.initData(mSexList.get(position).getName(), mSexList.get(position).getName());
	                }
	            });
	            _sexPopupWindow.refreshData(sexNameList, 0);
	            _sexPopupWindow.setWidth((int)(v.getLblVal().getPaint().measureText( CommonUtils.geStringListMax(sexNameList)) *1.4f));
	        }
	    }
	    
	    /**
	     * 选择生日日期
	     * @param v
	     */
	    
	    private void initBirthdayDialog(ItemEditList v) {
	        if (_birthDayDialog == null) {
	            final ItemEditList textView = ((ItemEditList)v);
	            Calendar calendar = Calendar.getInstance();
	            _birthDayDialog = new DatePickerDialog(this, 
	                    new OnDateSetListener() {
	                        
	                        @Override
	                        public void onDateSet(DatePicker view, int year, int monthOfYear,
	                                int dayOfMonth) {
	                            String str = new StringBuilder().append(year).append("-")
	                            .append(monthOfYear + 1).append("-").append(dayOfMonth).toString();
	    //                        textView.setText(str);
	                             //mUserVo.setBirthday(str);
	                             textView.initData(str, str);
	                        }
	                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
	        }
	    }
	    
	    /**
	     * 证件类型
	     * @param v
	     */
	    private void initIdentityPopupWindow(final ItemEditList v) {
	        if (_identityPopupWindow == null) {
	            _identityPopupWindow = new SpinerPopWindow(this);
	            _identityPopupWindow.setItemListener(new IOnItemSelectListener() {

	                @Override
	                public void onItemClick(int position) {
	                    DicVo identityType = mIdentityTypeList.get(position);
//	                    mUserVo.setIdentityTypeId(identityType.getVal());
//	                    mUserVo.setIdentityTypeName(identityType.getName());
	                    tmpIdentityTypeId = identityType.getVal();
	                    v.initData(identityType.getName(), identityType.getName());
	                }
	            });
	            _identityPopupWindow.refreshData(indentityNameList, 0);
	            _identityPopupWindow.setWidth((int)(v.getLblVal().getPaint().measureText( CommonUtils.geStringListMax(indentityNameList)) *1.4f));
	        }
	    }
	    
	    public void ClickListener(View v) {
	        int position = Integer.parseInt(String.valueOf(v.getTag()));
	        switch (position) {
	        case 1:
	            initRolePopupWidnow((ItemEditList)v);
	            _rolePopupWindow.showAsDropDown(v);
	            break;
	            
	        case 2:
	        	initShopPopupWindow((ItemEditList)v);
	            _shopPopupWindow.showAsDropDown(v);
	            break;
	            
	        case 3:
	            initDateDialog((ItemEditList)v);
	            _inDateDialog.show();
	            break;
	            
	        case 4:
	            initSexPopupWindow((ItemEditList)v);
	            _sexPopupWindow.showAsDropDown(v);
	            break;
	            
	        case 5:
	            initBirthdayDialog((ItemEditList)v);
	            _birthDayDialog.show();
	            break;
	            
	        case 6:
	            initIdentityPopupWindow((ItemEditList)v);
	            _identityPopupWindow.showAsDropDown(v);
	            break;
	        }
	    }



		@Override
		public void onItemIsChangeListener(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			//用户名修改
			case R.id.userName:
				isChange.put("1", !StringUtils.isEquals(mEDName.getCurrVal(), mEDName.getOldVal()));
				if(isChange(isChange)){
					change2saveMode();
					//保存用户信息
			    	mRight.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if(mUserVo == null)
								mUserVo = new UserVo();			    
						    //如果信息符合符合规范，提交数据
						    if(checkUserInfo() == null){
						    	upDateUserVo();	
						    	saveUserInfo(Constants.EDIT, mUserVo);
						    //添加的数据信息不符合规范	
						    }else{
						    	ToastUtil.showLongToast(getApplicationContext(),
						    			checkUserInfo());
						    }
						}
					});
				}else{
					showBackbtn();
				}
				break;
			}
			
		}

		
		@Override
		public void onItemEditTextChange(ItemEditText obj) {
			// TODO Auto-generated method stub
			
			switch (obj.getId()) {
			//用户名修改
			case R.id.userName:
				isChange.put("1", !StringUtils.isEquals(mEDName.getCurrVal(), mEDName.getOldVal()));
				if(isChange(isChange)){
					change2saveMode();
					//保存用户信息
			    	mRight.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if(mUserVo == null)
								mUserVo = new UserVo();			    
						    //如果信息符合符合规范，提交数据
						    if(checkUserInfo() == null){
						    	upDateUserVo();	
						    	saveUserInfo(Constants.EDIT, mUserVo);
						    //添加的数据信息不符合规范	
						    }else{
						    	ToastUtil.showLongToast(getApplicationContext(),
						    			checkUserInfo());
						    }
						}
					});
				}else{
					showBackbtn();
				}
				break;

			default:
				break;
			}
		}
		
		

	    /**
	     * 检测输入的参数是否符合要求
	     */
	    private String checkUserInfo( ){
	    	String ret = null;
	    	//名字不能为空。必填
	    	if(CommonUtils.isEmpty(mEDName.getCurrVal())){
	    		ret = "姓名不能为空";
	    	}else if(CommonUtils.isEmpty(mEDAccount.getCurrVal())){
	    		ret = "用户名不能为空";
	    	}else if(CommonUtils.isEmpty(mEDPassword.getCurrVal()) || (mEDPassword.getCurrVal().length() < 6)){
	    		if(CommonUtils.isEmpty(mEDPassword.getCurrVal())){
	    			ret = "密码不能为空";
	    		}else{
	    			ret = "密码长度最少6位";
	    		}	    		
	    	}else if(CommonUtils.isEmpty(mEDUserStaffId.getCurrVal())){
	    		ret = "员工工号不能为空";	    		
	    	}else if(CommonUtils.isEmpty(mELUserShop.getCurrVal())){
	    		ret = "员工所属门店";	    		
	    	}else if(CommonUtils.isEmpty(mELUserRole.getCurrVal())){
	    		ret = "角色类型不能为空";	    		
	    	}	    	  	
	    	return ret;	    	
	    }
	 

}