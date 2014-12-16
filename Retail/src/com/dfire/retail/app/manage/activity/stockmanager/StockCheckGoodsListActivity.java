package com.dfire.retail.app.manage.activity.stockmanager;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
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

import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.common.item.listener.IFootItemClickListener;
import com.dfire.retail.app.common.item.listener.IHelpClickEvent;
import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.common.view.FootItem;
import com.dfire.retail.app.common.view.FootScanView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.BaseActivity;
import com.dfire.retail.app.manage.activity.CaptureActivity;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.CodeSearchCheckGoodsVo;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.data.StockCheckArea;
import com.dfire.retail.app.manage.util.DBHelper;
import com.dfire.retail.app.manage.data.StockGoodsCheckVo;
import com.dfire.retail.app.manage.data.bo.StockCheckBo;
import com.dfire.retail.app.manage.data.bo.StockCheckSearchGoodsBo;
import com.dfire.retail.app.manage.data.bo.StockCheckStartBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 项目名称：Retail  
 * 类名称：StockCheckGoodsListActivity  
 * 类描述：   库存盘点列表
 * 创建时间：2014年11月18日 下午2:54:35  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckGoodsListActivity extends TitleActivity implements OnClickListener,IHelpClickEvent,IFootItemClickListener{
    private final static int REQUEST_GOODS_INFO = 1,REQUEST_STOCK_REVIEW = 2,REQUEST_STOCK_CHECK_GOODS_ADD = 3;
    private final static short LIST_TYPE_SEARCH = 1,LIST_TYPE_CHECK = 2;
	private EditText input;
	private ListView listView;
	private TextView listTitleTxt;
	private View submitBtn;
	private View continueBtn;
	private View clearBtn;
	List<StockGoodsCheckVo> checkGoodsList = new ArrayList<StockGoodsCheckVo>();
	private int currentPage=1;
	
	private String stockCheckId;
	private String selectShopId;
	private String region;
	
    private StockCheckGoodsListAdapter adapter;
    private Integer location;
   
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_check_goods_list);
		setTitleText("库存盘点");
		showBackbtn();
		findView();
		initData();
	}
    public void findView(){
		Intent intent=getIntent();
		stockCheckId=intent.getStringExtra("stockCheckId");
		selectShopId=intent.getStringExtra("selectShopId");
		region=intent.getStringExtra("region");
		//footView=(FootScanView)findViewById(R.id.footView);
		//footView.initListener(this,this);
		listTitleTxt = (TextView)findViewById(R.id.txt_list_title);
		if(region != null) {
		    listTitleTxt.setText("盘点商品（" + region + "）");
		}
		submitBtn = findViewById(R.id.submit);
		continueBtn = findViewById(R.id.btn_continue);
		clearBtn = findViewById(R.id.clear);
		submitBtn.setOnClickListener(this);
		continueBtn.setOnClickListener(this);
		clearBtn.setOnClickListener(this);
		
		adapter = new StockCheckGoodsListAdapter(StockCheckGoodsListActivity.this, checkGoodsList);
		listView=(ListView)findViewById(R.id.store_add_goods_lv);
		new ListAddFooterItem(this, listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
		    @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
		          location = position;
                  Intent intent = new Intent(StockCheckGoodsListActivity.this,StockCheckGoodsInfoActivity.class);
                  intent.putExtra("goods", checkGoodsList.get(position));
                  intent.putExtra("pageType", StockCheckGoodsInfoActivity.SAVE_UPDATE);
                  
                  startActivityForResult(intent,REQUEST_GOODS_INFO);
                
            }
		});
	}
    private void initData() {
        List<StockGoodsCheckVo> list = getCheckGoods();
        if(list != null && !list.isEmpty()) {
            this.checkGoodsList.addAll(list);
            adapter.notifyDataSetChanged();
        } 
        
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit:
            this.submit();
            break;
		case R.id.btn_continue:
		    startActivityForResult(new Intent(this, CaptureActivity.class),REQUEST_SCAN);
            break;
		case R.id.clear:
            this.delete();
            break;
		default:
			break;
		}
	}
	
	
    private void submit() {
        if(this.checkGoodsList.size() == 0) {
            new ErrDialog(this, region + "还没有盘点商品，点击继续可以盘点商品").show();
            return;
        }
        final AlertDialog alertDialog = new AlertDialog(this);
        alertDialog.setMessage("确定要提交  " + region + "的盘点吗？");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finishSubmit();
            }
        });
        alertDialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        
    }
    protected void finishSubmit() {
        
        RequestParameter params = new RequestParameter(true);
        params.setUrl(Constants.STOCK_CHECK_SUBMIT_STORE);
        params.setParam("shopId", this.selectShopId);
        params.setParam("operateType", "do");
        try {
            params.setParam("stockGoodsCheckVoList", new JSONArray(new Gson().toJson(this.checkGoodsList)));
        } catch (JSONException e1) {
            e1.printStackTrace();  
            
        }
        new AsyncHttpPost(this, params, StockCheckBo.class, new RequestCallback(){
            @Override
            public void onSuccess(Object bo) {
                StockCheckBo stockCheckStart = (StockCheckBo)bo;
                
                //清除本地数据
                DBHelper dbHepler = new DBHelper(StockCheckGoodsListActivity.this);
                try {
                    final SQLiteDatabase db = dbHepler.getWritableDatabase();
                    db.execSQL("delete from stockcheck where stockcheckid=? and region=?",new String[]{stockCheckId,region});
                   
                } catch (SQLException e) {
                    e.printStackTrace();  
                    
                }finally {
                    dbHepler.close();
                }
                
                
                ArrayList<StockGoodsCheckVo> goods = new ArrayList<StockGoodsCheckVo>();
                //Collections.copy(goods, checkGoodsList);
                for(StockGoodsCheckVo vo : checkGoodsList) {
                    goods.add(vo);
                }
                checkGoodsList.clear();
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(StockCheckGoodsListActivity.this,StockCheckReviewActivity.class);
                intent.putExtra("goods", goods);
                intent.putExtra("region", region);
                startActivityForResult(intent, REQUEST_STOCK_REVIEW);
            }

            @Override
            public void onFail(Exception e) {
                new ErrDialog(StockCheckGoodsListActivity.this, "盘点提交失败").show();
            }}).execute();
        
        
    }
   
    private void delete() {
        final AlertDialog alertDialog = new AlertDialog(this);
        alertDialog.setMessage("确定要清除 " + region + "的盘点吗？");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finishDelete();
            }
        });
        alertDialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        }); 
        
    }
    private void finishDelete() {
        
        RequestParameter params = new RequestParameter(true);
        params.setUrl(Constants.STOCK_CHECK_CLEAR_REGION_STORE);
        params.setParam("shopId", RetailApplication.getShopVo().getShopId());
        params.setParam("region", this.region);
        new AsyncHttpPost(this, params, StockCheckBo.class, new RequestCallback(){
            @Override
            public void onSuccess(Object bo) {
                StockCheckBo stockCheckStart = (StockCheckBo)bo;
                new ErrDialog(StockCheckGoodsListActivity.this, "清除成功").show();
                
                DBHelper dbHepler = new DBHelper(StockCheckGoodsListActivity.this);
                try {
                    final SQLiteDatabase db = dbHepler.getWritableDatabase();
                    db.execSQL("delete from stockcheckarea where stockcheckid=? and region=?",new String[]{stockCheckId,region});
                } catch (SQLException e) {
                    e.printStackTrace();  
                    
                }finally {
                    dbHepler.close();
                }
                
            }

            @Override
            public void onFail(Exception e) {
                new ErrDialog(StockCheckGoodsListActivity.this, "清除失败").show();
            }}).execute();
        
        
    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
          
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_SCAN == requestCode) {
            if(RESULT_OK == resultCode) {
                String code = data.getStringExtra("deviceCode");
                /*Intent intent = new Intent(this,StockCheckGoodsSearchListActivity.class);
                intent.putExtra("code", code);
                intent.putExtra("selectShopId", this.selectShopId);
                intent.putExtra("stockCheckId", this.stockCheckId);
                startActivity(intent);*/
              //本地查询
                DBHelper dbHelper = new DBHelper(this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from stockchecksearchgoods where shopid=? and barcode = ?", new String[]{this.selectShopId,code});
                if(cursor.getCount() > 0) {
                    ArrayList<CodeSearchCheckGoodsVo> list = new ArrayList<CodeSearchCheckGoodsVo>();
                    while(!cursor.isLast()) {
                        cursor.moveToNext();
                        CodeSearchCheckGoodsVo searchGoods = new CodeSearchCheckGoodsVo();
                        searchGoods.doInit(cursor);
                        list.add(searchGoods);
                    }
                    
                    if(list.size() == 1) {
                        CodeSearchCheckGoodsVo searchGoods = list.get(0);
                        Intent intent = new Intent(this,StockCheckGoodsInfoActivity.class);
                        StockGoodsCheckVo checkGoods = new StockGoodsCheckVo();
                        checkGoods.setStockCheckId(stockCheckId);
                        checkGoods.setGoodsId(searchGoods.getGoodsId());
                        checkGoods.setGoodsName(searchGoods.getGoodsName());
                        checkGoods.setRegion(region);
                        checkGoods.setBarCode(searchGoods.getBarcode());
                        checkGoods.setCount(searchGoods.getNowStore());
                        
                        intent.putExtra("selectShopId", selectShopId);
                        intent.putExtra("goods", checkGoods);
                        intent.putExtra("pageType", StockCheckGoodsInfoActivity.SAVE_CONTINUE);
                        startActivityForResult(intent,REQUEST_STOCK_CHECK_GOODS_ADD);
                    }else if(list.size() > 1) {
                        Intent intent = new Intent(this,StockCheckGoodsSearchListActivity.class);
                        intent.putExtra("selectShopId", this.selectShopId);
                        intent.putExtra("stockCheckId", this.stockCheckId);
                        intent.putExtra("region", this.region);
                        intent.putExtra("searchGoods", list);
                        intent.putExtra("code", code);
                        startActivity(intent);
                    }
                }else {
                    //ToastUtil.showShortToast(this, "没有查询到商品");
                    Intent intent = new Intent(this,StockCheckGoodsSearchListActivity.class);
                    intent.putExtra("selectShopId", this.selectShopId);
                    intent.putExtra("stockCheckId", this.stockCheckId);
                    intent.putExtra("region", this.region);
                    intent.putExtra("code", code);
                    startActivity(intent);
                }
            }else {
                Intent intent = new Intent(this,StockCheckGoodsSearchListActivity.class);
                intent.putExtra("selectShopId", this.selectShopId);
                intent.putExtra("stockCheckId", this.stockCheckId);
                intent.putExtra("region", this.region);
                startActivity(intent);
            }
            
        }else if(REQUEST_GOODS_INFO == requestCode) {
            if(StockCheckGoodsInfoActivity.RESULT_SAVE == resultCode) {
                int size = this.checkGoodsList.size();
                if(size > 0 && this.location != null && size > this.location) {
                    StockGoodsCheckVo goods = this.checkGoodsList.get(location);
                    goods.setCheckCount(data.getIntExtra("newStore", 0));
                    goods.setGetLossNumber(data.getIntExtra("lossNumber", 0));
                    adapter.notifyDataSetChanged();
                }
            }else if(StockCheckGoodsInfoActivity.RESULT_DELETE == resultCode) {
                int size = this.checkGoodsList.size();
                if(size > 0 && this.location != null && size > this.location) {
                    this.checkGoodsList.remove(location.intValue());
                    if(this.checkGoodsList.size() == 0) {
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            
        }else if(REQUEST_STOCK_REVIEW == requestCode && Activity.RESULT_OK == resultCode ) {
            startActivityForResult(new Intent(this, CaptureActivity.class),REQUEST_SCAN);
        }
    }
    private List<StockGoodsCheckVo> getCheckGoods() {
        List<StockGoodsCheckVo> checkGoodsList = new ArrayList<StockGoodsCheckVo>();
        DBHelper dbHelper = new DBHelper(this); 
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from stockcheck where stockcheckid=? and region=?",new String[]{this.stockCheckId,this.region});
            if(cursor.getCount() > 0) {
                while(!cursor.isLast()) {
                    cursor.moveToNext();
                    StockGoodsCheckVo vo = new StockGoodsCheckVo();
                    vo.doInit(cursor);
                    checkGoodsList.add(vo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  
        } finally {
            dbHelper.close();
        }
        
        return checkGoodsList;
        
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
	
	
	private class StockCheckGoodsListAdapter extends BaseAdapter{

	    private List<StockGoodsCheckVo> checkGoodsList;
	    private LayoutInflater mLayoutInflater;
	    
	    public StockCheckGoodsListAdapter(Context context, List<StockGoodsCheckVo> checkGoodsList) {
	        super();
	        this.checkGoodsList = checkGoodsList;
	        mLayoutInflater =  LayoutInflater.from(context);
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