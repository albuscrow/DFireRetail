package com.dfire.retail.app.manage.activity.logisticmanager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.retail.IViewItem;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.LogisticsDetailVo;

/**
 * 物流明细每一列信息
 */
public class LogisticsRecordDetailItem extends LinearLayout implements IViewItem 
{
	private Context context;
	
	private LayoutInflater inflater;
	
	private View itemView;
	
	private LogisticsRecordDetailActivity parentActivity;
	/**商品名称、数量、进货价*/
	private TextView goodsName, goodsPrice,goodsSum;
	
	private LogisticsDetailVo logisticsDetail;
	
	/**
	 * @param context
	 * @param attrs
	 */
	public LogisticsRecordDetailItem(Context context, AttributeSet attrs, LogisticsRecordDetailActivity parentActivity) 
	{
		super(context, attrs);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.parentActivity = parentActivity;
		init();
	}

	/** {@inheritDoc} */
	@Override
	public void init() {
		initMainView();
	}
	
	public void initMainView() 
	{
		this.itemView = inflater.inflate(R.layout.activity_logistics_records_detail_item, this);
		this.goodsName = (TextView)itemView.findViewById(R.id.goods_name);
		this.goodsPrice = (TextView)itemView.findViewById(R.id.goods_price);
		this.goodsSum = (TextView)itemView.findViewById(R.id.goods_sum);		
	}


	
	@Override
	public View getItemView() 
	{
		return this.itemView;
	}
	
	@Override
	public void itemSelect() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setVisibility(int visibility) 
	{
		this.itemView.setVisibility(visibility);
	}
	
	//设置商品名称、数量、进货价
	public void initWithData(LogisticsDetailVo logisticsDetail) 
	{
		this.logisticsDetail = logisticsDetail;
		goodsName.setText((logisticsDetail.getGoodsName()!=null)?logisticsDetail.getGoodsName().toString():"");
		goodsPrice.setText((logisticsDetail.getGoodsPrice()!=null)?logisticsDetail.getGoodsPrice().toString():"");
		goodsSum.setText(logisticsDetail.getGoodsSum()+"");
	}
	
	public LogisticsDetailVo getLogisticsDetailVo()
	{
		return this.logisticsDetail;
	}
}
