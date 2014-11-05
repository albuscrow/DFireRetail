/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.LogisticsRecordAdapter;
import com.dfire.retail.app.manage.data.LogisticsVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

/**
 * 物流记录查询结果列表
 * @author 李锦运
 *2014-10-28
 */
public class LogisticsRecordsListActivity extends TitleActivity{
	
	private LogisticsRecordAdapter logisticsrAdapter;
	
	private List<LogisticsVo> logisticsList = new ArrayList<LogisticsVo>();
	
	private ListView listView;
	
	private boolean is_divPage;// 是否进行分页操作
	
	private boolean nodata = false;
	
	private Integer pageSize = 0;
	
	private int currentPage = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logistics_records_list);
		setTitleText("物流记录");

		showBackbtn();
		findView();
	}
	public void findView(){
		listView = (ListView) findViewById(R.id.logistics_records_lv);
		logisticsrAdapter = new LogisticsRecordAdapter(this,logisticsList);
		listView.setAdapter(logisticsrAdapter);
		initData();
		
		listView.setOnItemClickListener(new ItemClickListener());
		//设置listView滑动分页
		this.listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				 if (is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (nodata) {
						currentPage++;
						initData();
					}
				 }
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});
	}
	
	/**点击单条物流记录进入明细页面*/
	private final class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ListView listView = (ListView) parent;
			LogisticsVo check = (LogisticsVo) listView.getItemAtPosition(position);
			Log.i("logisticsList", check.toString());
			Intent list = new Intent(LogisticsRecordsListActivity.this, LogisticsRecordDetailActivity.class);
			list.putExtra("logisticsId", check.getLogisticsId());
			list.putExtra("recordType", check.getRecordType());
			list.putExtra("sendEndTime", check.getSendEndTIme());
			startActivity(list);
		}
	}
	public void initData(){
		Intent intent = getIntent();
		Long sendEndTime = null;
		String dateStr = intent.getStringExtra("sendEndTime");
		if (dateStr!=null&&!dateStr.equals("")) {
			try {
				sendEndTime = 	new SimpleDateFormat("yyyy-MM-dd").parse(dateStr).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		RequestParameter params = new RequestParameter();
		params.setUrl(Constants.LOGISTICS_LIST);
		params.setParam("shopId", intent.getStringExtra("shopId"));
		params.setParam("supplyId", intent.getStringExtra("supplyId"));
		params.setParam("sendEndTime",sendEndTime);
		params.setParam("logisticsNo",intent.getStringExtra("logisticsNo"));
		params.setParam("currentPage",currentPage);
		
		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				JsonUtil ju = new JsonUtil(str);
				List<LogisticsVo> resultList = (List<LogisticsVo>) ju.get("logisticsList", new TypeToken<List<LogisticsVo>>() {}.getType());
				pageSize = (Integer)ju.get(Constants.PAGE_SIZE,Integer.class);
				
				logisticsList.addAll(resultList);
				
				if (resultList != null && resultList.size() > 0) {
					if (currentPage<=pageSize){
						nodata = true;
					}
				}
				logisticsrAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		}).execute();
	}
}
