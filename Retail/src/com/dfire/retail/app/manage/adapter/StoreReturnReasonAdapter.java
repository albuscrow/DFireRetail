package com.dfire.retail.app.manage.adapter;

import java.io.Serializable;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.util.StringUtils;
/**
 * 退货原因 适配器
 * @author ys
 *
 */
@SuppressLint("InflateParams")
public class StoreReturnReasonAdapter extends BaseAdapter{

	private List<DicVo> nameItems;
	
	private LayoutInflater inflater;
	
	private String typeVal;
	
	public StoreReturnReasonAdapter(Context mContext,List<DicVo> datas,String typeVal) {
		this.nameItems = datas;
		this.inflater = LayoutInflater.from(mContext);
		this.typeVal = typeVal;
	}
	@Override
	public int getCount() {
		return nameItems.size();
	}

	@Override
	public Object getItem(int position) {
		return nameItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView lHolderView;
		DicVo data = (DicVo)nameItems.get(position);
		if (convertView == null) {
			lHolderView = new HolderView();
			convertView = inflater.inflate(R.layout.adjustment_reason_adapter, null);
			lHolderView.setTypeName((TextView)convertView.findViewById(R.id.typeName));
			lHolderView.setTypeiv((ImageView)convertView.findViewById(R.id.typeiv));
			convertView.setTag(lHolderView);
		} else {
			lHolderView = (HolderView) convertView.getTag();
		}
		if (data!=null) {
			lHolderView.getTypeName().setText(data.getName());
			if(!StringUtils.isEmpty(typeVal) && StringUtils.isEquals(String.valueOf(data.getVal()), typeVal)){
				lHolderView.getTypeiv().setVisibility(View.VISIBLE);
			}else{
				lHolderView.getTypeiv().setVisibility(View.GONE);
			}
		}
		return convertView;
	}
	class HolderView implements Serializable {
		private static final long serialVersionUID = 1L;
		private TextView typeName;
		private ImageView typeiv;
		public TextView getTypeName() {
			return typeName;
		}
		public void setTypeName(TextView typeName) {
			this.typeName = typeName;
		}
		public ImageView getTypeiv() {
			return typeiv;
		}
		public void setTypeiv(ImageView typeiv) {
			this.typeiv = typeiv;
		}
	}
	public void setTypeVal(String typeVal){
		this.typeVal = typeVal;
	}
}
