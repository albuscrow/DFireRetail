package com.dfire.retail.app.manage.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.listview.PullToRefreshBase.Mode;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.common.CardStatusDialog;
import com.dfire.retail.app.manage.common.CardTypeDialog;
import com.dfire.retail.app.manage.common.DateDialog;
import com.dfire.retail.app.manage.data.CardSummarizingVo;
import com.dfire.retail.app.manage.data.CustomerVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.netData.MemberInfoDetailParam;
import com.dfire.retail.app.manage.netData.MemberInfoDetailResult;
import com.dfire.retail.app.manage.netData.MemberInfoSearchResult;
import com.dfire.retail.app.manage.netData.SaveMemberInfoResult;
import com.dfire.retail.app.manage.netData.SaveMemberInfoparam;
import com.google.gson.Gson;

public class MemberInfoDetailActivity extends TitleActivity {
	private EditText mCardId;// 会员卡号
	private EditText mUsrName;// 会员名
	private EditText mTelephone;// 手机号码
	private TextView mCardType;// 卡类型
	private EditText mBalance;// 卡内余额(元)
	private EditText mConsumeAmount;// 累计消费(元)
	private EditText mConsumeCount;// 消费次数
	private EditText mPoint;// 卡内积分
	private EditText mActiveDate;// 办卡日期
	private TextView mStatus;// 卡状态

	private EditText mCertificate;// 身份证
	private TextView mBirthday;// 生日
	private EditText mWeixin;// 微信
	private EditText mAddress;// 联系地址
	private EditText mEmail;// 邮箱
	private EditText mZipcode;// 邮编
	private EditText mJob;// 职业
	private EditText mMemo;// 备注

	private RelativeLayout mCardTypeSelect;// 卡类型选择按钮
	private RelativeLayout mStatusSelect;// 卡状态选择按钮
	private RelativeLayout mBirthdaySelect;// 生日日期选择按钮

	private ImageView mRecharge;// 充值按钮
	private ImageView mSearchRecord;// 交易记录查询按钮
	private ImageView mChangeGoods;// 兌换商品商品按钮

	private ProgressDialog mProgressDialog;// 对话框
	private String CUSTOMER_ID;// 会员ID
	private String CUSTOMER_NAME = "";// 会员姓名
	private MemberInfoSearchTask mMemberInfoDetailTask;// 获取会员详情异步
	private List<CustomerVo> mDatas = new ArrayList<CustomerVo>();// 会员信息详情

	private String mCARD_TYPE;// 卡类型
	private CardTypeDialog mCardDialog;// 卡类型对话框

	private String mCARD_STATUS;// 卡状态
	private CardStatusDialog mCardStatusDialog;// 卡状态对话框

	private String mBIRTH_DAY;// 生日
	private DateDialog mBirthDayDialog;// 生日对话框

	private ImageButton mLeft;// 取消按钮
	private ImageButton mRight;// 保存按钮
	protected SaveMemberInfoTask mSaveMemberInfoTask;
	public String OPERRATE_TYPE = "edit";// 操作类别：add：添加edit：编辑
	protected CustomerVo mSaveDatas = new CustomerVo();// 添加用户时提交的信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_info_detail_layout);

		// 接收上个画面传过来的数据
		Intent intent = getIntent();
		CUSTOMER_ID = intent.getStringExtra(Constants.INTENT_CUSTOMERID);
		CUSTOMER_NAME = intent.getStringExtra(Constants.INTENT_CUSTOMER_NAME);

		initDialog();
		findViews();
		if (CUSTOMER_ID == null) {
			// 为添加用户模式
			mRight = change2saveMode();
			mLeft = change2saveFinishMode();
			mCardId.requestFocus();
			OPERRATE_TYPE = "add";
			addListeners();
		} else {
			// 为编辑已有用户信息模式
			OPERRATE_TYPE = "edit";
			addListeners();
			// 执行显示已有用户信息异步
			doRequest();
		}

	}

	/**
	 * 注册单击事件监听器
	 */
	private void addListeners() {
		ButtonListener listener = new ButtonListener();
		mRecharge.setOnClickListener(listener);
		mSearchRecord.setOnClickListener(listener);
		mChangeGoods.setOnClickListener(listener);
		mStatusSelect.setOnClickListener(listener);
		mBirthdaySelect.setOnClickListener(listener);
		// 显示卡类型选择对话框
		mCardTypeSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCardDialog();
			}
		});
		mRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取要保存的所有信息
				getInfo();
				if (!mUsrName.getText().toString().equals("")) {
					if (!mTelephone.getText().toString().equals("")) {
						// 保存信息异步
						mSaveMemberInfoTask = new SaveMemberInfoTask();
						mSaveMemberInfoTask.execute();
						finish();
						Intent back_user_info = new Intent(MemberInfoDetailActivity.this, MemberInfoActivity.class);
						startActivity(back_user_info);
					} else {
						Toast.makeText(MemberInfoDetailActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MemberInfoDetailActivity.this, "请输入会员姓名", Toast.LENGTH_SHORT).show();
				}
			}

		});
		// //取消添加用户信息
		// mTitleActivity.getLeftButton().setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// finish();
		// Intent back_user_info = new Intent(MemberInfoDetailActivity.this,
		// MemberInfoActivity.class);
		// startActivity(back_user_info);
		// }
		//
		// });
	}

	/*
	 * 
	 * /获取要保存的所有信息
	 */
	private void getInfo() {
		if (!mUsrName.getText().toString().equals("")) {
			mSaveDatas.setName(mUsrName.getText().toString());
		} else {
			mSaveDatas.setName("");
		}
		if (!mTelephone.getText().toString().equals("")) {
			mSaveDatas.setMobile(mTelephone.getText().toString());
		} else {
			mSaveDatas.setMobile("");
		}
		if (!mCertificate.getText().toString().equals("")) {
			mSaveDatas.setCertificate(mCertificate.getText().toString());
		} else {
			mSaveDatas.setCertificate("");
		}
		if (!mBirthday.getText().toString().equals("")) {
			mSaveDatas.setBirthday(mBirthday.getText().toString());
		} else {
			mSaveDatas.setBirthday("");
		}
		if (!mWeixin.getText().toString().equals("")) {
			mSaveDatas.setWeixin(mWeixin.getText().toString());
		} else {
			mSaveDatas.setWeixin("");
		}
		if (!mAddress.getText().toString().equals("")) {
			mSaveDatas.setAddress(mAddress.getText().toString());
		} else {
			mSaveDatas.setAddress("");
		}
		if (!mAddress.getText().toString().equals("")) {
			mSaveDatas.setAddress(mAddress.getText().toString());
		} else {
			mSaveDatas.setAddress("");
		}
		if (!mEmail.getText().toString().equals("")) {
			mSaveDatas.setEmail(mEmail.getText().toString());
		} else {
			mSaveDatas.setEmail("");
		}
		if (!mZipcode.getText().toString().equals("")) {
			mSaveDatas.setZipcode(mZipcode.getText().toString());
		} else {
			mSaveDatas.setZipcode("");
		}
		if (!mJob.getText().toString().equals("")) {
			mSaveDatas.setJob(mJob.getText().toString());
		} else {
			mSaveDatas.setJob("");
		}
		if (!mMemo.getText().toString().equals("")) {
			mSaveDatas.setMemo(mMemo.getText().toString());
		} else {
			mSaveDatas.setMemo("");
		}

	}

	private class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.member_info_detail_balance_recharge:
				// 显示充值画面
				Intent intent1;
				break;
			case R.id.member_info_detail_consumeAmount_search_record:
				// 显示交易记录查询画面
				// Intent intent2 = new Intent(MemberInfoDetailActivity.this, CardTransactionRecordsActivity.class);
				// intent2.putExtra(Constants.INTENT_CARD_ID, mCardId.getText().toString());
				// intent2.putExtra(Constants.INTENT_CUSTOMER_NAME, mUsrName.getText().toString());
				// startActivity(intent2);
				break;
			case R.id.member_info_detail_point_change_goods:
				// 显示兌换商品画面
				Intent intent3;
				break;
			case R.id.member_info_detail_status_rl:
				// 显示卡状态选择对话框
				showCardStatusDialog();
				break;
			case R.id.member_info_detail_birthday_rl:
				// 显示生日对话框
				showBirthDayDialog();
				break;
			}

		}

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

	private void findViews() {
		setTitleText(CUSTOMER_NAME);
		mCardId = (EditText) findViewById(R.id.member_info_detail_card_id);
		mUsrName = (EditText) findViewById(R.id.member_info_detail_usr_name);
		mTelephone = (EditText) findViewById(R.id.member_info_detail_telephone);
		mCardType = (TextView) findViewById(R.id.member_info_detail_card_type);
		mBalance = (EditText) findViewById(R.id.member_info_detail_balance);
		mConsumeAmount = (EditText) findViewById(R.id.member_info_detail_consumeAmount);
		mConsumeCount = (EditText) findViewById(R.id.member_info_detail_consumeCount);
		mPoint = (EditText) findViewById(R.id.member_info_detail_point);
		mActiveDate = (EditText) findViewById(R.id.member_info_detail_activeDate);
		mStatus = (TextView) findViewById(R.id.member_info_detail_status);

		mCertificate = (EditText) findViewById(R.id.member_info_detail_certificate);
		mBirthday = (TextView) findViewById(R.id.member_info_detail_birthday);
		mWeixin = (EditText) findViewById(R.id.member_info_detail_weixin);
		mAddress = (EditText) findViewById(R.id.member_info_detail_address);
		mEmail = (EditText) findViewById(R.id.member_info_detail_email);
		mZipcode = (EditText) findViewById(R.id.member_info_detail_zipcode);
		mJob = (EditText) findViewById(R.id.member_info_detail_job);
		mMemo = (EditText) findViewById(R.id.member_info_detail_memo);

		mCardTypeSelect = (RelativeLayout) findViewById(R.id.member_info_detail_card_type_rl);
		mStatusSelect = (RelativeLayout) findViewById(R.id.member_info_detail_status_rl);
		mBirthdaySelect = (RelativeLayout) findViewById(R.id.member_info_detail_birthday_rl);

		mRecharge = (ImageView) findViewById(R.id.member_info_detail_balance_recharge);
		mSearchRecord = (ImageView) findViewById(R.id.member_info_detail_consumeAmount_search_record);
		mChangeGoods = (ImageView) findViewById(R.id.member_info_detail_point_change_goods);
	}

	/*
	 * 会员信息详情异步
	 */
	private void doRequest() {
		// 获取会员扩展信息异步
		mMemberInfoDetailTask = new MemberInfoSearchTask();
		mMemberInfoDetailTask.execute();

	}

	/*
	 * /获取会员扩展信息异步
	 */
	private class MemberInfoSearchTask extends AsyncTask<MemberInfoDetailParam, Void, MemberInfoDetailResult> {
		JSONAccessor accessor = new JSONAccessor(MemberInfoDetailActivity.this, HttpAccessor.METHOD_POST);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					if (mMemberInfoDetailTask != null) {
						mMemberInfoDetailTask.stop();
						mMemberInfoDetailTask = null;
					}
				}
			});
			if (!mProgressDialog.isShowing()) {
				mProgressDialog.show();

			}
		}

		@Override
		protected MemberInfoDetailResult doInBackground(MemberInfoDetailParam... params) {
			MemberInfoDetailParam param = new MemberInfoDetailParam();
			param.setSessionId(RetailApplication.mSessionId);
			param.setVersion(Constants.VEASION);
			param.setCustomerId(CUSTOMER_ID);
			MemberInfoDetailResult result = accessor.execute(Constants.MEMBER_INFO_DETAIL, param, MemberInfoDetailResult.class);
			return result;
		}

		@Override
		protected void onPostExecute(MemberInfoDetailResult result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			if (result != null) {
				if (result.getReturnCode() != null) {
					if (result.getReturnCode().equals("success")) {
						if (result.getCustomer() != null) {
							mDatas.clear();
							mDatas.addAll(result.getCustomer());
							initViews();
						} else {
							Toast.makeText(MemberInfoDetailActivity.this, R.string.empty_data, Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(MemberInfoDetailActivity.this, result.getExceptionMessage(), Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MemberInfoDetailActivity.this, "没有返回消息", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(MemberInfoDetailActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
			}
		}

		private void stop() {
			accessor.stop();
			if (mMemberInfoDetailTask != null) {
				mMemberInfoDetailTask.cancel(true);
			}
		}
	}

	/*
	 * /填充会员扩展信息
	 */
	public void initViews() {
		for (CustomerVo customer : mDatas) {
			if (customer.getCertificate() != null) {
				mCertificate.setText(customer.getCertificate());
			} else {
				mCertificate.setText("");
			}
			if (customer.getBirthday() != null) {
				mBirthday.setText((CharSequence) customer.getBirthday());
			} else {
				mBirthday.setText("");
			}
			if (customer.getWeixin() != null) {
				mWeixin.setText(customer.getWeixin());
			} else {
				mWeixin.setText("");
			}
			if (customer.getAddress() != null) {
				mAddress.setText(customer.getAddress());
			} else {
				mAddress.setText("");
			}
			if (customer.getEmail() != null) {
				mEmail.setText(customer.getEmail());
			} else {
				mEmail.setText("");
			}
			if (customer.getZipcode() != null) {
				mZipcode.setText(customer.getZipcode());
			} else {
				mZipcode.setText("");
			}
			if (customer.getJob() != null) {
				mJob.setText(customer.getJob());
			} else {
				mJob.setText("");
			}
			if (customer.getMemo() != null) {
				mMemo.setText(customer.getMemo());
			} else {
				mMemo.setText("");
			}

		}
	}

	/*
	 * 
	 * 显示卡类型选择框
	 */
	private void showCardDialog() {
		if (mCardDialog == null) {
			mCardDialog = new CardTypeDialog(MemberInfoDetailActivity.this);
			mCardDialog.show();
			mCardDialog.getConfirmButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mCARD_TYPE = mCardDialog.getCurrentData();
					mCardType.setText(mCARD_TYPE);
					mCardDialog.dismiss();
				}
			});

			mCardDialog.getCancelButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mCardDialog.dismiss();
				}
			});
		} else {
			mCardDialog.show();
		}

	}

	/*
	 * 
	 * 显示卡状态选择框
	 */
	private void showCardStatusDialog() {
		if (mCardStatusDialog == null) {
			mCardStatusDialog = new CardStatusDialog(MemberInfoDetailActivity.this);
			mCardStatusDialog.show();
			mCardStatusDialog.getConfirmButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mCARD_STATUS = mCardStatusDialog.getCurrentData();
					mStatus.setText(mCARD_STATUS);
					mCardStatusDialog.dismiss();
				}
			});

			mCardStatusDialog.getCancelButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mCardStatusDialog.dismiss();
				}
			});
		} else {
			mCardStatusDialog.show();
		}

	}

	/*
	 * 
	 * 显示生日选择框
	 */
	private void showBirthDayDialog() {
		if (mBirthDayDialog == null) {
			mBirthDayDialog = new DateDialog(MemberInfoDetailActivity.this);
			mBirthDayDialog.show();
			mBirthDayDialog.getConfirmButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mBIRTH_DAY = mBirthDayDialog.getCurrentData();
					mBirthday.setText(mBIRTH_DAY);
					mBirthDayDialog.dismiss();
				}
			});

			mBirthDayDialog.getCancelButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mBirthDayDialog.dismiss();
				}
			});
		} else {
			mBirthDayDialog.show();
		}

	}

	/*
	 * 保存会员信息异步
	 */
	private class SaveMemberInfoTask extends AsyncTask<SaveMemberInfoparam, Void, SaveMemberInfoResult> {
		JSONAccessor accessor = new JSONAccessor(MemberInfoDetailActivity.this, HttpAccessor.METHOD_POST);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					if (mSaveMemberInfoTask != null) {
						mSaveMemberInfoTask.stop();
						mSaveMemberInfoTask = null;
					}
				}
			});
			if (!mProgressDialog.isShowing()) {
				mProgressDialog.show();

			}
		}

		@Override
		protected SaveMemberInfoResult doInBackground(SaveMemberInfoparam... params) {
			accessor.enableJsonLog(true);
			SaveMemberInfoparam saveParam = new SaveMemberInfoparam();
			saveParam.setSessionId(RetailApplication.mSessionId);
			saveParam.setVersion(Constants.VEASION);
			saveParam.setCustomer(new Gson().toJson(mSaveDatas));
			saveParam.setOperateType(OPERRATE_TYPE);
			SaveMemberInfoResult saveResult = accessor.execute(Constants.SAVE_MEMBER_INFO, saveParam, SaveMemberInfoResult.class);
			return saveResult;
		}

		@Override
		protected void onPostExecute(SaveMemberInfoResult saveResult) {
			super.onPostExecute(saveResult);
			mProgressDialog.dismiss();
			if (saveResult != null) {
				if (saveResult.getReturnCode() != null) {
					if (saveResult.getReturnCode().equals("success")) {
						if (saveResult.getCustomerId() != null) {
							Toast.makeText(MemberInfoDetailActivity.this, saveResult.getCustomerId(), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(MemberInfoDetailActivity.this, "保存不成功", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(MemberInfoDetailActivity.this, saveResult.getExceptionMessage(), Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MemberInfoDetailActivity.this, "返回消息为空", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(MemberInfoDetailActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
			}
		}

		private void stop() {
			accessor.stop();
			if (mSaveMemberInfoTask != null) {
				mSaveMemberInfoTask.cancel(true);
			}
		}

	}
}
