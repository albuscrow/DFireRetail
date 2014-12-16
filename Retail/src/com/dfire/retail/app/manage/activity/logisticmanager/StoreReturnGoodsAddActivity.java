package com.dfire.retail.app.manage.activity.logisticmanager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.item.StoreReturnGoodsDetailItem;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.common.SupplyDialog;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.ReturnGoodsDetailBo;
import com.dfire.retail.app.manage.data.bo.ReturnGoodsSaveBo;
import com.dfire.retail.app.manage.data.bo.SupplyInfoListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.vo.ReturnGoodsDetailVo;
import com.dfire.retail.app.manage.vo.ReturnGoodsVo;
import com.dfire.retail.app.manage.vo.supplyManageVo;
import com.google.gson.Gson;

/**
 * 物流管理 -- 退货--添加
 * @author ys
 */
public class StoreReturnGoodsAddActivity extends Activity implements OnClickListener {
	public static StoreReturnGoodsAddActivity instance = null; 
	private ImageButton title_left,title_right,title_return;
	private RelativeLayout add_layout,return_layout;
	private TextView total_num, total_price,supplyName_tx,stockInNo_tx;
	private LinearLayout store_collect_add_layout;
	private LayoutInflater inflater;
	private List<ReturnGoodsDetailVo> list = new ArrayList<ReturnGoodsDetailVo>();
	private RelativeLayout total_layout;
	private String shopId;
	private SupplyDialog supplyDialog;
	private ReturnGoodsDetailVo returnGoodsDetailVo;
	public HashMap<String, StoreReturnGoodsDetailItem> storeReturnMap = new HashMap<String, StoreReturnGoodsDetailItem>();
	private List<supplyManageVo> supplyManageVos;
	private String supplyId;//供应商id  
	private String lastVer = "";//版本号
	private String returnGoodsId;
	private String operation;//操作状态
	private ReturnGoodsVo returnGoodsVo;
	private String operateType = "add";
	private LinearLayout btn_layout,goodsTotalPrice_view;
	private Button btn_confirm,btn_refuse,btn_del;
	private View view,view0;
	private String isPrice = "false";//是否显示进货价
	private TextView return_add_text;
	private ImageView return_add_iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_return_goods_add);
		instance = this;
		this.inflater = LayoutInflater.from(this);
		findView();
	}

	public void findView() {
		shopId = getIntent().getStringExtra("shopId");
		supplyManageVos=new ArrayList<supplyManageVo>();
		title_right = (ImageButton) findViewById(R.id.title_right);
		title_right.setOnClickListener(this);
		title_left = (ImageButton) findViewById(R.id.title_left);
		title_left.setOnClickListener(this);
		supplyName_tx=(TextView)findViewById(R.id.supplyName_tx);
		add_layout = (RelativeLayout) findViewById(R.id.add_layout);
		add_layout.setOnClickListener(this);
		store_collect_add_layout = (LinearLayout) findViewById(R.id.store_collect_add_layout);
		return_layout = (RelativeLayout)findViewById(R.id.return_layout);
		stockInNo_tx = (TextView)findViewById(R.id.stockInNo_tx);
		total_num = (TextView) findViewById(R.id.total_num);
		total_price = (TextView) findViewById(R.id.total_price);
		total_layout = (RelativeLayout) findViewById(R.id.total_layout);
		title_return = (ImageButton) findViewById(R.id.title_return);
		btn_layout = (LinearLayout) findViewById(R.id.btn_layout);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_refuse = (Button) findViewById(R.id.btn_refuse);
		btn_del = (Button) findViewById(R.id.btn_del);
		goodsTotalPrice_view = (LinearLayout) findViewById(R.id.goodsTotalPrice_view);
		view = (View) findViewById(R.id.view);
		view0 = (View) findViewById(R.id.view0);
		return_add_text = (TextView) findViewById(R.id.return_add_text);
		TextPaint tp = return_add_text.getPaint(); 
		tp.setFakeBoldText(true); 
		return_add_iv = (ImageView) findViewById(R.id.return_add_iv);
		this.supplyList();
		supplyDialog = new SupplyDialog(StoreReturnGoodsAddActivity.this,supplyManageVos);//状态
		if (RetailApplication.getShopVo().getType() == ShopVo.MENDIAN||RetailApplication.getShopVo().getType() == ShopVo.FENGBU) {
			isPrice = "false";
			goodsTotalPrice_view.setVisibility(View.INVISIBLE);
		}else if (RetailApplication.getShopVo().getType() == ShopVo.DANDIAN) {
			isPrice = "true";
			goodsTotalPrice_view.setVisibility(View.VISIBLE);
		}else if (RetailApplication.getShopVo().getType() == ShopVo.ZHONGBU){
			isPrice = "true";
			goodsTotalPrice_view.setVisibility(View.VISIBLE);
		}
		//获取传来的值
		operation = getIntent().getStringExtra("operation");
		if (!operation.equals("add")) {
			returnGoodsVo = (ReturnGoodsVo) getIntent().getSerializableExtra("returnGoodsVo");
			returnGoodsId = returnGoodsVo.getReturnGoodsId();
			if (operation.equals("edit")) {
				operateType = operation;
				supplyName_tx.setOnClickListener(this);
				return_add_iv.setOnClickListener(this);
				btn_del.setOnClickListener(this);
				btn_del.setVisibility(View.VISIBLE);
			}else if (operation.equals("see")) {
				title_return.setVisibility(View.VISIBLE);
				title_return.setOnClickListener(this);
				title_right.setVisibility(View.GONE);
				title_left.setVisibility(View.GONE);
				add_layout.setVisibility(View.GONE);
				view.setVisibility(View.GONE);
				return_add_iv.setVisibility(View.GONE);
				supplyName_tx.setCompoundDrawables(null, null, null, null);
				supplyName_tx.setTextColor(Color.parseColor("#666666"));
			}else if (operation.equals("receiptOrrefuse")) {
				supplyName_tx.setCompoundDrawables(null, null, null, null);
				supplyName_tx.setTextColor(Color.parseColor("#666666"));
				title_return.setVisibility(View.VISIBLE);
				title_return.setOnClickListener(this);
				btn_layout.setVisibility(View.VISIBLE);
				title_right.setVisibility(View.GONE);
				title_left.setVisibility(View.GONE);
				btn_confirm.setOnClickListener(this);
				btn_refuse.setOnClickListener(this);
				return_add_iv.setOnClickListener(this);
			}
			findReturnInfoById();
		}else {
			return_layout.setVisibility(View.GONE);
			operateType = operation;
			total_layout.setVisibility(View.GONE);
			view0.setVisibility(View.GONE);
			supplyName_tx.setOnClickListener(this);
			return_add_iv.setOnClickListener(this);
		}
	}
	/**
	 * 回调返回 数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		RetailApplication application = (RetailApplication) getApplication();
		HashMap<String, Object> map = application.getObjMap();
		returnGoodsDetailVo = (ReturnGoodsDetailVo)map.get("returnGoodsReason");
		if (returnGoodsDetailVo!=null) {
			//判断 map 里面是否存有 该key 对应的value
			if (storeReturnMap.containsKey(returnGoodsDetailVo.getGoodsId())) {
				StoreReturnGoodsDetailItem goodsItem = storeReturnMap.get(returnGoodsDetailVo.getGoodsId());
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getGoodsId().equals(goodsItem.getReturnGoodsDetailVo().getGoodsId())) {
						if (list.get(i).getOperateType().equals("del")) {
							list.get(i).setOperateType("edit");
							store_collect_add_layout.addView(goodsItem.getItemView());
						}
						Integer sum = list.get(i).getGoodsSum();//之前的数量
						Integer newSum = returnGoodsDetailVo.getGoodsSum();
						goodsItem.getNumTxt().setText(String.valueOf(sum+newSum));//
						goodsItem.getTxt_reason().setText(String.valueOf(returnGoodsDetailVo.getResonName()));
						list.get(i).setGoodsSum(sum+newSum);//设置list里面的数量
						list.get(i).setGoodsPrice(returnGoodsDetailVo.getGoodsPrice());
						BigDecimal price = list.get(i).getGoodsPrice();
						list.get(i).setGoodsTotalPrice(price.multiply(new BigDecimal(sum+newSum)));//合计
					}
				}
			}else {
				total_layout.setVisibility(View.VISIBLE);
				//添加新的view 并且加入list里面
				returnGoodsDetailVo.setOperateType("add"); //操作类型
				list.add(returnGoodsDetailVo);
				StoreReturnGoodsDetailItem goodsItem = new StoreReturnGoodsDetailItem(this,inflater,false,isPrice);
				if (isPrice.equals("false")) {
					goodsItem.getPriceLayout().setVisibility(View.INVISIBLE);//隐藏进货价
				}
				goodsItem.initWithAppInfo(returnGoodsDetailVo);
				store_collect_add_layout.addView(goodsItem.getItemView());
				storeReturnMap.put(returnGoodsDetailVo.getGoodsId(), goodsItem);//goodsItem 存入map(新view)
			}
		}
		map.put("returnGoodsReason", null);
	}
	@Override
	protected void onResume() {
		super.onResume();

	}
	/**
	 * 提交 对应设值
	 */
	private boolean initData(){
		if (list!=null&&storeReturnMap!=null&&storeReturnMap.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				StoreReturnGoodsDetailItem goodsItem = storeReturnMap.get(list.get(i).getGoodsId());
				Integer num = 0;
				try {
					if (list.get(i).getOperateType().equals("del")) {
						num = 0;
					}else {
						num = Integer.parseInt(goodsItem.getNumTxt().getText().toString());
					}
				} catch (NumberFormatException e) {
					num = 0;
				}  
				list.get(i).setGoodsTotalPrice(list.get(i).getGoodsPrice().multiply(new BigDecimal(num)));//设置总价
				list.get(i).setGoodsSum(num);//给list 里面的每项设置数量
			}
		}
		return true;
	}
	
	/**
	 * 移除item
	 */
	public void removeView(StoreReturnGoodsDetailItem goodsItem){
		store_collect_add_layout.removeView(goodsItem.getItemView());
		String goodsId = goodsItem.getReturnGoodsDetailVo().getGoodsId();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getGoodsId().equals(goodsId)) {
					if (list.get(i).getReturnGoodsDetailId()!=null&&!list.get(i).getReturnGoodsDetailId().equals("")) {
						list.get(i).setOperateType("del");
					}else {
						storeReturnMap.remove(list.get(i).getGoodsId());
						list.remove(i);
					}
				}
			}
		}
		changePriceNumber(null);
	}
	
	/**
	 * 动态监听editText 修改总价格 和数量
	 */
	public void changePriceNumber(StoreReturnGoodsDetailItem goodsItem){
		if (goodsItem==null) {
			initData();
		}
		if (list.size()>0) {
			Integer count = 0;
			BigDecimal countPrice = new BigDecimal("0");
			for (int i = 0; i < list.size(); i++) {
				count = count+list.get(i).getGoodsSum();
				countPrice = countPrice.add(list.get(i).getGoodsPrice().multiply(new BigDecimal(list.get(i).getGoodsSum())));//设置总价
			}
			total_num.setText(""+count);
			total_price.setText(""+countPrice);
		}else {
			total_num.setText("0");
			total_price.setText("0.00");
		}
	}
	/**
	 * 查看详情的时候 修改删除该商品
	 */
	public void changeGoodInfo(ReturnGoodsDetailVo detailVo){
		if (storeReturnMap.containsKey(detailVo.getGoodsId())) {
			StoreReturnGoodsDetailItem goodsItem = storeReturnMap.get(detailVo.getGoodsId());
			if (detailVo.getGoodsSum()==0) {
				removeView(goodsItem);
			}else {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getGoodsId().equals(detailVo.getGoodsId())) {
						goodsItem.getNumTxt().setText(String.valueOf(detailVo.getGoodsSum()));//设置editText
						goodsItem.getTxt_reason().setText(String.valueOf(detailVo.getResonName()));
						BigDecimal price = list.get(i).getGoodsPrice();
						list.get(i).setGoodsSum(detailVo.getGoodsSum());//设置list里面的数量
						list.get(i).setGoodsTotalPrice(price.multiply(new BigDecimal(detailVo.getGoodsSum())));//合计
						list.get(i).setResonName(detailVo.getResonName());
						list.get(i).setResonVal(detailVo.getResonVal());
					}
				}
			}
			changePriceNumber(null);
		}
	}
	/**
	 * 弹出状态
	 */
	private void pushStatus(){
		this.supplyDialog.show();
		this.supplyDialog.getmTitle().setText("供应商");
		this.supplyDialog.updateType(supplyId);
		this.supplyDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				supplyDialog.dismiss();
				Integer index = supplyDialog.getCurrentData();
				if (supplyManageVos!=null&&supplyManageVos.size()>0) {
					supplyManageVo supplyManageVo = supplyManageVos.get(index);
					if (!supplyName_tx.getText().toString().equals("请选择")) {
						if (!supplyName_tx.getText().toString().equals(supplyManageVo.getName())) {
							/**更换供应商清空选择的商品*/
							if (operation.equals("edit")) {/**修改*/ 
								for (int i = 0; i < list.size(); i++) {
									list.get(i).setOperateType("del");
									list.get(i).setGoodsSum(0);
								}
							}else {
								list.clear();
								storeReturnMap.clear();
							}
							store_collect_add_layout.removeAllViews();
							total_num.setText("0");
							total_price.setText("0.00");
						}
					}
					supplyName_tx.setText(supplyManageVo.getName());
					supplyId = supplyManageVo.getId();
					/**如果供应商是总部  不显示进货价  如果是单店 现实进货价 如果是其他供应商 现实进货价*/
					if (RetailApplication.getShopVo().getType() == ShopVo.DANDIAN) {
						isPrice = "true";
						goodsTotalPrice_view.setVisibility(View.VISIBLE);
					}else {
						if (supplyName_tx.getText().toString().equals(supplyManageVos.get(0).getName())) {
							isPrice = "false";
							goodsTotalPrice_view.setVisibility(View.INVISIBLE);
						}else {
							isPrice = "true";
							goodsTotalPrice_view.setVisibility(View.VISIBLE);
						}
					}
				}
			}
		});
		this.supplyDialog.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				supplyDialog.dismiss();
			}
		});
	}
	/**
	 * 供应商列表
	 */
	private void supplyList(){
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SUPPLY_INFO_MANAGE_LIST);
		if (RetailApplication.getEntityModel()==1) {
			params.setParam("showEntityFlg", "0");
		}else {
			params.setParam("showEntityFlg", "1");
		}
		params.setParam("isDividePage", "0");
		new AsyncHttpPost(this, params, SupplyInfoListBo.class,false, new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				SupplyInfoListBo bo = (SupplyInfoListBo) oj;
				if (bo!=null) {
				List<supplyManageVo> vos = bo.getSupplyManageList();
					if (vos!=null && vos.size()>0) {
						supplyManageVos.clear();
						supplyManageVos.addAll(vos);
					}
				}
			}
			@Override
			public void onFail(Exception e) {
			}
		}).execute();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			if (initData()) {
				saveReturnGoodsList();
			}
			break;
		case R.id.add_layout:
			addGoods();
			break;
		case R.id.supplyName_tx:
			pushStatus();
			break;
		case R.id.btn_confirm:
			if (initData()) {
				operateType = "receipt";
				saveReturnGoodsList();
			}
			break;
		case R.id.btn_refuse:
			if (initData()) {
				operateType = "refuse";
				saveReturnGoodsList();
			}
			break;
		case R.id.title_return:	
			finish();
			break;
		case R.id.return_add_iv:
			addGoods();
			break;
		case R.id.btn_del:
			final AlertDialog alertDialog = new AlertDialog(this);
			alertDialog.setMessage(getResources().getString(R.string.isdelete_return));
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener() {
				@Override
				public void onClick(View v) {
					operateType = "del";
					saveReturnGoodsList();
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
		}
	}
	/**
	 * 添加
	 */
	private void addGoods(){
		if (initData()) {
			if (supplyManageVos!=null&&supplyManageVos.size()>0) {
				if (!supplyName_tx.getText().toString().equals("请选择")) {
					Intent addGoods = new Intent(StoreReturnGoodsAddActivity.this, StoreOrderAddGoodsActivity.class);
					addGoods.putExtra("flag", "returnGoodsReason");
					addGoods.putExtra("isPrice",isPrice);
					addGoods.putExtra("shopId", shopId);
					startActivityForResult(addGoods, 100);
				}else {
					new ErrDialog(StoreReturnGoodsAddActivity.this, getResources().getString(R.string.LM_MSG_000006)).show();
				}
			}else {
				new ErrDialog(StoreReturnGoodsAddActivity.this, getResources().getString(R.string.LM_MSG_000006)).show();
			}
		}
	}
	
	/**
	 * 门店：  添加 修改 删除      总部：确认收货  拒绝收货   选择的供应商为总部时： 需要总部确认    非总部： 状态为 已 退货
	 * 操作类别： add：添加 edit：修改 receipt：确认收货   del：删除   refuse：拒绝收货
	 */
	private void saveReturnGoodsList() {
		if (list!=null&&list.size()>0) {
			RequestParameter parameters = new RequestParameter(true);
			parameters.setUrl(Constants.PURCHASE_SAVE);
			parameters.setParam("returnGoodsId", returnGoodsId);
			parameters.setParam("operateType", operateType);
			parameters.setParam("lastVer", lastVer);
			if (operateType.equals("receipt")||operateType.equals("add")||operateType.equals("edit")) {
				parameters.setParam("shopId", shopId);
				parameters.setParam("supplyId", supplyId);
				try {
					parameters.setParam("returnGoodsDetailList", new JSONArray(new Gson().toJson(list)));
				} catch (JSONException e1) {
					parameters.setParam("returnGoodsDetailList",  null);
				}
			}
			new AsyncHttpPost(this, parameters, ReturnGoodsSaveBo.class, new RequestCallback() {
				
				@Override
				public void onSuccess(Object oj) {
					ReturnGoodsSaveBo bo = (ReturnGoodsSaveBo)oj;
					if (bo!=null) {
						if (StringUtils.isEquals(operateType, "edit")) {
							StoreReturnGoodsAddActivity.this.finish();
						}else {
							StoreReturnGoodsAddActivity.this.finish();
							if (StringUtils.isEquals(operateType, "add")) {
								StoreReturnGoodsActivity.instance.pullDig(bo.getReturnGoodsNo());
							} 
							StoreReturnGoodsActivity.instance.reFreshing();
						}
					}
				}
				@Override
				public void onFail(Exception e) {
				}
			}).execute();
		}else {
			new ErrDialog(StoreReturnGoodsAddActivity.this, getResources().getString(R.string.please_select_return_goods)).show();
		}
	}
	/**
	 * 根据id 查询  详情 
	 */
	private void findReturnInfoById() {

		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.RETURNGOODS_DETAIL);
		parameters.setParam("returnGoodsId", returnGoodsId);
		new AsyncHttpPost(this, parameters, ReturnGoodsDetailBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				ReturnGoodsDetailBo bo = (ReturnGoodsDetailBo)oj;
				if (bo!=null) {
					String returnGoodsNo = bo.getReturnGoodsNo();
					String supplyName = bo.getSupplyName();
					Integer goodsTotalSum = bo.getGoodsTotalSum();
					BigDecimal goodsTotalPrice = bo.getGoodsTotalPrice();
					lastVer = bo.getLastVer();
					supplyId = bo.getSupplyId();//供应商ID
					
					list = bo.getReturnGoodsDetailList();
					supplyName_tx.setText(supplyName);
					stockInNo_tx.setText(returnGoodsNo);
					total_num.setText(String.valueOf(goodsTotalSum));
					total_price.setText(String.format("%.2f", goodsTotalPrice));
					if (RetailApplication.getShopVo().getType() == ShopVo.ZHONGBU||RetailApplication.getShopVo().getType() == ShopVo.DANDIAN) {
						isPrice = "true";
						goodsTotalPrice_view.setVisibility(View.VISIBLE);
					}else {
						if (supplyManageVos.size()>0) {
							if (supplyName.equals(supplyManageVos.get(0).getName())) {
								isPrice = "false";
								goodsTotalPrice_view.setVisibility(View.INVISIBLE);
							}else {
								isPrice = "true";
								goodsTotalPrice_view.setVisibility(View.VISIBLE);
							}
						}
					}

					if (list != null && list.size() != 0) {
						for (int i = 0; i < list.size(); ++i) {
							StoreReturnGoodsDetailItem detailItem = null;
							if (operation.equals("see")) {
								detailItem  = new StoreReturnGoodsDetailItem(StoreReturnGoodsAddActivity.this, inflater,true,isPrice);
								detailItem.getTxt_reason().setTextColor(Color.parseColor("#666666"));
							}else {
								detailItem  = new StoreReturnGoodsDetailItem(StoreReturnGoodsAddActivity.this, inflater,false,isPrice);
							}
							detailItem.initWithAppInfo((ReturnGoodsDetailVo) list.get(i));
							store_collect_add_layout.addView(detailItem.getItemView());
							storeReturnMap.put(list.get(i).getGoodsId(), detailItem);
							if (isPrice.equals("false")) {
								detailItem.getPriceLayout().setVisibility(View.INVISIBLE);//隐藏进货价
							}
						}
					}
				
				}
			}
			@Override
			public void onFail(Exception e) {
			}
		}).execute();
	}
}
