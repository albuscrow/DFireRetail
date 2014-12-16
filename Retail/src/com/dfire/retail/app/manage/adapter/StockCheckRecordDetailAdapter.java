package com.dfire.retail.app.manage.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.StockCheckRecordDetailVo;
/**
 * 盘点记录详情适配器
 * @author ys
 *
 */
@SuppressLint("InflateParams")
public class StockCheckRecordDetailAdapter extends BaseAdapter{
	
	private LayoutInflater layoutInflater;
	
	private List<StockCheckRecordDetailVo> checkRecordDetailVos;
	
	public StockCheckRecordDetailAdapter(Context mContext,List<StockCheckRecordDetailVo> checkRecordDetailVos) {
		this.checkRecordDetailVos = checkRecordDetailVos;
		this.layoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return checkRecordDetailVos.size();
	}

	@Override
	public Object getItem(int position) {
		return checkRecordDetailVos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		StockCheckRecordDetailVo checkRecordDetailVo = checkRecordDetailVos.get(position);
		if (convertView==null) {
			convertView = layoutInflater.inflate(R.layout.activity_stock_check_record_adapter, null);
			viewHolder = new ViewHolder();
			viewHolder.setGoodsName((TextView)convertView.findViewById(R.id.goodsName));
			viewHolder.setGoodsBarCode((TextView)convertView.findViewById(R.id.goodsBarCode));
			viewHolder.setStockTotalCount((TextView)convertView.findViewById(R.id.stockTotalCount));
			viewHolder.setStockRealCount((TextView)convertView.findViewById(R.id.stockRealCount));
			viewHolder.setExhibitCount((TextView)convertView.findViewById(R.id.exhibitCount));
			viewHolder.setSell((TextView)convertView.findViewById(R.id.sell));
			viewHolder.setStockMoney((TextView)convertView.findViewById(R.id.stockMoney));
			viewHolder.setExhibit((TextView)convertView.findViewById(R.id.exhibit));
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (checkRecordDetailVo!=null) {
			viewHolder.getGoodsName().setText(checkRecordDetailVo.getGoodsName());
			viewHolder.getGoodsBarCode().setText(checkRecordDetailVo.getGoodsBarCode());
			viewHolder.getStockTotalCount().setText(String.valueOf(checkRecordDetailVo.getStockTotalCount()));
			viewHolder.getStockRealCount().setText(String.valueOf(checkRecordDetailVo.getStockRealCount()));
			viewHolder.getExhibitCount().setText(String.valueOf(checkRecordDetailVo.getExhibitCount()));
			viewHolder.getSell().setText(String.valueOf(checkRecordDetailVo.getSell()));
			viewHolder.getStockMoney().setText(String.valueOf(checkRecordDetailVo.getStockMoney()));
			viewHolder.getExhibit().setText(String.valueOf(checkRecordDetailVo.getExhibit()));
		}
		return convertView;
	}
	public class ViewHolder {
	    private TextView goodsName;
	    private TextView goodsBarCode;
	    private TextView stockTotalCount;
	    private TextView stockRealCount;
	    private TextView exhibitCount;
	    private TextView sell;
	    private TextView stockMoney;
	    private TextView exhibit;
		public TextView getGoodsName() {
			return goodsName;
		}
		public void setGoodsName(TextView goodsName) {
			this.goodsName = goodsName;
		}
		public TextView getGoodsBarCode() {
			return goodsBarCode;
		}
		public void setGoodsBarCode(TextView goodsBarCode) {
			this.goodsBarCode = goodsBarCode;
		}
		public TextView getStockTotalCount() {
			return stockTotalCount;
		}
		public void setStockTotalCount(TextView stockTotalCount) {
			this.stockTotalCount = stockTotalCount;
		}
		public TextView getStockRealCount() {
			return stockRealCount;
		}
		public void setStockRealCount(TextView stockRealCount) {
			this.stockRealCount = stockRealCount;
		}
		public TextView getExhibitCount() {
			return exhibitCount;
		}
		public void setExhibitCount(TextView exhibitCount) {
			this.exhibitCount = exhibitCount;
		}
		public TextView getSell() {
			return sell;
		}
		public void setSell(TextView sell) {
			this.sell = sell;
		}
		public TextView getStockMoney() {
			return stockMoney;
		}
		public void setStockMoney(TextView stockMoney) {
			this.stockMoney = stockMoney;
		}
		public TextView getExhibit() {
			return exhibit;
		}
		public void setExhibit(TextView exhibit) {
			this.exhibit = exhibit;
		}
	}	
}
