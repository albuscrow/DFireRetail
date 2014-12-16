package com.dfire.retail.app.manage.activity.goodsmanager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.goodsmanager.MySpinnerLayout.Listener;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.common.LoadingDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.CategoryVo;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.zxing.MipcaActivityCapture;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * The Class GoodsDetailActivity.
 * 
 * @author albuscrow
 */
public class GoodsDetailActivity extends GoodsManagerBaseActivity implements OnClickListener{
	
	/** The mode. */
	private String mode;
	
	/** The goods. */
	private GoodsVo goods;
	
	/** The category. */
	private OneColumnSpinner categorySpinner;
	
	/** The sort spinner view. */
	private MySpinnerLayout sortSpinnerView;
	
	/** The tongbu spinner view. */
	private MySpinnerLayout tongbuSpinnerLayout;
	
	/** The current category. */
	private Category currentCategory;
	
	/** The categorys. */
	private ArrayList<Category> categorys;
	
	/** The current shop. */
	private ShopVo loginShop;
	
	private String asyncShopId;
	
	private ImageButton goodsImageBotton;

	private ImageButton deleteImageButton;

	private AllShopVo currentOperatedShop;

	private EditText barCodeEditView;

	private EditText innerCodeEditView;

	private PopupWindow getGoodsImageMethodChooser;

	private boolean isChanged = false;

	private Bitmap goodsImage;

	private EditText goodsNameEditText;

	private CountWatcher countWatcher;

	private EditText spellCodeEditText;

	private MyCheckBoxLayout creditsLayout;

	private MyCheckBoxLayout preferentialLayout;

	private View imageChangedFlag;
	private boolean originalImageFlag;

	private Button actionButton;

	private ScrollView root;
	
	/**
	 * Inits the ui.
	 */
	private EditText lingshoujia;

	private EditText specific;

	private EditText brand;

	private EditText origin;
	
	
	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail);
		setTitleText(Constants.GOODS_DETAIL);
		
		countWatcher = new CountWatcher(this);
		setWatcher(countWatcher);
		initUI();
	}
	
	
	private void initUI() {
		switchToEditMode();
		
		//init parameter;
		loginShop = RetailApplication.getShopVo();
		mode = getIntent().getStringExtra(Constants.MODE);
		currentOperatedShop = (AllShopVo) getIntent().getSerializableExtra(Constants.SHOP);
//		searchStatus = getIntent().getIntExtra(Constants.SEARCH_STATUS, -1);
		goodsImageBotton = ((ImageButton)findViewById(R.id.imageView));
		

		//get control for global ues;
		root = (ScrollView) findViewById(R.id.root);
		deleteImageButton = ((ImageButton)findViewById(R.id.deleteImage));
		categorySpinner = new OneColumnSpinner(this, (TextView) findViewById(R.id.sort).findViewById(R.id.second));
		categorySpinner.setTitleText(Constants.GOODS_SORT);
		imageChangedFlag = findViewById(R.id.image).findViewById(R.id.saveFlag);
		
		
		if (mode.equals(Constants.ADD)) {
			initUIInAddMode();
		}else{
			initUIInEditMode();
		}
		
		if (tongbuSpinnerLayout != null) {

			tongbuSpinnerLayout.getInputText().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent selectIntent =new Intent(GoodsDetailActivity.this,SelectShopForGoodsManagerActivity.class);
					selectIntent.putExtra(Constants.SHOP_ID, asyncShopId);
					selectIntent.putExtra(Constants.OPT_TYPE, SelectShopForGoodsManagerActivity.ASYNC);
//					selectIntent.putExtra(Constants.ALL_SHOP_ID, currentShop.getShopId());
					startActivityForResult(selectIntent, SELECTSHOPRECODE);
				}
			});
			Drawable arraw = getResources().getDrawable(R.drawable.ico_next);
			arraw.setBounds(0, 0, arraw.getIntrinsicWidth(), arraw.getIntrinsicHeight());
			tongbuSpinnerLayout.getInputText().setCompoundDrawables(null, null, arraw, null);
		}
		
		asyncShopId = currentOperatedShop.getShopId();
		
		findViewById(R.id.image).findViewById(R.id.second).setEnabled(false);
		
		findViewById(R.id.scan).setOnClickListener(this);
		getCategoryList();
		innerCodeEditView.setEnabled(false);
		innerCodeEditView.setFocusable(false);
		innerCodeEditView.setFocusableInTouchMode(false);
		innerCodeEditView.setTextColor(getResources().getColor(R.color.not_necessary));
		spellCodeEditText.setEnabled(false);
		spellCodeEditText.setFocusable(false);
		spellCodeEditText.setFocusableInTouchMode(false);
		spellCodeEditText.setTextColor(getResources().getColor(R.color.not_necessary));
	}




	private void getGoodsDetail() {
		RequestParameter param = new RequestParameter(true);
		param.setUrl(Constants.GOODS_DETAIL_URL);
		param.setParam(Constants.SHOP_ID, currentOperatedShop.getShopId());
		param.setParam(Constants.GOODS_ID, goods.getGoodsId());
		
		new AsyncHttpPost(this, param, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				System.out.println(str);
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsDetailActivity.this)) {
					return;
				}
				GoodsVo newGoods = (GoodsVo) ju.get(Constants.GOODS, GoodsVo.class);
				if (newGoods != null) {
					goods = newGoods;
					initUIInEditMode();
				}
			}
			
			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
				ToastUtil.showShortToast(GoodsDetailActivity.this, Constants.NO_NET);
			}
		}).execute();
	}




	private void initUIInAddMode() {
		findViewById(R.id.scan).setVisibility(View.VISIBLE);
		barCodeEditView = setEditTextContent(R.id.tiaoma,Constants.BAR_CODE,Constants.EMPTY_STRING,
				Constants.NOT_NECESSARY, InputType.TYPE_CLASS_NUMBER, Constants.THIRTEEN_LENGTH);
		innerCodeEditView = setEditTextContent(R.id.daima,Constants.GOODS_CODE,Constants.EMPTY_STRING,
				Constants.UNEDITABLE, InputType.TYPE_CLASS_NUMBER, Constants.THIRTEEN_LENGTH);
		goodsNameEditText = setEditTextContent(R.id.name,Constants.GOODS_NAME,Constants.EMPTY_STRING,Constants.NECESSARY,
				InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE, Constants.FIFTH_LENGTH);
		MyEditTextLayout layout = (MyEditTextLayout) findViewById(R.id.name);
		layout.setNeedTowLine(true);
		goodsNameEditText.setText(Constants.EMPTY_STRING);
		
		
		setEditTextContent(R.id.jinhuojia,Constants.GOODS_JINHUOJIA,Constants.EMPTY_STRING,
				Constants.NOT_NECESSARY, InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER, Constants.NINE_LENGTH);
		
		lingshoujia = setEditTextContent(R.id.lingshoujia,Constants.GOODS_LINGSHOUJIA,Constants.EMPTY_STRING ,
				Constants.NECESSARY, InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER, Constants.NINE_LENGTH);
		
		
		
		
		if (loginShop.isLeaf()) {
			EditText kucun = setEditTextContent(R.id.kucun,Constants.GOODS_KUCUN,Constants.EMPTY_STRING,Constants.UNEDITABLE,
					InputType.TYPE_CLASS_NUMBER);
			kucun.setEnabled(false);
			kucun.setTextColor(getResources().getColor(R.color.not_necessary));
			hide(R.id.tongbu);
		}else{
			String shopName = null;
			if (currentOperatedShop.getShopId().equals(RetailApplication.getShopVo().getShopId())) {
				shopName = Constants.ASYNC_ALL;
			}else{
				shopName = currentOperatedShop.getShopName();
			}
			tongbuSpinnerLayout = setSpinerConetent(R.id.tongbu, Constants.GOODS_TONGBU, shopName, null, null);
			
			hide(R.id.kucun);
		}
		MyEditTextLayout jianma = (MyEditTextLayout) setEditTextContent(R.id.jianma,Constants.GOODS_JIANMA,
				Constants.EMPTY_STRING,Constants.NOT_NECESSARY, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT, Constants.SIX_LENGTH).getParent();
		jianma.setDigitsAndNum(true);
		spellCodeEditText = setEditTextContent(R.id.pingyinma,Constants.GOODS_PINYIN,Constants.EMPTY_STRING,
				Constants.UNEDITABLE,InputType.TYPE_CLASS_TEXT, Constants.FIFTH_LENGTH);
		sortSpinnerView = setSpinerConetent(R.id.sort, Constants.GOODS_CATEGORY, Constants.EMPTY_STRING, categorySpinner, new Listener() {

			@Override
			public String confirm(int pos) {
				currentCategory = categorys.get(pos);
				return currentCategory.name;
			}

			@Override
			public void cancel() {
			}
		});
		specific = setEditTextContent(R.id.guige,Constants.GOODS_GUIGE,Constants.EMPTY_STRING,
				Constants.NOT_NECESSARY,InputType.TYPE_CLASS_TEXT, Constants.FIFTH_LENGTH);
		brand = setEditTextContent(R.id.pinpan,Constants.GOODS_PINGPAI,Constants.EMPTY_STRING,
				Constants.NOT_NECESSARY,InputType.TYPE_CLASS_TEXT, Constants.FIFTH_LENGTH);
		origin = setEditTextContent(R.id.chandi,Constants.GOODS_CHANDI,Constants.EMPTY_STRING,
				Constants.NOT_NECESSARY,InputType.TYPE_CLASS_TEXT, Constants.FIFTH_LENGTH);
		setEditTextContent(R.id.baozhiqi,Constants.GOODS_BAOZHIQI,Constants.EMPTY_STRING,
				Constants.NOT_NECESSARY,InputType.TYPE_CLASS_NUMBER,Constants.SIX_LENGTH);
		setEditTextContent(R.id.image,Constants.GOODS_IMAGE,Constants.EMPTY_STRING, 
				Constants.EMPTY_STRING,InputType.TYPE_CLASS_TEXT).setVisibility(View.GONE);
		setEditTextContent(R.id.ticheng,Constants.GOODS_TICHENG,Constants.ZERO_PERCENT,Constants.NECESSARY,
				InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER, Constants.FIVE_LENTH);
		creditsLayout = setCheckBoxContent(R.id.jifen,  Constants.GOODS_JIFEN ,true);
		preferentialLayout = setCheckBoxContent(R.id.youhui, Constants.GOODS_YOUHUI ,true);
		findViewById(R.id.image).findViewById(R.id.second).setEnabled(false);
		
		goodsImageBotton.setEnabled(true);
		deleteImageButton.setEnabled(true);
		deleteImageButton.setVisibility(View.GONE);
		goodsImageBotton.setOnClickListener(this);
		
		//本店没有， 总店有的情况下添加。
		if (goods == null) {
			goods = (GoodsVo)getIntent().getSerializableExtra(Constants.GOODS);
		}
		if (goods != null) {
//			goods = goods.cloneForAdd();
			barCodeEditView.setText(goods.getBarCode());
			innerCodeEditView.setText(goods.getInnerCode());
			goodsNameEditText.setText(goods.getGoodsName());
			if (goods.getCategoryName() != null && goods.getCategoryName().length() != 0) {
				sortSpinnerView.setValue(goods.getCategoryName(), true);
				if (currentCategory == null) {
					currentCategory = new Category();
				}
				currentCategory.name = goods.getCategoryName();
				currentCategory.id = goods.getCategoryId();
			}
			specific.setText(goods.getSpecification());
			brand.setText(goods.getBrandName());
			origin.setText(goods.getOrigin());
			lingshoujia.requestFocus();
			
			
			byte[] bitmapFile = goods.getFile();
			if (bitmapFile != null && bitmapFile.length != 0) {
				goodsImageBotton.setImageBitmap(BitmapFactory.decodeByteArray(bitmapFile, 0, bitmapFile.length));
				hasImage(true);
			}else{
				ImageLoader.getInstance().displayImage(goods.getFileNameBig(), goodsImageBotton, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						noImage(true);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						if (loadedImage == null) {
							noImage(true);
						}else{
							hasImage(true);
							goodsImage = loadedImage;
						}
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
					}
				});
			}
		}
		//如果在编辑模式，非总店和单店不能修改图片
//		goodsImageBotton.setEnabled(loginShop.canEdit());
//		if (!loginShop.canEdit() && mode.equals(Constants.EDIT)) {
//			deleteImageButton.setEnabled(loginShop.canEdit());
//			deleteImageButton.setVisibility(View.GONE);
//		}
		setTitleText(Constants.TITLE_ADD);
		actionButton = (Button) findViewById(R.id.action);
		actionButton.setBackgroundResource(R.drawable.goods_detail_green_btn);
		actionButton.setText(Constants.CONTINUS_ADD);
		actionButton.setOnClickListener(this);
//		noImage(true);
		switchToEditMode();
	}

	private void initUIInEditMode() {
		goods = (GoodsVo)getIntent().getSerializableExtra(Constants.GOODS);
		barCodeEditView = setEditTextContent(R.id.tiaoma,Constants.BAR_CODE,goods.getBarCode(),Constants.UNEDITABLE,
				InputType.TYPE_CLASS_NUMBER, Constants.THIRTEEN_LENGTH);
		barCodeEditView.setEnabled(false);
		innerCodeEditView = setEditTextContent(R.id.daima,Constants.GOODS_CODE,goods.getInnerCode(),Constants.UNEDITABLE,
				InputType.TYPE_CLASS_NUMBER,Constants.THIRTEEN_LENGTH);
		goodsNameEditText = setEditTextContent(R.id.name,Constants.GOODS_NAME,goods.getGoodsName(),Constants.NECESSARY,
				InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE, Constants.FIFTH_LENGTH);
		final MyEditTextLayout layout = (MyEditTextLayout) findViewById(R.id.name);
		layout.setNeedTowLine(true);
		goodsNameEditText.post(new Runnable() {
			
			@Override
			public void run() {
				goodsNameEditText.setText(goods.getGoodsName());
			}
		});
		
		
		setEditTextContent(R.id.jinhuojia,Constants.GOODS_JINHUOJIA,goods.getPurchasePrice(),
				Constants.NOT_NECESSARY,InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER, Constants.NINE_LENGTH);
		
		lingshoujia = setEditTextContent(R.id.lingshoujia,Constants.GOODS_LINGSHOUJIA,goods.getPetailPrice(),Constants.NECESSARY,
				InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER, Constants.NINE_LENGTH);
		lingshoujia.setEnabled(true);
		lingshoujia.setTextColor(getResources().getColor(R.color.acblue));

		if (loginShop.isLeaf()) {
			EditText kucun =  setEditTextContent(R.id.kucun,Constants.GOODS_KUCUN,goods.getNumber().toString(),
					Constants.UNEDITABLE,InputType.TYPE_CLASS_NUMBER);
			kucun.setEnabled(false);
			kucun.setTextColor(getResources().getColor(R.color.not_necessary));
//				if (userShop.getType() == ShopVo.MENDIAN) {
//					findViewById(R.id.kucun).findViewById(R.id.secend).setEnabled(true);
//				}
			hide(R.id.tongbu);
		}else{
			String shopName = null;
			if (currentOperatedShop.getShopId().equals(RetailApplication.getShopVo().getShopId())) {
				shopName = Constants.ASYNC_ALL;
			}else{
				shopName = currentOperatedShop.getShopName();
			}
			tongbuSpinnerLayout = setSpinerConetent(R.id.tongbu, Constants.GOODS_TONGBU, shopName, null, null);
			hide(R.id.kucun);
		}

		MyEditTextLayout jianma = (MyEditTextLayout) setEditTextContent(R.id.jianma,Constants.GOODS_JIANMA,
				goods.getShortCode(),Constants.NOT_NECESSARY, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT, Constants.SIX_LENGTH).getParent();
		jianma.setDigitsAndNum(true);
		spellCodeEditText = setEditTextContent(R.id.pingyinma,Constants.GOODS_PINYIN,goods.getSpell(),Constants.UNEDITABLE,
				InputType.TYPE_CLASS_TEXT, Constants.FIFTH_LENGTH);

		sortSpinnerView = setSpinerConetent(R.id.sort, Constants.GOODS_CATEGORY, goods.getCategoryName(), categorySpinner, new Listener() {

			@Override
			public String confirm(int pos) {
				if (categorys == null) {
					return Constants.PLEASE_CHOOSE;
				}
				if (pos >= categorys.size() || pos < 0) {
					return Constants.PLEASE_CHOOSE;
				}
				currentCategory = categorys.get(pos);
				return currentCategory.name;
			}

			@Override
			public void cancel() {
			}
		});
		
		//若分类不可编辑，设置相应内容
		if (!loginShop.canEdit()) {
			findViewById(R.id.sort).findViewById(R.id.second).setEnabled(loginShop.canEdit());
			sortSpinnerView.getInputText().setTextColor(getResources().getColor(R.color.light_acgray));
			if (sortSpinnerView.getText().equals(Constants.PLEASE_CHOOSE)) {
				sortSpinnerView.setValue(Constants.UNWRITE, true);
				sortSpinnerView.clearSaveFlag();
			}
		}

		setEditTextContent(R.id.guige,Constants.GOODS_GUIGE,goods.getSpecification(),Constants.NOT_NECESSARY,
				InputType.TYPE_CLASS_TEXT, Constants.FIFTH_LENGTH);
		setEditTextContent(R.id.pinpan,Constants.GOODS_PINGPAI,goods.getBrandName(),Constants.NOT_NECESSARY,
				InputType.TYPE_CLASS_TEXT, Constants.FIFTH_LENGTH);
		setEditTextContent(R.id.chandi,Constants.GOODS_CHANDI,goods.getOrigin(),Constants.NOT_NECESSARY,
				InputType.TYPE_CLASS_TEXT, Constants.FIFTH_LENGTH);
		Integer period = goods.getPeriod();
		String periodStr = null;
		if (period == null) {
			periodStr = Constants.EMPTY_STRING;
		}else{
			periodStr = Constants.EMPTY_STRING + period;
		}
		setEditTextContent(R.id.baozhiqi,Constants.GOODS_BAOZHIQI, periodStr,
				Constants.NOT_NECESSARY, InputType.TYPE_CLASS_NUMBER, Constants.SIX_LENGTH);
		setEditTextContent(R.id.image,Constants.GOODS_IMAGE,Constants.EMPTY_STRING,Constants.EMPTY_STRING,
				InputType.TYPE_CLASS_TEXT).setVisibility(View.GONE);;
		findViewById(R.id.image).findViewById(R.id.second).setEnabled(false);

		setEditTextContent(R.id.ticheng,Constants.GOODS_TICHENG,goods.getPercentage(),Constants.NECESSARY,
				InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER,Constants.FIVE_LENTH);

		creditsLayout = setCheckBoxContent(R.id.jifen, Constants.GOODS_JIFEN ,goods.hasDegree());
		preferentialLayout = setCheckBoxContent(R.id.youhui, Constants.GOODS_YOUHUI ,goods.isSales());

		actionButton = (Button) findViewById(R.id.action);
		actionButton.setBackgroundResource(R.drawable.goods_detail_red_btn);
		actionButton.setText(Constants.DELETE);
		actionButton.setOnClickListener(this);
		byte[] bitmapFile = goods.getFile();
		if (bitmapFile != null && bitmapFile.length != 0) {
			goodsImageBotton.setImageBitmap(BitmapFactory.decodeByteArray(bitmapFile, 0, bitmapFile.length));
			if (loginShop.canEdit()) {
				goodsImageBotton.setOnClickListener(this);
			}
			hasImage(true);
		}else{
			ImageLoader.getInstance().displayImage(goods.getFileNameBig(), goodsImageBotton, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {

				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					noImage(true);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					if (loadedImage == null) {
						noImage(true);
					}else{
						if (loginShop.canEdit()) {
							goodsImageBotton.setOnClickListener(GoodsDetailActivity.this);
						}
						hasImage(true);
						goodsImage = loadedImage;
					}
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
				}
			});
		}
		//如果在编辑模式，非总店和单店不能修改图片
		goodsImageBotton.setEnabled(loginShop.canEdit());
		if (!loginShop.canEdit() && mode.equals(Constants.EDIT)) {
			deleteImageButton.setEnabled(loginShop.canEdit());
			deleteImageButton.setVisibility(View.GONE);;
		}

		switchToBackMode();
	}
    private static final int SELECTSHOPRECODE =2;
    
	@Override
	public void switchToBackMode() {
		if (mode.equals(Constants.EDIT)) {
			super.switchToBackMode();
		}
	}
	
	


	private void hide(int tongbu2) {
		findViewById(tongbu2).setVisibility(View.GONE);
	}

	private void hasImage(boolean isInit) {
//		image.setOnClickListener(null);
		
		//在添加模式，或者编辑模式的总店或者单店，显示删除按钮
		if (loginShop.canEdit() || mode.equals(Constants.ADD)) {
			deleteImageButton.setVisibility(View.VISIBLE);
		}
		deleteImageButton.setOnClickListener(this);
		
		if (isInit) {
			originalImageFlag = true;
		}else{
			if (imageChangedFlag.getVisibility() != View.VISIBLE) {
				countWatcher.add();
				imageChangedFlag.setVisibility(View.VISIBLE);
			}
		}
	}

	private void noImage(boolean isInit) {
		isChanged = true;
		goodsImageBotton.setImageResource(R.drawable.goods_image_add);
		goodsImageBotton.setOnClickListener(this);
		deleteImageButton.setVisibility(View.GONE);
		
		if (isInit) {
			originalImageFlag = false;
		}else{
			if (!originalImageFlag) {
				if (imageChangedFlag.getVisibility() == View.VISIBLE) {
					imageChangedFlag.setVisibility(View.GONE);
					countWatcher.minus();
				}
			}else{
				if (imageChangedFlag.getVisibility() != View.VISIBLE) {
					countWatcher.add();
					imageChangedFlag.setVisibility(View.VISIBLE);
				}
			}
		}
	}

//	/**
//	 * Gets the shop.
//	 * 
//	 * @param next
//	 *            the next
//	 * @return the shop
//	 */
//	private void getShop(boolean next){
//		if (tongbuSpinnerLayout == null) {
//			return;
//		}
//		RequestParameter params = new RequestParameter(true);
//		params.setParam(Constants.SHOP_ID, currentShop.getShopId());
//		if (!next) {
//			currentPage = Constants.PAGE_SIZE_OFFSET;
//		}
//		params.setParam(Constants.PAGE,currentPage ++);
//		params.setUrl(Constants.SHOP_LIST_URL);
//		
//		new AsyncHttpPost(this, params, new RequestResultCallback() {
//
//
//			@Override
//			public void onSuccess(String str) {
//				JsonUtil ju = new JsonUtil(str);
//				if (ju.isError(GoodsDetailActivity.this)) {
//					return;
//				}
//				shops = (List<ShopVo>) ju.get(Constants.All_SHOP, new TypeToken<List<ShopVo>>(){}.getType());
//				shopsString = new ArrayList<String>();
//				shopsString.add(Constants.TONGBU);
//				shopsString.add(Constants.NO_TONGBU);
//				for (ShopVo shop : shops) {
//					String shopName = shop.getShopName();
//					shopsString.add(shopName);
//				}
//				tongbu.setData(shopsString);
//				tongbuShopId = currentShop.getShopId();
//				tongbuSpinnerLayout.setValue(shopsString.get(0), true);
//				tongbuSpinnerLayout.clearSaveFlag();
//				switchToBackMode();
//			}
//			
//			@Override
//			public void onFail(Exception e) {
//				e.printStackTrace();
//			}
//		}).execute();
//	}

	/**
	 * Gets the category list.
	 * 
	 * @return the category list
	 */
	private void getCategoryList() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.CATEGORY_LIST_URL);
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsDetailActivity.this)) {
					return;
				}
//				System.out.println(str);
//				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
//				JsonElement jsonElement = jo.get("returnCode");
//				String returnCode = null;
//				if (jsonElement != null) {
//					returnCode = jsonElement.getAsString();
//				}
//				if (returnCode == null || !returnCode.equals("success")) {
//					ToastUtil.showShortToast(GoodsDetailActivity.this, "获取商品目录失败");
//					return;
//				}
				List<CategoryVo> categoryVo =  (List<CategoryVo>) ju.get(Constants.CATEGORY_LIST, new TypeToken<List<CategoryVo>>(){}.getType());
				categorys = new ArrayList<Category>();
				if (categoryVo != null && categoryVo.size() != 0) {
					getCategory(categoryVo,Constants.EMPTY_STRING,categorys);
				}
				
				List<String> categorysString = new ArrayList<String>();
				for (Category item : categorys) {
					categorysString.add(item.name);
				}
				if (mode.equals(Constants.ADD)) {
					currentCategory = null;
				}else{
					for (Category category : categorys) {
						if (category.id.equals(goods.getCategoryId())) {
							currentCategory = category;
							sortSpinnerView.setValue(currentCategory.name, true);
							sortSpinnerView.clearSaveFlag();
							switchToBackMode();
						}
					}
				}
				categorySpinner.setData(categorysString);
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}).execute();
	}
	
	/**
	 * Gets the category.
	 * 
	 * @param categoryVo
	 *            the category vo
	 * @param parent
	 *            the parent
	 * @param categorys
	 *            the categorys
	 * @return the category
	 */
	private void getCategory(List<CategoryVo> categoryVo,
					String parent, List<Category> categorys) {
		for (CategoryVo item : categoryVo) {
			
			if (item.getCategoryList() == null) {
				Category category = new Category();
				if (parent.length() != 0) {
					category.parents = parent.substring(0, parent.length() - 1);
				}
				category.name = item.getName();
				category.id = item.getId();
				categorys.add(category);
			}else{
				getCategory(item.getCategoryList(), parent + item.getName() + "-", categorys);
			}
		}
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
//		if (mode.equals(Constants.EDIT)) {
//			layout.setEditable(userShop.canEdit());
//		}
		layout.clearSaveFlag();
		return layout;
	}
	
	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#switchToEditMode()
	 */
	@Override
	protected void switchToEditMode() {
		super.switchToEditMode();
		findViewById(R.id.title_right).setOnClickListener(this);
		findViewById(R.id.title_left).setOnClickListener(this);
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
		layout.clearSaveFlag();
		layout.setListener(listener);
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
	private EditText setEditTextContent(int id, String label, String content, final String hint, int inputType, int maxLength) {
		return setEditTextContent(id, label, content, hint, inputType, false, false, maxLength);
	}
	
	private EditText setEditTextContent(int id, String label, String content, final String hint, int inputType) {
		return setEditTextContent(id, label, content, hint, inputType, false, false);
	}
	
	private EditText setEditTextContent(int id, String label, String content, final String hint, int inputType, boolean isPercent, boolean isPrice) {
		return setEditTextContent(id, label, content, hint, inputType, isPercent, isPrice, Constants.INVALID_INT);
	}
	
	
	private EditText setEditTextContent(int id, String label, String content, String hint, int inputType, boolean isPercent, boolean isPrice, int maxLength) {
		
		MyEditTextLayout layout = (MyEditTextLayout) findViewById(id);
		
		if (!loginShop.canEdit() && mode.equals(Constants.EDIT)) {
			hint = Constants.UNWRITE;
		}
		
		layout.init(label, content, hint, inputType, maxLength);
		if (maxLength == Constants.NINE_LENGTH) {
			layout.getInputText().setKeyListener(new DigitsKeyListener(false, true));
		}else if (maxLength == Constants.FIVE_LENTH) {
			layout.getInputText().setKeyListener(new DigitsKeyListener(false, true));
			layout.getInputText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength + 1)});
		}
		if (hint.equals(Constants.UNEDITABLE) && (content == null || content.equals(Constants.EMPTY_STRING))) {
			layout.setVisibility(View.GONE);
			return layout.getInputText();
		}else{
			layout.setVisibility(View.VISIBLE);
		}
		
		if (mode.equals(Constants.EDIT)) {
			layout.setEditable(loginShop.canEdit());
			if (!loginShop.canEdit()) {
				layout.getInputText().setTextColor(getResources().getColor(R.color.light_acgray));
			}
		}
		
		layout.setPercent(isPercent);
		layout.setPrice(isPrice);

		layout.clearSaveFlag();
		return layout.getInputText();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right:
			if (mode.equals(Constants.ADD)) {
				checkBeforeSave(false);
			}else{
				checkBeforeSave(false);
			}
			break;
			
		case R.id.title_left:
			finish();
			break;
			
		case R.id.imageView:
			chooseImage();
			break;
		case R.id.deleteImage:
			noImage(false);
	
			break;
			
		case R.id.action:
			if (mode.equals(Constants.ADD) ) {
				continusAdd();
			}else{
				isDeleteGoods();
			}
			break;
			
		case R.id.scan:
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), 10086);
			break;
			
		case R.id.from_ablum:
			getGoodsImageMethodChooser.dismiss();
			Intent photoPickerIntent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			photoPickerIntent.setType(Constants.IMAGE_INTENT);
			photoPickerIntent.putExtra(Constants.OUTPUT_FORMAT, Bitmap.CompressFormat.JPEG.toString());
			photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
			startActivityForResult(photoPickerIntent, IMAGE_REQUEST_CODE);
			break;
			
		case R.id.from_camera:
			getGoodsImageMethodChooser.dismiss();
			Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intentFromCapture.putExtra(Constants.OUTPUT_FORMAT,Bitmap.CompressFormat.JPEG.toString());
			intentFromCapture.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
			File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);  
			File tempFile = new File(path,Constants.TEMP_PHOTO_FILE);
			// 判断存储卡是否可以用，可用进行存储
			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
			startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
			break;
			
		case R.id.cancel:
			getGoodsImageMethodChooser.dismiss();
			
		default:
			break;
			
		}
	}
	
	private void isDeleteGoods() {
		
		final AlertDialog ad = new AlertDialog(this);
		String goodsName = goods.getGoodsName();
		if (goodsName == null) {
			goodsName = "";
		}
		ad.setMessage("确定删除[" + goodsName + "]吗?");
		ad.setPositiveButton(Constants.CONFIRM, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteGoods();
				ad.dismiss();
			}
		});
		ad.setNegativeButton(Constants.CANCEL, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});

	}
		
	private void continusAdd() {
//		findViewById(R.id.scan).setVisibility(View.VISIBLE);
		checkBeforeSave(true);
	}

	private void deleteGoods() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.DELETE_URL);
		params.setParam(Constants.SHOP_ID, currentOperatedShop.getShopId());
		params.setParam(Constants.GOODS_ID, goods.getGoodsId());
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsDetailActivity.this)) {
					return;
				}
//				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
//				JsonElement jsonElement = jo.get("returnCode");
//				String returnCode = null;
//				if (jsonElement != null) {
//					returnCode = jsonElement.getAsString();
//				}
//				if (returnCode == null || !returnCode.equals("success")) {
//					ToastUtil.showShortToast(GoodsDetailActivity.this, "删除失败");
//					return;
//				}
//				ToastUtil.showShortToast(GoodsDetailActivity.this, Constants.DELETE_SUCCESS);
				setResult(RESULT_OK, new Intent()
				.putExtra(Constants.RESULT, GoodsListWithImageActivity.DELETE));
				finish();
			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(GoodsDetailActivity.this, Constants.DELETE_FAIL);
				e.printStackTrace();
				return;
			}
		}).execute();
	}


	private static final int IMAGE_REQUEST_CODE = 20;
	private static final int CAMERA_REQUEST_CODE = 30;
	private static final int RESULT_REQUEST_CODE = 10;
	
	private Uri getTempUri() {
		return Uri.fromFile(getTempFile());
	}
	
	
	private File getTempFile() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory(), Constants.TEMP_PHOTO_FILE);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return file;
		} else {
			return null;
		}
	}

	private void chooseImage() {
		
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		View view = LayoutInflater.from(this).inflate(R.layout.get_image, null);
		view.findViewById(R.id.from_ablum).setOnClickListener(this);
		view.findViewById(R.id.from_camera).setOnClickListener(this);
		view.findViewById(R.id.cancel).setOnClickListener(this);
		if (getGoodsImageMethodChooser == null) {
			getGoodsImageMethodChooser = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		}
		getGoodsImageMethodChooser.setAnimationStyle(R.style.anim_menu_bottombar);
		WindowManager.LayoutParams lp = getWindow().getAttributes();  
		lp.alpha = 0.5f;
		getWindow().setAttributes(lp);  
		getGoodsImageMethodChooser.setFocusable(true);
		getGoodsImageMethodChooser.setTouchable(true);
		getGoodsImageMethodChooser.setOutsideTouchable(true);
		ColorDrawable dw = new ColorDrawable(0x00);
		getGoodsImageMethodChooser.setBackgroundDrawable(dw);
		getGoodsImageMethodChooser.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = GoodsDetailActivity.this.getWindow().getAttributes();  
				lp.alpha = 1f;
				getWindow().setAttributes(lp);  
			}
		});
		getGoodsImageMethodChooser.showAtLocation(root, Gravity.BOTTOM, 0, 0);

	}
	
	public void handleImage(Uri uri) {
		goodsImage = decodeUriAsBitmap(uri);// decode bitmap
		goodsImageBotton.setImageBitmap(goodsImage);
		
		//调整imageview大小
		int w = goodsImage.getWidth();
		int h = goodsImage.getHeight();
		float ratio = (float) h / w;
		
		LayoutParams lp = goodsImageBotton.getLayoutParams();
//		lp.height = (int) (photo.getWidth() * ratio);
//		image.setLayoutParams(lp);
//		image.requestLayout();
		
		isChanged = true;
		switchToEditMode();
		hasImage(false);
	}


	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void zoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 116);
		intent.putExtra("aspectY", 77);

		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 580);
		intent.putExtra("outputY", 385);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
//				file = new File(data.getData().getPath());
//				Bitmap bitmap = decodeUriAsBitmap(data.getData());
//				final File tempFile = new File(getCacheDir(), Constants.TEMPFILE + System.currentTimeMillis() + Constants.PNG);
//				try {
//					bitmap.compress(CompressFormat.PNG, 80, new FileOutputStream(tempFile));
//				} catch (FileNotFoundException e1) {
//					e1.printStackTrace();
//				}
//				image.setImageBitmap(bitmap);
//				
				handleImage(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);  
				File tempFile2 = new File(path,Constants.TEMP_PHOTO_FILE);
				handleImage(Uri.fromFile(tempFile2));
//				file = tempFile2;
//				Bitmap bitmap2 = decodeUriAsBitmap(Uri.fromFile(tempFile2));
//
//				image.setImageBitmap(bitmap2);
				
				break;
				
			case RESULT_REQUEST_CODE: // 图片缩放完成后
				if (data != null) {
					getImageToView(data);
				}
				hasImage(false);
				break;
				
				
			case SELECTSHOPRECODE:
				currentOperatedShop = (AllShopVo) data.getSerializableExtra(Constants.SHOP);
				asyncShopId = currentOperatedShop.getShopId();
				tongbuSpinnerLayout.getInputText().setText(currentOperatedShop.getShopName());
				
				break;
			case 10086:
				search(data.getStringExtra(Constants.DEVICE_CODE));
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
 
	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			goodsImage = decodeUriAsBitmap(imageUri);// decode bitmap
			goodsImageBotton.setImageBitmap(goodsImage);
			isChanged = true;
		}
		switchToEditMode();
	}
	
	/**
	 * Search.
	 */
	private void search(final String code) {
		if (code == null || code.length() == 0) {
			return;
		}
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SHOP_ID, currentOperatedShop.getShopId());
		params.setParam(Constants.SEARCH_CODE, code);
		params.setParam(Constants.PAGE, Constants.PAGE_SIZE_OFFSET);
		params.setUrl(Constants.GOODS_LIST_URL);
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsDetailActivity.this)) {
					return;
				}
				
				ArrayList<GoodsVo> goodsList = (ArrayList<GoodsVo>) ju.get(Constants.GOODS_LIST, new TypeToken<List<GoodsVo>>(){}.getType());
				if (goodsList == null || goodsList.size() == 0) {
					goods = new GoodsVo();
					goods.setBarCode(code);
				}else{
					goods = goodsList.get(0);
				}
				initUI();
			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(GoodsDetailActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}).execute();
	}
	
	Uri imageUri = getTempUri(); // The Uri to store the big bitmap

	File file = null;

	private String oldFileName;
	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri), null, options);
			
			int width = options.outWidth;
			int height = options.outHeight;
			int samplerSize = width / 800;
			
			options.inJustDecodeBounds = false;
			options.inSampleSize = samplerSize;
			
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri), null, options);
			int dstWidth = 580;
			int dstHeight = (int) (580 * (float) height / width);
			bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, false);
			bitmap = rotaingImageView(readPictureDegree(uri.getPath()), bitmap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}
	
	

	private void save(final boolean isContinus, final LoadingDialog pd) {
		if (goods != null) {
			oldFileName = goods.getFileName();
		}
		String isAlreadyHave = getIntent().getStringExtra(Constants.IS_ALREADY_HAVE);
		String name = getValueStringFromTextView(R.id.name, true, Constants.INPUT_GOODS_NAME);
		if (name == null) {
			pd.dismiss();
			return;
		}
		if (isAlreadyHave != null && isAlreadyHave.equals(Constants.YES)&& name.equals(goods.getGoodsName())) {
			ToastUtil.showShortToast(GoodsDetailActivity.this, Constants.GOODS_ALREADY_HAVE, getView(R.id.name));
			pd.dismiss();
			return;
		}
		if (!pd.isShowing()) {
			pd.show();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				//				getMainLooper().prepare();
				final GoodsVo goods = genGoods();
				String goodsStr = new GsonBuilder().serializeNulls().create().toJson(goods);
				if (goods == null) {
					pd.dismiss();
					return;
				}
				RequestParameter params = new RequestParameter(true);
				params.setParam(Constants.SHOP_ID, currentOperatedShop.getShopId());
				try {
					params.setParam(Constants.GOODS, new JSONObject(goodsStr.toString()));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				params.setParam(Constants.OPT_TYPE, mode);
				params.setUrl(Constants.GOODS_SAVE_URL);

				new AsyncHttpPost(GoodsDetailActivity.this, params, new RequestResultCallback() {

					@Override
					public void onSuccess(String str) {
						pd.dismiss();
						JsonUtil ju = new JsonUtil(str);
						if (ju.isError(GoodsDetailActivity.this)) {
							return;
						}
//						ToastUtil.showShortToast(GoodsDetailActivity.this, Constants.GOODS_SAVE_SUCCESS);
						if (mode.equals(Constants.ADD)) {
							if (isContinus) {
								GoodsDetailActivity.this.goods = new GoodsVo();
								initUI();
								InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
								findViewById(R.id.scan).requestFocus();
								root.scrollTo(0, 0);
								startActivityForResult(new Intent(GoodsDetailActivity.this, MipcaActivityCapture.class), 10086);
							}else{
								goods.setFileName(oldFileName);
								goods.addLastVer();
								GoodsDetailActivity.this.setResult(RESULT_OK, new Intent()
								.putExtra(Constants.RESULT, GoodsListWithImageActivity.ADD)
								.putExtra(Constants.GOODS, goods));
								finish();
							}
						}else{
							goods.setFileName(oldFileName);
							goods.addLastVer();
							GoodsDetailActivity.this.setResult(RESULT_OK, new Intent()
							.putExtra(Constants.RESULT, GoodsListWithImageActivity.EDIT)
							.putExtra(Constants.GOODS, goods));
							finish();
						}
						if (GoodsDetailActivity.this.goods != null) {
							GoodsDetailActivity.this.goods.setFileName(oldFileName);
						}
					}

					@Override
					public void onFail(Exception e) {
						pd.dismiss();
						GoodsDetailActivity.this.goods.setFileName(oldFileName);
						ToastUtil.showShortToast(GoodsDetailActivity.this, Constants.NO_NET);
						e.printStackTrace();
					}
				}, false).execute();
			}
		}).start();
	}


	/**
	 * check before save goods.
	 */
	private void checkBeforeSave(final boolean isContinus) {
		if (goods != null) {
			oldFileName = goods.getFileName();
		}
		String isAlreadyHave = getIntent().getStringExtra(Constants.IS_ALREADY_HAVE);
		String name = getValueStringFromTextView(R.id.name, true, Constants.INPUT_GOODS_NAME);
		if (name == null) {
			return;
		}
		if (isAlreadyHave != null && isAlreadyHave.equals(Constants.YES)&& name.equals(goods.getGoodsName())) {
			ToastUtil.showShortToast(GoodsDetailActivity.this, Constants.GOODS_ALREADY_HAVE, getView(R.id.name));
			return;
		}
		
		final GoodsVo goods = genGoods();
		if (goods == null) {
			return;
		}
		
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SHOP_ID, currentOperatedShop.getShopId());
		params.setParam(Constants.GOODS_ID, goods.getGoodsId());
		String barCode = goods.getBarCode();
		if (barCode == null) {
			barCode = Constants.EMPTY_STRING;
		}
		params.setParam(Constants.BAR_CODE_FOR_REQUEST, barCode);
		params.setParam(Constants.GOODS_NAME_FOR_REQUEST, goods.getGoodsName());
		params.setParam(Constants.OPT_TYPE, mode);
		params.setUrl(Constants.GOODS_CHECK_URL);
		
		final LoadingDialog pd = new LoadingDialog(this, false);//请求网络 转转转
		pd.show();
		
		new AsyncHttpPost(GoodsDetailActivity.this, params, new RequestResultCallback() {

			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsDetailActivity.this)) {
					pd.dismiss();
					return;
				}
				int result = ju.getInt(Constants.RESULT);
				switch (result) {
				case 0:
					save(isContinus, pd);
					break;
				case 1:
					pd.dismiss();
					new ErrDialog(GoodsDetailActivity.this, Constants.GOODS_NAME_EXISTS).show();
					break;
				case 2:
					pd.dismiss();
					final AlertDialog ad = new AlertDialog(GoodsDetailActivity.this);
					ad.setMessage(Constants.ADD_YIMADUOPING);
					ad.setPositiveButton(Constants.CONFIRM, new OnClickListener() {

						@Override
						public void onClick(View v) {
							save(isContinus, pd);
							ad.dismiss();
						}
					});
					ad.setNegativeButton(Constants.CANCEL, new OnClickListener() {

						@Override
						public void onClick(View v) {
							ad.dismiss();
						}
					});
					break;

				default:
					break;
				}
				
			}

			@Override
			public void onFail(Exception e) {
				pd.dismiss();
				if (GoodsDetailActivity.this.goods == null) {
					GoodsDetailActivity.this.goods = new GoodsVo();
				}
				GoodsDetailActivity.this.goods.setFileName(oldFileName);
				ToastUtil.showShortToast(GoodsDetailActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}, false).execute();
		
	}
	
	static final int GOODS_NOT_EXISTS = 0;
	static final int GOODS_NAME_DUPLICATE = 1;
	static final int GOODS_BAR_CODE_DUPLICATE = 2;
	
	private GoodsVo genGoods() {
		return genGoods(true);
	}

	/**
	 * Gen goods.
	 * 
	 * @return the string
	 */
	private GoodsVo genGoods(boolean needImage) {
		GoodsVo goods = null;
		if (this.goods == null) {
			goods = new GoodsVo();
		}else{
			try {
				goods = (GoodsVo) this.goods.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		
		String name = getValueStringFromTextView(R.id.name, true, Constants.INPUT_GOODS_NAME);
		String lingshou = getValueStringFromTextView(R.id.lingshoujia, true, Constants.INPUT_GOODS_LINGSHOUJIA);
		
		if (name == null || lingshou == null ) {
			return null;
		}else{
			goods.setGoodsName(name);
			try {
				float temp = Float.parseFloat(lingshou);
				if (!checkFloat(lingshou,temp,Constants.GOODS_LINGSHOUJIA_ERROR, 999999.99f, getView(R.id.lingshoujia))) {
					return null;
				}
				goods.setPetailPrice(lingshou);
			} catch (Exception e) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_LINGSHOUJI, getView(R.id.lingshoujia));
				e.printStackTrace();
				return null;
			}
		}
		
		String tiaoma = getValueStringFromTextView(R.id.tiaoma, false, null);
		if (tiaoma != null) {
			try {
				float temp = Float.parseFloat(tiaoma);
				if (temp < 0) {
					ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_TIAOMA, getView(R.id.tiaoma));
					return null;
				}
			} catch (Exception e) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_TIAOMA, getView(R.id.tiaoma));
				e.printStackTrace();
				return null;
			}
		}
		goods.setBarCode(tiaoma);
		
//		String daima = getValueStringFromTextView(R.id.daima, false, null);
//		try {
//			float temp = Float.parseFloat(daima);
//			if (temp < 0 || daima.length() != 13) {
//				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_DAIMA);
//				return null;
//			}
//		} catch (Exception e) {
//			ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_DAIMA);
//			e.printStackTrace();
//			return null;
//		}
//		goods.setInnerCode(daima);
		
		String jinhuo = getValueStringFromTextView(R.id.jinhuojia, false, null);
		if (jinhuo != null && jinhuo.length() != 0) {
			try {
				float temp = Float.valueOf(jinhuo);
				if (!checkFloat(jinhuo,temp,Constants.GOODS_JINHUOJIA_ERROR, 999999.99f, getView(R.id.jinhuojia))) {
					return null;
				}
				goods.setPurchasePrice(jinhuo);
			} catch (Exception e) {
				e.printStackTrace();
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_JINHUO, getView(R.id.jinhuojia));
				return null;
			}
		}else{
			goods.setPurchasePrice(null);
		}
		
		if (loginShop.isLeaf()) {
			String kucun = getValueStringFromTextView(R.id.kucun, false, null);
			Integer kucunInt = null;
			if (kucun != null) {
//				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_KUCUN);
				try {
					kucunInt = Integer.parseInt(kucun);
					//库存可以为负数，所以注释掉了；
//					if (kucunInt < 0) {
//						ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_KUCUN);
//						return null;
//					}
				} catch (Exception e) {
					ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_KUCUN, getView(R.id.kucun));
					e.printStackTrace();
					return null;
				}
			}
			goods.setNumber(kucunInt);
		}else{
			goods.setSynShopId(asyncShopId);
		}
		
		String jianma = getValueStringFromTextView(R.id.jianma, false, null);
//		if (jianma == null || jianma.length() == 0) {
//			ToastUtil.showShortToast(this, Constants.INPUT_JIANMA);
//			return null;
//		}
		goods.setShortCode(jianma);
		
//		String pinyin = getValueStringFromTextView(R.id.pingyinma, false, null);
//		goods.setSpell(pinyin);
		
		if (currentCategory != null && currentCategory.id != null 
				&& currentCategory.id.length() != 0 && !sortSpinnerView.getText().equals(Constants.PLEASE_CHOOSE)) {
			goods.setCategoryId(currentCategory.id);
			goods.setCategoryName(currentCategory.name);
		}else{
			String categoryName = sortSpinnerView.getText();
			if (!categoryName.equals(Constants.PLEASE_CHOOSE)) {
				goods.setCategoryId(getCateGoryId(categoryName));
				goods.setCategoryName(categoryName);
			}else{
				goods.setCategoryId(getCateGoryId(null));
				goods.setCategoryName(null);
			}
		}
		
		String guige = getValueStringFromTextView(R.id.guige, false, null);
		goods.setSpecification(guige);
		
		String pinpai = getValueStringFromTextView(R.id.pinpan, false, null);
		goods.setBrandName(pinpai);
		
		String chandi = getValueStringFromTextView(R.id.chandi, false, null);
		goods.setOrigin(chandi);
		
		String baozhiqiStr = getValueStringFromTextView(R.id.baozhiqi, false, null);
		Integer baozhiqi = null;
		if (baozhiqiStr != null) {
			try {
				baozhiqi = Integer.valueOf(baozhiqiStr);
				if (baozhiqi < 0) {
					ToastUtil.showShortToast(this, Constants.GOODS_PERIOD, getView(R.id.baozhiqi));
					return null;
				}
			} catch (Exception e) {
				ToastUtil.showShortToast(this, Constants.GOODS_PERIOD, getView(R.id.baozhiqi));
				return null;
			}
		}
		goods.setPeriod(baozhiqi);

		if (mode.equals(Constants.ADD)) {
			if (deleteImageButton.getVisibility() == View.VISIBLE) {
				goods.setFile(bitmap2Bytes(goodsImage));
				goods.setFileName(goods.getGoodsName() + Constants.PNG);
			}else{
				goods.setFileDeleteFlg(true);
				goods.setFileName(null);
				goods.setFile(null);
				oldFileName = null;
			}
		}else{
			if (!loginShop.canEdit()) {
				goods.setFileDeleteFlg(false);
				goods.setFile(null);
			}else{
				if (isChanged) {
					if (deleteImageButton.getVisibility() == View.VISIBLE) {
						goods.setFile(bitmap2Bytes(goodsImage));
						goods.setFileName(goods.getGoodsName() + Constants.PNG);
					}else{
						goods.setFileDeleteFlg(true);
						goods.setFileName(null);
						goods.setFile(null);
						oldFileName = null;
					}
				}else{
					goods.setFileName(null);
					goods.setFileDeleteFlg(false);
				}
			}
		}
//		if (deleteImage.getVisibility() == View.VISIBLE && isChanged && needImage) {
////			goods.setBp(photo);
//			goods.setFile(bitmap2Bytes(photo));
//			goods.setFileName(goods.getGoodsName() + Constants.PNG);
//		}else{
//			goods.setFileDeleteFlg(fileDeleteFlg);
//		}
		
	
		String bili = getValueStringFromTextView(R.id.ticheng, false, null);
		if (bili != null) {
			try {
				Float valueOf = Float.parseFloat(bili);
				if (!checkFloat(bili,valueOf,Constants.GOODS_TICHENG_FOR_ERROR, 100, getView(R.id.ticheng))) {
					return null;
				}
				goods.setPercentage(String.valueOf((valueOf)));
			} catch (Exception e) {
				e.printStackTrace();
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_TICHENG, getView(R.id.ticheng));
				return null;
			}
		}else{
			ToastUtil.showShortToast(this, Constants.INPUT_TICHENG, getView(R.id.ticheng));
			return null;
		}
		
		goods.setHasDegree(creditsLayout.isChecked());
		
		goods.setIsSales(preferentialLayout.isChecked());
		
//		String baseId = goods.getBaseId();
//		if (baseId != null && baseId.length() != 32) {
//			for (int i = baseId.length(); i < 32; ++i) {
//				baseId = Constants.ZERO + baseId;
//			}
//			goods.setBaseId(baseId);
//		}
//
//		String brandId = goods.getBrandId();
//		if (brandId != null && brandId.length() != 32) {
//			for (int i = brandId.length(); i < 32; ++i) {
//				brandId = Constants.ZERO + brandId;
//			}
//			goods.setBaseId(brandId);
//		}
//
//		String unitId = goods.getUnitId();
//		if (unitId != null && unitId.length() != 32) {
//			for (int i = unitId.length(); i < 32; ++i) {
//				unitId = Constants.ZERO + unitId;
//			}
//			goods.setBaseId(unitId);
//		}
//		goods.setUnitId(null);
//		goods.setMemo(null);
//		goods.setBrandId(null);
		if (goods.getType() == null) {
			goods.setType(Constants.ONE_INT);
		}
		
//		goods.setInnerCode("1234567890123");
//		goods.setSpell("jxj");
//		return new GsonBuilder().serializeNulls().create().toJson(goods);
		return goods;
	}


	private String getCateGoryId(String categoryName) {
		if (categorys == null) {
			return null;
		}
		if (categoryName == null) {
			return null;
		}
		for (Category c : categorys) {
			if (categoryName.equals(c.name)) {
				return c.id;
			}
		}
		return null;
	}


	private View getView(int id) {
		return findViewById(id);
	}




	private byte[] bitmap2Bytes(Bitmap input) {
		if (input == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		input.compress(CompressFormat.JPEG, 80, baos);
		byte[] byteArray = baos.toByteArray();
		System.out.println(byteArray.length);
		return byteArray;
	}

	/**
	 * Gets the value string from text view.
	 * 
	 * @param tiaoma
	 *            the tiaoma
	 * @param b
	 *            the b
	 * @param string
	 *            the string
	 * @return the value string from text view
	 */
	private String getValueStringFromTextView(int id, boolean b, String string) {
		View findViewById = findViewById(id);
		TextView textView = (TextView)findViewById.findViewById(R.id.second);
		String content = textView.getText().toString();
		if (content.length() == 0) {
			if (b) {
				ToastUtil.showShortToast(this, string, textView);
			}
			return null;
		}
		return content;
	}
	
	
	/**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
    	int degree  = 0;
    	try {
    		ExifInterface exifInterface = new ExifInterface(path);
    		int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
    		switch (orientation) {
    		case ExifInterface.ORIENTATION_ROTATE_90:
    			degree = 90;
    			break;
    		case ExifInterface.ORIENTATION_ROTATE_180:
    			degree = 180;
    			break;
    		case ExifInterface.ORIENTATION_ROTATE_270:
    			degree = 270;
    			break;
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return degree;
    }
   /*
    * 旋转图片
    * @param angle
    * @param bitmap
    * @return Bitmap
    */ 
   public static Bitmap rotaingImageView(int angle , Bitmap bitmap) { 
       //旋转图片 动作  
       Matrix matrix = new Matrix();
       matrix.postRotate(angle); 
       System.out.println("angle2=" + angle); 
       // 创建新的图片  
       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, 
               bitmap.getWidth(), bitmap.getHeight(), matrix, true); 
       return resizedBitmap; 
   }
   
}

 