package com.dfire.retail.app.manage.common;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.vo.supplyManageVo;
import com.dfire.retail.app.manage.wheel.widget.WheelView;
import com.dfire.retail.app.manage.wheel.widget.adapters.AbstractWheelTextAdapter;
/**
 * 供应商
 * @author ys
 *
 */
@SuppressLint("NewApi")
public class SupplyDialog extends Dialog{

	private Context mContext;
	private CardTypeAdapter mAdapter;
	private WheelView mWheelView;
	private Button mConfirmButton, mCancelButton;
	private TextView mTitle;
	private List<supplyManageVo> supplyManageVos;

	public SupplyDialog(Context context,List<supplyManageVo> supplyManageVos) {
		super(context, R.style.dialog);
		this.supplyManageVos = supplyManageVos;
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_type_dialog);
		
		mTitle = (TextView) findViewById(R.id.card_type_title);
		mConfirmButton = (Button) findViewById(R.id.card_type_confirm);
		mCancelButton = (Button) findViewById(R.id.card_type_cancel);
		this.setCanceledOnTouchOutside(true);
		Window window = this.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		Point p = new Point();
		window.getWindowManager().getDefaultDisplay().getSize(p);
		params.width = p.x;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);

		mWheelView = (WheelView) findViewById(R.id.card_type_wheel);
		mWheelView.setVisibleItems(4); // Number of items
		mWheelView.setWheelBackground(android.R.color.transparent);
		mWheelView.setWheelForeground(android.R.color.transparent);
		mWheelView.setShadowColor(0x00000000, 0x00000000, 0x00000000);
		mAdapter = new CardTypeAdapter(mContext,supplyManageVos);
		mWheelView.setViewAdapter(mAdapter);
		mWheelView.setCurrentItem(2);
	}

	public Button getConfirmButton() {
		return mConfirmButton;

	}

	public Button getCancelButton() {
		return mCancelButton;

	}
	/**
	 * 显示已选择的项目名称
	 * 
	 * @param Id
	 */
	public void updateType(String TypeId) {
		if (TypeId!=null&&!TypeId.equals("")) {
			if (supplyManageVos.size() > 0) {
				for (int i = 0; i < supplyManageVos.size(); i++) {
					if (supplyManageVos.get(i).getId().equals(TypeId)) {
						mWheelView.setCurrentItem(i);
						break;
					}
				}
			}
		} else {
			mWheelView.setCurrentItem(0);
		}
	}
	public int getCurrentData() {
		return mWheelView.getCurrentItem();
//		return mAdapter.getItemText(mWheelView.getCurrentItem()).toString();

	}
	public TextView getmTitle() {
		return mTitle;
	}

	public void setmTitle(TextView mTitle) {
		this.mTitle = mTitle;
	}

	private class CardTypeAdapter extends AbstractWheelTextAdapter {
		private List<supplyManageVo> supplyManageVos;
		protected CardTypeAdapter(Context context,List<supplyManageVo> supplyManageVos) {
			super(context, R.layout.card_type_wheel, NO_RESOURCE);
			setItemTextResource(R.id.card_type_text);
			this.supplyManageVos = supplyManageVos;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return supplyManageVos.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return supplyManageVos.get(index).getName();
		}
	}

}
