/**
 * 
 */
package com.dfire.retail.app.manage;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Looper;

import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.ProvinceVo;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.ShopInfo;
import com.dfire.retail.app.manage.global.UserInfo;
import com.dfire.retail.app.manage.netData.LoginResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 应用类
 * 
 * @author qiuch
 * 
 */
public class RetailApplication extends Application {

	public static final Integer DANDIAN = 1;
	public static final Integer LIANSUO = 2;

	public static List<Activity> mActivityList = new ArrayList<Activity>();
	public static Map<String, Integer> mUserActionMap;// 用户权限
	public static UserInfo mUserInfo;
	public static ShopInfo mShopInfo;
	public static List<ProvinceVo> mProvinceVo = null;
	public static String mSessionId = "";
	public static LoginResult loginResult;
	private static List<DicVo> mShopTypeList;
	private static ShopVo shop;

	private static Integer entityModel; 

	private HashMap<String,List<Object>> objectMap=new HashMap<String,List<Object>>();
	
	private HashMap<String,Object> objMap=new HashMap<String,Object>();
	
	
	public static List<RoleVo> roleList;
	public static List<AllShopVo> shopList;
	public static List<AllShopVo> companyShopList;
	public static List<DicVo> sexList;
	public static List<DicVo> identityTypeList;
	
	public static String userName;
	public static String password;
	public static String code;
	
	@Override
	public void onCreate() {
		super.onCreate();
		// 出现应用级异常时的处理
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				throwable.printStackTrace();
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 弹出报错并强制退出的对话框
						if (mActivityList.size() > 0) {
							Looper.prepare();
							new AlertDialog.Builder(getCurrentActivity()).setTitle(R.string.app_name).setMessage(R.string.exception)
									.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// 强制退出程序
											finish();
										}
									}).show();
							Looper.loop();
						}
					}
				}).start();
			}
		});
		start();
		initImageLoader();	
	}
	private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.showImageForEmptyUri(R.drawable.pic_none)
		.showImageOnFail(R.drawable.pic_none)
		.showStubImage(R.drawable.pic_none)
		.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.defaultDisplayImageOptions(defaultOptions)
		.build();
		ImageLoader.getInstance().init(config);
	}

	private void initUserInfo() {
		if (mUserInfo == null) {
			// mUserInfo = new UserInfo(getApplicationContext());
		}
	}

	private void initShopInfo() {
		if (mShopInfo == null) {
			// mUserInfo = new UserInfo(getApplicationContext());
		}
	}

	// 生成Activity存入列表
	public static void addCurrentActivity(Activity activity) {
		mActivityList.add(activity);
	}

	// 获取当前Activity对象
	public static void removeActivity(Activity activity) {
		mActivityList.remove(activity);
	}

	// 获取当前Activity对象
	public static Activity getCurrentActivity() {
		if (mActivityList.size() > 0) {
			return mActivityList.get(mActivityList.size() - 1);
		}
		return null;
	}

	// 清空Activity列表
	public static void clearActivityList() {
		for (int i = 0; i < mActivityList.size(); i++) {
			Activity activity = mActivityList.get(i);
			activity.finish();
		}

		mActivityList.clear();
	}

	// 退出程序处理
	public static void finish() {
		clearActivityList();
		System.gc();
		System.exit(0);
	}

	/**
	 * 启动程序时的处理
	 */
	public void start() {

		// 文件路径设置
		String parentPath = null;

		// 存在SDCARD的时候，路径设置到SDCARD
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			parentPath = Environment.getExternalStorageDirectory().getPath() + File.separator + getPackageName();

			// 不存在SDCARD的时候，路径设置到ROM
		} else {
			parentPath = Environment.getDataDirectory().getPath() + "/data/" + getPackageName();

		}

		// // 临时文件路径设置
		// Settings.TEMP_PATH = parentPath + "/.tmp";
		// // 图片缓存路径设置
		// Settings.PIC_PATH = parentPath + "/.pic";
		// // 更新APK路径设置
		// Settings.VIDEO_PATH = parentPath + "/.video";
		//
		// // 创建各目录
		// new File(Settings.TEMP_PATH).mkdirs();
		// new File(Settings.PIC_PATH).mkdirs();
		// new File(Settings.VIDEO_PATH).mkdir();

		initUserInfo();
		initShopInfo();
	}
	


	public static UserInfo getmUserInfo() {
		return mUserInfo;
	}

	public static void setUserInfo(UserInfo userInfo) {
		RetailApplication.mUserInfo = userInfo;
	}

	public static ShopInfo getmShopInfo() {
		return mShopInfo;
	}

	public static void setmShopInfo(ShopInfo mShopInfo) {
		RetailApplication.mShopInfo = mShopInfo;
	}

	public static String getmSessionId() {
		return mSessionId;
	}

	public static void setmSessionId(String mSessionId) {
		RetailApplication.mSessionId = mSessionId;
	}

	public static LoginResult getLoginResult() {
		return loginResult;
	}

	public static void setLoginResult(LoginResult loginResult) {
		mShopInfo = loginResult.getShop();
		mUserInfo = loginResult.getUser();
		mSessionId = loginResult.getJsessionId();
		RetailApplication.loginResult = loginResult;
	}

	public static void setShopVo(ShopVo shop) {
		RetailApplication.shop = shop;
	}
	
	public static ShopVo getShopVo() {
		return RetailApplication.shop;
	}

	public static void setEntityModel(Integer entityModel) {
		RetailApplication.entityModel = entityModel;
	}

	/**
	 * @return the entityModel
	 */
	public static Integer getEntityModel() {
		return entityModel;
	}
	/**
	 * @return the objectMap
	 */
	public HashMap<String, List<Object>> getObjectMap() {
		return objectMap;
	}
	/**
	 * @param objectMap the objectMap to set
	 */
	public void setObjectMap(HashMap<String, List<Object>> objectMap) {
		this.objectMap = objectMap;
	}
	/**
	 * @return the objMap
	 */
	public HashMap<String, Object> getObjMap() {
		return objMap;
	}
	/**
	 * @param objMap the objMap to set
	 */
	public void setObjMap(HashMap<String, Object> objMap) {
		this.objMap = objMap;
	}

	/**
	 * 设置省份区域信息
	 * @param provinceVo
	 */
	public static  void setProvinceVo(List<ProvinceVo> provinceVo){
		RetailApplication.mProvinceVo = provinceVo;
	}
	
	/**
	 * 返回省份城市区域地址信息
	 * @return
	 */
	public static List<ProvinceVo> getProvinceVo() {
		return mProvinceVo;
	}
	
	/**
	 * 保存门店类型信息
	 * @param provinceVo
	 */
	public static  void setShopTypeList(List<DicVo> listDicVo){
		RetailApplication.mShopTypeList = listDicVo;
	}
	
	/**
	 * 返回门店类型列表
	 * @return
	 */
	public static List<DicVo> getShopTypeList() {
		return mShopTypeList;
	}
	public static List<RoleVo> getRoleList() {
		return roleList;
	}
	public static void setRoleList(List<RoleVo> roleList) {
		RetailApplication.roleList = roleList;
	}
	public static List<AllShopVo> getShopList() {
		return shopList;
	}
	public static void setShopList(List<AllShopVo> shopList) {
		RetailApplication.shopList = shopList;
	}
	public static List<DicVo> getSexList() {
		return sexList;
	}
	public static void setSexList(List<DicVo> sexList) {
		RetailApplication.sexList = sexList;
	}
	public static List<DicVo> getIdentityTypeList() {
		return identityTypeList;
	}
	public static void setIdentityTypeList(List<DicVo> identityTypeList) {
		RetailApplication.identityTypeList = identityTypeList;
	}
	public static List<AllShopVo> getCompanyShopList() {
		return companyShopList;
	}
	public static void setCompanyShopList(List<AllShopVo> companyShopList) {
		RetailApplication.companyShopList = companyShopList;
	}
	
	
	
}
