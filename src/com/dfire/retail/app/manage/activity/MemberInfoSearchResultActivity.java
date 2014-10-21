package com.dfire.retail.app.manage.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.listview.PullToRefreshBase;
import com.daoshun.lib.listview.PullToRefreshBase.Mode;
import com.daoshun.lib.listview.PullToRefreshListView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.data.CustomerVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.netData.MemberInfoSearchResult;
import com.dfire.retail.app.manage.netData.MenberInfoSearchParam;

public class MemberInfoSearchResultActivity extends TitleActivity {
	private PullToRefreshListView mSearchResult;// 搜索结果列表
	private SearchResultAdapter mSearchResultAdapter;// 会员信息搜索结果适配器
	private ArrayList<CustomerVo> mDatas;// 搜索结果数据存放列表
	private String KEYWORDS = "";// 会员卡号，姓名，手机号
	private String CARD_TYPE = "";// 卡类型
	private MemberInfoSearchTask mMemberInfoSearchTask;// 会员信息搜索异步
	private ProgressDialog mProgressDialog;
	private Integer mCURRENT_PAGE = 1;// 当前页

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_info_search_layout);

		// 接收上个画面传过来的数据
		Intent intent = getIntent();
		KEYWORDS = intent.getStringExtra(Constants.INTENT_EXTRA_KEYWORDS);
		CARD_TYPE = intent.getStringExtra(Constants.INTENT_EXTRA_CARD_TYPE);

		initDialog();
		findViews();
		addListener();

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

	/*
	 * 会员信息查询异步
	 */
	private void doRequest() {
		mMemberInfoSearchTask = new MemberInfoSearchTask();
		mMemberInfoSearchTask.execute();

	}

	private class MemberInfoSearchTask extends
			AsyncTask<MenberInfoSearchParam, Void, MemberInfoSearchResult> {
		JSONAccessor accessor;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					if (mMemberInfoSearchTask != null) {
						mMemberInfoSearchTask.stop();
						mMemberInfoSearchTask = null;
					}
				}
			});
			if (!mProgressDialog.isShowing()) {
				mProgressDialog.show();
			}
		}

		@Override
		protected MemberInfoSearchResult doInBackground(
				MenberInfoSearchParam... params) {
			accessor = new JSONAccessor(MemberInfoSearchResultActivity.this,
					HttpAccessor.METHOD_POST);
			accessor.enableJsonLog(true);
			MenberInfoSearchParam param = new MenberInfoSearchParam();
			param.setSessionId(RetailApplication.mSessionId);
			param.setVersion(Constants.VEASION);
			param.setKeywords(KEYWORDS);
			param.setDateFrom(RetailApplication.mShopInfo.getShopId());// 商户ID,SHOP_ID
			param.setCurrentPage(mCURRENT_PAGE);
			MemberInfoSearchResult result = accessor.execute(
					Constants.MEMBER_INFO_SEARCH, param,
					MemberInfoSearchResult.class);
			return result;
		}

		@Override
		protected void onPostExecute(MemberInfoSearchResult result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			mSearchResult.onRefreshComplete();
			if (result != null) {
				if (result.getReturnCode() != null) {
					if (result.getReturnCode().equals("success")) {
						if (result.getPageSize() != null) {
							if (result.getCustomerList() != null) {
								mDatas.clear();
								mDatas.addAll(result.getCustomerList());
								mSearchResultAdapter.notifyDataSetChanged();
								mCURRENT_PAGE++;
								if (mCURRENT_PAGE >= result.getPageSize()) {
									mSearchResult
											.setMode(Mode.PULL_DOWN_TO_REFRESH);
								} else {
									mSearchResult.setMode(Mode.BOTH);
								}
							}
						} else {
							Toast.makeText(MemberInfoSearchResultActivity.this,
									R.string.empty_data, Toast.LENGTH_SHORT)
									.show();
						}
					} else {
						Toast.makeText(MemberInfoSearchResultActivity.this,
								result.getExceptionMessage(),
								Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(MemberInfoSearchResultActivity.this,
							"返回消息为空",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(MemberInfoSearchResultActivity.this,
						R.string.net_error, Toast.LENGTH_SHORT).show();
			}
		}

		private void stop() {
			accessor.stop();
			if (mMemberInfoSearchTask != null) {
				mMemberInfoSearchTask.cancel(true);
			}
		}
	}

	private void findViews() {
		setTitleRes(R.string.member_info_title);
		showBackbtn();
		mSearchResult = (PullToRefreshListView) findViewById(R.id.member_info_search_result_lv);
		mDatas = new ArrayList<CustomerVo>();
		mSearchResultAdapter = new SearchResultAdapter();
		mSearchResult.getRefreshableView().setAdapter(mSearchResultAdapter);
		mSearchResult.setMode(Mode.PULL_FROM_START);
		mSearchResult.setRefreshing();
	}

	private void addListener() {

		// 列表点击事件
		mSearchResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MemberInfoSearchResultActivity.this,
						MemberInfoDetailActivity.class);
				intent.putExtra(Constants.INTENT_CUSTOMERID, mDatas.get(arg2)
						.getCustomerId());
				intent.putExtra(Constants.INTENT_CUSTOMER_NAME, mDatas
						.get(arg2).getName());
				startActivity(intent);
			}
		});
		// 列表刷新和加载更多操作
		mSearchResult
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(
								MemberInfoSearchResultActivity.this,
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						// 启动异步
						mCURRENT_PAGE = 1;
						doRequest();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(
								MemberInfoSearchResultActivity.this,
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						// 不做操作
					}
				});
	}

	/*
	 * 会员信息搜索结果适配器
	 */
	class SearchResultAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (mDatas != null) {
				return mDatas.size();
			} else {
				return 0;
			}
		}

		@Override
		public CustomerVo getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(
						MemberInfoSearchResultActivity.this).inflate(
						R.layout.member_info_search_item, null);
				holder.name = (TextView) convertView
						.findViewById(R.id.member_info_search_result_item_user_name);
				holder.phone = (TextView) convertView
						.findViewById(R.id.member_info_search_result_item_user_phone);
				holder.cardType = (TextView) convertView
						.findViewById(R.id.member_info_search_result_item_card_type);
				holder.balance = (TextView) convertView
						.findViewById(R.id.member_info_search_result_item_balance);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final CustomerVo memberInfo = getItem(position);
			if (memberInfo.getName() != null) {
				holder.name.setText(memberInfo.getName());
			} else {
				holder.name.setText("");
			}
			if (memberInfo.getMobile() != null) {
				holder.phone.setText(memberInfo.getMobile());
			} else {
				holder.phone.setText("");
			}
			if (KEYWORDS != null) {
				holder.cardType.setText(KEYWORDS);
			} else {
				holder.cardType.setText("");
			}
			return convertView;
		}

		class ViewHolder {
			public TextView name;
			public TextView phone;
			public TextView cardType;
			public TextView balance;
		}
	}
}
