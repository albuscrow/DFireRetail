package com.dfire.retail.app.manage.activity.goodsmanager;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.adapter.GoodsListWithImageAdapter;
import com.dfire.retail.app.manage.adapter.GoodsSortListForMenuAdapter;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.CategoryVo;
import com.dfire.retail.app.manage.data.GoodsVo;
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
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Class GoodsListWithImageActivity.
 * 
 * @author albuscrow
 */
public class GoodsListWithImageActivity extends GoodsManagerBaseActivity implements OnItemClickListener, OnClickListener/*, OnRefreshListener, OnLoadListener */{
	
	private static final int HANDLE_GOODS = 3;
	public static final int DELETE = 0;
	public static final int EDIT = 2;
	public static final int ADD = 1;
	private static final int BATCH = 5;
	/** The goods. */
	private ArrayList<GoodsVo> goods;
	/**
	 * @return the goods
	 */
	public ArrayList<GoodsVo> getGoods() {
		return goods;
	}

	/**
	 * @param goods the goods to set
	 */
	public void setGoods(ArrayList<GoodsVo> goods) {
		this.goods = goods;
	}

	private AllShopVo currentShop;
	private GoodsListWithImageAdapter adapter;
	private int currentPage = Constants.PAGE_SIZE_OFFSET + 1;
	private String categoryId;
	private String searchCode;
	private PullToRefreshListView goodsListView;
//	private SwipeRefreshLayout swipeRefreshLayout;
	private int totalPage;
	private int status;
	private MenuDrawer mMenu;
	private ListView sortList;
	private ArrayList<Category> categorys;
	private GoodsSortListForMenuAdapter categoryAdapter;
	private int searchStatus;
	private int index;
	private int top;
	private int currentPosition;
	private String mode;
	private String hasAlreadyHave;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mMenu = MenuDrawer.attach(this, Position.RIGHT);
		mMenu.setContentView(R.layout.goods_manager_title_layout);
		mMenu.setMenuView(R.layout.activity_goods_sort_menu);
		mMenu.findViewById(R.id.fenlei).setOnClickListener(this);
		sortList = ((ListView)mMenu.findViewById(R.id.goods_sort_list));
		categorys = (ArrayList<Category>) getIntent().getSerializableExtra(Constants.CATEGORY);
		searchStatus = getIntent().getIntExtra(Constants.SEARCH_STATUS, -1);
		mode = getIntent().getStringExtra(Constants.MODE);
		hasAlreadyHave = getIntent().getStringExtra(Constants.IS_ALREADY_HAVE);
		categoryAdapter = new GoodsSortListForMenuAdapter(GoodsListWithImageActivity.this, categorys);
		addFooter(sortList, true);
		sortList.setAdapter(categoryAdapter);
		sortList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				RequestParameter params = new RequestParameter(true);
				params.setUrl(Constants.GOODS_LIST_URL);
				params.setParam(Constants.SHOP_ID, currentShop.getShopId());
				params.setParam(Constants.CATEGORY_ID, categorys.get(position).id);
				params.setParam(Constants.PAGE, Constants.PAGE_SIZE_OFFSET);

				new AsyncHttpPost(GoodsListWithImageActivity.this, params, new RequestResultCallback() {

					@Override
					public void onSuccess(String str) {
						JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
						JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
						String returnCode = null;
						if (jsonElement != null) {
							returnCode = jsonElement.getAsString();
						}
						if (returnCode == null || !returnCode.equals(Constants.LSUCCESS)) {
							ToastUtil.showShortToast(GoodsListWithImageActivity.this, Constants.getErrorInf(null, null));
							return;
						}
						ArrayList<GoodsVo> goods = new Gson().fromJson(jo.get(Constants.GOODS_LIST), new TypeToken<List<GoodsVo>>(){}.getType());
						if (goods == null || goods.size() == 0) {
							ToastUtil.showShortToast(GoodsListWithImageActivity.this, Constants.NO_GOODS);
							return;
						}else{
							categoryId = categorys.get(position).id;
							GoodsListWithImageActivity.this.goods = goods;
							adapter.refreshData(goods);
							mMenu.toggleMenu();
						}
					}

					@Override
					public void onFail(Exception e) {
						e.printStackTrace();
					}
				}, false).execute();
			}
		});
		
		FrameLayout body = (FrameLayout) mMenu.findViewById(R.id.body);
		body.requestFocus();
		getLayoutInflater().inflate(R.layout.activity_goods_manager_list_with_image, body, true);

		findViewById(R.id.title_left).setOnClickListener(this);
		findViewById(R.id.title_right).setOnClickListener(this);
		((TextView)findViewById(R.id.title_text)).setText(Constants.GOODS_TITLE);


		goods = (ArrayList<GoodsVo>) getIntent().getSerializableExtra(Constants.GOODS);
		currentShop = (AllShopVo) getIntent().getSerializableExtra(Constants.SHOP);
		categoryId = getIntent().getStringExtra(Constants.CATEGORY_ID);
		searchCode = getIntent().getStringExtra(Constants.SEARCH_CODE);
		totalPage = getIntent().getIntExtra(Constants.PAGE_SIZE, Constants.INVALID_INT);
		status = getIntent().getIntExtra(Constants.SEARCH_STATUS, Constants.INVALID_INT);
		
		goodsListView = (PullToRefreshListView) findViewById(R.id.goodsList);
		
		adapter = new GoodsListWithImageAdapter(this, goods);
		//TODO
		addFooter(goodsListView.getRefreshableView(), false);
		goodsListView.setAdapter(adapter);
		goodsListView.setOnItemClickListener(this);
		
		initPullToRefreshText(goodsListView);
		

		// 列表刷新和加载更多操作
		goodsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(GoodsListWithImageActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// 启动异步
				getMoreData(true, false);
				// 不执行异步，数据写死测试
				// initDatasTest();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(GoodsListWithImageActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				getMoreData(false, false);
			}

		});
		
		if (totalPage < currentPage) {
			goodsListView.setMode(Mode.PULL_FROM_START);
		}else{
			goodsListView.setMode(Mode.BOTH);
		}
		
//		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.goodsListLayout);
//		swipeRefreshLayout.setOnRefreshListener(this);
//		swipeRefreshLayout.setOnLoadListener(this);
//		swipeRefreshLayout.setColor(android.R.color.holo_blue_bright,
//	                            android.R.color.holo_green_light,
//	                            android.R.color.holo_orange_light,
//	                            android.R.color.holo_red_light);
//		swipeRefreshLayout.setLoadNoFull(true);
		
		findViewById(R.id.more).setOnClickListener(this);
		findViewById(R.id.add).setOnClickListener(this);
		findViewById(R.id.scanButton).setOnClickListener(this);
		setBack();
		setRightBtn(R.drawable.ico_cate, Constants.CATEGORY_TEXT);
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
					ToastUtil.showShortToast(GoodsListWithImageActivity.this, Constants.getErrorInf(null, null));
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
				sortList.setAdapter(new GoodsSortListForMenuAdapter(GoodsListWithImageActivity.this, categorys));
//				sortList.setOnItemClickListener(GoodsListWithImageActivity.this);
			}	

			@Override
			public void onFail(Exception e) {
//				ToastUtil.showShortToast(GoodsListWithImageActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}, false).execute();
	}
	
	
	@Override
	protected void onResume() {
//		getMoreData(true, true);
		getCategoryList();
		super.onResume();
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more:
			startActivityForResult(new Intent(this, GoodsListActivity.class)
							.putExtra(Constants.GOODS, adapter.getData()), BATCH);
			break;
			
		case R.id.add:
			startActivityForResult(new Intent(GoodsListWithImageActivity.this, GoodsDetailActivity.class).putExtra(Constants.MODE, Constants.ADD)
					.putExtra(Constants.SHOP, currentShop), HANDLE_GOODS);
//			startActivityForResult(new Intent(this, CaptureActivity.class), Constants.FOR_ADD);
			break;
			
		case R.id.scanButton:
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.FOR_GET);
			break;
			
		case R.id.title_left:
			finish();
			break;
			
		case R.id.title_right:
			mMenu.toggleMenu();
			break;
			
		case R.id.fenlei:
			startActivity(new Intent(this, GoodsSortListActivity.class));
			break;

		default:
			break;
		}
		
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

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		rememberPosition(parent);
		currentPosition = position - 1;
		startActivityForResult(new Intent(this, GoodsDetailActivity.class)
		.putExtra(Constants.GOODS, goods.get(position - 1))
		.putExtra(Constants.SHOP, currentShop)
		.putExtra(Constants.SEARCH_STATUS, searchStatus)
		.putExtra(Constants.IS_ALREADY_HAVE, hasAlreadyHave)
		.putExtra(Constants.MODE, mode), HANDLE_GOODS);
	}

	private void rememberPosition(AdapterView<?> parent) {
		index = parent.getFirstVisiblePosition();  
		View v = parent.getChildAt(0);
		top = (v == null) ? 0 : v.getTop();
	}

	private String isEdit() {
		if (searchStatus == Constants.ZHONGDIANYOU) {
			return Constants.ADD;
		}else{
			return Constants.EDIT;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == BATCH){ 
			goodsListView.getRefreshableView().setSelection(0);
			goodsListView.post(new Runnable() {

				@Override
				public void run() {
					goodsListView.setRefreshing();
				}
			});
			return;
		}
		
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == HANDLE_GOODS) {
				int result = data.getIntExtra(Constants.RESULT, -1);
				if (result == DELETE) {
					goodsListView.setRefreshing();
//					goods.remove(currentPosition);
				}else if(result == ADD){
					goodsListView.getRefreshableView().setSelection(0);
					goodsListView.post(new Runnable() {
						
						@Override
						public void run() {
							goodsListView.setRefreshing();
						}
					});
//					goods.add(0, (GoodsVo) data.getSerializableExtra(Constants.GOODS));
				}else{
					goods.set(currentPosition, (GoodsVo) data.getSerializableExtra(Constants.GOODS));
				}
				adapter.setData(goods);
				adapter.notifyDataSetChanged();
			}else{
				String code = data.getStringExtra(Constants.DEVICE_CODE);
				//			((EditText)findViewById(R.id.code)).setText(code);
				search(code, requestCode);
			}
		}
	}
	
	
	
	private void search(final String code, final int requestCode) {
		if (code == null || code.length() == 0) {
			return;
		}
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SHOP_ID, currentShop.getShopId());
		params.setParam(Constants.SEARCH_CODE, code);
		params.setParam(Constants.PAGE, Constants.PAGE_SIZE_OFFSET);
		params.setUrl(Constants.GOODS_LIST_URL);
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				final JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsListWithImageActivity.this)) {
					return;
				}
				
				final ArrayList<GoodsVo> goods = (ArrayList<GoodsVo>) ju.get(Constants.GOODS_LIST, new TypeToken<List<GoodsVo>>(){}.getType());
				if (goods== null || goods.size() == 0) {
					if (code.length() != 0) {
						
						final AlertDialog alertDialog = new AlertDialog(GoodsListWithImageActivity.this);
						alertDialog.setMessage(Constants.INF_NO_GOODS);
						alertDialog.setPositiveButton(Constants.CONFIRM, new OnClickListener() {
							@Override
							public void onClick(View v) {
								alertDialog.dismiss();
								
								GoodsVo goods = new GoodsVo();
								goods.setBarCode(code);
								startActivity(new Intent(GoodsListWithImageActivity.this, GoodsDetailActivity.class)
								.putExtra(Constants.GOODS, goods)
								.putExtra(Constants.SHOP, currentShop)
								.putExtra(Constants.SEARCH_STATUS, Constants.ZHONGDIANYOU)
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
						ToastUtil.showShortToast(GoodsListWithImageActivity.this, Constants.NO_GOODS_SCAN);
					}
					return;
				}
				
//				if (goods== null || goods.size() == 0) {
//					final AlertDialog alertDialog = new AlertDialog(GoodsListWithImageActivity.this);
//					alertDialog.setMessage(Constants.INF_NO_GOODS);
//					alertDialog.setPositiveButton(Constants.CONFIRM, new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							alertDialog.dismiss();
//							
//							GoodsVo goods = new GoodsVo();
//							goods.setBarCode(code);
//							startActivity(new Intent(GoodsListWithImageActivity.this, GoodsDetailActivity.class)
//							.putExtra(Constants.GOODS, goods)
//							.putExtra(Constants.SHOP, currentShop)
//							.putExtra(Constants.SEARCH_STATUS, Constants.ZHONGDIANYOU)
//							.putExtra(Constants.MODE, Constants.ADD));
//						}
//					});
//					alertDialog.setNegativeButton(Constants.CANCEL, new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							alertDialog.dismiss();
//						}
//					});
//					return;
//				}
				
				final int searchStatus = ju.getInt(Constants.SEARCH_STATUS);
				//本店没有该商品
				if (searchStatus == Constants.ZHONGDIANYOU) {
					final AlertDialog alertDialog = new AlertDialog(GoodsListWithImageActivity.this);
					alertDialog.setMessage(Constants.INF_NO_GOODS);
					alertDialog.setPositiveButton(Constants.CONFIRM, new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
							if (goods.size() > 1) {
								startActivity(new Intent(GoodsListWithImageActivity.this, GoodsListWithImageActivity.class)
								.putExtra(Constants.GOODS, goods)
								.putExtra(Constants.PAGE_SIZE, ju.getInt(Constants.PAGE_SIZE))
								.putExtra(Constants.SEARCH_STATUS, searchStatus)
								.putExtra(Constants.SHOP, currentShop)
								.putExtra(Constants.CATEGORY, categorys)
								.putExtra(Constants.SEARCH_CODE, code));
							}else{
								startActivity(new Intent(GoodsListWithImageActivity.this, GoodsDetailActivity.class)
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
//					if (goods.size() > 1) {
//						startActivity(new Intent(GoodsListWithImageActivity.this, GoodsListWithImageActivity.class)
//						.putExtra(Constants.GOODS, goods)
//						.putExtra(Constants.PAGE_SIZE, jo.get(Constants.PAGE_SIZE).getAsInt())
//						.putExtra(Constants.SEARCH_STATUS, searchStatus)
//						.putExtra(Constants.SHOP, currentShop)
//						.putExtra(Constants.CATEGORY, categorys)
//						.putExtra(Constants.SEARCH_CODE, code));
//					}else{
//						startActivity(new Intent(GoodsListWithImageActivity.this, GoodsDetailActivity.class)
//						.putExtra(Constants.GOODS, goods.get(0))
//						.putExtra(Constants.SHOP, currentShop)
//						.putExtra(Constants.SEARCH_STATUS, searchStatus)
//						.putExtra(Constants.MODE, Constants.EDIT));
//					}
					if (goods.size() > 1) {
						int searchStatusForIntent = searchStatus;
						if (requestCode == Constants.FOR_ADD) {
							searchStatusForIntent = Constants.ZHONGDIANYOU;
						}	
						startActivity(new Intent(GoodsListWithImageActivity.this, GoodsListWithImageActivity.class)
						.putExtra(Constants.GOODS, goods)
						.putExtra(Constants.PAGE_SIZE, ju.getInt(Constants.PAGE_SIZE))
						.putExtra(Constants.SEARCH_STATUS, searchStatusForIntent)
						.putExtra(Constants.SHOP, currentShop)
						.putExtra(Constants.CATEGORY, categorys)
						.putExtra(Constants.SEARCH_CODE, code));
					}else{
						String mode = null;
						if (requestCode == Constants.FOR_ADD) {
							mode = Constants.ADD;
						}else{
							mode = Constants.EDIT;
						}
						startActivity(new Intent(GoodsListWithImageActivity.this, GoodsDetailActivity.class)
						.putExtra(Constants.GOODS, goods.get(0))
						.putExtra(Constants.SHOP, currentShop)
						.putExtra(Constants.SEARCH_STATUS, searchStatus)
						.putExtra(Constants.MODE, mode));
					}
				}
				totalPage = ju.getInt(Constants.PAGE_SIZE);
				if (totalPage < currentPage) {
					goodsListView.setMode(Mode.PULL_FROM_START);
				}else{
					goodsListView.setMode(Mode.BOTH);
				}
				adapter.refreshData(goods);
			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(GoodsListWithImageActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}).execute();
	}
	
	/**
	 * Search.
	 */
	private void getMoreData(final boolean isRefresh, final boolean isRememberPosition) {
		if (isRefresh) {
			currentPage = Constants.PAGE_SIZE_OFFSET;
			totalPage = Integer.MAX_VALUE;
		}
		if (totalPage < currentPage) {
			goodsListView.onRefreshComplete();
//			if (isRefresh) {
//				swipeRefreshLayout.setRefreshing(false);
//			}else{
//				swipeRefreshLayout.setLoading(false);
//			}
			return;
		}
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.SHOP_ID, currentShop.getShopId());
		if (searchCode != null) {
			params.setParam(Constants.SEARCH_CODE, searchCode);
		}
		if (categoryId != null) {
			params.setParam(Constants.CATEGORY_ID, categoryId);
		}
		params.setParam(Constants.PAGE, currentPage ++);
		params.setUrl(Constants.GOODS_LIST_URL);
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				goodsListView.onRefreshComplete();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsListWithImageActivity.this)) {
					return;
				}
//				rememberPosition(goodsListView.getRefreshableView());
				ArrayList<GoodsVo> goods = (ArrayList<GoodsVo>) ju.get(Constants.GOODS_LIST, new TypeToken<List<GoodsVo>>(){}.getType());
				if (isRefresh) {
					GoodsListWithImageActivity.this.goods = goods;
					adapter.refreshData(goods);
//					swipeRefreshLayout.setRefreshing(false);
				}else{
//					GoodsListWithImageActivity.this.goods.addAll(goods);
					adapter.addData(goods);
//					swipeRefreshLayout.setLoading(false);
				}
//				if (isRememberPosition) {
//					goodsListView.getRefreshableView().setSelectionFromTop(index, top);
//				}
				
				totalPage = ju.getInt(Constants.PAGE_SIZE);
				if (currentPage > totalPage) {
					goodsListView.setMode(Mode.PULL_FROM_START);
				} else {
					goodsListView.setMode(Mode.BOTH);
				}
			}
			
			@Override
			public void onFail(Exception e) {
				goodsListView.onRefreshComplete();
//				if (isRefresh) {
//					swipeRefreshLayout.setRefreshing(false);
//				}else{
//					swipeRefreshLayout.setLoading(false);
//				}
				ToastUtil.showShortToast(GoodsListWithImageActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}, false).execute();
	}
//
//	@Override
//	public void onLoad() {
//		getMoreData(false, false);
//	}
//
//	@Override
//	public void onRefresh() {
//	}
}
