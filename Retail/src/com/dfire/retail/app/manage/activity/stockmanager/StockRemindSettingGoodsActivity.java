package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.goodsmanager.Category;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsManagerBaseActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsSortListActivity;
import com.dfire.retail.app.manage.adapter.GoodsSortListForMenuAdapter;
import com.dfire.retail.app.manage.adapter.StockRemindSettingGoodsAdapter;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.CategoryVo;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.StockCategoryListBo;
import com.dfire.retail.app.manage.data.bo.StockInfoAlertGoodsListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.zxing.MipcaActivityCapture;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/**
 * 库存管理-提醒设置
 * 
 * @author ys
 */
public class StockRemindSettingGoodsActivity extends GoodsManagerBaseActivity implements OnClickListener {
	
	private MenuDrawer mMenu;
	private int currentPage = 1;
	private TextView shopTextView;
	private ListView sortList;
	private ArrayList<Category> categorys;
	private String shopId;//店铺id
	private AllShopVo allShopVo;
	private ShopVo currentShop;
	private ImageView clear_input;
	private EditText code;
	private PullToRefreshListView goodsListView;
	private StockRemindSettingGoodsAdapter adapter;
	private ArrayList<GoodsVo> mGoodsVos;
	private Integer pageSize = 0;
	private int mode;// 判断异步执行完是否禁用加载更多
	private String categoryId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.currentShop = RetailApplication.getShopVo();
		this.shopId = RetailApplication.getShopVo().getShopId();
		super.onCreate(savedInstanceState);
		mMenu = MenuDrawer.attach(this, Position.RIGHT);
		
		mMenu.setContentView(R.layout.goods_manager_title_layout);
		mMenu.setMenuView(R.layout.activity_goods_sort_menu);
		mMenu.findViewById(R.id.fenlei).setOnClickListener(this);
		sortList = ((ListView)mMenu.findViewById(R.id.goods_sort_list));
		addFooter(sortList);
		
		FrameLayout body = (FrameLayout) mMenu.findViewById(R.id.body);
		body.requestFocus();
		getLayoutInflater().inflate(R.layout.activity_stock_remind_setting_goods, body, true);
		clear_input = (ImageView) findViewById(R.id.clear_input);
		clear_input.setOnClickListener(this);
		findViewById(R.id.title_left).setOnClickListener(this);
		findViewById(R.id.title_right).setOnClickListener(this);
		((TextView)findViewById(R.id.title_text)).setText(Constants.REMIND_SETTING);
		findViewById(R.id.search).setOnClickListener(this);
		findViewById(R.id.scan).setOnClickListener(this);
		findViewById(R.id.scanButton).setOnClickListener(this);
		findViewById(R.id.minus).setOnClickListener(this);
		shopTextView = (TextView) findViewById(R.id.shop_name);
		mGoodsVos = new ArrayList<GoodsVo>();
		goodsListView = (PullToRefreshListView) findViewById(R.id.goodsList);
		goodsListView.setMode(Mode.BOTH);
		goodsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ArrayList<String> ids = new ArrayList<String>();
				ids.add(mGoodsVos.get(position-1).getGoodsId());

				if (ids.size() == 0) {
					ToastUtil.showShortToast(StockRemindSettingGoodsActivity.this, Constants.CHOOSE_SOMETHING);
				} else {
					startActivity(new Intent(StockRemindSettingGoodsActivity.this,
							StockRemindGoodsSaveSettingActivity.class)
							.putExtra(Constants.GOODS, mGoodsVos)
							.putExtra(Constants.GOODSIDS, ids)
							.putExtra(Constants.SHOP_ID, shopId)
							.putExtra("activity", "stockRemindSettingGoodsListActivity"));
				}
			}
		});
		new ListAddFooterItem(this, goodsListView.getRefreshableView());
		adapter = new StockRemindSettingGoodsAdapter(this, mGoodsVos);
		goodsListView.setAdapter(adapter);
		
		if (RetailApplication.getEntityModel()==1) {
			//单店
			this.shopTextView.setCompoundDrawables(null, null, null, null);
			this.shopTextView.setText(currentShop.getShopName());
			this.shopTextView.setTextColor(Color.parseColor("#666666"));
		}else {
			//连锁
			if (currentShop.getType() == ShopVo.MENDIAN) {
				this.shopTextView.setCompoundDrawables(null, null, null, null);
				this.shopTextView.setText(currentShop.getShopName());
				this.shopTextView.setTextColor(Color.parseColor("#666666"));
			}else{
				this.shopTextView.setText("请选择");
				this.shopTextView.setOnClickListener(this);
			}
		}
		setBack();
		setRightBtn(R.drawable.ico_cate, Constants.CATEGORY_TEXT);
		code = (EditText)findViewById(R.id.code);
		code.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s!=null&&!s.toString().equals("")) {
					clear_input.setVisibility(View.VISIBLE);
				}else{
					clear_input.setVisibility(View.GONE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		// 列表刷新和加载更多操作
		goodsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StockRemindSettingGoodsActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = 1;//选择以后初始化页数
				search();
			}
			/**
			 * 上拉加载更多
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StockRemindSettingGoodsActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				search();
			}
		});	
		getCategoryList();
		setSearch();
	}
    @Override
    protected void onRestart() {
    	super.onRestart();
    	setSearch();
    }
	
	/**
	 * 分类list
	 * @param parentName
	 * @param categoryVo
	 * @param parent
	 * @param categorys
	 * @param depth
	 */
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
	
	/**
	 * 分类一栏
	 */
	private void getCategoryList() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.CATEGORY_LIST_URL);
		new AsyncHttpPost(this, params, StockCategoryListBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				StockCategoryListBo bo = (StockCategoryListBo)oj;
				if (bo!=null) {
					List<CategoryVo> categoryVo = bo.getCategoryList();
					if (categoryVo == null) {
						categoryVo = new ArrayList<CategoryVo>();
					}
					categorys = new ArrayList<Category>();
					getCategory(Constants.EMPTY_STRING, categoryVo,Constants.EMPTY_STRING,categorys, 0);
					List<String> categorysString = new ArrayList<String>();
					for (Category item : categorys) {
						categorysString.add(item.name);
					}
					sortList.setAdapter(new GoodsSortListForMenuAdapter(StockRemindSettingGoodsActivity.this, categorys));
					sortList.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							categoryId = categorys.get(position).id;
							search();
						}
					});
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}
	/**
	 * 点击事件
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
		case R.id.search:
			categoryId = null;
			setSearch();
			break;
		case R.id.scanButton:
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.FOR_GET);
			break;
		case R.id.scan:
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.FOR_GET);
			break;
			
		case R.id.shop_name:
			Intent selectIntent =new Intent(StockRemindSettingGoodsActivity.this,AdjusSelectShopActivity.class);
			selectIntent.putExtra("selectShopId", shopId);
			startActivityForResult(selectIntent, 100);
			break;
			
		case R.id.fenlei:
			startActivity(new Intent(this, GoodsSortListActivity.class));
			break;
		case R.id.clear_input:
			code.setText("");
			clear_input.setVisibility(View.GONE);
			break;
		case R.id.minus:
			startActivity(new Intent(StockRemindSettingGoodsActivity.this, StockRemindSettingGoodsListActivity.class)
			.putExtra(Constants.SHOP_ID, shopId)
			.putExtra(Constants.CATEGORY, categorys));
			break;
		default:
			break;
		}
	}
	/**
	 * 回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){
		    allShopVo = (AllShopVo)data.getSerializableExtra("shopVo");
			if (allShopVo!=null) {
				this.shopTextView.setText(allShopVo.getShopName());
				this.shopId = allShopVo.getShopId();
				categoryId = null;
				setSearch();
			}
		}else if (resultCode==RESULT_OK) {
			String findParameter = data.getStringExtra(Constants.DEVICE_CODE);
			((TextView)findViewById(R.id.code)).setText(findParameter);
			categoryId = null;
			setSearch();
		}
	}
	
	/**
	 * Search.得到搜索控件
	 */
	private void setSearch() {
		currentPage =  1;
		goodsListView.setMode(Mode.PULL_FROM_START);
		search();
		goodsListView.setRefreshing();
	}

	/**
	 * 搜索店铺下面的所有商品
	 */
	private void search() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockInfoAlert/getGoodsList");
		params.setParam(Constants.SHOP_ID, shopId);
		params.setParam(Constants.SEARCH_CODE, code.getText().toString());
		params.setParam(Constants.PAGE, currentPage);
		params.setParam("showIsSetAlert", "1");
		params.setParam(Constants.CATEGORY_ID, categoryId);

		new AsyncHttpPost(this, params, StockInfoAlertGoodsListBo.class,false, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				StockInfoAlertGoodsListBo bo = (StockInfoAlertGoodsListBo)oj;
				if (bo!=null) {
					pageSize = bo.getPageCount();
					List<GoodsVo> goodsVos = new ArrayList<GoodsVo>();
					goodsVos = bo.getGoodsList();
					
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							mGoodsVos.clear();
						}
						if (goodsVos != null && goodsVos.size() > 0) {
							goodsListView.setMode(Mode.BOTH);
							mGoodsVos.addAll(goodsVos);
						}else {
							mode = 1;
						}
						adapter.notifyDataSetChanged();
					}else {
						mGoodsVos.clear();
						adapter.notifyDataSetChanged();
						mode = 1;
					}
					goodsListView.onRefreshComplete();
					if (mode == 1) {
						goodsListView.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
				}
			}
			@Override
			public void onFail(Exception e) {
				goodsListView.onRefreshComplete();
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
