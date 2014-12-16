package com.dfire.retail.app.manage.activity.stockmanager;

import java.math.BigDecimal;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.StockAdjustVo;
import com.dfire.retail.app.manage.data.bo.StockAdjustGoodsInfoBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;

/**
 * 库存调整  商品信息
 * @author ys
 *
 */
@SuppressLint({ "SimpleDateFormat", "UseValueOf" })
public class StockGoodInfoActivity extends TitleActivity implements OnClickListener,IItemListListener{

	private TextView goods_name,barCode,nowStore,purchasePrice,retailPrice,resultPrice;
	
	private ItemEditList adjustReasonId;
	
	private ItemEditText adjustStore;
	
	private LinearLayout goods_price_view;
	
	private View price_view;
	
	private Button delete;
	
	private String shopId,goodName,goodId,mBarCode;
	
	private StockAdjustVo adjustVo;
	
	private String activity;
	
	private RetailApplication application;
	
	private HashMap<String, Object> map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_good_info);
		showBackbtn();
		setTitleText("商品信息");
		this.init();
	}
	/**
	 * 初始化控件
	 */
	@SuppressWarnings("static-access")
	private void init(){
		this.application = (RetailApplication) getApplication();
		this.map = application.getObjMap();
		this.goods_name = (TextView) this.findViewById(R.id.goods_name);
		this.barCode = (TextView) this.findViewById(R.id.barCode);
		this.nowStore = (TextView) this.findViewById(R.id.nowStore);
		this.adjustReasonId = (ItemEditList) this.findViewById(R.id.adjustReasonId);
		this.adjustReasonId.setIsChangeListener(this.getItemChangeListener());
		this.adjustReasonId.initLabel("调整原因", null,Boolean.TRUE,this);
		this.adjustReasonId.initData("请选择", "请选择");
		this.adjustReasonId.getImg().setImageResource(R.drawable.ico_next);
		this.purchasePrice = (TextView) this.findViewById(R.id.purchasePrice);
		this.retailPrice = (TextView) this.findViewById(R.id.retailPrice);
		this.resultPrice = (TextView) this.findViewById(R.id.resultPrice);
		this.adjustStore = (ItemEditText) this.findViewById(R.id.adjustStore);
		this.adjustStore.initLabel("调整数", null, Boolean.TRUE,InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_SIGNED);
		this.adjustStore.setMaxLength(6);
		this.adjustStore.setTextColor(Color.parseColor("#0088cc"));
		this.adjustStore.setIsChangeListener(this.getItemChangeListener());
		this.goods_price_view = (LinearLayout) this.findViewById(R.id.goods_price_view);
		this.price_view = (View) this.findViewById(R.id.price_view);
		this.delete = (Button) this.findViewById(R.id.delete);

		this.activity = getIntent().getStringExtra("activity");
		if (StringUtils.isEquals(activity, "stockGoodInfoActivity")) {
			this.goodName = getIntent().getStringExtra("goodName");
			this.goodId = getIntent().getStringExtra("goodId");
			this.shopId = getIntent().getStringExtra("shopId");
			this.mBarCode = getIntent().getStringExtra("barCode");
		}else {
			this.delete.setVisibility(View.VISIBLE);
			adjustVo = (StockAdjustVo) map.get("returnAdjustmentAdd");
			this.goodName = adjustVo.getGoodsName();
			this.mBarCode = adjustVo.getBarCode();
			adjustReasonId.initData(adjustVo.getTypeName(), adjustVo.getTypeName());
			initData();
		}
		
		if (!StockAdjustmentActivity.instance.isVisPrice) {
			goods_price_view.setVisibility(View.GONE);
			price_view.setVisibility(View.GONE);
		}
		
		this.adjustReasonId.setOnClickListener(this);
		this.mRight.setOnClickListener(this);
		this.mLeft.setOnClickListener(this);
		this.delete.setOnClickListener(this);
		
		this.adjustStore.getLblVal().addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Integer sum = 0;
				try {
					sum = Integer.parseInt(s+"");
				} catch (NumberFormatException e) {
					sum = 0;
				}
				resultPrice.setText(String.valueOf((new BigDecimal(purchasePrice.getText().toString())).multiply(new BigDecimal(sum))));
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		if (goodId!=null&&!goodId.equals("")) {
			this.findAdjustById();
		}
	}
	/**
	 * 初始化数据
	 */
	private void initData(){
		if (adjustVo!=null) {
			this.goods_name.setText(goodName);
			this.barCode.setText(mBarCode);
			this.nowStore.setText(adjustVo.getNowStore()+"");
			this.purchasePrice.setText(adjustVo.getPurchasePrice()+"");
			this.retailPrice.setText(adjustVo.getRetailPrice()+"");
			this.resultPrice.setText(adjustVo.getResultPrice()+"");
			this.adjustStore.changeData(adjustVo.getAdjustStore()+"");
			this.adjustStore.clearChange();
		}
	}
	/**
	 * 回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==100) {
			if (data!=null) {
				if (data.getStringExtra("typeVal")!=null&&data.getStringExtra("typeName")!=null) {
					if (adjustVo!=null) {
						adjustVo.setAdjustReasonId(data.getStringExtra("typeVal"));
						adjustVo.setTypeName(data.getStringExtra("typeName"));
						adjustReasonId.changeData(adjustVo.getTypeName(),adjustVo.getTypeName());
					}
				}
			}
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right:
			saveAdjustmentVo();
			break;
		case R.id.title_left:
			map.put("returnAdjustmentAdd", null);
			StockGoodInfoActivity.this.finish();
			break;
		case R.id.delete:
			final AlertDialog alertDialog = new AlertDialog(this);
			alertDialog.setMessage(getResources().getString(R.string.isdelete));
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener() {
				@Override
				public void onClick(View v) {
					map.put("returnAdjustmentAdd", null);
					StockAdjustmentAddActivity.instance.removeItem(adjustVo.getGoodsId());
					StockGoodInfoActivity.this.finish();
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
	 * 保存
	 */
	private void saveAdjustmentVo(){
		String adjustStr = adjustStore.getStrVal();
		if (StringUtils.isEmpty(adjustStr)||StringUtils.isEquals(adjustStr, "0")) {
			new ErrDialog(this, getResources().getString(R.string.please_print_success_number)).show();
		}else {
			Integer number = 0;
			try {
				number = Integer.parseInt(adjustStore.getStrVal());
			} catch (NumberFormatException e) {
				number = null;
			}
			if (number==null) {
				new ErrDialog(this, getResources().getString(R.string.please_print_success_number)).show();
			}else {
				if (!StringUtils.isEmpty(adjustReasonId.getStrVal().toString())&&!StringUtils.isEquals(adjustReasonId.getStrVal().toString(), "请选择")) {
					if (adjustVo!=null) {
						adjustVo.setAdjustStore(number);
						adjustVo.setResultPrice(new BigDecimal(resultPrice.getText().toString()));
						if(StringUtils.isEquals(activity, "stockGoodInfoActivity")) {
							adjustVo.setGoodsName(goodName);
							adjustVo.setGoodsId(goodId);
							adjustVo.setBarCode(mBarCode);
							StockAddGoodsActivity.instance.finish();
						}
						map.put("returnAdjustmentAdd", adjustVo);
						StockGoodInfoActivity.this.finish();
					}else {
						new ErrDialog(this, getResources().getString(R.string.not_adjust_goods)).show();
					}
				}else {
					new ErrDialog(this, getResources().getString(R.string.please_select_reason)).show();
				}
			}
		}
		
	}
	
	/**
	 * 根据goodId 查询库存详情 stockAdjust/getGoodsInfo
	 */
	private void findAdjustById(){
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockAdjust/getGoodsInfo");
		params.setParam("shopId", shopId);
		params.setParam("goodsId", goodId);
		new AsyncHttpPost(this, params, StockAdjustGoodsInfoBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				StockAdjustGoodsInfoBo bo = (StockAdjustGoodsInfoBo)oj;
				if (bo!=null) {
					adjustVo = new StockAdjustVo();
					adjustVo = bo.getStockAdjustVo();
					initData();
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}
	@Override
	public void onItemListClick(ItemEditList obj) {
		int position = Integer.parseInt(String.valueOf(obj.getTag()));
		switch (position) {
		case 1:        	
			Intent reasonIntent = new Intent();
			reasonIntent.setClass(StockGoodInfoActivity.this, AdjustmentReasonActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("adjustVo", adjustVo);
			reasonIntent.putExtras(bundle);
			startActivityForResult(reasonIntent, 100);
			break;
		}
	}
}
