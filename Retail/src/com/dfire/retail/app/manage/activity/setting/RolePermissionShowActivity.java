package com.dfire.retail.app.manage.activity.setting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.stockmanager.StockCheckOverviewActivity;
import com.dfire.retail.app.manage.adapter.ChoiceAdapter;
import com.dfire.retail.app.manage.adapter.RolePermissionItem;
import com.dfire.retail.app.manage.adapter.RolePermissionSubItem;
import com.dfire.retail.app.manage.common.BillStatusDialog;
import com.dfire.retail.app.manage.data.ActionVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ModuleVo;
import com.dfire.retail.app.manage.data.StockCheckArea;
import com.dfire.retail.app.manage.data.SystemInfoVo;
import com.dfire.retail.app.manage.util.DBHelper;
import com.dfire.retail.app.manage.util.StringUtils;
/**
 * 项目名称：Retail  
 * 类名称：RolePermissionShowActivity  
 * 类描述：   角色权限-功能权限详情
 * 创建时间：2014年11月25日 下午2:37:42  
 * @author chengzi  
 * @version 1.0
 */
public class RolePermissionShowActivity extends TitleActivity 
    implements  OnClickListener {
    
    private ExpandableListView listView;
    //private RolePermissionItem mItem;
    private RolePermissionShowAdapter adapter;
    
    private ModuleVo module;
    private BillStatusDialog billStatusDialog;
    private List<DicVo> dicVos = new ArrayList<DicVo>();
    private Map<String,Short> itemEditMap = new HashMap<String,Short>();
    private Integer position;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_permission_show);
        module = (ModuleVo)getIntent().getSerializableExtra("data");
        this.setTitleText(module.getModuleName() + "权限");
        this.showBackbtn();
        this.mRight.setOnClickListener(this);
        /*String[] str = getResources().getStringArray(mItem.mTag);
        if (mItem.mChoice == null) {
            ArrayList<RolePermissionSubItem> choice = new ArrayList<RolePermissionSubItem>();
            for (int i = 1; i < str.length; i++) {
                choice.add(new RolePermissionSubItem(i, str[i]));
            }
            mItem.mChoice = choice;
        }*/
        
        this.billStatusDialog = new BillStatusDialog(this,dicVos);//权限
        
        initList();
        listView = (ExpandableListView) findViewById(R.id.setting_list);
        adapter = new RolePermissionShowAdapter(this, module.getActionVoList());
        listView.setAdapter(adapter);
        listView.setGroupIndicator(null);
        
        initData();
    }
    
    private void initList() {
          
        if(module.getActionVoList() != null) {
            for(int i=0,j=module.getActionVoList().size();i<j;i++) {
                ActionVo action = module.getActionVoList().get(i);
                if(action.getChoiceFlag() == null) {
                    action.setChoiceFlag(false);
                }
                action.setOldChoiceFlag(action.getChoiceFlag());
                //记录最开始的状态，用于module改变的依据
                if(action.getOriginChoiceFlag() == null) {
                    action.setOriginChoiceFlag(action.getChoiceFlag());
                }
                if(action.getActionDataType() == null &&  action.getDicVoList() != null &&  action.getDicVoList().size() > 0) {
                    DicVo dic = action.getDicVoList().get(0);
                    action.setActionDataType(dic.getVal());
                    action.setOldActionDataType(dic.getVal());
                }else {
                    action.setOldActionDataType(action.getActionDataType());
                }
                if(action.getOriginActionDataType() == null) {
                    action.setOriginActionDataType(action.getActionDataType());
                }
            }
        }   
        
    }

    private void initData() {
          
        if(module.getActionVoList() != null) {
            for(int i=0,j=module.getActionVoList().size();i<j;i++) {
                ActionVo action = module.getActionVoList().get(i);
                if(action.getChoiceFlag() != null && action.getChoiceFlag()) {
                    listView.expandGroup(i);
                }
            }
        }  
        
    }

    private void showDataPermissionDialog(final ActionVo vo) {
        this.dicVos.clear();
        this.dicVos.addAll(vo.getDicVoList());
        this.billStatusDialog.show();
        this.billStatusDialog.getmTitle().setText((vo.getActionName() + "数据"));
        this.billStatusDialog.updateType(vo.getActionDataType());
        this.billStatusDialog.getConfirmButton().setOnClickListener(this);
        this.billStatusDialog.getCancelButton().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            
        case R.id.count_permission:
            position = (Integer)v.getTag();
            if(position != null) {
                this.showDataPermissionDialog(module.getActionVoList().get(position));
            }
            break;
        case R.id.card_type_confirm:
            billStatusDialog.dismiss();
            Integer index = billStatusDialog.getCurrentData();
            DicVo dicVo = dicVos.get(index);
            if(position != null) {
                ActionVo  action = module.getActionVoList().get(position);
                action.setActionDataType(dicVo.getVal());
                action.setActionDataName(dicVo.getName());
                
                adapter.notifyDataSetChanged();
                
                if(action.getActionDataType() != null && action.getOldActionDataType() != null && !(action.getOldActionDataType().intValue() == action.getActionDataType().intValue())) {
                    itemEditMap.put(action.getActionId() + "dic", (short)1);
                }else {
                    itemEditMap.put(action.getActionId() + "dic", (short)0);
                }
                this.notifyTitleMode();
            }
            
            break;
        case R.id.card_type_cancel:
            billStatusDialog.dismiss();
            break;
        case R.id.title_right:
            this.save();
            break;
        }
    }
    
    private void save() {
        if(module.getActionVoList() != null) {
            int count = 0;
            StringBuffer name = new StringBuffer();
            for(int i=0,j=module.getActionVoList().size();i<j;i++) {
                ActionVo action = module.getActionVoList().get(i);
                if(action.getChoiceFlag() != null && action.getChoiceFlag()) {
                    count++;
                    name.append(action.getActionName()).append(",");
                }
            }
            module.setCount(count);
            if(name.length() > 0) {
                String actionNameOfModule = name.deleteCharAt(name.length() - 1).toString();
                module.setActionNameOfModule(actionNameOfModule);
            }
            
        }    
        Intent intent = new Intent();
        intent.putExtra("module", module);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private class RolePermissionShowAdapter extends BaseExpandableListAdapter   {
        private List<ActionVo> systemInfos;
        private LayoutInflater inflater;
        public RolePermissionShowAdapter(Context context, List<ActionVo> systemInfos) {
            this.inflater = LayoutInflater.from(context);  
            this.systemInfos = systemInfos; 
        }

        @Override
        public int getGroupCount() {
            return systemInfos == null ? 0 : systemInfos.size();
        }
        @Override
        public int getChildrenCount(int groupPosition) {
            ActionVo action = systemInfos.get(groupPosition);
            return action.getActionType() == null || action.getActionType()!= 2 || action.getDicVoList() == null || 
                    action.getDicVoList().isEmpty() ? 0 : 1;
        }
        @Override
        public Object getGroup(int groupPosition) {
            return systemInfos.get(groupPosition);
        }
        @Override
        public Object getChild(int groupPosition,
                               int childPosition) {
              
            return systemInfos.get(groupPosition);
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
                holder.saveTagTxt = (TextView) convertView.findViewById(R.id.saveTag);
                holder.permissionCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox_permission);
                holder.permissionCheckBox.setOnCheckedChangeListener(checkedChangeListener);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            
            ActionVo group = (ActionVo)this.getGroup(groupPosition);
            holder.titleTxt.setText(group.getActionName());
            holder.permissionCheckBox.setTag(groupPosition);
            if(group.getChoiceFlag() != null && group.getChoiceFlag()) {
                holder.permissionCheckBox.setChecked(true);
            }else {
                holder.permissionCheckBox.setChecked(false);
            }
            if(group.getChoiceFlag() != null && group.getOldChoiceFlag() != null &&  !group.getChoiceFlag().booleanValue()  == group.getOldChoiceFlag().booleanValue()) {
                holder.saveTagTxt.setVisibility(View.VISIBLE);
            }else {
                holder.saveTagTxt.setVisibility(View.GONE);
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
                convertView = inflater.inflate(R.layout.setting_role_permission_show_item, parent, false);
                holder.titleTxt = (TextView) convertView.findViewById(R.id.title);
                holder.subHeadTxt = (TextView) convertView.findViewById(R.id.subhead);
                holder.saveTagTxt = (TextView) convertView.findViewById(R.id.saveTag);
                holder.countPermissionTxt = (TextView) convertView.findViewById(R.id.count_permission);
                holder.countPermissionTxt.setOnClickListener(RolePermissionShowActivity.this);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ActionVo group = (ActionVo)this.getGroup(groupPosition);
            holder.titleTxt.setText(group.getActionName() + "数据");
            if(!StringUtils.isEmpty(group.getActionDataName())) {
                holder.countPermissionTxt.setText(group.getActionDataName());
            }else{
                if(group.getActionDataType() == null) {
                    DicVo dic = group.getDicVoList().get(0);
                    holder.countPermissionTxt.setText(dic.getName());
                    group.setActionDataName(dic.getName());
                    group.setActionDataType(dic.getVal());
                    group.setOldActionDataType(dic.getVal());
                }else{
                    for(DicVo dic : group.getDicVoList()) {
                        if(dic.getVal().intValue() == group.getActionDataType().intValue()) {
                            holder.countPermissionTxt.setText(dic.getName());
                            group.setActionDataName(dic.getName());
                            group.setActionDataType(dic.getVal());
                            break;
                        }
                    }
                }
                
            }
            if(group.getActionDataType() != null && group.getOldActionDataType() != null && !(group.getOldActionDataType().intValue() == group.getActionDataType().intValue())) {
                holder.saveTagTxt.setVisibility(View.VISIBLE);
            }else {
                holder.saveTagTxt.setVisibility(View.GONE);
            }
            holder.countPermissionTxt.setTag(groupPosition);
            
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
            TextView saveTagTxt;
        }
        private class HeaderViewHolder {
            TextView titleTxt;
            CheckBox permissionCheckBox;
            TextView saveTagTxt;
        }
        private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                Integer position = (Integer)buttonView.getTag();  
                if(position != null) {
                    ActionVo group = (ActionVo)getGroup(position);
                    if(isChecked) {
                        group.setChoiceFlag(true);
                        listView.expandGroup(position);
                    } else {
                        group.setChoiceFlag(false);
                        listView.collapseGroup(position);
                    }
                    if(group.getChoiceFlag() != null && group.getOldChoiceFlag() != null &&  !group.getChoiceFlag().booleanValue()  == group.getOldChoiceFlag().booleanValue()) {
                        itemEditMap.put(group.getActionId(), (short)1);
                    }else {
                        itemEditMap.put(group.getActionId(), (short)0);
                    }
                    if(!group.getOldChoiceFlag() && !isChecked) {
                        itemEditMap.put(group.getActionId() + "dic", (short)0);
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
