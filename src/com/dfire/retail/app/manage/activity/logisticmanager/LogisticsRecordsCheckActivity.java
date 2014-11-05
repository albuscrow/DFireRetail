/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.dfire.retail.app.manage.vo.supplyManageVo;
import com.google.gson.reflect.TypeToken;

/**
 * 物流记录填写查询条件界面
 * @author 李锦运
 *2014-10-20
 */
public class LogisticsRecordsCheckActivity extends TitleActivity implements OnClickListener{
	private Button confirm;

	private LayoutInflater inflater;

	private RetailApplication application;
	
	private RelativeLayout shop1,shop2;
	
	private TextView shop_name,shop_name1,supply_name,date; //门店、供应商、时间
	
	private EditText logistics_no;//单号

	private List<AllShopVo> shopList = new ArrayList<AllShopVo>();
	
	private List<supplyManageVo> supplyList = new ArrayList<supplyManageVo>();;
	
	private BillStatusDialog selectSupplyDialog,selectShopDialog;
	
	private String supplyId = "";
	
	private int supplyDialogPos = 0,shopDialogPos = 0;
	
	private String shopId;
	
	private SelectDateDialog mDateDialog;
	
	private String dateStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logistics_records_check);
		this.inflater = LayoutInflater.from(this);
		application = (RetailApplication) getApplication();
		setTitleText("物流记录");

		showBackbtn();
		findShopView();

	}

	private void findShopView() {

		date = (TextView) findViewById(R.id.date);
		shop1 = (RelativeLayout) findViewById(R.id.shop);
		shop2 = (RelativeLayout) findViewById(R.id.shop2);
		shop_name = (TextView) findViewById(R.id.shop_name);
		shop_name1 = (TextView) findViewById(R.id.shop_name1);
		logistics_no = (EditText) findViewById(R.id.logistics_no);
		supply_name = (TextView) findViewById(R.id.supply_name);
		confirm = (Button) findViewById(R.id.btn_confirm);
		
		
		date.setOnClickListener(this);
		shop_name.setOnClickListener(this);
		logistics_no.setOnClickListener(this);
		supply_name.setOnClickListener(this);
		confirm.setOnClickListener(this);

		initView();
	}

	private void initView() {
		shop_name.setText(RetailApplication.getShopVo().getShopName());
		supply_name.setText("全部");
		shop_name1.setText(RetailApplication.getShopVo().getShopName());
		shopId = RetailApplication.getShopVo().getShopId();
		// shopType 1公司（parentId=null 总公司；parentId!=null 分公司） ，2门店		
		
		if (RetailApplication.getShopVo().getShopType().equals("1")) {
			shop2.setVisibility(View.GONE);
			initCompanyData();

		} else if (RetailApplication.getShopVo().getShopType().equals("2")) {
			shop1.setVisibility(View.GONE);
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
				List<AllShopVo> shopVoList = (List<AllShopVo>) ju.get("allShopList", new TypeToken<List<AllShopVo>>() {}.getType());
		
				if (shopVoList != null && shopVoList.size() > 0) {
					shop_name.setText(shopVoList.get(0).getShopName());

					List<DicVo> dicVos = new ArrayList<DicVo>();
					for (int i=0;i<shopVoList.size();i++) {
						DicVo vo = new DicVo();
						vo.setName(shopVoList.get(i).getShopName());
						vo.setVal(i);
						dicVos.add(vo);
						shopList.add(shopVoList.get(i));
					}
					selectShopDialog = new BillStatusDialog(LogisticsRecordsCheckActivity.this, dicVos);
				}
				initData(shopVoList.get(0).getShopId());

			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}).execute();

	}
	
	// 加载供应商信息
	private void initData(String str) {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SUPPLY_INFO_MANAGE_LIST);
		params.setParam(Constants.PAGE, 1);
		params.setParam("findParameter", null);
		params.setParam("showEntityFlg",1);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				JsonUtil ju = new JsonUtil(str);
				List<supplyManageVo> supplyManageList = (List<supplyManageVo>) ju.get("supplyManageList", new TypeToken<List<supplyManageVo>>() {}.getType());
				
				supplyManageVo allSupply = new supplyManageVo();
				allSupply.setName("全部");
				allSupply.setId("");
				supplyManageList.add(0, allSupply);
			
				//Dialog
				List<DicVo> dicVos = new ArrayList<DicVo>();
				if (supplyManageList != null && supplyManageList.size() > 0) {

					for (int i=0;i<supplyManageList.size();i++){
						DicVo vo = new DicVo();
						vo.setName(supplyManageList.get(i).getName());
						vo.setVal(i);
						dicVos.add(vo);
						supplyList.add(supplyManageList.get(i));
					}
				}
				selectSupplyDialog = new BillStatusDialog(LogisticsRecordsCheckActivity.this, dicVos);
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}).execute();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			String logisticsNo = logistics_no.getText().toString();
			Intent list = new Intent(LogisticsRecordsCheckActivity.this, LogisticsRecordsListActivity.class);
			list.putExtra("shopId", shopId);
			list.putExtra("supplyId", supplyId);
			list.putExtra("sendEndTime",dateStr);
			list.putExtra("logisticsNo",logisticsNo);
			list.putExtra("currentPage",1);
			startActivity(list);
			break;
		case R.id.shop_name:
			pushShop();
			break;
		case R.id.supply_name:
			pushSupply();
			break;
		case R.id.date:
			pushDate();
			break;
		}

	}
	/**
	 * 弹出门店
	 */
	private void pushShop(){
		selectShopDialog.show();
		selectShopDialog.getmTitle().setText("供应商");
		selectShopDialog.updateType(shopDialogPos);
		selectShopDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				shopDialogPos = selectShopDialog.getCurrentData();
				AllShopVo shop = shopList.get(shopDialogPos);
				shopId = shop.getShopId();
				shop_name.setText(shop.getShopName());
				selectShopDialog.dismiss();
			}
		});
		selectShopDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectShopDialog.dismiss();
			}
		});
	}
	/**
	 * 弹出时间
	 */
	private void pushDate(){
		mDateDialog = new SelectDateDialog(LogisticsRecordsCheckActivity.this);
		mDateDialog.show();
		mDateDialog.getTitle().setText("时间");
		mDateDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dateStr = mDateDialog.getCurrentData();
				date.setText(dateStr);
				mDateDialog.dismiss();
			}
		});
		mDateDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDateDialog.dismiss();
			}
		});
	}
	/**
	 * 弹出供应商
	 */
	private void pushSupply(){
		selectSupplyDialog.show();
		selectSupplyDialog.getmTitle().setText("供应商");
		selectSupplyDialog.updateType(supplyDialogPos);
		selectSupplyDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				supplyDialogPos = selectSupplyDialog.getCurrentData();
				supplyManageVo supply = supplyList.get(supplyDialogPos);
				supplyId = supply.getId();
				supply_name.setText(supply.getName());
				selectSupplyDialog.dismiss();
			}
		});
		selectSupplyDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectSupplyDialog.dismiss();
			}
		});
	}
}
