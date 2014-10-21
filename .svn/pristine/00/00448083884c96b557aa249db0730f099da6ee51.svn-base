package com.dfire.retail.app.manage.activity.setting;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.ChoiceAdapter;
import com.dfire.retail.app.manage.adapter.RolePermissionItem;
import com.dfire.retail.app.manage.adapter.RolePermissionSubItem;

public class RolePermissionShowActivity extends TitleActivity 
    implements OnItemClickListener, OnClickListener {
    
    private ListView mList;
    private RolePermissionItem mItem;
    private ChoiceAdapter mAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_permission_show);
        
        mItem = (RolePermissionItem)getIntent().getSerializableExtra("data");
        String[] str = getResources().getStringArray(mItem.mTag);
        if (mItem.mChoice == null) {
            ArrayList<RolePermissionSubItem> choice = new ArrayList<RolePermissionSubItem>();
            for (int i = 1; i < str.length; i++) {
                choice.add(new RolePermissionSubItem(i, str[i]));
            }
            mItem.mChoice = choice;
        }
        
        View view = findViewById(R.id.cancel_title);
        view.findViewById(R.id.title_left_btn).setOnClickListener(this);
        TextView text = (TextView) view.findViewById(R.id.titleText);
        text.setText(str[0]);
        view.findViewById(R.id.title_right_btn).setOnClickListener(this);
        
        findViewById(R.id.batch).setOnClickListener(this);
        findViewById(R.id.uncheck_all).setOnClickListener(this);
        
        mList = (ListView) findViewById(R.id.setting_list);
        mAdapter = new ChoiceAdapter(this, mItem.mChoice, R.layout.setting_permission_item);
        mList.setAdapter(mAdapter);
        mList.requestFocus();
        mList.setClickable(true);
        mList.setOnItemClickListener(this);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
//        ViewHolder holder = (ViewHolder) view.getTag();
//        if (mItem.mChoice.get(position).mChecked) {
//            holder.mCheck.setBackgroundResource(R.drawable.ico_uncheck);
//            mItem.mChoice.get(position).mChecked = false;
//        } else {
//            holder.mCheck.setBackgroundResource(R.drawable.ico_check);
//            mItem.mChoice.get(position).mChecked = true;
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.title_left_btn:
            finish();
            break;

        case R.id.title_right_btn:
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", mItem);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
            break;
            
        case R.id.batch:
            for (int i = 0; i < mItem.mChoice.size(); i++) {
                mItem.mChoice.get(i).mChecked = true;
            }
            mAdapter.notifyDataSetChanged();
            break;
            
        case R.id.uncheck_all:
            for (int i = 0; i < mItem.mChoice.size(); i++) {
                mItem.mChoice.get(i).mChecked = false;
            }
            mAdapter.notifyDataSetChanged();
            break;
        }
    }
}
