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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.SupplyListAdapter;
import com.dfire.retail.app.manage.data.bo.SupplyManageListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.vo.supplyManageVo;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/**
 * 供应商列表
 * @author ys
 */
public class SupplierManagementActivity extends TitleActivity implements OnItemClickListener, OnClickListener {

	public static SupplierManagementActivity instance = null;
	
	private EditText searchEt;

	private TextView searchTxt;

	private PullToRefreshListView supplyList;

	private ImageButton addSupply;

	private Integer pageSize = 0;

	private SupplyListAdapter supplyListAdapter;

	public List<supplyManageVo> supplyManageVos;

	public int currentPage = 1;

	private String findParameter = null;
	
	private int mode;// 判断异步执行完是否禁用加载更

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_management);
		setTitleText("供应商");
		instance = this;
		showBackbtn();
		findSupplyView();
		supplyList.setMode(Mode.PULL_FROM_START);
		getSupplyList();
		supplyList.setRefreshing();
	}

	private void findSupplyView() {
		supplyManageVos = new ArrayList<supplyManageVo>();
		searchEt = (EditText) findViewById(R.id.et_search);
		searchTxt = (TextView) findViewById(R.id.txt_search);
		searchTxt.setOnClickListener(this);
		addSupply = (ImageButton) findViewById(R.id.minus);
		addSupply.setOnClickListener(this);
		supplyList = (PullToRefreshListView) findViewById(R.id.supply_list_view);
		supplyListAdapter = new SupplyListAdapter(SupplierManagementActivity.this, supplyManageVos);
		supplyList.setAdapter(supplyListAdapter);
		supplyList.setOnItemClickListener(this);
		supplyList.setMode(Mode.BOTH);
		new ListAddFooterItem(this, supplyList.getRefreshableView());
		// 列表刷新和加载更多操作
		supplyList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(SupplierManagementActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = 1;//选择以后初始化页数
				getSupplyList();
			}
			/**
			 * 上拉加载更多
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(SupplierManagementActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				getSupplyList();
			}
		});
	}
	/**
	 * 加条件以后刷新数据
	 */
	public void reFreshing(){
		currentPage = 1;//选择以后初始化页数
		supplyList.setMode(Mode.PULL_FROM_START);
		getSupplyList();
		supplyList.setRefreshing();
	}
	/**
	 * 查询供应商
	 */
	public void getSupplyList() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SUPPLY_INFO_MANAGE_LIST);
		params.setParam("currentPage", currentPage);
		params.setParam("showEntityFlg", 0);
		params.setParam("findParameter", findParameter);
		new AsyncHttpPost(this, params, SupplyManageListBo.class,false, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				SupplyManageListBo bo = (SupplyManageListBo)oj;
				if (bo!=null) {
					pageSize = bo.getPageCount();
					List<supplyManageVo> supplys = new ArrayList<supplyManageVo>();
					supplys = bo.getSupplyManageList();
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							supplyManageVos.clear();
						}
						if (supplys != null && supplys.size() > 0) {
							supplyList.setMode(Mode.BOTH);
							supplyManageVos.addAll(supplys);
						}else {
							mode = 1;
						}
						supplyListAdapter.notifyDataSetChanged();
					}else {
						supplyManageVos.clear();
						supplyListAdapter.notifyDataSetChanged();
						mode = 1;
					}
					supplyList.onRefreshComplete();
					if (mode == 1) {
						supplyList.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
				}
			}
			
			@Override
			public void onFail(Exception e) {
				supplyList.onRefreshComplete();
			}
		}).execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		supplyManageVo supplyManageVo = supplyManageVos.get(position-1);
		Intent supplyIntent = new Intent(SupplierManagementActivity.this, SupplyManagerDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("supplyManageVo", supplyManageVo);
		supplyIntent.putExtras(bundle);
		startActivity(supplyIntent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.minus:
			Intent supplyAdd = new Intent(SupplierManagementActivity.this, SupplyManagerAddActivity.class);
			startActivity(supplyAdd);
			break;
		case R.id.txt_search:
			findParameter = searchEt.getText().toString();
			reFreshing();
			break;
		}
	}
}
