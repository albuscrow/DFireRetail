package com.dfire.retail.app.manage.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.NoticeVo;

@SuppressLint({ "SimpleDateFormat", "InflateParams" })
public class MessageListsAdapter extends BaseAdapter{
	private List<NoticeVo> notice;
	private LayoutInflater layoutInflater;
	public MessageListsAdapter(Context mContext,List<NoticeVo> notice) {
		this.layoutInflater = LayoutInflater.from(mContext);
		this.notice = notice;
	}
	@Override
	public int getCount() {
		return notice.size();
	}
	@Override
	public Object getItem(int position) {
		return notice.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (v == null) {
			v = layoutInflater.inflate(R.layout.activity_message_manager_itme, null);
		}
		TextView noticetitle = (TextView) v.findViewById(R.id.message_title_item);
		TextView PubTime = (TextView) v.findViewById(R.id.message_Optime);
		TextView noticestatus = (TextView) v.findViewById(R.id.message_stuatus);
		TextView messageid = (TextView) v.findViewById(R.id.message_messageid);
		noticetitle.setText(notice.get(position).getNoticeTitle());
		Long time = Long.valueOf((notice.get(position).getPublishTime()));
		PubTime.setText("发布时间：" + sdf.format(new Date(time)));
		
		Date date = new Date();
		date.getTime();
		noticestatus.setText(time < date.getTime() ? "已发布" : "未发布");
		messageid.setText(notice.get(position).getNoticeId());
		return v;
	}
}
