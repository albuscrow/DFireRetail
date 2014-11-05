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
import com.dfire.retail.app.manage.vo.ReturnGoodsDetailVo;

/**
 * @author 李锦运 2014-10-22
 */
public class StoreReturnGoodsDetailItem implements OnClickListener {

	private LayoutInflater inflater;

	private StoreReturnGoodsAddActivity context;

	private View itemView;

	private TextView txt_name, txt_reason, txt_price, numTxt;

	private ImageView btnAdd, btnDel;

	private Integer firstNum = 1;
	
	private ReturnGoodsDetailVo returnGoodsDetailVo;

	private int listIndex;
	/**
	 * @return the listIndex
	 */
	public int getListIndex() {
		return listIndex;
	}

	/**
	 * @return the returnGoodsDetailVo
	 */
	public ReturnGoodsDetailVo getReturnGoodsDetailVo() {
		return returnGoodsDetailVo;
	}

	public StoreReturnGoodsDetailItem(StoreReturnGoodsAddActivity context, LayoutInflater inflater) {
		this.context = context;
		this.inflater = inflater;
		this.init();
	}

	private void init() {

		this.itemView = inflater.inflate(R.layout.store_return_detail_item, null);
		this.txt_name = (TextView) itemView.findViewById(R.id.txt_name);
		this.txt_reason = (TextView) itemView.findViewById(R.id.txt_reason);
		this.txt_price = (TextView) itemView.findViewById(R.id.txt_price);

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
				
				if ((firstNum-1)<=0) {
					final AlertDialog alertDialog = new AlertDialog(context);
					alertDialog.setMessage("是否删除该项!");
					alertDialog.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(View v) {
							((StoreReturnGoodsAddActivity)context).removeView(StoreReturnGoodsDetailItem.this);
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

	public void initWithAppInfo(ReturnGoodsDetailVo returnGoodsDetailVo,Integer listIndex) {
		this.listIndex = listIndex;
		this.returnGoodsDetailVo=returnGoodsDetailVo;
		numTxt.setText(returnGoodsDetailVo.getGoodsSum() == 0 ? "0" : returnGoodsDetailVo.getGoodsSum() + "");
		txt_name.setText(returnGoodsDetailVo.getGoodsName());
		firstNum = returnGoodsDetailVo.getGoodsSum();
		txt_reason.setText(returnGoodsDetailVo.getResonName());
		txt_price.setText(returnGoodsDetailVo.getGoodsPrice() + "");
	}

	public View getItemView() {
		return itemView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
