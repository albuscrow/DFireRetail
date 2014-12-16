package com.dfire.retail.app.manage.activity.messagemanage;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.SwitchRowItemEditText;
import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.logisticmanager.SelectShopActivity;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.common.SelectDateDialog;
import com.dfire.retail.app.manage.common.SelectTimeDialog;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.NoticeVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.NoticeAddBo;
import com.dfire.retail.app.manage.data.bo.ReturnNotMsgBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.google.gson.GsonBuilder;

/**
 * 添加公告 
 */
@SuppressLint("SimpleDateFormat")
public class MessageManageraddActivity extends TitleActivity implements OnClickListener,IItemListListener{
	private ShopVo currentshop;
	private AllShopVo allShopVo;
	private String noticeShopId;
	private SelectDateDialog mDateDialog;
	private String selectDate = null;
	private SelectTimeDialog mTimeDialog;
	private String selectTime= null;
	private ItemEditText message_title_content_edit;
	private SwitchRowItemEditText message_title_content;
	private ItemEditList message_date,message_time,message_shop;
	private String operateType;
	private Button delete;
	private Integer lastVer;
	private NoticeVo noticeVos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_manager_add);
		showBackbtn();
		setTitleText("添加"); 
		findview();
	}
	/**
	 * 初始化控件
	 */
	public void findview() {
		noticeVos = (NoticeVo) getIntent().getSerializableExtra("noticeVo");
		delete = (Button) findViewById(R.id.delete);
		message_shop = (ItemEditList) findViewById(R.id.message_shop);
		message_shop.initLabel("接收门店", "",Boolean.TRUE,this);
		currentshop = RetailApplication.getShopVo();
		if (currentshop.getType() == ShopVo.ZHONGBU || currentshop.getType() == ShopVo.FENGBU) {
			message_shop.initData("所有下属门店", "所有下属门店");
			message_shop.getImg().setImageResource(R.drawable.ico_next);
			message_shop.setIsChangeListener(this.getItemChangeListener());
		}else{
			message_shop.getLblVal().setTextColor(Color.parseColor("#666666"));
			message_shop.getLblVal().setCompoundDrawables(null, null, null, null);
			message_shop.initData(currentshop.getShopName(), currentshop.getShopName());
			message_shop.getImg().setVisibility(View.GONE);
			message_shop.setNotClickable(false);
		}
		message_title_content_edit = (ItemEditText) findViewById(R.id.message_title_content_edit);
		message_title_content_edit.initLabel("消息标题", "", Boolean.TRUE, InputType.TYPE_CLASS_TEXT);
		message_title_content_edit.setMaxLength(50);
		message_title_content_edit.setIsChangeListener(this.getItemChangeListener());
		message_title_content = (SwitchRowItemEditText) findViewById(R.id.message_title_content);
		message_title_content.initLabel("消息内容", "请输入消息内容", Boolean.TRUE, InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		message_title_content.getLblVal().setSingleLine(false);
		message_title_content.setMaxLength(500);
		message_title_content.setIsChangeListener(this.getItemChangeListener());
		message_date = (ItemEditList) findViewById(R.id.message_date);
		message_date.initLabel("发布日期", "",Boolean.TRUE,this);
		message_date.setIsChangeListener(this.getItemChangeListener());
		message_time = (ItemEditList) findViewById(R.id.message_time);
		message_time.initLabel("发布时间", "", this);
		message_time.setIsChangeListener(this.getItemChangeListener());
		mDateDialog = new SelectDateDialog(MessageManageraddActivity.this,false);//日期
		mTimeDialog = new SelectTimeDialog(MessageManageraddActivity.this,false);//时间
		this.mRight.setOnClickListener(this);
		initData();
	}
	/**
	 * 初始化值
	 */
	public void initData() {
		if (noticeVos!=null) {
			setTitleText("公告信息");
			delete.setVisibility(View.VISIBLE);
			delete.setOnClickListener(this);
			operateType = "edit";
			message_shop.initData(noticeVos.getTargetShopName(), noticeVos.getTargetShopName());
			message_title_content_edit.initData(noticeVos.getNoticeTitle());
			message_title_content.initData(noticeVos.getNoticeContent());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			selectDate = sdf.format(new Date(Long.parseLong(noticeVos.getPublishTime()))).substring(0, 10);
			selectTime = sdf.format(new Date(Long.parseLong(noticeVos.getPublishTime()))).subSequence(11, 16).toString();
			message_date.initData(selectDate, selectDate);
			message_time.initData(selectTime, selectTime);
			noticeShopId = noticeVos.gettargetShopId();
			lastVer = noticeVos.getLastVer();
		}else {
			noticeVos = new NoticeVo();
			Date date = new Date();
			noticeShopId = RetailApplication.getShopVo().getShopId();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			message_date.initData(sdf.format(date).substring(0, 10), sdf.format(date).substring(0, 10));
			SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			message_time.initData(sdfs.format(date).subSequence(11, 16)+"",sdfs.format(date).subSequence(11, 16)+"");
			operateType = "add";
		}
		message_date.setOnClickListener(this);
		message_time.setOnClickListener(this);
	}
	/**
	 * 
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right:
			save();
			break;
		case R.id.delete:
			if(Long.valueOf(noticeVos.getPublishTime())>(new Date()).getTime()){
				final AlertDialog alertDialog = new AlertDialog(this);
				alertDialog.setMessage(getResources().getString(R.string.isdelete_msg));
				alertDialog.setCanceledOnTouchOutside(false);
				alertDialog.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
						delNotice();
					}
				});
				alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
			}else{
				new ErrDialog(MessageManageraddActivity.this, getResources().getString(R.string.mc_msg_not_del)).show();
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 获取所有要消息的内容
	 */
	private void save() {
		if (message_date.getStrVal()==null||message_date.getStrVal().equals("")) {
			new ErrDialog(this, getResources().getString(R.string.mc_msg_send_time)).show();
			return;
		}
		if (message_time.getStrVal()==null||message_time.getStrVal().equals("")) {
			new ErrDialog(this, getResources().getString(R.string.mc_msg_send_time)).show();
			return;
		}
		if (message_title_content_edit.getStrVal()==null||message_title_content_edit.getStrVal().equals("")) {
			new ErrDialog(this, getResources().getString(R.string.MC_MSG_000001)).show();
			return;
		}
		if (message_title_content.getStrVal()==null||message_title_content.getStrVal().equals("")) {
			new ErrDialog(this, getResources().getString(R.string.MC_MSG_000002)).show();
			return;
		}
		String dateortime = message_date.getStrVal().toString() + " "+ message_time.getStrVal().toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Long ltime = null;
		try {
			ltime = Long.valueOf(((sdf.parse(dateortime)).getTime()));
			noticeVos.setPublishTime(ltime+"");
		} catch (Exception e) {
			ltime = 0l;
		}
		if (ltime>new Date().getTime()) {
			noticeVos.settargetShopId(noticeShopId);
			noticeVos.setNoticeTitle(message_title_content_edit.getStrVal());
			noticeVos.setNoticeContent(message_title_content.getStrVal());
			commitMessageList();
		} else {
			new ErrDialog(this, getResources().getString(R.string.MC_MSG_000007)).show();
		}
	}
	/**
	 * 保存 
	 */
	private void commitMessageList() {

		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "notice/save");
		params.setParam("operateType", operateType);
		params.setParam("noticeType", 1);
		try {
			params.setParam("notice", new JSONObject(new GsonBuilder().serializeNulls().create().toJson(noticeVos)));
		} catch (JSONException e1) {
			params.setParam("notice", null);
		}
		new AsyncHttpPost(this, params,NoticeAddBo.class,new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				NoticeAddBo bo = (NoticeAddBo)oj;
				if (bo!=null) {
					MessageManageraddActivity.this.finish();
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}
	/**
	 * 删除公告
	 */
	private void delNotice() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "notice/delete");
		params.setParam("noticeType", "del");
		params.setParam("noticeId",noticeVos.getNoticeId());
		params.setParam("lastVer", lastVer);
		new AsyncHttpPost(this, params,ReturnNotMsgBo.class,new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				ReturnNotMsgBo bo = (ReturnNotMsgBo)oj;
				if (bo!=null) {
					MessageManageraddActivity.this.finish();
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
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
				message_shop.changeData(sn, sn);
				noticeShopId = allShopVo.getShopId();
			}
		}
	}
	/**
	 * 弹出日期
	 */
	private void pushDate(){
		mDateDialog.show();
		mDateDialog.getTitle().setText("选择发布日期");
		mDateDialog.updateDays(selectDate);
		mDateDialog.getConfirmButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDateDialog.dismiss();
				selectDate = mDateDialog.getCurrentData();
				message_date.changeData(selectDate, selectDate);
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
				selectTime = mTimeDialog.getCurrentTime();
				message_time.changeData(selectTime, selectTime);
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
	/**
	 * 选择门店
	 */
	private void pushShop(){
		Intent selectIntent =new Intent(MessageManageraddActivity.this,SelectShopActivity.class);
		selectIntent.putExtra("selectShopId", noticeShopId);
		selectIntent.putExtra("activity", "messageManageraddActivity");
		startActivityForResult(selectIntent, 100);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		  if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			  MessageManangeActivity.parent.currentPage=1;
				MessageManangeActivity.parent.noticeVos.clear();
				MessageManangeActivity.parent.getMessageList();
				finish();
            return true;
        }
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onItemListClick(ItemEditList v) {
		int position = Integer.parseInt(String.valueOf(v.getTag()));
		switch (position) {
		case 1:        	
			pushShop();
            break;
        case 2:        	
        	pushDate();
            break;
        case 3:
        	pushTime();
            break;
		}
	}
}
