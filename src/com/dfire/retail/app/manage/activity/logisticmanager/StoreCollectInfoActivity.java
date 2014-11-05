package com.dfire.retail.app.manage.activity.logisticmanager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.item.StoreCollectGoodsItem;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.data.StockInDetailVo;
/**
 * 进货的单商品信息
 * @author ys
 *
 */
@SuppressLint({ "SimpleDateFormat", "UseValueOf" })
public class StoreCollectInfoActivity extends Activity implements OnClickListener{

	private ImageButton cancel,save;
	
	private TextView goods_name,goods_code,goods_price,goods_num,goods_date;
	
	private SearchGoodsVo searchGoodsVo;
	
	private StockInDetailVo mStockInDetailVo;
	
	private SelectDateDialog mDateDialog;
	
	private String selectDate = null;
	
	private Long sendEndTime;
	
	private LinearLayout goods_price_view;
	
	private View price_view;
	
	private String activity;
	
	private Button delete;
	
	private Integer goodsNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_collect_info);
		
		this.init();
		this.initData();
	}
	/**
	 * 初始化控件
	 */
	@SuppressWarnings("static-access")
	private void init(){
		activity = getIntent().getStringExtra("activity");
		this.cancel = (ImageButton) this.findViewById(R.id.cancel);
		this.save = (ImageButton) this.findViewById(R.id.save);
		this.goods_name = (TextView) this.findViewById(R.id.goods_name);
		this.goods_code = (TextView) this.findViewById(R.id.goods_code);
		this.goods_price = (TextView) this.findViewById(R.id.goods_price);
		this.goods_num = (TextView) this.findViewById(R.id.goods_num);
		this.goods_date = (TextView) this.findViewById(R.id.goods_date);
		this.goods_price_view = (LinearLayout) this.findViewById(R.id.goods_price_view);
		this.price_view = (View) this.findViewById(R.id.price_view);
		this.delete = (Button) this.findViewById(R.id.delete);
		if (!StoreCollectActivity.instance.isVisPrice) {
			goods_price_view.setVisibility(View.GONE);
			price_view.setVisibility(View.GONE);
		}

		  this.goods_num.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					try {
						goodsNumber = Integer.parseInt(s+"");
					} catch (NumberFormatException e) {
						goodsNumber = 0;
					}
					if (goodsNumber<=0) {
						final AlertDialog alertDialog = new AlertDialog(StoreCollectInfoActivity.this);
						alertDialog.setMessage("是否删除该项!");
						alertDialog.setCanceledOnTouchOutside(false);
						alertDialog.setPositiveButton("确定", new OnClickListener() {
							@Override
							public void onClick(View v) {
								alertDialog.dismiss();
								
								if (activity.equals("storeOrderAddGoodsActivity")) {
									StoreCollectInfoActivity.this.finish();
									StoreOrderAddGoodsActivity.instance.finish();
								}else {
									Integer sum;
									try {
										sum = Integer.valueOf(goods_num.getText().toString());
									} catch (NumberFormatException e) {
										sum = 0;
									}
									StockInDetailVo stockInDetailVo = new StockInDetailVo();
									stockInDetailVo = mStockInDetailVo;
									stockInDetailVo.setGoodsSum(sum);
									stockInDetailVo.setProductionDate(sendEndTime);
									
									StoreCollectAddActivity.instance.changeGoodInfo(stockInDetailVo,Integer.parseInt(activity));
									StoreCollectInfoActivity.this.finish();
								}
							}
						});
						alertDialog.setNegativeButton("取消", new OnClickListener() {
							@Override
							public void onClick(View v) {
								goods_num.setText("1");
								alertDialog.dismiss();
							}
						});
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

		this.mDateDialog = new SelectDateDialog(StoreCollectInfoActivity.this, true);//时间
		this.cancel.setOnClickListener(this);
		this.goods_date.setOnClickListener(this);
		this.save.setOnClickListener(this);
		this.delete.setOnClickListener(this);
	}
	/**
	 * 初始化数据
	 */
	private void initData(){
		RetailApplication application = (RetailApplication) getApplication();
		HashMap<String, Object> map = application.getObjMap();
		if (activity.equals("storeOrderAddGoodsActivity")) {
			searchGoodsVo = (SearchGoodsVo) map.get("returnCollectAdd");
			searchGoodsVo.getGoodsId();
			this.goods_name.setText(searchGoodsVo.getGoodsName());
			this.goods_code.setText(searchGoodsVo.getBarcode());
			this.goods_price.setText(String.format("%.2f", searchGoodsVo.getPurchasePrice()));
			this.goods_num.setText("1");
		}else{
			mStockInDetailVo = (StockInDetailVo) map.get("returnCollectAdd");
			this.delete.setVisibility(View.VISIBLE);
			mStockInDetailVo.getGoodsId();
			this.goods_name.setText(mStockInDetailVo.getGoodsName());
			this.goods_code.setText(mStockInDetailVo.getGoodsBarcode());
			this.goods_price.setText(String.format("%.2f", mStockInDetailVo.getGoodsPrice()));
			this.goods_num.setText(mStockInDetailVo.getGoodsSum()+"");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (mStockInDetailVo.getProductionDate()!=null) {
				this.goods_date.setText(sdf.format(new Date(mStockInDetailVo.getProductionDate())));
			}
		}
		map.put("returnCollectAdd", null);
	}
	/**
	 * 弹出日期
	 */
	private void pushDate(){
		mDateDialog.show();
		mDateDialog.getTitle().setText("生产日期");
		mDateDialog.updateDays(selectDate);
		mDateDialog.getmClearDate().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDateDialog.dismiss();
				goods_date.setText("请选择");
				sendEndTime = null;
			}
			
		});
		mDateDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDateDialog.dismiss();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");
				selectDate = mDateDialog.getCurrentData();
				goods_date.setText(selectDate);
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
	@Override
	public void onClick(View v) {
		if (v==goods_date) {
			this.pushDate();
		}else if (v==cancel) {
			finish();
		}else if (v==save) {
			RetailApplication application = (RetailApplication) getApplication();
			HashMap<String, Object> map = application.getObjMap();
			StockInDetailVo stockInDetailVo = new StockInDetailVo();
			Integer sum;
			try {
				sum = Integer.valueOf(goods_num.getText().toString());
			} catch (NumberFormatException e) {
				sum = 0;
			}
			BigDecimal count = new BigDecimal(sum);
			if (activity.equals("storeOrderAddGoodsActivity")) {
				stockInDetailVo.setGoodsId(searchGoodsVo.getGoodsId());
				stockInDetailVo.setGoodsName(searchGoodsVo.getGoodsName());
				stockInDetailVo.setGoodsBarcode(searchGoodsVo.getBarcode());
				stockInDetailVo.setGoodsPrice(searchGoodsVo.getPurchasePrice());
				stockInDetailVo.setGoodsTotalPrice(searchGoodsVo.getPurchasePrice().multiply(count));
				stockInDetailVo.setGoodsSum(sum);
				stockInDetailVo.setProductionDate(sendEndTime);
				map.put("returnCollectAdd", stockInDetailVo);
				StoreCollectInfoActivity.this.finish();
				StoreOrderAddGoodsActivity.instance.finish();
			}else{
				stockInDetailVo = mStockInDetailVo;
				stockInDetailVo.setGoodsSum(sum);
				stockInDetailVo.setProductionDate(sendEndTime);
				
				StoreCollectAddActivity.instance.changeGoodInfo(stockInDetailVo, Integer.parseInt(activity));
				StoreCollectInfoActivity.this.finish();
			}
		}else if (v==delete) {
			StoreCollectGoodsItem goodsItem = StoreCollectAddActivity.instance.collectHashMap.get(mStockInDetailVo.getGoodsId());
			if (goodsItem!=null) {
				StoreCollectAddActivity.instance.removeView(goodsItem);
				StoreCollectInfoActivity.this.finish();
			}
		}
	}
}