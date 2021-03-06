package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;

public class SelectDateAdapter extends BaseAdapter {
	
	private static final String TAG = "SelectDateAdapter";

	ArrayList<DateItem> mList = new ArrayList<DateItem>();
	private LayoutInflater mLayoutInflater;
	
	public SelectDateAdapter(Context context,ArrayList<DateItem> list) {		
		mList = list;
		mLayoutInflater =  LayoutInflater.from(context);
		Log.d(TAG,"mList size = "+mList.size());
	}
    private class ViewHolder {
        TextView title;
        Button ischeck;
 
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
	            convertView = mLayoutInflater.inflate(R.layout.select_date_item, null);
	            viewHolder = new ViewHolder();
	            viewHolder.title = (TextView) convertView.findViewById(R.id.date_item_title);
	            viewHolder.ischeck  = (Button) convertView.findViewById(R.id.date_item_select);	
	
	            convertView.setTag(viewHolder);
	        } else {
	            viewHolder = (ViewHolder) convertView.getTag();
	        }
	        viewHolder.title.setText(mList.get(position).getTitle());
	        if(mList.get(position).isIscheck())
	        {
	        	viewHolder.ischeck.setVisibility(View.VISIBLE);
	        }
	        else
	        {
	        	viewHolder.ischeck.setVisibility(View.INVISIBLE);
	        }
	
	        return convertView;
	}
}

