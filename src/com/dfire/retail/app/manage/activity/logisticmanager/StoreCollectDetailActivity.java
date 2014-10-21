package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;

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
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.StockInDetailVo;
import com.dfire.retail.app.manage.data.StockInVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 物流管理-门店进货详情
 * @author wangpeng
 *
 */
public class StoreCollectDetailActivity extends TitleActivity {
	
	private ListView store_collect_lv;
	private TextView stockInNo;
	private TextView supplyName;
	
	ProgressDialog progressDialog;
	StockInDetailVo stockInDetailVo;
	
	private String stockInId;
	private String recordType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_collect_detail);
		setTitleText("门店进货");
		showBackbtn();
		findView();
	}
	
	public void findView(){
		store_collect_lv=(ListView)findViewById(R.id.store_collect_lv);
		stockInNo=(TextView)findViewById(R.id.stockInNo);
		supplyName=(TextView)findViewById(R.id.supplyName);
		getResult();
	}

	private void getResult() {
		progressDialog = new ProgressDialog(StoreCollectDetailActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		progressDialog.show();
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "purchase/detail");
		params.setParam("stockInId", stockInId);
		params.setParam("recordType", recordType);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				System.out.println(">>>>>>>"+str);
//				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();

			}
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StoreCollectDetailActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}

}