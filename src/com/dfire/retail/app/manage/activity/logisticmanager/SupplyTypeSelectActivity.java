/************************* 版权声明 **********************************
 * 版权所有：Copyright (c) 2008, 2013 张向华
 *
 * 工程名称：	rest
 * 创建者：	Administrator 创建日期： 2013-8-9
 * 创建记录：	创建类结构。
 *
 * ************************* 变更记录 ********************************
 * 修改者： 
 * 修改日期：
 * 修改记录：
 *
 * 
 * ......************************* To Do List*****************************
 * 
 *
 * Suberversion 信息
 * ID:			$Id$
 * 源代码URL：	$HeadURL$
 * 最后修改者：	$LastChangedBy$
 * 最后修改日期：	$LastChangedDate$
 * 最新版本：		$LastChangedRevision$
 **/

package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

/**
 * 单选项页面.
 * 
 */
public class SupplyTypeSelectActivity extends TitleActivity implements View.OnClickListener{
					
	/**
	 * <code>[listView]</code>.
	 */
	private ListView listView;
	
	private LayoutInflater inflater;
	
	private ImageButton title_right;
	
	private ImageButton addTypeBtn;
	
	private RetailApplication retailApplication;
	
	private String selectValue;
	
	private String selectState;
	
	private ProgressDialog progressDialog;
	
	private ISingleChecksAdapter_New singleChecksAdapter_New;
	
//	List<Map> types;

	private List<Map> types = new ArrayList<Map>();
	
//	/**
//	 * <code>角色集合适配器</code>.
//	 */
//	private ISingleChecksAdapter_New singleChecksAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supply_type_select_view);
		inflater = LayoutInflater.from(this);
		findView();
		setTitleText("类别");
		change2saveFinishMode();
	}
	
	public void findView(){
		types = new ArrayList<Map>();
		title_right = (ImageButton) findViewById(R.id.title_right);
		title_right.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.checkList);
		addTypeBtn = (ImageButton) findViewById(R.id.add_type);
		addTypeBtn.setOnClickListener(this);
		singleChecksAdapter_New = new ISingleChecksAdapter_New(SupplyTypeSelectActivity.this, types);
		listView.setAdapter(singleChecksAdapter_New);
		listView.setOnItemClickListener(new ItemClickListener());
		listView.setDivider(null);
		
		progressDialog = new ProgressDialog(SupplyTypeSelectActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("加载中，请稍后。。。");
		
		this.selectState = getIntent().getStringExtra("selectState").toString();
		if (selectState.equals("SELECT")) {
			this.selectValue = (String) getIntent().getSerializableExtra("selectValue");
		}
		getSupplyType();
	}

	private void getSupplyType() {
		progressDialog.show();
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SUPPLY_INFO_MANAGE_GET_TYPE);
		AsyncHttpPost httppost = new AsyncHttpPost(params, new RequestResultCallback() {
			@Override
			public void onSuccess(String str) {
				progressDialog.dismiss();
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null ||!returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(SupplyTypeSelectActivity.this, "获取失败");
					return;
				}
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(SupplyTypeSelectActivity.this)) {
					return;
				}
				
				types = (List<Map>) ju.get("listMap", new TypeToken<List<Map>>(){}.getType());
				singleChecksAdapter_New.initWithData(types, selectValue);
				singleChecksAdapter_New.notifyDataSetChanged();

			}

			@Override
			public void onFail(Exception e) {
				e.printStackTrace();
			}
		});
		httppost.execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_type:
			Intent supplyTypeAdd =new Intent(SupplyTypeSelectActivity.this,SupplyTypeAddActivity.class);
			startActivity(supplyTypeAdd);
			break;

		default:
			break;
		}
	}
	
	private final class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adpterView, View view, int position,
				long id) {
			ListView listView = (ListView) adpterView;		
			LinkedTreeMap<String, String> type = (LinkedTreeMap<String, String>) listView.getItemAtPosition(position);
			RetailApplication application = (RetailApplication) SupplyTypeSelectActivity.this.getApplication();
			HashMap<String, Object> objMap = application.getObjMap();
			objMap.put("typeVo", type);	
			finish();						
		}
	}
	
}