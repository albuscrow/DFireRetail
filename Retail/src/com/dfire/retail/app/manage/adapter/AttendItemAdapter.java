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

public class AttendItemAdapter extends BaseAdapter {

    private static final String TAG = "AttendItemAdapter";

    ArrayList<AttendanceItem> mList = new ArrayList<AttendanceItem>();
    private LayoutInflater mLayoutInflater;

    public AttendItemAdapter(Context context, ArrayList<AttendanceItem> list) {
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
        Log.d(TAG, "mList size = " + mList.size());
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
        EmployeeViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.shiftwork_item, null);
            viewHolder = new EmployeeViewHolder();
            viewHolder.profession = (TextView) convertView.findViewById(R.id.profession);
            viewHolder.empname = (TextView) convertView.findViewById(R.id.emp_name);
            viewHolder.empno = (TextView) convertView.findViewById(R.id.emp_number);
            viewHolder.starttitle = (TextView) convertView.findViewById(R.id.start_time);
            viewHolder.endtitle = (TextView) convertView.findViewById(R.id.end_time);
            viewHolder.startdate = (TextView) convertView.findViewById(R.id.start_date);
            viewHolder.starthour = (TextView) convertView.findViewById(R.id.start_hour);
            viewHolder.enddate = (TextView) convertView.findViewById(R.id.end_date);
            viewHolder.endhour = (TextView) convertView.findViewById(R.id.end_hour);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EmployeeViewHolder) convertView.getTag();
        }

        // viewHolder.profession.setBackground(mList.get(position).getmBitmap());
        viewHolder.profession.setCompoundDrawablesWithIntrinsicBounds(null,
                mList.get(position).getmBitmap(), null, null);
        viewHolder.profession.setText(mList.get(position).getProfessional());
        viewHolder.empname.setText(mList.get(position).getName());
        viewHolder.empno.setText(mList.get(position).getEmp_no());
        viewHolder.starttitle.setText(mList.get(position).getStartTitle());
        viewHolder.startdate.setText(mList.get(position).getStartdate());
        viewHolder.starthour.setText(mList.get(position).getStarthour());
        viewHolder.enddate.setText(mList.get(position).getEnddate());
        viewHolder.endtitle.setText(mList.get(position).getEndTitle());
        viewHolder.endhour.setText(mList.get(position).getEndhour());

        return convertView;
    }
}
