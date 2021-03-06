package com.dfire.retail.app.manage.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;

import java.util.ArrayList;

public class EmployeeInfoDetailAdapter extends BaseAdapter {

    private static final String TAG = "StoreManagerAdapter";

    ArrayList<EmployeeInfoDetailItem> mList = new ArrayList<EmployeeInfoDetailItem>();
    private LayoutInflater mLayoutInflater;
    
    public EmployeeInfoDetailAdapter(Context context,
            ArrayList<EmployeeInfoDetailItem> list) {
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
        Log.d(TAG, "mList size = " + mList.size());
    }

    private class ViewHolder {
        TextView title;
        TextView value;
        ImageView image;
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
            convertView = mLayoutInflater.inflate(R.layout.employee_info_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView
                    .findViewById(R.id.employee_info_title);
            viewHolder.value = (TextView) convertView
                    .findViewById(R.id.employee_item_value);
            //viewHolder.image = (ImageView) convertView
            //        .findViewById(R.id.employee_item_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(mList.get(position).getTitle());
        viewHolder.value.setText(mList.get(position).getValue());
        if (mList.get(position).isChange()) {
            viewHolder.value.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
            viewHolder.value.setClickable(true);
            viewHolder.value.setTag(position);
        }

        return convertView;
    }
}