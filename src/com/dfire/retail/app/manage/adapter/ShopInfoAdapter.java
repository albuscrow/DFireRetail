package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;


public class ShopInfoAdapter extends BaseAdapter {

    private static final String TAG = "ShopInfoAdapter";

    ArrayList<ShopInfoItem> mList = new ArrayList<ShopInfoItem>();
    private LayoutInflater mLayoutInflater;

    public ShopInfoAdapter(Context context, ArrayList<ShopInfoItem> list) {
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
        Log.d(TAG, "mList size = " + mList.size());
    }

    private class ViewHolder {
        TextView shopName;
        TextView shopCode;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.shop_info_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.shopName = (TextView) convertView
                    .findViewById(R.id.shop_item_name);
            viewHolder.shopCode = (TextView) convertView
                    .findViewById(R.id.shop_item_code);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // viewHolder.profession.setBackground(mList.get(position).getmBitmap());


        viewHolder.shopName.setText(mList.get(position).getShopName());

        viewHolder.shopCode.setText(mList.get(position).getCode());
        return convertView;
    }
}