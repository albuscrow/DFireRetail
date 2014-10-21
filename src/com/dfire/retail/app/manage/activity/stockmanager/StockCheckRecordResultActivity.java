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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.StockCheckRecordDetailVo;
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
 * 库存管理-盘点记录结果报告
 * @author wangpeng
 *
 */
public class StockCheckRecordResultActivity extends TitleActivity  {

	ProgressDialog progressDialog;
	StockCheckRecordDetailVo stockCheckRecordDetailVo;
	List<StockCheckRecordDetailVo> detailList;
	private int currentPage=1;
	private String stockCheckId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_check_records_result);
		setTitleText("盘点结果报告");
		showBackbtn();
		findView();
	}
	
	public void findView(){
		handler.sendEmptyMessage(1);
	}
	
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				getResult();
				break;

			default:
				break;
			}
		}
	};
	
	private void getResult() {
		progressDialog = new ProgressDialog(StockCheckRecordResultActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		progressDialog.show();
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "checkStockRecord/detail");
		params.setParam("shopId", RetailApplication.getmShopInfo().getShopId());
		params.setParam("currentPage", currentPage);
		params.setParam("stockCheckId", stockCheckId);
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
					ToastUtil.showShortToast(StockCheckRecordResultActivity.this, "获取商品分类失败");
					return;
				}
				detailList=new ArrayList<StockCheckRecordDetailVo>();
//				stockCheckRecordList.clear();
//				stockCheckRecordList=getJson(str);
//				System.out.println("Gson>>>"+stockCheckRecordList);
//				inventory_records_lv.setAdapter(new StockCheckRecordAdapter(StockCheckRecordResultActivity.this, stockCheckRecordList));
				
			}
			
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StockCheckRecordResultActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}
	
	

}
