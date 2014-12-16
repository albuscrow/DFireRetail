package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
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

@SuppressLint("InflateParams")
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
        if (position==0) {
        	if(!CommonUtils.getPermission(ConfigConstants.ACTION_EMPLOYEE_ACTION)) {
        		viewHolder.lock.setVisibility(View.VISIBLE);
        	}
		}else {
			if (!CommonUtils.getPermission(ConfigConstants.ACTION_EMPLOYEE_MANAGE)) {
				viewHolder.lock.setVisibility(View.VISIBLE);
			}
		}
        viewHolder.imageView.setImageResource(mList.get(position).getImageId());
        viewHolder.mainText.setText(mList.get(position).getMainText());
        viewHolder.subheadText.setText(mList.get(position).getSubheadText());

        return convertView;
    }
}
