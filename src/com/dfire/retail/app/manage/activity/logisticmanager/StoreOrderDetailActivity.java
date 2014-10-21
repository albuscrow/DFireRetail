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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.OrderGoodsDetailVo;
import com.dfire.retail.app.manage.data.OrderGoodsVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 物流管理-门店订货详情
 * @author wangpeng
 *
 */
public class StoreOrderDetailActivity extends TitleActivity implements OnClickListener{
	
	private ListView store_order_lv;
	private TextView store_order_time;
	ProgressDialog progressDialog;
	OrderGoodsDetailVo orderGoodsDetailVo;
	private String  orderGoodsId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_order_detail);
		setTitleText("门店订货");
		showBackbtn();
		findView();
	}
	
	public void findView(){
		store_order_lv=(ListView)findViewById(R.id.store_order_lv);
		store_order_time=(TextView)findViewById(R.id.store_order_time);
		store_order_time.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.store_order_time:
			getResult();
			
			break;

		default:
			break;
		}
	}
	
	private void getResult() {
		progressDialog = new ProgressDialog(StoreOrderDetailActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		progressDialog.show();
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "orderGoods/detail");
		params.setParam("orderGoodsId", orderGoodsId);
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
				if (returnCode == null ||!returnCode.equals("success")) {
					ToastUtil.showShortToast(StoreOrderDetailActivity.this, "获取失败");
					return;
				}
				
				
			}
			
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StoreOrderDetailActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}

}