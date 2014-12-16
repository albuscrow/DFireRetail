package com.dfire.retail.app.manage.activity.setting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.listener.IItemIsChangeListener;
import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.RolePermissionItem;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.ActionVo;
import com.dfire.retail.app.manage.data.ModuleVo;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.SystemInfoVo;
import com.dfire.retail.app.manage.data.bo.RolePermissionsBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.listview.StickyListHeadersAdapter;


/**
 * 项目名称：Retail  
 * 类名称：RolePermissionActivity  
 * 类描述：   角色权限-角色对应的权限详情
 * 创建时间：2014年11月22日 下午3:56:51  
 * @author chengzi  
 * @version 1.0
 */
public class RolePermissionActivity extends TitleActivity 
    implements OnCheckedChangeListener, OnClickListener{
    
    private static final int REQUEST_ACTION_PERMISSION = 1;
    private LinearLayout mPrePermission;
    private LinearLayout mBgPremission;
    
    private LinearLayout rolePremissionLayout;
    private ItemEditText roleTxt;
    private List<SystemInfoVo> permissions = new ArrayList<SystemInfoVo>();
    private List<ModuleVo> modules = new ArrayList<ModuleVo>();
    private Map<String,Short> itemEditMap = new HashMap<String,Short>();
    //private StickyListHeadersListView listView;
    //private RolePermissionAdapter adapter;
    private ExpandableListView listView;
    private RolePermissionExpandableAdapter adapter;
    private String roleId;
    private String operateType;
    private Integer groupPoition;
    private Integer childPosition;
    private RoleVo role;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_role_permission);
        roleTxt = (ItemEditText) findViewById(R.id.role);
        roleTxt.initLabel("角色","角色", true, InputType.TYPE_CLASS_TEXT);
        roleTxt.setMaxLength(20);
        roleTxt.setIsChangeListener(new IItemIsChangeListener() {
            @Override
            public void onItemIsChangeListener(View v) {
                
                if(StringUtils.isEquals(roleTxt.getCurrVal(), roleTxt.getOldVal())) {
                    itemEditMap.put("角色", (short)0);
                }else {
                    itemEditMap.put("角色", (short)1);
                } 
                notifyTitleMode();
            }
        });
        roleId = getIntent().getStringExtra("roleId");
        operateType = getIntent().getStringExtra("operateType");
        
        String name = null;
        View footerView = null;
        if("edit".equals(operateType)) {
            name = getIntent().getStringExtra("roleName");
            footerView = this.getLayoutInflater().inflate(R.layout.setting_role_permission_footer, null);
            footerView.findViewById(R.id.delete).setOnClickListener(this);
            roleTxt.initData(name);
        }else{
            name = "添加";
        }
        setTitleText(name);
        
        this.showBackbtn();
        this.mRight.setOnClickListener(this);
        
        //rolePremissionLayout = (LinearLayout) findViewById(R.id.ll_role_permission);
        /*CheckBox pre = (CheckBox) findViewById(R.id.pre_permission_checkbox);
        pre.setOnCheckedChangeListener(this);
        CheckBox bg = (CheckBox) findViewById(R.id.bg_permission_checkbox);
        bg.setOnCheckedChangeListener(this);
        
        initPre(pre.isChecked());
        initBg(bg.isChecked());*/
        
        /*adapter = new RolePermissionAdapter(this,permissions,modules);
        listView = (StickyListHeadersListView)this.findViewById(R.id.lv_role_permission);
        listView.setAdapter(adapter);*/
        
        adapter = new RolePermissionExpandableAdapter(this,permissions,modules);
        listView = (ExpandableListView)this.findViewById(R.id.lv_role_permission);
        if(footerView != null) {
            listView.addFooterView(footerView);
        }
        listView.setAdapter(adapter);
        listView.setGroupIndicator(null);
        listView.setOnChildClickListener(new OnChildClickListener() {
            
            @Override
            public boolean onChildClick(ExpandableListView parent,
                                        View v,
                                        int groupPosition,
                                        int childPosition,
                                        long id) {
                //new ErrDialog(RolePermissionActivity.this, groupPosition + "-----" +  childPosition).show();
                return true;
            }
        });
        this.initData();
    }
    
    private void initData() {
          
       /*if(permissions != null && !permissions.isEmpty()) {
           LayoutInflater inflater = this.getLayoutInflater();
           //系统权限
           for(SystemInfoVo systemInfo : permissions) {
               View systemView = inflater.inflate(R.layout.setting_role_permission_header, null);
               TextView roleNameTxt = (TextView)systemView.findViewById(R.id.txt_system_info_name);
               roleNameTxt.setText(systemInfo.getSystemName());
               CheckBox permissionCheckBox = (CheckBox)systemView.findViewById(R.id.checkbox_permission);
               permissionCheckBox.setTag(systemInfo);
               permissionCheckBox.setOnCheckedChangeListener(this);
               rolePremissionLayout.addView(systemView);
               if(systemInfo.isChoiceFlag()) {
                   permissionCheckBox.setChecked(true);
                   //模块权限
                   List<ModuleVo> modules = systemInfo.getModuleVoList();
                   if(modules != null && !modules.isEmpty()) {
                       LinearLayout ll = new LinearLayout(RolePermissionActivity.this);
                       ll.setOrientation(LinearLayout.VERTICAL);
                       ll.setTag(permissionCheckBox);
                       LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.
                               LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                       rolePremissionLayout.addView(ll);
                       for(ModuleVo module : modules) {
                           View moduleView = inflater.inflate(R.layout.setting_role_permission_item, null);
                           TextView titleTxt = (TextView)moduleView.findViewById(R.id.title);
                           TextView countTxt = (TextView)moduleView.findViewById(R.id.count_permission);
                           TextView subTxt = (TextView)moduleView.findViewById(R.id.subhead);
                           titleTxt.setText(module.getModuleName());
                           countTxt.setText(module.getCount());
                           if(module.getCount() != null && module.getCount() > 0) {
                               subTxt.setText(module.getActionNameOfModule());
                               subTxt.setVisibility(View.VISIBLE);
                           }
                           ll.addView(moduleView,param);
                       }
                   }
               }else {
                   permissionCheckBox.setChecked(false);
               }
           }
       }*/
        if("edit".equals(operateType)) {
            this.reloadRolePermissions();
        }else{
            this.initRolePermissions();
        }
        
    }

    private void initRolePermissions() {
          
        RequestParameter requestParams = new RequestParameter(true);
        requestParams.setUrl(Constants.ROLE_PERMISSION_PERMISSION_INIT);  
        new AsyncHttpPost(this, requestParams, RolePermissionsBo.class, new RequestCallback(){

            @Override
            public void onSuccess(Object bo) {
                RolePermissionsBo rolePermissionsBo = (RolePermissionsBo)bo;
                List<SystemInfoVo> systemInfos = rolePermissionsBo.getSystemInfoList();
                
                if(systemInfos != null && !systemInfos.isEmpty()) {
                    permissions.clear();
                    permissions.addAll(systemInfos);
                    
                    //reloadModules();
                    /*if(permissions != null && !permissions.isEmpty()) {
                        for(int i=0,j=permissions.size();i<j;i++) {
                            SystemInfoVo systemInfo = permissions.get(i);
                            if(systemInfo.getModuleVoList() != null) {
                                int count = 0;
                                for(ModuleVo module : systemInfo.getModuleVoList()) {
                                    count += module.getCount();
                                }
                                if(count > 0) {
                                    systemInfo.setChoiceFlag(true);
                                }
                            }
                        }
                    }*/
                    adapter.notifyDataSetChanged();
                    
                    if(permissions != null && !permissions.isEmpty()) {
                        for(int i=0,j=permissions.size();i<j;i++) {
                            if(permissions.get(i).isChoiceFlag()) {
                                listView.expandGroup(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFail(Exception e) {
            }}).execute();
        
    }

    private void reloadRolePermissions() {
        RequestParameter requestParams = new RequestParameter(true);
        requestParams.setUrl(Constants.ROLE_PERMISSION_PERMISSION_LIST);  
        requestParams.setParam("roleId", roleId);
        new AsyncHttpPost(this, requestParams, RolePermissionsBo.class, new RequestCallback(){

            @Override
            public void onSuccess(Object bo) {
                RolePermissionsBo rolePermissionsBo = (RolePermissionsBo)bo;
                role = rolePermissionsBo.getRole();
                List<SystemInfoVo> systemInfos = rolePermissionsBo.getActionList();
                
                if(systemInfos != null && !systemInfos.isEmpty()) {
                    permissions.clear();
                    permissions.addAll(systemInfos);
                    
                    //reloadModules();
                    if(permissions != null && !permissions.isEmpty()) {
                        for(int i=0,j=permissions.size();i<j;i++) {
                            SystemInfoVo systemInfo = permissions.get(i);
                            if(systemInfo.getModuleVoList() != null) {
                                int count = 0;
                                for(ModuleVo module : systemInfo.getModuleVoList()) {
                                    count += module.getCount();
                                    module.setOldCount(module.getCount());
                                }
                                if(count > 0) {
                                    systemInfo.setChoiceFlag(true);
                                }
                                systemInfo.setOldChoiceFlag(systemInfo.isChoiceFlag());
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    
                    if(permissions != null && !permissions.isEmpty()) {
                        for(int i=0,j=permissions.size();i<j;i++) {
                            if(permissions.get(i).isChoiceFlag()) {
                                listView.expandGroup(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFail(Exception e) {
            }}).execute();
        
    }

    private void reloadModules() {
          
        if(permissions != null && !permissions.isEmpty()) {
            this.modules.clear();
            for(int i=0,j=permissions.size();i<j;i++) {
                SystemInfoVo systemInfoVo = permissions.get(i);
               // systemInfoVo.setChoiceFlag(true);
                systemInfoVo.setSystemId(i);
                List<ModuleVo> modules = systemInfoVo.getModuleVoList();
                if(modules != null && !modules.isEmpty()) {
                    for(int i1=0,j1=modules.size();i1<j1;i1++) {
                        ModuleVo module = modules.get(i1);
                        module.setSystemInfoVo(systemInfoVo);
                        
                        this.modules.add(module);
                    }
                }
            }
        }
        
    }

    private void initPre(boolean showing) {
        mPrePermission = (LinearLayout) findViewById(R.id.pre_permission);
        String[] preStr = getResources().getStringArray(R.array.role_pre_proscenium);
        int[] tag = {R.array.setting_checkstand, R.array.member_permission, 
                R.array.bill_permission, R.array.message_permission,
                R.array.system_param_permission, R.array.logistics_permission};
        
        View view;
        LayoutInflater inflater = LayoutInflater.from(this);
        RolePermissionItem item;
        TextView text;
        for (int i = 0; i < preStr.length; i++) {
            view = inflater.inflate(R.layout.setting_role_permission_item, null);
            text = (TextView) view.findViewById(R.id.title);
            text.setText(preStr[i]);
            text = (TextView) view.findViewById(R.id.count_permission);
            text.setText("0项");
            int id = R.layout.setting_role_permission_item + i + 1;
            view.setId(id);
            item = new RolePermissionItem(i, preStr[i], tag[i], id, true);
            view.setTag(item);
            view.setOnClickListener(this);
            mPrePermission.addView(view);
        }
        
        if (showing) {
            mPrePermission.setVisibility(View.VISIBLE);
        } else {
            mPrePermission.setVisibility(View.GONE);
        }
    }
    
    private void initBg(boolean showing) {
        mBgPremission = (LinearLayout) findViewById(R.id.bg_permission);
        String[] preStr = getResources().getStringArray(R.array.role_bg_proscenium);
        int[] tag = {R.array.setting_checkstand, R.array.member_permission, 
                R.array.bill_permission, R.array.message_permission,
                R.array.bill_permission, R.array.message_permission,
                R.array.system_param_permission, R.array.logistics_permission};
        
        View view;
        LayoutInflater inflater = LayoutInflater.from(this);
        RolePermissionItem item;
        TextView text;
        for (int i = 0; i < preStr.length; i++) {
            view = inflater.inflate(R.layout.setting_role_permission_item, null);
            text = (TextView) view.findViewById(R.id.title);
            text.setText(preStr[i]);
            text = (TextView) view.findViewById(R.id.count_permission);
            text.setText("0项");
            int id = R.layout.setting_role_permission_item + i + 1;
            view.setId(id);
            item = new RolePermissionItem(i, preStr[i], tag[i], id, false);
            view.setTag(item);
            view.setOnClickListener(this);
            mBgPremission.addView(view);
        }
        
        if (showing) {
            mBgPremission.setVisibility(View.VISIBLE);
        } else {
            mBgPremission.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
        case R.id.pre_permission_checkbox:
            if (isChecked) {
                mPrePermission.setVisibility(View.VISIBLE);
            } else {
                mPrePermission.setVisibility(View.GONE);
            }
            break;

        case R.id.bg_permission_checkbox:
            if (isChecked) {
                mBgPremission.setVisibility(View.VISIBLE);
            } else {
                mBgPremission.setVisibility(View.GONE);
            }
            break;
        case R.id.checkbox_permission:
            if (isChecked) {
                View sub = buttonView.findViewWithTag(buttonView);
                sub.setVisibility(View.VISIBLE);
            } else {
                View sub = buttonView.findViewWithTag(buttonView);
                sub.setVisibility(View.GONE);
            }
            break;
        }
        
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.count_permission:
            //Bundle bundle = new Bundle();
            //bundle.putSerializable("data", (RolePermissionItem)v.getTag());
            groupPoition = (Integer)v.getTag(R.id.groupPosition);
            childPosition = (Integer)v.getTag(R.id.childPosition);
            if(groupPoition != null && childPosition != null) {
                Intent intent = new Intent();
                intent.setClass(this, RolePermissionShowActivity.class);
                intent.putExtra("data", this.permissions.get(groupPoition).getModuleVoList().get(childPosition));
                startActivityForResult(intent, REQUEST_ACTION_PERMISSION);
            }
            break;
        case R.id.help:
            break;
        case R.id.delete:
            this.delete();
            break;
        case R.id.title_right:
            this.save();
            break;
        }
    }

    private void save() {
        if(StringUtils.isEmpty(this.roleTxt.getStrVal())) {
            new ErrDialog(this, "请输入角色名称").show();
            return;
        }   
        RequestParameter requestParams = new RequestParameter(true);
        requestParams.setUrl(Constants.ROLE_PERMISSION_ROLE_SAVEORUPDATE);  
        requestParams.setParam("operateType", operateType);
        if("add".equals(operateType)){
            role = new RoleVo();
        }else if("edit".equals(operateType)) {
            if(role == null) {
                role = new RoleVo();
            }
            role.setRoleId(roleId);
        }
        role.setRoleName(this.roleTxt.getStrVal());
        try {
            requestParams.setParam("role", new JSONObject(new Gson().toJson(role)));
            //处理全部权限
            if(this.permissions != null) {
                ArrayList<ActionVo> allActions = new ArrayList<ActionVo>();
                for(SystemInfoVo systemInfo : this.permissions) {
                    List<ModuleVo> modules = systemInfo.getModuleVoList();
                    if(modules != null) {
                        for(ModuleVo module : modules) {
                            ArrayList<ActionVo> actions = module.getActionVoList();
                            //如果系统块的权限已经关闭，设置所有的子权限为关闭
                            if(actions != null && !systemInfo.isChoiceFlag()) {
                                for(ActionVo action : actions) {
                                    action.setChoiceFlag(false);
                                    allActions.add(action);
                                }
                            }else if(actions != null) {
                                allActions.addAll(actions);
                            }
                        }
                    }
                   
                }
                
                requestParams.setParam("actionList", new JSONArray(new Gson().toJson(allActions)));
            }
           
        } catch (JSONException e1) {
            e1.printStackTrace();  
            
        }
        new AsyncHttpPost(this, requestParams, RolePermissionsBo.class, new RequestCallback(){

            @Override
            public void onSuccess(Object bo) {
                if("add".equals(operateType)){
                    setResult(RESULT_SAVE);
                }else if ("edit".equals(operateType)) {
                    Intent intent = new Intent();
                    intent.putExtra("role", role);
                    setResult(RESULT_EDIT,intent);
                }
                finish();
            }

            @Override
            public void onFail(Exception e) {
                new ErrDialog(RolePermissionActivity.this, "保存失败");
            }}).execute();
        
    }

    private void delete() {
        final AlertDialog alertDialog = new AlertDialog(this);
        alertDialog.setMessage("确定要删除" + role.getRoleName() + "角色吗？");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finishDelete();
            }
        });
        alertDialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });  
        
    }


    protected void finishDelete() {
          
        RequestParameter requestParams = new RequestParameter(true);
        requestParams.setUrl(Constants.ROLE_PERMISSION_ROLE_DELETE);  
        requestParams.setParam("roleId", roleId);
        new AsyncHttpPost(this, requestParams, RolePermissionsBo.class, new RequestCallback(){

            @Override
            public void onSuccess(Object bo) {
                setResult(RESULT_DELETE);
                finish();
            }

            @Override
            public void onFail(Exception e) {
            }}).execute();
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ACTION_PERMISSION && resultCode == RESULT_OK) {
            ModuleVo module = (ModuleVo)data.getSerializableExtra("module");
            if(groupPoition != null && childPosition != null) {
                ModuleVo moduleVo = this.permissions.get(groupPoition).getModuleVoList().get(childPosition); 
                moduleVo.setActionNameOfModule(module.getActionNameOfModule());
                moduleVo.setCount(module.getCount());
                moduleVo.setActionVoList(module.getActionVoList());
                
                adapter.notifyDataSetChanged();
                
               /* if(moduleVo.getOldCount().intValue() == moduleVo.getCount().intValue()) {
                    itemEditMap.put(moduleVo.getModuleId(), (short)0);
                }else {
                    itemEditMap.put(moduleVo.getModuleId(), (short)1);
                }*/
                if(moduleVo.isModuleChanged()){
                    itemEditMap.put("m" + moduleVo.getModuleId(), (short)1);
                    moduleVo.setChanged(true);
                }else {
                    itemEditMap.put("m" + moduleVo.getModuleId(), (short)0);
                    moduleVo.setChanged(false);
                }
                notifyTitleMode();
            }
        }
    }
    private class RolePermissionAdapter extends BaseAdapter implements StickyListHeadersAdapter {
        private List<SystemInfoVo> systemInfos;
        private List<ModuleVo> modules;
        private LayoutInflater inflater;
        public RolePermissionAdapter(Context context, List<SystemInfoVo> systemInfos, List<ModuleVo> modules) {
            this.inflater = LayoutInflater.from(context);  
            this.systemInfos = systemInfos; 
            this.modules = modules;
        }

        @Override
        public int getCount() {
            return modules.size();
        }

        @Override
        public Object getItem(int position) {
            return modules.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position,
                            View convertView,
                            ViewGroup parent) {
              
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.setting_role_permission_item, parent, false);
                holder.titleTxt = (TextView) convertView.findViewById(R.id.title);
                holder.subHeadTxt = (TextView) convertView.findViewById(R.id.subhead);
                holder.countPermissionTxt = (TextView) convertView.findViewById(R.id.count_permission);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ModuleVo module = (ModuleVo)this.getItem(position);
            holder.titleTxt.setText(module.getModuleName());
            holder.subHeadTxt.setText(module.getActionNameOfModule());
            holder.countPermissionTxt.setText(String.valueOf(module.getCount()) + "项");
            if(module.getCount() != null && module.getCount() > 0) {
                holder.subHeadTxt.setVisibility(View.VISIBLE);
            }else {
                holder.subHeadTxt.setVisibility(View.GONE);
            }
            SystemInfoVo systemInfo = module.getSystemInfoVo();
            
           
            return convertView;
        }

        @Override
        public View getHeaderView(int position,
                                  View convertView,
                                  ViewGroup parent) {
              
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.setting_role_permission_header, parent, false);
                holder.titleTxt = (TextView) convertView.findViewById(R.id.txt_system_info_name);
                holder.permissionCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox_permission);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            
            ModuleVo module = (ModuleVo)this.getItem(position);
            SystemInfoVo systemInfo = module.getSystemInfoVo();
            holder.titleTxt.setText(systemInfo.getSystemName());
            if(systemInfo.isChoiceFlag()) {
                holder.permissionCheckBox.setChecked(true);
            }else {
                holder.permissionCheckBox.setChecked(false);
            }
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            ModuleVo module = (ModuleVo)this.getItem(position);
            SystemInfoVo systemInfo = module.getSystemInfoVo();
            return systemInfo.getSystemId();
        }
        
        private class ViewHolder {
            TextView titleTxt;
            TextView subHeadTxt;
            TextView countPermissionTxt;
        }
        private class HeaderViewHolder {
            TextView titleTxt;
            CheckBox permissionCheckBox;
        }
    }

    private class RolePermissionExpandableAdapter extends BaseExpandableListAdapter   {
        private List<SystemInfoVo> systemInfos;
        private List<ModuleVo> modules;
        private LayoutInflater inflater;
        public RolePermissionExpandableAdapter(Context context, List<SystemInfoVo> systemInfos, List<ModuleVo> modules) {
            this.inflater = LayoutInflater.from(context);  
            this.systemInfos = systemInfos; 
            this.modules = modules;
        }

        @Override
        public int getGroupCount() {
            return systemInfos.size();
        }
        @Override
        public int getChildrenCount(int groupPosition) {
            return systemInfos.get(groupPosition).getModuleVoList().size();
        }
        @Override
        public Object getGroup(int groupPosition) {
            return systemInfos.get(groupPosition);
        }
        @Override
        public Object getChild(int groupPosition,
                               int childPosition) {
              
            return systemInfos.get(groupPosition).getModuleVoList().get(childPosition);
        }
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        @Override
        public long getChildId(int groupPosition,
                               int childPosition) {
            return childPosition;
        }
        @Override
        public boolean hasStableIds() {
              
            return false;
        }
        @Override
        public View getGroupView(int groupPosition,
                                 boolean isExpanded,
                                 View convertView,
                                 ViewGroup parent) {
              
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.setting_role_permission_header, parent, false);
                holder.titleTxt = (TextView) convertView.findViewById(R.id.txt_system_info_name);
                holder.saveTag = (TextView) convertView.findViewById(R.id.saveTag);
                holder.permissionCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox_permission);
                holder.permissionCheckBox.setOnCheckedChangeListener(checkedChangeListener);
                //holder.permissionCheckBox.setOnClickListener(clickedListener);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            
            SystemInfoVo group = (SystemInfoVo)this.getGroup(groupPosition);
            holder.titleTxt.setText(group.getSystemName());
            holder.permissionCheckBox.setTag(groupPosition);
            if(group.isChoiceFlag()) {
                holder.permissionCheckBox.setChecked(true);
            }else {
                holder.permissionCheckBox.setChecked(false);
            }
            
            if(group.isOldChoiceFlag() == group.isChoiceFlag()){
                holder.saveTag.setVisibility(View.GONE);
            }else {
                holder.saveTag.setVisibility(View.VISIBLE);
            }
            
            return convertView;
        }
        @Override
        public View getChildView(int groupPosition,
                                 int childPosition,
                                 boolean isLastChild,
                                 View convertView,
                                 ViewGroup parent) {
              
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.setting_role_permission_item, parent, false);
                holder.titleTxt = (TextView) convertView.findViewById(R.id.title);
                holder.subHeadTxt = (TextView) convertView.findViewById(R.id.subhead);
                holder.saveTag = (TextView) convertView.findViewById(R.id.saveTag);
                holder.countPermissionTxt = (TextView) convertView.findViewById(R.id.count_permission);
                holder.countPermissionTxt.setOnClickListener(RolePermissionActivity.this);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ModuleVo module = (ModuleVo)this.getChild(groupPosition, childPosition);
            holder.titleTxt.setText(module.getModuleName());
            holder.subHeadTxt.setText(module.getActionNameOfModule());
            holder.countPermissionTxt.setText(String.valueOf(module.getCount()) + "项");
            holder.countPermissionTxt.setTag(R.id.groupPosition,groupPosition);
            holder.countPermissionTxt.setTag(R.id.childPosition,childPosition);
            if(module.getCount() != null && module.getCount() > 0) {
                holder.subHeadTxt.setVisibility(View.VISIBLE);
            }else {
                holder.subHeadTxt.setVisibility(View.GONE);
            }
            if(module.isChanged()) {
                holder.saveTag.setVisibility(View.VISIBLE);
            }else {
                holder.saveTag.setVisibility(View.GONE);
            }
            
           
            return convertView;
        }
        @Override
        public boolean isChildSelectable(int groupPosition,
                                         int childPosition) {
              
            return false;
        }
        private class ViewHolder {
            TextView titleTxt;
            TextView subHeadTxt;
            TextView countPermissionTxt;
            TextView saveTag;
        }
        private class HeaderViewHolder {
            TextView titleTxt;
            CheckBox permissionCheckBox;
            TextView saveTag;
        }
        private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                Integer position = (Integer)buttonView.getTag();  
                if(position != null) {
                    SystemInfoVo systemInfo = systemInfos.get(position);
                    if(isChecked) {
                        systemInfo.setChoiceFlag(true);
                        /*if(systemInfo.getModuleVoList() != null && !systemInfo.getModuleVoList().isEmpty()) {
                            for(ModuleVo module : systemInfo.getModuleVoList()) {
                                if(module.getActionVoList() != null) {
                                    module.setCount(module.getActionVoList().size());
                                }
                            }
                        }*/
                        listView.expandGroup(position);
                    } else {
                        systemInfo.setChoiceFlag(false);
                        /*if(systemInfo.getModuleVoList() != null && !systemInfo.getModuleVoList().isEmpty()) {
                            for(ModuleVo module : systemInfo.getModuleVoList()) {
                                module.setCount(0);
                            }
                        }*/
                        listView.collapseGroup(position);
                    }
                    
                    if(systemInfo.isChoiceFlag() == systemInfo.isOldChoiceFlag()) {
                        itemEditMap.put("s" + systemInfo.getSystemInfoId(), (short)0);
                    }else {
                        itemEditMap.put("s" + systemInfo.getSystemInfoId(), (short)1);
                    }
                    if(!systemInfo.isOldChoiceFlag() && !isChecked) {
                        if(systemInfo.getModuleVoList() != null) {
                            for(ModuleVo  module : systemInfo.getModuleVoList()) {
                                if(itemEditMap.containsKey(module.getModuleId())) {
                                    itemEditMap.put("m" + module.getModuleId(), (short)0);
                                }
                            }
                        }
                       
                    }
                    notifyTitleMode();
                }
                
            }
            
        };
       
        
    }
    private void notifyTitleMode() {
        
        Collection<Short> coll = itemEditMap.values();  
        if(coll.contains((short)1) && super.mBack.getVisibility() == View.VISIBLE) {
            super.change2saveMode();
        }else if(!coll.contains((short)1) && super.mRight.getVisibility() == View.VISIBLE) {
            super.showBackbtn();
        }
        
    }
}
