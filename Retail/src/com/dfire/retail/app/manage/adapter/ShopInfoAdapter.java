package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;


@SuppressLint("InflateParams")
public class ShopInfoAdapter extends BaseAdapter {

    ArrayList<ShopInfoItem> mList = new ArrayList<ShopInfoItem>();
    private LayoutInflater mLayoutInflater;

    public ShopInfoAdapter(Context context, ArrayList<ShopInfoItem> list) {
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
        TextView shopName;
        TextView shopCode;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.select_shop_adapter,null);
            viewHolder = new ViewHolder();
            viewHolder.shopName = (TextView) convertView.findViewById(R.id.shop_name);
            viewHolder.shopCode = (TextView) convertView.findViewById(R.id.shop_code);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.shopCode.setTextColor(Color.parseColor("#0d8dc8"));
        viewHolder.shopName.setText(mList.get(position).getShopName());
        viewHolder.shopCode.setText(mList.get(position).getCode());
        return convertView;
    }
}
