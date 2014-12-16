package com.dfire.retail.app.manage.activity.usermanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.adapter.EmployeeInfoAdapter;
import com.dfire.retail.app.manage.common.CommonSelectTypeDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.UserVo;
import com.dfire.retail.app.manage.data.bo.UserInitBo;
import com.dfire.retail.app.manage.data.bo.UserQueryBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/**
 * 员工 -- 员工列表一览
 */
public class EmployeeInfoActivity extends UserBaseActivity implements OnClickListener, IItemListListener {
	public static EmployeeInfoActivity instance = null;
    private ItemEditList _shopChoiceButton;
    private ItemEditList  _roleChoiceButton;
    private Button	searchButton;
    private EditText editKeyWord;
    private ImageButton mBTAddUserInfo;
    private List<RoleVo> mRoleList;
    private List<AllShopVo> mShopList;
    private List<DicVo> mSexList;
    private List<DicVo> mIdentityTypeList;
	private String tmpDataFromId;
	private static final int SELECTSHOPRECODE =2;
	public String dataFromShopId;
	private String roleId;
	List<UserVo> mList = new ArrayList<UserVo>();
    private PullToRefreshListView mListView;
    private EmployeeInfoAdapter mAdapter;
    String tmpShopName;
    private ArrayList<String> roleNameList = new ArrayList<String>();
    private CommonSelectTypeDialog mSelectRole;
    private int mode;// 判断异步执行完是否禁用加载更多
    private Integer currentPage = 1;
    private Integer pageSize;
    private boolean isRuning = false; //标记单签正在查找
    private ImageView clear_input;
    private Integer index;//listView 定位
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_info);
        initView();
        instance = this;
     // 设置列表的刷新模式为下拉刷新，同时进行刷新操作
        mListView.setMode(Mode.PULL_FROM_START);
        getUserInfoInit();
		mListView.setRefreshing();
    }
    private void initView(){
        mListView = (PullToRefreshListView) findViewById(R.id.attendance_list);
        mAdapter = new EmployeeInfoAdapter(EmployeeInfoActivity.this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setMode(Mode.BOTH);
        new ListAddFooterItem(this, mListView.getRefreshableView());
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(EmployeeInfoActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = 1;
				startSerachData();
			}
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(EmployeeInfoActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				startSerachData();
			}
		});
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                    long arg3) {
            	index = position - 1;
                Intent intent = new Intent(EmployeeInfoActivity.this, ShowUserDetailInfoActivity.class);
                intent.putExtra(Constants.USERID, mList.get(position-1).getUserId());
                intent.putExtra(Constants.SHOP_ID, tmpDataFromId);
                intent.putExtra(Constants.SHOPCOPNAME,tmpShopName);
                startActivity(intent);
            }
        });
    
    	clear_input = (ImageView) findViewById(R.id.clear_input);
		clear_input.setOnClickListener(this);
		editKeyWord = (EditText) findViewById(R.id.edit_user_name);
	    searchButton = (Button) findViewById(R.id.bt_search_userinfo);
	    searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!isRuning){
					reFreshing();
				}
			}
		});
         updateView();
         _shopChoiceButton = (ItemEditList) findViewById(R.id.shop_choice);
         String shopName = RetailApplication.getShopVo().getShopName();
         if (RetailApplication.getEntityModel()==1) {
 			//单店
         	_shopChoiceButton.setVisibility(View.GONE);
 		}else {
 			//连锁
 			if (RetailApplication.getShopVo().getType() == ShopVo.MENDIAN) {
 				_shopChoiceButton.initLabel("选择店家","",Boolean.TRUE,this);
 				_shopChoiceButton.getImg().setVisibility(View.GONE);
 	        	_shopChoiceButton.getLblVal().setTextColor(Color.parseColor("#666666"));
 			}else{
 				_shopChoiceButton.initLabel("选择店家","",Boolean.TRUE,this);
 				_shopChoiceButton.getImg().setImageResource(R.drawable.ico_next);
 			}
 			_shopChoiceButton.initData(shopName,shopName);
 		}
         setTitleRes(R.string.employee_info);
         showBackbtn();
         //默认店名Id
         tmpDataFromId = RetailApplication.getShopVo().getShopId();
         tmpShopName = RetailApplication.getShopVo().getShopName();
         roleId = "";
         
         editKeyWord.addTextChangedListener(new TextWatcher() {
 			@Override
 			public void onTextChanged(CharSequence s, int start, int before, int count) {
 				if (s!=null&&!s.toString().equals("")) {
 					clear_input.setVisibility(View.VISIBLE);
 				}else{
 					clear_input.setVisibility(View.GONE);
 				}
 			}
 			@Override
 			public void beforeTextChanged(CharSequence s, int start, int count,
 					int after) {
 			}
 			@Override
 			public void afterTextChanged(Editable s) {
 			}
 		});
    }
	
	private void updateView(){
	    _roleChoiceButton = (ItemEditList) findViewById(R.id.role_choice);
        _roleChoiceButton.initLabel("选择角色","",Boolean.TRUE,this);
        _roleChoiceButton.initData("全部", "全部");
        _roleChoiceButton.getSaveTag().setVisibility(View.GONE);
        _roleChoiceButton.setOnClickListener(this);
        //隐藏分隔线
        _roleChoiceButton.hindViewLine();
        mBTAddUserInfo = (ImageButton)findViewById(R.id.member_info_add);
        mBTAddUserInfo.setOnClickListener(this);
	}
	/**
	 * 初始化员工角色控件，绑定spinner
	 * @param v
	 */
    private void initRolePopupWidnow(final TextView v) {
    	mSelectRole = new CommonSelectTypeDialog(EmployeeInfoActivity.this,roleNameList);
        mSelectRole.show();
        mSelectRole.updateType(_roleChoiceButton.getCurrVal());
        mSelectRole.getTitle().setText(this.getResources().getString(R.string.role_type));;	        
        mSelectRole.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_roleChoiceButton.initData(mSelectRole.getCurrentData(),mSelectRole.getCurrentData());
				if(mSelectRole.getCurrentPosition() >0)
					roleId = mRoleList.get(mSelectRole.getCurrentPosition()-1).getRoleId();
				else{
					roleId = "";
				}
				reFreshing();
				mSelectRole.dismiss();
			}
		});
        mSelectRole.getCancelButton().setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			mSelectRole.dismiss();
    	}
	    });
    }
	/**
	 * 获取初始化员工信息
	 */
	private void getUserInfoInit(){
		//显示进度条对话框	
		RequestParameter params = new RequestParameter(true);		
		params.setUrl(Constants.EMPLOYEE_INFO_INIT);
		AsyncHttpPost httpinit = new AsyncHttpPost(EmployeeInfoActivity.this,params,UserInitBo.class,true,new RequestCallback() {
			@Override
			public void onSuccess(Object str) {
		        // 初始化角色信息
				UserInitBo bo = (UserInitBo)str;
	        	mRoleList = bo.getRoleList();		        	
	        	mShopList = bo.getShopList();
	        	mSexList = bo.getSexList();
	        	mIdentityTypeList = bo.getIdentityTypeList();
	        	RetailApplication.setRoleList(mRoleList);
	        	RetailApplication.setShopList(mShopList);
	        	RetailApplication.setSexList(mSexList);
	        	RetailApplication.setIdentityTypeList(mIdentityTypeList);
	        	roleNameList.clear();
	        	roleNameList.add(EmployeeInfoActivity.this.getResources().getString(R.string.allrole));
	        	for(int i = 0;i < mRoleList.size();i++){
	    			roleNameList.add(mRoleList.get(i).getRoleName());
	    		}
			}
			@Override
			public void onFail(Exception e) {
		        
			}
		});
		httpinit.execute();
	}    
	/**
	 * 加条件以后刷新数据
	 */
	public void reFreshing(){
		currentPage = 1;//选择以后初始化页数
		mListView.setMode(Mode.PULL_FROM_START);
		startSerachData();
		mListView.setRefreshing();
	}
	
    /**
     * 显示员工列表.
     */
    public void startSerachData() {
        RequestParameter parameters = new RequestParameter(true);
        parameters.setUrl(Constants.EMPLOYEE_INFO_LIST);
        parameters.setParam(Constants.SHOP_ID, tmpDataFromId);
        parameters.setParam(Constants.ROLEID, roleId);
        parameters.setParam(Constants.PAGE,currentPage);
        parameters.setParam(Constants.SHOPKEYWORD, editKeyWord.getText());
        
        new  AsyncHttpPost(this,parameters,UserQueryBo.class,false,new RequestCallback(){
	        @Override
	        public void onSuccess(Object str) {
	        	UserQueryBo bo = (UserQueryBo)str;
	        	if (bo!=null) {
	        		List<UserVo> tmpList= bo.getUserList();
					pageSize = bo.getPageSize();
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							mList.clear();
						}
						if (tmpList != null && tmpList.size() > 0) {
							mListView.setMode(Mode.BOTH);
							mList.addAll(tmpList);
						}else {
							mode = 1;
						}
						mAdapter.notifyDataSetChanged();
					}else {
						mList.clear();
						mAdapter.notifyDataSetChanged();
						mode = 1;
					}
					mListView.onRefreshComplete();
					if (mode == 1) {
						mListView.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
	        	}
            }
	        @Override
	        public void onFail(Exception e) {
	        	mListView.onRefreshComplete();
	        }
		}).execute();
    }
    /**
     * 回调函数
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
		//选择查询的门店信息
    	if(requestCode == SELECTSHOPRECODE && data !=null){
			 tmpDataFromId =  data.getStringExtra(Constants.SHOP_ID);
			tmpShopName = data.getStringExtra(Constants.SHOPCOPNAME);
			_shopChoiceButton.initData(tmpShopName, tmpShopName);
			reFreshing();
    	} 
    }
	/**
	 * itemList点击事件
	 */
	@Override
	public void onItemListClick(ItemEditList obj) {
		 switch (obj.getId()) {
	        case R.id.shop_choice:
	        	if (RetailApplication.getShopVo().getType() != ShopVo.MENDIAN) {
					Intent intent = new Intent(EmployeeInfoActivity.this,UserShopSelectActivity.class);
					startActivityForResult(intent, SELECTSHOPRECODE);
				}
	            break;

	        case R.id.role_choice:     
	            ItemEditList tmp = obj;
	            initRolePopupWidnow(tmp.getLblVal());
	            break;
		 }
	}
	/**
	 * 点击添加按钮
	 */
	@Override
	public void onClick(View v) {
	 switch (v.getId()) {
        case R.id.member_info_add:
        	startActivity(new Intent(EmployeeInfoActivity.this,AddUserInfoActivity.class));
            break;
        case R.id.clear_input:
        	editKeyWord.setText("");
			clear_input.setVisibility(View.GONE);
        	break;
        default:
        	break;
        }
	}
	/**
	 * 更新数据以后修改列表数据不刷新页面
	 */
	public void reList(UserVo mUserVo,String fileName){
		mUserVo.setRoleId(null);
		mUserVo.setFileName(fileName);
		mList.set(index, mUserVo);
		
		mAdapter.notifyDataSetChanged();
	}
}
