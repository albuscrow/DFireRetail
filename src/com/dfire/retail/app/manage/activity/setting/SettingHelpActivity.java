package com.dfire.retail.app.manage.activity.setting;

import android.os.Bundle;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;


public class SettingHelpActivity extends TitleActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_payway_help);
        setTitle(R.string.help);
    }
}
