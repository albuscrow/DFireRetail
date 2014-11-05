package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.vo.AllocateVo;
import com.dfire.retail.app.manage.vo.supplyManageVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
/**
 * 物流管理-编辑物流信息
 *
 */
@SuppressLint("SimpleDateFormat")
public class SupplyManagerDetailActivity extends Activity implements OnClickListener, IItemListListener{

	private ProgressDialog progressDialog;
		
	private LayoutInflater inflater;
	
	private ItemEditText supply_name_code,supplyName, supplyNameSpell, linkPerson, linkPhone, telePhone, supplyEmail, supplyFox, supplyAdress, openBank, bankAccount, bankAccountName;
	
	private ImageButton title_left,title_right, title_back;
	
	private TextView titleTxt;
		
	private ItemEditList type;
		
	private String supplyState;//操作状态
	
	private Button searchLogisticsRecordsBtn, deleteSupplyBtn;
	
	private supplyManageVo supplyManageVo;
	
	private String val;
	
	private LinkedTreeMap<String, String> typeVo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_add);
		inflater = LayoutInflater.from(this);
		findView();
	}
	
	public void findView()
	{
		supplyName=(ItemEditText)findViewById(R.id.supply_name);
		supplyNameSpell=(ItemEditText)findViewById(R.id.supply_name_spell);
		supply_name_code=(ItemEditText)findViewById(R.id.supply_name_code);
		linkPerson=(ItemEditText)findViewById(R.id.link_person);
		linkPhone=(ItemEditText)findViewById(R.id.link_phone);
		telePhone=(ItemEditText)findViewById(R.id.tel_phone);
		type=(ItemEditList)findViewById(R.id.type);
		supplyEmail=(ItemEditText)findViewById(R.id.supply_email);
		supplyFox=(ItemEditText)findViewById(R.id.supply_fox);
		supplyAdress=(ItemEditText)findViewById(R.id.supply_address);
		openBank=(ItemEditText)findViewById(R.id.supply_open_bank);
		bankAccount = (ItemEditText) findViewById(R.id.supply_bank_account);
		bankAccountName = (ItemEditText) findViewById(R.id.supply_bank_name);
		searchLogisticsRecordsBtn = (Button) findViewById(R.id.search_logistics_records);
		deleteSupplyBtn = (Button) findViewById(R.id.delete_supply_record);
		title_left = (ImageButton) findViewById(R.id.title_left);
		title_right = (ImageButton) findViewById(R.id.title_right);
		title_back = (ImageButton) findViewById(R.id.title_back);
		titleTxt = (TextView) findViewById(R.id.title_text);	
		supplyName.initLabel("供应商名称", null, Boolean.TRUE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		supplyNameSpell.initLabel("供应商简称", null, Boolean.TRUE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		supply_name_code.initLabel("编码", null, Boolean.TRUE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		linkPerson.initLabel("联系人", null, Boolean.TRUE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		linkPhone.initLabel("联系电话", null, Boolean.FALSE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		telePhone.initLabel("手机", null, Boolean.TRUE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		type.initLabel("类别", null, Boolean.TRUE, SupplyManagerDetailActivity.this);
		type.setOnClickListener(this);
		
		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);
		title_back.setOnClickListener(this);
		searchLogisticsRecordsBtn.setOnClickListener(this);
		deleteSupplyBtn.setOnClickListener(this);
		supplyEmail.initLabel("邮箱", null, Boolean.FALSE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		supplyFox.initLabel("传真", null, Boolean.FALSE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		supplyAdress.initLabel("地址", null, Boolean.FALSE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		openBank.initLabel("开户银行", null, Boolean.FALSE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		bankAccount.initLabel("银行账户", null, Boolean.FALSE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		bankAccountName.initLabel("户名", null, Boolean.FALSE, InputType.TYPE_NUMBER_FLAG_SIGNED);
		
		progressDialog = new ProgressDialog(SupplyManagerDetailActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) 
		{
			supplyState = intent.getStringExtra("supplyState");
		}
		if(supplyState!=null && supplyState.equals("SUPPLY_EDIT"))
		{						
			searchLogisticsRecordsBtn.setVisibility(View.VISIBLE);
			deleteSupplyBtn.setVisibility(View.VISIBLE);
			supplyManageVo = (supplyManageVo) getIntent().getSerializableExtra("supplyManageVo");
			dataload();
			title_left.setVisibility(View.GONE);
			title_right.setVisibility(View.VISIBLE);
			title_back.setVisibility(View.VISIBLE);
			titleTxt.setText(supplyManageVo.getName());							
		}		
	}
	
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		RetailApplication application = (RetailApplication) getApplication();
		HashMap<String, Object> objMap = application.getObjMap();
		typeVo = (LinkedTreeMap<String, String>) objMap.get("typeVo");
		if (typeVo != null && typeVo.size()>0) {
			String typeName = typeVo.get("typeName").toString();
			val = typeVo.get("typeVal").toString();
			if (StringUtils.isEmpty(typeName)) 
			{
				this.type.initData("", "");
			} 
			else 
			{
				this.type.initData(typeName, typeName);
			}
		}		
	}

	private void dataload()
	{
		supplyName.initData(supplyManageVo.getName());
		supplyNameSpell.initData(supplyManageVo.getShortname());
		supply_name_code.initData(supplyManageVo.getCode());
		linkPerson.initData(supplyManageVo.getRelation());
		linkPhone.initData(supplyManageVo.getMobile());
		telePhone.initData(supplyManageVo.getPhone());
		type.initData(supplyManageVo.getTypeName(), supplyManageVo.getTypeVal());
		supplyEmail.initData(supplyManageVo.getEmail());
		supplyFox.initData(supplyManageVo.getFax());
		supplyAdress.initData(supplyManageVo.getAddress());
		openBank.initData(supplyManageVo.getBankaccountname());
		bankAccount.initData(supplyManageVo.getBankcardno());
		bankAccountName.initData(supplyManageVo.getBankname());
	}
 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_left:
				finish();
				break;
			case R.id.title_right:
				saveSupplyManager();
				break;
			case R.id.title_back:
				finish();
				break;
			case R.id.add_layout:
				Intent addGoods=new Intent(SupplyManagerDetailActivity.this,StoreOrderAddGoodsActivity.class);
				addGoods.putExtra("flag", "returnOrderAdd");
				startActivityForResult(addGoods, 100);
				break;
			case R.id.search_logistics_records:
				Intent list = new Intent(SupplyManagerDetailActivity.this, LogisticsRecordsListActivity.class);
				list.putExtra("shopId", RetailApplication.getShopVo().getShopId());
				list.putExtra("supplyId", supplyManageVo.getId());
				list.putExtra("currentPage",1);
				startActivity(list);
				break;
			case R.id.delete_supply_record:
				deleteSupply();
				break;
		}
	}

	private void saveSupplyManager() {
			RequestParameter parameters = new RequestParameter(true);			
			parameters.setUrl(Constants.SUPPLY_INFO_MANAGE_UPDATE);
			supplyManageVo.setName(supplyName.getCurrVal());
			supplyManageVo.setShortname(supplyNameSpell.getCurrVal());
			supplyManageVo.setRelation(linkPerson.getCurrVal());
			supplyManageVo.setShortname(supplyNameSpell.getCurrVal());
			supplyManageVo.setRelation(linkPerson.getCurrVal());
			supplyManageVo.setAddress(supplyAdress.getCurrVal());
			supplyManageVo.setBankcardno(bankAccount.getCurrVal());
			supplyManageVo.setBankname(bankAccountName.getCurrVal());
			supplyManageVo.setEmail(supplyEmail.getCurrVal());
			supplyManageVo.setFax(supplyFox.getCurrVal());
			supplyManageVo.setMobile(linkPhone.getCurrVal());
			supplyManageVo.setPhone(telePhone.getCurrVal());
			supplyManageVo.setTypeVal(val);
			supplyManageVo.setTypeName(type.getCurrVal());
			try {
				parameters.setParam("supply",  new JSONObject(new GsonBuilder().serializeNulls().create().toJson(supplyManageVo)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
	
			AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
				@Override
				public void onSuccess(String str) {
					JsonUtil ju = new JsonUtil(str);
					Intent listGoods = new Intent(SupplyManagerDetailActivity.this, SupplierManagementActivity.class);
					startActivity(listGoods);
				}
	
				@Override
				public void onFail(Exception e) {
					e.printStackTrace();
				}
			});
			httppost.execute();
		}
	
	private void deleteSupply() {
		RequestParameter parameters = new RequestParameter(true);	
		parameters.setUrl(Constants.SUPPLY_INFO_MANAGE_DELETE);	
		parameters.setParam("id", supplyManageVo.getId());
		parameters.setParam("lastver", supplyManageVo.getLastver());

		AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				Intent listGoods = new Intent(SupplyManagerDetailActivity.this, SupplierManagementActivity.class);
				startActivity(listGoods);
			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();
		
	}
	
	@Override
	public void onItemListClick(ItemEditList obj) {
	if(obj.getId() == R.id.type){
		Intent selectType=new Intent(SupplyManagerDetailActivity.this,SupplyTypeSelectActivity.class);
		selectType.putExtra("selectState", "SELECT");// 新生订单
		selectType.putExtra("selectValue", val);
		startActivity(selectType);
	}
	}
}
