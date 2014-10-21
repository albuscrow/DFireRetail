package com.dfire.retail.app.manage.activity.setting;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.RolePermissionSettingAdapter;

public class RolePermissionSettingActivity extends TitleActivity implements OnItemClickListener, OnClickListener {

    private String[] mData;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_role);
        setTitle(R.string.setting_role);
        
        mData = getResources().getStringArray(R.array.setting_role);
        ListView list = (ListView) findViewById(R.id.setting_role_list);
        list.setAdapter(new RolePermissionSettingAdapter(this, Arrays.asList(mData), R.layout.setting_role_item));
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Intent intent = new Intent();
        intent.setClass(this, RolePermissionActivity.class);
        intent.putExtra("role", mData[position]);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

}