package com.dfire.retail.app.manage.activity.stockmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.zxing.MipcaActivityCapture;

/**
 * 库存管理-库存查询
 * @author ys
 */
public class StockQueryActivity extends TitleActivity implements OnClickListener{
	
	private TextView search,mELStockShop;
	
	private EditText input;
	
	private ImageButton stock_query_scan;
	
	private ImageView scan;
	
    private AllShopVo allShopVo;
    
    private String shopId,findParameter,isStore;
    
	private ShopVo currentShop;
	
	private ImageView clear_input;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_query);
		setTitleText("库存查询");
		showBackbtn();
		findView();
	}
	
	public void findView(){
		stock_query_scan = (ImageButton) findViewById(R.id.stock_query_scan);
		mELStockShop= (TextView) findViewById(R.id.stockShopName);
		mELStockShop.setOnClickListener(this);
		stock_query_scan.setOnClickListener(this);
		shopId = RetailApplication.getShopVo().getShopId();
		currentShop = RetailApplication.getShopVo();
		search=(TextView)findViewById(R.id.stock_search);
		search.setOnClickListener(this);
		input=(EditText)findViewById(R.id.stock_shop_input);
		scan=(ImageView)findViewById(R.id.stock_scan);
		scan.setOnClickListener(this);
		clear_input = (ImageView) findViewById(R.id.clear_input);
		clear_input.setOnClickListener(this);
		
		if (RetailApplication.getEntityModel()==1) {
			//单店
			this.mELStockShop.setCompoundDrawables(null, null, null, null);
			this.mELStockShop.setText(currentShop.getShopName());
			this.mELStockShop.setTextColor(Color.parseColor("#666666"));
			isStore = "1";
		}else {
			//连锁
			if (currentShop.getType() == ShopVo.MENDIAN) {
				this.mELStockShop.setCompoundDrawables(null, null, null, null);
				this.mELStockShop.setTextColor(Color.parseColor("#666666"));
				this.mELStockShop.setText(currentShop.getShopName());
			}else{
				this.mELStockShop.setText("请选择");
				this.mELStockShop.setOnClickListener(this);
				if (currentShop.getType() == ShopVo.ZHONGBU) {
					isStore = "1";
				}
			}
		}
		
		input.addTextChangedListener(new TextWatcher() {
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stock_search:
			findParameter = input.getText().toString();
			if (!StringUtils.isEquals(mELStockShop.getText().toString(),"请选择")) {
				getStockInfoList();
			}else {
				new ErrDialog(this, getResources().getString(R.string.please_select_shop)).show();
			}
			break;
		case R.id.stockShopName:	
			Intent selectIntent =new Intent(this,AdjusSelectShopActivity.class);
			selectIntent.putExtra("selectShopId", shopId);
			startActivityForResult(selectIntent, 100);
			break;
		case R.id.stock_scan:    
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.FOR_GET);
			break;
		case R.id.stock_query_scan:    
			startActivityForResult(new Intent(this, MipcaActivityCapture.class), Constants.FOR_GET);
			break;	
		case R.id.clear_input:
			input.setText("");
			clear_input.setVisibility(View.GONE);
		break;
		default:
			break;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode==100){
		    allShopVo = (AllShopVo)data.getSerializableExtra("shopVo");
			if (allShopVo!=null) {
				mELStockShop.setText(allShopVo.getShopName());
				shopId = allShopVo.getShopId();
			}
		}else if (resultCode==RESULT_OK) {
			findParameter = data.getStringExtra(Constants.DEVICE_CODE);
			input.setText(findParameter);
			getStockInfoList();
		}
    }
    /**
     * 查询库存列表
     */
    private void getStockInfoList(){ 
    	String shopName = mELStockShop.getText().toString();
    	startActivity(new Intent(StockQueryActivity.this, StockQueryListActivity.class)
		.putExtra("shopId", shopId)
		.putExtra("shopName", shopName)
		.putExtra("searchCode", findParameter)
		.putExtra("isStore", isStore)
		);
    }
}