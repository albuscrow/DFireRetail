package com.dfire.retail.app.manage.common;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.CityVo;
import com.dfire.retail.app.manage.data.DistrictVo;
import com.dfire.retail.app.manage.data.ProvinceVo;
import com.dfire.retail.app.manage.wheel.widget.OnWheelChangedListener;
import com.dfire.retail.app.manage.wheel.widget.WheelView;
import com.dfire.retail.app.manage.wheel.widget.adapters.ArrayWheelAdapter;

public class SelectEreaDialog extends Dialog{

	private static final String TAG = "SelectDialog";
	
	private Context mContext;
	private WheelView mProvince, mCity, mDistrict;
	
	private ArrayAdapter mCityAdapter,mProvinceAdapter,mDistrictAdapter;

	private Button mConfirmButton, mCancelButton;
	private TextView mTitle;
	private LinearLayout mClearDate;
	private String mType = "";// 
	List<ProvinceVo> provinceVoList;
	List<CityVo> cityVoList;
	List<DistrictVo> districtVoList;
	
	private Integer curProvinceId,curCityId,curDistrictId;
	private String curProvName,curCityName,curDistrictName;
	
	int provinceIndex,cityIndex ,districtIndex;
	
	private ArrayList<String> mProvinceList = new ArrayList<String>();
	private ArrayList<String> mCityList = new ArrayList<String>();
	private ArrayList<String> mDistictList =new ArrayList<String>();
	
	public SelectEreaDialog(Context context) {
		super(context, R.style.dialog);
		mContext = context;
	}

	public SelectEreaDialog(Context context, String type) {
		super(context, R.style.dialog);
		mContext = context;
		mType = type;
	}
	
	public SelectEreaDialog(Context context,List<ProvinceVo> adressList,
			int provinceSelect,int citySelect,int districtSelect){
		super(context,R.style.dialog);
		mContext = context;
		this.provinceVoList = adressList;
		this.cityIndex = citySelect;
		this.provinceIndex = provinceSelect;
		this.districtIndex = districtSelect;
		
		//获取当前选中的地址城市信息
		cityVoList = provinceVoList.get(provinceSelect).getCityVoList();
		//获取当前选中的地区信息
		districtVoList = cityVoList.get(citySelect).getDistrictVoList();
		//获取省份列表信息
		for(int i = 0; i < provinceVoList.size();i++)
			mProvinceList.add(provinceVoList.get(i).getProvinceName());
		//获取城市列表信息
		for(int i = 0; i < cityVoList.size();i++){
			mCityList.add(cityVoList.get(i).getCityName());			
		}
		
		//获取区域名字列表信息
		for(int i = 0; i <  districtVoList.size();i++){
			mDistictList.add(districtVoList.get(i).getDistrictName());
		}
		
		curCityName = mCityList.get(0);
		curProvName = mProvinceList.get(0);
		curDistrictName = districtVoList.get(0).getDistrictName();
		
		curProvinceId = provinceVoList.get(0).getProvinceId();
		curCityId = cityVoList.get(0).getCityId();
		curDistrictId = districtVoList.get(0).getDistrictId();
		
		
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_eara_dialog);

		mConfirmButton = (Button) findViewById(R.id.card_type_confirm);
		mCancelButton = (Button) findViewById(R.id.card_type_cancel);
		mTitle = (TextView) findViewById(R.id.date_dialog_title);
		mTitle.setText("选择区域");
		mClearDate = (LinearLayout) findViewById(R.id.clear_date);
	
		this.setCanceledOnTouchOutside(true);
		Window window = this.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		DisplayMetrics metric = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
		params.width =width;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);

		
		mProvince = (WheelView) findViewById(R.id.year_wheel);
		mCity = (WheelView) findViewById(R.id.month_wheel);
		mDistrict = (WheelView) findViewById(R.id.day_wheel);

		mProvince.setVisibleItems(4); // Number of items
		mProvince.setWheelBackground(android.R.color.transparent);
		mProvince.setWheelForeground(android.R.color.transparent);
		mProvince.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		mProvince.setCyclic(true);

		mCity.setVisibleItems(4); // Number of items
		mCity.setWheelBackground(android.R.color.transparent);
		mCity.setWheelForeground(android.R.color.transparent);
		mCity.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		mCity.setCyclic(true);

		mDistrict.setVisibleItems(4); // Number of items
		mDistrict.setWheelBackground(android.R.color.transparent);
		mDistrict.setWheelForeground(android.R.color.transparent);
		mDistrict.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		mDistrict.setCyclic(true);

		OnWheelChangedListener provListener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				mCityList.clear();
				mDistictList.clear();
				//获取当前选中的地址城市信息
				cityVoList = provinceVoList.get(wheel.getCurrentItem()).getCityVoList();
				//获取当前选中的地区信息
				districtVoList = cityVoList.get(0).getDistrictVoList();
				//获取城市列表信息
				for(int i = 0; i < cityVoList.size();i++){
					mCityList.add(cityVoList.get(i).getCityName());			
				}				
				//获取区域名字列表信息
				for(int i = 0; i <  districtVoList.size();i++){
					mDistictList.add(districtVoList.get(i).getDistrictName());
				}
				
				curProvName =  provinceVoList.get(wheel.getCurrentItem()).getProvinceName();
				curProvinceId = provinceVoList.get(wheel.getCurrentItem()).getProvinceId();
				provinceIndex = newValue;
				//刷新城市信息
				mCity.setViewAdapter(null);	
				mCityAdapter = new ArrayAdapter(mContext,mCityList, cityIndex);
				mCity.setViewAdapter(mCityAdapter);
				mCity.setCurrentItem(0);

				// 地区
				mDistrict.setViewAdapter(null);		
				mDistrictAdapter= new ArrayAdapter(mContext,mDistictList, districtIndex);
				mDistrict.setViewAdapter(mDistrictAdapter);
				mDistrict.setCurrentItem(0);
				
				//curDistrictId = provinceVoList.get(newValue).getProvinceId();
				//获取当前区域名字信息
				if(mCityList.size() > 0){
					curCityName = mCityList.get(0);
					curCityId = cityVoList.get(0).getCityId();
				}else{
					curCityId = curProvinceId;
					curCityName = "";
				}
				//获取区域信息
				if(mDistictList.size() > 0){
					curDistrictName = mDistictList.get(0);
					curDistrictId = districtVoList.get(0).getDistrictId();
				}else{
					curDistrictId = curCityId;
					curDistrictName = "";
				}
				
				Log.i(TAG,"oldValue "+oldValue+" newValue" +newValue+ 
						" item "+wheel.getCurrentItem()+" curProvName"+curProvName);
				
			}
		};
		
		//城市数据滚动
		OnWheelChangedListener cityListener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				mDistictList.clear();				
				//获取当前选中的地区信息
				cityVoList = provinceVoList.get(provinceIndex).getCityVoList();
				districtVoList = cityVoList.get(wheel.getCurrentItem()).getDistrictVoList();			
				//获取区域名字列表信息
				for(int i = 0; i <  districtVoList.size();i++){
					mDistictList.add(districtVoList.get(i).getDistrictName());
				}
				//城市ID							
				curCityId =cityVoList.get(wheel.getCurrentItem()).getCityId() ;
				curCityName = cityVoList.get(wheel.getCurrentItem()).getCityName();
				//获取区域信息
				if(mDistictList.size() > 0){
					curDistrictName = mDistictList.get(0);
					curDistrictId = districtVoList.get(0).getDistrictId();
				}else{
					curDistrictId = curCityId;
					curDistrictName = "";
				}
				cityIndex = newValue;
				// 地区
				mDistrict.setViewAdapter(null);		
				mDistrictAdapter= new ArrayAdapter(mContext,mDistictList, districtIndex);
				mDistrict.setViewAdapter(mDistrictAdapter);
				mDistrict.setCurrentItem(0);
				cityIndex = newValue;
				Log.i(TAG,"oldValue "+oldValue+" newValue" +newValue+ 
						" item "+wheel.getCurrentItem()+" curDistrictName"+curDistrictName);
				
			}
		};
		
		
		OnWheelChangedListener distictListener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				//mDistictList.clear();				
				//获取当前选中的地区信息
				//Log.i("kyolee"," district name = "+districtVoList.get(wheel.getCurrentItem()).getDistrictName());
			/*	districtVoList = cityVoList.get(cityIndex).getDistrictVoList();
											
				//获取区域名字列表信息
				for(int i = 0; i <  districtVoList.size();i++){
					mDistictList.add(districtVoList.get(i).getDistrictName());
				}
				// 地区
				mDistrict.setViewAdapter(null);		
				mDistrictAdapter= new ArrayAdapter(mContext,mDistictList, districtIndex);
				mDistrict.setViewAdapter(mDistrictAdapter);
				mDistrict.setCurrentItem(0);*/
				//获取区域信息
				//cityIndex = newValue;
				districtVoList = cityVoList.get(cityIndex).getDistrictVoList();
				districtIndex = newValue;
				if(mDistictList.size() > 0){
					curDistrictName = mDistictList.get(newValue);
					curDistrictId = districtVoList.get(newValue).getDistrictId();
				}else{
					curDistrictId = curCityId;
					curDistrictName = "";
				}
				//curDistrictName = mDistictList.get(newValue);
				Log.i(TAG,"oldValue "+oldValue+" newValue" +newValue+ 
						" item "+wheel.getCurrentItem()+" curDistrictName"+curDistrictName);
			}
		};
		
		//省份
		mProvinceAdapter = new ArrayAdapter(mContext,mProvinceList, provinceIndex);
		mProvince.setViewAdapter(mProvinceAdapter);
		mProvince.setCurrentItem(provinceIndex);
		
		// 城市
		mCityAdapter = new ArrayAdapter(mContext,mCityList, cityIndex);
		mCity.setViewAdapter(mCityAdapter);
		mCity.setCurrentItem(cityIndex);

		// 地区
		mDistrictAdapter= new ArrayAdapter(mContext,mDistictList, districtIndex);
		mDistrict.setViewAdapter(mDistrictAdapter);
		mDistrict.setCurrentItem(districtIndex);
		
		mProvince.addChangingListener(provListener);
		mCity.addChangingListener(cityListener);
		mDistrict.addChangingListener(distictListener);
	}

	

	public Button getConfirmButton() {
		return mConfirmButton;

	}

	public Button getCancelButton() {
		return mCancelButton;

	}

	public TextView getTitle() {
		return mTitle;

	}



	private class ArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		// int currentItem;
		// Index of item to be highlighted
		// int currentValue;

		/**
		 * Constructor
		 */
		public ArrayAdapter(Context context, ArrayList<String> items, int current) {
			super(context, items);
			// this.currentValue = current;
			setTextSize(20);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			// if (currentItem == currentValue) {
			// view.setTextColor(0xFF0000F0);
			// }
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			// currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}
	
	//返回选择的区域信息
	public String getSelectErea(){
		String ret = "";
		
		if(curProvName.equals(curCityName)){
			ret = curProvName+curDistrictName;
		}else{
			ret = curProvName+curCityName+curDistrictName;
		}
		
		return ret;
	}

	/**
	 * 获取省份id
	 * @return
	 */
	public Integer getCurProvinceId() {
		return curProvinceId;
	}

	/**
	 * 获取城市id
	 * @return
	 */
	public Integer getCurCityId() {
		return curCityId;
	}

	/**
	 * 获取区域id
	 * @return
	 */

	public Integer getCurDistrictId() {
		return curDistrictId;
	}

	public String getCurProvName() {
		return curProvName;
	}

	public void setCurProvName(String curProvName) {
		this.curProvName = curProvName;
	}

	public String getCurCityName() {
		return curCityName;
	}

	public void setCurCityName(String curCityName) {
		this.curCityName = curCityName;
	}

	public String getCurDistrictName() {
		return curDistrictName;
	}

	public void setCurDistrictName(String curDistrictName) {
		this.curDistrictName = curDistrictName;
	}

	/**
	 * 显示已选择的项目名称
	 * 
	 * @param cardTypeId
	 */
		public void updateType(Integer proviceId,Integer cityId,Integer countryId) {
			List<CityVo> cityVoList;
			List<DistrictVo> districtVoList;
			
			if(proviceId == null){
				mProvince.setCurrentItem(0);
				mCity.setCurrentItem(0);
				mDistrict.setCurrentItem(0);
			}
			if(proviceId != null){
				//更新选择省份信息
				if(provinceVoList.size() >0){
					for(int i=0; i < provinceVoList.size();i++){
						if(provinceVoList.get(i).getProvinceId() == proviceId){
							mProvince.setCurrentItem(i);
							//更新选择城市信息
							cityVoList = provinceVoList.get(i).getCityVoList();
							if(cityVoList.size() >0){
								for(int j = 0;j < cityVoList.size();j++){
									if(cityId == cityVoList.get(j).getCityId()){										
										mCity.setCurrentItem(j);
										//查找地区
										districtVoList = cityVoList.get(j).getDistrictVoList();
										
										//判断地区信息
										if(districtVoList.size() >0){
											for(int p=0;p < districtVoList.size();p++){
												if(districtVoList.get(p).getDistrictId() == countryId){
													mDistrict.setCurrentItem(p);
													break;
												}
											}
										}
										
										//找到城市
										break;
									}
								}
							}
							
							//找到省份
							break;
						}
					}
				}
			}
//			if (TypeId!=null&&!TypeId.equals("")) {
//				if (dicVos.size() > 0) {
//					for (int i = 0; i < dicVos.size(); i++) {
//						if (dicVos.get(i).getVal()==TypeId) {
//							mWheelView.setCurrentItem(i);
//							break;
//						}
//					}
//				}
//			} else {
//				mWheelView.setCurrentItem(0);
//			}
		}
	
}

