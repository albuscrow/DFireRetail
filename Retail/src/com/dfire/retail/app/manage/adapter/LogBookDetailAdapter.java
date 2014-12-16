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



public class LogBookDetailAdapter  extends BaseAdapter {
	
	private static final String TAG = "LogBookDetailAdapter";

	ArrayList<CommonItem> mList = new ArrayList<CommonItem>();
	private LayoutInflater mLayoutInflater;
	
	public LogBookDetailAdapter(Context context,ArrayList<CommonItem> list) {		
		mList = list;
		mLayoutInflater =  LayoutInflater.from(context);
		Log.d(TAG,"mList size = "+mList.size());
	}
    private class ViewHolder {
        TextView title;
        TextView infomsg1;
        TextView infomsg2;

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
	            convertView = mLayoutInflater.inflate(R.layout.common_item, null);
	            viewHolder = new ViewHolder();
	            viewHolder.title = (TextView) convertView.findViewById(R.id.info_title);
	            viewHolder.infomsg1 = (TextView) convertView.findViewById(R.id.info_msg1);
	            viewHolder.infomsg2 = (TextView) convertView.findViewById(R.id.info_msg2);	          
	
	            convertView.setTag(viewHolder);
	        } else {
	            viewHolder = (ViewHolder) convertView.getTag();
	        }
	       
	        viewHolder.title.setText(mList.get(position).getInfotitle());
	        viewHolder.infomsg1.setText(mList.get(position).getInfomsg1());
	        viewHolder.infomsg2.setText(mList.get(position).getInfomsg2());
		     
		        
	        return convertView;
	}
}

