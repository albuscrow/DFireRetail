/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.common.item.DateUtil;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.OrderGoodsVo;
import com.dfire.retail.app.manage.vo.AllocateVo;

/**
 * @author 李锦运
 *2014-10-29
 */
public class StoreAllocationAdapter  extends BaseAdapter{
	
	private List<AllocateVo> allocateVoList;
	
	private LayoutInflater layoutInflater;
	
	public StoreAllocationAdapter(StoreAllocationActivity mContext,List<AllocateVo> allocateVoList) {
		
		this.allocateVoList = allocateVoList;
		
		this.layoutInflater = LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return allocateVoList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return allocateVoList.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.activity_goods_order_adapter_item, null);
		}
		AllocateVo allocateVo = allocateVoList.get(position);
		if (allocateVo!=null) {
			((TextView)convertView.findViewById(R.id.orderno)).setText(allocateVo.getBatchNo());
			if (allocateVo.getSendEndTime()!=0) {
				((TextView)convertView.findViewById(R.id.order_time)).setText("要求到货日："+DateUtil.timeToStrYMD_EN(allocateVo.getSendEndTime()));
			}
			((TextView)convertView.findViewById(R.id.states)).setText(allocateVo.getBillStatusName());
			if (allocateVo.getBillStatus()==1) {
				((TextView)convertView.findViewById(R.id.states)).setTextColor(Color.parseColor("#00CC00"));
			}else{
				((TextView)convertView.findViewById(R.id.states)).setTextColor(Color.parseColor("#FF0033"));
			}
		}
		return convertView;
	}

}
