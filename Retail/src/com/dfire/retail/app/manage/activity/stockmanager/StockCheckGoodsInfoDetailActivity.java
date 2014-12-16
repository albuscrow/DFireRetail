package com.dfire.retail.app.manage.activity.stockmanager;


import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.SwitchRowItemEditText;
import com.dfire.retail.app.common.item.listener.IItemTextChangeListener;
import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.StockGoodsCheckVo;
import com.dfire.retail.app.manage.data.bo.StockCheckBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.Gson;
/**
 * 项目名称：Retail  
 * 类名称：StockCheckGoodsInfo  
 * 类描述：   商品盘点详情页,由盘点列表页进入
 * 创建时间：2014年11月15日 下午6:29:16  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckGoodsInfoDetailActivity extends TitleActivity implements View.OnClickListener {
    public final static int RESULT_SAVE = 1,RESULT_DELETE = 2;
    private TextView goodsNameTxt;
    private TextView barcodeTxt;
    private TextView nowStoreTxt;
    private TextView realStoreTxt;
    private ItemEditText adjustStoreTxt;
    private TextView exhibitTxt;
    private TextView retailPriceTxt;
    private TextView checkPriceTxt;
    private TextView resultPriceTxt;
    private Button deleteBtn;
    
    private StockGoodsCheckVo goods;
    private String selectShopId;
    private String stockCheckId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_check_goods_info_detail);
        setTitleText("商品盘点");
        initView();
        initData();
    }
    private void initView() {
        goodsNameTxt = (TextView)this.findViewById(R.id.goods_name);
        barcodeTxt = (TextView)this.findViewById(R.id.barCode);
        nowStoreTxt = (TextView)this.findViewById(R.id.nowStore);
        realStoreTxt = (TextView)this.findViewById(R.id.realStore);
        adjustStoreTxt = (ItemEditText)this.findViewById(R.id.adjustStore);
        adjustStoreTxt.setIsChangeListener(super.getItemChangeListener());
        adjustStoreTxt.setTextChangeListener(new IItemTextChangeListener() {
            
            @Override
            public void onItemEditTextChange(ItemEditText obj) {
                String currVal = obj.getCurrVal();
                if(!StringUtils.isEmpty(currVal)) {
                    realStoreTxt.setText(String.valueOf(goods.getCheckCount() + Integer.parseInt(currVal)));
                }
            }
            
            @Override
            public void onItemEditTextChange(ItemEditList obj) {
            }

			@Override
			public void onItemEditTextChange(SwitchRowItemEditText obj) {
				// TODO Auto-generated method stub
			}
        });
        this.exhibitTxt = (TextView)this.findViewById(R.id.txt_exhibit_count);
        this.retailPriceTxt = (TextView)this.findViewById(R.id.txt_retail_price);
        this.checkPriceTxt = (TextView)this.findViewById(R.id.txt_check_price);
        this.resultPriceTxt = (TextView)this.findViewById(R.id.txt_result_price);
        adjustStoreTxt.initLabel("调整库存数", "调整库存数", false, InputType.TYPE_CLASS_NUMBER);
        deleteBtn = (Button)this.findViewById(R.id.delete);
        deleteBtn.setOnClickListener(this);
        
    }
    private void initData() {
        Intent intent = this.getIntent();
        goods = (StockGoodsCheckVo)intent.getSerializableExtra("goods");
        selectShopId = intent.getStringExtra("selectShopId");
        stockCheckId = intent.getStringExtra("stockCheckId");
        this.showBackbtn();
        this.mRight.setOnClickListener(this);
        if(goods != null) {
            this.goodsNameTxt.setText(goods.getGoodsName());
            this.barcodeTxt.setText(goods.getBarCode());
            if(goods.getCount() != null) {
                this.nowStoreTxt.setText(String.valueOf(goods.getCount()));
            }else {
                this.nowStoreTxt.setText("无");
            }
            if(goods.getCheckCount() != null) {
                //this.realStoreTxt.initData(String.valueOf(goods.getCheckCount()));
                this.realStoreTxt.setText(String.valueOf(goods.getCheckCount()));
            }
            this.exhibitTxt.setText(String.valueOf(goods.getGetLossNumber()));
            this.retailPriceTxt.setText(goods.getRetailPrice() == null ? "" : goods.getRetailPrice().toString());
            this.checkPriceTxt.setText(goods.getCheckCountPrice() == null ? "" : goods.getCheckCountPrice().toString());
            this.resultPriceTxt.setText(goods.getResultPrice() == null ? "" : goods.getResultPrice().toString());
        }
    }
    @Override
    public void onClick(View v) {
          
        switch(v.getId()) {
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
    private void save() {
       /* if(StringUtils.isEmpty(realStoreTxt.getStrVal())) {
            ToastUtil.showShortToast(this, "请输入实际数量");
            return;
        }  
        if(realStoreTxt.getStrVal().equals(realStoreTxt.getOldVal())) {
            finish();
            return;
        }*/
        if(StringUtils.isEmpty(adjustStoreTxt.getCurrVal())) {
            ToastUtil.showShortToast(this, "请输入调整库存数量");
            return;
        }
        StockGoodsCheckVo vo = new StockGoodsCheckVo();
        vo.setRegion("其他");
        vo.setCheckCount(Integer.parseInt(adjustStoreTxt.getCurrVal()));
        vo.setGoodsId(goods.getGoodsId());
        vo.setGoodsName(goods.getGoodsName());
        vo.setBarCode(goods.getBarCode());
        vo.setPurchasePrice(goods.getPurchasePrice());
        vo.setRetailPrice(goods.getRetailPrice());
        vo.setStockCheckId(this.stockCheckId);
        vo.setCount(0);
        vo.setGetLossNumber(0);
        vo.setCheckCountPrice(BigDecimal.valueOf(0));
        vo.setResultPrice(BigDecimal.valueOf(0));
        
        RequestParameter params = new RequestParameter(true);
        params.setUrl(Constants.STOCK_CHECK_UPDATE_STORE);
        params.setParam("shopId",this.selectShopId);
        try {
            params.setParam("stockGoodsCheckVo", new JSONObject(new Gson().toJson(vo)));
        } catch (JSONException e1) {
            e1.printStackTrace();  
            
        }
        
        new AsyncHttpPost(this, params, StockCheckBo.class, new RequestCallback(){
            @Override
            public void onSuccess(Object bo) {
                StockCheckBo stockCheck = (StockCheckBo)bo;
                if(Constants.SUCCESS.equals(stockCheck.getMessage())) {
                    Intent data = new Intent();
                    data.putExtra("goodsId", goods.getGoodsId());
                    int newStore = Integer.parseInt(realStoreTxt.getText().toString());
                    data.putExtra("newStore", newStore);
                    int count = 0;
                    if(goods.getCount() != null) {
                        count = goods.getCount();
                    }
                    data.putExtra("exhibitCount", newStore - count);
                    setResult(RESULT_SAVE,data);
                    finish();
                }else {
                    new ErrDialog(StockCheckGoodsInfoDetailActivity.this, "保存失败");
                }
            }

            @Override
            public void onFail(Exception e) {
                new ErrDialog(StockCheckGoodsInfoDetailActivity.this, "保存失败");
            }}).execute();
        
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
        RequestParameter params = new RequestParameter(true);
        params.setUrl(Constants.STOCK_CHECK_CLEAR_STORE);
        params.setParam("shopId", this.selectShopId);
        params.setParam("goodsId", goods.getGoodsId());
        
        new AsyncHttpPost(this, params, StockCheckBo.class, new RequestCallback(){
            @Override
            public void onSuccess(Object bo) {
                StockCheckBo stockCheck = (StockCheckBo)bo;
                Intent data = new Intent();
                data.putExtra("goodsId", goods.getGoodsId());
                setResult(RESULT_DELETE, data);
                finish();
            }

            @Override
            public void onFail(Exception e) {
                ToastUtil.showShortToast(StockCheckGoodsInfoDetailActivity.this, "删除失败");
            }}).execute();
        
    }
   
    
}
