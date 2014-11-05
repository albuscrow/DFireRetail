/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.List;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.common.item.DateUtil;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.vo.AllocateVo;
import com.dfire.retail.app.manage.vo.ReturnGoodsVo;

/**
 * @author 李锦运
 *2014-10-30
 */
public class StoreReturnGoodsAdapter extends BaseAdapter{
	
	private List<ReturnGoodsVo> returnVoList;
	
	private LayoutInflater layoutInflater;
	
	public StoreReturnGoodsAdapter(StoreReturnGoodsActivity mContext,List<ReturnGoodsVo> returnVoList) {
		
		this.returnVoList = returnVoList;
		
		this.layoutInflater = LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return returnVoList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return returnVoList.get(position);
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
		ReturnGoodsVo returnVo = returnVoList.get(position);
		if (returnVo!=null) {
			((TextView)convertView.findViewById(R.id.orderno)).setText(returnVo.getReturnGoodsNo());
			if (returnVo.getSendEndTime()!=0) {
				((TextView)convertView.findViewById(R.id.order_time)).setText("要求到货日："+DateUtil.timeToStrYMD_EN(returnVo.getSendEndTime()));
			}
			((TextView)convertView.findViewById(R.id.states)).setText(returnVo.getBillStatusName());
			if (returnVo.getBillStatus()==1) {
				((TextView)convertView.findViewById(R.id.states)).setTextColor(Color.parseColor("#00CC00"));
			}else{
				((TextView)convertView.findViewById(R.id.states)).setTextColor(Color.parseColor("#FF0033"));
			}
		}
		return convertView;
	}

}
