package com.dfire.retail.app.manage.activity.messagemanage;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.NoticeVo;
/**
 * 查看公告  不可修改
 *
 */
@SuppressLint("SimpleDateFormat")
public class MessageDetailActivity extends TitleActivity{
	private TextView message_shop;
	private TextView message_title;
	private TextView message_date;
	private TextView message_time;
	private EditText message_Content;
	private NoticeVo noticeVos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_manager_detail);
		findview();
		showBackbtn();
		setTitleText("公告信息");
	}

	public void findview() {
		noticeVos = (NoticeVo) getIntent().getSerializableExtra("noticeVo");
		message_shop = (TextView) findViewById(R.id.message_shop);
		message_title = (TextView) findViewById(R.id.message_title);
		message_date = (TextView) findViewById(R.id.message_date);
		message_time = (TextView) findViewById(R.id.message_time);
		message_Content = (EditText) findViewById(R.id.message_Content);
		message_Content.setFocusable(false);
		mBack = (ImageButton) findViewById(R.id.title_back);
		mRight = (ImageButton) findViewById(R.id.title_right);
		if (noticeVos!=null) {
			message_shop.setText(noticeVos.getTargetShopName());
			message_title.setText(noticeVos.getNoticeTitle());
			message_Content.setText(noticeVos.getNoticeContent());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			message_date.setText(sdf.format(new Date(Long.parseLong(noticeVos.getPublishTime()))).substring(0, 10));
			message_time.setText(sdf.format(new Date(Long.parseLong(noticeVos.getPublishTime()))).subSequence(11, 16));
		}
	}
}
