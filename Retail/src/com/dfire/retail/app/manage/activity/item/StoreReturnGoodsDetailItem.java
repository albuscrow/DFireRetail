package com.dfire.retail.app.manage.activity.item;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.logisticmanager.StoreReturnGoodsAddActivity;
import com.dfire.retail.app.manage.activity.logisticmanager.StoreReturnGoodsDetailItemActivity;
import com.dfire.retail.app.manage.vo.ReturnGoodsDetailVo;

/**
 * @author ys 2014-11-12
 */
@SuppressLint("InflateParams")
public class StoreReturnGoodsDetailItem implements OnClickListener {

	private LayoutInflater inflater;

	private View itemView;

	private TextView txt_name, txt_reason, txt_price,retrun_num;

	private EditText numTxt;
	
	private ImageView btnAdd, btnDel;

	private ReturnGoodsDetailVo returnGoodsDetailVo;
	
	private Context mContext;
	
	private Integer goodsNumber;
	
	private boolean mwhText;//是否是文本
	
	private LinearLayout stock_order_add_layout,gooditemview,price_layout;

	private String mIsPrice;
	
	public StoreReturnGoodsDetailItem(StoreReturnGoodsAddActivity context, LayoutInflater inflater,boolean mwhText,String isPrice) {
		this.inflater = inflater;
		this.mContext = context;
		this.mwhText = mwhText;
		this.mIsPrice = isPrice;
		this.init();
	}

	private void init() {

		this.itemView = inflater.inflate(R.layout.store_return_detail_item, null);
		this.txt_name = (TextView) itemView.findViewById(R.id.txt_name);
		this.txt_reason = (TextView) itemView.findViewById(R.id.txt_reason);
		this.txt_price = (TextView) itemView.findViewById(R.id.txt_price);
		this.price_layout = (LinearLayout) itemView.findViewById(R.id.price_layout);

		this.btnAdd = (ImageView) itemView.findViewById(R.id.btn_add);
		this.btnDel = (ImageView) itemView.findViewById(R.id.btn_del);
		this.numTxt = (EditText) itemView.findViewById(R.id.txt_nums);
		this.retrun_num = (TextView) itemView.findViewById(R.id.retrun_num);
		this.stock_order_add_layout = (LinearLayout) itemView.findViewById(R.id.stock_order_add_layout);
		this.gooditemview =(LinearLayout) itemView.findViewById(R.id.gooditemview);
		
		this.btnAdd.setOnClickListener(this);
		this.btnDel.setOnClickListener(this);
		this.numTxt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					goodsNumber = Integer.parseInt(s+"");
				} catch (NumberFormatException e) {
					goodsNumber = 0;
				}
				if (goodsNumber<=0) {
					final AlertDialog alertDialog = new AlertDialog(mContext);
					alertDialog.setMessage(mContext.getResources().getString(R.string.isdelete));
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.setPositiveButton(mContext.getResources().getString(R.string.confirm), new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
							((StoreReturnGoodsAddActivity)mContext).removeView(StoreReturnGoodsDetailItem.this);
							goodsNumber = -1;
						}
					});
					alertDialog.setNegativeButton(mContext.getResources().getString(R.string.cancel), new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
							goodsNumber = 0;
						}
					});
				}	
				if (goodsNumber==0) {
					numTxt.setText("1");
				}
				returnGoodsDetailVo.setGoodsSum(goodsNumber);
				((StoreReturnGoodsAddActivity)mContext).changePriceNumber(StoreReturnGoodsDetailItem.this);
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

	public void initWithAppInfo(ReturnGoodsDetailVo returnGoodsDetailVo) {
		this.returnGoodsDetailVo=returnGoodsDetailVo;
		txt_name.setText(returnGoodsDetailVo.getGoodsName());
		txt_reason.setText(returnGoodsDetailVo.getResonName());
		txt_price.setText(returnGoodsDetailVo.getGoodsPrice() + "");
		if (mwhText) {
       	 	stock_order_add_layout.setVisibility(View.GONE);
       	 	retrun_num.setVisibility(View.VISIBLE);
       	 	retrun_num.setText("退货数："+returnGoodsDetailVo.getGoodsSum());
        }else {
        	numTxt.setText(returnGoodsDetailVo.getGoodsSum()+"");
    		goodsNumber = returnGoodsDetailVo.getGoodsSum();
    		this.gooditemview.setOnClickListener(this);
        }
	}

	public View getItemView() {
		return itemView;
	}

	@Override
	public void onClick(View v) {
		if (!numTxt.getText().toString().equals("")) {
			goodsNumber = Integer.parseInt(numTxt.getText().toString());
			if (v==btnDel) {
				goodsNumber--;
				if (goodsNumber>=0) {
					this.numTxt.setText(String.valueOf(goodsNumber));
				}
			}else if (v==btnAdd) {
				goodsNumber++;
				this.numTxt.setText(String.valueOf(goodsNumber));
			}else if (v==gooditemview) {
				Intent add = new Intent(mContext, StoreReturnGoodsDetailItemActivity.class);
				add.putExtra("activity", "1");
				add.putExtra("isPrice", mIsPrice);
				RetailApplication rapp = (RetailApplication)((StoreReturnGoodsAddActivity)mContext).getApplication();
				HashMap<String, Object> map = rapp.getObjMap();
				map.put("returnGoodsReason", returnGoodsDetailVo);
				mContext.startActivity(add);
			}
		}
	}
	/**
	 * @return the returnGoodsDetailVo
	 */
	public ReturnGoodsDetailVo getReturnGoodsDetailVo() {
		return returnGoodsDetailVo;
	}
	/**
	 * 得到editText
	 */
	public EditText getNumTxt(){
		return numTxt;
	}
	/**
	 * 得到textView
	 */
	public TextView getTxt_reason(){
		return txt_reason;
	}
	/**
	 * price_layout
	 */
	public LinearLayout getPriceLayout(){
		return price_layout;
	}
}
