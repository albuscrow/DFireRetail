package com.dfire.retail.app.manage.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.NoticeVo;

public class GetMessageAdapter	extends BaseAdapter {
	List<NoticeVo> noticeVo;
	private LayoutInflater layoutInflater;
	public GetMessageAdapter(List<NoticeVo> noticeVo, Context context) {
		super();
		this.noticeVo = noticeVo;
		this.layoutInflater =LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		
		return noticeVo.size();
	}
	@Override
	public Object getItem(int position) {
		
		return noticeVo.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		if(v==null){
			v=layoutInflater.inflate(R.layout.activity_message_manager_itme, null);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		TextView noticetitle = (TextView) v
				.findViewById(R.id.message_title_item);
		TextView PubTime = (TextView) v.findViewById(R.id.message_Optime);
		TextView noticestatus = (TextView) v
				.findViewById(R.id.message_stuatus);
		TextView messageid = (TextView) v
				.findViewById(R.id.message_messageid);
		noticetitle.setText(noticeVo.get(position).getNoticeTitle());
		Long time = Long.valueOf((noticeVo.get(position).getPublishTime()));
		PubTime.setText("发布时间：" + sdf.format(new Date(time)));
		
		Date date = new Date();
		date.getTime();
		noticestatus.setText(time < date.getTime() ? "已发布" : "未发布");
		messageid.setText(noticeVo.get(position).getNoticeId());
		return null;
	}
	
}
