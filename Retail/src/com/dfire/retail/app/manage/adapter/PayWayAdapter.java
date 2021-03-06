package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;

public class PayWayAdapter extends BaseAdapter {

    private ArrayList<PayWayElement> mData;
    private LayoutInflater mLayoutInflater;
    
    public PayWayAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        
        //添加默认支付方式
        mData = new ArrayList<PayWayElement>();
        String[] strs = context.getResources().getStringArray(R.array.setting_payway_type_array);
        mData.add(new PayWayElement(strs[0], strs[0]));
        mData.add(new PayWayElement(strs[1], strs[1], false, true, 100));
        
        //从SharedPreferences读取设置的数据
        
    }
    
    public void addElement(PayWayElement element) {
        mData.add(element);
        notifyDataSetChanged();
    }

    public void deleteElement(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }
    
    public void changeElement(PayWayElement element) {
        mData.set(element.mPosition, element);
        notifyDataSetChanged();
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.setting_payway_item, null);
            holder = new ViewHolder();
            holder.mMainText = (TextView) convertView.findViewById(R.id.payway_main);
            holder.mSubHeadText = (TextView) convertView.findViewById(R.id.payway_subhead);
            holder.mType = (TextView) convertView.findViewById(R.id.payway_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        final PayWayElement element = mData.get(position);
        element.mPosition = position;
        holder.mMainText.setText(element.mName);
        holder.mSubHeadText.setText(element.mChoiceSale ? 
                R.string.setting_payway_in_sale : R.string.setting_payway_out_sale);
        holder.mType.setText(element.mNameType);
        
        return convertView;
    }

    private class ViewHolder {
        TextView mMainText;
        TextView mSubHeadText;
        TextView mType;
    }
}
