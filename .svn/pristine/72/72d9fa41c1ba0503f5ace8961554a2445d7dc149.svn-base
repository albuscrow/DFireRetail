package com.dfire.retail.app.manage.activity.messagemanage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemClickListener;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.logisticmanager.SelectShopActivity;
import com.dfire.retail.app.manage.activity.logisticmanager.StoreOrderActivity;
import com.dfire.retail.app.manage.common.BillStatusDialog;
import com.dfire.retail.app.manage.common.DateDialog;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.NoticeVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.StringUtils;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * The Class GoodsListActivity.
 * 
 * @author albuscrow
 * @param <MessageListAdapter>
 */
public class MessageManageraddActivity extends TitleActivity implements OnClickListener {
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
	private ShopListAdapter shopListAdapter;
	private AlertDialog adailog;
	private ImageButton mBack;
	private ImageButton mRight;
	private ImageButton title_left;
	private int noticeType;
	private AllShopVo allShopVo;
	private String noticeShopId;
	private ProgressDialog progressDialog;
	private MessageDateDialog mDateDialog;
	private String selectDate = null;
	private Long sendEndTime;
	private SelectTimeDialog mTimeDialog;
	private String selectTime= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_manager_add);
		findview();
		setUi();
		getShop1(true);
		
		
		
	}
	/**
	 * 
	 */
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
		title_left= (ImageButton) findViewById(R.id.title_left);
		
	}
	/**
	 * 
	 */
	public void setUi() {
		noticeVonoti = new NoticeVo();
		shops = new ArrayList<ShopVo>();
		noticeVo = new ArrayList<NoticeVo>();
		noticeType = 1;
		currentPages = 1;
		currentPage = 1;
		targetShopId = getIntent().getExtras().getString("noticeId");
		Date date = new Date();
		noticeShopId = RetailApplication.getShopVo().getShopId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		message_date.setText(sdf.format(date).substring(0, 10));
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		message_time.setText(sdfs.format(date).subSequence(11, 16));
		this.progressDialog = new ProgressDialog(MessageManageraddActivity.this);
		this.progressDialog.setCancelable(false);
		this.progressDialog.setCanceledOnTouchOutside(false);
		this.progressDialog.setMessage("加载中，请稍后。。。");
		this.mDateDialog = new MessageDateDialog(MessageManageraddActivity.this,true);//时间
		this.mTimeDialog = new SelectTimeDialog(MessageManageraddActivity.this,true);//时间
		change2saveMode();
		message_date.setOnClickListener(this);
		message_time.setOnClickListener(this);
		message_shop.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mRight.setOnClickListener(this);
		message_title.setOnClickListener(this);
		title_left.setOnClickListener(this);

	}
	/**
	 * 
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		case R.id.title_left:
			finish();
			break;
		case R.id.message_title:
			message_title.setVisibility(View.GONE);
			message_title_Content_edit.setVisibility(View.VISIBLE);
			break;
		case R.id.message_shop:
				Intent selectIntent =new Intent(MessageManageraddActivity.this,SelectShopActivity.class);
				selectIntent.putExtra("selectShopId", noticeShopId);
				selectIntent.putExtra("activity", "messageManageraddActivity");
				startActivityForResult(selectIntent, 100);
		default:
			break;
		}

	}
	/**
	 * 获取所有要消息的内容
	 */
	private void save() {
		String dateortime = message_date.getText().toString() + " "
				+ message_time.getText().toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Long ltime = null;
		try {

			ltime = Long.valueOf(((sdf.parse(dateortime)).getTime()));
			noticeVonoti.setPublishTime(ltime + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Date date = new Date();
		date.getTime();
		if (Long.valueOf(date.getTime()) < ltime) {
			if ((message_title_Content_edit.getText().toString()).length() > 0
					&& (message_title_content.getText().toString()).length() > 0) {
				noticeVonoti.settargetShopId(message_shop_noti.getText()
						.toString());
				noticeVonoti.setNoticeTitle(message_title_Content_edit
						.getText().toString());
				noticeVonoti.setNoticeContent(message_Content.getText()
						.toString());
				commitMessageList();
			} else {
				ToastUtil.showShortToast(MessageManageraddActivity.this,
						"消息标题或消息内容不能为空");
			}
		} else {
			ToastUtil.showShortToast(MessageManageraddActivity.this,
					"你选择的时间小于当前时间");
		}

	}
	/**
	 * 
	 * @param isInit
	 */
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
								MessageManageraddActivity.this,
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
					
					ToastUtil.showShortToast(MessageManageraddActivity.this,
							Constants.getErrorInf(null, null));
					e.printStackTrace();
				}
			}).execute();
		}
	}
	/**
	 * 显示所有门店的内容
	 */
	public void listshop() {
		LinearLayout mylayout = new LinearLayout(
				MessageManageraddActivity.this);
		mylayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		ListView listView = new ListView(MessageManageraddActivity.this);
		getShop1(true);
		shopListAdapter = new ShopListAdapter(shops);
		listView.setAdapter(shopListAdapter);
		mylayout.addView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView itemsshopName = (TextView) view
						.findViewById(R.id.shopName);
				TextView itemshopid = (TextView) view.findViewById(R.id.shopid);
				message_shop_noti.setText(itemshopid.getText().toString());
				message_shop.setText(itemsshopName.getText().toString());
				noticeVonoti.settargetShopId(itemshopid.getText().toString());

				adailog.cancel();

			}
		});
		adailog = new AlertDialog.Builder(MessageManageraddActivity.this)
				.setView(mylayout).setTitle("请选择门店")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();

					}
				}).create();
		adailog.show();
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
					.from(MessageManageraddActivity.this);

			View v = (View) inflater.inflate(R.layout.activity_message_style,
					null);

			TextView itemsshopName = (TextView) v.findViewById(R.id.shopName);
			TextView shopid = (TextView) v.findViewById(R.id.shopid);
			itemsshopName.setText(shops.get(position).getShopName());
			shopid.setText(shops.get(position).getShopId());

			return v;
		}

	}
	
	/**
	 * 时间按键
	 * @return
	 */
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

		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "notice/save");
		params.setParam("operateType", "add");
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
					ToastUtil.showShortToast(MessageManageraddActivity.this,
							Constants.getErrorInf(null, null));
					return;
				}

			}

			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(MessageManageraddActivity.this,
						Constants.getErrorInf(null, null));
				e.printStackTrace();

			}
		}).execute();

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){
			Bundle b=data.getExtras();
			allShopVo=(AllShopVo)b.getSerializable("shopVo");
			if (allShopVo!=null) {
				String sn=allShopVo.getShopName();
				 ((TextView)findViewById(R.id.message_shop)).setText(sn);
				noticeShopId = allShopVo.getShopId();
				message_shop_noti.setText(noticeShopId);
				
			}
		}
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
				TextView date_dialog_title=(TextView)findViewById(R.id.date_dialog_title);
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
