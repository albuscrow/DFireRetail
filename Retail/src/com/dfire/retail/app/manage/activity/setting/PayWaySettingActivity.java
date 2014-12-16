package com.dfire.retail.app.manage.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.PayWayAdapter;
import com.dfire.retail.app.manage.adapter.PayWayElement;

/**
 * 付款方式的主界面
 * @author 刘思海
 */
public class PayWaySettingActivity extends TitleActivity implements OnItemClickListener,
        OnClickListener {
    
    private static final int CHANGE_PAYWAY = 1;
    private static final int CREATE_NEW_PAYWAY = 2;
    
    private PayWayAdapter mAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_payway);
        setTitle(R.string.setting_payway);

        ListView list = (ListView) findViewById(R.id.setting_payway_list);
        mAdapter = new PayWayAdapter(this);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(this);
        
        findViewById(R.id.setting_payway_help).setOnClickListener(this);
        findViewById(R.id.setting_payway_add).setOnClickListener(this);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Intent intent = new Intent();
        intent.setClass(this, PayWayShowOrCreateActivity.class);
        intent.putExtra(PayWayShowOrCreateActivity.CHANGE_OR_ADD, true);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PayWayShowOrCreateActivity.PAYWAY_DATA, (PayWayElement)mAdapter.getItem(position));
        intent.putExtras(bundle);
        startActivityForResult(intent, CHANGE_PAYWAY);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
        case R.id.setting_payway_help:
            intent.setClass(this, SettingHelpActivity.class);
            startActivity(intent);
            break;

        case R.id.setting_payway_add:
            intent.setClass(this, PayWayShowOrCreateActivity.class);
            intent.putExtra(PayWayShowOrCreateActivity.CHANGE_OR_ADD, false);
            startActivityForResult(intent, CREATE_NEW_PAYWAY);
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            PayWayElement e = (PayWayElement)data.getSerializableExtra(PayWayShowOrCreateActivity.PAYWAY_DATA);
            if (CHANGE_PAYWAY == requestCode) {
                mAdapter.changeElement(e);
            } else if (CREATE_NEW_PAYWAY == requestCode) {
                mAdapter.addElement(e);
            }
        } else if (resultCode == PayWayShowOrCreateActivity.PAYWAY_DELETE) {//仅在CHANGE_PAYWAY时发生
            PayWayElement e = (PayWayElement)data.getSerializableExtra(PayWayShowOrCreateActivity.PAYWAY_DATA);
            mAdapter.deleteElement(e.mPosition);
        }
    }
}
