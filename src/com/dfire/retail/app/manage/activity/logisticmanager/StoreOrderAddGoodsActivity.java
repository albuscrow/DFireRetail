package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.StoreAddGoodsAdapter;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 物流管理-添加门店订货-选择商品
 * @author wangpeng
 *
 */
public class StoreOrderAddGoodsActivity extends TitleActivity implements OnClickListener{
	
	private TextView search;
	private EditText input;
	private ImageView scan;
	private ListView store_add_goods_lv;
	ProgressDialog progressDialog;
	SearchGoodsVo searchGoodsVo;
	List<SearchGoodsVo> searchGoodsVoList;
	private int currentPage=1;
	private String searchCode;
	int add=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_order_add_goods);
		setTitleText("选择商品");
		showBackbtn();
		findView();
	}
	public void findView(){
		Intent intent=getIntent();
		add=intent.getIntExtra("add", 1);
		search=(TextView)findViewById(R.id.search);
		search.setOnClickListener(this);
		input=(EditText)findViewById(R.id.input);
		scan=(ImageView)findViewById(R.id.scan);
		scan.setOnClickListener(this);
		store_add_goods_lv=(ListView)findViewById(R.id.store_add_goods_lv);
		store_add_goods_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent add =new Intent(StoreOrderAddGoodsActivity.this,StoreOrderAddActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("orderAdd", searchGoodsVoList.get(arg2));
				add.putExtras(bundle);
				setResult(100, add);
				finish();
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			getResult();
			break;

		default:
			break;
		}
	}
	
	private void getResult() {
		progressDialog = new ProgressDialog(StoreOrderAddGoodsActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		progressDialog.show();
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "orderGoods/goodsList");
		params.setParam("shopId", RetailApplication.getmShopInfo().getShopId());
		params.setParam("currentPage", currentPage);
		
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				searchGoodsVoList=new ArrayList<SearchGoodsVo>();
				searchGoodsVoList.clear();
				searchGoodsVoList=getJson(str);
				store_add_goods_lv.setAdapter(new StoreAddGoodsAdapter(StoreOrderAddGoodsActivity.this, searchGoodsVoList));
				
			}
			
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StoreOrderAddGoodsActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}
	
	public static List<SearchGoodsVo> getJson(String json) {

		List<SearchGoodsVo> list = new ArrayList<SearchGoodsVo>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj != null && jsonObj.length() > 0) {
				JSONArray jsonArray = jsonObj.getJSONArray("goodsList");
					for (int i = 0; i < jsonArray.length(); i++) {
						SearchGoodsVo entity = new SearchGoodsVo();
						JSONObject js = jsonArray.getJSONObject(i);
						entity.setGoodsId(js.getString("goodsId"));
						entity.setGoodsName(js.getString("goodsName"));
						entity.setNowstore(js.getInt("nowstore"));
						entity.setPeriod(js.getInt("period"));
						entity.setPurchasePrice(js.getDouble("purchasePrice"));
						list.add(entity);
					}
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	

}