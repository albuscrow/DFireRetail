
package com.dfire.retail.app.manage.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.util.StringUtils;
import com.google.gson.internal.LinkedTreeMap;

@SuppressLint("InflateParams")
public class ISingleChecksAdapter_New extends BaseAdapter 
{
	private Context context;
	
	private List<Map<String,String>> nameItems;
	
	private String typeVal;
	
	public ISingleChecksAdapter_New(Context context, List<Map<String,String>> datas,String typeVal)
	{
		this.context = context;
		this.nameItems = datas;
		this.typeVal = typeVal;
	}
	/** {@inheritDoc} */
	@Override
	public int getCount() 
	{	
		return nameItems.size();
	}

	/** {@inheritDoc} */
	@Override
	public Object getItem(int position) 
	{
		return nameItems.get(position);
	}

	/** {@inheritDoc} */
	@Override
	public long getItemId(int position)
	{
		return 0;
	}
	
	static class ListItemView{
		public TextView nameTxt;
		public ImageView selectImg;
	}

	/** {@inheritDoc} */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ListItemView listItemView=null;
		
		if(convertView==null)
		{
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_supply_type_select_adapter, null);
			listItemView=new ListItemView();
			listItemView.nameTxt=(TextView)convertView.findViewById(R.id.txtLabel);
			listItemView.selectImg = (ImageView)convertView.findViewById(R.id.imgCheck);
			convertView.setTag(listItemView);
		}else{
			listItemView=(ListItemView) convertView.getTag();
		}		
		LinkedTreeMap<String, String> data = (LinkedTreeMap<String, String>) nameItems.get(position);
		if(data != null)
		{	
			listItemView.nameTxt.setText(data.get("typeName").toString());			
			if(!StringUtils.isEmpty(typeVal) && StringUtils.isEquals(data.get("typeVal"), typeVal))
			{
				listItemView.selectImg.setVisibility(View.VISIBLE);
			}
			else
			{
				listItemView.selectImg.setVisibility(View.INVISIBLE);
			}
			
		}
		return convertView;
	}

	public void initWithData(List<Map<String,String>> nameItems, String originId)
	{
		this.typeVal = originId;
		this.nameItems = nameItems;
	}
	public void setTypeVal(String typeVal,int positionFlag){
		this.typeVal = typeVal;
		
	}

}
