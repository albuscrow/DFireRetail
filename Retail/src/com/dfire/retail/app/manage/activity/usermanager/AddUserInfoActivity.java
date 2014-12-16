package com.dfire.retail.app.manage.activity.usermanager;

import java.io.File;
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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.ItemPortraitImage;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.AddImageDialog;
import com.dfire.retail.app.manage.common.CommonSelectTypeDialog;
import com.dfire.retail.app.manage.common.CommonUtils;
import com.dfire.retail.app.manage.common.ErrorMsg;
import com.dfire.retail.app.manage.common.SelectDateDialog;
//import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.UserVo;
import com.dfire.retail.app.manage.data.bo.UserSaveBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.util.MD5;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.google.gson.GsonBuilder;

@SuppressLint("SimpleDateFormat")
public class AddUserInfoActivity extends TitleActivity implements RequestCallback, IItemListListener,IOnItemSelectListener {

	private static final String TAG = "UserDetailInfoActivity";
	private static final int SELECTSHOPRECODE = 0;
	private static final int CAPTURERETURNCODE = 1;
	private static final int ALBUMRETURNCODE = 2;
	private ItemEditText mEDName, mEDAccount, mEDPassword, mEDAdress, mEDIdentityNo,mEDUserStaffId, mEDUserPhone;
	private ItemEditList mELUserRole, mELUserShop, mELUserInDate, mELUserSex,mELUserBirthday, mELUserIdentityType;
	private ItemPortraitImage mIMGPortrait;
	private UserVo mUserVo;

	// 弹出选择添加图片对话框
	private final static int ADD_PHOTO = 1;
	private final static int FROM_CAPTURE = 2;
	private final static int FROM_ABLUM = 3;
	private final static int ADD_CANCEL = 4;

	private PopupWindow mPw;
	private SpinerPopWindow _sexPopupWindow;
	private SpinerPopWindow _identityPopupWindow;
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

	private String tmpRoleId;
	private Integer tmpIdentityTypeId;
	private String tmpShopId;
	private Integer tmpSexId;// 性别
	SimpleDateFormat dateFormat;
	private AddImageDialog addDialog;
	private View spareLine;

	AsyncHttpPost httpsave;
	private CommonSelectTypeDialog mSelectRole;
	private CommonSelectTypeDialog mSelectSex;
	private CommonSelectTypeDialog mSelectIndentity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_user_detail_info);
		setTitleRes(R.string.add);
		tmpShopId = RetailApplication.getShopVo().getShopId();
		// 可以方便地修改日期格式
		dateFormat = new SimpleDateFormat(getResources().getString(R.string.employee_date_format));
		showBackbtn();
		findView();
		mRoleList = RetailApplication.getRoleList();
		mShopList = RetailApplication.getShopList();
		mSexList = RetailApplication.getSexList();
		mIdentityTypeList = RetailApplication.getIdentityTypeList();
		tmpRoleId = null;
		tmpIdentityTypeId = null;
		updateView();

	}

	// 查询所有控件ID
	private void findView() {

		spareLine = (View) findViewById(R.id.spareLine);
		spareLine.setVisibility(View.GONE);

		mEDName = (ItemEditText) findViewById(R.id.userName);
		mEDAccount = (ItemEditText) findViewById(R.id.userAccount);
		mEDAdress = (ItemEditText) findViewById(R.id.userAdress);
		mEDIdentityNo = (ItemEditText) findViewById(R.id.userIdentityNo);
		mEDPassword = (ItemEditText) findViewById(R.id.userPassword);
		mEDUserPhone = (ItemEditText) findViewById(R.id.userPhone);
		mEDUserStaffId = (ItemEditText) findViewById(R.id.userStaffCode);
		// 可编译的员工信息初始化
		mEDName.initLabel("员工姓名", null, Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		mEDName.setMaxLength(Constants.USERNAMEMAXLENGTH);
		mEDName.setIsChangeListener(this.getItemChangeListener());

		mEDPassword.initLabel("登录密码", "", Boolean.TRUE,
				InputType.TYPE_TEXT_VARIATION_PASSWORD);
		mEDPassword.setMaxLength(Constants.USERPASSWORDMAXLENGTH);
		mEDPassword.setDigitsAndNum(true);
		mEDPassword.setIsChangeListener(this.getItemChangeListener());

		mEDUserStaffId.initLabel("员工工号", "", Boolean.TRUE,
				InputType.TYPE_CLASS_NUMBER);
		mEDUserStaffId.setMaxLength(Constants.USERSTAFFIDMAXLENGTH);
		mEDUserStaffId.setDigitsAndNum(true);
		mEDUserStaffId.setIsChangeListener(this.getItemChangeListener());

		mEDAccount
				.initLabel("用户名", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		mEDAccount.setMaxLength(Constants.USERACCOUNTMAXLENGTH);
		mEDAccount.setDigitsAndNum(true);
		mEDAccount.setIsChangeListener(this.getItemChangeListener());

		mEDIdentityNo.initLabel("证件号码", "", Boolean.FALSE,
				InputType.TYPE_CLASS_TEXT);
		mEDIdentityNo.setMaxLength(Constants.USERIDENTITYNOMAXLENGTH);
		mEDIdentityNo.setIsChangeListener(this.getItemChangeListener());

		mEDAdress.initLabel("住址", "", Boolean.FALSE, InputType.TYPE_CLASS_TEXT);
		mEDAdress.setMaxLength(Constants.USERADDRESSMAXLENGTH);
		mEDAdress.setIsChangeListener(this.getItemChangeListener());

		mEDUserPhone.initLabel("手机", "", Boolean.FALSE,
				InputType.TYPE_CLASS_PHONE);
		mEDUserPhone.setMaxLength(Constants.USERMOBILELENGTH);
		mEDUserPhone.setIsChangeListener(this.getItemChangeListener());

		mELUserBirthday = (ItemEditList) findViewById(R.id.userBirthday);
		mELUserInDate = (ItemEditList) findViewById(R.id.userInDate);
		mELUserRole = (ItemEditList) findViewById(R.id.userRole);
		mELUserSex = (ItemEditList) findViewById(R.id.userSex);
		mELUserShop = (ItemEditList) findViewById(R.id.userShopName);
		mELUserIdentityType = (ItemEditList) findViewById(R.id.userIdentityType);

		mELUserShop.initLabel("所属店家", "", Boolean.TRUE, this);
		mELUserShop.getImg().setImageResource(R.drawable.ico_next);
		mELUserShop.setIsChangeListener(this.getItemChangeListener());

		mELUserRole.initLabel("员工角色", "", Boolean.TRUE, this);
		mELUserRole.initData("请选择", "");
		mELUserRole.setIsChangeListener(this.getItemChangeListener());
		mELUserSex.initLabel("性别", "", Boolean.FALSE, this);
		mELUserSex.initData("请选择", "");
		mELUserSex.setIsChangeListener(this.getItemChangeListener());
		mELUserInDate.initLabel("入职时间", "", Boolean.FALSE, this);
		mELUserInDate.initData("请选择", "");
		mELUserInDate.setIsChangeListener(this.getItemChangeListener());
		mELUserBirthday.initLabel("生日", "", Boolean.FALSE, this);
		mELUserBirthday.initData("请选择", "");
		mELUserBirthday.setIsChangeListener(this.getItemChangeListener());
		mELUserIdentityType.initLabel("证件类型", "", Boolean.FALSE, this);
		mELUserIdentityType.initData("请选择", "");
		mELUserIdentityType.setIsChangeListener(this.getItemChangeListener());

		mIMGPortrait = (ItemPortraitImage) findViewById(R.id.userPortrait);
		mIMGPortrait.initData(null);
		mIMGPortrait.setIsChangeListener(this.getItemChangeListener());
		mIMGPortrait.initLabel("头像", "");
		mIMGPortrait.getPortImge().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showAddMenu();
			}
		});
		// 保存用户信息
		mRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mUserVo == null)
					mUserVo = new UserVo();
				// 如果信息符合符合规范，提交数据
				if (checkUserInfo() == null) {
					addUserVo();
					saveUserInfo(Constants.ADD, mUserVo);
					// 添加的数据信息不符合规范
				} else {
					ToastUtil.showLongToast(AddUserInfoActivity.this,
							checkUserInfo());
				}
			}
		});

	}
	@Override
	public void onItemClick(int pos) {
	}
	@Override
	public void onItemListClick(ItemEditList v) {
		int position = Integer.parseInt(String.valueOf(v.getTag()));
		ItemEditList tmp = (ItemEditList) v;
		switch (position) {
		case 1:
			initRolePopupWidnow(tmp);
			break;

		case 2:
			if (RetailApplication.getShopVo().getType() == ShopVo.ZHONGBU) {
				Intent intent = new Intent(AddUserInfoActivity.this,UserShopSelectActivity.class);
				startActivityForResult(intent, SELECTSHOPRECODE);
	 		}
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

	@Override
	public void onSuccess(Object str) {
		Message msg = new Message();
		msg.what = Constants.HANDLER_SUCESS;
		mSaveHandler.sendMessage(msg);

	}

	@Override
	public void onFail(Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (getProgressDialog().isShowing()) {
			getProgressDialog().dismiss();
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
				AddUserInfoActivity.this.finish();
				EmployeeInfoActivity.instance.reFreshing();
				break;
			case Constants.HANDLER_FAIL:
				ToastUtil.showLongToast(AddUserInfoActivity.this,
						ErrorMsg.getErrorMsg(msg.obj.toString()));

				break;
			case Constants.HANDLER_ERROR:
				if (msg.obj != null) {
					ToastUtil.showLongToast(AddUserInfoActivity.this,
							msg.obj.toString());
				}
				if (httpsave != null)
					httpsave.cancel();
				break;
			}
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 退出网络请求
		if (httpsave != null)
			httpsave.cancel();
	}

	/**
	 * 添加员工信息
	 */
	public void saveUserInfo(String operate, UserVo userVo) {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.EMPLOYEE_INFO_SAVE);
		params.setParam(Constants.OPT_TYPE, operate);
		try {
			params.setParam(Constants.USER, new JSONObject(new GsonBuilder()
					.serializeNulls().create().toJson(userVo)));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		httpsave = new AsyncHttpPost(AddUserInfoActivity.this, params,
				UserSaveBo.class, true, this);
		httpsave.execute();
	}

	/**
	 * 网络提交数据时，更新用户资料
	 */
	private void addUserVo() {
		mUserVo.setUserId(null);
		mUserVo.setUserName(mEDAccount.getCurrVal().toUpperCase());
		mUserVo.setPwd(MD5.GetMD5Code(mEDPassword.getCurrVal().toUpperCase()));
		mUserVo.setName(mEDName.getCurrVal());
		mUserVo.setStaffId(mEDUserStaffId.getCurrVal());
		mUserVo.setRoleId(tmpRoleId);
		mUserVo.setShopId(tmpShopId);
		mUserVo.setAddress(mEDAdress.getCurrVal());
		if (!StringUtils.isEquals(mELUserInDate.getCurrVal(), "请选择")) {
			mUserVo.setInDate(mELUserInDate.getCurrVal());
		}
		if (!StringUtils.isEquals(mELUserBirthday.getCurrVal(), "请选择")) {
			mUserVo.setBirthday(mELUserBirthday.getCurrVal());
		}
		mUserVo.setMobile(mEDUserPhone.getCurrVal());
		mUserVo.setSex(tmpSexId);

		mUserVo.setIdentityTypeId(tmpIdentityTypeId);
		mUserVo.setIdentityNo(mEDIdentityNo.getCurrVal());
		mUserVo.setAddress(mEDAdress.getCurrVal());
		mUserVo.setLastVer(Long.valueOf(1));
		if (mIMGPortrait.getPortrait() != null) {
			mUserVo.setFile(mIMGPortrait.getPortrait());
			mUserVo.setFileName("test.png"
					+ "@1e_144w_144h_1c_0i_1o_90Q_1x.jpg");
			mUserVo.setFileOperate(Short.valueOf("1"));
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
		tmpSexId = null;

		mELUserShop.getSaveTag().setVisibility(View.GONE);
		mELUserIdentityType.getSaveTag().setVisibility(View.GONE);
		mELUserRole.getSaveTag().setVisibility(View.GONE);
		mELUserSex.getSaveTag().setVisibility(View.GONE);
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
		if (mIdentityTypeList != null) {
			for (int i = 0; i < mIdentityTypeList.size(); i++) {
				indentityNameList.add(mIdentityTypeList.get(i).getName());
			}
		}

	}

	/**
	 * 选择角色信息
	 * 
	 * @param mELUserRole
	 */

	private void initRolePopupWidnow(final ItemEditList mRole) {

		mSelectRole = new CommonSelectTypeDialog(AddUserInfoActivity.this,
				roleNameList);
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
	 * 
	 * @param v
	 */
	private void initDateDialog(ItemEditList v) {
		if (_inDateDialog == null) {
			final ItemEditList textView = ((ItemEditList) v);
			_inDateDialog = new SelectDateDialog(AddUserInfoActivity.this, true);

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
	 * 
	 * @param v
	 */
	private void initSexPopupWindow(final TextView v) {
		mSelectSex = new CommonSelectTypeDialog(AddUserInfoActivity.this,
				sexNameList);
		mSelectSex.show();
		mSelectSex.updateType(mELUserSex.getCurrVal());
		mSelectSex.getTitle().setText(this.getResources().getString(R.string.sex_type));
		mSelectSex.getConfirmButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mELUserSex.changeData(mSelectSex.getCurrentData(),
						mSelectSex.getCurrentData());

				tmpSexId = mSexList.get(mSelectSex.getCurrentPosition())
						.getVal();

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
	 * 
	 * @param v
	 */

	private void initBirthdayDialog(ItemEditList v) {
		if (_birthDayDialog == null) {
			final ItemEditList textView = ((ItemEditList) v);
			_birthDayDialog = new SelectDateDialog(AddUserInfoActivity.this,
					true);
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
	 * 
	 * @param v
	 */
	private void initIdentityPopupWindow(final TextView v) {
		mSelectIndentity = new CommonSelectTypeDialog(AddUserInfoActivity.this,
				indentityNameList);
		mSelectIndentity.show();
		mSelectIndentity.updateType(mELUserIdentityType.getCurrVal());
		mSelectIndentity.getTitle().setText(
				this.getResources().getString(R.string.initIdentity_type));
		;
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
			startPhotoZoom(Uri.fromFile(temp));

			// 将图片开始crop
		} else if (requestCode == ALBUMRETURNCODE && data != null) {
			startPhotoZoom(data.getData());
		}
		// 取得裁剪后的图片
		else if (requestCode == 3 && data != null) {
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
		startActivityForResult(intent, 3);
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

	/**
	 * 检测输入的参数是否符合要求
	 */
	private String checkUserInfo() {
		String ret = null;
		// 名字不能为空。必填
		if (CommonUtils.isEmpty(mEDName.getCurrVal())) {
			ret = "请输入姓名！";
		} else if (CommonUtils.isEmpty(mEDAccount.getCurrVal())) {
			ret = "请输入用户名！";
		} else if (CommonUtils.isEmpty(mEDPassword.getCurrVal())
				|| (mEDPassword.getCurrVal().length() < 6)) {
			if (CommonUtils.isEmpty(mEDPassword.getCurrVal())) {
				ret = "密码不能为空";
			} else {
				ret = "密码长度最少6位";
			}
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
		if (addDialog == null){
			addDialog = new AddImageDialog(AddUserInfoActivity.this);
		}
		addDialog.show();
		// 如果没有头像信息，不显示删除按钮
		if (mIMGPortrait.getBitmapPortrait() == null) {
			mIMGPortrait.getImg().setVisibility(View.INVISIBLE);
		} else {
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
}
