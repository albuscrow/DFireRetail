package com.dfire.retail.app.manage.activity.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;

public class StockQueryListAddFooterItem{
	private Context mContext;
	private TextView countPrice,countNum;
	private LinearLayout count_layout;
	public StockQueryListAddFooterItem(Context mContext,ListView listView) {
		this.mContext = mContext;
		addFooter(listView);
	}
	public void addFooter(ListView listView){
		if (listView == null) {
			return;
		}
		View inflate = LayoutInflater.from(mContext).inflate(R.layout.stock_query_list_add_footer_item, listView, false);
		countPrice = (TextView) inflate.findViewById(R.id.count_price);
		countNum = (TextView) inflate.findViewById(R.id.count_num);
		count_layout = (LinearLayout) inflate.findViewById(R.id.count_layout);
		listView.addFooterView(inflate, null, false);
	}
	public void initData(String countPriceStr,String countNumStr,Boolean isGoenPrice){
		count_layout.setVisibility(View.VISIBLE);
		if (!isGoenPrice) {
			countPrice.setVisibility(View.GONE);
		}else {
			countPrice.setText(countPriceStr+"元");
		}
		countNum.setText("合计"+countNumStr+"件商品");
	}
}
