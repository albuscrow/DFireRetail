package com.dfire.retail.app.manage.activity.messagemanage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.DateDialog;
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
public class CopyOfMessag extends TitleActivity implements OnClickListener {
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
		/*	private ShopListAdapter shopListAdapter;*/
			private AlertDialog adailog;
			private DateDialog mDateDialog;
			private ImageButton mBack;
			private ImageButton mRight;
			private int noticeType;
			private String noticeId;
			private TextView message_noticeId;
			private TextView message_lastV;
			private NoticeVo noticeVos;
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_message_manager_add);
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
				message_Content.setFocusable(false);
				message_delete = (TextView) findViewById(R.id.message_delete);
				message_delete.setVisibility(View.GONE);
				mBack = (ImageButton) findViewById(R.id.title_back);
				mRight = (ImageButton) findViewById(R.id.title_right);
				message_noticeId = (TextView) findViewById(R.id.message_noticeId);
				message_lastV = (TextView) findViewById(R.id.message_lastV);
			}

			public void setUi() {
				noticeVonoti = new NoticeVo();
				shops = new ArrayList<ShopVo>();
				noticeVo = new ArrayList<NoticeVo>();
				noticeType = 1;
				currentPages = 1;
				getShop1(true);
				 showBackbtn(); 
				noticeVos=new NoticeVo();
				mBack.setOnClickListener(this);
				mRight.setOnClickListener(this);
				message_title.setOnClickListener(this);
				
			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.title_back:
					finish();
					break;
				case R.id.message_title:
					break;
				default:
					break;
				}

			}

		

			private void getShop1(final boolean isInit) {

				if (isInit) {
					RequestParameter params = new RequestParameter(true);
					params.setUrl(Constants.SHOP_LIST_URL);
					params.setParam(Constants.SHOP_ID, RetailApplication.getmShopInfo()
							.getShopId());
					params.setParam(Constants.PAGE, currentPages++);
					new AsyncHttpPost(params, new RequestResultCallback() {
						@SuppressWarnings("unchecked")
						@Override
						public void onSuccess(String str) {
							JsonObject jo = new JsonParser().parse(str)
									.getAsJsonObject();
							JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
							String returnCode = null;
							if (jsonElement != null) {
								returnCode = jsonElement.getAsString();
							}
							if (returnCode == null
									|| !returnCode.equals(Constants.LSUCCESS)) {
								ToastUtil.showShortToast(
										CopyOfMessag.this,
										Constants.getErrorInf(null, null));
								return;
							}
							List<ShopVo> shopV = new ArrayList<ShopVo>();
							shopV.clear();
							if (isInit != false) {
								shopV.addAll((Collection<? extends ShopVo>) new Gson()
										.fromJson(jo.get("shopList"),
												new TypeToken<List<ShopVo>>() {
												}.getType()));
								int pageSize = jo.get(Constants.PAGE_SIZE).getAsInt();
								if (pageSize >= currentPages) {
									if (shopV == null) {
										getShop1(false);
									} else {

										shops.addAll(shopV);
										getShop1(true);

									}
								}
							}
						}

						@Override
						public void onFail(Exception e) {
							ToastUtil.showShortToast(CopyOfMessag.this,
									Constants.getErrorInf(null, null));
							e.printStackTrace();
						}
					}).execute();
				}
			}

		
			/**
			 * 显示消息的列表
			 */
			private class ShopListAdapter extends BaseAdapter {
				private List<ShopVo> shops;

				public ShopListAdapter(List<ShopVo> shops) {

					this.shops = shops;

				}

				@Override
				public int getCount() {

					return shops.size();

				}

				@Override
				public Object getItem(int position) {

					return shops.get(position);
				}

				@Override
				public long getItemId(int position) {

					return position;
				}

				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					LayoutInflater inflater = LayoutInflater
							.from(CopyOfMessag.this);

					View v = (View) inflater.inflate(R.layout.activity_message_style,
							null);

					TextView itemsshopName = (TextView) v.findViewById(R.id.shopName);
					TextView shopid = (TextView) v.findViewById(R.id.shopid);
					itemsshopName.setText(shops.get(position).getShopName());
					shopid.setText(shops.get(position).getShopId());

					return v;
				}

			}
			//
			private void openDate() {
				mDateDialog = new DateDialog(CopyOfMessag.this);
				mDateDialog.show();
				mDateDialog.getConfirmButton().setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								String date = mDateDialog.getPushDate();
								message_date.setText(date);
								mDateDialog.dismiss();
							}
						});
				mDateDialog.getCancelButton().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mDateDialog.dismiss();
					}
				});
			}

		
			private void getMessageList() {
				System.out.println(noticeId);
				RequestParameter params = new RequestParameter(true);
				params.setUrl(Constants.BASE_URL + "notice/detail");
				params.setParam("currentPage", currentPage++);
				params.setParam("noticeType", noticeType);
				params.setParam("noticeId", noticeId);
				new AsyncHttpPost(params, new RequestResultCallback() {
					public void onSuccess(String str) {
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
								 message_shop.setText(noticeVos.getAcceptShopName()) ;
								 message_shop_noti.setText(noticeVos.gettargetShopId());
								 message_title.setText(noticeVos.getNoticeTitle());
								 message_Content.setText(noticeVos.getNoticeContent());
								 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								message_date.setText(sdf.format(new Date(Long.parseLong(noticeVos.getPublishTime()))).substring(0, 10));
								message_time.setText(sdf.format(new Date(Long.parseLong(noticeVos.getPublishTime()))).subSequence(11, 16));
								message_lastV.setText(noticeVos.getLastVer());
								message_noticeId.setText(noticeVos.getNoticeId());
								setTitleText(noticeVos.getNoticeTitle());
							 }
							 
						if (returnCode == null
								|| !returnCode.equals(Constants.LSUCCESS)) {
							ToastUtil.showShortToast(CopyOfMessag.this,
									Constants.getErrorInf(null, null));
							return;
						}

						
						}
					
					}
					@Override
					public void onFail(Exception e) {
						ToastUtil.showShortToast(CopyOfMessag.this,
								Constants.getErrorInf(null, null));
						e.printStackTrace();

					}
				}).execute();
			
			}
		}
