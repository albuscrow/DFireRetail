
/************************* 版权声明 **********************************
 * 版权所有：Copyright (c) 2008, 2014 张向华
 *
 * 工程名称：	Retail
 * 创建者：	zxh 创建日期： 2014年10月20日
 * 创建记录：	创建类结构。
 *
 * ************************* 变更记录 ********************************
 * 修改者： 
 * 修改日期：
 * 修改记录：
 *
 * 
 * ......************************* To Do List*****************************
 * 
 *
 * Suberversion 信息
 * ID:			$Id$
 * 源代码URL：	$HeadURL$
 * 最后修改者：	$LastChangedBy$
 * 最后修改日期：	$LastChangedDate$
 * 最新版本：		$LastChangedRevision$
 **/

package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dfire.retail.app.common.exception.BizException;
import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.common.LoadingDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.CodeSearchCheckGoodsVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
import com.dfire.retail.app.manage.data.bo.LoginReturnBo;
import com.dfire.retail.app.manage.data.bo.StockCheckBo;
import com.dfire.retail.app.manage.data.bo.StockCheckSearchGoodsBo;
import com.dfire.retail.app.manage.data.bo.StockCheckStartBo;
import com.dfire.retail.app.manage.data.bo.StockCheckStatusBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestException;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.DBHelper;
import com.dfire.retail.app.manage.util.MD5;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * 库存盘点管理.
 * 
 * @author <a href="mailto:zxh1000@163.com">张向华</a>.
 * @version $Revision: 1570 $
 */
public class StockCheckActivity extends TitleActivity implements
		OnClickListener, IItemListListener, IOnItemSelectListener {

	/**
	 * <code>从盘点人员:其他的盘点人员</code>.
	 */
	private static final int STOCK_SECOND_USER = 4;
	/**
	 * <code>主盘点人员:第一个创建盘存单的人员</code>.
	 */
	private static final int STOCK_MAIN_USER = 3;
	/**
	 * <code>盘存还未开始</code>.
	 */
	private static final int STOCK_UNSTART = 2;
	/**
	 * <code>查店的过程，需要隐藏所有的按钮</code>.
	 */
	private static final int BUTTON_HIDEN = 1;
	private static final short CHECK_TYPE_ALL = 1,CHECK_TYPE_SAMPLE = 2;
	private static final int REQUEST_CODE_SHOP = 100;
	private static final int REQUEST_CODE_OVERVIEW = 1;
	
	private static final int MSG_EXCEPTION = 1,MSG_SUCCESS = 0;
	private ItemEditList lsShop; // 下拉店
	private Button btnStart; // 开始盘点（全部）
	private Button btnStartSample; // 开始盘点(抽样)
	private Button btnContinue; // 继续盘点
	private Button btnReport; // 查询未盘点商品
	private Button btnCancel; // 取消汇总.
	private Button btnFinish; // 完成盘点.
	private View btnHelp;
	
	private TextView shopTextView;
	private TextView tipTextView;
	
	private ShopVo currentShop; // 当前店.
	private int currentPage = Constants.PAGE_SIZE_OFFSET;
	private SpinerPopWindow shopsSpinner;
	private ArrayList<ShopVo> shops;
	private ArrayList<String> shopsStr;
	
	
	private String stockCheckId;  //盘存单Id.
	private String selectedShopId;
	private String selectedShopName;
	private String opUserId;
	private Short checkType;
    private boolean goodsHasLoaded;

	/** {@inheritDoc} */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_check);
		setTitleText("库存盘点");
		showBackbtn();
		findView();
		initView();
	}

	/**
	 * 初始化界面.
	 */
	public void findView() {
		//lsShop = (ItemEditList) findViewById(R.id.lsShop);
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStartSample = (Button) findViewById(R.id.btnStartSample);
		btnContinue = (Button) findViewById(R.id.btnContinue);
		btnReport = (Button) findViewById(R.id.btnReport);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnFinish = (Button) findViewById(R.id.btnFinish);
		btnHelp = findViewById(R.id.help);
		shopTextView = (TextView) findViewById(R.id.shopName);
		tipTextView = (TextView) findViewById(R.id.txtTip);
		
		this.currentShop = RetailApplication.getShopVo();
		selectedShopId = currentShop.getShopId();
		selectedShopName = currentShop.getShopName();
		
		if (RetailApplication.getEntityModel()==1) {
            //单店
            this.shopTextView.setCompoundDrawables(null, null, null, null);
            this.shopTextView.setText(currentShop.getShopName());
        }else {
            //连锁
            if (currentShop.getType() == ShopVo.DANDIAN || currentShop.getType() == ShopVo.MENDIAN) {
                this.shopTextView.setCompoundDrawables(null, null, null, null);
                this.shopTextView.setText(currentShop.getShopName());
            }else{
                if (currentShop.getParentId()!=null) {
                }
                this.shopTextView.setText("请选择");
                this.shopTextView.setOnClickListener(this);
                
            }
        }
	}

	private void initView() {
		//lsShop.initLabel("门店", null, this);
		btnStart.setOnClickListener(this);
		btnStartSample.setOnClickListener(this);
		btnContinue.setOnClickListener(this);
		btnReport.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnFinish.setOnClickListener(this);
		btnHelp.setOnClickListener(this);
		initShopInfo();

	}

	private void initShopInfo() {
	    if (currentShop.getType() == ShopVo.DANDIAN || currentShop.getType() == ShopVo.MENDIAN) {
	        getHashStoreQuery(this.selectedShopId);
	    }
		
		//getShop(true);
	}

	/**
	 * 初始按钮 .
	 */
	private void initUIButton(int status) {
		if (STOCK_UNSTART == status) { // 未开始盘点单.
			hideAllBtn();
			btnStart.setVisibility(View.VISIBLE);
			btnStartSample.setVisibility(View.VISIBLE);
			tipTextView.setVisibility(View.VISIBLE);
		} else if (STOCK_MAIN_USER == status) { // 进入主盘点人员
		    hideAllBtn();
		    if(checkType != null && CHECK_TYPE_ALL == checkType) {
		        btnContinue.setText("继续盘点（全库）");
		    }else if(checkType != null && CHECK_TYPE_SAMPLE == checkType) {
		        btnContinue.setText("继续盘点（抽样）");
		    }
			btnContinue.setVisibility(View.VISIBLE);
			btnReport.setVisibility(View.VISIBLE);
			btnCancel.setVisibility(View.VISIBLE);
			btnFinish.setVisibility(View.VISIBLE);
		} else if (STOCK_SECOND_USER == status) { // 进入非主盘点人员
			hideAllBtn();
			if(checkType != null && CHECK_TYPE_ALL == checkType) {
                btnContinue.setText("继续盘点（全库）");
            }else if(checkType != null && CHECK_TYPE_SAMPLE == checkType) {
                btnContinue.setText("继续盘点（抽样）");
            }
			btnContinue.setVisibility(View.VISIBLE);
			btnReport.setVisibility(View.VISIBLE);
		}
	}

	private void hideAllBtn() {
	    btnStart.setVisibility(View.GONE);
		btnStartSample.setVisibility(View.GONE);
		btnContinue.setVisibility(View.GONE);
		btnReport.setVisibility(View.GONE);
		btnCancel.setVisibility(View.GONE);
		btnFinish.setVisibility(View.GONE);
		tipTextView.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnStart) { // 开始盘点.
			startStockEvent(CHECK_TYPE_ALL);
		}else if (v.getId() == R.id.btnStartSample) { // 开始盘点(抽样).
            startStockEvent(CHECK_TYPE_SAMPLE);
        } else if (v.getId() == R.id.btnContinue) { // 继续盘点.
		    startStockEvent(CHECK_TYPE_ALL);
		} else if (v.getId() == R.id.btnReport) { // 查询未盘点商品
		    Intent intent =new Intent(StockCheckActivity.this,StockCheckUncheckedGoodsListActivity.class);
		    intent.putExtra("selectShopId", selectedShopId);
            intent.putExtra("stockCheckId", this.stockCheckId);
            startActivity(intent);
		} else if (v.getId() == R.id.btnCancel) { // 取消盘点
		    this.cancelStockCheckConfirm();
		} else if (v.getId() == R.id.btnFinish) { // 完成盘点.
		    //this.finishStockCheckConfirm();
		    Intent intent =new Intent(StockCheckActivity.this,StockCheckOverviewActivity.class);
            intent.putExtra("selectShopId", selectedShopId);
            intent.putExtra("selectShopName", selectedShopName);
            intent.putExtra("stockCheckId", this.stockCheckId);
            intent.putExtra("opUserId", this.opUserId);
            startActivityForResult(intent, REQUEST_CODE_OVERVIEW);
		}else if(v.getId() == R.id.shopName) {
		    Intent selectIntent =new Intent(StockCheckActivity.this,AdjusSelectShopActivity.class);
            selectIntent.putExtra("selectShopId", selectedShopId);
            startActivityForResult(selectIntent, REQUEST_CODE_SHOP);
		}else if(v.getId() == R.id.help) {
		    
        }

	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SHOP && resultCode == AdjusSelectShopActivity.RESULT_CODE_SHOP_SEL){
            AllShopVo allShopVo = (AllShopVo)data.getSerializableExtra("shopVo");
            if (allShopVo!=null) {
                this.shopTextView.setText(allShopVo.getShopName());
                this.selectedShopId = allShopVo.getShopId();
                this.selectedShopName = allShopVo.getShopName();
                getHashStoreQuery(selectedShopId);
            }
        }else if(requestCode == REQUEST_CODE_OVERVIEW && resultCode == StockCheckOverviewActivity.RESULT_FINISH_STOCK_CHECK) {
            initUIButton(STOCK_UNSTART);
            new ErrDialog(this, "盘点完成").show();
            stockCheckId = null;
            goodsHasLoaded = false;
        }
    }
	private void startAddActivity() {
	    if(goodsHasLoaded) {
	        Intent addGoods = new Intent(StockCheckActivity.this,
                    StockCheckRegionActivity.class);
            addGoods.putExtra("selectShopId", selectedShopId);
            addGoods.putExtra("stockCheckId", stockCheckId);
            
            startActivity(addGoods);
            return;
	    }
	    final DBHelper dbHelper = new DBHelper(this);
	    final SQLiteDatabase db1 = dbHelper.getReadableDatabase();
	    try {
            Cursor cursor = db1.rawQuery("select count(*) from stockchecksearchgoods where shopid=?", new String[]{this.selectedShopId});
            if(cursor.getCount() > 0) {
                cursor.moveToNext();
                int count = cursor.getInt(0);
                if(count > 0) {
                    this.goodsHasLoaded = true;
                    Intent addGoods = new Intent(StockCheckActivity.this,
                            StockCheckRegionActivity.class);
                    addGoods.putExtra("selectShopId", selectedShopId);
                    addGoods.putExtra("stockCheckId", stockCheckId);
                    
                    startActivity(addGoods);
                    return;
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();  
        }finally {
            dbHelper.close();
        }
	    
	    new AsyncCheckGoodsTask ().execute();   
       
	}

	private class AsyncCheckGoodsTask extends AsyncTask<Void, Integer, Object> {
	    private Integer pageSize;
	    final LoadingDialog dialog = new LoadingDialog(StockCheckActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dialog.dismiss();
            
            if(result != null) {
                new ErrDialog(StockCheckActivity.this, (String)result).show();
                return;
            }
            goodsHasLoaded = true;
            Intent addGoods = new Intent(StockCheckActivity.this,
                    StockCheckRegionActivity.class);
            addGoods.putExtra("selectShopId", selectedShopId);
            addGoods.putExtra("stockCheckId", stockCheckId);
            
            startActivity(addGoods);
        }

        @Override
        protected Object doInBackground(Void... params) {
            final RequestParameter requestParameter = new RequestParameter(true);
            requestParameter.setUrl(Constants.STOCK_CHECK_DOWNLOAD_GOODS_URL);
            requestParameter.setParam("shopId", selectedShopId);
            
            boolean isFirstPage;
            int currentPage = 1;
            final Gson gson = new Gson();
            final DBHelper dbHelper = new DBHelper(StockCheckActivity.this);
            final SQLiteDatabase db = dbHelper.getReadableDatabase();
            try {
                do {
                    isFirstPage = false;
                    requestParameter.setParam("currentPage", currentPage++);
                    String res = AsyncHttpPost.postParm(requestParameter.getUrl(), requestParameter.getParams().toString());
                    Object oj = gson.fromJson((String) res, StockCheckSearchGoodsBo.class);
                    if (oj instanceof BaseRemoteBo) {
                        BaseRemoteBo base = (BaseRemoteBo) oj;
                        if (!base.isSuccess()) {
                            //session expired
                            if (base.getExceptionCode() != null && base.getExceptionCode().equals("CS_MSG_000019")) {
                                boolean isSuccess = loginTask(gson);
                                if (isSuccess) {
                                    requestParameter.refreshSessionParams();
                                    isFirstPage = true;
                                    continue;
                                }
                                return "下载盘点商品失败";
                            } else {
                                String exceptionCode = base.getExceptionCode();
                                String message = null;
                                if (exceptionCode != null && exceptionCode.matches(AsyncHttpPost.REMOTE_MSG_PATTERN)) {
                                    int resource = StockCheckActivity.this.getResources().getIdentifier(exceptionCode, "string", StockCheckActivity.this.getPackageName());
                                    if (resource != 0) {
                                        message = StockCheckActivity.this.getResources().getString(resource);
                                    } else {
                                        message = exceptionCode;
                                    }
                                } else {
                                    message = exceptionCode;
                                }
                                
                                return message;
                            }
                        } else {
                            //success 
                            StockCheckSearchGoodsBo goodsBo = (StockCheckSearchGoodsBo) oj;
                            if(pageSize == null) {
                                pageSize = goodsBo.getPageSize();
                            }
                            List<CodeSearchCheckGoodsVo> list = goodsBo.getGoodsList();
                            if (list != null && !list.isEmpty()) {
                                for (CodeSearchCheckGoodsVo vo : list) {
                                    vo.setShopId(selectedShopId);
                                    db.insert("stockchecksearchgoods", null, vo.getContentValues());
                                }
                            }
                        }
                    }
                } while ((pageSize != null && --pageSize > 0) || isFirstPage);
                
            } catch (JsonSyntaxException e) {
                return "下载盘点商品失败";
            } catch (NotFoundException e) {
                return "下载盘点商品失败";
            } catch(BizException e) {
                return e.getMessage();
            }finally {
                dbHelper.close();
            }
            return null;
        }
        private boolean loginTask(Gson gson) {
            
            RetailApplication.setmSessionId("");
            RequestParameter parameters = new RequestParameter(true);
            parameters.setUrl(Constants.LOGIN);
            String strPass="";
            String userName = RetailApplication.userName.toUpperCase();
            String password = RetailApplication.password.toUpperCase();
            String code = RetailApplication.code;
            strPass = MD5.GetMD5Code(password);
            parameters.setParam("entityCode", code);
            parameters.setParam("username",userName);
            parameters.setParam("password", strPass);
            String res = AsyncHttpPost.postParm(parameters.getUrl(), parameters.getParams().toString());
            Object oj = gson.fromJson((String)res, LoginReturnBo.class);
            LoginReturnBo bo = (LoginReturnBo)oj;
            if (bo!=null) {
                RetailApplication.setShopVo(bo.getShop());
                RetailApplication.setEntityModel(bo.getEntityModel());
                RetailApplication.setUserInfo(bo.getUser());                                                                       
                RetailApplication.setmSessionId(bo.getJsessionId());
                return true;
            }
            return false;
        }
    }

    /**
	 * 开始盘点.
	 */
	private void startStockEvent(short checkType) {
	    if(this.stockCheckId != null) {
	        startAddActivity();
	        return;
	    }
	    this.checkType = checkType;
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.STOCK_CHECK_START_URL);
		params.setParam(Constants.SHOP_ID,this.selectedShopId);
		params.setParam("checkType",checkType);
		new AsyncHttpPost(this, params, StockCheckStartBo.class, new RequestCallback(){
            @Override
            public void onSuccess(Object bo) {
                StockCheckStartBo stockCheckStart = (StockCheckStartBo)bo;
                if(Constants.SUCCESS.equals(stockCheckStart.getMessage()) && !StringUtils.isEmpty(stockCheckStart.getStockCheckId())){
                    stockCheckId = stockCheckStart.getStockCheckId();
                    initUIButton(STOCK_MAIN_USER);
                    startAddActivity();
                }else {
                    new ErrDialog(StockCheckActivity.this, "盘点请求失败").show();
                }
            }

            @Override
            public void onFail(Exception e) {
                new ErrDialog(StockCheckActivity.this, "盘点请求失败").show();
            }}).execute();

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
        params.setParam(Constants.SHOP_ID,this.selectedShopId);
        params.setParam("stockCheckId",this.stockCheckId);
        new AsyncHttpPost(this, params, StockCheckBo.class, new RequestCallback(){
            @Override
            public void onSuccess(Object bo) {
                StockCheckBo stockCheckStart = (StockCheckBo)bo;
                new ErrDialog(StockCheckActivity.this, "盘点完成").show();
                initUIButton(STOCK_UNSTART);
                stockCheckId = null;
                
                //删除盘点区域
                DBHelper dbHepler = new DBHelper(StockCheckActivity.this);
                try {
                    final SQLiteDatabase db = dbHepler.getWritableDatabase();
                    db.execSQL("delete from stockcheckarea where stockcheckid=? ",new String[]{stockCheckId});
                } catch (SQLException e) {
                    e.printStackTrace();  
                    
                }finally {
                    dbHepler.close();
                }
            }

            @Override
            public void onFail(Exception e) {
                new ErrDialog(StockCheckActivity.this, "盘点结束失败").show();
            }}).execute();
        
    }
	private void cancelStockCheckConfirm() {
        final AlertDialog alertDialog = new AlertDialog(this);
        alertDialog.setMessage("确定要取消盘点吗？");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                cancelStockCheck();
            }
        });
        alertDialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    private void cancelStockCheck() {
          
        RequestParameter params = new RequestParameter(true);
        params.setUrl(Constants.STOCK_CHECK_CANCEL);
        params.setParam(Constants.SHOP_ID,this.selectedShopId);
        params.setParam("stockCheckId",this.stockCheckId);
        new AsyncHttpPost(this, params, StockCheckBo.class, new RequestCallback(){
            @Override
            public void onSuccess(Object bo) {
                StockCheckBo stockCheckStart = (StockCheckBo)bo;
                new ErrDialog(StockCheckActivity.this, "盘点已取消").show();
                stockCheckId = null;
                goodsHasLoaded = false;
                initUIButton(STOCK_UNSTART);
                
                //删除盘点商品
                DBHelper dbHepler = new DBHelper(StockCheckActivity.this);
                try {
                    final SQLiteDatabase db = dbHepler.getWritableDatabase();
                    db.execSQL("delete from stockcheck where stockcheckid=? ",new String[]{stockCheckId});
                    db.execSQL("delete from stockchecksearchgoods where shopid=? ",new String[]{selectedShopId});
                } catch (SQLException e) {
                    e.printStackTrace();  
                    
                }finally {
                    dbHepler.close();
                }
            }

            @Override
            public void onFail(Exception e) {
                new ErrDialog(StockCheckActivity.this, "盘点取消失败").show();
            }}).execute();
        
    }
	/** {@inheritDoc} */
	@Override
	public void onItemListClick(ItemEditList obj) {
		/*if (obj.getId() == R.id.lsShop) {
			shopsSpinner.setWidth(lsShop.getWidth());
			shopsSpinner.showAsDropDown(lsShop);
		}*/
	}

	@Override
	public void onItemClick(int pos) {
		currentShop = shops.get(pos);
		lsShop.initData(currentShop.getShopName(), currentShop.getShopId());
		getHashStoreQuery(RetailApplication.getmShopInfo().getShopId());
	}

	/**
	 * 查询是否盘点状态，返回主盘点员。
	 * 
	 * @param shopId
	 */
	private void getHashStoreQuery(final String shopId) {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.STOCK_CHECK_STATUS_URL);
		params.setParam(Constants.SHOP_ID, this.selectedShopId);
		new AsyncHttpPost(this, params, StockCheckStatusBo.class, new RequestCallback(){

            @Override
            public void onSuccess(Object bo) {
                StockCheckStatusBo stockCheckStatus = (StockCheckStatusBo)bo;
                String userId = RetailApplication.getmUserInfo().getUserId();
                stockCheckId = stockCheckStatus.getStockCheckId();
                if(StringUtils.isEmpty(stockCheckStatus.getStockCheckId())){
                    opUserId = userId;
                    initUIButton(STOCK_UNSTART);
                }else if(stockCheckStatus.getOpUserId().equals(userId)){
                    opUserId = stockCheckStatus.getOpUserId();
                    checkType = stockCheckStatus.getCheckType();
                    initUIButton(STOCK_MAIN_USER);
                }else{
                    opUserId = stockCheckStatus.getOpUserId();
                    checkType = stockCheckStatus.getCheckType();
                    initUIButton(STOCK_SECOND_USER);
                }
            }

            @Override
            public void onFail(Exception e) {
                
            }}).execute();
		
	}
}
