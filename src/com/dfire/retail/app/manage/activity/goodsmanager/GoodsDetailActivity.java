package com.dfire.retail.app.manage.activity.goodsmanager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.CaptureActivity;
import com.dfire.retail.app.manage.data.CategoryVo;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

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
	
	/** The edited. */
	private boolean edited = false;
	
	/** The tongbu. */
	private SpinerPopWindow tongbu;
	
	/** The fenlei. */
	private SpinerPopWindow fenlei;
	
	/** The sort spinner view. */
	private TextView sortSpinnerView;
	
	/** The tongbu spinner view. */
	private TextView tongbuSpinnerView;
	
	/** The current category. */
	private Category currentCategory;
	
	/** The categorys. */
	private ArrayList<Category> categorys;
	
	
	/** The shops. */
	private List<ShopVo> shops;
	
	/** The current page. */
	private int currentPage = 1;
	
	/** The current shop. */
	private ShopVo userShop;
	
	private String tongbuShopId;
	
	/** The current shop position. */
	private int currentShopPosition;

	private ImageButton image;

	private ImageButton deleteImage;

	private ShopVo shop;

	private EditText tiaomaEditView;

	private EditText daimaEditView;

	private EditText jinhuoEditView;
	
	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail);
		setTitleText("商品详情");
		initUI();
		setBack();
	}

	/**
	 * Inits the ui.
	 */
	private void initUI() {
		switchToEditMode();
		userShop = RetailApplication.getShopVo();
		mode = getIntent().getStringExtra(Constants.MODE);
		shop = (ShopVo) getIntent().getSerializableExtra(Constants.SHOP);
		image = ((ImageButton)findViewById(R.id.imageView));
		deleteImage = ((ImageButton)findViewById(R.id.deleteImage));
		if (mode.equals(Constants.ADD)) {
			edited = true;
			tiaomaEditView = setEditTextContent(R.id.tiaoma,Constants.BAR_CODE,Constants.EMPTY_STRING,Constants.NOT_NECESSARY, InputType.TYPE_CLASS_NUMBER);
			daimaEditView = setEditTextContent(R.id.daima,Constants.GOODS_CODE,Constants.EMPTY_STRING,Constants.NOT_NECESSARY, InputType.TYPE_CLASS_NUMBER);
			setEditTextContent(R.id.name,Constants.GOODS_NAME,Constants.EMPTY_STRING,Constants.NECESSARY, InputType.TYPE_CLASS_TEXT);
			
			jinhuoEditView = setEditTextContent(R.id.jinhuojia,Constants.GOODS_JINHUOJIA,Constants.EMPTY_STRING,Constants.NOT_NECESSARY, InputType.TYPE_CLASS_NUMBER);
			
			setEditTextContent(R.id.lingshoujia,Constants.GOODS_LINGSHOUJIA,Constants.EMPTY_STRING,Constants.NECESSARY, InputType.TYPE_CLASS_NUMBER);
			
			if (userShop.isLeaf()) {
				setEditTextContent(R.id.kucun,Constants.GOODS_KUCUN,Constants.EMPTY_STRING,Constants.NOT_NECESSARY, InputType.TYPE_CLASS_NUMBER);
				hide(R.id.tongbu);
			}else{
				setSpinerConetent(R.id.tongbu, Constants.GOODS_TONGBU, Constants.EMPTY_STRING);
				hide(R.id.kucun);
			}
			setEditTextContent(R.id.jianma,Constants.GOODS_JIANMA,Constants.EMPTY_STRING,Constants.NOT_NECESSARY,InputType.TYPE_CLASS_TEXT);
			setEditTextContent(R.id.pingyinma,Constants.GOODS_PINYIN,Constants.EMPTY_STRING,Constants.NOT_NECESSARY,InputType.TYPE_CLASS_TEXT);
			setSpinerConetent(R.id.sort, Constants.GOODS_CATEGORY, Constants.EMPTY_STRING);
			setEditTextContent(R.id.guige,Constants.GOODS_GUIGE,Constants.EMPTY_STRING,Constants.NOT_NECESSARY,InputType.TYPE_CLASS_TEXT);
			setEditTextContent(R.id.pinpan,Constants.GOODS_PINGPAI,Constants.EMPTY_STRING,Constants.NOT_NECESSARY,InputType.TYPE_CLASS_TEXT);
			setEditTextContent(R.id.chandi,Constants.GOODS_CHANDI,Constants.EMPTY_STRING,Constants.NOT_NECESSARY,InputType.TYPE_CLASS_TEXT);
			setEditTextContent(R.id.baozhiqi,Constants.GOODS_BAOZHIQI,Constants.EMPTY_STRING,Constants.NOT_NECESSARY,InputType.TYPE_CLASS_NUMBER);
			setEditTextContent(R.id.image,Constants.GOODS_IMAGE,Constants.EMPTY_STRING, Constants.EMPTY_STRING,InputType.TYPE_CLASS_TEXT);
			setEditTextContent(R.id.ticheng,Constants.GOODS_TICHENG,Constants.EMPTY_STRING,Constants.NOT_NECESSARY,InputType.TYPE_CLASS_NUMBER);
			setCheckBoxContent(R.id.jifen,  Constants.GOODS_JIFEN ,false);
			setCheckBoxContent(R.id.youhui, Constants.GOODS_YOUHUI ,false);
			findViewById(R.id.image).findViewById(R.id.secend).setEnabled(false);
			switchToEditMode();
			Button button = (Button) findViewById(R.id.delete);
			button.setBackgroundResource(R.drawable.goods_detail_green_btn);
			button.setText(Constants.CONTINUS_ADD);
			button.setOnClickListener(this);
			noImage();
		}else{
			goods = (GoodsVo)getIntent().getSerializableExtra(Constants.GOODS);
			tiaomaEditView = setEditTextContent(R.id.tiaoma,Constants.BAR_CODE,goods.getBarCode(),Constants.NOT_NECESSARY,InputType.TYPE_CLASS_NUMBER);
			daimaEditView = setEditTextContent(R.id.daima,Constants.GOODS_CODE,goods.getInnerCode(),Constants.NOT_NECESSARY,InputType.TYPE_CLASS_NUMBER);
			setEditTextContent(R.id.name,Constants.GOODS_NAME,goods.getGoodsName(),Constants.NECESSARY, InputType.TYPE_CLASS_TEXT);
			setEditTextContent(R.id.jinhuojia,Constants.GOODS_JINHUOJIA,goods.getPurchasePrice(),Constants.NOT_NECESSARY,InputType.TYPE_CLASS_NUMBER);
			
			setEditTextContent(R.id.lingshoujia,Constants.GOODS_LINGSHOUJIA,goods.getPetailPrice(),Constants.NECESSARY, InputType.TYPE_CLASS_NUMBER);
			findViewById(R.id.lingshoujia).findViewById(R.id.secend).setEnabled(true);
			
			if (userShop.isLeaf()) {
				setEditTextContent(R.id.kucun,Constants.GOODS_KUCUN,Constants.EMPTY_STRING,Constants.NOT_NECESSARY,InputType.TYPE_CLASS_NUMBER);
				if (userShop.getType() == ShopVo.MENDIAN) {
					findViewById(R.id.kucun).findViewById(R.id.secend).setEnabled(true);
				}
				hide(R.id.tongbu);
			}else{
				setSpinerConetent(R.id.tongbu, Constants.GOODS_TONGBU, Constants.EMPTY_STRING);
				hide(R.id.kucun);
			}

			setEditTextContent(R.id.jianma,Constants.GOODS_JIANMA,goods.getShortCode(),Constants.NOT_NECESSARY, InputType.TYPE_CLASS_TEXT);
			setEditTextContent(R.id.pingyinma,Constants.GOODS_PINYIN,goods.getSpell(),Constants.NOT_NECESSARY, InputType.TYPE_CLASS_TEXT);

			setSpinerConetent(R.id.sort, Constants.GOODS_CATEGORY, goods.getCategoryName());
			if (mode.equals(Constants.EDIT)) {
				findViewById(R.id.sort).findViewById(R.id.secend).setEnabled(userShop.canEdit());
			}

			setEditTextContent(R.id.guige,Constants.GOODS_GUIGE,goods.getSpecification(),Constants.NOT_NECESSARY, InputType.TYPE_CLASS_TEXT);
			setEditTextContent(R.id.pinpan,Constants.GOODS_PINGPAI,goods.getBrandName(),Constants.NOT_NECESSARY, InputType.TYPE_CLASS_TEXT);
			setEditTextContent(R.id.chandi,Constants.GOODS_CHANDI,goods.getOrigin(),Constants.NOT_NECESSARY, InputType.TYPE_CLASS_TEXT);
			setEditTextContent(R.id.baozhiqi,Constants.GOODS_BAOZHIQI,Constants.EMPTY_STRING + goods.getPeriod(),Constants.NOT_NECESSARY, InputType.TYPE_CLASS_NUMBER);
			setEditTextContent(R.id.image,Constants.GOODS_IMAGE,Constants.EMPTY_STRING,Constants.EMPTY_STRING, InputType.TYPE_CLASS_TEXT);
			findViewById(R.id.image).findViewById(R.id.secend).setEnabled(false);
			
			setEditTextContent(R.id.ticheng,Constants.GOODS_TICHENG,goods.getPercentage(),Constants.EMPTY_STRING, InputType.TYPE_CLASS_NUMBER );

			setCheckBoxContent(R.id.jifen, Constants.GOODS_JIFEN ,goods.hasDegree());
			setCheckBoxContent(R.id.youhui, Constants.GOODS_YOUHUI ,goods.isSales());
			
			Button button = (Button) findViewById(R.id.delete);
			button.setBackgroundResource(R.drawable.goods_detail_red_btn);
			button.setText(Constants.DELETE);
			button.setOnClickListener(this);
			if (goods.getFile() == null) {
				noImage();
			}else{
				hasImage();
			}
		}
		findViewById(R.id.image).findViewById(R.id.secend).setEnabled(false);
		
		sortSpinnerView = (TextView) findViewById(R.id.sort).findViewById(R.id.secend);
		sortSpinnerView.setOnClickListener(this);
		
		tongbuSpinnerView = (TextView) findViewById(R.id.tongbu).findViewById(R.id.secend);
		tongbuSpinnerView.setOnClickListener(this);
		
		tongbu = new SpinerPopWindow(this);
		fenlei = new SpinerPopWindow(this);
		findViewById(R.id.scan).setOnClickListener(this);
		getCategoryList();
		getShop(false);
	}


	private void hide(int tongbu2) {
		findViewById(tongbu2).setVisibility(View.GONE);
	}

	private void hasImage() {
		image.setOnClickListener(null);
		deleteImage.setVisibility(View.VISIBLE);
		deleteImage.setOnClickListener(this);
	}

	private void noImage() {
		image.setImageResource(R.drawable.ic_launcher);
		image.setOnClickListener(this);
		deleteImage.setVisibility(View.GONE);
	}

	/**
	 * Gets the shop.
	 * 
	 * @param next
	 *            the next
	 * @return the shop
	 */
	private void getShop(boolean next){
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SHOP_ID, RetailApplication.getmShopInfo().getShopId());
		if (!next) {
			currentPage = Constants.PAGE_SIZE_OFFSET;
		}
		params.setParam(Constants.PAGE,currentPage ++);
		params.setUrl(Constants.SHOP_LIST_URL);
		
		new AsyncHttpPost(params, new RequestResultCallback() {

			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsDetailActivity.this)) {
					return;
				}
				shops = (List<ShopVo>) ju.get(Constants.SHOP_LIST, new TypeToken<List<ShopVo>>(){}.getType());
				final List<String> shopsString = new ArrayList<String>();
				shopsString.add(Constants.NO_TONGBU);
				shopsString.add(Constants.TONGBU);
				int maxLength = Constants.TONGBU.length();
				String Lengest = Constants.EMPTY_STRING;
				for (ShopVo shop : shops) {
					String shopName = shop.getShopName();
					if (shopName.length() > maxLength) {
						maxLength = shopName.length();
						Lengest = shopName;
					}
					shopsString.add(shopName);
				}
				TextPaint paint = tongbuSpinnerView.getPaint();
				tongbuSpinnerView.setWidth((int) (paint.measureText(Lengest) * 1.4f));
				tongbu.refreshData(shopsString, 0);
				tongbuShopId = null;
				tongbuSpinnerView.setText(shopsString.get(0));
				tongbu.setItemListener(new IOnItemSelectListener() {


					@Override
					public void onItemClick(int pos) {
						if (pos > 1) {
							tongbuShopId = shops.get(pos - 2).getShopId();
						}else{
							if (pos == 1) {
								//TODO
								tongbuShopId = userShop.getShopId();
							}else{
								//TODO
								tongbuShopId = null;
							}
						}
						currentShopPosition = pos;
						tongbuSpinnerView.setText(shopsString.get(pos));
					}
				});
			}
			
			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}).execute();
	}

	/**
	 * Gets the category list.
	 * 
	 * @return the category list
	 */
	private void getCategoryList() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.CATEGORY_LIST_URL);
		new AsyncHttpPost(params, new RequestResultCallback() {
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
				getCategory(categoryVo,Constants.EMPTY_STRING,categorys);
				List<String> categorysString = new ArrayList<String>();
				String longest = Constants.EMPTY_STRING;
				for (Category item : categorys) {
					if (longest.length() < item.name.length()) {
						longest = item.name;
					}
					categorysString.add(item.name);
				}
				TextPaint tp = sortSpinnerView.getPaint();
				sortSpinnerView.setWidth((int) (tp.measureText(longest) * 2f));
				currentCategory = categorys.get(0);
				fenlei.refreshData(categorysString, 0);
				fenlei.setItemListener(new IOnItemSelectListener() {

					@Override
					public void onItemClick(int pos) {
						System.out.println(pos);
						currentCategory = categorys.get(pos);
						sortSpinnerView.setText(currentCategory.name);
					}
				});
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
	private void setCheckBoxContent(int id, String label, boolean b) {
		View layout = findViewById(id);
		TextView labelText = (TextView) layout.findViewById(R.id.check_title);
		labelText.setText(label);
		CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
		checkBox.setChecked(b);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!edited) {
					switchToEditMode();
				}
			}
		});
		
		if (mode.equals(Constants.EDIT)) {
			checkBox.setEnabled(userShop.canEdit());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#switchToEditMode()
	 */
	@Override
	protected void switchToEditMode() {
		super.switchToEditMode();
		findViewById(R.id.title_right).setOnClickListener(this);
		findViewById(R.id.title_left).setOnClickListener(this);
		edited = true;
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
	private void setSpinerConetent(int tongbu, String label, String value) {
		View layout = findViewById(tongbu);
		TextView tongbuLable = (TextView) layout.findViewById(R.id.main);
		tongbuLable.setText(label);
		TextView tongbuValue = (TextView) layout.findViewById(R.id.secend);
		tongbuValue.setText(value);
		
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
	private EditText setEditTextContent(int id, String lable, String content, String hint, int inputType) {
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
		if (mode.equals(Constants.EDIT)) {
			inputView.setEnabled(userShop.canEdit());
		}
		inputView.setInputType(inputType);
		return inputView;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right:
			save();
			break;
			
		case R.id.secend:
			if (v == sortSpinnerView) {
				fenlei.setWidth(sortSpinnerView.getWidth());
				fenlei.showAsDropDown(sortSpinnerView);
			}else{
				tongbu.setWidth(tongbuSpinnerView.getWidth());
				tongbu.showAsDropDown(tongbuSpinnerView);
			}
			break;
			
		case R.id.imageView:
			chooseImage();
			break;
		case R.id.deleteImage:
			noImage();
			break;
			
		case R.id.delete:
			if (mode.equals(Constants.ADD) ) {
				continusAdd();
			}else{
				deleteGoods();
			}
			break;
			
		case R.id.scan:
			startActivityForResult(new Intent(this, CaptureActivity.class), 10086);
			break;
		default:
			break;
		}
	}
	
	
	
	private void continusAdd() {
		findViewById(R.id.scan).setVisibility(View.VISIBLE);
	}

	private void deleteGoods() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.DELETE_URL);
		params.setParam(Constants.SHOP_ID, shop.getShopId());
		params.setParam(Constants.GOODS_ID, goods.getGoodsId());
		getProgressDialog().show();
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				getProgressDialog().hide();
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
				ToastUtil.showShortToast(GoodsDetailActivity.this, Constants.DELETE_SUCCESS);
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
		
		new AlertDialog.Builder(this).setTitle(Constants.WHERE_IMAGE).setItems(
				Constants.GET_IMAGE_METHOD, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							Intent photoPickerIntent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							photoPickerIntent.setType(Constants.IMAGE_INTENT);
							photoPickerIntent.putExtra(Constants.OUTPUT_FORMAT, Bitmap.CompressFormat.JPEG.toString());
							photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
							startActivityForResult(photoPickerIntent, IMAGE_REQUEST_CODE);
						}else{
							Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							intentFromCapture.putExtra(Constants.OUTPUT_FORMAT,Bitmap.CompressFormat.JPEG.toString());
							intentFromCapture.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
							File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);  
							File tempFile = new File(path,Constants.TEMP_PHOTO_FILE);
							// 判断存储卡是否可以用，可用进行存储
							intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
							startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
						}
						dialog.dismiss();
					}
				}).show();		
		
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				file = new File(data.getData().getPath());
				Bitmap bitmap = decodeUriAsBitmap(data.getData());
				final File tempFile = new File(getCacheDir(), Constants.TEMPFILE + System.currentTimeMillis() + Constants.PNG);
				try {
					bitmap.compress(CompressFormat.PNG, 80, new FileOutputStream(tempFile));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				image.setImageBitmap(bitmap);
				
				break;
			case CAMERA_REQUEST_CODE:
				File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);  
				File tempFile2 = new File(path,Constants.TEMP_PHOTO_FILE);
				file = tempFile2;
				Bitmap bitmap2 = decodeUriAsBitmap(Uri.fromFile(tempFile2));

				image.setImageBitmap(bitmap2);
				
				break;
				
			case 10086:
				search(data.getStringExtra(Constants.DEVICE_CODE));
				break;
			}
			hasImage();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
	/**
	 * Search.
	 */
	private void search(String code) {
		getProgressDialog().show();
		if (code == null || code.length() == 0) {
			return;
		}
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SHOP_ID, userShop.getShopId());
		params.setParam(Constants.SEARCH_CODE, code);
		params.setParam(Constants.PAGE_SIZE, Constants.PAGE_SIZE_OFFSET);
		params.setUrl(Constants.GOODS_LIST_URL);
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				getProgressDialog().dismiss();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsDetailActivity.this)) {
					return;
				}
//				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
//				
//				JsonElement jsonElement = jo.get("returnCode");
//				String returnCode = null;
//				if (jsonElement != null) {
//					returnCode = jsonElement.getAsString();
//				}
//				if (returnCode == null || !returnCode.equals("success")) {
//					ToastUtil.showShortToast(GoodsDetailActivity.this, "获取失败");
//					return;
//				}
				ArrayList<GoodsVo> goods = (ArrayList<GoodsVo>) ju.get(Constants.GOODS_LIST, new TypeToken<List<GoodsVo>>(){}.getType());
				
				//TODO
				startActivity(new Intent(GoodsDetailActivity.this, GoodsListWithImageActivity.class).putExtra("goods", goods)
						.putExtra("shop", userShop));
			}
			
			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}).execute();
	}
	Uri imageUri = getTempUri(); // The Uri to store the big bitmap

	File file = null;
	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri), null, options);
			
			int width = options.outWidth;
			int height = options.outHeight;
			int samplerSize = width * height / (1000 * 500);
			
			options.inJustDecodeBounds = false;
			options.inSampleSize = samplerSize;
			
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri), null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}


	/**
	 * Save.
	 */
	private void save() {
		String goods = genGoods();
		if (goods == null) {
			return;
		}
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SHOP_ID, RetailApplication.getmShopInfo().getShopId());
		try {
			params.setParam(Constants.GOODS, new JSONObject(goods.toString()));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		params.setParam(Constants.OPT_TYPE, mode);
		params.setUrl(Constants.GOODS_SAVE_URL);
		
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsDetailActivity.this)) {
					return;
				}
				ToastUtil.showShortToast(GoodsDetailActivity.this, Constants.GOODS_SAVE_SUCCESS);
				finish();
			}
			
			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}).execute();
	}

	/**
	 * Gen goods.
	 * 
	 * @return the string
	 */
	private String genGoods() {
		if (goods == null) {
			goods = new GoodsVo();
		}
		
		String name = getValueStringFromTextView(R.id.name, true, Constants.INPUT_GOODS_NAME);
		String lingshou = getValueStringFromTextView(R.id.lingshoujia, true, Constants.INPUT_GOODS_LINGSHOUJIA);
		
		if (name == null || lingshou == null ) {
			return null;
		}else{
			goods.setGoodsName(name);
			try {
				float temp = Float.parseFloat(lingshou);
				if (temp < 0) {
					ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_LINGSHOUJI);
					return null;
				}
				goods.setPetailPrice(lingshou);
			} catch (Exception e) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_LINGSHOUJI);
				e.printStackTrace();
				return null;
			}
		}
		
		String tiaoma = getValueStringFromTextView(R.id.tiaoma, false, null);
		try {
			float temp = Float.parseFloat(tiaoma);
			if (temp < 0 || tiaoma.length() != 16) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_TIAOMA);
				return null;
			}
		} catch (Exception e) {
			ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_TIAOMA);
			e.printStackTrace();
			return null;
		}
		goods.setBarCode(tiaoma);
		
		String daima = getValueStringFromTextView(R.id.daima, false, null);
		try {
			float temp = Float.parseFloat(daima);
			if (temp < 0 || daima.length() != 16) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_DAIMA);
				return null;
			}
		} catch (Exception e) {
			ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_DAIMA);
			e.printStackTrace();
			return null;
		}
		goods.setInnerCode(daima);
		
		String jinhuo = getValueStringFromTextView(R.id.jinhuojia, false, null);
		if (jinhuo != null) {
			try {
				float temp = Float.valueOf(jinhuo);
				if (temp < 0) {
					ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_JINHUO);
					return null;
				}
				goods.setPurchasePrice(jinhuo);
			} catch (Exception e) {
				e.printStackTrace();
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_JINHUO);
				return null;
			}
		}
		
		if (userShop.isLeaf()) {
			String kucun = getValueStringFromTextView(R.id.kucun, false, null);
			if (kucun == null) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_KUCUN);
				return null;
			}
			int kucunInt = 0;
			try {
				kucunInt = Integer.parseInt(kucun);
				if (kucunInt < 0) {
					ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_KUCUN);
					return null;
				}
			} catch (Exception e) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_KUCUN);
				e.printStackTrace();
				return null;
			}
			goods.setNumber(kucunInt);
		}else{
			goods.setSynShopId(tongbuShopId);
		}
		
		String jianma = getValueStringFromTextView(R.id.jianma, false, null);
		goods.setShortCode(jianma);
		
		String pinyin = getValueStringFromTextView(R.id.pingyinma, false, null);
		goods.setSpell(pinyin);
		
		goods.setCategoryId(currentCategory.id);
		
		String guige = getValueStringFromTextView(R.id.guige, false, null);
		goods.setSpecification(guige);
		
		String pinpai = getValueStringFromTextView(R.id.pinpan, false, null);
		goods.setBrandName(pinpai);
		
		String chandi = getValueStringFromTextView(R.id.chandi, false, null);
		goods.setOrigin(chandi);
		
		String baozhiqi = getValueStringFromTextView(R.id.baozhiqi, false, null);
		if (baozhiqi != null) {
			try {
				Integer valueOf = Integer.valueOf(baozhiqi);
				if (valueOf < 0) {
					ToastUtil.showShortToast(this, Constants.GOODS_PERIOD);
					return null;
				}
				goods.setPeriod(valueOf);
			} catch (Exception e) {
				ToastUtil.showShortToast(this, Constants.GOODS_PERIOD);
				return null;
			}
		}
		
		if (deleteImage.getVisibility() == View.VISIBLE) {
			image.buildDrawingCache();
			goods.setFile(bitmap2Bytes(image.getDrawingCache()));
			goods.setFileName(goods.getGoodsName() + Constants.PNG);
		}
		
	
		String bili = getValueStringFromTextView(R.id.ticheng, false, null);
		if (bili != null) {
			try {
				Float valueOf = Float.parseFloat(getValueStringFromTextView(R.id.ticheng, false, null));
				if(valueOf < 0 || valueOf > 100){
					ToastUtil.showShortToast(this, Constants.GOODS_PERIOD);
					return null;
				}
				goods.setPercentage(String.valueOf((valueOf / 100)));
			} catch (Exception e) {
				ToastUtil.showShortToast(this, Constants.GOODS_PERIOD);
				return null;
			}
		}
		
		CheckBox jifen = (CheckBox)findViewById(R.id.jifen).findViewById(R.id.checkbox);
		goods.setHasDegree(jifen.isChecked());
		
		
		CheckBox youhui = (CheckBox) findViewById(R.id.youhui).findViewById(R.id.checkbox);
		goods.setIsSales(youhui.isChecked());
		
		String baseId = goods.getBaseId();
		if (baseId.length() != 32) {
			baseId = String.format("%032d", Integer.parseInt(baseId));
			goods.setBaseId(baseId);
		}
		String brandId = goods.getBrandId();
		if (brandId.length() != 32) {
			brandId = String.format("%032d", Integer.parseInt(brandId));
			goods.setBaseId(brandId);
		}
		String unitId = goods.getUnitId();
		if (unitId.length() != 32) {
			unitId = String.format("%032d", Integer.parseInt(unitId));
			goods.setBaseId(unitId);
		}
		
		return new GsonBuilder().serializeNulls().create().toJson(goods);
	}


	private byte[] bitmap2Bytes(Bitmap input) {
		if (input == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		input.compress(CompressFormat.PNG, 80, baos);
		return baos.toByteArray();
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
		String content = ((TextView)findViewById(id).findViewById(R.id.secend)).getText().toString();
		if (content.length() == 0) {
			if (b) {
				ToastUtil.showShortToast(this, string);
			}
			return null;
		}
		return content;
	}
	
}

 