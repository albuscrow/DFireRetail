package com.dfire.retail.app.manage.activity.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.RolePermissionSettingAdapter;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.bo.RoleListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.ToastUtil;
/**
 * 项目名称：Retail  
 * 类名称：RolePermissionSettingActivity  
 * 类描述：   角色权限-角色一览
 * 创建时间：2014年11月22日 下午2:45:14  
 * @author chengzi  
 * @version 1.0
 */
public class RolePermissionSettingActivity extends TitleActivity implements OnItemClickListener, OnClickListener {

    //private String[] mData;
    private List<RoleVo> roles = new ArrayList<RoleVo>();
    private RolePermissionSettingAdapter adapter;
    private Integer location;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_role);
        setTitleText(this.getString(R.string.setting_role));
        findViewById(R.id.minus).setOnClickListener(this);
        findViewById(R.id.help).setOnClickListener(this);
        //mData = getResources().getStringArray(R.array.setting_role);
        ListView list = (ListView) findViewById(R.id.setting_role_list);
        new ListAddFooterItem(this,list);
        adapter = new RolePermissionSettingAdapter(this, roles, R.layout.setting_role_item);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        this.showBackbtn();
        initData();
    }

    private void initData() {
          
        this.initRoleList(); 
        
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        //当前修改的角色
        this.location = position;
        Intent intent = new Intent();
        intent.setClass(this, RolePermissionActivity.class);
        RoleVo role = roles.get(position);
        intent.putExtra("roleName", role.getRoleName());
        intent.putExtra("roleId", role.getRoleId());
        intent.putExtra("operateType", "edit");
        startActivityForResult(intent, RESULT_EDIT);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.help:
            break;
        case R.id.minus:
            this.add();
            break;
        }
    }
    private void add() {
          
        Intent intent = new Intent(this,RolePermissionActivity.class);
        intent.putExtra("operateType", "add");
        startActivityForResult(intent, RESULT_SAVE);
        
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(resultCode == RESULT_SAVE || resultCode == RESULT_DELETE) {
            initRoleList();
        }else if(resultCode == RESULT_EDIT) {
            RoleVo role = (RoleVo)data.getSerializableExtra("role");
            if(role != null && this.location != null && this.roles.size() > this.location) {
                this.roles.get(location).setRoleName(role.getRoleName());
            }
        }
    }

    private void initRoleList() {
        RequestParameter requestParams = new RequestParameter(true);
        requestParams.setUrl(Constants.ROLE_PERMISSION_ROLE_LIST);
        new AsyncHttpPost(this, requestParams, RoleListBo.class, new RequestCallback(){

            @Override
            public void onSuccess(Object bo) {
                RoleListBo roleBo = (RoleListBo)bo;
                List<RoleVo> roleVos = roleBo.getRoleList();
                roles.clear();
                if(roleVos != null && !roleVos.isEmpty()) {
                    roles.addAll(roleVos);
                    adapter.notifyDataSetChanged();
                }else {
                    ToastUtil.showShortToast(RolePermissionSettingActivity.this, "您还没有添加角色");
                }
            }

            @Override
            public void onFail(Exception e) {
                
            }}).execute();
        
    }
}
