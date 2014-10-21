package com.dfire.retail.app.manage.activity.goodsmanager;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.CaptureActivity;
import com.dfire.retail.app.manage.adapter.GoodsSortListForMenuAdapter;
import com.dfire.retail.app.manage.data.CategoryVo;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Class GoodsSearchActivity.
 * 
 * @author albuscrow
 */
public class GoodsSearchActivity extends GoodsManagerBaseActivity implements OnClickListener, IOnItemSelectListener, OnItemClickListener {
	
	/** The m menu. */
	private MenuDrawer mMenu;
	private int currentPage = Constants.PAGE_SIZE_OFFSET;
	private SpinerPopWindow shopsSpinner;
	private TextView shopTextView;
	private  ArrayList<ShopVo> shops;
	private ArrayList<String> shopsStr;
	private ShopVo currentShop;
	private ListView sortList;
	private ArrayList<Category> categorys;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		currentShop = RetailApplication.getShopVo();
		super.onCreate(savedInstanceState);
		mMenu = MenuDrawer.attach(this, Position.RIGHT);
		
		mMenu.setContentView(R.layout.goods_manager_title_layout);
		mMenu.setMenuView(R.layout.activity_goods_sort_menu);
		sortList = ((ListView)mMenu.findViewById(R.id.goods_sort_list));
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
		shopTextView = (TextView) findViewById(R.id.shopName);
		if (currentShop.getType() == ShopVo.DANDIAN || currentShop.getType() == ShopVo.MENDIAN) {
			shopTextView.setCompoundDrawables(null, null, null, null);
		}else{
			shopTextView.setOnClickListener(this);
		}
		
		getShop(true);
		getCategoryList();
		getGoodsNum();
	}
	
	private void getGoodsNum() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.GOODS_COUNT_URL);
		params.setParam(Constants.SHOP_ID, currentShop.getShopId());
		new AsyncHttpPost(params, new RequestResultCallback() {
			
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
				ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
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
		new AsyncHttpPost(params, new RequestResultCallback() {


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
				ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}

	private void getShop(final boolean isInit) {
		if (isInit) {
			currentPage = Constants.PAGE_SIZE_OFFSET;
		}
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SHOP_LIST_URL);
		params.setParam(Constants.SHOP_ID, RetailApplication.getmShopInfo().getShopId());
		params.setParam(Constants.PAGE, currentPage ++);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null ||!returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.getErrorInf(null, null));
					return;
				}
				if (isInit) {
					if (shops == null) {
						shops = new ArrayList<ShopVo>();
					}
					shops.clear();
					shops.add(currentShop);
				}
				shops.addAll((Collection<? extends ShopVo>) new Gson().fromJson(jo.get("shopList"), new TypeToken<List<ShopVo>>(){}.getType()));
				int pageSize = jo.get(Constants.PAGE_SIZE).getAsInt();
				if (pageSize >= currentPage) {
					getShop(false);
				}else{
					shopsStr = new ArrayList<String>();
					int maxLength = 0;
					String Lengest = Constants.EMPTY_STRING;
					for (ShopVo shop : shops) {
						String shopName = shop.getShopName();
						if (shopName.length() > maxLength) {
							maxLength = shopName.length();
							Lengest = shopName;
						}
						shopsStr.add(shopName);
					}
					TextPaint paint = shopTextView.getPaint();
					shopTextView.setWidth((int) (paint.measureText(Lengest) * 1.2f));
					shopsSpinner = new SpinerPopWindow(GoodsSearchActivity.this);
					shopsSpinner.refreshData(shopsStr, 0);
					shopTextView.setText(currentShop.getShopName());
					shopsSpinner.setItemListener(GoodsSearchActivity.this);				
				}

			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
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
			mMenu.toggleMenu();
			break;
			
		case R.id.add:
			startActivity(new Intent(GoodsSearchActivity.this, GoodsDetailActivity.class).putExtra(Constants.MODE, Constants.ADD));
			break;
			
		case R.id.search:
			search();
			break;
			
		case R.id.scanButton:
		case R.id.scan:
			startActivityForResult(new Intent(this, CaptureActivity.class), 10086);
			break;
			
		case R.id.shopName:
			shopsSpinner.setWidth(shopTextView.getWidth());
			shopsSpinner.showAsDropDown(shopTextView);
			break;
		default:
			break;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			search(data.getStringExtra(Constants.DEVICE_CODE));
		}
	}
	
	/**
	 * Search.
	 */
	private void search() {
		String code = ((TextView)findViewById(R.id.code)).getText().toString();
		search(code);
	}

	/**
	 * Search.
	 */
	private void search(final String code) {
		final ProgressDialog pd = ProgressDialog.show(this, null, Constants.WAIT_SEARCH_GOODS, true, false);
		if (code == null || code.length() == 0) {
			return;
		}
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SHOP_ID, currentShop.getShopId());
		params.setParam(Constants.SEARCH_CODE, code);
		params.setParam(Constants.PAGE, Constants.PAGE_SIZE_OFFSET);
		params.setUrl(Constants.GOODS_LIST_URL);
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				pd.dismiss();
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
				startActivity(new Intent(GoodsSearchActivity.this, GoodsListWithImageActivity.class)
				.putExtra(Constants.GOODS, goods)
				.putExtra(Constants.PAGE_SIZE, jo.get(Constants.PAGE_SIZE).getAsInt())
				.putExtra(Constants.SEARCH_STATUS, jo.get(Constants.SEARCH_STATUS).getAsInt())
				.putExtra(Constants.SHOP, currentShop)
				.putExtra(Constants.SEARCH_CODE, code));
			}
			
			@Override
			public void onFail(Exception e) {
				pd.dismiss();
				ToastUtil.showShortToast(GoodsSearchActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}

	@Override
	public void onItemClick(int pos) {
		shopTextView.setText(shops.get(pos).getShopName());
		currentShop = shops.get(pos);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position,
			long id) {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.GOODS_LIST_URL);
		params.setParam(Constants.SHOP_ID, currentShop.getShopId());
		params.setParam(Constants.CATEGORY_ID, categorys.get(position).id);
		params.setParam(Constants.PAGE, Constants.PAGE_SIZE_OFFSET);
		
		new AsyncHttpPost(params, new RequestResultCallback() {
			
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
				.putExtra(Constants.SHOP, currentShop));
			}
			
			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}).execute();
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