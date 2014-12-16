package com.dfire.retail.app.manage.activity.logisticmanager;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.bo.ReturnNotMsgBo;
import com.dfire.retail.app.manage.data.bo.SupplyManageBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.vo.supplyManageVo;
import com.google.gson.GsonBuilder;

/**
 * 物流管理-编辑物流信息
 * 
 */
@SuppressLint("SimpleDateFormat")
public class SupplyManagerDetailActivity extends TitleActivity implements OnClickListener, IItemListListener {

	private ItemEditText supply_name_code, supplyName, supplyNameSpell, linkPerson, linkPhone, telePhone, supplyEmail, supplyFox, supplyAdress, openBank, bankAccount, bankAccountName;

	private ItemEditList type;

	private Button searchLogisticsRecordsBtn, deleteSupplyBtn;

	private supplyManageVo supplyManageVo;

	private DicVo dicVo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_add1);
		showBackbtn();
		findView();
	}

	/**
	 * 
	 */
	public void findView() {
		dicVo = new DicVo();
		supplyName = (ItemEditText) findViewById(R.id.supply_name);
		supplyNameSpell = (ItemEditText) findViewById(R.id.supply_name_spell);
		supply_name_code = (ItemEditText) findViewById(R.id.supply_name_code);
		linkPerson = (ItemEditText) findViewById(R.id.link_person);
		linkPhone = (ItemEditText) findViewById(R.id.link_phone);
		telePhone = (ItemEditText) findViewById(R.id.tel_phone);
		type = (ItemEditList) findViewById(R.id.type);
		supplyEmail = (ItemEditText) findViewById(R.id.supply_email);
		supplyFox = (ItemEditText) findViewById(R.id.supply_fox);
		supplyAdress = (ItemEditText) findViewById(R.id.supply_address);
		openBank = (ItemEditText) findViewById(R.id.supply_open_bank);
		bankAccount = (ItemEditText) findViewById(R.id.supply_bank_account);
		bankAccountName = (ItemEditText) findViewById(R.id.supply_bank_name);
		searchLogisticsRecordsBtn = (Button) findViewById(R.id.search_logistics_records);
		deleteSupplyBtn = (Button) findViewById(R.id.delete_supply_record);
		supplyName.initLabel("供应商名称", null, Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		supplyName.setMaxLength(50);
		supplyName.setIsChangeListener(this.getItemChangeListener());
		supplyNameSpell.initLabel("供应商简称", null, Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		supplyNameSpell.setMaxLength(20);
		supplyNameSpell.setIsChangeListener(this.getItemChangeListener());
		supply_name_code.initLabel("编码", null, Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		supply_name_code.setMaxLength(20);
		supply_name_code.setIsChangeListener(this.getItemChangeListener());
		linkPerson.initLabel("联系人", null, Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		linkPerson.setMaxLength(50);
		linkPerson.setIsChangeListener(this.getItemChangeListener());
		linkPhone.initLabel("联系电话", null, Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		linkPhone.setMaxLength(50);
		linkPhone.setIsChangeListener(this.getItemChangeListener());
		telePhone.initLabel("手机", null, Boolean.TRUE, InputType.TYPE_CLASS_NUMBER);
		telePhone.setMaxLength(11);
		telePhone.setIsChangeListener(this.getItemChangeListener());
		type.initLabel("类别", null, Boolean.TRUE,this);
		type.initData("请选择", "请选择");
		type.setIsChangeListener(this.getItemChangeListener());
		type.getImg().setImageResource(R.drawable.ico_next);
		mRight.setOnClickListener(this);
		searchLogisticsRecordsBtn.setOnClickListener(this);
		deleteSupplyBtn.setOnClickListener(this);
		supplyEmail.initLabel("邮箱", null, Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		supplyEmail.setMaxLength(50);
		supplyEmail.setIsChangeListener(this.getItemChangeListener());
		supplyFox.initLabel("传真", null, Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		supplyFox.setMaxLength(30);
		supplyFox.setIsChangeListener(this.getItemChangeListener());
		supplyAdress.initLabel("地址", null, Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		supplyAdress.setMaxLength(100);
		supplyAdress.setIsChangeListener(this.getItemChangeListener());
		openBank.initLabel("开户银行", null, Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		openBank.setMaxLength(50);
		openBank.setIsChangeListener(this.getItemChangeListener());
		bankAccount.initLabel("银行账户", null, Boolean.FALSE, InputType.TYPE_CLASS_NUMBER);
		bankAccount.setMaxLength(50);
		bankAccount.setIsChangeListener(this.getItemChangeListener());
		bankAccountName.initLabel("户名", null, Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		bankAccountName.setMaxLength(50);
		bankAccountName.setIsChangeListener(this.getItemChangeListener());
		supplyManageVo=(supplyManageVo)getIntent().getExtras().getSerializable("supplyManageVo");
		dataload();
	}


	private void dataload() {
		if (supplyManageVo!=null) {
			setTitleText(supplyManageVo.getName());
			supplyName.initData(supplyManageVo.getName());
			supplyNameSpell.initData(supplyManageVo.getShortname());
			supply_name_code.initData(supplyManageVo.getCode());
			linkPerson.initData(supplyManageVo.getRelation());
			linkPhone.initData(supplyManageVo.getMobile());
			telePhone.initData(supplyManageVo.getPhone());
			if (supplyManageVo.getTypeName()!=null&&!supplyManageVo.getTypeName().equals("")) {
				type.initData(supplyManageVo.getTypeName(), supplyManageVo.getTypeName());
			}
			supplyEmail.initData(supplyManageVo.getEmail());
			supplyFox.initData(supplyManageVo.getFax());
			supplyAdress.initData(supplyManageVo.getAddress());
			openBank.initData(supplyManageVo.getBankaccountname());
			bankAccount.initData(supplyManageVo.getBankcardno());
			bankAccountName.initData(supplyManageVo.getBankname());
			Integer val;
			try {
				val = Integer.parseInt(supplyManageVo.getTypeVal());
			} catch (NumberFormatException e) {
				val = null;
			}
			dicVo.setVal(val);
			dicVo.setName(supplyManageVo.getTypeName());
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){ 
			dicVo.setVal(Integer.parseInt(data.getStringExtra("typeVal")));
			dicVo.setName(data.getStringExtra("typeName"));
			this.type.changeData(dicVo.getName(), dicVo.getName());
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right:
			saveSupplyManager();
			break;
		case R.id.search_logistics_records:
			Intent list = new Intent(SupplyManagerDetailActivity.this, LogisticsRecordsListActivity.class);
			list.putExtra("shopId", RetailApplication.getShopVo().getShopId());
			list.putExtra("supplyId", supplyManageVo.getId());
			list.putExtra("currentPage", 1);
			startActivity(list);
			break;
		case R.id.delete_supply_record:
			final AlertDialog alertDialog = new AlertDialog(this);
			alertDialog.setMessage(getResources().getString(R.string.isdelete_supply));
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener() {
				@Override
				public void onClick(View v) {
					deleteSupply();
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
	 * 保存
	 */
	private void saveSupplyManager() {
		if (!valid()) {
			return;
		}
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
		supplyManageVo.setTypeVal(dicVo.getVal()+"");
		supplyManageVo.setTypeName(dicVo.getName());
		supplyManageVo.setBankaccountname(openBank.getCurrVal());
		supplyManageVo.setCode(supply_name_code.getCurrVal());
		try {
			parameters.setParam("supply", new JSONObject(new GsonBuilder().serializeNulls().create().toJson(supplyManageVo)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new AsyncHttpPost(this, parameters, SupplyManageBo.class, new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				SupplyManageBo bo = (SupplyManageBo)oj;
				if (bo!=null) {
					SupplyManagerDetailActivity.this.finish();
					SupplierManagementActivity.instance.reFreshing();
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}
	/**
	 * 删除
	 */
	private void deleteSupply() {
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.SUPPLY_INFO_MANAGE_DELETE);
		parameters.setParam("id", supplyManageVo.getId());
		parameters.setParam("lastver", supplyManageVo.getLastver());
		new AsyncHttpPost(this, parameters, ReturnNotMsgBo.class, new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				ReturnNotMsgBo bo = (ReturnNotMsgBo)oj;
				if (bo!=null) {
					SupplyManagerDetailActivity.this.finish();
					SupplierManagementActivity.instance.reFreshing();
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}

	@Override
	public void onItemListClick(ItemEditList obj) {
		int position = Integer.parseInt(String.valueOf(obj.getTag()));
		switch (position) {
		case 1:  
			Intent selectType = new Intent(SupplyManagerDetailActivity.this, SupplyTypeSelectActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("dicVo", dicVo);
			selectType.putExtras(bundle);
			startActivityForResult(selectType, 100);
			break;
		}
	}

	private boolean valid() {
		if (StringUtils.isEmpty(supplyName.getStrVal())) {
			new ErrDialog(this, getResources().getString(R.string.please_print_supply_name)).show();
			return false;
		} else if (StringUtils.isEmpty(supplyNameSpell.getStrVal())) {
			new ErrDialog(this, getResources().getString(R.string.please_print_supply_sname)).show();
			return false;
		} else if (StringUtils.isEmpty(supply_name_code.getStrVal())) {
			new ErrDialog(this, getResources().getString(R.string.please_print_supply_code)).show();
			return false;
		} else if (StringUtils.isEmpty(linkPerson.getStrVal())) {
			new ErrDialog(this, getResources().getString(R.string.please_print_phone_name)).show();
			return false;
		} else if (StringUtils.isEmpty(telePhone.getStrVal())) {
			new ErrDialog(this, getResources().getString(R.string.please_print_phone)).show();
			return false;
		} else if(!StringUtils.isEmpty(telePhone.getStrVal())&&!isMobileNO(telePhone.getStrVal())){
			new ErrDialog(this, getResources().getString(R.string.phone_kind_err)).show();
			return false;
		}else if(!StringUtils.isEmpty(supplyFox.getStrVal())&&!isfax(supplyFox.getStrVal())){
			new ErrDialog(this, getResources().getString(R.string.supply_fox_err)).show();
			return false;
		}else if(!StringUtils.isEmpty(supplyEmail.getStrVal())&&!isEmail(supplyEmail.getStrVal())){
			new ErrDialog(this, getResources().getString(R.string.supply_email_err)).show();
			return false;
		}else if (!StringUtils.isEmpty(linkPhone.getStrVal())&&!isPhone(linkPhone.getStrVal())) {
			new ErrDialog(this, getResources().getString(R.string.phone_err)).show();
			return false;
		}
		return true;
	}
	/**
	 * 手机号码
	 * @param mobiles
	 * @return
	 */
	public boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "1((3\\d)|(4[57])|(5[012356789])|(8\\d))\\d{8}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}
	/**
	 * 邮编
	 * @param fax
	 * @return
	 */
	public boolean isfax(String fax) {
		String telRegex = "^(([0-9]{3})|([0-9]{4}))[-]\\d{6,8}$";
		if (TextUtils.isEmpty(fax))
			return false;
		else
			return fax.matches(telRegex);
	}
	/**
	 * 邮箱
	 * @param isEmail
	 * @return
	 */
	public boolean isEmail(String isEmail) {
		String telRegex = "\\w{1,}[@][\\w\\-]{1,}([.]([\\w\\-]{1,})){1,3}$";
		if (TextUtils.isEmpty(isEmail))
			return false;
		else
			return isEmail.matches(telRegex);
	}
	/**
	 * 联系电话
	 * @param phone
	 * @return
	 */
	public boolean isPhone(String phone){
		String telRegex = "^(([0-9]{3})|([0-9]{4}))[-]\\d{6,8}$";
		return phone.matches(telRegex);
	}
}
