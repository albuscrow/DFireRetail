package com.dfire.retail.app.manage.activity.setting;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.goodsmanager.CountWatcher;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsManagerBaseActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.MyCheckBoxLayout;
import com.dfire.retail.app.manage.activity.goodsmanager.MySpinnerLayout;
import com.dfire.retail.app.manage.activity.goodsmanager.OneColumnSpinner;
import com.dfire.retail.app.manage.activity.goodsmanager.MyCheckBoxLayout.Listener;
import com.dfire.retail.app.manage.data.ConfigItemOptionVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.google.gson.reflect.TypeToken;


public class ParamSettingActivity extends GoodsManagerBaseActivity 
    implements OnClickListener {
    
    private OneColumnSpinner mSpinerPopWindow;
	private ArrayList<ConfigItemOptionVo> nameList;

	private int negativestoreStatus;
	private int remnantMode;
	private MySpinnerLayout zeroView;
	private MyCheckBoxLayout checkLayout;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_param);
        setTitleRes(R.string.param_setting);
        setWatcher(new CountWatcher(this));
        checkLayout = (MyCheckBoxLayout) findViewById(R.id.goods_stock);
        checkLayout.init(getResources().getString(R.string.goods_stock), false);
        checkLayout.setListener(new Listener() {

			@Override
			public void checkedChange(boolean isChecked) {
				if (isChecked) {
					negativestoreStatus = 1;
				}else{
					negativestoreStatus = 2;
				}
			}
			
		});
        
        mSpinerPopWindow = new OneColumnSpinner(this);
        mSpinerPopWindow.setTitleText(Constants.LENGTOU);
        zeroView = (MySpinnerLayout) findViewById(R.id.odd_dispose);
        zeroView.init(getResources().getString(R.string.odd_dispose), null, mSpinerPopWindow);
        zeroView.setListener(new MySpinnerLayout.Listener() {
			
			@Override
			public String confirm(int pos) {
				remnantMode = pos + 1;
				return nameList.get(pos).toString();
			}
			
			@Override
			public void cancel() {
			}
		});
        zeroView.clearSaveFlag();
        getParams();

        findViewById(R.id.title_right).setOnClickListener(this);
        findViewById(R.id.title_left).setOnClickListener(this);
        setBack();
        hideRight();
    }


	private void getParams() {

		JsonUtil ju = new JsonUtil(getIntent().getStringExtra(Constants.JSON));
		negativestoreStatus = ju.getInt(Constants.NEGATIVE_STORE_STATUS);
		if (negativestoreStatus == 1) {
			checkLayout.setChecked(true);
		}else{
			checkLayout.setChecked(false);
		}
		checkLayout.clearSaveFlag();
		remnantMode = ju.getInt(Constants.REMNANE_MODEL) - 1;
		if (remnantMode < 0) {
			remnantMode = 0;
		}else if (remnantMode > 4) {
			remnantMode = 4;
		}
		nameList = (ArrayList<ConfigItemOptionVo>) ju.get("remnantModelList", new TypeToken<List<ConfigItemOptionVo>>(){}.getType());
		zeroView.setValue(nameList.get(remnantMode).toString(), true);
		zeroView.clearSaveFlag();
		String longest = Constants.EMPTY_STRING;

		mSpinerPopWindow.setData(nameList);
		switchToBackMode();
	}

	@Override
	public void onClick(View v) {
        switch (v.getId()) {
        	
        case R.id.title_right:
        	save();
        	break;
        	
        case R.id.title_left:
        	finish();
        	break;

        default:
            break;
        }
    }

	private void save() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SAVE_CONFIG_URL);
		params.setParam(Constants.NEGATIVE_STORE_STATUS, negativestoreStatus);
		params.setParam(Constants.REMNANE_MODEL, remnantMode);
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				switchToEditMode();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(ParamSettingActivity.this)) {
					return;
				}
				ToastUtil.showShortToast(ParamSettingActivity.this, Constants.SAVE_SETTING_SUCCESS);
				finish();
			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(ParamSettingActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}).execute();
	}
    
}
