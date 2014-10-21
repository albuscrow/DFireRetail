package com.dfire.retail.app.manage.activity.retailmanager;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.EmployeeInfoDetailAdapter;
import com.dfire.retail.app.manage.adapter.EmployeeInfoDetailItem;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.UserVo;
import com.dfire.retail.app.manage.util.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class EmployeeDetailActivity extends TitleActivity implements
        OnClickListener {

    private UserVo mEmployee;
    
    ArrayList<EmployeeInfoDetailItem> mList;
    private ListView mListView;
    private EmployeeInfoDetailAdapter mAdapter;

    private PopupWindow _shopPopupWindow;
    private PopupWindow _rolePopupWindow;
    private PopupWindow _sexPopupWindow;
    private PopupWindow _identityPopupWindow;
    private DatePickerDialog _inDateDialog;
    private DatePickerDialog _birthDayDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.employe_detal_info);
        change2saveFinishMode().setOnClickListener(this);
        setRightBtn(R.drawable.save);
        
        Intent intent = getIntent();
        mEmployee = (UserVo) intent.getSerializableExtra("data");
        setTitleText(mEmployee.getName());
        
        ImageView logo = (ImageView) findViewById(R.id.start_date_time);
        logo.setImageResource(mEmployee.getLogoId());
        
        findViewById(R.id.delete).setOnClickListener(this);
        
        initEmployeeInfo();
        mListView = (ListView) findViewById(R.id.employee_info_detail_list);
        mAdapter = new EmployeeInfoDetailAdapter(EmployeeDetailActivity.this, mList);
        mListView.setAdapter(mAdapter);
        Utility.setListViewHeightBasedOnChildren(mListView);
    }

    private void initEmployeeInfo() {
        mList = new ArrayList<EmployeeInfoDetailItem>();
        mList.add(new EmployeeInfoDetailItem("员工姓名", mEmployee.getName(), false));
        mList.add(new EmployeeInfoDetailItem("员工工号", mEmployee.getStaffId(), false));
        mList.add(new EmployeeInfoDetailItem("登陆密码", "******", false));//到设置页面修改
        mList.add(new EmployeeInfoDetailItem("员工角色", mEmployee.getRoleName(), true, 1));
        mList.add(new EmployeeInfoDetailItem("所属门店", mEmployee.getShopName(), true, 2));
        mList.add(new EmployeeInfoDetailItem("入职时间", mEmployee.getInDateStr(), true, 3));
        mList.add(new EmployeeInfoDetailItem("联系电话", mEmployee.getMobile(), false));
        mList.add(new EmployeeInfoDetailItem("性别", UserInfoInit.getSex(mEmployee.getSex().intValue()), true, 4));
        mList.add(new EmployeeInfoDetailItem("生日", mEmployee.getBirthdayStr(), true, 5));
        mList.add(new EmployeeInfoDetailItem("证件类型", UserInfoInit.getIdentityType(mEmployee.getIdentityTypeId().intValue()), true, 6));
        mList.add(new EmployeeInfoDetailItem("证件号码", mEmployee.getIdentityNo(), false));
        mList.add(new EmployeeInfoDetailItem("住址", mEmployee.getAddress(), false));
    }
    
    private PopupWindow initPopupWindow(final TextView textView, List<String> data) {
        ListView listView = (ListView) LayoutInflater.from(this).inflate(R.layout.list, null);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.text, data));

        float dp = getResources().getDisplayMetrics().density;
        final PopupWindow popupWindow = new PopupWindow(listView, (int)(150 * dp), (int)(200 * dp),
                true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                popupWindow.dismiss();
                textView.setText(((TextView)view).getText());
                
                int listPosition = Integer.parseInt(String.valueOf(textView.getTag()));
                switch (mList.get(listPosition).getTag()) {
                case 1:
                    RoleVo role = UserInfoInit.getRoleList().get(position);
                    mEmployee.setRoleName(role.getRoleName());
                    mEmployee.setRoleId(role.getRoleId());
                    break;
                    
                case 2:
                    ShopVo shop = UserInfoInit.getShopList().get(position);
                    mEmployee.setShopId(shop.getShopId());
                    mEmployee.setShopName(shop.getShopName());
                    break;
                    
                case 4:
                    DicVo sex = UserInfoInit.getSexList().get(position);
                    mEmployee.setSex(sex.getDicId());
                    break;
                    
                case 6:
                    DicVo identityType = UserInfoInit.getIdentityTypeList().get(position);
                    mEmployee.setIdentityTypeId(identityType.getDicId());
                    break;
                }
            }
        });
        
        return popupWindow;
    }
    
    public void ClickListener(View v) {
        int position = Integer.parseInt(String.valueOf(v.getTag()));
        switch (mList.get(position).getTag()) {
        case 1:
            if (_rolePopupWindow == null) {
                _rolePopupWindow = initPopupWindow((TextView)v, UserInfoInit.getRoleListWithStirng());
            }
            _rolePopupWindow.showAsDropDown(v);
            break;
            
        case 2:
            if (_shopPopupWindow == null) {
                _shopPopupWindow = initPopupWindow((TextView)v, UserInfoInit.getShopListWithString());
            }
            _shopPopupWindow.showAsDropDown(v);
            break;
            
        case 3:
            if (_inDateDialog == null) {
                final TextView textView = ((TextView)v);
                Calendar calendar = Calendar.getInstance();
                _inDateDialog = new DatePickerDialog(this, 
                        new OnDateSetListener() {
                            
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                    int dayOfMonth) {
                                String str = new StringBuilder().append(year).append("-")
                                .append(monthOfYear + 1).append("-").append(dayOfMonth).toString();
                                textView.setText(str);
                                mEmployee.setInDate(str);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }
            _inDateDialog.show();
            break;
            
        case 4:
            if (_sexPopupWindow == null) {
                _sexPopupWindow = initPopupWindow((TextView)v, UserInfoInit.getSexListWithString());
            }
            _sexPopupWindow.showAsDropDown(v);
            break;
            
        case 5:
            if (_birthDayDialog == null) {
                final TextView textView = ((TextView)v);
                Calendar calendar = Calendar.getInstance();
                _birthDayDialog = new DatePickerDialog(this, 
                        new OnDateSetListener() {
                            
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                    int dayOfMonth) {
                                String str = new StringBuilder().append(year).append("-")
                                .append(monthOfYear + 1).append("-").append(dayOfMonth).toString();
                                textView.setText(str);
                                mEmployee.setBirthday(str);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }
            _birthDayDialog.show();
            break;
            
        case 6:
            if (_identityPopupWindow == null) {
                _identityPopupWindow = initPopupWindow((TextView)v, UserInfoInit.getIdentityListWithString());
            }
            _identityPopupWindow.showAsDropDown(v);
            break;
        }
    }
    
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (v.getId() == R.id.delete) {
            intent.putExtra("result", true);
            setResult(RESULT_OK, intent);
        } else {
            intent.putExtra("result", false);
            Bundle bundle = new Bundle();
            bundle.putSerializable("employee_info", mEmployee);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (_shopPopupWindow != null) {
            _shopPopupWindow.dismiss();
            _shopPopupWindow = null;
        }
        
        if (_rolePopupWindow != null) {
            _rolePopupWindow.dismiss();
            _rolePopupWindow = null;
        }
        
        if (_sexPopupWindow != null) {
            _sexPopupWindow.dismiss();
            _sexPopupWindow = null;
        }
        
        if (_identityPopupWindow != null) {
            _identityPopupWindow.dismiss();
            _identityPopupWindow = null;
        }
        
        if (_inDateDialog != null) {
            _inDateDialog.dismiss();
            _inDateDialog = null;
        }
        
        if (_birthDayDialog != null) {
            _birthDayDialog.dismiss();
            _inDateDialog = null;
        }
    }
}
