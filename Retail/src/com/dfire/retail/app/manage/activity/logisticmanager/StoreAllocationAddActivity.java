/**
 * 
 */
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
import com.dfire.retail.app.manage.activity.item.StoreAllocationDetailItem;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.AllocateDetailBo;
import com.dfire.retail.app.manage.data.bo.RllocateSaveBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.vo.AllocateVo;
import com.dfire.retail.app.manage.vo.allocateDetailVo;
import com.google.gson.Gson;

/**
 * 调拨   添加页面
 * @author ys 2014-11-17
 */
public class StoreAllocationAddActivity extends Activity implements OnClickListener{
	
	public static StoreAllocationAddActivity instance = null;
	
	private LayoutInflater inflater;

	private ImageButton title_right,title_left,title_return;

	private RelativeLayout add_layout;
	
	private ImageView collect_add_iv;

	private TextView out_shop,in_shop;

	private TextView total_num;

	private LinearLayout allocation_add_layout,btn_layout,count_price;

	private String inShopId,shopName,allocation,inShopName,outShopId;

	public HashMap<String, StoreAllocationDetailItem> allocationHashMap = new HashMap<String, StoreAllocationDetailItem>();
	
	private allocateDetailVo allocateDetailVo;
	
	private List<allocateDetailVo> allocateDetailVos;
	
	private AllShopVo allShopVo;
	
	private AllocateVo allocateVo;
	
	private String lastVe = "",allocateId = null,operateType;
	
	private View view;
	
	private Button btn_refuse,btn_confirm,btn_del;
	
	private TextView collect_add_text,mAllocateNo,price_total;
	
	private RelativeLayout allocatelayout;
	
	private View allocateiv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_allocation_add);
		this.inflater = LayoutInflater.from(this);
		instance = this;
		findView();

	}
	private void findView() {
		allocateDetailVos =  new ArrayList<allocateDetailVo>();
		title_right = (ImageButton) findViewById(R.id.title_right);
		title_right.setOnClickListener(this);
		title_left = (ImageButton) findViewById(R.id.title_left);
		title_left.setOnClickListener(this);
		add_layout = (RelativeLayout) findViewById(R.id.add_layout);
		add_layout.setOnClickListener(this);
		title_return = (ImageButton) findViewById(R.id.title_return);
		title_return.setOnClickListener(this);
		out_shop = (TextView) findViewById(R.id.out_shop);
		in_shop = (TextView) findViewById(R.id.in_shop);
		allocation_add_layout = (LinearLayout) findViewById(R.id.allocation_add_layout);
		total_num = (TextView) findViewById(R.id.num_total);
		collect_add_iv = (ImageView) findViewById(R.id.collect_add_iv);
		view = (View) findViewById(R.id.view);
		btn_layout = (LinearLayout) findViewById(R.id.btn_layout);
		btn_refuse = (Button) findViewById(R.id.btn_refuse);
		btn_refuse.setOnClickListener(this);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		btn_del = (Button) findViewById(R.id.btn_del);
		collect_add_text = (TextView)findViewById(R.id.collect_add_text);
		TextPaint tp = collect_add_text.getPaint(); 
		tp.setFakeBoldText(true);
		mAllocateNo = (TextView) findViewById(R.id.allocateNo);
		allocatelayout = (RelativeLayout)findViewById(R.id.allocatelayout);
		allocateiv = (View) findViewById(R.id.allocateiv);
		count_price = (LinearLayout) findViewById(R.id.count_price);
		price_total = (TextView) findViewById(R.id.price_total);
		
		allocation = getIntent().getStringExtra("allocation");
		outShopId = getIntent().getStringExtra("shopId");
		if (!allocation.equals("add")) {//不是添加  
			allocateVo = (AllocateVo)getIntent().getSerializableExtra("allocateVo");
			if (allocation.endsWith("see")) {//其他的
				title_right.setVisibility(View.GONE);
				title_left.setVisibility(View.GONE);
				title_return.setVisibility(View.VISIBLE);
				in_shop.setCompoundDrawables(null, null, null, null);
				in_shop.setTextColor(Color.parseColor("#666666"));
				add_layout.setVisibility(View.GONE);
				collect_add_iv.setVisibility(View.GONE);
				view.setVisibility(View.GONE);
				allocatelayout.setVisibility(View.VISIBLE);
				allocateiv.setVisibility(View.VISIBLE);
			}else {//调拨中的可以修改  添加
				in_shop.setCompoundDrawables(null, null, null, null);
				in_shop.setTextColor(Color.parseColor("#666666"));
				collect_add_iv.setVisibility(View.VISIBLE);
				collect_add_iv.setOnClickListener(this);
				operateType = "edit";
				btn_del.setOnClickListener(this);
				btn_del.setVisibility(View.VISIBLE);
			}
			if (allocateVo!=null) {
				allocateId = allocateVo.getAllocateId();
				loadData();
			}
		}else {//添加的时候  设置调出门点  点入门店可选择  
			shopName = getIntent().getStringExtra("shopName");
			out_shop.setText(shopName);
			in_shop.setOnClickListener(this);
			collect_add_iv.setOnClickListener(this);
			operateType = "add";
		}
	}
	
	/**
	 * 回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 200) {
			allShopVo = (AllShopVo)data.getSerializableExtra("shopVo");
			if (allShopVo!=null) {
				this.in_shop.setText(allShopVo.getShopName());
				this.inShopName = allShopVo.getShopName();
				this.inShopId = allShopVo.getShopId();
			}
		}else {
			RetailApplication application = (RetailApplication) getApplication();
			HashMap<String, Object> map = application.getObjMap();
			allocateDetailVo = (allocateDetailVo)map.get("allocationAdd");
			if (allocateDetailVo!=null) {
				//判断 map 里面是否存有 该key 对应的value
				if (allocationHashMap.containsKey(allocateDetailVo.getGoodsId())) {
					StoreAllocationDetailItem goodsItem = allocationHashMap.get(allocateDetailVo.getGoodsId());
					for (int i = 0; i < allocateDetailVos.size(); i++) {
						if (allocateDetailVos.get(i).getGoodsId().equals(goodsItem.getAllocateDetailVo().getGoodsId())) {
							if (allocateDetailVos.get(i).getOperateType().equals("del")) {
								allocateDetailVos.get(i).setOperateType("edit");
								allocation_add_layout.addView(goodsItem.getItemView());
							}
							Integer sum = allocateDetailVos.get(i).getGoodsSum();//之前的数量
							Integer newSum = allocateDetailVo.getGoodsSum();
							goodsItem.getNumTxt().setText(String.valueOf(sum+newSum));//设置数量
							allocateDetailVos.get(i).setGoodsSum(sum+newSum);//设置list里面的数量
							allocateDetailVos.get(i).setGoodsTotalPrice(allocateDetailVos.get(i).getGoodsPrice().multiply(new BigDecimal(sum+newSum)));//合计
						}
					}
				}else {
					allocateDetailVos.add(allocateDetailVo);
					StoreAllocationDetailItem collectGoodsItem = new StoreAllocationDetailItem(this,inflater,false);
					collectGoodsItem.initWithData(allocateDetailVo);
					allocation_add_layout.addView(collectGoodsItem.getItemView());
					allocationHashMap.put(allocateDetailVo.getGoodsId(), collectGoodsItem);
				}
				changePriceNumber(null);
			}
			map.put("allocationAdd", null);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_return:
			finish();
			break;
		case R.id.title_right:
			saveAllocateList();
			break;
		case R.id.add_layout:
			add();
			break;
		case R.id.in_shop:
			Intent selectIntent =new Intent(StoreAllocationAddActivity.this,StoreAllocationSelectShopActivity.class);
			selectIntent.putExtra("selectShopId", inShopId);
			startActivityForResult(selectIntent, 200);
			break;
		case R.id.collect_add_iv:
			add();
			break;
		case R.id.btn_confirm:
			operateType = "receipt";
			saveAllocateList();
			break;
		case R.id.btn_refuse:
			operateType = "refuse";
			saveAllocateList();
			break;
		case R.id.btn_del:
			final AlertDialog alertDialog = new AlertDialog(this);
			alertDialog.setMessage(getResources().getString(R.string.isdelete_allocation));
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener() {
				@Override
				public void onClick(View v) {
					operateType = "del";
					saveAllocateList();
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
	private void add(){
		Intent allocation = new Intent(StoreAllocationAddActivity.this, StoreOrderAddGoodsActivity.class);
		allocation.putExtra("shopId",outShopId);
		allocation.putExtra("isPrice", "false");
		allocation.putExtra("flag", "allocationAdd");
		startActivityForResult(allocation, 100);
	}
	
	/**
	 * 提交 对应设值
	 */
	private boolean initData(){
		if (allocateDetailVos!=null&&allocationHashMap!=null&&allocationHashMap.size()>0) {
			for (int i = 0; i < allocateDetailVos.size(); i++) {
				StoreAllocationDetailItem goodsItem = allocationHashMap.get(allocateDetailVos.get(i).getGoodsId());
				Integer num = 0;
				try {
					if (allocateDetailVos.get(i).getOperateType().equals("del")) {
						num = 0;
					}else {
						num = Integer.parseInt(goodsItem.getNumTxt().getText().toString());
					}
				} catch (NumberFormatException e) {
					num = 0;
				}  
				allocateDetailVos.get(i).setGoodsTotalPrice(allocateDetailVos.get(i).getGoodsPrice().multiply(new BigDecimal(num)));//设置总价
				allocateDetailVos.get(i).setGoodsSum(num);//给list 里面的每项设置数量
			}
		}
		return true;
	}
	
	/**
	 * 移除item
	 */
	public void removeView(StoreAllocationDetailItem goodsItem){
		allocation_add_layout.removeView(goodsItem.getItemView());
		String goodsId = goodsItem.getAllocateDetailVo().getGoodsId();
		if (allocateDetailVos!=null&&allocateDetailVos.size()>0) {
			for (int i = 0; i < allocateDetailVos.size(); i++) {
				if (allocateDetailVos.get(i).getGoodsId().equals(goodsId)) {
					if (allocateDetailVos.get(i).getAllocateDetailId()!=null&&!allocateDetailVos.get(i).getAllocateDetailId().equals("")) {
						allocateDetailVos.get(i).setOperateType("del");
					}else {
						allocationHashMap.remove(allocateDetailVos.get(i).getGoodsId());
						allocateDetailVos.remove(i);
					}
				}
			}
		}
		changePriceNumber(null);
	}
	
	/**
	 * 动态监听editText 修改总价格 和数量
	 */
	public void changePriceNumber(StoreAllocationDetailItem goodsItem){
		if (goodsItem==null) {
			initData();
		}
		if (allocateDetailVos.size()>0) {
			Integer count = 0;
			for (int i = 0; i < allocateDetailVos.size(); i++) {
				count = count+allocateDetailVos.get(i).getGoodsSum();
			}
			total_num.setText(""+count);
		}else {
			total_num.setText("0");
		}
	}
	/**
	 * 查看详情的时候 修改删除该商品
	 */
	public void changeGoodInfo(allocateDetailVo detailVo){
		if (allocationHashMap.containsKey(detailVo.getGoodsId())) {
			StoreAllocationDetailItem goodsItem = allocationHashMap.get(detailVo.getGoodsId());
			if (detailVo.getGoodsSum()==0) {
				removeView(goodsItem);
			}else {
				for (int i = 0; i < allocateDetailVos.size(); i++) {
					if (allocateDetailVos.get(i).getGoodsId().equals(detailVo.getGoodsId())) {
						goodsItem.getNumTxt().setText(String.valueOf(detailVo.getGoodsSum()));//设置数量
						allocateDetailVos.get(i).setGoodsSum(detailVo.getGoodsSum());//设置list里面的数量
						allocateDetailVos.get(i).setGoodsTotalPrice(allocateDetailVos.get(i).getGoodsPrice().multiply(new BigDecimal(detailVo.getGoodsSum())));//合计
						allocateDetailVos.get(i).setProductionDate(detailVo.getProductionDate());//设置生产日期
					}
				}
			}
			changePriceNumber(null);
		}
	}
	/**根据id查询详情*/
	private void loadData() {

		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.ALLOCATE_DETAIL);
		parameters.setParam("allocateId", allocateVo.getAllocateId());

		new AsyncHttpPost(this, parameters, AllocateDetailBo.class, new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				AllocateDetailBo bo = (AllocateDetailBo)oj;
				if (bo!=null) {
					String allocateNo = bo.getAllocateNo();
					outShopId = bo.getOutShopId();
					shopName = bo.getOutShopName();
					inShopId = bo.getInShopId();
					Integer goodsTotalSum = bo.getGoodsTotalSum();
					BigDecimal goodsTotalPrice = bo.getGoodsTotalPrice();
					inShopName = bo.getInShopName();
					lastVe =  bo.getLastVer();
					List<allocateDetailVo> allocateDetailList = bo.getAllocateDetailList();
					if (allocateDetailList!=null&& allocateDetailList.size() > 0) {
						allocateDetailVos.addAll(allocateDetailList);
					}
					out_shop.setText(shopName);
					in_shop.setText(inShopName);
					total_num.setText(String.valueOf(goodsTotalSum));
					mAllocateNo.setText(allocateNo);
					
					if (!allocation.endsWith("see")) {/**调拨中查看*/
						if (RetailApplication.getShopVo().getShopName().equals(inShopName)) {
							btn_layout.setVisibility(View.VISIBLE);
							allocatelayout.setVisibility(View.VISIBLE);
							allocateiv.setVisibility(View.VISIBLE);
							title_right.setVisibility(View.GONE);
							title_left.setVisibility(View.GONE);
							title_return.setVisibility(View.VISIBLE);
							in_shop.setCompoundDrawables(null, null, null, null);
							in_shop.setTextColor(Color.parseColor("#666666"));
							add_layout.setVisibility(View.GONE);
							collect_add_iv.setVisibility(View.GONE);
							view.setVisibility(View.GONE);
						}
					}else if(RetailApplication.getShopVo().getType()==ShopVo.ZHONGBU){/**确定或拒绝的 总部可以查看进货价 以及合计金额*/
						count_price.setVisibility(View.VISIBLE);
						price_total.setText(String.format("%.2f", goodsTotalPrice));
					}
					/**循环创建选项*/
					if (allocateDetailVos != null && allocateDetailVos.size() != 0) {
						for (int i = 0; i < allocateDetailVos.size(); ++i) {
							StoreAllocationDetailItem detailItem = null;
							if(allocation.equals("see")){/**如果是确定的 。  就显示文本。 不能添加 修改 */
								detailItem = new StoreAllocationDetailItem(StoreAllocationAddActivity.this,inflater,true);
							}else {/**调拨中的*/
								if (RetailApplication.getShopVo().getShopName().equals(inShopName)) {/**如果是被调入的门点 也不能修改 。*/
									detailItem = new StoreAllocationDetailItem(StoreAllocationAddActivity.this,inflater,true);
								}else {
									/**在调拨中的  自己是调出门店*/
									detailItem = new StoreAllocationDetailItem(StoreAllocationAddActivity.this,inflater,false);
								}
							}
							detailItem.initWithData(allocateDetailVos.get(i));
							allocation_add_layout.addView(detailItem.getItemView());
							allocationHashMap.put(allocateDetailVos.get(i).getGoodsId(), detailItem);/**goodsItem 存入map(原有view)*/
						}
					}
				}
			}
			
			@Override
			public void onFail(Exception e) {
			}
		}).execute();
	}
	/**check shop*/
	private boolean checkShop(){
		if (inShopName!=null&&!inShopName.equals("")) {
			if(inShopName.equals(shopName)){
				new ErrDialog(StoreAllocationAddActivity.this, getResources().getString(R.string.LM_MSG_000019)).show();
				return false;
			}else {
				return true;
			}
		}else {
			new ErrDialog(StoreAllocationAddActivity.this, getResources().getString(R.string.LM_MSG_000007)).show();
			return false;
		}
	}
	/**保存  修改 添加  确定 拒绝*/
	private void saveAllocateList() {
		if (allocateDetailVos!=null&&allocateDetailVos.size()>0) {
			if (checkShop()) {
				RequestParameter parameters = new RequestParameter(true);
				parameters.setUrl(Constants.ALLOCATE_SAVE);
				if (operateType.equals("add")||operateType.equals("edit")||operateType.equals("receipt")) {
					parameters.setParam("inShopId", inShopId);
					parameters.setParam("outShopId", outShopId);
					try {
						parameters.setParam("allocateDetailList", new JSONArray(new Gson().toJson(allocateDetailVos)));
					} catch (JSONException e1) {
						parameters.setParam("allocateDetailList", null);
					}
				}
				parameters.setParam("allocateId", allocateId);
				parameters.setParam("operateType", operateType);
				parameters.setParam("lastVer", lastVe);
				new AsyncHttpPost(this, parameters, RllocateSaveBo.class, new RequestCallback() {
					
					@Override
					public void onSuccess(Object oj) {
						RllocateSaveBo bo = (RllocateSaveBo)oj;
						if (bo!=null) {
							if (StringUtils.isEquals(operateType, "edit")) {
								StoreAllocationAddActivity.this.finish();
							}else {
								StoreAllocationAddActivity.this.finish();
								if (StringUtils.isEquals(operateType, "add")) {
									StoreAllocationActivity.instance.pullDig(bo.getAllocateNo());
								}
								StoreAllocationActivity.instance.reFreshing();
							}
						}
					}
					@Override
					public void onFail(Exception e) {
					}
				}).execute();
			}
		}else {
			new ErrDialog(StoreAllocationAddActivity.this, getResources().getString(R.string.please_select_allocate_goods)).show();
		}
	}
}
