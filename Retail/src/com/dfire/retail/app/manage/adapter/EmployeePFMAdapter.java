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

public class EmployeePFMAdapter extends BaseAdapter {

    private static final String TAG = "EmployeePFMAdapter";

    ArrayList<EmployeePFMItem> mList = new ArrayList<EmployeePFMItem>();
    private LayoutInflater mLayoutInflater;

    public EmployeePFMAdapter(Context context, ArrayList<EmployeePFMItem> list) {
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
        Log.d(TAG, "mList size = " + mList.size());
    }

    private class ViewHolder {
        TextView profession;
        TextView empname;
        TextView empno;
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
            convertView = mLayoutInflater.inflate(R.layout.employee_pfm_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.profession = (TextView) convertView
                    .findViewById(R.id.emp_pfm_profession);
            viewHolder.empname = (TextView) convertView
                    .findViewById(R.id.emp_pfm_name);
            viewHolder.empno = (TextView) convertView
                    .findViewById(R.id.emp_pfm_number);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // viewHolder.profession.setBackground(mList.get(position).getmBitmap());
        if (mList.get(position).getmBitmap() != null) {
            viewHolder.profession.setBackgroundDrawable(mList.get(position)
                    .getmBitmap());
            viewHolder.profession
                    .setText(mList.get(position).getProfessional());
        } else {
            viewHolder.profession.setVisibility(View.GONE);
        }

        viewHolder.empname.setText(mList.get(position).getName());

        viewHolder.empno.setText(mList.get(position).getEmp_no());
        return convertView;
    }
}
