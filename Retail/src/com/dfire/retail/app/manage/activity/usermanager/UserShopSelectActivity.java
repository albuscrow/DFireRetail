package com.dfire.retail.app.manage.activity.usermanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.ShopInfoAdapter;
import com.dfire.retail.app.manage.adapter.ShopInfoItem;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.bo.ShopListReturnBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;
/**
 * 员工选择门店 第二类  包括门店类型=1（公司）或门店类型=2（门店）的所有门店  包括自己
 *
 */
public class UserShopSelectActivity extends TitleActivity implements OnClickListener{
	
	static final String TAG="ShopInfo";
	ArrayList<ShopInfoItem> shopInfoList = new ArrayList<ShopInfoItem>();
	private ShopInfoAdapter mshopAdapter;
	private PullToRefreshListView mListView;
    private TextView	searchButton;
    private EditText editKeyWord;
    private Integer currentPage = 1;
    private Integer pageSize;
    private String shopId;
    private String shopName;
    private String shopCode;
    private String parentId;
    private String keyWord;
    private int mode;// 判断异步执行完是否禁用加载更多
    private ImageView clear_input;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_shop_ext);
		setTitleRes(R.string.selectShop);
		showBackbtn();
		initView();
		mListView.setMode(Mode.PULL_FROM_START);
		startSerachData();
		mListView.setRefreshing();
	}
	/**
	 * init
	 */
	private void initView(){
		shopId = RetailApplication.getShopVo().getShopId();
		shopCode = RetailApplication.getShopVo().getCode();
		shopName= RetailApplication.getShopVo().getShopName();
		parentId = RetailApplication.getShopVo().getParentId();
		mListView = (PullToRefreshListView) findViewById(R.id.selectshoplist);
		editKeyWord = (EditText) findViewById(R.id.input);
		searchButton = (TextView) findViewById(R.id.search);
		clear_input = (ImageView) findViewById(R.id.clear_input);
		clear_input.setOnClickListener(this);
		mshopAdapter = new ShopInfoAdapter(UserShopSelectActivity.this, shopInfoList);		
		mListView.setAdapter(mshopAdapter);		
	    mListView.setMode(Mode.BOTH);
	    new ListAddFooterItem(this, mListView.getRefreshableView());
	    mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(UserShopSelectActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = 1;
				startSerachData();
			}
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(UserShopSelectActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				startSerachData();
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra(Constants.SHOP_ID, shopInfoList.get(arg2-1).getShopId());
				intent.putExtra(Constants.SHOPCOPNAME,shopInfoList.get(arg2-1).getShopName());
				intent.putExtra(Constants.SHOP, shopInfoList.get(arg2-1));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		searchButton.setOnClickListener(this);
		
		editKeyWord.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s!=null&&!s.toString().equals("")) {
					clear_input.setVisibility(View.VISIBLE);
				}else{
					clear_input.setVisibility(View.GONE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	/**
	 * 查询所有店家
	 * @param pagesize
	 */
	private void startSerachData() {
        RequestParameter parameters = new RequestParameter(true);
        parameters.setUrl(Constants.COMPANY_ALL_SHOP_LIST_URL);
        parameters.setParam(Constants.SHOP_ID, shopId);       
        parameters.setParam(Constants.PAGE,currentPage);
        parameters.setParam(Constants.SHOPKEYWORD, keyWord);
        new AsyncHttpPost(UserShopSelectActivity.this,parameters,ShopListReturnBo.class,false,new RequestCallback() {
            @Override
            public void onSuccess(Object str) {
            	ShopListReturnBo bo = (ShopListReturnBo)str;
            	if (bo!=null) {
            		pageSize = bo.getPageSize();
					List<AllShopVo> allShopVos = new ArrayList<AllShopVo>();
					allShopVos = bo.getAllShopList();
					if (pageSize!=null&&pageSize!=0) {
						if (currentPage == 1) {
							shopInfoList.clear();
							if (StringUtils.isEmpty(keyWord)||StringUtils.isEquals(keyWord, "")) {
								shopInfoList.add(new ShopInfoItem(shopId, shopName, parentId, shopCode));
							}
						}
						if (allShopVos != null && allShopVos.size() > 0) {
			        		for(int i =0 ; i < allShopVos.size();i++){
			        			shopInfoList.add(new ShopInfoItem(allShopVos.get(i).getShopId(),allShopVos.get(i).getShopName(),allShopVos.get(i).getParentId(),UserShopSelectActivity.this.getResources().getString(R.string.shop_code)+allShopVos.get(i).getCode()));
			        			mshopAdapter.notifyDataSetChanged();
			        		}
							mListView.setMode(Mode.BOTH);
						}else {
							mode = 1;
						}
					}else {
						shopInfoList.clear();
						mshopAdapter.notifyDataSetChanged();
						mode = 1;
					}
					mListView.onRefreshComplete();
					if (mode == 1) {
						mListView.setMode(Mode.PULL_FROM_START);
					}
					mode = -1;
				}
            }
            @Override
            public void onFail(Exception e) {
            	mListView.onRefreshComplete(); 
            }
		}).execute();
    }
	/**
	 * 搜索数据
	 */
	@Override
	public void onClick(View v) {
		if (v==searchButton) {
			currentPage = 1;
			keyWord = editKeyWord.getText().toString();
			mListView.setMode(Mode.PULL_FROM_START);
			startSerachData();
			mListView.setRefreshing();
		}else if (v==clear_input) {
			editKeyWord.setText("");
			clear_input.setVisibility(View.GONE);
		}	
	}
}
