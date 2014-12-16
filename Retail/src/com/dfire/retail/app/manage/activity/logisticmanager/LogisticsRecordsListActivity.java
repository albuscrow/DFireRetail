/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.LogisticsRecordAdapter;
import com.dfire.retail.app.manage.data.LogisticsVo;
import com.dfire.retail.app.manage.data.bo.LogisticsListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/**
 * 物流记录查询结果列表
 * @author ys
 * 2014-11-14
 */
public class LogisticsRecordsListActivity extends TitleActivity{
	
	private LogisticsRecordAdapter logisticsrAdapter;
	
	private List<LogisticsVo> logisticsList = new ArrayList<LogisticsVo>();
	
	private PullToRefreshListView listView;
	
	private Integer pageSize = 0;
	
	private int currentPage = 1;
	
	private int mode;// 判断异步执行完是否禁用加载更多
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logistics_records_list);
		setTitleText("物流记录");
		showBackbtn();
		findView();
		listView.setMode(Mode.PULL_FROM_START);
		initData();
		listView.setRefreshing();
	}
	public void findView(){
		listView = (PullToRefreshListView) findViewById(R.id.logistics_records_lv);
		logisticsrAdapter = new LogisticsRecordAdapter(this,logisticsList);
		
		listView.setAdapter(logisticsrAdapter);
		listView.setOnItemClickListener(new ItemClickListener());
		listView.setMode(Mode.BOTH);
		new ListAddFooterItem(this, listView.getRefreshableView());
		// 列表刷新和加载更多操作
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(LogisticsRecordsListActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = 1;//选择以后初始化页数
				initData();
			}
			/**
			 * 上拉加载更多
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(LogisticsRecordsListActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				initData();
			}
		});
	}
	
	/**点击单条物流记录进入明细页面*/
	private final class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			LogisticsVo logisticsVo = logisticsList.get(position-1);
			Intent intent = new Intent(LogisticsRecordsListActivity.this, LogisticsRecordDetailActivity.class);
			intent.putExtra("logisticsVo", logisticsVo);
			startActivity(intent);
		}
	}
	/**获取物流列表*/
	public void initData(){
		Intent intent = getIntent();
		Long sendEndTime = null;
		try {
			sendEndTime = Long.parseLong(intent.getStringExtra("sendEndTime"));
		} catch (NumberFormatException e1) {
			sendEndTime = null;
		}
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.LOGISTICS_LIST);
		params.setParam("shopId", intent.getStringExtra("shopId"));
		params.setParam("supplyId", intent.getStringExtra("supplyId"));
		params.setParam("sendEndTime",sendEndTime);
		params.setParam("logisticsNo",intent.getStringExtra("logisticsNo"));
		params.setParam("currentPage",currentPage);
		new AsyncHttpPost(this, params, LogisticsListBo.class,false, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				listView.onRefreshComplete();
				LogisticsListBo bo = (LogisticsListBo)oj;
				if (bo!=null) {
					List<LogisticsVo> resultList = bo.getLogisticsList();
					pageSize = bo.getPageSize();
					
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							logisticsList.clear();
						}
						if (resultList != null && resultList.size() > 0) {
							listView.setMode(Mode.BOTH);
							logisticsList.addAll(resultList);
						}else {
							mode = 1;
						}
						logisticsrAdapter.notifyDataSetChanged();
					}else {
						logisticsList.clear();
						logisticsrAdapter.notifyDataSetChanged();
						mode = 1;
					}
					listView.onRefreshComplete();
					if (mode == 1) {
						listView.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
				}
			}
			@Override
			public void onFail(Exception e) {
				listView.onRefreshComplete();
			}
		}).execute();
	}
}
