package com.dfire.retail.app.manage.activity.setting;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;

public class PayWayTypeActivity extends Activity implements OnItemClickListener {
    
    private List<String> mData;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_payway_type);
        
        mData = Arrays.asList(getResources().getStringArray(R.array.setting_payway_type_array));
        ListView list = (ListView) findViewById(R.id.type_list);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
                mData));
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Intent intent = new Intent();
        intent.putExtra("result", mData.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }
}
