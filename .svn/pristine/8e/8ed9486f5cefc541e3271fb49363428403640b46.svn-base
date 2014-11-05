/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.vo.ReturnGoodsDetailVo;

/**
 * @author 李锦运 2014-10-24
 */
public class StoreReturnGoodsListDetailItem implements OnClickListener {

	private LayoutInflater inflater;

	private StoreReturnGoodsListDetailActivity context;

	private StoreReturnGoodsListOfCompanyDetailActivity context1;

	private View itemView;

	private TextView txt_name, txt_reason, txt_price, txt_nums;

	private ImageButton btnAdd, btnDel;

	private Integer firstNum = 1;

	private LinearLayout btn_layout;

	private RetailApplication application;

	private ReturnGoodsDetailVo returnGoodsDetailVo;

	private LinearLayout layout;

	public StoreReturnGoodsListDetailItem(StoreReturnGoodsListDetailActivity context, LayoutInflater inflater) {
		this.context = context;
		this.inflater = inflater;
		this.init();
	}

	public StoreReturnGoodsListDetailItem(StoreReturnGoodsListOfCompanyDetailActivity context1, LayoutInflater inflater) {
		this.context1 = context1;
		this.inflater = inflater;
		application = (RetailApplication) context1.getApplication();

		this.init2();
	}

	private void init() {

		this.itemView = inflater.inflate(R.layout.store_return_list_detail_item, null);
		this.txt_name = (TextView) itemView.findViewById(R.id.txt_name);
		this.txt_reason = (TextView) itemView.findViewById(R.id.txt_reason);
		this.txt_price = (TextView) itemView.findViewById(R.id.txt_price);

		this.txt_nums = (TextView) itemView.findViewById(R.id.txt_nums);
		this.layout = (LinearLayout) itemView.findViewById(R.id.layout);
		layout.setOnClickListener(this);
	}

	private void init2() {

		this.itemView = inflater.inflate(R.layout.store_return_list_detail2_item, null);
		this.txt_name = (TextView) itemView.findViewById(R.id.txt_name);
		this.txt_reason = (TextView) itemView.findViewById(R.id.txt_reason);

		this.btnAdd = (ImageButton) itemView.findViewById(R.id.btn_add);
		this.btnDel = (ImageButton) itemView.findViewById(R.id.btn_del);
		this.txt_nums = (TextView) itemView.findViewById(R.id.txt_nums);
		this.layout = (LinearLayout) itemView.findViewById(R.id.layout);
		layout.setOnClickListener(this);
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				firstNum++;
				txt_nums.setText(firstNum + "");
			}
		});

		btnDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				firstNum--;
				if (firstNum <= 1) {
					firstNum = 1;
				}
				txt_nums.setText(firstNum + "");
			}
		});

	}

	public void initWithAppInfo(ReturnGoodsDetailVo returnGoodsDetailVo) {
		txt_name.setText(returnGoodsDetailVo.getGoodsName() == null ? null : returnGoodsDetailVo.getGoodsName());
		txt_reason.setText(returnGoodsDetailVo.getResonName() == null ? null : returnGoodsDetailVo.getResonName());
		txt_price.setText(returnGoodsDetailVo.getGoodsPrice() == null ? "无" : returnGoodsDetailVo.getGoodsPrice() + "");
		txt_nums.setText(returnGoodsDetailVo.getGoodsSum() == 0 ? "无" : returnGoodsDetailVo.getGoodsSum() + "");
		this.returnGoodsDetailVo = returnGoodsDetailVo;
	}

	public void initWithAppInfo2(ReturnGoodsDetailVo returnGoodsDetailVo) {
		txt_name.setText(returnGoodsDetailVo.getGoodsName() == null ? null : returnGoodsDetailVo.getGoodsName());
		txt_reason.setText(returnGoodsDetailVo.getResonName() == null ? null : returnGoodsDetailVo.getResonName());
		txt_nums.setText(returnGoodsDetailVo.getGoodsSum() == 0 ? "无" : returnGoodsDetailVo.getGoodsSum() + "");
		this.returnGoodsDetailVo = returnGoodsDetailVo;
	}

	public View getItemView() {
		return itemView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.layout) {
			Intent delete = new Intent(context, StoreReturnGoodsDetaiDeleteActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("returnGoodsDetailVo", returnGoodsDetailVo);
			delete.putExtras(bundle);
			context.startActivity(delete);

		}
	}

}
