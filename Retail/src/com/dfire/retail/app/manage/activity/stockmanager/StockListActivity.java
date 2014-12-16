
/************************* 版权声明 **********************************
 * 版权所有：Copyright (c) 2008, 2014 张向华
 *
 * 工程名称：	Retail
 * 创建者：	zxh 创建日期： 2014年10月20日
 * 创建记录：	创建类结构。
 *
 * ************************* 变更记录 ********************************
 * 修改者： 
 * 修改日期：
 * 修改记录：
 *
 * 
 * ......************************* To Do List*****************************
 * 
 *
 * Suberversion 信息
 * ID:			$Id$
 * 源代码URL：	$HeadURL$
 * 最后修改者：	$LastChangedBy$
 * 最后修改日期：	$LastChangedDate$
 * 最新版本：		$LastChangedRevision$
 **/

package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 库存盘点管理.
 * 
 * @author <a href="mailto:zxh1000@163.com">张向华</a>.
 * @version $Revision: 1570 $
 */
public class StockListActivity extends TitleActivity implements
		OnClickListener, IItemListListener, IOnItemSelectListener {

	/**
	 * <code>从盘点人员:其他的盘点人员</code>.
	 */
	private static final int STOCK_SECOND_USER = 4;
	/**
	 * <code>主盘点人员:第一个创建盘存单的人员</code>.
	 */
	private static final int STOCK_MAIN_USER = 3;
	/**
	 * <code>盘存还未开始</code>.
	 */
	private static final int STOCK_UNSTART = 2;
	/**
	 * <code>查店的过程，需要隐藏所有的按钮</code>.
	 */
	private static final int BUTTON_HIDEN = 1;
	private ItemEditList lsShop; // 下拉店
	private Button btnStart; // 开始盘点
	private Button btnContinue; // 继续盘点
	private Button btnReport; // 汇总盘点结果报告.
	private Button btnCancel; // 取消汇总.
	private Button btnFinish; // 完成盘点.

	private ShopVo currentShop; // 当前店.
	private int currentPage = Constants.PAGE_SIZE_OFFSET;
	private SpinerPopWindow shopsSpinner;
	private ArrayList<ShopVo> shops;
	private ArrayList<String> shopsStr;
	
	
	private String stockCheckId;  //盘存单Id.
	

	/** {@inheritDoc} */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_list);
		setTitleText("库存盘点");
		showBackbtn();
		findView();
		initView();
	}

	/**
	 * 初始化界面.
	 */
	public void findView() {
		lsShop = (ItemEditList) findViewById(R.id.lsShop);
		btnStart = (Button) findViewById(R.id.btnStart);
		btnContinue = (Button) findViewById(R.id.btnContinue);
		btnReport = (Button) findViewById(R.id.btnReport);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnFinish = (Button) findViewById(R.id.btnFinish);

	}

	private void initView() {
		lsShop.initLabel("门店", null, this);
		btnStart.setOnClickListener(this);
		btnContinue.setOnClickListener(this);
		btnReport.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnFinish.setOnClickListener(this);
		initShopInfo();

	}

	private void initShopInfo() {
		currentShop = RetailApplication.getShopVo();
		getShop(true);
	}

	/**
	 * 初始按钮 .
	 */
	private void initUIButton(int status) {
		if (BUTTON_HIDEN == status) { // 中间状态时，隐藏所有按钮
			hideAllBtn();
		} else if (STOCK_UNSTART == status) { // 未开始盘点单.
			hideAllBtn();
			btnStart.setVisibility(View.VISIBLE);
		} else if (STOCK_MAIN_USER == status) { // 进入主盘点人员
			hideAllBtn();
			btnContinue.setVisibility(View.VISIBLE);
			btnReport.setVisibility(View.VISIBLE);
			btnCancel.setVisibility(View.VISIBLE);
			btnFinish.setVisibility(View.VISIBLE);
		} else if (STOCK_SECOND_USER == status) { // 进入非主盘点人员
			hideAllBtn();
			btnContinue.setVisibility(View.VISIBLE);
			btnReport.setVisibility(View.VISIBLE);
		}
	}

	private void hideAllBtn() {
		btnStart.setVisibility(View.GONE);
		btnContinue.setVisibility(View.GONE);
		btnReport.setVisibility(View.GONE);
		btnCancel.setVisibility(View.GONE);
		btnFinish.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnStart) { // 开始盘点.
			startStockEvent();
		} else if (v.getId() == R.id.btnContinue) { // 继续盘点.
			startAddActivity();
		} else if (v.getId() == R.id.btnReport) { // 汇总盘点结果报告.

		} else if (v.getId() == R.id.btnCancel) { // 取消盘点

		} else if (v.getId() == R.id.btnFinish) { // 完成盘点.

		}

	}

	private void startAddActivity() {
		Intent addGoods = new Intent(StockListActivity.this,
				StoreCheckAddActivity.class);
		addGoods.putExtra("add", 1);
//		addGoods.putExtra("stockCheckId", stockCheckId);
		startActivity(addGoods);
	}

	/**
	 * 开始盘点.
	 */
	private void startStockEvent() {
		RequestParameter params = new RequestParameter(true);
		//params.setUrl(Constants.CHECK_STOCK_START_URL);
		params.setParam(Constants.SHOP_ID, RetailApplication.getmShopInfo()
				.getShopId());
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null
						|| !returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(StockListActivity.this,
							Constants.getErrorInf(null, null));
					return;
				}
				// 待定.
//				jsonElement = jo.get(Constants.STOCKCHECKID);
				if (jsonElement != null) {
					stockCheckId = jsonElement.getAsString();
					startAddActivity();
				}
			}

			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(StockListActivity.this,
						Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();

	}

	/** {@inheritDoc} */
	@Override
	public void onItemListClick(ItemEditList obj) {
		if (obj.getId() == R.id.lsShop) {
			shopsSpinner.setWidth(lsShop.getWidth());
			shopsSpinner.showAsDropDown(lsShop);
		}
	}

	@Override
	public void onItemClick(int pos) {
		currentShop = shops.get(pos);
		lsShop.initData(currentShop.getShopName(), currentShop.getShopId());
		getHashStoreQuery(RetailApplication.getmShopInfo().getShopId());
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	/**
	 * 查找门店.
	 * 
	 * @param isInit
	 */
	private void getShop(final boolean isInit) {
		if (isInit) {
			currentPage = Constants.PAGE_SIZE_OFFSET;
		}
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SHOP_LIST_URL);
		params.setParam(Constants.SHOP_ID, RetailApplication.getmShopInfo()
				.getShopId());
		params.setParam(Constants.PAGE, currentPage++);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null
						|| !returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(StockListActivity.this,
							Constants.getErrorInf(null, null));
					return;
				}
				if (isInit) {
					if (shops == null) {
						shops = new ArrayList<ShopVo>();
					}
					shops.clear();
					shops.add(currentShop);
				}
				shops.addAll((Collection<? extends ShopVo>) new Gson()
						.fromJson(jo.get("shopList"),
								new TypeToken<List<ShopVo>>() {
								}.getType()));
				int pageSize = jo.get(Constants.PAGE_SIZE).getAsInt();
				if (pageSize >= currentPage) {
					getShop(false);
				} else {
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
					shopsSpinner = new SpinerPopWindow(StockListActivity.this);
					shopsSpinner.refreshData(shopsStr, 0);
					lsShop.initData(currentShop.getShopName(),
							currentShop.getShopId());
					shopsSpinner.setItemListener(StockListActivity.this);
					initUIButton(BUTTON_HIDEN);
					getHashStoreQuery(RetailApplication.getmShopInfo()
							.getShopId());
				}

			}

			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(StockListActivity.this,
						Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}

	/**
	 * 查询是否盘点状态，返回主盘点员。
	 * 
	 * @param shopId
	 *            餐店Id.
	 */
	private void getHashStoreQuery(final String shopId) {
		RequestParameter params = new RequestParameter(true);
		//params.setUrl(Constants.CHECK_STOCK_STATUS_URL);
		params.setParam(Constants.SHOP_ID, RetailApplication.getmShopInfo().getShopId());
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null
						|| !returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(StockListActivity.this,
							Constants.getErrorInf(null, null));
					return;
				}
				//待定.
				String opUserId=null;
//				jsonElement = jo.get(Constants.OPUSERID);
				if(jsonElement==null){
					ToastUtil.showShortToast(StockListActivity.this,"服务器返回数据异常");
				}
				opUserId=jsonElement.getAsString();
				String userId=RetailApplication.getmUserInfo().getUserId();
				if(StringUtils.isEmpty(opUserId)){
					initUIButton(STOCK_UNSTART);
				}else if(opUserId.equals(userId)){
					initUIButton(STOCK_MAIN_USER);
				}else{
					initUIButton(STOCK_SECOND_USER);
				}
//				jsonElement = jo.get(Constants.STOCKCHECKID);
				if(jsonElement==null){
					ToastUtil.showShortToast(StockListActivity.this,"服务器返回数据异常");
				}
				stockCheckId=jsonElement.getAsString();
			}

			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(StockListActivity.this,
						Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}
}
