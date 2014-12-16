/**
 * 
 */
package com.dfire.retail.app.manage.activity.item;

import java.util.HashMap;

import android.annotation.SuppressLint;
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
import com.dfire.retail.app.manage.activity.logisticmanager.StoreAllocationAddActivity;
import com.dfire.retail.app.manage.activity.logisticmanager.StoreAllocationDetailItemActivity;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.vo.allocateDetailVo;

/**
 * @author ys 2014-11-15
 */
@SuppressLint("InflateParams")
public class StoreAllocationDetailItem implements OnClickListener {

	private LayoutInflater inflater;

	private StoreAllocationAddActivity context;

	private View itemView;

	private TextView txt_name;
	
	private EditText numTxt;

	private ImageView btnAdd, btnDel;
	
	private Integer goodsNumber;

	private allocateDetailVo mAllocateDetailVo;
	
	private TextView text_good_num,text_good_price;
	
	private boolean mwhText;
	
	private LinearLayout allocate_add_layout,gooditemview;
	
	public StoreAllocationDetailItem(StoreAllocationAddActivity context, LayoutInflater inflater,boolean mwhText) {
		this.context = context;
		this.inflater = inflater;
		this.mwhText = mwhText;
		this.init();
	}

	private void init() {

		this.itemView = inflater.inflate(R.layout.store_allocation_detail_item, null);
		this.txt_name = (TextView) itemView.findViewById(R.id.txt_name);

		this.btnAdd = (ImageView) itemView.findViewById(R.id.btn_add);
		this.btnDel = (ImageView) itemView.findViewById(R.id.btn_del);
		this.numTxt = (EditText) itemView.findViewById(R.id.txt_nums);
		this.text_good_num = (TextView) itemView.findViewById(R.id.text_good_num);
		this.text_good_price = (TextView) itemView.findViewById(R.id.text_good_price);
		this.allocate_add_layout = (LinearLayout) itemView.findViewById(R.id.allocate_add_layout);
		this.gooditemview=(LinearLayout) itemView.findViewById(R.id.gooditemview);
		
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
					final AlertDialog alertDialog = new AlertDialog(context);
					alertDialog.setMessage(context.getResources().getString(R.string.isdelete));
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.setPositiveButton(context.getResources().getString(R.string.confirm), new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
							((StoreAllocationAddActivity)context).removeView(StoreAllocationDetailItem.this);
							goodsNumber = -1;
						}
					});
					alertDialog.setNegativeButton(context.getResources().getString(R.string.cancel), new OnClickListener() {
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
				mAllocateDetailVo.setGoodsSum(goodsNumber);
				((StoreAllocationAddActivity)context).changePriceNumber(StoreAllocationDetailItem.this);
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

	public void initWithData(allocateDetailVo allocateDetailVo) {
		this.mAllocateDetailVo = allocateDetailVo;
		txt_name.setText(mAllocateDetailVo.getGoodsName());
		if (mwhText) {
			if (RetailApplication.getShopVo().getType()==ShopVo.ZHONGBU) {
				text_good_price.setText("进货价:"+String.format("%.2f", mAllocateDetailVo.getGoodsPrice()));
				text_good_price.setVisibility(View.VISIBLE);
			}
			text_good_num.setText("调拨数:"+mAllocateDetailVo.getGoodsSum());
			text_good_num.setVisibility(View.VISIBLE);
			allocate_add_layout.setVisibility(View.GONE);
		}else {
			numTxt.setText(mAllocateDetailVo.getGoodsSum()+"");
			this.gooditemview.setOnClickListener(this);
		}
	}
	
	@Override
	public void onClick(View v) {
		goodsNumber = Integer.parseInt(numTxt.getText().toString());
		if (v==btnAdd) {
			goodsNumber++;
			this.numTxt.setText(String.valueOf(goodsNumber));
		}else if (v==btnDel) {
			goodsNumber--;
			if (goodsNumber>=0) {
				this.numTxt.setText(String.valueOf(goodsNumber));
			}
		}else if (v==gooditemview) {
			Intent add = new Intent(context, StoreAllocationDetailItemActivity.class);
			add.putExtra("activity", "1");
			RetailApplication rapp = (RetailApplication)((StoreAllocationAddActivity)context).getApplication();
			HashMap<String, Object> map = rapp.getObjMap();
			map.put("allocationAdd", mAllocateDetailVo);
			context.startActivity(add);
		}
	}
	
	public allocateDetailVo getAllocateDetailVo(){
		return mAllocateDetailVo;
	}
	
	public View getItemView() {
		return itemView;
	}
	
	public EditText getNumTxt(){
		return numTxt;
	}
}
