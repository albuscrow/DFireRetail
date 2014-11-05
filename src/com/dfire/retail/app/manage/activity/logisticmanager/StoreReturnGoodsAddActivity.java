package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.vo.ReturnGoodsDetailVo;
import com.dfire.retail.app.manage.vo.supplyManageVo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 物流管理
 * 
 * 
 */
public class StoreReturnGoodsAddActivity extends TitleActivity implements OnClickListener {

	ImageButton title_left;
	ImageButton title_right;
	RelativeLayout add_layout;
	private TextView txt_supplier, total_num, total_price;
	private List<supplyManageVo> supplyManageVoList;
	private LinearLayout store_collect_add_layout;
	private SearchGoodsVo searchGoodsVo;
	private LayoutInflater inflater;
	private RetailApplication rapp;
	private List<ReturnGoodsDetailVo> list = new ArrayList<ReturnGoodsDetailVo>();
	private int num;
	private double price;
	private RelativeLayout total_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_return_goods_add);
		this.inflater = LayoutInflater.from(this);
//		change2saveFinishMode();
		change2saveMode();
		findView();
	}

	public void findView() {
		title_right = (ImageButton) findViewById(R.id.title_right);
		title_right.setOnClickListener(this);
		title_left = (ImageButton) findViewById(R.id.title_left);
		title_left.setOnClickListener(this);
		
		
		add_layout = (RelativeLayout) findViewById(R.id.add_layout);
		add_layout.setOnClickListener(this);
		txt_supplier = (TextView) findViewById(R.id.txt_supplier);
		store_collect_add_layout = (LinearLayout) findViewById(R.id.store_collect_add_layout);

		total_num = (TextView) findViewById(R.id.total_num);
		total_price = (TextView) findViewById(R.id.total_price);
		total_layout = (RelativeLayout) findViewById(R.id.total_layout);
		initView();
	}

	private void initView() {

		initData();
		rapp = (RetailApplication) getApplication();
		HashMap objList = rapp.getObjectMap();
		list = (List<ReturnGoodsDetailVo>) objList.get("returnGoods");
		addItemView(list);
		if (list == null || list.isEmpty()) {
			total_layout.setVisibility(View.INVISIBLE);
		}

	}

	private void initData() {

		// 传递请求参数
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.SUPPLY_INFO_MANAGE_LIST);
		parameters.setParam("showEntityFlg", "1");
		parameters.setParam("currentPage", "1");

		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				JsonObject object = new JsonParser().parse(str).getAsJsonObject();
				supplyManageVoList = new ArrayList<supplyManageVo>();

				supplyManageVoList.clear();
				supplyManageVoList = getJson(str);

				txt_supplier.setText(supplyManageVoList.get(0).getName());

				Message msg = new Message();
				if (CommonUtils.isResuestSucess(str)) {
					msg.what = Constants.HANDLER_SUCESS;
					msg.obj = str;
				} else {
					msg.what = Constants.HANDLER_ERROR;
					msg.obj = CommonUtils.getUMFailMsg(getBaseContext(), str);
				}

			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();

	}

	private void addItemView(List<ReturnGoodsDetailVo> list) {

		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); ++i) {

				StoreReturnGoodsDetailItem storeReturnGoodsDetailItem = new StoreReturnGoodsDetailItem(StoreReturnGoodsAddActivity.this, inflater);

				storeReturnGoodsDetailItem.initWithAppInfo((ReturnGoodsDetailVo) list.get(i),i);

				store_collect_add_layout.addView(storeReturnGoodsDetailItem.getItemView());

				num = list.get(i).getGoodsSum();
//				price = new BigDecimal()  * list.get(i).getGoodsPrice();
			}

			++num;
			++price;

		}
		total_num.setText(num - 1 + "");
		total_price.setText(price - 1 + "");

	}

	public static List<supplyManageVo> getJson(String json) {

		List<supplyManageVo> list = new ArrayList<supplyManageVo>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj != null && jsonObj.length() > 0) {
				JSONArray jsonArray = jsonObj.getJSONArray("supplyManageList");
				for (int i = 0; i < jsonArray.length(); i++) {
					supplyManageVo supplyManagevo = new supplyManageVo();
					JSONObject js = jsonArray.getJSONObject(i);
					supplyManagevo.setName(js.getString("name"));
					supplyManagevo.setId(js.getString("id"));
					list.add(supplyManagevo);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	protected void onResume() {
		super.onResume();

	}
	/**
	 * 移除item
	 */
	public void removeView(StoreReturnGoodsDetailItem goodsItem){
		store_collect_add_layout.removeView(goodsItem.getItemView());
		ReturnGoodsDetailVo detailVo = goodsItem.getReturnGoodsDetailVo();
		if (detailVo.getGoodsId()!=null&&!detailVo.getGoodsId().equals("")) {
			list.get(goodsItem.getListIndex()).setOperateType("del");
		}
		list.get(goodsItem.getListIndex()).setGoodsSum(0);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			Intent addGoods1 = new Intent(StoreReturnGoodsAddActivity.this, StoreOrderAddGoodsActivity.class);
			startActivity(addGoods1);
			finish();
			break;

		case R.id.title_right:

			saveReturnGoodsList();

			break;

		case R.id.add_layout:
			Intent addGoods = new Intent(StoreReturnGoodsAddActivity.this, StoreOrderAddGoodsActivity.class);
			addGoods.putExtra("flag", "returnGoodsReason");
			startActivity(addGoods);
			finish();
			break;
		}
	}

	private void saveReturnGoodsList() {

		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.PURCHASE_SAVE);

		parameters.setParam("shopId", rapp.getShopVo().getShopId());
		parameters.setParam("operateType", "add");
		try {
			parameters.setParam("returnGoodsDetailList", new JSONArray(new Gson().toJson(list)));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		parameters.setParam("lastVer", rapp.getShopVo().getLastVer());
		parameters.setParam("supplyId", supplyManageVoList.get(0).getId());

		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				String returnGoodsId = (String) ju.get("returnGoodsId", String.class);

				Intent listGoods = new Intent(StoreReturnGoodsAddActivity.this, StoreReturnGoodsActivity.class);
				startActivity(listGoods);
				finish();
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();

	}
}