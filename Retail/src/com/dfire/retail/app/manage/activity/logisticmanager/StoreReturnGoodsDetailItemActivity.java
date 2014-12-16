/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.math.BigDecimal;
import java.util.HashMap;

import android.content.Intent;
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
import com.dfire.retail.app.manage.activity.item.StoreReturnGoodsDetailItem;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.vo.ReturnGoodsDetailVo;

/**
 * 商品详情  --  退货
 * @author ys 2014-11-12
 */
public class StoreReturnGoodsDetailItemActivity extends TitleActivity implements OnClickListener,IItemListListener {

	private SearchGoodsVo searchGoodsVo;
	
	private TextView goods_name,goods_code;
	
	private ItemEditList lsReason;
	
	private ItemEditText goods_num,goods_price;

	private LinearLayout goods_price_view;
	
	private View price_view;
	
	private DicVo dicVo;
	
	private HashMap<String, Object> map;
	
	private RetailApplication application;
	
	private String activity;
	
	private ReturnGoodsDetailVo mReturnGoodsDetailVo;
	
	private Button btn_delete;
	
	private BigDecimal price = null;
	
	private String isPrice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_return_goods_item_detail);
		showBackbtn();
		setTitleText("商品信息");
		initMainUI();
		initData();
	}

	private void initMainUI() {
		this.isPrice = getIntent().getStringExtra("isPrice");
		this.activity = getIntent().getStringExtra("activity");
		this.application = (RetailApplication) getApplication();
		this.map = application.getObjMap();
		this.dicVo = new DicVo();
		this.goods_name = (TextView) this.findViewById(R.id.goods_name);
		this.goods_code = (TextView) this.findViewById(R.id.goods_code);
		this.goods_price = (ItemEditText) this.findViewById(R.id.goods_price);
		this.goods_price.initLabel("退货价(元)", null, Boolean.TRUE,InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
		this.goods_price.setMaxLength(8);
		this.goods_price.setIsChangeListener(this.getItemChangeListener());
		this.goods_num = (ItemEditText) this.findViewById(R.id.goods_num);
		this.goods_num.initLabel("退货数量", null, Boolean.TRUE,InputType.TYPE_CLASS_NUMBER);
		this.goods_num.setMaxLength(6);
		this.goods_num.setTextColor(Color.parseColor("#0088cc"));
		this.goods_num.setIsChangeListener(this.getItemChangeListener());
		this.lsReason = (ItemEditList) this.findViewById(R.id.lsReason);
		this.lsReason.initLabel("退货原因", null,Boolean.TRUE,this);
		this.lsReason.setIsChangeListener(this.getItemChangeListener());
		this.lsReason.getImg().setImageResource(R.drawable.ico_next);
		this.goods_price_view = (LinearLayout) this.findViewById(R.id.goods_price_view);
		this.price_view = (View) this.findViewById(R.id.price_view);
		this.btn_delete = (Button) this.findViewById(R.id.btn_delete);
		
		this.btn_delete.setOnClickListener(this);
		this.mRight.setOnClickListener(this);
		this.lsReason.setOnClickListener(this);
	}

	private void initData() {
		if (isPrice.equals("false")) {
			goods_price_view.setVisibility(View.GONE);
			price_view.setVisibility(View.GONE);
		}
		if (activity.equals("storeReturnGoodsAddActivity")) {
			this.searchGoodsVo = (SearchGoodsVo) map.get("returnGoodsReason");
			this.goods_name.setText(searchGoodsVo.getGoodsName());
			this.goods_code.setText(searchGoodsVo.getBarcode());
			this.goods_price.initData(String.format("%.2f", searchGoodsVo.getPurchasePrice()));
			this.goods_num.initData("");
		}else {
			this.btn_delete.setVisibility(View.VISIBLE);
			this.mReturnGoodsDetailVo = (ReturnGoodsDetailVo) map.get("returnGoodsReason");
			this.goods_name.setText(mReturnGoodsDetailVo.getGoodsName());
			this.goods_code.setText(mReturnGoodsDetailVo.getGoodsBarcode());
			this.goods_price.initData(String.format("%.2f", mReturnGoodsDetailVo.getGoodsPrice()));
			this.goods_num.initData(String.valueOf(mReturnGoodsDetailVo.getGoodsSum()));
			this.lsReason.initData(mReturnGoodsDetailVo.getResonName(), mReturnGoodsDetailVo.getResonName());
			this.dicVo.setName(mReturnGoodsDetailVo.getResonName());
			this.dicVo.setVal(mReturnGoodsDetailVo.getResonVal());
		}
		map.put("returnGoodsReason", null);
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
					dicVo.setVal(Integer.parseInt(data.getStringExtra("typeVal")));
					dicVo.setName(data.getStringExtra("typeName"));
					lsReason.changeData(dicVo.getName(), dicVo.getName());
				}
			}
		}
	}
	/**判断数量*/
	private boolean valid() {
		if (StringUtils.isEmpty(goods_num.getStrVal())) {
			new ErrDialog(StoreReturnGoodsDetailItemActivity.this, getResources().getString(R.string.please_collect_notnull)).show();	
			return false;
		}else if(("0").equals(goods_num.getStrVal().trim())){
			new ErrDialog(StoreReturnGoodsDetailItemActivity.this, getResources().getString(R.string.return_0)).show();	
			return false;
		}
		return true;
	}
	/**
	 * 判断价格
	 */
	private boolean checkPrice(){
		if (isPrice.equals("false")) {
			return true;
		}else {
			String goodPrice = goods_price.getStrVal();
			if (StringUtils.isEmpty(goodPrice)||StringUtils.isEquals(goodPrice, "0")) {
				new ErrDialog(StoreReturnGoodsDetailItemActivity.this, getResources().getString(R.string.coller_goods_price)).show();
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
						new ErrDialog(StoreReturnGoodsDetailItemActivity.this, getResources().getString(R.string.coller_goods_price)).show();
						return false;
					}else {
						new ErrDialog(StoreReturnGoodsDetailItemActivity.this,getResources().getString(R.string.please_collect_notnull)).show();
						return false;
					}
				}else {
					new ErrDialog(StoreReturnGoodsDetailItemActivity.this, getResources().getString(R.string.please_collect_notnull)).show();
					return false;
				}
			}
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right:
			
			if (valid()&&checkPrice()) {
				Integer number = 0;
				try {
					number = Integer.parseInt(goods_num.getStrVal());
				} catch (NumberFormatException e) {
					number = null;
				}
				if (number==null) {
					new ErrDialog(StoreReturnGoodsDetailItemActivity.this, getResources().getString(R.string.please_collect_notnull)).show();	
				}else {
					if (!StringUtils.isEmpty(lsReason.getStrVal())&&!StringUtils.isEquals(lsReason.getStrVal(), "请选择")) {
						save();
					}else {
						new ErrDialog(StoreReturnGoodsDetailItemActivity.this, getResources().getString(R.string.please_select_return_reason)).show();	
					}
				}
			}
			break;
		case R.id.btn_delete:
			final AlertDialog alertDialog = new AlertDialog(this);
			alertDialog.setMessage(getResources().getString(R.string.isdelete));
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener() {
				@Override
				public void onClick(View v) {
					StoreReturnGoodsDetailItem goodsItem = StoreReturnGoodsAddActivity.instance.storeReturnMap.get(mReturnGoodsDetailVo.getGoodsId());
					if (goodsItem!=null) {
						map.put("returnGoodsReason", null);
						StoreReturnGoodsAddActivity.instance.removeView(goodsItem);
						StoreReturnGoodsDetailItemActivity.this.finish();
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
	 * 保存
	 */
	private void save() {
		ReturnGoodsDetailVo returnGoodsDetailVo = new ReturnGoodsDetailVo();
		Integer sum;
		try {
			sum = Integer.valueOf(goods_num.getStrVal());
		} catch (NumberFormatException e) {
			sum = 0;
		}
		BigDecimal count = new BigDecimal(sum);
		if (activity.equals("storeReturnGoodsAddActivity")) {
			returnGoodsDetailVo.setGoodsId(searchGoodsVo.getGoodsId());
			returnGoodsDetailVo.setGoodsName(searchGoodsVo.getGoodsName());
			returnGoodsDetailVo.setGoodsBarcode(searchGoodsVo.getBarcode());
			if (isPrice.equals("false")) {
				returnGoodsDetailVo.setGoodsPrice(searchGoodsVo.getPurchasePrice());
			}else {
				returnGoodsDetailVo.setGoodsPrice(price);
			}
			returnGoodsDetailVo.setResonVal(dicVo.getVal());
			returnGoodsDetailVo.setResonName(dicVo.getName());
			returnGoodsDetailVo.setGoodsSum(sum);
			returnGoodsDetailVo.setGoodsTotalPrice(searchGoodsVo.getPurchasePrice().multiply(count));
			map.put("returnGoodsReason", returnGoodsDetailVo);
			StoreReturnGoodsDetailItemActivity.this.finish();
			StoreOrderAddGoodsActivity.instance.finish();
		}else {
			returnGoodsDetailVo = mReturnGoodsDetailVo;
			returnGoodsDetailVo.setGoodsSum(sum);
			returnGoodsDetailVo.setResonVal(dicVo.getVal());
			returnGoodsDetailVo.setResonName(dicVo.getName());
			if (isPrice.equals("true")) {
				returnGoodsDetailVo.setGoodsPrice(price);
			}
			StoreReturnGoodsAddActivity.instance.changeGoodInfo(returnGoodsDetailVo);;
			StoreReturnGoodsDetailItemActivity.this.finish();
		}
	}

	@Override
	public void onItemListClick(ItemEditList obj) {
		int position = Integer.parseInt(String.valueOf(obj.getTag()));
		switch (position) {
		case 1:     
			Intent reasonIntent = new Intent();
			reasonIntent.setClass(StoreReturnGoodsDetailItemActivity.this, StoreReturnGoodsReasonActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("dicVo", dicVo);
			reasonIntent.putExtras(bundle);
			startActivityForResult(reasonIntent, 100);
			break;
		}
	}
}
