package com.dfire.retail.app.manage.activity.logisticmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.BillStatusDialog;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.SupplyInfoListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.vo.supplyManageVo;

/**
 * 物流记录填写查询条件界面
 * @author ys
 *2014-11-14
 */
@SuppressLint("SimpleDateFormat")
public class LogisticsRecordsCheckActivity extends TitleActivity implements OnClickListener{
	private Button confirm;

	private TextView shop_name,supply_name,date; //门店、供应商、时间
	
	private EditText logistics_no;//单号

	private List<supplyManageVo> supplyList = new ArrayList<supplyManageVo>();;
	
	private BillStatusDialog selectSupplyDialog;
	
	private String supplyId = "";
	
	private int supplyDialogPos = 0;
	
	private String shopId;
	
	private SelectDateDialog mDateDialog;
	
	private ShopVo currentShop;
	
	private AllShopVo allShopVo;
	
	private String selectDate = null;
	
	private Long sendEndTime;
	
	private ImageView clear_input;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logistics_records_check);
		LayoutInflater.from(this);
		setTitleText("物流记录");
		showBackbtn();
		findShopView();

	}

	private void findShopView() {
		initData();
		shopId = RetailApplication.getShopVo().getShopId();
		currentShop = RetailApplication.getShopVo();
		date = (TextView) findViewById(R.id.date);
		shop_name = (TextView) findViewById(R.id.shop_name);
		logistics_no = (EditText) findViewById(R.id.logistics_no);
		supply_name = (TextView) findViewById(R.id.supply_name);
		confirm = (Button) findViewById(R.id.btn_confirm);
		clear_input = (ImageView) findViewById(R.id.clear_input);
		clear_input.setOnClickListener(this);
		date.setOnClickListener(this);
		logistics_no.setOnClickListener(this);
		supply_name.setOnClickListener(this);
		confirm.setOnClickListener(this);
		if (RetailApplication.getEntityModel()==1) {
			//单店
			this.shop_name.setCompoundDrawables(null, null, null, null);
			this.shop_name.setTextColor(Color.parseColor("#666666"));
			this.shop_name.setText(currentShop.getShopName());
		}else {
			//连锁
			if (currentShop.getType() == ShopVo.MENDIAN) {
				this.shop_name.setCompoundDrawables(null, null, null, null);
				this.shop_name.setTextColor(Color.parseColor("#666666"));
				this.shop_name.setText(currentShop.getShopName());
			}else{
				this.shop_name.setText("所有下属门店");
				this.shop_name.setOnClickListener(this);
			}
		}
		logistics_no.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s!=null&&!s.toString().equals("")) {
					clear_input.setVisibility(View.VISIBLE);
				}else{
					clear_input.setVisibility(View.GONE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

// 加载供应商信息
	private void initData() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SUPPLY_INFO_MANAGE_LIST);
		if (RetailApplication.getEntityModel()==1) {
			params.setParam("showEntityFlg", "0");
		}else {
			params.setParam("showEntityFlg", "1");
		}
		params.setParam("isDividePage", "0");
		
		new AsyncHttpPost(this, params, SupplyInfoListBo.class,false, new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				SupplyInfoListBo bo = (SupplyInfoListBo) oj;
				if (bo!=null) {
					List<supplyManageVo> supplyManageList = bo.getSupplyManageList();
					supplyManageVo allSupply = new supplyManageVo();
					allSupply.setName("全部");
					allSupply.setId("");
					supplyManageList.add(0, allSupply);
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
			}
			@Override
			public void onFail(Exception e) {
			}
		}).execute();
	}
	/**点击选择门店 回调*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){
			Bundle b=data.getExtras();
			allShopVo=(AllShopVo)b.getSerializable("shopVo");
			if (allShopVo!=null) {
				this.shop_name.setText(allShopVo.getShopName());
				this.shopId = allShopVo.getShopId();
			}
		}
	}
	/**点击事件*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			String logisticsNo = logistics_no.getText().toString();
			Intent list = new Intent(LogisticsRecordsCheckActivity.this, LogisticsRecordsListActivity.class);
			list.putExtra("shopId", shopId);
			list.putExtra("supplyId", supplyId);
			list.putExtra("sendEndTime",sendEndTime+"");
			list.putExtra("logisticsNo",logisticsNo);
			startActivity(list);
			break;
		case R.id.shop_name:
			Intent selectIntent =new Intent(LogisticsRecordsCheckActivity.this,SelectShopActivity.class);
			selectIntent.putExtra("selectShopId", shopId);
			selectIntent.putExtra("activity", "logisticsRecordsCheckActivity");
			startActivityForResult(selectIntent, 100);
			break;
		case R.id.supply_name:
			pushSupply();
			break;
		case R.id.date:
			pushDate();
			break;
		case R.id.clear_input:
			logistics_no.setText("");
			clear_input.setVisibility(View.GONE);
			break;
		}

	}
	/**
	 * 弹出时间
	 */
	private void pushDate(){
		mDateDialog = new SelectDateDialog(LogisticsRecordsCheckActivity.this,true);
		mDateDialog.show();
		mDateDialog.getTitle().setText("时间");
		this.mDateDialog.updateDays(selectDate);
		this.mDateDialog.getmClearDate().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDateDialog.dismiss();
				date.setText("请选择");
				sendEndTime = null;
			}
		});
		
		mDateDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDateDialog.dismiss();
				selectDate = mDateDialog.getCurrentData();
				date.setText(selectDate);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");
				if (selectDate!=null) {
					try {
						sendEndTime = (sdf.parse((selectDate+" 00:00:00"))).getTime();
					} catch (ParseException e) {
						sendEndTime = null;
					}
				}
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
