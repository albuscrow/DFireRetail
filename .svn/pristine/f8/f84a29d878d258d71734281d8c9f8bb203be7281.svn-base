/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.ItemTextView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.vo.ReturnGoodsDetailVo;
import com.dfire.retail.app.manage.vo.allocateDetailVo;

/**
 * @author 李锦运
 *2014-10-27
 */
public class StoreAllocationDetailItemActivity extends TitleActivity implements OnClickListener{

	private ItemTextView lblName;
	
	private ItemEditText txtNum;
	
	private ItemTextView lblCode;
	
	
	private SearchGoodsVo searchGoodsVo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_allocation_item_detail);
		setTitleText("商品信息");
		showBackbtn();
		initMainUI();
		initData();
		change2saveMode();


		mRight = (ImageButton) findViewById(R.id.title_right);
		mRight.setOnClickListener(this);
	}
	private void initMainUI() {

		this.lblName = (ItemTextView) this.findViewById(R.id.lblName);
		this.lblCode = (ItemTextView) this.findViewById(R.id.lblCode);
		this.txtNum = (ItemEditText) this.findViewById(R.id.txtNum);

		this.lblName.initLabel("商品名称", null);
		this.lblCode.initLabel("商品条码", null);
		this.txtNum.initLabel("调拨数量", null, Boolean.TRUE, InputType.TYPE_NUMBER_FLAG_SIGNED);
	}
	
	private void initData() {

		RetailApplication rapp = (RetailApplication) getApplication();

		HashMap<String, Object> map = rapp.getObjMap();

		searchGoodsVo = (SearchGoodsVo) map.get("allocationAdd");

		lblName.initData(searchGoodsVo.getGoodsName(), searchGoodsVo.getGoodsId());
		
		lblCode.initData(searchGoodsVo.getGoodsBarCode(), searchGoodsVo.getGoodsBarCode());
		
		txtNum.initData(String.format("%d", searchGoodsVo.getNowstore()));


	}
	
	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.title_right) { // 保存事件处理.
			save();
		}		
	}
	
	private boolean valid() {
		if (StringUtils.isEmpty(txtNum.getStrVal())) {
			ToastUtil.showShortToast(StoreAllocationDetailItemActivity.this, "调拨数量为空！");
			return false;
		}else if(("0").equals(txtNum.getStrVal().trim())){
			ToastUtil.showShortToast(StoreAllocationDetailItemActivity.this, "调拨数量要大于0");
			return false;
		}
		return true;
	}
	
	private void save() {
		if (!valid()) {
			return;
		}
		allocateDetailVo allocateDetailVo = transMode();
		
		saveEvent(allocateDetailVo);

		Intent returngoods = new Intent(StoreAllocationDetailItemActivity.this, StoreAllocationAddActivity.class);

//		Bundle bundle = new Bundle();
//		bundle.putSerializable("returngoods", searchGoodsVo);

		startActivity(returngoods);

	}
	
	private void saveEvent(allocateDetailVo allocateDetailVo) {
		RetailApplication rapp = (RetailApplication) getApplication();

		HashMap<String, List<Object>> map = rapp.getObjectMap();

		if (map.containsKey("allocationAdd")) {

			List<Object> objList = map.get("allocationAdd");

			objList.add(allocateDetailVo);

			map.put("allocationAdd", objList);

			rapp.setObjectMap(map);

		} else {
			HashMap<String, List<Object>> mapNew = new HashMap<String, List<Object>>();

			List<Object> objList = new ArrayList<Object>();

			objList.add(allocateDetailVo);

			mapNew.put("allocationAdd", objList);

			rapp.setObjectMap(mapNew);
		}
	}
	private allocateDetailVo transMode() {

		allocateDetailVo allocateDetailVo = new allocateDetailVo();
		allocateDetailVo.setGoodsId(searchGoodsVo.getGoodsId());
		allocateDetailVo.setGoodsName(searchGoodsVo.getGoodsName());
		allocateDetailVo.setGoodsBarcode(searchGoodsVo.getGoodsBarCode());
		allocateDetailVo.setGoodsPrice(searchGoodsVo.getPurchasePrice());
		allocateDetailVo.setGoodsSum(Integer.valueOf(txtNum.getStrVal()));
		allocateDetailVo.setOperateType("add");
		
		return allocateDetailVo;

	}

}
