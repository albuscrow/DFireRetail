/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.item.DateUtil;
import com.dfire.retail.app.common.item.ItemTextView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.vo.ReturnGoodsDetailVo;
import com.dfire.retail.app.manage.vo.ReturnGoodsVo;
import com.google.gson.reflect.TypeToken;

/**
 * @author 李锦运 2014-10-23
 */
public class StoreReturnGoodsListOfCompanyDetailActivity extends TitleActivity implements OnClickListener {

	private ReturnGoodsVo returnGoodsVo;

	private ItemTextView lblName, lblNo;

	private TextView prict_total, num_total;

	private LayoutInflater inflater;

	private LinearLayout store_collect_add_layout, btn_layout;

	private Button confirm_btn, refuse_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_return_goods_list_of_company_detail);
		setTitleText("门店退货");
		this.inflater = LayoutInflater.from(this);
		showBackbtn();
		initView();

	}

	private void initView() {

		lblName = (ItemTextView) this.findViewById(R.id.lblName);
		lblNo = (ItemTextView) this.findViewById(R.id.lblNo);

		prict_total = (TextView) this.findViewById(R.id.prict_total);
		num_total = (TextView) this.findViewById(R.id.num_total);
		store_collect_add_layout = (LinearLayout) findViewById(R.id.store_collect_add_layout);

		this.btn_layout = (LinearLayout) this.findViewById(R.id.btn_layout);

		confirm_btn = (Button) this.findViewById(R.id.confirm_btn);

		refuse_btn = (Button) this.findViewById(R.id.refuse_btn);

		confirm_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		refuse_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		if (((RetailApplication) getApplication()).getmShopInfo().getParentId() == null) {

			btn_layout.setVisibility(View.VISIBLE);

		} else {

			btn_layout.setVisibility(View.GONE);

		}

		initData();

	}

	private void initData() {
		RetailApplication application = (RetailApplication) getApplication();
		HashMap<String, Object> objMap = application.getObjMap();
		returnGoodsVo = (ReturnGoodsVo) objMap.get("returnGoodsVo");

		loadData();

	}

	private void loadData() {

		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.RETURNGOODS_DETAIL);
		parameters.setParam("returnGoodsId", returnGoodsVo.getReturnGoodsId());

		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				JsonUtil ju = new JsonUtil(str);
				String returnGoodsNo = (String) ju.get("returnGoodsNo", String.class);
				String supplyName = (String) ju.get("supplyName", String.class);
				String goodsTotalSum = (String) ju.get("goodsTotalSum", String.class);

				String goodsTotalPrice = (String) ju.get("goodsTotalPrice", String.class);

				List<ReturnGoodsDetailVo> returnGoodsDetailList = (List<ReturnGoodsDetailVo>) ju.get("returnGoodsDetailList", new TypeToken<List<ReturnGoodsDetailVo>>() {
				}.getType());

				lblName.initLabel("退货单号", null);
				lblName.initData(returnGoodsNo, returnGoodsNo);

				lblNo.initLabel("退货退货日期", null);
				lblNo.initData(DateUtil.timeToStrYMD_EN(returnGoodsVo.getSendEndTime()), DateUtil.timeToStrYMD_EN(returnGoodsVo.getSendEndTime()));

				num_total.setText(goodsTotalSum);
				prict_total.setText(goodsTotalPrice);

				if (returnGoodsDetailList != null && returnGoodsDetailList.size() != 0) {
					for (int i = 0; i < returnGoodsDetailList.size(); ++i) {

						StoreReturnGoodsListDetailItem storeReturnGoodsListDetailItem = new StoreReturnGoodsListDetailItem(StoreReturnGoodsListOfCompanyDetailActivity.this, inflater);

						storeReturnGoodsListDetailItem.initWithAppInfo2((ReturnGoodsDetailVo) returnGoodsDetailList.get(i));

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

	public static List<ReturnGoodsVo> getJson(String json) {

		List<ReturnGoodsVo> list = new ArrayList<ReturnGoodsVo>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj != null && jsonObj.length() > 0) {
				JSONArray jsonArray = jsonObj.getJSONArray("returnGoodsList");
				for (int i = 0; i < jsonArray.length(); i++) {
					ReturnGoodsVo returnGoodsVo = new ReturnGoodsVo();
					JSONObject js = jsonArray.getJSONObject(i);
					returnGoodsVo.setReturnGoodsId(js.getString("returnGoodsId"));
					returnGoodsVo.setReturnGoodsNo(js.getString("returnGoodsNo"));
					returnGoodsVo.setSendEndTime(js.getLong("sendEndTime"));
					returnGoodsVo.setBillStatus(js.getInt("billStatus"));
					returnGoodsVo.setBillStatusName(js.getString("billStatusName"));
					returnGoodsVo.setGoodsTotalSum(js.getInt("goodsTotalSum"));
					returnGoodsVo.setReturnGoodsId(js.getString("returnGoodsId"));
					returnGoodsVo.setGoodsTotalPrice(js.getInt("goodsTotalPrice"));
					returnGoodsVo.setSupplyId(js.getString("supplyId"));
					returnGoodsVo.setSupplyName(js.getString("supplyName"));
					// supplyManagevo.setName(js.getString("name"));
					// supplyManagevo.setId(js.getString("id"));
					list.add(returnGoodsVo);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
