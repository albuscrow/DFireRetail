package com.dfire.retail.app.manage.activity.goodsmanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.SelectShopAdapter;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.bo.AllShopListIncludeCompanyBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
/**
 * 全部门店 包括分公司
 * @author ys
 * 类别二
 */
public class SelectShopActivityForTongbu extends TitleActivity implements OnItemClickListener,OnClickListener{

	private ListView selectshoplist;
	
	private int currentPage = 1;
	
	private  ArrayList<AllShopVo> allShops;
	
	private String selectShopId;
	
	private boolean nodata;
	
	private boolean is_divPage;// 是否进行分页操作
	
	private Integer pageSize = 0;
	
	private EditText input;
	
	private TextView search;
	
	private SelectShopAdapter selectShopAdapter;
	
	private String keyWord;
	
	private AllShopVo allShopVo;

	private AllShopVo noShop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_shop);
		setTitleText("选择门店");
		showBackbtn();
		this.init();
	}
	private void init(){
		selectShopId = getIntent().getExtras().getString("selectShopId");
		allShops = new ArrayList<AllShopVo>();//店铺
		//add all shop
		allShopVo = new AllShopVo();
		allShopVo.setShopId(RetailApplication.getShopVo().getShopId());
		allShopVo.setShopName("同步所有");
		allShopVo.setParentId(RetailApplication.getShopVo().getParentId());
		allShopVo.setCode("");
		allShops.add(allShopVo);
		
		//add no shop
		noShop = new AllShopVo();
		noShop.setShopId(null);
		noShop.setShopName("不同步");
		noShop.setCode(Constants.EMPTY_STRING);
		allShops.add(noShop);
		
		input = (EditText) findViewById(R.id.input);
		search = (TextView) findViewById(R.id.search);
		search.setOnClickListener(this);
		selectshoplist = (ListView) findViewById(R.id.selectshoplist);
		selectShopAdapter = new SelectShopAdapter(SelectShopActivityForTongbu.this, allShops,selectShopId);
		selectshoplist.setAdapter(selectShopAdapter);
		selectshoplist.setOnItemClickListener(SelectShopActivityForTongbu.this);
		
		selectshoplist.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				 if (is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (nodata) {
						currentPage++;
						getShop();
					}
				 }
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});
		this.getShop();
	}
	@Override
	public void onClick(View v) {
		if (v==search) {
			currentPage = 1;
			allShops.clear();
			keyWord = input.getText().toString();
			if (StringUtils.isEmpty(keyWord)||StringUtils.isEquals(keyWord, "")) {
				allShops.add(allShopVo);
				allShops.add(noShop);
			}
			getShop();
		}
	}
	/**
	 * 获取店铺列表
	 * @param isInit
	 */
	private void getShop() {
		nodata = false;
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.ALL_SHOPLIST_INCLUDECOMPANY);
		params.setParam(Constants.SHOP_ID, RetailApplication.getShopVo().getShopId());
		params.setParam("keyWord", keyWord);
		params.setParam(Constants.PAGE, currentPage);
		
		new AsyncHttpPost(this, params, AllShopListIncludeCompanyBo.class,false,new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				AllShopListIncludeCompanyBo  bo = (AllShopListIncludeCompanyBo)oj;
				if (bo!=null) {
					pageSize = bo.getPageSize();
					List<AllShopVo> allShopVos = new ArrayList<AllShopVo>();
					allShopVos = bo.getAllShopList();
					if (allShopVos != null && allShopVos.size() > 0) {
						if (currentPage<=pageSize){
							nodata = true;
						}
						allShops.addAll(allShopVos);
					}
					selectShopAdapter.notifyDataSetChanged();
				}
			}
			@Override
			public void onFail(Exception e) {
				
			}
		}).execute();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		Intent select = null;
		select = new Intent();
		select.putExtra(Constants.SHOP, allShops.get(arg2));
		setResult(RESULT_OK, select);
		finish();
		return;

	}
}
