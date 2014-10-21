/**
 * 
 */
package com.dfire.retail.app.manage;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Looper;

import com.dfire.retail.app.manage.activity.retailmanager.UserInfoInit;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.ShopInfo;
import com.dfire.retail.app.manage.global.UserInfo;
import com.dfire.retail.app.manage.netData.LoginResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

	public static UserInfo mUserInfo;
	public static ShopInfo mShopInfo;
	public static String mSessionId = null;
	public static LoginResult loginResult;

	private static ShopVo shop;

	private static Integer entityModel; 

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
		.showImageForEmptyUri(R.drawable.common_default_image)
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

	public static void setmUserInfo(UserInfo mUserInfo) {
		RetailApplication.mUserInfo = mUserInfo;
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
	
	

	
}