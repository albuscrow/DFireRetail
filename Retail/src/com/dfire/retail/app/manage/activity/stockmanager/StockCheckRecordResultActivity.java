package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.StockCheckRecordDetailAdapter;
import com.dfire.retail.app.manage.data.StockCheckRecordDetailVo;
import com.dfire.retail.app.manage.data.StockCheckRecordVo;
import com.dfire.retail.app.manage.data.bo.CheckStockRecordDetailBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.wheel.widget.BaseListView;

/**
 * 库存管理-盘点记录结果报告
 * @author ys
 *
 */
@SuppressLint("SimpleDateFormat")
public class StockCheckRecordResultActivity extends TitleActivity  {

	private StockCheckRecordDetailVo stockCheckRecordDetailVo;
	
	private int currentPage=1;
	
	private String stockCheckId;
	
	private StockCheckRecordVo stockCheckRecordVo;
	
	private String shopId;
	
	private StockCheckRecordDetailAdapter checkRecordDetailAdapter;
	
	private ListView checkRecordDetail;
	
	private List<StockCheckRecordDetailVo> checkRecordDetailVos;
	
	private Integer pageSize = 0;
	
	private boolean nodata;
	
	private boolean is_divPage;// 是否进行分页操作
	
    private TextView stockTotalCount,stockRealCount,exhibitCount,stockMoney,exhibit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_check_records_result);
		setTitleText("盘点结果");
		showBackbtn();
		findView();
	}
	/**
	 * 得到控件
	 */
	public void findView(){
		this.shopId = getIntent().getStringExtra("shopId").toString();
		this.stockCheckRecordVo = (StockCheckRecordVo) getIntent().getSerializableExtra("checkRecordVo");
		this.checkRecordDetailVos = new ArrayList<StockCheckRecordDetailVo>();
		this.stockTotalCount = (TextView) findViewById(R.id.stockTotalCount);
		this.stockRealCount = (TextView) findViewById(R.id.stockRealCount);
		this.exhibitCount = (TextView) findViewById(R.id.exhibitCount);
		this.stockMoney = (TextView) findViewById(R.id.stockMoney);
		this.exhibit = (TextView) findViewById(R.id.exhibit);
		this.checkRecordDetail = (ListView) findViewById(R.id.checkRecordDetail);
		this.checkRecordDetailAdapter = new StockCheckRecordDetailAdapter(StockCheckRecordResultActivity.this, checkRecordDetailVos);
		this.checkRecordDetail.setAdapter(checkRecordDetailAdapter);
		this.checkRecordDetail.setOnScrollListener(new BaseListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				 if (is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (nodata) {
						currentPage++;
						getResult();
					}
				 }
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});
		
		this.initData();
	}
	/**
	 * 初始化页面值
	 */
	private void initData(){
		if (stockCheckRecordVo.getStockCheckRecordTime()!=null) {
			this.stockCheckId =stockCheckRecordVo.getStockCheckId();
			this.getResult();
        }
	}
	/**
	 * 获取详情列表
	 */
	private void getResult() {
		nodata = false;
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "checkStockRecord/detail");
		params.setParam("shopId", shopId);
		params.setParam("currentPage", currentPage);
		params.setParam("stockCheckId", stockCheckId);
		
		new AsyncHttpPost(this, params, CheckStockRecordDetailBo.class,false, new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				CheckStockRecordDetailBo bo = (CheckStockRecordDetailBo)oj;
				if (bo!=null) {
					List<StockCheckRecordDetailVo> detailList = new ArrayList<StockCheckRecordDetailVo>();
					detailList = bo.getStockCheckRecordList();
					pageSize = bo.getPageCount();
					if (detailList != null && detailList.size() > 0) {
						if (currentPage<=pageSize){
							nodata = true;
						}
						checkRecordDetailVos.addAll(detailList);
					}
					if (currentPage==1) {
						/**
						 * 设置合计
						 */
						if (checkRecordDetailVos.size()>0) {
							stockCheckRecordDetailVo = checkRecordDetailVos.get(0);
							stockTotalCount.setText(String.valueOf(stockCheckRecordDetailVo.getStockTotalCount()));
							stockRealCount.setText(String.valueOf(stockCheckRecordDetailVo.getStockRealCount()));
							exhibitCount.setText(String.valueOf(stockCheckRecordDetailVo.getExhibitCount()));
							stockMoney.setText(String.valueOf(stockCheckRecordDetailVo.getStockMoney()));
							exhibit.setText(String.valueOf(stockCheckRecordDetailVo.getExhibit()));
							checkRecordDetailVos.remove(0);
						}
					}
					checkRecordDetailAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}
}
