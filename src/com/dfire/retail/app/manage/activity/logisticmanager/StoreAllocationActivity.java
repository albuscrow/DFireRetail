/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.dfire.retail.app.common.event.OnSampleItemClickListener;
import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemTextView;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.BillStatusDialog;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.vo.AllocateVo;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.google.gson.reflect.TypeToken;

/** 
 * 物流管理-门店调拨
 * 
 * @author 李锦运 2014-10-20
 */
public class StoreAllocationActivity extends TitleActivity implements OnClickListener,OnItemClickListener, OnSampleItemClickListener, IOnItemSelectListener, IItemListListener {

	private LayoutInflater inflater;

	private RetailApplication application;

	private ItemTextView shop_name1;

	private ImageButton add;

//	private LinearLayout store_collect_add_layout;

	private ItemEditList lsStatus, date;

	private List<AllocateVo> allocateVoList=new ArrayList<AllocateVo>();

	private SpinerPopWindow shopsSpinner;

	private List<DicVo> dicVos = new ArrayList<DicVo>();

	private List<String> statusNameList = new ArrayList<String>();

	private int status = 0;

	private SelectDateDialog mDateDialog;

	private SimpleDateFormat sDateFormat;

	private Long sendEndTimeDate;

	private BillStatusDialog billStatusDialog;

	private Integer val;

	private String selectDate = null;
	
	private ListView store_allocation_lv;
	
	private StoreAllocationAdapter storeAllocationAdapter;
	
	private boolean is_divPage;// 是否进行分页操作
	
	private boolean nodata;
	
	private int currentPage = 1;//初始化页数
	
	private Integer pageSize = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_allocation);
		this.inflater = LayoutInflater.from(this);
		application = (RetailApplication) getApplication();
		setTitleText("门店调拨");
		showBackbtn();
		findShopView();

	}

	private void findShopView() {

		date = (ItemEditList) findViewById(R.id.date);
		shop_name1 = (ItemTextView) findViewById(R.id.shop_name1);
		add = (ImageButton) findViewById(R.id.add);
		add.setOnClickListener(this);
//		store_collect_add_layout = (LinearLayout) findViewById(R.id.store_collect_add_layout);
		lsStatus = (ItemEditList) findViewById(R.id.lsStatus);
		store_allocation_lv=(ListView)findViewById(R.id.store_allocation_lv);
		
		this.store_allocation_lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				 if (is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (nodata) {
						currentPage++;
						initData(RetailApplication.getShopVo().getShopId());
					}
				 }
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});		
		initView();
	}

	private void initView() {
		date.initLabel("调拨日期", null, Boolean.TRUE, this);
		lsStatus.initLabel("状态", null, Boolean.TRUE, this);
		lsStatus.initData("全部", null);
		shop_name1.initLabel("门店", null);
		shop_name1.initData(RetailApplication.getShopVo().getShopName(), RetailApplication.getShopVo().getShopName());

		this.mDateDialog = new SelectDateDialog(StoreAllocationActivity.this, true);// 时间
		initData(RetailApplication.getShopVo().getShopId());
	}

	private void initData(String str) {
		nodata = false;
		// 传递请求参数
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.ALLOCATE_LIST);
		parameters.setParam("shopId", str);
		parameters.setParam("billStatus", status);
		parameters.setParam("currentPage", currentPage);
		if (sendEndTimeDate != null) {
			parameters.setParam("sendEndTime", sendEndTimeDate);
		}

		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				JsonUtil ju = new JsonUtil(str);
				List<AllocateVo> allocateVoList1 = new ArrayList<AllocateVo>();
				pageSize = (Integer)ju.get(Constants.PAGE_SIZE,Integer.class);
				allocateVoList1 = getJson(str);
//				addItemView(allocateVoList);
				searchStatusList();
				if (currentPage<pageSize){
					nodata = true;
				}
				
				allocateVoList.addAll(allocateVoList1);
			    storeAllocationAdapter = new StoreAllocationAdapter(StoreAllocationActivity.this, allocateVoList);
				store_allocation_lv.setAdapter(storeAllocationAdapter);
				store_allocation_lv.setOnItemClickListener(StoreAllocationActivity.this);
			
			
				storeAllocationAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();

	}

	private void searchStatusList() {

		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.ALLOCATE_STATUSLIST);

		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);

				List<DicVo> vos = new ArrayList<DicVo>();
				vos = (List<DicVo>) ju.get(Constants.STATUSLIST, new TypeToken<List<DicVo>>() {
				}.getType());

				dicVos.clear();
				statusNameList.clear();
				for (DicVo dicVo : vos) {
					statusNameList.add(dicVo.getName());
					dicVos.add(dicVo);
				}
				billStatusDialog = new BillStatusDialog(StoreAllocationActivity.this, dicVos);// 状态
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();

	}

	public static List<AllocateVo> getJson(String json) {

		List<AllocateVo> list = new ArrayList<AllocateVo>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj != null && jsonObj.length() > 0) {
				JSONArray jsonArray = jsonObj.getJSONArray("allocateList");
				for (int i = 0; i < jsonArray.length(); i++) {
					AllocateVo allocateVo = new AllocateVo();
					JSONObject js = jsonArray.getJSONObject(i);
					allocateVo.setAllocateId(js.getString("allocateId"));
					allocateVo.setBatchNo(js.getString("allocateNo"));

					allocateVo.setBillStatus(js.getInt("billStatus"));
					allocateVo.setBillStatusName(js.getString("billStatusName"));
					allocateVo.setRealSum(js.getInt("goodsTotalSum"));
					allocateVo.setRealTotalPrice(js.getDouble("goodsTotalPrice"));

					// Date a=DateUtil.strToDateYMD_ZH();
					allocateVo.setSendEndTime(js.getLong("sendEndTime"));

					list.add(allocateVo);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void onItemListClick(ItemEditList obj) {

		if (obj.getId() == R.id.lsStatus) {
			pushStatus();
		} else if (obj.getId() == R.id.date) {
			pushDate();
		}

	}

	private void pushStatus() {
		billStatusDialog.show();
		billStatusDialog.getmTitle().setText("订单状态");
		billStatusDialog.updateType(val);
		billStatusDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Integer index = billStatusDialog.getCurrentData();
				DicVo dicVo = dicVos.get(index);
				lsStatus.initData(dicVo.getName(), dicVo.getName());
				status = dicVo.getVal();
				allocateVoList.clear();
				currentPage = 1;//选择以后初始化页数
				initData(RetailApplication.getShopVo().getShopId());
				billStatusDialog.dismiss();
			}
		});
		billStatusDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				billStatusDialog.dismiss();
			}
		});
	}

	@Override
	public void onItemClick(int pos) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSampleItemClick(String eventType, View view, Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.add:

			HashMap<String, List<Object>> map = application.getObjectMap();
			if (map.get("allocation") != null) {
				map.get("allocation").clear();
				application.setObjectMap(map);
			}

			Intent returnGoods = new Intent(StoreAllocationActivity.this, StoreAllocationAddActivity.class);
			startActivity(returnGoods);
			break;

		}

	}

	private void pushDate() {
		mDateDialog.show();
		mDateDialog.getTitle().setText("要求调拨日期");
		mDateDialog.updateDays(selectDate);
		mDateDialog.getmClearDate().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDateDialog.dismiss();
				date.initData("请选择", "请选择");
				currentPage = 1;//选择以后初始化页数
				
				
			}

		});

		mDateDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDateDialog.dismiss();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");
				selectDate = mDateDialog.getCurrentData();
				// store_order_time.setText(selectDate);
				date.initData(selectDate, selectDate);
				if (selectDate != null) {
					try {
						sendEndTimeDate = (sdf.parse((selectDate + " 00:00:00"))).getTime();
					} catch (ParseException e) {
						sendEndTimeDate = null;
					}
				}
				allocateVoList.clear();
				initData(RetailApplication.getShopVo().getShopId());
			}
		});
		mDateDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDateDialog.dismiss();
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AllocateVo allocateVo = allocateVoList.get(position);
		Intent listDetail = new Intent(StoreAllocationActivity.this, StoreAllocationListDetailActivity.class);
		startActivity(listDetail);

		RetailApplication application = (RetailApplication) StoreAllocationActivity.this.getApplication();
		HashMap<String, Object> objMap = application.getObjMap();
		objMap.put("allocateVo", allocateVo);
	}

}