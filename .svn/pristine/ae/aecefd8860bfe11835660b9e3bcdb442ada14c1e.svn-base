/**
 * 
 */
package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.ItemTextView;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.vo.ReturnGoodsDetailVo;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author 李锦运 2014-10-21
 */
public class StoreReturnGoodsDetaiDeleteActivity extends TitleActivity implements OnClickListener, IItemListListener, IOnItemSelectListener {

	private ReturnGoodsDetailVo returnGoodsDetailVo;

	private ItemTextView lblName;
	private ItemTextView lblPrice;
	private ItemTextView lblCode;
	private ItemEditText txtNum;
	private ItemEditList lsReason;

	private List<DicVo> dicVoList;

	private SpinerPopWindow shopsSpinner;

	private List<String> reasonStrList;

	private int reasonVal;

	private Button btn_delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_return_goods_item_detail);
		setTitleText("商品信息");
		showBackbtn();
		initMainUI();
		initData();
		getReasonList();
		change2saveMode();

		mRight = (ImageButton) findViewById(R.id.title_right);
		mRight.setOnClickListener(this);
	}

	private void initMainUI() {

		this.lblName = (ItemTextView) this.findViewById(R.id.lblName);
		this.lblCode = (ItemTextView) this.findViewById(R.id.lblCode);
		this.lblPrice = (ItemTextView) this.findViewById(R.id.lblPrice);
		this.txtNum = (ItemEditText) this.findViewById(R.id.txtNum);
		this.lsReason = (ItemEditList) this.findViewById(R.id.lsReason);

		this.lblName.initLabel("商品名称", null);
		this.lblCode.initLabel("商品条码", null);
		this.lblPrice.initLabel("进货价(元)", null);
		this.txtNum.initLabel("退货数量", null, Boolean.TRUE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		this.lsReason.initLabel("退货原因", null, Boolean.TRUE, this);

		this.btn_delete = (Button) this.findViewById(R.id.btn_delete);
		this.btn_delete.setVisibility(View.VISIBLE);
		this.btn_delete.setOnClickListener(this);
	}

	private void initData() {

		// RetailApplication rapp = (RetailApplication) getApplication();
		//
		// HashMap<String, Object> map = rapp.getObjMap();
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();

		returnGoodsDetailVo = (ReturnGoodsDetailVo) bundle.getSerializable("returnGoodsDetailVo");

		lblName.initData(returnGoodsDetailVo.getGoodsName(), returnGoodsDetailVo.getGoodsId());
		lblCode.initData(returnGoodsDetailVo.getGoodsBarcode(), returnGoodsDetailVo.getGoodsBarcode());
		lblPrice.initData(String.format("%f", returnGoodsDetailVo.getGoodsPrice()), String.format("%f", returnGoodsDetailVo.getGoodsPrice()));
		txtNum.initData(String.format("%d", returnGoodsDetailVo.getGoodsSum()));
		lsReason.initData(returnGoodsDetailVo.getResonName(), returnGoodsDetailVo.getResonName());
		// Intent intent = this.getIntent();
		// Bundle bundle = intent.getExtras();
		// if (bundle != null) {
		// String reason = bundle.getString("reason");
		// reasonVal = bundle.getInt("reasonVal");
		// if (StringUtils.isEmpty(reason)) {
		// lsReason.initData("", "");
		// } else {
		// lsReason.initData(reason, reason);
		// }
		// }

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.title_right) { // 保存事件处理.
			save();
		} else if (v.getId() == R.id.btn_delete) {
			delete();
		}

	}

	private void delete() {

		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.PURCHASE_SAVE);
		parameters.setParam("returnGoodId", returnGoodsDetailVo.getGoodsId());
		parameters.setParam("operateType", "del");
		parameters.setParam("lastVer", ((RetailApplication) getApplication()).getShopVo().getLastVer());

		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				JsonUtil ju = new JsonUtil(str);
				Intent intent = new Intent(StoreReturnGoodsDetaiDeleteActivity.this, StoreReturnGoodsListDetailActivity.class);
				startActivity(intent);
				finish();

			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();

	}

	/**
	 * @return
	 */
	private boolean valid() {
		if (StringUtils.isEmpty(txtNum.getStrVal())) {
			ToastUtil.showShortToast(StoreReturnGoodsDetaiDeleteActivity.this, "退货数量为空！");
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dfire.retail.app.common.item.listener.IItemListListener#onItemListClick
	 * (com.dfire.retail.app.common.item.ItemEditList)
	 */
	@Override
	public void onItemListClick(ItemEditList obj) {
		if (obj.getId() == R.id.lsReason) {

			// if (dicVoList == null || dicVoList.size() == 0) {
			// ToastUtil.showShortToast(StoreReturnGoodsDetailItemActivity.this,
			// "请先添加退货！");
			// return;
			// }
			// shopsSpinner.setWidth(lsReason.getWidth());
			// shopsSpinner.showAsDropDown(lsReason);
			// // 页面跳转从此添加 代码.
			Intent reason = new Intent(StoreReturnGoodsDetaiDeleteActivity.this, StoreReturnGoodsReasonActivity.class);
			if (lsReason.getStrVal() != null && !lsReason.getStrVal().equals("")) {

				Bundle bundle = new Bundle();
				bundle.putString("txt", lsReason.getStrVal());
				reason.putExtras(bundle);

			}
			startActivity(reason);
			finish();
		}
	}

	/**
	 * 
	 */
	private void save() {
		if (!valid()) {
			return;
		}
		ReturnGoodsDetailVo returnGoodsDetailVo = transMode();
		saveEvent(returnGoodsDetailVo);

		Intent returngoods = new Intent(StoreReturnGoodsDetaiDeleteActivity.this, StoreReturnGoodsAddActivity.class);

		Bundle bundle = new Bundle();
		// bundle.putSerializable("returngoods", searchGoodsVo);

		startActivity(returngoods);
		finish();
	}

	@Override
	public void onItemClick(int pos) {
		// DicVo vo = dicVoList.get(pos);
		// lsReason.changeData(vo.getDicName(), vo.getDicValue());

	}

	private ReturnGoodsDetailVo transMode() {

		ReturnGoodsDetailVo returnGoodsDetailVo = new ReturnGoodsDetailVo();
		// returnGoodsDetailVo.setGoodsId(searchGoodsVo.getGoodsId());
		// returnGoodsDetailVo.setGoodsName(searchGoodsVo.getGoodsName());
		// returnGoodsDetailVo.setGoodsBarcode(searchGoodsVo.getBarcode());
		// returnGoodsDetailVo.setGoodsPrice(searchGoodsVo.getPurchasePrice());
		returnGoodsDetailVo.setOperateType("add");
		if (!StringUtils.isEmpty(txtNum.getStrVal())) {
			Integer val = new Integer(txtNum.getStrVal());
			returnGoodsDetailVo.setGoodsSum(val);
		}
		returnGoodsDetailVo.setResonName(lsReason.getStrVal());
		returnGoodsDetailVo.setResonVal(reasonVal);
		return returnGoodsDetailVo;

	}

	/**
	 * @param returnGoodsDetailVo
	 */
	private void saveEvent(ReturnGoodsDetailVo returnGoodsDetailVo) {
		RetailApplication rapp = (RetailApplication) getApplication();

		HashMap<String, List<Object>> map = rapp.getObjectMap();

		if (map.containsKey("returnGoods")) {

			List<Object> objList = map.get("returnGoods");

			objList.add(returnGoodsDetailVo);

			map.put("returnGoods", objList);

			rapp.setObjectMap(map);

		} else {
			HashMap<String, List<Object>> mapNew = new HashMap<String, List<Object>>();

			List<Object> objList = new ArrayList<Object>();

			objList.add(returnGoodsDetailVo);

			mapNew.put("returnGoods", objList);

			rapp.setObjectMap(mapNew);
		}
	}

	// ////////////////////////////////////////////////////////////
	public void getReasonList() {
		// 传递请求参数
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.RETURNGOODS_REASONLIST);
		// parameters.setParam("shopId",
		// RetailApplication.getShopVo().getShopId());
		// parameters.setParam("currentPage", "1");

		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {

				// JsonUtil ju = new JsonUtil(str);
				//
				// dicVoList = (List<DicVo>) ju.get("returnResonList", new
				// TypeToken<List<DicVo>>(){}.getType());
				//
				// String a=dicVoList.get(0).getDicName();

				JsonObject object = new JsonParser().parse(str).getAsJsonObject();
				dicVoList = new ArrayList<DicVo>();

				dicVoList.clear();
				dicVoList = getJson(str);
				if (dicVoList != null && dicVoList.size() > 0) {
					// lsReason.initData(dicVoList.get(0).getDicName(),
					// dicVoList.get(0).getDicValue());
					reasonStrList = new ArrayList<String>();
					for (DicVo dic : dicVoList) {
						reasonStrList.add(dic.getName());
					}
					shopsSpinner = new SpinerPopWindow(StoreReturnGoodsDetaiDeleteActivity.this);
					shopsSpinner.refreshData(reasonStrList, 0);
					shopsSpinner.setItemListener(StoreReturnGoodsDetaiDeleteActivity.this);
				}

			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();
	}

	public static List<DicVo> getJson(String json) {

		List<DicVo> list = new ArrayList<DicVo>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj != null && jsonObj.length() > 0) {
				JSONArray jsonArray = jsonObj.getJSONArray("returnResonList");
				for (int i = 0; i < jsonArray.length(); i++) {
					DicVo dicVo = new DicVo();
					JSONObject js = jsonArray.getJSONObject(i);
					dicVo.setName(js.getString("name"));
					dicVo.setVal(Integer.valueOf(js.getString("val")));
					// supplyManagevo.setName(js.getString("name"));
					// supplyManagevo.setId(js.getString("id"));
					list.add(dicVo);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
