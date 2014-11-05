/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.SupplyListAdapter;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.vo.supplyManageVo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * @author 李锦运
 *2014-10-20
 */
public class SupplierManagementActivity extends TitleActivity implements OnItemClickListener, OnClickListener{
	
	private LayoutInflater inflater;
	
	private RetailApplication application;
	
	private EditText searchEt;
	
	private TextView searchTxt;
	
	private ListView supplyList;
	
	private ImageButton addSupply;
	
	private String shopId;//店铺id
	
	private Integer val = 0;
	
	private Integer pageSize = 0;
	
	private boolean is_divPage;// 是否进行分页操作
	
	private ProgressDialog progressDialog;
	
	private SupplyListAdapter supplyListAdapter;
	
	private List<supplyManageVo> supplyManageVos;
	
	private int currentPage = 1;
	
	private boolean nodata;
	
	private String findParameter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_management);
		this.inflater = LayoutInflater.from(this);
		application = (RetailApplication) getApplication();
		setTitleText("供应商");

		showBackbtn();
		findSupplyView();

	}

	private void findSupplyView() {
		shopId = RetailApplication.getShopVo().getShopId();
//		dicVos = new ArrayList<DicVo>();//状态
		supplyManageVos=new ArrayList<supplyManageVo>();
//		currentShop = RetailApplication.getShopVo();
		searchEt=(EditText)findViewById(R.id.et_search);
		searchTxt=(TextView)findViewById(R.id.txt_search);
		supplyList = (ListView) findViewById(R.id.supply_list_view);
		addSupply=(ImageButton)findViewById(R.id.minus);
		supplyListAdapter = new SupplyListAdapter(SupplierManagementActivity.this, supplyManageVos);
		supplyList.setAdapter(supplyListAdapter);
		supplyList.setOnItemClickListener(SupplierManagementActivity.this);
		addSupply.setOnClickListener(this);
		supplyList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				 if (is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (nodata) {
						currentPage++;
						getSupplyList();
					}
				 }
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});
		
		progressDialog = new ProgressDialog(SupplierManagementActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		searchTxt.setOnClickListener(this);
		this.getSupplyList();//获取全部订单列表
	}
	
	/**
	 * 查询订货列表
	 */
	private void getSupplyList() {
		nodata = false;
		progressDialog.show();
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SUPPLY_INFO_MANAGE_LIST);
		params.setParam("currentPage", currentPage);
		params.setParam("showEntityFlg", 1);
		params.setParam("findParameter", findParameter);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null ||!returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(SupplierManagementActivity.this, "获取失败");
					return;
				}
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(SupplierManagementActivity.this)) {
					return;
				}
				pageSize = (Integer)ju.get("pageCount",Integer.class);
				List<supplyManageVo> supplys = new ArrayList<supplyManageVo>();
				supplys = (List<supplyManageVo>) ju.get(Constants.SUPPLY_MANAGER_LIST, new TypeToken<List<supplyManageVo>>(){}.getType());
				
				if (supplys != null && supplys.size() > 0) {
					if (currentPage<=pageSize){
						nodata = true;
					}
					supplyManageVos.addAll(supplys);
					supplyListAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(SupplierManagementActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		supplyManageVo supplyManageVo = supplyManageVos.get(position);
		Intent supplyIntent =new Intent(SupplierManagementActivity.this,SupplyManagerDetailActivity.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable("supplyManageVo", supplyManageVo);
		supplyIntent.putExtra("supplyState", "SUPPLY_EDIT");
		supplyIntent.putExtras(bundle);
		startActivity(supplyIntent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.minus:
			Intent supplyAdd =new Intent(SupplierManagementActivity.this,SupplyManagerAddActivity.class);
			supplyAdd.putExtra("supplyState", "SUPPLY_ADD");// 新生订单
			startActivity(supplyAdd);
			break;
		case R.id.txt_search:
			this.findParameter= searchEt.getText().toString();
			this.currentPage = 1;
			getSupplyList();
			break;
		}
	}
}
