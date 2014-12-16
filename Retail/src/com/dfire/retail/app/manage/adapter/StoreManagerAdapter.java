package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.global.ConfigConstants;

public class StoreManagerAdapter extends BaseAdapter {

    private static final String TAG = "StoreManagerAdapter";

    ArrayList<StoreManagerItem> mList = new ArrayList<StoreManagerItem>();
    private LayoutInflater mLayoutInflater;

    public StoreManagerAdapter(Context context, ArrayList<StoreManagerItem> list) {
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
        Log.d(TAG, "mList size = " + mList.size());
    }

    private class ViewHolder {
        ImageView imageView;
        TextView mainText;
        TextView subheadText;
        ImageView lock;
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
            convertView = mLayoutInflater.inflate(R.layout.storemanager_item,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.storemanagericon);
            viewHolder.mainText = (TextView) convertView.findViewById(R.id.storemanager_item_main_title);
            viewHolder.subheadText = (TextView) convertView.findViewById(R.id.storemanager_item_subhead_title);
            viewHolder.lock = (ImageView) convertView.findViewById(R.id.lock);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(!CommonUtils.getPermission(ConfigConstants.ACTION_EMPLOYEE_ACTION)||!CommonUtils.getPermission(ConfigConstants.ACTION_EMPLOYEE_MANAGE)) {//角色
        	viewHolder.lock.setVisibility(View.VISIBLE);
		}
        viewHolder.imageView.setImageResource(mList.get(position).getImageId());
        viewHolder.mainText.setText(mList.get(position).getMainText());
        viewHolder.subheadText.setText(mList.get(position).getSubheadText());

        return convertView;
    }
}
