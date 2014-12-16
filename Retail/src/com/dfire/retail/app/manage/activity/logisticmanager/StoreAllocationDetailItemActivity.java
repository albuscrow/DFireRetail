package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.HashMap;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.item.StoreAllocationDetailItem;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.vo.allocateDetailVo;

/**
 * @author ys
 * 2014-11-15
 */
public class StoreAllocationDetailItemActivity extends TitleActivity implements OnClickListener{
	
	private SearchGoodsVo searchGoodsVo;
	
	private TextView lblName,lblCode;
	
	private ItemEditText txtNum;
	
	private String activity;
	
	private HashMap<String, Object> map;
	
	private RetailApplication application;
	
	private allocateDetailVo detailVo;
	
	private Button btn_delete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_allocation_item_detail);
		showBackbtn();
		setTitleText("商品信息");
		initMainUI();
		initData();
	}
	private void initMainUI() {
		this.application = (RetailApplication) getApplication();
		this.map = application.getObjMap();
		this.activity = getIntent().getStringExtra("activity");
		this.lblName = (TextView) this.findViewById(R.id.lblName);
		this.lblCode = (TextView) this.findViewById(R.id.lblCode);
		this.txtNum = (ItemEditText) this.findViewById(R.id.txtNum);
		this.txtNum.initLabel("调拨数量", null, Boolean.TRUE,InputType.TYPE_CLASS_NUMBER);
		this.txtNum.setMaxLength(6);
		this.txtNum.setIsChangeListener(this.getItemChangeListener());
		this.txtNum.setTextColor(Color.parseColor("#0088cc"));
		this.btn_delete = (Button) this.findViewById(R.id.btn_delete);
		
		this.mRight.setOnClickListener(this);
		this.btn_delete.setOnClickListener(this);
	}
	
	private void initData() {
		if (activity.equals("storeAllocationAddActivity")) {
			searchGoodsVo = (SearchGoodsVo) map.get("allocationAdd");
			if (searchGoodsVo!=null) {
				lblName.setText(searchGoodsVo.getGoodsName());
				lblCode.setText(searchGoodsVo.getBarcode());
				txtNum.initData("");
			}
		}else {
			detailVo = (allocateDetailVo) map.get("allocationAdd");
			if (detailVo!=null) {
				lblName.setText(detailVo.getGoodsName());
				lblCode.setText(detailVo.getGoodsBarcode());
				txtNum.initData(detailVo.getGoodsSum()+"");
				btn_delete.setVisibility(View.VISIBLE);
			}
		}
		map.put("allocationAdd", null);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right:
			 // 保存事件处理.
			String numberStr = txtNum.getStrVal();
			if (valid()) {
				Integer number = 0;
				try {
					number = Integer.parseInt(numberStr);
				} catch (NumberFormatException e) {
					number = null;
				}
				if (number==null) {
					new ErrDialog(StoreAllocationDetailItemActivity.this, getResources().getString(R.string.please_print_success_number)).show();
				}else {
					save();
				}
			}
			break;
		case R.id.btn_delete:
			final AlertDialog alertDialog = new AlertDialog(this);
			alertDialog.setMessage(getResources().getString(R.string.isdelete));
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener() {
				@Override
				public void onClick(View v) {
					StoreAllocationDetailItem detailItem = StoreAllocationAddActivity.instance.allocationHashMap.get(detailVo.getGoodsId());
					if (detailItem!=null) {
						map.put("allocationAdd", null);
						StoreAllocationAddActivity.instance.removeView(detailItem);
						StoreAllocationDetailItemActivity.this.finish();
					}
					alertDialog.dismiss();
				}
			});
			alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener() {
				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
				}
			});
			break;
		default:
			break;
		}
	}
	/**判断数量*/
	private boolean valid() {
		if (StringUtils.isEmpty(txtNum.getStrVal())) {
			new ErrDialog(StoreAllocationDetailItemActivity.this, getResources().getString(R.string.allocation_notnull)).show();
			return false;
		}else if(("0").equals(txtNum.getStrVal().trim())){
			new ErrDialog(StoreAllocationDetailItemActivity.this, getResources().getString(R.string.allocation_0)).show();
			return false;
		}
		return true;
	}
	
	private void save() {
		allocateDetailVo allocateDetailVo = transMode();
		map.put("allocationAdd", allocateDetailVo);
		if (activity.equals("storeAllocationAddActivity")) {
			
		StoreAllocationDetailItemActivity.this.finish();
		StoreOrderAddGoodsActivity.instance.finish();
		}else {
			StoreAllocationAddActivity.instance.changeGoodInfo(allocateDetailVo);
			StoreAllocationDetailItemActivity.this.finish();
		}
		
	}
	private allocateDetailVo transMode() {
		Integer sum;
		try {
			sum = Integer.valueOf(txtNum.getStrVal());
		} catch (NumberFormatException e) {
			sum = 0;
		}
		allocateDetailVo allocateDetailVo = new allocateDetailVo();
		if (activity.equals("storeAllocationAddActivity")) {
			allocateDetailVo.setGoodsId(searchGoodsVo.getGoodsId());
			allocateDetailVo.setGoodsName(searchGoodsVo.getGoodsName());
			allocateDetailVo.setGoodsBarcode(searchGoodsVo.getGoodsBarCode());
			allocateDetailVo.setGoodsPrice(searchGoodsVo.getPurchasePrice());
			allocateDetailVo.setOperateType("add");
		}else {
			allocateDetailVo = detailVo;
		}
		allocateDetailVo.setGoodsSum(Integer.valueOf(sum));
		return allocateDetailVo;
	}
}
