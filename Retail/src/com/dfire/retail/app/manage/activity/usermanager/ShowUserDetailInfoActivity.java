package com.dfire.retail.app.manage.activity.usermanager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.item.DateUtil;
import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.ItemPortraitImage;
import com.dfire.retail.app.common.item.SwitchRowItemEditText;
import com.dfire.retail.app.common.item.listener.IItemIsChangeListener;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.common.item.listener.IItemTextChangeListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.common.AddImageDialog;
import com.dfire.retail.app.manage.common.CommonSelectTypeDialog;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.common.ErrorMsg;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.UserVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
import com.dfire.retail.app.manage.data.bo.UserDetailBo;
import com.dfire.retail.app.manage.data.bo.UserSaveBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.MD5;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.google.gson.GsonBuilder;

@SuppressLint("SimpleDateFormat")
public class ShowUserDetailInfoActivity extends UserBaseActivity implements
		OnClickListener, IItemListListener, IOnItemSelectListener,
		IItemIsChangeListener, IItemTextChangeListener {
	private static final int SELECTSHOPRECODE = 0;
	private static final int CAPTURERETURNCODE = 1;
	private static final int ALBUMRETURNCODE = 2;
	private static final int IMAGECROP = 3;
	private ItemEditText mEDName, mEDAccount, mEDPassword, mEDAdress, mEDIdentityNo,mEDUserStaffId, mEDUserPhone;
	private ItemEditList mELUserRole, mELUserShop, mELUserInDate, mELUserSex,mELUserBirthday, mELUserIdentityType;
	private ItemPortraitImage mIMGPortrait;
	private Bitmap tmpPortrait;// 记录用户修改前的头像信息，如果修改是，不保存当前记录，设置成原始的头像
	private UserVo mUserVo;
	private String userId;
	private final static int ADD_PHOTO = 1;// 弹出选择添加图片对话框
	private final static int FROM_CAPTURE = 2;
	private final static int FROM_ABLUM = 3;
	private final static int ADD_CANCEL = 4;
	private final static int EDITCOUNDT = 13;
	WindowManager mWindowManager;
	Window mWindow;
	private PopupWindow mPw;
	private SelectDateDialog _inDateDialog;
	private SelectDateDialog _birthDayDialog;

	private List<RoleVo> mRoleList;
	private List<AllShopVo> mShopList;
	private List<DicVo> mSexList;
	private List<DicVo> mIdentityTypeList;

	private ArrayList<String> roleNameList = new ArrayList<String>();
	private ArrayList<String> shopNameList = new ArrayList<String>();
	private ArrayList<String> sexNameList = new ArrayList<String>();
	private ArrayList<String> indentityNameList = new ArrayList<String>();
	private AddImageDialog addDialog;
	private String tmpRoleId;
	private Integer tmpIdentityTypeId;
	private String tmpShopId,oldShopId;
	private String tmpShopName;
	private Integer tmpSexId;// 性别
	private Button userDelete;// 删除按钮
	SimpleDateFormat dateFormat;
	// 标记当前员工资料是否有被修改而未被保存
	private boolean[] isChange;
	// 记录当前UI是否是保存模式
	private boolean isSaveMode = false;
	private RelativeLayout userPasswordReset;
	private UserVo tmpUserVo;
	private CommonSelectTypeDialog mSelectRole;
	private CommonSelectTypeDialog mSelectSex;
	private CommonSelectTypeDialog mSelectIndentity;
	private String imagePath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		userId = intent.getStringExtra(Constants.USERID);
		tmpShopId = intent.getStringExtra(Constants.SHOP_ID);
		oldShopId = intent.getStringExtra(Constants.SHOP_ID);
		tmpShopName = intent.getStringExtra(Constants.SHOPCOPNAME);

		tmpSexId = null;
		tmpIdentityTypeId = null;
		// 可以方便地修改日期格式
		dateFormat = new SimpleDateFormat(ShowUserDetailInfoActivity.this
				.getResources().getString(R.string.employee_date_format));
		// 初始化标记是否修改资料信息,如果有修改，更细Title信息
		isChange = new boolean[EDITCOUNDT];
		setContentView(R.layout.activity_user_detail_info);
		setTitleRes(R.string.employee_info);
		// showBackbtn();
		mRoleList = RetailApplication.getRoleList();
		mShopList = RetailApplication.getShopList();
		mSexList = RetailApplication.getSexList();
		mIdentityTypeList = RetailApplication.getIdentityTypeList();
		showBackbtn();
		// change2saveMode();
		findView();
		getUserInfoInit();

	}

	// 查询所有控件ID
	private void findView() {
		userPasswordReset = (RelativeLayout) findViewById(R.id.userPasswordReset);
		userPasswordReset.setVisibility(View.VISIBLE);
		userPasswordReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resetDialog();
			}
		});
		// 绑定数据变化监听器
		mEDName = (ItemEditText) findViewById(R.id.userName);
		mEDAccount = (ItemEditText) findViewById(R.id.userAccount);
		mEDAdress = (ItemEditText) findViewById(R.id.userAdress);
		mEDIdentityNo = (ItemEditText) findViewById(R.id.userIdentityNo);
		mEDPassword = (ItemEditText) findViewById(R.id.userPassword);
		mEDPassword.setVisibility(View.GONE);

		mEDUserPhone = (ItemEditText) findViewById(R.id.userPhone);
		mEDUserStaffId = (ItemEditText) findViewById(R.id.userStaffCode);

		// 可编译的员工信息初始化
		mEDName.initLabel("员工姓名", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		mEDName.setMaxLength(Constants.USERNAMEMAXLENGTH);

		mEDPassword.initLabel("登录密码", "", Boolean.TRUE,
				InputType.TYPE_TEXT_VARIATION_PASSWORD);
		mEDPassword.setMaxLength(Constants.USERPASSWORDMAXLENGTH);
		mEDPassword.setDigitsAndNum(true);

		mEDUserStaffId.initLabel("员工工号", "", Boolean.TRUE,
				InputType.TYPE_CLASS_NUMBER);
		mEDUserStaffId.setMaxLength(Constants.USERSTAFFIDMAXLENGTH);
		mEDUserStaffId.setDigitsAndNum(true);

		mEDAccount
				.initLabel("用户名", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		mEDAccount.setMaxLength(Constants.USERACCOUNTMAXLENGTH);
		mEDAccount.setDigitsAndNum(true);

		mEDIdentityNo.initLabel("证件号码", "", Boolean.FALSE,
				InputType.TYPE_CLASS_TEXT);
		mEDIdentityNo.setMaxLength(Constants.USERIDENTITYNOMAXLENGTH);

		mEDAdress.initLabel("住址", "", Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		mEDAdress.setMaxLength(Constants.USERADDRESSMAXLENGTH);

		mEDUserPhone.initLabel("手机", "", Boolean.FALSE,
				InputType.TYPE_CLASS_PHONE);
		mEDUserPhone.setMaxLength(Constants.USERMOBILELENGTH);

		mELUserBirthday = (ItemEditList) findViewById(R.id.userBirthday);
		mELUserInDate = (ItemEditList) findViewById(R.id.userInDate);
		mELUserRole = (ItemEditList) findViewById(R.id.userRole);
		mELUserSex = (ItemEditList) findViewById(R.id.userSex);
		mELUserShop = (ItemEditList) findViewById(R.id.userShopName);
		mELUserIdentityType = (ItemEditList) findViewById(R.id.userIdentityType);
		mELUserShop.setOnClickListener(this);
		mELUserShop.initLabel("所属店家", "", Boolean.TRUE, this);
		mELUserShop.initData("请选择", "");
		mELUserShop.getImg().setImageResource(R.drawable.ico_next);
		mELUserRole.initLabel("员工角色", "", Boolean.TRUE, this);
		mELUserRole.initData("请选择", "");
		mELUserSex.initLabel("性别", "", Boolean.FALSE, this);
		mELUserSex.initData("请选择", "");
		mELUserInDate.initLabel("入职时间", "", Boolean.FALSE, this);
		mELUserInDate.initData("请选择", "");
		mELUserBirthday.initLabel("生日", "", Boolean.FALSE, this);
		mELUserBirthday.initData("请选择", "");
		mELUserIdentityType.initLabel("证件类型", "", Boolean.FALSE, this);
		mELUserIdentityType.initData("请选择", "");
		mIMGPortrait = (ItemPortraitImage) findViewById(R.id.userPortrait);
		mIMGPortrait.initLabel("头像", "");
 		mIMGPortrait.getPortImge().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showAddMenu();
			}
		});
		userDelete = (Button) findViewById(R.id.userDelete);
		userDelete.setVisibility(View.VISIBLE);
		userDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog();
			}
		});
	}

	// 重置密码提示框
	private void resetDialog() {
		final AlertDialogCommon alertDialog1 = new AlertDialogCommon(
				ShowUserDetailInfoActivity.this);

		alertDialog1.setMessage(ShowUserDetailInfoActivity.this.getResources()
				.getString(R.string.resetPasswordMsg));
		alertDialog1.setPositiveButton(Constants.CONFIRM,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog1.dismiss();
						resetPassword();
					}
				});
		alertDialog1.setNegativeButton(Constants.CANCEL, new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog1.dismiss();
			}
		});
	}

	// 删除用户提示框
	protected void dialog() {
		final AlertDialogCommon alertDialog = new AlertDialogCommon(
				ShowUserDetailInfoActivity.this);
		alertDialog.setMessage(ShowUserDetailInfoActivity.this.getResources()
				.getString(R.string.user_delete));
		alertDialog.setPositiveButton(Constants.CONFIRM, new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				deleteUserInfo();
			}
		});
		alertDialog.setNegativeButton(Constants.CANCEL, new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
	}

	@Override
	public void onItemClick(int pos) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onItemListClick(ItemEditList v) {
		// TODO Auto-generated method stub
		int position = Integer.parseInt(String.valueOf(v.getTag()));
		ItemEditList tmp = (ItemEditList) v;
		switch (position) {
		case 1:
			initRolePopupWidnow(tmp.getLblVal());
			// _rolePopupWindow.showAsDropDown(tmp.getLblVal());
			break;

		case 2:
			if (RetailApplication.getShopVo().getType() == ShopVo.ZHONGBU) {
				Intent intent = new Intent(ShowUserDetailInfoActivity.this,
						UserShopSelectActivity.class);
				startActivityForResult(intent, SELECTSHOPRECODE);
	 		}
			break;

		case 3:
			initDateDialog((ItemEditList) v);
			_inDateDialog.show();
			break;

		case 4:
			initSexPopupWindow(tmp.getLblVal());
			// _sexPopupWindow.showAsDropDown(tmp.getLblVal());
			break;

		case 5:
			initBirthdayDialog((ItemEditList) v);
			_birthDayDialog.show();
			break;

		case 6:
			initIdentityPopupWindow(tmp.getLblVal());
			// _identityPopupWindow.showAsDropDown(tmp.getLblVal());
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.userShopName) {
			Intent intent = new Intent(ShowUserDetailInfoActivity.this,
					UserShopSelectActivity.class);
			startActivityForResult(intent, SELECTSHOPRECODE);
		}
	}

	/**
	 * 处理返回的结果
	 */
	Handler mSaveHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.HANDLER_SUCESS:
				ShowUserDetailInfoActivity.this.finish();
				if (oldShopId.equals(tmpShopId)) {
					// 显示进度条对话框
					RequestParameter params = new RequestParameter(true);
					params.setUrl(Constants.EMPLOYEE_INFO_DETAIL);
					params.setParam(Constants.USERID, userId);
					AsyncHttpPost httpinit = new AsyncHttpPost(
							ShowUserDetailInfoActivity.this, params, UserDetailBo.class,
							false, new RequestCallback() {
								@Override
								public void onSuccess(Object str) {
									UserDetailBo bo = (UserDetailBo) str;
									imagePath = bo.getUser().getFileName();
									EmployeeInfoActivity.instance.reList(tmpUserVo,imagePath);
								}
								@Override
								public void onFail(Exception e) {
									// TODO Auto-generated method stub
								}
							});
					httpinit.execute();
				}else {
					EmployeeInfoActivity.instance.reFreshing();
				}
				break;
			case Constants.HANDLER_FAIL:
				ToastUtil.showLongToast(ShowUserDetailInfoActivity.this,
						ErrorMsg.getErrorMsg(msg.obj.toString()));
				// 查询用户详细信息时，如果查询fail,直接退出UI
				if (msg.arg1 == 1) {
					ShowUserDetailInfoActivity.this.finish();
				}
				break;
			case Constants.HANDLER_ERROR:
				ToastUtil.showLongToast(ShowUserDetailInfoActivity.this,
						msg.obj.toString());
				break;
			}
		}
	};

	/**
	 * 添加员工信息
	 */
	public void saveUserInfo(String operate, UserVo userVo) {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.EMPLOYEE_INFO_SAVE);
		params.setParam(Constants.OPT_TYPE, operate);
		try {
			String saveStr = new GsonBuilder().serializeNulls().create()
					.toJson(userVo);
			params.setParam(Constants.USER, new JSONObject(saveStr));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		AsyncHttpPost httpsave = new AsyncHttpPost(
				ShowUserDetailInfoActivity.this, params, UserSaveBo.class,
				true, new RequestCallback() {
					// 网路请求成功
					@Override
					public void onSuccess(Object str) {
						Message msg = new Message();
						msg.obj = "保存成功";
						msg.what = Constants.HANDLER_SUCESS;
						mSaveHandler.sendMessage(msg);
					}

					@Override
					public void onFail(Exception e) {
					}
				});
		httpsave.execute();
	}

	/**
	 * 重置密码
	 */
	public void resetPassword() {
		// 显示进度条对话框
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.RESTPASSWORD);
		params.setParam(Constants.USERID, userId);
		params.setParam(Constants.PASSWORD, MD5.GetMD5Code("123456"));
		AsyncHttpPost httpinit = new AsyncHttpPost(
				ShowUserDetailInfoActivity.this, params, BaseRemoteBo.class,
				true, new RequestCallback() {
					@Override
					public void onSuccess(Object str) {
						ToastUtil.showLongToast(ShowUserDetailInfoActivity.this, "重置密码成功！");
						mUserVo.setLastVer(mUserVo.getLastVer()+1);
					}
					@Override
					public void onFail(Exception e) {
					}
				});
		httpinit.execute();
	}

	/**
	 * 获取初始化员工信息
	 */
	public void getUserInfoInit() {
		// 显示进度条对话框
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.EMPLOYEE_INFO_DETAIL);
		params.setParam(Constants.USERID, userId);
		AsyncHttpPost httpinit = new AsyncHttpPost(
				ShowUserDetailInfoActivity.this, params, UserDetailBo.class,
				true, new RequestCallback() {
					@Override
					public void onSuccess(Object str) {
						UserDetailBo bo = (UserDetailBo) str;
						mUserVo = bo.getUser();
						if (!CommonUtils.isEmpty(mUserVo.getRoleId())) {
							tmpRoleId = mUserVo.getRoleId();
						}
						if (!CommonUtils.isEmpty(mUserVo.getShopId())) {
							tmpShopId = mUserVo.getShopId();
							oldShopId = mUserVo.getShopId();
						}
						updateView();
					}
					@Override
					public void onFail(Exception e) {
						// TODO Auto-generated method stub
					}
				});
		httpinit.execute();
	}

	public void ClickListener(View v) {
		int position = Integer.parseInt(String.valueOf(v.getTag()));
		ItemEditList tmp = (ItemEditList) v;
		switch (position) {
		case 1:
			initRolePopupWidnow(tmp.getLblVal());
			break;
		case 2:
			Intent intent = new Intent(ShowUserDetailInfoActivity.this,
					UserShopSelectActivity.class);
			startActivityForResult(intent, SELECTSHOPRECODE);
			break;
		case 3:
			initDateDialog((ItemEditList) v);
			_inDateDialog.show();
			break;
		case 4:
			initSexPopupWindow(tmp.getLblVal());
			break;
		case 5:
			initBirthdayDialog((ItemEditList) v);
			_birthDayDialog.show();
			break;
		case 6:
			initIdentityPopupWindow(tmp.getLblVal());
			break;
		}
	}
	/**
	 * 删除员工信息
	 */
	public void deleteUserInfo() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.EMPLOYEE_INFO_DELETE);
		params.setParam(Constants.USERID, userId);
		AsyncHttpPost httpinit = new AsyncHttpPost(
				ShowUserDetailInfoActivity.this, params, BaseRemoteBo.class,
				true, new RequestCallback() {
					@Override
					public void onSuccess(Object str) {
						ShowUserDetailInfoActivity.this.finish();
						EmployeeInfoActivity.instance.reFreshing();
					}
					@Override
					public void onFail(Exception e) {
						// TODO Auto-generated method stub
					}
				});
		httpinit.execute();
	}

	/**
	 * 网络提交数据时，更新用户资料
	 */
	private void upDateUserVo() {
		tmpUserVo = mUserVo;
		tmpUserVo.setUserName(mEDAccount.getCurrVal());
		tmpUserVo.setName(mEDName.getCurrVal());
		tmpUserVo.setStaffId(mEDUserStaffId.getCurrVal());
		tmpUserVo.setRoleId(tmpRoleId);
		tmpUserVo.setShopId(tmpShopId);
		if (!StringUtils.isEquals(mELUserInDate.getCurrVal(), "请选择")) {
			mUserVo.setInDate(mELUserInDate.getCurrVal());
		}
		if (!StringUtils.isEquals(mELUserBirthday.getCurrVal(), "请选择")) {
			mUserVo.setBirthday(mELUserBirthday.getCurrVal());
		}
		tmpUserVo.setAddress(mEDAdress.getCurrVal());

		tmpUserVo.setMobile(mEDUserPhone.getCurrVal());
		if (!CommonUtils.isEmpty(mELUserSex.getCurrVal()))
			tmpUserVo.setSex(tmpSexId);
		if (!CommonUtils.isEmpty(mELUserIdentityType.getCurrVal())) {
			tmpUserVo.setIdentityTypeId(tmpIdentityTypeId);
			tmpUserVo.setIdentityNo(mEDIdentityNo.getCurrVal());
		}
		tmpUserVo.setPwd(null);
		tmpUserVo.setAddress(mEDAdress.getCurrVal());
		// 修改或者增加圖片
		if (mIMGPortrait.getPortrait() != null && isChange[6]) {
			tmpUserVo.setFile(mIMGPortrait.getPortrait());
			if (CommonUtils.isEmpty(tmpUserVo.getFileName())) {
				tmpUserVo.setFileName("test.jpg" + Constants.IMGENDWITH144);
			} else {
				tmpUserVo.setFileName(tmpUserVo.getFileName()+ Constants.IMGENDWITH144);
			}
			tmpUserVo.setFileOperate(Short.valueOf("1"));
			// 刪除信息
		} else if (isChange[6] && mIMGPortrait.getPortrait() == null) {
			tmpUserVo.setFile(null);
			tmpUserVo.setFileOperate(Short.valueOf("0"));
		} else {
			tmpUserVo.setFile(null);
			tmpUserVo.setFileOperate(null);
		}

	}

	/**
	 * 更新ui信息
	 */
	private void updateView() {
		String shopName = RetailApplication.getShopVo().getShopName();
		if (RetailApplication.getEntityModel()==1) {
	 		//单店
			 mELUserShop.initData(shopName,shopName);
			 mELUserShop.getImg().setVisibility(View.GONE);
			 mELUserShop.getLblVal().setTextColor(Color.parseColor("#666666"));
 		}else {
 			//连锁
 			if (RetailApplication.getShopVo().getType() == ShopVo.MENDIAN) {
 				mELUserShop.getImg().setVisibility(View.GONE);
 	        	mELUserShop.getLblVal().setTextColor(Color.parseColor("#666666"));
 			}else{
 				mELUserShop.getImg().setImageResource(R.drawable.ico_next);
 			}
 			 mELUserShop.initData(shopName,shopName);
 		}
		mEDAccount.initData(mUserVo.getUserName());
		mEDAccount.getLblVal().setTextColor(Color.parseColor("#666666"));
		mEDAccount.getLblVal().setKeyListener(null);
		mEDAccount.getLblVal().setOnFocusChangeListener(null);
		// 设置标题信息
		setTitleText(mUserVo.getName());
		mEDAdress.initData(mUserVo.getAddress());
		mEDIdentityNo.initData(mUserVo.getIdentityNo());
		mEDName.initData(mUserVo.getName());
		mEDUserPhone.initData(mUserVo.getMobile());
		mEDUserStaffId.initData(mUserVo.getStaffId());

		// 如果修改记录是，直接点击取消，恢复到初始数据
		if (tmpPortrait != null) {
			mIMGPortrait.initData(tmpPortrait);
			// 第一次，从网络上获取数据信息
		} else {
			if (!CommonUtils.isEmpty(mUserVo.getFileName())) {
				new GetImageThread(mUserVo.getFileName()).start();
			} else {
				mIMGPortrait.initData(null);
			}
		}
		tmpRoleId = mUserVo.getRoleId();
		mELUserShop.initData(tmpShopName, tmpShopName);

		if (!CommonUtils.isEmpty(mUserVo.getInDate())) {
			mELUserInDate.initData(DateUtil.timeToStrYMD_EN(Long.valueOf(mUserVo.getInDate())), DateUtil.timeToStrYMD_EN(Long.valueOf(mUserVo.getInDate())));
		}
		if (!CommonUtils.isEmpty(mUserVo.getBirthday())) {
			mELUserBirthday.initData(DateUtil.timeToStrYMD_EN(Long.valueOf(mUserVo.getBirthday())), DateUtil.timeToStrYMD_EN(Long.valueOf(mUserVo.getBirthday())));
		}

		mELUserRole.initData(getRoleName(mUserVo.getRoleId()),
				getRoleName(mUserVo.getRoleId()));

		if (mUserVo.getSex() != null)
			mELUserSex.initData(getSexName(mUserVo.getSex().toString()),
					getSexName(mUserVo.getSex().toString()));

		if (mUserVo.getIdentityTypeId() != null)
			mELUserIdentityType.initData(getIndentityName(mUserVo
					.getIdentityTypeId().toString()), getIndentityName(mUserVo
					.getIdentityTypeId().toString()));

		roleNameList.clear();
		shopNameList.clear();
		sexNameList.clear();
		indentityNameList.clear();
		if (mRoleList != null) {
			for (int i = 0; i < mRoleList.size(); i++) {
				roleNameList.add(mRoleList.get(i).getRoleName());
			}
		}
		if (mShopList != null) {
			for (int i = 0; i < mShopList.size(); i++) {
				shopNameList.add(mShopList.get(i).getShopName());
			}
		}
		if (mSexList != null) {
			for (int i = 0; i < mSexList.size(); i++) {
				sexNameList.add(mSexList.get(i).getName());
			}
		}
		if (indentityNameList != null) {
			for (int i = 0; i < mIdentityTypeList.size(); i++) {
				indentityNameList.add(mIdentityTypeList.get(i).getName());
			}
		}
		// 监听当前控件的数据值变化。
		mEDName.setIsChangeListener(this);
		mEDAccount.setIsChangeListener(this);
		mEDAdress.setIsChangeListener(this);
		mEDUserPhone.setIsChangeListener(this);
		mEDUserStaffId.setIsChangeListener(this);
		mEDIdentityNo.setIsChangeListener(this);
		mIMGPortrait.setIsChangeListener(this);
		mELUserBirthday.setIsChangeListener(this);
		mELUserIdentityType.setIsChangeListener(this);
		mELUserInDate.setIsChangeListener(this);
		mELUserRole.setIsChangeListener(this);
		mELUserSex.setIsChangeListener(this);
		mELUserShop.setIsChangeListener(this);
	}

	/**
	 * 选择角色信息
	 * 
	 * @param mELUserRole
	 */

	private void initRolePopupWidnow(final TextView mRole) {
		mSelectRole = new CommonSelectTypeDialog(
				ShowUserDetailInfoActivity.this, roleNameList);
		mSelectRole.show();
		mSelectRole.updateType(mELUserRole.getCurrVal());
		mSelectRole.getTitle().setText(
				this.getResources().getString(R.string.role_type));
		;
		mSelectRole.getConfirmButton().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mELUserRole.changeData(mSelectRole.getCurrentData(),
								mSelectRole.getCurrentData());

						tmpRoleId = mRoleList.get(
								mSelectRole.getCurrentPosition()).getRoleId();

						mSelectRole.dismiss();
					}
				});

		mSelectRole.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSelectRole.dismiss();
			}
		});
	}

	/**
	 * 选择入职时间
	 */
	private void initDateDialog(ItemEditList v) {
		if (_inDateDialog == null) {
			final ItemEditList textView = ((ItemEditList) v);
			_inDateDialog = new SelectDateDialog(
					ShowUserDetailInfoActivity.this, true);
			_inDateDialog.show();
			_inDateDialog.getTitle().setText("选择日期");
			_inDateDialog.getmClearDate().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					_inDateDialog.dismiss();
					textView.initData("请选择","请选择");
				}
				
			});
			_inDateDialog.getConfirmButton().setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String dateStr = _inDateDialog.getCurrentData();
						textView.changeData(dateStr, dateStr);
						_inDateDialog.dismiss();
					}
				});
			_inDateDialog.getCancelButton().setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						_inDateDialog.dismiss();
					}
				});
		}
	}
	/**
	 * 选择性别
	 */
	private void initSexPopupWindow(final TextView v) {
		mSelectSex = new CommonSelectTypeDialog(ShowUserDetailInfoActivity.this, sexNameList);
		mSelectSex.show();
		mSelectSex.updateType(mELUserSex.getCurrVal());
		mSelectSex.getTitle().setText(this.getResources().getString(R.string.sex_type));
		mSelectSex.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mELUserSex.changeData(mSelectSex.getCurrentData(),mSelectSex.getCurrentData());
				tmpSexId = mSexList.get(mSelectSex.getCurrentPosition()).getVal();
				mSelectSex.dismiss();
			}
		});

		mSelectSex.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSelectSex.dismiss();
			}
		});
	}
	/**
	 * 选择生日日期
	 */
	private void initBirthdayDialog(ItemEditList v) {
		if (_birthDayDialog == null) {
			final ItemEditList textView = ((ItemEditList) v);
			_birthDayDialog = new SelectDateDialog(ShowUserDetailInfoActivity.this, true);
			_birthDayDialog.show();
			_birthDayDialog.getTitle().setText("选择日期");
			_birthDayDialog.getmClearDate().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					_birthDayDialog.dismiss();
					textView.initData("请选择","请选择");
				}
			});
			_birthDayDialog.getConfirmButton().setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String dateStr = _birthDayDialog.getCurrentData();
						textView.changeData(dateStr, dateStr);
						_birthDayDialog.dismiss();
					}
				});
			_birthDayDialog.getCancelButton().setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(View v) {
					_birthDayDialog.dismiss();
				}
			});
		}
	}
	/**
	 * 证件类型
	 */
	private void initIdentityPopupWindow(final TextView v) {
		mSelectIndentity = new CommonSelectTypeDialog(ShowUserDetailInfoActivity.this, indentityNameList);
		mSelectIndentity.show();
		mSelectIndentity.updateType(mELUserIdentityType.getCurrVal());
		mSelectIndentity.getTitle().setText(this.getResources().getString(R.string.initIdentity_type));
		mSelectIndentity.getConfirmButton().setOnClickListener(
			new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DicVo identityType = mIdentityTypeList
							.get(mSelectIndentity.getCurrentPosition());
					tmpIdentityTypeId = identityType.getVal();
					mELUserIdentityType.changeData(identityType.getName(),
							identityType.getName());
					mSelectIndentity.dismiss();
				}
			});

		mSelectIndentity.getCancelButton().setOnClickListener(
			new OnClickListener() {
				@Override
				public void onClick(View v) {
					mSelectIndentity.dismiss();
				}
			});
		}

	@Override
	public void onItemIsChangeListener(View v) {
		switch (v.getId()) {
		// 员工姓名修改
		case R.id.userName:
			isChange[0] = mEDName.getChangeStatus();
			setTitleText(mEDName.getCurrVal());
			break;
		// 员工账号信息修改
		case R.id.userAccount:
			isChange[1] = mEDAccount.getChangeStatus();
			break;
		// 员工地址信息修改
		case R.id.userAdress:
			isChange[2] = mEDAdress.getChangeStatus();
			break;
		// 身份证件信息修改
		case R.id.userIdentityNo:
			isChange[3] = mEDIdentityNo.getChangeStatus();
			break;
		// 用户手机信息
		case R.id.userPhone:
			isChange[4] = mEDUserPhone.getChangeStatus();
			break;
		// 员工工号
		case R.id.userStaffCode:
			isChange[5] = mEDUserStaffId.getChangeStatus();
			break;
		case R.id.userPortrait:
			isChange[6] = mIMGPortrait.getChangeStatus();
			// 统计当前数据时候有变化
			isSaveMode = isHaveChange(isChange);
			break;
		// 员工生日修改
		case R.id.userBirthday:
			isChange[7] = mELUserBirthday.getChangeStatus();
			// 统计当前数据时候有变化
			break;
		// 证件类型修改
		case R.id.userIdentityType:
			isChange[8] = mELUserIdentityType.getChangeStatus();
			break;
		case R.id.userInDate:
			isChange[9] = mELUserInDate.getChangeStatus();
			break;
		// 员工角色修改
		case R.id.userRole:
			isChange[10] = mELUserRole.getChangeStatus();
			break;
		// 员工性别
		case R.id.userSex:
			isChange[11] = mELUserSex.getChangeStatus();
			break;
		// 员工所属店家修改
		case R.id.userShopName:
			isChange[12] = mELUserShop.getChangeStatus();
			break;
		}
		isSaveMode = isHaveChange(isChange);
		if (isSaveMode) {
			change2saveMode();
		} else {
			change2saveFinishMode();
		}
	}
	@Override
	public ImageButton change2saveFinishMode() {
		// TODO Auto-generated method stub
		userDelete.setVisibility(View.VISIBLE);
		return super.change2saveFinishMode();
	}
	// 显示保存的title工具栏
	@Override
	public ImageButton change2saveMode() {
		// TODO Auto-generated method stub
		super.change2saveMode();
		// 保存用户信信息
		userDelete.setVisibility(View.GONE);
		mRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mUserVo == null)
					mUserVo = new UserVo();
				// 如果信息符合符合规范，提交数据
				if (checkUserInfo() == null) {
					upDateUserVo();
					saveUserInfo(Constants.EDIT, tmpUserVo);
					// 添加的数据信息不符合规范
				} else {
					ToastUtil.showLongToast(ShowUserDetailInfoActivity.this,
							checkUserInfo());
				}
			}
		});
		// 取消编辑信息，都返回到原始的数据结果
		mLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// updateView();
				// change2saveFinishMode();
				ShowUserDetailInfoActivity.this.finish();
			}
		});
		// 不需要返回结果。
		return null;
	}
	@Override
	public void onItemEditTextChange(ItemEditText obj) {
		switch (obj.getId()) {
		// 用户名修改
		case R.id.userName:
			change2saveMode();
			// 保存用户信息
			mRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (mUserVo == null)
						mUserVo = new UserVo();
					// 如果信息符合符合规范，提交数据
					if (checkUserInfo() == null) {
						upDateUserVo();
						saveUserInfo(Constants.EDIT, mUserVo);
						// 添加的数据信息不符合规范
					} else {
						ToastUtil.showLongToast(
								ShowUserDetailInfoActivity.this,
								checkUserInfo());
					}
				}
			});
			break;

		default:
			break;
		}
	}

	/**
	 * 检测输入的参数是否符合要求
	 */
	private String checkUserInfo() {
		String ret = null;
		if (CommonUtils.isEmpty(mEDName.getCurrVal())) {
			ret = "请输入姓名！";
		} else if (CommonUtils.isEmpty(mEDAccount.getCurrVal())) {
			ret = "请输入用户名！";
		} else if (CommonUtils.isEmpty(mEDUserStaffId.getCurrVal())) {
			ret = "请输入员工工号！";
		} else if (CommonUtils.isEmpty(mELUserShop.getCurrVal())) {
			ret = "请选择所属店家！";
		} else if (CommonUtils.isEmpty(mELUserRole.getCurrVal())) {
			ret = "请选择角色类型！";
		}
		if (ret==null) {
			if (!CommonUtils.isEmpty(mEDUserPhone.getCurrVal())) {
				String mobiles = mEDUserPhone.getCurrVal();
				String telRegex = "1((3\\d)|(4[57])|(5[012356789])|(8\\d))\\d{8}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
				if (!mobiles.matches(telRegex)) {
					ret = "手机号码格式不对";
					return ret;
				}
			} 
			if (!CommonUtils.isEmpty(mELUserIdentityType.getCurrVal()) && mELUserIdentityType.getCurrVal().equals("身份证")) {
				if (mEDIdentityNo.getCurrVal().length() != 0 && mEDIdentityNo.getCurrVal().length() != 15 && mEDIdentityNo.getCurrVal().length() != 18) {
					ret = "身份证号码是15或者18位";
					return ret;
				}
			} 
		}
		return ret;
	}

	@Override
	public void onItemEditTextChange(ItemEditList obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && data != null) {
			tmpShopId = data.getStringExtra(Constants.SHOP_ID);
			String tmpShopName = data.getStringExtra(Constants.SHOPCOPNAME);
			mELUserShop.changeData(tmpShopName, tmpShopName);
		}
		/**
		 * 因为两种方式都用到了startActivityForResult方法，这个方法执行完后都会执行onActivityResult方法，
		 * 所以为了区别到底选择了那个方式获取图片要进行判断
		 * ，这里的requestCode跟startActivityForResult里面第二个参数对应
		 */
		else if (requestCode == CAPTURERETURNCODE) {
			// 获得图片的uri
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/mytest.jpg");
			// startPhotoZoom(data.getData());
			startPhotoZoom(Uri.fromFile(temp));

			// 将图片开始crop
		} else if (requestCode == ALBUMRETURNCODE && data != null) {
			startPhotoZoom(data.getData());
		}
		// 取得裁剪后的图片
		else if (requestCode == IMAGECROP && data != null) {
			if (data != null) {
				setPicToView(data);
			}
		}

	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 144);
		intent.putExtra("outputY", 144);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, IMAGECROP);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			// Drawable drawable = new BitmapDrawable(photo);

			/*
			 * ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 * photo.compress(Bitmap.CompressFormat.JPEG, 60, stream); byte[] b
			 * = stream.toByteArray(); // 将图片流以字符串形式存储下来 tp = new
			 * String(Base64Coder.encodeLines(b));
			 * 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了， 服务器处理的方法是服务器那边的事
			 * 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换 为我们可以用的图片类型 Bitmap
			 * dBitmap = BitmapFactory.decodeFile(tp); Drawable drawable = new
			 * BitmapDrawable(dBitmap);
			 */
			mIMGPortrait.changeData(photo);

		}
	}

	public void addClick(View v) {
		switch (Integer.parseInt(String.valueOf(v.getTag()))) {

		case ADD_PHOTO:
			showAddMenu();
			break;
		case FROM_CAPTURE:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 下面这句指定调用相机拍照后的照片存储的路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
					Environment.getExternalStorageDirectory(), "mytest.jpg")));
			startActivityForResult(intent, CAPTURERETURNCODE);

			if (mPw != null && mPw.isShowing()) {
				mPw.dismiss();
			}
			break;

		case FROM_ABLUM:
			Intent intent1 = new Intent(Intent.ACTION_PICK, null);
			intent1.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent1, ALBUMRETURNCODE);

			if (mPw != null && mPw.isShowing()) {
				mPw.dismiss();
			}
			break;
		case ADD_CANCEL:
			if (mPw != null && mPw.isShowing()) {
				mPw.dismiss();
			}
			break;
		}

	}

	private void showAddMenu() {
		if (addDialog == null)
			addDialog = new AddImageDialog(ShowUserDetailInfoActivity.this);
		addDialog.show();
		// 从相册添加图片
		// 如果没有头像信息，不显示删除按钮
		if (mIMGPortrait.getBitmapPortrait() == null) {
			mIMGPortrait.getImg().setVisibility(View.INVISIBLE);
		} else {
			// addDialog.getAddFromCapture().setBackgroundResource(R.drawable.noradius);
			mIMGPortrait.getImg().setVisibility(View.VISIBLE);
		}
		addDialog.getAddFromAlbum().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent1, ALBUMRETURNCODE);
				if (addDialog != null)
					addDialog.dismiss();
			}
		});
		// 从相机添加图片
		addDialog.getAddFromCapture().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 下面这句指定调用相机拍照后的照片存储的路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment
								.getExternalStorageDirectory(), "mytest.jpg")));
				startActivityForResult(intent, CAPTURERETURNCODE);
				if (addDialog != null)
					addDialog.dismiss();
			}
		});

		addDialog.getCancelButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addDialog.dismiss();
			}
		});

	}

	/**
	 * 该线程从网络上获取员工的图片信息
	 * 
	 * @author Administrator
	 * 
	 */

	public class GetImageThread extends Thread {

		private String path;

		GetImageThread(String path) {
			this.path = path;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Message msg = new Message();
			msg.what = 1;
			byte[] image = null;

			try {
				image = getImage(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg.obj = image;
			mHandler.sendMessage(msg);
		}

		Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				Bitmap myBitMap;
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					myBitMap = getPicFromBytes((byte[]) msg.obj, null);
					mIMGPortrait.initData(myBitMap);
					break;
				default:
					break;
				}

			}

		};

	}

	@Override
	public void onItemEditTextChange(SwitchRowItemEditText obj) {
		// TODO Auto-generated method stub

	}
}
