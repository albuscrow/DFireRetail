package com.dfire.retail.app.manage.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.global.ShopInfo;
import com.dfire.retail.app.manage.global.UserInfo;


public class AccountInfoSettingActivity extends TitleActivity implements OnClickListener {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_account);
        setTitle(R.string.account_info);
        
        ShopInfo shopInfo=RetailApplication.getmShopInfo();
        //shopInfo.getCountyId()
        UserInfo userInfo=RetailApplication.getmUserInfo();
        
        //原信息从login中拿去
        View view = findViewById(R.id.user_name);
        TextView text = (TextView) view.findViewById(R.id.main);
        text.setText(R.string.account_name);
        text = (TextView) view.findViewById(R.id.second);
        text.setText(userInfo.getUserId());
        
//        view = findViewById(R.id.shop_number);
        text = (TextView) view.findViewById(R.id.main);
        text.setText(R.string.account_name_number);
        text = (TextView) view.findViewById(R.id.second);
        text.setText(shopInfo.getShopId());
        
        view = findViewById(R.id.password);
        view.setOnClickListener(this);
        text = (TextView) view.findViewById(R.id.main);
        text.setText(R.string.account_passward);
        
        view = findViewById(R.id.name);
        text = (TextView) view.findViewById(R.id.main);
        text.setText(R.string.user_name);
        text = (TextView) view.findViewById(R.id.second);
        text.setText(userInfo.getUserName());
        
        view = findViewById(R.id.shop_name);
        text = (TextView) view.findViewById(R.id.main);
        text.setText(R.string.shop_name);
        text = (TextView) view.findViewById(R.id.second);
        text.setText(shopInfo.getShopName());
        
        view = findViewById(R.id.shop_phone);
        text = (TextView) view.findViewById(R.id.main);
        text.setText(R.string.shop_phone_number);
        text = (TextView) view.findViewById(R.id.second);
        String phoneStr="";
        if(!(CommonUtils.isEmpty(shopInfo.getPhone1()) ||shopInfo.getPhone1().trim().equals("null")))
        	phoneStr+=shopInfo.getPhone1();
      
        if(!(CommonUtils.isEmpty(shopInfo.getPhone2()) || shopInfo.getPhone2().trim().equals("null"))){
        	if(!CommonUtils.isEmpty(phoneStr))
        		phoneStr+="  "+shopInfo.getPhone2();
        	else
        		phoneStr+=shopInfo.getPhone2();
        }
        text.setText(phoneStr);

        view = findViewById(R.id.logo);
        text = (TextView) view.findViewById(R.id.main);
        text.setText(R.string.shop_img);
        text = (TextView) view.findViewById(R.id.second);
        text.setBackgroundResource(R.drawable.setting_account_info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.password:
            Intent intent = new Intent();
            intent.setClass(this, AccountPWActivity.class);
            startActivity(intent);
            break;
        }
    }

}
