
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.List;
import java.util.Map;

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

public class ISingleChecksAdapter_New extends BaseAdapter 
{
	private Context context;
	
	private LayoutInflater inflater;
		
	private List<Map> nameItems;
	
	private int resource;
	
	private String originId;
	
	public ISingleChecksAdapter_New(Context context, List<Map> datas)
	{
		this.context = context;
		this.nameItems = datas;
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
		String key = null;
		String val = null;
		if(data != null)
		{	
			listItemView.nameTxt.setText(data.get("typeName").toString());			
			if(!StringUtils.isEmpty(originId) && StringUtils.isEquals(data.get("typeVal"), originId))
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

	public void initWithData(List<Map> nameItems, String originId)
	{
		this.originId = originId;
		this.nameItems = nameItems;
	}
	public void setSelectItem(int position) 
	{
		
	}
}
