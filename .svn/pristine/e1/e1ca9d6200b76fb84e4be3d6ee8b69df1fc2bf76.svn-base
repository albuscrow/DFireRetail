/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.LogisticsDetailVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

/**
 * 物流记录明细页面
 * @author 李锦运
 *2014-10-28
 */
public class LogisticsRecordDetailActivity extends TitleActivity{
	
	private List<LogisticsDetailVo> logisticsDetailList = new ArrayList<LogisticsDetailVo>();
	
	private LinearLayout logisticsDetails;
	
	private List<LogisticsRecordDetailItem> itemList=new ArrayList<LogisticsRecordDetailItem>();
	
	private TextView txtSupplyName,txtTypeName,txtLogisticsNo,txtSendStartTIme,txtTotalPrice,txtTotalSum;
	
	private String logisticsNo,supplyName,shopName,typeName;
	
	private int goodsTotalSum;
	
	private Double goodsTotalPrice = 0d;
	
	private Long sendEndTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logistics_records_detail);
		setTitleText("物流记录");
		
		showBackbtn();
		findView();
	}
	public void findView(){
		txtSupplyName = (TextView) findViewById(R.id.txt_supply_name);
		txtTypeName = (TextView) findViewById(R.id.txt_type_name);
		txtLogisticsNo = (TextView) findViewById(R.id.logistics_no);
		txtSendStartTIme = (TextView) findViewById(R.id.send_start_time);
		txtTotalPrice = (TextView) findViewById(R.id.total_price);
		txtTotalSum = (TextView) findViewById(R.id.total_sum);
		logisticsDetails = (LinearLayout) findViewById(R.id.content_list);
		initData();
	}
	
	private void initViewData(){
		txtSupplyName.setText("("+supplyName+")");
		txtTypeName.setText(typeName);
		txtLogisticsNo.setText(logisticsNo);
		txtSendStartTIme.setText(sendEndTime == 0 ?"":new SimpleDateFormat("yyyy-MM-dd").format(sendEndTime));
		txtTotalPrice.setText(goodsTotalPrice.toString());
		txtTotalSum.setText(goodsTotalSum+"");
	}
	//创建明细项
	public void createItemView(){
		logisticsDetails.removeAllViews();
		if(logisticsDetailList !=null && logisticsDetailList.size() >0){		
			LogisticsRecordDetailItem item=null;
			for (LogisticsDetailVo vo : logisticsDetailList) {
				item=new LogisticsRecordDetailItem(this, null, this);
				item.initWithData(vo);
				logisticsDetails.addView(item.getItemView());
				itemList.add(item);
			}
		}
	}
	
	public void initData(){
		Intent intent = getIntent();
		String logisticsId = intent.getStringExtra("logisticsId");
		String recordType = intent.getStringExtra("recordType");
		sendEndTime = intent.getLongExtra("sendEndTime", 0);
		RequestParameter params = new RequestParameter();
		
		params.setUrl(Constants.LOGISTICS_DETAIL);	
		params.setParam("logisticsId", logisticsId);
		params.setParam("recordType", recordType);
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				JsonUtil ju = new JsonUtil(str);
				List<LogisticsDetailVo> resultList = (List<LogisticsDetailVo>) ju.get("logisticsDetailList", new TypeToken<List<LogisticsDetailVo>>() {}.getType());
				logisticsNo = (String) ju.get("logisticsNo", new TypeToken<String>() {}.getType());
				supplyName = (String) ju.get("supplyName", new TypeToken<String>() {}.getType());
				shopName = (String) ju.get("shopName", new TypeToken<String>() {}.getType());
				typeName = (String) ju.get("typeName", new TypeToken<String>() {}.getType());
				goodsTotalSum = (Integer) ju.get("goodsTotalSum", new TypeToken<Integer>() {}.getType());
				goodsTotalPrice = (Double) ju.get("goodsTotalPrice", new TypeToken<Double>() {}.getType());
				
				logisticsDetailList.clear();
				logisticsDetailList.addAll(resultList);
				
				initViewData();
				createItemView();
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}).execute();
	}
}
