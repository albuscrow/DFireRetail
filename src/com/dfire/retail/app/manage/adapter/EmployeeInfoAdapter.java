package com.dfire.retail.app.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.UserVo;

import java.util.List;

/**
 * EmployeeInfoActivity中用于部分展示员工信息的List Adapter
 * @author 刘思海
 *
 */
public class EmployeeInfoAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<UserVo> mData;
    
    public EmployeeInfoAdapter (Context context, List<UserVo> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
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
        EmployeeViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.shiftwork_item, null);
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
        viewHolder.profession.setCompoundDrawablesWithIntrinsicBounds(0,
                mData.get(position).getLogoId(), 0, 0);
        viewHolder.profession.setText(mData.get(position).getRoleName());
        viewHolder.empname.setText(mData.get(position).getName());
        viewHolder.empno.setText("工号 ：" + mData.get(position).getStaffId());
        viewHolder.starttitle.setText(mData.get(position).getShopName());
        viewHolder.startdate.setVisibility(View.INVISIBLE);
        viewHolder.starthour.setVisibility(View.INVISIBLE);
        viewHolder.endtitle.setText("手机 ：" + mData.get(position).getMobile());
        viewHolder.enddate.setVisibility(View.INVISIBLE);
        viewHolder.endhour.setVisibility(View.INVISIBLE);

        return convertView;
    }

}