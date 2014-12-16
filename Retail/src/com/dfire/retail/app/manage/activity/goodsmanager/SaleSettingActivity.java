package com.dfire.retail.app.manage.activity.goodsmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.goodsmanager.MyCheckBoxLayout.Listener;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.data.SalesSetGoodsVo;
import com.dfire.retail.app.manage.data.SalesSetVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.Gson;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.format.DateFormat;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * The Class SaleSettingActivity.
 * 
 * @author albuscrow
 */
public class SaleSettingActivity extends GoodsManagerBaseActivity implements OnClickListener{
	
	private ArrayList<String> ids;
	private MyEditTextLayout ticheng;
	private MyEditTextLayout zhekou;
	private CheckBox jifen;
	private CheckBox youhui;
	private CheckBox tejia;
	private CheckBox huiyuan;
	private MySpinnerLayout begin;
	private MySpinnerLayout end;
	private ArrayList<GoodsVo> goods;
	private HashMap<String, GoodsVo> goodsMap;
	
	private long endMillis;
	private long startMillis;
	private SelectDateDialog mDateDialog;
	private String selectDate;
	private long currentMillis;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setWatcher(new CountWatcher(this));
		setContentView(R.layout.activity_sale_setting);
		findViewById(R.id.title_left).setOnClickListener(this);
		findViewById(R.id.title_right).setOnClickListener(this);
		
		ticheng = setEditTextContent(R.id.ticheng,Constants.GOODS_TICHENG,Constants.EMPTY_STRING,Constants.NOT_NECESSARY, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//		ticheng.setPercent(true);
		ticheng.getInputText().setKeyListener(new DigitsKeyListener(false, true));
		ticheng.getInputText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(Constants.SIX_LENGTH)});
		jifen = setCheckBoxContent(R.id.jifen, Constants.GOODS_JIFEN ,true, null);
		youhui = setCheckBoxContent(R.id.youhui, Constants.GOODS_YOUHUI ,true, new Listener() {
			
			@Override
			public void checkedChange(boolean isChecked) {
				if (!isChecked) {
					tejia.setChecked(false);
				}
				tejia.setEnabled(isChecked);
			}
		});
		
		tejia = setCheckBoxContent(R.id.isOpen, Constants.IS_OPEN, true, new Listener() {
			
			@Override
			public void checkedChange(boolean isChecked) {
				if (isChecked) {
					showTejisShezhi();
				}else{
					hideTejiaShezhi();
				}
			}
		});
		zhekou = setEditTextContent(R.id.zhekou, Constants.ZHEKOU, Constants.EMPTY_STRING,
				Constants.NECESSARY, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//		zhekou.setPercent(true);
		zhekou.getInputText().setKeyListener(new DigitsKeyListener(false, true));
		zhekou.getInputText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(Constants.SIX_LENGTH)});
		
		begin = setSpinerConetent(R.id.beginTime, Constants.BEGIN_TIME, Constants.EMPTY_STRING);
		begin.setOnClickListener(this);
		end = setSpinerConetent(R.id.endTime, Constants.END_TIME, Constants.EMPTY_STRING);
		end.setOnClickListener(this);
		initDate();
		huiyuan =setCheckBoxContent(R.id.huiyuanzhuanxiang, Constants.HUIYUAN, false, null);
		
		ids = (ArrayList<String>) getIntent().getSerializableExtra(Constants.GOODSIDS);
		goods = (ArrayList<GoodsVo>) getIntent().getSerializableExtra(Constants.GOODS); 
		goodsMap = new HashMap<String, GoodsVo>();
		for (GoodsVo item : goods) {
			goodsMap.put(item.getGoodsId(), item);
		}

		setTitleText(Constants.SALE_SETTING);
		switchToEditMode();
		this.mDateDialog = new SelectDateDialog(SaleSettingActivity.this,true);//时间
		
		tejia.setChecked(false);
		((MyCheckBoxLayout)tejia.getParent()).clearSaveFlag();
//		setBack();
	}
	
	private void initDate() {
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		String date = sdf.format(cal.getTime());
		begin.setValue(date, true);
		end.setValue(date, true);
		begin.clearSaveFlag();
		end.clearSaveFlag();
		
		sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
		try {
			startMillis = sdf.parse(date+ " 00:00:00").getTime();
			endMillis = sdf.parse(date+ " 23:59:59").getTime();
			currentMillis = endMillis;
		} catch (ParseException e) {
			startMillis = -1;
			endMillis = -1;
			e.printStackTrace();
		}
	}

	/**
	 * Show tejis shezhi.
	 */
	void showTejisShezhi(){
		findViewById(R.id.zhekou).setVisibility(View.VISIBLE);;
		findViewById(R.id.beginTime).setVisibility(View.VISIBLE);;
		findViewById(R.id.endTime).setVisibility(View.VISIBLE);;
		findViewById(R.id.huiyuanzhuanxiang).setVisibility(View.VISIBLE);;
	};
	
	/**
	 * Hide tejia shezhi.
	 */
	void hideTejiaShezhi(){
		findViewById(R.id.zhekou).setVisibility(View.GONE);;
		findViewById(R.id.beginTime).setVisibility(View.GONE);;
		findViewById(R.id.endTime).setVisibility(View.GONE);;
		findViewById(R.id.huiyuanzhuanxiang).setVisibility(View.GONE);;
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
	 * @param listener
	 *            the listener
	 */
	private CheckBox setCheckBoxContent(int id, String label, boolean b, Listener listener) {
		MyCheckBoxLayout layout = (MyCheckBoxLayout) findViewById(id);
		layout.init(label, b);
		layout.setListener(listener);
		layout.clearSaveFlag();
		return layout.getCheckBox();
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
	private MySpinnerLayout setSpinerConetent(int tongbu, String label, String value) {
		MySpinnerLayout layout = (MySpinnerLayout) findViewById(tongbu);
		layout.init(label, value, null);
		layout.clearSaveFlag();
		return layout;
	}

	/**
	 * Sets the edit text content.
	 * 
	 * @param id
	 *            the id
	 * @param label
	 *            the lable
	 * @param content
	 *            the content
	 * @param hint
	 *            the hint
	 */
	private MyEditTextLayout setEditTextContent(int id, String label, String content,
			String hint, int inputType) {
		MyEditTextLayout layout = (MyEditTextLayout) findViewById(id);
		layout.init(label, content, hint, inputType);
		layout.clearSaveFlag();
		return layout;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.title_right:
			save();
			break;

		case R.id.second:
			pushDate(v);
			break;

		default:
			break;
		}
	}

	private void save() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SAVE_SETTING_BATCH_URL);
		SalesSetVo salesSetVo = new SalesSetVo();
		String tichengStr = ticheng.getValue();
		if (tichengStr.length() != 0) {
			try {
				float parseFloat = Float.parseFloat(tichengStr);
				if (!checkFloat(tichengStr, parseFloat, Constants.GOODS_TICHENG_FOR_ERROR, 100, ticheng)) {
					return;
				}
				salesSetVo.setPercentAge(parseFloat);
			} catch (Exception e) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_TICHENG,ticheng);
				e.printStackTrace();
				return;
			}
		}
		if (jifen.isChecked()) {
			salesSetVo.setHasDegree((short) 1);
		}else{
			salesSetVo.setHasDegree((short) 0);
		}
		if (youhui.isChecked()) {
			salesSetVo.setIsSales((short) 1);
		}else{
			salesSetVo.setIsSales((short) 0);
		}
		List<SalesSetGoodsVo> salesSetGoodsVos = new ArrayList<SalesSetGoodsVo>();
		if (tejia.isChecked()) {
			salesSetVo.setSpecialFlag("1");
			String zhekouStr = zhekou.getValue();
			if (zhekouStr.length() == 0) {
				ToastUtil.showShortToast(this, Constants.INPUT_ZHEKOU, zhekou);
				return;
			}
			try {
				float parseFloat = Float.parseFloat(zhekouStr);
				if (!checkFloat(zhekouStr, parseFloat, Constants.ZHEKOU_FOR_ERROR, 100, zhekou)) {
					return;
				}
				salesSetVo.setDiscountRate(parseFloat);
			} catch (Exception e) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_ZHEKOU, zhekou);
				e.printStackTrace();
				return;
			}
			String beginStr = begin.getText().toString();
			String endStr = end.getText().toString();
			if (startMillis == -1 || endMillis == -1 || beginStr.equals(Constants.CLICK_CHOOSE)||endStr.equals(Constants.CLICK_CHOOSE)) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_TIME, begin);
				return;
			}
			if (startMillis >= endMillis) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_TIME_S, begin);
				return;
			}
			if (System.currentTimeMillis() >= endMillis) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_TIME_E, begin);
				return;
			}
			salesSetVo.setStartTime(startMillis);
			salesSetVo.setEndTime(endMillis);
			if (huiyuan.isChecked()) {
				salesSetVo.setIsMember((short)1);
			}else{
				salesSetVo.setIsMember((short)0);
			}
			for (String id : ids) {
				SalesSetGoodsVo temp = new SalesSetGoodsVo();
				GoodsVo goodsVo = goodsMap.get(id);
				temp.setGoodsId(goodsVo.getGoodsId());
				temp.setRetailPrice(Float.parseFloat(goodsVo.getPetailPrice()) * salesSetVo.getDiscountRate()/100);
				salesSetGoodsVos.add(temp);
			}
		}else{
			salesSetVo.setSpecialFlag("0");
			for (String id : ids) {
				SalesSetGoodsVo temp = new SalesSetGoodsVo();
				GoodsVo goodsVo = goodsMap.get(id);
				temp.setGoodsId(goodsVo.getGoodsId());;
				temp.setRetailPrice(Float.parseFloat(goodsVo.getPetailPrice()));
				salesSetGoodsVos.add(temp);
			}
		}
		
		salesSetVo.setSalesSetGoodsList(salesSetGoodsVos);
		try {
			params.setParam(Constants.SALES_SETTING, new JSONObject(new Gson().toJson(salesSetVo)));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(SaleSettingActivity.this)) {
					return;
				}
				ToastUtil.showShortToast(SaleSettingActivity.this, Constants.SALES_SAVE_SUCCESS);
				setResult(RESULT_OK);
				finish();
			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(SaleSettingActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}).execute();
		
	}

	
	
	private void pushDate(final View v){
		mDateDialog.show();
		if (v == begin) {
			mDateDialog.getTitle().setText(Constants.BEGIN_TIME);
		}else{
			mDateDialog.getTitle().setText(Constants.END_TIME);
		}
		mDateDialog.updateDays(selectDate);
		mDateDialog.getmClearDate().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDateDialog.dismiss();
				((TextView) v).setText(Constants.PRESS_CHOOSE);
				if (v == begin) {
					startMillis = -1;
				}
			}
			
		});
		
		mDateDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v1) {
				mDateDialog.dismiss();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
				selectDate = mDateDialog.getCurrentData();
				((TextView)v).setText(selectDate);
				
				if (v == begin.findViewById(R.id.second)) {
					if (selectDate!=null) {
						try {
							startMillis = (sdf.parse((selectDate+" 00:00:00"))).getTime();
						} catch (ParseException e) {
							startMillis = -1;
							e.printStackTrace();
						}
					}

				}else{
					if (selectDate!=null) {
						try {
							endMillis = (sdf.parse((selectDate+" 23:59:59"))).getTime();
						} catch (ParseException e) {
							endMillis = -1;
							e.printStackTrace();
						}
					}
				}

			}
		});
		mDateDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDateDialog.dismiss();
			}
		});
	}

	@Override
	public void switchToBackMode() {
	}
	
}
