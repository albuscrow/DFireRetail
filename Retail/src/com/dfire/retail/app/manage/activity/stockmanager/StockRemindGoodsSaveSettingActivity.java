package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemEditRadio;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.data.StockInfoAlertVo;
import com.dfire.retail.app.manage.data.bo.ReturnNotMsgBo;
import com.dfire.retail.app.manage.data.bo.StockInfoAlertBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * 提醒设置
 * @author ys
 */
public class StockRemindGoodsSaveSettingActivity extends TitleActivity implements OnClickListener{

	private TextView code,goodname;
	
	private ItemEditRadio isAlertNum,isAlertDay;
	
	private ItemEditText alertNum,alertDay;
	
	private ArrayList<String> ids;
	
	private ArrayList<GoodsVo> goods;
	
	private String activity;
	
	private StockInfoAlertVo stockInfoAlertVo;
	
	private LinearLayout good_info_lv,alertnumlayout,alertdaylayout;
	
	private String shopId;
	
	private View numview,dayview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind_goods_save);
		showBackbtn();
		setTitleText("提醒设置");
		findView();
	}
	/**
	 * 得到控件
	 */
	@SuppressWarnings("unchecked")
	private void findView(){
		
		this.ids = (ArrayList<String>) getIntent().getSerializableExtra(Constants.GOODSIDS);
		this.goods = (ArrayList<GoodsVo>) getIntent().getSerializableExtra(Constants.GOODS);
		this.activity = getIntent().getStringExtra("activity");
		this.shopId = getIntent().getStringExtra(Constants.SHOP_ID);
		
		this.alertNum = (ItemEditText)findViewById(R.id.alertNum);
		this.alertDay = (ItemEditText)findViewById(R.id.alertDay);
		this.isAlertNum = (ItemEditRadio)findViewById(R.id.isAlertNum);
		this.isAlertDay = (ItemEditRadio)findViewById(R.id.isAlertDay);
		this.code = (TextView)findViewById(R.id.code);
		this.goodname = (TextView)findViewById(R.id.goodname);
		this.good_info_lv = (LinearLayout)findViewById(R.id.good_info_lv);
		this.alertnumlayout = (LinearLayout)findViewById(R.id.alertnumlayout);
		this.alertdaylayout = (LinearLayout)findViewById(R.id.alertdaylayout);
		this.numview = (View)findViewById(R.id.numview);
		this.dayview = (View)findViewById(R.id.dayview);
		
		this.mRight.setOnClickListener(this);
		
		initView();
	}
	/**
	 * 初始化控件
	 */
	private void initView(){
		this.alertNum.initLabel("库存数量少于多少时提醒", null,Boolean.TRUE,InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_SIGNED);
		this.alertNum.setMaxLength(6);
		this.alertNum.setTextColor(Color.parseColor("#0088cc"));
		this.alertNum.setIsChangeListener(this.getItemChangeListener());
		this.alertDay.initLabel("保质期限低于多少天提醒", null, Boolean.TRUE,InputType.TYPE_CLASS_NUMBER);
		this.alertDay.setMaxLength(6);
		this.alertDay.setTextColor(Color.parseColor("#0088cc"));
		this.alertDay.setIsChangeListener(this.getItemChangeListener());
		this.isAlertNum.initLabel("库存数量提醒", null);  
		this.isAlertNum.setIsChangeListener(this.getItemChangeListener());
		this.isAlertDay.initLabel("保质期提醒", null);
		this.isAlertDay.setIsChangeListener(this.getItemChangeListener());
		this.isAlertNum.getBtn().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isAlertNum.setOnclik();
				if (isAlertNum.getStrVal().equals("0")) {
					alertnumlayout.setVisibility(View.GONE);
					numview.setVisibility(View.GONE);
				}else {
					alertnumlayout.setVisibility(View.VISIBLE);
					numview.setVisibility(View.VISIBLE);
				}
			}
		});
		this.isAlertDay.getBtn().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isAlertDay.setOnclik();
				if (isAlertDay.getStrVal().equals("0")) {
					alertdaylayout.setVisibility(View.GONE);
					dayview.setVisibility(View.GONE);
				}else {
					alertdaylayout.setVisibility(View.VISIBLE);
					dayview.setVisibility(View.VISIBLE);
				}
			}
		});
		/**
		 * 全选 初始化
		 */
		this.isAlertNum.changeData(String.valueOf(0));
		this.isAlertDay.changeData(String.valueOf(0));
		this.isAlertNum.clearChange();
		this.isAlertDay.clearChange();
		
		if (activity.equals("stockRemindSettingGoodsListActivity")) {
			String goodsId = ids.get(0);
			this.getAlert(goodsId);
			if (goods!=null) {
				for (int i = 0; i < goods.size(); i++) {
					if (goods.get(i).getGoodsId()!=null) {
						if (goods.get(i).getGoodsId().equals(goodsId)) {
							this.code.setText(goods.get(i).getBarCode());
							this.goodname.setText(goods.get(i).getGoodsName());
						}
					}
				}
			}
		}else if (activity.equals("stockRemindGoodsListActivity")) {
			good_info_lv.setVisibility(View.GONE);
			stockInfoAlertVo = new StockInfoAlertVo();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right:
			if (alertDay.getStrVal()!=null&&!alertDay.getStrVal().equals("")&&alertNum.getStrVal()!=null&&!alertNum.getStrVal().equals("")) {
				Integer number = null;
				try {
					number = Integer.parseInt(alertNum.getStrVal());
				} catch (NumberFormatException e) {
					number = null;
				}
				if (number!=null) {
					stockInfoAlertVo.setAlertDay(Integer.parseInt(alertDay.getStrVal()));
					stockInfoAlertVo.setAlertNum(number);
					stockInfoAlertVo.setIsAlertDay(Short.parseShort(isAlertDay.getStrVal()));
					stockInfoAlertVo.setIsAlertNum(Short.parseShort(isAlertNum.getStrVal()));
					setAlert(stockInfoAlertVo);
				}else {
					new ErrDialog(StockRemindGoodsSaveSettingActivity.this, getResources().getString(R.string.please_print_success_number)).show();
				}
			}else {
				new ErrDialog(StockRemindGoodsSaveSettingActivity.this, getResources().getString(R.string.please_print_success_number)).show();
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 查询提醒设置详情  （单个商品点击）
	 */
	private void getAlert(String goodsId){
		
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockInfoAlert/getAlert");
		params.setParam(Constants.SHOP_ID, shopId);
		params.setParam(Constants.GOODS_ID, goodsId);
		new AsyncHttpPost(this, params, StockInfoAlertBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				StockInfoAlertBo bo = (StockInfoAlertBo)oj;
				if (bo!=null) {
					stockInfoAlertVo = bo.getStockInfoAlertVo();
					if (stockInfoAlertVo!=null) {
						/**初始化值*/
						alertNum.changeData(String.valueOf(stockInfoAlertVo.getAlertNum()));
						alertDay.changeData(String.valueOf(stockInfoAlertVo.getAlertDay()));
						isAlertNum.changeData(String.valueOf(stockInfoAlertVo.getIsAlertNum()));
						isAlertDay.changeData(String.valueOf(stockInfoAlertVo.getIsAlertDay()));
						/**初始化的时候清空未保存按钮*/
						alertNum.clearChange();
						alertDay.clearChange();
						isAlertNum.clearChange();
						isAlertDay.clearChange();
						if (isAlertNum.getStrVal().equals("1")) {
							alertnumlayout.setVisibility(View.VISIBLE);
							numview.setVisibility(View.VISIBLE);
						}
						if (isAlertDay.getStrVal().equals("1")) {
							alertdaylayout.setVisibility(View.VISIBLE);
							dayview.setVisibility(View.VISIBLE);
						}
					}
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}
	/**
	 * 保存 提醒设置/
	 */
	private void setAlert(StockInfoAlertVo stockInfoAlertVo){
		if (stockInfoAlertVo!=null&&ids!=null&&ids.size()>0) {
			RequestParameter params = new RequestParameter(true);
			params.setUrl(Constants.BASE_URL + "stockInfoAlert/setAlert");
			params.setParam(Constants.SHOP_ID, shopId);
			try {
				params.setParam("stockInfoAlertVo", new JSONObject(new GsonBuilder().serializeNulls().create().toJson(stockInfoAlertVo)));
				params.setParam("goodsIds", new JSONArray(new Gson().toJson(ids)));
			} catch (JSONException e1) {
				new ErrDialog(StockRemindGoodsSaveSettingActivity.this, getResources().getString(R.string.please_print_remindinfo)).show();
				return;
			}
			
			new AsyncHttpPost(this, params, ReturnNotMsgBo.class, new RequestCallback() {
				@Override
				public void onSuccess(Object oj) {
					ReturnNotMsgBo bo = (ReturnNotMsgBo)oj;
					if (bo!=null) {
						StockRemindGoodsSaveSettingActivity.this.finish();
					}
				}
				@Override
				public void onFail(Exception e) {
					// TODO Auto-generated method stub
				}
			}).execute();
		}else {
			new ErrDialog(StockRemindGoodsSaveSettingActivity.this, getResources().getString(R.string.please_print_remindinfo)).show();
		}
	}
}
