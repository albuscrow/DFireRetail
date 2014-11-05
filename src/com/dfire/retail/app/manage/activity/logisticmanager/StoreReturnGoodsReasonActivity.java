/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author 李锦运 2014-10-23
 */
public class StoreReturnGoodsReasonActivity extends TitleActivity implements OnClickListener {

	private List<DicVo> dicVoList;

	private RetailApplication application;

	private StoreReturnGoodsReasonActivity context;

	private LayoutInflater inflater;

	private LinearLayout store_collect_add_layout;

	private ImageButton add;
	
	private String txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_return_reason);
		this.inflater = LayoutInflater.from(this);
		setTitleText("退货原因");
		initData();
		// change2saveMode();
		showBackbtn();
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null&&bundle.getString("txt")!=null){
			txt=bundle.getString("txt");
		}
		
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initData() {

		store_collect_add_layout = (LinearLayout) findViewById(R.id.store_collect_add_layout);
		add = (ImageButton) findViewById(R.id.add);
		add.setOnClickListener(this);

		// 传递请求参数
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.RETURNGOODS_REASONLIST);
		// parameters.setParam("shopId",
		// RetailApplication.getShopVo().getShopId());
		// parameters.setParam("currentPage", "1");

		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				JsonObject object = new JsonParser().parse(str).getAsJsonObject();
				dicVoList = new ArrayList<DicVo>();

				dicVoList.clear();
				dicVoList = getJson(str);

				addItemView(dicVoList);

			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();

	}

	private void addItemView(List<DicVo> list) {

		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); ++i) {

				StoreReturnGoodsReasonItem storeReturnGoodsReasonItem = new StoreReturnGoodsReasonItem(this, inflater,txt);

				storeReturnGoodsReasonItem.initWithAppInfo((DicVo) list.get(i));

				store_collect_add_layout.addView(storeReturnGoodsReasonItem.getItemView());

			}

		}

	}

	public static List<DicVo> getJson(String json) {

		List<DicVo> list = new ArrayList<DicVo>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj != null && jsonObj.length() > 0) {
				JSONArray jsonArray = jsonObj.getJSONArray("returnResonList");
				for (int i = 0; i < jsonArray.length(); i++) {
					DicVo dicVo = new DicVo();
					JSONObject js = jsonArray.getJSONObject(i);
					dicVo.setName(js.getString("name"));
					dicVo.setVal(js.getInt("val"));
					// supplyManagevo.setName(js.getString("name"));
					// supplyManagevo.setId(js.getString("id"));
					list.add(dicVo);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.add) {

			Intent retureReasonAdd = new Intent(StoreReturnGoodsReasonActivity.this, StoreReturnGoodsReasonAddActivity.class);
			startActivity(retureReasonAdd);

		}
	}

	public void showReasonText(String reasontxt) {

		Intent retureReason = new Intent(StoreReturnGoodsReasonActivity.this, StoreReturnGoodsDetailItemActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("reasontxt", reasontxt);
		retureReason.putExtras(bundle);

		startActivity(retureReason);

	}

}