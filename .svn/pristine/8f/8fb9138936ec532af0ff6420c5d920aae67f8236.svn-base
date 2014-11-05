package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.StockAddGoodsAdapter;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.data.StockInfoVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 库存管理-库存调整-选择商品
 * 
 * @author ys
 * 
 */
@SuppressLint("UseValueOf")
public class StockAddGoodsActivity extends TitleActivity implements OnClickListener {
	
	public static StockAddGoodsActivity instance = null;  
	
	private TextView search;
	private EditText input;
	private ImageView scan;
	private ListView store_add_goods_lv;
	private ProgressDialog progressDialog;
	private SearchGoodsVo searchGoodsVo;
	private List<StockInfoVo> searchGoodsVoList;
	private int currentPage = 1;
	private String searchCode;
	private String flag;
	private Integer pageSize = 0;
	private boolean nodata;
	private boolean is_divPage;// 是否进行分页操作
	private StockAddGoodsAdapter stockAddGoodsAdapter;
	private String shopId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_order_add_goods);
		setTitleText("选择商品");
		instance = this;
		showBackbtn();
		findView();
	}

	public void findView() {
		searchGoodsVoList = new ArrayList<StockInfoVo>();
		flag = getIntent().getStringExtra("flag");
		shopId = getIntent().getStringExtra("shopId");
		search = (TextView) findViewById(R.id.search);
		search.setOnClickListener(this);
		input = (EditText) findViewById(R.id.input);
		scan = (ImageView) findViewById(R.id.scan);
		scan.setOnClickListener(this);
		store_add_goods_lv = (ListView) findViewById(R.id.store_add_goods_lv);
		stockAddGoodsAdapter = new StockAddGoodsAdapter(StockAddGoodsActivity.this, searchGoodsVoList);
		store_add_goods_lv.setAdapter(stockAddGoodsAdapter);
		//滚动 加载更多
		store_add_goods_lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {//下拉分页
				 if (is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (nodata) {
						currentPage++;
						getResult();
					}
				 }
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});
		store_add_goods_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent add;
				if (("returnAdjustmentAdd").equals(flag)) { 
					add = new Intent(StockAddGoodsActivity.this, StockGoodInfoActivity.class);
					add.putExtra("goodId", searchGoodsVoList.get(arg2).getGoodsId());
					add.putExtra("shopId", shopId);
					add.putExtra("shopName", searchGoodsVoList.get(arg2).getGoodsName());
					startActivity(add);
				}
			}
		});
		
		progressDialog = new ProgressDialog(StockAddGoodsActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
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
		progressDialog.show();

		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockAdjust/list");
		params.setParam("shopId", RetailApplication.getShopVo().getShopId());
		params.setParam("currentPage", currentPage);

		new AsyncHttpPost(params, new RequestResultCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get("returnCode");
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null ||!returnCode.equals("success")) {
					ToastUtil.showShortToast(StockAddGoodsActivity.this, "获取失败");
					return;
				}
				
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(StockAddGoodsActivity.this)) {
					return;
				}
				pageSize = (Integer)ju.get("pageCount",Integer.class);
				List<StockInfoVo> infoVos = new ArrayList<StockInfoVo>();
				infoVos = (List<StockInfoVo>) ju.get(Constants.STOCK_INFO_lIST, new TypeToken<List<StockInfoVo>>(){}.getType());
				
				if (infoVos != null && infoVos.size() > 0) {
					if (currentPage<=pageSize){
						nodata = true;
					}
					searchGoodsVoList.addAll(infoVos);
				}
				stockAddGoodsAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StockAddGoodsActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}
}