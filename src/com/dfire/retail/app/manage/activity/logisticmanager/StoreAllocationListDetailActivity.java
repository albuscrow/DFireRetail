/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemTextView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.vo.AllocateVo;
import com.dfire.retail.app.manage.vo.allocateDetailVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author 李锦运 2014-10-28
 */
public class StoreAllocationListDetailActivity extends TitleActivity implements OnClickListener {
	private LayoutInflater inflater;

	private ItemTextView lblName, out_shop, in_shop;

	private LinearLayout store_collect_add_layout;

	private TextView num_total;

	private AllocateVo allocateVo;

	private RetailApplication application;

	private List<allocateDetailVo> allocateDetailList1;
	
	private Button btn_refuse,btn_confirm;

	private String outShopId;
	
	private String inShopId;
	
	private String lastVer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_allocation_list_detail);
		setTitleText("门店调拨");
		this.inflater = LayoutInflater.from(this);
		this.application = (RetailApplication) getApplication();
		showBackbtn();
		initView();
		initData();

	}

	private void initView() {

		lblName = (ItemTextView) this.findViewById(R.id.lblName);
		out_shop = (ItemTextView) this.findViewById(R.id.out_shop);
		in_shop = (ItemTextView) this.findViewById(R.id.in_shop);

		store_collect_add_layout = (LinearLayout) this.findViewById(R.id.store_collect_add_layout);
		num_total = (TextView) this.findViewById(R.id.num_total);
		
		btn_confirm= (Button) this.findViewById(R.id.btn_confirm);
		btn_refuse= (Button) this.findViewById(R.id.btn_refuse);
		btn_confirm.setOnClickListener(this);
		btn_refuse.setOnClickListener(this);
	}

	private void initData() {
		RetailApplication application = (RetailApplication) getApplication();
		HashMap<String, Object> objMap = application.getObjMap();
		allocateVo = (AllocateVo) objMap.get("allocateVo");

		lblName.initLabel("调拨单号", null);
		out_shop.initLabel("调出门店", null);
		in_shop.initLabel("调入门店", null);

		loadData();

	}

	private void loadData() {

		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.ALLOCATE_DETAIL);
		parameters.setParam("allocateId", allocateVo.getAllocateId());

		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				JsonUtil ju = new JsonUtil(str);

				String allocateNo = (String) ju.get("allocateNo", String.class);
				 outShopId = (String) ju.get("outShopId", String.class);
				String outShopName = (String) ju.get("outShopName", String.class);
				 inShopId = (String) ju.get("inShopId", String.class);
				String goodsTotalSum = (String) ju.get("goodsTotalSum", String.class);
				String goodsTotalPrice = (String) ju.get("goodsTotalPrice", String.class);
				String inShopName = (String) ju.get("inShopName", String.class);
				 lastVer=  (String) ju.get("lastVer", String.class);

				
				
				List<allocateDetailVo> allocateDetailList = (List<allocateDetailVo>) ju.get("allocateDetailList", new TypeToken<List<allocateDetailVo>>() {
				}.getType());

				
				allocateDetailList1=allocateDetailList;
				lblName.initData(allocateNo, allocateNo);
				out_shop.initData(outShopName, outShopName);
				in_shop.initData(inShopName, inShopName);

				num_total.setText(goodsTotalSum);

				if (allocateDetailList != null && allocateDetailList.size() != 0) {
					for (int i = 0; i < allocateDetailList.size(); ++i) {

						StoreAllocationListDetailItem storeReturnGoodsListDetailItem = new StoreAllocationListDetailItem(StoreAllocationListDetailActivity.this, inflater);

						storeReturnGoodsListDetailItem.initWithAppInfo((allocateDetailVo) allocateDetailList.get(i));

						store_collect_add_layout.addView(storeReturnGoodsListDetailItem.getItemView());

					}
				}
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_confirm:
			saveConfirmVo();
			break;
		case R.id.btn_refuse:
			saveRefuseVo();
			break;
		}

	}

	private void saveConfirmVo() {
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.ALLOCATE_SAVE);

		parameters.setParam("operateType", "receipt");
		try {
			parameters.setParam("allocateDetailList", new JSONArray(new Gson().toJson(allocateDetailList1)));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		parameters.setParam("lastVer", lastVer);
		parameters.setParam("allocateId", allocateVo.getAllocateId());
		parameters.setParam("inShopId",inShopId);
		parameters.setParam("outShopId",outShopId);
		
		
		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				Intent application = new Intent(StoreAllocationListDetailActivity.this, StoreAllocationActivity.class);
				startActivity(application);

			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();
	}

	private void saveRefuseVo() {
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.ALLOCATE_SAVE);

		parameters.setParam("operateType", "refuse");
		parameters.setParam("lastVer", lastVer);
		parameters.setParam("allocateId",  allocateVo.getAllocateId());
		

		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				Intent application = new Intent(StoreAllocationListDetailActivity.this, StoreAllocationActivity.class);
				startActivity(application);

			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();
	}

}
