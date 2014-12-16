package com.dfire.retail.app.manage.activity.goodsmanager;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.logisticmanager.SelectShopActivity;
import com.dfire.retail.app.manage.adapter.GoodsSortListForMenuAdapter;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Class GoodsSearchActivity.
 * 
 * @author albuscrow
 */
public class GoodsSearchActivity extends GoodsManagerBaseActivity implements OnClickListener, OnItemClickListener {
	
	/** The m menu. */
	private MenuDrawer mMenu;
	private int currentPage = Constants.PAGE_SIZE_OFFSET;
	private TextView shopTextView;
	private  ArrayList<ShopVo> shops;
	private ArrayList<String> shopsStr;
	private AllShopVo currentShop;
	private ListView sortList;
	private ArrayList<Category> categorys;
//	private OneColumnSpinner shopsSpinner;
	private EditText searchCode;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ShopVo currentShopDetail = RetailApplication.getShopVo();
		currentShop = currentShopDetail.toAllShopVo();
		super.onCreate(savedInstanceState);
		mMenu = MenuDrawer.attach(this, Position.RIGHT);
		
		mMenu.setContentView(R.layout.goods_manager_title_layout);
		mMenu.setMenuView(R.layout.activity_goods_sort_menu);
		mMenu.findViewById(R.id.fenlei).setOnClickListener(this);
		sortList = ((ListView)mMenu.findViewById(R.id.goods_sort_list));
		addFooter(sortList, true);
		
		FrameLayout body = (FrameLayout) mMenu.findViewById(R.id.body);
		body.requestFocus();
		getLayoutInflater().inflate(R.layout.activity_goods_search, body, true);
		
		findViewById(R.id.title_left).setOnClickListener(this);
		findViewById(R.id.title_right).setOnClickListener(this);
		((TextView)findViewById(R.id.title_text)).setText(Constants.GOODS_TITLE);
		
		findViewById(R.id.add).setOnClickListener(this);
		findViewById(R.id.search).setOnClickListener(this);
		
		findViewById(R.id.scan).setOnClickListener(this);
		findViewById(R.id.scanButton).setOnClickListener(this);
		shopTextView = (TextView) findViewById(R.id.shop_name);
		
//		if (currentShop.getParentId() == null || currentShop.getParentId().length() == 0) {
		if (currentShopDetail.getType() == ShopVo.DANDIAN || currentShopDetail.getType() == ShopVo.MENDIAN) {
			shopTextView.setCompoundDrawables(null, null, null, null);
			shopTextView.setText(currentShopDetail.getShopName());
			shopTextView.setTextColor(getResources().getColor(R.color.not_necessary));
		}else{
			shopTextView.setOnClickListener(this);
			shopTextView.setText(Constants.ALL_CHIRLDREN_SHOP);
		}
		setBack();
		setRightBtn(R.drawable.ico_cate, Constants.CATEGORY_TEXT);
		
//		getShop(true);
//		getCategoryList();
		
//		shopsSpinner = new OneColumnSpinner(this);
//		shopsSpinner.setTitleText(Constants.CHOOSE_SHOP);
		
		searchCode = ((EditText) findViewById(R.id.code));
		setSearchClear(searchCode);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		getGoodsNum();
		getCategoryList();
	}
	
    
	
	private void getGoodsNum() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.GOODS_COUNT_URL);
		params.setParam(Constants.SHOP_ID, currentShop.getShopId());
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null || !returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.getErrorInf(null, null));
					return;
				}
				String count = jo.get(Constants.COUNT).getAsString();
				((TextView)findViewById(R.id.num)).setText(count);;
			}
			
			@Override
			public void onFail(Exception e) {
//				ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}, false).execute();
	}

	private void getCategory(String parentName, List<CategoryVo> categoryVo,
					String parent, List<Category> categorys, int depth) {
		for (CategoryVo item : categoryVo) {
			if (item.getCategoryList() != null) {
				getCategory(item.getName(), item.getCategoryList(), parent + item.getName() + Constants.CONNECTOR, categorys, depth + 1);
			}else{
				Category category = new Category();
				if (parent.length() != 0) {
					category.parents = parent.substring(0, parent.length() - 1);
					category.parentName = parentName;
				}
				category.name = item.getName();
				category.id = item.getId();
				category.depth = depth;
				category.original = item;
				categorys.add(category);
			}
		}
	}
	
	private void getCategoryList() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.CATEGORY_LIST_URL);
		new AsyncHttpPost(this, params, new RequestResultCallback() {


			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null || !returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.getErrorInf(null, null));
					return;
				}
				List<CategoryVo> categoryVo =  new Gson().fromJson(jo.get(Constants.CATEGORY_LIST), new TypeToken<List<CategoryVo>>(){}.getType());
				if (categoryVo == null) {
					categoryVo = new ArrayList<CategoryVo>();
				}
				categorys = new ArrayList<Category>();
				getCategory(Constants.EMPTY_STRING, categoryVo,Constants.EMPTY_STRING,categorys, 0);
				List<String> categorysString = new ArrayList<String>();
				for (Category item : categorys) {
					categorysString.add(item.name);
				}
				
				sortList.setAdapter(new GoodsSortListForMenuAdapter(GoodsSearchActivity.this, categorys));
				sortList.setOnItemClickListener(GoodsSearchActivity.this);
			}	

			@Override
			public void onFail(Exception e) {
//				ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}, false).execute();
	}
//
//	private void getShop(final boolean isInit) {
//		if (isInit) {
//			currentPage = Constants.PAGE_SIZE_OFFSET;
//		}
//		RequestParameter params = new RequestParameter(true);
//		params.setUrl(Constants.SHOP_LIST_URL);
//		params.setParam(Constants.SHOP_ID, RetailApplication.getShopVo().getShopId());
//		params.setParam(Constants.PAGE, currentPage ++);
//		new AsyncHttpPost(params, new RequestResultCallback() {
//
//			@Override
//			public void onSuccess(String str) {
//				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
//				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
//				String returnCode = null;
//				if (jsonElement != null) {
//					returnCode = jsonElement.getAsString();
//				}
//				if (returnCode == null ||!returnCode.equals(Constants.LSUCCESS)) {
//					ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.getErrorInf(null, null));
//					return;
//				}
//				if (isInit) {
//					if (shops == null) {
//						shops = new ArrayList<ShopVo>();
//					}
//					shops.clear();
//					shops.add(currentShop);
//				}
//				shops.addAll((Collection<? extends ShopVo>) new Gson().fromJson(jo.get(Constants.All_SHOP), new TypeToken<List<ShopVo>>(){}.getType()));
//				int pageSize = jo.get(Constants.PAGE_SIZE).getAsInt();
//				if (pageSize >= currentPage) {
//					getShop(false);
//				}else{
//					shopsStr = new ArrayList<String>();
//					for (ShopVo shop : shops) {
//						String shopName = shop.getShopName();
//						shopsStr.add(shopName);
//					}
//					shopsSpinner.setData(shopsStr);
//					shopTextView.setText(shopsStr.get(0));
//				}
//
//			}
//			
//			@Override
//			public void onFail(Exception e) {
//				ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.getErrorInf(null, null));
//				e.printStackTrace();
//			}
//		}).execute();
//	}

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
			mMenu.toggleMenu();
			break;
			
		case R.id.add:
//			startActivity(new Intent(GoodsSearchActivity.this, GoodsDetailActivity.class).putExtra(Constants.MODE, Constants.ADD));
			startActivity(new Intent(GoodsSearchActivity.this, GoodsDetailActivity.class)
								.putExtra(Constants.SHOP, currentShop)
								.putExtra(Constants.SEARCH_STATUS, Constants.ZHONGDIANYOU)
								.putExtra(Constants.MODE, Constants.ADD));
//			startActivityForResult(new Intent(this, CaptureActivity.class), Constants.FOR_ADD);
			break;
			
		case R.id.search:
			search();
			break;
			
		case R.id.scanButton:
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.FOR_SEARCH);
			break;
		case R.id.scan:
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.FOR_GET);
			break;
			
		case R.id.shop_name:
//			shopsSpinner.show();
//			shopsSpinner.getConfirmButton().setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					shopsSpinner.dismiss();
//					Integer index = shopsSpinner.getCurrentData();
//					shopTextView.setText(shops.get(index).getShopName());
//					currentShop = shops.get(index);
//					getGoodsNum();
//				}
//			});
//			shopsSpinner.getCancelButton().setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					shopsSpinner.dismiss();
//				}
//			});
//			Intent intent = new Intent(GoodsSearchActivity.this,UserShopSelectActivity.class);	   	           
//            startActivityForResult(intent, SELECTSHOPRECODE);
			Intent selectIntent =new Intent(GoodsSearchActivity.this,SelectShopForGoodsManagerActivity.class);
			selectIntent.putExtra(Constants.SHOP_ID, currentShop.getShopId());
			selectIntent.putExtra(Constants.OPT_TYPE, SelectShopForGoodsManagerActivity.SEARCH);
			startActivityForResult(selectIntent, SELECTSHOPRECODE);
			break;
			
		case R.id.fenlei:
			startActivity(new Intent(this, GoodsSortListActivity.class));
			break;
		default:
			break;
		}
		
	}
    private static final int SELECTSHOPRECODE =2;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECTSHOPRECODE) {
				currentShop = (AllShopVo) data.getSerializableExtra(Constants.SHOP);
				shopTextView.setText(currentShop.getShopName());
				getGoodsNum();
			}else{
				String code = data.getStringExtra(Constants.DEVICE_CODE);
				searchCode.setText(code);
				search(code, true, requestCode);
			}
		}
	}
	
	/**
	 * Search.
	 */
	private void search() {
		String code = searchCode.getText().toString();
		search(code, false, Constants.INVALID_INT);
	}

	/**
	 * Search.
	 */
	private void search(final String code, final boolean isScan, final int requestCode) {
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SHOP_ID, currentShop.getShopId());
		params.setParam(Constants.SEARCH_CODE, code);
		params.setParam(Constants.PAGE, Constants.PAGE_SIZE_OFFSET);
		params.setUrl(Constants.GOODS_LIST_URL);
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				final JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsSearchActivity.this)) {
					return;
				}
				
				final ArrayList<GoodsVo> goods = (ArrayList<GoodsVo>) ju.get(Constants.GOODS_LIST, new TypeToken<List<GoodsVo>>(){}.getType());
				if (goods == null || goods.size() == 0) {
					if (code.length() != 0) {
						final AlertDialog alertDialog = new AlertDialog(GoodsSearchActivity.this);
						alertDialog.setMessage(Constants.INF_NO_GOODS);
						alertDialog.setPositiveButton(Constants.CONFIRM, new OnClickListener() {
							@Override
							public void onClick(View v) {
								alertDialog.dismiss();

								GoodsVo goods = new GoodsVo();
								if (isScan) {
									goods.setBarCode(code);
								}
								startActivity(new Intent(GoodsSearchActivity.this, GoodsDetailActivity.class)
								.putExtra(Constants.GOODS, goods)
								.putExtra(Constants.SHOP, currentShop)
//								.putExtra(Constants.SEARCH_STATUS, Constants.ZHONGDIANYOU)
								.putExtra(Constants.MODE, Constants.ADD));
							}
						});
						alertDialog.setNegativeButton(Constants.CANCEL, new OnClickListener() {
							@Override
							public void onClick(View v) {
								alertDialog.dismiss();
							}
						});
					}else{
						ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.NO_GOODS_SCAN);
					}
					return;
				}
				final int searchStatus = ju.getInt(Constants.SEARCH_STATUS);
				
				//本店没有该商品
				if (searchStatus != Constants.BENDIANYOU /*&& !RetailApplication.getShopVo().isZhongdian()*/ ) {
					final AlertDialog alertDialog = new AlertDialog(GoodsSearchActivity.this);
					alertDialog.setMessage(Constants.INF_NO_GOODS);
					alertDialog.setPositiveButton(Constants.CONFIRM, new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
							if (goods.size() > 1) {
								startActivity(new Intent(GoodsSearchActivity.this, GoodsListWithImageActivity.class)
								.putExtra(Constants.GOODS, goods)
								.putExtra(Constants.PAGE_SIZE, ju.getInt(Constants.PAGE_SIZE))
								.putExtra(Constants.SEARCH_STATUS, searchStatus)
								.putExtra(Constants.SHOP, currentShop)
								.putExtra(Constants.CATEGORY, categorys)
								.putExtra(Constants.MODE, Constants.ADD)
								.putExtra(Constants.SEARCH_CODE, code));
							}else{
								startActivity(new Intent(GoodsSearchActivity.this, GoodsDetailActivity.class)
								.putExtra(Constants.GOODS, goods.get(0))
								.putExtra(Constants.SHOP, currentShop)
								.putExtra(Constants.SEARCH_STATUS, searchStatus)
								.putExtra(Constants.MODE, Constants.ADD));
							}	
						}
					});
					alertDialog.setNegativeButton(Constants.CANCEL, new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
						}
					});
				}else{
					if (goods.size() > 1) {
						int searchStatusForIntent = searchStatus;
						Intent intent = new Intent(GoodsSearchActivity.this, GoodsListWithImageActivity.class)
						.putExtra(Constants.GOODS, goods)
						.putExtra(Constants.PAGE_SIZE, ju.getInt(Constants.PAGE_SIZE))
						.putExtra(Constants.SEARCH_STATUS, searchStatusForIntent)
						.putExtra(Constants.SHOP, currentShop)
						.putExtra(Constants.MODE, Constants.EDIT)
						.putExtra(Constants.CATEGORY, categorys)
						.putExtra(Constants.SEARCH_CODE, code);
						
						intent.putExtra(Constants.MODE, Constants.EDIT);
						startActivity(intent);
					}else{
						Intent intent = new Intent(GoodsSearchActivity.this, GoodsDetailActivity.class)
						.putExtra(Constants.GOODS, goods.get(0))
						.putExtra(Constants.SHOP, currentShop)
						.putExtra(Constants.SEARCH_STATUS, searchStatus);
						
						
						intent.putExtra(Constants.MODE, Constants.EDIT);
						startActivity(intent);
					}
				}
			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}).execute();
	}

//	@Override
//	public void onItemClick(int pos) {
//		shopTextView.setText(shops.get(pos).getShopName());
//		currentShop = shops.get(pos);
//		getGoodsNum();
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position,
			long id) {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.GOODS_LIST_URL);
		params.setParam(Constants.SHOP_ID, currentShop.getShopId());
		params.setParam(Constants.CATEGORY_ID, categorys.get(position).id);
		params.setParam(Constants.PAGE, Constants.PAGE_SIZE_OFFSET);
		
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null || !returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.getErrorInf(null, null));
					return;
				}
				ArrayList<GoodsVo> goods = new Gson().fromJson(jo.get(Constants.GOODS_LIST), new TypeToken<List<GoodsVo>>(){}.getType());
				if (goods == null || goods.size() == 0) {
					ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.NO_GOODS);
					return;
				}
				startActivity(new Intent(GoodsSearchActivity.this, GoodsListWithImageActivity.class)
				.putExtra(Constants.GOODS, goods)
				.putExtra(Constants.PAGE_SIZE, jo.get(Constants.PAGE_SIZE).getAsInt())
				.putExtra(Constants.SEARCH_STATUS, jo.get(Constants.SEARCH_STATUS).getAsInt())
				.putExtra(Constants.CATEGORY_ID,categorys.get(position).id)
				.putExtra(Constants.CATEGORY, categorys)
				.putExtra(Constants.MODE, Constants.EDIT)
				.putExtra(Constants.SHOP, currentShop));
			}
			
			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}, false).execute();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && 
				(mMenu.getDrawerState() == MenuDrawer.STATE_OPEN || mMenu.getDrawerState() == MenuDrawer.STATE_OPENING)) {
			mMenu.closeMenu();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
