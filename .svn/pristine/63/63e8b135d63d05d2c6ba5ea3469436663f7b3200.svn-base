package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.StockQueryAdapter;
import com.dfire.retail.app.manage.data.CategoryVo;
import com.dfire.retail.app.manage.data.StockInfoVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 库存管理-库存查询
 * @author wangpeng
 *
 */
public class StockQueryActivity extends TitleActivity implements OnClickListener{
	
	private TextView search;
	private EditText input;
	private ImageView scan;
	private ListView stock_query_lv;
	ProgressDialog progressDialog;
	StockInfoVo stockInfo;
	List<StockInfoVo> stockInfoList;
	private int currentPage=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_query);
		setTitleText("库存查询");
		showBackbtn();
		findView();
	}
	public void findView(){
		search=(TextView)findViewById(R.id.search);
		search.setOnClickListener(this);
		input=(EditText)findViewById(R.id.input);
		scan=(ImageView)findViewById(R.id.scan);
		scan.setOnClickListener(this);
		stock_query_lv=(ListView)findViewById(R.id.stock_query_lv);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			getStockQuery();
			break;

		default:
			break;
		}
	}
	
	private void getStockQuery() {
		progressDialog = new ProgressDialog(StockQueryActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		progressDialog.show();
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockInfo/list");
		params.setParam("shopId", RetailApplication.getmShopInfo().getShopId());
		params.setParam("currentPage", currentPage);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				System.out.println(">>>>>>>"+str);
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				stockInfoList=new ArrayList<StockInfoVo>();
				stockInfoList.clear();
				stockInfoList=getJson(str);
				System.out.println("Gson>>>"+stockInfoList);
				stock_query_lv.setAdapter(new StockQueryAdapter(StockQueryActivity.this, stockInfoList));
				
			}
			
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StockQueryActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}
	
	public static List<StockInfoVo> getJson(String json) {

		List<StockInfoVo> list = new ArrayList<StockInfoVo>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj != null && jsonObj.length() > 0) {
				JSONArray jsonArray = jsonObj.getJSONArray("stockInfoList");
					for (int i = 0; i < jsonArray.length(); i++) {
						StockInfoVo entity = new StockInfoVo();
						JSONObject js = jsonArray.getJSONObject(i);
						entity.setFallDueNum(js.getInt("fallDueNum"));
						entity.setGoodsId(js.getString("goodsId"));
						entity.setGoodsName(js.getString("goodsName"));
						entity.setNowStore(js.getInt("nowStore"));
						entity.setPowerPrice(js.getDouble("powerPrice"));
						entity.setShopId(js.getString("shopId"));
						entity.setShortCode(js.getString("barCode"));
						entity.setSumMoney(js.getDouble("sumMoney"));
						list.add(entity);
					}
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	

}