package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.item.StoreCollectGoodsItem;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.data.StockInDetailVo;

/**
 * 物流管理-门店进货
 * @author ys
 *
 */
public class StoreCollectAddActivity extends Activity implements OnClickListener{

	private ImageButton title_left;
	
	private ImageButton title_right;
	
	private RelativeLayout add_layout;
	
	private LinearLayout store_collect_add_lv;
	
	private StockInDetailVo stockInDetailVo;
	
	private List<StockInDetailVo> stockInDetailVos;
	
	private SearchGoodsVo searchGoodsVo;
	
	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_collect_add);
		inflater = LayoutInflater.from(this);
		findView();
	}
	
	public void findView(){
		stockInDetailVos=new ArrayList<StockInDetailVo>();
		title_left=(ImageButton)findViewById(R.id.title_left);
		title_left.setOnClickListener(this);
		title_right=(ImageButton)findViewById(R.id.title_right);
		title_right.setOnClickListener(this);
		add_layout=(RelativeLayout)findViewById(R.id.add_layout);
		add_layout.setOnClickListener(this);
		store_collect_add_lv=(LinearLayout)findViewById(R.id.store_collect_add_lv);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){
			Bundle b=data.getExtras();
			searchGoodsVo=(SearchGoodsVo)b.getSerializable("orderAdd");
			stockInDetailVo = new StockInDetailVo();
			stockInDetailVo.setGoodsId(searchGoodsVo.getGoodsId());//id
			stockInDetailVo.setGoodsName(searchGoodsVo.getGoodsName());//名称
			stockInDetailVo.setGoodsBarcode(searchGoodsVo.getGoodsBarCode());//条码
			stockInDetailVo.setGoodsPrice(searchGoodsVo.getPurchasePrice());//价格
			stockInDetailVo.setGoodsSum(1);//默认 选择1件
			stockInDetailVo.setGoodsTotalPrice(searchGoodsVo.getPurchasePrice());//默认合计 。 会乘以数量
			stockInDetailVo.setOperateType("add"); //操作类型
			stockInDetailVos.add(stockInDetailVo);
			
			StoreCollectGoodsItem collectGoodsItem = new StoreCollectGoodsItem(this,inflater,false);
			collectGoodsItem.initWithData(stockInDetailVo);
			store_collect_add_lv.addView(collectGoodsItem.getItemView());
		}
	}
 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			
			break;

		case R.id.title_right:
			
			break;
			
		case R.id.add_layout:
			Intent addGoods=new Intent(StoreCollectAddActivity.this,StoreOrderAddGoodsActivity.class);
			addGoods.putExtra("add", 2);
			startActivityForResult(addGoods, 100);
			break;
		}
	}

}
