package com.dfire.retail.app.manage.activity.messagemanage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.logisticmanager.SelectShopActivity;
import com.dfire.retail.app.manage.adapter.MessageListsAdapter;
import com.dfire.retail.app.manage.data.AllShopVo;
import com.dfire.retail.app.manage.data.NoticeVo;
import com.dfire.retail.app.manage.data.ShopVo;
import com.dfire.retail.app.manage.data.bo.NoticeDetailBo;
import com.dfire.retail.app.manage.data.bo.NoticeListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.handmark.pulltorefresh.listview.PullToRefreshBase;
import com.handmark.pulltorefresh.listview.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.listview.PullToRefreshListView;
/**
 * 通知公告
 *
 */
@SuppressLint("SimpleDateFormat")
public class MessageManangeActivity extends TitleActivity implements OnItemClickListener,OnClickListener {
	
	public static MessageManangeActivity parent = null;
	
	public List<NoticeVo> noticeVos;
	
	private ImageView imageadd;
	
	private ShopVo currentShop;
	
	public PullToRefreshListView messageListView;
	
	public int currentPage = 1;
	
	public MessageListsAdapter messageAdapter;
	
	private TextView shopId;
	
	private TextView shopName;
	
	public static String noticeShopId;
	
	private AllShopVo allShopVo;
	
	private Integer pageSize = 0;
	
	private NoticeVo noticeVo;
	
	private Integer editDelFlg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_manager);
		showBackbtn();
		parent=this;
		setTitleText("公告通知");
		init();
		getMessageList();
	}
	public void init(){
		noticeVos = new ArrayList<NoticeVo>();
		currentShop = RetailApplication.getShopVo();
		shopId = (TextView) findViewById(R.id.shopId);
		shopName = (TextView) findViewById(R.id.shopName);
		imageadd = (ImageView) findViewById(R.id.message_add);
		imageadd.setOnClickListener(this);
		setShop();
		messageListView = (PullToRefreshListView) findViewById(R.id.message_remind_setting_lv);
		messageAdapter = new MessageListsAdapter(MessageManangeActivity.this,noticeVos);
		messageListView.setAdapter(messageAdapter);
		messageListView.setOnItemClickListener(this);
		messageListView.setMode(Mode.BOTH);
		messageListView.setRefreshing(false);
		new ListAddFooterItem(this, messageListView.getRefreshableView());
		// 列表刷新和加载更多操作
		messageListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			/**
			 * 下拉重置数据
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(MessageManangeActivity.this, System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = 1;//选择以后初始化页数
				noticeVos.clear();
				messageAdapter.notifyDataSetChanged();
				getMessageList();
			}
			/**
			 * 上拉加载更多
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(MessageManangeActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				currentPage = currentPage + 1;
				getMessageList();
			}
		});
	}
	/**
	 * 显到当门店的消息
	 */
	public void setShop(){
		noticeShopId = RetailApplication.getShopVo().getShopId();
		shopId.setText(noticeShopId);
		if (currentShop.getType() == ShopVo.ZHONGBU || currentShop.getType() == ShopVo.FENGBU) {
			shopName.setText("所有下属门店");
			shopName.setOnClickListener(this);
		}else{
			shopName.setCompoundDrawables(null, null, null, null);
			shopName.setText(currentShop.getShopName());
			shopName.setTextColor(Color.parseColor("#666666"));
		}
	}
	/**
	 * 向消息列表里添加内容
	 */
	public  void getMessageList() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "notice/list");
		params.setParam("currentPage", currentPage);
		params.setParam("noticeType", 1);
		params.setParam("noticeShopId", noticeShopId);
		new AsyncHttpPost(this, params, NoticeListBo.class,false,new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				messageListView.onRefreshComplete();
				NoticeListBo bo = (NoticeListBo)oj;
				if (bo!=null) {
					pageSize = bo.getPageSize();
					List<NoticeVo> noticeVo=new ArrayList<NoticeVo>();
					 noticeVo = bo.getNoticeList();
					 if(noticeVo!=null && noticeVo.size()>0){
						if (currentPage>pageSize){
							 messageListView.setMode(Mode.DISABLED);
						}
						noticeVos.addAll(noticeVo);
					 }
					messageAdapter.notifyDataSetChanged();
				}
			}
			@Override
			public void onFail(Exception e) {
				messageListView.onRefreshComplete();
			}
		}).execute();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		currentPage=1;
		noticeVos.clear();
		messageAdapter.notifyDataSetChanged();
		getMessageList();
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		NoticeVo mNoticeVo = noticeVos.get(position-1);
		getNoticeInfo(mNoticeVo);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shopName:
			Intent selectIntent =new Intent(MessageManangeActivity.this,SelectShopActivity.class);
			selectIntent.putExtra("selectShopId", noticeShopId);
			selectIntent.putExtra("activity", "messageManangeActivity");
			startActivityForResult(selectIntent, 100);
			break;
		case R.id.message_add:
			Intent intent = new Intent(MessageManangeActivity.this,MessageManageraddActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}
	/**
	 * 得到反回的门店的所有消息
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100){
			Bundle b=data.getExtras();
			allShopVo=(AllShopVo)b.getSerializable("shopVo");
			if (allShopVo!=null) {
				String sn=allShopVo.getShopName();
				shopName.setText(sn);
				noticeShopId = allShopVo.getShopId();
				currentPage=1;
				noticeVos.clear();
				messageAdapter.notifyDataSetChanged();
				getMessageList();
			}
		}
	}
	/**
	 * 获取公告详情
	 */
	private void getNoticeInfo(final NoticeVo mNoticeVo) {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "notice/detail");
		params.setParam("noticeId", mNoticeVo.getNoticeId());
		new AsyncHttpPost(this, params,NoticeDetailBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				NoticeDetailBo bo = (NoticeDetailBo)oj;
				if (bo!=null) {
					noticeVo = bo.getNotice();
					editDelFlg = bo.getEditDelFlg();
					long currentTime=new Date().getTime();
					long itemTime=0;
					String dateStr = mNoticeVo.getPublishTime();
					try {
						itemTime = Long.parseLong(dateStr);
					} catch (NumberFormatException e) {
						itemTime = 0l;
					}
					if (currentTime > itemTime) {
						startActivity(new Intent(MessageManangeActivity.this,MessageDetailActivity.class).putExtra("noticeVo",noticeVo));
					} else {
						if (editDelFlg==1) {
							startActivity(new Intent(MessageManangeActivity.this,MessageManageraddActivity.class).putExtra("noticeVo",noticeVo));
						}else {
							startActivity(new Intent(MessageManangeActivity.this,MessageDetailActivity.class).putExtra("noticeVo",noticeVo));
						}
					}
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}
}
