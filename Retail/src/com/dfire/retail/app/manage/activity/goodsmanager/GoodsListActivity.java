package com.dfire.retail.app.manage.activity.goodsmanager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.adapter.GoodsListAdapter;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.Gson;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

/**
 * The Class GoodsListActivity.
 * 
 * @author albuscrow
 */
public class GoodsListActivity extends GoodsManagerBaseActivity implements OnClickListener, OnItemClickListener{
	
	private static final int SALESETTING_REQUEST_CODE = 1;

	/** The goods. */
	private ArrayList<GoodsVo> goods;
	
	/** The goods select status. */
	private boolean[] goodsSelectStatus;
	
	/** The goods list view. */
	private ListView goodsListView;
	
	/** The adapter. */
	private GoodsListAdapter adapter;
	
	/** The popup window. */
	private PopupWindow popupWindow;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_manager_list);
		setTitleText(Constants.CHOOSE_GOODS);
		
		goods = (ArrayList<GoodsVo>) getIntent().getSerializableExtra("goods");
		goodsSelectStatus = new boolean[goods.size()];
		selectAll(false);
		
		goodsListView = (ListView) findViewById(R.id.goodsList);
		
		adapter = new GoodsListAdapter(this, goods, goodsSelectStatus);
		addFooter(goodsListView,true);
		goodsListView.setAdapter(adapter);
		goodsListView.setOnItemClickListener(this);
		
		findViewById(R.id.not).setOnClickListener(this);
		findViewById(R.id.all).setOnClickListener(this);
		
		switchToEditMode();
		setBack();
		findViewById(R.id.title_right).setOnClickListener(this);
		setRightBtn(R.drawable.yes, Constants.OPT);
	}

	/**
	 * Select all.
	 * 
	 * @param b
	 *            the b
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
			if (getIds().size() == 0) {
				ToastUtil.showShortToast(this, Constants.CHOOSE);
				return;
			}
			View view = LayoutInflater.from(this).inflate(R.layout.batch_menu, null);
			view.findViewById(R.id.delete).setOnClickListener(this);
			view.findViewById(R.id.setting).setOnClickListener(this);
			view.findViewById(R.id.cancel).setOnClickListener(this);
			if (popupWindow == null) {
				popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
			}
			popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
			WindowManager.LayoutParams lp = getWindow().getAttributes();  
			lp.alpha = 0.5f;
			getWindow().setAttributes(lp);  
			popupWindow.setFocusable(true);
			popupWindow.setTouchable(true);
			popupWindow.setOutsideTouchable(true);
			ColorDrawable dw = new ColorDrawable(0x00);
			popupWindow.setBackgroundDrawable(dw);
			popupWindow.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					WindowManager.LayoutParams lp = GoodsListActivity.this.getWindow().getAttributes();  
					lp.alpha = 1f;
					getWindow().setAttributes(lp);  
				}
			});
			popupWindow.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
			break;
			
		case R.id.delete:
			isDeleteGoods();
			break;
			
		case R.id.setting:
			ArrayList<String> ids = (ArrayList<String>) getIds();
			if (ids.size() == 0) {
				ToastUtil.showShortToast(this, Constants.CHOOSE_SOMETHING);
			}else{
				startActivityForResult(new Intent(this, SaleSettingActivity.class)
				.putExtra(Constants.GOODS, goods)
				.putExtra(Constants.GOODSIDS, ids), SALESETTING_REQUEST_CODE);
			}
			break;
			
		case R.id.cancel:
			popupWindow.dismiss();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == SALESETTING_REQUEST_CODE) {
			adapter.reset();
			popupWindow.dismiss();
		}
	}
	
	
	private void isDeleteGoods() {
		final AlertDialog ad = new AlertDialog(this);
		ad.setMessage(Constants.DELETEP_FOR_MANY_GOODS);
		ad.setPositiveButton(Constants.CONFIRM, new OnClickListener() {

			@Override
			public void onClick(View v) {
				delete();
				ad.dismiss();
			}
		});
		ad.setNegativeButton(Constants.CANCEL, new OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});

	}

	private void delete() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.DELETE_URL);
		final List<String> ids = getIds();
		if (ids.size() == 0) {
			ToastUtil.showShortToast(this, Constants.CHOOSE);
			return;
		}
		try {
			params.setParam(Constants.GOODSIDS, new JSONArray(new Gson().toJson(ids)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		params.setParam(Constants.SHOP_ID, RetailApplication.getShopVo().getShopId());
		
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsListActivity.this)) {
					return;
				}
				ToastUtil.showShortToast(GoodsListActivity.this, Constants.DELETE_SUCCESS);
				for (Iterator iterator = goods.iterator(); iterator.hasNext();) {
					GoodsVo goods = (GoodsVo) iterator.next();
					if(isDelete(goods)){
						iterator.remove();
					}
				}
				runOnUiThread(new Runnable() {
					public void run() {
						adapter.refreshData(goods, new boolean[goods.size()]);
					}
				});
				popupWindow.dismiss();
			}
			
			private boolean isDelete(GoodsVo goods) {
				for (String id : ids) {
					if (id.equals(goods.getGoodsId())) {
						return true;
					}
				}
				return false;
			}

			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(GoodsListActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}).execute();
	}

	private List<String> getIds() {
		List<String> ids = new ArrayList<String>();
		for (int i = 0; i < goods.size(); ++i) {
			if (goodsSelectStatus[i]) {
				ids.add(goods.get(i).getGoodsId());
			}
		}
		return ids;
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CheckBox checkBox = (CheckBox)view.findViewById(R.id.good_checkbox);
		checkBox.setChecked(!checkBox.isChecked());
	}
	
}
