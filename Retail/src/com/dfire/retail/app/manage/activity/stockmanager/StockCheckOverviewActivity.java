package com.dfire.retail.app.manage.activity.stockmanager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.common.view.StickyListHeadersListView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.BillStatusDialog;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.data.StockCheckArea;
import com.dfire.retail.app.manage.data.StockCheckSummaryVo;
import com.dfire.retail.app.manage.data.StockGoodsCheckVo;
import com.dfire.retail.app.manage.data.bo.StockCheckBo;
import com.dfire.retail.app.manage.data.bo.StockCheckRegionGoodsBo;
import com.dfire.retail.app.manage.data.bo.StockCheckSummaryBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.DBHelper;
import com.handmark.pulltorefresh.listview.StickyListHeadersAdapter;

/**
 * 项目名称：Retail  
 * 类名称：StockCheckOverviewActivity  
 * 类描述：   库存盘点一览
 * 创建时间：2014年11月17日 下午2:39:45  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckOverviewActivity extends TitleActivity implements OnClickListener{
    private final static int REQUEST_GOODS_STOCK_CHECK = 1;
    public final static int RESULT_FINISH_STOCK_CHECK = 1;
	private TextView shopNameTxt,regionTxt,nowStoreTxt,realStoreTxt;
	/**盈亏库存*/
	private TextView exhibitCountTxt;
	/**库存金额*/
    private TextView checkCountPriceTxt;
    /**盈亏金额*/
    private TextView resultPriceTxt;
    private View printBtn,exportBtn,finishBtn;
    private View totalLayout;
    private StickyListHeadersListView listView;
	ProgressDialog progressDialog;
	List<StockGoodsCheckVo> checkGoodsList = new ArrayList<StockGoodsCheckVo>();
	private List<DicVo> dicVos = new ArrayList<DicVo>();
	private int currentPage=1;
	private String stockCheckId;
	private String selectShopId;
	private String selectShopName;
	
	int add=1;
    private BillStatusDialog billStatusDialog;
    private SharedPreferences sp;
    private int selectedRegionId;
    private StockCheckGoodsListAdapter adapter;
    private Integer location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_check_overview);
		setTitleText("库存盘点");
		showBackbtn();
		setRightBtn(R.drawable.finish_btn);
		findView();
		initData();
	}
	
    public void findView(){
		shopNameTxt = (TextView)findViewById(R.id.txt_shop_name);
		this.regionTxt = (TextView)findViewById(R.id.txt_region);
		regionTxt.setOnClickListener(this);
		this.nowStoreTxt = (TextView)findViewById(R.id.txt_now_store);
		this.realStoreTxt = (TextView)findViewById(R.id.txt_real_store);
		this.exhibitCountTxt = (TextView)findViewById(R.id.txt_exhibit_count);
		this.checkCountPriceTxt = (TextView)findViewById(R.id.txt_check_price);
		this.resultPriceTxt = (TextView)findViewById(R.id.txt_result_price);
		this.totalLayout = findViewById(R.id.ll_total);
		this.printBtn = findViewById(R.id.print);
		this.exportBtn = findViewById(R.id.export);
		printBtn.setOnClickListener(this);
		exportBtn.setOnClickListener(this);
		this.mRight.setOnClickListener(this);
		
		adapter = new StockCheckGoodsListAdapter(this.checkGoodsList,this.getLayoutInflater());
		listView=(StickyListHeadersListView)findViewById(R.id.lv_stock_check_overview);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
		    @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
		          location = position;
                  Intent intent = new Intent(StockCheckOverviewActivity.this,StockCheckGoodsInfoDetailActivity.class);
                  intent.putExtra("goods", checkGoodsList.get(position));
                  intent.putExtra("selectShopId", selectShopId);
                  intent.putExtra("stockCheckId", stockCheckId);
                  startActivityForResult(intent,REQUEST_GOODS_STOCK_CHECK);
                
            }
		});
	}
    private void initData() {
        Intent intent=getIntent();
        stockCheckId=intent.getStringExtra("stockCheckId");
        selectShopId = intent.getStringExtra("selectShopId");
        selectShopName = intent.getStringExtra("selectShopName");
        String opUserId = intent.getStringExtra("opUserId");
        String userId = RetailApplication.getmUserInfo().getUserId();
        if(!userId.equals(opUserId)) {
            this.finishBtn.setVisibility(View.GONE);
        }
        //区域列表
       /* DicVo vo = new DicVo();
        vo.setName("全部");
        vo.setVal(0);
        dicVos.add(vo);
        sp = this.getSharedPreferences("stockcheckregion", Activity.MODE_PRIVATE);
        String regionStr = sp.getString("region", "");
        if(!StringUtils.isEmpty(regionStr)) {
            String[] regions = regionStr.split(",");
            int index = 1;
            for(String region : regions) {
                DicVo dv = new DicVo();
                dv.setName(region);
                dv.setVal(index++);
                dicVos.add(dv);
            }
        }*/
        this.billStatusDialog = new BillStatusDialog(this,dicVos);//盘点区域
        //this.billStatusDialog.getConfirmButton().setOnClickListener(this);
        //this.billStatusDialog.getCancelButton().setOnClickListener(this);
        
        shopNameTxt.setText(selectShopName);
        
        this.initStockCheckSummary();
    }

    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_region:
		    this.showRegionDialog();
			break;
		case R.id.print:
		    this.print();
            break;
		case R.id.export:
            this.export();
            break;
		case R.id.title_right:
            this.finishStockCheckConfirm();
            break;
		case R.id.card_type_confirm:
		    billStatusDialog.dismiss();
            Integer index = billStatusDialog.getCurrentData();
            if(selectedRegionId == index.intValue()) {
                return;
            }
            selectedRegionId = index;
            DicVo dicVo = dicVos.get(index);
            regionTxt.setText(dicVo.getName());
            if(selectedRegionId == 0) {
                this.initStockCheckSummary();
            }else {
                reloadRegionGoodsCheck(dicVo.getName());
            }
            
		    break;
		case R.id.card_type_cancel:
		    billStatusDialog.dismiss();
		    break;
		default:
			break;
		}
	}
	
    private void showRegionDialog() {
        if(dicVos.size() == 0) {
            DicVo vo = new DicVo();
            vo.setName("全部");
            vo.setVal(0);
            dicVos.add(vo);
            
            DBHelper dbHepler = new DBHelper(StockCheckOverviewActivity.this);
            try {
                final SQLiteDatabase db = dbHepler.getWritableDatabase();
                Cursor cursor = db.rawQuery("select * from stockcheckarea where shopid=?", new String[]{this.selectShopId});
                if(cursor.getCount() > 0) {
                    int index = 1;
                    while(!cursor.isLast()) {
                        cursor.moveToNext();
                        StockCheckArea area = new StockCheckArea();
                        area.doInit(cursor);
                        DicVo dv = new DicVo();
                        dv.setName(area.getRegion());
                        dv.setVal(index++);
                        dicVos.add(dv);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();  
                
            }finally {
                dbHepler.close();
            } 
        }
        this.billStatusDialog.show();
        this.billStatusDialog.getmTitle().setText("盘点区域");
        this.billStatusDialog.updateType(selectedRegionId);
        this.billStatusDialog.getConfirmButton().setOnClickListener(this);
        this.billStatusDialog.getCancelButton().setOnClickListener(this);
    }

    private void finishStockCheckConfirm() {
        final AlertDialog alertDialog = new AlertDialog(this);
        alertDialog.setMessage("确定要完成盘点吗？");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finishStockCheck();
            }
        });
        alertDialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
	private void finishStockCheck() {
          
	    RequestParameter params = new RequestParameter(true);
        params.setUrl(Constants.STOCK_CHECK_FINISH);
        params.setParam(Constants.SHOP_ID,this.selectShopId);
        params.setParam("stockCheckId",this.stockCheckId);
        new AsyncHttpPost(this, params, StockCheckBo.class, new RequestCallback(){
            @Override
            public void onSuccess(Object bo) {
                StockCheckBo stockCheckStart = (StockCheckBo)bo;
                //删除盘点商品
                DBHelper dbHepler = new DBHelper(StockCheckOverviewActivity.this);
                try {
                    final SQLiteDatabase db = dbHepler.getWritableDatabase();
                    db.execSQL("delete from stockcheck where stockcheckid=? ",new String[]{stockCheckId});
                    db.execSQL("delete from stockchecksearchgoods where shopid=? ",new String[]{selectShopId});
                } catch (SQLException e) {
                    e.printStackTrace();  
                    
                }finally {
                    dbHepler.close();
                }
                setResult(RESULT_FINISH_STOCK_CHECK);
                finish();
            }

            @Override
            public void onFail(Exception e) {
                new ErrDialog(StockCheckOverviewActivity.this, "盘点结束失败").show();
            }}).execute();
        
    }

    private void export() {
          
        // TODO Auto-generated method stub  
        
    }

    private void print() {
          
        // TODO Auto-generated method stub  
        
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
          
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_GOODS_STOCK_CHECK == requestCode) {
           if(StockCheckGoodsInfoDetailActivity.RESULT_SAVE == resultCode) {
               if(this.selectedRegionId == 0) {
                   this.initStockCheckSummary();
               }else {
                   this.reloadRegionGoodsCheck(regionTxt.getText().toString());
               }
               int size = this.checkGoodsList.size();
               if(this.location != null && size > this.location) {
                   /*StockGoodsCheckVo vo = this.checkGoodsList.get(location);
                   int newStore = data.getIntExtra("newStore", 0);
                   int exhibitCount = data.getIntExtra("exhibitCount", 0);
                   vo.setCheckCount(newStore);
                   vo.setGetLossNumber(exhibitCount);
                   adapter.notifyDataSetChanged();*/
                   this.listView.setSelection(location.intValue());
               }
           }else if(StockCheckGoodsInfoDetailActivity.RESULT_DELETE == resultCode) {
               /*int size = this.checkGoodsList.size();
               if(this.location != null && size > this.location) {
                   this.checkGoodsList.remove(location.intValue());
                   adapter.notifyDataSetChanged();
               }*/
               if(this.selectedRegionId == 0) {
                   this.initStockCheckSummary();
                   
               }else {
                   this.reloadRegionGoodsCheck(regionTxt.getText().toString());
               }
           }
        }
    }
	
    private void initStockCheckSummary() {
        
        RequestParameter params = new RequestParameter(true);
        params.setUrl(Constants.STOCK_CHECK_SUMMARY);
        params.setParam("shopId", this.selectShopId);
        params.setParam("stockCheckId", this.stockCheckId);
        new AsyncHttpPost(this, params, StockCheckSummaryBo.class, new RequestCallback(){

            @Override
            public void onSuccess(Object bo) {
                StockCheckSummaryBo summaryBo = (StockCheckSummaryBo)bo;
                StockCheckSummaryVo summary = summaryBo.getStockCheckSummaryVo();
                if(summary != null) {
                    nowStoreTxt.setText(String.valueOf(summary.getTotalCount()));
                    realStoreTxt.setText(String.valueOf(summary.getTotalCheckCount()));
                    exhibitCountTxt.setText(String.valueOf(summary.getTotalResultCount()));
                    if(summary.getTotalRetailPrice() != null) {
                        checkCountPriceTxt.setText(summary.getTotalRetailPrice().toString());
                    }
                    if(summary.getTotalResultPrice() != null) {
                        resultPriceTxt.setText(summary.getTotalResultPrice().toString());
                    }
                }
                
                checkGoodsList.clear();
                List<StockGoodsCheckVo> vos = summary.getStockGoodsCheckVoList();
                if(vos != null && !vos.isEmpty()) {
                    checkGoodsList.addAll(vos);
                }
                totalLayout.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Exception e) {
                
            }}).execute();
    }
	private void reloadRegionGoodsCheck(String region) {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.STOCK_CHECK_GET_REGION_GOODS);
		params.setParam("shopId", selectShopId);
		params.setParam("region", region);
		 new AsyncHttpPost(this, params, StockCheckRegionGoodsBo.class, new RequestCallback(){

	            @Override
	            public void onSuccess(Object bo) {
	                StockCheckRegionGoodsBo summaryBo = (StockCheckRegionGoodsBo)bo;
	                checkGoodsList.clear();
	                List<StockGoodsCheckVo> vos = summaryBo.getStockGoodsCheckVoList();
	                if(vos != null && !vos.isEmpty()) {
	                    checkGoodsList.addAll(vos);
	                }
	                totalLayout.setVisibility(View.GONE);
	                adapter.notifyDataSetChanged();
	            }

	            @Override
	            public void onFail(Exception e) {
	                
	            }}).execute();
	}
	
	public static List<SearchGoodsVo> getJson(String json) {

		List<SearchGoodsVo> list = new ArrayList<SearchGoodsVo>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj != null && jsonObj.length() > 0) {
				JSONArray jsonArray = jsonObj.getJSONArray("goodsList");
					for (int i = 0; i < jsonArray.length(); i++) {
						SearchGoodsVo entity = new SearchGoodsVo();
						JSONObject js = jsonArray.getJSONObject(i);
						entity.setGoodsId(js.getString("goodsId"));
						entity.setGoodsName(js.getString("goodsName"));
						entity.setNowstore(js.getInt("nowstore"));
						entity.setPeriod(js.getInt("period"));
						entity.setBarcode(js.getString("barcode"));
						entity.setFileName(js.getString("fileName"));
						entity.setPurchasePrice(new BigDecimal(String.valueOf(js.getDouble("purchasePrice"))));
						list.add(entity);
					}
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	private class StockCheckGoodsListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

	    private List<StockGoodsCheckVo> checkGoodsList;
	    private LayoutInflater inflater;
	    public StockCheckGoodsListAdapter(List<StockGoodsCheckVo> checkGoodsList,LayoutInflater inflater) {
	        this.checkGoodsList = checkGoodsList;
	        this.inflater = inflater;
	    }
        @Override
        public int getCount() {
            return checkGoodsList.size();
        }

        @Override
        public Object getItem(int position) {
            return checkGoodsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position,
                            View convertView,
                            ViewGroup parent) {
              
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.stock_check_overview_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.goodsName = (TextView) convertView.findViewById(R.id.txt_goods_name);
                viewHolder.barcode = (TextView) convertView.findViewById(R.id.txt_barcode);
                viewHolder.nowStore = (TextView) convertView.findViewById(R.id.txt_now_store);
                viewHolder.realStore = (TextView) convertView.findViewById(R.id.txt_real_store);
                viewHolder.exhibitCount = (TextView) convertView.findViewById(R.id.txt_exhibit_count);
                viewHolder.retailPrice = (TextView) convertView.findViewById(R.id.txt_retail_price);
                viewHolder.checkPrice = (TextView) convertView.findViewById(R.id.txt_check_price);
                viewHolder.resultPrice = (TextView) convertView.findViewById(R.id.txt_result_price);
                
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            StockGoodsCheckVo goods = (StockGoodsCheckVo)this.getItem(position);
            viewHolder.goodsName.setText(goods.getGoodsName());
            viewHolder.barcode.setText(goods.getBarCode());
            if(goods.getCount() == null) {
                viewHolder.nowStore.setText("无");
            }else {
                viewHolder.nowStore.setText(String.valueOf(goods.getCount()));
            }
            
            viewHolder.realStore.setText(String.valueOf(goods.getCheckCount()));
            viewHolder.exhibitCount.setText(String.valueOf(goods.getGetLossNumber()));
            viewHolder.retailPrice.setText(String.valueOf(goods.getRetailPrice()));
            if(goods.getCheckCountPrice() != null) {
                viewHolder.checkPrice.setText(String.valueOf(goods.getCheckCountPrice()));
            }
            if(goods.getResultPrice() != null) {
                viewHolder.resultPrice.setText(String.valueOf(goods.getResultPrice()));
            }
            
            return convertView;
        }

        @Override
        public View getHeaderView(int position,
                                  View convertView,
                                  ViewGroup parent) {
              
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.stock_check_overview_header_item, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.txt_list_title);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            return 'A';
        }
        private class ViewHolder {
            TextView goodsName;
            TextView barcode;
            TextView nowStore;
            TextView realStore;
            TextView exhibitCount;
            TextView retailPrice;
            TextView checkPrice;
            TextView resultPrice;
        }
        private class HeaderViewHolder {
            TextView text;
        }
	    
	}

}