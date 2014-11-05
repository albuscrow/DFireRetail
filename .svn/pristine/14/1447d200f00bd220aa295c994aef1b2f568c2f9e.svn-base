/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemTextView;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.login.LoginActivity;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.vo.allocateDetailVo;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * @author 李锦运 2014-10-27
 */
public class StoreAllocationAddActivity extends TitleActivity implements OnClickListener, IItemListListener, IOnItemSelectListener {

	private LayoutInflater inflater;

	private ImageButton title_right;

	private RelativeLayout add_layout;

	private ItemTextView shop_name;

	private RetailApplication application;

	private List<AllShopVo> shopList = new ArrayList<AllShopVo>();;

	private SpinerPopWindow shopsSpinner;

	private RetailApplication rapp;

	private List<allocateDetailVo> list = new ArrayList<allocateDetailVo>();

	private TextView total_num, total_price;

	private LinearLayout store_collect_add_layout;

	private int num = 0;

	private double price = 0;

	private ItemEditList ls_shop_child;

	private String inShopId;

	private String shopName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_allocation_add);
		this.inflater = LayoutInflater.from(this);
		this.application = (RetailApplication) getApplication();

		change2saveFinishMode();
		findView();

	}

	private void addItemView(List<allocateDetailVo> list) {

		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); ++i) {

				StoreAllocationDetailItem storeReturnGoodsDetailItem = new StoreAllocationDetailItem(StoreAllocationAddActivity.this, inflater);

				storeReturnGoodsDetailItem.initWithAppInfo((allocateDetailVo) list.get(i));

				store_collect_add_layout.addView(storeReturnGoodsDetailItem.getItemView());

				num = list.get(i).getGoodsSum();

//				price = list.get(i).getGoodsSum() * list.get(i).getGoodsPrice();
				++num;
				++price;
			}

		}
		total_num.setText(num - 1 + "");
//		total_price.setText(price - 1 + "");

	}

	private void findView() {
		title_right = (ImageButton) findViewById(R.id.title_right);
		title_right.setOnClickListener(this);
		add_layout = (RelativeLayout) findViewById(R.id.add_layout);
		add_layout.setOnClickListener(this);
		shop_name = (ItemTextView) findViewById(R.id.shop_name);
		ls_shop_child = (ItemEditList) findViewById(R.id.ls_shop_child);
		ls_shop_child.initLabel("调入门店", null, Boolean.TRUE, this);

		shop_name.initLabel("调出门店", null);
		shop_name.initData(application.getmShopInfo().getShopName(), application.getmShopInfo().getShopName());

		store_collect_add_layout = (LinearLayout) findViewById(R.id.store_collect_add_layout);
		total_num = (TextView) findViewById(R.id.total_num);
		total_price = (TextView) findViewById(R.id.total_price);

		initViewData();

		rapp = (RetailApplication) getApplication();
		HashMap objList = rapp.getObjectMap();
		if (objList != null && !objList.isEmpty()) {
			list = (List<allocateDetailVo>) objList.get("allocationAdd");
			addItemView(list);
		}
	}

	private void initViewData() {

		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SHOPLIST_BY_ENTITYID);
		params.setParam(Constants.PAGE, 1);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				JsonUtil ju = new JsonUtil(str);
				List<AllShopVo> shopVoList = (List<AllShopVo>) ju.get("shopList", new TypeToken<List<AllShopVo>>() {
				}.getType());

				if (shopVoList != null && shopVoList.size() > 0) {
					ls_shop_child.initData(shopVoList.get(0).getShopName(), shopVoList.get(0).getShopName());
					shopName=shopVoList.get(0).getShopName();
					List<String> strList = new ArrayList<String>();
					for (AllShopVo dic : shopVoList) {
						strList.add(dic.getShopName());
						shopList.add(dic);
					}
					shopsSpinner = new SpinerPopWindow(StoreAllocationAddActivity.this);
					shopsSpinner.refreshData(strList, 0);
					shopsSpinner.setItemListener(StoreAllocationAddActivity.this);
				}

				if (shopVoList != null) {
					initData(shopVoList.get(0).getShopId());
				}
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}).execute();

	}

	private void initData(String str) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:

			break;

		case R.id.title_right:

			saveAllocateList();

			break;

		case R.id.add_layout:
			Intent allocation = new Intent(StoreAllocationAddActivity.this, StoreOrderAddGoodsActivity.class);
			allocation.putExtra("flag", "allocationAdd");
			startActivity(allocation);
			break;
		}

	}

	private void saveAllocateList() {
		if(application.getmShopInfo().getShopName().equals(shopName)){
			Toast.makeText(StoreAllocationAddActivity.this, "调入门店和调出门店不能相同",
					Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.ALLOCATE_SAVE);
		parameters.setParam("shopId", application.getShopVo().getShopId());
		parameters.setParam("operateType", "add");

		try {
			parameters.setParam("allocateDetailList", new JSONArray(new Gson().toJson(list)));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		parameters.setParam("lastVer", application.getShopVo().getLastVer());
		parameters.setParam("inShopId", inShopId);
//		parameters.setParam("outShopId", application.getShopVo().getShopId());

		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				JsonUtil ju = new JsonUtil(str);

				Intent application = new Intent(StoreAllocationAddActivity.this, StoreAllocationActivity.class);
				startActivity(application);

			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.
	 * IOnItemSelectListener#onItemClick(int)
	 */
	@Override
	public void onItemClick(int pos) {
		String vo = shopList.get(pos).getShopName();
		shopName=shopList.get(pos).getShopName();
		this.inShopId = shopList.get(pos).getShopId();
		ls_shop_child.initData(vo, vo);
		initData(shopList.get(pos).getShopId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dfire.retail.app.common.item.listener.IItemListListener#onItemListClick
	 * (com.dfire.retail.app.common.item.ItemEditList)
	 */
	@Override
	public void onItemListClick(ItemEditList obj) {
		if (obj.getId() == R.id.ls_shop_child) {

			shopsSpinner.setWidth(ls_shop_child.getWidth());
			shopsSpinner.showAsDropDown(ls_shop_child);
			// // 页面跳转从此添加 代码.

		}
	}

}
