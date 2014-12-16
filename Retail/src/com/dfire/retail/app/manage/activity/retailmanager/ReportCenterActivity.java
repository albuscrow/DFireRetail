package com.dfire.retail.app.manage.activity.retailmanager;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.usermanager.EmployeeInfoActivity;
import com.dfire.retail.app.manage.adapter.StoreManagerAdapter;
import com.dfire.retail.app.manage.adapter.StoreManagerItem;

public class ReportCenterActivity extends TitleActivity {

    ArrayList<StoreManagerItem> mList;
    private ListView mListView;
    private StoreManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.storemanager);

        setTitleText("报表中心");
        showBackbtn();

        mListView = (ListView) findViewById(R.id.storemanager_main_list);

        mList = new ArrayList<StoreManagerItem>();
        //
//        mList.add(new StoreManagerItem(R.drawable.dianjia, "店家信息", "设置本店基本信息",
//                RetailInfoActivity.class));
//        mList.add(new StoreManagerItem(R.drawable.guanli, "员工管理",
//                "已添加2个职位，5个员工", EmployeeInfoActivity.class));
        mList.add(new StoreManagerItem(R.drawable.jiaojieban, "交接班记录", "查询交接交接班",
                RetailLogBookActivity.class));
        //
        adapter = new StoreManagerAdapter(ReportCenterActivity.this, mList);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ReportCenterActivity.this, mList
                        .get(arg2).getDestClass());
                startActivity(intent);
            }
        });
    }
}
