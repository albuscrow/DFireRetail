/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.HashMap;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.item.DateUtil;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.vo.AllocateVo;
import com.dfire.retail.app.manage.vo.ReturnGoodsVo;

/**
 * @author 李锦运 2014-10-23
 */
public class StoreAllocationListItem implements OnClickListener {

	private LayoutInflater inflater;

	private StoreAllocationActivity context;

	private View itemView;

	private TextView goods_no, good_date, txt_status;

	private ImageButton btnAdd, btnDel;

	private Integer firstNum = 1;

	private LinearLayout layout;

	private ReturnGoodsVo returnGoodsVo;

	private RetailApplication application;
	
	private AllocateVo allocateVo;

	public StoreAllocationListItem(StoreAllocationActivity context, LayoutInflater inflater) {
		this.context = context;
		this.inflater = inflater;
		application = (RetailApplication) context.getApplication();
		this.init();
	}

	private void init() {

		this.itemView = inflater.inflate(R.layout.store_return_list_item, null);
		this.goods_no = (TextView) itemView.findViewById(R.id.goods_no);
		this.good_date = (TextView) itemView.findViewById(R.id.good_date);
		this.txt_status = (TextView) itemView.findViewById(R.id.txt_status);

		layout = (LinearLayout) itemView.findViewById(R.id.layout);
		layout.setOnClickListener(this);

	}

	public void initWithAppInfo(AllocateVo allocateVo) {
		this.allocateVo = allocateVo;
		goods_no.setText(allocateVo.getBatchNo() == null ? null : allocateVo.getBatchNo());
		good_date.setText(allocateVo.getSendEndTime() == 0 ? "0" : DateUtil.timeToStrYMD_EN(allocateVo.getSendEndTime())+ "");
		txt_status.setText(allocateVo.getBillStatusName() == null ? null : allocateVo.getBillStatusName());

	}

	public View getItemView() {
		return itemView;
	}

	@Override
	public void onClick(View v) {

		// shopType 1公司（parentId=null 总公司；parentId!=null 分公司） ，2门店
			Intent listDetail = new Intent(context, StoreAllocationListDetailActivity.class);
			context.startActivity(listDetail);

			RetailApplication application = (RetailApplication) context.getApplication();
			HashMap<String, Object> objMap = application.getObjMap();
			objMap.put("allocateVo", allocateVo);
	}
}
