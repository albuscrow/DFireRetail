package com.dfire.retail.app.manage.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.RolePermissionItem;



public class RolePermissionActivity extends TitleActivity 
    implements OnCheckedChangeListener, OnClickListener{
    
    private LinearLayout mPrePermission;
    private LinearLayout mBgPremission;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_role_permission);
        
        String name = getIntent().getStringExtra("role");
        setTitle(name);
        
        TextView textName = (TextView) findViewById(R.id.role);
        textName.setText(name);
        
        CheckBox pre = (CheckBox) findViewById(R.id.pre_permission_checkbox);
        pre.setOnCheckedChangeListener(this);
        CheckBox bg = (CheckBox) findViewById(R.id.bg_permission_checkbox);
        bg.setOnCheckedChangeListener(this);
        
        initPre(pre.isChecked());
        initBg(bg.isChecked());
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
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, RolePermissionShowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", (RolePermissionItem)v.getTag());
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            RolePermissionItem item = (RolePermissionItem)data.getSerializableExtra("data");
            View view = item.mPreOrBg ? mPrePermission.findViewById(item.mId) 
                    : mBgPremission.findViewById(item.mId);
            TextView text = (TextView) view.findViewById(R.id.title);
            text.setText(item.mName);
            text = (TextView) view.findViewById(R.id.count_permission);
            text.setText(new StringBuilder().append(item.mChoice.size()).append("项").toString());
        }
    }
    
}
