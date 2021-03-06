package com.dfire.retail.app.manage.adapter;

import com.dfire.retail.app.manage.R;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoiceAdapter extends BaseAdapter {
	
	 class ViewHolder {
	    public ImageView mCheck;
	    public TextView mTitle;
	}

    private LayoutInflater mInflater;
    private List<RolePermissionSubItem> mData;
    private int mResId;
    
    public ChoiceAdapter(Context context, List<RolePermissionSubItem> data, int resId) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        mResId = resId;
    }
    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mResId, null);
            holder = new ViewHolder();
            holder.mCheck = (ImageView)convertView.findViewById(R.id.setting_bussiness_check);
            holder.mTitle = (TextView)convertView.findViewById(R.id.setting_bussiness_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        RolePermissionSubItem item = mData.get(position);
        holder.mCheck.setBackgroundResource(item.mChecked ? R.drawable.ico_check : R.drawable.ico_uncheck);
        holder.mTitle.setText(item.mName);
        
        return convertView;
    }
}
