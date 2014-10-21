package com.dfire.retail.app.manage.activity.retailmanager;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.EmployeeInfoAdapter;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.UserVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmployeeInfoActivity extends TitleActivity implements
        RequestResultCallback {

    private static final String TAG = "EmployeeInfoActivity";
    
    private TextView _shopChoiceButton;
    private TextView _roleChoiceButton;

    List<UserVo> mList;
    private ListView mListView;
    private EmployeeInfoAdapter mAdapter;

    private PopupWindow _shopPopupWindow;
    private PopupWindow _rolePopupWindow;

    private int _shopChoicePosition = 0;
    private int _roleChoicePosition = 0;
    /**
     * 标记List当前显示的页面，当向下滑动时，请求下一页.
     */
    private int _currentRequestPage = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.employee_info);
        
        mListView = (ListView) findViewById(R.id.attendance_list);
        
        //理论上可以直接获取到员工所在的店铺和角色，然后直接设置
        _shopChoiceButton = (TextView) findViewById(R.id.shop_choice);
        List<ShopVo> shopList = UserInfoInit.getShopList();
        if (shopList != null && shopList.size() > _shopChoicePosition) {
            _shopChoiceButton.setClickable(true);
            _shopChoiceButton.setText(shopList.get(_shopChoicePosition).getShopName());
        } else {
            _shopChoiceButton.setText(null);//无数据
        }
        
        _roleChoiceButton = (TextView) findViewById(R.id.role_choice);
        List<RoleVo> roleList = UserInfoInit.getRoleList();
        if (roleList != null && roleList.size() > _roleChoicePosition) {
            _roleChoiceButton.setClickable(true);
            _roleChoiceButton.setText(roleList.get(_roleChoicePosition).getRoleName());//注意数据的存在性
        } else {
            _roleChoiceButton.setText(null);
        }
        
        setTitleRes(R.string.employee_info);
        showBackbtn();
        startRequestListData();
    }

    public void ClickListener(View v) {
        switch (Integer.parseInt(String.valueOf(v.getTag()))) {
        case 1:
            if (_shopPopupWindow == null) {
                _shopPopupWindow = initPopupWindow((TextView)v, UserInfoInit.getShopListWithString());
            }
            _shopPopupWindow.showAsDropDown(v);
            break;

        case 2:
            if (_rolePopupWindow == null) {
                _rolePopupWindow = initPopupWindow((TextView)v, UserInfoInit.getRoleListWithStirng());
            }
            _rolePopupWindow.showAsDropDown(v);
            break;
            
        case 3://member_info_add
            break;
        }
    }

    /**
     * 下拉菜单的模拟数据
     * @param textView
     * @param data
     * @return
     */
    private PopupWindow initPopupWindow(final TextView textView, List<String> data) {
        ListView listView = (ListView) LayoutInflater.from(this).inflate(
                R.layout.list, null);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.text, data));

        float dp = getResources().getDisplayMetrics().density;
        final PopupWindow popupWindow = new PopupWindow(listView, (int)(150 * dp), (int)(200 * dp),
                true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        // 网络获取数据.
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                popupWindow.dismiss();
                textView.setText(((TextView) view).getText());
                int tag = Integer.parseInt(String.valueOf(textView.getTag()));
                if (tag == 1) {
                    _shopChoicePosition = position;
                } else if (tag == 2) {
                    _roleChoicePosition = position;
                }
                startRequestListData();//更新搜索数据
            }
        });

        return popupWindow;
    }
    
    /**
     * 网络请求，显示员工列表.
     * 根据操作(viewTag) 和 选中位置(position)决定
     * 
     */
    private void startRequestListData() {
        List<ShopVo> shopList = UserInfoInit.getShopList();
        List<RoleVo> roleList = UserInfoInit.getRoleList();
        if (shopList == null || shopList.size() <= _shopChoicePosition
                || roleList == null || roleList.size() <= _roleChoicePosition) {
            return;//无数据，直接结束
        }
        
        getProgressDialog().setCancelable(false);
        getProgressDialog().setMessage("请求员工信息");
        getProgressDialog().show();
        
        RequestParameter parameters = new RequestParameter(true);
        parameters.setUrl(Constants.EMPLOYEE_INFO_LIST);
        parameters.setParam("shopId", shopList.get(_shopChoicePosition).getShopId());
        parameters.setParam("roleId", roleList.get(_roleChoicePosition).getRoleId());
        parameters.setParam("currentPage", String.valueOf(_currentRequestPage));
        new AsyncHttpPost(parameters, this).execute();
    }

    @Override
    public void onFail(Exception e) {
        getProgressDialog().dismiss();
        ToastUtil.showShortToast(getApplicationContext(), e.toString());
        testListData();
    }
    
    @Override
    public void onSuccess(String str) {
        getProgressDialog().dismiss();
        try {
            JSONObject object = new JSONObject(str);
            if (object.isNull("returnCode") || "fail".equals(object.getString("returnCode"))) {
                if (Constants.DEBUG)
                    Log.i(TAG, object.getString("exceptionCode"));
                testListData();
            } else {
                JSONArray userList = object.getJSONArray("userList");
                for (int i = 0; i < userList.length(); i++) {
                    mList.add(new UserVo(userList.getJSONObject(i)));
                }
                initListShopAndRoleData();
                initList();
            }
        } catch (Exception e) {
            testListData();
        }
    }

    /**
     * 初始化员工管理查询列表的角色数据及店家数据
     */
    private void initListShopAndRoleData() {
        ShopVo shop = UserInfoInit.getShopList().get(_shopChoicePosition);
        RoleVo role = UserInfoInit.getRoleList().get(_roleChoicePosition);
        UserVo user;
        for (int i = 0; i < mList.size(); i++) {
            user = mList.get(i);
            user.setShopId(shop.getShopId());
            user.setShopName(shop.getShopName());
            user.setRoleId(role.getRoleId());
            user.setRoleName(role.getRoleName());
            user.setLogId(R.drawable.man);
        }
    }
    
    private void initList() {
        mAdapter = new EmployeeInfoAdapter(EmployeeInfoActivity.this, mList);
        mListView.setAdapter(mAdapter);
        Utility.setListViewHeightBasedOnChildren(mListView);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                    long arg3) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", mList.get(position));
                Intent intent = new Intent(EmployeeInfoActivity.this,
                        EmployeeDetailActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        if (_shopPopupWindow != null) {
            _shopPopupWindow.dismiss();
            _shopPopupWindow = null;
        }

        if (_rolePopupWindow != null) {
            _rolePopupWindow.dismiss();
            _rolePopupWindow = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (data.getBooleanExtra("result", false)) {// 默认保存操作
                mList.remove(requestCode);// 删除
            } else {// 保存
                UserVo item = (UserVo) data
                        .getSerializableExtra("employee_info");
                if (item != null) {
                    mList.set(requestCode, item);
                }
            }

            mAdapter.notifyDataSetChanged();
            Utility.setListViewHeightBasedOnChildren(mListView);
        }
    }
    
    /**
     * List的模拟测试数据
     */
    private void testListData() {
        if (!Constants.TEST) {
            return;
        }
        mList = new ArrayList<UserVo>();
        mList.add(new UserVo("huajianlianshuoceshiuserinfoid00", "王晓红", "王晓红", 
                "001", "2014-9-26", "13488822653", 
                Integer.valueOf(1), "1994-9-26", 
                "12455441551122", Integer.valueOf(1), "西湖西溪花园"));
        mList.add(new UserVo("huajianlianshuoceshiuserinfoid00", "王宇", "王宇", 
                "002", "2014-1-26", "18888822653", 
                Integer.valueOf(1), "1984-9-26", 
                "15555441551122", Integer.valueOf(1), "西湖文一路"));
        mList.add(new UserVo("huajianlianshuoceshiuserinfoid00", "孙晓梅", "孙晓梅", 
                "003", "2013-1-26", "17788822653", 
                Integer.valueOf(1), "1987-9-26", 
                "28855441551122", Integer.valueOf(1), "西湖文三路"));
        
        initListShopAndRoleData();
        
        initList();
    }
}
