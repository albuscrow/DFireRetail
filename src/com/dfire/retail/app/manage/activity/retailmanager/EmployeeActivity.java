package com.dfire.retail.app.manage.activity.retailmanager;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.StoreManagerAdapter;
import com.dfire.retail.app.manage.adapter.StoreManagerItem;

public class EmployeeActivity extends TitleActivity {

    ArrayList<StoreManagerItem> mList;
    private ListView mListView;
    private StoreManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.storemanager);

        setTitleText("员工");
        showBackbtn();

        mListView = (ListView) findViewById(R.id.storemanager_main_list);

        mList = new ArrayList<StoreManagerItem>();
        //
        mList.add(new StoreManagerItem(R.drawable.jiaojieban, "角色权限", "已添加2个角色",
                RetailLogBookActivity.class));
        mList.add(new StoreManagerItem(R.drawable.setting_account_info, "员工管理", "已添加5个员工",
                EmployeeInfoActivity.class));
        //
        adapter = new StoreManagerAdapter(EmployeeActivity.this, mList);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(EmployeeActivity.this, mList
                        .get(arg2).getDestClass());
                startActivity(intent);
            }
        });
    }
}
