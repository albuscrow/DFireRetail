package com.dfire.retail.app.manage.activity.messagemanage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.logisticmanager.SelectShopActivity;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.NoticeVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
	
	public class MseeageUpdateActivity extends TitleActivity implements OnClickListener {
		private TextView message_shop;
		private TextView message_title;
		private TextView message_shop_noti;
		private EditText message_title_Content_edit;
		private TextView message_date;
		private TextView message_time;
		private TextView message_title_content;
		private EditText message_Content;
		private TextView message_delete;
		private String shiopNaem;
		private List<NoticeVo> noticeVo;
		private NoticeVo noticeVonoti;
		private String targetShopId;
		private int currentPages;
		private int currentPage;
		private List<ShopVo> shops;
		private AlertDialog adailog;
		private ImageButton mBack;
		private ImageButton mRight;
		private int noticeType;
		private String noticeId;
		private TextView message_noticeId;
		private TextView message_lastV;
		private NoticeVo noticeVos;
		private ProgressDialog progressDialog;
		private MessageDateDialog mDateDialog;
		private SelectTimeDialog mTimeDialog;
		private String selectDate = null;
		private Long sendEndTime;
		private String currentShop;
		private AllShopVo allShopVo;
		private ImageButton title_left;
		private String selectTime= null;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_message_manager_update);
			findview();
			setUi();
			noticeId = getIntent().getExtras().getString("noticeId");
			getMessageList();
		}

		public void findview() {
			message_shop = (TextView) findViewById(R.id.message_shop);
			message_shop_noti = (TextView) findViewById(R.id.message_shop_noti);
			message_title = (TextView) findViewById(R.id.message_title);
			message_title_Content_edit = (EditText) findViewById(R.id.message_title_Content_edit);
			message_date = (TextView) findViewById(R.id.message_date);
			message_time = (TextView) findViewById(R.id.message_time);
			message_title_content = (TextView) findViewById(R.id.message_title_content);
			message_Content = (EditText) findViewById(R.id.message_Content);
			message_delete = (TextView) findViewById(R.id.message_delete);
			mBack = (ImageButton) findViewById(R.id.title_back);
			mRight = (ImageButton) findViewById(R.id.title_right);
			message_noticeId = (TextView) findViewById(R.id.message_noticeId);
			message_lastV = (TextView) findViewById(R.id.message_lastV);
			title_left=(ImageButton)findViewById(R.id.title_left);
		}

		public void setUi() {
			noticeVonoti = new NoticeVo();
			shops = new ArrayList<ShopVo>();
			noticeVo = new ArrayList<NoticeVo>();
			noticeType = 1;
			currentPages = 1;
			currentPage = 1;
			change2saveMode();
			this.progressDialog = new ProgressDialog(MseeageUpdateActivity.this);
			this.progressDialog.setCancelable(false);
			this.progressDialog.setCanceledOnTouchOutside(false);
			this.progressDialog.setMessage("加载中，请稍后。。。");
			this.mDateDialog = new MessageDateDialog(MseeageUpdateActivity.this,true);//日期
			this.mTimeDialog = new SelectTimeDialog(MseeageUpdateActivity.this,true);//时间
			noticeVos=new NoticeVo();
			message_date.setOnClickListener(this);
			message_time.setOnClickListener(this);
			message_shop.setOnClickListener(this);
			mBack.setOnClickListener(this);
			mRight.setOnClickListener(this);
			message_title.setOnClickListener(this);
			message_delete.setOnClickListener(this);
			title_left.setOnClickListener(this);
			currentShop=RetailApplication.getmShopInfo().getShopId();
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.message_shop:
				Intent selectIntent =new Intent(MseeageUpdateActivity.this,SelectShopActivity.class);
				selectIntent.putExtra("selectShopId", currentShop);
				selectIntent.putExtra("activity", "mseeageUpdateActivity");
				startActivityForResult(selectIntent, 100);
				break;
			case R.id.message_date:
				pushDate();
				break;
			case R.id.message_time:
				pushTime();
				break;
			case R.id.title_right:
				save();
				break;
			case R.id.title_back:
				finish();
				break;
			case R.id.message_delete:
				delMessageList();
				break;
			case R.id.title_left:
				finish();
				break;
			case R.id.message_title:
				message_title.setVisibility(View.GONE);
				message_title_Content_edit.setText(message_title.getText().toString());
				message_title_Content_edit.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}

		}
		private void save() {
			String dateortime = message_date.getText().toString()+" "+message_time.getText().toString();
			System.out.println(dateortime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Long ltime = null;
			try {
				System.out.println(sdf.parse(dateortime));
				
				ltime = sdf.parse(dateortime).getTime();
				noticeVonoti.setPublishTime(ltime + "");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Date date = new Date();
			if (Long.valueOf(date.getTime()) < ltime) {
				if ((message_title_Content_edit.getText().toString()).length() > 0
						&& (message_title_content.getText().toString()).length() > 0) {
					noticeVonoti.settargetShopId(message_shop_noti.getText()
							.toString());
					noticeVonoti.setNoticeTitle(message_title_Content_edit
							.getText().toString());
					noticeVonoti.setNoticeContent(message_Content.getText()
							.toString());
					noticeVonoti.setLastVer(message_lastV.getText().toString());
					noticeVonoti.setNoticeId(message_noticeId.getText().toString());
					commitMessageList();
				} else {
					ToastUtil.showShortToast(MseeageUpdateActivity.this,
							"消息标题或消息内容不能为空");
				}
			} else {
				ToastUtil.showShortToast(MseeageUpdateActivity.this,
						"你选择的时间小于当前时间");
			}

		}
		protected Dialog onCreateDialog() {
			// 用来获取时间的
			Calendar calendar = Calendar.getInstance();

			Dialog dialog = null;

			TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker timerPicker, int hourOfDay,
						int minute) {
					message_time.setText(+hourOfDay + ":" + minute);

				}
			};
			dialog = new TimePickerDialog(this, timeListener,
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE), false); // 是否为二十四制

			return dialog;
		}

		/*
		 * 向服务器发送请求
		 */
		private void commitMessageList() {
			progressDialog.show();
			RequestParameter params = new RequestParameter(true);
			params.setUrl(Constants.BASE_URL + "notice/save");
			params.setParam("operateType", "edit");
			params.setParam("noticeType", 1);
			try {
				params.setParam("notice", new JSONObject(new GsonBuilder()
						.serializeNulls().create().toJson(noticeVonoti)));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			new AsyncHttpPost(params, new RequestResultCallback() {
				public void onSuccess(String str) {
					progressDialog.dismiss();
					JsonObject jo = new JsonParser().parse(str).getAsJsonObject();

					JsonElement jsonElement = jo.get(Constants.RETURN_CODE);

					String returnCode = null;
					if (jsonElement != null) {
						returnCode = jsonElement.getAsString();

						if (returnCode.equals("success")) {
							finish();
						}

					}
					if (returnCode == null
							|| !returnCode.equals(Constants.LSUCCESS)) {
						ToastUtil.showShortToast(MseeageUpdateActivity.this,
								Constants.getErrorInf(null, null));
						return;
					}

				}

				@Override
				public void onFail(Exception e) {
					progressDialog.dismiss();
					ToastUtil.showShortToast(MseeageUpdateActivity.this,
							Constants.getErrorInf(null, null));
					e.printStackTrace();

				}
			}).execute();

		}
		/*
		 * 向服务器发送请求
		 */
		private void getMessageList() {
			progressDialog.show();
			RequestParameter params = new RequestParameter(true);
			params.setUrl(Constants.BASE_URL + "notice/detail");
			params.setParam("noticeId", noticeId);
			new AsyncHttpPost(params, new RequestResultCallback() {
				public void onSuccess(String str) {
					progressDialog.dismiss();
					System.out.println(str);

					JsonObject jo = new JsonParser().parse(str).getAsJsonObject();

					JsonElement jsonElement = jo.get(Constants.RETURN_CODE);

					String returnCode = null;
					if (jsonElement != null) {
						returnCode = jsonElement.getAsString();
						System.out.println(returnCode);
						 noticeVos=new Gson().fromJson(
								jo.get("notice"),
								new TypeToken<NoticeVo>() {
								}.getType());
						 if(noticeVos!=null){
							System.out.println(noticeVos.toString()); 
							for (ShopVo shop : shops) {
								if(shop.getShopId()==noticeVos.gettargetShopId()){
									 message_shop.setText(shop.getShopName()) ;
								}
							}
							message_shop.setText("请选择门店");
							 message_shop_noti.setText(noticeVos.gettargetShopId());
							 message_title.setText(noticeVos.getNoticeTitle());
							 message_Content.setText(noticeVos.getNoticeContent());
							 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							message_date.setText(sdf.format(new Date(Long.parseLong(noticeVos.getPublishTime()))).substring(0, 10));
							message_time.setText(sdf.format(new Date(Long.parseLong(noticeVos.getPublishTime()))).subSequence(11, 16));
							message_lastV.setText(noticeVos.getLastVer());
							message_noticeId.setText(noticeVos.getNoticeId());
						 }
						 
					if (returnCode == null
							|| !returnCode.equals(Constants.LSUCCESS)) {
						progressDialog.dismiss();
						ToastUtil.showShortToast(MseeageUpdateActivity.this,
								Constants.getErrorInf(null, null));
						return;
					}

					
					}
				
				}
				@Override
				public void onFail(Exception e) {
					ToastUtil.showShortToast(MseeageUpdateActivity.this,
							Constants.getErrorInf(null, null));
					e.printStackTrace();

				}
			}).execute();
		
		}
		/*
		 * 向服务器发送请求
		 */
		private void delMessageList() {
			progressDialog.show();
			RequestParameter params = new RequestParameter(true);
			params.setUrl(Constants.BASE_URL + "notice/delete");
			params.setParam("noticeType", "del");
			params.setParam("noticeId",message_noticeId.getText().toString() );
			params.setParam("lastVer", message_lastV.getText().toString());
			new AsyncHttpPost(params, new RequestResultCallback() {
				public void onSuccess(String str) {
					progressDialog.dismiss();
					System.out.println(str);

					JsonObject jo = new JsonParser().parse(str).getAsJsonObject();

					JsonElement jsonElement = jo.get(Constants.RETURN_CODE);

					String returnCode = null;
					if (jsonElement != null) {
						returnCode = jsonElement.getAsString();
						if(returnCode.equals("success")){
							finish();
						}
					}
							
						 
					if (returnCode == null
							|| !returnCode.equals(Constants.LSUCCESS)) {
						ToastUtil.showShortToast(MseeageUpdateActivity.this,
								Constants.getErrorInf(null, null));
						return;
					}
				
				}
				@Override
				public void onFail(Exception e) {
					progressDialog.dismiss();
					ToastUtil.showShortToast(MseeageUpdateActivity.this,
							Constants.getErrorInf(null, null));
					e.printStackTrace();

				}
			}).execute();
		
		}
		/**
		 * 弹出日期
		 */
		private void pushDate(){
			mDateDialog.show();
			mDateDialog.getTitle().setText("选择发布时间");
			mDateDialog.updateDays(selectDate);
			
			mDateDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDateDialog.dismiss();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");
					selectDate = mDateDialog.getCurrentData();
					message_date.setText(selectDate);
					if (selectDate!=null) {
						try {
							sendEndTime = (sdf.parse((selectDate+" 00:00:00"))).getTime();
						} catch (ParseException e) {
							sendEndTime = null;
						}
					}
					
				}
			});
			mDateDialog.getCancelButton().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mDateDialog.dismiss();
					mDateDialog.closeOptionsMenu();
					
				}
			});
		}
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if(resultCode==100){
				Bundle b=data.getExtras();
				allShopVo=(AllShopVo)b.getSerializable("shopVo");
				if (allShopVo!=null) {
					message_shop.setText(allShopVo.getShopName());
					message_shop_noti.setText(allShopVo.getShopId());
				
				}
			}
		}
		/**
		 * 弹出时间
		 */
		private void pushTime(){
			mTimeDialog.show();
			mTimeDialog.getTitle().setText("选择发布时间");
			mTimeDialog.updateDays(selectTime);
			mTimeDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mTimeDialog.dismiss();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");
					selectTime = mTimeDialog.getCurrentTime();
					message_time.setText(selectTime);
					if (selectTime!=null) {
						try {
							sendEndTime = (sdf.parse((selectTime+" 00:00:00"))).getTime();
						} catch (ParseException e) {
							sendEndTime = null;
						}
					}
					
				}
			});
			mTimeDialog.getCancelButton().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mTimeDialog.dismiss();
					mTimeDialog.closeOptionsMenu();
				}
			});
		}
}