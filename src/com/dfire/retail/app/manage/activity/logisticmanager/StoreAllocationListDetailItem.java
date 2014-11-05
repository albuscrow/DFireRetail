/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.vo.allocateDetailVo;

/**
 * @author 李锦运 2014-10-24
 */
public class StoreAllocationListDetailItem implements OnClickListener {

	private LayoutInflater inflater;

	private StoreAllocationListDetailActivity context;

	private StoreAllocationListDetailActivity context1;

	private View itemView;

	private TextView txt_name,  txt_nums;

	private ImageButton btnAdd, btnDel;

	private Integer firstNum = 1;

	private LinearLayout btn_layout;

	private RetailApplication application;

	public StoreAllocationListDetailItem(StoreAllocationListDetailActivity context, LayoutInflater inflater) {
		this.context = context;
		this.inflater = inflater;
		this.init();
	}


	private void init() {

		this.itemView = inflater.inflate(R.layout.store_allocation_list_detail_item, null);
		this.txt_name = (TextView) itemView.findViewById(R.id.txt_name);

		this.txt_nums = (TextView) itemView.findViewById(R.id.txt_nums);
	}


	public void initWithAppInfo(allocateDetailVo allocateDetailVo) {
		txt_name.setText(allocateDetailVo.getGoodsName() == null ? null : allocateDetailVo.getGoodsName());
		txt_nums.setText(allocateDetailVo.getGoodsSum() == 0 ? "无" : allocateDetailVo.getGoodsSum() + "");

	}


	public View getItemView() {
		return itemView;
	}

	@Override
	public void onClick(View v) {

	}

}
