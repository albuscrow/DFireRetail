/**
 * 
 */
package com.dfire.retail.app.manage.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 菜单画面
 * 
 * @author qiuch
 * 
 */
public class MenuActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
		findviews();
	}

	private void findviews() {
//		((TextView) findViewById(R.id.title_text)).setText(RetailApplication.mShopInfo.getShopName());
//		((TextView) findViewById(R.id.text_view_name)).setText(RetailApplication.mUserInfo.getName());
		((TextView) findViewById(R.id.text_view_time)).setText("今天是" + new SimpleDateFormat("yyyy年MM月dd日 E", Locale.CHINA).format(new Date()));
	}

	public void ImgClickListener(View v) {
//		switch (Integer.parseInt(String.valueOf(v.getTag()))) {
//		case 1:// 会员信息
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		case 2:// 会员类型
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		case 3:// 交易查询
//			//startActivity(new Intent(MenuActivity.this, MemberTranSearchActivity.class));
//			break;
//		case 4:// 统计查询
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		case 5:// 会员充值
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		case 6:// 积分兑换
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		case 7:// 短信发送
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		case 8:// 会员卡挂失
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		case 9:// 短信模板
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		case 10:// 兑换设置
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		case 11:// 生日提醒
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		case 12:// 优惠券
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		case 13:// 充值促销
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		case 14:// 满就送
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		case 15:
//			startActivity(new Intent(MenuActivity.this, MemberInfoActivity.class));
//			break;
//		}
	}

}
