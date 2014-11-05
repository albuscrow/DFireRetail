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
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.vo.ReturnGoodsVo;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 物流管理-门店退货
 * 
 * @author 李锦运 2014-10-20
 */
public class StoreReturnGoodsActivity extends TitleActivity implements OnClickListener, OnSampleItemClickListener,OnItemClickListener, IOnItemSelectListener, IItemListListener {

	private ImageButton add;
	private ItemEditList lsStatus;

	private StoreReturnGoodsActivity context;
	private LayoutInflater inflater;

	private RetailApplication application;

	private ItemEditList shop_name,date;

	private List<AllShopVo> shopList = new ArrayList<AllShopVo>();;

	private SpinerPopWindow shopsSpinner;

	private ItemTextView shop_name1;
	
	private SelectDateDialog mDateDialog;
	
	private String selectDate = null;
	
	private int currentPage = 1;//初始化页数
	
	private Long sendEndTimeDate;
	
	private int status = 0;
	
	private BillStatusDialog billStatusDialog;
	
	private List<DicVo> dicVos = new ArrayList<DicVo>();
	
	private List<ReturnGoodsVo> returnGoodsVoList=new ArrayList<ReturnGoodsVo>();

	private List<String> statusNameList = new ArrayList<String>();
	
	private Integer val;
	
	private boolean is_divPage;// 是否进行分页操作
	
	private ListView store_return_goods_lv;
	
	private boolean nodata;
	
	private Integer pageSize = 0;
	
	private StoreReturnGoodsAdapter storeReturnGoodsAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_return_goods);
		this.inflater = LayoutInflater.from(this);
		application = (RetailApplication) getApplication();
		setTitleText("门店退货");

		showBackbtn();
		findShopView();
		searchStatusList();
		this.mDateDialog = new SelectDateDialog(StoreReturnGoodsActivity.this, true);// 时间
	}

	private void findShopView() {

		date = (ItemEditList) findViewById(R.id.date);
		shop_name = (ItemEditList) findViewById(R.id.shop_name);
		shop_name1 = (ItemTextView) findViewById(R.id.shop_name1);
		add = (ImageButton) findViewById(R.id.add);
		add.setOnClickListener(this);

		lsStatus = (ItemEditList) findViewById(R.id.lsStatus);

		
		store_return_goods_lv=(ListView)findViewById(R.id.store_return_goods_lv);
		
		this.store_return_goods_lv.setOnScrollListener(new OnScrollListener() {
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
		shop_name.initLabel("门店", null, Boolean.TRUE, this);
		shop_name.initData(RetailApplication.getShopVo().getShopName(),RetailApplication.getShopVo().getShopName());
		lsStatus.initLabel("状态", null, Boolean.TRUE, this);
		lsStatus.initData("全部", null);
		shop_name1.initLabel("门店", null);
		shop_name1.initData(RetailApplication.getShopVo().getShopName(), RetailApplication.getShopVo().getShopName());
		date.initLabel("退货日期", null, Boolean.TRUE, this);
		// shopType 1公司（parentId=null 总公司；parentId!=null 分公司） ，2门店
		
		
		if (application.getShopVo().getShopType().equals("1")) {
			date.setVisibility(View.GONE);
			add.setVisibility(View.GONE);
			shop_name1.setVisibility(View.GONE);
			initCompanyData();

		} else if (application.getShopVo().getShopType() .equals("2")) {
			date.setVisibility(View.VISIBLE);
			shop_name.setVisibility(View.GONE);
			initData(RetailApplication.getShopVo().getShopId());
		}
	}

	private void initCompanyData() {

		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.ALL_SHOP_LIST_URL);
		params.setParam(Constants.SHOP_ID, RetailApplication.getShopVo().getShopId());
		params.setParam(Constants.PAGE, 1);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				JsonUtil ju = new JsonUtil(str);
				List<AllShopVo> shopVoList = (List<AllShopVo>) ju.get("allShopList", new TypeToken<List<AllShopVo>>() {
				}.getType());

				if (shopVoList != null && shopVoList.size() > 0) {
					shop_name.initData(shopVoList.get(0).getShopName(), shopVoList.get(0).getShopName());

					List<String> strList = new ArrayList<String>();
					for (AllShopVo dic : shopVoList) {
						strList.add(dic.getShopName());
						shopList.add(dic);
					}
					shopsSpinner = new SpinerPopWindow(StoreReturnGoodsActivity.this);
					shopsSpinner.refreshData(strList, 0);
					shopsSpinner.setItemListener(StoreReturnGoodsActivity.this);
				}

				initData(shopVoList.get(0).getShopId());

			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}).execute();

	}

	
	private void searchStatusList() {

		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.RETURNGOODS_STATUSLIST);

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
				billStatusDialog = new BillStatusDialog(StoreReturnGoodsActivity.this, dicVos);// 状态
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();

	}
	
	
//	@Override
//	public void onItemClick(int pos) {
//		String vo = shopList.get(pos).getShopName();
//		shop_name.initData(vo, vo);
//
//		initData(shopList.get(pos).getShopId());
//
//	}

	// 加载退货单号列表
	private void initData(String str) {
		nodata = false;
		// 传递请求参数
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.RETURN_GOODS_LIST);
		parameters.setParam("shopId", str);
		parameters.setParam("currentPage", currentPage);
		parameters.setParam("billStatus", status);
		if (sendEndTimeDate != null) {
			parameters.setParam("sendEndTime", sendEndTimeDate);
		}
		
		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				JsonObject object = new JsonParser().parse(str).getAsJsonObject();
				List<ReturnGoodsVo>	returnGoodsVoList1 = new ArrayList<ReturnGoodsVo>();
				pageSize = (Integer)ju.get(Constants.PAGE_SIZE,Integer.class);
				returnGoodsVoList1.clear();
				returnGoodsVoList1 = getJson(str);
				
				
				
				if (currentPage<pageSize){
					nodata = true;
				}
				
				returnGoodsVoList.addAll(returnGoodsVoList1);
				storeReturnGoodsAdapter = new StoreReturnGoodsAdapter(StoreReturnGoodsActivity.this, returnGoodsVoList);
				store_return_goods_lv.setAdapter(storeReturnGoodsAdapter);
				store_return_goods_lv.setOnItemClickListener(StoreReturnGoodsActivity.this);
			
			
				storeReturnGoodsAdapter.notifyDataSetChanged();
				
				
				
				
				
//				addItemView(returnGoodsVoList);
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();

	}

//	private void addItemView(List<ReturnGoodsVo> list) {
//		store_collect_add_layout.removeAllViews();
//		if (list != null && list.size() != 0) {
//			for (int i = 0; i < list.size(); ++i) {
//
//				StoreReturnGoodsListItem storeReturnGoodsListItem = new StoreReturnGoodsListItem(this, inflater);
//
//				storeReturnGoodsListItem.initWithAppInfo((ReturnGoodsVo) list.get(i));
//
//				store_collect_add_layout.addView(storeReturnGoodsListItem.getItemView());
//
//			}
//
//		}
//
//	}

	public static List<ReturnGoodsVo> getJson(String json) {

		List<ReturnGoodsVo> list = new ArrayList<ReturnGoodsVo>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj != null && jsonObj.length() > 0) {
				JSONArray jsonArray = jsonObj.getJSONArray("returnGoodsList");
				for (int i = 0; i < jsonArray.length(); i++) {
					ReturnGoodsVo returnGoodsVo = new ReturnGoodsVo();
					JSONObject js = jsonArray.getJSONObject(i);
					returnGoodsVo.setReturnGoodsId(js.getString("returnGoodsId"));
					returnGoodsVo.setReturnGoodsNo(js.getString("returnGoodsNo"));
					returnGoodsVo.setSendEndTime(js.getLong("sendEndTime"));
					returnGoodsVo.setBillStatus(js.getInt("billStatus"));
					returnGoodsVo.setBillStatusName(js.getString("billStatusName"));
					returnGoodsVo.setGoodsTotalSum(js.getInt("goodsTotalSum"));
					returnGoodsVo.setReturnGoodsId(js.getString("returnGoodsId"));
					returnGoodsVo.setGoodsTotalPrice(js.getInt("goodsTotalPrice"));
					returnGoodsVo.setSupplyId(js.getString("supplyId"));
					returnGoodsVo.setSupplyName(js.getString("supplyName"));
					// supplyManagevo.setName(js.getString("name"));
					// supplyManagevo.setId(js.getString("id"));
					list.add(returnGoodsVo);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.add:

			HashMap<String, List<Object>> map = application.getObjectMap();
			if (map.get("returnGoods") != null) {
				map.get("returnGoods").clear();
				// map.put("returnGoods", null);
				application.setObjectMap(map);
			}

			Intent returnGoods = new Intent(StoreReturnGoodsActivity.this, StoreReturnGoodsAddActivity.class);
			startActivity(returnGoods);
			break;
			
		case R.id.date:
			pushDate();
		}

	}

	private void pushDate(){
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
				returnGoodsVoList.clear();
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
	
	@Override
	public void onSampleItemClick(String eventType, View view, Object obj) {
	}

	@Override
	public void onItemListClick(ItemEditList obj) {
		if (obj.getId() == R.id.shop_name) {

			shopsSpinner.setWidth(shop_name.getWidth());
			shopsSpinner.showAsDropDown(shop_name);
			// 页面跳转从此添加 代码.

		}if (obj.getId() == R.id.lsStatus) {
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
				returnGoodsVoList.clear();
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ReturnGoodsVo returnGoodsVo = returnGoodsVoList.get(position);
		Intent listDetail = new Intent(StoreReturnGoodsActivity.this, StoreReturnGoodsListDetailActivity.class);
		startActivity(listDetail);

		RetailApplication application = (RetailApplication) StoreReturnGoodsActivity.this.getApplication();
		HashMap<String, Object> objMap = application.getObjMap();
		objMap.put("returnGoodsVo", returnGoodsVo);
	}

	/* (non-Javadoc)
	 * @see com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener#onItemClick(int)
	 */
	@Override
	public void onItemClick(int pos) {
		// TODO Auto-generated method stub
		
	}
}