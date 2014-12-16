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

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.goodsmanager.Category;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsManagerBaseActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsSortListActivity;
import com.dfire.retail.app.manage.adapter.GoodsSortListForMenuAdapter;
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
import com.dfire.retail.app.zxing.MipcaActivityCapture;

/**
 * 库存管理-提醒设置
 * 
 * @author ys
 */
public class StockRemindSettingGoodsActivity extends GoodsManagerBaseActivity implements OnClickListener, OnItemClickListener {
	
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
		shopTextView = (TextView) findViewById(R.id.shop_name);
		
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
		
		getCategoryList();
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
					sortList.setOnItemClickListener(StockRemindSettingGoodsActivity.this);
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
			search();
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
			}
		}else if (resultCode==RESULT_OK) {
			String findParameter = data.getStringExtra(Constants.DEVICE_CODE);
			((TextView)findViewById(R.id.code)).setText(findParameter);
			search();
		}
	}
	
	/**
	 * Search.得到搜索控件
	 */
	private void search() {
		String codeStr = code.getText().toString();
		search(codeStr, false);
	}

	/**
	 * 搜索店铺下面的所有商品
	 */
	private void search(final String codeStr, final boolean isScan) {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockInfoAlert/getGoodsList");
		params.setParam(Constants.SHOP_ID, shopId);
		params.setParam(Constants.SEARCH_CODE, codeStr);
		params.setParam(Constants.PAGE, currentPage);
		new AsyncHttpPost(this, params, StockInfoAlertGoodsListBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				StockInfoAlertGoodsListBo bo = (StockInfoAlertGoodsListBo)oj;
				if (bo!=null) {
					ArrayList<GoodsVo> goods = (ArrayList<GoodsVo>) bo.getGoodsList();
					if (goods.size() > 0) {
						startActivity(new Intent(StockRemindSettingGoodsActivity.this, StockRemindSettingGoodsListActivity.class)
						.putExtra(Constants.GOODS, goods)
						.putExtra(Constants.PAGE_SIZE,bo.getPageCount())
						.putExtra(Constants.SHOP_ID, shopId)
						.putExtra(Constants.CATEGORY, categorys)
						.putExtra(Constants.SEARCH_CODE, codeStr)
						);
					}
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position,
			long id) {
		/**
		 * 点击分类  查询商品
		 */
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockInfoAlert/getGoodsList");
		params.setParam(Constants.SHOP_ID,shopId);
		params.setParam(Constants.CATEGORY_ID, categorys.get(position).id);
		params.setParam(Constants.PAGE, currentPage);
		new AsyncHttpPost(this, params, StockInfoAlertGoodsListBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				StockInfoAlertGoodsListBo bo = (StockInfoAlertGoodsListBo)oj;
				if (bo!=null) {
					ArrayList<GoodsVo> goods = (ArrayList<GoodsVo>) bo.getGoodsList();
					if (goods.size() > 0) {
						startActivity(new Intent(StockRemindSettingGoodsActivity.this, StockRemindSettingGoodsListActivity.class)
						.putExtra(Constants.GOODS, goods)
						.putExtra(Constants.PAGE_SIZE,bo.getPageCount())
						.putExtra(Constants.SHOP_ID, shopId)
						.putExtra(Constants.CATEGORY, categorys)
						.putExtra(Constants.CATEGORY_ID,categorys.get(position).id)
						);
					}
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
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
