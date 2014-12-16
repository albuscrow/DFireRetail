package com.dfire.retail.app.manage.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.RoleVo;

public class RolePermissionSettingAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<RoleVo> mData;
    private int mResId;
    
    public RolePermissionSettingAdapter(Context context, List<RoleVo> data, int resId) {
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
        TextView holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mResId, null);
            holder = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (TextView) convertView.getTag();
        }
        RoleVo role = (RoleVo)this.getItem(position);
        holder.setText(role.getRoleName());
        
        return convertView;
    }

}
