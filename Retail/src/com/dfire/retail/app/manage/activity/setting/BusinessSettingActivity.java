package com.dfire.retail.app.manage.activity.setting;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.ChoiceAdapter;
import com.dfire.retail.app.manage.adapter.RolePermissionSubItem;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.global.ShopInfo;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;


public class BusinessSettingActivity extends TitleActivity
    implements OnItemClickListener, OnClickListener {
	
	private final static String TAG="BusinessSettingActivity";
    
	//要显示的数据
    private List<RolePermissionSubItem> mData;
    //适配器
    private ChoiceAdapter mAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_business);
        setTitle(R.string.setting_business);
        ShopInfo shopInfo=RetailApplication.getmShopInfo();
        String[] str = getResources().getStringArray(R.array.business_type);
        mData = new ArrayList<RolePermissionSubItem>();
        for (int i = 0; i < str.length; i++) {
        	RolePermissionSubItem rolePermissionSubItem=new RolePermissionSubItem(i, str[i]);
        	if(rolePermissionSubItem.mName.equals("单店版")){
        		if(shopInfo.getShopType() != null && shopInfo.getShopType().intValue() == 1)
        			rolePermissionSubItem.mChecked=true;
        	}
        	else if(rolePermissionSubItem.mName.equals("连锁版")){
        		if(shopInfo.getShopType() != null && shopInfo.getShopType().intValue() == 2)
        			rolePermissionSubItem.mChecked=true;
        	}
            mData.add(rolePermissionSubItem);
        }
        
        findViewById(R.id.help).setOnClickListener(this);
        
        //获取列表
        ListView list = (ListView) findViewById(R.id.setting_list);
        mAdapter = new ChoiceAdapter(this, mData, R.layout.setting_business_item);
        //设置适配器
        list.setAdapter(mAdapter);
        //选项添加监听事件
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id){
    	RolePermissionSubItem rolePermissionSubItem=mData.get(position);
        if (!rolePermissionSubItem.mChecked) {
        	Integer shopType;
        	if(rolePermissionSubItem.mName.equals("单店版")){
        		shopType=1;
        	}
        	else if(rolePermissionSubItem.mName.equals("连锁版")){
        		shopType=2;
        	}
        	else{
        		Toast.makeText(BusinessSettingActivity.this, "设置失败，不存在该模式！",
						Toast.LENGTH_SHORT).show();
        		return;
        	}
        	
        	/*显示进度条*/
        	getProgressDialog().setCancelable(false);
        	getProgressDialog().setMessage(getResources().getText(R.string.in_change_password));
			getProgressDialog().show();
			
			RequestParameter params = new RequestParameter(true);
			params.setUrl(Constants.CHANGE_SHOPTYPE);
			params.setParam("entityMode",shopType);
			
			final int itemIdex=position;
			
			AsyncHttpPost httppost = new AsyncHttpPost(params,
					new RequestResultCallback() {
				@Override
				public void onSuccess(String str) {
					Log.d(TAG,str);
					Message msg = new Message();
					msg.what = Constants.HANDLER_SUCESS;
					msg.obj = str;
					Bundle bundle = new Bundle();    
                    bundle.putInt("position", itemIdex);
                    msg.setData(bundle);
					mChangeShopTypeHandler.sendMessage(msg);
				}
				
				@Override
				public void onFail(Exception e) {
					e.printStackTrace();
	                Message msg = new Message();
					msg.what = Constants.HANDLER_FAIL;
					msg.obj = e.getMessage();
					Bundle bundle = new Bundle();    
                    bundle.putInt("position", itemIdex);
                    msg.setData(bundle);
					mChangeShopTypeHandler.sendMessage(msg);
				}
			});
			httppost.execute();
    	}
    }

    private void initChecked() {
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).mChecked = false;
        }
    }
    
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
        case R.id.setting_payway_help:
            intent.setClass(this, SettingHelpActivity.class);
            startActivity(intent);
            break;
        }
    }
    
    /**
	 * 处理返回的结果，如果成功解析网络返回的json数据
	 */
	Handler mChangeShopTypeHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			getProgressDialog().dismiss();
			
			switch (msg.what) {
				case Constants.HANDLER_SUCESS:
				try {
					if(parserJson(msg.obj.toString())!=0){
						Toast.makeText(BusinessSettingActivity.this, "店铺商业模式设置失败！",
								Toast.LENGTH_SHORT).show();
						return;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(BusinessSettingActivity.this, "店铺商业模式设置成功！",
						Toast.LENGTH_SHORT).show();
				initChecked();
				int position = msg.getData().getInt("position");
	            mData.get(position).mChecked = true;
	            mAdapter.notifyDataSetChanged();
				break;
				
				case Constants.HANDLER_FAIL:
					Toast.makeText(BusinessSettingActivity.this, "店铺商业模式设置失败！！",
							Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};
	
	//解析json数据
	protected int parserJson(String result) throws JSONException {
		JSONObject jobj = new JSONObject(result);
		String returnCode = jobj.getString("returnCode");
		if(returnCode.equals(Constants.REPONSE_FAIL))
			return -1;
		return 0;
	}
}
