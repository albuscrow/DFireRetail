package com.dfire.retail.app.manage.activity.logisticmanager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.LogisticsRecordDetailAdapter;
import com.dfire.retail.app.manage.data.LogisticsDetailVo;
import com.dfire.retail.app.manage.data.LogisticsVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.LogisticsDetailBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;

/**
 * 物流记录明细页面
 * @author ys
 * 2014-11-14
 */
@SuppressLint("SimpleDateFormat")
public class LogisticsRecordDetailActivity extends TitleActivity{
	
	private List<LogisticsDetailVo> logisticsDetailList = new ArrayList<LogisticsDetailVo>();
	
	private ListView logisticsDetails;
	
	private TextView txtSupplyName,txtTypeName,txtLogisticsNo,txtSendStartTIme,txtTotalPrice,txtTotalSum,priceText;
	
	private String logisticsNo,supplyName,typeName;
	
	private int goodsTotalSum;
	
	private BigDecimal goodsTotalPrice;
	
	private Long sendEndTime;
	
	private LogisticsVo logisticsVo;
	
	private String logisticsId;
	
	private String recordType;
	
	private LogisticsRecordDetailAdapter logisticsRecordDetailAdapter;
	
	private boolean isPrice = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logistics_records_detail);
		setTitleText("物流记录");
		showBackbtn();
		findView();
	}
	public void findView(){
		logisticsVo = (LogisticsVo)getIntent().getSerializableExtra("logisticsVo");
		txtSupplyName = (TextView) findViewById(R.id.txt_supply_name);
		txtTypeName = (TextView) findViewById(R.id.txt_type_name);
		TextPaint tp = txtTypeName.getPaint(); 
		tp.setFakeBoldText(true);
		txtLogisticsNo = (TextView) findViewById(R.id.logistics_no);
		txtSendStartTIme = (TextView) findViewById(R.id.send_start_time);
		txtTotalPrice = (TextView) findViewById(R.id.total_price);
		priceText = (TextView) findViewById(R.id.priceText);
		txtTotalSum = (TextView) findViewById(R.id.total_sum);
		//连锁
		if (RetailApplication.getShopVo().getType() == ShopVo.MENDIAN||RetailApplication.getShopVo().getType() == ShopVo.FENGBU) {
			isPrice = false;
			txtTotalPrice.setVisibility(View.GONE);
			priceText.setVisibility(View.GONE);
		}else{
			isPrice = true;
		}
		logisticsDetails = (ListView) findViewById(R.id.content_list);
		logisticsRecordDetailAdapter = new LogisticsRecordDetailAdapter(this, logisticsDetailList,isPrice);
		logisticsDetails.setAdapter(logisticsRecordDetailAdapter);
		if (logisticsVo!=null) {
			logisticsId = logisticsVo.getLogisticsId();
			recordType = logisticsVo.getRecordType();
			sendEndTime = logisticsVo.getSendEndTIme();
			initData();
		}
	}
	
	private void initViewData(){
		txtSupplyName.setText("("+supplyName+")");
		txtTypeName.setText(typeName);
		txtLogisticsNo.setText(logisticsNo);
		txtSendStartTIme.setText(sendEndTime == 0 ?"":new SimpleDateFormat("yyyy-MM-dd").format(sendEndTime));
		txtTotalPrice.setText(String.format("%.2f", goodsTotalPrice));
		txtTotalSum.setText(goodsTotalSum+"");
	}
	//获取详情
	public void initData(){
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.LOGISTICS_DETAIL);	
		params.setParam("logisticsId", logisticsId);
		params.setParam("recordType", recordType);
		new AsyncHttpPost(this, params, LogisticsDetailBo.class, new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				LogisticsDetailBo bo = (LogisticsDetailBo)oj;
				if (bo!=null) {
					List<LogisticsDetailVo> resultList = bo.getLogisticsDetailList();
					logisticsNo = bo.getLogisticsNo();
					supplyName = bo.getSupplyName();
					typeName = bo.getTypeName();
					goodsTotalSum = bo.getGoodsTotalSum();
					goodsTotalPrice = bo.getGoodsTotalPrice();
					Integer supplyIsHeadShop = bo.getSupplyIsHeadShop();
					if (RetailApplication.getShopVo().getType() == ShopVo.ZHONGBU){//总部登陆的时候显示进货价
						isPrice = true;
					}else {
						if (RetailApplication.getShopVo().getType() == ShopVo.FENGBU) {//分公司不显示进货价
							isPrice = false;
						}else {
							if (supplyIsHeadShop!=null) {
								if (supplyIsHeadShop==0) {//非总部登陆的时候 根据供应商判断   如果供应商是总部时不显示进货价 
									if (StringUtils.isEquals(recordType, "a")) {
										isPrice = false;
									}else {
										isPrice = true;
										txtTotalPrice.setVisibility(View.VISIBLE);
										priceText.setVisibility(View.VISIBLE);
									}
								}else {
									isPrice = false;
								}
							}
						}
					}
					if (resultList!=null&&resultList.size()>0) {
						logisticsDetailList.addAll(resultList);
					}
					logisticsRecordDetailAdapter.setIsPrice(isPrice);
					logisticsRecordDetailAdapter.notifyDataSetChanged();
					initViewData();	
				}
			}
			
			@Override
			public void onFail(Exception e) {
			}
		}).execute();
	}
}
