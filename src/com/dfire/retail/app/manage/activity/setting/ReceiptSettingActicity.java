package com.dfire.retail.app.manage.activity.setting;


import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment.SavedState;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsManagerBaseActivity;
import com.dfire.retail.app.manage.data.ConfigItemOptionVo;
import com.dfire.retail.app.manage.data.ReceiptStyleVo;
import com.dfire.retail.app.manage.data.ReceiptTemplateVo;
import com.dfire.retail.app.manage.data.ReceiptVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.google.gson.Gson;


public class ReceiptSettingActicity extends GoodsManagerBaseActivity implements OnClickListener{
	private ReceiptVo receiptSetting;
	private SpinerPopWindow guigePopup;
	private CheckBox showLogoCheckbox;
	private EditText receiptTitle;
	private EditText bottomContent;
	private TextView guigeView;
	private ConfigItemOptionVo currentWidthId;
	private TextView mobanView;
	private ReceiptTemplateVo currentTemplate;
	private ReceiptStyleVo receiptStyle;private SpinerPopWindow mobanPopup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_receipt);
		setTitleRes(R.string.receipt_setting_title);
		mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);
        
        getOriginSetting();
        
        guigePopup = new SpinerPopWindow(ReceiptSettingActicity.this);
        mobanPopup = new SpinerPopWindow(ReceiptSettingActicity.this);
        hideRight();
//        View view = findViewById(R.id.cancel_title);
//        view.findViewById(R.id.title_left_btn).setOnClickListener(this);
//        view.findViewById(R.id.title_right_btn).setOnClickListener(this);
//        TextView text = (TextView) view.findViewById(R.id.titleText);
//        text.setText(R.string.receipt_setting);
        
//        View view = findViewById(R.id.logo);
//        TextView text = (TextView) view.findViewById(R.id.check_title);
//        text.setText(R.string.shop_logo);
//        
//        view = findViewById(R.id.receipt_title);
//        text = (TextView) view.findViewById(R.id.main);
//        text.setText(R.string.receipt_title);
//        text = (TextView) view.findViewById(R.id.secend);
//        text.setText(RetailApplication.getShopVo().getShopName());
//        
//        view = findViewById(R.id.endnote);
//        text = (TextView) view.findViewById(R.id.main);
//        text.setText(R.string.endnote);
//        text = (TextView) view.findViewById(R.id.secend);
//        text.setText("祝你生活愉快");
        
//        view = findViewById(R.id.avorable);
//        text = (TextView) view.findViewById(R.id.check_title);
//        text.setText(R.string.avorable);
//        
//        view = findViewById(R.id.exchange);
//        text = (TextView) view.findViewById(R.id.check_title);
//        text.setText(R.string.exchange);
        
//        view = findViewById(R.id.receipt_standard);
//        text = (TextView) view.findViewById(R.id.main);
//        text.setText(R.string.receipt_standard);
//        text = (TextView) view.findViewById(R.id.secend);
//        text.setText("58mm");
//        
//        view = findViewById(R.id.choice_temp);
//        text = (TextView) view.findViewById(R.id.main);
//        text.setText(R.string.choice_temp);
//        text = (TextView) view.findViewById(R.id.secend);
//        text.setText("默认模板");
    }
    
    /**
	 * Sets the edit text content.
	 * 
	 * @param id
	 *            the id
	 * @param lable
	 *            the lable
	 * @param content
	 *            the content
	 * @param hint
	 *            the hint
	 */
	private EditText setEditTextContent(int id, String lable, String content,
			String hint) {
		View layout = findViewById(id);
		TextView lableView = (TextView) layout.findViewById(R.id.main);
		if (lable != null) {
			lableView.setText(lable);
		}
		EditText inputView = (EditText) layout.findViewById(R.id.secend);
		inputView.setHint(hint);
		if (content != null) {
			inputView.setText(content);
		}
		inputView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				switchToEditMode();
			}
		});
		return inputView;
	}
    
    /**
	 * Sets the spiner conetent.
	 * 
	 * @param tongbu
	 *            the tongbu
	 * @param label
	 *            the label
	 * @param value
	 *            the value
	 */
	private TextView setSpinerConetent(int tongbu, String label, String value) {
		View layout = findViewById(tongbu);
		TextView tongbuLable = (TextView) layout.findViewById(R.id.main);
		tongbuLable.setText(label);
		TextView tongbuValue = (TextView) layout.findViewById(R.id.secend);
		tongbuValue.setText(value);
		return tongbuValue;
	}
    
	/**
	 * Sets the check box content.
	 * 
	 * @param id
	 *            the id
	 * @param label
	 *            the label
	 * @param b
	 *            the b
	 */
	private CheckBox setCheckBoxContent(int id, String label, boolean b) {
		View layout = findViewById(id);
		TextView labelText = (TextView) layout.findViewById(R.id.check_title);
		labelText.setText(label);
		CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
		checkBox.setChecked(b);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				switchToEditMode();
			}
		});
		return checkBox;
	}
    

    private void getOriginSetting() {
    	RequestParameter params = new RequestParameter(true);
    	params.setUrl(Constants.GET_RECEIPT);
    	new AsyncHttpPost(params, new RequestResultCallback() {
			



			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(ReceiptSettingActicity.this)) {
					return;
				}
				receiptSetting = (ReceiptVo) ju.get(ReceiptVo.class);
				receiptStyle = receiptSetting.getReceiptStyle();
				showLogoCheckbox = setCheckBoxContent(R.id.logo, Constants.SHOW_LOGO, receiptStyle.getHasLogoBoolean());
				receiptTitle = setEditTextContent(R.id.receipt_title, Constants.RECEIPT_TITLE, receiptStyle.getReceiptTitle(), Constants.NECESSARY);
				bottomContent = setEditTextContent(R.id.endnote, Constants.BOTTOM_CONTENT, receiptStyle.getBottomContent(), Constants.NECESSARY);
				
				final List<ConfigItemOptionVo> receiptWidthList = receiptSetting.getReceiptWidthList();
				currentWidthId = getConfigItemOptionVo(receiptStyle.getReceiptWidthId().toString(), receiptWidthList);
				guigeView = setSpinerConetent(R.id.receipt_standard, Constants.RECEIPT_SPECIFIC, 
						currentWidthId.toString());
				guigeView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						guigePopup.setWidth(v.getWidth());
						guigePopup.showAsDropDown(v);
					}
				});
				guigePopup.refreshData(receiptWidthList, 0);
				guigePopup.setItemListener(new IOnItemSelectListener() {
					@Override
					public void onItemClick(int pos) {
						currentWidthId = receiptWidthList.get(pos);
						guigeView.setText(currentWidthId.toString());
						switchToEditMode();
					}
				});
				
				final List<ReceiptTemplateVo> receiptTemplateList = receiptSetting.getReceiptTemplete();
				String longest = "";
				for (ReceiptTemplateVo receiptTemplateVo : receiptTemplateList) {
					if (receiptTemplateVo.toString().length() > longest.length()) {
						longest = receiptTemplateVo.toString();
					}
				}
				currentTemplate = getReceiptTemplateVo(receiptStyle.getReceiptTemplateCode(), receiptTemplateList);
				mobanView = setSpinerConetent(R.id.choice_temp, Constants.CHOOSE_TEMPLATE, 
						currentTemplate.toString());
				TextPaint tp = mobanView.getPaint();
				mobanView.setWidth((int) (tp.measureText(longest) * 1.4f));
				mobanView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mobanPopup.setWidth(v.getWidth());
						mobanPopup.showAsDropDown(v);
					}
				});
				mobanPopup.refreshData(receiptTemplateList, 0);
				mobanPopup.setItemListener(new IOnItemSelectListener() {

					@Override
					public void onItemClick(int pos) {
						currentTemplate = receiptTemplateList.get(pos);
						mobanView.setText(currentTemplate.toString());
						switchToEditMode();
					}
				});
			}
			
			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}).execute();
	}
    
    private ReceiptTemplateVo getReceiptTemplateVo(String code, List<ReceiptTemplateVo> lists){
    	for (ReceiptTemplateVo receiptTemplateVo : lists) {
			if (receiptTemplateVo.getReceiptTemplateId().equals(code)) {
				return receiptTemplateVo;
			}
		}
    	return null;
    }
    
    private ConfigItemOptionVo getConfigItemOptionVo(String value, List<ConfigItemOptionVo> lists){
    	for (ConfigItemOptionVo configItemOptionVo : lists) {
			if (configItemOptionVo.getValue().equals(value)) {
				return configItemOptionVo;
			}
		}
    	return null;
    };

	@Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.title_left:
            finish();
            break;

        case R.id.title_right:
        	save();
            break;
        }
    }

	private void save() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SAVE_RECEIPT_URL);
		String title = receiptTitle.getText().toString();
		if (title.length() == 0) {
			ToastUtil.showShortToast(this, Constants.INPUT_RECEIPT_TITLE);
			return;
		}
		String endcontent = bottomContent.getText().toString();
		if (endcontent.length() == 0) {
			ToastUtil.showShortToast(this, Constants.INPUT_RECEIPT_BOTTTOM);
			return;
		}
		receiptStyle.setShowLogo(showLogoCheckbox.isChecked());
		receiptStyle.setReceiptTitle(title);
		receiptStyle.setBottomContent(endcontent);
		receiptStyle.setReceiptTemplateCode(currentTemplate.getReceiptTemplateId());
		receiptStyle.setReceiptWidthId(Integer.parseInt(currentWidthId.getValue()));
		final ProgressDialog pd = ProgressDialog.show(this, null, Constants.WAIT_SAVE_RECEIPT);
		try {
			params.setParam(Constants.RECEIPT, new JSONObject(new Gson().toJson(receiptStyle)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				pd.dismiss();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(ReceiptSettingActicity.this)) {
					return;
				}
				ToastUtil.showShortToast(ReceiptSettingActicity.this, Constants.SAVE_RECEIPT_SUCCESS);
				finish();
			}
			
			@Override
			public void onFail(Exception e) {
				pd.dismiss();
				ToastUtil.showUnknowError(ReceiptSettingActicity.this);
				e.printStackTrace();
			}
		}).execute();
	}
    
}