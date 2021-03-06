package com.dfire.retail.app.manage.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.shopchain.ShopInfoActivity;
import com.dfire.retail.app.manage.data.ReceiptVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;

public class SettingActivity extends TitleActivity implements OnClickListener {

	public static final int[][] RES = new int[][]{
		/*setting中位置,  图片, 主标题, 副标题, 小图标 */
		{R.id.systemParamenter, R.drawable.setting_params, R.string.param_setting, R.string.param_setting_sub},/* 参数设置 */
		{R.id.shopInformation, R.drawable.setting_account_info, R.string.account_info, R.string.account_info_sub},/* 店家信息 */
		{R.id.busnessMode, R.drawable.setting_business_model, R.string.business_setting, R.string.business_setting_subhead},/* 商业模式设置 */

		{R.id.printPatern, R.drawable.setting_receipt, R.string.receipt_setting, R.string.receipt_setting_sub},/* 小票设置 */

		{R.id.clearData, R.drawable.setting_data_clear, R.string.data_clear, R.string.data_clear_subhead}, /* 数据清理 */
		{R.id.oprateLog, R.drawable.setting_operate_log, R.string.operate_log, R.string.operate_log_sub},/* 查看操作日志 */
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_main);
		setTitleRes(R.string.setting_main);
		for (int[] item : RES) {
			RelativeLayout layout = (RelativeLayout) findViewById(item[0]);
			ImageView imageView = (ImageView) layout.findViewById(R.id.setting_item_img);
			TextView mainText = (TextView) layout.findViewById(R.id.setting_item_main_title);
			TextView subText = (TextView) layout.findViewById(R.id.setting_item_subhead_title);
			//            ImageView lockImage = (ImageView) layout.findViewById(R.id.setting_item_lock);

			imageView.setImageResource(item[1]);
			mainText.setText(item[2]);
			subText.setText(item[3]);
			layout.setOnClickListener(this);

		}
		
		showBackbtn();
	}


	private void getParams() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.GET_CONFIG_DETAIL);
		params.setParam(Constants.SHOP_ID, RetailApplication.getShopVo().getShopId());
		new AsyncHttpPost(this, params, new RequestResultCallback() {

			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(SettingActivity.this)) {
					return;
				}
				startActivity(new Intent(SettingActivity.this, ParamSettingActivity.class)
				.putExtra(Constants.JSON, str));
				
			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(SettingActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}		}).execute();
	}

	@Override
	public void onClick(View view) {
		Class<?> tag = null;
		switch (view.getId()) {
		case R.id.shopInformation:
			tag = ShopInfoActivity.class;
			break;
		case R.id.systemParamenter :
			getParams();
			return;
		case R.id.printPatern :
			getOriginSetting();
			return;
		case R.id.busnessMode :
			tag = BusinessSettingActivity.class;
			break;
		case R.id.oprateLog:
			tag = OperateLogSettingActivity.class;
			break;
		case R.id.clearData:
			tag = DataClearSettingActivity.class;
			break;
		default:
			break;
		}

		Intent intent = new Intent();
		if (tag != null) {
			intent.setClass(this, tag);
			startActivity(intent);
		}
	}
	
	private void getOriginSetting() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.GET_RECEIPT);
		new AsyncHttpPost(this, params, new RequestResultCallback() {

			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(SettingActivity.this)) {
					return;
				}
				ReceiptVo receiptSetting = (ReceiptVo) ju.get(ReceiptVo.class);
				startActivity(new Intent(SettingActivity.this, ReceiptSettingActicity.class)
				.putExtra(Constants.RECEIPT, receiptSetting));
			}

			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(SettingActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}).execute();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//陆哲琪 11.3注释 这段代码不需要。
//		getProgressDialog().setCancelable(false);
//		// progressDialog.setCanceledOnTouchOutside(false);
//		getProgressDialog()
//				.setMessage(getResources().getText(R.string.update_incomemsg));
//		getProgressDialog().show();
//		
//		//传递请求参数
//		RequestParameter parameters = new RequestParameter(true);
//		parameters.setUrl(Constants.SHOPLIST);
//		parameters.setParam("keyWord", getMyApp().getShopVo().getShopId());
//		parameters.setParam("shopId", getMyApp().getShopVo().getParentId());
//		parameters.setParam("currentPage", Integer.valueOf(1));
//		//请求商店list
//
//		AsyncHttpPost httppost = new AsyncHttpPost(parameters,
//        new RequestResultCallback() {
//	        @Override
//	        public void onSuccess(String str) {
//                Log.i("results setting", str);
//                Message msg = new Message();
//                mIncomeHandler.sendMessage(msg);
//	        }
//	        @Override
//	        public void onFail(Exception e) {
//                e.printStackTrace();
//                Message msg = new Message();
//                msg.what = Constants.HANDLER_FAIL;
//                msg.obj = e.getMessage();
//                mIncomeHandler.sendMessage(msg);
//	        }
//        });
//		httppost.execute();
	}

	/**
	 * 处理返回的结果，如果成功解析网络返回的json数据
	 */
	Handler mIncomeHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			getProgressDialog().dismiss();
			
			switch(msg.what){
			case Constants.HANDLER_SUCESS:								

					
				break;
				
            case Constants.HANDLER_FAIL:
                if (msg.obj != null) {
                    ToastUtil.showShortToast(getApplicationContext(),
                            msg.obj.toString());
                } else {
                    ToastUtil.showShortToast(getApplicationContext(), "网络请求失败");
                }
                break;
            case Constants.HANDLER_ERROR:
                if (msg.obj != null) {
                    ToastUtil.showShortToast(getApplicationContext(),
                            msg.obj.toString());
                } else {
                    ToastUtil.showShortToast(getApplicationContext(), "网络请求错误");
                }
                break;

			}
		}
	};
	


}
