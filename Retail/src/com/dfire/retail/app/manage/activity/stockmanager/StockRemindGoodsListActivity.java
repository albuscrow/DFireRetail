package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.goodsmanager.GoodsManagerBaseActivity;
import com.dfire.retail.app.manage.adapter.StockRemindGoodsListAdapter;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.util.ToastUtil;

/**
 * The Class 单选 ，全选 全不选.
 * 
 * @author ys
 */
public class StockRemindGoodsListActivity extends GoodsManagerBaseActivity implements OnClickListener, OnItemClickListener{
	
	private ArrayList<GoodsVo> goods;
	
	private boolean[] goodsSelectStatus;
	
	private ListView goodsListView;
	
	private StockRemindGoodsListAdapter adapter;

	private TextView settingKind;
	
	private String shopId,categoryName;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_manager_list);
		setTitleText(Constants.CHOOSE_GOODS);
		
		goods = (ArrayList<GoodsVo>) getIntent().getSerializableExtra("goods");
		categoryName = getIntent().getStringExtra("categoryName");
		shopId = getIntent().getStringExtra("shopId");
		goodsSelectStatus = new boolean[goods.size()];
		selectAll(false);

		settingKind = (TextView) findViewById(R.id.settingKind);
		if (categoryName==null) {
			settingKind.setVisibility(View.GONE);
		}else {
			settingKind.setVisibility(View.VISIBLE);
			settingKind.setText(categoryName);
		}
		goodsListView = (ListView) findViewById(R.id.goodsList);
		adapter = new StockRemindGoodsListAdapter(this, goods, goodsSelectStatus);
		goodsListView.setAdapter(adapter);
		goodsListView.setOnItemClickListener(this);
		
		findViewById(R.id.not).setOnClickListener(this);
		findViewById(R.id.all).setOnClickListener(this);
		
		switchToEditMode();
		setCancel();
		findViewById(R.id.title_right).setOnClickListener(this);
		setRightBtn(R.drawable.yes, Constants.OPT);
	}

	/**
	 * 全选
	 */
	private void selectAll(boolean b) {
		for (int i = 0; i < goodsSelectStatus.length; ++i) {
			goodsSelectStatus[i] = b; 
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.not:
			selectAll(false);
			adapter.notifyDataSetChanged();
			break;
		case R.id.all:
			selectAll(true);
			adapter.notifyDataSetChanged();
			break;
		case R.id.title_right:
			ArrayList<String> ids = (ArrayList<String>) getIds();
			if (ids.size() == 0) {
				ToastUtil.showShortToast(this, Constants.CHOOSE_SOMETHING);
			}else{
				startActivity(new Intent(this, StockRemindGoodsSaveSettingActivity.class)
				.putExtra(Constants.GOODS, goods)
				.putExtra(Constants.GOODSIDS, ids)
				.putExtra(Constants.SHOP_ID, shopId)
				.putExtra("activity", "stockRemindGoodsListActivity"));
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 获取id 列表
	 */
	private List<String> getIds() {
		List<String> ids = new ArrayList<String>();
		for (int i = 0; i < goods.size(); ++i) {
			if (goodsSelectStatus[i]) {
				ids.add(goods.get(i).getGoodsId());
			}
		}
		return ids;
	}
	/** 
	 * 点击选中
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CheckBox checkBox = (CheckBox)view.findViewById(R.id.good_checkbox);
		checkBox.setChecked(!checkBox.isChecked());
	}
	
}
