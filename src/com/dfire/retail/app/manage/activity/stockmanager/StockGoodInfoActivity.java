package com.dfire.retail.app.manage.activity.stockmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.logisticmanager.StoreOrderAddActivity;
import com.dfire.retail.app.manage.activity.logisticmanager.StoreOrderAddGoodsActivity;
import com.dfire.retail.app.manage.data.StockAdjustVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 库存调整  商品信息
 * @author ys
 *
 */
@SuppressLint({ "SimpleDateFormat", "UseValueOf" })
public class StockGoodInfoActivity extends Activity implements OnClickListener{

	private ImageButton cancel,save;
	
	private TextView goods_name,barCode,nowStore,adjustReasonId,purchasePrice,retailPrice,resultPrice;
	
	private EditText adjustStore;
	
	private LinearLayout goods_price_view;
	
	private View price_view;
	
	private String goodId;
	
	private Button delete;
	
	private ProgressDialog progressDialog;
	
	private String shopId,shopName,typeName;
	
	private StockAdjustVo adjustVo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_good_info);
		
		this.init();
	}
	/**
	 * 初始化控件
	 */
	private void init(){
		this.shopName = getIntent().getStringExtra("shopName");
		this.goodId = getIntent().getStringExtra("goodId");
		this.shopId = getIntent().getStringExtra("shopId");
		this.cancel = (ImageButton) this.findViewById(R.id.cancel);
		this.save = (ImageButton) this.findViewById(R.id.save);
		this.goods_name = (TextView) this.findViewById(R.id.goods_name);
		this.barCode = (TextView) this.findViewById(R.id.barCode);
		this.nowStore = (TextView) this.findViewById(R.id.nowStore);
		this.adjustReasonId = (TextView) this.findViewById(R.id.adjustReasonId);
		this.purchasePrice = (TextView) this.findViewById(R.id.purchasePrice);
		this.retailPrice = (TextView) this.findViewById(R.id.retailPrice);
		this.resultPrice = (TextView) this.findViewById(R.id.resultPrice);
		this.adjustStore = (EditText) this.findViewById(R.id.adjustStore);
		
		this.goods_price_view = (LinearLayout) this.findViewById(R.id.goods_price_view);
		this.price_view = (View) this.findViewById(R.id.price_view);
		this.delete = (Button) this.findViewById(R.id.delete);

		this.cancel.setOnClickListener(this);
		this.adjustReasonId.setOnClickListener(this);
		this.save.setOnClickListener(this);
		this.delete.setOnClickListener(this);
		
		this.progressDialog = new ProgressDialog(StockGoodInfoActivity.this);
		this.progressDialog.setCancelable(false);
		this.progressDialog.setCanceledOnTouchOutside(false);
		this.progressDialog.setMessage("加载中，请稍后。。。");
		
		if (goodId!=null&&!goodId.equals("")) {
			this.findAdjustById();
		}
	}
	/**
	 * 初始化数据
	 */
	private void initData(){
		if (adjustVo!=null) {
			this.goods_name.setText(shopName);
			this.barCode.setText(adjustVo.getBarCode());
			this.nowStore.setText(adjustVo.getNowStore()+"");
			this.purchasePrice.setText(adjustVo.getPurchasePrice()+"");
			this.retailPrice.setText(adjustVo.getResultPrice()+"");
			this.resultPrice.setText(adjustVo.getResultPrice()+"");
			this.adjustStore.setText(adjustVo.getAdjustStore()+"");
		}
	}
	/**
	 * 回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==100) {
			if (data!=null) {
				adjustVo.setAdjustReasonId(data.getStringExtra("typeVal"));
				adjustVo.setTypeName(data.getStringExtra("typeName"));
				adjustReasonId.setText(adjustVo.getTypeName());
			}
		}
	}
	@Override
	public void onClick(View v) {
		if (v==cancel) {
			finish();
		}else if (v==save) { 
			saveAdjustmentVo();
		}else if (v==delete) {
			
		}else if (v==adjustReasonId) {
			Intent reasonIntent = new Intent();
			reasonIntent.setClass(StockGoodInfoActivity.this, AdjustmentReasonActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("adjustVo", adjustVo);
			reasonIntent.putExtras(bundle);
			startActivityForResult(reasonIntent, 100);
		}
	}
	/**
	 * 保存
	 */
	private void saveAdjustmentVo(){
		if (adjustStore.getText().toString().equals("")) {
			ToastUtil.showShortToast(StockGoodInfoActivity.this, "请输入正确的调整数量！");
		}else {
			if (adjustVo!=null) {
				adjustVo.setAdjustStore(Integer.parseInt(adjustStore.getText().toString()));
				Intent add = new Intent(StockGoodInfoActivity.this, StockAdjustmentAddActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("adjustVo", adjustVo);
				add.putExtras(bundle);
				setResult(100, add);
				StockAddGoodsActivity.instance.finish();
				this.finish();
			}else {
				ToastUtil.showShortToast(StockGoodInfoActivity.this, "没有要调整的商品！");
			}
		}
		
	}
	
	/**
	 * 根据goodId 查询库存详情 stockAdjust/getGoodsInfo
	 */
	private void findAdjustById(){
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockAdjust/getGoodsInfo");
		params.setParam("shopId", shopId);
		params.setParam("goodsId", goodId);

		new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get("returnCode");
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null ||!returnCode.equals("success")) {
					ToastUtil.showShortToast(StockGoodInfoActivity.this, "获取失败");
					return;
				}
				
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(StockGoodInfoActivity.this)) {
					return;
				}
				adjustVo = new StockAdjustVo();
				adjustVo = (StockAdjustVo) ju.get(Constants.STOCKADJUSTVO, new TypeToken<StockAdjustVo>(){}.getType());
				initData();
			}

			@Override
			public void onFail(Exception e) {
				progressDialog.dismiss();
				ToastUtil.showShortToast(StockGoodInfoActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}
}
