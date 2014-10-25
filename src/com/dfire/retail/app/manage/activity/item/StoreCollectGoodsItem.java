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
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.StockInDetailVo;
import com.dfire.retail.app.manage.util.ToastUtil;
/**
 * 项目名称：retail  
 * 类名称：StoreOrderGoodsItem  
 * @author ys  
 * @version 1.0
 */
@SuppressLint("InflateParams")
public class StoreCollectGoodsItem implements IViewItem, OnClickListener {

	private LayoutInflater inflater;
	
	private View itemView;
	
	private TextView goods_name,goods_price,text_good_num;
	
	private ImageView reduce_img,plus_img;
	
	private EditText goods_num;
	
	private Integer goodsNumber = 1;
	
	private Context mContext;
	
	private LinearLayout stock_order_add_layout;
	
	private boolean mwhText;//是否是文本
	
	public StoreCollectGoodsItem(Context mContext,LayoutInflater inflater,boolean mwhText) {
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
		this.itemView = inflater.inflate(R.layout.stock_collect_add_item, null);
		this.itemView.setTag(this);
		this.goods_name=(TextView) itemView.findViewById(R.id.goods_name);
        this.goods_price=(TextView) itemView.findViewById(R.id.goods_price);
        this.reduce_img=(ImageView) itemView.findViewById(R.id.reduce_img); //減
        this.plus_img=(ImageView) itemView.findViewById(R.id.plus_img);//加
        this.goods_num=(EditText) itemView.findViewById(R.id.goods_num);
        this.text_good_num=(TextView) itemView.findViewById(R.id.text_good_num);
        this.stock_order_add_layout=(LinearLayout) itemView.findViewById(R.id.stock_order_add_layout);
        
        this.initButtonEvent();
	}

	public void initWithData(StockInDetailVo stockInDetailVo){
		 this.goods_name.setText(stockInDetailVo.getGoodsName()+"");
         this.goods_price.setText("进货价:"+stockInDetailVo.getGoodsPrice()+"");
         this.goods_num.setText(stockInDetailVo.getGoodsSum()+"");
         this.goodsNumber = stockInDetailVo.getGoodsSum();
         if (mwhText) {
        	 this.stock_order_add_layout.setVisibility(View.GONE);
        	 this.text_good_num.setVisibility(View.VISIBLE);
        	 this.text_good_num.setText("订单数："+stockInDetailVo.getGoodsSum());
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
		if (v==reduce_img) {
			this.goodsNumber--;
			if (goodsNumber==0) {
				ToastUtil.showShortToast(mContext, "是否删除该项!");
			}
			this.goods_num.setText(String.valueOf(goodsNumber));
		}else if (v==plus_img) {
			this.goodsNumber++;
			this.goods_num.setText(String.valueOf(goodsNumber));
		}
	}

	@Override
	public void itemSelect() {
		
	}

	@Override
	public View getItemView() {
		return this.itemView;
	}
	//editText 变动动态改变其他控件的值
	class TextWatcherOnChangeListener implements TextWatcher{
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}
		@Override
		public void afterTextChanged(Editable s) {
			
		}
		
	}
}
