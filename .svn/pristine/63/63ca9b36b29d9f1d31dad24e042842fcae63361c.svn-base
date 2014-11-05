package com.dfire.retail.app.manage.activity.logisticmanager;

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
import com.dfire.retail.app.manage.vo.supplyManageVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * 物流管理-添加门店订货
 *
 */
@SuppressLint("SimpleDateFormat")
public class SupplyManagerAddActivity extends Activity implements OnClickListener, IItemListListener{

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
		type.initLabel("类别", null, Boolean.TRUE, SupplyManagerAddActivity.this);
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
		
		progressDialog = new ProgressDialog(SupplyManagerAddActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			supplyState = intent.getStringExtra("supplyState");
		}
		if(supplyState!=null && supplyState.equals("SUPPLY_ADD"))
		{		
			searchLogisticsRecordsBtn.setVisibility(View.GONE);
			deleteSupplyBtn.setVisibility(View.GONE);
			titleTxt.setText("添加");
			title_left.setVisibility(View.VISIBLE);
			title_right.setVisibility(View.VISIBLE);
			title_back.setVisibility(View.GONE);				
		}	
		String typeName = bundle.getString("typeName");
		val = bundle.getString("typeVal");
		if (StringUtils.isEmpty(typeName)) 
		{
			this.type.initData("", "");
		} 
		else 
		{
			this.type.initData(typeName, typeName);
		}	
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
				Intent addGoods=new Intent(SupplyManagerAddActivity.this,StoreOrderAddGoodsActivity.class);
				addGoods.putExtra("flag", "returnOrderAdd");
				startActivityForResult(addGoods, 100);
				break;
		}
	}

	private void saveSupplyManager() {
		if (supplyState==null||supplyState.equals("SUPPLY_ADD")) {
			RequestParameter parameters = new RequestParameter(true);			
			parameters.setUrl(Constants.SUPPLY_INFO_MANAGE_ADD);
			supplyManageVo supplyManageVo = new supplyManageVo();
			supplyManageVo.setName(supplyName.getCurrVal());
			supplyManageVo.setShortname(supplyNameSpell.getCurrVal());
			supplyManageVo.setCode(supply_name_code.getCurrVal());
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
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
	
			AsyncHttpPost httppost = new AsyncHttpPost(parameters, new RequestResultCallback() {
				@Override
				public void onSuccess(String str) {
					JsonUtil ju = new JsonUtil(str);
					Intent listGoods = new Intent(SupplyManagerAddActivity.this, SupplierManagementActivity.class);
					startActivity(listGoods);
				}
	
				@Override
				public void onFail(Exception e) {
					e.printStackTrace();
				}
			});
			httppost.execute();
		}
	}
	
	@Override
	public void onItemListClick(ItemEditList obj) {
	if(obj.getId() == R.id.type){
		Intent selectType=new Intent(SupplyManagerAddActivity.this,SupplyTypeSelectActivity.class);
		selectType.putExtra("selectState", "SELECT");// 新生订单
		selectType.putExtra("selectValue", val);
		startActivity(selectType);
	}
	}
}
