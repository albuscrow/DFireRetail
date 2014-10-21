package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsSortListActivity;
import com.dfire.retail.app.manage.adapter.StockCheckRecordAdapter;
import com.dfire.retail.app.manage.data.StockCheckRecordVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 库存管理-盘点记录
 * @author wangpeng
 *
 */
public class StockCheckRecordActivity extends TitleActivity implements OnClickListener {

	TextView inventory_records_time;
	ProgressDialog progressDialog;
	StockCheckRecordVo stockCheckRecordVo;
	List<StockCheckRecordVo> stockCheckRecordList;
	ListView inventory_records_lv;
	private int currentPage=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_check_records);
		setTitleText("盘点记录");
		showBackbtn();
		findView();
	}
	
	public void findView(){
		inventory_records_time=(TextView)findViewById(R.id.inventory_records_time);
		inventory_records_time.setOnClickListener(this);
		inventory_records_lv=(ListView)findViewById(R.id.inventory_records_lv);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.inventory_records_time:
			getStockQuery();
			
			break;
		}
	}
	
	private void getStockQuery() {
		progressDialog = new ProgressDialog(StockCheckRecordActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		progressDialog.show();
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "checkStockRecord/list");
		params.setParam("shopId", RetailApplication.getmShopInfo().getShopId());
		params.setParam("currentPage", currentPage);
		params.setParam("starttime", "2014-10-04");
		params.setParam("endtime", "2014-10-04");
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				System.out.println(">>>>>>>"+str);
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get("returnCode");
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}

				if (returnCode == null || !returnCode.equals("success")) {
					ToastUtil.showShortToast(StockCheckRecordActivity.this, "获取商品分类失败");
					return;
				}
				stockCheckRecordList=new ArrayList<StockCheckRecordVo>();
//				stockCheckRecordList.clear();
//				stockCheckRecordList=getJson(str);
//				System.out.println("Gson>>>"+stockCheckRecordList);
//				inventory_records_lv.setAdapter(new StockCheckRecordAdapter(StockCheckRecordActivity.this, stockCheckRecordList));
				
			}
			
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StockCheckRecordActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}
	
	public static List<StockCheckRecordVo> getJson(String json) {
		List<StockCheckRecordVo> list = new ArrayList<StockCheckRecordVo>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj != null && jsonObj.length() > 0) {
				JSONArray jsonArray = jsonObj.getJSONArray("stockInfoList");
					for (int i = 0; i < jsonArray.length(); i++) {
						StockCheckRecordVo entity = new StockCheckRecordVo();
						JSONObject js = jsonArray.getJSONObject(i);
						list.add(entity);
					}
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
