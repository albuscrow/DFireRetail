package com.dfire.retail.app.manage.activity.goodsmanager;

import java.io.Serializable;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.util.StringUtils;

@SuppressLint("InflateParams")
public class SelectShopForGoodsManagerAdapter extends BaseAdapter{
	
	private List<AllShopVo> allShopVos;
	
	private LayoutInflater layoutInflater;
	
	private String selectShopId;
	
	public SelectShopForGoodsManagerAdapter(Context mContext,List<AllShopVo> allShopVos,String selectShopId) {
		this.allShopVos = allShopVos;
		this.layoutInflater = LayoutInflater.from(mContext);
		this.selectShopId = selectShopId;
	}
	@Override
	public int getCount() {
		return allShopVos.size();
	}

	@Override
	public Object getItem(int position) {
		return allShopVos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView lHolderView;
		AllShopVo allShopVo = allShopVos.get(position);
		if (convertView == null) {
			lHolderView = new HolderView();
			convertView = layoutInflater.inflate(R.layout.select_shop_adapter, null);
			lHolderView.setShop_name((TextView)convertView.findViewById(R.id.shop_name));
			lHolderView.setShop_code((TextView)convertView.findViewById(R.id.shop_code));
			lHolderView.setCheck_iv((ImageView)convertView.findViewById(R.id.check_iv));
			convertView.setTag(lHolderView);
		} else {
			lHolderView = (HolderView) convertView.getTag();
		}
		if (allShopVo!=null) {
			lHolderView.getShop_name().setText(allShopVo.getShopName());
			if (allShopVo.getCode().equals("")) {
				lHolderView.getShop_code().setText("");
			}else {
				lHolderView.getShop_code().setText("店家代码: "+allShopVo.getCode());
			}
			lHolderView.getShop_code().setTextColor(Color.parseColor("#0d8dc8"));
			if (!StringUtils.isEmpty(selectShopId)&&selectShopId.equals(allShopVo.getShopId())) {
				lHolderView.getCheck_iv().setVisibility(View.VISIBLE);
			}else {
				lHolderView.getCheck_iv().setVisibility(View.GONE);
			}
			
			//for get async shop
			if (selectShopId == null && allShopVo.getShopId() == null) {
				lHolderView.getCheck_iv().setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}
	class HolderView implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private TextView shop_name;
		
		private TextView shop_code;
		
		private ImageView check_iv;

		public TextView getShop_name() {
			return shop_name;
		}

		public void setShop_name(TextView shop_name) {
			this.shop_name = shop_name;
		}

		public TextView getShop_code() {
			return shop_code;
		}

		public void setShop_code(TextView shop_code) {
			this.shop_code = shop_code;
		}

		public ImageView getCheck_iv() {
			return check_iv;
		}

		public void setCheck_iv(ImageView check_iv) {
			this.check_iv = check_iv;
		}
	}
}
