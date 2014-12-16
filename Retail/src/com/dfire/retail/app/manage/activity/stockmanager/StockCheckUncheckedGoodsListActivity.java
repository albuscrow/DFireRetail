package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.List;










import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.listener.IFootItemClickListener;
import com.dfire.retail.app.common.item.listener.IHelpClickEvent;
import com.dfire.retail.app.common.view.FootItem;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.BaseActivity;
import com.dfire.retail.app.manage.activity.CaptureActivity;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.CodeSearchCheckGoodsVo;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.data.StockGoodsCheckVo;
import com.dfire.retail.app.manage.data.bo.StockCheckSearchGoodsBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.util.DBHelper;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;

/**
 * 项目名称：Retail  
 * 类名称：StockCheckGoodsListActivity  
 * 类描述：   库存盘点未盘点商品查询列表
 * 创建时间：2014年11月18日 下午2:54:35  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckUncheckedGoodsListActivity extends TitleActivity implements OnClickListener,IHelpClickEvent,IFootItemClickListener{
    private final static int REQUEST_GOODS_INFO = 1,REQUEST_STOCK_REVIEW = 2;
    private final static int PAGESIZE = 25;
	private TextView search;
	private EditText input;
	//private PullToRefreshListView listView;
	private ListView  listView;
	private ImageView scan;
	private View scanBtn;
    private	SearchGoodsVo searchGoodsVo;
	private List<CodeSearchCheckGoodsVo> searchGoodsList = new ArrayList<CodeSearchCheckGoodsVo>();
	private int currentPage=1;
	private String searchCode;
	
	private String stockCheckId;
	private String selectShopId;
	
    private StockCheckGoodsListAdapter adapter;
    private String region;
    private Integer location;
    protected Integer pageSize;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_check_search_goods_list);
		setTitleText("未盘点商品");
		showBackbtn();
		findView();
		initData();
	}
   
    public void findView(){
		Intent intent=getIntent();
		stockCheckId=intent.getStringExtra("stockCheckId");
		selectShopId=intent.getStringExtra("selectShopId");
		region=intent.getStringExtra("region");
		search=(TextView)findViewById(R.id.search);
		search.setOnClickListener(this);
		input=(EditText)findViewById(R.id.input);
		scan=(ImageView)findViewById(R.id.scan);
		scan.setOnClickListener(this);
		scanBtn=findViewById(R.id.btn_scan);
		scanBtn.setOnClickListener(this);
		
		adapter = new StockCheckGoodsListAdapter(StockCheckUncheckedGoodsListActivity.this, searchGoodsList);
		//listView=(PullToRefreshListView)findViewById(R.id.store_add_goods_lv);
		listView=(ListView)findViewById(R.id.store_add_goods_lv);
		//listView.setMode(Mode.PULL_FROM_START);
		listView.setAdapter(adapter);
		/*listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getResult(searchCode,++currentPage);
            }});*/
		/*listView.setOnItemClickListener(new OnItemClickListener() {
		    @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
		          location = position;
                  Intent intent = new Intent(StockCheckUncheckedGoodsListActivity.this,StockCheckGoodsInfoActivity.class);
                  CodeSearchCheckGoodsVo searchGoods = searchGoodsList.get(position);
                  StockGoodsCheckVo checkGoods = new StockGoodsCheckVo();
                  checkGoods.setStockCheckId(stockCheckId);
                  checkGoods.setGoodsId(searchGoods.getGoodsId());
                  checkGoods.setGoodsName(searchGoods.getGoodsName());
                  checkGoods.setRegion(region);
                  checkGoods.setBarCode(searchGoods.getBarcode());
                  checkGoods.setCount(searchGoods.getNowStore());
                  checkGoods.setRetailPrice(searchGoods.getRetailPrice());
                  checkGoods.setPurchasePrice(searchGoods.getPurchasePrice());
                  
                  intent.putExtra("selectShopId", selectShopId);
                  intent.putExtra("goods", checkGoods);
                  intent.putExtra("pageType", StockCheckGoodsInfoActivity.SAVE_CONTINUE);
                  
                  startActivityForResult(intent,REQUEST_GOODS_INFO);
                
            }
		});*/
	}
    
    private void initData() {
        
        getResult("",1);
        
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
		    String keyword = input.getText().toString();
			getResult(keyword,1);
			break;
		case R.id.scan:
		    startActivityForResult(new Intent(this, CaptureActivity.class),REQUEST_SCAN);
            break;
		case R.id.btn_scan:
            startActivityForResult(new Intent(this, CaptureActivity.class),REQUEST_SCAN);
            break;
		default:
			break;
		}
	}
	
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
          
        super.onActivityResult(requestCode, resultCode, data);
        if(Activity.RESULT_OK == resultCode && BaseActivity.REQUEST_SCAN == requestCode) {
            String code = data.getStringExtra("deviceCode");
            if(code == null) {
                return;
            }
            this.input.setText(code);
            getResult(code,1);
        }
    }
    /**
	 * 扫一扫按钮.
	 * @param obj
	 */
	@Override
	public void onFootItemClick(FootItem obj) {
		// TODO Auto-generated method stub
		
	}
	/** {@inheritDoc} */
	@Override
	public void onHelpClick(Button obj) {
		// TODO Auto-generated method stub
		
	}
	
	private void getResult(String keyword,final int page) {
	    if(page == 1) {
            searchGoodsList.clear();
            this.currentPage = page;
        }
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.STOCK_CHECK_SEARCH_NUCHECKED_GOODS);
		params.setParam("shopId", this.selectShopId);
		params.setParam("currentPage", currentPage);
		if(!StringUtils.isEmpty(keyword)) {
		    params.setParam("searchCode", keyword);
		}
		new AsyncHttpPost(this, params, StockCheckSearchGoodsBo.class, new RequestCallback(){

            @Override
            public void onSuccess(Object bo) {
                  
                StockCheckSearchGoodsBo goodsBo = (StockCheckSearchGoodsBo)bo;
                List<CodeSearchCheckGoodsVo> list = goodsBo.getGoodsList();
                if(page == 1) {
                    pageSize = goodsBo.getPageSize();
                }
                
                if(list != null && list.size() > 0) {
                    searchGoodsList.addAll(list);
                }else if(page == 1){
                    ToastUtil.showShortToast(StockCheckUncheckedGoodsListActivity.this, "没有查询到未盘点商品");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Exception e) {
                
            }}).execute();
	}
	
	
	private class StockCheckGoodsListAdapter extends BaseAdapter{

	    private List<CodeSearchCheckGoodsVo> searchGoodsList;
	    private LayoutInflater mLayoutInflater;
	    
	    public StockCheckGoodsListAdapter(Context context, List<CodeSearchCheckGoodsVo> searchGoodsList) {
	        super();
	        this.searchGoodsList = searchGoodsList;
	        mLayoutInflater =  LayoutInflater.from(context);
	    }

	    @Override
	    public int getCount() {
	        return searchGoodsList.size();
	        
	    }

	    @Override
	    public Object getItem(int position) {
	        return searchGoodsList.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup arg2) {
	        ViewHolder viewHolder = null;
	        if (convertView == null) {
	            convertView = mLayoutInflater.inflate(R.layout.stock_check_goods_list_item, null);
	            viewHolder = new ViewHolder();
	            viewHolder.goodsName = (TextView) convertView.findViewById(R.id.txt_goods_name);
	            viewHolder.barcode = (TextView) convertView.findViewById(R.id.txt_barcode);
	            viewHolder.nowStore = (TextView) convertView.findViewById(R.id.txt_now_store);
	            viewHolder.realStore = (TextView) convertView.findViewById(R.id.txt_real_store);
	            viewHolder.exhibitCount = (TextView) convertView.findViewById(R.id.txt_exhibit_count);
	            
	            convertView.setTag(viewHolder);
	        } else {
	            viewHolder = (ViewHolder) convertView.getTag();
	        }
	        CodeSearchCheckGoodsVo goods = (CodeSearchCheckGoodsVo)this.getItem(position);    
            viewHolder.goodsName.setText(goods.getGoodsName());
            viewHolder.barcode.setText(goods.getBarcode());
            if(goods.getNowStore() == null) {
                viewHolder.nowStore.setText("无");
            }else {
                viewHolder.nowStore.setText(String.valueOf(goods.getNowStore()));
            }
            viewHolder.realStore.setText("");
            viewHolder.exhibitCount.setText("");
	                    
	        return convertView;
	    }
	    
	     private class ViewHolder {
	            TextView goodsName;
	            TextView barcode;
	            TextView nowStore;
	            TextView realStore;
	            TextView exhibitCount;
	        }

	}
	

}