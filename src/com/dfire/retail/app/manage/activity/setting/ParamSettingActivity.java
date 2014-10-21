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
import com.dfire.retail.app.manage.activity.TitleActivity;
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


public class ParamSettingActivity extends TitleActivity 
    implements OnCheckedChangeListener, OnClickListener, IOnItemSelectListener {
    
    private SpinerPopWindow mSpinerPopWindow;
	private TextView zeroInputText;
	private ArrayList<ConfigItemOptionVo> nameList;

	private int negativestoreStatus;
	private int remnantMode;
	private CheckBox check;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_param);
        setTitleRes(R.string.param_setting);
        showBackbtn();
        
        View view = findViewById(R.id.goods_stock);
        TextView text = (TextView) view.findViewById(R.id.check_title);
        text.setText(R.string.goods_stock);
        check = (CheckBox) view.findViewById(R.id.checkbox);
        check.setOnCheckedChangeListener(this);
        
        View zeroView = findViewById(R.id.odd_dispose);
        
        TextView zeroLabel = (TextView) zeroView.findViewById(R.id.main);
        zeroLabel.setText(R.string.odd_dispose);
        
        zeroInputText = (TextView) zeroView.findViewById(R.id.secend);
        
        zeroInputText.setOnClickListener(this);
        
        mSpinerPopWindow = new SpinerPopWindow(this);
        
        getParams();

        findViewById(R.id.title_right).setOnClickListener(this);
    }


	private void getParams() {
		final ProgressDialog pd = ProgressDialog.show(this, null, Constants.WAIT_SETTING, true, false);
    	RequestParameter params = new RequestParameter(true);
    	params.setUrl(Constants.GET_CONFIG_DETAIL);
    	params.setParam(Constants.SHOP_ID, RetailApplication.getmShopInfo().getShopId());
    	new AsyncHttpPost(params, new RequestResultCallback() {

			@Override
			public void onSuccess(String str) {
				pd.dismiss();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(ParamSettingActivity.this)) {
					return;
				}
				negativestoreStatus = ju.getInt(Constants.NEGATIVE_STORE_STATUS);
				if (negativestoreStatus == 1) {
					check.setChecked(true);
				}else{
					check.setChecked(false);
				}
				remnantMode = ju.getInt(Constants.REMNANE_MODEL) - 1;
				if (remnantMode < 0) {
					remnantMode = 0;
				}else if (remnantMode > 4) {
					remnantMode = 4;
				}
				nameList = (ArrayList<ConfigItemOptionVo>) ju.get("remnantModelList", new TypeToken<List<ConfigItemOptionVo>>(){}.getType());
				zeroInputText.setText(nameList.get(remnantMode).toString());
				String longest = Constants.EMPTY_STRING;
				for (ConfigItemOptionVo item : nameList) {
					if (longest.length() < item.getName().length()) {
						longest = item.getName();
					}
				}
				zeroInputText.setWidth((int) (zeroInputText.getPaint().measureText(longest) * 1.4f));
				mSpinerPopWindow.refreshData(nameList, remnantMode);
				mSpinerPopWindow.setItemListener(ParamSettingActivity.this);
			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showUnknowError(ParamSettingActivity.this);
				pd.dismiss();
				e.printStackTrace();
			}
		}).execute();
	}

	@Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		change2saveMode();
		if (isChecked) {
			negativestoreStatus = 1;
		}else{
			negativestoreStatus = 2;
		}
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.secend:
        	mSpinerPopWindow.setWidth(zeroInputText.getWidth());
        	mSpinerPopWindow.showAsDropDown(zeroInputText);
        	break;
        	
        case R.id.title_right:
        	save();
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
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				change2saveFinishMode();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(ParamSettingActivity.this)) {
					return;
				}
				ToastUtil.showShortToast(ParamSettingActivity.this, Constants.SAVE_SETTING_SUCCESS);
				finish();
			}
			
			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}).execute();
	}

	@Override
	public void onItemClick(int pos) {
		zeroInputText.setText(nameList.get(pos).toString());
		remnantMode = pos + 1;
		change2saveMode();
	}
    
}
