package com.dfire.retail.app.manage.activity.shopchain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.ErrorMsg;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.CityVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.DistrictVo;
import com.dfire.retail.app.manage.data.ProvinceVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
import com.dfire.retail.app.manage.data.bo.ShopSaveBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.GsonBuilder;

public class ShopInfoBaseActivity extends TitleActivity {

	static final String TAG = "ShopInfoBaseActivity";
	private ShopVo mShopVo;
	private List<DicVo> mShopTypeList;
	private List<ProvinceVo> mAdressList;
	private List<AllShopVo> mShopList;

	public void showBackbtn() {
		if (mBack == null) {
			mBack = (ImageButton) findViewById(R.id.title_back);
		}
		mBack.setVisibility(View.VISIBLE);
		mLeft = (ImageButton) findViewById(R.id.title_left);
		mLeft.setImageResource(R.drawable.cancel);
		mLeft.setVisibility(View.GONE);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				ShopInfoBaseActivity.this.finish();
			}
		});

		mRight = (ImageButton) findViewById(R.id.title_right);
		mRight.setVisibility(View.GONE);
	}

	public ImageButton change2saveFinishMode() {
		if (mBack == null) {
			mBack = (ImageButton) findViewById(R.id.title_back);
		}
		mBack.setVisibility(View.VISIBLE);
		mLeft = (ImageButton) findViewById(R.id.title_left);
		mLeft.setImageResource(R.drawable.cancel);
		mLeft.setVisibility(View.GONE);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				ShopInfoBaseActivity.this.finish();
			}
		});

		mRight = (ImageButton) findViewById(R.id.title_right);
		mRight.setVisibility(View.GONE);
		mRight.setImageResource(R.drawable.save);
		return mRight;
	}

	//
	// public ImageButton change2saveMode() {
	// if (mBack == null) {
	// mBack = (ImageButton) findViewById(R.id.title_back);
	// }
	// mBack.setVisibility(View.GONE);
	// mLeft = (ImageButton) findViewById(R.id.title_left);
	// mLeft.setImageResource(R.drawable.cancel);
	// mLeft.setVisibility(View.VISIBLE);
	// mLeft.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// mBack.setVisibility(View.VISIBLE);
	// mLeft.setVisibility(View.GONE);
	// mRight.setVisibility(View.GONE);
	// mBack.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// ShopInfoBaseActivity.this.finish();
	// }
	// });
	//
	// }
	// });
	//
	// mRight = (ImageButton) findViewById(R.id.title_right);
	// mRight.setVisibility(View.GONE);
	// mRight.setImageResource(R.drawable.save);
	// setRightBtn(R.drawable.save);
	// return mRight;
	// }

	/**
	 * 将byte类型的图片数据、转换成bitmap类型
	 * 
	 * @param bytes
	 *            图片数据信息
	 * @param opts
	 *            转换选项
	 * @return
	 */
	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	/**
	 * 从输入流中读取数据、并转换成byte[]类型
	 * 
	 * @param inStream
	 *            相册图片的输入流
	 * @return 返回byte[]类型的数据流
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

	/**
	 * 通过省份ID，获取省份名字
	 * 
	 * @param provId
	 *            省份ID
	 * @return 返回省份名称
	 */
	public String getProvName(Integer provId, List<ProvinceVo> mAdressList) {
		String ret = "";
		for (int i = 0; i < mAdressList.size(); i++) {
			if (mAdressList.get(i).getProvinceId().equals(provId)) {
				ret = mAdressList.get(i).getProvinceName();
				break;
			}
			Log.i(TAG, " mAdressList " + i + " prvic = "
					+ mAdressList.get(i).getProvinceName());
		}
		return ret;
	}

	/**
	 * 获取城市名称
	 * 
	 * @param provId
	 *            省份ID
	 * @param cityId
	 *            城市ID
	 * @return 如果省份和城市ID都正确，返回城市名称,否则返回""
	 */
	public String getCityName(Integer provId, Integer cityId,
			List<ProvinceVo> mAdressList) {

		String ret = "";
		CityVo tmpCityVo;
		for (int i = 0; i < mAdressList.size(); i++) {
			// 如果找到对应的省份ID，终止循环
			if (mAdressList.get(i).getProvinceId().equals(provId)) {
				// 开始查找城市信息
				for (int j = 0; j < mAdressList.get(i).getCityVoList().size(); j++) {
					tmpCityVo = mAdressList.get(i).getCityVoList().get(j);
					// 如果找到对应的城市ID，终止循环
					if (tmpCityVo.getCityId().equals(cityId)) {
						ret = tmpCityVo.getCityName();
						break;
					}
				}
				break;
			}
			Log.i(TAG, " mAdressList " + i + " prvic = "
					+ mAdressList.get(i).getProvinceName());
		}
		return ret;
	}

	/**
	 * 获取城市名称
	 * 
	 * @param provId
	 *            省份ID
	 * @param cityId
	 *            城市ID
	 * @return 如果省份和城市ID都正确，返回城市名称,否则返回""
	 */
	public Integer getCityID(Integer provId, String cityName,
			List<ProvinceVo> mAdressList) {

		Integer ret = null;
		CityVo tmpCityVo;
		for (int i = 0; i < mAdressList.size(); i++) {
			// 如果找到对应的省份ID，终止循环
			if (mAdressList.get(i).getProvinceId().equals(provId)) {
				// 开始查找城市信息
				for (int j = 0; j < mAdressList.get(i).getCityVoList().size(); j++) {
					tmpCityVo = mAdressList.get(i).getCityVoList().get(j);
					// 如果找到对应的城市ID，终止循环
					if (tmpCityVo.getCityName().equals(cityName)) {
						ret = tmpCityVo.getCityId();
						break;
					}
				}
				break;
			}
			Log.i(TAG, " mAdressList " + i + " prvic = "
					+ mAdressList.get(i).getProvinceName());
		}
		return ret;
	}

	public String getDistrictName(Integer provId, Integer cityId,
			Integer countyId, List<ProvinceVo> mAdressList) {
		String ret = "";
		CityVo tmpCityVo;
		DistrictVo districtVo;

		for (int i = 0; i < mAdressList.size(); i++) {
			// 如果找到对应的省份ID，终止循环
			if (mAdressList.get(i).getProvinceId().equals(provId)) {
				// 开始查找城市信息
				for (int j = 0; j < mAdressList.get(i).getCityVoList().size(); j++) {
					tmpCityVo = mAdressList.get(i).getCityVoList().get(j);
					// 如果找到对应的城市ID，终止循环
					if (tmpCityVo.getCityId().equals(cityId)) {

						// 查询对应的区域信息
						for (int m = 0; m < tmpCityVo.getDistrictVoList()
								.size(); m++) {

							districtVo = tmpCityVo.getDistrictVoList().get(m);
							Log.i("districtVo","districtVo = "+ districtVo.getDistrictName());
							if (districtVo.getDistrictId().equals(countyId)) {
								ret = districtVo.getDistrictName();
								break;
							}
						}

						break;
					}
				}
				break;
			}
			Log.i(TAG, " mAdressList " + i + " prvic = "
					+ mAdressList.get(i).getProvinceName());
		}
		return ret;
	}

	/**
	 */
	public String getShopTypeName(List<DicVo> shopTypeList, String shopType) {

		String ret = "";
		for (int i = 0; i < shopTypeList.size(); i++) {
			if (shopTypeList.get(i).getVal().equals(Integer.valueOf(shopType))) {

				ret = shopTypeList.get(i).getName();
				break;
			}

		}
		return ret;
	}

	public Integer getDistrictID(Integer provId, Integer cityId, String name,
			List<ProvinceVo> mAdressList) {

		Integer ret = 0;
		CityVo tmpCityVo;
		DistrictVo districtVo;

		for (int i = 0; i < mAdressList.size(); i++) {
			// 如果找到对应的省份ID，终止循环
			if (mAdressList.get(i).getProvinceId().equals(provId)) {
				// 开始查找城市信息
				for (int j = 0; j < mAdressList.get(i).getCityVoList().size(); j++) {
					tmpCityVo = mAdressList.get(i).getCityVoList().get(j);
					// 如果找到对应的城市ID，终止循环
					if (tmpCityVo.getCityId().equals(cityId)) {

						// 查询对应的区域信息
						for (int m = 0; m < tmpCityVo.getDistrictVoList()
								.size(); m++) {

							districtVo = tmpCityVo.getDistrictVoList().get(m);
							Log.i("districtVo",
									"districtVo = "
											+ districtVo.getDistrictName());
							if (districtVo.getDistrictName().equals(name)) {
								ret = districtVo.getDistrictId();
								break;
							}
						}

						break;
					}
				}
				break;
			}
			Log.i(TAG, " mAdressList " + i + " prvic = "
					+ mAdressList.get(i).getProvinceName());
		}
		return ret;
	}

	public String getStreetName(Integer provId, Integer cityId,
			Integer countyId, Integer streetId) {

		return "";
	}

	public void updateView() {

	}

	/**
	 * 处理返回的结果，如果成功解析网络返回的json数据
	 */
	public Handler mShopHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// getProgressDialog().dismiss();

			switch (msg.what) {
			case Constants.HANDLER_SUCESS:
				if (msg.obj != null) {
					ToastUtil.showShortToast(ShopInfoBaseActivity.this,
							msg.obj.toString());
				}
				// if(msg.arg1 == 1){
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				ShopInfoBaseActivity.this.finish();
				// }

				break;

			case Constants.HANDLER_FAIL:
				if (msg.obj != null) {
					ToastUtil.showShortToast(getApplicationContext(),
							ErrorMsg.getErrorMsg(msg.obj.toString()));
					if (msg.arg1 == 1) {
						ShopInfoBaseActivity.this.finish();
					}
				}

				break;
			case Constants.HANDLER_ERROR:
				if (msg.obj != null) {
					ToastUtil.showShortToast(getApplicationContext(),
							msg.obj.toString());
				}
				if (msg.arg1 == 1)
					ShopInfoBaseActivity.this.finish();

				break;
			}
		}
	};

	/**
	 * 保存修改数据
	 */
	public void saveShopInfo(final String operate, ShopVo shopVo) {

		// 显示进度条对话框

		RequestParameter params = new RequestParameter(true);

		params.setUrl(Constants.SHOPSAVE);
		// mShopVo.setFile(mContent);
		// params.setParam(Constants.SHOP,mShopVo.toString());
		params.setParam(Constants.OPT_TYPE, operate);

		try {
			params.setParam(Constants.SHOP, new JSONObject(new GsonBuilder()
					.serializeNulls().create().toJson(shopVo)));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		AsyncHttpPost httpsave = new AsyncHttpPost(ShopInfoBaseActivity.this,
				params, ShopSaveBo.class, true, new RequestCallback() {
					@Override
					public void onSuccess(Object str) {

						// getProgressDialog().dismiss();
						//
						// JsonUtil ju = new JsonUtil(str.toString());
						ShopSaveBo bo = (ShopSaveBo) str;
						Message msg = new Message();
						// if(bo.getReturnCode().equals(Constants.SUCCESS)){
						msg.what = Constants.HANDLER_SUCESS;
						if (Constants.ADD.equals(operate)) {
							msg.arg1 = 1;
						}
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						ShopInfoBaseActivity.this.finish();

					}

					@Override
					public void onFail(Exception e) {

						// getProgressDialog().dismiss();
						e.printStackTrace();
						Log.e("results", "Login FaiL");
						Message msg = new Message();
						msg.what = Constants.HANDLER_ERROR;
						msg.obj = e.getMessage();
						mShopHandler.sendMessage(msg);
					}
				});
		httpsave.execute();
	}

	/**
	 * 网络上获取图片资源
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static byte[] getImage(String path) throws IOException {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET"); // 设置请求方法为GET
		conn.setReadTimeout(5 * 1000); // 设置请求过时时间为5秒
		InputStream inputStream = conn.getInputStream(); // 通过输入流获得图片数据
		byte[] data = readInputStream(inputStream); // 获得图片的二进制数据
		return data;

	}

	/*
	 * 从数据流中获得数据
	 */
	public static byte[] readInputStream(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();

	}

	/*
	 * 通过网络请求，删除门店信息
	 */
	public void deleteShopInfo(String deleteShopID) {
		// 传递请求参数
		RequestParameter param = new RequestParameter(true);
		param.setUrl(Constants.SHOPDELETE);
		param.setParam(Constants.SHOP_ID, deleteShopID);

		AsyncHttpPost httppost = new AsyncHttpPost(ShopInfoBaseActivity.this,
				param, BaseRemoteBo.class, true, new RequestCallback() {
					@Override
					public void onSuccess(Object str) {

						BaseRemoteBo bo = (BaseRemoteBo) str;
						//
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						ShopInfoBaseActivity.this.finish();

					}

					@Override
					public void onFail(Exception e) {

					}
				});
		httppost.execute();

	}

}
