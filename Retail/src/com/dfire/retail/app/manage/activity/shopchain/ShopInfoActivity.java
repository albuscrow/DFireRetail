package com.dfire.retail.app.manage.activity.shopchain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.ItemPortraitImage;
import com.dfire.retail.app.common.item.listener.IItemIsChangeListener;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.adapter.ShopInfoAdapter;
import com.dfire.retail.app.manage.adapter.ShopInfoItem;
import com.dfire.retail.app.manage.common.AddImageDialog;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.common.SelectEreaDialog;
import com.dfire.retail.app.manage.common.SelectTimeDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ProvinceVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.ShopInitBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.util.Utility;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;

/**
 * 店家信息
 * 
 * @author Administrator
 * 
 */
@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class ShopInfoActivity extends ShopInfoBaseActivity implements
		OnClickListener, IItemListListener, IOnItemSelectListener,
		IItemIsChangeListener {
	static final String TAG = "ShopInfo";
	ArrayList<String> mAreaList = new ArrayList<String>();
	AsyncHttpPost httppost;
	ArrayList<ShopInfoItem> mSubStoreList;
	private ShopInfoAdapter mSubAdapter;
	private ListView mSubStoreListView;
	ListView mMenuList = null;
	private ShopVo mShopVo;
	private List<DicVo> mShopTypeList;
	private List<ProvinceVo> mAdressList;
	private List<AllShopVo> mShopList;
	String shopStr;
	private EditText mEditCode;
	private LinearLayout mSearchLine = null;
	private LinearLayout mSubCountLine;
	private TextView textShopCount;
	private ImageView retailAddSubShop;
	private AddImageDialog addDialog;
	private static final int CAPTURERETURNCODE = 1;
	private static final int ALBUMRETURNCODE = 2;
	Bitmap myBitmap;
	private SelectTimeDialog mStartTime;
	private SelectTimeDialog mEndTime;
	private SelectEreaDialog mSelectErea;
	private Integer tmpSelectProvId, tmpSelectCityId, tmpSelectDistrictId;
	private String tmpSelectCityName, tmpSelectDistictName;
	private ItemPortraitImage mLogoImage;
	private String startSelectTime,endSelectTime;
	// 店家区域列表
	private ItemEditList mEL_RetailArea;
	// 门店开始营业时间
	private ItemEditList mEL_RetailStartTime;
	// 门店结束营业时间
	private ItemEditList mEL_RetailEndTime;
	// 门店名称
	private ItemEditText mED_RetailName;
	// 门店联系电话
	private ItemEditText mED_RetailPhone;
	// 门店微信
	private ItemEditText mED_RetailWeixin;
	// 门店详细地址
	private ItemEditText mED_RetailAdress;
	// 店家编号
	private ItemEditText mTX_RetailNO;
	private final static int ADD_PHOTO = 1;
	private final static int ADDCHILDSHOP = 6;
	private final static int SEARCHCHILDSHOP = 7;
	// add the new window
	WindowManager mWindowManager;
	Window mWindow;
	private Button mAddChildShop;
	private boolean[] isChange;
	private boolean isSaveMode = false;
	private final static int EDITCOUNDT = 9;
	private LinearLayout mAddChildShopLine;
	private View mBottomView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_retail_info);

		setTitleRes(R.string.shop_info);
		// 初始化是否改变呢信息
		isChange = new boolean[EDITCOUNDT];
		for (int i = 0; i < EDITCOUNDT; i++) {
			isChange[i] = false;
		}
		showBackbtn();
		findView();
		initView();
		getRetialInfo();
		httppost.execute();
	}

	/**
	 * 查找控件ID信息
	 * 
	 */
	private void findView() {
		mSearchLine = (LinearLayout) findViewById(R.id.retail_search_line);
		mSubStoreListView = (ListView) findViewById(R.id.sub_store_info_detail_list);
		mSubStoreListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 点击门店信息，跳转到子门店
				Intent intent = new Intent(ShopInfoActivity.this,
						ChildShopInfoActivity.class);
				intent.putExtra(Constants.SEARCHSHOPCODE, mShopVo.getShopList()
						.get(arg2).getCode());
				intent.putExtra(Constants.SHOPARENTNAME, mShopVo.getShopName());
				startActivityForResult(intent, ADDCHILDSHOP);

			}
		});
		mLogoImage = (ItemPortraitImage) findViewById(R.id.retailLogoImage);
		mLogoImage.initData(null);
		mLogoImage.setIsChangeListener(this.getItemChangeListener());
		mLogoImage.initLabel("店家LOGO", "");
		mLogoImage.getPortImge().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showAddMenu();
			}
		});
		mEL_RetailArea = (ItemEditList) findViewById(R.id.retailArea);
		mEL_RetailStartTime = (ItemEditList) findViewById(R.id.retailStartTime);
		mEL_RetailEndTime = (ItemEditList) findViewById(R.id.retailEndTime);
		mED_RetailName = (ItemEditText) findViewById(R.id.retailName);
		mED_RetailAdress = (ItemEditText) findViewById(R.id.retailDetailAdress);
		mED_RetailPhone = (ItemEditText) findViewById(R.id.retailPhone);
		mED_RetailWeixin = (ItemEditText) findViewById(R.id.retailWeixin);
		mTX_RetailNO = (ItemEditText) findViewById(R.id.retailNo);

		mEditCode = (EditText) findViewById(R.id.edit_shop_code);
		mAddChildShop = (Button) findViewById(R.id.add_child_shop);
		mSubCountLine = (LinearLayout) findViewById(R.id.retailCountLinear);
		textShopCount = (TextView) findViewById(R.id.subShopCount);
		retailAddSubShop = (ImageView) findViewById(R.id.retailAddSubShop);

		mAddChildShopLine = (LinearLayout) findViewById(R.id.addChildShopLine);
		mBottomView = (View) findViewById(R.id.retailBottom);

		mEL_RetailArea.setOnClickListener(this);
		mEL_RetailEndTime.setOnClickListener(this);
		mEL_RetailStartTime.setOnClickListener(this);
		mAddChildShop.setOnClickListener(this);
		retailAddSubShop.setOnClickListener(this);
		mLogoImage.setOnClickListener(this);
		// 单店下，以下空间不显示
		if (RetailApplication.getEntityModel() != null
				&& RetailApplication.getEntityModel().intValue() == 1) {
			mBottomView.setVisibility(View.GONE);
			mAddChildShopLine.setVisibility(View.GONE);
			mSubCountLine.setVisibility(View.GONE);
			mAddChildShop.setVisibility(View.GONE);
			mSubStoreListView.setVisibility(View.GONE);
			mSearchLine.setVisibility(View.GONE);
		}
		addDialog = new AddImageDialog(ShopInfoActivity.this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 初始化页面显示信息
	 */
	private void initView() {
		mStartTime = new SelectTimeDialog(ShopInfoActivity.this, false);
		mEndTime = new SelectTimeDialog(ShopInfoActivity.this, false);
		mED_RetailName.initLabel("店家名称", "", Boolean.TRUE,InputType.TYPE_CLASS_TEXT);
		mED_RetailName.setMaxLength(50);
		mTX_RetailNO.initLabel("店家代码", "", Boolean.TRUE,InputType.TYPE_CLASS_TEXT);
		mTX_RetailNO.setDigitsAndNum(true);
		mTX_RetailNO.setMaxLength(50);
		mEL_RetailArea.initLabel("所在区域", "", Boolean.TRUE, this);
		mED_RetailAdress.initLabel("详细地址", "", Boolean.FALSE,InputType.TYPE_CLASS_TEXT);
		mED_RetailAdress.setMaxLength(100);
		mED_RetailPhone.initLabel("联系电话", "", Boolean.FALSE,InputType.TYPE_CLASS_NUMBER);
		mED_RetailPhone.setMaxLength(13);
		mED_RetailWeixin.initLabel("微信", "", Boolean.FALSE,InputType.TYPE_CLASS_TEXT);
		mED_RetailWeixin.setMaxLength(50);
		mEL_RetailStartTime.initLabel("开始时间", "", Boolean.TRUE, this);
		mEL_RetailEndTime.initLabel("结束时间", "", Boolean.TRUE, this);
		mLogoImage.initLabel("店家LOGO", "");

		mSubStoreList = new ArrayList<ShopInfoItem>();
		mSubAdapter = new ShopInfoAdapter(ShopInfoActivity.this, mSubStoreList);
		mSubStoreListView.setAdapter(mSubAdapter);
		Utility.setListViewHeightBasedOnChildren(mSubStoreListView);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (ADDCHILDSHOP == requestCode && data != null) {
			getRetialInfo();
			httppost.execute();
			updateView();
		} else if (SEARCHCHILDSHOP == requestCode && data != null) {
			Intent intent = new Intent(ShopInfoActivity.this,
					ChildShopInfoActivity.class);
			String tmpShopCode = data.getStringExtra(Constants.SHOPCODE);
			String tmpShopName = data.getStringExtra(Constants.SHOPCOPNAME);
			intent.putExtra(Constants.SHOPCODE, tmpShopCode);
			intent.putExtra(Constants.SHOPARENTNAME, tmpShopName);

			startActivityForResult(intent, ADDCHILDSHOP);
		}
		/**
		 * 因为两种方式都用到了startActivityForResult方法，这个方法执行完后都会执行onActivityResult方法，
		 * 所以为了区别到底选择了那个方式获取图片要进行判断
		 * ，这里的requestCode跟startActivityForResult里面第二个参数对应
		 */
		else if (requestCode == CAPTURERETURNCODE) {
			// 获得图片的uri
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/mytest.jpg");
			startPhotoZoom(Uri.fromFile(temp));

			// 将图片开始crop
		} else if (requestCode == ALBUMRETURNCODE && data != null) {
			startPhotoZoom(data.getData());
		}
		// 取得裁剪后的图片
		else if (requestCode == 3 && data != null) {
			if (data != null) {
				setPicToView(data);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			/*
			 * ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 * photo.compress(Bitmap.CompressFormat.JPEG, 60, stream); byte[] b
			 * = stream.toByteArray(); // 将图片流以字符串形式存储下来 tp = new
			 * String(Base64Coder.encodeLines(b));
			 * 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了， 服务器处理的方法是服务器那边的事
			 * 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换 为我们可以用的图片类型 Bitmap
			 * dBitmap = BitmapFactory.decodeFile(tp); Drawable drawable = new
			 * BitmapDrawable(dBitmap);
			 */
			mLogoImage.changeData(photo);
		}

	}

	public void addClick(View v) {
		switch (Integer.parseInt(String.valueOf(v.getTag()))) {
		case ADD_PHOTO:
			showAddMenu();
			break;
		case 5:
			searchShopBycode(mEditCode.getText().toString());
			break;
		}
	}

	private void showAddMenu() {
		if (addDialog == null)
			addDialog = new AddImageDialog(ShopInfoActivity.this);
		addDialog.show();
		// 从相册添加图片
		if (mLogoImage.getBitmapPortrait() == null) {
			mLogoImage.getImg().setVisibility(View.INVISIBLE);
		} else {
			mLogoImage.getImg().setVisibility(View.VISIBLE);
		}
		addDialog.getAddFromAlbum().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent1, ALBUMRETURNCODE);
				if (addDialog != null)
					addDialog.dismiss();
			}
		});
		// 从相机添加图片
		addDialog.getAddFromCapture().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 下面这句指定调用相机拍照后的照片存储的路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment
								.getExternalStorageDirectory(), "mytest.jpg")));
				startActivityForResult(intent, CAPTURERETURNCODE);
				if (addDialog != null)
					addDialog.dismiss();
			}
		});
		addDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addDialog.dismiss();
			}
		});

	}

	@Override
	public void onItemClick(int pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemListClick(ItemEditList obj) {
		// TODO Auto-generated method stub

		if (obj.getId() == R.id.retailEndTime) {
			pushEndTime();
		}
		// 开始营业时间
		else if (obj.getId() == R.id.retailStartTime) {
			pushStartTime();
		}
		// 如果是选择区域，显示选择区域对话框
		else if (obj.getId() == R.id.retailArea) {
			selectErea();
			// 新增子门店
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 营业结束时间
		if (v.getId() == R.id.retailEndTime) {
			pushEndTime();
		}
		// 开始营业时间
		else if (v.getId() == R.id.retailStartTime) {
			pushStartTime();
		}
		// 如果是选择区域，显示选择区域对话框
		else if (v.getId() == R.id.retailArea) {
			selectErea();
			// 新增子门店
		} else if (v.getId() == R.id.add_child_shop
				|| v.getId() == R.id.retailAddSubShop) {
			Intent intent = new Intent(ShopInfoActivity.this,
					AddChildShopActivity.class);
			// 传递父门店的信息，门店类型，门店ID,门店父名称
			intent.putExtra(Constants.SHOPTYPE,
					getShopTypeName(mShopTypeList, mShopVo.getShopType()));
			intent.putExtra(Constants.SHOPARENTNAME, mShopVo.getShopName());
			intent.putExtra(Constants.DATAFROMSHOPID, mShopVo.getShopId());
			intent.putExtra(Constants.PARENTID, mShopVo.getParentId());
			intent.putExtra(Constants.SHOPNAME, mShopVo.getShopName());
			intent.putExtra(Constants.SHOPCODE, mShopVo.getCode());

			startActivityForResult(intent, ADDCHILDSHOP);

		}
	}
	/**
	 * 选择开始时间
	 */
	private void pushStartTime() {
		mStartTime.show();
		mStartTime.getTitle().setText(ShopInfoActivity.this.getResources().getString(R.string.starttime));
		mStartTime.updateDays(startSelectTime);
		mStartTime.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mStartTime.dismiss();
				startSelectTime = mStartTime.getCurrentTime();
				mEL_RetailStartTime.changeData(startSelectTime, startSelectTime);
			}
		});
		mStartTime.getCancelButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mStartTime.dismiss();
				mStartTime.closeOptionsMenu();

			}
		});
	}
	/**
	 * 选择结束时间
	 */
	private void pushEndTime() {
		mEndTime.show();
		mEndTime.getTitle().setText(ShopInfoActivity.this.getResources().getString(R.string.endtime));
		mEndTime.updateDays(endSelectTime);
		mEndTime.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mEndTime.dismiss();
				endSelectTime = mEndTime.getCurrentTime();
				mEL_RetailEndTime.changeData(endSelectTime, endSelectTime);

			}
		});
		mEndTime.getCancelButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEndTime.dismiss();
				mEndTime.closeOptionsMenu();

			}
		});
	}

	/*
	 * 通过网络请求，获取门店信息
	 */
	private void getRetialInfo() {
		// 传递请求参数
		RequestParameter param = new RequestParameter(true);
		param.setUrl(Constants.SHOPINIT);
		String shopId = RetailApplication.getShopVo().getShopId();
		param.setParam(Constants.SHOP_ID, shopId);

		httppost = new AsyncHttpPost(ShopInfoActivity.this, param,
				ShopInitBo.class, true, new RequestCallback() {
					@Override
					public void onSuccess(Object str) {
						ShopInitBo bo = (ShopInitBo) str;
						mShopVo = bo.getShop();
						mShopTypeList = bo.getShopTypeList();
						mShopList = bo.getShopList();
						mAdressList = bo.getAddressList();
						// 保存省份城市区域信息
						RetailApplication.setProvinceVo(mAdressList);
						RetailApplication.setShopTypeList(mShopTypeList);
						RetailApplication.setCompanyShopList(mShopList);
						tmpSelectCityId = mShopVo.getCityId();
						tmpSelectDistrictId = mShopVo.getCountyId();
						tmpSelectProvId = mShopVo.getProvinceId();
						// 更新UI显示信息
						updateView();
					}

					@Override
					public void onFail(Exception e) {
						ShopInfoActivity.this.finish();
					}
				});

	}

	/**
	 * 搜索数据
	 */
	protected void searchShopBycode(String shopCode) {

		Intent intent = new Intent(ShopInfoActivity.this,
				SearchResultActivity.class);
		// 传递父门店的信息，门店类型，门店ID,门店父名称
		intent.putExtra(Constants.SHOPTYPE,
				getShopTypeName(mShopTypeList, mShopVo.getShopType()));
		intent.putExtra(Constants.SHOPARENTNAME, mShopVo.getShopName());
		intent.putExtra(Constants.DATAFROMSHOPID, mShopVo.getShopId());
		intent.putExtra(Constants.SHOPNAME, mShopVo.getShopName());
		intent.putExtra(Constants.SHOPCODE, mShopVo.getCode());
		intent.putExtra(Constants.SHOPKEYWORD, shopCode);

		startActivityForResult(intent, SEARCHCHILDSHOP);
	}

	/**
	 * 网络请求成功，更新UI信息
	 */
	public void updateView() {
		mED_RetailName.initData(mShopVo.getShopName());
		mTX_RetailNO.initData(mShopVo.getCode());
		String area = "";
		tmpSelectProvId = mShopVo.getProvinceId();
		tmpSelectCityId = mShopVo.getCityId();
		tmpSelectProvId = mShopVo.getProvinceId();

		if (mShopVo.getProvinceId() != null) {
			area += getProvName(mShopVo.getProvinceId(), mAdressList);
		}
		if (mShopVo.getCityId() != null) {
			String tmpCity = "";
			tmpCity = getCityName(mShopVo.getProvinceId(), mShopVo.getCityId(),
					mAdressList);
			tmpSelectCityName = tmpCity;
			if (!area.equals(tmpCity)) {
				area += tmpCity;
			}

		}
		if (mShopVo.getCountyId() != null) {
			tmpSelectDistictName = getDistrictName(mShopVo.getProvinceId(),
					mShopVo.getCityId(), mShopVo.getCountyId(), mAdressList);
			area += tmpSelectDistictName;
		}

		mEL_RetailArea.initData(area, area);

		mED_RetailAdress.initData(mShopVo.getAddress());

		mED_RetailPhone.initData(mShopVo.getPhone1());

		mED_RetailWeixin.initData(mShopVo.getWeixin());

		mEL_RetailStartTime.initData(mShopVo.getStartTime(),mShopVo.getStartTime());
		startSelectTime = mShopVo.getStartTime();
		mEL_RetailEndTime.initData(mShopVo.getEndTime(), mShopVo.getStartTime());
		endSelectTime = mShopVo.getEndTime();
		if (!CommonUtils.isEmpty(mShopVo.getFileName())) {
			new GetImageThread(mShopVo.getFileName()).start();
		} else {
			mLogoImage.initData(null);
		}
		if (mShopVo.getShopList().size() > 0) {
			mSubStoreList.clear();
			mSubCountLine.setVisibility(View.VISIBLE);
			mSubStoreListView.setVisibility(View.VISIBLE);
			textShopCount.setText(String.format(ShopInfoActivity.this
					.getResources().getString(R.string.shopcountmsg), mShopVo
					.getShopList().size()));

			for (int i = 0; i < mShopVo.getShopList().size(); i++) {
				mSubStoreList.add(new ShopInfoItem(mShopVo.getShopList().get(i)
						.getShopId(), mShopVo.getShopList().get(i)
						.getShopName(), mShopVo.getShopList().get(i)
						.getParentId(), ShopInfoActivity.this.getResources()
						.getString(R.string.shop_code)
						+ mShopVo.getShopList().get(i).getCode()));
				mSubAdapter.notifyDataSetChanged();
			}
			// 如果下家门店信息为null
		} else {
			mSubCountLine.setVisibility(View.GONE);
			mSubStoreListView.setVisibility(View.GONE);

		}
		if (RetailApplication.getEntityModel().intValue() == 1
				|| getShopTypeName(mShopTypeList, mShopVo.getShopType())
						.equals(ShopInfoActivity.this.getResources().getString(
								R.string.shop))) {

			mBottomView.setVisibility(View.GONE);
			mAddChildShopLine.setVisibility(View.GONE);
			mSubCountLine.setVisibility(View.GONE);
			mAddChildShop.setVisibility(View.GONE);
			mSubStoreListView.setVisibility(View.GONE);
			mSearchLine.setVisibility(View.GONE);
		}
		mSubAdapter = new ShopInfoAdapter(ShopInfoActivity.this, mSubStoreList);
		mSubStoreListView.setAdapter(mSubAdapter);
		Utility.setListViewHeightBasedOnChildren(mSubStoreListView);

		mED_RetailAdress.setIsChangeListener(this);
		mED_RetailName.setIsChangeListener(this);
		mED_RetailPhone.setIsChangeListener(this);
		mED_RetailWeixin.setIsChangeListener(this);
		mEL_RetailArea.setIsChangeListener(this);
		mEL_RetailStartTime.setIsChangeListener(this);
		mEL_RetailEndTime.setIsChangeListener(this);
		mLogoImage.setIsChangeListener(this);
		mTX_RetailNO.setIsChangeListener(this);

	}

	public void selectErea() {
		if (mAdressList == null)
			ToastUtil.showLongToast(ShopInfoActivity.this, "地址信息为空，无法选择！");
		mSelectErea = new SelectEreaDialog(ShopInfoActivity.this, mAdressList,
				0, 0, 0);
		mSelectErea.show();
		mSelectErea.updateType(tmpSelectProvId, tmpSelectCityId,
				tmpSelectDistrictId);
		mSelectErea.getConfirmButton().setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						tmpSelectCityName = mSelectErea.getCurCityName();
						tmpSelectDistictName = mSelectErea.getCurDistrictName();
						tmpSelectProvId = mSelectErea.getCurProvinceId();
						tmpSelectCityId = getCityID(tmpSelectProvId,
								tmpSelectCityName, mAdressList);
						tmpSelectDistrictId = getDistrictID(tmpSelectProvId,
								tmpSelectCityId, tmpSelectDistictName,
								mAdressList);

						mEL_RetailArea.changeData(mSelectErea.getSelectErea(),
								mSelectErea.getSelectErea());
						mSelectErea.dismiss();
					}
				});

		mSelectErea.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSelectErea.dismiss();
			}
		});
	}

	/**
	 * 检测当前的数据是否符合要求
	 * 
	 * @return
	 */
	private String checkShopVo() {

		String ret = null;
		if (CommonUtils.isEmpty(mED_RetailName.getCurrVal())) {
			ret = "店家名称不能为空，请输入!";
		} else if (CommonUtils.isEmpty(mTX_RetailNO.getCurrVal())) {
			ret = "店家代码不能为空，请输入！";
		} else if (tmpSelectCityId == null || tmpSelectProvId == null) {
			ret = "所在地区不能为空，请输入!";
		} else if (!CommonUtils.isEmpty(mED_RetailPhone.getCurrVal())) {
			if (mED_RetailPhone.getCurrVal().length() != 0
					&& mED_RetailPhone.getCurrVal().length() != 11
					&& mED_RetailPhone.getCurrVal().length() != 13) {
				ret = "电话号码是11或13位！";
			}
		}

		return ret;
	}

	@Override
	public void onItemIsChangeListener(View v) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onItemIsChangeListener test");
		switch (v.getId()) {
		// 店家名称修改
		case R.id.retailArea:
			isChange[0] = mEL_RetailArea.getChangeStatus();
			// setTitleText(mED_ChildShopName.getCurrVal());
			break;
		// 店家地址信息修改
		case R.id.retailStartTime:
			isChange[1] = mEL_RetailStartTime.getChangeStatus();

			break;
		// 店家联系方式
		case R.id.retailEndTime:
			isChange[2] = mEL_RetailEndTime.getChangeStatus();
			break;
		// 店家微信
		case R.id.retailName:
			isChange[3] = mED_RetailName.getChangeStatus();
			break;
		// 店家编号
		case R.id.retailDetailAdress:
			isChange[4] = mED_RetailAdress.getChangeStatus();
			break;
		// 员工工号
		case R.id.retailPhone:
			isChange[5] = mED_RetailPhone.getChangeStatus();
			break;

		case R.id.retailWeixin:
			isChange[6] = mED_RetailWeixin.getChangeStatus();
			// 统计当前数据时候有变化

			break;
		// LOGO图标，暂时未实现保留
		case R.id.retailLogoImage:
			isChange[7] = mLogoImage.getChangeStatus();
			break;
		case R.id.retailNo:
			isChange[8] = mTX_RetailNO.getChangeStatus();
			break;

		}

		isSaveMode = isHaveChange(isChange);
		if (isSaveMode) {
			change2saveMode();
		} else {
			change2saveFinishMode();
		}
	}

	@Override
	public ImageButton change2saveFinishMode() {
		// TODO Auto-generated method stub
		if (RetailApplication.getEntityModel().intValue() != Constants.SINGLEMODE) {
			mAddChildShop.setVisibility(View.VISIBLE);
			mAddChildShop.setVisibility(View.VISIBLE);
			mSearchLine.setVisibility(View.VISIBLE);
			mAddChildShopLine.setVisibility(View.VISIBLE);
			mBottomView.setVisibility(View.VISIBLE);
			if (mShopVo.getShopList() == null
					|| mShopVo.getShopList().size() == 0) {
				mSubCountLine.setVisibility(View.VISIBLE);
				mSubStoreListView.setVisibility(View.VISIBLE);
			}
		}
		return super.change2saveFinishMode();
	}

	@Override
	public ImageButton change2saveMode() {
		// TODO Auto-generated method stub
		super.change2saveMode();

		mSearchLine.setVisibility(View.GONE);
		mSubCountLine.setVisibility(View.GONE);
		mAddChildShop.setVisibility(View.GONE);
		mSubStoreListView.setVisibility(View.GONE);
		mAddChildShop.setVisibility(View.GONE);
		mAddChildShopLine.setVisibility(View.GONE);
		mBottomView.setVisibility(View.GONE);

		mRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mShopVo.setSpell("");
				if (checkShopVo() != null) {
					ToastUtil.showLongToast(ShopInfoActivity.this,
							checkShopVo());
					return;
				}
				mShopVo.setAddress(mED_RetailAdress.getCurrVal());
				mShopVo.setShopName(mED_RetailName.getCurrVal());
				mShopVo.setWeixin(mED_RetailWeixin.getCurrVal());
				mShopVo.setPhone1(mED_RetailPhone.getCurrVal());
				mShopVo.setStartTime(mEL_RetailStartTime.getCurrVal());
				mShopVo.setEndTime(mEL_RetailEndTime.getCurrVal());
				mShopVo.setBusinessTime(null);
				mShopVo.setProvinceId(tmpSelectProvId);
				tmpSelectCityId = getCityID(tmpSelectProvId, tmpSelectCityName,
						mAdressList);
				mShopVo.setCityId(tmpSelectCityId);
				tmpSelectDistrictId = getDistrictID(tmpSelectProvId,
						tmpSelectCityId, tmpSelectDistictName, mAdressList);
				mShopVo.setCountyId(tmpSelectDistrictId);
				mShopVo.setCode(mTX_RetailNO.getCurrVal());
				if (CommonUtils.isEmpty(mShopVo.getCopyFlag())) {
					mShopVo.setCopyFlag("0");
				}
				// 如果图片数据有变化，更新logo图片信息
				if (isChange[7]) {
					if (CommonUtils.isEmpty(mShopVo.getFileName())) {
						mShopVo.setFileName("test" + Constants.PNG
								+ Constants.IMGENDWITH580);
					} else {
						mShopVo.setFileName(mShopVo.getFileName()
								+ Constants.IMGENDWITH580);
					}
					if (mLogoImage.getBitmapPortrait() != null) {
						mShopVo.setFile(mLogoImage.getBytes());
						mShopVo.setFileOperate(Short.valueOf("1"));
					} else {
						mShopVo.setFile(null);
						mShopVo.setFileOperate(Short.valueOf("0"));
					}

				} else {
					mShopVo.setFile(null);
					mShopVo.setFileOperate(null);
				}
				saveShopInfo(Constants.EDIT, mShopVo);
			}
		});
		mLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShopInfoActivity.this.finish();
			}
		});
		return null;
	}
	/**
	 * 获取图片
	 */
	public class GetImageThread extends Thread {
		private String path;
		GetImageThread(String path) {
			this.path = path;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Message msg = new Message();
			msg.what = 1;
			byte[] image = null;

			try {
				image = getImage(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg.obj = image;
			mHandler.sendMessage(msg);
		}

		Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
			super.handleMessage(msg);
				Log.i("kyolee", "set bitmap");
				switch (msg.what) {
				case 1:
					myBitmap = getPicFromBytes((byte[]) msg.obj, null);
					mLogoImage.initData(myBitmap);
					break;

				default:
					break;
				}

			}

		};

	}

}
