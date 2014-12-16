package com.dfire.retail.app.manage.activity.usermanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.setting.RolePermissionSettingActivity;
import com.dfire.retail.app.manage.adapter.StoreManagerAdapter;
import com.dfire.retail.app.manage.adapter.StoreManagerItem;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.bo.UserInitBo;
import com.dfire.retail.app.manage.global.ConfigConstants;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;

public class EmployeeActivity extends TitleActivity {

    ArrayList<StoreManagerItem> mList;
    private ListView mListView;
    private StoreManagerAdapter adapter;
    private List<RoleVo> mRoleList;
    private List<AllShopVo> mShopList;
    AsyncHttpPost httpinit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.storemanager);

        setTitleText("员工");
        showBackbtn();

        mListView = (ListView) findViewById(R.id.storemanager_main_list);

        mList = new ArrayList<StoreManagerItem>();

        adapter = new StoreManagerAdapter(EmployeeActivity.this, mList);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
            	if(!CommonUtils.getPermission(ConfigConstants.ACTION_EMPLOYEE_ACTION)||!CommonUtils.getPermission(ConfigConstants.ACTION_EMPLOYEE_MANAGE)) {//角色
            		new ErrDialog(EmployeeActivity.this, EmployeeActivity.this.getString(R.string.MC_MSG_000005)).show();
    	            return;
        		}
                Intent intent = new Intent(EmployeeActivity.this, mList.get(arg2).getDestClass());
                startActivity(intent);
            }
        });
        
    }
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getUserInfoInit();
	}
	/**
	 * 获取初始化员工信息
	 */
	public void getUserInfoInit(){
		//显示进度条对话框	
		RequestParameter params = new RequestParameter(true);		
		params.setUrl(Constants.EMPLOYEE_INFO_INIT);
		httpinit = new AsyncHttpPost(EmployeeActivity.this,params,UserInitBo.class,true,new RequestCallback() {
			@Override
			public void onSuccess(Object str) {
		        UserInitBo bo = (UserInitBo)str;
		        	// 初始化角色信息
		        	mList.clear();
		        	mRoleList = bo.getRoleList();		        	
		        	mShopList = bo.getShopList();
		        	if(mRoleList != null){
			            mList.add(new StoreManagerItem(R.drawable.jiaojieban, EmployeeActivity.this.getResources().getString(R.string.role_permission),
			            		String.format(EmployeeActivity.this.getResources().getString(R.string.rolecount),mRoleList.size()),
			            		RolePermissionSettingActivity.class));
		        	}else{
		        		mList.add(new StoreManagerItem(R.drawable.jiaojieban,EmployeeActivity.this.getResources().getString(R.string.role_permission),
			            		String.format(EmployeeActivity.this.getResources().getString(R.string.rolecount),0),
			            		RolePermissionSettingActivity.class));
		        	}
		            adapter.notifyDataSetChanged();
		            if(mShopList != null){
			            mList.add(new StoreManagerItem(R.drawable.setting_account_info, EmployeeActivity.this.getResources().getString(R.string.usermanager), 
			            		String.format(EmployeeActivity.this.getResources().getString(R.string.usercount),mShopList.size()),
			            		EmployeeInfoActivity.class));
		            }else{
		            	 mList.add(new StoreManagerItem(R.drawable.setting_account_info, EmployeeActivity.this.getResources().getString(R.string.usermanager), 
				            		String.format(EmployeeActivity.this.getResources().getString(R.string.user_info),""),
				            		EmployeeInfoActivity.class));
		            }
		            adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		});
		httpinit.execute();
	}  

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//退出页面时，网络请求结束
		if(httpinit != null){
			httpinit.cancel();
		}
	}
}
