package com.dfire.retail.app.manage.activity.logisticmanager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.item.StoreCollectGoodsItem;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.data.StockInDetailVo;
import com.dfire.retail.app.manage.util.StringUtils;
/**
 * 进货的单商品信息
 * @author ys
 *
 */
@SuppressLint({ "SimpleDateFormat", "UseValueOf" })
public class StoreCollectInfoActivity extends TitleActivity implements OnClickListener,IItemListListener{

	private TextView goods_name,goods_code;
	
	private ItemEditList goods_date;
	
	private ItemEditText goods_num,goods_price;
	
	private SearchGoodsVo searchGoodsVo;
	
	private StockInDetailVo mStockInDetailVo;
	
	private SelectDateDialog mDateDialog;
	
	private String selectDate = null;
	
	private Long sendEndTime;
	
	private LinearLayout goods_price_view;
	
	private View price_view;
	
	private String activity;
	
	private Button delete;
	
	private RetailApplication application;
	
	private HashMap<String, Object> map;
	
	private Integer number = 0;
	
	private BigDecimal price = null;
	
	private String isPrice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_collect_info);
		showBackbtn();
		setTitleText("商品信息");
		this.init();
		this.initData();
	}
	/**
	 * 初始化控件
	 */
	private void init(){
		this.isPrice = getIntent().getStringExtra("isPrice");
		this.application = (RetailApplication) getApplication();
		this.map = application.getObjMap();
		this.activity = getIntent().getStringExtra("activity");
		this.goods_name = (TextView) this.findViewById(R.id.goods_name);
		this.goods_code = (TextView) this.findViewById(R.id.goods_code);
		this.goods_price = (ItemEditText) this.findViewById(R.id.goods_price);
		this.goods_price.initLabel("进货价(元)", null, Boolean.TRUE, InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
		this.goods_price.setMaxLength(8);
		this.goods_price.setTextColor(Color.parseColor("#0088cc"));
		this.goods_price.setIsChangeListener(this.getItemChangeListener());
		this.goods_num = (ItemEditText) this.findViewById(R.id.goods_num);
		this.goods_num.initLabel("进货数量", null, Boolean.TRUE,InputType.TYPE_CLASS_NUMBER);
		this.goods_num.setMaxLength(6);
		this.goods_num.setTextColor(Color.parseColor("#0088cc"));
		this.goods_num.setIsChangeListener(this.getItemChangeListener());
		this.goods_date = (ItemEditList) this.findViewById(R.id.goods_date);
		this.goods_date.initLabel("生产日期", "",Boolean.FALSE,this);
		this.goods_date.initData("请选择", "请选择");
		this.goods_date.setIsChangeListener(this.getItemChangeListener());
		this.goods_price_view = (LinearLayout) this.findViewById(R.id.goods_price_view);
		this.price_view = (View) this.findViewById(R.id.price_view);
		this.delete = (Button) this.findViewById(R.id.delete);
		if (isPrice.equals("false")) {
			goods_price_view.setVisibility(View.GONE);
			price_view.setVisibility(View.GONE);
		}
		this.mDateDialog = new SelectDateDialog(StoreCollectInfoActivity.this, true);//时间
		this.goods_date.setOnClickListener(this);
		this.mRight.setOnClickListener(this);
		this.delete.setOnClickListener(this);
	}
	/**
	 * 初始化数据
	 */
	private void initData(){
		if (activity.equals("storeOrderAddGoodsActivity")) {
			searchGoodsVo = (SearchGoodsVo) map.get("returnCollectAdd");
			searchGoodsVo.getGoodsId();
			this.goods_name.setText(searchGoodsVo.getGoodsName());
			this.goods_code.setText(searchGoodsVo.getBarcode());
			this.goods_price.initData(String.format("%.2f", searchGoodsVo.getPurchasePrice()));
			this.goods_num.initData("");
		}else{
			mStockInDetailVo = (StockInDetailVo) map.get("returnCollectAdd");
			this.delete.setVisibility(View.VISIBLE);
			mStockInDetailVo.getGoodsId();
			this.goods_name.setText(mStockInDetailVo.getGoodsName());
			this.goods_code.setText(mStockInDetailVo.getGoodsBarcode());
			this.goods_price.initData(String.format("%.2f", mStockInDetailVo.getGoodsPrice()));
			this.goods_num.initData(mStockInDetailVo.getGoodsSum()+"");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (mStockInDetailVo.getProductionDate()!=null) {
				String dateStr = sdf.format(new Date(mStockInDetailVo.getProductionDate()));
				this.goods_date.initData(dateStr, dateStr);
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
				goods_date.initData("请选择", "请选择");
				sendEndTime = null;
			}
			
		});
		mDateDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDateDialog.dismiss();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");
				selectDate = mDateDialog.getCurrentData();
				goods_date.changeData(selectDate, selectDate);
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
		switch (v.getId()) {
		case R.id.title_right:
			if (isPrice.equals("false")) {
				if (checkNum()) {
					save();
				}
			}else {
				if (checkPrice()) {
					if (checkNum()) {
						save();
					}
				}
			}
			break;
		case R.id.delete:
			final AlertDialog alertDialog = new AlertDialog(this);
			alertDialog.setMessage(getResources().getString(R.string.isdelete));
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener() {
				@Override
				public void onClick(View v) {
					StoreCollectGoodsItem goodsItem = StoreCollectAddActivity.instance.collectHashMap.get(mStockInDetailVo.getGoodsId());
					if (goodsItem!=null) {
						map.put("returnCollectAdd", null);
						StoreCollectAddActivity.instance.removeView(goodsItem);
						StoreCollectInfoActivity.this.finish();
					}
					alertDialog.dismiss();
				}
			});
			alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener() {
				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
				}
			});
			break;
		default:
			break;
		}
	}
	/**
	 * 判断价格
	 */
	private boolean checkPrice(){
		String goodPrice = goods_price.getStrVal();
		if (StringUtils.isEmpty(goodPrice)||StringUtils.isEquals(goodPrice, "0")) {
			new ErrDialog(StoreCollectInfoActivity.this, getResources().getString(R.string.coller_goods_price)).show();
			return false;
		}else {
			try {
				price = new BigDecimal(goodPrice);
			} catch (NumberFormatException e) {
				price = null;
			}
			if (price!=null) {
				if (price.signum()>0) {
					return true;
				}else if(price.signum()==0) {
					new ErrDialog(StoreCollectInfoActivity.this, getResources().getString(R.string.coller_goods_price)).show();
					return false;
				}else {
					new ErrDialog(StoreCollectInfoActivity.this,getResources().getString(R.string.please_collect_notnull)).show();
					return false;
				}
			}else {
				new ErrDialog(StoreCollectInfoActivity.this, getResources().getString(R.string.please_collect_notnull)).show();
				return false;
			}
		}
	}
	/**
	 * 判断数量
	 */
	private boolean checkNum(){
		String goodNum = goods_num.getStrVal();
		if (StringUtils.isEmpty(goodNum)||StringUtils.isEquals(goodNum, "0")) {
			new ErrDialog(StoreCollectInfoActivity.this, getResources().getString(R.string.LM_MSG_000012)).show();
			return false;
		}else {
			try {
				number = Integer.parseInt(goodNum);
			} catch (NumberFormatException e) {
				number = null;
			}
			if (number==null) {
				new ErrDialog(StoreCollectInfoActivity.this, getResources().getString(R.string.please_print_success_number)).show();
				return false;
			}else {
				return true;
			}
		}
	}
	/**
	 * 保存
	 */
	private void save(){
		StockInDetailVo stockInDetailVo = new StockInDetailVo();
		if (activity.equals("storeOrderAddGoodsActivity")) {
			stockInDetailVo.setGoodsId(searchGoodsVo.getGoodsId());
			stockInDetailVo.setGoodsName(searchGoodsVo.getGoodsName());
			stockInDetailVo.setGoodsBarcode(searchGoodsVo.getBarcode());
			if (isPrice.equals("false")) {
				stockInDetailVo.setGoodsPrice(searchGoodsVo.getPurchasePrice());
			}else {
				stockInDetailVo.setGoodsPrice(price);
			}
			stockInDetailVo.setGoodsTotalPrice(searchGoodsVo.getPurchasePrice().multiply(new BigDecimal(number)));
			stockInDetailVo.setGoodsSum(number);
			stockInDetailVo.setProductionDate(sendEndTime);
			map.put("returnCollectAdd", stockInDetailVo);
			StoreCollectInfoActivity.this.finish();
			StoreOrderAddGoodsActivity.instance.finish();
		}else{
			stockInDetailVo = mStockInDetailVo;
			stockInDetailVo.setGoodsSum(number);
			if (isPrice.equals("true")) {
				stockInDetailVo.setGoodsPrice(price);
			}
			stockInDetailVo.setProductionDate(sendEndTime);
			
			StoreCollectAddActivity.instance.changeGoodInfo(stockInDetailVo);
			StoreCollectInfoActivity.this.finish();
		}
	
	}
	@Override
	public void onItemListClick(ItemEditList obj) {
		int position = Integer.parseInt(String.valueOf(obj.getTag()));
		switch (position) {
		case 1:     
			pushDate();
			break;
		}
	}
}
