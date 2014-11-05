package com.dfire.retail.app.manage.activity.messagemanage;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.SelectShopAdapter;
import com.dfire.retail.app.manage.data.AllShopVo;
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

public class MessageShopActivity extends TitleActivity implements OnItemClickListener{

	private ListView selectshoplist;
	
	private int currentPage = 1;
	
	private  ArrayList<AllShopVo> allShops;
	
	private ProgressDialog progressDialog;
	
	private String selectShopId;
	
	private final String ALLSHOPID="00000000000000000000000000000001";
	
	private boolean fag=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_shop);
		setTitleText("选择门店");
		showBackbtn();
		this.init();
		
	}
	private void init(){
		selectShopId = getIntent().getExtras().getString("selectShopId");
		allShops = new ArrayList<AllShopVo>();//店铺
		AllShopVo allShopVo = new AllShopVo();
		allShopVo.setShopId(RetailApplication.getShopVo().getShopId());
		allShopVo.setShopName("所有门店");
		allShopVo.setParentId(RetailApplication.getShopVo().getParentId());
		allShopVo.setCode(RetailApplication.getShopVo().getCode());
		allShops.add(allShopVo);

		selectshoplist = (ListView) findViewById(R.id.selectshoplist);
		progressDialog = new ProgressDialog(MessageShopActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		
		this.getShop(fag);
	}
	/**
	 * 获取店铺列表
	 * @param isInit
	 */
	private void getShop(boolean fag) {
		if(fag){
		progressDialog.show();
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SHOPALLSHOPLIST);
		params.setParam(Constants.SHOP_ID, ALLSHOPID);
		params.setParam(Constants.PAGE, currentPage ++);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null ||!returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(MessageShopActivity.this, "获取失败");
					return;
				}
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(MessageShopActivity.this)) {
					return;
				}
				List<AllShopVo> allShopVos = new ArrayList<AllShopVo>();
				int pageSize = jo.get(Constants.PAGE_SIZE).getAsInt();
				allShopVos = (List<AllShopVo>) ju.get(Constants.ALL_SHOP_LIST,new TypeToken<List<AllShopVo>>(){}.getType());
				if(pageSize>currentPage){
				if (allShopVos.size()>0) {
					allShops.addAll(allShopVos);
					getShop(true);
				}
				}else{
					getShop(false);
				}
				selectshoplist.setAdapter(new SelectShopAdapter(MessageShopActivity.this, allShops,selectShopId));
				selectshoplist.setOnItemClickListener(MessageShopActivity.this);
			}
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(MessageShopActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
		}else{
			progressDialog.dismiss();
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		Intent select =new Intent(MessageShopActivity.this,MseeageUpdateActivity.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable("shopVo", allShops.get(arg2));
		select.putExtras(bundle);
		setResult(100, select);
		finish();
	
	}
}
