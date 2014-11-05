/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.item.StoreOrderGoodsItem;
import com.dfire.retail.app.manage.vo.allocateDetailVo;

/**
 * @author 李锦运 2014-10-22
 */
public class StoreAllocationDetailItem implements OnClickListener {

	private LayoutInflater inflater;

	private StoreAllocationAddActivity context;

	private View itemView;

	private TextView txt_name,numTxt;

	private ImageView btnAdd, btnDel;

	private Integer firstNum = 1;

	public StoreAllocationDetailItem(StoreAllocationAddActivity context, LayoutInflater inflater) {
		this.context = context;
		this.inflater = inflater;
		this.init();
	}

	private void init() {

		this.itemView = inflater.inflate(R.layout.store_allocation_detail_item, null);
		this.txt_name = (TextView) itemView.findViewById(R.id.txt_name);

		this.btnAdd = (ImageView) itemView.findViewById(R.id.btn_add);
		this.btnDel = (ImageView) itemView.findViewById(R.id.btn_del);
		this.numTxt = (TextView) itemView.findViewById(R.id.txt_nums);

		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				firstNum++;
				numTxt.setText(firstNum + "");
				
				
			}
		});

		btnDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				firstNum--;
//				if (firstNum <= 1) {
//					firstNum = 1;
//				}
//				numTxt.setText(firstNum + "");
//				
//				
				
				if ((firstNum-1)<=0) {
					final AlertDialog alertDialog = new AlertDialog(context);
					alertDialog.setMessage("是否删除该项!");
					alertDialog.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(View v) {
//							((StoreAllocationAddActivity)context).removeView(StoreAllocationDetailItem.this);
							alertDialog.dismiss();
						}
					});
					alertDialog.setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
						}
					});
				}else{
					firstNum--;
				}
					
			numTxt.setText(String.valueOf(firstNum));
				
				
				
				

			}
		});

	}

	public void initWithAppInfo(allocateDetailVo allocateDetailVo) {

		numTxt.setText(allocateDetailVo.getGoodsSum() == 0 ? "0" : allocateDetailVo.getGoodsSum() + "");
		txt_name.setText(allocateDetailVo.getGoodsName());
		firstNum = allocateDetailVo.getGoodsSum();
	}

	public View getItemView() {
		return itemView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	
	
}
