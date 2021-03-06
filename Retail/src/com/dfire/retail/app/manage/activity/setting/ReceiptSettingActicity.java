package com.dfire.retail.app.manage.activity.setting;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.goodsmanager.CountWatcher;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsManagerBaseActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.MyCheckBoxLayout;
import com.dfire.retail.app.manage.activity.goodsmanager.MyEditTextLayout;
import com.dfire.retail.app.manage.activity.goodsmanager.MyEditTextLayout.TextChangeListener;
import com.dfire.retail.app.manage.activity.goodsmanager.MySpinnerLayout;
import com.dfire.retail.app.manage.activity.goodsmanager.MySpinnerLayout.Listener;
import com.dfire.retail.app.manage.activity.goodsmanager.OneColumnSpinner;
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
import com.google.gson.Gson;


public class ReceiptSettingActicity extends GoodsManagerBaseActivity implements OnClickListener{
	private ReceiptVo receiptSetting;
	private OneColumnSpinner guigePopup;
	private MyCheckBoxLayout showLogoCheckbox;
	private MyEditTextLayout receiptTitle;
	private MyEditTextLayout bottomContent;
	private MySpinnerLayout guigeView;
	private ConfigItemOptionVo currentWidthId;
	private MySpinnerLayout mobanView;
	private ReceiptTemplateVo currentTemplate;
	private ReceiptStyleVo receiptStyle;
	private OneColumnSpinner mobanPopup;
	private ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setWatcher(new CountWatcher(this));
		setContentView(R.layout.setting_receipt);
		setTitleRes(R.string.receipt_setting_title);
		
		
		mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);
        
        
        guigePopup = new OneColumnSpinner(ReceiptSettingActicity.this);
        guigePopup.setTitleText(Constants.RECEIPT_SPECIFIC);
        mobanPopup = new OneColumnSpinner(ReceiptSettingActicity.this);
        mobanPopup.setTitleText(Constants.CHOOSE_TEMPLATE);
        hideRight();
        
        receiptTitle = setEditTextContent(R.id.receipt_title, Constants.RECEIPT_TITLE, Constants.EMPTY_STRING, Constants.NECESSARY, Constants.RECEIPT_TEXT_MAX_LENGTH);
        bottomContent = setEditTextContent(R.id.endnote, Constants.BOTTOM_CONTENT, Constants.EMPTY_STRING, Constants.NECESSARY, Constants.RECEIPT_TEXT_MAX_LENGTH);
        
        image = (ImageView)findViewById(R.id.temp_preview);
        image.setOnClickListener(this);
        setBack();
        hideRight();
        getOriginSetting();

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
	private MyEditTextLayout setEditTextContent(int id, String lable, String content,
			String hint, int maxLength) {
		final MyEditTextLayout layout = (MyEditTextLayout) findViewById(id);
		layout.init(lable, content, hint, InputType.TYPE_CLASS_TEXT, maxLength);
		layout.clearSaveFlag();
//		layout.setTextChangeListener(new TextChangeListener() {
//			@Override
//			public void afterTextChange(String after, String before) {
//				if (after.length() > Constants.MAX_RECEIPT) {
//					layout.setValue(before);
//				}
//			}
//		});
		return layout;
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
	private MySpinnerLayout setSpinerConetent(int tongbu, String label, String value, OneColumnSpinner spinner, Listener listener) {
		MySpinnerLayout layout = (MySpinnerLayout) findViewById(tongbu);
		layout.init(label, value, spinner);
		layout.setListener(listener);
		layout.clearSaveFlag();
		return layout;
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
	private MyCheckBoxLayout setCheckBoxContent(int id, String label, boolean b) {
		MyCheckBoxLayout layout = (MyCheckBoxLayout) findViewById(id);
		layout.init(label, b);
		layout.clearSaveFlag();
		return layout;
	}
    

    private void getOriginSetting() {

    	receiptSetting = (ReceiptVo) getIntent().getSerializableExtra(Constants.RECEIPT);
    	receiptStyle = receiptSetting.getReceiptStyle();
    	showLogoCheckbox = setCheckBoxContent(R.id.logo, Constants.SHOW_LOGO, receiptStyle.getHasLogoBoolean());
    	receiptTitle = setEditTextContent(R.id.receipt_title, Constants.RECEIPT_TITLE, receiptStyle.getReceiptTitle(), Constants.NECESSARY, Constants.RECEIPT_TEXT_MAX_LENGTH);
    	bottomContent = setEditTextContent(R.id.endnote, Constants.BOTTOM_CONTENT, receiptStyle.getBottomContent(), Constants.NECESSARY, Constants.RECEIPT_TEXT_MAX_LENGTH);

    	final List<ConfigItemOptionVo> receiptWidthList = receiptSetting.getReceiptWidthList();
    	currentWidthId = getConfigItemOptionVo(receiptStyle.getReceiptWidthId().toString(), receiptWidthList);
    	guigeView = setSpinerConetent(R.id.receipt_standard, Constants.RECEIPT_SPECIFIC, currentWidthId.toString(), guigePopup, new Listener() {
			
			@Override
			public String confirm(int pos) {
				currentWidthId = receiptWidthList.get(pos);
				return currentWidthId.toString();
			}
			
			@Override
			public void cancel() {
			}
		});
    	guigePopup.setData(receiptWidthList);


    	final List<ReceiptTemplateVo> receiptTemplateList = receiptSetting.getReceiptTemplete();

    	currentTemplate = getReceiptTemplateVo(receiptStyle.getReceiptTemplateCode(), receiptTemplateList);
    	mobanView = setSpinerConetent(R.id.choice_temp, Constants.CHOOSE_TEMPLATE, currentTemplate.toString(), mobanPopup, new Listener() {
			
			@Override
			public String confirm(int pos) {
				image.setImageResource(EXAMPLE[pos]);
				currentTemplate = receiptTemplateList.get(pos);
				return currentTemplate.toString();
			}
			
			@Override
			public void cancel() {
			}
		});

    	mobanPopup.setData(receiptTemplateList);

    	image.setImageResource(EXAMPLE[currentTemplate.getMobanId()]);
    	setBack();
    	hideRight();
	}
    
    static public final int[] EXAMPLE = new int[]{R.drawable.ico_receipt,R.drawable.ico_receipt2,R.drawable.ico_receipt3};
    
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
            
        case R.id.temp_preview:
        	File cacheDir = getExternalFilesDir(null);
        	if (!cacheDir.exists()) {
				cacheDir.mkdir();
			}
			File file = new File(cacheDir, Constants.TEMP_PNG_NAME);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}	
			}
        	FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ico_receipt);
				bitmap.compress(CompressFormat.PNG, 90, fos);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally{
				try {
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        	
        	Uri uri = Uri.fromFile(file);
			Intent it = new Intent(Intent.ACTION_VIEW);
//			Resources r = getResources();
//			Uri uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
//					+ r.getResourcePackageName(R.drawable.ico_receipt) + "/"
//					+ r.getResourceTypeName(R.drawable.ico_receipt) + "/"
//					+ r.getResourceEntryName(R.drawable.ico_receipt));
			it.setDataAndType(uri, Constants.VIEW_IMAGE);
        	
        	startActivity(it);
        	break;
        }
    }

	private void save() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SAVE_RECEIPT_URL);
		String title = receiptTitle.getValue();
		if (title.length() == 0) {
			ToastUtil.showShortToast(this, Constants.INPUT_RECEIPT_TITLE, receiptTitle);
			return;
		}
		String endcontent = bottomContent.getValue();
		if (endcontent.length() == 0) {
			ToastUtil.showShortToast(this, Constants.INPUT_RECEIPT_BOTTTOM, bottomContent);
			return;
		}
		receiptStyle.setShowLogo(showLogoCheckbox.isChecked());
		receiptStyle.setReceiptTitle(title);
		receiptStyle.setBottomContent(endcontent);
		receiptStyle.setReceiptTemplateCode(currentTemplate.getReceiptTemplateId());
		receiptStyle.setReceiptWidthId(Integer.parseInt(currentWidthId.getValue()));
		try {
			params.setParam(Constants.RECEIPT, new JSONObject(new Gson().toJson(receiptStyle)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(ReceiptSettingActicity.this)) {
					return;
				}
				ToastUtil.showShortToast(ReceiptSettingActicity.this, Constants.SAVE_RECEIPT_SUCCESS);
				finish();
			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(ReceiptSettingActicity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}).execute();
	}
    
}
