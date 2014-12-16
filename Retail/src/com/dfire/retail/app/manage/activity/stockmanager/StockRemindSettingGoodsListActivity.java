package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.content.Intent;
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
import com.dfire.retail.app.manage.activity.CaptureActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.Category;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsManagerBaseActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsSortListActivity;
import com.dfire.retail.app.manage.adapter.GoodsSortListForMenuAdapter;
import com.dfire.retail.app.manage.adapter.StockRemindSettingGoodsAdapter;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.GoodsVo;
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
 * 选择商品
 * 
 * @author ys
 */
public class StockRemindSettingGoodsListActivity extends
		GoodsManagerBaseActivity implements OnItemClickListener,
		OnClickListener{

	/** 商品list */
	private ArrayList<GoodsVo> goods;
	/** 店铺id */
	private String shopId;
	/** 适配器 */
	private StockRemindSettingGoodsAdapter adapter;
	/** 页数 */
	private int currentPage = 1;
	/** 类别id */
	private String categoryId;
	/** 查询条件 ，简码 拼音码 条形码 */
	private String searchCode;
	/** PullToRefreshListView goodsList */
	private PullToRefreshListView goodsListView;
	/** 总页数 */
	private int totalPage;
	/** 右边的类别目录 */
	private MenuDrawer mMenu;
	/** 类别listView */
	private ListView sortList;
	/** 类别数据 list */
	private ArrayList<Category> categorys;
	/** 类别适配器 */
	private GoodsSortListForMenuAdapter categoryAdapter;
	
	private Integer pageSize = 0;
	
	private EditText code;
	
	private ArrayList<GoodsVo> mGoodsVos;
	
	private int mode;// 判断异步执行完是否禁用加载更多
	
	private String categoryName;
	
	private ImageView clear_input;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 分类
		mMenu = MenuDrawer.attach(this, Position.RIGHT);
		mMenu.setContentView(R.layout.goods_manager_title_layout);
		mMenu.setMenuView(R.layout.activity_goods_sort_menu);
		mMenu.findViewById(R.id.fenlei).setOnClickListener(this);
		sortList = ((ListView) mMenu.findViewById(R.id.goods_sort_list));
		categorys = (ArrayList<Category>) getIntent().getSerializableExtra(Constants.CATEGORY);
		categoryAdapter = new GoodsSortListForMenuAdapter(StockRemindSettingGoodsListActivity.this, categorys);
		addFooter(sortList);
		sortList.setAdapter(categoryAdapter);
		// 分类点击 查询商品
		sortList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentPage = 1;
				mGoodsVos.clear();
				categoryId = categorys.get(position).id;   
				categoryName = categorys.get(position).name;
				RequestParameter params = new RequestParameter(true);
				params.setUrl(Constants.BASE_URL+"stockInfoAlert/getGoodsList");
				params.setParam(Constants.SHOP_ID, shopId);
				params.setParam(Constants.CATEGORY_ID,categorys.get(position).id);
				params.setParam(Constants.PAGE, currentPage);
				new AsyncHttpPost(StockRemindSettingGoodsListActivity.this, params, StockInfoAlertGoodsListBo.class, new RequestCallback() {
					@Override
					public void onSuccess(Object oj) {
						goodsListView.onRefreshComplete();
						StockInfoAlertGoodsListBo bo = (StockInfoAlertGoodsListBo)oj;
						if (bo!=null) {
							ArrayList<GoodsVo> goods = (ArrayList<GoodsVo>) bo.getGoodsList();
							if (goods == null || goods.size() == 0) {
								new ErrDialog(StockRemindSettingGoodsListActivity.this, getResources().getString(R.string.the_kind_not_goods)).show();
								return;
							} else {
								if (currentPage>pageSize){
									goodsListView.setMode(Mode.DISABLED);
								}
							mGoodsVos.addAll(goods);
							adapter.notifyDataSetChanged();
							mMenu.toggleMenu();
						}}
					}
					@Override
					public void onFail(Exception e) {
						goodsListView.onRefreshComplete();
					}
				}).execute();
			}
		});

		FrameLayout body = (FrameLayout) mMenu.findViewById(R.id.body);
		body.requestFocus();
		getLayoutInflater().inflate(R.layout.stock_remind_setting_goods_image,body, true);
		findViewById(R.id.title_left).setOnClickListener(this);
		findViewById(R.id.title_right).setOnClickListener(this);
		((TextView) findViewById(R.id.title_text)).setText(Constants.CHOOSE_GOODS);
		mGoodsVos = new ArrayList<GoodsVo>();
		goods = (ArrayList<GoodsVo>) getIntent().getSerializableExtra(Constants.GOODS);
		shopId = getIntent().getStringExtra(Constants.SHOP_ID);
		categoryId = getIntent().getStringExtra(Constants.CATEGORY_ID);
		searchCode = getIntent().getStringExtra(Constants.SEARCH_CODE);
		totalPage = getIntent().getIntExtra(Constants.PAGE_SIZE,Constants.INVALID_INT);
		pageSize = totalPage;
		if (searchCode!=null) {
			((EditText) findViewById(R.id.code)).setText(searchCode);
		}
		goodsListView = (PullToRefreshListView) findViewById(R.id.goodsList);
		goodsListView.setMode(Mode.BOTH);
		goodsListView.setRefreshing(false);
		goodsListView.setOnItemClickListener(this);
		new ListAddFooterItem(this, goodsListView.getRefreshableView());
		if (goods!=null) {
			mGoodsVos.addAll(goods);
			goodsListView.onRefreshComplete();
		}
		adapter = new StockRemindSettingGoodsAdapter(this, mGoodsVos);
		goodsListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		// 列表刷新和加载更多操作
		goodsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(StockRemindSettingGoodsListActivity.this, System.currentTimeMillis(), 
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
				String label = DateUtils.formatDateTime(StockRemindSettingGoodsListActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				search();
			}
		});	
		findViewById(R.id.scan).setOnClickListener(this);
		findViewById(R.id.more).setOnClickListener(this);
		findViewById(R.id.scanButton).setOnClickListener(this);
		findViewById(R.id.search).setOnClickListener(this);
		code = (EditText) findViewById(R.id.code);
		clear_input = (ImageView) findViewById(R.id.clear_input);
		clear_input.setOnClickListener(this);
		setBack();
		setRightBtn(R.drawable.ico_cate, Constants.CATEGORY_TEXT);
		
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
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 点击按钮
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more:
			startActivity(new Intent(this, StockRemindGoodsListActivity.class)
					.putExtra(Constants.GOODS, mGoodsVos)
					.putExtra(Constants.SHOP_ID, shopId)
					.putExtra("categoryName", categoryName));
			break;
		case R.id.scanButton:
			startActivityForResult(new Intent(this, CaptureActivity.class),
					10086);
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
		case R.id.scan:
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.FOR_GET);
			break;
		case R.id.search:
			reFreshing();
			break;
		case R.id.clear_input:
			code.setText("");
			clear_input.setVisibility(View.GONE);
		break;
		default:
			break;
		}
	}

	/**
	 * 键盘返回事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK&& (mMenu.getDrawerState() == MenuDrawer.STATE_OPEN || mMenu.getDrawerState() == MenuDrawer.STATE_OPENING)) {
			mMenu.closeMenu();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * list itme 点击
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ArrayList<String> ids = new ArrayList<String>();
		ids.add(mGoodsVos.get(position-1).getGoodsId());

		if (ids.size() == 0) {
			ToastUtil.showShortToast(this, Constants.CHOOSE_SOMETHING);
		} else {
			startActivity(new Intent(this,
					StockRemindGoodsSaveSettingActivity.class)
					.putExtra(Constants.GOODS, mGoodsVos)
					.putExtra(Constants.GOODSIDS, ids)
					.putExtra(Constants.SHOP_ID, shopId)
					.putExtra("activity", "stockRemindSettingGoodsListActivity"));
		}
	}

	/**
	 * 回调
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK) {
			String findParameter = data.getStringExtra(Constants.DEVICE_CODE);
			code.setText(findParameter);
			reFreshing();
		}
	}
	/**
	 * 加条件以后刷新数据
	 */
	public void reFreshing(){
		currentPage = 1;//选择以后初始化页数
		goodsListView.setMode(Mode.PULL_FROM_START);
		search();
		goodsListView.setRefreshing();
	}
	/**
	 * 搜索
	 * @param code
	 */
	private void search() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockInfoAlert/getGoodsList");
		params.setParam(Constants.SHOP_ID, shopId);
		params.setParam(Constants.SEARCH_CODE, code.getText().toString());
		params.setParam(Constants.PAGE, currentPage);
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
}
