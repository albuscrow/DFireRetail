/**
 * 
 */
package com.dfire.retail.app.manage.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.common.CardTypeDialog;
import com.dfire.retail.app.manage.data.CardSummarizingVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.netData.CardSummarizingRequestData;
import com.dfire.retail.app.manage.netData.CardSummarizingResult;

/**
 * 会员管理画面
 * 
 * @author qiuch
 * 
 */
public class MemberInfoActivity extends TitleActivity {

	private ImageView mFlicking;// 扫一扫
	private EditText mSearchInput;// 搜索输入框
	private TextView mSearch;// 查询按钮

	private RelativeLayout mCardTypeRl;// 卡类型选择按钮
	private TextView mCardTypeTv;// 显示选择的卡类型

	private TextView mMemberTotleNum;// 会员总数
	private LinearLayout mMemberTypeNum;// 各类型会员总数
	private TextView mThisMonthApplyNum;// 本月申领会员总数

	private ImageView mHelp;// 帮助
	private ImageView mAdd;// 添加

	private String mCardType = "";// 卡类型
	private CardTypeDialog mCardDialog;// 卡类型对话框

	private ProgressDialog mProgressDialog;// 对话框
	private CardSummarizingTask mCardSummarizingTask;// 获取会员汇总信息异步线程
	private CardSummarizingVo mCardSummarizing = new CardSummarizingVo();// 会员信息汇总数据

	private String KEYWORDS = "";// 关键字（会员卡号，姓名，手机号，卡类型）
	private String CARD_TYPE = "";// 卡类型

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_info_layout);

		initDialog();
		findViews();
		addListeners();
		doRequest();
	}

	/**
	 * 初始化对话框
	 */
	private void initDialog() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle(R.string.app_name);
		mProgressDialog.setMessage("请稍后...");
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(true);
	}

	/**
	 * 注册单击事件监听器
	 */
	private void addListeners() {
		ButtonListener listener = new ButtonListener();
		mFlicking.setOnClickListener(listener);
		mSearch.setOnClickListener(listener);
		mHelp.setOnClickListener(listener);
		mAdd.setOnClickListener(listener);
		// 显示卡类型选择对话框
		mCardTypeRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCardDialog();
			}
		});
	}

	private class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.member_info__flicking:
				// 显示扫一扫画面
				break;
			case R.id.member_info_search:
				// 显示查询结果画面
				CARD_TYPE = mCardTypeTv.getText().toString();
				KEYWORDS = mSearchInput.getText().toString() + CARD_TYPE;
				if(!KEYWORDS.equals("")){
					Intent intent1 = new Intent(MemberInfoActivity.this,
							MemberInfoSearchResultActivity.class);
					intent1.putExtra(Constants.INTENT_EXTRA_KEYWORDS, KEYWORDS);
					intent1.putExtra(Constants.INTENT_EXTRA_CARD_TYPE, CARD_TYPE);
					startActivity(intent1);
				}else {
					Toast.makeText(MemberInfoActivity.this, R.string.input_keywords, Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.member_info_help:
				// 显示帮助画面
				break;
			case R.id.member_info_add:
				// 显示添加会员信息画面
				Intent intent2 = new Intent(MemberInfoActivity.this,
						MemberInfoDetailActivity.class);
				startActivity(intent2);
				break;
			}

		}

	}

	/**
	 * 执行获取会员信息异步请求，并填充具体数据
	 */
	private void doRequest() {
		mCardSummarizingTask = new CardSummarizingTask();
		mCardSummarizingTask.execute();
	}

	/**
	 * 获取会员信息异步请求
	 * 
	 */
	private class CardSummarizingTask extends
			AsyncTask<CardSummarizingRequestData, Void, CardSummarizingResult> {
		JSONAccessor accessor;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					if (mCardSummarizingTask != null) {
						mCardSummarizingTask.stop();
						mCardSummarizingTask = null;
					}
				}
			});
			if (!mProgressDialog.isShowing()) {
				mProgressDialog.show();
			}
		}

		@Override
		protected CardSummarizingResult doInBackground(
		CardSummarizingRequestData... params) {
			accessor = new JSONAccessor(MemberInfoActivity.this,
					HttpAccessor.METHOD_POST);
			accessor.enableJsonLog(true);
			CardSummarizingRequestData param = new CardSummarizingRequestData();
			param.setSessionId(RetailApplication.mSessionId);
			param.setVersion(Constants.VEASION);
			param.setShopId(RetailApplication.mShopInfo.getShopId());
			// param.setShopId("100");
//			CardSummarizingResult result = accessor.execute(
//					"http://122.224.66.18:8081/serviceCenter/api/customer!summarizing.action", param, CardSummarizingResult.class);
			CardSummarizingResult result = accessor.execute(
					Constants.SUMMARIZING, param, CardSummarizingResult.class);
			
			return result;
		}

		@Override
		protected void onPostExecute(CardSummarizingResult result) {
			super.onPostExecute(result);

			mProgressDialog.dismiss();
			if (result != null) {
				// if (result.getReturnCode().equals("success")) {
				if (result.getCardSummarizing() != null) {
					mCardSummarizing=result.getCardSummarizing();
					initViews();
				}
				// }
				// else {
				// Toast.makeText(MemberInfoActivity.this,
				// result.getExceptionMessage(), Toast.LENGTH_SHORT)
				// .show();
				// }
			} else {
				Toast.makeText(MemberInfoActivity.this, R.string.net_error,
						Toast.LENGTH_SHORT).show();
			}
		}

		private void stop() {
			accessor.stop();
			if (mCardSummarizingTask != null) {
				mCardSummarizingTask.cancel(true);
			}
		}
	}

	/*
	 * 填充会员总数和本月申领会员总数以及根据卡类型显示的该类型会员总数
	 */
	private void initViews() {
			if (mCardSummarizing.getMemberSum() != null) {
				mMemberTotleNum.setText(mCardSummarizing.getMemberSum() + "");
			} else {
				mMemberTotleNum.setText("");
			}
			if (mCardSummarizing.getSumPerMonth() != null) {
				mThisMonthApplyNum.setText(mCardSummarizing.getSumPerMonth());
			} else {
				mThisMonthApplyNum.setText("");
			}
			if (mCardSummarizing.getCardTypeSumMap() != null) {
				mMemberTypeNum.removeAllViews();
				for (Map.Entry<String, Integer> entry : mCardSummarizing.getCardTypeSumMap()
						.entrySet()) {
					if (!entry.getKey().toString().equals("")) {
						String TYPE = entry.getKey().toString();
						String NUMBER = entry.getValue().toString();
						View view = getLayoutInflater().inflate(
								R.layout.member_info_card_type_num_item,
								mMemberTypeNum, false);
						mMemberTypeNum.addView(view);
						TextView type = (TextView) view
								.findViewById(R.id.member_info_card_type);
						TextView number = (TextView) view
								.findViewById(R.id.member_info_card_num);
						type.setText(TYPE);
						number.setText(NUMBER);
					}
				}
				mMemberTypeNum.setVisibility(View.VISIBLE);
			} else {
				mMemberTypeNum.setVisibility(View.GONE);
			}
		
	}

	private void findViews() {
		setTitleRes(R.string.member_info_title);
		showBackbtn();
		mFlicking = (ImageView) findViewById(R.id.member_info__flicking);
		mSearchInput = (EditText) findViewById(R.id.member_info__search_input);
		mSearch = (TextView) findViewById(R.id.member_info_search);

		mCardTypeRl = (RelativeLayout) findViewById(R.id.member_info_card_type_rl);
		mCardTypeTv = (TextView) findViewById(R.id.member_info_card_type_tv);

		mMemberTotleNum = (TextView) findViewById(R.id.member_info_num);
		mMemberTypeNum = (LinearLayout) findViewById(R.id.member_info_card_type_and_num);
		mThisMonthApplyNum = (TextView) findViewById(R.id.member_info_this_month_apply_num);

		mHelp = (ImageView) findViewById(R.id.member_info_help);
		mAdd = (ImageView) findViewById(R.id.member_info_add);

	}

	private void showCardDialog() {
		if (mCardDialog == null) {
			mCardDialog = new CardTypeDialog(MemberInfoActivity.this);
			mCardDialog.show();
			mCardDialog.getConfirmButton().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							mCardType = mCardDialog.getCurrentData();
							mCardTypeTv.setText(mCardType);
							mCardDialog.dismiss();
						}
					});

			mCardDialog.getCancelButton().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							mCardDialog.dismiss();
						}
					});
		} else {
			mCardDialog.show();
		}

	}
}
