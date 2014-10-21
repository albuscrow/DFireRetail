package com.dfire.retail.app.manage.activity.goodsmanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.dfire.retail.app.manage.R;
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

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The Class SaleSettingActivity.
 * 
 * @author albuscrow
 */
public class SaleSettingActivity extends GoodsManagerBaseActivity implements OnClickListener, OnCheckedChangeListener{
	
	private ArrayList<String> ids;
	private TextView ticheng;
	private TextView zhekou;
	private CheckBox jifen;
	private CheckBox youhui;
	private CheckBox tejia;
	private CheckBox huiyuan;
	private TextView begin;
	private TextView end;
	private ArrayList<GoodsVo> goods;
	private HashMap<String, GoodsVo> goodsMap;
	
	private long endMillis;
	private long startMillis;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sale_setting);
		switchToEditMode();
		findViewById(R.id.title_left).setOnClickListener(this);
		findViewById(R.id.title_right).setOnClickListener(this);
		
		ticheng = setEditTextContent(R.id.ticheng,Constants.GOODS_TICHENG,Constants.EMPTY_STRING,Constants.NECESSARY);

		jifen = setCheckBoxContent(R.id.jifen, Constants.GOODS_JIFEN ,false, null);
		youhui = setCheckBoxContent(R.id.youhui, Constants.GOODS_YOUHUI ,false, null);
		
		tejia = setCheckBoxContent(R.id.isOpen, Constants.IS_OPEN, true, this);
		zhekou = setEditTextContent(R.id.zhekou, Constants.ZHEKOU, Constants.EMPTY_STRING, Constants.NECESSARY);
		begin = setSpinerConetent(R.id.beginTime, Constants.BEGIN_TIME, Constants.CLICK_CHOOSE);;
		begin.setOnClickListener(this);
		end = setSpinerConetent(R.id.endTime, Constants.END_TIME, Constants.CLICK_CHOOSE);;
		end.setOnClickListener(this);
		huiyuan =setCheckBoxContent(R.id.huiyuanzhuanxiang, Constants.HUIYUAN, false, null);
		
		ids = (ArrayList<String>) getIntent().getSerializableExtra(Constants.GOODSIDS);
		goods = (ArrayList<GoodsVo>) getIntent().getSerializableExtra(Constants.GOODS); 
		goodsMap = new HashMap<String, GoodsVo>();
		for (GoodsVo item : goods) {
			goodsMap.put(item.getGoodsId(), item);
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
	private CheckBox setCheckBoxContent(int id, String label, boolean b, OnCheckedChangeListener listener) {
		View layout = findViewById(id);
		TextView labelText = (TextView) layout.findViewById(R.id.check_title);
		labelText.setText(label);
		CheckBox checkBox = (CheckBox) layout.findViewById(R.id.checkbox);
		checkBox.setOnCheckedChangeListener(listener);
		checkBox.setChecked(b);
		return checkBox;
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
	private TextView setEditTextContent(int id, String lable, String content,
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
		return inputView;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			save();
			break;

		case R.id.secend:
			if (v == begin) {

				Calendar calendar = Calendar.getInstance();
				DatePickerDialog dd = new DatePickerDialog(this, new OnDateSetListener() {


					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						Calendar calendar = Calendar.getInstance();
						calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
						startMillis = calendar.getTimeInMillis();
						begin.setText( year + Constants.CONNECTOR + 
								(monthOfYear+1) + Constants.CONNECTOR +
								dayOfMonth);
					}
				}, calendar.get(Calendar.YEAR) , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
				dd.show();
			}else{

				Calendar calendar2 = Calendar.getInstance();
				DatePickerDialog dd2 = new DatePickerDialog(this, new OnDateSetListener() {


					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						System.currentTimeMillis();
						Calendar calendar = Calendar.getInstance();
						calendar.set(year, monthOfYear, dayOfMonth, 23, 59, 59);
						endMillis = calendar.getTimeInMillis();
						end.setText( year + Constants.CONNECTOR + 
								(monthOfYear+1) + Constants.CONNECTOR +
								dayOfMonth);
					}
				}, calendar2.get(Calendar.YEAR) , calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH));
				dd2.show();
			}
			break;

		default:
			break;
		}
	}

	private void save() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SAVE_SETTING_BATCH_URL);
		SalesSetVo salesSetVo = new SalesSetVo();
		String tichengStr = ticheng.getText().toString();
		if (tichengStr.length() == 0) {
			ToastUtil.showShortToast(this, Constants.INPUT_TICHENG);
			return;
		}
		try {
			float parseFloat = Float.parseFloat(tichengStr);
			if (parseFloat < 0 || parseFloat > 100) {
				ToastUtil.showShortToast(this, Constants.INPUT_TICHENG);
				return;
			}
			salesSetVo.setPercentAge(parseFloat/100);
		} catch (Exception e) {
			ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_TICHENG);
			e.printStackTrace();
			return;
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
			String zhekouStr = zhekou.getText().toString();
			if (zhekouStr.length() == 0) {
				ToastUtil.showShortToast(this, Constants.INPUT_ZHEKOU);
				return;
			}
			try {
				float parseFloat = Float.parseFloat(zhekouStr);
				if (parseFloat < 0 || parseFloat > 100) {
					ToastUtil.showShortToast(this, Constants.INPUT_ZHEKOU);
					return;
				}
				salesSetVo.setDiscountRate(parseFloat/100);
			} catch (Exception e) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_ZHEKOU);
				e.printStackTrace();
				return;
			}
			String beginStr = begin.getText().toString();
			String endStr = end.getText().toString();
			if (beginStr.equals(Constants.CLICK_CHOOSE)||endStr.equals(Constants.CLICK_CHOOSE)) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_TIME);
				return;
			}
			salesSetVo.setStartTime(startMillis);
			salesSetVo.setEndTime(endMillis);
			if (huiyuan.isChecked()) {
				salesSetVo.setIsMember((short)1);
			}else{
				salesSetVo.setIsMember((short)2);
			}
			for (String id : ids) {
				SalesSetGoodsVo temp = new SalesSetGoodsVo();
				GoodsVo goodsVo = goodsMap.get(id);
				temp.setGoodsId(goodsVo.getGoodsId());
				temp.setRetailPrice(Float.parseFloat(goodsVo.getPetailPrice()) * salesSetVo.getDiscountRate());
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
		
		final ProgressDialog pd = ProgressDialog.show(this, null, Constants.WAIT_SAVE_SET, true, false);
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				pd.dismiss();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(SaleSettingActivity.this)) {
					return;
				}
				ToastUtil.showShortToast(SaleSettingActivity.this, Constants.SALES_SAVE_SUCCESS);
				finish();
			}
			
			@Override
			public void onFail(Exception e) {
				pd.dismiss();
				ToastUtil.showUnknowError(SaleSettingActivity.this);
				e.printStackTrace();
			}
		}).execute();
		
	}

	/* (non-Javadoc)
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			showTejisShezhi();
		}else{
			hideTejiaShezhi();
		}
	}
}