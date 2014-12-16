package com.dfire.retail.app.manage.activity.stockmanager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;
import android.widget.Button;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.CaptureActivity;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.CodeSearchCheckGoodsVo;
import com.dfire.retail.app.manage.data.StockGoodsCheckVo;
import com.dfire.retail.app.manage.data.bo.StockCheckSearchGoodsBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.DBHelper;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
/**
 * 项目名称：Retail  
 * 类名称：StockCheckGoodsInfo  
 * 类描述：   商品盘点详情页，扫码或者查询列表点击进入的页面 
 * 创建时间：2014年11月15日 下午6:29:16  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckGoodsInfoActivity extends TitleActivity implements View.OnClickListener {

    public final static short SAVE_CONTINUE = 1,SAVE_UPDATE = 2;
    public final static int RESULT_SAVE = 1,RESULT_DELETE = 2;
    private final static int REQUEST_SEARCH = 9;
    private TextView goodsNameTxt;
    private TextView barcodeTxt;
    private TextView nowStoreTxt;
    private ItemEditText realStoreTxt;
    private View cancelBtn;
    private View saveBtn;
    private Button saveAndContinueBtn;
    private Button deleteBtn;
    
    private StockGoodsCheckVo goods;
    private ProgressDialog progressDialog;
    private boolean hasSaved;
    private String selectShopId;
    private short pageType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_check_goods_info);
        setTitleText("商品盘点");
        initView();
        initData();
    }
    private void initView() {
        goodsNameTxt = (TextView)this.findViewById(R.id.goods_name);
        barcodeTxt = (TextView)this.findViewById(R.id.barCode);
        nowStoreTxt = (TextView)this.findViewById(R.id.nowStore);
        realStoreTxt = (ItemEditText)this.findViewById(R.id.realStore);
        realStoreTxt.setMaxLength(7);
        realStoreTxt.initLabel("实际数量", "实际库存", true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
        realStoreTxt.setIsChangeListener(super.getItemChangeListener());
        //cancelBtn =this.findViewById(R.id.cancel);
        //saveBtn = this.findViewById(R.id.save);
        saveAndContinueBtn = (Button)this.findViewById(R.id.saveAndContinue);
        deleteBtn = (Button)this.findViewById(R.id.delete);
        //cancelBtn.setOnClickListener(this);
        //saveBtn.setOnClickListener(this);
        saveAndContinueBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        
        super.showBackbtn();
        this.mRight.setOnClickListener(this);
        this.mLeft.setOnClickListener(this);
        this.mBack.setOnClickListener(this);
        
    }
    private void initData() {
        Intent intent = this.getIntent();
        pageType = intent.getShortExtra("pageType", (short)1);
        selectShopId = intent.getStringExtra("selectShopId");
        goods = (StockGoodsCheckVo)intent.getSerializableExtra("goods");
        
        if(SAVE_UPDATE == pageType) {
            this.saveAndContinueBtn.setVisibility(View.GONE);
            this.deleteBtn.setVisibility(View.VISIBLE);
        }
        if(goods != null) {
            this.goodsNameTxt.setText(goods.getGoodsName());
            this.barcodeTxt.setText(goods.getBarCode());
            if(goods.getCount() != null) {
                this.nowStoreTxt.setText(String.valueOf(goods.getCount()));
            }else {
                this.nowStoreTxt.setText("无");
            }
            if(goods.getCheckCount() != null) {
                this.realStoreTxt.initData(String.valueOf(goods.getCheckCount()));
            }
        }
    }
    @Override
    public void onClick(View v) {
          
        switch(v.getId()) {
        case R.id.title_back:
        case R.id.title_left:
            if(SAVE_CONTINUE == this.pageType) {
                Intent intent = new Intent(this,StockCheckGoodsListActivity.class);
                intent.putExtra("stockCheckId", goods.getStockCheckId());
                intent.putExtra("selectShopId", this.selectShopId);
                intent.putExtra("region", goods.getRegion());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }else {
                finish();
            }
            break;
        case R.id.save:
            break;
        case R.id.saveAndContinue:
            this.saveAndContinue();
            break;
        case R.id.delete:
            this.delete();
            break;
        case R.id.title_right:
            this.save();
            break;
        default:
            break;
        }
        
    }
    private void saveAndContinue() {
        if(this.goods != null && !hasSaved) {
            //保存并继续盘点
            if(StringUtils.isEmpty(realStoreTxt.getStrVal())) {
                ToastUtil.showShortToast(this, "请输入实际数量");
                return;
            }
            goods.setCheckCount(Integer.parseInt(realStoreTxt.getStrVal()));
            if(goods.getCount() == null){
                goods.setCount(0);
            }
            goods.setGetLossNumber(goods.getCheckCount() - goods.getCount());
            if(goods.getRetailPrice() == null) {
                goods.setRetailPrice(BigDecimal.valueOf(0));
            }
            goods.setCheckCountPrice(goods.getRetailPrice().multiply(BigDecimal.valueOf(goods.getCheckCount())));
            goods.setResultPrice(goods.getRetailPrice().multiply(BigDecimal.valueOf(goods.getGetLossNumber())));
            
            DBHelper dbHelper = new DBHelper(this);
            try {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select * from stockcheck where goodsid=? and stockcheckid=? and region=?", new String[] { goods.getGoodsId(),goods.getStockCheckId(),goods.getRegion() });
                if(cursor.getCount() > 0) {
                    db.execSQL("update stockcheck set checkcount=?,checkcountprice=?,resultprice=?,lossnumber=? where goodsid=? and stockcheckid=? and region=?", new String[]{realStoreTxt.getStrVal(),String.valueOf(goods.getCheckCountPrice()),String.valueOf(goods.getResultPrice()),String.valueOf(goods.getGetLossNumber()),goods.getGoodsId(),goods.getStockCheckId(),goods.getRegion()});
                }else {
                    db.insert("stockcheck", null, goods.getContentValues());
                }
                hasSaved = true;
                this.realStoreTxt.clearChange();
                this.showBackbtn();
            } catch (SQLException e) {
                e.printStackTrace();  
            }finally{
                dbHelper.close();
            }
        }
        
        startActivityForResult(new Intent(this, CaptureActivity.class),REQUEST_SCAN);
        
        
    }
    private void save() {
        if(pageType == SAVE_UPDATE) {
            if(StringUtils.isEmpty(realStoreTxt.getStrVal())) {
                ToastUtil.showShortToast(this, "请输入实际数量");
                return;
            }
            if(realStoreTxt.getOldVal().equals(realStoreTxt.getStrVal())) {
                finish();
                return;
            }
            goods.setCheckCount(Integer.parseInt(realStoreTxt.getStrVal()));
            if(goods.getCount() == null){
                goods.setCount(0);
            }
            goods.setGetLossNumber(goods.getCheckCount() - goods.getCount());
            if(goods.getRetailPrice() == null) {
                goods.setRetailPrice(BigDecimal.valueOf(0));
            }
            goods.setCheckCountPrice(goods.getRetailPrice().multiply(BigDecimal.valueOf(goods.getCheckCount())));
            goods.setResultPrice(goods.getRetailPrice().multiply(BigDecimal.valueOf(goods.getGetLossNumber())));
            //更新数据库
            DBHelper dbHelper = new DBHelper(this);
            try {
                dbHelper.getWritableDatabase().execSQL("update stockcheck set checkcount=?,checkcountprice=?,resultprice=?,lossnumber=? where goodsid=? and stockcheckid=? and goods.region=?", new String[]{realStoreTxt.getStrVal(),String.valueOf(goods.getCheckCountPrice()),String.valueOf(goods.getResultPrice()),String.valueOf(goods.getGetLossNumber()),goods.getGoodsId(),goods.getStockCheckId(),goods.getRegion()});
            } catch (SQLException e) {
                e.printStackTrace();  
                
            }finally {
                dbHelper.close();
            }
            
            Intent data = new Intent();
            data.putExtra("newStore", goods.getCheckCount());
            data.putExtra("lossNumber", goods.getGetLossNumber());
            data.putExtra("checkCountPrice", goods.getCheckCountPrice());
            data.putExtra("resultPrice", goods.getResultPrice());
            setResult(RESULT_SAVE, data);
            finish();
        }else if(pageType == SAVE_CONTINUE) {
            if(StringUtils.isEmpty(realStoreTxt.getStrVal())) {
                ToastUtil.showShortToast(this, "请输入实际数量");
                return;
            }
            if(StringUtils.isEmpty(goods.getGoodsId())) {
                return;
            }
            goods.setCheckCount(Integer.parseInt(realStoreTxt.getStrVal()));
            if(goods.getCount() == null){
                goods.setCount(0);
            }
            goods.setGetLossNumber(goods.getCheckCount() - goods.getCount());
            if(goods.getRetailPrice() == null) {
                goods.setRetailPrice(BigDecimal.valueOf(0));
            }
            goods.setCheckCountPrice(goods.getRetailPrice().multiply(BigDecimal.valueOf(goods.getCheckCount())));
            goods.setResultPrice(goods.getRetailPrice().multiply(BigDecimal.valueOf(goods.getGetLossNumber())));
          //更新数据库
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                Cursor cursor = db.rawQuery("select * from stockcheck where goodsid=? and stockcheckid=? and region=?", new String[] { goods.getGoodsId(),goods.getStockCheckId(),goods.getRegion() });
                if(cursor.getCount() > 0) {
                    db.execSQL("update stockcheck set checkcount=?,checkcountprice=?,resultprice=?,lossnumber=? where goodsid=? and stockcheckid=? and region=?", new String[]{realStoreTxt.getStrVal(),String.valueOf(goods.getCheckCountPrice()),String.valueOf(goods.getResultPrice()),String.valueOf(goods.getGetLossNumber()),goods.getGoodsId(),goods.getStockCheckId(),goods.getRegion()});
                }else {
                    db.insert("stockcheck", null, goods.getContentValues());
                }
            } catch (SQLException e) {
                e.printStackTrace();  
                
            }finally {
                dbHelper.close();
            }
            
            Intent intent = new Intent(this,StockCheckGoodsListActivity.class);
            intent.putExtra("stockCheckId", goods.getStockCheckId());
            intent.putExtra("selectShopId", this.selectShopId);
            intent.putExtra("region", goods.getRegion());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
    private void delete() {
        final AlertDialog alertDialog = new AlertDialog(this);
        alertDialog.setMessage("确定删除吗？");
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
    protected void finishDelete() {
        //更新数据库
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.getWritableDatabase().execSQL("delete from stockcheck where goodsid=? and stockcheckid=? and region=?", new String[]{goods.getGoodsId(),goods.getStockCheckId(),goods.getRegion()});
        dbHelper.close();
        Intent data = new Intent();
        data.putExtra("goodsId", goods.getGoodsId());
        setResult(RESULT_DELETE, data);
        finish();
        
    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
          
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SCAN && resultCode == Activity.RESULT_OK) {
            this.goodsNameTxt.setText("");
            this.barcodeTxt.setText("");
            this.nowStoreTxt.setText("");
            //this.realStoreTxt.initLabel("实际数量", "请输入", true, InputType.TYPE_CLASS_NUMBER);
            this.realStoreTxt.initData("");
            this.showBackbtn();
            String code = data.getStringExtra("deviceCode");
            if(code != null && !"".equals(code)) {
                //查询商品
                searchGoods(code);
            }else {
                ToastUtil.showShortToast(StockCheckGoodsInfoActivity.this, "读取条码失败！");
            }
        }else if(requestCode == REQUEST_SCAN) {
            Intent intent = new Intent(this,StockCheckGoodsSearchListActivity.class);
            intent.putExtra("stockCheckId", goods.getStockCheckId());
            intent.putExtra("selectShopId", this.selectShopId);
            intent.putExtra("region", goods.getRegion());
            startActivityForResult(intent, REQUEST_SEARCH);
        }else if(requestCode == REQUEST_SEARCH) {
            Intent intent = new Intent(this,StockCheckGoodsListActivity.class);
            intent.putExtra("stockCheckId", goods.getStockCheckId());
            intent.putExtra("selectShopId", this.selectShopId);
            intent.putExtra("region", goods.getRegion());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
   
    private void searchGoods(String keyword) {
      //本地查询
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from stockchecksearchgoods where shopid=? and barcode = ?", new String[]{this.selectShopId,keyword});
        if(cursor.getCount() > 0) {
            ArrayList<CodeSearchCheckGoodsVo> list = new ArrayList<CodeSearchCheckGoodsVo>();
            while(!cursor.isLast()) {
                cursor.moveToNext();
                CodeSearchCheckGoodsVo searchGoods = new CodeSearchCheckGoodsVo();
                searchGoods.doInit(cursor);
                list.add(searchGoods);
            }
            //扫码只有一个商品的时候直接显示，一码多品的情况下，转到搜索页面，展示多商品
            if(list.size() == 1) {
                CodeSearchCheckGoodsVo searchGoods = list.get(0);
                String stockCheckId = goods.getStockCheckId();
                String region = goods.getRegion();
                goods = new StockGoodsCheckVo();
                goods.setStockCheckId(stockCheckId);
                goods.setGoodsId(searchGoods.getGoodsId());
                goods.setGoodsName(searchGoods.getGoodsName());
                goods.setRegion(region);
                goods.setBarCode(searchGoods.getBarcode());
                goods.setCount(searchGoods.getNowStore());
                
                this.goodsNameTxt.setText(goods.getGoodsName());
                this.barcodeTxt.setText(goods.getBarCode());
                if(goods.getCount() != null) {
                    this.nowStoreTxt.setText(String.valueOf(goods.getCount()));
                }else {
                    this.nowStoreTxt.setText("无");
                }
                if(goods.getCheckCount() != null) {
                    this.realStoreTxt.initData(String.valueOf(goods.getCheckCount()));
                }else {
                    realStoreTxt.initData("");
                }
                
                hasSaved = false;
            }else if(list.size() > 1) {
                Intent intent = new Intent(this,StockCheckGoodsSearchListActivity.class);
                intent.putExtra("selectShopId", this.selectShopId);
                intent.putExtra("stockCheckId", goods.getStockCheckId());
                intent.putExtra("region", goods.getRegion());
                intent.putExtra("searchGoods", list);
                intent.putExtra("code", keyword);
                startActivity(intent);
                finish();
            }
            
        }else {
            //ToastUtil.showShortToast(this, "没有查询到商品");
            Intent intent = new Intent(this,StockCheckGoodsSearchListActivity.class);
            intent.putExtra("stockCheckId", goods.getStockCheckId());
            intent.putExtra("selectShopId", this.selectShopId);
            intent.putExtra("region", goods.getRegion());
            intent.putExtra("code", keyword);
            startActivityForResult(intent, REQUEST_SEARCH);
        }
        
       /* RequestParameter params = new RequestParameter(true);
        params.setUrl(Constants.STOCK_CHECK_SEARCH_GOODS);
        params.setParam("shopId", this.selectShopId);
        params.setParam("currentPage", 1);
        if(!StringUtils.isEmpty(keyword)) {
            params.setParam("keyword", keyword);
        }
        new AsyncHttpPost(this, params, StockCheckSearchGoodsBo.class, new RequestCallback(){

            @Override
            public void onSuccess(Object bo) {
                  
                StockCheckSearchGoodsBo goodsBo = (StockCheckSearchGoodsBo)bo;
                List<CodeSearchCheckGoodsVo> list = goodsBo.getGoodsList();
                if(list != null && list.size() > 0) {
                    CodeSearchCheckGoodsVo searchGoods = list.get(0);
                    String stockCheckId = goods.getStockCheckId();
                    goods = new StockGoodsCheckVo();
                    goods.setStockCheckId(stockCheckId);
                    goods.setGoodsId(searchGoods.getGoodsId());
                    goods.setGoodsName(searchGoods.getGoodsName());
                    goods.setBarCode(searchGoods.getBarcode());
                    goods.setCount(searchGoods.getNowStore());
                    
                    hasSaved = false;
                    String realStore = "";
                    realStoreTxt.initData(realStore);
                }
            }

            @Override
            public void onFail(Exception e) {
                
            }}).execute();;*/
    }
}
