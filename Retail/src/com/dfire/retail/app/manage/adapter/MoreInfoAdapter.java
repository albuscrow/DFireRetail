package com.dfire.retail.app.manage.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;


public class MoreInfoAdapter extends BaseAdapter {
	
	private static final String TAG = "StoreManagerAdapter";

	ArrayList<MoreInfoItem> mList = new ArrayList<MoreInfoItem>();
	private LayoutInflater mLayoutInflater;
	
	public MoreInfoAdapter(Context context,ArrayList<MoreInfoItem> list) {		
		mList = list;
		mLayoutInflater =  LayoutInflater.from(context);
		Log.d(TAG,"mList size = "+mList.size());
	}
    private class ViewHolder {
        ImageView imageView;
        TextView mainText;
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
	            convertView = mLayoutInflater.inflate(R.layout.more_info_item, null);
	            viewHolder = new ViewHolder();
	            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.more_info_icon);
	            viewHolder.mainText = (TextView) convertView.findViewById(R.id.more_info_text);	                 
	            convertView.setTag(viewHolder);
	        } else {
	            viewHolder = (ViewHolder) convertView.getTag();
	        }

		        viewHolder.imageView.setImageResource(mList.get(position).getImageId());
		        viewHolder.mainText.setText(mList.get(position).getText());

		       return convertView;
	}
}
