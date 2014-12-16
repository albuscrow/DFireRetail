package com.dfire.retail.app.manage.activity.item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.retail.IViewItem;
import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.logisticmanager.StoreOrderAddActivity;
import com.dfire.retail.app.manage.data.OrderGoodsDetailVo;
/**
 * 项目名称：retail  
 * 类名称：StoreOrderGoodsItem  
 * @author ys  
 * @version 1.0
 */
@SuppressLint("InflateParams")
public class StoreOrderGoodsItem implements IViewItem, OnClickListener {

	private LayoutInflater inflater;
	
	private View itemView;
	
	private TextView goods_name,goods_number,text_good_num;
	
	private ImageView reduce_img,plus_img;
	
	private EditText goods_num;
	
	private Context mContext;
	
	private LinearLayout stock_order_add_layout;
	
	private boolean mwhText;//是否是文本
	
	private OrderGoodsDetailVo orderGoodsDetailVo;
	
	private Integer goodsNumber;
	
	public AlertDialog alertDialog;
	
	public StoreOrderGoodsItem(Context mContext,LayoutInflater inflater,boolean mwhText) {
		this.inflater = inflater;
		this.mContext = mContext;
		this.mwhText = mwhText;
		init();
	}
	@Override
	public void init() {
		initMainView();
	}

	@Override
	public void initMainView() {
		this.itemView = inflater.inflate(R.layout.stock_order_add_item, null);
		this.itemView.setTag(this);
		this.goods_name=(TextView) itemView.findViewById(R.id.goods_name);
        this.goods_number=(TextView) itemView.findViewById(R.id.goods_number);
        this.reduce_img=(ImageView) itemView.findViewById(R.id.reduce_img); //減
        this.plus_img=(ImageView) itemView.findViewById(R.id.plus_img);//加
        this.goods_num=(EditText) itemView.findViewById(R.id.goods_num);
        this.text_good_num=(TextView) itemView.findViewById(R.id.text_good_num);
        this.stock_order_add_layout=(LinearLayout) itemView.findViewById(R.id.stock_order_add_layout);
        this.goods_num.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					goodsNumber = Integer.parseInt(s+"");
				} catch (NumberFormatException e) {
					goodsNumber = 0;
				}
				if (goodsNumber==0) {
					alertDialog = new AlertDialog(mContext);
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.setMessage(mContext.getResources().getString(R.string.isdelete));
					alertDialog.setPositiveButton(mContext.getResources().getString(R.string.confirm), new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
							((StoreOrderAddActivity)mContext).removeView(StoreOrderGoodsItem.this);
							goodsNumber = -1;
						}
					});
					alertDialog.setNegativeButton(mContext.getResources().getString(R.string.cancel), new OnClickListener() {
						@Override
						public void onClick(View v) {
							goodsNumber = 0;
							alertDialog.dismiss();
						}
					});
				}
				if (goodsNumber==0) {
					goods_num.setText("1");
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
        this.initButtonEvent();
	}

	public void initWithData(OrderGoodsDetailVo orderGoodsDetailVo){
		this.orderGoodsDetailVo = orderGoodsDetailVo;
		this.goods_name.setText(orderGoodsDetailVo.getGoodsName()+"");
        this.goods_number.setText("库存数:"+orderGoodsDetailVo.getNowStore()+"");
        this.goods_num.setText(orderGoodsDetailVo.getGoodsSum()+"");
        
        if (mwhText) {
        	this.stock_order_add_layout.setVisibility(View.GONE);
        	this.text_good_num.setVisibility(View.VISIBLE);
        	this.text_good_num.setText("订货数："+orderGoodsDetailVo.getGoodsSum());
		}
	}
	
	@Override
	public void setVisibility(int visibility) {
		this.itemView.setVisibility(visibility);		
	}

	private void initButtonEvent(){
		this.reduce_img.setOnClickListener(this);
		this.plus_img.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (!goods_num.getText().toString().equals("")) {
			goodsNumber = Integer.parseInt(goods_num.getText().toString());
			if (v==reduce_img) {
				goodsNumber--;
				if (goodsNumber>=0) {
					this.goods_num.setText(String.valueOf(goodsNumber));
				}
			}else if (v==plus_img) {
				goodsNumber++;
				this.goods_num.setText(String.valueOf(goodsNumber));
			}
		}
		
	}

	@Override
	public void itemSelect() {
		
	}

	@Override
	public View getItemView() {
		return this.itemView;
	}
	/**
	 * 获取单项信息
	 */
	public OrderGoodsDetailVo getOrderGoodsDetailVo(){
		return orderGoodsDetailVo;
	}
	/**
	 * 获取数量
	 */
	public TextView getGoodNum(){
		return goods_num;
	}
}